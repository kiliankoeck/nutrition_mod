package net.koeck.nutritionmod.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class HealthBarSyncS2CPacket {

    private final float maxHealth;

    public HealthBarSyncS2CPacket(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public HealthBarSyncS2CPacket(FriendlyByteBuf buf) {
        maxHealth = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(maxHealth);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if(player != null) {
                //TODO: check why sometimes health bar doesnt adjust accordingly
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);

                float currentHealth = player.getHealth();
                player.setHealth(Math.min(currentHealth, maxHealth));
            }
        });
        return true;
    }

}