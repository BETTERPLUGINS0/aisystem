package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTIntArray;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.PositionSource;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.PositionSourceType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.PositionSourceTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.builtin.BlockPositionSource;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.positionsource.builtin.EntityPositionSource;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.Optional;

public class ParticleVibrationData extends ParticleData {
   private Vector3i startingPosition;
   private PositionSource source;
   private int ticks;

   public ParticleVibrationData(@Nullable Vector3i startingPos, Vector3i blockPosition, int ticks) {
      this(startingPos, (PositionSource)(new BlockPositionSource(blockPosition)), ticks);
   }

   public ParticleVibrationData(@Nullable Vector3i startingPos, int entityId, int ticks) {
      this(startingPos, (PositionSource)(new EntityPositionSource(entityId)), ticks);
   }

   public ParticleVibrationData(@Nullable Vector3i startingPos, int entityId, float entityEyeHeight, int ticks) {
      this(startingPos, (PositionSource)(new EntityPositionSource(entityId, entityEyeHeight)), ticks);
   }

   public ParticleVibrationData(PositionSource source, int ticks) {
      this((Vector3i)null, (PositionSource)source, ticks);
   }

   public ParticleVibrationData(@Nullable Vector3i startingPos, PositionSource source, int ticks) {
      this.startingPosition = startingPos;
      this.source = source;
      this.ticks = ticks;
   }

   public static ParticleVibrationData read(PacketWrapper<?> wrapper) {
      Vector3i startingPos = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_4) ? Vector3i.zero() : wrapper.readBlockPosition();
      PositionSourceType sourceType;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         sourceType = (PositionSourceType)wrapper.readMappedEntity(PositionSourceTypes::getById);
      } else {
         String sourceTypeName = wrapper.readString();
         sourceType = PositionSourceTypes.getByName(sourceTypeName);
         if (sourceType == null) {
            throw new IllegalArgumentException("Illegal position type: " + sourceTypeName);
         }
      }

      PositionSource source = sourceType.read(wrapper);
      int ticks = wrapper.readVarInt();
      return new ParticleVibrationData(startingPos, source, ticks);
   }

   public static void write(PacketWrapper<?> wrapper, ParticleVibrationData data) {
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_19_4)) {
         wrapper.writeBlockPosition(data.getStartingPosition());
      }

      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
         wrapper.writeMappedEntity(data.getSourceType());
      } else {
         wrapper.writeIdentifier(data.getType().getName());
      }

      PositionSourceType<PositionSource> sourceType = data.getSourceType();
      sourceType.write(wrapper, data.getSource());
      wrapper.writeVarInt(data.getTicks());
   }

   public static ParticleVibrationData decode(NBTCompound compound, ClientVersion version) {
      Vector3i origin = version.isNewerThanOrEquals(ClientVersion.V_1_19) ? null : new Vector3i(((NBTIntArray)compound.getTagOfTypeOrThrow("origin", NBTIntArray.class)).getValue());
      PositionSource destination = PositionSource.decode(compound.getCompoundTagOrThrow("destination"), version);
      int arrivalInTicks = compound.getNumberTagOrThrow("arrival_in_ticks").getAsInt();
      return new ParticleVibrationData(origin, destination, arrivalInTicks);
   }

   public static void encode(ParticleVibrationData data, ClientVersion version, NBTCompound compound) {
      if (version.isOlderThan(ClientVersion.V_1_19)) {
         Vector3i startPos = data.getStartingPosition();
         if (startPos != null) {
            compound.setTag("origin", new NBTIntArray(new int[]{startPos.x, startPos.y, startPos.z}));
         }
      }

      compound.setTag("destination", PositionSource.encode(data.source, version));
      compound.setTag("arrival_in_ticks", new NBTInt(data.ticks));
   }

   @ApiStatus.Obsolete
   public Vector3i getStartingPosition() {
      return this.startingPosition;
   }

   @ApiStatus.Obsolete
   public void setStartingPosition(Vector3i startingPosition) {
      this.startingPosition = startingPosition;
   }

   /** @deprecated */
   @Deprecated
   public ParticleVibrationData.PositionType getType() {
      return ParticleVibrationData.PositionType.byModern(this.source.getType());
   }

   public PositionSourceType<?> getSourceType() {
      return this.source.getType();
   }

   public PositionSource getSource() {
      return this.source;
   }

   public Optional<Vector3i> getBlockPosition() {
      return this.source instanceof BlockPositionSource ? Optional.of(((BlockPositionSource)this.source).getPos()) : Optional.empty();
   }

   public void setBlockPosition(Vector3i blockPosition) {
      this.source = new BlockPositionSource(blockPosition);
   }

   public Optional<Integer> getEntityId() {
      return this.source instanceof EntityPositionSource ? Optional.of(((EntityPositionSource)this.source).getEntityId()) : Optional.empty();
   }

   public void setEntityId(int entityId) {
      float offsetY = (Float)this.getEntityEyeHeight().orElse(0.0F);
      this.source = new EntityPositionSource(entityId, offsetY);
   }

   public Optional<Float> getEntityEyeHeight() {
      return this.source instanceof EntityPositionSource ? Optional.of(((EntityPositionSource)this.source).getOffsetY()) : Optional.empty();
   }

   public void setEntityEyeHeight(Float offsetY) {
      this.setEntityEyeHeight(offsetY == null ? 0.0F : offsetY);
   }

   public void setEntityEyeHeight(float offsetY) {
      int entityId = (Integer)this.getEntityId().orElse(0);
      this.source = new EntityPositionSource(entityId, offsetY);
   }

   public int getTicks() {
      return this.ticks;
   }

   public void setTicks(int ticks) {
      this.ticks = ticks;
   }

   public boolean isEmpty() {
      return false;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         ParticleVibrationData that = (ParticleVibrationData)obj;
         if (this.ticks != that.ticks) {
            return false;
         } else {
            return !this.startingPosition.equals(that.startingPosition) ? false : this.source.equals(that.source);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.startingPosition, this.source, this.ticks});
   }

   /** @deprecated */
   @Deprecated
   public static enum PositionType implements MappedEntity {
      BLOCK(PositionSourceTypes.BLOCK),
      ENTITY(PositionSourceTypes.ENTITY);

      private final PositionSourceType<?> type;

      private PositionType(PositionSourceType<?> type) {
         this.type = type;
      }

      @Contract("null -> null; !null -> !null")
      @Nullable
      public static ParticleVibrationData.PositionType byModern(@Nullable PositionSourceType<?> type) {
         if (type == null) {
            return null;
         } else {
            ParticleVibrationData.PositionType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               ParticleVibrationData.PositionType legacyType = var1[var3];
               if (legacyType.type == type) {
                  return legacyType;
               }
            }

            throw new UnsupportedOperationException("Unsupported modern type: " + type.getName());
         }
      }

      public static ParticleVibrationData.PositionType getById(int id) {
         ClientVersion version = PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
         return byModern(PositionSourceTypes.getById(version, id));
      }

      @Nullable
      public static ParticleVibrationData.PositionType getByName(String name) {
         return getByName(new ResourceLocation(name));
      }

      @Nullable
      public static ParticleVibrationData.PositionType getByName(ResourceLocation name) {
         return byModern(PositionSourceTypes.getByName(name.toString()));
      }

      public ResourceLocation getName() {
         return this.type.getName();
      }

      public int getId(ClientVersion version) {
         return this.type.getId(version);
      }

      // $FF: synthetic method
      private static ParticleVibrationData.PositionType[] $values() {
         return new ParticleVibrationData.PositionType[]{BLOCK, ENTITY};
      }
   }
}
