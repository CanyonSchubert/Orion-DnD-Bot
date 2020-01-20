package commands;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CharCreate {

	public static void run(MessageReceivedEvent event, List<String> args) {
		System.out.println("\nEntered CharCreate Command!");
		
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
		 * Grabs the message and channel
		 * 
		 */
		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();
		
		/*
		 * Grabs the database from database.json.
		 * 
		 */
		JSONObject db = new JSONObject();
		try {
			db = (JSONObject) json.parse(new FileReader("./database/json/database.json"));	
		} catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } catch (ParseException e) { e.printStackTrace(); }
		JSONObject users = (JSONObject) db.get("users");
		JSONObject user = (JSONObject) users.get(message.getAuthor().getId());
		JSONObject characters = (JSONObject) user.get("characters");
		JSONObject selected = (JSONObject) characters.get((String) user.get("selected"));
		
		if (args.size() == 0) {
			message.getAuthor().openPrivateChannel().queue(dm -> dm.sendMessage("Please re-enter this command with the desired name of your new character as an argument. If the name would have spaces in it, simply replace any spaces with an underscore. (ex. **" + prefix + "charcreate Spiderman** or **" + prefix + "charcreate Peter_Parker**").queue());
			return;
		}
		
		else if (args.size() == 1) {
			
			String charName = args.get(0);
			if (charName.contains("_")) {
				charName = String.join(" ", charName.split("_"));
			}
			System.out.println("Desired Character Name: " + charName);
			
			String footer = "No Selected Character";
			try {
				footer = selected.get("name") + " (Lv. " + selected.get("level") + ") - " + selected.get("class");
			} catch (NullPointerException e) { System.out.println("ERROR: User has no selected character!"); }
			final File file = new File("./assets/placeholders/placeholder-icon.png");
			final EmbedBuilder embed = new EmbedBuilder()
					.setTitle("Class Selector")
					.setColor(new Color(0x1330c2))
					.setDescription("*Desired Name: " + charName + "*. Read through this menu and then re-enter this command one more time with the number of the corresponding class as an argument after your desired name. (ex. **" + prefix + "charcreate " + args.get(0) + " 2** to make a Ranger named " + charName + ".)")
					.addField("1. **Warrior** (Melee DPS / Tank)", 
							"A brutal fighter wielding an axe, with enough grit, rage, and prowess to be at the center of any conflict. Promotes to **Barbarian**, **Berserker**, or **Pit Fighter**.", true)
					.addField("2. **Ranger** (Ranged DPS)", 
							"A skilled archer with superior aim, raining ceaseless death from afar. Promotes to **Bowmaster**, **Sniper**, or **Gunslinger**.", true)

					
					.setAuthor("Orion", null, event.getJDA().getSelfUser().getAvatarUrl())
					.setFooter(footer)
					.setImage(null)
					.setThumbnail("attachment://placeholder-icon.png")
					.setTimestamp(Instant.now())
					;
			
			message.getAuthor().openPrivateChannel().queue(dm -> dm.sendMessage(embed.build()).addFile(file, "placeholder-icon.png").queue());
			return;
		}
		
		else if (args.size() == 2) {
			
			String charName = args.get(0);
			if (charName.contains("_")) {
				charName = String.join(" ", charName.split("_"));
			}
			
			String charClass = "";
			switch(args.get(1)) {
				case "1":
					charClass = "Warrior"; break;
				case "2":
					charClass = "Ranger"; break;
				
				default:
					channel.sendMessage("That class number is invalid, please refer to the Class Selector Menu for the appropriate class number and try again!").queue(); return;
			}
			
			if (characters.containsKey(charName)) {
				channel.sendMessage("You already have a character with that name; please delete that character through the Character Menu if you wish to continue.").queue();
				return;
			}
			
			characters.put(charName, new JSONObject());
			JSONObject character = (JSONObject) characters.get(charName);
			character.put("name", charName);
			character.put("class", charClass);
			character.put("level", 1);
			character.put("stats", new JSONObject());
			character.put("gear", new JSONObject());
			character.put("bag", new ArrayList());
			user.put("selected", charName);
			
			/*
			 * Rewrites and saves the database 
			 * FINAL
			 * 
			 */
			try (FileWriter file = new FileWriter("./database/json/database.json")) {
				file.write(db.toString());
				System.out.println("Successfully saved the database!");
			} catch (IOException e) { e.printStackTrace(); }
			
			channel.sendMessage("Your character, **" + charName + "** (" + charClass + "), has been successfully created!").queue(); // TODO: Change args.get(1) to the name of the selected class
			return;
		}
		
		else {
			channel.sendMessage("There are too many arguments in your command. Try again start with **" + prefix + "charcreate** and follow the instructions and examples carefully.").queue();
			return;
		}
		
	}
	
}
