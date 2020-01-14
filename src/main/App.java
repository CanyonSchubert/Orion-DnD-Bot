package main;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.*;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.*;

public class App {
	
	public static void main(String[] args) throws Exception {
		
		/*
		 * This block is simply retrieving the bot token and prefix from auth.json
		 * 
		 */
		String token = "";
		String prefix = "";

		JSONParser json = new JSONParser();
		try {
			JSONObject res = (JSONObject) json.parse(new FileReader("./src/main/auth.json"));
			
			token = (String) res.get("token");
			prefix = (String) res.get("prefix");
		} catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } catch (ParseException e) { e.printStackTrace(); }
		
		/*
		 * Initializes Bot
		 * 
		 */
		JDA jda = new JDABuilder(AccountType.BOT)
				.setToken(token)
				.addEventListeners(new MessageListener())
				.build().awaitReady();
		System.out.println("Ready!");
	}
}