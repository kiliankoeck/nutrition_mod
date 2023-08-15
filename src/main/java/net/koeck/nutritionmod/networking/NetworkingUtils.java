package net.koeck.nutritionmod.networking;

import net.koeck.nutritionmod.diet.foodgroups.FoodGroup;
import net.koeck.nutritionmod.networking.packet.*;
import net.minecraft.server.level.ServerPlayer;

public class NetworkingUtils {

    public static void sendS2CPackage(FoodGroup foodGroup, int value, ServerPlayer player) {
        switch (foodGroup.name) {
            case "dairy":
                ModMessages.sendToPlayer(new DairyDataSyncS2CPacket(value), player);
                break;
            case "drinks":
                ModMessages.sendToPlayer(new DrinksDataSyncS2CPacket(value), player);
                break;
            case "extras":
                ModMessages.sendToPlayer(new ExtrasDataSyncS2CPacket(value), player);
                break;
            case "fat and oils":
                ModMessages.sendToPlayer(new FatOilDataSyncS2CPacket(value), player);
                break;
            case "grain and sides":
                ModMessages.sendToPlayer(new GrainDataSyncS2CPacket(value), player);
                break;
            case "protein":
                ModMessages.sendToPlayer(new ProteinDataSyncS2CPacket(value), player);
                break;
            case "vegetables and fruit":
                ModMessages.sendToPlayer(new VegetablesFruitDataSyncS2CPacket(value), player);
                break;
            default:
                break;
        }
    }
}
