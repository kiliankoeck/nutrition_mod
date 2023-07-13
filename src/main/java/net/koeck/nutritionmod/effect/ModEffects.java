package net.koeck.nutritionmod.effect;

import net.koeck.nutritionmod.NutritionMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, NutritionMod.MOD_ID);

    public static final RegistryObject<MobEffect> NOURISHED = MOB_EFFECTS.register("nourished", () -> new NourishedEffect(MobEffectCategory.BENEFICIAL, 0x00FFFFFF));
    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
