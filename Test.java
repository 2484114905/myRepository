package code;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Test {
    public static void main(String[] args){
        try {
            MongoClient client = new MongoClient("localhost", 27017);
            DB db = client.getDB("test");
            System.out.println("Connect to databases successfully");
            DBCollection collection = db.getCollection("demo1");
            BasicDBObject dbObject = new BasicDBObject("title", "mongodb");
            dbObject.append("database", "test");
            dbObject.append("user", "hadoop");
            dbObject.append("data","2020/3/18");
            collection.insert(dbObject);
            System.out.println("insert successfully");
        }
        catch (Exception e){
            System.err.println(e.getClass().getName() + " : " + e.getMessage() + "!error");
        }

        System.out.println("show collections");
        find();
    }

    public static void find(){
        try{
            MongoCollection<Document> collection = getCollection("test","demo1");  //数据库名,集合名

            MongoCursor<Document> cursor= collection.find().iterator();
            while(cursor.hasNext()){
                System.out.println(cursor.next().toJson());
            }
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public static MongoCollection<Document> getCollection(String dbname,String collectionname){
        //实例化一个mongo客户端,服务器地址：localhost(本地)，端口号：27017
        MongoClient  mongoClient=new MongoClient("localhost",27017);
        //实例化一个mongo数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbname);
        //获取数据库中某个集合
        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionname);
        return collection;
    }
}
