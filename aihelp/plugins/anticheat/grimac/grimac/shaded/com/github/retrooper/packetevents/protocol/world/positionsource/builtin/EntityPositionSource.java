package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.PositionSource;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.PositionSourceTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.UniqueIdUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class EntityPositionSource extends PositionSource {
   private static final UUID EMPTY_UNIQUE_ID = new UUID(0L, 0L);
   @Nullable
   private UUID entityUniqueId;
   private int entityId;
   private float offsetY;

   public EntityPositionSource(int entityId) {
      this(entityId, 0.0F);
   }

   public EntityPositionSource(int entityId, float offsetY) {
      super(PositionSourceTypes.ENTITY);
      this.entityId = entityId;
      this.offsetY = offsetY;
   }

   public EntityPositionSource(Optional<UUID> entityUniqueId, float offsetY) {
      super(PositionSourceTypes.ENTITY);
      this.entityUniqueId = (UUID)entityUniqueId.orElse((Object)null);
      this.offsetY = offsetY;
   }

   public static EntityPositionSource read(PacketWrapper<?> wrapper) {
      int entityId = wrapper.readVarInt();
      float offsetY = 0.0F;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19)) {
         offsetY = wrapper.readFloat();
      }

      return new EntityPositionSource(entityId, offsetY);
   }

   public static void write(PacketWrapper<?> wrapper, EntityPositionSource source) {
      wrapper.writeVarInt(source.entityId);
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19)) {
         wrapper.writeFloat(source.offsetY);
      }

   }

   public static EntityPositionSource decodeSource(NBTCompound compound, ClientVersion version) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_19)) {
         int[] entityUniqueIdArr = ((NBTIntArray)compound.getTagOfTypeOrThrow("source_entity", NBTIntArray.class)).getValue();
         UUID entityUniqueId = UniqueIdUtil.fromIntArray(entityUniqueIdArr);
         NBTNumber offsetYTag = compound.getNumberTagOrNull("y_offset");
         float offsetY = offsetYTag == null ? 0.0F : offsetYTag.getAsFloat();
         return new EntityPositionSource(Optional.of(entityUniqueId), offsetY);
      } else {
         int entityId = compound.getNumberTagOrThrow("source_entity_id").getAsInt();
         return new EntityPositionSource(entityId);
      }
   }

   public static void encodeSource(EntityPositionSource source, ClientVersion version, NBTCompound compound) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_19)) {
         UUID uniqueId = source.entityUniqueId != null ? source.entityUniqueId : EMPTY_UNIQUE_ID;
         compound.setTag("source_entity", new NBTIntArray(UniqueIdUtil.toIntArray(uniqueId)));
         compound.setTag("y_offset", new NBTFloat(source.offsetY));
      } else {
         compound.setTag("source_entity_id", new NBTInt(source.entityId));
      }

   }

   public Optional<UUID> getEntityUniqueId() {
      return Optional.ofNullable(this.entityUniqueId);
   }

   public void setEntityUniqueId(Optional<UUID> entityUniqueId) {
      this.entityUniqueId = (UUID)entityUniqueId.orElse((Object)null);
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public float getOffsetY() {
      return this.offsetY;
   }

   public void setOffsetY(float offsetY) {
      this.offsetY = offsetY;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         EntityPositionSource that = (EntityPositionSource)obj;
         if (this.entityId != that.entityId) {
            return false;
         } else {
            return Float.compare(that.offsetY, this.offsetY) != 0 ? false : Objects.equals(this.entityUniqueId, that.entityUniqueId);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.entityUniqueId, this.entityId, this.offsetY});
   }
}
