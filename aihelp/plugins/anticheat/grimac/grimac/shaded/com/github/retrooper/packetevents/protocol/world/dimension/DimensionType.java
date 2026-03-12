package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntityRefSet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.CodecNameable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributeMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.timelines.Timeline;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.TagKey;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.OptionalLong;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DimensionType extends MappedEntity, CopyableEntity<DimensionType>, DeepComparableEntity {
   NbtCodec<DimensionType> CODEC = (new NbtMapCodec<DimensionType>() {
      public DimensionType decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
         DimensionTypeBuilder builder = DimensionTypeBuilder.dimensionTypeBuilder();
         ServerVersion version = wrapper.getServerVersion();
         if (version.isNewerThanOrEquals(ServerVersion.V_1_21_11)) {
            builder.setHasFixedTime(compound.getBooleanOr("has_fixed_time", false)).setSkybox((DimensionType.Skybox)compound.getOr("skybox", DimensionType.Skybox.CODEC, DimensionType.Skybox.OVERWORLD, wrapper)).setCardinalLight((DimensionType.CardinalLight)compound.getOr("cardinal_light", DimensionType.CardinalLight.CODEC, DimensionType.CardinalLight.DEFAULT, wrapper)).setAttributes((EnvironmentAttributeMap)compound.getOr("attributes", EnvironmentAttributeMap.CODEC, EnvironmentAttributeMap.EMPTY, wrapper)).setTimelines((MappedEntityRefSet)compound.getOr("timelines", MappedEntitySet::decodeRefSet, MappedEntitySet.createEmpty(), wrapper));
         } else {
            Number fixedTimeNum = compound.getNumberTagValueOrNull("fixed_time");
            builder.setFixedTime(fixedTimeNum != null ? fixedTimeNum.longValue() : null).setAttribute(EnvironmentAttributes.GAMEPLAY_WATER_EVAPORATES, compound.getBoolean("ultrawarm")).setNatural(compound.getBoolean("natural")).setBedWorks(compound.getBoolean("bed_works")).setRespawnAnchorWorks(compound.getBoolean("respawn_anchor_works")).setAttribute(EnvironmentAttributes.GAMEPLAY_PIGLINS_ZOMBIFY, !compound.getBoolean("piglin_safe")).setAttribute(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, compound.getBoolean("has_raids"));
            if (version.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
               builder.setEffects((ResourceLocation)compound.getOrThrow("effects", ResourceLocation.CODEC, wrapper));
            }

            if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
               NBTNumber cloudHeightTag = compound.getNumberTagOrNull("cloud_height");
               if (cloudHeightTag != null) {
                  builder.setAttribute(EnvironmentAttributes.VISUAL_CLOUD_HEIGHT, cloudHeightTag.getAsFloat());
               }
            }
         }

         if (version.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
            builder.setCoordinateScale(compound.getNumberTagOrThrow("coordinate_scale").getAsDouble());
            if (version.isNewerThanOrEquals(ServerVersion.V_1_17)) {
               builder.setMinY(compound.getNumberTagOrThrow("min_y").getAsInt());
               builder.setHeight(compound.getNumberTagOrThrow("height").getAsInt());
               if (version.isNewerThanOrEquals(ServerVersion.V_1_19)) {
                  builder.setMonsterSpawnLightLevel(compound.getTagOrThrow("monster_spawn_light_level"));
                  builder.setMonsterSpawnBlockLightLimit(compound.getNumberTagOrThrow("monster_spawn_block_light_limit").getAsInt());
               }
            }
         } else {
            builder.setCoordinateScale(compound.getBoolean("shrunk") ? 8.0D : 1.0D);
         }

         builder.setInfiniburn(version.isNewerThanOrEquals(ServerVersion.V_1_18_2) ? (TagKey)compound.getOrThrow("infiniburn", TagKey.CODEC, wrapper) : new TagKey((ResourceLocation)compound.getOrThrow("infiniburn", ResourceLocation.CODEC, wrapper)));
         return builder.setHasSkylight(compound.getBooleanOrThrow("has_skylight")).setHasCeiling(compound.getBooleanOrThrow("has_ceiling")).setLogicalHeight(compound.getNumberTagValueOrThrow("logical_height").intValue()).setAmbientLight(compound.getNumberTagValueOrThrow("ambient_light").floatValue()).build();
      }

      public void encode(NBTCompound compound, PacketWrapper<?> wrapper, DimensionType value) throws NbtCodecException {
         ServerVersion version = wrapper.getServerVersion();
         if (version.isNewerThanOrEquals(ServerVersion.V_1_21_11)) {
            if (value.hasFixedTime()) {
               compound.setTag("has_fixed_time", new NBTByte(true));
            }

            DimensionType.Skybox skybox = value.getSkybox();
            if (skybox != DimensionType.Skybox.OVERWORLD) {
               compound.set("skybox", skybox, DimensionType.Skybox.CODEC, wrapper);
            }

            DimensionType.CardinalLight cardinalLight = value.getCardinalLight();
            if (cardinalLight != DimensionType.CardinalLight.DEFAULT) {
               compound.set("cardinal_light", cardinalLight, DimensionType.CardinalLight.CODEC, wrapper);
            }

            EnvironmentAttributeMap env = value.getAttributes();
            if (!env.isEmpty()) {
               compound.set("attributes", env, EnvironmentAttributeMap.CODEC, wrapper);
            }

            MappedEntityRefSet<Timeline> timelines = value.getTimelinesRef();
            if (!timelines.isEmpty()) {
               compound.set("timelines", timelines, MappedEntitySet::encodeRefSet, wrapper);
            }
         } else {
            OptionalLong fixedTime = value.getFixedTime();
            if (fixedTime.isPresent()) {
               compound.setTag("fixed_time", new NBTLong(fixedTime.getAsLong()));
            }

            compound.setTag("ultrawarm", new NBTByte(value.isUltraWarm()));
            compound.setTag("natural", new NBTByte(value.isNatural()));
            compound.setTag("bed_works", new NBTByte(value.isBedWorking()));
            compound.setTag("respawn_anchor_works", new NBTByte(value.isRespawnAnchorWorking()));
            compound.setTag("piglin_safe", new NBTByte(value.isPiglinSafe()));
            compound.setTag("has_raids", new NBTByte(value.hasRaids()));
            if (version.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
               compound.set("effects", value.getEffectsLocation(), ResourceLocation.CODEC, wrapper);
            }

            if (version.isNewerThanOrEquals(ServerVersion.V_1_21_6)) {
               Integer cloudHeight = value.getCloudHeight();
               if (cloudHeight != null) {
                  compound.setTag("cloud_height", new NBTInt(cloudHeight));
               }
            }
         }

         if (version.isNewerThanOrEquals(ServerVersion.V_1_16_2)) {
            compound.setTag("coordinate_scale", new NBTDouble(value.getCoordinateScale()));
            if (version.isNewerThanOrEquals(ServerVersion.V_1_17)) {
               compound.setTag("min_y", new NBTInt(value.getMinY()));
               compound.setTag("height", new NBTInt(value.getHeight()));
               if (version.isNewerThanOrEquals(ServerVersion.V_1_19)) {
                  compound.setTag("monster_spawn_light_level", value.getMonsterSpawnLightLevel());
                  compound.setTag("monster_spawn_block_light_limit", new NBTInt(value.getMonsterSpawnBlockLightLimit()));
               }
            }
         } else {
            compound.setTag("shrunk", new NBTByte(value.isShrunk()));
         }

         compound.setTag("has_skylight", new NBTByte(value.hasSkyLight()));
         compound.setTag("has_ceiling", new NBTByte(value.hasCeiling()));
         compound.setTag("logical_height", new NBTInt(value.getLogicalHeight()));
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_18_2)) {
            compound.set("infiniburn", value.getInfiniburn(), TagKey.CODEC, wrapper);
         } else {
            compound.set("infiniburn", value.getInfiniburn().getId(), ResourceLocation.CODEC, wrapper);
         }

         compound.setTag("ambient_light", new NBTFloat(value.getAmbientLight()));
      }
   }).codec();

   boolean hasFixedTime();

   @ApiStatus.Obsolete
   OptionalLong getFixedTime();

   boolean hasSkyLight();

   boolean hasCeiling();

   @ApiStatus.Obsolete
   boolean isUltraWarm();

   @ApiStatus.Obsolete
   boolean isNatural();

   double getCoordinateScale();

   default boolean isShrunk() {
      return this.getCoordinateScale() > 1.0D;
   }

   @ApiStatus.Obsolete
   boolean isBedWorking();

   @ApiStatus.Obsolete
   boolean isRespawnAnchorWorking();

   int getMinY();

   /** @deprecated */
   @Deprecated
   default int getMinY(ClientVersion version) {
      return this.getMinY();
   }

   int getHeight();

   /** @deprecated */
   default int getHeight(ClientVersion version) {
      return this.getHeight();
   }

   int getLogicalHeight();

   /** @deprecated */
   @Deprecated
   default int getLogicalHeight(ClientVersion version) {
      return this.getLogicalHeight();
   }

   TagKey getInfiniburn();

   /** @deprecated */
   @Deprecated
   default String getInfiniburnTag() {
      return this.getInfiniburn().toString();
   }

   @ApiStatus.Obsolete
   ResourceLocation getEffectsLocation();

   float getAmbientLight();

   @ApiStatus.Obsolete
   @Nullable
   Integer getCloudHeight();

   @ApiStatus.Obsolete
   boolean isPiglinSafe();

   @ApiStatus.Obsolete
   boolean hasRaids();

   @ApiStatus.Experimental
   NBT getMonsterSpawnLightLevel();

   int getMonsterSpawnBlockLightLimit();

   DimensionType.Skybox getSkybox();

   DimensionType.CardinalLight getCardinalLight();

   EnvironmentAttributeMap getAttributes();

   MappedEntitySet<Timeline> getTimelines();

   MappedEntityRefSet<Timeline> getTimelinesRef();

   default DimensionTypeRef asRef(PacketWrapper<?> wrapper) {
      return new DimensionTypeRef.DirectRef(this, wrapper);
   }

   /** @deprecated */
   @Deprecated
   default DimensionTypeRef asRef(ClientVersion version) {
      return this.asRef(PacketWrapper.createDummyWrapper(version));
   }

   /** @deprecated */
   @Deprecated
   static DimensionType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      return (DimensionType)((DimensionType)CODEC.decode(nbt, PacketWrapper.createDummyWrapper(version))).copy(data);
   }

   /** @deprecated */
   @Deprecated
   static NBT encode(DimensionType dimensionType, ClientVersion version) {
      return CODEC.encode(PacketWrapper.createDummyWrapper(version), dimensionType);
   }

   public static enum CardinalLight implements CodecNameable {
      DEFAULT("default"),
      NETHER("nether");

      public static final NbtCodec<DimensionType.CardinalLight> CODEC = NbtCodecs.forEnum(values());
      private final String name;

      private CardinalLight(String name) {
         this.name = name;
      }

      public String getCodecName() {
         return this.name;
      }

      // $FF: synthetic method
      private static DimensionType.CardinalLight[] $values() {
         return new DimensionType.CardinalLight[]{DEFAULT, NETHER};
      }
   }

   public static enum Skybox implements CodecNameable {
      NONE("none"),
      OVERWORLD("overworld"),
      END("end");

      public static final NbtCodec<DimensionType.Skybox> CODEC = NbtCodecs.forEnum(values());
      private final String name;

      private Skybox(String name) {
         this.name = name;
      }

      public String getCodecName() {
         return this.name;
      }

      // $FF: synthetic method
      private static DimensionType.Skybox[] $values() {
         return new DimensionType.Skybox[]{NONE, OVERWORLD, END};
      }
   }
}
