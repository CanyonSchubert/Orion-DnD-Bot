package main;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	
	LinkedList<Class> commands = new LinkedList<Class>();
	
	/*
	 * Initializes all commands into a LinkedList.
	 * Update this with every new command until it's possible/known how to fill the LinkedList
	 * from the file system.
	 */
	public MessageListener() {
		commands.add(Character.class);
		commands.add(CharColor.class);
		commands.add(CharCreate.class);
		commands.add(CharDelete.class);
		commands.add(CharSelect.class);
		commands.add(Menu.class);
		commands.add(Register.class);
		commands.add(Test.class);
	}
	
	public LinkedList<Class> getCommands() {
		return this.commands;
	}
		
		@SuppressWarnings("unchecked")
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
			if (content.length() < prefix.length() || !(content.substring(0, prefix.length()).equals(prefix))) return;
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
			Database userDB;
			if (!App.DEV_MODE) userDB = new Database("users");
			else userDB = new Database("sampledb");
			JSONObject users = userDB.getDatabase();
			
			if (!(users.containsKey(message.getAuthor().getId())) && !(command.equals("register"))) {
				channel.sendMessage("Please register to gain further access. To do this simply use the command: **" + prefix + "register**").queue();
				return;
			}
			
			for (int i = 0; i < commands.size(); ++i) { // why doesn't this need ++i?
				if (command.toLowerCase().equals(commands.get(i).getName().replace("commands.", "").toLowerCase())) {
					try {
						Method cmd = commands.get(i).getDeclaredMethod("run", MessageReceivedEvent.class, List.class);
						cmd.invoke(commands.get(i), event, args);
						return;
					} catch (NoSuchMethodException e) {
						System.out.println("Cannot find run method in command: " + commands.get(i).getName().replace("commands.", ""));
						e.printStackTrace();
						return;
					} catch (SecurityException e) {
						System.out.println("Security Exception for command: " + commands.get(i).getName().replace("commands.", ""));
						e.printStackTrace();
						return;
					} catch (IllegalAccessException e) {
						System.out.println("Illegal Access Exception for command: " + commands.get(i).getName().replace("commands.", ""));
						e.printStackTrace();
						return;
					} catch (IllegalArgumentException e) {
						System.out.println("Illegal Argument Exception for command: " + commands.get(i).getName().replace("commands.", ""));
						e.printStackTrace();
						return;
					} catch (InvocationTargetException e) {
						System.out.println("Invocation Target Exception for command: " + commands.get(i).getName().replace("commands.", ""));
						e.printStackTrace();
						return;
					}
				}
			}
			message.getChannel().sendMessage("Command not recognized. Check your command and try again - or try **" + prefix + "register** or **" + prefix + "menu** to get started!").queue();
		}	
	}