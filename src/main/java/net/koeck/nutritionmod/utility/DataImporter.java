package net.koeck.nutritionmod.utility;

import com.google.common.collect.Lists;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.koeck.nutritionmod.diet.foodgroups.JsonFoodGroup;
import org.apache.commons.io.FilenameUtils;

import com.google.gson.stream.JsonReader;
import java.io.File;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DataImporter {

    private static final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();

    public static void reload() {
        FoodGroupList.register(DataParser.parseFoodGroup(loadJsonFoodGroup()));
    }

    private static List<JsonFoodGroup> loadJsonFoodGroup() {
        List<String> foodGroupFiles = Lists.newArrayList("dairy.json", "drinks.json", "extras.json", "grain.json", "protein.json", "vegetables_fruit.json", "fat_oils.json");
        File foodGroupDirectory = new File(Config.configDirectory, "diet");
        createConfigurationDirectory("assets/nutritionmod/configs/foodgroups", foodGroupDirectory, foodGroupFiles);
        return readConfigurationDirectory(JsonFoodGroup.class, foodGroupDirectory);
    }


    // Copies files from internal resources to external files.  Accepts an input resource path, output directory, and list of files
    private static void createConfigurationDirectory(String inputDirectory, File outputDirectory, List<String> files) {
        // Make no changes if directory already exists
        if (outputDirectory.exists())
            return;

        // Create config directory
        outputDirectory.mkdir();

        // Copy each file over
        ClassLoader loader = Thread.currentThread().getContextClassLoader(); // Can access resources via class loader
        for (String file : files) {
            try (InputStream inputStream = loader.getResourceAsStream(inputDirectory + "/" + file)) {// Get input stream of resource
                Files.copy(inputStream, new File(outputDirectory + "/" + file).toPath()); // Create files from stream
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Reads in JSON as objects.  Accepts object to serialize into, and directory to read json files.  Returns array of JSON objects.
    private static <T> List<T> readConfigurationDirectory(Class<T> classImport, File configDirectory) {
        File[] files = configDirectory.listFiles(); // List json files
        List<T> jsonObjectList = new ArrayList<>(); // List json objects

        for (File file : files) {
            if (FilenameUtils.isExtension(file.getName(), "json")) {
                try {
                    JsonReader jsonReader = new JsonReader(new FileReader(file)); // Read in JSON
                    jsonObjectList.add(gson.fromJson(jsonReader, classImport)); // Deserialize with GSON and store for later processing
                } catch (IOException | com.google.gson.JsonSyntaxException e) {
                    System.out.println("The file " + file.getName() + " has invalid JSON and could not be loaded.");
                    throw new IllegalArgumentException("Unable to load " + file.getName() + ".  Is the JSON valid?", e);
                }
            }
        }

        return jsonObjectList;
    }
}

