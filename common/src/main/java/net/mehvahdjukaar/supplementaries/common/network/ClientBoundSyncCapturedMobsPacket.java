package net.mehvahdjukaar.supplementaries.common.network;

import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.CapturedMobHandler;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.DataDefinedCatchableMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientBoundSyncCapturedMobsPacket implements Message {

    public static final TypeAndCodec<RegistryFriendlyByteBuf, ClientBoundSyncCapturedMobsPacket> CODEC = Message.makeType(
            Supplementaries.res("s2c_sync_captured_mobs"), ClientBoundSyncCapturedMobsPacket::new);

    protected final Set<DataDefinedCatchableMob> mobSet;
    @Nullable
    protected final DataDefinedCatchableMob fish;

    public ClientBoundSyncCapturedMobsPacket(final Set<DataDefinedCatchableMob> mobMap, @Nullable DataDefinedCatchableMob fish) {
        this.mobSet = mobMap;
        this.fish = fish;
    }

    public ClientBoundSyncCapturedMobsPacket(RegistryFriendlyByteBuf buf) {
        int size = buf.readInt();
        this.mobSet = new HashSet<>();
        for (int i = 0; i < size; i++) {
            var r = DataDefinedCatchableMob.STREAM_CODEC.decode(buf);
            mobSet.add(r);
        }
        if (buf.readBoolean()) {
            var r = DataDefinedCatchableMob.STREAM_CODEC.decode(NbtOps.INSTANCE, tag);
                this.fish = r.result().get();
        } else fish = null;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        List<CompoundTag> tags = new ArrayList<>();
        for (var entry : this.mobSet) {
            if (entry == null) {
                Supplementaries.LOGGER.error("Found a null captured mob property. How??");
                continue; //satefy check
            }
            var r = DataDefinedCatchableMob.CODEC.encodeStart(NbtOps.INSTANCE, entry);
            if (r.result().isPresent()) {
                tags.add((CompoundTag) r.result().get());
            }
        }
        buf.writeInt(tags.size());
        tags.forEach(buf::writeNbt);
        if (fish != null) {
            var r = DataDefinedCatchableMob.CODEC.encodeStart(NbtOps.INSTANCE, fish);
            if (r.result().isPresent()) {
                buf.writeBoolean(true);
                buf.writeNbt((CompoundTag) r.result().get());
                return;
            }
        }
        buf.writeBoolean(false);
    }

    @Override
    public void handle(Context context) {
        //client world
        CapturedMobHandler.acceptClientData(mobSet, fish);
        Supplementaries.LOGGER.info("Synced Captured Mobs settings");
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return CODEC.type();
    }
}
