package commands;

import java.util.List;

import main.MessageListener;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import resources.Authenticator;


public class Reload {

	public static void run(MessageReceivedEvent event, List<String> args, MessageListener bot) {
		System.out.println("\nEntered Reload Command!");
		
		if (event.getAuthor().isBot()) return;
		
		System.out.println("Second!");
		
		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();
		
		Authenticator auth = new Authenticator();
		final String prefix = auth.getPrefix();
		final List<String> devs = auth.getDevs();
		
		if (!devs.contains(message.getAuthor().getId())) {
			channel.sendMessage("<@" + message.getAuthor().getId() + ">, only developers have access to that command!").queue();
			return;
		}
		
		String cmdToReload = "";
		String augmentation = "";
		if (args.size() == 1) {
			cmdToReload = args.get(0).toLowerCase();
			System.out.println("Attempting to reload: " + args.get(0));
		} else {
			channel.sendMessage("<@" + message.getAuthor().getId() + ">, please enter a command to reload!").queue();
			return;
		}
		if (args.size() == 2) {
			augmentation = args.get(1);
		}
		if (args.size() > 2) {
			channel.sendMessage("<@" + message.getAuthor().getId() + ">, you entered too many arguments!").queue();
			return;
		}
		
		
		for (int i = 0; i < bot.getCommands().size(); ++i) {
			if (cmdToReload.equals(bot.getCommands().get(i).getName().replace("commands.", "").toLowerCase())) {
				bot.getCommands().remove(i);
				
				switch(cmdToReload) {
				
				case "menu":
					bot.getCommands().add(Menu.class);
					break;
					
				case "character":
					bot.getCommands().add(Character.class);
					break;
					
				case "reload":
					bot.getCommands().add(Reload.class);
					break;
				
				}
				
				channel.sendMessage("Command refreshed!").queue();
				return;
			}
		}
	}

}
