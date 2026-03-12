package me.gypopo.economyshopgui.providers.placeholders;

import java.util.ArrayList;
import java.util.Iterator;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.TransactionLog;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.objects.shops.ShopSection;
import me.gypopo.economyshopgui.util.PermissionsCache;
import me.gypopo.economyshopgui.util.SimplePair;
import me.gypopo.economyshopgui.util.Transaction;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PAPIExpansion extends PlaceholderExpansion {
   private final EconomyShopGUI plugin;
   private PAPIExpansion.PlaceholderCache cache;
   private boolean placeholderCache = false;

   public PAPIExpansion(EconomyShopGUI plugin) {
      this.plugin = plugin;
      this.register();
   }

   public String getAuthor() {
      return "Gypopo";
   }

   public String getName() {
      return "EconomyShopGUI";
   }

   public String getIdentifier() {
      return "esgui";
   }

   public String getVersion() {
      return "1.1.0";
   }

   public boolean persist() {
      return true;
   }

   public String onRequest(OfflinePlayer player, String params) {
      String section;
      if (params.startsWith("buyprice_")) {
         section = params.replace("buyprice_", "");
         return !section.contains(".") ? "§cInvalid item path for '" + section + "'" : this.getItemBuyPrice(section.split("\\.", 2));
      } else if (params.startsWith("sellprice_")) {
         section = params.replace("sellprice_", "");
         return !section.contains(".") ? "§cInvalid item path for '" + section + "'" : this.getItemSellPrice(section.split("\\.", 2));
      } else if (params.startsWith("discount_")) {
         if (!EconomyShopGUI.getInstance().discountsActive) {
            return "§cDiscounts not enabled";
         } else {
            section = params.replace("discount_", "");
            return player != null && player.isOnline() ? this.getDiscount(player.getPlayer(), section) : "§cInvalid player";
         }
      } else if (params.startsWith("sell-multiplier_")) {
         if (!EconomyShopGUI.getInstance().multipliers) {
            return "§cSell multipliers not enabled";
         } else {
            section = params.replace("sell-multiplier_", "");
            return player != null && player.isOnline() ? this.getMultiplier(player.getPlayer(), section) : "§cInvalid player";
         }
      } else {
         PAPIExpansion.PlaceholderCache cache = this.getPlaceholderCache();
         if (cache == null) {
            return "§cLoading cache...";
         } else {
            try {
               SimplePair<PAPIExpansion.PlaceholderType, PAPIExpansion.SimpleIndex> placeholder = this.getPlaceholder(params.toUpperCase().split("_", 2));
               if (placeholder == null) {
                  return "Unknown placeholder";
               } else {
                  switch(((PAPIExpansion.PlaceholderType)placeholder.key).ordinal()) {
                  case 0:
                     return ((TransactionLog.TopPlayer)cache.getTopPlayerBoughtItemsCurrentMonth().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).player.name;
                  case 1:
                     return String.valueOf(((TransactionLog.TopPlayer)cache.getTopPlayerBoughtItemsCurrentMonth().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).amount);
                  case 2:
                     return ((TransactionLog.TopPlayer)cache.getTopPlayerBoughtItemsCurrentWeek().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).player.name;
                  case 3:
                     return String.valueOf(((TransactionLog.TopPlayer)cache.getTopPlayerBoughtItemsCurrentWeek().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).amount);
                  case 4:
                     return ((TransactionLog.TopPlayer)cache.getTopPlayerBoughtItemsCurrentDay().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).player.name;
                  case 5:
                     return String.valueOf(((TransactionLog.TopPlayer)cache.getTopPlayerBoughtItemsCurrentDay().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).amount);
                  case 6:
                     return ((TransactionLog.TopPlayer)cache.getTopPlayerSoldItemsCurrentMonth().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).player.name;
                  case 7:
                     return String.valueOf(((TransactionLog.TopPlayer)cache.getTopPlayerSoldItemsCurrentMonth().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).amount);
                  case 8:
                     return ((TransactionLog.TopPlayer)cache.getTopPlayerSoldItemsCurrentWeek().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).player.name;
                  case 9:
                     return String.valueOf(((TransactionLog.TopPlayer)cache.getTopPlayerSoldItemsCurrentWeek().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).amount);
                  case 10:
                     return ((TransactionLog.TopPlayer)cache.getTopPlayerSoldItemsCurrentDay().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).player.name;
                  case 11:
                     return String.valueOf(((TransactionLog.TopPlayer)cache.getTopPlayerSoldItemsCurrentDay().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).amount);
                  case 12:
                     return ((TransactionLog.TopItem)cache.getTopBoughtItemsCurrentMonth().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).item.name;
                  case 13:
                     return String.valueOf(((TransactionLog.TopItem)cache.getTopBoughtItemsCurrentMonth().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).amount);
                  case 14:
                     return ((TransactionLog.TopItem)cache.getTopBoughtItemsCurrentWeek().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).item.name;
                  case 15:
                     return String.valueOf(((TransactionLog.TopItem)cache.getTopBoughtItemsCurrentWeek().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).amount);
                  case 16:
                     return ((TransactionLog.TopItem)cache.getTopBoughtItemsCurrentDay().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).item.name;
                  case 17:
                     return String.valueOf(((TransactionLog.TopItem)cache.getTopBoughtItemsCurrentDay().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).amount);
                  case 18:
                     return ((TransactionLog.TopItem)cache.getTopSoldItemsCurrentMonth().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).item.name;
                  case 19:
                     return String.valueOf(((TransactionLog.TopItem)cache.getTopSoldItemsCurrentMonth().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).amount);
                  case 20:
                     return ((TransactionLog.TopItem)cache.getTopSoldItemsCurrentWeek().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).item.name;
                  case 21:
                     return String.valueOf(((TransactionLog.TopItem)cache.getTopSoldItemsCurrentWeek().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).amount);
                  case 22:
                     return ((TransactionLog.TopItem)cache.getTopSoldItemsCurrentDay().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).item.name;
                  case 23:
                     return String.valueOf(((TransactionLog.TopItem)cache.getTopSoldItemsCurrentDay().get(((PAPIExpansion.SimpleIndex)placeholder.value).index)).amount);
                  default:
                     return "Unknown placeholder";
                  }
               }
            } catch (IndexOutOfBoundsException var5) {
               return "null";
            }
         }
      }
   }

   public String getItemBuyPrice(String[] parts) {
      ShopSection shop = this.plugin.getSection(parts[0]);
      if (shop == null) {
         return "§cNo shop found for '" + parts[0] + "'";
      } else {
         ShopItem item = shop.getShopItem(parts[0] + "." + parts[1]);
         if (item == null) {
            return "§cNo item found in shop '" + parts[0] + "' at index '" + parts[1] + "'";
         } else {
            return item.hasItemError() ? "§cCannot get price of invalid item" : EconomyShopGUI.getInstance().formatPrice(item.getEcoType(), item.getBuyPrice());
         }
      }
   }

   public String getItemSellPrice(String[] parts) {
      ShopSection shop = this.plugin.getSection(parts[0]);
      if (shop == null) {
         return "§cNo shop found for '" + parts[0] + "'";
      } else {
         ShopItem item = shop.getShopItem(parts[0] + "." + parts[1]);
         if (item == null) {
            return "§cNo item found in shop '" + parts[0] + "' at index '" + parts[1] + "'";
         } else {
            return item.hasItemError() ? "§cCannot get price of invalid item" : EconomyShopGUI.getInstance().formatPrice(item.getEcoType(), item.getSellPrice());
         }
      }
   }

   public String getDiscount(Player player, String section) {
      if (this.plugin.getSection(section) == null) {
         return "§cNo shop found for '" + section + "'";
      } else if (!this.plugin.hasDiscount(section)) {
         return "0";
      } else {
         double discount = 0.0D;
         Iterator var5 = this.plugin.getDiscounts(section).keySet().iterator();

         while(true) {
            double i;
            do {
               String group;
               do {
                  if (!var5.hasNext()) {
                     return String.valueOf(discount);
                  }

                  group = (String)var5.next();
               } while(!PermissionsCache.hasPermission(player, "EconomyShopGUI.discounts." + group));

               i = (Double)this.plugin.getDiscounts(section).get(group);
            } while(discount != 0.0D && !(discount < i));

            discount = i;
         }
      }
   }

   public String getMultiplier(Player player, String section) {
      if (this.plugin.getSection(section) == null) {
         return "§cNo shop found for '" + section + "'";
      } else if (!this.plugin.hasMultiplier(section)) {
         return "0";
      } else {
         double multiplier = 0.0D;
         Iterator var5 = this.plugin.getMultipliers(section).keySet().iterator();

         while(true) {
            double i;
            do {
               String group;
               do {
                  if (!var5.hasNext()) {
                     return String.valueOf(multiplier);
                  }

                  group = (String)var5.next();
               } while(!PermissionsCache.hasPermission(player, "EconomyShopGUI.sell-multipliers." + group));

               i = (Double)this.plugin.getMultipliers(section).get(group);
            } while(multiplier != 0.0D && !(multiplier < i));

            multiplier = i;
         }
      }
   }

   private void enablePlaceholderStore() {
      int delay = ConfigManager.getConfig().getInt("placeholders.transaction-cache") * 20;
      if (delay < 200) {
         delay = 200;
      }

      this.plugin.runTaskAsyncTimer(() -> {
         this.plugin.getTransactionLog().loadCache();
         if (this.cache == null) {
            this.cache = new PAPIExpansion.PlaceholderCache();
         } else {
            this.getPlaceholderCache().reloadActivePlaceholders();
         }

      }, 0L, (long)delay);
      this.placeholderCache = true;
   }

   public PAPIExpansion.PlaceholderCache getPlaceholderCache() {
      if (!this.placeholderCache) {
         SendMessage.infoMessage("Received placeholder request, enabling transaction store...");
         this.enablePlaceholderStore();
         return null;
      } else {
         return this.cache;
      }
   }

   private SimplePair<PAPIExpansion.PlaceholderType, PAPIExpansion.SimpleIndex> getPlaceholder(String... params) {
      try {
         PAPIExpansion.SimpleIndex index = PAPIExpansion.SimpleIndex.get(params[0]);
         PAPIExpansion.PlaceholderType type = PAPIExpansion.PlaceholderType.valueOf(index == null ? String.join("_", params) : params[1]);
         return new SimplePair(type, index == null ? PAPIExpansion.SimpleIndex.FIRST : index);
      } catch (IllegalArgumentException var4) {
         return null;
      }
   }

   public final class PlaceholderCache {
      private final PAPIExpansion.ActivePlaceholders activePlaceholders = PAPIExpansion.this.new ActivePlaceholders();
      private ArrayList<TransactionLog.TopPlayer> top_player_bought_items_current_month;
      private ArrayList<TransactionLog.TopPlayer> top_player_bought_items_current_week;
      private ArrayList<TransactionLog.TopPlayer> top_player_bought_items_current_day;
      private ArrayList<TransactionLog.TopPlayer> top_player_sold_items_current_month;
      private ArrayList<TransactionLog.TopPlayer> top_player_sold_items_current_week;
      private ArrayList<TransactionLog.TopPlayer> top_player_sold_items_current_day;
      private ArrayList<TransactionLog.TopItem> top_bought_items_current_month;
      private ArrayList<TransactionLog.TopItem> top_bought_items_current_week;
      private ArrayList<TransactionLog.TopItem> top_bought_items_current_day;
      private ArrayList<TransactionLog.TopItem> top_sold_items_current_month;
      private ArrayList<TransactionLog.TopItem> top_sold_items_current_week;
      private ArrayList<TransactionLog.TopItem> top_sold_items_current_day;

      public PlaceholderCache() {
         this.reloadActivePlaceholders();
      }

      public void reloadActivePlaceholders() {
         if (this.activePlaceholders.top_player_bought_items_current_month) {
            this.loadTopPlayerBoughtItemsCurrentMonth();
         }

         if (this.activePlaceholders.top_player_bought_items_current_week) {
            this.loadTopPlayerBoughtItemsCurrentWeek();
         }

         if (this.activePlaceholders.top_player_bought_items_current_day) {
            this.loadTopPlayerBoughtItemsCurrentDay();
         }

         if (this.activePlaceholders.top_player_sold_items_current_month) {
            this.loadTopPlayerSoldItemsCurrentMonth();
         }

         if (this.activePlaceholders.top_player_sold_items_current_week) {
            this.loadTopPlayerSoldItemsCurrentWeek();
         }

         if (this.activePlaceholders.top_player_sold_items_current_day) {
            this.loadTopPlayerSoldItemsCurrentDay();
         }

         if (this.activePlaceholders.top_bought_items_current_month) {
            this.loadTopBoughtItemsCurrentMonth();
         }

         if (this.activePlaceholders.top_bought_items_current_week) {
            this.loadTopBoughtItemsCurrentWeek();
         }

         if (this.activePlaceholders.top_bought_items_current_day) {
            this.loadTopBoughtItemsCurrentDay();
         }

         if (this.activePlaceholders.top_sold_items_current_month) {
            this.loadTopSoldItemsCurrentMonth();
         }

         if (this.activePlaceholders.top_sold_items_current_week) {
            this.loadTopSoldItemsCurrentWeek();
         }

         if (this.activePlaceholders.top_sold_items_current_day) {
            this.loadTopSoldItemsCurrentDay();
         }

      }

      public ArrayList<TransactionLog.TopPlayer> getTopPlayerBoughtItemsCurrentMonth() {
         if (!this.activePlaceholders.top_player_bought_items_current_month) {
            this.loadTopPlayerBoughtItemsCurrentMonth();
            this.activePlaceholders.top_player_bought_items_current_month = true;
         }

         return this.top_player_bought_items_current_month;
      }

      private void loadTopPlayerBoughtItemsCurrentMonth() {
         this.top_player_bought_items_current_month = PAPIExpansion.this.plugin.getTransactionLog().getTopThreePlayers(TransactionLog.TimeFrame.MONTH, Transaction.Mode.BUY);
      }

      public ArrayList<TransactionLog.TopPlayer> getTopPlayerBoughtItemsCurrentWeek() {
         if (!this.activePlaceholders.top_player_bought_items_current_week) {
            this.loadTopPlayerBoughtItemsCurrentWeek();
            this.activePlaceholders.top_player_bought_items_current_week = true;
         }

         return this.top_player_bought_items_current_week;
      }

      private void loadTopPlayerBoughtItemsCurrentWeek() {
         this.top_player_bought_items_current_week = PAPIExpansion.this.plugin.getTransactionLog().getTopThreePlayers(TransactionLog.TimeFrame.WEEK, Transaction.Mode.BUY);
      }

      public ArrayList<TransactionLog.TopPlayer> getTopPlayerBoughtItemsCurrentDay() {
         if (!this.activePlaceholders.top_player_bought_items_current_day) {
            this.loadTopPlayerBoughtItemsCurrentDay();
            this.activePlaceholders.top_player_bought_items_current_day = true;
         }

         return this.top_player_bought_items_current_day;
      }

      private void loadTopPlayerBoughtItemsCurrentDay() {
         this.top_player_bought_items_current_day = PAPIExpansion.this.plugin.getTransactionLog().getTopThreePlayers(TransactionLog.TimeFrame.DAY, Transaction.Mode.BUY);
      }

      public ArrayList<TransactionLog.TopPlayer> getTopPlayerSoldItemsCurrentMonth() {
         if (!this.activePlaceholders.top_player_sold_items_current_month) {
            this.loadTopPlayerSoldItemsCurrentMonth();
            this.activePlaceholders.top_player_sold_items_current_month = true;
         }

         return this.top_player_sold_items_current_month;
      }

      private void loadTopPlayerSoldItemsCurrentMonth() {
         this.top_player_sold_items_current_month = PAPIExpansion.this.plugin.getTransactionLog().getTopThreePlayers(TransactionLog.TimeFrame.MONTH, Transaction.Mode.SELL);
      }

      public ArrayList<TransactionLog.TopPlayer> getTopPlayerSoldItemsCurrentWeek() {
         if (!this.activePlaceholders.top_player_sold_items_current_week) {
            this.loadTopPlayerSoldItemsCurrentWeek();
            this.activePlaceholders.top_player_sold_items_current_week = true;
         }

         return this.top_player_sold_items_current_week;
      }

      private void loadTopPlayerSoldItemsCurrentWeek() {
         this.top_player_sold_items_current_week = PAPIExpansion.this.plugin.getTransactionLog().getTopThreePlayers(TransactionLog.TimeFrame.WEEK, Transaction.Mode.SELL);
      }

      public ArrayList<TransactionLog.TopPlayer> getTopPlayerSoldItemsCurrentDay() {
         if (!this.activePlaceholders.top_player_sold_items_current_day) {
            this.loadTopPlayerSoldItemsCurrentDay();
            this.activePlaceholders.top_player_sold_items_current_day = true;
         }

         return this.top_player_sold_items_current_day;
      }

      private void loadTopPlayerSoldItemsCurrentDay() {
         this.top_player_sold_items_current_day = PAPIExpansion.this.plugin.getTransactionLog().getTopThreePlayers(TransactionLog.TimeFrame.DAY, Transaction.Mode.SELL);
      }

      public ArrayList<TransactionLog.TopItem> getTopBoughtItemsCurrentMonth() {
         if (!this.activePlaceholders.top_bought_items_current_month) {
            this.loadTopBoughtItemsCurrentMonth();
            this.activePlaceholders.top_bought_items_current_month = true;
         }

         return this.top_bought_items_current_month;
      }

      private void loadTopBoughtItemsCurrentMonth() {
         this.top_bought_items_current_month = PAPIExpansion.this.plugin.getTransactionLog().getTopThreeItems(TransactionLog.TimeFrame.MONTH, Transaction.Mode.BUY);
      }

      public ArrayList<TransactionLog.TopItem> getTopBoughtItemsCurrentWeek() {
         if (!this.activePlaceholders.top_bought_items_current_week) {
            this.loadTopBoughtItemsCurrentWeek();
            this.activePlaceholders.top_bought_items_current_week = true;
         }

         return this.top_bought_items_current_week;
      }

      private void loadTopBoughtItemsCurrentWeek() {
         this.top_bought_items_current_week = PAPIExpansion.this.plugin.getTransactionLog().getTopThreeItems(TransactionLog.TimeFrame.WEEK, Transaction.Mode.BUY);
      }

      public ArrayList<TransactionLog.TopItem> getTopBoughtItemsCurrentDay() {
         if (!this.activePlaceholders.top_bought_items_current_day) {
            this.loadTopBoughtItemsCurrentDay();
            this.activePlaceholders.top_bought_items_current_day = true;
         }

         return this.top_bought_items_current_day;
      }

      private void loadTopBoughtItemsCurrentDay() {
         this.top_bought_items_current_day = PAPIExpansion.this.plugin.getTransactionLog().getTopThreeItems(TransactionLog.TimeFrame.DAY, Transaction.Mode.BUY);
      }

      public ArrayList<TransactionLog.TopItem> getTopSoldItemsCurrentMonth() {
         if (!this.activePlaceholders.top_sold_items_current_month) {
            this.loadTopSoldItemsCurrentMonth();
            this.activePlaceholders.top_sold_items_current_month = true;
         }

         return this.top_sold_items_current_month;
      }

      private void loadTopSoldItemsCurrentMonth() {
         this.top_sold_items_current_month = PAPIExpansion.this.plugin.getTransactionLog().getTopThreeItems(TransactionLog.TimeFrame.MONTH, Transaction.Mode.SELL);
      }

      public ArrayList<TransactionLog.TopItem> getTopSoldItemsCurrentWeek() {
         if (!this.activePlaceholders.top_sold_items_current_week) {
            this.loadTopSoldItemsCurrentWeek();
            this.activePlaceholders.top_sold_items_current_week = true;
         }

         return this.top_sold_items_current_week;
      }

      private void loadTopSoldItemsCurrentWeek() {
         this.top_sold_items_current_week = PAPIExpansion.this.plugin.getTransactionLog().getTopThreeItems(TransactionLog.TimeFrame.WEEK, Transaction.Mode.SELL);
      }

      public ArrayList<TransactionLog.TopItem> getTopSoldItemsCurrentDay() {
         if (!this.activePlaceholders.top_sold_items_current_day) {
            this.loadTopSoldItemsCurrentDay();
            this.activePlaceholders.top_sold_items_current_day = true;
         }

         return this.top_sold_items_current_day;
      }

      private void loadTopSoldItemsCurrentDay() {
         this.top_sold_items_current_day = PAPIExpansion.this.plugin.getTransactionLog().getTopThreeItems(TransactionLog.TimeFrame.DAY, Transaction.Mode.SELL);
      }
   }

   private static enum PlaceholderType {
      PLAYER_MOST_BOUGHT_ITEMS_CURRENT_MONTH,
      PLAYER_MOST_BOUGHT_ITEMS_CURRENT_MONTH_AMOUNT,
      PLAYER_MOST_BOUGHT_ITEMS_CURRENT_WEEK,
      PLAYER_MOST_BOUGHT_ITEMS_CURRENT_WEEK_AMOUNT,
      PLAYER_MOST_BOUGHT_ITEMS_CURRENT_DAY,
      PLAYER_MOST_BOUGHT_ITEMS_CURRENT_DAY_AMOUNT,
      PLAYER_MOST_SOLD_ITEMS_CURRENT_MONTH,
      PLAYER_MOST_SOLD_ITEMS_CURRENT_MONTH_AMOUNT,
      PLAYER_MOST_SOLD_ITEMS_CURRENT_WEEK,
      PLAYER_MOST_SOLD_ITEMS_CURRENT_WEEK_AMOUNT,
      PLAYER_MOST_SOLD_ITEMS_CURRENT_DAY,
      PLAYER_MOST_SOLD_ITEMS_CURRENT_DAY_AMOUNT,
      MOST_BOUGHT_ITEM_CURRENT_MONTH,
      MOST_BOUGHT_ITEM_CURRENT_MONTH_AMOUNT,
      MOST_BOUGHT_ITEM_CURRENT_WEEK,
      MOST_BOUGHT_ITEM_CURRENT_WEEK_AMOUNT,
      MOST_BOUGHT_ITEM_CURRENT_DAY,
      MOST_BOUGHT_ITEM_CURRENT_DAY_AMOUNT,
      MOST_SOLD_ITEM_CURRENT_MONTH,
      MOST_SOLD_ITEM_CURRENT_MONTH_AMOUNT,
      MOST_SOLD_ITEM_CURRENT_WEEK,
      MOST_SOLD_ITEM_CURRENT_WEEK_AMOUNT,
      MOST_SOLD_ITEM_CURRENT_DAY,
      MOST_SOLD_ITEM_CURRENT_DAY_AMOUNT;

      // $FF: synthetic method
      private static PAPIExpansion.PlaceholderType[] $values() {
         return new PAPIExpansion.PlaceholderType[]{PLAYER_MOST_BOUGHT_ITEMS_CURRENT_MONTH, PLAYER_MOST_BOUGHT_ITEMS_CURRENT_MONTH_AMOUNT, PLAYER_MOST_BOUGHT_ITEMS_CURRENT_WEEK, PLAYER_MOST_BOUGHT_ITEMS_CURRENT_WEEK_AMOUNT, PLAYER_MOST_BOUGHT_ITEMS_CURRENT_DAY, PLAYER_MOST_BOUGHT_ITEMS_CURRENT_DAY_AMOUNT, PLAYER_MOST_SOLD_ITEMS_CURRENT_MONTH, PLAYER_MOST_SOLD_ITEMS_CURRENT_MONTH_AMOUNT, PLAYER_MOST_SOLD_ITEMS_CURRENT_WEEK, PLAYER_MOST_SOLD_ITEMS_CURRENT_WEEK_AMOUNT, PLAYER_MOST_SOLD_ITEMS_CURRENT_DAY, PLAYER_MOST_SOLD_ITEMS_CURRENT_DAY_AMOUNT, MOST_BOUGHT_ITEM_CURRENT_MONTH, MOST_BOUGHT_ITEM_CURRENT_MONTH_AMOUNT, MOST_BOUGHT_ITEM_CURRENT_WEEK, MOST_BOUGHT_ITEM_CURRENT_WEEK_AMOUNT, MOST_BOUGHT_ITEM_CURRENT_DAY, MOST_BOUGHT_ITEM_CURRENT_DAY_AMOUNT, MOST_SOLD_ITEM_CURRENT_MONTH, MOST_SOLD_ITEM_CURRENT_MONTH_AMOUNT, MOST_SOLD_ITEM_CURRENT_WEEK, MOST_SOLD_ITEM_CURRENT_WEEK_AMOUNT, MOST_SOLD_ITEM_CURRENT_DAY, MOST_SOLD_ITEM_CURRENT_DAY_AMOUNT};
      }
   }

   private static enum SimpleIndex {
      FIRST(0),
      SECOND(1),
      THIRD(2);

      public final int index;

      private SimpleIndex(int param3) {
         this.index = index;
      }

      public static PAPIExpansion.SimpleIndex get(String param) {
         try {
            return valueOf(param);
         } catch (IllegalArgumentException var2) {
            return null;
         }
      }

      // $FF: synthetic method
      private static PAPIExpansion.SimpleIndex[] $values() {
         return new PAPIExpansion.SimpleIndex[]{FIRST, SECOND, THIRD};
      }
   }

   private final class ActivePlaceholders {
      boolean top_player_bought_items_current_month;
      boolean top_player_bought_items_current_week;
      boolean top_player_bought_items_current_day;
      boolean top_player_sold_items_current_month;
      boolean top_player_sold_items_current_week;
      boolean top_player_sold_items_current_day;
      boolean top_bought_items_current_month;
      boolean top_bought_items_current_week;
      boolean top_bought_items_current_day;
      boolean top_sold_items_current_month;
      boolean top_sold_items_current_week;
      boolean top_sold_items_current_day;

      private ActivePlaceholders() {
         this.top_player_bought_items_current_month = false;
         this.top_player_bought_items_current_week = false;
         this.top_player_bought_items_current_day = false;
         this.top_player_sold_items_current_month = false;
         this.top_player_sold_items_current_week = false;
         this.top_player_sold_items_current_day = false;
         this.top_bought_items_current_month = false;
         this.top_bought_items_current_week = false;
         this.top_bought_items_current_day = false;
         this.top_sold_items_current_month = false;
         this.top_sold_items_current_week = false;
         this.top_sold_items_current_day = false;
      }

      // $FF: synthetic method
      ActivePlaceholders(Object x1) {
         this();
      }
   }
}
