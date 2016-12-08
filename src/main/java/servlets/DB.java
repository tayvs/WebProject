package servlets;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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

    public String getEntrysJSON(String[] collNames) {
        Document doc = new Document();

        for (String collName : collNames) {
            MongoCollection<Document> collection = mongoDB.getCollection(collName);
            doc.put(collName, collection.toString());
        }

        return doc.toString();
    }
}
