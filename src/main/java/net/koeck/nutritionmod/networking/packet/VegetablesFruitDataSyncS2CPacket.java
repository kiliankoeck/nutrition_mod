package net.koeck.nutritionmod.networking.packet;

import net.koeck.nutritionmod.client.ClientDietData;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class VegetablesFruitDataSyncS2CPacket {

    private final int vegetablesFruitValue;

    public VegetablesFruitDataSyncS2CPacket(int vegetablesFruitValue) {
        this.vegetablesFruitValue = vegetablesFruitValue;
    }

    public VegetablesFruitDataSyncS2CPacket(FriendlyByteBuf buf) {
        vegetablesFruitValue = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(vegetablesFruitValue);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientDietData.setFoodGroup(FoodGroupList.getByName("vegetables and fruit"), vegetablesFruitValue);

        });
        return true;
    }

}