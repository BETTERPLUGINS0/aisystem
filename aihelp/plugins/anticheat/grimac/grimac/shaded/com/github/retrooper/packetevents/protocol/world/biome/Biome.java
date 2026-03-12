package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.CodecNameable;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributeMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface Biome extends MappedEntity, CopyableEntity<Biome>, DeepComparableEntity {
   NbtCodec<Biome> CODEC = (new NbtMapCodec<Biome>() {
      public Biome decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
         float temperature = compound.getNumberTagOrThrow("temperature").getAsFloat();
         Biome.TemperatureModifier temperatureModifier = (Biome.TemperatureModifier)compound.getOr("temperature_modifier", Biome.TemperatureModifier.CODEC, Biome.TemperatureModifier.NONE, wrapper);
         float downfall = compound.getNumberTagOrThrow("downfall").getAsFloat();
         Biome.Category category = null;
         Float depth = null;
         Float scale = null;
         boolean precipitation;
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            precipitation = compound.getBoolean("has_precipitation");
         } else {
            precipitation = compound.getOrThrow("precipitation", Biome.Precipitation.CODEC, wrapper) != Biome.Precipitation.NONE;
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_19)) {
               category = (Biome.Category)compound.getOrThrow("category", Biome.Category.CODEC, wrapper);
               if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_18)) {
                  depth = compound.getNumberTagOrThrow("depth").getAsFloat();
                  scale = compound.getNumberTagOrThrow("scale").getAsFloat();
               }
            }
         }

         EnvironmentAttributeMap attributes;
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11)) {
            attributes = (EnvironmentAttributeMap)compound.getOr("attributes", EnvironmentAttributeMap.CODEC, EnvironmentAttributeMap.EMPTY, wrapper);
         } else {
            attributes = EnvironmentAttributeMap.EMPTY;
         }

         BiomeEffects effects = (BiomeEffects)compound.getOrThrow("effects", BiomeEffects.codecWithAttributes(attributes), wrapper);
         return new StaticBiome((TypesBuilderData)null, precipitation, temperature, temperatureModifier, downfall, category, depth, scale, effects, attributes);
      }

      public void encode(NBTCompound compound, PacketWrapper<?> wrapper, Biome value) throws NbtCodecException {
         compound.setTag("temperature", new NBTFloat(value.getTemperature()));
         if (value.getTemperatureModifier() != Biome.TemperatureModifier.NONE) {
            compound.set("temperature_modifier", value.getTemperatureModifier(), Biome.TemperatureModifier.CODEC, wrapper);
         }

         compound.setTag("downfall", new NBTFloat(value.getDownfall()));
         compound.set("effects", value.getEffects(), BiomeEffects.CODEC, wrapper);
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            compound.setTag("has_precipitation", new NBTByte(value.hasPrecipitation()));
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11)) {
               compound.set("attributes", value.getAttributes(), EnvironmentAttributeMap.CODEC, wrapper);
            }
         } else {
            compound.set("precipitation", value.getPrecipitation(), Biome.Precipitation.CODEC, wrapper);
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_19)) {
               if (value.getCategory() != null) {
                  compound.set("category", value.getCategory(), Biome.Category.CODEC, wrapper);
               }

               if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_18)) {
                  if (value.getDepth() != null) {
                     compound.setTag("depth", new NBTFloat(value.getDepth()));
                  }

                  if (value.getScale() != null) {
                     compound.setTag("scale", new NBTFloat(value.getScale()));
                  }
               }
            }
         }

      }
   }).codec();

   boolean hasPrecipitation();

   @ApiStatus.Obsolete(
      since = "1.19.3"
   )
   Biome.Precipitation getPrecipitation();

   float getTemperature();

   Biome.TemperatureModifier getTemperatureModifier();

   float getDownfall();

   @ApiStatus.Obsolete(
      since = "1.19"
   )
   @Nullable
   Biome.Category getCategory();

   @ApiStatus.Obsolete(
      since = "1.18"
   )
   @Nullable
   Float getDepth();

   @ApiStatus.Obsolete(
      since = "1.18"
   )
   @Nullable
   Float getScale();

   BiomeEffects getEffects();

   EnvironmentAttributeMap getAttributes();

   /** @deprecated */
   @Deprecated
   static Biome decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      return (Biome)((Biome)CODEC.decode(nbt, PacketWrapper.createDummyWrapper(version))).copy(data);
   }

   /** @deprecated */
   @Deprecated
   static NBT encode(Biome biome, ClientVersion version) {
      return CODEC.encode(PacketWrapper.createDummyWrapper(version), biome);
   }

   public static enum TemperatureModifier implements CodecNameable {
      NONE("none"),
      FROZEN("frozen");

      public static final NbtCodec<Biome.TemperatureModifier> CODEC = NbtCodecs.forEnum(values());
      public static final Index<String, Biome.TemperatureModifier> ID_INDEX = Index.create(Biome.TemperatureModifier.class, Biome.TemperatureModifier::getId);
      private final String id;

      private TemperatureModifier(String id) {
         this.id = id;
      }

      public String getId() {
         return this.id;
      }

      public String getCodecName() {
         return this.id;
      }

      // $FF: synthetic method
      private static Biome.TemperatureModifier[] $values() {
         return new Biome.TemperatureModifier[]{NONE, FROZEN};
      }
   }

   @ApiStatus.Obsolete(
      since = "1.19.3"
   )
   public static enum Precipitation implements CodecNameable {
      NONE("none"),
      RAIN("rain"),
      SNOW("snow");

      public static final NbtCodec<Biome.Precipitation> CODEC = NbtCodecs.forEnum(values());
      public static final Index<String, Biome.Precipitation> ID_INDEX = Index.create(Biome.Precipitation.class, Biome.Precipitation::getId);
      private final String id;

      private Precipitation(String id) {
         this.id = id;
      }

      public String getId() {
         return this.id;
      }

      public String getCodecName() {
         return this.id;
      }

      // $FF: synthetic method
      private static Biome.Precipitation[] $values() {
         return new Biome.Precipitation[]{NONE, RAIN, SNOW};
      }
   }

   @ApiStatus.Obsolete(
      since = "1.19"
   )
   public static enum Category implements CodecNameable {
      NONE("none"),
      TAIGA("taiga"),
      EXTREME_HILLS("extreme_hills"),
      JUNGLE("jungle"),
      MESA("mesa"),
      PLAINS("plains"),
      SAVANNA("savanna"),
      ICY("icy"),
      THE_END("the_end"),
      BEACH("beach"),
      FOREST("forest"),
      OCEAN("ocean"),
      DESERT("desert"),
      RIVER("river"),
      SWAMP("swamp"),
      MUSHROOM("mushroom"),
      NETHER("nether"),
      UNDERGROUND("underground"),
      MOUNTAIN("mountain");

      public static final NbtCodec<Biome.Category> CODEC = NbtCodecs.forEnum(values());
      public static final Index<String, Biome.Category> ID_INDEX = Index.create(Biome.Category.class, Biome.Category::getId);
      private final String id;

      private Category(String id) {
         this.id = id;
      }

      public String getId() {
         return this.id;
      }

      public String getCodecName() {
         return this.id;
      }

      // $FF: synthetic method
      private static Biome.Category[] $values() {
         return new Biome.Category[]{NONE, TAIGA, EXTREME_HILLS, JUNGLE, MESA, PLAINS, SAVANNA, ICY, THE_END, BEACH, FOREST, OCEAN, DESERT, RIVER, SWAMP, MUSHROOM, NETHER, UNDERGROUND, MOUNTAIN};
      }
   }
}
