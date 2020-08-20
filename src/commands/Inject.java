package commands;

import java.util.List;

import org.json.simple.JSONObject;

import main.App;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import resources.Authenticator;
import resources.Database;

public class Inject {

	@SuppressWarnings("unchecked")
	public static void run(MessageReceivedEvent event, List<String> args) {
		System.out.println("\nEntered Inject Command!");
		
		if (event.getAuthor().isBot()) return;
		
		/*
		 * Grabs the prefix from auth.json.
		 * Gets message and channel for ease of access.
		 * Grabs the user database.
		 */
		Authenticator auth = new Authenticator();
		//final String prefix = auth.getPrefix();
		final List<String> devs = auth.getDevs();

		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();
		
		if (!devs.contains(message.getAuthor().getId())) {
			channel.sendMessage("<@" + message.getAuthor().getId() + ">, you don't have permission to use that command!").queue();
			return;
		}
		
		Database userDB;
		if (!App.DEV_MODE) userDB = new Database("users");
		else userDB = new Database("sampledb");
		JSONObject users = userDB.getDatabase();
		Database classDB = new Database("classes");
		JSONObject classes = classDB.getDatabase();
		
		/*
		 * Injections per user happen here!
		 */
		for (Object userObj : users.keySet()) {
			JSONObject user = (JSONObject) users.get(((String) userObj));
			JSONObject selected = (JSONObject) user.get("selected");
			
			/*
			 * Injections per character happen here!
			 */
			JSONObject characters = (JSONObject) user.get("characters");
			for (Object characterObj : characters.values()) {
				JSONObject character = (JSONObject) characterObj;
				
				/*
				 * If a character does not have base stats, inject them from the class database!
				 */
				if (((JSONObject) character.get("stats")).equals(new JSONObject())) {
					character.put("stats", ((JSONObject) classes.get(((String) character.get("class")).toLowerCase())).get("basestats"));
					characters.put(((String) character.get("name")), character);
					user.put("characters", characters);
					if (((String) character.get("name")).equals(selected.get("name"))) {
						selected.put("stats", ((JSONObject) classes.get(((String) character.get("class")).toLowerCase())).get("basestats"));
						user.put("selected", selected);
					}
					users.put(((String) userObj), user); 
					System.out.println(((String) user.get("username")) + "'s " + ((String) character.get("name")) + "'s base stats have been injected!");
				}
				
				/*
				 * If a character does not have gear slots, inject them here!
				 */
				if (((JSONObject) character.get("gear")).equals(new JSONObject())) {
					JSONObject gear = (JSONObject) character.get("gear");
					gear.put("head", "");
					gear.put("chest", "");
					gear.put("legs", "");
					gear.put("trinket1", "");
					gear.put("trinket2", "");
					gear.put("mainhand", "");
					gear.put("offhand", "");
					character.put("gear", gear);
					characters.put(((String) character.get("name")), character);
					user.put("selected", selected);
					if (((String) character.get("name")).equals(selected.get("name"))) {
						gear = (JSONObject) selected.get("gear");
						gear.put("head", "");
						gear.put("chest", "");
						gear.put("legs", "");
						gear.put("trinket1", "");
						gear.put("trinket2", "");
						gear.put("mainhand", "");
						gear.put("offhand", "");
						selected.put("gear", gear);
						user.put("selected", selected);
					}
					users.put(((String) userObj), user);
					System.out.println(((String) user.get("username")) + "'s " + ((String) character.get("name")) + "'s gear slots have been injected!");
				}
			}
		}
		userDB.saveDatabase(users);
		channel.sendMessage("Injections completed!").queue();
	}
}
