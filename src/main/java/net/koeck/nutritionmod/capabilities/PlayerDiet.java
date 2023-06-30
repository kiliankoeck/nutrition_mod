package net.koeck.nutritionmod.capabilities;

import net.koeck.nutritionmod.diet.foodgroups.FoodGroup;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: maybe refactor name
public class PlayerDiet{

    private Map<FoodGroup, Integer> consumedFoodGroups = new HashMap<>();
    private Double consumedCalories = 0D;

    private Double bodyWeight = 5800D;
    //TODO: can be individual? Maybe config file
    //in cm
    private final double HEIGHT = 175;
    private final int AGE = 20;
    private final boolean IS_MALE = true;

    // calculated by formula 1000g / 7778 (7778 kcal roughly equal 1 kg bodyweight)
    private final double WEIGH_GAIN_COEFFICIENT = 0.1286;

    public PlayerDiet() {
        updateCapability();
    }

    public Double getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(Double bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public void addBodyWeight(Double bodyWeight) {
        this.bodyWeight += bodyWeight;
    }

    public double addBodyWeightFromCalories() {
        double caloricDifference = calculateTotalEnergyExpenditure() - consumedCalories;
        addBodyWeight(Math.max(caloricDifference * WEIGH_GAIN_COEFFICIENT, 0.0));
        return calculateBMI();
    }

    //Source: https://onlinelibrary.wiley.com/doi/10.1111/j.1467-789X.2006.00249.x
    private double calculateTotalEnergyExpenditure () {
        if (IS_MALE) {
            return 64 - 9.72 * AGE + 1.54 * (14.2 * bodyWeight/1000 + 503 * HEIGHT);
        } else {
            return 387 - 7.31 * AGE + 1.45 * (10.9 * bodyWeight/1000 + 660.7 * HEIGHT);
        }
    }

    private double calculateBMI() {
        return bodyWeight / (HEIGHT * HEIGHT);
    }
    
    public Map<FoodGroup, Integer> getFoodGroup() {
        return consumedFoodGroups;
    }

    
    public Integer getFoodGroup(FoodGroup foodgroup) {
        return consumedFoodGroups.get(foodgroup);
    }

    
    public void setFoodGroup(FoodGroup foodgroup, Integer value) {
        consumedFoodGroups.put(foodgroup, value);
    }


    public void setFoodGroup(Map<FoodGroup, Integer> foodgroupData) {
        for (Map.Entry<FoodGroup, Integer> entry : foodgroupData.entrySet())
            consumedFoodGroups.put(entry.getKey(), entry.getValue());
    }

    public Double getConsumedCalories() {
        return consumedCalories;
    }

    public void setConsumedCalories(Double consumedCalories) {
        this.consumedCalories = consumedCalories;
    }

    public void addConsumedCalories(Double consumedCalories) {
        this.consumedCalories += consumedCalories;
    }

    public void addFoodGroup(FoodGroup foodgroup, int amount) {
        int currentAmount = consumedFoodGroups.get(foodgroup);
        consumedFoodGroups.put(foodgroup, currentAmount + amount);
    }

    
    public void addFoodGroup(List<FoodGroup> foodgroupData, int amount) {
        for (FoodGroup foodGroup : foodgroupData) {
            addFoodGroup(foodGroup, 1);
        }
    }
    
    public void reset(FoodGroup foodgroup) {
      setFoodGroup(foodgroup, 0);
    }

    
    public void reset() {
        consumedCalories = 0D;
        for (FoodGroup foodGroup : consumedFoodGroups.keySet()) {
            reset(foodGroup);
        }
    }
    
    public void updateCapability() {
        Map<FoodGroup, Integer> dietOld = new HashMap<>(consumedFoodGroups);
        consumedFoodGroups.clear();
        for (FoodGroup foodGroup : FoodGroupList.get()) {
            for (Map.Entry<FoodGroup, Integer> foodGroupOld : dietOld.entrySet()) {
                if (foodGroup.name.equals(foodGroupOld.getKey().name)) {
                    consumedFoodGroups.put(foodGroup, foodGroupOld.getValue());
                    break;
                }
            }
            consumedFoodGroups.put(foodGroup, 0);
        }
    }

    public void saveNBTData(CompoundTag nbt) {
        for (FoodGroup foodGroup: FoodGroupList.get()){
            nbt.putInt(foodGroup.name, consumedFoodGroups.get(foodGroup));
        }
        nbt.putDouble("calorie_count", consumedCalories);
        nbt.putDouble("body_weight", bodyWeight);
    }


    public void loadNBTData(CompoundTag nbt) {
        for (FoodGroup foodGroup: FoodGroupList.get()){
            consumedFoodGroups.put(foodGroup, nbt.getInt(foodGroup.name));
        }
        consumedCalories = nbt.getDouble("calorie_count");
        bodyWeight = nbt.getDouble("body_weight");
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (FoodGroup foodGroup: consumedFoodGroups.keySet()) {
            res.append(foodGroup.name).append(": ").append(consumedFoodGroups.get(foodGroup)).append("\n");
        }
        String calorieString = "\n" + "consumed calories: " + consumedCalories;
        String weightString = "\n" + "body weight: " + bodyWeight;

        res.append(calorieString);
        res.append(weightString);

        return res.toString();
    }
}
