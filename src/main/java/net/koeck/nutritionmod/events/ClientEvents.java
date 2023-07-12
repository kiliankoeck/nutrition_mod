package net.koeck.nutritionmod.events;

import net.koeck.nutritionmod.NutritionMod;
import net.koeck.nutritionmod.gui.DietGui;
import net.koeck.nutritionmod.util.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = NutritionMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent event) {
            if (KeyBinding.GUI_KEY.consumeClick()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                        () -> () -> Minecraft.getInstance().setScreen(new DietGui()));
            }
        }

    }

    @Mod.EventBusSubscriber(modid = NutritionMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.GUI_KEY);
        }

    }
}
