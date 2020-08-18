package commands;

import java.util.List;

import org.json.simple.JSONObject;

import main.App;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import resources.Authenticator;
import resources.Database;

public class Register {

	@SuppressWarnings("unchecked")
	public static void run(MessageReceivedEvent event, List<String> args) {
		System.out.println("\nEntered Register Command!");
		
		if (event.getAuthor().isBot()) return;
		
		/*
		 * Grabs the prefix from auth.json.
		 * 
		 */
		Authenticator auth = new Authenticator();
		final String prefix = auth.getPrefix();
		
		/*
		 * Grabs the database from database.json.
		 * 
		 */
		Database userDB;
		if (!App.DEV_MODE) userDB = new Database("users");
		else userDB = new Database("sampledb");
		JSONObject users = userDB.getDatabase();
		
		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();
		
		if (users.containsKey(message.getAuthor().getId())) {
			channel.sendMessage("Your account is already registered. Try **" + prefix + "menu** to get started.").queue();
			return;
		}
		
		users.put(message.getAuthor().getId(), new JSONObject());	
		JSONObject user = (JSONObject) users.get(message.getAuthor().getId());
		user.put("username", message.getAuthor().getAsTag());
		user.put("selected", new JSONObject());
		user.put("characters", new JSONObject());
		
		if (!App.DEV_MODE) userDB.saveDatabase(users);
		else userDB.saveDatabase(users);
		
		if (!(channel.getType() == ChannelType.PRIVATE))
				channel.sendMessage("Check your DMs - that's where I'll send most of my menus and information relevant only to you in an attempt to not clutter this server!").queue();
		else
			channel.sendMessage("Check back here often, this is where I'll send most of my menus and information relevant only to you so I don't clutter any servers!").queue();
		message.getAuthor().openPrivateChannel().queue(dm -> dm.sendMessage("Your Discord account has been successfully registered! Your next step should be using **" + 
				prefix + "menu** and following the steps in the Character Menu to create your first character!").queue());
	}
}
