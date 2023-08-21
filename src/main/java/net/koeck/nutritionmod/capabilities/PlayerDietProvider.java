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

public class PlayerDietProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerDiet> PLAYER_DIET = CapabilityManager.get(new CapabilityToken<PlayerDiet>() { });

    private PlayerDiet diet = null;
    private final LazyOptional<PlayerDiet> optional = LazyOptional.of(this::createPlayerDiet);

    private PlayerDiet createPlayerDiet() {
        if(this.diet == null) {
            this.diet = new PlayerDiet();
        }

        return this.diet;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_DIET) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }


    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == PLAYER_DIET)
            return optional.cast();
        else
            return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerDiet().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerDiet().loadNBTData(nbt);
    }
}
