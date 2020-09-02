import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

import java.util.Date;
import java.util.LinkedList;

public class Main {

    static String array_names[] = {"John", "Tim", "Brit", "Robin", "Smith", "Lora", "Jennifer", "Lyla", "Victor", "Adam"};
    static String array_address[][] ={{"US", "FL", " Miami"}, {"US", "FL", " Orlando"}, {"US", "CA", "San Diego"}, {"US", "FL", " Orlando"}, {"US", "FL", " Orlando"},
            {"US", "NY", "New York"}, {"US", "NY", "Buffalo"}, {"US", "TX", " Houston"}, {"US", "CA", "San Diego"}, {"US", "TX", " Houston"}};

    // This Helper Method Is Used To Build The Random Friend's Data
    private static String[] pickFriends() {
        int numberOfFriends = (int) (Math.random() * 10);
        LinkedList<String> friends = new LinkedList<String>();
        int random = 0;
        while(friends.size() < numberOfFriends) {
            random = (int) (Math.random()*10);
            if(!friends.contains(array_names[random])) {
                friends.add(array_names[random]);
            }
        }
        String arr[] = {};
        return  friends.toArray(arr);
    }

    // This Helper Method Is Used To Build The Random Address
    private static String[] pickAddress() {
        int random = (int) (Math.random() * 10);
        return array_address[random];
    }

    public static void main(String[] args) {
        // Connecting To The MongoDb Server Listening On A Default Port (i.e. 27017).
        MongoClient mongoClntObj = new MongoClient("localhost", 27017);

        // Get MongoDb Database. If The Database Doesn't Exists, MongoDb Will Automatically Create It For You
        DB dbObj = mongoClntObj.getDB("mongodbdemo1");

        // Get MongoDb Collection. If The Collection Doesn't Exists, MongoDb Will Automatically Create It For You
        DBCollection collectionObj = dbObj.getCollection("jcg");





    }

    private static void insertDocuments(DBCollection collection){
        /**** INSERT OPERATION ****/
        // Creating The MongoDb Documents To Store Key-Value Pair
        BasicDBObject documentObj;
        String address[];
        for(int i = 0 ; i < array_names.length ; i++) {
            documentObj = new BasicDBObject();
            documentObj.append("name", array_names[i]);
            documentObj.append("age", (int)(Math.random() * 60));
            documentObj.append("joined_date", new Date());
            documentObj.append("friends", pickFriends());
            address = pickAddress();
            documentObj.append("address", new BasicDBObject("country",address[0]).append("state", address[1]).append("city", address[2]));
            collection.insert(documentObj);
        }

    }


    private static void readAll(DBCollection dbCollection){
        DBCursor cursorObj = dbCollection.find();
        try {
            while(cursorObj.hasNext()) {
                System.out.println(cursorObj.next());
            }
        } finally {
            cursorObj.close();
        }
    }

    private static void getObject(DBCollection dbCollection){
        BasicDBObject selectQuery = new BasicDBObject("age", new BasicDBObject("$gt", 40));
       DBCursor  cursorObj = dbCollection.find(selectQuery);
        System.out.println("\nPersons With Age Greater Than 40 Years?= "+ cursorObj.count());

        /**** UPDATE OPERATION ****/
        // Update Documents Found By The Query i.e. Update The Documents Having 'Age > 40'
        BasicDBObject ageDocument = new BasicDBObject();
        ageDocument.put("age", 20);

        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", ageDocument);

        dbCollection.update(selectQuery, updateObj, false, true);

        // Find & Display
        cursorObj = dbCollection.find(selectQuery);
        System.out.println("Persons With Age > 40 After Update?= "+ cursorObj.count());
    }
}


