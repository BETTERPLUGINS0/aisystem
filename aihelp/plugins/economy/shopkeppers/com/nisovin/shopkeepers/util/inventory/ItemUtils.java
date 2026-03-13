package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.bukkit.MinecraftEnumUtils;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.EnumUtils;
import com.nisovin.shopkeepers.util.java.MathUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.HangingSign;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.block.data.type.WallHangingSign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;

public final class ItemUtils {
   public static final int MAX_STACK_SIZE_64 = 64;
   private static final Predicate<ItemStack> EMPTY_ITEMS = ItemUtils::isEmpty;
   private static final Predicate<ItemStack> NON_EMPTY_ITEMS = (itemStack) -> {
      return !isEmpty(itemStack);
   };

   @Nullable
   public static Material parseMaterial(@Nullable String input) {
      return (Material)MinecraftEnumUtils.parseEnum(Material.class, input);
   }

   public static boolean isContainer(@Nullable Material material) {
      if (material == null) {
         return false;
      } else if (isChest(material)) {
         return true;
      } else if (isShulkerBox(material)) {
         return true;
      } else {
         switch(material) {
         case BARREL:
         case BREWING_STAND:
         case DISPENSER:
         case DROPPER:
         case HOPPER:
         case FURNACE:
         case BLAST_FURNACE:
         case SMOKER:
         case ENDER_CHEST:
            return true;
         default:
            return false;
         }
      }
   }

   public static boolean isChest(@Nullable Material material) {
      if (material == null) {
         return false;
      } else {
         return material.data == Chest.class;
      }
   }

   public static boolean isShulkerBox(@Nullable Material material) {
      if (material == null) {
         return false;
      } else {
         switch(material) {
         case SHULKER_BOX:
         case WHITE_SHULKER_BOX:
         case ORANGE_SHULKER_BOX:
         case MAGENTA_SHULKER_BOX:
         case LIGHT_BLUE_SHULKER_BOX:
         case YELLOW_SHULKER_BOX:
         case LIME_SHULKER_BOX:
         case PINK_SHULKER_BOX:
         case GRAY_SHULKER_BOX:
         case LIGHT_GRAY_SHULKER_BOX:
         case CYAN_SHULKER_BOX:
         case PURPLE_SHULKER_BOX:
         case BLUE_SHULKER_BOX:
         case BROWN_SHULKER_BOX:
         case GREEN_SHULKER_BOX:
         case RED_SHULKER_BOX:
         case BLACK_SHULKER_BOX:
            return true;
         default:
            return false;
         }
      }
   }

   public static boolean isSign(@Nullable Material material) {
      if (material == null) {
         return false;
      } else {
         return material.data == Sign.class || material.data == WallSign.class;
      }
   }

   public static boolean isHangingSign(@Nullable Material material) {
      if (material == null) {
         return false;
      } else {
         return material.data == HangingSign.class || material.data == WallHangingSign.class;
      }
   }

   public static boolean isRail(@Nullable Material material) {
      if (material == null) {
         return false;
      } else {
         switch(material) {
         case RAIL:
         case POWERED_RAIL:
         case DETECTOR_RAIL:
         case ACTIVATOR_RAIL:
            return true;
         default:
            return false;
         }
      }
   }

   public static boolean isClickableDoor(Material material) {
      return material.data == Door.class || material.data == TrapDoor.class || material.data == Gate.class;
   }

   public static boolean isClickableSwitch(Material material) {
      return material.data == Switch.class;
   }

   public static String formatMaterialName(Material material) {
      Validate.notNull(material, (String)"material is null");
      return EnumUtils.formatEnumName(material.name());
   }

   public static String formatMaterialName(@ReadOnly ItemStack itemStack) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      return formatMaterialName(itemStack.getType());
   }

   public static boolean isEmpty(@Nullable @ReadOnly ItemStack itemStack) {
      return itemStack == null || itemStack.getType() == Material.AIR || itemStack.getAmount() <= 0;
   }

   public static boolean isEmpty(@Nullable UnmodifiableItemStack itemStack) {
      return isEmpty(asItemStackOrNull(itemStack));
   }

   @Nullable
   public static ItemStack getNullIfEmpty(@Nullable @ReadOnly ItemStack itemStack) {
      return isEmpty(itemStack) ? null : itemStack;
   }

   @Nullable
   public static UnmodifiableItemStack getNullIfEmpty(@Nullable UnmodifiableItemStack itemStack) {
      return isEmpty(itemStack) ? null : itemStack;
   }

   public static ItemStack getOrEmpty(@Nullable @ReadOnly ItemStack itemStack) {
      if (!isEmpty(itemStack)) {
         return (ItemStack)Unsafe.assertNonNull(itemStack);
      } else {
         return itemStack != null && itemStack.getType() == Material.AIR ? itemStack : new ItemStack(Material.AIR);
      }
   }

   @PolyNull
   public static UnmodifiableItemStack getFallbackIfNull(@Nullable UnmodifiableItemStack item, @PolyNull UnmodifiableItemStack fallback) {
      return item == null ? fallback : item;
   }

   @Nullable
   public static ItemStack cloneOrNullIfEmpty(@Nullable @ReadOnly ItemStack itemStack) {
      return isEmpty(itemStack) ? null : ((ItemStack)Unsafe.assertNonNull(itemStack)).clone();
   }

   @Nullable
   public static ItemStack copyOrNullIfEmpty(@Nullable UnmodifiableItemStack itemStack) {
      return isEmpty(itemStack) ? null : ((UnmodifiableItemStack)Unsafe.assertNonNull(itemStack)).copy();
   }

   @PolyNull
   public static ItemStack copyOrNull(@PolyNull UnmodifiableItemStack itemStack) {
      return itemStack == null ? null : itemStack.copy();
   }

   @PolyNull
   public static UnmodifiableItemStack shallowCopy(@PolyNull UnmodifiableItemStack itemStack) {
      return itemStack == null ? null : itemStack.shallowCopy();
   }

   @PolyNull
   public static UnmodifiableItemStack unmodifiableClone(@PolyNull @ReadOnly ItemStack itemStack) {
      return itemStack == null ? null : UnmodifiableItemStack.of(itemStack.clone());
   }

   public static UnmodifiableItemStack nonNullUnmodifiableClone(@ReadOnly ItemStack itemStack) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      return (UnmodifiableItemStack)Unsafe.assertNonNull(unmodifiableClone(itemStack));
   }

   @Nullable
   public static UnmodifiableItemStack unmodifiableOrNullIfEmpty(@Nullable @ReadOnly ItemStack itemStack) {
      return UnmodifiableItemStack.of(getNullIfEmpty(itemStack));
   }

   /** @deprecated */
   @Deprecated
   @PolyNull
   public static ItemStack asItemStackOrNull(@PolyNull UnmodifiableItemStack itemStack) {
      return itemStack == null ? null : asItemStack(itemStack);
   }

   /** @deprecated */
   @Deprecated
   public static ItemStack asItemStack(UnmodifiableItemStack itemStack) {
      assert itemStack != null;

      return ((SKUnmodifiableItemStack)itemStack).getInternalItemStack();
   }

   public static ItemStack copySingleItem(@ReadOnly ItemStack itemStack) {
      return copyWithAmount((ItemStack)itemStack, 1);
   }

   public static ItemStack copyWithAmount(@ReadOnly ItemStack itemStack, int amount) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      ItemStack copy = itemStack.clone();
      copy.setAmount(amount);
      return copy;
   }

   public static ItemStack copyWithAmount(UnmodifiableItemStack itemStack, int amount) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      return copyWithAmount(asItemStack(itemStack), amount);
   }

   public static UnmodifiableItemStack unmodifiableCopyWithAmount(@ReadOnly ItemStack itemStack, int amount) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      return UnmodifiableItemStack.ofNonNull(copyWithAmount(itemStack, amount));
   }

   public static UnmodifiableItemStack unmodifiableCopyWithAmount(UnmodifiableItemStack itemStack, int amount) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      return itemStack.getAmount() != amount ? UnmodifiableItemStack.ofNonNull(copyWithAmount(itemStack, amount)) : itemStack;
   }

   public static int clampAmount(@ReadOnly ItemStack itemStack, int amount) {
      return MathUtils.clamp(amount, 1, itemStack.getMaxStackSize());
   }

   public static int clampAmount(Material itemType, int amount) {
      return MathUtils.clamp(amount, 1, itemType.getMaxStackSize());
   }

   @Nullable
   public static ItemStack increaseItemAmount(@Nullable @ReadWrite ItemStack itemStack, int amountToIncrease) {
      if (isEmpty(itemStack)) {
         return null;
      } else {
         assert itemStack != null;

         int newAmount = Math.min(itemStack.getAmount() + amountToIncrease, itemStack.getMaxStackSize());
         if (newAmount <= 0) {
            return null;
         } else {
            itemStack.setAmount(newAmount);
            return itemStack;
         }
      }
   }

   @Nullable
   public static ItemStack decreaseItemAmount(@Nullable @ReadWrite ItemStack itemStack, int amountToDecrease) {
      return increaseItemAmount(itemStack, -amountToDecrease);
   }

   public static int getItemStackAmount(@Nullable @ReadOnly ItemStack itemStack) {
      return isEmpty(itemStack) ? 0 : ((ItemStack)Unsafe.assertNonNull(itemStack)).getAmount();
   }

   public static int getItemStackAmount(@Nullable UnmodifiableItemStack itemStack) {
      return getItemStackAmount(asItemStackOrNull(itemStack));
   }

   public static ItemStack createItemStack(Material type, int amount, @Nullable String displayName, @Nullable @ReadOnly List<? extends String> lore) {
      assert type != null;

      assert type.isItem();

      ItemStack itemStack = new ItemStack(type, amount);
      return setDisplayNameAndLore(itemStack, displayName, lore);
   }

   public static ItemStack createItemStack(ItemData itemData, int amount, @Nullable String displayName, @Nullable @ReadOnly List<? extends String> lore) {
      Validate.notNull(itemData, (String)"itemData is null");
      return setDisplayNameAndLore(itemData.createItemStack(amount), displayName, lore);
   }

   public static ItemStack setDisplayNameAndLore(@ReadWrite ItemStack itemStack, @Nullable String displayName, @Nullable @ReadOnly List<? extends String> lore) {
      return setItemMeta(itemStack, displayName, lore, (Integer)null);
   }

   /** @deprecated */
   @Deprecated
   public static ItemStack setItemMetaLegacy(@ReadWrite ItemStack itemStack, @Nullable String displayName, @Nullable @ReadOnly List<? extends String> lore, @Nullable Integer maxStackSize) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      ItemMeta meta = itemStack.getItemMeta();
      if (meta != null) {
         if (displayName != null) {
            meta.setDisplayName(displayName);
         }

         if (lore != null) {
            meta.setLore((List)Unsafe.cast(lore));
         }

         if (maxStackSize != null) {
            meta.setMaxStackSize(maxStackSize);
         }

         itemStack.setItemMeta(meta);
      }

      return itemStack;
   }

   public static ItemStack setItemMeta(@ReadWrite ItemStack itemStack, String displayName, @ReadOnly List<? extends String> lore, Integer maxStackSize) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      ItemMeta originalMeta = itemStack.getItemMeta();
      if (originalMeta != null) {
         ItemMeta newItemMeta = originalMeta;
         if (displayName != null || lore != null) {
            LinkedHashMap<String, Object> serializedMeta = new LinkedHashMap(originalMeta.serialize());
            if (displayName != null) {
               serializedMeta.put("display-name", displayName);
            }

            if (lore != null) {
               serializedMeta.put("lore", Unsafe.cast(lore));
            }

            ItemMeta deserializedMeta = ItemSerialization.deserializeItemMeta(serializedMeta);
            if (deserializedMeta != null) {
               newItemMeta = deserializedMeta;
            }
         }

         if (maxStackSize != null) {
            newItemMeta.setMaxStackSize(maxStackSize);
         }

         itemStack.setItemMeta(newItemMeta);
      }

      return itemStack;
   }

   public static ItemStack setItemName(@ReadWrite ItemStack itemStack, String itemName) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      if (itemName == null) {
         return itemStack;
      } else {
         ItemMeta originalMeta = itemStack.getItemMeta();
         if (originalMeta != null) {
            ItemMeta newItemMeta = originalMeta;
            LinkedHashMap<String, Object> serializedMeta = new LinkedHashMap(originalMeta.serialize());
            serializedMeta.put("item-name", itemName);
            ItemMeta deserializedMeta = ItemSerialization.deserializeItemMeta(serializedMeta);
            if (deserializedMeta != null) {
               newItemMeta = deserializedMeta;
            }

            itemStack.setItemMeta(newItemMeta);
         }

         return itemStack;
      }
   }

   public static ItemStack clearDisplayNameAndLore(@ReadWrite ItemStack itemStack) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      ItemMeta itemMeta = itemStack.getItemMeta();
      if (itemMeta == null) {
         return itemStack;
      } else {
         boolean itemMetaModified = false;
         if (itemMeta.hasDisplayName()) {
            itemMeta.setDisplayName((String)null);
            itemMetaModified = true;
         }

         if (itemMeta.hasLore()) {
            itemMeta.setLore((List)null);
            itemMetaModified = true;
         }

         if (itemMetaModified) {
            itemStack.setItemMeta(itemMeta);
         }

         return itemStack;
      }
   }

   public static ItemStack setDisplayName(@ReadWrite ItemStack itemStack, @Nullable String displayName) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      ItemMeta itemMeta = itemStack.getItemMeta();
      if (itemMeta == null) {
         return itemStack;
      } else if (displayName == null && !itemMeta.hasDisplayName()) {
         return itemStack;
      } else {
         itemMeta.setDisplayName(displayName);
         itemStack.setItemMeta(itemMeta);
         return itemStack;
      }
   }

   public static ItemStack setUnbreakable(@ReadWrite ItemStack itemStack) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      ItemMeta itemMeta = itemStack.getItemMeta();
      if (itemMeta == null) {
         return itemStack;
      } else {
         if (!itemMeta.isUnbreakable()) {
            itemMeta.setUnbreakable(true);
            itemStack.setItemMeta(itemMeta);
         }

         return itemStack;
      }
   }

   public static ItemStack setLeatherColor(@ReadWrite ItemStack leatherArmorItem, @Nullable Color color) {
      Validate.notNull(leatherArmorItem, (String)"leatherArmorItem is null");
      ItemMeta meta = leatherArmorItem.getItemMeta();
      if (meta instanceof LeatherArmorMeta) {
         LeatherArmorMeta leatherMeta = (LeatherArmorMeta)meta;
         leatherMeta.setColor(color);
         leatherArmorItem.setItemMeta(leatherMeta);
      }

      return leatherArmorItem;
   }

   public static String getDisplayNameOrEmpty(@Nullable @ReadOnly ItemStack itemStack) {
      return itemStack == null ? "" : getDisplayNameOrEmpty(itemStack.getItemMeta());
   }

   public static String getDisplayNameOrEmpty(@Nullable @ReadOnly ItemMeta itemMeta) {
      if (itemMeta == null) {
         return "";
      } else {
         return !itemMeta.hasDisplayName() ? "" : itemMeta.getDisplayName();
      }
   }

   public static boolean hasDisplayName(@Nullable @ReadOnly ItemStack itemStack) {
      return !getDisplayNameOrEmpty(itemStack).isEmpty();
   }

   public static Material getWoolType(DyeColor dyeColor) {
      switch(dyeColor) {
      case ORANGE:
         return Material.ORANGE_WOOL;
      case MAGENTA:
         return Material.MAGENTA_WOOL;
      case LIGHT_BLUE:
         return Material.LIGHT_BLUE_WOOL;
      case YELLOW:
         return Material.YELLOW_WOOL;
      case LIME:
         return Material.LIME_WOOL;
      case PINK:
         return Material.PINK_WOOL;
      case GRAY:
         return Material.GRAY_WOOL;
      case LIGHT_GRAY:
         return Material.LIGHT_GRAY_WOOL;
      case CYAN:
         return Material.CYAN_WOOL;
      case PURPLE:
         return Material.PURPLE_WOOL;
      case BLUE:
         return Material.BLUE_WOOL;
      case BROWN:
         return Material.BROWN_WOOL;
      case GREEN:
         return Material.GREEN_WOOL;
      case RED:
         return Material.RED_WOOL;
      case BLACK:
         return Material.BLACK_WOOL;
      case WHITE:
      default:
         return Material.WHITE_WOOL;
      }
   }

   public static Material getCarpetType(DyeColor dyeColor) {
      switch(dyeColor) {
      case ORANGE:
         return Material.ORANGE_CARPET;
      case MAGENTA:
         return Material.MAGENTA_CARPET;
      case LIGHT_BLUE:
         return Material.LIGHT_BLUE_CARPET;
      case YELLOW:
         return Material.YELLOW_CARPET;
      case LIME:
         return Material.LIME_CARPET;
      case PINK:
         return Material.PINK_CARPET;
      case GRAY:
         return Material.GRAY_CARPET;
      case LIGHT_GRAY:
         return Material.LIGHT_GRAY_CARPET;
      case CYAN:
         return Material.CYAN_CARPET;
      case PURPLE:
         return Material.PURPLE_CARPET;
      case BLUE:
         return Material.BLUE_CARPET;
      case BROWN:
         return Material.BROWN_CARPET;
      case GREEN:
         return Material.GREEN_CARPET;
      case RED:
         return Material.RED_CARPET;
      case BLACK:
         return Material.BLACK_CARPET;
      case WHITE:
      default:
         return Material.WHITE_CARPET;
      }
   }

   public static ItemStack createHead(UUID uuid, String name, String skinUrl) {
      ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
      PlayerProfile profile = Bukkit.createPlayerProfile(uuid, name);

      URL url;
      try {
         url = (new URI(skinUrl)).toURL();
      } catch (Exception var9) {
         Log.debug((String)("Invalid skin URL: " + skinUrl), (Throwable)var9);
         return itemStack;
      }

      profile.getTextures().setSkin(url);
      SkullMeta skullMeta = (SkullMeta)Unsafe.assertNonNull((SkullMeta)itemStack.getItemMeta());

      try {
         skullMeta.setOwnerProfile(profile);
      } catch (Exception var8) {
         Log.debug((String)("Failed to apply skull profile: " + skinUrl), (Throwable)var8);
         return itemStack;
      }

      itemStack.setItemMeta(skullMeta);
      return itemStack;
   }

   public static final ItemStack getSkull_MHF_ArrowLeft() {
      return createHead(UUID.fromString("a68f0b64-8d14-4000-a95f4b9ba14f-8df9"), "MHF_ArrowLeft", "https://textures.minecraft.net/texture/f7aacad193e2226971ed95302dba433438be4644fbab5ebf818054061667fbe2");
   }

   public static final ItemStack getSkull_MHF_ArrowRight() {
      return createHead(UUID.fromString("50c8510b-5ea0-4d60-be9a7d542d6c-d156"), "MHF_ArrowRight", "https://textures.minecraft.net/texture/d34ef0638537222b20f480694dadc0f85fbe0759d581aa7fcdf2e43139377158");
   }

   public static boolean isDamageable(@Nullable @ReadOnly ItemStack itemStack) {
      return itemStack == null ? false : isDamageable(itemStack.getType());
   }

   public static boolean isDamageable(Material type) {
      return type.getMaxDurability() > 0;
   }

   public static int getDurability(@Nullable @ReadOnly ItemStack itemStack) {
      if (!isDamageable(itemStack)) {
         return 0;
      } else {
         assert itemStack != null;

         ItemMeta itemMeta = itemStack.getItemMeta();
         return itemMeta instanceof Damageable ? ((Damageable)itemMeta).getDamage() : 0;
      }
   }

   @Nullable
   public static ItemStack ensureBukkitItemStack(@Nullable @ReadOnly ItemStack itemStack) {
      if (itemStack == null) {
         return null;
      } else if (itemStack.getClass() == ItemStack.class) {
         return itemStack;
      } else {
         ItemStack bukkitItemStack = new ItemStack(itemStack.getType(), itemStack.getAmount());
         ItemMeta itemMeta = itemStack.getItemMeta();
         if (!Bukkit.getItemFactory().equals(itemMeta, (ItemMeta)null)) {
            bukkitItemStack.setItemMeta(itemMeta);
         }

         return bukkitItemStack;
      }
   }

   public static String getSimpleItemInfo(@Nullable UnmodifiableItemStack item) {
      if (item == null) {
         return "empty";
      } else {
         StringBuilder sb = new StringBuilder();
         sb.append(item.getAmount()).append('x').append(item.getType());
         return sb.toString();
      }
   }

   public static String getSimpleRecipeInfo(@Nullable TradingRecipe recipe) {
      if (recipe == null) {
         return "none";
      } else {
         StringBuilder sb = new StringBuilder();
         sb.append("[item1=").append(getSimpleItemInfo(recipe.getItem1())).append(", item2=").append(getSimpleItemInfo(recipe.getItem2())).append(", result=").append(getSimpleItemInfo(recipe.getResultItem())).append("]");
         return sb.toString();
      }
   }

   public static boolean equals(@Nullable UnmodifiableItemStack unmodifiableItemStack, @Nullable @ReadOnly ItemStack itemStack) {
      if (unmodifiableItemStack == null) {
         return itemStack == null;
      } else {
         return unmodifiableItemStack.equals(itemStack);
      }
   }

   public static boolean isSimilar(@Nullable @ReadOnly ItemStack item1, @Nullable @ReadOnly ItemStack item2) {
      if (item1 == null) {
         return item2 == null;
      } else {
         return item1.isSimilar(item2);
      }
   }

   public static boolean isSimilar(@Nullable UnmodifiableItemStack item1, @Nullable UnmodifiableItemStack item2) {
      return isSimilar(asItemStackOrNull(item1), asItemStackOrNull(item2));
   }

   public static boolean isSimilar(@Nullable UnmodifiableItemStack item1, @Nullable @ReadOnly ItemStack item2) {
      return isSimilar(asItemStackOrNull(item1), item2);
   }

   public static boolean isSimilar(@Nullable @ReadOnly ItemStack item, Material type, @Nullable String displayName, @Nullable @ReadOnly List<? extends String> lore) {
      if (item == null) {
         return false;
      } else if (item.getType() != type) {
         return false;
      } else {
         boolean checkDisplayName = displayName != null && !displayName.isEmpty();
         boolean checkLore = lore != null && !lore.isEmpty();
         if (!checkDisplayName && !checkLore) {
            return true;
         } else {
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta == null) {
               return false;
            } else {
               if (checkDisplayName) {
                  assert displayName != null;

                  if (!itemMeta.hasDisplayName() || !displayName.equals(itemMeta.getDisplayName())) {
                     return false;
                  }
               }

               if (checkLore) {
                  assert lore != null;

                  if (!itemMeta.hasLore() || !lore.equals(itemMeta.getLore())) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   public static boolean matchesData(@Nullable @ReadOnly ItemStack provided, @Nullable @ReadOnly ItemStack required) {
      return matchesData(provided, required, true);
   }

   public static boolean matchesData(@Nullable UnmodifiableItemStack provided, @Nullable UnmodifiableItemStack required) {
      return matchesData(provided, required, true);
   }

   public static boolean matchesData(@Nullable UnmodifiableItemStack provided, @Nullable UnmodifiableItemStack required, boolean matchPartialLists) {
      return matchesData(asItemStackOrNull(provided), asItemStackOrNull(required), matchPartialLists);
   }

   public static boolean matchesData(@Nullable @ReadOnly ItemStack provided, @Nullable @ReadOnly ItemStack required, boolean matchPartialLists) {
      if (provided == required) {
         return true;
      } else if (required == null) {
         return true;
      } else if (provided == null) {
         return false;
      } else {
         return provided.getType() != required.getType() ? false : matchesData(ItemStackMetaTag.of(provided), ItemStackMetaTag.of(required), matchPartialLists);
      }
   }

   public static boolean matchesData(@Nullable UnmodifiableItemStack provided, Material requiredType, ItemStackMetaTag required, boolean matchPartialLists) {
      return matchesData(asItemStackOrNull(provided), requiredType, required, matchPartialLists);
   }

   public static boolean matchesData(@Nullable @ReadOnly ItemStack provided, Material requiredType, ItemStackMetaTag required, boolean matchPartialLists) {
      if (provided == null) {
         return false;
      } else if (provided.getType() != requiredType) {
         return false;
      } else {
         return required.isEmpty() ? true : matchesData(ItemStackMetaTag.of(provided), required, matchPartialLists);
      }
   }

   public static boolean matchesData(ItemStackMetaTag provided, ItemStackMetaTag required, boolean matchPartialLists) {
      return required.matches(provided, matchPartialLists);
   }

   public static Predicate<ItemStack> emptyItems() {
      return EMPTY_ITEMS;
   }

   public static Predicate<ItemStack> nonEmptyItems() {
      return NON_EMPTY_ITEMS;
   }

   public static Predicate<ItemStack> matchingItems(ItemData itemData) {
      Validate.notNull(itemData, (String)"itemData is null");
      return (itemStack) -> {
         return itemData.matches(itemStack);
      };
   }

   public static Predicate<ItemStack> matchingItems(@ReadOnly List<? extends ItemData> itemDataList) {
      Validate.notNull(itemDataList, (String)"itemDataList is null");

      assert !CollectionUtils.containsNull(itemDataList);

      return (itemStack) -> {
         if (isEmpty(itemStack)) {
            return false;
         } else {
            Iterator var2 = itemDataList.iterator();

            ItemData itemData;
            do {
               if (!var2.hasNext()) {
                  return false;
               }

               itemData = (ItemData)var2.next();

               assert itemData != null;
            } while(!itemData.matches(itemStack));

            return true;
         }
      };
   }

   public static Predicate<ItemStack> similarItems(@ReadOnly ItemStack itemStack) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      return (otherItemStack) -> {
         return itemStack.isSimilar(otherItemStack);
      };
   }

   public static Predicate<ItemStack> similarItems(UnmodifiableItemStack itemStack) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      return (otherItemStack) -> {
         return itemStack.isSimilar(otherItemStack);
      };
   }

   public static Predicate<ItemStack> itemsOfType(Material itemType) {
      Validate.notNull(itemType, (String)"itemType is null");
      return (itemStack) -> {
         return itemStack != null && itemStack.getType() == itemType;
      };
   }

   private ItemUtils() {
   }
}
