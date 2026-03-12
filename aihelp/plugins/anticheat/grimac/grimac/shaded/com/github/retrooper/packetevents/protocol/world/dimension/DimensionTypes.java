package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.AlphaColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.Particle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sounds;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.AmbientSounds;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.BackgroundMusic;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.BiomeEffects;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.VersionRange;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureNbtUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.nbt.CompoundBinaryTag;
import java.util.Collections;
import java.util.function.Consumer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DimensionTypes {
   private static final VersionedRegistry<DimensionType> REGISTRY;
   @ApiStatus.Obsolete
   public static final DimensionType OVERWORLD_PRE_1_18;
   public static final DimensionType OVERWORLD;
   @ApiStatus.Obsolete
   public static final DimensionType OVERWORLD_CAVES_PRE_1_18;
   public static final DimensionType OVERWORLD_CAVES;
   private static final Consumer<DimensionTypeBuilder> THE_END_COMMON;
   @ApiStatus.Obsolete
   public static final DimensionType THE_END_PRE_1_21_9;
   public static final DimensionType THE_END;
   public static final DimensionType THE_NETHER;

   private DimensionTypes() {
   }

   @ApiStatus.Internal
   public static DimensionType define(String name, Consumer<DimensionTypeBuilder> builder) {
      return define(name, VersionRange.ALL_VERSIONS, builder);
   }

   @ApiStatus.Internal
   public static DimensionType define(String name, VersionRange range, Consumer<DimensionTypeBuilder> builder) {
      return (DimensionType)REGISTRY.define(name, range, (data) -> {
         DimensionTypeBuilder dimBuilder = DimensionTypeBuilder.dimensionTypeBuilder();
         builder.accept(dimBuilder);
         return dimBuilder.build(data);
      });
   }

   public static VersionedRegistry<DimensionType> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY = new VersionedRegistry("dimension_type", new ClientVersion[]{ClientVersion.V_1_18});
      OVERWORLD_PRE_1_18 = define("overworld", new VersionRange((ClientVersion)null, ClientVersion.V_1_17_1), (builder) -> {
         builder.setCoordinateScale(1.0D).setMinY(0).setHeight(256).setLogicalHeight(256).setInfiniburn(BlockTags.INFINIBURN_OVERWORLD.getKey()).setHasCeiling(false).setHasSkylight(true).setAmbientLight(0.0F).setNatural(true).setRespawnAnchorWorks(false).setBedWorks(true).setEffects(new ResourceLocation("overworld")).setAttribute(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, true);
      });
      OVERWORLD = define("overworld", new VersionRange(ClientVersion.V_1_18, (ClientVersion)null), (builder) -> {
         builder.setMonsterSpawnLightLevel(AdventureNbtUtil.fromAdventure(((CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)CompoundBinaryTag.builder().putString("type", (new ResourceLocation("uniform")).toString())).putInt("min_inclusive", 0)).putInt("max_inclusive", 7)).put("value", ((CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)CompoundBinaryTag.builder().putInt("min_inclusive", 0)).putInt("max_inclusive", 7)).build())).build())).setMonsterSpawnBlockLightLimit(0).setCoordinateScale(1.0D).setMinY(-64).setHeight(384).setLogicalHeight(384).setInfiniburn(BlockTags.INFINIBURN_OVERWORLD.getKey()).setHasCeiling(false).setHasSkylight(true).setAmbientLight(0.0F).setAttribute(EnvironmentAttributes.AUDIO_BACKGROUND_MUSIC, new BackgroundMusic(new BiomeEffects.MusicSettings(Sounds.MUSIC_GAME, 12000, 24000, false), new BiomeEffects.MusicSettings(Sounds.MUSIC_CREATIVE, 12000, 24000, false), (BiomeEffects.MusicSettings)null)).setAttribute(EnvironmentAttributes.AUDIO_AMBIENT_SOUNDS, new AmbientSounds((Sound)null, new BiomeEffects.MoodSettings(Sounds.AMBIENT_CAVE, 6000, 8, 2.0D), Collections.emptyList())).setAttribute(EnvironmentAttributes.VISUAL_CLOUD_HEIGHT, 192.33F).setAttribute(EnvironmentAttributes.VISUAL_CLOUD_COLOR, new AlphaColor(-855638017)).setAttribute(EnvironmentAttributes.VISUAL_FOG_COLOR, new Color(12638463)).setAttribute(EnvironmentAttributes.VISUAL_SKY_COLOR, new Color(7907327)).setNatural(true).setRespawnAnchorWorks(false).setBedWorks(true).setEffects(new ResourceLocation("overworld")).setAttribute(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, true);
      });
      OVERWORLD_CAVES_PRE_1_18 = define("overworld_caves", new VersionRange((ClientVersion)null, ClientVersion.V_1_17_1), (builder) -> {
         builder.setCoordinateScale(1.0D).setMinY(0).setHeight(256).setLogicalHeight(256).setInfiniburn(BlockTags.INFINIBURN_OVERWORLD.getKey()).setHasCeiling(true).setHasSkylight(true).setAmbientLight(0.0F).setNatural(true).setRespawnAnchorWorks(false).setBedWorks(true).setEffects(new ResourceLocation("overworld")).setAttribute(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, true);
      });
      OVERWORLD_CAVES = define("overworld_caves", new VersionRange(ClientVersion.V_1_18, (ClientVersion)null), (builder) -> {
         builder.setMonsterSpawnLightLevel(AdventureNbtUtil.fromAdventure(((CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)CompoundBinaryTag.builder().putString("type", (new ResourceLocation("uniform")).toString())).putInt("min_inclusive", 0)).putInt("max_inclusive", 7)).put("value", ((CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)CompoundBinaryTag.builder().putInt("min_inclusive", 0)).putInt("max_inclusive", 7)).build())).build())).setMonsterSpawnBlockLightLimit(0).setCoordinateScale(1.0D).setMinY(-64).setHeight(384).setLogicalHeight(384).setInfiniburn(BlockTags.INFINIBURN_OVERWORLD.getKey()).setHasCeiling(true).setHasSkylight(true).setAmbientLight(0.0F).setAttribute(EnvironmentAttributes.AUDIO_BACKGROUND_MUSIC, new BackgroundMusic(new BiomeEffects.MusicSettings(Sounds.MUSIC_GAME, 12000, 24000, false), new BiomeEffects.MusicSettings(Sounds.MUSIC_CREATIVE, 12000, 24000, false), (BiomeEffects.MusicSettings)null)).setAttribute(EnvironmentAttributes.AUDIO_AMBIENT_SOUNDS, new AmbientSounds((Sound)null, new BiomeEffects.MoodSettings(Sounds.AMBIENT_CAVE, 6000, 8, 2.0D), Collections.emptyList())).setAttribute(EnvironmentAttributes.VISUAL_CLOUD_HEIGHT, 192.33F).setAttribute(EnvironmentAttributes.VISUAL_CLOUD_COLOR, new AlphaColor(-855638017)).setAttribute(EnvironmentAttributes.VISUAL_FOG_COLOR, new Color(12638463)).setAttribute(EnvironmentAttributes.VISUAL_SKY_COLOR, new Color(7907327)).setNatural(true).setRespawnAnchorWorks(false).setBedWorks(true).setEffects(new ResourceLocation("overworld")).setAttribute(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, true);
      });
      THE_END_COMMON = (builder) -> {
         builder.setHasFixedTime(true).setMonsterSpawnBlockLightLimit(0).setMonsterSpawnLightLevel(new NBTInt(15)).setCoordinateScale(1.0D).setSkybox(DimensionType.Skybox.END).setMinY(0).setHeight(256).setLogicalHeight(256).setInfiniburn(BlockTags.INFINIBURN_END.getKey()).setHasCeiling(false).setHasSkylight(true).setAmbientLight(0.25F).setAttribute(EnvironmentAttributes.AUDIO_BACKGROUND_MUSIC, new BackgroundMusic(new BiomeEffects.MusicSettings(Sounds.MUSIC_END, 6000, 24000, true), (BiomeEffects.MusicSettings)null, (BiomeEffects.MusicSettings)null)).setAttribute(EnvironmentAttributes.AUDIO_AMBIENT_SOUNDS, new AmbientSounds((Sound)null, new BiomeEffects.MoodSettings(Sounds.AMBIENT_CAVE, 6000, 8, 2.0D), Collections.emptyList())).setAttribute(EnvironmentAttributes.VISUAL_FOG_COLOR, new Color(1577752)).setAttribute(EnvironmentAttributes.VISUAL_SKY_COLOR, new Color(0)).setAttribute(EnvironmentAttributes.VISUAL_SKY_LIGHT_COLOR, new Color(15040767)).setAttribute(EnvironmentAttributes.VISUAL_SKY_LIGHT_FACTOR, 0.0F).setNatural(false).setRespawnAnchorWorks(false).setBedWorks(false).setEffects(new ResourceLocation("the_end")).setFixedTime(6000L).setAttribute(EnvironmentAttributes.GAMEPLAY_CAN_START_RAID, true);
      };
      THE_END_PRE_1_21_9 = define("the_end", new VersionRange((ClientVersion)null, ClientVersion.V_1_21_7), THE_END_COMMON.andThen((builder) -> {
         builder.setHasSkylight(false);
      }));
      THE_END = define("the_end", new VersionRange(ClientVersion.V_1_21_9, (ClientVersion)null), THE_END_COMMON);
      THE_NETHER = define("the_nether", (builder) -> {
         builder.setHasFixedTime(true).setMonsterSpawnLightLevel(new NBTInt(7)).setMonsterSpawnBlockLightLimit(15).setCoordinateScale(8.0D).setSkybox(DimensionType.Skybox.NONE).setCardinalLight(DimensionType.CardinalLight.NETHER).setMinY(0).setHeight(256).setLogicalHeight(128).setInfiniburn(BlockTags.INFINIBURN_NETHER.getKey()).setHasCeiling(true).setHasSkylight(false).setAmbientLight(0.1F).setAttribute(EnvironmentAttributes.VISUAL_SKY_LIGHT_FACTOR, 0.0F).setAttribute(EnvironmentAttributes.VISUAL_SKY_LIGHT_COLOR, new Color(8026879)).setAttribute(EnvironmentAttributes.GAMEPLAY_SKY_LIGHT_LEVEL, 4.0F).setAttribute(EnvironmentAttributes.VISUAL_FOG_START_DISTANCE, 10.0F).setAttribute(EnvironmentAttributes.VISUAL_FOG_END_DISTANCE, 96.0F).setAttribute(EnvironmentAttributes.VISUAL_DEFAULT_DRIPSTONE_PARTICLE, new Particle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA)).setAttribute(EnvironmentAttributes.GAMEPLAY_PIGLINS_ZOMBIFY, false).setAttribute(EnvironmentAttributes.GAMEPLAY_FAST_LAVA, true).setAttribute(EnvironmentAttributes.GAMEPLAY_WATER_EVAPORATES, true).setNatural(false).setRespawnAnchorWorks(true).setBedWorks(false).setEffects(new ResourceLocation("the_nether")).setFixedTime(18000L);
      });
      REGISTRY.unloadMappings();
   }
}
