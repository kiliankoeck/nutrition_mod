package net.koeck.nutritionmod.networking.packet;

import net.koeck.nutritionmod.client.ClientDietData;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExtrasDataSyncS2CPacket {

    private final int extrasValue;

    public ExtrasDataSyncS2CPacket(int extrasValue) {
        this.extrasValue = extrasValue;
    }

    public ExtrasDataSyncS2CPacket(FriendlyByteBuf buf) {
        extrasValue = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(extrasValue);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientDietData.setFoodGroup(FoodGroupList.getByName("extras"), extrasValue);

        });
        return true;
    }

}