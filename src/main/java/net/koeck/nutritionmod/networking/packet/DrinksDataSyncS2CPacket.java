package net.koeck.nutritionmod.networking.packet;

import net.koeck.nutritionmod.client.ClientDietData;
import net.koeck.nutritionmod.diet.foodgroups.FoodGroupList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DrinksDataSyncS2CPacket {

    private final int drinksValue;

    public DrinksDataSyncS2CPacket(int drinksValue) {
        this.drinksValue = drinksValue;
    }

    public DrinksDataSyncS2CPacket(FriendlyByteBuf buf) {
        drinksValue = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(drinksValue);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientDietData.setFoodGroup(FoodGroupList.getByName("drinks"), drinksValue);

        });
        return true;
    }

}