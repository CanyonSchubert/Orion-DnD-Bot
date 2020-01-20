package main;

//import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
			String prefixAuth = "";
			String command = "";
			List<String> args = new ArrayList<String>();
			JSONParser json = new JSONParser();
			try {
				JSONObject res = (JSONObject) json.parse(new FileReader("./src/main/auth.json"));
				
				prefixAuth = (String) res.get("prefix");
			} catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } catch (ParseException e) { e.printStackTrace(); }
			final String prefix = prefixAuth;
			
			/*
			 * Checks to see if a message starts with the command prefix
			 * 
			 */
			Message message = event.getMessage();
			String content = message.getContentRaw();
			if (content.length() == 0 || !(content.substring(0, prefix.length()).equals(prefix))) return;
			
			/*
			 * Splits the command and arguments into their respective variables
			 * 
			 */
			String[] splitter = content.split(" ");
			command = splitter[0].substring(prefix.length(), splitter[0].length());
			for (int i = 1; i < splitter.length; ++i) { args.add(splitter[i]); }
			System.out.println("\nCommand: " + command + '\n' + "Arguments: " + args.toString());
			
			/*
			 * Grabs the database from database.json.
			 * 
			 */
			List<String> regUsers = new ArrayList<String>();
			try {
				JSONObject db = (JSONObject) json.parse(new FileReader("./database/json/database.json"));
				JSONObject users = (JSONObject) db.get("users");
				for (Object key : users.keySet()) { regUsers.add(key.toString()); }			
			} catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); } catch (ParseException e) { e.printStackTrace(); }

			MessageChannel channel = message.getChannel();
			
			if (!(regUsers.contains(message.getAuthor().getId())) && !(command.equals("register"))) {
				channel.sendMessage("Please register to gain further access. To do this simply use the command: **" + prefix + "register**").queue();
				return;
			}
			
			/*
			 * Creates collection of commands and delegates the arguments to one of them.
			 * TODO: Command Collection
			 */
			if (command.toLowerCase().equals("ping")) {
				Ping.run(event, args);
				return;
			}
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
			if (command.toLowerCase().contentEquals("charcreate")) {
				CharCreate.run(event, args);
				return;
			}
			message.getChannel().sendMessage("Command not recognized. Check your command and try again - or try **" + prefix + "register** or **" + prefix + "menu** to get started!").queue();
		}
	}