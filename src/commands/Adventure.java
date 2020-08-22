package commands;

import java.awt.Color;
import java.io.File;
import java.time.Instant;
import java.util.List;

import org.json.simple.JSONObject;

import main.App;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import resources.Authenticator;
import resources.Database;

public class Adventure {
	
	public static void run(MessageReceivedEvent event, List<String> args) {
		System.out.println("\nEntered Adventure Command!");
		
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
				.setTitle("Adventure Menu")
				.setColor(prefColor)
				.setDescription("Please pick a command from this menu. For a more detailed description of the command, use " + prefix + "help [menu item].")
				.addField("Start a Hunt", prefix + "hunts", false)
				.addField("Start a Dungeon", prefix + "dungeons", false)
				.addField("Start a Raid", prefix + "raids", false)
				.addField("Change Preferred Battle Color", prefix + "battlecolor", false)
				.setAuthor("Orion", null, event.getJDA().getSelfUser().getAvatarUrl()) // TODO: Update Discord avatar to official logo, Change null to official webpage
				.setFooter(footer)
				.setThumbnail("attachment://placeholder-icon.png") // TODO: See line "File file..."
				.setTimestamp(Instant.now())
				;

		channel.sendMessage(embed.build()).addFile(file, "placeholder-icon.png").queue();
	}
}
