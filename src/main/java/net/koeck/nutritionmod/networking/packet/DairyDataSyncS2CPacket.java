package net.koeck.nutritionmod.networking.packet;

import net.koeck.nutritionmod.client.ClientDietData;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DairyDataSyncS2CPacket {

    private final int dairyValue;

    public DairyDataSyncS2CPacket(int dairyValue) {
        this.dairyValue = dairyValue;
    }

    public DairyDataSyncS2CPacket(FriendlyByteBuf buf) {
        dairyValue = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(dairyValue);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientDietData.setFoodGroup(FoodGroupList.getByName("dairy"), dairyValue);
        });
        return true;
    }

}