package commands;

import java.awt.Color;
import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import main.App;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import resources.Authenticator;
import resources.Database;

public class CharCreate {

	@SuppressWarnings("unchecked")
	public static void run(MessageReceivedEvent event, List<String> args) {
		System.out.println("\nEntered CharCreate Command!");
		
		if (event.getAuthor().isBot()) return;
		
		/*
		 * Grabs the prefix from auth.json.
		 * Gets message and channel for ease of access.
		 * Grabs the user database.
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
		JSONObject characters = (JSONObject) user.get("characters");
		JSONObject selected = (JSONObject) user.get("selected");
		
		/*
		 * First pass, instructs to re-enter with a single argument of the character name
		 */
		if (args.size() == 0) {
			if (!(channel.getType() == ChannelType.PRIVATE))
				channel.sendMessage("<@" + message.getAuthor().getId() + ">, Check your DMs.").queue();
			message.getAuthor().openPrivateChannel().queue(dm -> dm.sendMessage("Please re-enter this command with the desired name of your new character as an argument. If the name would have spaces in it, simply replace any spaces with an underscore. (ex. **" + prefix + "charcreate Spiderman** or **" + prefix + "charcreate Peter_Parker**").queue());
			return;
		}
		
		/*
		 * Grabs the class database.
		 */
		Database classesDB = new Database("classes");
		JSONObject classes = classesDB.getDatabase();
		
		/*
		 * Second pass, instructs to re-enter with a second argument of the corresponding class number
		 */
		if (args.size() == 1) {
			
			/*
			 * Sets up desired character name.
			 * Sets up the basic attributes of the embed and populates them.
			 */
			String charName = args.get(0);
			if (charName.contains("_"))
				charName = String.join(" ", charName.split("_"));
			System.out.println("Desired Character Name: " + charName);
			
			final File file = new File("./assets/placeholders/placeholder-icon.png");
			EmbedBuilder embed = new EmbedBuilder(); // TODO: Use reactions to tab through pages? or maybe select class through reactions?
			embed.setTitle("Class Selector");
			embed.setAuthor("Orion", null, event.getJDA().getSelfUser().getAvatarUrl());
			embed.setImage(null);
			embed.setThumbnail("attachment://placeholder-icon.png");
			embed.setTimestamp(Instant.now());
			
			/*
			 * Description generation for embed
			 */
			String description = 
					"*Desired Name: " + charName + "*\nRead through this menu and then re-enter this command one more time with the number of the corresponding class as "
							+ "an argument after your desired name. (ex. **" + prefix + "charcreate " + args.get(0) + " 2** to make a Ranger named " + charName + ".)";
			embed.setDescription(description);
			
			/*
			 * Field Generation for embed
			 */
			int classNum = 1;
			JSONObject charClass = new JSONObject();
			String title = "";
			String field = "";
			JSONObject subclasses = new JSONObject();
			JSONObject subClass = new JSONObject();
			for (Object nextClass : classes.values()) {
				charClass = (JSONObject) nextClass;
				
				/*
				 * title for each field
				 */
				title = classNum + ". **" + charClass.get("id") + "** (";
				List<String> tags = (ArrayList<String>) charClass.get("tags");
				for (int i = 0; i < tags.size(); ++i) {
					if (i < tags.size()-1) title += (tags.get(i) + " | ");
					else title += (tags.get(i) + ")");
				}
				
				/*
				 * body for each field
				 */
				field = charClass.get("description") + " Promotes to ";
				subclasses = (JSONObject) charClass.get("subclasses");
				int subCounter = 0;
				for (Object nextSub : subclasses.values()) {
					subClass = (JSONObject) nextSub;
					if (subCounter < subclasses.values().size()-1) field += ("**" + subClass.get("id") + "**, ");
					else field += ("or **" + subClass.get("id") + "**.");					
					++subCounter;
				}
				embed.addField(title, field, true);
				
				/*
				 * next class
				 */
				++classNum;
			}
			
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
			
			embed.setFooter(footer);
			embed.setColor(prefColor);

			/*
			 * sends the menu in a dm to the requester.
			 */
			if (!(channel.getType() == ChannelType.PRIVATE))
				channel.sendMessage("<@" + message.getAuthor().getId() + ">, Check your DMs.").queue();
			message.getAuthor().openPrivateChannel().queue(dm -> dm.sendMessage(embed.build()).addFile(file, "placeholder-icon.png").queue());
			return;
		}
		
		/*
		 * Third pass, creates the character with the two arguments provided (provided that they are valid).
		 */
		else if (args.size() == 2) {
			
			/*
			 * Sets up desired character name.
			 * Sets up desired character class (or refers them to the class selector if invalid).
			 * Checks to see if the user already has a character with the desired name.
			 */
			String charName = args.get(0);
			if (charName.contains("_")) {
				charName = String.join(" ", charName.split("_"));
			}
			
			String charClass = "";
			int switchCount = 1;
			boolean classFound = false;
			JSONObject controlClass = new JSONObject();
			for (Object nextClass : classes.values()) {
				controlClass = (JSONObject) nextClass;
				if (args.get(1).equals(Integer.toString(switchCount))) {
					charClass = (String) controlClass.get("id");
					classFound = true;
					break;
				}
				++switchCount;
			}
			if (!classFound) {
				channel.sendMessage("That class number is invalid, please refer to the Class Selector Menu for the appropriate class number and try again!").queue();
				return;
			}
			
			if (characters.containsKey(charName)) {
				channel.sendMessage("You already have a character with that name; please delete that character through the Character Menu if you wish to continue.").queue();
				return;
			}
			
			/*
			 * Sets the new character in the user database.
			 * Saves the user database.
			 * Sends the success message in the channel it was requested.
			 */
			characters.put(charName, new JSONObject());
			JSONObject character = (JSONObject) characters.get(charName);
			character.put("name", charName);
			character.put("color", "0x1330c2");
			character.put("class", charClass);
			character.put("level", 1);
			character.put("gear", new JSONObject()); // TODO: Create empty slots when gear slots are determined
			character.put("bag", new ArrayList<String>());
			character.put("stats", new JSONObject()); // TODO: Create and populate slots from base stats in the class database
			character.put("statpoints", 0);
			character.put("talentpoints", 0);
			character.put("abilities", new ArrayList<String>());
			user.put("selected", character);

			if (!App.DEV_MODE) userDB.saveDatabase(users);
			else userDB.saveDatabase(users);
			
			channel.sendMessage("Your character, **" + charName + "** (" + charClass + "), has been successfully created!").queue();
			return;
		}
		
		/*
		 * Response if more than two arguments.
		 */
		else {
			channel.sendMessage("There are too many arguments in your command. Try again start with **" + prefix + "charcreate** and follow the instructions and examples carefully.").queue();
			return;
		}	
	}	
}