// https://github.com/RIvance/minecraft-chatgpt-assistant/blob/master/src/main/java/org/ivance/gptassistant/mixin/ChatScreenMixin.java#L16

package com.aphidgpt.mixin;

import com.aphidgpt.AphidGPT;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Inject(at = @At("HEAD"), method = "sendMessage")
    private void sendMessage(String chatText, boolean addToHistory, CallbackInfoReturnable<Boolean> cir) {
        AphidGPT.handleChatMessage(chatText, MinecraftClient.getInstance().player);
    }
}