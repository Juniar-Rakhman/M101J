package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {

        final Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(App.class, "/");

        MongoClient client = new MongoClient();

        MongoDatabase database = client.getDatabase("students");
        MongoCollection<Document> collection = database.getCollection("grades");

        Bson filter = new Document("type", "homework");
        Bson sort = new Document("student_id", 1).append("score", 1);

        List<Document> all = collection
                .find(filter)
                .sort(sort)
                .into(new ArrayList<Document>());

        int prevStudent = -1;

        for (Document document : all) {
            if (prevStudent != document.getInteger("student_id")) {
                System.out.println(document + "deleted");
                prevStudent = document.getInteger("student_id");
                collection.deleteOne(new Document("student_id", document.getInteger("student_id"))
                        .append("score", document.getDouble("score")));
            } else {
                System.out.println(document);
            }
        }

    }
}
