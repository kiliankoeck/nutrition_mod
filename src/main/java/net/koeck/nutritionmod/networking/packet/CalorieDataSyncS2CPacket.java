package net.koeck.nutritionmod.networking.packet;

import net.koeck.nutritionmod.client.ClientDietData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CalorieDataSyncS2CPacket {

    private final double calorieValue;

    public CalorieDataSyncS2CPacket(double calorieValue) {
        this.calorieValue = calorieValue;
    }

    public CalorieDataSyncS2CPacket(FriendlyByteBuf buf) {
        calorieValue = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(calorieValue);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientDietData.setConsumedCalories( calorieValue);
        });
        return true;
    }

}