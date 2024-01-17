package com.aphidgpt.commands;

import com.aphidgpt.AphidGPT;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Info {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // Your command logic goes here
        dispatcher.register(CommandManager.literal("info")
                .then(CommandManager.literal("gpt")
                     // Makes you are the "Game master" / an op with permission level 2
                    .executes(Info::gptInfo)
                    .requires(source -> source.hasPermissionLevel(2))
                )
                .then(CommandManager.literal("mainprompt")
                    .executes(Info::mainPromptInfo)
                    .requires(source -> source.hasPermissionLevel(1)) // Requires at least op with level 1
                )
        );
    }

    private static int gptInfo(CommandContext<ServerCommandSource> context) {

        context.getSource().sendFeedback(() -> Text.of("API key: " + AphidGPT.API_KEY), false);
        context.getSource().sendFeedback(() -> Text.of("GPT model: " + AphidGPT.MODEL), false);

        return 1;
    }

    private static int mainPromptInfo(CommandContext<ServerCommandSource> context) {

        context.getSource().sendFeedback(() -> Text.of("Main prompt: " + AphidGPT.MAIN_PROMPT_STR), false);

        return 1;
    }

}
