package commands;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Register {

	public static void run(MessageReceivedEvent event, List<String> args) {
		System.out.println("Entered Register Command!");
		
		if (event.getAuthor().isBot()) return;
		
		/*
		 * Grabs the prefix from auth.json.
		 * 
		 */
		String prefixAuth = "";
		JSONParser json = new JSONParser();
		try {
			JSONObject res = (JSONObject) json.parse(new FileReader("./src/main/auth.json"));
			
			prefixAuth = (String) res.get("prefix");
		} catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } catch (ParseException e) { e.printStackTrace(); }
		final String prefix = prefixAuth;
		
		/*
		 * Grabs the database from database.json.
		 * 
		 */
		JSONObject db = new JSONObject();
		JSONObject regUsers = new JSONObject();
		try {
			db = (JSONObject) json.parse(new FileReader("./database/json/database.json"));
			regUsers = (JSONObject) db.get("users");		
		} catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } catch (ParseException e) { e.printStackTrace(); }
		
		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();
		
		if (regUsers.containsKey(message.getAuthor().getId())) {
			channel.sendMessage("Your account is already registered. Try **" + prefix + "menu** to get started.").queue();
			return;
		}
		
		regUsers.put(message.getAuthor().getId(), new JSONObject());	
		((JSONObject) regUsers.get(message.getAuthor().getId())).put("username", message.getAuthor().getAsTag());
		
		db.put("users", regUsers);
		
		try (FileWriter file = new FileWriter("./database/json/database.json")) {
			file.write(db.toString());
			System.out.println("Successfully saved the database!");
		} catch (IOException e) { e.printStackTrace(); }
		message.getAuthor().openPrivateChannel().queue(dm -> dm.sendMessage("Your Discord account has been successfully registered! Your next step should be using **" + 
				prefix + "menu** and following the steps to create your first character!").queue());
	}
}
