package com.aphidgpt.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;


public class ModCommands {

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {
            SetAPIKey.register(dispatcher);
            Info.register(dispatcher);
            ResetConversation.register(dispatcher);
        });
    }

}
