package main;

import net.dv8tion.jda.api.*;
import resources.Authenticator;

public class App {
	
	public static void main(String[] args) throws Exception {
		
		/*
		 * This block is simply retrieving the bot token and prefix from auth.json
		 * 
		 */
		Authenticator auth = new Authenticator();
		final String token = auth.getToken();
		
		/*
		 * Initializes Bot
		 * 
		 */
		@SuppressWarnings("unused")
		JDA jda = new JDABuilder(AccountType.BOT)
				.setToken(token)
				.addEventListeners(new MessageListener())
				.build()
				.awaitReady();
		System.out.println("Ready!");
	}
}