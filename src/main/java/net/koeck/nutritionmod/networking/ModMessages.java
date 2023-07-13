package net.koeck.nutritionmod.networking;

import net.koeck.nutritionmod.NutritionMod;
import net.koeck.nutritionmod.networking.packet.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

//handles messages between client and server to sync them
public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(NutritionMod.MOD_ID, "messages"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(DairyDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(DairyDataSyncS2CPacket::new)
            .encoder(DairyDataSyncS2CPacket::toBytes)
            .consumerMainThread(DairyDataSyncS2CPacket::handle)
            .add();

        net.messageBuilder(DrinksDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(DrinksDataSyncS2CPacket::new)
            .encoder(DrinksDataSyncS2CPacket::toBytes)
            .consumerMainThread(DrinksDataSyncS2CPacket::handle)
            .add();

        net.messageBuilder(ExtrasDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ExtrasDataSyncS2CPacket::new)
            .encoder(ExtrasDataSyncS2CPacket::toBytes)
            .consumerMainThread(ExtrasDataSyncS2CPacket::handle)
            .add();

        net.messageBuilder(FatOilDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(FatOilDataSyncS2CPacket::new)
            .encoder(FatOilDataSyncS2CPacket::toBytes)
            .consumerMainThread(FatOilDataSyncS2CPacket::handle)
            .add();

        net.messageBuilder(GrainDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(GrainDataSyncS2CPacket::new)
            .encoder(GrainDataSyncS2CPacket::toBytes)
            .consumerMainThread(GrainDataSyncS2CPacket::handle)
            .add();

        net.messageBuilder(ProteinDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(ProteinDataSyncS2CPacket::new)
            .encoder(ProteinDataSyncS2CPacket::toBytes)
            .consumerMainThread(ProteinDataSyncS2CPacket::handle)
            .add();

        net.messageBuilder(VegetablesFruitDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(VegetablesFruitDataSyncS2CPacket::new)
            .encoder(VegetablesFruitDataSyncS2CPacket::toBytes)
            .consumerMainThread(VegetablesFruitDataSyncS2CPacket::handle)
            .add();

        net.messageBuilder(HealthBarSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(HealthBarSyncS2CPacket::new)
            .encoder(HealthBarSyncS2CPacket::toBytes)
            .consumerMainThread(HealthBarSyncS2CPacket::handle)
            .add();

        net.messageBuilder(CalorieDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(CalorieDataSyncS2CPacket::new)
            .encoder(CalorieDataSyncS2CPacket::toBytes)
            .consumerMainThread(CalorieDataSyncS2CPacket::handle)
            .add();

        net.messageBuilder(WeightDataSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(WeightDataSyncS2CPacket::new)
            .encoder(WeightDataSyncS2CPacket::toBytes)
            .consumerMainThread(WeightDataSyncS2CPacket::handle)
            .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}