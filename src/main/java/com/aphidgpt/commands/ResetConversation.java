package com.aphidgpt.commands;

import com.aphidgpt.AphidGPT;
import com.aphidgpt.json.manage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Objects;


public class ResetConversation {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("resetconv").executes(ResetConversation::reset)
        );
    }

    private static int reset(CommandContext<ServerCommandSource> ctx) {
        Entity entity = ctx.getSource().getEntity();

        if (entity != null) {


            String uuid = entity.getUuid().toString();

            for (int i = 0; i < AphidGPT.PLAYERS.size(); i++) {
                if (Objects.equals(AphidGPT.PLAYERS.get(i).getUUID(), uuid)) {

                    AphidGPT.PLAYERS.remove(i);

                    manage.WriteJson(AphidGPT.PLAYERS, "players.json");

                    ctx.getSource().sendFeedback(() -> Text.of("Reset conversation"), false);

                    return 2;
                }
            }
            ctx.getSource().sendFeedback(() -> Text.of("You aren't even registered"), false);

            return 1;

        }

        ctx.getSource().sendFeedback(() -> Text.of("You require to be a entity to run this command."), false);
        return 0;
    }
}
