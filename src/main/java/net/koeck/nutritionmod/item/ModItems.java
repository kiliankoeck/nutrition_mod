package net.koeck.nutritionmod.item;

import net.koeck.nutritionmod.NutritionMod;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, NutritionMod.MOD_ID);

    public static final RegistryObject<Item> PECAN = ITEMS.register("pecan",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(2f).build())));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
