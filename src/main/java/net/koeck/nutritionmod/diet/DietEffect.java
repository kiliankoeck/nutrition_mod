package net.koeck.nutritionmod.diet;

import net.koeck.nutritionmod.diet.foodgroups.FoodGroup;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.koeck.nutritionmod.effect.ModEffects;
import net.koeck.nutritionmod.networking.ModMessages;
import net.koeck.nutritionmod.networking.packet.HealthBarSyncS2CPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class DietEffect {

    public enum DietEffectType {
        HEALTH_UP, HEALTH_DOWN
    }

    public static void applyDietEffects(Map<FoodGroup, Integer> foodGroupIntake, Player player, int weightClass) {

        applyWeightClassEffect(weightClass, player);

        int optimalAmtReached = 0;
        for (FoodGroup foodGroup : foodGroupIntake.keySet()) {

            int intakeValue = checkIntakeAmount(foodGroup, foodGroupIntake.get(foodGroup));
            if (intakeValue == 2) {
                optimalAmtReached++;
            }

            matchEffect(foodGroup.effects[intakeValue], player);
        }

        if (optimalAmtReached == FoodGroupList.size()) {
            //Strength
            MobEffectInstance strength = new MobEffectInstance(Objects.requireNonNull(MobEffect.byId(5)), 24000, 0, true, false);
            strength.setCurativeItems(new ArrayList<ItemStack>());
            //Absorption
            MobEffectInstance absorption = new MobEffectInstance(Objects.requireNonNull(MobEffect.byId(22)), 24000, 0, true, false);
            absorption.setCurativeItems(new ArrayList<ItemStack>());
            //Haste
            MobEffectInstance haste = new MobEffectInstance(Objects.requireNonNull(MobEffect.byId(3)), 24000, 0, true, false);
            haste.setCurativeItems(new ArrayList<ItemStack>());

            player.addEffect(strength);
            player.addEffect(absorption);
            player.addEffect(haste);
        }
    }

    public static void applyWeightClassEffect(int weightClass, Player player) {

        player.removeEffect(ModEffects.OVERWEIGHT.get());
        player.removeEffect(ModEffects.OBESITY_ONE.get());
        player.removeEffect(ModEffects.OBESITY_TWO.get());
        player.removeEffect(ModEffects.OBESITY_THREE.get());

        if (weightClass >= 14) {
            MobEffectInstance obesity_three = new MobEffectInstance(ModEffects.OBESITY_THREE.get(), -1, 0, true, false);
            obesity_three.setCurativeItems(new ArrayList<>());
            player.addEffect(obesity_three);
        } else if (weightClass >= 11) {
            MobEffectInstance obesity_two = new MobEffectInstance(ModEffects.OBESITY_TWO.get(), -1, 0, true, false);
            obesity_two.setCurativeItems(new ArrayList<>());
            player.addEffect(obesity_two);
        } else if (weightClass >= 8) {
            MobEffectInstance obesity_one = new MobEffectInstance(ModEffects.OBESITY_ONE.get(), -1, 0, true, false);
            obesity_one.setCurativeItems(new ArrayList<>());
            player.addEffect(obesity_one);
        } else if (weightClass >= 4) {
            MobEffectInstance overweight = new MobEffectInstance(ModEffects.OVERWEIGHT.get(), -1, 0, true, false);
            overweight.setCurativeItems(new ArrayList<>());
            player.addEffect(overweight);
        }


    }

    private static int checkIntakeAmount(FoodGroup foodGroup, int amount) {
        if (amount < foodGroup.dailyPortionAmt && foodGroup.dailyPortionBoundary != Boundary.UPPER) {
            return 0;
        } else if (amount > foodGroup.dailyPortionAmt && foodGroup.dailyPortionBoundary != Boundary.LOWER) {
            return 1;
        } else {
            return 2;
        }
    }

    private static void matchEffect(DietEffectType effect, Player player) {
        if (effect == null) {
            return;
        }
        switch (effect) {
            case HEALTH_DOWN -> applyHealthDown(player);
            case HEALTH_UP -> applyHealthUp(player);
        }
    }

    private static void applyHealthUp(Player player) {
        double random = Math.random();
        if (random <= 0.4 && player.getMaxHealth() < 30) {
            Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier("health_up", 0.5D, AttributeModifier.Operation.ADDITION));
            ModMessages.sendToPlayer(new HealthBarSyncS2CPacket(player.getMaxHealth()), (ServerPlayer) player);
        }
    }

    private static void applyHealthDown(Player player) {
        double random = Math.random();
        if (random <= 0.4 && player.getMaxHealth() > 10) {
            Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier("health_down", -1.0D, AttributeModifier.Operation.ADDITION));
            ModMessages.sendToPlayer(new HealthBarSyncS2CPacket(player.getMaxHealth()), (ServerPlayer) player);
        }
    }

}
