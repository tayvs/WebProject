package servlets;

import com.mongodb.Cursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tayvs on 05.12.2016.
 */
public class DB {

//    final static String DB_TABLE_NAME = "test";
    final static String DB_NAME = "test";
    final static LinkedList<String> collectionNames = new LinkedList<>();

    public static boolean writeCollection(String collectionName, List<Entry> list) {
        //Prepare db to work with it
        MongoClient client = new MongoClient();
        MongoDatabase mongoDB = client.getDatabase(DB_NAME);

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

    public static List<Entry> getEntrys(String collName){
        MongoClient client = new MongoClient();
        MongoDatabase mongoDB = client.getDatabase(DB_NAME);

        FindIterable<Document> iter = mongoDB.getCollection(collName).find();
        LinkedList<Entry> list = new LinkedList<>();

        for (Document d : iter) {
            list.add(new Entry(d.getString("tag"), d.getString("value")));
        }

        return list;
    }

    public static HashMap getEntrys() {
        HashMap<String, List<Entry>> urlPairs = new HashMap<>();

        for (String collName : collectionNames) {
            urlPairs.put(collName, getEntrys(collName));
        }

        return urlPairs;
    }
}
