package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntityRefSet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.ResolvableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributeMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.timelines.Timeline;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.timelines.Timelines;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.TagKey;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import java.util.OptionalLong;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticDimensionType extends AbstractMappedEntity implements DimensionType, ResolvableEntity {
   private final boolean hasFixedTime;
   private final DimensionType.Skybox skybox;
   private final DimensionType.CardinalLight cardinalLight;
   private final EnvironmentAttributeMap attributes;
   private final MappedEntityRefSet<Timeline> timelinesRef;
   @Nullable
   private MappedEntitySet<Timeline> timelines;
   @ApiStatus.Obsolete
   @Nullable
   private final Long fixedTime;
   @ApiStatus.Obsolete
   private final boolean natural;
   @ApiStatus.Obsolete
   private final boolean bedWorks;
   @ApiStatus.Obsolete
   private final boolean respawnAnchorWorks;
   @ApiStatus.Obsolete
   @Nullable
   private final ResourceLocation effects;
   private final double coordinateScale;
   private final int minY;
   private final int height;
   @ApiStatus.Experimental
   private final NBT monsterSpawnLightLevel;
   private final int monsterSpawnBlockLightLimit;
   private final boolean hasSkylight;
   private final boolean hasCeiling;
   private final int logicalHeight;
   private final TagKey infiniburn;
   private final float ambientLight;

   /** @deprecated */
   @Deprecated
   public StaticDimensionType(OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling, boolean ultraWarm, boolean natural, double coordinateScale, boolean bedWorking, boolean respawnAnchorWorking, int minY, int height, int logicalHeight, String infiniburnTag, @Nullable ResourceLocation effectsLocation, float ambientLight, boolean piglinSafe, boolean hasRaids, @Nullable NBT monsterSpawnLightLevel, int monsterSpawnBlockLightLimit) {
      this(fixedTime, hasSkyLight, hasCeiling, ultraWarm, natural, coordinateScale, bedWorking, respawnAnchorWorking, minY, height, logicalHeight, infiniburnTag, effectsLocation, ambientLight, 192, piglinSafe, hasRaids, monsterSpawnLightLevel, monsterSpawnBlockLightLimit);
   }

   /** @deprecated */
   @Deprecated
   public StaticDimensionType(OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling, boolean ultraWarm, boolean natural, double coordinateScale, boolean bedWorking, boolean respawnAnchorWorking, int minY, int height, int logicalHeight, String infiniburnTag, @Nullable ResourceLocation effectsLocation, float ambientLight, @Nullable Integer cloudHeight, boolean piglinSafe, boolean hasRaids, @Nullable NBT monsterSpawnLightLevel, int monsterSpawnBlockLightLimit) {
      this((TypesBuilderData)null, fixedTime.isPresent(), DimensionType.Skybox.OVERWORLD, DimensionType.CardinalLight.DEFAULT, EnvironmentAttributeMap.create().set(EnvironmentAttributes.GAMEPLAY_WATER_EVAPORATES, ultraWarm).set(EnvironmentAttributes.VISUAL_CLOUD_HEIGHT, cloudHeight != null ? (float)cloudHeight : 192.0F).set(EnvironmentAttributes.GAMEPLAY_NETHER_PORTAL_SPAWNS_PIGLIN, !piglinSafe).set(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, hasRaids).copyImmutable(), MappedEntitySet.createEmpty(), fixedTime.isPresent() ? fixedTime.getAsLong() : null, natural, bedWorking, respawnAnchorWorking, effectsLocation, coordinateScale, minY, height, (NBT)(monsterSpawnLightLevel != null ? monsterSpawnLightLevel : new NBTInt(7)), monsterSpawnBlockLightLimit, hasSkyLight, hasCeiling, logicalHeight, TagKey.parse(infiniburnTag), ambientLight);
   }

   @ApiStatus.Internal
   public StaticDimensionType(@Nullable TypesBuilderData data, boolean hasFixedTime, DimensionType.Skybox skybox, DimensionType.CardinalLight cardinalLight, EnvironmentAttributeMap attributes, MappedEntityRefSet<Timeline> timelinesRef, @Nullable Long fixedTime, boolean natural, boolean bedWorks, boolean respawnAnchorWorks, @Nullable ResourceLocation effects, double coordinateScale, int minY, int height, NBT monsterSpawnLightLevel, int monsterSpawnBlockLightLimit, boolean hasSkylight, boolean hasCeiling, int logicalHeight, TagKey infiniburn, float ambientLight) {
      super(data);
      this.hasFixedTime = hasFixedTime;
      this.skybox = skybox;
      this.cardinalLight = cardinalLight;
      this.attributes = attributes;
      this.timelinesRef = timelinesRef;
      this.fixedTime = fixedTime;
      this.natural = natural;
      this.bedWorks = bedWorks;
      this.respawnAnchorWorks = respawnAnchorWorks;
      this.effects = effects;
      this.coordinateScale = coordinateScale;
      this.minY = minY;
      this.height = height;
      this.monsterSpawnLightLevel = monsterSpawnLightLevel;
      this.monsterSpawnBlockLightLimit = monsterSpawnBlockLightLimit;
      this.hasSkylight = hasSkylight;
      this.hasCeiling = hasCeiling;
      this.logicalHeight = logicalHeight;
      this.infiniburn = infiniburn;
      this.ambientLight = ambientLight;
   }

   public void doResolve(PacketWrapper<?> wrapper) {
      this.timelines = this.timelinesRef.resolve((PacketWrapper)wrapper, Timelines.getRegistry());
   }

   public DimensionType copy(@Nullable TypesBuilderData newData) {
      return new StaticDimensionType(newData, this.hasFixedTime, this.skybox, this.cardinalLight, this.attributes, this.timelinesRef, this.fixedTime, this.natural, this.bedWorks, this.respawnAnchorWorks, this.effects, this.coordinateScale, this.minY, this.height, this.monsterSpawnLightLevel, this.monsterSpawnBlockLightLimit, this.hasSkylight, this.hasCeiling, this.logicalHeight, this.infiniburn, this.ambientLight);
   }

   public boolean hasFixedTime() {
      return this.hasFixedTime;
   }

   public OptionalLong getFixedTime() {
      return this.fixedTime != null ? OptionalLong.of(this.fixedTime) : OptionalLong.empty();
   }

   public boolean hasSkyLight() {
      return this.hasSkylight;
   }

   public boolean hasCeiling() {
      return this.hasCeiling;
   }

   public boolean isUltraWarm() {
      return (Boolean)this.attributes.getOrDefault(EnvironmentAttributes.GAMEPLAY_WATER_EVAPORATES);
   }

   public boolean isNatural() {
      return this.natural;
   }

   public double getCoordinateScale() {
      return this.coordinateScale;
   }

   public boolean isBedWorking() {
      return this.bedWorks;
   }

   public boolean isRespawnAnchorWorking() {
      return this.respawnAnchorWorks;
   }

   public int getMinY() {
      return this.minY;
   }

   public int getHeight() {
      return this.height;
   }

   public int getLogicalHeight() {
      return this.logicalHeight;
   }

   public TagKey getInfiniburn() {
      return this.infiniburn;
   }

   public ResourceLocation getEffectsLocation() {
      if (this.effects == null) {
         throw new UnsupportedOperationException();
      } else {
         return this.effects;
      }
   }

   public float getAmbientLight() {
      return this.ambientLight;
   }

   @Nullable
   public Integer getCloudHeight() {
      return ((Float)this.attributes.getOrDefault(EnvironmentAttributes.VISUAL_CLOUD_HEIGHT)).intValue();
   }

   public boolean isPiglinSafe() {
      return !(Boolean)this.attributes.getOrDefault(EnvironmentAttributes.GAMEPLAY_PIGLINS_ZOMBIFY);
   }

   public boolean hasRaids() {
      return (Boolean)this.attributes.getOrDefault(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID);
   }

   public NBT getMonsterSpawnLightLevel() {
      return this.monsterSpawnLightLevel;
   }

   public int getMonsterSpawnBlockLightLimit() {
      return this.monsterSpawnBlockLightLimit;
   }

   public DimensionType.Skybox getSkybox() {
      return this.skybox;
   }

   public DimensionType.CardinalLight getCardinalLight() {
      return this.cardinalLight;
   }

   public EnvironmentAttributeMap getAttributes() {
      return this.attributes;
   }

   public MappedEntitySet<Timeline> getTimelines() {
      if (this.timelines == null) {
         throw new UnsupportedOperationException();
      } else {
         return this.timelines;
      }
   }

   public MappedEntityRefSet<Timeline> getTimelinesRef() {
      return this.timelinesRef;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         StaticDimensionType that = (StaticDimensionType)obj;
         if (this.hasFixedTime != that.hasFixedTime) {
            return false;
         } else if (this.natural != that.natural) {
            return false;
         } else if (this.bedWorks != that.bedWorks) {
            return false;
         } else if (this.respawnAnchorWorks != that.respawnAnchorWorks) {
            return false;
         } else if (Double.compare(that.coordinateScale, this.coordinateScale) != 0) {
            return false;
         } else if (this.minY != that.minY) {
            return false;
         } else if (this.height != that.height) {
            return false;
         } else if (this.monsterSpawnBlockLightLimit != that.monsterSpawnBlockLightLimit) {
            return false;
         } else if (this.hasSkylight != that.hasSkylight) {
            return false;
         } else if (this.hasCeiling != that.hasCeiling) {
            return false;
         } else if (this.logicalHeight != that.logicalHeight) {
            return false;
         } else if (Float.compare(that.ambientLight, this.ambientLight) != 0) {
            return false;
         } else if (this.skybox != that.skybox) {
            return false;
         } else if (this.cardinalLight != that.cardinalLight) {
            return false;
         } else if (!this.attributes.equals(that.attributes)) {
            return false;
         } else if (!this.timelinesRef.equals(that.timelinesRef)) {
            return false;
         } else if (!Objects.equals(this.fixedTime, that.fixedTime)) {
            return false;
         } else if (!Objects.equals(this.effects, that.effects)) {
            return false;
         } else {
            return !this.monsterSpawnLightLevel.equals(that.monsterSpawnLightLevel) ? false : this.infiniburn.equals(that.infiniburn);
         }
      } else {
         return false;
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.hasFixedTime, this.skybox, this.cardinalLight, this.attributes, this.timelinesRef, this.fixedTime, this.natural, this.bedWorks, this.respawnAnchorWorks, this.effects, this.coordinateScale, this.minY, this.height, this.monsterSpawnLightLevel, this.monsterSpawnBlockLightLimit, this.hasSkylight, this.hasCeiling, this.logicalHeight, this.infiniburn, this.ambientLight});
   }
}
