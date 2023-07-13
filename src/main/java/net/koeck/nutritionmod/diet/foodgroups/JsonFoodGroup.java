package net.koeck.nutritionmod.diet.foodgroups;

import net.koeck.nutritionmod.diet.Boundary;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Json representation of a food group
public class JsonFoodGroup {
    public String name;
    public String icon;
    public String color;
    public int dailyPortionAmt;
    public String dailyPortionBoundary;
    //Double[0] = kcal/100g, Double[1] = g (for simplicity 1ml liquid = 1g)
    public Map<String, Double[]> foods = new HashMap<>();
    //effects for underconsumption, overconsumption and adequate consumption
    public String[] effects = new String[3];
}
