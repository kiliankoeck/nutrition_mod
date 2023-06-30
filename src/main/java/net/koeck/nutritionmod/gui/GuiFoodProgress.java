package net.koeck.nutritionmod.gui;

import net.koeck.nutritionmod.NutritionMod;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GuiFoodProgress extends Screen {

    private static final ResourceLocation BACKGROUND =
        new ResourceLocation("minecraft", "textures/gui/demo_background.png");
    private static final ResourceLocation ICONS =
        new ResourceLocation(NutritionMod.MOD_ID, "textures/gui/icons.png");

    private final int xSize;
    private int ySize;
    private final boolean fromInventory;

    protected GuiFoodProgress(boolean fromInventory) {
        super(Component.translatable("gui." + NutritionMod.MOD_ID + ".title"));
        this.xSize = 248;
        this.fromInventory = fromInventory;
    }




}
