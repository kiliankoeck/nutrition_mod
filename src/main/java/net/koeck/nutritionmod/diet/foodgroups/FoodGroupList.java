package net.koeck.nutritionmod.diet.foodgroups;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

//keeps information on all food groups
public class FoodGroupList {
    private static final List<FoodGroup> foodGroups = new ArrayList<>();

    public static void register(List<FoodGroup> foodGroupList) {
        foodGroups.clear();
        foodGroups.addAll(foodGroupList);
    }

    // Get all food groups
    public static List<FoodGroup> get() {
        return foodGroups;
    }

    // Get food groups by name (null if not found)
    public static FoodGroup getByName(String name) {
        for (FoodGroup foodGroup : foodGroups) {
            if (foodGroup.name.equals(name)) {
                return foodGroup;
            }
        }
        return null;
    }

    public static List<ItemStack> getAllAllowedFoods() {
        List<ItemStack> allowedFoods = new ArrayList<>();
        for (FoodGroup foodGroup: foodGroups) {
            allowedFoods.addAll(foodGroup.foods.keySet());
        }
        return allowedFoods;
    }

    public static List<FoodGroup> getConsumedFoodGroups(ItemStack consumedFood) {
        List<FoodGroup> foodGroupsFound = new ArrayList<>();

        // Loop through food groups to look for food
        for (FoodGroup foodGroup : foodGroups) { // All food groups
            // Search foods
            for (ItemStack listedFood : foodGroup.foods.keySet()) {// All foods in that category
                if (listedFood.is(consumedFood.getItem())) {
                    foodGroupsFound.add(foodGroup); // Add nutrient
                    break; // Skip rest of search in this food group, try others
                }
            }
        }
        return foodGroupsFound;
    }

    public static double getFoodCalories(ItemStack consumedFood) {
        // Loop through food groups to look for food
        for (FoodGroup foodGroup : foodGroups) { // All food groups
            // Search foods
            for (ItemStack listedFood : foodGroup.foods.keySet()) {// All foods in that category
                if (listedFood.is(consumedFood.getItem())) {
                    Double[] info = foodGroup.foods.get(listedFood);
                    if (info != null && info.length >= 2) {
                        return (info[0]*info[1])/100;
                    }
                    return 0;
                }
            }
        }
        return 0;
    }

    public static Double[] getFoodInfo(ItemStack consumedFood) {
        // Loop through food groups to look for food
        for (FoodGroup foodGroup : foodGroups) { // All food groups
            // Search foods
            for (ItemStack listedFood : foodGroup.foods.keySet()) {// All foods in that category
                if (listedFood.is(consumedFood.getItem())) {
                    Double[] info = foodGroup.foods.get(listedFood);
                    if (info != null && info.length >= 2) {
                        return info;
                    }
                    return null;
                }
            }
        }
        return null;
    }

    public static int size() {
        return foodGroups.size();
    }
}
