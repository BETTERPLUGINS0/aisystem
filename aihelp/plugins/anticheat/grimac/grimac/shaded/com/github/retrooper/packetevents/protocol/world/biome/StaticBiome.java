package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributeMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticBiome extends AbstractMappedEntity implements Biome {
   private final boolean precipitation;
   private final float temperature;
   private final Biome.TemperatureModifier temperatureModifier;
   private final float downfall;
   @Nullable
   private final Biome.Category category;
   @Nullable
   private final Float depth;
   @Nullable
   private final Float scale;
   private final BiomeEffects effects;
   private final EnvironmentAttributeMap attributes;

   public StaticBiome(boolean precipitation, float temperature, Biome.TemperatureModifier temperatureModifier, float downfall, BiomeEffects effects, EnvironmentAttributeMap attributes) {
      this((TypesBuilderData)null, precipitation, temperature, temperatureModifier, downfall, (Biome.Category)null, (Float)null, (Float)null, effects, attributes);
   }

   public StaticBiome(boolean precipitation, float temperature, Biome.TemperatureModifier temperatureModifier, float downfall, BiomeEffects effects) {
      this(precipitation, temperature, temperatureModifier, downfall, effects, EnvironmentAttributeMap.EMPTY);
   }

   public StaticBiome(boolean precipitation, float temperature, Biome.TemperatureModifier temperatureModifier, float downfall, @Nullable Biome.Category category, @Nullable Float depth, @Nullable Float scale, BiomeEffects effects) {
      this((TypesBuilderData)null, precipitation, temperature, temperatureModifier, downfall, category, depth, scale, effects, EnvironmentAttributeMap.EMPTY);
   }

   @ApiStatus.Internal
   public StaticBiome(@Nullable TypesBuilderData data, boolean precipitation, float temperature, Biome.TemperatureModifier temperatureModifier, float downfall, @Nullable Biome.Category category, @Nullable Float depth, @Nullable Float scale, BiomeEffects effects, EnvironmentAttributeMap attributes) {
      super(data);
      this.precipitation = precipitation;
      this.temperature = temperature;
      this.temperatureModifier = temperatureModifier;
      this.downfall = downfall;
      this.category = category;
      this.depth = depth;
      this.scale = scale;
      this.effects = effects;
      this.attributes = attributes;
   }

   public Biome copy(@Nullable TypesBuilderData newData) {
      return new StaticBiome(newData, this.precipitation, this.temperature, this.temperatureModifier, this.downfall, this.category, this.depth, this.scale, this.effects, this.attributes);
   }

   public boolean hasPrecipitation() {
      return this.precipitation;
   }

   public Biome.Precipitation getPrecipitation() {
      if (!this.hasPrecipitation()) {
         return Biome.Precipitation.NONE;
      } else {
         switch(this.getTemperatureModifier()) {
         case NONE:
            return Biome.Precipitation.RAIN;
         case FROZEN:
            return Biome.Precipitation.SNOW;
         default:
            throw new AssertionError();
         }
      }
   }

   public float getTemperature() {
      return this.temperature;
   }

   public Biome.TemperatureModifier getTemperatureModifier() {
      return this.temperatureModifier;
   }

   public float getDownfall() {
      return this.downfall;
   }

   @Nullable
   public Biome.Category getCategory() {
      return this.category;
   }

   @Nullable
   public Float getDepth() {
      return this.depth;
   }

   @Nullable
   public Float getScale() {
      return this.scale;
   }

   public BiomeEffects getEffects() {
      return this.effects;
   }

   public EnvironmentAttributeMap getAttributes() {
      return this.attributes;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof StaticBiome)) {
         return false;
      } else {
         StaticBiome that = (StaticBiome)obj;
         if (this.precipitation != that.precipitation) {
            return false;
         } else if (Float.compare(that.temperature, this.temperature) != 0) {
            return false;
         } else if (Float.compare(that.downfall, this.downfall) != 0) {
            return false;
         } else if (this.temperatureModifier != that.temperatureModifier) {
            return false;
         } else if (this.category != that.category) {
            return false;
         } else if (!Objects.equals(this.depth, that.depth)) {
            return false;
         } else if (!Objects.equals(this.scale, that.scale)) {
            return false;
         } else {
            return !this.effects.equals(that.effects) ? false : this.attributes.equals(that.attributes);
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.precipitation, this.temperature, this.temperatureModifier, this.downfall, this.category, this.depth, this.scale, this.effects, this.attributes});
   }

   public String toString() {
      return "StaticBiome{precipitation=" + this.precipitation + ", temperature=" + this.temperature + ", temperatureModifier=" + this.temperatureModifier + ", downfall=" + this.downfall + ", category=" + this.category + ", depth=" + this.depth + ", scale=" + this.scale + ", effects=" + this.effects + ", attributes=" + this.attributes + '}';
   }
}
