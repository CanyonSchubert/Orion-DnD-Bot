package resources;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Database {
	JSONObject db = new JSONObject();
	String dbType;
	
	public Database(String dbType) {
		JSONParser json = new JSONParser();
		this.dbType = dbType.toLowerCase();
		
		try {
			this.db = (JSONObject) json.parse(new FileReader("./database/json/" + dbType.toLowerCase() + ".json"));
		} catch (FileNotFoundException e) { 
			System.out.println(dbType + " Database File not found, creating one now!");			
			saveDatabase(this.db);
		} catch (IOException e) { e.printStackTrace(); } catch (ParseException e) { e.printStackTrace(); }
	}
	
	public JSONObject getDatabase() {
		System.out.println("Successfully retrieved the " + this.dbType + " database!\n");
		return this.db;
	}
	
	public void saveDatabase(JSONObject db) {
		try (FileWriter file = new FileWriter("./database/json/" + this.dbType + ".json")) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String dbToSave = gson.toJson(db);
			file.write(dbToSave);
			System.out.println("Successfully saved the " + this.dbType + " database!\n");
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public String getDBType() {
		return this.dbType;
	}
}
