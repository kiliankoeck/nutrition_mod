package net.koeck.nutritionmod.diet.foodgroups;

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
    public Map<String, Double> foods = new HashMap<>();
    //effects for underconsumption, overconsumption and adequate consumption
    public String[] effects = new String[3];
}
