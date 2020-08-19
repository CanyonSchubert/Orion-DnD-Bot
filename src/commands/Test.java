package commands;

import java.util.List;

import org.json.simple.JSONObject;

import main.App;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import resources.Database;

public class Test {
	
	public static void run(MessageReceivedEvent event, List<String> args) {
		System.out.println("\nEntered Test Command!");
		
		if (event.getAuthor().isBot()) return;
		
		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();
		
		if (args.get(0).equals("colorgeneralizer")) {
			System.out.println("\nTesting the color generalizer algorithm!");
			
			Database userDB;
			if (!App.DEV_MODE) userDB = new Database("users");
			else userDB = new Database("sampledb");
			JSONObject users = userDB.getDatabase();
			JSONObject user = (JSONObject) users.get(message.getAuthor().getId());
			JSONObject selected = (JSONObject) user.get("selected");
			
			String colorGen = "";
			
			/*int red = Integer.decode((String) selected.get("color"));
			int green;
			int blue;*/
			
			
			
			channel.sendMessage("Color Preference: " + selected.get("color") + " | Generalized: " + colorGen).queue();
		}
	}

}
