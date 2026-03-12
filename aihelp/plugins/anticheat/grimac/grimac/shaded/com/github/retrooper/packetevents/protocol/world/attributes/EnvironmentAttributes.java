package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.AlphaColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.Particle;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.BiomeEffects;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.util.TriState;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class EnvironmentAttributes {
   private static final VersionedRegistry<EnvironmentAttribute<?>> REGISTRY = new VersionedRegistry("environment_attribute");
   public static final NbtCodec<EnvironmentAttribute<?>> CODEC;
   public static EnvironmentAttribute<Color> VISUAL_FOG_COLOR;
   public static EnvironmentAttribute<Float> VISUAL_FOG_START_DISTANCE;
   public static EnvironmentAttribute<Float> VISUAL_FOG_END_DISTANCE;
   public static EnvironmentAttribute<Float> VISUAL_SKY_FOG_END_DISTANCE;
   public static EnvironmentAttribute<Float> VISUAL_CLOUD_FOG_END_DISTANCE;
   public static EnvironmentAttribute<Color> VISUAL_WATER_FOG_COLOR;
   public static EnvironmentAttribute<Float> VISUAL_WATER_FOG_START_DISTANCE;
   public static EnvironmentAttribute<Float> VISUAL_WATER_FOG_END_DISTANCE;
   public static EnvironmentAttribute<Color> VISUAL_SKY_COLOR;
   public static EnvironmentAttribute<AlphaColor> VISUAL_SUNRISE_SUNSET_COLOR;
   public static EnvironmentAttribute<AlphaColor> VISUAL_CLOUD_COLOR;
   public static EnvironmentAttribute<Float> VISUAL_CLOUD_HEIGHT;
   public static EnvironmentAttribute<Float> VISUAL_SUN_ANGLE;
   public static EnvironmentAttribute<Float> VISUAL_MOON_ANGLE;
   public static EnvironmentAttribute<Float> VISUAL_STAR_ANGLE;
   public static EnvironmentAttribute<MoonPhase> VISUAL_MOON_PHASE;
   public static EnvironmentAttribute<Float> VISUAL_STAR_BRIGHTNESS;
   public static EnvironmentAttribute<Color> VISUAL_SKY_LIGHT_COLOR;
   public static EnvironmentAttribute<Float> VISUAL_SKY_LIGHT_FACTOR;
   public static EnvironmentAttribute<Particle<?>> VISUAL_DEFAULT_DRIPSTONE_PARTICLE;
   public static EnvironmentAttribute<List<BiomeEffects.ParticleSettings>> VISUAL_AMBIENT_PARTICLES;
   public static EnvironmentAttribute<BackgroundMusic> AUDIO_BACKGROUND_MUSIC;
   public static EnvironmentAttribute<Float> AUDIO_MUSIC_VOLUME;
   public static EnvironmentAttribute<AmbientSounds> AUDIO_AMBIENT_SOUNDS;
   public static EnvironmentAttribute<Boolean> AUDIO_FIREFLY_BUSH_SOUNDS;
   public static EnvironmentAttribute<Float> GAMEPLAY_SKY_LIGHT_LEVEL;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<Boolean> GAMEPLAY_CAN_START_RAID;
   public static EnvironmentAttribute<Boolean> GAMEPLAY_WATER_EVAPORATES;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<?> GAMEPLAY_BED_RULE;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<Boolean> GAMEPLAY_RESPAWN_ANCHOR_WORKS;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<Boolean> GAMEPLAY_NETHER_PORTAL_SPAWNS_PIGLIN;
   public static EnvironmentAttribute<Boolean> GAMEPLAY_FAST_LAVA;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<Boolean> GAMEPLAY_INCREASED_FIRE_BURNOUT;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<TriState> GAMEPLAY_EYEBLOSSOM_OPEN;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<Float> GAMEPLAY_TURTLE_EGG_HATCH_CHANCE;
   public static EnvironmentAttribute<Boolean> GAMEPLAY_PIGLINS_ZOMBIFY;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<Boolean> GAMEPLAY_SNOW_GOLEM_MELTS;
   public static EnvironmentAttribute<Boolean> GAMEPLAY_CREAKING_ACTIVE;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<Float> GAMEPLAY_SURFACE_SLIME_SPAWN_CHANCE;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<Float> GAMEPLAY_CAT_WAKING_UP_GIFT_CHANCE;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<Boolean> GAMEPLAY_BEES_STAY_IN_HIVE;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<Boolean> GAMEPLAY_MONSTERS_BURN;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<Boolean> GAMEPLAY_CAN_PILLAGER_PATROL_SPAWN;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<?> GAMEPLAY_VILLAGER_ACTIVITY;
   @ApiStatus.Obsolete
   public static EnvironmentAttribute<?> GAMEPLAY_BABY_VILLAGER_ACTIVITY;

   private EnvironmentAttributes() {
   }

   public static VersionedRegistry<EnvironmentAttribute<?>> getRegistry() {
      return REGISTRY;
   }

   @ApiStatus.Internal
   public static <T> EnvironmentAttribute<T> defineUnsynced(String name) {
      return (EnvironmentAttribute)REGISTRY.define(name, (data) -> {
         return new StaticEnvironmentAttribute(data, (AttributeType)null, (Object)null);
      });
   }

   @ApiStatus.Internal
   public static <T> EnvironmentAttribute<T> define(String name, AttributeType<T> attributeType, T defaultValue) {
      return (EnvironmentAttribute)REGISTRY.define(name, (data) -> {
         return new StaticEnvironmentAttribute(data, attributeType, defaultValue);
      });
   }

   static {
      CODEC = NbtCodecs.forRegistry(REGISTRY);
      VISUAL_FOG_COLOR = define("visual/fog_color", AttributeTypes.RGB_COLOR, Color.BLACK);
      VISUAL_FOG_START_DISTANCE = define("visual/fog_start_distance", AttributeTypes.FLOAT, 0.0F);
      VISUAL_FOG_END_DISTANCE = define("visual/fog_end_distance", AttributeTypes.FLOAT, 1024.0F);
      VISUAL_SKY_FOG_END_DISTANCE = define("visual/sky_fog_end_distance", AttributeTypes.FLOAT, 512.0F);
      VISUAL_CLOUD_FOG_END_DISTANCE = define("visual/cloud_fog_end_distance", AttributeTypes.FLOAT, 2048.0F);
      VISUAL_WATER_FOG_COLOR = define("visual/water_fog_color", AttributeTypes.RGB_COLOR, new Color(16448205));
      VISUAL_WATER_FOG_START_DISTANCE = define("visual/water_fog_start_distance", AttributeTypes.FLOAT, -8.0F);
      VISUAL_WATER_FOG_END_DISTANCE = define("visual/water_fog_end_distance", AttributeTypes.FLOAT, 96.0F);
      VISUAL_SKY_COLOR = define("visual/sky_color", AttributeTypes.RGB_COLOR, Color.BLACK);
      VISUAL_SUNRISE_SUNSET_COLOR = define("visual/sunrise_sunset_color", AttributeTypes.ARGB_COLOR, AlphaColor.TRANSPARENT);
      VISUAL_CLOUD_COLOR = define("visual/cloud_color", AttributeTypes.ARGB_COLOR, AlphaColor.TRANSPARENT);
      VISUAL_CLOUD_HEIGHT = define("visual/cloud_height", AttributeTypes.FLOAT, 192.33F);
      VISUAL_SUN_ANGLE = define("visual/sun_angle", AttributeTypes.ANGLE_DEGREES, 0.0F);
      VISUAL_MOON_ANGLE = define("visual/moon_angle", AttributeTypes.ANGLE_DEGREES, 0.0F);
      VISUAL_STAR_ANGLE = define("visual/star_angle", AttributeTypes.ANGLE_DEGREES, 0.0F);
      VISUAL_MOON_PHASE = define("visual/moon_phase", AttributeTypes.MOON_PHASE, MoonPhase.FULL_MOON);
      VISUAL_STAR_BRIGHTNESS = define("visual/star_brightness", AttributeTypes.FLOAT, 0.0F);
      VISUAL_SKY_LIGHT_COLOR = define("visual/sky_light_color", AttributeTypes.RGB_COLOR, Color.WHITE);
      VISUAL_SKY_LIGHT_FACTOR = define("visual/sky_light_factor", AttributeTypes.FLOAT, 1.0F);
      VISUAL_DEFAULT_DRIPSTONE_PARTICLE = define("visual/default_dripstone_particle", AttributeTypes.PARTICLE, new Particle(ParticleTypes.DRIPPING_DRIPSTONE_WATER));
      VISUAL_AMBIENT_PARTICLES = define("visual/ambient_particles", AttributeTypes.AMBIENT_PARTICLES, Collections.emptyList());
      AUDIO_BACKGROUND_MUSIC = define("audio/background_music", AttributeTypes.BACKGROUND_MUSIC, BackgroundMusic.EMPTY);
      AUDIO_MUSIC_VOLUME = define("audio/music_volume", AttributeTypes.FLOAT, 1.0F);
      AUDIO_AMBIENT_SOUNDS = define("audio/ambient_sounds", AttributeTypes.AMBIENT_SOUNDS, AmbientSounds.EMPTY);
      AUDIO_FIREFLY_BUSH_SOUNDS = define("audio/firefly_bush_sounds", AttributeTypes.BOOLEAN, false);
      GAMEPLAY_SKY_LIGHT_LEVEL = define("gameplay/sky_light_level", AttributeTypes.FLOAT, 15.0F);
      GAMEPLAY_CAN_START_RAID = defineUnsynced("gameplay/can_start_raid");
      GAMEPLAY_WATER_EVAPORATES = define("gameplay/water_evaporates", AttributeTypes.BOOLEAN, false);
      GAMEPLAY_BED_RULE = defineUnsynced("gameplay/bed_rule");
      GAMEPLAY_RESPAWN_ANCHOR_WORKS = defineUnsynced("gameplay/respawn_anchor_works");
      GAMEPLAY_NETHER_PORTAL_SPAWNS_PIGLIN = defineUnsynced("gameplay/nether_portal_spawns_piglin");
      GAMEPLAY_FAST_LAVA = define("gameplay/fast_lava", AttributeTypes.BOOLEAN, false);
      GAMEPLAY_INCREASED_FIRE_BURNOUT = defineUnsynced("gameplay/increased_fire_burnout");
      GAMEPLAY_EYEBLOSSOM_OPEN = defineUnsynced("gameplay/eyeblossom_open");
      GAMEPLAY_TURTLE_EGG_HATCH_CHANCE = defineUnsynced("gameplay/turtle_egg_hatch_chance");
      GAMEPLAY_PIGLINS_ZOMBIFY = define("gameplay/piglins_zombify", AttributeTypes.BOOLEAN, true);
      GAMEPLAY_SNOW_GOLEM_MELTS = defineUnsynced("gameplay/snow_golem_melts");
      GAMEPLAY_CREAKING_ACTIVE = define("gameplay/creaking_active", AttributeTypes.BOOLEAN, false);
      GAMEPLAY_SURFACE_SLIME_SPAWN_CHANCE = defineUnsynced("gameplay/surface_slime_spawn_chance");
      GAMEPLAY_CAT_WAKING_UP_GIFT_CHANCE = defineUnsynced("gameplay/cat_waking_up_gift_chance");
      GAMEPLAY_BEES_STAY_IN_HIVE = defineUnsynced("gameplay/bees_stay_in_hive");
      GAMEPLAY_MONSTERS_BURN = defineUnsynced("gameplay/monsters_burn");
      GAMEPLAY_CAN_PILLAGER_PATROL_SPAWN = defineUnsynced("gameplay/can_pillager_patrol_spawn");
      GAMEPLAY_VILLAGER_ACTIVITY = defineUnsynced("gameplay/villager_activity");
      GAMEPLAY_BABY_VILLAGER_ACTIVITY = defineUnsynced("gameplay/baby_villager_activity");
      REGISTRY.unloadMappings();
   }
}
