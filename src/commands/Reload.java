package commands;

import java.io.File;
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
		
		System.out.println("First!");
		
		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();
		
		Authenticator auth = new Authenticator();
		final String prefix = auth.getPrefix();
		final List<String> devs = auth.getDevs();
		
		String cmdToReload = args.get(0).toLowerCase();
		String augmentation = args.get(1);
		
		if (devs.contains(message.getAuthor().getId())) {
			for (int i = 0; i < bot.getCommands().size(); ++i) {
				if (cmdToReload.equals(bot.getCommands().get(i).getName().replace(".class", ""))) {
					System.out.println("found");
				}
			}
		}
		else channel.sendMessage("<@" + message.getAuthor().getId() + ">, only developers have access to that command!").queue();
	}

}
