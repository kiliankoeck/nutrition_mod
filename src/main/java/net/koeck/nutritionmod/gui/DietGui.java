package net.koeck.nutritionmod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.koeck.nutritionmod.client.ClientDietData;
import net.koeck.nutritionmod.diet.Boundary;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroup;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Map;

public class DietGui extends Screen {

    public DietGui() {
        super(Component.literal("Diet"));
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);


        Map<FoodGroup, Integer> consumedFoodGroups = ClientDietData.getConsumedFoodGroups();
        if (consumedFoodGroups != null) {
            int y = 20;
            for (FoodGroup foodGroup : consumedFoodGroups.keySet()) {
                System.out.println(foodGroup.name);
                ItemStack icon = foodGroup.icon;
                int consumedAmount = consumedFoodGroups.get(foodGroup);
                int dailyPortionAmt = foodGroup.dailyPortionAmt;
                Boundary dailyPortionBoundary = foodGroup.dailyPortionBoundary;
                int textColor;

                if (consumedAmount > dailyPortionAmt && dailyPortionBoundary != Boundary.LOWER) {
                    textColor = Color.RED.getRGB();
                } else if (consumedAmount >= dailyPortionAmt || dailyPortionBoundary == Boundary.UPPER) {
                    textColor = Color.GREEN.getRGB();
                } else {
                    textColor = Color.DARK_GRAY.getRGB();
                }

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                font.drawShadow(poseStack, String.format("%s", foodGroup.name), 10, y, Color.WHITE.getRGB());
                y += 10;
                fill(poseStack, 10, y, 10 + 16, y + 16, Color.LIGHT_GRAY.getRGB());
                itemRenderer.renderGuiItem(poseStack, icon, 10, y);
                String boundary = dailyPortionBoundary == Boundary.UPPER ? "<=" : dailyPortionBoundary == Boundary.LOWER ? ">=" : "";
                font.drawShadow(poseStack, String.format("%d / %s%d", consumedAmount, boundary, dailyPortionAmt), 30, y + 6, textColor);

                y += 20;
            }

            y = 20;
            Double consumedCalories = ClientDietData.getConsumedCalories();
            if (consumedCalories != null) {
                font.drawShadow(poseStack, String.format("%s: %.2fkcal", "consumed calories", consumedCalories), 240, y + 6, Color.CYAN.getRGB());
            }



            y += 20;
            int weightClass = ClientDietData.getWeightClass();
            font.drawShadow(poseStack, String.format("%s: %d", "weight class", weightClass), 240, y + 6, Color.CYAN.getRGB());

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

            y += 20;
            font.drawShadow(poseStack, String.format("%s: %f", "TEE", tee), 240, y + 6, Color.CYAN.getRGB());

        }
    }
}