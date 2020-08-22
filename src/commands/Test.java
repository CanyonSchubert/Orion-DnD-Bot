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
import resources.Constants;
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
			
			String[][] grid = new String[Constants.CEDARWOOD_CAVE_WIDTH][Constants.CEDARWOOD_CAVE_HEIGHT];
			grid[0][0] = Constants.GRID_CORNER;
			grid[0][1] = Constants.GRID_A;
			grid[0][2] = Constants.GRID_B;
			grid[0][3] = Constants.GRID_C;
			grid[0][4] = Constants.GRID_D;
			grid[1][0] = Constants.GRID_1;
			grid[2][0] = Constants.GRID_2;
			grid[3][0] = Constants.GRID_3;
			grid[4][0] = Constants.GRID_4;
			for (int x = 1; x < Constants.CEDARWOOD_CAVE_WIDTH; ++x) {
				for (int y = 1; y < Constants.CEDARWOOD_CAVE_HEIGHT; ++y) {
					grid[x][y] = Constants.GRID_EMPTY;
				}
			}
			grid[1][1] = Constants.WARRIOR_BLUE_EAST;
			String gridOut = "";
			for (int x = 0; x < Constants.CEDARWOOD_CAVE_WIDTH; ++x) {
				for (int y = 0; y < Constants.CEDARWOOD_CAVE_HEIGHT; ++y) {
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
					.addField(Constants.CEDARWOOD_CAVE_NAME, "Nino Lv. 3 --- HP 50/50 | Mana 20/100\nBuck Lv. 5 --- HP 62/70 | Rage 2/20", false)
					;

			channel.sendMessage(embed.build()).queue();
			channel.sendMessage(gridOut).queue();
		}
	}

}
