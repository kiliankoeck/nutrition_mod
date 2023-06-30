package net.koeck.nutritionmod.utility;

import java.io.File;

public class Config {
    public static File configDirectory;

    public static void registerConfigs(File configDirectory) {
        Config.configDirectory = configDirectory;

    }
}
