package net.koeck.nutritionmod.networking.packet;

import net.koeck.nutritionmod.client.ClientDietData;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ProteinDataSyncS2CPacket {

    private final int proteinValue;

    public ProteinDataSyncS2CPacket(int proteinValue) {
        this.proteinValue = proteinValue;
    }

    public ProteinDataSyncS2CPacket(FriendlyByteBuf buf) {
        proteinValue = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(proteinValue);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientDietData.setFoodGroup(FoodGroupList.getByName("protein"), proteinValue);

        });
        return true;
    }

}