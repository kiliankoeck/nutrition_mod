package net.koeck.nutritionmod.client;

import net.koeck.nutritionmod.diet.foodgroups.FoodGroup;

import java.util.HashMap;
import java.util.Map;

public class ClientDietData {

    private static Map<FoodGroup, Integer> consumedFoodGroups = new HashMap<>();
    private static Double consumedCalories;
    private static int weightClass = 1;

    public static int getWeightClass() {
        return weightClass;
    }

    public static void setWeightClass(int weightClass) {
        ClientDietData.weightClass = weightClass;
    }


    public static void setFoodGroup(FoodGroup foodGroup, int value) {
        consumedFoodGroups.put(foodGroup, value);
    }

    public static Map<FoodGroup, Integer> getConsumedFoodGroups() {
        return consumedFoodGroups;
    }

    public static Double getConsumedCalories() {
        return consumedCalories;
    }

    public static void setConsumedCalories(Double calories) {
        consumedCalories = calories;
    }
}
