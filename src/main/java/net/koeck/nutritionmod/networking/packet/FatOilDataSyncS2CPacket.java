package net.koeck.nutritionmod.networking.packet;

import net.koeck.nutritionmod.client.ClientDietData;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FatOilDataSyncS2CPacket {

    private final int fatOilsValue;

    public FatOilDataSyncS2CPacket(int fatOilsValue) {
        this.fatOilsValue = fatOilsValue;
    }

    public FatOilDataSyncS2CPacket(FriendlyByteBuf buf) {
        fatOilsValue = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(fatOilsValue);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientDietData.setFoodGroup(FoodGroupList.getByName("fat and oils"), fatOilsValue);

        });
        return true;
    }

}