package net.koeck.nutritionmod.networking.packet;

import net.koeck.nutritionmod.client.ClientDietData;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GrainDataSyncS2CPacket {

    private final int grainValue;

    public GrainDataSyncS2CPacket(int grainValue) {
        this.grainValue = grainValue;
    }

    public GrainDataSyncS2CPacket(FriendlyByteBuf buf) {
        grainValue = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(grainValue);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientDietData.setFoodGroup(FoodGroupList.getByName("grain"), grainValue);

        });
        return true;
    }

}