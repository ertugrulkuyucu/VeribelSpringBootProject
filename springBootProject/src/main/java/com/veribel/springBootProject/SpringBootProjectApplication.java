package com.veribel.springBootProject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import com.mongodb.client.MongoDatabase;
import org.json.JSONObject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class SpringBootProjectApplication implements CommandLineRunner {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		csvToMongoDb();
		System.out.println("Please wait 10 secont for data preparation...");
		TimeUnit.SECONDS.sleep(10);
		mongoDbToJson();
	}
		
	public static void csvToMongoDb() {
		  String Host = "localhost"; String Port = "27017"; String DB = "Veribel";
		  String CollectionName = "xrtest"; String FileName = "C:\\Users\\Ertugrul\\Desktop\\eclipse_projects\\so_far_course\\springBootProject\\src\\main\\resources\\xrtest_1500120_1_1641547130799.csv";
		  
		  // Without Credential 
		  String command = "C:\\Program Files\\MongoDB\\Tools\\100\\bin\\mongoimport.exe --host " + Host + " --port " + Port + " --db " + DB + " --collection " + CollectionName + "  --headerline --type=csv --file "+ FileName;
		 
		  try { Process process = Runtime.getRuntime().exec(command);
		  System.out.println("Imported csv into Database"); 
		  } 
		  catch (Exception e) {
		  System.out.println("Error executing " + command + e.toString());
		  }	
	}

	public static void mongoDbToJson() throws IOException{
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);

		try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
			MongoDatabase database = mongoClient.getDatabase("Veribel");

			JSONObject json = new JSONObject();
			// Creating a collection object
			MongoCollection<org.bson.Document> collection = database.getCollection("xrtest");
			// Retrieving the documents
			FindIterable<org.bson.Document> iterDoc = collection.find();
			Iterator it = iterDoc.iterator();
			int temp = 1;
			while (it.hasNext()) {
				json.put(Integer.toString(temp), it.next());
				temp++;
			}
		
			FileWriter file = new FileWriter("C:\\Users\\Ertugrul\\Desktop\\json\\Veribel.json");
			file.write(json.toString());
			System.out.println("Create Json file is successfully.");
			file.close();
		}		
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
