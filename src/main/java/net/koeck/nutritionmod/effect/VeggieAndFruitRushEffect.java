package net.koeck.nutritionmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VeggieAndFruitRushEffect extends MobEffect {
    protected VeggieAndFruitRushEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }

    public void removeAttributeModifiers(LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        Objects.requireNonNull(pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifiers();
        Objects.requireNonNull(pLivingEntity.getAttribute(Attributes.ATTACK_DAMAGE)).removeModifiers();
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    public void addAttributeModifiers(LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        Objects.requireNonNull(pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED)).addPermanentModifier(new AttributeModifier("speed_up", 0.3, AttributeModifier.Operation.MULTIPLY_TOTAL));
        Objects.requireNonNull(pLivingEntity.getAttribute(Attributes.ATTACK_DAMAGE)).addPermanentModifier(new AttributeModifier("attack_damage_up", 0.3, AttributeModifier.Operation.MULTIPLY_TOTAL));
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }
}