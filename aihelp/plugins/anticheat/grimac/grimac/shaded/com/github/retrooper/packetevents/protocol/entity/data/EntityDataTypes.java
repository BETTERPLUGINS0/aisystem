package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProfile;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.armadillo.ArmadilloState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cat.CatVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.chicken.ChickenVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cow.CowVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.struct.CopperGolemState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.data.struct.WeatheringCopperState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.frog.FrogVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.nautilus.ZombieNautilusVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.pig.PigVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.sniffer.SnifferState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfSoundVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.Particle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.HumanoidArm;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.BlockFace;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.painting.PaintingVariant;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Quaternion4f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class EntityDataTypes {
   private static final VersionedRegistry<EntityDataType<?>> REGISTRY = new VersionedRegistry("entity_data_serializer");
   public static final EntityDataType<Byte> BYTE = define("byte", PacketWrapper::readByte, PacketWrapper::writeByte);
   public static final EntityDataType<Short> SHORT = define("short", PacketWrapper::readShort, PacketWrapper::writeShort);
   public static final EntityDataType<Integer> INT = define("int", (wrapper) -> {
      return wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9) ? wrapper.readVarInt() : wrapper.readInt();
   }, (wrapper, value) -> {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
         wrapper.writeVarInt(value);
      } else {
         wrapper.writeInt(value);
      }

   });
   public static final EntityDataType<Long> LONG = define("long", PacketWrapper::readVarLong, PacketWrapper::writeVarLong);
   public static final EntityDataType<Float> FLOAT = define("float", PacketWrapper::readFloat, PacketWrapper::writeFloat);
   public static final EntityDataType<String> STRING = define("string", PacketWrapper::readString, PacketWrapper::writeString);
   /** @deprecated */
   @Deprecated
   public static final EntityDataType<String> COMPONENT = define("component", PacketWrapper::readComponentJSON, PacketWrapper::writeComponentJSON);
   public static final EntityDataType<Component> ADV_COMPONENT = define("component", PacketWrapper::readComponent, PacketWrapper::writeComponent);
   /** @deprecated */
   @Deprecated
   public static final EntityDataType<Optional<String>> OPTIONAL_COMPONENT = define("optional_component", readOptionalComponentJSONDeserializer(), writeOptionalComponentJSONSerializer());
   public static final EntityDataType<Optional<Component>> OPTIONAL_ADV_COMPONENT = define("optional_component", readOptionalComponentDeserializer(), writeOptionalComponentSerializer());
   public static final EntityDataType<ItemStack> ITEMSTACK = define("itemstack", PacketWrapper::readItemStack, PacketWrapper::writeItemStack);
   public static final EntityDataType<Optional<ItemStack>> OPTIONAL_ITEMSTACK = define("optional_itemstack", (wrapper) -> {
      return Optional.of(wrapper.readItemStack());
   }, (wrapper, value) -> {
      wrapper.writeItemStack((ItemStack)value.orElse((Object)null));
   });
   public static final EntityDataType<Boolean> BOOLEAN = define("boolean", PacketWrapper::readBoolean, PacketWrapper::writeBoolean);
   public static final EntityDataType<Vector3f> ROTATION = define("rotation", (wrapper) -> {
      return new Vector3f(wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat());
   }, (wrapper, value) -> {
      wrapper.writeFloat(value.x);
      wrapper.writeFloat(value.y);
      wrapper.writeFloat(value.z);
   });
   public static final EntityDataType<Vector3i> BLOCK_POSITION = define("block_position", (wrapper) -> {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
         return wrapper.readBlockPosition();
      } else {
         int x = wrapper.readInt();
         int y = wrapper.readInt();
         int z = wrapper.readInt();
         return new Vector3i(x, y, z);
      }
   }, (wrapper, blockPosition) -> {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
         wrapper.writeBlockPosition(blockPosition);
      } else {
         wrapper.writeInt(blockPosition.getX());
         wrapper.writeInt(blockPosition.getY());
         wrapper.writeInt(blockPosition.getZ());
      }

   });
   public static final EntityDataType<Optional<Vector3i>> OPTIONAL_BLOCK_POSITION = define("optional_block_position", readOptionalBlockPositionDeserializer(), writeOptionalBlockPositionSerializer());
   public static final EntityDataType<BlockFace> BLOCK_FACE = define("block_face", (wrapper) -> {
      int id = wrapper.readVarInt();
      return BlockFace.getBlockFaceByValue(id);
   }, (wrapper, value) -> {
      wrapper.writeVarInt(value.getFaceValue());
   });
   public static final EntityDataType<Optional<UUID>> OPTIONAL_UUID = define("optional_uuid", (wrapper) -> {
      return Optional.ofNullable((UUID)wrapper.readOptional(PacketWrapper::readUUID));
   }, (wrapper, value) -> {
      wrapper.writeOptional((UUID)value.orElse((Object)null), PacketWrapper::writeUUID);
   });
   public static final EntityDataType<Integer> BLOCK_STATE = define("block_state", readIntDeserializer(), writeIntSerializer());
   public static final EntityDataType<Integer> OPTIONAL_BLOCK_STATE = define("optional_block_state", readIntDeserializer(), writeIntSerializer());
   @ApiStatus.Obsolete
   public static final EntityDataType<NBTCompound> NBT = define("nbt", PacketWrapper::readNBT, PacketWrapper::writeNBT);
   public static final EntityDataType<Particle<?>> PARTICLE = define("particle", Particle::read, Particle::write);
   public static final EntityDataType<VillagerData> VILLAGER_DATA = define("villager_data", PacketWrapper::readVillagerData, PacketWrapper::writeVillagerData);
   public static final EntityDataType<Optional<Integer>> OPTIONAL_INT = define("optional_int", (wrapper) -> {
      int i = wrapper.readVarInt();
      return i == 0 ? Optional.empty() : Optional.of(i - 1);
   }, (wrapper, value) -> {
      wrapper.writeVarInt((Integer)value.orElse(-1) + 1);
   });
   public static final EntityDataType<EntityPose> ENTITY_POSE = define("entity_pose", (wrapper) -> {
      int id = wrapper.readVarInt();
      return EntityPose.getById(wrapper.getServerVersion().toClientVersion(), id);
   }, (wrapper, value) -> {
      wrapper.writeVarInt(value.getId(wrapper.getServerVersion().toClientVersion()));
   });
   /** @deprecated */
   @Deprecated
   public static final EntityDataType<Integer> CAT_VARIANT = define("cat_variant_type", readIntDeserializer(), writeIntSerializer());
   public static final EntityDataType<CatVariant> TYPED_CAT_VARIANT = define("cat_variant_type", CatVariant::read, CatVariant::write);
   /** @deprecated */
   @Deprecated
   public static final EntityDataType<Integer> FROG_VARIANT = define("frog_variant_type", readIntDeserializer(), writeIntSerializer());
   public static final EntityDataType<FrogVariant> TYPED_FROG_VARIANT = define("frog_variant_type", FrogVariant::read, FrogVariant::write);
   public static final EntityDataType<Optional<WorldBlockPosition>> OPTIONAL_GLOBAL_POSITION = define("optional_global_position", (wrapper) -> {
      return Optional.ofNullable((WorldBlockPosition)wrapper.readOptional((w) -> {
         return new WorldBlockPosition(new ResourceLocation(w.readString(32767)), w.readBlockPosition());
      }));
   }, (wrapper, value) -> {
      wrapper.writeOptional((WorldBlockPosition)value.orElse((Object)null), (w, globalPos) -> {
         w.writeString(globalPos.getWorld().toString());
         w.writeBlockPosition(globalPos.getBlockPosition());
      });
   });
   /** @deprecated */
   @Deprecated
   public static final EntityDataType<Integer> PAINTING_VARIANT_TYPE = define("painting_variant_type", readIntDeserializer(), writeIntSerializer());
   public static final EntityDataType<PaintingVariant> PAINTING_VARIANT = define("painting_variant_type", PaintingVariant::read, PaintingVariant::write);
   public static final EntityDataType<SnifferState> SNIFFER_STATE = define("sniffer_state", (wrapper) -> {
      int id = wrapper.readVarInt();
      return SnifferState.values()[id];
   }, (wrapper, value) -> {
      wrapper.writeVarInt(value.ordinal());
   });
   public static final EntityDataType<Vector3f> VECTOR3F = define("vector3f", (wrapper) -> {
      return new Vector3f(wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat());
   }, (wrapper, value) -> {
      wrapper.writeFloat(value.x);
      wrapper.writeFloat(value.y);
      wrapper.writeFloat(value.z);
   });
   public static final EntityDataType<Quaternion4f> QUATERNION = define("quaternion", (wrapper) -> {
      return new Quaternion4f(wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat());
   }, (wrapper, value) -> {
      wrapper.writeFloat(value.getX());
      wrapper.writeFloat(value.getY());
      wrapper.writeFloat(value.getZ());
      wrapper.writeFloat(value.getW());
   });
   public static final EntityDataType<ArmadilloState> ARMADILLO_STATE = define("armadillo_state", (wrapper) -> {
      return ArmadilloState.values()[wrapper.readVarInt()];
   }, (wrapper, value) -> {
      wrapper.writeVarInt(value.ordinal());
   });
   public static final EntityDataType<List<Particle<?>>> PARTICLES = define("particles", (wrapper) -> {
      return wrapper.readList(Particle::read);
   }, (wrapper, particles) -> {
      wrapper.writeList(particles, Particle::write);
   });
   /** @deprecated */
   @Deprecated
   public static final EntityDataType<Integer> WOLF_VARIANT = define("wolf_variant_type", readIntDeserializer(), writeIntSerializer());
   public static final EntityDataType<WolfVariant> TYPED_WOLF_VARIANT = define("wolf_variant_type", WolfVariant::read, WolfVariant::write);
   public static final EntityDataType<CowVariant> COW_VARIANT = define("cow_variant_type", CowVariant::read, CowVariant::write);
   public static final EntityDataType<WolfSoundVariant> WOLF_SOUND_VARIANT = define("wolf_sound_variant_type", WolfSoundVariant::read, WolfSoundVariant::write);
   public static final EntityDataType<PigVariant> PIG_VARIANT = define("pig_variant_type", PigVariant::read, PigVariant::write);
   public static final EntityDataType<ChickenVariant> CHICKEN_VARIANT = define("chicken_variant_type", ChickenVariant::read, ChickenVariant::write);
   public static final EntityDataType<CopperGolemState> COPPER_GOLEM_STATE = define("copper_golem_state", CopperGolemState::read, CopperGolemState::write);
   public static final EntityDataType<WeatheringCopperState> WEATHERING_COPPER_STATE = define("weathering_copper_state", WeatheringCopperState::read, WeatheringCopperState::write);
   public static final EntityDataType<ItemProfile> RESOLVABLE_PROFILE = define("resolvable_profile", ItemProfile::read, ItemProfile::write);
   public static final EntityDataType<ZombieNautilusVariant> ZOMBIE_NAUTILUS_VARIANT = define("zombie_nautilus_variant", ZombieNautilusVariant::read, ZombieNautilusVariant::write);
   public static final EntityDataType<HumanoidArm> HUMANOID_ARM = define("humanoid_arm", HumanoidArm::read, HumanoidArm::write);

   private EntityDataTypes() {
   }

   public static VersionedRegistry<EntityDataType<?>> getRegistry() {
      return REGISTRY;
   }

   public static Collection<EntityDataType<?>> values() {
      return REGISTRY.getEntries();
   }

   @Nullable
   public static EntityDataType<?> getById(ClientVersion version, int id) {
      return (EntityDataType)REGISTRY.getById(version, id);
   }

   @Nullable
   public static EntityDataType<?> getByName(String name) {
      return (EntityDataType)REGISTRY.getByName(name);
   }

   @ApiStatus.Internal
   public static <T, Z extends T> EntityDataType<Z> define(String name, PacketWrapper.Reader<Z> reader, PacketWrapper.Writer<T> writer) {
      return (EntityDataType)REGISTRY.define(name, (data) -> {
         Objects.requireNonNull(writer);
         return new EntityDataType(data, reader, writer::accept);
      });
   }

   private static PacketWrapper.Reader<Integer> readIntDeserializer() {
      return (wrapper) -> {
         return wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9) ? wrapper.readVarInt() : wrapper.readInt();
      };
   }

   private static PacketWrapper.Writer<Number> writeIntSerializer() {
      return (wrapper, value) -> {
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeVarInt(value.intValue());
         } else {
            wrapper.writeInt(value.intValue());
         }

      };
   }

   /** @deprecated */
   @Deprecated
   private static PacketWrapper.Reader<Optional<String>> readOptionalComponentJSONDeserializer() {
      return (wrapper) -> {
         return wrapper.readJavaOptional(PacketWrapper::readComponentJSON);
      };
   }

   /** @deprecated */
   @Deprecated
   private static PacketWrapper.Writer<Optional<String>> writeOptionalComponentJSONSerializer() {
      return (wrapper, value) -> {
         wrapper.writeJavaOptional(value, PacketWrapper::writeComponentJSON);
      };
   }

   private static PacketWrapper.Reader<Optional<Component>> readOptionalComponentDeserializer() {
      return (wrapper) -> {
         return wrapper.readJavaOptional(PacketWrapper::readComponent);
      };
   }

   private static PacketWrapper.Writer<Optional<Component>> writeOptionalComponentSerializer() {
      return (wrapper, value) -> {
         wrapper.writeJavaOptional(value, PacketWrapper::writeComponent);
      };
   }

   private static PacketWrapper.Reader<Optional<Vector3i>> readOptionalBlockPositionDeserializer() {
      return (wrapper) -> {
         return wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9) ? wrapper.readJavaOptional(PacketWrapper::readBlockPosition) : wrapper.readJavaOptional((ew) -> {
            return new Vector3i(ew.readInt(), ew.readInt(), ew.readInt());
         });
      };
   }

   private static PacketWrapper.Writer<Optional<Vector3i>> writeOptionalBlockPositionSerializer() {
      return (wrapper, value) -> {
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeJavaOptional(value, PacketWrapper::writeBlockPosition);
         } else {
            wrapper.writeJavaOptional(value, (ew, valuee) -> {
               wrapper.writeInt(valuee.getX());
               wrapper.writeInt(valuee.getY());
               wrapper.writeInt(valuee.getZ());
            });
         }

      };
   }

   static {
      REGISTRY.unloadMappings();
   }
}
