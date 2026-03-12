package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntityRefSet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributeMap;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.modifiers.AttributeModifier;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.timelines.Timeline;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.TagKey;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class DimensionTypeBuilder {
   private boolean hasFixedTime = false;
   private DimensionType.Skybox skybox;
   private DimensionType.CardinalLight cardinalLight;
   private final EnvironmentAttributeMap attributes;
   private MappedEntityRefSet<Timeline> timelines;
   @ApiStatus.Obsolete
   @Nullable
   private Long fixedTime;
   @ApiStatus.Obsolete
   private boolean natural;
   @ApiStatus.Obsolete
   private boolean bedWorks;
   @ApiStatus.Obsolete
   private boolean respawnAnchorWorks;
   @ApiStatus.Obsolete
   @Nullable
   private ResourceLocation effects;
   private double coordinateScale;
   private int minY;
   private int height;
   @ApiStatus.Experimental
   private NBT monsterSpawnLightLevel;
   private int monsterSpawnBlockLightLimit;
   private boolean hasSkylight;
   private boolean hasCeiling;
   private int logicalHeight;
   private TagKey infiniburn;
   private float ambientLight;

   private DimensionTypeBuilder() {
      this.skybox = DimensionType.Skybox.OVERWORLD;
      this.cardinalLight = DimensionType.CardinalLight.DEFAULT;
      this.attributes = EnvironmentAttributeMap.create();
      this.timelines = MappedEntitySet.createEmpty();
      this.fixedTime = null;
      this.coordinateScale = 1.0D;
      this.minY = 0;
      this.height = 256;
      this.monsterSpawnLightLevel = new NBTInt(7);
      this.monsterSpawnBlockLightLimit = 7;
      this.hasSkylight = true;
      this.hasCeiling = false;
      this.logicalHeight = 256;
      this.infiniburn = BlockTags.INFINIBURN_OVERWORLD.getKey();
      this.ambientLight = 0.0F;
   }

   public static DimensionTypeBuilder dimensionTypeBuilder() {
      return new DimensionTypeBuilder();
   }

   public DimensionType build() {
      return this.build((TypesBuilderData)null);
   }

   public DimensionType build(@Nullable TypesBuilderData data) {
      return new StaticDimensionType(data, this.hasFixedTime, this.skybox, this.cardinalLight, this.attributes.copyImmutable(), this.timelines, this.fixedTime, this.natural, this.bedWorks, this.respawnAnchorWorks, this.effects, this.coordinateScale, this.minY, this.height, this.monsterSpawnLightLevel, this.monsterSpawnBlockLightLimit, this.hasSkylight, this.hasCeiling, this.logicalHeight, this.infiniburn, this.ambientLight);
   }

   public boolean isRespawnAnchorWorks() {
      return this.respawnAnchorWorks;
   }

   public DimensionTypeBuilder setRespawnAnchorWorks(boolean respawnAnchorWorks) {
      this.respawnAnchorWorks = respawnAnchorWorks;
      return this;
   }

   public boolean isHasFixedTime() {
      return this.hasFixedTime;
   }

   public DimensionTypeBuilder setHasFixedTime(boolean hasFixedTime) {
      this.hasFixedTime = hasFixedTime;
      return this;
   }

   public DimensionType.Skybox getSkybox() {
      return this.skybox;
   }

   public DimensionTypeBuilder setSkybox(DimensionType.Skybox skybox) {
      this.skybox = skybox;
      return this;
   }

   public DimensionType.CardinalLight getCardinalLight() {
      return this.cardinalLight;
   }

   public DimensionTypeBuilder setCardinalLight(DimensionType.CardinalLight cardinalLight) {
      this.cardinalLight = cardinalLight;
      return this;
   }

   public EnvironmentAttributeMap getAttributes() {
      return this.attributes;
   }

   public <T> DimensionTypeBuilder setAttribute(EnvironmentAttribute<T> attribute, T value) {
      return this.setAttribute(attribute, value, AttributeModifier.override());
   }

   public <T, A> DimensionTypeBuilder setAttribute(EnvironmentAttribute<T> attribute, A value, AttributeModifier<T, A> modifier) {
      this.attributes.set(attribute, value, modifier);
      return this;
   }

   public DimensionTypeBuilder setAttributes(EnvironmentAttributeMap attributes) {
      this.attributes.setAll(attributes);
      return this;
   }

   public MappedEntityRefSet<Timeline> getTimelines() {
      return this.timelines;
   }

   public DimensionTypeBuilder setTimelines(MappedEntityRefSet<Timeline> timelines) {
      this.timelines = timelines;
      return this;
   }

   @Nullable
   public Long getFixedTime() {
      return this.fixedTime;
   }

   public DimensionTypeBuilder setFixedTime(@Nullable Long fixedTime) {
      this.hasFixedTime = fixedTime != null;
      this.fixedTime = fixedTime;
      return this;
   }

   public boolean isNatural() {
      return this.natural;
   }

   public DimensionTypeBuilder setNatural(boolean natural) {
      this.natural = natural;
      return this;
   }

   public boolean isBedWorks() {
      return this.bedWorks;
   }

   public DimensionTypeBuilder setBedWorks(boolean bedWorks) {
      this.bedWorks = bedWorks;
      return this;
   }

   public ResourceLocation getEffects() {
      return this.effects;
   }

   public DimensionTypeBuilder setEffects(ResourceLocation effects) {
      this.effects = effects;
      return this;
   }

   public double getCoordinateScale() {
      return this.coordinateScale;
   }

   public DimensionTypeBuilder setCoordinateScale(double coordinateScale) {
      this.coordinateScale = coordinateScale;
      return this;
   }

   public int getMinY() {
      return this.minY;
   }

   public DimensionTypeBuilder setMinY(int minY) {
      this.minY = minY;
      return this;
   }

   public int getHeight() {
      return this.height;
   }

   public DimensionTypeBuilder setHeight(int height) {
      this.height = height;
      return this;
   }

   @ApiStatus.Experimental
   public NBT getMonsterSpawnLightLevel() {
      return this.monsterSpawnLightLevel;
   }

   @ApiStatus.Experimental
   public DimensionTypeBuilder setMonsterSpawnLightLevel(NBT monsterSpawnLightLevel) {
      this.monsterSpawnLightLevel = monsterSpawnLightLevel;
      return this;
   }

   public int getMonsterSpawnBlockLightLimit() {
      return this.monsterSpawnBlockLightLimit;
   }

   public DimensionTypeBuilder setMonsterSpawnBlockLightLimit(int monsterSpawnBlockLightLimit) {
      this.monsterSpawnBlockLightLimit = monsterSpawnBlockLightLimit;
      return this;
   }

   public boolean isHasSkylight() {
      return this.hasSkylight;
   }

   public DimensionTypeBuilder setHasSkylight(boolean hasSkylight) {
      this.hasSkylight = hasSkylight;
      return this;
   }

   public boolean isHasCeiling() {
      return this.hasCeiling;
   }

   public DimensionTypeBuilder setHasCeiling(boolean hasCeiling) {
      this.hasCeiling = hasCeiling;
      return this;
   }

   public int getLogicalHeight() {
      return this.logicalHeight;
   }

   public DimensionTypeBuilder setLogicalHeight(int logicalHeight) {
      this.logicalHeight = logicalHeight;
      return this;
   }

   public TagKey getInfiniburn() {
      return this.infiniburn;
   }

   public DimensionTypeBuilder setInfiniburn(TagKey infiniburn) {
      this.infiniburn = infiniburn;
      return this;
   }

   public float getAmbientLight() {
      return this.ambientLight;
   }

   public DimensionTypeBuilder setAmbientLight(float ambientLight) {
      this.ambientLight = ambientLight;
      return this;
   }
}
