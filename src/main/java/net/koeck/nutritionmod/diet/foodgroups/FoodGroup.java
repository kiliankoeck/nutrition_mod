package net.koeck.nutritionmod.diet.foodgroups;

import net.koeck.nutritionmod.diet.Boundary;
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
    public Boundary dailyPortionBoundary;

    //Double[0] = kcal/100g, Double[1] = g (for simplicity 1ml liquid = 1g)
    public Map<ItemStack, Double[]> foods = new HashMap<>();
    public DietEffectType[] effects = new DietEffectType[3];
}
