package servlets;

import com.mongodb.Cursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by tayvs on 05.12.2016.
 */
public class DB {

    final static String DB_TABLE_NAME = "test";
    final static String DB_NAME = "test";

    public static boolean writeIntoDB(List<Entry> list) {
        MongoClient client = new MongoClient();
        MongoDatabase mongoDB = client.getDatabase(DB_NAME);

        for (Entry entry : list){
            Document d = new Document();

            d.append("tag", entry.getTag());
            d.append("value", entry.getValue());

            mongoDB.getCollection(DB_TABLE_NAME).insertOne(d);
        }

        return true;
    }

    public static List<Entry> getEntrys(){
        MongoClient client = new MongoClient();
        MongoDatabase mongoDB = client.getDatabase(DB_NAME);

        FindIterable<Document> iter = mongoDB.getCollection(DB_TABLE_NAME).find();
        LinkedList<Entry> list = new LinkedList<>();

        for (Document d : iter) {
            list.add(new Entry(d.getString("tag"), d.getString("value")));
        }

        return list;
    }

}
