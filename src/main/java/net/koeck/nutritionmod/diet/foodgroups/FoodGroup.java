package net.koeck.nutritionmod.diet.foodgroups;

import net.koeck.nutritionmod.diet.DietEffect.DietEffectType;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

//Food Group object representing a certain food group
public class FoodGroup {
    public String name;
    public ItemStack icon;
    public int color;
    public int dailyPortionAmt;
    public Map<ItemStack, Double> foods = new HashMap<>();
    public DietEffectType[] effects = new DietEffectType[3];
}
