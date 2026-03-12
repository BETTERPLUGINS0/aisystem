package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collection;

public final class DamageTypes {
   private static final VersionedRegistry<DamageType> REGISTRY = new VersionedRegistry("damage_type");
   public static final DamageType ARROW = define("arrow", "arrow", 0.1F);
   public static final DamageType BAD_RESPAWN_POINT;
   public static final DamageType CACTUS;
   public static final DamageType CAMPFIRE;
   public static final DamageType CRAMMING;
   public static final DamageType DRAGON_BREATH;
   public static final DamageType DROWN;
   public static final DamageType DRY_OUT;
   public static final DamageType EXPLOSION;
   public static final DamageType FALL;
   public static final DamageType FALLING_ANVIL;
   public static final DamageType FALLING_BLOCK;
   public static final DamageType FALLING_STALACTITE;
   public static final DamageType FELL_OUT_OF_WORLD;
   public static final DamageType FIREBALL;
   public static final DamageType FIREWORKS;
   public static final DamageType FLY_INTO_WALL;
   public static final DamageType FREEZE;
   public static final DamageType GENERIC;
   public static final DamageType GENERIC_KILL;
   public static final DamageType HOT_FLOOR;
   public static final DamageType IN_FIRE;
   public static final DamageType IN_WALL;
   public static final DamageType INDIRECT_MAGIC;
   public static final DamageType LAVA;
   public static final DamageType LIGHTNING_BOLT;
   public static final DamageType MAGIC;
   public static final DamageType MOB_ATTACK;
   public static final DamageType MOB_ATTACK_NO_AGGRO;
   public static final DamageType MOB_PROJECTILE;
   public static final DamageType ON_FIRE;
   public static final DamageType OUTSIDE_BORDER;
   public static final DamageType PLAYER_ATTACK;
   public static final DamageType PLAYER_EXPLOSION;
   public static final DamageType SONIC_BOOM;
   public static final DamageType SPIT;
   public static final DamageType STALAGMITE;
   public static final DamageType STARVE;
   public static final DamageType STING;
   public static final DamageType SWEET_BERRY_BUSH;
   public static final DamageType THORNS;
   public static final DamageType THROWN;
   public static final DamageType TRIDENT;
   public static final DamageType UNATTRIBUTED_FIREBALL;
   public static final DamageType WIND_CHARGE;
   public static final DamageType WITHER;
   public static final DamageType WITHER_SKULL;
   public static final DamageType ENDER_PEARL;
   public static final DamageType MACE_SMASH;
   public static final DamageType SPEAR;

   private DamageTypes() {
   }

   @ApiStatus.Internal
   public static DamageType define(String key, String messageId, float exhaustion) {
      return define(key, messageId, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, exhaustion, DamageEffects.HURT, DeathMessageType.DEFAULT);
   }

   @ApiStatus.Internal
   public static DamageType define(String key, String messageId, float exhaustion, DamageEffects damageEffects) {
      return define(key, messageId, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, exhaustion, damageEffects, DeathMessageType.DEFAULT);
   }

   @ApiStatus.Internal
   public static DamageType define(String key, String messageId, DamageScaling scaling, float exhaustion) {
      return define(key, messageId, scaling, exhaustion, DamageEffects.HURT, DeathMessageType.DEFAULT);
   }

   @ApiStatus.Internal
   public static DamageType define(String key, String messageId, DamageScaling scaling, float exhaustion, DamageEffects damageEffects, DeathMessageType deathMessageType) {
      return (DamageType)REGISTRY.define(key, (data) -> {
         return new StaticDamageType(data, messageId, scaling, exhaustion, damageEffects, deathMessageType);
      });
   }

   public static DamageType getByName(String name) {
      return (DamageType)REGISTRY.getByName(name);
   }

   public static DamageType getById(ClientVersion version, int id) {
      return (DamageType)REGISTRY.getById(version, id);
   }

   public static VersionedRegistry<DamageType> getRegistry() {
      return REGISTRY;
   }

   public static Collection<DamageType> values() {
      return REGISTRY.getEntries();
   }

   static {
      BAD_RESPAWN_POINT = define("bad_respawn_point", "badRespawnPoint", DamageScaling.ALWAYS, 0.1F, DamageEffects.HURT, DeathMessageType.INTENTIONAL_GAME_DESIGN);
      CACTUS = define("cactus", "cactus", 0.1F);
      CAMPFIRE = define("campfire", "inFire", 0.1F, DamageEffects.BURNING);
      CRAMMING = define("cramming", "cramming", 0.0F);
      DRAGON_BREATH = define("dragon_breath", "dragonBreath", 0.0F);
      DROWN = define("drown", "drown", 0.0F, DamageEffects.DROWNING);
      DRY_OUT = define("dry_out", "dryout", 0.1F);
      EXPLOSION = define("explosion", "explosion", DamageScaling.ALWAYS, 0.1F);
      FALL = define("fall", "fall", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F, DamageEffects.HURT, DeathMessageType.FALL_VARIANTS);
      FALLING_ANVIL = define("falling_anvil", "anvil", 0.1F);
      FALLING_BLOCK = define("falling_block", "fallingBlock", 0.1F);
      FALLING_STALACTITE = define("falling_stalactite", "fallingStalactite", 0.1F);
      FELL_OUT_OF_WORLD = define("out_of_world", "outOfWorld", 0.0F);
      FIREBALL = define("fireball", "fireball", 0.1F, DamageEffects.BURNING);
      FIREWORKS = define("fireworks", "fireworks", 0.1F);
      FLY_INTO_WALL = define("fly_into_wall", "flyIntoWall", 0.0F);
      FREEZE = define("freeze", "freeze", 0.0F, DamageEffects.FREEZING);
      GENERIC = define("generic", "generic", 0.0F);
      GENERIC_KILL = define("generic_kill", "genericKill", 0.0F);
      HOT_FLOOR = define("hot_floor", "hotFloor", 0.1F, DamageEffects.BURNING);
      IN_FIRE = define("in_fire", "inFire", 0.1F, DamageEffects.BURNING);
      IN_WALL = define("in_wall", "inWall", 0.0F);
      INDIRECT_MAGIC = define("indirect_magic", "indirectMagic", 0.0F);
      LAVA = define("lava", "lava", 0.1F, DamageEffects.BURNING);
      LIGHTNING_BOLT = define("lightning_bolt", "lightningBolt", 0.1F);
      MAGIC = define("magic", "magic", 0.0F);
      MOB_ATTACK = define("mob_attack", "mob", 0.1F);
      MOB_ATTACK_NO_AGGRO = define("mob_attack_no_aggro", "mob", 0.1F);
      MOB_PROJECTILE = define("mob_projectile", "mob", 0.1F);
      ON_FIRE = define("on_fire", "onFire", 0.0F, DamageEffects.BURNING);
      OUTSIDE_BORDER = define("outside_border", "outsideBorder", 0.0F);
      PLAYER_ATTACK = define("player_attack", "player", 0.1F);
      PLAYER_EXPLOSION = define("player_explosion", "explosion.player", DamageScaling.ALWAYS, 0.1F);
      SONIC_BOOM = define("sonic_boom", "sonic_boom", DamageScaling.ALWAYS, 0.0F);
      SPIT = define("spit", "mob", 0.1F);
      STALAGMITE = define("stalagmite", "stalagmite", 0.0F);
      STARVE = define("starve", "starve", 0.0F);
      STING = define("sting", "sting", 0.1F);
      SWEET_BERRY_BUSH = define("sweet_berry_bush", "sweetBerryBush", 0.1F, DamageEffects.POKING);
      THORNS = define("thorns", "thorns", 0.1F, DamageEffects.THORNS);
      THROWN = define("thrown", "thrown", 0.1F);
      TRIDENT = define("trident", "trident", 0.1F);
      UNATTRIBUTED_FIREBALL = define("unattributed_fireball", "onFire", 0.1F, DamageEffects.BURNING);
      WIND_CHARGE = define("wind_charge", "mob", 0.1F);
      WITHER = define("wither", "wither", 0.0F);
      WITHER_SKULL = define("wither_skull", "witherSkull", 0.1F);
      ENDER_PEARL = define("ender_pearl", "fall", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F, DamageEffects.HURT, DeathMessageType.FALL_VARIANTS);
      MACE_SMASH = define("mace_smash", "mace_smash", 0.1F);
      SPEAR = define("spear", "spear", 0.1F);
      REGISTRY.unloadMappings();
   }
}
