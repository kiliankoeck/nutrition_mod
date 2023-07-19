package net.koeck.nutritionmod.networking.packet;

import net.koeck.nutritionmod.client.ClientDietData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WeightClassDataSyncS2CPacket {

    private final int weightClassValue;

    public WeightClassDataSyncS2CPacket(int weightClassValue) {
        this.weightClassValue = weightClassValue;
    }

    public WeightClassDataSyncS2CPacket(FriendlyByteBuf buf) {
        weightClassValue = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(weightClassValue);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientDietData.setWeightClass(weightClassValue);
        });
        return true;
    }

}