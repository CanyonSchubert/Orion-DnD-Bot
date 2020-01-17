package commands;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Menu {

	public static void run(MessageReceivedEvent event, List<String> args) {
		System.out.println("Entered Menu Command!");
		
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
		 * NOT CURRENTLY USED
		 */
		/*JSONObject db = new JSONObject();
		JSONObject regUsers = new JSONObject();
		try {
			db = (JSONObject) json.parse(new FileReader("./database/json/database.json"));
			regUsers = (JSONObject) db.get("users");	
		} catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } catch (ParseException e) { e.printStackTrace(); }
		 */

		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();
		
		File file = new File("./assets/placeholders/placeholder-icon.png"); // TODO: Update to official icon
		File file2 = new File("./assets/placeholders/placeholder-title.jpg"); // TODO: Update to official title
		EmbedBuilder embed = new EmbedBuilder()
				.setTitle("Main Menu")
				.setColor(new Color(0x1330c2))
				.setDescription("Please pick a command from this menu. For a more detailed description of the command, use " + prefix + "help [menu item].")
				.addField("New Character", "**" + prefix + "newchar** | " + prefix + "help newchar", false)
				//.addField(" ", "Takes you to the Character Creation Menu", false)
				//.addBlankField(true)
				.addField("Character Info", "**" + prefix + "selectinfo** | " + prefix + "help selectinfo", false)
				//.addField(" ", "Shows info about your currently selected character", false)
				//.addBlankField(true)
				.addField("Form a Party", "**" + prefix + "formparty** | " + prefix + "help formparty", false)
				//.addField(" ", "Forms a party with the mentioned users (Up to 4 others)", true)
				//.addBlankField(true)
				.addField("Start Adventure", "**" + prefix + "adventure** | " + prefix + "help adventure", false)
				//.addField(" ", "Takes you to the Adventure Select Menu", true)
				//.addBlankField(true)
				.setAuthor("Orion", null, event.getJDA().getSelfUser().getAvatarUrl()) // TODO: Update Discord avatar to official logo, Change null to official webpage
				.setFooter("Apollo (Lv. 28) - Bowmaster") // TODO: Set Footer to currently selected character
				.setImage("attachment://placeholder-title.jpg") // TODO: See line "File file2..."
				.setThumbnail("attachment://placeholder-icon.png") // TODO: See line "File file..."
				.setTimestamp(Instant.now())
				;
		
		channel.sendMessage(embed.build()).addFile(file, "placeholder-icon.png").addFile(file2, "placeholder-title.jpg").queue();
	}
	
}
