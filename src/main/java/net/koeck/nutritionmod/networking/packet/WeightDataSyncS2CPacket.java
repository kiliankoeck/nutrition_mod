package net.koeck.nutritionmod.networking.packet;

import net.koeck.nutritionmod.client.ClientDietData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WeightDataSyncS2CPacket {

    private final double weightValue;

    public WeightDataSyncS2CPacket(double weightValue) {
        this.weightValue = weightValue;
    }

    public WeightDataSyncS2CPacket(FriendlyByteBuf buf) {
        weightValue = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(weightValue);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientDietData.setBodyWeight( weightValue);
        });
        return true;
    }

}