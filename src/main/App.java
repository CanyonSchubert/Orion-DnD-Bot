package main;

import net.dv8tion.jda.api.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.*;

public class App {
	
	public static void main(String[] args) throws Exception {
		
		/*
		 * This block is simply retrieving the bot token and prefix from auth.json
		 * 
		 */
		String tokenAuth = "";
		JSONParser json = new JSONParser();
		try {
			JSONObject res = (JSONObject) json.parse(new FileReader("./src/main/auth.json"));
			
			tokenAuth = (String) res.get("token");
		} catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } catch (ParseException e) { e.printStackTrace(); }
		final String token = tokenAuth;
		
		/*
		 * Initializes Bot
		 * 
		 */
		@SuppressWarnings("unused")
		JDA jda = new JDABuilder(AccountType.BOT)
				.setToken(token)
				.addEventListeners(new MessageListener())
				.build()
				.awaitReady();
		System.out.println("Ready!");
	}
}