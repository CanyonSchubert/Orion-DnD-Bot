package commands;

import java.util.List;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping {

	public static void run(MessageReceivedEvent event, List<String> args) {
		System.out.println("Entered Ping Command!");
		
		if (event.getAuthor().isBot()) return;

		Message message = event.getMessage();
		MessageChannel channel = message.getChannel();
		channel.sendMessage("Pong!").queue();
	}	
}
