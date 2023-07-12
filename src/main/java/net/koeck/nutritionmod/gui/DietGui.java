package net.koeck.nutritionmod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.koeck.nutritionmod.capabilities.PlayerDiet;
import net.koeck.nutritionmod.capabilities.PlayerDietProvider;
import net.koeck.nutritionmod.client.ClientDietData;
import net.koeck.nutritionmod.diet.Boundary;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroup;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
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
                ItemStack icon = foodGroup.icon;
                int consumedAmount =  consumedFoodGroups.get(foodGroup);
                int dailyPortionAmt = foodGroup.dailyPortionAmt;
                Boundary dailyPortionBoundary = foodGroup.dailyPortionBoundary;
                int textColor;

                if (consumedAmount > dailyPortionAmt && dailyPortionBoundary != Boundary.LOWER) {
                    textColor = Color.RED.getRGB();
                } else if (consumedAmount == dailyPortionAmt || dailyPortionBoundary == Boundary.UPPER) {
                    textColor = Color.GREEN.getRGB();
                } else {
                    textColor = Color.DARK_GRAY.getRGB();
                }

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                fill(poseStack, 10, y, 10 + 16, y + 16, Color.LIGHT_GRAY.getRGB());
                itemRenderer.renderGuiItem(poseStack, icon, 10, y);
                String boundary = dailyPortionBoundary == Boundary.UPPER ? "<=" : dailyPortionBoundary == Boundary.LOWER ? ">=" : "";
                font.drawShadow(poseStack, String.format("%d / %s%d", consumedAmount, boundary, dailyPortionAmt), 30, y + 6, textColor);

                y += 20;
            }
            y += 20;
            Double consumedCalories = ClientDietData.getConsumedCalories();
            if(consumedCalories != null) {
                font.drawShadow(poseStack, String.format("%s: %dkcal", "consumed calories", consumedCalories.intValue() ), 30, y + 6, Color.CYAN.getRGB());
            }
        }
    }
}