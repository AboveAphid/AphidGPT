package com.aphidgpt.item;

import com.aphidgpt.AphidGPT;
import com.aphidgpt.gpt.call;
import com.aphidgpt.json.Player;
import com.aphidgpt.json.manage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Mic extends Item {

    public Mic(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!player.getWorld().isClient) { // Check if the function is run by server because this function is called twice, once by the server and then by the client and we don't want this function to execute its code twice every time the item is used.
            if (hand == Hand.MAIN_HAND) { // If in Mainhand send test input if possible
                player.playSound(SoundEvents.BLOCK_ANCIENT_DEBRIS_HIT, 1, 1);
                player.sendMessage(Text.of("Sending test input."));

                List<call.Message> conversation = null;
                if (AphidGPT.PLAYERS != null) {
                    for (int i = 0; i < AphidGPT.PLAYERS.size(); i++) { // Run through PLAYERS to get conversation

                        if (Objects.equals(AphidGPT.PLAYERS.get(i).getUUID(), player.getUuidAsString())) {
                            conversation = AphidGPT.PLAYERS.get(i).getConversation();

                            break;
                        }
                    }
                    if (conversation == null) {
                        player.sendMessage(Text.of("You aren't in players.json."));
                        player.sendMessage(Text.of("Please place microphone in offhand and right click."));
                        return TypedActionResult.success(player.getStackInHand(hand));
                    }

                    // Add message to conversation history
                    conversation.add(new call.Message("user", "Hello, how are you?"));
                    // Get response
                    call.Result response = call.chatGPT(conversation, AphidGPT.RETRY_AMOUNT, AphidGPT.WAIT_TIMEOUT);

                    // Send player the output.
                    String inputMessage = response.getUserInput();
                    String generatedResponse = response.getAIResponse();

                    player.sendMessage(Text.of("<Input> " + inputMessage));
                    player.sendMessage(Text.of("<AphidGPT> " + generatedResponse));

                } else {
                    player.sendMessage(Text.of("You aren't in players.json."));
                    player.sendMessage(Text.of("Please place microphone in offhand and right click."));
                }

            } else { // If in offhand register player into json if needed
                player.playSound(SoundEvents.ENTITY_VILLAGER_YES, 1, 1);
                player.sendMessage(Text.of("Adding you to players.json..."));

                if (AphidGPT.PLAYERS != null) { // Make sure it isn't null otherwise we will get an error
                    // Loop over list and check that they aren't in list. We use this type of loop because AphidGPT.PLAYERS is static
                    for (int i = 0; i < AphidGPT.PLAYERS.size(); i++) {

                        if (Objects.equals(AphidGPT.PLAYERS.get(i).getUUID(), player.getUuidAsString())) {
                            player.sendMessage(Text.of("You are already in list!"));

                            return TypedActionResult.success(player.getStackInHand(hand));
                        }
                    }
                }


                // Add players to json and make conversations. (Also players.json is saved in "./run/players.json"
                ArrayList<call.Message> conv = new ArrayList<>();
                // Add main prompt.
//                conv.add(new call.Message("system", call.sanitizeString(AphidGPT.MAIN_PROMPT)));
                // Create player
                Player p = new Player(player.getUuidAsString(), conv);

                ArrayList<Player> players = manage.addPlayer(p, AphidGPT.PLAYERS, "players.json");
                manage.WriteJson(players, "players.json"); // Write it to json file as addPlayer doesn't do it properly with a static var

                AphidGPT.PLAYERS = players; // Refresh PLAYERS var.

                player.sendMessage(Text.of("You have now joined players.json"));

                // Check there up to date
//                AphidGPT.LOGGER.info("AphidGPT Players:");
//                AphidGPT.LOGGER.info(String.valueOf(AphidGPT.PLAYERS));
//                AphidGPT.LOGGER.info("Players:");
//                AphidGPT.LOGGER.info(String.valueOf(players));
//                AphidGPT.LOGGER.info("New Read:");
//                AphidGPT.LOGGER.info(String.valueOf(manage.ReadPlayersJson("players.json")));

            }
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }
}