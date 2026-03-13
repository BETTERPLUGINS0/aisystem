package com.ryandw11.structure.loottables;

import com.ryandw11.structure.exceptions.LootTableException;
import com.ryandw11.structure.utils.NumberStylizer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class StandardLootItem implements LootItem {
   private int weight;
   private final String amount;
   private final Map<String, String> enchants;
   private ItemStack item;

   public StandardLootItem(String customName, String material, int amount, int weight, List<String> lore, Map<String, String> enchants) {
      this.weight = weight;

      try {
         this.item = new ItemStack(Material.valueOf(material.toUpperCase()));
      } catch (IllegalArgumentException var8) {
         throw new LootTableException("Unknown Material Type: " + material);
      }

      this.amount = amount.makeConcatWithConstants<invokedynamic>(amount);
      this.item.setAmount(amount);
      if (customName != null) {
         ItemMeta meta = (ItemMeta)Objects.requireNonNull(this.item.getItemMeta());
         meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', customName));
         this.item.setItemMeta(meta);
      }

      if (!lore.isEmpty()) {
         lore = (List)lore.stream().map((s) -> {
            return ChatColor.translateAlternateColorCodes('&', s);
         }).collect(Collectors.toList());
         ((ItemMeta)Objects.requireNonNull(this.item.getItemMeta())).setLore(lore);
      }

      this.enchants = enchants;
   }

   public StandardLootItem(String customName, String material, String amount, int weight, List<String> lore, Map<String, String> enchants) {
      this.weight = weight;

      try {
         this.item = new ItemStack(Material.valueOf(material.toUpperCase()));
      } catch (IllegalArgumentException var8) {
         throw new LootTableException("Unknown Material Type: " + material);
      }

      this.amount = amount;
      this.item.setAmount(NumberStylizer.getStylizedInt(amount));
      ItemMeta meta = (ItemMeta)Objects.requireNonNull(this.item.getItemMeta());
      if (customName != null) {
         meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', customName));
      }

      if (!lore.isEmpty()) {
         lore = (List)lore.stream().map((s) -> {
            return ChatColor.translateAlternateColorCodes('&', s);
         }).collect(Collectors.toList());
         meta.setLore(lore);
      }

      this.item.setItemMeta(meta);
      this.enchants = enchants;
   }

   private void applyStats(ItemStack item) {
      item.setAmount(NumberStylizer.getStylizedInt(this.amount));
      Iterator var2 = this.enchants.keySet().iterator();

      while(var2.hasNext()) {
         String enchantName = (String)var2.next();
         int level = NumberStylizer.getStylizedInt((String)this.enchants.get(enchantName));
         Enchantment enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(enchantName.toLowerCase()));
         if (enchantment == null) {
            throw new LootTableException("Invalid Enchantment: " + enchantName);
         }

         ItemMeta var7 = item.getItemMeta();
         if (var7 instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta)var7;
            enchantmentStorageMeta.addStoredEnchant(enchantment, level, true);
            item.setItemMeta(enchantmentStorageMeta);
         } else {
            item.addUnsafeEnchantment(enchantment, level);
         }
      }

   }

   public int getWeight() {
      return this.weight;
   }

   public void setWeight(int weight) {
      this.weight = weight;
   }

   public ItemStack getItemStack() {
      ItemStack cloneStack = this.item.clone();
      this.applyStats(cloneStack);
      return cloneStack;
   }

   public void setItem(ItemStack item) {
      this.item = item;
   }
}
