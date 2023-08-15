package net.koeck.nutritionmod.capabilities;

import net.koeck.nutritionmod.diet.foodgroups.FoodGroup;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDiet {

    private Map<FoodGroup, Integer> consumedFoodGroups = new HashMap<>();
    private Double consumedCalories = 0D;

    public boolean isOvereating() {
        return isOvereating;
    }

    public void setOvereating(boolean overeating) {
        isOvereating = overeating;
    }

    private boolean isOvereating = false;

    //weight classes 1-12
    private int weightClass = 0;

    public PlayerDiet() {
        updateCapability();
    }

    public void changeWeightClass(int amount) {
        while (weightClass + amount > 14) {
            amount--;
        }
        while (weightClass + amount < 0) {
            amount++;
        }
        weightClass = weightClass + amount;
    }

    public int getWeightClass() {
        return weightClass;
    }

    public int addWeightClassFromCalories() {
        int weightEstimate = 61;
        if (weightClass > 13) {
            weightEstimate = 122;
        } else if (weightClass > 10) {
            weightEstimate = 107;
        } else if (weightClass > 7) {
            weightEstimate = 92;
        } else if (weightClass > 4) {
            weightEstimate = 77;
        }
        //Source: https://onlinelibrary.wiley.com/doi/10.1111/j.1467-789X.2006.00249.x
        double tee = 64 - 9.72 * 20 + 1.54 * (14.2 * weightEstimate + 503 * 1.75);
        if (consumedCalories > 1.2 * tee) {
            changeWeightClass(1);
        } else if (consumedCalories < 0.8 * tee) {
            changeWeightClass(-1);
        }
        return weightClass;
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

    public Map<FoodGroup, Integer> getConsumedFoodGroups() {
        return consumedFoodGroups;
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
        for (FoodGroup foodGroup : FoodGroupList.get()) {
            nbt.putInt(foodGroup.name, consumedFoodGroups.get(foodGroup));
        }
        nbt.putDouble("calorie_count", consumedCalories);
        nbt.putInt("weight_class", weightClass);
    }


    public void loadNBTData(CompoundTag nbt) {
        for (FoodGroup foodGroup : FoodGroupList.get()) {
            consumedFoodGroups.put(foodGroup, nbt.getInt(foodGroup.name));
        }
        consumedCalories = nbt.getDouble("calorie_count");
        weightClass = nbt.getInt("weight_class");
    }

}
