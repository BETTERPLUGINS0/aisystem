package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.NonExtendable
public interface DimensionTypeRef {
   /** @deprecated */
   @Deprecated
   default DimensionType resolve(IRegistry<DimensionType> registry, ClientVersion version) {
      return this.resolve(registry, PacketWrapper.createDummyWrapper(version));
   }

   DimensionType resolve(IRegistry<DimensionType> registry, PacketWrapper<?> wrapper);

   default ResourceLocation getName() {
      throw new UnsupportedOperationException();
   }

   default int getId() {
      throw new UnsupportedOperationException();
   }

   default NBT getData() {
      throw new UnsupportedOperationException();
   }

   static DimensionTypeRef read(PacketWrapper<?> wrapper) {
      ServerVersion version = wrapper.getServerVersion();
      if (version.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         return new DimensionTypeRef.IdRef(wrapper.readVarInt());
      } else {
         boolean v1162 = version.isNewerThanOrEquals(ServerVersion.V_1_16_2);
         if (!version.isNewerThanOrEquals(ServerVersion.V_1_19) && (v1162 || !version.isNewerThanOrEquals(ServerVersion.V_1_16))) {
            return (DimensionTypeRef)(v1162 ? new DimensionTypeRef.DataRef(wrapper.readNBTRaw()) : new DimensionTypeRef.IdRef(wrapper instanceof WrapperPlayServerJoinGame && version.isOlderThan(ServerVersion.V_1_9_2) ? wrapper.readByte() : wrapper.readInt()));
         } else {
            return new DimensionTypeRef.NameRef(wrapper.readIdentifier());
         }
      }
   }

   static void write(PacketWrapper<?> wrapper, DimensionTypeRef ref) {
      ServerVersion version = wrapper.getServerVersion();
      if (version.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         wrapper.writeVarInt(ref.getId());
      } else {
         boolean v1162 = version.isNewerThanOrEquals(ServerVersion.V_1_16_2);
         if (version.isNewerThanOrEquals(ServerVersion.V_1_19) || !v1162 && version.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            wrapper.writeIdentifier(ref.getName());
         } else if (v1162) {
            wrapper.writeNBTRaw(ref.getData());
         } else if (wrapper instanceof WrapperPlayServerJoinGame && version.isOlderThan(ServerVersion.V_1_9_2)) {
            wrapper.writeByte(ref.getId());
         } else {
            wrapper.writeInt(ref.getId());
         }

      }
   }

   public static final class IdRef implements DimensionTypeRef {
      private final int id;

      public IdRef(int id) {
         this.id = id;
      }

      public DimensionType resolve(IRegistry<DimensionType> registry, PacketWrapper<?> wrapper) {
         ClientVersion version = wrapper.getServerVersion().toClientVersion();
         return (DimensionType)registry.getByIdOrThrow(version, this.id);
      }

      public int getId() {
         return this.id;
      }
   }

   public static final class NameRef implements DimensionTypeRef {
      private final ResourceLocation name;

      public NameRef(ResourceLocation name) {
         this.name = name;
      }

      public DimensionType resolve(IRegistry<DimensionType> registry, PacketWrapper<?> wrapper) {
         ClientVersion version = wrapper.getServerVersion().toClientVersion();
         return (DimensionType)registry.getByNameOrThrow(version, this.name);
      }

      public ResourceLocation getName() {
         return this.name;
      }
   }

   public static final class DataRef implements DimensionTypeRef {
      private final NBT data;

      public DataRef(NBT data) {
         this.data = data;
      }

      public DimensionType resolve(IRegistry<DimensionType> registry, PacketWrapper<?> wrapper) {
         ResourceLocation name = this.getNullableName();
         if (name != null) {
            DimensionType dimensionType = (DimensionType)registry.getByName(name);
            if (dimensionType != null) {
               return dimensionType;
            }
         }

         return (DimensionType)DimensionType.CODEC.decode(this.data, wrapper);
      }

      @Nullable
      public ResourceLocation getNullableName() {
         if (this.data instanceof NBTCompound) {
            String effectsName = ((NBTCompound)this.data).getStringTagValueOrNull("effects");
            if (effectsName != null) {
               return new ResourceLocation(effectsName);
            }
         }

         return null;
      }

      public ResourceLocation getName() {
         ResourceLocation name = this.getNullableName();
         return name != null ? name : DimensionTypeRef.super.getName();
      }

      public NBT getData() {
         return this.data;
      }
   }

   public static final class DirectRef implements DimensionTypeRef {
      private final DimensionType dimensionType;
      private final PacketWrapper<?> wrapper;

      /** @deprecated */
      @Deprecated
      public DirectRef(DimensionType dimensionType, ClientVersion version) {
         this(dimensionType, PacketWrapper.createDummyWrapper(version));
      }

      public DirectRef(DimensionType dimensionType, PacketWrapper<?> wrapper) {
         this.dimensionType = dimensionType;
         this.wrapper = wrapper;
      }

      public DimensionType resolve(IRegistry<DimensionType> registry, PacketWrapper<?> wrapper) {
         if (wrapper.getServerVersion() != this.wrapper.getServerVersion()) {
            throw new IllegalArgumentException("Expected version " + this.wrapper.getServerVersion() + ", received " + wrapper.getServerVersion() + " for direct dimension type ref " + this.dimensionType);
         } else {
            return this.dimensionType;
         }
      }

      public ResourceLocation getName() {
         return this.dimensionType.getName();
      }

      public int getId() {
         return this.dimensionType.getId(this.getVersion());
      }

      public NBT getData() {
         return DimensionType.CODEC.encode(this.wrapper, this.dimensionType);
      }

      public DimensionType getDimensionType() {
         return this.dimensionType;
      }

      public ClientVersion getVersion() {
         return this.wrapper.getServerVersion().toClientVersion();
      }
   }
}
