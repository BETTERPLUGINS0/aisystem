package com.badbones69.crazyauctions.controllers;

import com.badbones69.crazyauctions.CrazyAuctions;
import com.badbones69.crazyauctions.Methods;
import com.badbones69.crazyauctions.api.CrazyManager;
import com.badbones69.crazyauctions.api.builders.ItemBuilder;
import com.badbones69.crazyauctions.api.enums.Category;
import com.badbones69.crazyauctions.api.enums.Files;
import com.badbones69.crazyauctions.api.enums.Messages;
import com.badbones69.crazyauctions.api.enums.Reasons;
import com.badbones69.crazyauctions.api.enums.ShopType;
import com.badbones69.crazyauctions.api.events.AuctionBuyEvent;
import com.badbones69.crazyauctions.api.events.AuctionCancelledEvent;
import com.badbones69.crazyauctions.api.events.AuctionNewBidEvent;
import com.badbones69.crazyauctions.currency.VaultSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import libs.com.ryderbelserion.vital.paper.util.scheduler.FoliaRunnable;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GuiListener implements Listener {
   private static final CrazyAuctions plugin = CrazyAuctions.get();
   private static final CrazyManager crazyManager;
   private static final Map<UUID, Integer> bidding;
   private static final Map<UUID, String> biddingID;
   private static final Map<UUID, ShopType> shopType;
   private static final Map<UUID, Category> shopCategory;
   private static final Map<UUID, List<Integer>> List;
   private static final Map<UUID, String> IDs;

   public static void openShop(Player player, ShopType sell, Category cat, int page) {
      Methods.updateAuction();
      FileConfiguration config = Files.config.getConfiguration();
      FileConfiguration data = Files.data.getConfiguration();
      List<ItemStack> items = new ArrayList();
      List<Integer> ID = new ArrayList();
      if (!data.contains("Items")) {
         data.set("Items.Clear", (Object)null);
         Files.data.save();
      }

      if (cat != null) {
         shopCategory.put(player.getUniqueId(), cat);
      } else {
         shopCategory.put(player.getUniqueId(), Category.NONE);
      }

      ArrayList lore;
      String o;
      String id;
      String time;
      if (data.contains("Items")) {
         Iterator var8 = data.getConfigurationSection("Items").getKeys(false).iterator();

         label157:
         while(true) {
            String i;
            ItemBuilder itemBuilder;
            String format;
            Iterator var16;
            String l;
            do {
               while(true) {
                  do {
                     do {
                        do {
                           if (!var8.hasNext()) {
                              break label157;
                           }

                           i = (String)var8.next();
                           itemBuilder = ItemBuilder.convertItemStack(data.getString("Items." + i + ".Item"));
                           lore = new ArrayList(itemBuilder.getUpdatedLore());
                        } while(itemBuilder == null);
                     } while(!data.contains("Items." + i + ".Item"));
                  } while(!cat.getItems().contains(itemBuilder.getItemStack().getType()) && cat != Category.NONE);

                  if (data.getBoolean("Items." + i + ".Biddable")) {
                     break;
                  }

                  if (sell == ShopType.SELL) {
                     o = data.getString("Items." + i + ".SellerName");
                     id = Methods.getPrice(i, false);
                     time = Methods.convertToTime(data.getLong("Items." + i + ".Time-Till-Expire"));
                     format = String.format(Locale.ENGLISH, "%,d", Long.parseLong(id));
                     var16 = config.getStringList("Settings.GUISettings.SellingItemLore").iterator();

                     while(var16.hasNext()) {
                        l = (String)var16.next();
                        lore.add(l.replace("%Price%", format).replace("%price%", format).replace("%Seller%", o != null ? o : "N/A").replace("%seller%", o != null ? o : "N/A").replace("%Time%", time).replace("%time%", time));
                     }

                     itemBuilder.setLore(lore);
                     items.add(itemBuilder.build());
                     ID.add(data.getInt("Items." + i + ".StoreID"));
                  }
               }
            } while(sell != ShopType.BID);

            o = data.getString("Items." + i + ".SellerName");
            id = Methods.getPrice(i, false);
            time = Methods.convertToTime(data.getLong("Items." + i + ".Time-Till-Expire"));
            format = data.getString("Items." + i + ".TopBidderName");
            var16 = config.getStringList("Settings.GUISettings.Bidding").iterator();

            while(var16.hasNext()) {
               l = (String)var16.next();
               String line = l.replace("%TopBid%", id).replace("%topbid%", id);
               line = o != null ? line.replace("%Seller%", o).replace("%seller%", o) : line.replace("%Seller%", "N/A").replace("%seller%", "N/A");
               line = format != null ? line.replace("%TopBidder%", format).replace("%topbidder%", format) : line.replace("%TopBidder%", "N/A").replace("%topbidder%", "N/A");
               lore.add(line.replace("%Time%", time).replace("%time%", time));
            }

            itemBuilder.setLore(lore);
            items.add(itemBuilder.build());
            ID.add(data.getInt("Items." + i + ".StoreID"));
         }
      }

      for(int maxPage = Methods.getMaxPage(items); page > maxPage; --page) {
      }

      Server var10000 = plugin.getServer();
      String var10003 = config.getString("Settings.GUIName");
      Inventory inv = var10000.createInventory((InventoryHolder)null, 54, Methods.color(var10003 + " #" + page));
      List<String> options = new ArrayList();
      options.add("SellingItems");
      options.add("Cancelled/ExpiredItems");
      options.add("PreviousPage");
      options.add("Refesh");
      options.add("NextPage");
      options.add("Category1");
      options.add("Category2");
      if (sell == ShopType.SELL) {
         shopType.put(player.getUniqueId(), ShopType.SELL);
         if (crazyManager.isBiddingEnabled()) {
            options.add("Bidding/Selling.Selling");
         }

         options.add("WhatIsThis.SellingShop");
      }

      if (sell == ShopType.BID) {
         shopType.put(player.getUniqueId(), ShopType.BID);
         if (crazyManager.isSellingEnabled()) {
            options.add("Bidding/Selling.Bidding");
         }

         options.add("WhatIsThis.BiddingShop");
      }

      Iterator var24 = options.iterator();

      while(true) {
         while(true) {
            do {
               if (!var24.hasNext()) {
                  var24 = Methods.getPage(items, page).iterator();

                  while(var24.hasNext()) {
                     ItemStack item = (ItemStack)var24.next();
                     int slot = inv.firstEmpty();
                     inv.setItem(slot, item);
                  }

                  lore = new ArrayList(Methods.getPageInts(ID, page));
                  List.put(player.getUniqueId(), lore);
                  player.openInventory(inv);
                  return;
               }

               o = (String)var24.next();
            } while(config.contains("Settings.GUISettings.OtherSettings." + o + ".Toggle") && !config.getBoolean("Settings.GUISettings.OtherSettings." + o + ".Toggle"));

            id = config.getString("Settings.GUISettings.OtherSettings." + o + ".Item");
            time = config.getString("Settings.GUISettings.OtherSettings." + o + ".Name");
            int slot = config.getInt("Settings.GUISettings.OtherSettings." + o + ".Slot");
            Category var10001 = (Category)shopCategory.get(player.getUniqueId());
            String cName = Methods.color(config.getString("Settings.GUISettings.Category-Settings." + var10001.getName() + ".Name"));
            ItemBuilder itemBuilder = (new ItemBuilder()).setMaterial(id).setName(time).setAmount(1);
            List<String> lore = new ArrayList(itemBuilder.getUpdatedLore());
            if (config.contains("Settings.GUISettings.OtherSettings." + o + ".Lore")) {
               Iterator var19 = config.getStringList("Settings.GUISettings.OtherSettings." + o + ".Lore").iterator();

               while(var19.hasNext()) {
                  String l = (String)var19.next();
                  lore.add(l.replace("%Category%", cName).replace("%category%", cName));
               }

               inv.setItem(slot - 1, itemBuilder.setLore(lore).build());
            } else {
               inv.setItem(slot - 1, itemBuilder.setLore(lore).build());
            }
         }
      }
   }

   public static void openCategories(Player player, ShopType shop) {
      Methods.updateAuction();
      FileConfiguration config = Files.config.getConfiguration();
      Inventory inv = plugin.getServer().createInventory((InventoryHolder)null, 54, Methods.color(config.getString("Settings.Categories")));
      List<String> options = new ArrayList();
      options.add("OtherSettings.Back");
      options.add("OtherSettings.WhatIsThis.Categories");
      options.add("Category-Settings.Armor");
      options.add("Category-Settings.Weapons");
      options.add("Category-Settings.Tools");
      options.add("Category-Settings.Food");
      options.add("Category-Settings.Potions");
      options.add("Category-Settings.Blocks");
      options.add("Category-Settings.Other");
      options.add("Category-Settings.None");
      Iterator var5 = options.iterator();

      while(true) {
         String o;
         do {
            if (!var5.hasNext()) {
               shopType.put(player.getUniqueId(), shop);
               player.openInventory(inv);
               return;
            }

            o = (String)var5.next();
         } while(config.contains("Settings.GUISettings." + o + ".Toggle") && !config.getBoolean("Settings.GUISettings." + o + ".Toggle"));

         String id = config.getString("Settings.GUISettings." + o + ".Item");
         String name = config.getString("Settings.GUISettings." + o + ".Name");
         int slot = config.getInt("Settings.GUISettings." + o + ".Slot");
         ItemBuilder itemBuilder = (new ItemBuilder()).setMaterial(id).setName(name).setAmount(1);
         if (config.contains("Settings.GUISettings." + o + ".Lore")) {
            itemBuilder.setLore(config.getStringList("Settings.GUISettings." + o + ".Lore"));
         }

         inv.setItem(slot - 1, itemBuilder.build());
      }
   }

   public static void openPlayersCurrentList(Player player, int page) {
      Methods.updateAuction();
      FileConfiguration config = Files.config.getConfiguration();
      FileConfiguration data = Files.data.getConfiguration();
      List<ItemStack> items = new ArrayList();
      List<Integer> ID = new ArrayList();
      Inventory inv = plugin.getServer().createInventory((InventoryHolder)null, 54, Methods.color(config.getString("Settings.Players-Current-Items")));
      List<String> options = new ArrayList();
      options.add("Back");
      options.add("WhatIsThis.CurrentItems");
      Iterator var8 = options.iterator();

      while(true) {
         String i;
         String price;
         String time;
         do {
            if (!var8.hasNext()) {
               if (data.contains("Items")) {
                  var8 = data.getConfigurationSection("Items").getKeys(false).iterator();

                  label46:
                  while(true) {
                     do {
                        if (!var8.hasNext()) {
                           break label46;
                        }

                        i = (String)var8.next();
                     } while(!data.getString("Items." + i + ".Seller").equalsIgnoreCase(player.getUniqueId().toString()));

                     price = Methods.getPrice(i, false);
                     time = Methods.convertToTime(data.getLong("Items." + i + ".Time-Till-Expire"));
                     ItemBuilder itemBuilder = ItemBuilder.convertItemStack(data.getString("Items." + i + ".Item"));
                     List<String> lore = new ArrayList(itemBuilder.getUpdatedLore());
                     Iterator var14 = config.getStringList("Settings.GUISettings.CurrentLore").iterator();

                     while(var14.hasNext()) {
                        String l = (String)var14.next();
                        lore.add(l.replace("%Price%", price).replace("%price%", price).replace("%Time%", time).replace("%time%", time));
                     }

                     itemBuilder.setLore(lore);
                     items.add(itemBuilder.build());
                     ID.add(data.getInt("Items." + i + ".StoreID"));
                  }
               }

               var8 = Methods.getPage(items, page).iterator();

               while(var8.hasNext()) {
                  ItemStack item = (ItemStack)var8.next();
                  int slot = inv.firstEmpty();
                  inv.setItem(slot, item);
               }

               List<Integer> Id = new ArrayList(Methods.getPageInts(ID, page));
               List.put(player.getUniqueId(), Id);
               player.openInventory(inv);
               return;
            }

            i = (String)var8.next();
         } while(config.contains("Settings.GUISettings.OtherSettings." + i + ".Toggle") && !config.getBoolean("Settings.GUISettings.OtherSettings." + i + ".Toggle"));

         price = config.getString("Settings.GUISettings.OtherSettings." + i + ".Item");
         time = config.getString("Settings.GUISettings.OtherSettings." + i + ".Name");
         int slot = config.getInt("Settings.GUISettings.OtherSettings." + i + ".Slot");
         ItemBuilder itemBuilder = (new ItemBuilder()).setMaterial(price).setName(time).setAmount(1);
         if (config.contains("Settings.GUISettings.OtherSettings." + i + ".Lore")) {
            itemBuilder.setLore(config.getStringList("Settings.GUISettings.OtherSettings." + i + ".Lore"));
         }

         inv.setItem(slot - 1, itemBuilder.build());
      }
   }

   public static void openPlayersExpiredList(Player player, int page) {
      Methods.updateAuction();
      FileConfiguration config = Files.config.getConfiguration();
      FileConfiguration data = Files.data.getConfiguration();
      List<ItemStack> items = new ArrayList();
      List<Integer> ID = new ArrayList();
      if (data.contains("OutOfTime/Cancelled")) {
         Iterator var6 = data.getConfigurationSection("OutOfTime/Cancelled").getKeys(false).iterator();

         label70:
         while(true) {
            String i;
            do {
               do {
                  if (!var6.hasNext()) {
                     break label70;
                  }

                  i = (String)var6.next();
               } while(data.getString("OutOfTime/Cancelled." + i + ".Seller") == null);
            } while(!data.getString("OutOfTime/Cancelled." + i + ".Seller").equalsIgnoreCase(player.getUniqueId().toString()));

            String price = Methods.getPrice(i, true);
            String time = Methods.convertToTime(data.getLong("OutOfTime/Cancelled." + i + ".Full-Time"));
            ItemBuilder itemBuilder = ItemBuilder.convertItemStack(data.getString("OutOfTime/Cancelled." + i + ".Item"));
            List<String> lore = new ArrayList(itemBuilder.getUpdatedLore());
            Iterator var12 = config.getStringList("Settings.GUISettings.Cancelled/ExpiredLore").iterator();

            while(var12.hasNext()) {
               String l = (String)var12.next();
               lore.add(l.replace("%Price%", price).replace("%price%", price).replace("%Time%", time).replace("%time%", time));
            }

            itemBuilder.setLore(lore);
            items.add(itemBuilder.build());
            ID.add(data.getInt("OutOfTime/Cancelled." + i + ".StoreID"));
         }
      }

      for(int maxPage = Methods.getMaxPage(items); page > maxPage; --page) {
      }

      Server var10000 = plugin.getServer();
      String var10003 = config.getString("Settings.Cancelled/Expired-Items");
      Inventory inv = var10000.createInventory((InventoryHolder)null, 54, Methods.color(var10003 + " #" + page));
      List<String> options = new ArrayList();
      options.add("Back");
      options.add("PreviousPage");
      options.add("Return");
      options.add("NextPage");
      options.add("WhatIsThis.Cancelled/ExpiredItems");
      Iterator var18 = options.iterator();

      while(true) {
         String o;
         do {
            if (!var18.hasNext()) {
               var18 = Methods.getPage(items, page).iterator();

               while(var18.hasNext()) {
                  ItemStack item = (ItemStack)var18.next();
                  int slot = inv.firstEmpty();
                  inv.setItem(slot, item);
               }

               List<Integer> Id = new ArrayList(Methods.getPageInts(ID, page));
               List.put(player.getUniqueId(), Id);
               player.openInventory(inv);
               return;
            }

            o = (String)var18.next();
         } while(config.contains("Settings.GUISettings.OtherSettings." + o + ".Toggle") && !config.getBoolean("Settings.GUISettings.OtherSettings." + o + ".Toggle"));

         String id = config.getString("Settings.GUISettings.OtherSettings." + o + ".Item");
         String name = config.getString("Settings.GUISettings.OtherSettings." + o + ".Name");
         int slot = config.getInt("Settings.GUISettings.OtherSettings." + o + ".Slot");
         ItemBuilder itemBuilder = (new ItemBuilder()).setMaterial(id).setName(name).setAmount(1);
         if (config.contains("Settings.GUISettings.OtherSettings." + o + ".Lore")) {
            itemBuilder.setLore(config.getStringList("Settings.GUISettings.OtherSettings." + o + ".Lore"));
         }

         inv.setItem(slot - 1, itemBuilder.build());
      }
   }

   public static void openBuying(Player player, String ID) {
      Methods.updateAuction();
      FileConfiguration config = Files.config.getConfiguration();
      FileConfiguration data = Files.data.getConfiguration();
      if (!data.contains("Items." + ID)) {
         openShop(player, ShopType.SELL, (Category)shopCategory.get(player.getUniqueId()), 1);
         player.sendMessage(Messages.ITEM_DOESNT_EXIST.getMessage(player));
      } else {
         Inventory inv = plugin.getServer().createInventory((InventoryHolder)null, 9, Methods.color(config.getString("Settings.Buying-Item")));
         List<String> options = new ArrayList();
         options.add("Confirm");
         options.add("Cancel");
         Iterator var6 = options.iterator();

         String o;
         String id;
         while(var6.hasNext()) {
            o = (String)var6.next();
            String id = config.getString("Settings.GUISettings.OtherSettings." + o + ".Item");
            id = config.getString("Settings.GUISettings.OtherSettings." + o + ".Name");
            ItemBuilder itemBuilder = (new ItemBuilder()).setMaterial(id).setName(id).setAmount(1);
            if (config.contains("Settings.GUISettings.OtherSettings." + o + ".Lore")) {
               itemBuilder.setLore(config.getStringList("Settings.GUISettings.OtherSettings." + o + ".Lore")).build();
            }

            ItemStack item = itemBuilder.build();
            if (o.equals("Confirm")) {
               inv.setItem(0, item);
               inv.setItem(1, item);
               inv.setItem(2, item);
               inv.setItem(3, item);
            }

            if (o.equals("Cancel")) {
               inv.setItem(5, item);
               inv.setItem(6, item);
               inv.setItem(7, item);
               inv.setItem(8, item);
            }
         }

         String price = Methods.getPrice(ID, false);
         o = Methods.convertToTime(data.getLong("Items." + ID + ".Time-Till-Expire"));
         OfflinePlayer target = null;
         id = data.getString("Items." + ID + ".Seller");
         if (id != null) {
            target = Methods.getOfflinePlayer(id);
         }

         ItemBuilder itemBuilder = ItemBuilder.convertItemStack(data.getString("Items." + ID + ".Item"));
         List<String> lore = new ArrayList(itemBuilder.getUpdatedLore());
         Iterator var12 = config.getStringList("Settings.GUISettings.SellingItemLore").iterator();

         while(var12.hasNext()) {
            String l = (String)var12.next();
            lore.add(l.replace("%Price%", price).replace("%price%", price).replace("%Seller%", target != null ? target.getName() : "N/A").replace("%seller%", target != null ? target.getName() : "N/A").replace("%Time%", o).replace("%time%", o));
         }

         itemBuilder.setLore(lore);
         inv.setItem(4, itemBuilder.build());
         IDs.put(player.getUniqueId(), ID);
         player.openInventory(inv);
      }
   }

   public static void openBidding(Player player, String ID) {
      Methods.updateAuction();
      FileConfiguration config = Files.config.getConfiguration();
      FileConfiguration data = Files.data.getConfiguration();
      if (!data.contains("Items." + ID)) {
         openShop(player, ShopType.BID, (Category)shopCategory.get(player.getUniqueId()), 1);
         player.sendMessage(Messages.ITEM_DOESNT_EXIST.getMessage(player));
      } else {
         Inventory inv = plugin.getServer().createInventory((InventoryHolder)null, 27, Methods.color(config.getString("Settings.Bidding-On-Item")));
         if (!bidding.containsKey(player.getUniqueId())) {
            bidding.put(player.getUniqueId(), 0);
         }

         inv.setItem(9, (new ItemBuilder()).setMaterial(Material.LIME_STAINED_GLASS_PANE).setName("&a+1").setAmount(1).build());
         inv.setItem(10, (new ItemBuilder()).setMaterial(Material.LIME_STAINED_GLASS_PANE).setName("&a+10").setAmount(1).build());
         inv.setItem(11, (new ItemBuilder()).setMaterial(Material.LIME_STAINED_GLASS_PANE).setName("&a+100").setAmount(1).build());
         inv.setItem(12, (new ItemBuilder()).setMaterial(Material.LIME_STAINED_GLASS_PANE).setName("&a+1000").setAmount(1).build());
         inv.setItem(14, (new ItemBuilder()).setMaterial(Material.LIME_STAINED_GLASS_PANE).setName("&c-1000").setAmount(1).build());
         inv.setItem(15, (new ItemBuilder()).setMaterial(Material.LIME_STAINED_GLASS_PANE).setName("&c-100").setAmount(1).build());
         inv.setItem(16, (new ItemBuilder()).setMaterial(Material.LIME_STAINED_GLASS_PANE).setName("&c-10").setAmount(1).build());
         inv.setItem(17, (new ItemBuilder()).setMaterial(Material.LIME_STAINED_GLASS_PANE).setName("&c-1").setAmount(1).build());
         inv.setItem(13, getBiddingGlass(player, ID));
         inv.setItem(22, (new ItemBuilder()).setMaterial(config.getString("Settings.GUISettings.OtherSettings.Bid.Item")).setAmount(1).setName(config.getString("Settings.GUISettings.OtherSettings.Bid.Name")).setLore(config.getStringList("Settings.GUISettings.OtherSettings.Bid.Lore")).build());
         inv.setItem(4, getBiddingItem(ID));
         player.openInventory(inv);
      }
   }

   public static void openViewer(Player player, String other, int page) {
      Methods.updateAuction();
      FileConfiguration config = Files.config.getConfiguration();
      FileConfiguration data = Files.data.getConfiguration();
      List<ItemStack> items = new ArrayList();
      List<Integer> ID = new ArrayList();
      if (!Methods.isUUID(other)) {
         other = String.valueOf(plugin.getServer().getPlayerUniqueId(other));
      }

      if (!data.contains("Items")) {
         data.set("Items.Clear", (Object)null);
         Files.data.save();
      }

      String id;
      ItemBuilder itemBuilder;
      if (data.contains("Items")) {
         Iterator var7 = data.getConfigurationSection("Items").getKeys(false).iterator();

         label123:
         while(true) {
            String i;
            do {
               if (!var7.hasNext()) {
                  break label123;
               }

               i = (String)var7.next();
            } while(!data.getString("Items." + i + ".Seller").equalsIgnoreCase(other));

            String price = Methods.getPrice(i, false);
            String time = Methods.convertToTime(data.getLong("Items." + i + ".Time-Till-Expire"));
            OfflinePlayer target = null;
            id = data.getString("Items." + i + ".Seller");
            if (id != null) {
               target = Methods.getOfflinePlayer(id);
            }

            OfflinePlayer bidder = null;
            String bid = data.getString("Items." + i + ".TopBidder");
            if (bid != null && !bid.equals("None")) {
               bidder = Methods.getOfflinePlayer(bid);
            }

            itemBuilder = ItemBuilder.convertItemStack(data.getString("Items." + i + ".Item"));
            List<String> lore = new ArrayList(itemBuilder.getUpdatedLore());
            Iterator var17;
            String l;
            if (data.getBoolean("Items." + i + ".Biddable")) {
               var17 = config.getStringList("Settings.GUISettings.Bidding").iterator();

               while(var17.hasNext()) {
                  l = (String)var17.next();
                  lore.add(l.replace("%TopBid%", price).replace("%topbid%", price).replace("%Seller%", target != null ? target.getName() : "N/A").replace("%seller%", target != null ? target.getName() : "N/A").replace("%TopBidder%", bidder != null ? bidder.getName() : "N/A").replace("%topbidder%", bidder != null ? bidder.getName() : "N/A").replace("%Time%", time).replace("%time%", time));
               }
            } else {
               var17 = config.getStringList("Settings.GUISettings.SellingItemLore").iterator();

               while(var17.hasNext()) {
                  l = (String)var17.next();
                  lore.add(l.replace("%Price%", price).replace("%price%", price).replace("%Seller%", target != null ? target.getName() : "N/A").replace("%seller%", target != null ? target.getName() : "N/A").replace("%Time%", time).replace("%time%", time));
               }
            }

            itemBuilder.setLore(lore);
            items.add(itemBuilder.build());
            ID.add(data.getInt("Items." + i + ".StoreID"));
         }
      }

      for(int maxPage = Methods.getMaxPage(items); page > maxPage; --page) {
      }

      Server var10000 = plugin.getServer();
      String var10003 = config.getString("Settings.GUIName");
      Inventory inv = var10000.createInventory((InventoryHolder)null, 54, Methods.color(var10003 + " #" + page));
      List<String> options = new ArrayList();
      options.add("WhatIsThis.Viewing");
      Iterator var22 = options.iterator();

      while(true) {
         String o;
         do {
            if (!var22.hasNext()) {
               var22 = Methods.getPage(items, page).iterator();

               while(var22.hasNext()) {
                  ItemStack item = (ItemStack)var22.next();
                  int slot = inv.firstEmpty();
                  inv.setItem(slot, item);
               }

               List.put(player.getUniqueId(), new ArrayList(Methods.getPageInts(ID, page)));
               player.openInventory(inv);
               return;
            }

            o = (String)var22.next();
         } while(config.contains("Settings.GUISettings.OtherSettings." + o + ".Toggle") && !config.getBoolean("Settings.GUISettings.OtherSettings." + o + ".Toggle"));

         id = config.getString("Settings.GUISettings.OtherSettings." + o + ".Item");
         String name = config.getString("Settings.GUISettings.OtherSettings." + o + ".Name");
         int slot = config.getInt("Settings.GUISettings.OtherSettings." + o + ".Slot");
         itemBuilder = (new ItemBuilder()).setMaterial(id).setName(name).setAmount(1);
         if (config.contains("Settings.GUISettings.OtherSettings." + o + ".Lore")) {
            itemBuilder.setLore(config.getStringList("Settings.GUISettings.OtherSettings." + o + ".Lore"));
         }

         inv.setItem(slot - 1, itemBuilder.build());
      }
   }

   private static ItemStack getBiddingGlass(Player player, String ID) {
      FileConfiguration config = Files.config.getConfiguration();
      String id = config.getString("Settings.GUISettings.OtherSettings.Bidding.Item");
      String name = config.getString("Settings.GUISettings.OtherSettings.Bidding.Name");
      ItemBuilder itemBuilder = (new ItemBuilder()).setMaterial(id).setName(name).setAmount(1);
      int bid = (Integer)bidding.get(player.getUniqueId());
      String price = Methods.getPrice(ID, false);
      if (config.contains("Settings.GUISettings.OtherSettings.Bidding.Lore")) {
         List<String> lore = new ArrayList(itemBuilder.getUpdatedLore());
         Iterator var9 = config.getStringList("Settings.GUISettings.OtherSettings.Bidding.Lore").iterator();

         while(var9.hasNext()) {
            String l = (String)var9.next();
            lore.add(l.replace("%Bid%", String.valueOf(bid)).replace("%bid%", String.valueOf(bid)).replace("%TopBid%", price).replace("%topbid%", price));
         }

         itemBuilder.setLore(lore);
      }

      return itemBuilder.build();
   }

   private static ItemStack getBiddingItem(String ID) {
      FileConfiguration config = Files.config.getConfiguration();
      FileConfiguration data = Files.data.getConfiguration();
      ItemStack item = Methods.fromBase64(data.getString("Items." + ID + ".Item"));
      String price = Methods.getPrice(ID, false);
      String time = Methods.convertToTime(data.getLong("Items." + ID + ".Time-Till-Expire"));
      OfflinePlayer target = null;
      String id = data.getString("Items." + ID + ".Seller");
      if (id != null) {
         target = Methods.getOfflinePlayer(id);
      }

      OfflinePlayer bidder = null;
      String bid = data.getString("Items." + ID + ".TopBidder");
      if (bid != null && !bid.equals("None")) {
         bidder = Methods.getOfflinePlayer(bid);
      }

      ItemBuilder itemBuilder = ItemBuilder.convertItemStack(item);
      List<String> lore = new ArrayList(itemBuilder.getUpdatedLore());
      Iterator var12 = config.getStringList("Settings.GUISettings.Bidding").iterator();

      while(var12.hasNext()) {
         String l = (String)var12.next();
         lore.add(l.replace("%TopBid%", price).replace("%topbid%", price).replace("%Seller%", target != null ? target.getName() : "N/A").replace("%seller%", target != null ? target.getName() : "N/A").replace("%TopBidder%", bidder != null ? bidder.getName() : "N/A").replace("%topbidder%", bidder != null ? bidder.getName() : "N/A").replace("%Time%", time).replace("%time%", time));
      }

      itemBuilder.setLore(lore);
      return itemBuilder.build();
   }

   private static void playClick(Player player) {
      FileConfiguration config = Files.config.getConfiguration();
      if (config.getBoolean("Settings.Sounds.Toggle", false)) {
         String sound = config.getString("Settings.Sounds.Sound", "UI_BUTTON_CLICK");
         player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0F, 1.0F);
      }

   }

   private void playSoldSound(@NotNull Player player) {
      FileConfiguration config = Files.config.getConfiguration();
      String sound = config.getString("Settings.Sold-Item-Sound", "UI_BUTTON_CLICK");
      if (!sound.isEmpty()) {
         player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0F, 1.0F);
      }
   }

   @EventHandler
   public void onInvClose(InventoryCloseEvent e) {
      FileConfiguration config = Files.config.getConfiguration();
      Player player = (Player)e.getPlayer();
      if (e.getView().getTitle().contains(Methods.color(config.getString("Settings.Bidding-On-Item")))) {
         bidding.remove(player);
      }

   }

   @EventHandler
   public void onInvClick(InventoryClickEvent e) {
      FileConfiguration config = Files.config.getConfiguration();
      FileConfiguration data = Files.data.getConfiguration();
      Player player = (Player)e.getWhoClicked();
      final Inventory inv = e.getClickedInventory();
      if (inv != null) {
         final int slot;
         final ItemStack item;
         int bid;
         if (e.getView().getTitle().contains(Methods.color(config.getString("Settings.Categories")))) {
            e.setCancelled(true);
            slot = e.getRawSlot();
            if (slot <= inv.getSize() && e.getCurrentItem() != null) {
               item = e.getCurrentItem();
               if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                  Category[] var8 = Category.values();
                  bid = var8.length;

                  for(int var10 = 0; var10 < bid; ++var10) {
                     Category cat = var8[var10];
                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.Category-Settings." + cat.getName() + ".Name")))) {
                        openShop(player, (ShopType)shopType.get(player.getUniqueId()), cat, 1);
                        playClick(player);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Back.Name")))) {
                        openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), 1);
                        playClick(player);
                        return;
                     }
                  }
               }
            }
         }

         long var10002;
         Iterator var21;
         String i;
         String ID;
         if (e.getView().getTitle().contains(Methods.color(config.getString("Settings.Bidding-On-Item")))) {
            e.setCancelled(true);
            slot = e.getRawSlot();
            if (slot <= inv.getSize() && e.getCurrentItem() != null) {
               item = e.getCurrentItem();
               if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                  if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Bid.Name")))) {
                     ID = (String)biddingID.get(player.getUniqueId());
                     bid = (Integer)bidding.get(player.getUniqueId());
                     i = data.getString("Items." + ID + ".TopBidder");
                     HashMap placeholders;
                     if (plugin.getSupport().getMoney(player) < (long)bid) {
                        placeholders = new HashMap();
                        var10002 = (long)bid - plugin.getSupport().getMoney(player);
                        placeholders.put("%Money_Needed%", var10002.makeConcatWithConstants<invokedynamic>(var10002));
                        var10002 = (long)bid - plugin.getSupport().getMoney(player);
                        placeholders.put("%money_needed%", var10002.makeConcatWithConstants<invokedynamic>(var10002));
                        player.sendMessage(Messages.NEED_MORE_MONEY.getMessage(player, placeholders));
                        return;
                     }

                     if (data.getLong("Items." + ID + ".Price") > (long)bid) {
                        player.sendMessage(Messages.BID_MORE_MONEY.getMessage(player));
                        return;
                     }

                     if (data.getLong("Items." + ID + ".Price") >= (long)bid && !i.equalsIgnoreCase("None")) {
                        player.sendMessage(Messages.BID_MORE_MONEY.getMessage(player));
                        return;
                     }

                     plugin.getServer().getPluginManager().callEvent(new AuctionNewBidEvent(player, Methods.fromBase64(data.getString("Items." + ID + ".Item")), (long)bid));
                     data.set("Items." + ID + ".Price", bid);
                     data.set("Items." + ID + ".TopBidder", player.getUniqueId().toString());
                     data.set("Items." + ID + ".TopBidderName", player.getName());
                     placeholders = new HashMap();
                     placeholders.put("%Bid%", bid.makeConcatWithConstants<invokedynamic>(bid));
                     player.sendMessage(Messages.BID_MESSAGE.getMessage(player, placeholders));
                     Files.data.save();
                     bidding.put(player.getUniqueId(), 0);
                     player.closeInventory();
                     playClick(player);
                     return;
                  }

                  Map<String, Integer> priceEdits = new HashMap();
                  priceEdits.put("&a+1", 1);
                  priceEdits.put("&a+10", 10);
                  priceEdits.put("&a+100", 100);
                  priceEdits.put("&a+1000", 1000);
                  priceEdits.put("&c-1", -1);
                  priceEdits.put("&c-10", -10);
                  priceEdits.put("&c-100", -100);
                  priceEdits.put("&c-1000", -1000);
                  var21 = priceEdits.keySet().iterator();

                  while(var21.hasNext()) {
                     i = (String)var21.next();
                     if (item.getItemMeta().getDisplayName().equals(Methods.color(i))) {
                        try {
                           bidding.put(player.getUniqueId(), (Integer)bidding.get(player.getUniqueId()) + (Integer)priceEdits.get(i));
                           inv.setItem(4, getBiddingItem((String)biddingID.get(player.getUniqueId())));
                           inv.setItem(13, getBiddingGlass(player, (String)biddingID.get(player.getUniqueId())));
                           playClick(player);
                           return;
                        } catch (Exception var18) {
                           player.closeInventory();
                           player.sendMessage(Messages.ITEM_DOESNT_EXIST.getMessage(player));
                           return;
                        }
                     }
                  }
               }
            }
         }

         int ID;
         String itemName;
         int page;
         boolean T;
         Iterator var25;
         String i;
         if (e.getView().getTitle().contains(Methods.color(config.getString("Settings.GUIName")))) {
            e.setCancelled(true);
            slot = e.getRawSlot();
            if (slot <= inv.getSize() && e.getCurrentItem() != null) {
               item = e.getCurrentItem();
               if (item.hasItemMeta()) {
                  if (item.getItemMeta().hasDisplayName()) {
                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.NextPage.Name")))) {
                        Methods.updateAuction();
                        page = Integer.parseInt(e.getView().getTitle().split("#")[1]);
                        openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), page + 1);
                        playClick(player);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.PreviousPage.Name")))) {
                        Methods.updateAuction();
                        page = Integer.parseInt(e.getView().getTitle().split("#")[1]);
                        if (page == 1) {
                           ++page;
                        }

                        openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), page - 1);
                        playClick(player);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Refesh.Name")))) {
                        Methods.updateAuction();
                        page = Integer.parseInt(e.getView().getTitle().split("#")[1]);
                        openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), page);
                        playClick(player);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Bidding/Selling.Selling.Name")))) {
                        openShop(player, ShopType.BID, (Category)shopCategory.get(player.getUniqueId()), 1);
                        playClick(player);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Bidding/Selling.Bidding.Name")))) {
                        openShop(player, ShopType.SELL, (Category)shopCategory.get(player.getUniqueId()), 1);
                        playClick(player);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Cancelled/ExpiredItems.Name")))) {
                        openPlayersExpiredList(player, 1);
                        playClick(player);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.SellingItems.Name")))) {
                        openPlayersCurrentList(player, 1);
                        playClick(player);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Category1.Name")))) {
                        openCategories(player, (ShopType)shopType.get(player.getUniqueId()));
                        playClick(player);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Category2.Name")))) {
                        openCategories(player, (ShopType)shopType.get(player.getUniqueId()));
                        playClick(player);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Your-Item.Name")))) {
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Cant-Afford.Name")))) {
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Top-Bidder.Name")))) {
                        return;
                     }
                  }

                  if (List.containsKey(player.getUniqueId()) && ((List)List.get(player.getUniqueId())).size() >= slot) {
                     page = (Integer)((List)List.get(player.getUniqueId())).get(slot);
                     T = false;
                     if (data.contains("Items")) {
                        var25 = data.getConfigurationSection("Items").getKeys(false).iterator();

                        while(var25.hasNext()) {
                           i = (String)var25.next();
                           ID = data.getInt("Items." + i + ".StoreID");
                           if (page == ID) {
                              String seller;
                              if ((player.hasPermission("crazyauctions.admin") || player.hasPermission("crazyauctions.force-end")) && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                                 int num;
                                 for(num = 1; data.contains("OutOfTime/Cancelled." + num); ++num) {
                                 }

                                 seller = data.getString("Items." + i + ".Seller");
                                 Player sellerPlayer = Methods.getPlayer(seller);
                                 if (Methods.isOnline(seller) && sellerPlayer != null) {
                                    sellerPlayer.sendMessage(Messages.ADMIN_FORCE_CANCELLED_TO_PLAYER.getMessage(player));
                                 }

                                 AuctionCancelledEvent event = new AuctionCancelledEvent((OfflinePlayer)(sellerPlayer != null ? sellerPlayer : Methods.getOfflinePlayer(seller)), Methods.fromBase64(data.getString("Items." + i + ".Item")), Reasons.ADMIN_FORCE_CANCEL);
                                 plugin.getServer().getPluginManager().callEvent(event);
                                 data.set("OutOfTime/Cancelled." + num + ".Seller", data.getString("Items." + i + ".Seller"));
                                 data.set("OutOfTime/Cancelled." + num + ".Full-Time", data.getLong("Items." + i + ".Full-Time"));
                                 data.set("OutOfTime/Cancelled." + num + ".StoreID", data.getInt("Items." + i + ".StoreID"));
                                 data.set("OutOfTime/Cancelled." + num + ".Item", data.getString("Items." + i + ".Item"));
                                 data.set("Items." + i, (Object)null);
                                 Files.data.save();
                                 player.sendMessage(Messages.ADMIN_FORCE_CANCELLED.getMessage(player));
                                 playClick(player);
                                 int page = Integer.parseInt(e.getView().getTitle().split("#")[1]);
                                 openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), page);
                                 return;
                              }

                              if (data.getString("Items." + i + ".Seller").equalsIgnoreCase(player.getUniqueId().toString())) {
                                 String itemName = config.getString("Settings.GUISettings.OtherSettings.Your-Item.Item");
                                 seller = config.getString("Settings.GUISettings.OtherSettings.Your-Item.Name");
                                 ItemBuilder itemBuilder = (new ItemBuilder()).setMaterial(itemName).setName(seller).setAmount(1);
                                 if (config.contains("Settings.GUISettings.OtherSettings.Your-Item.Lore")) {
                                    itemBuilder.setLore(config.getStringList("Settings.GUISettings.OtherSettings.Your-Item.Lore"));
                                 }

                                 inv.setItem(slot, itemBuilder.build());
                                 playClick(player);
                                 (new FoliaRunnable(this, plugin.getServer().getGlobalRegionScheduler()) {
                                    public void run() {
                                       inv.setItem(slot, item);
                                    }
                                 }).runDelayed(plugin, 60L);
                                 return;
                              }

                              long cost = data.getLong("Items." + i + ".Price");
                              String name;
                              ItemBuilder itemBuilder;
                              if (plugin.getSupport().getMoney(player) < cost) {
                                 itemName = config.getString("Settings.GUISettings.OtherSettings.Cant-Afford.Item");
                                 name = config.getString("Settings.GUISettings.OtherSettings.Cant-Afford.Name");
                                 itemBuilder = (new ItemBuilder()).setMaterial(itemName).setName(name).setAmount(1);
                                 if (config.contains("Settings.GUISettings.OtherSettings.Cant-Afford.Lore")) {
                                    itemBuilder.setLore(config.getStringList("Settings.GUISettings.OtherSettings.Cant-Afford.Lore"));
                                 }

                                 inv.setItem(slot, itemBuilder.build());
                                 playClick(player);
                                 (new FoliaRunnable(this, plugin.getServer().getGlobalRegionScheduler()) {
                                    public void run() {
                                       inv.setItem(slot, item);
                                    }
                                 }).runDelayed(plugin, 60L);
                                 return;
                              }

                              if (data.getBoolean("Items." + i + ".Biddable")) {
                                 if (player.getUniqueId().toString().equalsIgnoreCase(data.getString("Items." + i + ".TopBidder"))) {
                                    itemName = config.getString("Settings.GUISettings.OtherSettings.Top-Bidder.Item");
                                    name = config.getString("Settings.GUISettings.OtherSettings.Top-Bidder.Name");
                                    itemBuilder = (new ItemBuilder()).setMaterial(itemName).setName(name).setAmount(1);
                                    if (config.contains("Settings.GUISettings.OtherSettings.Top-Bidder.Lore")) {
                                       itemBuilder.setLore(config.getStringList("Settings.GUISettings.OtherSettings.Top-Bidder.Lore"));
                                    }

                                    inv.setItem(slot, itemBuilder.build());
                                    playClick(player);
                                    (new FoliaRunnable(this, plugin.getServer().getGlobalRegionScheduler()) {
                                       public void run() {
                                          inv.setItem(slot, item);
                                       }
                                    }).runDelayed(plugin, 60L);
                                    return;
                                 }

                                 playClick(player);
                                 openBidding(player, i);
                                 biddingID.put(player.getUniqueId(), i);
                              } else {
                                 playClick(player);
                                 openBuying(player, i);
                              }

                              return;
                           }
                        }
                     }

                     if (!T) {
                        playClick(player);
                        openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), 1);
                        player.sendMessage(Messages.ITEM_DOESNT_EXIST.getMessage(player));
                        return;
                     }
                  }
               }
            }
         }

         if (e.getView().getTitle().contains(Methods.color(config.getString("Settings.Buying-Item")))) {
            e.setCancelled(true);
            slot = e.getRawSlot();
            if (slot <= inv.getSize() && e.getCurrentItem() != null) {
               item = e.getCurrentItem();
               if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                  if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Confirm.Name")))) {
                     ID = (String)IDs.get(player.getUniqueId());
                     long cost = data.getLong("Items." + ID + ".Price");
                     i = data.getString("Items." + ID + ".Seller");
                     if (!data.contains("Items." + ID)) {
                        playClick(player);
                        openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), 1);
                        player.sendMessage(Messages.ITEM_DOESNT_EXIST.getMessage(player));
                        return;
                     }

                     if (Methods.isInvFull(player)) {
                        playClick(player);
                        player.closeInventory();
                        player.sendMessage(Messages.INVENTORY_FULL.getMessage(player));
                        return;
                     }

                     VaultSupport support = plugin.getSupport();
                     Map<String, String> placeholders = new HashMap();
                     if (support.getMoney(player) < cost) {
                        playClick(player);
                        player.closeInventory();
                        var10002 = cost - plugin.getSupport().getMoney(player);
                        placeholders.put("%Money_Needed%", var10002.makeConcatWithConstants<invokedynamic>(var10002));
                        var10002 = cost - plugin.getSupport().getMoney(player);
                        placeholders.put("%money_needed%", var10002.makeConcatWithConstants<invokedynamic>(var10002));
                        player.sendMessage(Messages.NEED_MORE_MONEY.getMessage(player, placeholders));
                        return;
                     }

                     ItemStack i = Methods.fromBase64(data.getString("Items." + ID + ".Item"));
                     plugin.getServer().getPluginManager().callEvent(new AuctionBuyEvent(player, i, cost));
                     if (!support.removeMoney(player, cost)) {
                        playClick(player);
                        player.closeInventory();
                        var10002 = cost - support.getMoney(player);
                        placeholders.put("%Money_Needed%", var10002.makeConcatWithConstants<invokedynamic>(var10002));
                        var10002 = cost - support.getMoney(player);
                        placeholders.put("%money_needed%", var10002.makeConcatWithConstants<invokedynamic>(var10002));
                        player.sendMessage(Messages.NEED_MORE_MONEY.getMessage(player, placeholders));
                        return;
                     }

                     support.addMoney(Methods.getOfflinePlayer(i), cost);
                     itemName = Methods.getPrice(ID, false);
                     placeholders.put("%Price%", itemName);
                     placeholders.put("%price%", itemName);
                     placeholders.put("%Player%", player.getName());
                     placeholders.put("%player%", player.getName());
                     player.sendMessage(Messages.BOUGHT_ITEM.getMessage(player, placeholders));
                     Player auctioneer = Methods.getPlayer(i);
                     if (auctioneer != null) {
                        auctioneer.sendMessage(Messages.PLAYER_BOUGHT_ITEM.getMessage(player, placeholders));
                        this.playSoldSound(auctioneer);
                     }

                     player.getInventory().addItem(new ItemStack[]{i});
                     data.set("Items." + ID, (Object)null);
                     Files.data.save();
                     playClick(player);
                     openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), 1);
                     return;
                  }

                  if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Cancel.Name")))) {
                     openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), 1);
                     playClick(player);
                     return;
                  }
               }
            }
         }

         if (e.getView().getTitle().contains(Methods.color(config.getString("Settings.Players-Current-Items")))) {
            e.setCancelled(true);
            slot = e.getRawSlot();
            if (slot <= inv.getSize() && e.getCurrentItem() != null) {
               item = e.getCurrentItem();
               if (item.hasItemMeta()) {
                  if (item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Back.Name")))) {
                     openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), 1);
                     playClick(player);
                     return;
                  }

                  if (List.containsKey(player.getUniqueId()) && ((List)List.get(player.getUniqueId())).size() >= slot) {
                     page = (Integer)((List)List.get(player.getUniqueId())).get(slot);
                     T = false;
                     if (data.contains("Items")) {
                        var25 = data.getConfigurationSection("Items").getKeys(false).iterator();

                        while(var25.hasNext()) {
                           i = (String)var25.next();
                           ID = data.getInt("Items." + i + ".StoreID");
                           if (page == ID) {
                              player.sendMessage(Messages.CANCELLED_ITEM.getMessage(player));
                              AuctionCancelledEvent event = new AuctionCancelledEvent(player, Methods.fromBase64(data.getString("Items." + i + ".Item")), Reasons.PLAYER_FORCE_CANCEL);
                              plugin.getServer().getPluginManager().callEvent(event);

                              int num;
                              for(num = 1; data.contains("OutOfTime/Cancelled." + num); ++num) {
                              }

                              data.set("OutOfTime/Cancelled." + num + ".Seller", data.getString("Items." + i + ".Seller"));
                              data.set("OutOfTime/Cancelled." + num + ".Full-Time", data.getLong("Items." + i + ".Full-Time"));
                              data.set("OutOfTime/Cancelled." + num + ".StoreID", data.getInt("Items." + i + ".StoreID"));
                              data.set("OutOfTime/Cancelled." + num + ".Item", data.getString("Items." + i + ".Item"));
                              data.set("Items." + i, (Object)null);
                              Files.data.save();
                              playClick(player);
                              openPlayersCurrentList(player, 1);
                              return;
                           }
                        }
                     }

                     if (!T) {
                        playClick(player);
                        openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), 1);
                        player.sendMessage(Messages.ITEM_DOESNT_EXIST.getMessage(player));
                        return;
                     }
                  }
               }
            }
         }

         if (e.getView().getTitle().contains(Methods.color(config.getString("Settings.Cancelled/Expired-Items")))) {
            e.setCancelled(true);
            slot = e.getRawSlot();
            if (slot <= inv.getSize() && e.getCurrentItem() != null) {
               item = e.getCurrentItem();
               if (item.hasItemMeta()) {
                  if (item.getItemMeta().hasDisplayName()) {
                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Back.Name")))) {
                        Methods.updateAuction();
                        playClick(player);
                        openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), 1);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.PreviousPage.Name")))) {
                        Methods.updateAuction();
                        page = Integer.parseInt(e.getView().getTitle().split("#")[1]);
                        if (page == 1) {
                           ++page;
                        }

                        playClick(player);
                        openPlayersExpiredList(player, page - 1);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.Return.Name")))) {
                        Methods.updateAuction();
                        page = Integer.parseInt(e.getView().getTitle().split("#")[1]);
                        if (data.contains("OutOfTime/Cancelled")) {
                           var21 = data.getConfigurationSection("OutOfTime/Cancelled").getKeys(false).iterator();

                           while(var21.hasNext()) {
                              i = (String)var21.next();
                              if (data.getString("OutOfTime/Cancelled." + i + ".Seller").equalsIgnoreCase(player.getUniqueId().toString())) {
                                 if (Methods.isInvFull(player)) {
                                    player.sendMessage(Messages.INVENTORY_FULL.getMessage(player));
                                    break;
                                 }

                                 player.getInventory().addItem(new ItemStack[]{Methods.fromBase64(data.getString("OutOfTime/Cancelled." + i + ".Item"))});
                                 data.set("OutOfTime/Cancelled." + i, (Object)null);
                              }
                           }
                        }

                        player.sendMessage(Messages.GOT_ITEM_BACK.getMessage(player));
                        Files.data.save();
                        playClick(player);
                        openPlayersExpiredList(player, page);
                        return;
                     }

                     if (item.getItemMeta().getDisplayName().equals(Methods.color(config.getString("Settings.GUISettings.OtherSettings.NextPage.Name")))) {
                        Methods.updateAuction();
                        page = Integer.parseInt(e.getView().getTitle().split("#")[1]);
                        playClick(player);
                        openPlayersExpiredList(player, page + 1);
                        return;
                     }
                  }

                  if (List.containsKey(player.getUniqueId()) && ((List)List.get(player.getUniqueId())).size() >= slot) {
                     page = (Integer)((List)List.get(player.getUniqueId())).get(slot);
                     T = false;
                     if (data.contains("OutOfTime/Cancelled")) {
                        var25 = data.getConfigurationSection("OutOfTime/Cancelled").getKeys(false).iterator();

                        while(var25.hasNext()) {
                           i = (String)var25.next();
                           ID = data.getInt("OutOfTime/Cancelled." + i + ".StoreID");
                           if (page == ID) {
                              if (!Methods.isInvFull(player)) {
                                 player.sendMessage(Messages.GOT_ITEM_BACK.getMessage(player));
                                 player.getInventory().addItem(new ItemStack[]{Methods.fromBase64(data.getString("OutOfTime/Cancelled." + i + ".Item"))});
                                 data.set("OutOfTime/Cancelled." + i, (Object)null);
                                 Files.data.save();
                                 playClick(player);
                                 openPlayersExpiredList(player, 1);
                              } else {
                                 player.sendMessage(Messages.INVENTORY_FULL.getMessage(player));
                              }

                              return;
                           }
                        }
                     }

                     if (!T) {
                        playClick(player);
                        openShop(player, (ShopType)shopType.get(player.getUniqueId()), (Category)shopCategory.get(player.getUniqueId()), 1);
                        player.sendMessage(Messages.ITEM_DOESNT_EXIST.getMessage(player));
                     }
                  }
               }
            }
         }
      }

   }

   static {
      crazyManager = plugin.getCrazyManager();
      bidding = new HashMap();
      biddingID = new HashMap();
      shopType = new HashMap();
      shopCategory = new HashMap();
      List = new HashMap();
      IDs = new HashMap();
   }
}
