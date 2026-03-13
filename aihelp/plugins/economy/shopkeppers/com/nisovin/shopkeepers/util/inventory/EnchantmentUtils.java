package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.util.bukkit.NamespacedKeyUtils;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class EnchantmentUtils {
   private static final int MIN_LEVEL = -32768;
   private static final int MAX_LEVEL = 32767;
   private static final Map<String, Integer> LEVEL_NAMES = new HashMap();
   private static final Map<String, Enchantment> ALIASES;

   @Nullable
   public static Enchantment parseEnchantment(String input) {
      Validate.notNull(input, (String)"input is null");
      NamespacedKey namespacedKey = NamespacedKeyUtils.parse(input);
      if (namespacedKey == null) {
         return null;
      } else if (!namespacedKey.getNamespace().equals("minecraft")) {
         return null;
      } else {
         String enchantmentName = namespacedKey.getKey();
         Enchantment enchantment = (Enchantment)ALIASES.get(enchantmentName);
         return enchantment != null ? enchantment : (Enchantment)Compat.getProvider().getRegistry(Enchantment.class).get(namespacedKey);
      }
   }

   @Nullable
   public static EnchantmentUtils.EnchantmentWithLevel parseEnchantmentWithLevel(String input) {
      Validate.notNull(input, (String)"input is null");
      String formattedInput = input.trim();
      formattedInput = formattedInput.toLowerCase(Locale.ROOT);
      String enchantmentInput = formattedInput;
      Integer level = null;
      int lastSpace = formattedInput.lastIndexOf(32);
      if (lastSpace != -1) {
         String levelString = formattedInput.substring(lastSpace + 1);
         level = ConversionUtils.parseInt(levelString);
         if (level == null) {
            level = (Integer)LEVEL_NAMES.get(levelString);
         }

         if (level != null) {
            enchantmentInput = formattedInput.substring(0, lastSpace);
         }
      }

      Enchantment enchantment = parseEnchantment(enchantmentInput);
      if (enchantment == null) {
         if (level == null && lastSpace != -1) {
            enchantmentInput = enchantmentInput.substring(0, lastSpace);
            enchantment = parseEnchantment(enchantmentInput);
         }

         if (enchantment == null) {
            return null;
         }
      }

      assert enchantment != null;

      if (level != null && level != Integer.MIN_VALUE) {
         if (level == Integer.MAX_VALUE) {
            level = enchantment.getMaxLevel();
         } else if (level < -32768) {
            level = -32768;
         } else if (level > 32767) {
            level = 32767;
         }
      } else {
         level = enchantment.getStartLevel();
      }

      assert level != null;

      return new EnchantmentUtils.EnchantmentWithLevel(enchantment, level);
   }

   public static boolean isValidLevel(Enchantment enchantment, int level) {
      Validate.notNull(enchantment, (String)"enchantment is null");
      return level >= enchantment.getStartLevel() && level <= enchantment.getMaxLevel();
   }

   public static ItemStack createEnchantedBook(Enchantment enchantment, int level) {
      Validate.notNull(enchantment, (String)"enchantment is null");
      ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
      EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta)Unsafe.castNonNull(item.getItemMeta());
      itemMeta.addStoredEnchant(enchantment, level, true);
      item.setItemMeta(itemMeta);
      return item;
   }

   private EnchantmentUtils() {
   }

   static {
      LEVEL_NAMES.put("min", Integer.MIN_VALUE);
      LEVEL_NAMES.put("max", Integer.MAX_VALUE);
      LEVEL_NAMES.put("i", 1);
      LEVEL_NAMES.put("ii", 2);
      LEVEL_NAMES.put("iii", 3);
      LEVEL_NAMES.put("iv", 4);
      LEVEL_NAMES.put("v", 5);
      LEVEL_NAMES.put("vi", 6);
      LEVEL_NAMES.put("vii", 7);
      LEVEL_NAMES.put("viii", 8);
      LEVEL_NAMES.put("ix", 9);
      LEVEL_NAMES.put("x", 10);
      ALIASES = new HashMap();
      ALIASES.put("curse_of_binding", Enchantment.BINDING_CURSE);
      ALIASES.put("curse_of_vanishing", Enchantment.VANISHING_CURSE);
      ALIASES.put("sweeping_edge", Enchantment.SWEEPING_EDGE);
      ALIASES.put("channelling", Enchantment.CHANNELING);
   }

   public static final class EnchantmentWithLevel {
      private final Enchantment enchantment;
      private final int level;

      public EnchantmentWithLevel(Enchantment enchantment, int level) {
         Validate.notNull(enchantment, (String)"enchantment is null");
         this.enchantment = enchantment;
         this.level = level;
      }

      public Enchantment getEnchantment() {
         return this.enchantment;
      }

      public int getLevel() {
         return this.level;
      }

      public String toString() {
         StringBuilder builder = new StringBuilder();
         builder.append("EnchantmentWithLevel [enchantment=");
         builder.append(this.enchantment);
         builder.append(", level=");
         builder.append(this.level);
         builder.append("]");
         return builder.toString();
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + this.enchantment.hashCode();
         result = 31 * result + this.level;
         return result;
      }

      public boolean equals(@Nullable Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof EnchantmentUtils.EnchantmentWithLevel)) {
            return false;
         } else {
            EnchantmentUtils.EnchantmentWithLevel other = (EnchantmentUtils.EnchantmentWithLevel)obj;
            if (!this.enchantment.equals(other.enchantment)) {
               return false;
            } else {
               return this.level == other.level;
            }
         }
      }
   }
}
