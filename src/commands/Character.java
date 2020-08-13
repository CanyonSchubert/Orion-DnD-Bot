package commands;

import java.awt.Color;
import java.io.File;
import java.time.Instant;
import java.util.List;

import org.json.simple.JSONObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import resources.Authenticator;
import resources.Database;

public class Character {

	public static void run(MessageReceivedEvent event, List<String> args) {
		System.out.println("\nEntered Character Command!");
		
		if (event.getAuthor().isBot()) return;
		
		/*
		 * Grabs the prefix from auth.json.
		 * Gets message and channel for ease of access.
		 * Grabs the user database from users.json.
		 */
		Authenticator auth = new Authenticator();
		final String prefix = auth.getPrefix();
		
		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();
		
		Database userDB = new Database("users");
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
		
		final File file = new File("./assets/placeholders/placeholder-icon.png"); // TODO: Update to official icon
		final EmbedBuilder embed = new EmbedBuilder()
				.setTitle("Character Menu")
				.setColor(prefColor)
				.setDescription("Please pick a command from this menu. For a more detailed description of the command, use " + prefix + "help [menu item].")
				.addField("Create a Character", "**" + prefix + "charcreate**", false)
				.addField("Select Active Character", "**" + prefix + "charselect**", false)
				.addField("Active Character Info", "**" + prefix + "charinfo**", false)
				.addField("Delete a Character", "**" + prefix + "chardelete**", false)
				.setAuthor("Orion", null, event.getJDA().getSelfUser().getAvatarUrl()) // TODO: Update Discord avatar to official logo, Change null to official webpage
				.setFooter(footer)
				.setImage(null)
				.setThumbnail("attachment://placeholder-icon.png") // TODO: See line "File file..."
				.setTimestamp(Instant.now())
				;
		
		if (!(channel.getType() == ChannelType.PRIVATE))
			channel.sendMessage("<@" + message.getAuthor().getId() + ">, Check your DMs.").queue();
		message.getAuthor().openPrivateChannel().queue(dm -> dm.sendMessage(embed.build()).addFile(file, "placeholder-icon.png").queue());
	}
}