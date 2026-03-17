package me.PM2.infinitevehicles.xseries.particles;

import java.util.Optional;
import me.PM2.infinitevehicles.xseries.base.XBase;
import me.PM2.infinitevehicles.xseries.base.XRegistry;
import me.PM2.infinitevehicles.xseries.base.annotations.XMerge;
import me.PM2.infinitevehicles.xseries.base.annotations.XMerges;
import org.bukkit.Particle;

public enum XParticle implements XBase<XParticle, Particle> {
   ANGRY_VILLAGER(new String[]{"VILLAGER_ANGRY"}),
   ASH(new String[0]),
   @XMerge(
      version = "1.20.5",
      name = "BLOCK_DUST"
   )
   BLOCK(new String[]{"BLOCK_CRACK"}),
   BLOCK_CRUMBLE(new String[0]),
   @XMerge(
      version = "1.17",
      name = "LIGHT"
   )
   BLOCK_MARKER(new String[]{"BARRIER"}),
   BUBBLE(new String[]{"WATER_BUBBLE"}),
   BUBBLE_COLUMN_UP(new String[0]),
   BUBBLE_POP(new String[0]),
   CAMPFIRE_COSY_SMOKE(new String[0]),
   CAMPFIRE_SIGNAL_SMOKE(new String[0]),
   @XMerges({@XMerge(
   version = "1.20",
   name = "FALLING_CHERRY_LEAVES"
), @XMerge(
   version = "1.20",
   name = "LANDING_CHERRY_LEAVES"
)})
   CHERRY_LEAVES(new String[]{"DRIPPING_CHERRY_LEAVES"}),
   CLOUD(new String[0]),
   COMPOSTER(new String[0]),
   CRIMSON_SPORE(new String[0]),
   CRIT(new String[0]),
   CURRENT_DOWN(new String[0]),
   DAMAGE_INDICATOR(new String[0]),
   DOLPHIN(new String[0]),
   DRAGON_BREATH(new String[0]),
   DRIPPING_DRIPSTONE_LAVA(new String[0]),
   DRIPPING_DRIPSTONE_WATER(new String[0]),
   DRIPPING_HONEY(new String[0]),
   DRIPPING_LAVA(new String[]{"DRIP_LAVA"}),
   DRIPPING_OBSIDIAN_TEAR(new String[0]),
   DRIPPING_WATER(new String[]{"DRIP_WATER"}),
   DUST(new String[]{"REDSTONE"}),
   DUST_COLOR_TRANSITION(new String[0]),
   DUST_PILLAR(new String[0]),
   DUST_PLUME(new String[0]),
   EFFECT(new String[]{"SPELL"}),
   EGG_CRACK(new String[0]),
   ELDER_GUARDIAN(new String[]{"MOB_APPEARANCE"}),
   ELECTRIC_SPARK(new String[0]),
   ENCHANT(new String[]{"ENCHANTMENT_TABLE"}),
   ENCHANTED_HIT(new String[]{"CRIT_MAGIC"}),
   END_ROD(new String[0]),
   @XMerge(
      version = "1.20.5",
      name = "SPELL_MOB_AMBIENT"
   )
   ENTITY_EFFECT(new String[]{"SPELL_MOB"}),
   EXPLOSION(new String[]{"EXPLOSION_LARGE"}),
   EXPLOSION_EMITTER(new String[]{"EXPLOSION_HUGE"}),
   FALLING_DRIPSTONE_LAVA(new String[0]),
   FALLING_DRIPSTONE_WATER(new String[0]),
   FALLING_DUST(new String[0]),
   FALLING_HONEY(new String[0]),
   FALLING_LAVA(new String[0]),
   FALLING_NECTAR(new String[0]),
   FALLING_OBSIDIAN_TEAR(new String[0]),
   FALLING_SPORE_BLOSSOM(new String[0]),
   FALLING_WATER(new String[0]),
   FIREFLY(new String[0]),
   FIREWORK(new String[]{"FIREWORKS_SPARK"}),
   FISHING(new String[]{"WATER_WAKE"}),
   FLAME(new String[0]),
   FLASH(new String[0]),
   /** @deprecated */
   @Deprecated
   FOOTSTEP(new String[0]),
   GLOW(new String[0]),
   GLOW_SQUID_INK(new String[0]),
   GUST(new String[0]),
   GUST_EMITTER_LARGE(new String[0]),
   GUST_EMITTER_SMALL(new String[0]),
   HAPPY_VILLAGER(new String[]{"VILLAGER_HAPPY"}),
   HEART(new String[0]),
   INFESTED(new String[0]),
   INSTANT_EFFECT(new String[]{"SPELL_INSTANT"}),
   ITEM(new String[]{"ITEM_CRACK"}),
   ITEM_COBWEB(new String[0]),
   ITEM_SLIME(new String[]{"SLIME"}),
   @XMerge(
      version = "1.20.5",
      name = "SNOW_SHOVEL"
   )
   ITEM_SNOWBALL(new String[]{"SNOWBALL"}),
   /** @deprecated */
   @Deprecated
   ITEM_TAKE(new String[0]),
   LANDING_HONEY(new String[0]),
   LANDING_LAVA(new String[0]),
   LANDING_OBSIDIAN_TEAR(new String[0]),
   LARGE_SMOKE(new String[]{"SMOKE_LARGE"}),
   LAVA(new String[0]),
   MYCELIUM(new String[]{"TOWN_AURA"}),
   NAUTILUS(new String[0]),
   NOTE(new String[0]),
   OMINOUS_SPAWNING(new String[0]),
   PALE_OAK_LEAVES(new String[0]),
   POOF(new String[]{"EXPLOSION_NORMAL"}),
   PORTAL(new String[0]),
   RAID_OMEN(new String[0]),
   RAIN(new String[]{"WATER_DROP"}),
   REVERSE_PORTAL(new String[0]),
   SCRAPE(new String[0]),
   SCULK_CHARGE(new String[0]),
   SCULK_CHARGE_POP(new String[0]),
   SCULK_SOUL(new String[0]),
   SHRIEK(new String[0]),
   SMALL_FLAME(new String[0]),
   SMALL_GUST(new String[0]),
   SMOKE(new String[]{"SMOKE_NORMAL"}),
   SNEEZE(new String[0]),
   SNOWFLAKE(new String[0]),
   SONIC_BOOM(new String[0]),
   SOUL(new String[0]),
   SOUL_FIRE_FLAME(new String[0]),
   SPIT(new String[0]),
   SPLASH(new String[]{"WATER_SPLASH"}),
   SPORE_BLOSSOM_AIR(new String[0]),
   SQUID_INK(new String[0]),
   SWEEP_ATTACK(new String[0]),
   TINTED_LEAVES(new String[0]),
   TOTEM_OF_UNDYING(new String[]{"TOTEM"}),
   TRAIL(new String[0]),
   TRIAL_OMEN(new String[0]),
   TRIAL_SPAWNER_DETECTION(new String[0]),
   TRIAL_SPAWNER_DETECTION_OMINOUS(new String[0]),
   @XMerge(
      version = "1.20.5",
      name = "SUSPENDED_DEPTH"
   )
   UNDERWATER(new String[]{"SUSPENDED"}),
   VAULT_CONNECTION(new String[0]),
   VIBRATION(new String[0]),
   WARPED_SPORE(new String[0]),
   WAX_OFF(new String[0]),
   WAX_ON(new String[0]),
   WHITE_ASH(new String[0]),
   WHITE_SMOKE(new String[0]),
   WITCH(new String[]{"SPELL_WITCH"}),
   COPPER_FIRE_FLAME(new String[0]);

   public static final XRegistry<XParticle, Particle> REGISTRY = XParticle.Data.REGISTRY;
   private final Particle particle;

   private XParticle(String... param3) {
      this.particle = (Particle)XParticle.Data.REGISTRY.stdEnum(this, var3);
   }

   public String[] getNames() {
      return new String[]{this.name()};
   }

   public Particle get() {
      return this.particle;
   }

   public static XParticle of(Particle var0) {
      return (XParticle)REGISTRY.getByBukkitForm(var0);
   }

   public static Optional<XParticle> of(String var0) {
      return REGISTRY.getByName(var0);
   }

   // $FF: synthetic method
   private static XParticle[] $values() {
      return new XParticle[]{ANGRY_VILLAGER, ASH, BLOCK, BLOCK_CRUMBLE, BLOCK_MARKER, BUBBLE, BUBBLE_COLUMN_UP, BUBBLE_POP, CAMPFIRE_COSY_SMOKE, CAMPFIRE_SIGNAL_SMOKE, CHERRY_LEAVES, CLOUD, COMPOSTER, CRIMSON_SPORE, CRIT, CURRENT_DOWN, DAMAGE_INDICATOR, DOLPHIN, DRAGON_BREATH, DRIPPING_DRIPSTONE_LAVA, DRIPPING_DRIPSTONE_WATER, DRIPPING_HONEY, DRIPPING_LAVA, DRIPPING_OBSIDIAN_TEAR, DRIPPING_WATER, DUST, DUST_COLOR_TRANSITION, DUST_PILLAR, DUST_PLUME, EFFECT, EGG_CRACK, ELDER_GUARDIAN, ELECTRIC_SPARK, ENCHANT, ENCHANTED_HIT, END_ROD, ENTITY_EFFECT, EXPLOSION, EXPLOSION_EMITTER, FALLING_DRIPSTONE_LAVA, FALLING_DRIPSTONE_WATER, FALLING_DUST, FALLING_HONEY, FALLING_LAVA, FALLING_NECTAR, FALLING_OBSIDIAN_TEAR, FALLING_SPORE_BLOSSOM, FALLING_WATER, FIREFLY, FIREWORK, FISHING, FLAME, FLASH, FOOTSTEP, GLOW, GLOW_SQUID_INK, GUST, GUST_EMITTER_LARGE, GUST_EMITTER_SMALL, HAPPY_VILLAGER, HEART, INFESTED, INSTANT_EFFECT, ITEM, ITEM_COBWEB, ITEM_SLIME, ITEM_SNOWBALL, ITEM_TAKE, LANDING_HONEY, LANDING_LAVA, LANDING_OBSIDIAN_TEAR, LARGE_SMOKE, LAVA, MYCELIUM, NAUTILUS, NOTE, OMINOUS_SPAWNING, PALE_OAK_LEAVES, POOF, PORTAL, RAID_OMEN, RAIN, REVERSE_PORTAL, SCRAPE, SCULK_CHARGE, SCULK_CHARGE_POP, SCULK_SOUL, SHRIEK, SMALL_FLAME, SMALL_GUST, SMOKE, SNEEZE, SNOWFLAKE, SONIC_BOOM, SOUL, SOUL_FIRE_FLAME, SPIT, SPLASH, SPORE_BLOSSOM_AIR, SQUID_INK, SWEEP_ATTACK, TINTED_LEAVES, TOTEM_OF_UNDYING, TRAIL, TRIAL_OMEN, TRIAL_SPAWNER_DETECTION, TRIAL_SPAWNER_DETECTION_OMINOUS, UNDERWATER, VAULT_CONNECTION, VIBRATION, WARPED_SPORE, WAX_OFF, WAX_ON, WHITE_ASH, WHITE_SMOKE, WITCH, COPPER_FIRE_FLAME};
   }

   static {
      REGISTRY.discardMetadata();
   }

   private static final class Data {
      private static final XRegistry<XParticle, Particle> REGISTRY = new XRegistry(Particle.class, XParticle.class, (var0) -> {
         return new XParticle[var0];
      });
   }
}
