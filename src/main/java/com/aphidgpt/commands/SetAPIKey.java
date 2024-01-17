package com.aphidgpt.commands;

import com.aphidgpt.AphidGPT;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class SetAPIKey {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("setapikey")
                        .then(CommandManager.argument("key", StringArgumentType.word())
                                .executes(SetAPIKey::setApiKey)
                        ).requires(source -> source.hasPermissionLevel(2)) // Makes you are the "Game master" / an op with permission level 2
        );
    }


    private static int setApiKey(CommandContext<ServerCommandSource> context) {
        String apiKey = StringArgumentType.getString(context, "key");

        AphidGPT.LOGGER.info("API key changed to: " + apiKey);

        AphidGPT.API_KEY = apiKey;

        // Store the API key in your mod's data or configuration

        // You can also perform any other logic with the API key here

        context.getSource().sendFeedback(() -> Text.of("API key set to: " + apiKey), false);
        return 1;
    }

}
