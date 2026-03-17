package me.PM2.infinitevehicles.xseries;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.xseries.base.XBase;
import me.PM2.infinitevehicles.xseries.base.XRegistry;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public enum XItemFlag implements XBase<XItemFlag, ItemFlag> {
   HIDE_ADDITIONAL_TOOLTIP(new String[]{"HIDE_POTION_EFFECTS"}),
   HIDE_ARMOR_TRIM(new String[0]),
   HIDE_ATTRIBUTES(new String[0]),
   HIDE_DESTROYS(new String[0]),
   HIDE_DYE(new String[0]),
   HIDE_ENCHANTS(new String[0]),
   HIDE_PLACED_ON(new String[0]),
   HIDE_STORED_ENCHANTMENTS(new String[]{"HIDE_STORED_ENCHANTS"}),
   HIDE_UNBREAKABLE(new String[0]),
   HIDE_CUSTOM_DATA(new String[0]),
   HIDE_MAX_STACK_SIZE(new String[0]),
   HIDE_MAX_DAMAGE(new String[0]),
   HIDE_DAMAGE(new String[0]),
   HIDE_CUSTOM_NAME(new String[0]),
   HIDE_ITEM_NAME(new String[0]),
   HIDE_ITEM_MODEL(new String[0]),
   HIDE_LORE(new String[0]),
   HIDE_RARITY(new String[0]),
   HIDE_ENCHANTMENTS(new String[0]),
   HIDE_CAN_PLACE_ON(new String[0]),
   HIDE_CAN_BREAK(new String[0]),
   HIDE_ATTRIBUTE_MODIFIERS(new String[0]),
   HIDE_CUSTOM_MODEL_DATA(new String[0]),
   HIDE_TOOLTIP_DISPLAY(new String[0]),
   HIDE_REPAIR_COST(new String[0]),
   HIDE_CREATIVE_SLOT_LOCK(new String[0]),
   HIDE_ENCHANTMENT_GLINT_OVERRIDE(new String[0]),
   HIDE_INTANGIBLE_PROJECTILE(new String[0]),
   HIDE_FOOD(new String[0]),
   HIDE_CONSUMABLE(new String[0]),
   HIDE_USE_REMAINDER(new String[0]),
   HIDE_USE_COOLDOWN(new String[0]),
   HIDE_DAMAGE_RESISTANT(new String[0]),
   HIDE_TOOL(new String[0]),
   HIDE_WEAPON(new String[0]),
   HIDE_ENCHANTABLE(new String[0]),
   HIDE_EQUIPPABLE(new String[0]),
   HIDE_REPAIRABLE(new String[0]),
   HIDE_GLIDER(new String[0]),
   HIDE_TOOLTIP_STYLE(new String[0]),
   HIDE_DEATH_PROTECTION(new String[0]),
   HIDE_BLOCKS_ATTACKS(new String[0]),
   HIDE_DYED_COLOR(new String[0]),
   HIDE_MAP_COLOR(new String[0]),
   HIDE_MAP_ID(new String[0]),
   HIDE_MAP_DECORATIONS(new String[0]),
   HIDE_MAP_POST_PROCESSING(new String[0]),
   HIDE_CHARGED_PROJECTILES(new String[0]),
   HIDE_BUNDLE_CONTENTS(new String[0]),
   HIDE_POTION_CONTENTS(new String[0]),
   HIDE_POTION_DURATION_SCALE(new String[0]),
   HIDE_SUSPICIOUS_STEW_EFFECTS(new String[0]),
   HIDE_WRITABLE_BOOK_CONTENT(new String[0]),
   HIDE_WRITTEN_BOOK_CONTENT(new String[0]),
   HIDE_TRIM(new String[0]),
   HIDE_DEBUG_STICK_STATE(new String[0]),
   HIDE_ENTITY_DATA(new String[0]),
   HIDE_BUCKET_ENTITY_DATA(new String[0]),
   HIDE_BLOCK_ENTITY_DATA(new String[0]),
   HIDE_INSTRUMENT(new String[0]),
   HIDE_PROVIDES_TRIM_MATERIAL(new String[0]),
   HIDE_OMINOUS_BOTTLE_AMPLIFIER(new String[0]),
   HIDE_JUKEBOX_PLAYABLE(new String[0]),
   HIDE_PROVIDES_BANNER_PATTERNS(new String[0]),
   HIDE_RECIPES(new String[0]),
   HIDE_LODESTONE_TRACKER(new String[0]),
   HIDE_FIREWORK_EXPLOSION(new String[0]),
   HIDE_FIREWORKS(new String[0]),
   HIDE_PROFILE(new String[0]),
   HIDE_NOTE_BLOCK_SOUND(new String[0]),
   HIDE_BANNER_PATTERNS(new String[0]),
   HIDE_BASE_COLOR(new String[0]),
   HIDE_POT_DECORATIONS(new String[0]),
   HIDE_CONTAINER(new String[0]),
   HIDE_BLOCK_STATE(new String[0]),
   HIDE_BEES(new String[0]),
   HIDE_LOCK(new String[0]),
   HIDE_CONTAINER_LOOT(new String[0]),
   HIDE_BREAK_SOUND(new String[0]),
   HIDE_VILLAGER_VARIANT(new String[0]),
   HIDE_WOLF_VARIANT(new String[0]),
   HIDE_WOLF_SOUND_VARIANT(new String[0]),
   HIDE_WOLF_COLLAR(new String[0]),
   HIDE_FOX_VARIANT(new String[0]),
   HIDE_SALMON_SIZE(new String[0]),
   HIDE_PARROT_VARIANT(new String[0]),
   HIDE_TROPICAL_FISH_PATTERN(new String[0]),
   HIDE_TROPICAL_FISH_BASE_COLOR(new String[0]),
   HIDE_TROPICAL_FISH_PATTERN_COLOR(new String[0]),
   HIDE_MOOSHROOM_VARIANT(new String[0]),
   HIDE_RABBIT_VARIANT(new String[0]),
   HIDE_PIG_VARIANT(new String[0]),
   HIDE_COW_VARIANT(new String[0]),
   HIDE_CHICKEN_VARIANT(new String[0]),
   HIDE_FROG_VARIANT(new String[0]),
   HIDE_HORSE_VARIANT(new String[0]),
   HIDE_PAINTING_VARIANT(new String[0]),
   HIDE_LLAMA_VARIANT(new String[0]),
   HIDE_AXOLOTL_VARIANT(new String[0]),
   HIDE_CAT_VARIANT(new String[0]),
   HIDE_CAT_COLLAR(new String[0]),
   HIDE_SHEEP_COLOR(new String[0]),
   HIDE_SHULKER_COLOR(new String[0]);

   public static final XRegistry<XItemFlag, ItemFlag> REGISTRY = XItemFlag.Data.REGISTRY;
   private static final ItemFlag[] NONE_DECORATION_FLAGS = (ItemFlag[])Arrays.stream(values()).filter((var0) -> {
      return var0 != HIDE_LORE && var0 != HIDE_ITEM_NAME && var0 != HIDE_CUSTOM_NAME;
   }).filter(XBase::isSupported).map(XItemFlag::get).toArray((var0) -> {
      return new ItemFlag[var0];
   });
   private final ItemFlag itemFlag;

   private XItemFlag(String... param3) {
      this.itemFlag = (ItemFlag)XItemFlag.Data.REGISTRY.stdEnum(this, var3);
   }

   @NotNull
   public static XItemFlag of(@NotNull ItemFlag var0) {
      return (XItemFlag)REGISTRY.getByBukkitForm(var0);
   }

   @NotNull
   public static Optional<XItemFlag> of(@NotNull String var0) {
      return REGISTRY.getByName(var0);
   }

   @NotNull
   @Unmodifiable
   public static Collection<XItemFlag> getValues() {
      return REGISTRY.getValues();
   }

   public String[] getNames() {
      return new String[]{this.name()};
   }

   @Nullable
   public ItemFlag get() {
      return this.itemFlag;
   }

   @Contract(
      mutates = "param1"
   )
   public void set(@NotNull ItemStack var1) {
      ItemMeta var2 = var1.getItemMeta();
      var2.addItemFlags(new ItemFlag[]{this.itemFlag});
      var1.setItemMeta(var2);
   }

   @Contract(
      mutates = "param1"
   )
   public void set(@NotNull ItemMeta var1) {
      var1.addItemFlags(new ItemFlag[]{this.itemFlag});
   }

   @Contract(
      mutates = "param1"
   )
   public void removeFrom(@NotNull ItemStack var1) {
      ItemMeta var2 = var1.getItemMeta();
      this.removeFrom(var2);
      var1.setItemMeta(var2);
   }

   @Contract(
      mutates = "param1"
   )
   public void removeFrom(@NotNull ItemMeta var1) {
      var1.removeItemFlags(new ItemFlag[]{this.itemFlag});
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public static Set<XItemFlag> getFlags(@NotNull ItemStack var0) {
      return getFlags(var0.getItemMeta());
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public static Set<XItemFlag> getFlags(@NotNull ItemMeta var0) {
      return (Set)var0.getItemFlags().stream().map(XItemFlag::of).collect(Collectors.toSet());
   }

   @Contract(
      mutates = "param1"
   )
   public boolean has(@NotNull ItemStack var1) {
      return this.has(var1.getItemMeta());
   }

   @Contract(
      mutates = "param1"
   )
   public boolean has(@NotNull ItemMeta var1) {
      return var1.getItemFlags().contains(this.itemFlag);
   }

   /** @deprecated */
   @Deprecated
   @Contract(
      mutates = "param1"
   )
   public static void hideEverything(@NotNull ItemMeta var0) {
      decorationOnly(var0);
   }

   @Contract(
      mutates = "param1"
   )
   public static void decorationOnly(@NotNull ItemMeta var0) {
      var0.addItemFlags(NONE_DECORATION_FLAGS);
   }

   // $FF: synthetic method
   private static XItemFlag[] $values() {
      return new XItemFlag[]{HIDE_ADDITIONAL_TOOLTIP, HIDE_ARMOR_TRIM, HIDE_ATTRIBUTES, HIDE_DESTROYS, HIDE_DYE, HIDE_ENCHANTS, HIDE_PLACED_ON, HIDE_STORED_ENCHANTMENTS, HIDE_UNBREAKABLE, HIDE_CUSTOM_DATA, HIDE_MAX_STACK_SIZE, HIDE_MAX_DAMAGE, HIDE_DAMAGE, HIDE_CUSTOM_NAME, HIDE_ITEM_NAME, HIDE_ITEM_MODEL, HIDE_LORE, HIDE_RARITY, HIDE_ENCHANTMENTS, HIDE_CAN_PLACE_ON, HIDE_CAN_BREAK, HIDE_ATTRIBUTE_MODIFIERS, HIDE_CUSTOM_MODEL_DATA, HIDE_TOOLTIP_DISPLAY, HIDE_REPAIR_COST, HIDE_CREATIVE_SLOT_LOCK, HIDE_ENCHANTMENT_GLINT_OVERRIDE, HIDE_INTANGIBLE_PROJECTILE, HIDE_FOOD, HIDE_CONSUMABLE, HIDE_USE_REMAINDER, HIDE_USE_COOLDOWN, HIDE_DAMAGE_RESISTANT, HIDE_TOOL, HIDE_WEAPON, HIDE_ENCHANTABLE, HIDE_EQUIPPABLE, HIDE_REPAIRABLE, HIDE_GLIDER, HIDE_TOOLTIP_STYLE, HIDE_DEATH_PROTECTION, HIDE_BLOCKS_ATTACKS, HIDE_DYED_COLOR, HIDE_MAP_COLOR, HIDE_MAP_ID, HIDE_MAP_DECORATIONS, HIDE_MAP_POST_PROCESSING, HIDE_CHARGED_PROJECTILES, HIDE_BUNDLE_CONTENTS, HIDE_POTION_CONTENTS, HIDE_POTION_DURATION_SCALE, HIDE_SUSPICIOUS_STEW_EFFECTS, HIDE_WRITABLE_BOOK_CONTENT, HIDE_WRITTEN_BOOK_CONTENT, HIDE_TRIM, HIDE_DEBUG_STICK_STATE, HIDE_ENTITY_DATA, HIDE_BUCKET_ENTITY_DATA, HIDE_BLOCK_ENTITY_DATA, HIDE_INSTRUMENT, HIDE_PROVIDES_TRIM_MATERIAL, HIDE_OMINOUS_BOTTLE_AMPLIFIER, HIDE_JUKEBOX_PLAYABLE, HIDE_PROVIDES_BANNER_PATTERNS, HIDE_RECIPES, HIDE_LODESTONE_TRACKER, HIDE_FIREWORK_EXPLOSION, HIDE_FIREWORKS, HIDE_PROFILE, HIDE_NOTE_BLOCK_SOUND, HIDE_BANNER_PATTERNS, HIDE_BASE_COLOR, HIDE_POT_DECORATIONS, HIDE_CONTAINER, HIDE_BLOCK_STATE, HIDE_BEES, HIDE_LOCK, HIDE_CONTAINER_LOOT, HIDE_BREAK_SOUND, HIDE_VILLAGER_VARIANT, HIDE_WOLF_VARIANT, HIDE_WOLF_SOUND_VARIANT, HIDE_WOLF_COLLAR, HIDE_FOX_VARIANT, HIDE_SALMON_SIZE, HIDE_PARROT_VARIANT, HIDE_TROPICAL_FISH_PATTERN, HIDE_TROPICAL_FISH_BASE_COLOR, HIDE_TROPICAL_FISH_PATTERN_COLOR, HIDE_MOOSHROOM_VARIANT, HIDE_RABBIT_VARIANT, HIDE_PIG_VARIANT, HIDE_COW_VARIANT, HIDE_CHICKEN_VARIANT, HIDE_FROG_VARIANT, HIDE_HORSE_VARIANT, HIDE_PAINTING_VARIANT, HIDE_LLAMA_VARIANT, HIDE_AXOLOTL_VARIANT, HIDE_CAT_VARIANT, HIDE_CAT_COLLAR, HIDE_SHEEP_COLOR, HIDE_SHULKER_COLOR};
   }

   static {
      REGISTRY.discardMetadata();
   }

   private static final class Data {
      private static final XRegistry<XItemFlag, ItemFlag> REGISTRY = new XRegistry(ItemFlag.class, XItemFlag.class, (var0) -> {
         return new XItemFlag[var0];
      });
   }
}
