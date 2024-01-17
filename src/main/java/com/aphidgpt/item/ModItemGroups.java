package com.aphidgpt.item;

import com.aphidgpt.AphidGPT;
import com.aphidgpt.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup AphidGPT_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(AphidGPT.MOD_ID, "mic"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.aphidgpt"))
            .icon(() -> new ItemStack(ModItems.MIC)).entries(((displayContext, entries) -> {
// Add items here: (In the order you want to display)
entries.add(ModItems.MIC);
entries.add(ModItems.STEEL_INGOT);
entries.add(ModItems.UNREFINED_STEEL);
entries.add(ModBlocks.STEEL_BLOCK);
entries.add(ModBlocks.POWER_BLOCK);
            })).build());


    public static void registerItemGroups() {
        AphidGPT.LOGGER.info("Registering custom item groups");
    }
}
