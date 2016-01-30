package com.adam.learning;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Created by alaplante on 1/28/16.
 */
public class MongoDriver {
    public static void main(String[] args) {
        MongoDriver mongoDriver = new MongoDriver();
        mongoDriver.run();
    }

    public void run() {
        MongoClientURI mongoClientURI = new MongoClientURI("mongodb://localhost:27017");
        MongoClient mongoClient = new MongoClient(mongoClientURI);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");

//        Document document = new Document("name", "MongoDB")
//                .append("type", "database")
//                .append("count", 1)
//                .append("info", new Document("x", 203).append("y", 102));

        Document document = new Document("_class", "hello.Customer")
                .append("firstName", "Adam").append("lastName", "LaPlante");

        MongoCollection<Document> collection = mongoDatabase.getCollection("customer");

        collection.insertOne(document);

        MongoCursor<Document> mongoCursor = collection.find().iterator();
        try {
            while(mongoCursor.hasNext()) {
                p(mongoCursor.next().toJson());
            }
        } finally {
            mongoCursor.close();
        }

        p("MongoDriver done");
    }

    private void p(String s) {
        System.out.print(s);
    }
}
