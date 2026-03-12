package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ParticleBlockStateData extends ParticleData implements LegacyConvertible {
   private WrappedBlockState blockState;

   public ParticleBlockStateData(WrappedBlockState blockState) {
      this.blockState = blockState;
   }

   public WrappedBlockState getBlockState() {
      return this.blockState;
   }

   public void setBlockState(WrappedBlockState blockState) {
      this.blockState = blockState;
   }

   public static ParticleBlockStateData read(PacketWrapper<?> wrapper) {
      int blockID;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
         blockID = wrapper.readVarInt();
      } else if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
         blockID = wrapper.readInt();
      } else {
         blockID = wrapper.readVarInt();
      }

      return new ParticleBlockStateData(WrappedBlockState.getByGlobalId(wrapper.getServerVersion().toClientVersion(), blockID));
   }

   public static void write(PacketWrapper<?> wrapper, ParticleBlockStateData data) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
         wrapper.writeVarInt(data.getBlockState().getGlobalId());
      } else {
         wrapper.writeInt(data.getBlockState().getGlobalId());
      }

   }

   public static ParticleBlockStateData decode(NBTCompound compound, ClientVersion version) {
      String key = version.isNewerThanOrEquals(ClientVersion.V_1_20_5) ? "block_state" : "value";
      WrappedBlockState state = WrappedBlockState.decode(compound.getTagOrThrow(key), version);
      return new ParticleBlockStateData(state);
   }

   public static void encode(ParticleBlockStateData data, ClientVersion version, NBTCompound compound) {
      String key = version.isNewerThanOrEquals(ClientVersion.V_1_20_5) ? "block_state" : "value";
      compound.setTag(key, WrappedBlockState.encode(data.blockState, version));
   }

   public boolean isEmpty() {
      return false;
   }

   public LegacyParticleData toLegacy(ClientVersion version) {
      return LegacyParticleData.ofOne(this.blockState.getGlobalId());
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         ParticleBlockStateData that = (ParticleBlockStateData)obj;
         return this.blockState.equals(that.blockState);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.blockState);
   }
}
