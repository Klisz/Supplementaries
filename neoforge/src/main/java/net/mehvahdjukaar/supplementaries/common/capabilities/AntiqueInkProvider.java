package net.mehvahdjukaar.supplementaries.common.capabilities;

import net.mehvahdjukaar.supplementaries.common.block.IAntiquable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

//actual capability provider (which provides itself as a cap instance)
public class AntiqueInkProvider implements IAntiquable, ICapabilitySerializable<CompoundTag> {

    private boolean hasAntiqueInk = false;

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, Direction facing) {
        return capability == CapabilityHandler.ANTIQUE_TEXT_CAP ?
                LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("ink", this.hasAntiqueInk);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.hasAntiqueInk = tag.getBoolean("ink");
    }

    @Override
    public boolean supplementaries$isAntique() {
        return this.hasAntiqueInk;
    }

    @Override
    public void supplementaries$setAntique(boolean hasInk) {
        this.hasAntiqueInk = hasInk;
    }
}
