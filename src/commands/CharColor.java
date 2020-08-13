package commands;

import java.util.List;

import org.json.simple.JSONObject;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import resources.Authenticator;
import resources.Database;

public class CharColor {
	
	@SuppressWarnings("unchecked")
	public static void run(MessageReceivedEvent event, List<String> args) {
		System.out.println("\nEntered CharColor Command!");
		
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
		 * First Pass, no args. Explain how to enter the command with the proper hex value.
		 */
		if (args.size() == 0) {
			if (!(channel.getType() == ChannelType.PRIVATE))
				channel.sendMessage("<@" + message.getAuthor().getId() + ">, Check your DMs.").queue();
			message.getAuthor().openPrivateChannel().queue(dm -> dm.sendMessage("Please re-enter this command with a hex color as the argument. Pick a color from this website (https://htmlcolorcodes.com/color-picker/) and find the 'HEX' value just above the color picker. Paste that value after the command. (ex. **" + prefix + "charcolor #1330c2** or **" + prefix + "charcolor #000000**)").queue());
			return;
		}
		
		/*
		 * Second pass, hex value as arg. Save the color to the character.
		 */
		if (args.size() == 1 && args.get(0).length() == 7) {
			selected.put("color", args.get(0));
			user.put("selected", selected);
			
			userDB.saveDatabase(users);
			
			channel.sendMessage("<@" + message.getAuthor().getId() + ">, your preferred color for " + selected.get("name") + " has been successfully changed!").queue();
			return;
		}
		
		/*
		 * Too many args or arg was formatted wrong.
		 */
		else {
			channel.sendMessage("<@" + message.getAuthor().getId() + ">, you either entered the command with too many arguments or your hex value formatting was wrong. Please try again!").queue();
			return;
		}
	}

}
