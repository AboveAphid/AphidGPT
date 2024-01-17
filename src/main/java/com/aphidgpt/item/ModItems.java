package com.aphidgpt.item;

import com.aphidgpt.AphidGPT;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    // Create items

    private static final Item.Settings MicSettings = new FabricItemSettings().maxCount(1);
    public static final Item MIC = new Mic(MicSettings);

    public static final Item STEEL_INGOT = new Item(new FabricItemSettings());
    public static final Item UNREFINED_STEEL = new Item(new FabricItemSettings());


    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(STEEL_INGOT);
        entries.add(UNREFINED_STEEL);
    }

    private static void addItemsToToolsTabGroup(FabricItemGroupEntries entries) {
        entries.add(MIC);
    }





    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(AphidGPT.MOD_ID, name), item);
    }

    public static void registerModItems() {
        AphidGPT.LOGGER.info("Registering items");

        // Register items
        registerItem("mic", MIC);
        registerItem("steel_ingot", STEEL_INGOT);
        registerItem("unrefined_steel", UNREFINED_STEEL);



        // Add items to tabs
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addItemsToToolsTabGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
