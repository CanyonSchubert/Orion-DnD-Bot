package commands;

import java.awt.Color;
import java.io.File;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;

import main.App;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import resources.Authenticator;
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
		
		if (args.get(0).equals("grid")) {
			/*
			 * Grabs the prefix from auth.json.
			 * Gets the message and channels for ease of access.
			 * Grabs the User Database from users.json.
			 */
			Authenticator auth = new Authenticator();
			final String prefix = auth.getPrefix();
			
			Database userDB;
			if (!App.DEV_MODE) userDB = new Database("users");
			else userDB = new Database("sampledb");
			JSONObject users = userDB.getDatabase();
			JSONObject user = (JSONObject) users.get(message.getAuthor().getId());
			JSONObject selected = (JSONObject) user.get("selected");
			
			int CEDARWOOD_CAVE_WIDTH = 5;
			int CEDARWOOD_CAVE_HEIGHT = 5;
			String[][] grid = new String[CEDARWOOD_CAVE_WIDTH][CEDARWOOD_CAVE_HEIGHT];
			grid[0][0] = "<:" + message.getGuild().getEmotesByName("corner", true).get(0).getName() + ":" + message.getGuild().getEmotesByName("corner", true).get(0).getId() + ">";
			grid[0][1] = "<:" + message.getGuild().getEmotesByName("A_grid", true).get(0).getName() + ":" + message.getGuild().getEmotesByName("A_grid", true).get(0).getId() + ">";
			grid[0][2] = "<:" + message.getGuild().getEmotesByName("B_grid", true).get(0).getName() + ":" + message.getGuild().getEmotesByName("B_grid", true).get(0).getId() + ">";
			grid[0][3] = "<:" + message.getGuild().getEmotesByName("C_grid", true).get(0).getName() + ":" + message.getGuild().getEmotesByName("C_grid", true).get(0).getId() + ">";
			grid[0][4] = "<:" + message.getGuild().getEmotesByName("D_grid", true).get(0).getName() + ":" + message.getGuild().getEmotesByName("D_grid", true).get(0).getId() + ">";
			grid[1][0] = "<:" + message.getGuild().getEmotesByName("1_grid", true).get(0).getName() + ":" + message.getGuild().getEmotesByName("1_grid", true).get(0).getId() + ">";
			grid[2][0] = "<:" + message.getGuild().getEmotesByName("2_grid", true).get(0).getName() + ":" + message.getGuild().getEmotesByName("2_grid", true).get(0).getId() + ">";
			grid[3][0] = "<:" + message.getGuild().getEmotesByName("3_grid", true).get(0).getName() + ":" + message.getGuild().getEmotesByName("3_grid", true).get(0).getId() + ">";
			grid[4][0] = "<:" + message.getGuild().getEmotesByName("4_grid", true).get(0).getName() + ":" + message.getGuild().getEmotesByName("4_grid", true).get(0).getId() + ">";
			for (int x = 1; x < CEDARWOOD_CAVE_WIDTH; ++x) {
				for (int y = 1; y < CEDARWOOD_CAVE_HEIGHT; ++y) {
					grid[x][y] = "<:" + message.getGuild().getEmotesByName("empty", true).get(0).getName() + ":" + message.getGuild().getEmotesByName("empty", true).get(0).getId() + ">";;
				}
			}
			String gridOut = "";
			for (int x = 0; x < CEDARWOOD_CAVE_WIDTH; ++x) {
				for (int y = 0; y < CEDARWOOD_CAVE_HEIGHT; ++y) {
					gridOut += grid[x][y];
				}
				gridOut += "\n";
			}
			
			Color prefColor;
			if (!(selected.get("color") == null))
				prefColor = new Color(Integer.decode((String) selected.get("color")));
			else prefColor = new Color(0x1330c2);

			EmbedBuilder embed = new EmbedBuilder()
					.setColor(prefColor)
					.addField("Cedarwood Cave", "Nino Lv. 3 --- HP 50/50 | Mana 20/100\nBuck Lv. 5 --- HP 62/70 | Rage 2/20", false)
					;

			channel.sendMessage(embed.build()).queue();
			channel.sendMessage(gridOut).queue();
		}
	}

}
