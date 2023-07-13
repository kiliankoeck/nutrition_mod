package net.koeck.nutritionmod.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {

    public static final String KEY_CATEGORY_NUTRITION = "key.category.nutritionmod.nutrition";
    public static final String KEY_OPEN_DIETGUI = "key.tutorial.open_gui";

    public static final KeyMapping GUI_KEY = new KeyMapping(KEY_OPEN_DIETGUI, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_N, KEY_CATEGORY_NUTRITION);
}
