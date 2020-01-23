package resources;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Authenticator {
	String token;
	String prefix;
	
	public Authenticator() {
		JSONParser json = new JSONParser();
		String path = "./src/main/auth.json";
		try {
			JSONObject res = (JSONObject) json.parse(new FileReader(path));
			
			this.token = (String) res.get("token");
			this.prefix = (String) res.get("prefix");
		} catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } catch (ParseException e) { e.printStackTrace(); }
	}
	
	public String getToken() {
		return token;
	}
	
	public String getPrefix() {
		return prefix;
	}
}
