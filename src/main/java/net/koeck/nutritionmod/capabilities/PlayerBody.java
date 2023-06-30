package net.koeck.nutritionmod.capabilities;

import net.minecraft.nbt.CompoundTag;

public class PlayerBody {

    //TODO: move to PlayerDiet
    //in g
    private Double bodyWeight = 5800D;
    //TODO: can be individual? Maybe config file
    //in cm
    private final double HEIGHT = 175;
    private final int AGE = 20;
    private final boolean IS_MALE = true;

    // calculated by formula 450g / 3500 (3500kcal roughly equal 0.45kg bodyweight)
    private final double WEIGH_GAIN_COEFFICIENT = 0.12857142857;

    public PlayerBody() {

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

    public double addBodyWeightFromCalories(Double consumedCalories) {
        double caloricDifference = calculateTotalEnergyExpenditure() - consumedCalories;
        addBodyWeight(Math.max(caloricDifference * WEIGH_GAIN_COEFFICIENT, 0.0));
        return bodyWeight;
    }

    //Source: https://onlinelibrary.wiley.com/doi/10.1111/j.1467-789X.2006.00249.x
    private double calculateTotalEnergyExpenditure () {
        if (IS_MALE) {
            return 64 - 9.72 * AGE + 1.54 * (14.2 * bodyWeight/1000 + 503 * HEIGHT);
        } else {
            return 387 - 7.31 * AGE + 1.45 * (10.9 * bodyWeight/1000 + 660.7 * HEIGHT);
        }
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putDouble("body_weight", bodyWeight);
    }


    public void loadNBTData(CompoundTag nbt) {
        bodyWeight = nbt.getDouble("body_weight");
    }

    @Override
    public String toString() {
       return bodyWeight.toString();
    }
}
