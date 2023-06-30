package net.koeck.nutritionmod.client;

import net.koeck.nutritionmod.diet.foodgroups.FoodGroup;

import java.util.Map;

public class ClientDietData {

    private static Map<FoodGroup, Integer> consumedFoodGroups;
    private static Double consumedCalories;

    public static void setFoodGroup(FoodGroup foodGroup, int value){
        consumedFoodGroups.put(foodGroup, value);
    }

    public static Map<FoodGroup, Integer> getConsumedFoodGroups() {
        return consumedFoodGroups;
    }

    public static Double getConsumedCalories() {
        return consumedCalories;
    }

    public static void setConsumedCalories(Double consumedCalories) {
        ClientDietData.consumedCalories = consumedCalories;
    }
}
