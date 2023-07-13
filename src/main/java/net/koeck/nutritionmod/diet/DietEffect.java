package net.koeck.nutritionmod.diet;

import net.koeck.nutritionmod.diet.foodgroups.FoodGroup;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.koeck.nutritionmod.networking.ModMessages;
import net.koeck.nutritionmod.networking.packet.HealthBarSyncS2CPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class DietEffect {

    public enum DietEffectType {
        HEALTH_UP,
        HEALTH_DOWN
    }

    public static void applyDietEffects(Map<FoodGroup, Integer> foodGroupIntake, Player player, double bmi) {
        double slowDownCoefficient = 0;
        if (bmi >= 40 ) {
            slowDownCoefficient = -0.5;
        } else if (bmi >= 35) {
            slowDownCoefficient = -0.4;
        }else if (bmi >= 30) {
            slowDownCoefficient = -0.3;
        } else if (bmi >= 25) {
            slowDownCoefficient = -0.2;
        }

        Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifiers();
        Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).addPermanentModifier(new AttributeModifier("speed_down",  slowDownCoefficient, AttributeModifier.Operation.MULTIPLY_TOTAL));

        int optimalAmtReached = 0;
        for (FoodGroup foodGroup : foodGroupIntake.keySet()) {

            int intakeValue = checkIntakeAmount(foodGroup, foodGroupIntake.get(foodGroup));
            if (intakeValue == 2) {
                optimalAmtReached++;
            }

            matchEffect(foodGroup.effects[intakeValue], player);
        }

        if(optimalAmtReached == FoodGroupList.size()) {
            //Strength
            MobEffectInstance strength = new MobEffectInstance(Objects.requireNonNull(MobEffect.byId(5)), 24000, 0, true, true);
            strength.setCurativeItems(new ArrayList<ItemStack>());
            //Absorption
            MobEffectInstance absorption = new MobEffectInstance(Objects.requireNonNull(MobEffect.byId(22)), 24000, 0, true, true);
            absorption.setCurativeItems(new ArrayList<ItemStack>());
            //Haste
            MobEffectInstance haste = new MobEffectInstance(Objects.requireNonNull(MobEffect.byId(3)), 24000, 0, true, true);
            haste.setCurativeItems(new ArrayList<ItemStack>());

            player.addEffect(strength);
            player.addEffect(absorption);
            player.addEffect(haste);

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

    private static void applyHealthUp (Player player) {
        double random = Math.random();
        if(random <= 0.8 && player.getMaxHealth() < 30) {
            Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier("health_up",  0.5D, AttributeModifier.Operation.ADDITION));
            ModMessages.sendToPlayer(new HealthBarSyncS2CPacket(player.getMaxHealth()), (ServerPlayer) player);
        }
    }

    private static void applyHealthDown (Player player) {
        double random = Math.random();
        if(random <= 0.8 && player.getMaxHealth() > 10) {
            Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).addPermanentModifier(new AttributeModifier("health_down",  -1.0D, AttributeModifier.Operation.ADDITION));
            ModMessages.sendToPlayer(new HealthBarSyncS2CPacket(player.getMaxHealth()), (ServerPlayer) player);
        }
    }

}
