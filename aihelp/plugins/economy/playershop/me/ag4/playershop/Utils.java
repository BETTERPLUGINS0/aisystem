package me.ag4.playershop;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.geysermc.floodgate.api.FloodgateApi;

public class Utils {
   public static ItemStack createItem(Material material, String name) {
      ItemStack itemStack = new ItemStack(material);
      ItemMeta meta = itemStack.getItemMeta();

      assert meta != null;

      meta.setDisplayName(hex(name));
      meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
      itemStack.setItemMeta(meta);
      return itemStack;
   }

   public static ItemStack createItem(Material material, String name, int amount) {
      ItemStack itemStack = new ItemStack(material, amount);
      ItemMeta meta = itemStack.getItemMeta();

      assert meta != null;

      meta.setDisplayName(hex(name));
      meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
      itemStack.setItemMeta(meta);
      return itemStack;
   }

   public static ItemStack createItem(Material material, String name, boolean enchanted) {
      ItemStack itemStack = new ItemStack(material);
      ItemMeta meta = itemStack.getItemMeta();

      assert meta != null;

      meta.setDisplayName(hex(name));
      if (enchanted) {
         meta.addEnchant(Enchantment.CHANNELING, 1, true);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
      }

      itemStack.setItemMeta(meta);
      return itemStack;
   }

   public static ItemStack createItem(Material material, String name, int amount, String[] lore) {
      ItemStack itemStack = new ItemStack(material, amount);
      ItemMeta meta = itemStack.getItemMeta();

      assert meta != null;

      meta.setDisplayName(hex(name));
      ArrayList<String> list = new ArrayList();
      String[] var7 = lore;
      int var8 = lore.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         String loreList = var7[var9];
         list.add(hex(loreList));
      }

      meta.setLore(list);
      meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
      itemStack.setItemMeta(meta);
      return itemStack;
   }

   public static ItemStack createItem(Material material, String name, String[] lore) {
      ItemStack itemStack = new ItemStack(material);
      ItemMeta meta = itemStack.getItemMeta();

      assert meta != null;

      meta.setDisplayName(hex(name));
      ArrayList<String> list = new ArrayList();
      String[] var6 = lore;
      int var7 = lore.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         String loreList = var6[var8];
         list.add(hex(loreList));
      }

      meta.setLore(list);
      meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
      itemStack.setItemMeta(meta);
      return itemStack;
   }

   public static ItemStack createItem(Material material, String name, String[] lore, boolean enchanted) {
      ItemStack itemStack = new ItemStack(material);
      ItemMeta meta = itemStack.getItemMeta();

      assert meta != null;

      meta.setDisplayName(hex(name));
      ArrayList<String> list = new ArrayList();
      String[] var7 = lore;
      int var8 = lore.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         String loreList = var7[var9];
         list.add(hex(loreList));
      }

      meta.setLore(list);
      if (enchanted) {
         meta.addEnchant(Enchantment.CHANNELING, 1, true);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
      }

      itemStack.setItemMeta(meta);
      return itemStack;
   }

   public static ItemStack createItem(Material material, String name, ArrayList<String> lore) {
      ItemStack itemStack = new ItemStack(material);
      ItemMeta meta = itemStack.getItemMeta();

      assert meta != null;

      meta.setDisplayName(hex(name));
      ArrayList<String> list = new ArrayList();
      Iterator var6 = lore.iterator();

      while(var6.hasNext()) {
         String loreList = (String)var6.next();
         list.add(hex(loreList));
      }

      meta.setLore(list);
      meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
      itemStack.setItemMeta(meta);
      return itemStack;
   }

   public static ItemStack createItem(Material material, String name, List<String> lore) {
      ItemStack itemStack = new ItemStack(material);
      ItemMeta meta = itemStack.getItemMeta();

      assert meta != null;

      meta.setDisplayName(hex(name));
      ArrayList<String> list = new ArrayList();
      Iterator var6 = lore.iterator();

      while(var6.hasNext()) {
         String loreList = (String)var6.next();
         list.add(hex(loreList));
      }

      meta.setLore(list);
      meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
      itemStack.setItemMeta(meta);
      return itemStack;
   }

   public static ItemStack createItem(Material material, String name, ArrayList<String> lore, boolean enchanted) {
      ItemStack itemStack = new ItemStack(material);
      ItemMeta meta = itemStack.getItemMeta();

      assert meta != null;

      meta.setDisplayName(hex(name));
      ArrayList<String> list = new ArrayList();
      Iterator var7 = lore.iterator();

      while(var7.hasNext()) {
         String loreList = (String)var7.next();
         list.add(hex(loreList));
      }

      meta.setLore(list);
      meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
      if (enchanted) {
         meta.addEnchant(Enchantment.CHANNELING, 1, true);
         meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
      }

      itemStack.setItemMeta(meta);
      return itemStack;
   }

   public static Long getCurrentTime() {
      return System.currentTimeMillis() / 1000L;
   }

   public static boolean isBedrockPlayer(Player player) {
      FloodgateApi floodgateApi = FloodgateApi.getInstance();
      return floodgateApi.isFloodgateId(player.getUniqueId());
   }

   public static String decimalFormatToString(double value) {
      DecimalFormatSymbols symbols = new DecimalFormatSymbols();
      symbols.setDecimalSeparator('.');
      DecimalFormat df = new DecimalFormat("#,###.###", symbols);
      return df.format(value);
   }

   public static boolean isTopInventory(int getRawSlot, InventoryView view) {
      return getRawSlot < view.getTopInventory().getSize();
   }

   public static String hex(String message) {
      Pattern pattern1 = Pattern.compile("&(?=#[a-fA-F0-9]{6})");
      Matcher matcher1 = pattern1.matcher(message);
      message = matcher1.replaceAll("");
      Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

      for(Matcher matcher = pattern.matcher(message); matcher.find(); matcher = pattern.matcher(message)) {
         String hexCode = message.substring(matcher.start(), matcher.end());
         String replaceSharp = hexCode.replace('#', 'x');
         char[] ch = replaceSharp.toCharArray();
         StringBuilder builder = new StringBuilder("");
         char[] var9 = ch;
         int var10 = ch.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            char c = var9[var11];
            builder.append("&" + c);
         }

         message = message.replace(hexCode, builder.toString());
      }

      return ChatColor.translateAlternateColorCodes('&', message);
   }

   public static boolean isInventoryFull(Inventory inventory) {
      return inventory.firstEmpty() == -1;
   }

   public static void addItemManually(Inventory inventory, ItemStack item) {
      for(int i = 0; i < inventory.getSize(); ++i) {
         if (inventory.getItem(i) == null) {
            inventory.setItem(i, item);
            return;
         }
      }

   }

   public static String formatPrice(double number) {
      boolean hasFractionalPart = number % 1.0D != 0.0D;
      String pattern = hasFractionalPart ? "0.00" : "#,###";
      DecimalFormat decimalFormat = new DecimalFormat(pattern);
      return decimalFormat.format(number);
   }
}
