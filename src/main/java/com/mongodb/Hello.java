package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.bson.Document;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;


public class Hello {
    public static void main(String[] args) {

        final Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(App.class, "/");

        MongoClient client = new MongoClient();

        MongoDatabase database = client.getDatabase("test");
        final MongoCollection<Document> collection = database.getCollection("hello");

        collection.dropCollection();

        collection.insertOne(new Document("name", "MongoDB"));

        Spark.get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) {
                StringWriter writer = new StringWriter();
                try {
                    Template helloTemplate = configuration.getTemplate("hello.ftl");

                    Document document = collection.find().first();

                    helloTemplate.process(document, writer);

                } catch (Exception e) {
                    halt(500);
                    e.printStackTrace();
                }

                return writer;
            }
        });
    }
}
