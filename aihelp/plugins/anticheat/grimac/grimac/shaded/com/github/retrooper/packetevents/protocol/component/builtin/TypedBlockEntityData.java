package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.blockentity.BlockEntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TypedBlockEntityData {
   private static final BlockEntityType FALLBACK_TYPE;
   private final BlockEntityType type;
   private final NBTCompound compound;

   public TypedBlockEntityData(NBTCompound compound) {
      this(FALLBACK_TYPE, compound);
   }

   public TypedBlockEntityData(BlockEntityType type, NBTCompound compound) {
      this.type = type;
      this.compound = compound;
   }

   public static TypedBlockEntityData read(PacketWrapper<?> wrapper) {
      BlockEntityType type = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9) ? (BlockEntityType)wrapper.readMappedEntity((IRegistry)BlockEntityTypes.getRegistry()) : FALLBACK_TYPE;
      NBTCompound compound = wrapper.readNBT();
      return new TypedBlockEntityData(type, compound);
   }

   public static void write(PacketWrapper<?> wrapper, TypedBlockEntityData data) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         wrapper.writeMappedEntity(data.type);
      }

      wrapper.writeNBT(data.compound);
   }

   public BlockEntityType getType() {
      return this.type;
   }

   public NBTCompound getCompound() {
      return this.compound;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         TypedBlockEntityData that = (TypedBlockEntityData)obj;
         return !this.type.equals(that.type) ? false : this.compound.equals(that.compound);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.type, this.compound});
   }

   static {
      FALLBACK_TYPE = BlockEntityTypes.FURNACE;
   }
}
