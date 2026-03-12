package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Collection;

public final class PotionTypes {
   private static final VersionedRegistry<PotionType> REGISTRY = new VersionedRegistry("mob_effect");
   public static final PotionType SPEED = define("speed");
   public static final PotionType SLOWNESS = define("slowness");
   public static final PotionType HASTE = define("haste");
   public static final PotionType MINING_FATIGUE = define("mining_fatigue");
   public static final PotionType STRENGTH = define("strength");
   public static final PotionType INSTANT_HEALTH = define("instant_health");
   public static final PotionType INSTANT_DAMAGE = define("instant_damage");
   public static final PotionType JUMP_BOOST = define("jump_boost");
   public static final PotionType NAUSEA = define("nausea");
   public static final PotionType REGENERATION = define("regeneration");
   public static final PotionType RESISTANCE = define("resistance");
   public static final PotionType FIRE_RESISTANCE = define("fire_resistance");
   public static final PotionType WATER_BREATHING = define("water_breathing");
   public static final PotionType INVISIBILITY = define("invisibility");
   public static final PotionType BLINDNESS = define("blindness");
   public static final PotionType NIGHT_VISION = define("night_vision");
   public static final PotionType HUNGER = define("hunger");
   public static final PotionType WEAKNESS = define("weakness");
   public static final PotionType POISON = define("poison");
   public static final PotionType WITHER = define("wither");
   public static final PotionType HEALTH_BOOST = define("health_boost");
   public static final PotionType ABSORPTION = define("absorption");
   public static final PotionType SATURATION = define("saturation");
   public static final PotionType GLOWING = define("glowing");
   public static final PotionType LEVITATION = define("levitation");
   public static final PotionType LUCK = define("luck");
   public static final PotionType UNLUCK = define("unluck");
   public static final PotionType SLOW_FALLING = define("slow_falling");
   public static final PotionType CONDUIT_POWER = define("conduit_power");
   public static final PotionType DOLPHINS_GRACE = define("dolphins_grace");
   public static final PotionType BAD_OMEN = define("bad_omen");
   public static final PotionType HERO_OF_THE_VILLAGE = define("hero_of_the_village");
   public static final PotionType DARKNESS = define("darkness");
   public static final PotionType TRIAL_OMEN = define("trial_omen");
   public static final PotionType RAID_OMEN = define("raid_omen");
   public static final PotionType WIND_CHARGED = define("wind_charged");
   public static final PotionType WEAVING = define("weaving");
   public static final PotionType OOZING = define("oozing");
   public static final PotionType INFESTED = define("infested");
   public static final PotionType BREATH_OF_THE_NAUTILUS = define("breath_of_the_nautilus");

   private PotionTypes() {
   }

   public static VersionedRegistry<PotionType> getRegistry() {
      return REGISTRY;
   }

   /** @deprecated */
   @Deprecated
   public static PotionType define(String name, int ignoredId) {
      return define(name);
   }

   @ApiStatus.Internal
   public static PotionType define(String name) {
      return (PotionType)REGISTRY.define(name, StaticPotionType::new);
   }

   @Nullable
   public static PotionType getByName(String name) {
      return (PotionType)REGISTRY.getByName(name);
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public static PotionType getById(int id) {
      return getById(id, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public static PotionType getById(int id, ServerVersion version) {
      return getById(id, version.toClientVersion());
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public static PotionType getById(int id, ClientVersion version) {
      return getById(version, id);
   }

   @Nullable
   public static PotionType getById(ClientVersion version, int id) {
      return (PotionType)REGISTRY.getById(version, id);
   }

   public static Collection<PotionType> values() {
      return REGISTRY.getEntries();
   }

   static {
      REGISTRY.unloadMappings();
   }
}
