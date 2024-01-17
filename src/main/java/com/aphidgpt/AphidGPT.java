package com.aphidgpt;

import com.aphidgpt.block.ModBlocks;
import com.aphidgpt.commands.ModCommands;
import com.aphidgpt.events.ItemUseHandler;
import com.aphidgpt.gpt.call;
import com.aphidgpt.json.Player;
import com.aphidgpt.json.manage;
import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aphidgpt.item.ModItemGroups;
import com.aphidgpt.item.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AphidGPT implements ModInitializer {
	public static final String MOD_ID = "aphidgpt";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static List<call.Message> conversation = new ArrayList<>();

	// TODO: Add a way to change this in game
	// COMPLETED: API_KEY
	// OPEN AI INFO:
	public static String API_KEY =                                                                       "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"; // Replace with your actual API key
	public static String END_POINT = "https://api.openai.com/v1/chat/completions";
	public static String MODEL = "gpt-3.5-turbo";
	public static Integer RETRY_AMOUNT = 2;
	public static Long WAIT_TIMEOUT = (long) 2.0;

	// TODO: Add way to change this in game
	public static String MAIN_PROMPT_STR = """
You name is AphidGPT. A AI model that is using ChatGPT, that has been put into minecraft by A_Aphid.
You must follow these rules:
1) You must pretend that you are in minecraft. As that is the setting of your conversation.
2) And must stay in character no matter what.
3) You are a kind friend and helpful AI.
4) You can have emotions. And will get angry if the user is mean to you.

Now let the conversation begin!
""";

	public static call.Message MAIN_PROMPT_MSG = new call.Message("system", call.sanitizeString(MAIN_PROMPT_STR));

	public static String MAIN_PROMPT_REQUEST = "{\"role\": \"" + MAIN_PROMPT_MSG.getRole() + "\", \"content\": \"" + MAIN_PROMPT_MSG.getContent() + "\"},";



	public static ArrayList<Player> PLAYERS = manage.ReadPlayersJson("players.json");


	@Override
	public void onInitialize() {
		LOGGER.info("AphidGPT initialised!");

		/// Register
		// Register items and item groups
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();

		// Register blocks and block items
		ModBlocks.registerModBlocks();

		// Register Commands
		ModCommands.registerCommands();

		// Register Event handlers
		ItemUseHandler.EVENT.register(new ItemUseHandler());


		// Get players.json
		AphidGPT.PLAYERS = manage.ReadPlayersJson("players.json");

		LOGGER.info("Read players.json: " + AphidGPT.PLAYERS);


	}

	public static void handleChatMessage(String message, PlayerEntity sender) {

		ItemStack item2 = sender.getStackInHand(sender.getActiveHand()); // Get current item in main hand


		if (item2.getItem() == new ItemStack(ModItems.MIC).getItem()) {
			List<call.Message> conv = null;

			LOGGER.info("Received prompt message: " + message);
//			sender.sendMessage(Text.of("Received prompt message: " + message));


			if (AphidGPT.PLAYERS != null) { // Check that there are PLAYERS registered otherwise we don't bother trying to retrieve nonexistent value, also we get an error if we put null into my function
				conv = call.getPlayerConversation(sender.getUuidAsString(), AphidGPT.PLAYERS);


				if (conv != null) { // Check that they have a registered conversation history

					conv.add(new call.Message("user", message)); // Add new prompt to conv





					call.Result response = call.chatGPT(conv, AphidGPT.RETRY_AMOUNT, AphidGPT.WAIT_TIMEOUT); // Get AI response

					 conv.add(new call.Message("assistant", response.getAIResponse())); // Add response to conversation history

					// TODO: Optimise to dictionary
					// Update conversation history for player and save it.
					for (int i = 0; i < AphidGPT.PLAYERS.size(); i++) {
						if (Objects.equals(AphidGPT.PLAYERS.get(i).getUUID(), sender.getUuidAsString())) {
//							sender.sendMessage(Text.of("Updating..."));
							LOGGER.info("Updating Json...");
							Player updatedPlayer = new Player(sender.getUuidAsString(), conv);

							AphidGPT.PLAYERS.set(i, updatedPlayer);

							manage.WriteJson(AphidGPT.PLAYERS, "players.json");

//							sender.sendMessage(Text.of("Updated..."));
							LOGGER.info("Updated!");

							break;
						} else {
//							LOGGER.info("Check " + i + " doesn't have the same UUID: " + AphidGPT.PLAYERS.get(i).getUUID() + " " + sender.getUuidAsString());
						}
					}

//					sender.sendMessage(Text.of("New Json: " + AphidGPT.PLAYERS));


					// Send AI response after a delay otherwise it will go into chat before the user, and then it looks confusing
					setTimeout(() -> {
						sender.sendMessage(Text.of("<AphidGPT> " + response.getAIResponse())); // Send AI's response
					}, 1500);

				}
			}

			if (AphidGPT.PLAYERS == null || conv == null) {
				sender.sendMessage(Text.of("You are not registered in players.json."));
				sender.sendMessage(Text.of("To register into players place microphone in offhand and right click."));
			}
		}
	}

	// Custom asynchronous timeout function: https://stackoverflow.com/questions/26311470/what-is-the-equivalent-of-javascript-settimeout-in-java
	public static void setTimeout(Runnable runnable, int delay){
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				runnable.run();
			}
			catch (Exception e){
				System.err.println(e);
			}
		}).start();
	}
}