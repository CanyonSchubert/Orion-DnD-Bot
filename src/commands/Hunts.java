package commands;

import java.awt.Color;
import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import main.App;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import resources.Authenticator;
import resources.Database;

public class Hunts {
	
	public static void run(MessageReceivedEvent event, List<String> args) {
		System.out.println("\nEntered Hunts Command!");
		
		if (event.getAuthor().isBot()) return;
		
		/*
		 * Grabs the prefix from auth.json.
		 * Gets the message and channels for ease of access.
		 * Grabs the User Database from users.json.
		 */
		Authenticator auth = new Authenticator();
		final String prefix = auth.getPrefix();

		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();

		Database userDB;
		if (!App.DEV_MODE) userDB = new Database("users");
		else userDB = new Database("sampledb");
		JSONObject users = userDB.getDatabase();
		JSONObject user = (JSONObject) users.get(message.getAuthor().getId());
		JSONObject selected = (JSONObject) user.get("selected");
		
		/*
		 * Grabs the class database.
		 */
		Database huntsDB = new Database("hunts");
		JSONObject hunts = huntsDB.getDatabase();
		
		if (args.size() == 0) {
		
			/*
			 * Sets up the footer with the selected character (or "No Character") and color preference (or 0x1330c2).
			 * Populates the embed.
			 * Sends the menu to the channel it was requested from.
			 */
			String footer;
			if (!(selected.get("name") == null)) 
				footer = selected.get("name") + " (Lv. " + selected.get("level") + ") - " + selected.get("class");
			else footer = "No Character";
			
			Color prefColor;
			if (!(selected.get("color") == null))
				prefColor = new Color(Integer.decode((String) selected.get("color")));
			else prefColor = new Color(0x1330c2);
	
			File file = new File("./assets/placeholders/placeholder-icon.png"); // TODO: Update to official icon
			EmbedBuilder embed = new EmbedBuilder()
					.setTitle("Hunts Menu") // TODO: If user is new, suggest tutorial?
					.setColor(prefColor)
					.setDescription("Please re-enter this command with a number as an argument. Every available hunt is listed below with it's corresponding number. (ex. **" + prefix + "hunts 1** for Cedarwood Cave)")
					.setAuthor("Orion", null, event.getJDA().getSelfUser().getAvatarUrl()) // TODO: Update Discord avatar to official logo, Change null to official webpage
					.setFooter(footer)
					.setThumbnail("attachment://placeholder-icon.png") // TODO: See line "File file..."
					.setTimestamp(Instant.now())
					;
			
			/*
			 * Field Generation for embed
			 */
			int huntNum = 1;
			JSONObject currHunt = new JSONObject();
			String title = "";
			String field = "";
			for (Object nextHunt : hunts.values()) {
				currHunt = (JSONObject) nextHunt;
				
				/*
				 * title for each field
				 */
				title = huntNum + ". **" + currHunt.get("name") + "** (Difficulty: " + currHunt.get("difficulty") + " | Suggested Party Level: " + currHunt.get("suggested_level") + ")";
				
				/*
				 * body for each field
				 */ // TODO: for enemy pool, use the key found here to go into the enemy database and get the actual name
				field = currHunt.get("description") + "\nPossible Enemies: " + currHunt.get("enemy_pool").toString() + "\nAmount of Enemies: " + currHunt.get("min_enemies") + "-" + currHunt.get("max_enemies");
				
				embed.addField(title, field, true);
				
				/*
				 * next class
				 */
				++huntNum;
			}
			
			channel.sendMessage(embed.build()).addFile(file, "placeholder-icon.png").queue();
			return;
		}
		
		if (args.size() == 1) {
			
		}
		else {
			channel.sendMessage("The command was entered incorrectly. Try *" + prefix + "hunts* and carefully follow the instructions.").queue();
			return;
		}
	}

}
