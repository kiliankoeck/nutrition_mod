package net.koeck.nutritionmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ObesityOneEffect extends MobEffect {
    protected ObesityOneEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return false;
    }

    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifiers();
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(new AttributeModifier("speed_down", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL));
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }
}
