package me.gypopo.economyshopgui.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.api.objects.BuyPrice;
import me.gypopo.economyshopgui.api.objects.SellPrice;
import me.gypopo.economyshopgui.api.objects.SellPrices;
import me.gypopo.economyshopgui.api.prices.AdvancedBuyPrice;
import me.gypopo.economyshopgui.api.prices.AdvancedSellPrice;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.objects.shops.ShopSection;
import me.gypopo.economyshopgui.providers.EconomyProvider;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.Sold;
import me.gypopo.economyshopgui.util.Transaction;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EconomyShopGUIHook {
   private static EconomyShopGUI plugin;

   public EconomyShopGUIHook(EconomyShopGUI plugin) {
      EconomyShopGUIHook.plugin = plugin;
   }

   public static boolean isSellAble(ShopItem shopItem) {
      return shopItem.getSellPrice() >= 0.0D;
   }

   /** @deprecated */
   @Nullable
   @Deprecated
   public static Double getItemSellPrice(ItemStack item) {
      ShopItem shopItem = plugin.createItem.matchShopItem(item);
      return shopItem == null ? null : shopItem.getSellPrice(item);
   }

   /** @deprecated */
   @Nullable
   @Deprecated
   public static Double getItemSellPrice(Player player, ItemStack item) {
      ShopItem shopItem = plugin.createItem.matchShopItem(player, item, Transaction.Mode.SELL);
      return shopItem == null ? null : shopItem.getSellPrice(player, item);
   }

   /** @deprecated */
   @Nullable
   @Deprecated
   public static Double getItemBuyPrice(ItemStack item) {
      ShopItem shopItem = plugin.createItem.matchShopItem(item);
      return shopItem == null ? null : shopItem.getBuyPrice() * (double)item.getAmount();
   }

   /** @deprecated */
   @Nullable
   @Deprecated
   public static Double getItemBuyPrice(Player player, ItemStack item) {
      ShopItem shopItem = plugin.createItem.matchShopItem(player, item, Transaction.Mode.BUY);
      return shopItem == null ? null : shopItem.getBuyPrice(player) * (double)item.getAmount();
   }

   public static Double getItemSellPrice(ShopItem shopItem, ItemStack item) {
      return shopItem.getSellPrice(item);
   }

   public static Double getItemSellPrice(ShopItem shopItem, ItemStack item, int amount, int sold) {
      return shopItem.getSellPrice(item);
   }

   public static Double getItemSellPrice(ShopItem shopItem, ItemStack item, Player player) {
      return shopItem.getSellPrice(player, item);
   }

   public static Double getItemSellPrice(ShopItem shopItem, ItemStack item, Player player, int amount, int sold) {
      return shopItem.getSellPrice(player, item, amount);
   }

   public static Optional<SellPrice> getSellPrice(OfflinePlayer player, ItemStack item) {
      Objects.requireNonNull(item, "Item cannot be null");
      Objects.requireNonNull(player, "Player cannot be null");
      boolean online = player.isOnline();
      int amount = item.getAmount();
      ShopItem shopItem = !online ? getShopItem(item) : plugin.createItem.matchShopItem((Player)player, item, Transaction.Mode.SELL);
      if (shopItem != null && isSellAble(shopItem)) {
         if (shopItem.isMaxSell(amount)) {
            return Optional.empty();
         } else if (shopItem.isMinSell(amount)) {
            return Optional.empty();
         } else {
            double sellPrice = !online ? getItemSellPrice(shopItem, item, item.getAmount(), 0) : getItemSellPrice(shopItem, item, (Player)player, item.getAmount(), 0);
            SellPrice price = new SellPrice(player, amount, shopItem, shopItem.getEcoType(), sellPrice);
            return Optional.of(price);
         }
      } else {
         return Optional.empty();
      }
   }

   public static SellPrices getSellPrices(OfflinePlayer player, ItemStack... items) {
      Objects.requireNonNull(player, "Player cannot be null");
      Objects.requireNonNull(items, "Items cannot be null");
      boolean online = player.isOnline();
      Map<EcoType, Double> prices = new HashMap();
      Map<ShopItem, Sold> CACHE = new HashMap();
      ItemStack[] var5 = items;
      int var6 = items.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ItemStack item = var5[var7];
         if (item != null && item.getType() != Material.AIR) {
            ShopItem shopItem = !online ? getShopItem(item) : plugin.createItem.matchShopItem((Player)player, item, Transaction.Mode.SELL);
            if (shopItem != null && isSellAble(shopItem)) {
               Sold cached = (Sold)CACHE.computeIfAbsent(shopItem, (k) -> {
                  return new Sold();
               });
               int alreadySold = cached.getAmount();
               if (!shopItem.isMaxSell(item.getAmount() + alreadySold)) {
                  double sellPrice = !online ? getItemSellPrice(shopItem, item, item.getAmount(), alreadySold) : getItemSellPrice(shopItem, item, (Player)player, item.getAmount(), alreadySold);
                  prices.merge(shopItem.getEcoType(), sellPrice, Double::sum);
                  cached.addValues(item.getAmount(), sellPrice);
               }
            }
         }
      }

      Iterator var14 = (new HashSet(CACHE.entrySet())).iterator();

      while(var14.hasNext()) {
         Entry<ShopItem, Sold> sold = (Entry)var14.next();
         if (((ShopItem)sold.getKey()).isMinSell(((Sold)sold.getValue()).getAmount())) {
            CACHE.remove(sold.getKey());
            prices.compute(((ShopItem)sold.getKey()).getEcoType(), (key, value) -> {
               double newValue = value - ((Sold)sold.getValue()).getPrice();
               return newValue <= 0.0D ? null : newValue;
            });
         }
      }

      return new SellPrices(player, (Map)CACHE.entrySet().stream().collect(Collectors.toMap(Entry::getKey, (entry) -> {
         return ((Sold)entry.getValue()).getAmount();
      }, Integer::sum)), prices);
   }

   public static SellPrices getCutSellPrices(OfflinePlayer player, ItemStack[] items, boolean allowModify) {
      Objects.requireNonNull(player, "Player cannot be null");
      Objects.requireNonNull(items, "Items cannot be null");
      boolean online = player.isOnline();
      Map<ShopItem, Sold> CACHE = new HashMap();
      Map<EcoType, Double> prices = new HashMap();

      for(int i = 0; i < items.length; ++i) {
         ItemStack item = items[i];
         if (item != null && item.getType() != Material.AIR) {
            ShopItem shopItem = !online ? getShopItem(item) : plugin.createItem.matchShopItem((Player)player, item, Transaction.Mode.SELL);
            if (shopItem != null && isSellAble(shopItem)) {
               Sold cached = (Sold)CACHE.computeIfAbsent(shopItem, (k) -> {
                  return new Sold();
               });
               int alreadySold = cached.getAmount();
               int limit = getMaxSell(shopItem, item.getAmount(), alreadySold);
               if (limit != -1) {
                  double sellPrice = !online ? getItemSellPrice(shopItem, item, limit, alreadySold) : getItemSellPrice(shopItem, item, (Player)player, limit, alreadySold);
                  prices.merge(shopItem.getEcoType(), sellPrice, Double::sum);
                  cached.addValues(limit, sellPrice);
                  cached.addIndex(i);
               }
            }
         }
      }

      Iterator var14 = (new HashSet(CACHE.entrySet())).iterator();

      while(true) {
         while(var14.hasNext()) {
            Entry<ShopItem, Sold> sold = (Entry)var14.next();
            if (((ShopItem)sold.getKey()).isMinSell(((Sold)sold.getValue()).getAmount())) {
               CACHE.remove(sold.getKey());
               prices.compute(((ShopItem)sold.getKey()).getEcoType(), (key, value) -> {
                  double newValue = value - ((Sold)sold.getValue()).getPrice();
                  return newValue <= 0.0D ? null : newValue;
               });
            } else if (allowModify) {
               int amount = ((Sold)sold.getValue()).getAmount();

               Integer i;
               for(Iterator var17 = ((Sold)sold.getValue()).getIndexes().iterator(); var17.hasNext(); items[i] = null) {
                  i = (Integer)var17.next();
                  if (amount < items[i].getAmount()) {
                     ItemStack stack = new ItemStack(items[i]);
                     stack.setAmount(stack.getAmount() - amount);
                     items[i] = stack;
                     break;
                  }

                  amount -= items[i].getAmount();
               }
            }
         }

         return new SellPrices(player, (Map)CACHE.entrySet().stream().collect(Collectors.toMap(Entry::getKey, (entry) -> {
            return ((Sold)entry.getValue()).getAmount();
         }, Integer::sum)), prices);
      }
   }

   public static boolean isBuyAble(ShopItem shopItem) {
      return shopItem.getBuyPrice() >= 0.0D;
   }

   /** @deprecated */
   @Deprecated
   public static Double getItemBuyPrice(ShopItem shopItem, ItemStack item) {
      return shopItem.getBuyPrice() * (double)item.getAmount();
   }

   /** @deprecated */
   @Deprecated
   public static Double getItemBuyPrice(ShopItem shopItem, ItemStack item, Player player) {
      return shopItem.getBuyPrice(player) * (double)item.getAmount();
   }

   public static double getItemBuyPrice(ShopItem shopItem, int amount) {
      return shopItem.getBuyPrice() * (double)amount;
   }

   public static double getItemBuyPrice(ShopItem shopItem, Player player, int amount) {
      return shopItem.getBuyPrice(player) * (double)amount;
   }

   public static Optional<BuyPrice> getBuyPrice(OfflinePlayer player, ItemStack item) {
      Objects.requireNonNull(item, "Item cannot be null");
      Objects.requireNonNull(player, "Player cannot be null");
      boolean online = player.isOnline();
      int amount = item.getAmount();
      ShopItem shopItem = !online ? getShopItem(item) : plugin.createItem.matchShopItem((Player)player, item, Transaction.Mode.BUY);
      if (shopItem != null && isBuyAble(shopItem)) {
         if (shopItem.isMaxBuy(item.getAmount())) {
            return Optional.empty();
         } else if (shopItem.isMinBuy(item.getAmount())) {
            return Optional.empty();
         } else {
            if (shopItem.getLimitedStockMode() != 0) {
               int stock = getItemStock(shopItem, player.getUniqueId());
               if (amount > stock) {
                  return Optional.empty();
               }
            }

            double buyPrice = !online ? shopItem.getBuyPrice() * (double)amount : shopItem.getBuyPrice((Player)player) * (double)amount;
            BuyPrice price = new BuyPrice(player, amount, shopItem, shopItem.getEcoType(), buyPrice);
            return Optional.of(price);
         }
      } else {
         return Optional.empty();
      }
   }

   public static boolean hasMultipleBuyPrices(ShopItem shopItem) {
      return false;
   }

   public static AdvancedBuyPrice getMultipleBuyPrices(ShopItem shopItem) {
      return new AdvancedBuyPrice(shopItem);
   }

   public static boolean hasMultipleSellPrices(ShopItem shopItem) {
      return false;
   }

   public static AdvancedSellPrice getMultipleSellPrices(ShopItem shopItem) {
      return new AdvancedSellPrice(shopItem);
   }

   public static EconomyProvider getEcon(@Nullable EcoType ecoType) {
      return plugin.getEcoHandler().getEcon(ecoType);
   }

   /** @deprecated */
   @Deprecated
   public static void buyItem(ItemStack item, int amount) {
   }

   /** @deprecated */
   @Deprecated
   public static void sellItem(ItemStack item, int amount) {
   }

   public static void buyItem(ShopItem shopItem, int amount) {
   }

   public static void sellItem(ShopItem shopItem, int amount) {
   }

   @Nullable
   public static ShopItem getShopItem(ItemStack item) {
      return plugin.createItem.matchShopItem(item);
   }

   @Nullable
   public static ShopItem getShopItem(Player player, ItemStack item) {
      return plugin.createItem.matchShopItem(player, item);
   }

   @Nullable
   public static ShopItem getShopItem(String itemPath) {
      try {
         ShopSection section = plugin.getSection(itemPath.split("\\.")[0]);
         return section.getShopItem(itemPath);
      } catch (Exception var2) {
         return null;
      }
   }

   @Nullable
   public static ShopSection getShopSection(String section) {
      return plugin.getSection(section);
   }

   public static List<String> getShopSections() {
      return plugin.getShopSections();
   }

   public static Map<String, ShopSection> getSections() {
      return plugin.getSections();
   }

   public static int getItemStock(ShopItem shopItem, @Nullable UUID uuid) {
      return Integer.MAX_VALUE;
   }

   public static Long getItemStockRestockTime(ShopItem shopItem, @Nullable UUID uuid) {
      return null;
   }

   public static int buyItemStock(ShopItem shopItem, @Nullable UUID uuid, int amount) {
      return 0;
   }

   public static void sellItemStock(ShopItem shopItem, @Nullable UUID uuid, int amount) {
   }

   public static int getSellLimit(ShopItem shopItem, @Nullable UUID uuid) {
      return Integer.MAX_VALUE;
   }

   public static Long getSellLimitRestockTime(ShopItem shopItem, @Nullable UUID uuid) {
      return null;
   }

   public static int sellItemLimit(ShopItem shopItem, @Nullable UUID uuid, int amount) {
      return 0;
   }

   public static boolean hasPermissions(ShopItem shopItem, Player player) {
      return true;
   }

   public static boolean hasPermissions(ShopItem shopItem, Player player, String root) {
      return true;
   }

   private static int getMaxSell(ShopItem shopItem, int qty, int alreadySold) {
      if (shopItem.getMaxSell() > 0) {
         if (shopItem.isMaxSell(alreadySold)) {
            return -1;
         }

         qty = Math.min(qty, shopItem.getMaxSell() - alreadySold);
      }

      return qty;
   }
}
