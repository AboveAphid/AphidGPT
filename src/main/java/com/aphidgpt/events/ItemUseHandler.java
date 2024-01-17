package com.aphidgpt.events;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemUseHandler implements UseItemCallback {
    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        if (player instanceof PlayerEntity && !world.isClient()) {
            ItemStack itemStack = player.getStackInHand(hand);
            String ItemName = itemStack.getItem().getName().getString();
            String ItemCustomName = itemStack.getName().getString();
            int AmountOfItem = itemStack.getCount();
            //player.sendMessage(Text.of(AmountOfItem + " " + ItemName + " was used by player with the item name " + ItemCustomName));

        }
        return TypedActionResult.pass(ItemStack.EMPTY);
    }
}
