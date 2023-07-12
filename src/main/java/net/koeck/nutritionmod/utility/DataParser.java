package net.koeck.nutritionmod.utility;

import net.koeck.nutritionmod.diet.Boundary;
import net.koeck.nutritionmod.diet.DietEffect.DietEffectType;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroup;
import net.koeck.nutritionmod.diet.foodgroups.JsonFoodGroup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DataParser {

    public static List<FoodGroup> parseFoodGroup(List<JsonFoodGroup> jsonFoodGroups) {
        List<FoodGroup> foodGroups = new ArrayList<>();

        for (JsonFoodGroup foodGroupRaw : jsonFoodGroups) {

            FoodGroup foodGroup = new FoodGroup();

            try {
                foodGroup.name = foodGroupRaw.name;
                foodGroup.icon = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(foodGroupRaw.icon)));
                foodGroup.color = Integer.parseUnsignedInt("ff" + foodGroupRaw.color, 16);
                foodGroup.dailyPortionAmt = foodGroupRaw.dailyPortionAmt;
            } catch (NullPointerException e) {
                System.out.println(("Missing or invalid JSON.  A name, icon, and color are required."));
                throw e;
            }

            if (foodGroupRaw.foods != null) {
                for (String foodName : foodGroupRaw.foods.keySet()) {
                    String name = foodName;

                    if (name == null) {
                        System.out.println("There is a null item in the '" + foodGroup.name + "' JSON.  Check for a trailing comma in the file.");
                        throw new NullPointerException("There is a null item in the '" + foodGroup.name + "' JSON.  Check for a trailing comma in the file.");
                    }

                    if (StringUtils.countMatches(foodName, ":") == 2) {
                        name = StringUtils.substringBeforeLast(foodName, ":");
                    }

                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));

                    if (item == null) {
                        continue;
                    }

                    foodGroup.foods.put(new ItemStack(item, 1), foodGroupRaw.foods.get(foodName));
                }
            }

            if (foodGroupRaw.effects != null) {
                for (int i = 0; i < foodGroupRaw.effects.length; i++) {
                    if(!foodGroupRaw.effects[i].isBlank()) {
                        foodGroup.effects[i] = DietEffectType.valueOf(foodGroupRaw.effects[i]);
                    }
                }
            }

            if (foodGroupRaw.dailyPortionBoundary != null) {
                foodGroup.dailyPortionBoundary = Boundary.valueOf(foodGroupRaw.dailyPortionBoundary);
            } else {
                foodGroup.dailyPortionBoundary = Boundary.EQUAL;
            }

            foodGroups.add(foodGroup);
        }
        return foodGroups;
    }
}
