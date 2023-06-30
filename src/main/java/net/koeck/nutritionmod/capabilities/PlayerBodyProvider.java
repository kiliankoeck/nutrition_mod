package net.koeck.nutritionmod.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerBodyProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerBody> PLAYER_BODY = CapabilityManager.get(new CapabilityToken<PlayerBody>() { });

    private PlayerBody body = null;
    private final LazyOptional<PlayerBody> optional = LazyOptional.of(this::createPlayerBody);

    private PlayerBody createPlayerBody() {
        if(this.body == null) {
            this.body = new PlayerBody();
        }

        return this.body;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_BODY) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerBody().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerBody().loadNBTData(nbt);
    }
}
