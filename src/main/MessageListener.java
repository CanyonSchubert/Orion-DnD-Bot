package main;

//import java.io.File;
//import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import resources.Authenticator;
import resources.Database;
import commands.*;
import commands.Character;

public class MessageListener extends ListenerAdapter {
	/*
	File commandFolder = new File("./src/commands");
	File[] cmds = commandFolder.listFiles(new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) { return name.endsWith(".java"); }
		});
	*/
		
		@Override
		public void onMessageReceived(MessageReceivedEvent event) {
			/*
			 * Grabs the prefix from auth.json.
			 * 
			 */
			Authenticator auth = new Authenticator();
			final String prefix = auth.getPrefix();
			
			/*
			 * Checks to see if a message starts with the command prefix
			 * 
			 */
			Message message = event.getMessage();
			String content = message.getContentRaw();
			if (content.length() == 0 || !(content.substring(0, prefix.length()).equals(prefix))) return;
			MessageChannel channel = message.getChannel();
			
			/*
			 * Splits the command and arguments into their respective variables
			 * 
			 */
			String command = "";
			List<String> args = new ArrayList<String>();
			String[] splitter = content.split(" ");
			command = splitter[0].substring(prefix.length(), splitter[0].length());
			for (int i = 1; i < splitter.length; ++i) { args.add(splitter[i]); }
			System.out.println("\nCommand: " + command + '\n' + "Arguments: " + args.toString());
			
			/*
			 * Grabs the database from database.json.
			 * 
			 */
			Database userDB = new Database("users");
			JSONObject users = userDB.getDatabase();
			
			if (!(users.containsKey(message.getAuthor().getId())) && !(command.equals("register"))) {
				channel.sendMessage("Please register to gain further access. To do this simply use the command: **" + prefix + "register**").queue();
				return;
			}
			
			/*
			 * Creates collection of commands and delegates the arguments to one of them.
			 * TODO: Command Collection
			 */
			if (command.toLowerCase().equals("menu")) {
				Menu.run(event, args);
				return;
			}
			if (command.toLowerCase().equals("register")) {
				Register.run(event, args);
				return;
			}
			if (command.toLowerCase().equals("character")) {
				Character.run(event, args);
				return;
			}
			if (command.toLowerCase().equals("charcreate")) {
				CharCreate.run(event, args);
				return;
			}
			if (command.toLowerCase().equals("charselect")) {
				CharSelect.run(event, args);
				return;
			}
			if (command.toLowerCase().equals("chardelete")) {
				CharDelete.run(event, args);
				return;
			}
			if (command.toLowerCase().equals("charcolor")) {
				CharColor.run(event, args);
				return;
			}
			message.getChannel().sendMessage("Command not recognized. Check your command and try again - or try **" + prefix + "register** or **" + prefix + "menu** to get started!").queue();
		}
	}