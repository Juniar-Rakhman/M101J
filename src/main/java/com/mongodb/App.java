package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class App {
    public static void main(String[] args) {

        final Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(App.class, "/");

        MongoClient client = new MongoClient();

        MongoDatabase database = client.getDatabase("students");
        MongoCollection<Document> collection = database.getCollection("grades");

//        myDoc = collection.find(exists("i")).sort(descending("i")).first();
//        System.out.println(myDoc.toJson());

        List<Document> all = collection.find(eq("type", "homework")).into(new ArrayList<Document>());
        int prevStudent = -1;
        for (Document document : all) {
            if (prevStudent != document.getInteger("student_id")) {
                prevStudent = document.getInteger("student_id");
            } else {
                List<Document> scores = collection.find(and(eq("student_id", document.getInteger("student_id")), eq("type", "homework"))).into(new ArrayList<Document>());
            }
        }

    }
}
