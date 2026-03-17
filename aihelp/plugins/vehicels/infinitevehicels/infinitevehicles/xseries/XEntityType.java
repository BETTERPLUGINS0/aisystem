package me.PM2.infinitevehicles.xseries;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import me.PM2.infinitevehicles.xseries.base.XBase;
import me.PM2.infinitevehicles.xseries.base.XRegistry;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public enum XEntityType implements XBase<XEntityType, EntityType> {
   ACACIA_BOAT(new String[0]),
   ACACIA_CHEST_BOAT(new String[0]),
   ALLAY(new String[0]),
   AREA_EFFECT_CLOUD(new String[0]),
   ARMADILLO(new String[0]),
   ARMOR_STAND(new String[0]),
   ARROW(new String[0]),
   AXOLOTL(new String[0]),
   BAMBOO_CHEST_RAFT(new String[0]),
   BAMBOO_RAFT(new String[0]),
   BAT(new String[0]),
   BEE(new String[0]),
   BIRCH_BOAT(new String[0]),
   BIRCH_CHEST_BOAT(new String[0]),
   BLAZE(new String[0]),
   BLOCK_DISPLAY(new String[0]),
   BOGGED(new String[0]),
   BREEZE(new String[0]),
   BREEZE_WIND_CHARGE(new String[0]),
   CAMEL(new String[0]),
   CAT(new String[0]),
   CAVE_SPIDER(new String[0]),
   CHERRY_BOAT(new String[0]),
   CHERRY_CHEST_BOAT(new String[0]),
   CHEST_MINECART(new String[]{"MINECART_CHEST"}),
   CHICKEN(new String[0]),
   COD(new String[0]),
   COMMAND_BLOCK_MINECART(new String[]{"MINECART_COMMAND"}),
   COW(new String[0]),
   CREAKING(new String[0]),
   /** @deprecated */
   @Deprecated
   CREAKING_TRANSIENT(new String[0]),
   CREEPER(new String[0]),
   DARK_OAK_BOAT(new String[0]),
   DARK_OAK_CHEST_BOAT(new String[0]),
   DOLPHIN(new String[0]),
   DONKEY(new String[0]),
   DRAGON_FIREBALL(new String[0]),
   DROWNED(new String[0]),
   EGG(new String[0]),
   ELDER_GUARDIAN(new String[0]),
   ENDERMAN(new String[0]),
   ENDERMITE(new String[0]),
   ENDER_DRAGON(new String[0]),
   ENDER_PEARL(new String[0]),
   END_CRYSTAL(new String[]{"ENDER_CRYSTAL"}),
   EVOKER(new String[0]),
   EVOKER_FANGS(new String[0]),
   EXPERIENCE_BOTTLE(new String[]{"THROWN_EXP_BOTTLE"}),
   EXPERIENCE_ORB(new String[0]),
   EYE_OF_ENDER(new String[]{"ENDER_SIGNAL"}),
   FALLING_BLOCK(new String[0]),
   FIREBALL(new String[0]),
   FIREWORK_ROCKET(new String[]{"FIREWORK"}),
   FISHING_BOBBER(new String[]{"FISHING_HOOK"}),
   FOX(new String[0]),
   FROG(new String[0]),
   FURNACE_MINECART(new String[0]),
   GHAST(new String[0]),
   GIANT(new String[0]),
   GLOW_ITEM_FRAME(new String[0]),
   GLOW_SQUID(new String[0]),
   GOAT(new String[0]),
   GUARDIAN(new String[0]),
   HAPPY_GHAST(new String[0]),
   HOGLIN(new String[0]),
   HOPPER_MINECART(new String[]{"MINECART_HOPPER"}),
   HORSE(new String[0]),
   HUSK(new String[0]),
   ILLUSIONER(new String[0]),
   INTERACTION(new String[0]),
   IRON_GOLEM(new String[0]),
   ITEM(new String[]{"DROPPED_ITEM"}),
   ITEM_DISPLAY(new String[0]),
   ITEM_FRAME(new String[0]),
   JUNGLE_BOAT(new String[0]),
   JUNGLE_CHEST_BOAT(new String[0]),
   LEASH_KNOT(new String[]{"LEASH_HITCH"}),
   LIGHTNING_BOLT(new String[]{"LIGHTNING"}),
   LINGERING_POTION(new String[0]),
   LLAMA(new String[0]),
   LLAMA_SPIT(new String[0]),
   MAGMA_CUBE(new String[0]),
   MANGROVE_BOAT(new String[0]),
   MANGROVE_CHEST_BOAT(new String[0]),
   MARKER(new String[0]),
   MINECART(new String[0]),
   MOOSHROOM(new String[]{"MUSHROOM_COW"}),
   MULE(new String[0]),
   OAK_BOAT(new String[]{"BOAT"}),
   OAK_CHEST_BOAT(new String[]{"CHEST_BOAT"}),
   OCELOT(new String[0]),
   OMINOUS_ITEM_SPAWNER(new String[0]),
   PAINTING(new String[0]),
   PALE_OAK_BOAT(new String[0]),
   PALE_OAK_CHEST_BOAT(new String[0]),
   PANDA(new String[0]),
   PARROT(new String[0]),
   PHANTOM(new String[0]),
   PIG(new String[0]),
   PIGLIN(new String[0]),
   PIGLIN_BRUTE(new String[0]),
   PILLAGER(new String[0]),
   PLAYER(new String[0]),
   POLAR_BEAR(new String[0]),
   PUFFERFISH(new String[0]),
   RABBIT(new String[0]),
   RAVAGER(new String[0]),
   SALMON(new String[0]),
   SHEEP(new String[0]),
   SHULKER(new String[0]),
   SHULKER_BULLET(new String[0]),
   SILVERFISH(new String[0]),
   SKELETON(new String[0]),
   SKELETON_HORSE(new String[0]),
   SLIME(new String[0]),
   SMALL_FIREBALL(new String[0]),
   SNIFFER(new String[0]),
   SNOWBALL(new String[0]),
   SNOW_GOLEM(new String[]{"SNOWMAN"}),
   SPAWNER_MINECART(new String[]{"MINECART_MOB_SPAWNER"}),
   SPECTRAL_ARROW(new String[0]),
   SPIDER(new String[0]),
   SPLASH_POTION(new String[]{"POTION"}),
   SPRUCE_BOAT(new String[0]),
   SPRUCE_CHEST_BOAT(new String[0]),
   SQUID(new String[0]),
   STRAY(new String[0]),
   STRIDER(new String[0]),
   TADPOLE(new String[0]),
   TEXT_DISPLAY(new String[0]),
   TNT(new String[]{"PRIMED_TNT"}),
   TNT_MINECART(new String[]{"MINECART_TNT"}),
   TRADER_LLAMA(new String[0]),
   TRIDENT(new String[0]),
   TROPICAL_FISH(new String[0]),
   TURTLE(new String[0]),
   UNKNOWN(new String[0]),
   VEX(new String[0]),
   VILLAGER(new String[0]),
   VINDICATOR(new String[0]),
   WANDERING_TRADER(new String[0]),
   WARDEN(new String[0]),
   WIND_CHARGE(new String[0]),
   WITCH(new String[0]),
   WITHER(new String[0]),
   WITHER_SKELETON(new String[0]),
   WITHER_SKULL(new String[0]),
   WOLF(new String[0]),
   ZOGLIN(new String[0]),
   ZOMBIE(new String[0]),
   ZOMBIE_HORSE(new String[0]),
   ZOMBIE_VILLAGER(new String[0]),
   ZOMBIFIED_PIGLIN(new String[0]),
   COPPER_GOLEM(new String[0]),
   MANNEQUIN(new String[0]);

   public static final XRegistry<XEntityType, EntityType> REGISTRY = XEntityType.Data.REGISTRY;
   private final EntityType entityType;

   private XEntityType(String... param3) {
      this.entityType = (EntityType)XEntityType.Data.REGISTRY.stdEnum(this, var3);
   }

   @NotNull
   @Unmodifiable
   public static Collection<XEntityType> getValues() {
      return REGISTRY.getValues();
   }

   @NotNull
   public static XEntityType of(@NotNull Entity var0) {
      Objects.requireNonNull(var0, "Cannot match entity type from null entity");
      return of(var0.getType());
   }

   @NotNull
   public static XEntityType of(@NotNull EntityType var0) {
      return (XEntityType)REGISTRY.getByBukkitForm(var0);
   }

   public static Optional<XEntityType> of(@NotNull String var0) {
      return REGISTRY.getByName(var0);
   }

   public String[] getNames() {
      return new String[]{this.name()};
   }

   public EntityType get() {
      return this.entityType;
   }

   // $FF: synthetic method
   private static XEntityType[] $values() {
      return new XEntityType[]{ACACIA_BOAT, ACACIA_CHEST_BOAT, ALLAY, AREA_EFFECT_CLOUD, ARMADILLO, ARMOR_STAND, ARROW, AXOLOTL, BAMBOO_CHEST_RAFT, BAMBOO_RAFT, BAT, BEE, BIRCH_BOAT, BIRCH_CHEST_BOAT, BLAZE, BLOCK_DISPLAY, BOGGED, BREEZE, BREEZE_WIND_CHARGE, CAMEL, CAT, CAVE_SPIDER, CHERRY_BOAT, CHERRY_CHEST_BOAT, CHEST_MINECART, CHICKEN, COD, COMMAND_BLOCK_MINECART, COW, CREAKING, CREAKING_TRANSIENT, CREEPER, DARK_OAK_BOAT, DARK_OAK_CHEST_BOAT, DOLPHIN, DONKEY, DRAGON_FIREBALL, DROWNED, EGG, ELDER_GUARDIAN, ENDERMAN, ENDERMITE, ENDER_DRAGON, ENDER_PEARL, END_CRYSTAL, EVOKER, EVOKER_FANGS, EXPERIENCE_BOTTLE, EXPERIENCE_ORB, EYE_OF_ENDER, FALLING_BLOCK, FIREBALL, FIREWORK_ROCKET, FISHING_BOBBER, FOX, FROG, FURNACE_MINECART, GHAST, GIANT, GLOW_ITEM_FRAME, GLOW_SQUID, GOAT, GUARDIAN, HAPPY_GHAST, HOGLIN, HOPPER_MINECART, HORSE, HUSK, ILLUSIONER, INTERACTION, IRON_GOLEM, ITEM, ITEM_DISPLAY, ITEM_FRAME, JUNGLE_BOAT, JUNGLE_CHEST_BOAT, LEASH_KNOT, LIGHTNING_BOLT, LINGERING_POTION, LLAMA, LLAMA_SPIT, MAGMA_CUBE, MANGROVE_BOAT, MANGROVE_CHEST_BOAT, MARKER, MINECART, MOOSHROOM, MULE, OAK_BOAT, OAK_CHEST_BOAT, OCELOT, OMINOUS_ITEM_SPAWNER, PAINTING, PALE_OAK_BOAT, PALE_OAK_CHEST_BOAT, PANDA, PARROT, PHANTOM, PIG, PIGLIN, PIGLIN_BRUTE, PILLAGER, PLAYER, POLAR_BEAR, PUFFERFISH, RABBIT, RAVAGER, SALMON, SHEEP, SHULKER, SHULKER_BULLET, SILVERFISH, SKELETON, SKELETON_HORSE, SLIME, SMALL_FIREBALL, SNIFFER, SNOWBALL, SNOW_GOLEM, SPAWNER_MINECART, SPECTRAL_ARROW, SPIDER, SPLASH_POTION, SPRUCE_BOAT, SPRUCE_CHEST_BOAT, SQUID, STRAY, STRIDER, TADPOLE, TEXT_DISPLAY, TNT, TNT_MINECART, TRADER_LLAMA, TRIDENT, TROPICAL_FISH, TURTLE, UNKNOWN, VEX, VILLAGER, VINDICATOR, WANDERING_TRADER, WARDEN, WIND_CHARGE, WITCH, WITHER, WITHER_SKELETON, WITHER_SKULL, WOLF, ZOGLIN, ZOMBIE, ZOMBIE_HORSE, ZOMBIE_VILLAGER, ZOMBIFIED_PIGLIN, COPPER_GOLEM, MANNEQUIN};
   }

   static {
      REGISTRY.discardMetadata();
   }

   private static final class Data {
      public static final XRegistry<XEntityType, EntityType> REGISTRY = new XRegistry(EntityType.class, XEntityType.class, (var0) -> {
         return new XEntityType[var0];
      });
   }
}
