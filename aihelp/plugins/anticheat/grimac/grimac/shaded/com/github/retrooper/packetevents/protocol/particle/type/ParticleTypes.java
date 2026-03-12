package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleBlockStateData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleColorData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleDustColorTransitionData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleDustData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleItemStackData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticlePowerData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleSculkChargeData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleShriekData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleSpellData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleTrailData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleVibrationData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ParticleTypes {
   private static final VersionedRegistry<ParticleType<?>> REGISTRY = new VersionedRegistry("particle_type");
   public static final NbtCodec<ParticleType<?>> CODEC;
   /** @deprecated */
   @Deprecated
   public static final ParticleType<ParticleData> AMBIENT_ENTITY_EFFECT;
   public static final ParticleType<ParticleData> ANGRY_VILLAGER;
   public static final ParticleType<ParticleBlockStateData> BLOCK;
   @ApiStatus.Obsolete
   public static final ParticleType<ParticleData> BARRIER;
   @ApiStatus.Obsolete
   public static final ParticleType<ParticleData> LIGHT;
   public static final ParticleType<ParticleBlockStateData> BLOCK_MARKER;
   public static final ParticleType<ParticleData> BUBBLE;
   public static final ParticleType<ParticleData> CLOUD;
   public static final ParticleType<ParticleData> CRIT;
   public static final ParticleType<ParticleData> DAMAGE_INDICATOR;
   public static final ParticleType<ParticlePowerData> DRAGON_BREATH;
   public static final ParticleType<ParticleData> DRIPPING_LAVA;
   public static final ParticleType<ParticleData> FALLING_LAVA;
   public static final ParticleType<ParticleData> LANDING_LAVA;
   public static final ParticleType<ParticleData> DRIPPING_WATER;
   public static final ParticleType<ParticleData> FALLING_WATER;
   public static final ParticleType<ParticleDustData> DUST;
   public static final ParticleType<ParticleDustColorTransitionData> DUST_COLOR_TRANSITION;
   public static final ParticleType<ParticleSpellData> EFFECT;
   public static final ParticleType<ParticleData> ELDER_GUARDIAN;
   public static final ParticleType<ParticleData> ENCHANTED_HIT;
   public static final ParticleType<ParticleData> ENCHANT;
   public static final ParticleType<ParticleData> END_ROD;
   public static final ParticleType<ParticleColorData> ENTITY_EFFECT;
   public static final ParticleType<ParticleData> EXPLOSION_EMITTER;
   public static final ParticleType<ParticleData> EXPLOSION;
   public static final ParticleType<ParticleData> SONIC_BOOM;
   public static final ParticleType<ParticleBlockStateData> FALLING_DUST;
   public static final ParticleType<ParticleData> FIREWORK;
   public static final ParticleType<ParticleData> FISHING;
   public static final ParticleType<ParticleData> FLAME;
   public static final ParticleType<ParticleData> SCULK_SOUL;
   public static final ParticleType<ParticleSculkChargeData> SCULK_CHARGE;
   public static final ParticleType<ParticleData> SCULK_CHARGE_POP;
   public static final ParticleType<ParticleData> SOUL_FIRE_FLAME;
   public static final ParticleType<ParticleData> SOUL;
   public static final ParticleType<ParticleColorData> FLASH;
   public static final ParticleType<ParticleData> HAPPY_VILLAGER;
   public static final ParticleType<ParticleData> COMPOSTER;
   public static final ParticleType<ParticleData> HEART;
   public static final ParticleType<ParticleSpellData> INSTANT_EFFECT;
   public static final ParticleType<ParticleItemStackData> ITEM;
   public static final ParticleType<ParticleVibrationData> VIBRATION;
   public static final ParticleType<ParticleData> ITEM_SLIME;
   public static final ParticleType<ParticleData> ITEM_SNOWBALL;
   public static final ParticleType<ParticleData> LARGE_SMOKE;
   public static final ParticleType<ParticleData> LAVA;
   public static final ParticleType<ParticleData> MYCELIUM;
   public static final ParticleType<ParticleData> NOTE;
   public static final ParticleType<ParticleData> POOF;
   public static final ParticleType<ParticleData> PORTAL;
   public static final ParticleType<ParticleData> RAIN;
   public static final ParticleType<ParticleData> SMOKE;
   public static final ParticleType<ParticleData> SNEEZE;
   public static final ParticleType<ParticleData> SPIT;
   public static final ParticleType<ParticleData> SQUID_INK;
   public static final ParticleType<ParticleData> SWEEP_ATTACK;
   public static final ParticleType<ParticleData> TOTEM_OF_UNDYING;
   public static final ParticleType<ParticleData> UNDERWATER;
   public static final ParticleType<ParticleData> SPLASH;
   public static final ParticleType<ParticleData> WITCH;
   public static final ParticleType<ParticleData> BUBBLE_POP;
   public static final ParticleType<ParticleData> CURRENT_DOWN;
   public static final ParticleType<ParticleData> BUBBLE_COLUMN_UP;
   public static final ParticleType<ParticleData> NAUTILUS;
   public static final ParticleType<ParticleData> DOLPHIN;
   public static final ParticleType<ParticleData> CAMPFIRE_COSY_SMOKE;
   public static final ParticleType<ParticleData> CAMPFIRE_SIGNAL_SMOKE;
   public static final ParticleType<ParticleData> DRIPPING_HONEY;
   public static final ParticleType<ParticleData> FALLING_HONEY;
   public static final ParticleType<ParticleData> LANDING_HONEY;
   public static final ParticleType<ParticleData> FALLING_NECTAR;
   public static final ParticleType<ParticleData> FALLING_SPORE_BLOSSOM;
   public static final ParticleType<ParticleData> ASH;
   public static final ParticleType<ParticleData> CRIMSON_SPORE;
   public static final ParticleType<ParticleData> WARPED_SPORE;
   public static final ParticleType<ParticleData> SPORE_BLOSSOM_AIR;
   public static final ParticleType<ParticleData> DRIPPING_OBSIDIAN_TEAR;
   public static final ParticleType<ParticleData> FALLING_OBSIDIAN_TEAR;
   public static final ParticleType<ParticleData> LANDING_OBSIDIAN_TEAR;
   public static final ParticleType<ParticleData> REVERSE_PORTAL;
   public static final ParticleType<ParticleData> WHITE_ASH;
   public static final ParticleType<ParticleData> SMALL_FLAME;
   public static final ParticleType<ParticleData> SNOWFLAKE;
   public static final ParticleType<ParticleData> DRIPPING_DRIPSTONE_LAVA;
   public static final ParticleType<ParticleData> FALLING_DRIPSTONE_LAVA;
   public static final ParticleType<ParticleData> DRIPPING_DRIPSTONE_WATER;
   public static final ParticleType<ParticleData> FALLING_DRIPSTONE_WATER;
   public static final ParticleType<ParticleData> GLOW_SQUID_INK;
   public static final ParticleType<ParticleData> GLOW;
   public static final ParticleType<ParticleData> WAX_ON;
   public static final ParticleType<ParticleData> WAX_OFF;
   public static final ParticleType<ParticleData> ELECTRIC_SPARK;
   public static final ParticleType<ParticleData> SCRAPE;
   public static final ParticleType<ParticleShriekData> SHRIEK;
   public static final ParticleType<ParticleData> DRIPPING_CHERRY_LEAVES;
   public static final ParticleType<ParticleData> FALLING_CHERRY_LEAVES;
   public static final ParticleType<ParticleData> LANDING_CHERRY_LEAVES;
   public static final ParticleType<ParticleData> CHERRY_LEAVES;
   public static final ParticleType<ParticleData> EGG_CRACK;
   public static final ParticleType<ParticleData> GUST;
   /** @deprecated */
   @Deprecated
   public static final ParticleType<ParticleData> GUST_EMITTER;
   public static final ParticleType<ParticleData> WHITE_SMOKE;
   public static final ParticleType<ParticleData> DUST_PLUME;
   public static final ParticleType<ParticleData> GUST_DUST;
   public static final ParticleType<ParticleData> TRIAL_SPAWNER_DETECTION;
   public static final ParticleType<ParticleData> SMALL_GUST;
   public static final ParticleType<ParticleData> GUST_EMITTER_LARGE;
   public static final ParticleType<ParticleData> GUST_EMITTER_SMALL;
   public static final ParticleType<ParticleData> INFESTED;
   public static final ParticleType<ParticleData> ITEM_COBWEB;
   public static final ParticleType<ParticleData> TRIAL_SPAWNER_DETECTION_OMINOUS;
   public static final ParticleType<ParticleData> VAULT_CONNECTION;
   public static final ParticleType<ParticleBlockStateData> DUST_PILLAR;
   public static final ParticleType<ParticleData> OMINOUS_SPAWNING;
   public static final ParticleType<ParticleData> RAID_OMEN;
   public static final ParticleType<ParticleData> TRIAL_OMEN;
   public static final ParticleType<ParticleTrailData> TRAIL;
   public static final ParticleType<ParticleBlockStateData> BLOCK_CRUMBLE;
   public static final ParticleType<ParticleData> PALE_OAK_LEAVES;
   public static final ParticleType<ParticleColorData> TINTED_LEAVES;
   public static final ParticleType<ParticleData> FIREFLY;
   public static final ParticleType<ParticleData> COPPER_FIRE_FLAME;

   private ParticleTypes() {
   }

   public static VersionedRegistry<ParticleType<?>> getRegistry() {
      return REGISTRY;
   }

   @ApiStatus.Internal
   public static ParticleType<ParticleData> define(String name) {
      return define(name, (wrapper) -> {
         return ParticleData.emptyData();
      }, (PacketWrapper.Writer)null, (nbt, version) -> {
         return ParticleData.emptyData();
      }, (ParticleTypes.Encoder)null);
   }

   @ApiStatus.Internal
   public static <T extends ParticleData> ParticleType<T> define(String name, PacketWrapper.Reader<T> reader, @Nullable PacketWrapper.Writer<T> writer, ParticleTypes.Decoder<T> decoder, @Nullable ParticleTypes.Encoder<T> encoder) {
      return (ParticleType)REGISTRY.define(name, (data) -> {
         return new StaticParticleType(data, reader, writer, decoder, encoder);
      });
   }

   public static ParticleType<?> getByName(String name) {
      return (ParticleType)REGISTRY.getByName(name);
   }

   public static ParticleType<?> getById(ClientVersion version, int id) {
      return (ParticleType)REGISTRY.getById(version, id);
   }

   public static Collection<ParticleType<?>> values() {
      return REGISTRY.getEntries();
   }

   static {
      CODEC = NbtCodecs.forRegistry(REGISTRY);
      AMBIENT_ENTITY_EFFECT = define("ambient_entity_effect");
      ANGRY_VILLAGER = define("angry_villager");
      BLOCK = define("block", ParticleBlockStateData::read, ParticleBlockStateData::write, ParticleBlockStateData::decode, ParticleBlockStateData::encode);
      BARRIER = define("barrier");
      LIGHT = define("light");
      BLOCK_MARKER = define("block_marker", ParticleBlockStateData::read, ParticleBlockStateData::write, ParticleBlockStateData::decode, ParticleBlockStateData::encode);
      BUBBLE = define("bubble");
      CLOUD = define("cloud");
      CRIT = define("crit");
      DAMAGE_INDICATOR = define("damage_indicator");
      DRAGON_BREATH = define("dragon_breath", ParticlePowerData::read, ParticlePowerData::write, ParticlePowerData::decode, ParticlePowerData::encode);
      DRIPPING_LAVA = define("dripping_lava");
      FALLING_LAVA = define("falling_lava");
      LANDING_LAVA = define("landing_lava");
      DRIPPING_WATER = define("dripping_water");
      FALLING_WATER = define("falling_water");
      DUST = define("dust", ParticleDustData::read, ParticleDustData::write, ParticleDustData::decode, ParticleDustData::encode);
      DUST_COLOR_TRANSITION = define("dust_color_transition", ParticleDustColorTransitionData::read, ParticleDustColorTransitionData::write, ParticleDustColorTransitionData::decode, ParticleDustColorTransitionData::encode);
      EFFECT = define("effect", ParticleSpellData::read, ParticleSpellData::write, ParticleSpellData::decode, ParticleSpellData::encode);
      ELDER_GUARDIAN = define("elder_guardian");
      ENCHANTED_HIT = define("enchanted_hit");
      ENCHANT = define("enchant");
      END_ROD = define("end_rod");
      ENTITY_EFFECT = define("entity_effect", ParticleColorData::read, ParticleColorData::write, ParticleColorData::decode, ParticleColorData::encode);
      EXPLOSION_EMITTER = define("explosion_emitter");
      EXPLOSION = define("explosion");
      SONIC_BOOM = define("sonic_boom");
      FALLING_DUST = define("falling_dust", ParticleBlockStateData::read, ParticleBlockStateData::write, ParticleBlockStateData::decode, ParticleBlockStateData::encode);
      FIREWORK = define("firework");
      FISHING = define("fishing");
      FLAME = define("flame");
      SCULK_SOUL = define("sculk_soul");
      SCULK_CHARGE = define("sculk_charge", ParticleSculkChargeData::read, ParticleSculkChargeData::write, ParticleSculkChargeData::decode, ParticleSculkChargeData::encode);
      SCULK_CHARGE_POP = define("sculk_charge_pop");
      SOUL_FIRE_FLAME = define("soul_fire_flame");
      SOUL = define("soul");
      FLASH = define("flash", (wrapper) -> {
         return wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9) ? ParticleColorData.read(wrapper) : new ParticleColorData(0);
      }, (wrapper, data) -> {
         if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            ParticleColorData.write(wrapper, data);
         }

      }, (compound, version) -> {
         return version.isNewerThanOrEquals(ClientVersion.V_1_21_9) ? ParticleColorData.decode(compound, version) : new ParticleColorData(0);
      }, (data, version, compound) -> {
         if (version.isNewerThanOrEquals(ClientVersion.V_1_21_9)) {
            ParticleColorData.encode(data, version, compound);
         }

      });
      HAPPY_VILLAGER = define("happy_villager");
      COMPOSTER = define("composter");
      HEART = define("heart");
      INSTANT_EFFECT = define("instant_effect", ParticleSpellData::read, ParticleSpellData::write, ParticleSpellData::decode, ParticleSpellData::encode);
      ITEM = define("item", ParticleItemStackData::read, ParticleItemStackData::write, ParticleItemStackData::decode, ParticleItemStackData::encode);
      VIBRATION = define("vibration", ParticleVibrationData::read, ParticleVibrationData::write, ParticleVibrationData::decode, ParticleVibrationData::encode);
      ITEM_SLIME = define("item_slime");
      ITEM_SNOWBALL = define("item_snowball");
      LARGE_SMOKE = define("large_smoke");
      LAVA = define("lava");
      MYCELIUM = define("mycelium");
      NOTE = define("note");
      POOF = define("poof");
      PORTAL = define("portal");
      RAIN = define("rain");
      SMOKE = define("smoke");
      SNEEZE = define("sneeze");
      SPIT = define("spit");
      SQUID_INK = define("squid_ink");
      SWEEP_ATTACK = define("sweep_attack");
      TOTEM_OF_UNDYING = define("totem_of_undying");
      UNDERWATER = define("underwater");
      SPLASH = define("splash");
      WITCH = define("witch");
      BUBBLE_POP = define("bubble_pop");
      CURRENT_DOWN = define("current_down");
      BUBBLE_COLUMN_UP = define("bubble_column_up");
      NAUTILUS = define("nautilus");
      DOLPHIN = define("dolphin");
      CAMPFIRE_COSY_SMOKE = define("campfire_cosy_smoke");
      CAMPFIRE_SIGNAL_SMOKE = define("campfire_signal_smoke");
      DRIPPING_HONEY = define("dripping_honey");
      FALLING_HONEY = define("falling_honey");
      LANDING_HONEY = define("landing_honey");
      FALLING_NECTAR = define("falling_nectar");
      FALLING_SPORE_BLOSSOM = define("falling_spore_blossom");
      ASH = define("ash");
      CRIMSON_SPORE = define("crimson_spore");
      WARPED_SPORE = define("warped_spore");
      SPORE_BLOSSOM_AIR = define("spore_blossom_air");
      DRIPPING_OBSIDIAN_TEAR = define("dripping_obsidian_tear");
      FALLING_OBSIDIAN_TEAR = define("falling_obsidian_tear");
      LANDING_OBSIDIAN_TEAR = define("landing_obsidian_tear");
      REVERSE_PORTAL = define("reverse_portal");
      WHITE_ASH = define("white_ash");
      SMALL_FLAME = define("small_flame");
      SNOWFLAKE = define("snowflake");
      DRIPPING_DRIPSTONE_LAVA = define("dripping_dripstone_lava");
      FALLING_DRIPSTONE_LAVA = define("falling_dripstone_lava");
      DRIPPING_DRIPSTONE_WATER = define("dripping_dripstone_water");
      FALLING_DRIPSTONE_WATER = define("falling_dripstone_water");
      GLOW_SQUID_INK = define("glow_squid_ink");
      GLOW = define("glow");
      WAX_ON = define("wax_on");
      WAX_OFF = define("wax_off");
      ELECTRIC_SPARK = define("electric_spark");
      SCRAPE = define("scrape");
      SHRIEK = define("shriek", ParticleShriekData::read, ParticleShriekData::write, ParticleShriekData::decode, ParticleShriekData::encode);
      DRIPPING_CHERRY_LEAVES = define("dripping_cherry_leaves");
      FALLING_CHERRY_LEAVES = define("falling_cherry_leaves");
      LANDING_CHERRY_LEAVES = define("landing_cherry_leaves");
      CHERRY_LEAVES = define("cherry_leaves");
      EGG_CRACK = define("egg_crack");
      GUST = define("gust");
      GUST_EMITTER = define("gust_emitter");
      WHITE_SMOKE = define("white_smoke");
      DUST_PLUME = define("dust_plume");
      GUST_DUST = define("gust_dust");
      TRIAL_SPAWNER_DETECTION = define("trial_spawner_detection");
      SMALL_GUST = define("small_gust");
      GUST_EMITTER_LARGE = define("gust_emitter_large");
      GUST_EMITTER_SMALL = define("gust_emitter_small");
      INFESTED = define("infested");
      ITEM_COBWEB = define("item_cobweb");
      TRIAL_SPAWNER_DETECTION_OMINOUS = define("trial_spawner_detection_ominous");
      VAULT_CONNECTION = define("vault_connection");
      DUST_PILLAR = define("dust_pillar", ParticleBlockStateData::read, ParticleBlockStateData::write, ParticleBlockStateData::decode, ParticleBlockStateData::encode);
      OMINOUS_SPAWNING = define("ominous_spawning");
      RAID_OMEN = define("raid_omen");
      TRIAL_OMEN = define("trial_omen");
      TRAIL = define("trail", ParticleTrailData::read, ParticleTrailData::write, ParticleTrailData::decode, ParticleTrailData::encode);
      BLOCK_CRUMBLE = define("block_crumble", ParticleBlockStateData::read, ParticleBlockStateData::write, ParticleBlockStateData::decode, ParticleBlockStateData::encode);
      PALE_OAK_LEAVES = define("pale_oak_leaves");
      TINTED_LEAVES = define("tinted_leaves", ParticleColorData::read, ParticleColorData::write, ParticleColorData::decode, ParticleColorData::encode);
      FIREFLY = define("firefly");
      COPPER_FIRE_FLAME = define("copper_fire_flame");
      REGISTRY.unloadMappings();
   }

   @FunctionalInterface
   @ApiStatus.Internal
   public interface Decoder<T> {
      T decode(NBTCompound compound, ClientVersion version);
   }

   @FunctionalInterface
   @ApiStatus.Internal
   public interface Encoder<T> {
      void encode(T value, ClientVersion version, NBTCompound compound);
   }
}
