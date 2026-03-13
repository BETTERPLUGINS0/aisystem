package tntrun.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Registry;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;

public class Shop {
   private TNTRun plugin;
   private String invname;
   private int invsize;
   private ShopFiles shopFiles;
   private List<String> buyers = new ArrayList();
   private Map<Integer, Integer> itemSlot = new HashMap();
   private Map<String, List<ItemStack>> pitems = new HashMap();
   private Map<String, List<PotionEffect>> potionMap = new HashMap();
   private Map<String, List<String>> commandMap = new HashMap();
   private boolean doublejumpPurchase;
   private FileConfiguration cfg;
   private Map<String, Inventory> invMap = new HashMap();

   public Shop(TNTRun plugin) {
      this.plugin = plugin;
      this.shopFiles = new ShopFiles(plugin);
      this.shopFiles.setShopItems();
      this.cfg = this.shopFiles.getShopConfiguration();
      this.invsize = this.getValidSize();
      this.invname = FormattingCodesParser.parseFormattingCodes(plugin.getConfig().getString("shop.name"));
   }

   public void buildShopMenu(Player player) {
      Inventory inv = Bukkit.createInventory((InventoryHolder)null, this.getInvsize(), this.getInvname());
      this.invMap.put(player.getName(), inv);
      this.setItems(inv, player);
   }

   public void giveItem(int slot, Player player, String title) {
      int kit = (Integer)this.itemSlot.get(slot);
      if (this.doublejumpPurchase) {
         int quantity = this.cfg.getInt(kit + ".items." + kit + ".amount", 1);
         this.giveDoubleJumps(player, quantity);
      } else {
         this.buyers.add(player.getName());
         ArrayList itemlist;
         if (this.isCommandPurchase(kit)) {
            itemlist = new ArrayList();
            List<String> lore = this.cfg.getStringList(kit + ".lore");
            lore.stream().limit((long)this.cfg.getInt(kit + ".items.1.amount")).forEachOrdered((e) -> {
               itemlist.add(ChatColor.stripColor(FormattingCodesParser.parseFormattingCodes(e)));
            });
            this.commandMap.put(player.getName(), itemlist);
            player.closeInventory();
         } else {
            itemlist = new ArrayList();
            List<PotionEffect> pelist = new ArrayList();
            Iterator var8 = this.cfg.getConfigurationSection(kit + ".items").getKeys(false).iterator();

            while(var8.hasNext()) {
               String items = (String)var8.next();

               try {
                  Material material = Material.getMaterial(this.cfg.getString(kit + ".items." + items + ".material"));
                  int amount = this.cfg.getInt(kit + ".items." + items + ".amount");
                  List<String> enchantments = this.cfg.getStringList(kit + ".items." + items + ".enchantments");
                  String peffects;
                  if (material.toString().equalsIgnoreCase("POTION")) {
                     if (enchantments != null && !enchantments.isEmpty()) {
                        Iterator var18 = enchantments.iterator();

                        while(var18.hasNext()) {
                           peffects = (String)var18.next();
                           PotionEffect effect = this.createPotionEffect(peffects);
                           if (effect != null) {
                              pelist.add(effect);
                           }
                        }
                     }
                  } else {
                     peffects = FormattingCodesParser.parseFormattingCodes(this.cfg.getString(kit + ".items." + items + ".displayname"));
                     List<String> lore = this.cfg.getStringList(kit + ".items." + items + ".lore");
                     if (material.toString().equalsIgnoreCase("SPLASH_POTION")) {
                        itemlist.add(this.getPotionItem(material, amount, peffects, lore, enchantments));
                     } else {
                        itemlist.add(this.getItem(material, amount, peffects, lore, enchantments));
                     }
                  }
               } catch (Exception var15) {
                  var15.printStackTrace();
               }
            }

            player.updateInventory();
            player.closeInventory();
            this.pitems.put(player.getName(), itemlist);
            this.potionMap.put(player.getName(), pelist);
         }
      }
   }

   private void giveDoubleJumps(Player player, int quantity) {
      if (this.plugin.getConfig().getBoolean("freedoublejumps.enabled")) {
         quantity += this.plugin.getPData().getDoubleJumpsFromFile((OfflinePlayer)player);
         this.plugin.getPData().saveDoubleJumpsToFile((OfflinePlayer)player, quantity);
      } else {
         Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
         arena.getPlayerHandler().incrementDoubleJumps(player.getName(), quantity);
         if (!arena.getStatusManager().isArenaStarting() && this.plugin.getConfig().getBoolean("scoreboard.displaydoublejumps") && this.plugin.getConfig().getBoolean("special.UseScoreboard")) {
            arena.getScoreboardHandler().updateWaitingScoreboard(player);
         }
      }

      Inventory inv = player.getOpenInventory().getTopInventory();
      inv.setItem(this.getInvsize() - 1, this.setMoneyItem(inv, player));
   }

   private void logPurchase(Player player, String item, int cost) {
      if (this.plugin.getConfig().getBoolean("shop.logpurchases")) {
         ConsoleCommandSender console = this.plugin.getServer().getConsoleSender();
         String var10001 = String.valueOf(ChatColor.AQUA);
         console.sendMessage("[TNTRun_reloaded] " + var10001 + player.getName() + String.valueOf(ChatColor.WHITE) + " has bought a " + String.valueOf(ChatColor.RED) + item + String.valueOf(ChatColor.WHITE) + " for " + String.valueOf(ChatColor.RED) + Utils.getFormattedCurrency(String.valueOf(cost)));
      }

   }

   private ItemStack getItem(Material material, int amount, String displayname, List<String> lore, List<String> enchantments) {
      ItemStack item = new ItemStack(material, amount);
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName(displayname);
      if (lore != null && !lore.isEmpty()) {
         meta.setLore(this.getFormattedLore(lore));
      }

      if (enchantments != null && !enchantments.isEmpty()) {
         this.addEnchantmentsToMeta(material, enchantments, meta);
      }

      item.setItemMeta(meta);
      return item;
   }

   private void addEnchantmentsToMeta(Material material, List<String> enchantments, ItemMeta meta) {
      Iterator var5 = enchantments.iterator();

      while(var5.hasNext()) {
         String enchs = (String)var5.next();
         String ench = this.getEnchantmentName(enchs);
         int level = this.getEnchantmentLevel(enchs);
         if (this.getEnchantmentFromString(ench) == null) {
            this.plugin.getLogger().info("Enchantment is invalid: " + ench);
         } else {
            meta.addEnchant(this.getEnchantmentFromString(ench), level, true);
         }
      }

   }

   private ItemStack getPotionItem(Material material, int amount, String displayname, List<String> lore, List<String> enchantments) {
      ItemStack item = new ItemStack(material, amount);
      PotionMeta potionmeta = (PotionMeta)item.getItemMeta();
      potionmeta.setDisplayName(displayname);
      if (lore != null && !lore.isEmpty()) {
         potionmeta.setLore(this.getFormattedLore(lore));
      }

      if (enchantments != null && !enchantments.isEmpty()) {
         Iterator var9 = enchantments.iterator();

         while(var9.hasNext()) {
            String peffects = (String)var9.next();
            PotionEffect effect = this.createPotionEffect(peffects);
            if (effect != null) {
               potionmeta.addCustomEffect(effect, true);
               NamespacedKey key = NamespacedKey.minecraft(this.getEnchantmentName(peffects).toLowerCase());
               if (Registry.EFFECT.get(key) != null) {
                  potionmeta.setColor(((PotionEffectType)Registry.EFFECT.get(key)).getColor());
               }
            }
         }
      }

      item.setItemMeta(potionmeta);
      return item;
   }

   private PotionEffect createPotionEffect(String effect) {
      String name = this.getEnchantmentName(effect);
      int duration = this.getEnchantmentDuration(effect);
      int amplifier = this.getEnchantmentAmplifier(effect);
      NamespacedKey key = NamespacedKey.minecraft(name.toLowerCase());
      PotionEffectType type = (PotionEffectType)Registry.EFFECT.get(key);
      if (type == null) {
         this.plugin.getLogger().info("Potion effect type is invalid: " + name);
         return null;
      } else {
         return new PotionEffect(type, duration * 20, amplifier);
      }
   }

   private boolean canBuyDoubleJumps(FileConfiguration cfg, Player p, int kit) {
      Arena arena = this.plugin.amanager.getPlayerArena(p.getName());
      int maxjumps = Utils.getAllowedDoubleJumps(p, this.plugin.getConfig().getInt("shop.doublejump.maxdoublejumps", 10));
      int quantity = cfg.getInt(kit + ".items." + kit + ".amount", 1);
      if (this.plugin.getConfig().getBoolean("freedoublejumps.enabled")) {
         return maxjumps >= this.plugin.getPData().getDoubleJumpsFromFile((OfflinePlayer)p) + quantity;
      } else {
         return maxjumps >= arena.getPlayerHandler().getDoubleJumps(p.getName()) + quantity;
      }
   }

   public void setItems(Inventory inventory, Player player) {
      int slot = 0;

      for(Iterator var5 = this.cfg.getConfigurationSection("").getKeys(false).iterator(); var5.hasNext(); ++slot) {
         String kitCounter = (String)var5.next();
         String title = FormattingCodesParser.parseFormattingCodes(this.cfg.getString(kitCounter + ".name"));
         List<String> lore = this.cfg.getStringList(kitCounter + ".lore");
         List<String> enchantments = this.cfg.getStringList(kitCounter + ".items.1.enchantments");
         String firstEnchantment = enchantments != null && !enchantments.isEmpty() ? (String)enchantments.get(0) : "";
         boolean isGlowing = this.cfg.getBoolean(kitCounter + ".glow");
         Material material = Material.getMaterial(this.cfg.getString(kitCounter + ".material"));
         int amount = this.cfg.getInt(kitCounter + ".amount");
         if (!material.toString().equalsIgnoreCase("POTION") && !material.toString().equalsIgnoreCase("SPLASH_POTION")) {
            inventory.setItem(slot, this.getShopItem(material, title, lore, amount, isGlowing));
         } else {
            inventory.setItem(slot, this.getShopPotionItem(material, title, lore, amount, firstEnchantment));
         }

         this.itemSlot.put(slot, Integer.valueOf(kitCounter));
      }

      inventory.setItem(this.getInvsize() - 1, this.setMoneyItem(inventory, player));
   }

   private ItemStack setMoneyItem(Inventory inv, Player player) {
      Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
      Material material = Material.getMaterial(this.plugin.getConfig().getString("shop.showmoneyitem", "GOLD_INGOT"));
      String title = FormattingCodesParser.parseFormattingCodes(Messages.shopmoneyheader);
      String balance = String.valueOf(arena.getArenaEconomy().getPlayerBalance(player));
      List<String> lore = new ArrayList();
      lore.add(FormattingCodesParser.parseFormattingCodes(Messages.shopmoneybalance).replace("{BAL}", Utils.getFormattedCurrency(balance)));
      return this.getShopItem(material, title, lore, 1, false);
   }

   private ItemStack getShopItem(Material material, String title, List<String> lore, int amount, boolean isGlowing) {
      ItemStack item = new ItemStack(material, amount);
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(title);
         if (lore != null && !lore.isEmpty()) {
            meta.setLore(this.getFormattedLore(lore));
         }

         if (isGlowing) {
            meta.addEnchant(Enchantment.UNBREAKING, 2, true);
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
         }

         item.setItemMeta(meta);
      }

      return item;
   }

   private ItemStack getShopPotionItem(Material material, String title, List<String> lore, int amount, String enchantment) {
      ItemStack item = new ItemStack(material, amount);
      PotionMeta potionmeta = (PotionMeta)item.getItemMeta();
      potionmeta.setDisplayName(title);
      PotionEffect effect = this.createPotionEffect(enchantment);
      if (effect != null) {
         potionmeta.addCustomEffect(effect, true);
         potionmeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ADDITIONAL_TOOLTIP});
         NamespacedKey key = NamespacedKey.minecraft(this.getEnchantmentName(enchantment).toLowerCase());
         if (Registry.EFFECT.get(key) != null) {
            potionmeta.setColor(((PotionEffectType)Registry.EFFECT.get(key)).getColor());
         }
      }

      if (lore != null && !lore.isEmpty()) {
         potionmeta.setLore(this.getFormattedLore(lore));
      }

      item.setItemMeta(potionmeta);
      return item;
   }

   private Enchantment getEnchantmentFromString(String enchantment) {
      return (Enchantment)Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchantment.toLowerCase()));
   }

   public List<PotionEffect> getPotionEffects(Player player) {
      return (List)this.potionMap.get(player.getName());
   }

   public void removePotionEffects(Player player) {
      this.potionMap.remove(player.getName());
   }

   public String getInvname() {
      return this.invname;
   }

   public int getInvsize() {
      return this.invsize;
   }

   public Map<String, List<ItemStack>> getPlayersItems() {
      return this.pitems;
   }

   public List<String> getBuyers() {
      return this.buyers;
   }

   public Map<String, List<String>> getPurchasedCommands() {
      return this.commandMap;
   }

   private int getValidSize() {
      int size = Math.max(this.plugin.getConfig().getInt("shop.size"), this.getShopFileEntries());
      return size >= 9 && size <= 54 ? (int)(Math.ceil((double)size / 9.0D) * 9.0D) : Math.min(Math.max(size, 9), 54);
   }

   private int getShopFileEntries() {
      return this.cfg.getConfigurationSection("").getKeys(false).size();
   }

   public ShopFiles getShopFiles() {
      return this.shopFiles;
   }

   public Map<Integer, Integer> getItemSlot() {
      return this.itemSlot;
   }

   public boolean validatePurchase(Player p, int kit, String title) {
      this.doublejumpPurchase = Material.getMaterial(this.cfg.getString(kit + ".material").toUpperCase()) == Material.FEATHER;
      if (!this.doublejumpPurchase && this.buyers.contains(p.getName())) {
         Messages.sendMessage(p, Messages.alreadyboughtitem);
         this.plugin.getSound().ITEM_SELECT(p);
         p.closeInventory();
         return false;
      } else {
         Arena arena = this.plugin.amanager.getPlayerArena(p.getName());
         if (this.doublejumpPurchase && !this.canBuyDoubleJumps(this.cfg, p, kit)) {
            String var10001 = Messages.maxdoublejumpsexceeded;
            int var10003 = Utils.getAllowedDoubleJumps(p, this.plugin.getConfig().getInt("shop.doublejump.maxdoublejumps", 10));
            Messages.sendMessage(p, var10001.replace("{MAXJUMPS}", var10003.makeConcatWithConstants<invokedynamic>(var10003)));
            this.plugin.getSound().ITEM_SELECT(p);
            p.closeInventory();
            return false;
         } else {
            int cost = this.cfg.getInt(kit + ".cost");
            if (!arena.getArenaEconomy().hasMoney((double)cost, p)) {
               Messages.sendMessage(p, Messages.notenoughmoney.replace("{MONEY}", Utils.getFormattedCurrency(String.valueOf(cost))));
               this.plugin.getSound().ITEM_SELECT(p);
               return false;
            } else {
               Messages.sendMessage(p, Messages.playerboughtitem.replace("{ITEM}", title).replace("{MONEY}", Utils.getFormattedCurrency(String.valueOf(cost))));
               this.logPurchase(p, title, cost);
               if (!this.doublejumpPurchase) {
                  Messages.sendMessage(p, Messages.playerboughtwait);
               }

               this.plugin.getSound().NOTE_PLING(p, 5.0F, 10.0F);
               return true;
            }
         }
      }
   }

   private boolean isCommandPurchase(int kit) {
      return this.cfg.getString(kit + ".items.1.material").equalsIgnoreCase("command");
   }

   private List<String> getFormattedLore(List<String> lore) {
      List<String> formattedLore = new ArrayList();
      Iterator var4 = lore.iterator();

      while(var4.hasNext()) {
         String loreline = (String)var4.next();
         formattedLore.add(FormattingCodesParser.parseFormattingCodes(loreline));
      }

      return formattedLore;
   }

   private String getEnchantmentName(String enchant) {
      String[] array = enchant.split("#");
      return array[0].toUpperCase();
   }

   private int getEnchantmentLevel(String enchant) {
      String[] array = enchant.split("#");
      return array.length > 1 && Utils.isNumber(array[1]) ? Integer.valueOf(array[1]) : 1;
   }

   private int getEnchantmentDuration(String enchant) {
      String[] array = enchant.split("#");
      return array.length > 1 && Utils.isNumber(array[1]) ? Integer.valueOf(array[1]) : 30;
   }

   private int getEnchantmentAmplifier(String enchant) {
      String[] array = enchant.split("#");
      return array.length > 2 && Utils.isNumber(array[2]) ? Integer.valueOf(array[2]) : 1;
   }

   public Inventory getInv(String playerName) {
      return (Inventory)this.invMap.get(playerName);
   }
}
