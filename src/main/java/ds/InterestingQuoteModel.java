//Name: Dhruvi Ajay Khankhoje, andrewid: dkhankho, email: dkhankho@andrew.cmu.edu, Project 4 Task 2

package ds;

/*
 * @author Dhruvi Ajay Khankhoje
 * 
 * This file is the Model component of the MVC, and it models the business
 * logic for the web application.  In this case, the business logic involves
 * making a request to quotable API and fetching relevant information.
 */
//import javax.servlet.annotation.WebServlet;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
//import org.apache.log4j.Level;
import org.bson.Document;
import org.slf4j.LoggerFactory;

public class InterestingQuoteModel {

    /**
     * Arguments.
     *
     * @param searchTag The tag of the author to be searched for.
     */

    //This method calls the third-party API and gets the required information from the user
    public static String printJsonData(String searchTag) {



        String inputtime = String.valueOf(new Date());
        String authorSearch = searchTag.replace(" ", "-");


        String url = "https://api.quotable.io/authors?slug=" + authorSearch;

        String response = fetchContent(url);
        String name = "";
        String link = "";
        String bio = "";
        String content = "";
        String numberofQuotes = "0";
        String lenOfQuote = "0";

        //handling API data being unavailable
        if(response.toLowerCase().equals("error"))
        {
            name = "API Unavailable";
            link = "API Unavailable";
            bio = "API Unavailable";
            content = "API Unavailable";
            numberofQuotes = "0";
            lenOfQuote = "0";
        }
        //if API is available but response does not have a correct response

        else if(response.contains("\"results\": []")) {

            name = "API Unavailable";
            link = "API Unavailable";
            bio = "API Unavailable";
            content = "API Unavailable";
            numberofQuotes = "0";
            lenOfQuote = "0";

        }

        //if API is available and response does have a correct response
        else {

            //parsing the response and removing required elements from results
            //Citation: https://howtodoinjava.com/gson/gson-jsonparser/
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(response);
            JsonObject jsonObject = jsonElement.getAsJsonObject();


            JsonArray resultsArray = jsonObject.getAsJsonArray("results");
            for (JsonElement element : resultsArray) {
                JsonObject jsonObject1 = element.getAsJsonObject();
                name = jsonObject1.get("name").getAsString();
                link = jsonObject1.get("link").getAsString();
                bio = jsonObject1.get("bio").getAsString();
            }

            //if the author is not available in API
            if (name.isEmpty())
            {
                name = "Author Unavailable";
                link = "Author Unavailable";
                bio = "Author Unavailable";
                content = "Author Unavailable";
            }

            //if the author is available in API
            else
            {
                String author = name.replace(" ", "-");

                String url2 = "https://api.quotable.io/quotes?author=" + author.toLowerCase();
                String response2 = fetchContent(url2);
                JsonElement jsonElement2 = parser.parse(response2);
                JsonObject jsonObject2 = jsonElement2.getAsJsonObject();
                JsonArray resultsArray2 = jsonObject2.getAsJsonArray("results");

                //choosing a random quote from the list of quotes
                int randomNumber = (int) (Math.random() * (resultsArray2.size() - 0)) + 0;

                JsonElement element2 = resultsArray2.get(randomNumber);
                JsonObject jsonObject3 = element2.getAsJsonObject();
                content = jsonObject3.get("content").getAsString();
                numberofQuotes = String.valueOf(resultsArray2.size());
            }
        }




        //creating an object of result to store in the database
        Result result = new Result(name, bio, link, content);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String quoteInfo = gson.toJson(result);
        lenOfQuote = String.valueOf(content.length());
        String outputtime = String.valueOf(new Date());

        //calling the function to store logs and inputs with all information
        mongoDB(name, bio, link, content, inputtime, outputtime, numberofQuotes,lenOfQuote );

        return quoteInfo;

    }


    /**
     * @param urlvalue The url of the website from where data is to be fetched
     */

    //Citation: https://github.com/CMU-Heinz-95702/Lab2-InterestingPicture/blob/master/InterestingPictureModel.java
    //this method creates HTTP Connection and fetches data from it
    private static String fetchContent(String urlvalue) {
        String contentfetched = "";
        try {
            URL url = new URL(urlvalue);
            //creating http connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //Citation: http://www.java2s.com/example/java-api/java/net/httpurlconnection/getresponsecode-0-15.html

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            //To handle the error where Third-party API unavailable
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // reading all data returned by the server
                BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String data;
                //reading each line until there is no content left and adding it to "contentfetched"
                while ((data = input.readLine()) != null) {
                    contentfetched += data;
                }

                input.close();
            }
            else {
                contentfetched = "Error";
            }
        } catch (IOException e) {
            System.out.println("An Exception Has Occured");

        }
        System.out.println("--" + contentfetched);
        return contentfetched;
    }


    /**
     * Arguments.
     *
     * @param name Name of the author
     * @param bio Bio of the author
     * @param link link of the author
     * @param content Quote by author
     * @param inputtime Time when 3rd party API was called
     * @param outputtime Time when response was sent
     * @param lenOfQuote length of a quote
     * @param numberofQuotes number of quotes for an author
     */

    //This method connects to mongoDB collections, calls a function to add input
    public static void mongoDB(String name, String bio, String link, String content, String inputtime, String outputtime, String numberofQuotes, String lenOfQuote) {

        //Citation: https://www.mongodb.com/docs/drivers/java/sync/v4.3/quick-start/


        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.OFF);

        //Connect MongoDB
        ConnectionString connectionString = new ConnectionString("mongodb+srv://dhruvikhankhoje22:distributed2210@cluster0.afm6jhp.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("test");


        addInput(name, bio, link, content, inputtime, outputtime, numberofQuotes, lenOfQuote);

//        MongoCollection<Document> collection = database.getCollection("myCollection");
//        MongoCollection<Document> collection_logs = database.getCollection("mylogs");


//        System.out.println("Document inserted successfully");
//        FindIterable<Document> documents = collection.find();
//
//
//
//        for (Document document : documents) {
//            String name1 = document.getString("Name");
//            String bio1 = document.getString("Bio");
//            String link1 = document.getString("Link");
//            String content1 = document.getString("Content");
//
//            FindIterable<Document> document_logs = collection_logs.find();
        }



    /**
     * @param name: The name of the author
     * @param bio: The biography of the author
     * @param link: The link of the wikipedia page of the author
     * @param content: The quote of the author
     * @param inputtime: The time the search started
     * @param outputtime: The time the result was returned
     * This method adds the data into mongoDb
     */

    //this method adds input to te database
    private static void addInput(String name, String bio, String link, String content, String inputtime, String outputtime, String numberofQuotes, String lenOfQuote) {

        String uri = "mongodb+srv://dhruvikhankhoje22:distributed2210@cluster0.afm6jhp.mongodb.net/?retryWrites=true&w=majority";


        try (MongoClient mongoClient = MongoClients.create(uri)) {

            MongoDatabase database = mongoClient.getDatabase("test");

            MongoCollection<Document> collection = database.getCollection("mylogs");

            database.getCollection("myCollection")
                    .insertOne(new Document("Name", name)
                            .append("Bio", bio).append("Link", link).append("Content", content));


            database.getCollection("mylogs")
                    .insertOne(new Document("User_Input", name)
                            .append("Input_Time", inputtime).append("Output_Time", outputtime).append("Number",numberofQuotes).append("Quote_Sent", content).append("Length", lenOfQuote));


        }
    }




    //This method fetches records from mongoDB
    public static List<LogDashboard> logRecords()
    {
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.OFF);

        ConnectionString connectionString = new ConnectionString("mongodb+srv://dhruvikhankhoje22:distributed2210@cluster0.afm6jhp.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        MongoClient mongoClient = MongoClients.create(settings);

        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection_logs = database.getCollection("mylogs");

        List<LogDashboard> logs = new ArrayList<>();
        //fetching each record and storing it in a list of type LogDashboard
        FindIterable<Document> document_logs = collection_logs.find();
        for (Document document1 : document_logs) {

            String ip = document1.getString("User_Input");
            String inputtime1 = document1.getString("Input_Time");
            String outputtime1 = document1.getString("Output_Time");
            String number = document1.getString("Number");
            String quotesent1 = document1.getString("Quote_Sent");
            String length = document1.getString("Length");
            LogDashboard logDashboard = new LogDashboard(ip, inputtime1, outputtime1, number, quotesent1, length);
            logs.add(logDashboard);
        }

        return logs;
    }


}

