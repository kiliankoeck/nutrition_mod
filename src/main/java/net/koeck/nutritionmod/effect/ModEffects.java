package net.koeck.nutritionmod.effect;

import net.koeck.nutritionmod.NutritionMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, NutritionMod.MOD_ID);

    public static final RegistryObject<MobEffect> OVERWEIGHT = MOB_EFFECTS.register("overweight", () -> new OverweightEffect(MobEffectCategory.HARMFUL, 0x00FFFFFF));
    public static final RegistryObject<MobEffect> OBESITY_ONE = MOB_EFFECTS.register("obesity_one", () -> new ObesityOneEffect(MobEffectCategory.HARMFUL, 0x00FFFFFF));
    public static final RegistryObject<MobEffect> OBESITY_TWO = MOB_EFFECTS.register("obesity_two", () -> new ObesityTwoEffect(MobEffectCategory.HARMFUL, 0x00FFFFFF));
    public static final RegistryObject<MobEffect> OBESITY_THREE = MOB_EFFECTS.register("obesity_three", () -> new ObesityThreeEffect(MobEffectCategory.HARMFUL, 0x00FFFFFF));
    public static final RegistryObject<MobEffect> VEGGIE_AND_FRUIT_RUSH = MOB_EFFECTS.register("veggie_and_fruit_rush", () -> new VeggieAndFruitRushEffect(MobEffectCategory.BENEFICIAL, 0x00FFFFFF));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
