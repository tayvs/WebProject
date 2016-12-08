package servlets;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;

/**
 * Created by tayvs on 05.12.2016.
 */
public class DB {

    final String DB_NAME = "test";
    final LinkedList<String> collectionNames = new LinkedList<>();
    final MongoClient client = new MongoClient();
    final MongoDatabase mongoDB = client.getDatabase(DB_NAME);

    public boolean writeCollection(String collectionName, List<Entry> list) {
        //Add new collection name (coll name equals URL)
        collectionNames.add(collectionName);

        for (Entry entry : list){
            Document d = new Document();

            d.append("tag", entry.getTag());
            d.append("value", entry.getValue());

            mongoDB.getCollection(collectionName).insertOne(d);
        }

        return true;
    }

    public List<Entry> getEntrys(String collName){
        FindIterable<Document> iter = mongoDB.getCollection(collName).find();
        LinkedList<Entry> list = new LinkedList<>();

        for (Document d : iter) {
            list.add(new Entry(d.getString("tag"), d.getString("value")));
        }

        return list;
    }

    public HashMap getAlllEntrys() {
        HashMap<String, List<Entry>> urlPairs = new HashMap<>();

        for (String collName : collectionNames) {
            urlPairs.put(collName, getEntrys(collName));
        }

        return urlPairs;
    }


    public String getEntrysJSON() {
        //<URL, List<Entry(Tag, Attribute)>>
        HashMap<String, List<Entry>> urlPairs = getAlllEntrys();
        Document returnJSON = new Document();

        //URL index
        Integer urlIndex = 1;
        for (Map.Entry<String, List<Entry>> urlEntryList : urlPairs.entrySet()) {
            //get entryList
            LinkedList<Entry> entryList = (LinkedList<Entry>) urlEntryList.getValue();
            urlEntryList.getValue().sort(Comparator.comparing(Entry::getTag));


            Document tagAttrJSON = new Document();
            //<tag, list<attributes>>
            HashMap<String, List<String>> entryMap = tagValueToMap(entryList);
            entryMap.entrySet().forEach((mEntry) -> tagAttrJSON.put(mEntry.getKey(), Arrays.toString(mEntry.getValue().toArray())));


            Document urlJSON = new Document();
            urlJSON.put("url", urlEntryList.getKey());
            urlJSON.put("pairs", tagAttrJSON.toJson());
            returnJSON.put((urlIndex++).toString(), urlJSON);
        }

        return returnJSON.toJson();
    }

    private HashMap tagValueToMap(List<Entry> list) {
        //<Tag, List<Attributes>>
        HashMap<String, List<String>> tagValueMap = new HashMap<>();

        list.forEach((e) -> {
            List l = tagValueMap.get(e.getTag());
            if (l == null) {
                l = new LinkedList<String>();
            }
            l.add(e.getValue());
            tagValueMap.put(e.getTag(), l);
        });

        return tagValueMap;
    }
}
