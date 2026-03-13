package me.gypopo.economyshopgui.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.api.events.PostTransactionEvent;
import me.gypopo.economyshopgui.api.events.PreTransactionEvent;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.SmartStack;
import me.gypopo.economyshopgui.util.Sold;
import me.gypopo.economyshopgui.util.Transaction;
import me.gypopo.economyshopgui.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class SellGUI extends ShopInventory {
   private Inventory inv;
   private static final Map<Integer, ItemStack> staticItems = new HashMap();
   private static final int size = Math.max(1, ConfigManager.getConfig().getInt("sellgui-screen.gui-rows", 6)) * 9;

   public void open(Player p) {
      this.inv = EconomyShopGUI.getInstance().getMetaUtils().createInventory(this, size, Lang.INVENTORY_SELLGUI_TITLE.get());
      staticItems.forEach((s, i) -> {
         this.inv.setItem(s, i);
      });
      EconomyShopGUI.getInstance().navBar.addSellGUINavBar(this.inv, p);
      p.openInventory(this.inv);
   }

   public void sellItems(Player player) {
      Transaction.Result result = Transaction.Result.SUCCESS;
      Map<Integer, SmartStack> toRemove = new HashMap();
      Map<ShopItem, Sold> CACHE = new HashMap();
      int amount = 0;

      for(int i = 0; i <= size - (EconomyShopGUI.getInstance().navBar.isEnableSellGUINav() ? 10 : 1); ++i) {
         if (!staticItems.containsKey(i)) {
            ItemStack item = this.inv.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
               ShopItem shopItem = EconomyShopGUI.getInstance().createItem.matchShopItem(item, player, "sellgui");
               if (shopItem != null && shopItem.getSellPrice() >= 0.0D) {
                  int qty = item.getAmount();
                  if (shopItem.isMaxSell(((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount() + qty)) {
                     if (((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount() >= shopItem.getMaxSell()) {
                        continue;
                     }

                     qty = shopItem.getMaxSell() - ((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount();
                  }

                  Sold sold = (Sold)CACHE.computeIfAbsent(shopItem, (k) -> {
                     return new Sold();
                  });
                  sold.addValues(qty, shopItem.getSellPrice(player, item, qty));
                  amount += qty;
                  ItemStack itemStack = new ItemStack(item);
                  itemStack.setAmount(item.getAmount() - qty);
                  toRemove.put(i, new SmartStack(itemStack));
                  sold.addIndex(i);
               } else if (EconomyShopGUI.getInstance().sellShulkers && XMaterial.isShulker(item.getType())) {
                  Integer sold = this.sellShulker(CACHE, toRemove, player, item, "sellgui", i);
                  if (sold != null) {
                     amount += sold;
                  }
               }
            }
         }
      }

      Iterator var12 = (new HashSet(CACHE.entrySet())).iterator();

      while(var12.hasNext()) {
         Entry<ShopItem, Sold> sold = (Entry)var12.next();
         if (((ShopItem)sold.getKey()).isMinSell(((Sold)sold.getValue()).getAmount())) {
            CACHE.remove(sold.getKey());
            amount -= ((Sold)sold.getValue()).getAmount();
            Set var10000 = ((Sold)sold.getValue()).getIndexes();
            Objects.requireNonNull(toRemove);
            var10000.forEach(toRemove::remove);
         }
      }

      Map<EcoType, Double> prices = new HashMap();
      Map<ShopItem, Integer> items = new HashMap();
      if (amount > 0) {
         prices = this.callPreTransactionEvent(this.getItems(items, CACHE), this.getPrices((Map)prices, CACHE), player, amount);
         if (prices != null) {
            this.removeItems(this.inv, toRemove);
            ((Map)prices).forEach((type, price) -> {
               EconomyShopGUI.getInstance().getEcoHandler().getEcon(type).depositBalance(player, price);
            });
            SendMessage.sendTransactionMessage(player, amount, (Map)prices, items, Transaction.Type.SELL_GUI_SCREEN);
         } else {
            result = Transaction.Result.TRANSACTION_CANCELLED;
         }
      } else {
         result = Transaction.Result.NO_ITEMS_FOUND;
         SendMessage.chatToPlayer(player, Lang.NO_ITEM_FOUND.get());
      }

      this.addUnsoldItems(player);
      Bukkit.getPluginManager().callEvent(new PostTransactionEvent(items, (Map)prices, player, amount, Transaction.Type.SELL_ALL_COMMAND, result));
   }

   private void addUnsoldItems(Player p) {
      for(int i = 0; i <= size - (EconomyShopGUI.getInstance().navBar.isEnableSellGUINav() ? 10 : 1); ++i) {
         if (!staticItems.containsKey(i)) {
            ItemStack item = this.inv.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
               Iterator var4 = p.getInventory().addItem(new ItemStack[]{item}).values().iterator();

               while(var4.hasNext()) {
                  ItemStack stack = (ItemStack)var4.next();
                  p.getWorld().dropItem(p.getLocation(), stack);
               }
            }
         }
      }

   }

   private Integer sellShulker(Map<ShopItem, Sold> CACHE, Map<Integer, SmartStack> toRemove, Player p, ItemStack box, String perm, int slot) {
      Map<ShopItem, Sold> bCACHE = new HashMap();
      Map<Integer, ItemStack> items = new HashMap();
      int removed = 0;
      if (box.getItemMeta() instanceof BlockStateMeta) {
         BlockStateMeta bm = (BlockStateMeta)box.getItemMeta();
         if (bm.getBlockState() instanceof ShulkerBox) {
            Inventory inv = ((ShulkerBox)bm.getBlockState()).getInventory();
            if (inv.isEmpty()) {
               return null;
            }

            for(int i = 0; i < 27; ++i) {
               ItemStack is = inv.getItem(i);
               if (is != null && !is.getType().equals(Material.AIR)) {
                  ShopItem shopItem = EconomyShopGUI.getInstance().createItem.matchShopItem(is, p, perm);
                  if (shopItem != null && shopItem.getSellPrice() >= 0.0D) {
                     int qty = is.getAmount();
                     if (shopItem.isMaxSell(((Sold)bCACHE.getOrDefault(shopItem, new Sold())).getAmount() + ((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount() + qty)) {
                        if (((Sold)bCACHE.getOrDefault(shopItem, new Sold())).getAmount() + ((Sold)CACHE.getOrDefault(shopItem, new Sold())).getAmount() >= shopItem.getMaxSell()) {
                           continue;
                        }

                        qty = shopItem.getMaxSell() - ((Sold)bCACHE.getOrDefault(shopItem, new Sold())).getAmount();
                     }

                     Sold sold = (Sold)bCACHE.computeIfAbsent(shopItem, (k) -> {
                        return new Sold();
                     });
                     sold.addValues(qty, shopItem.getSellPrice(p, is, qty));
                     removed += qty;
                     ItemStack stack = new ItemStack(is);
                     stack.setAmount(is.getAmount() - qty);
                     items.put(i, stack);
                     sold.addIndex(i);
                  }
               }
            }

            if (!bCACHE.isEmpty()) {
               toRemove.put(slot, new SmartStack.Shulker(box, items));
               bCACHE.forEach((item, soldx) -> {
                  CACHE.merge(item, soldx, Sold::addValue);
               });
               return removed;
            }
         }
      }

      return null;
   }

   private void removeItems(Inventory inv, Map<Integer, SmartStack> items) {
      items.forEach((i, stack) -> {
         if (stack instanceof SmartStack.Shulker) {
            this.emptyShulker((SmartStack.Shulker)stack);
         } else if (stack.getItem().getAmount() != 0) {
            inv.setItem(i, stack.getItem());
         } else {
            inv.clear(i);
         }

      });
   }

   private void emptyShulker(SmartStack.Shulker shulker) {
      ItemStack sBox = shulker.getItem();
      BlockStateMeta sbm = (BlockStateMeta)sBox.getItemMeta();
      ShulkerBox s = (ShulkerBox)sbm.getBlockState();
      shulker.getItems().forEach((i, item) -> {
         if (item.getAmount() != 0) {
            s.getInventory().setItem(i, item);
         } else {
            s.getInventory().clear(i);
         }

      });
      sbm.setBlockState(s);
      sBox.setItemMeta(sbm);
   }

   private Map<EcoType, Double> getPrices(Map<EcoType, Double> prices, Map<ShopItem, Sold> CACHE) {
      Iterator var3 = CACHE.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<ShopItem, Sold> entry = (Entry)var3.next();
         prices.merge(((ShopItem)entry.getKey()).getEcoType(), ((Sold)entry.getValue()).getPrice(), Double::sum);
      }

      return prices;
   }

   private Map<ShopItem, Integer> getItems(Map<ShopItem, Integer> items, Map<ShopItem, Sold> CACHE) {
      Iterator var3 = CACHE.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<ShopItem, Sold> entry = (Entry)var3.next();
         items.merge((ShopItem)entry.getKey(), ((Sold)entry.getValue()).getAmount(), Integer::sum);
      }

      return items;
   }

   private Map<EcoType, Double> callPreTransactionEvent(Map<ShopItem, Integer> items, Map<EcoType, Double> prices, Player player, int amount) {
      PreTransactionEvent preTransactionEvent = new PreTransactionEvent(items, prices, player, amount, Transaction.Type.SELL_GUI_SCREEN);
      Bukkit.getPluginManager().callEvent(preTransactionEvent);
      return preTransactionEvent.isCancelled() ? null : preTransactionEvent.getPrices();
   }

   public Inventory getInventory() {
      return this.inv;
   }

   private static void loadItems() {
      Iterator var0 = ConfigManager.getConfig().getConfigurationSection("sellgui-screen.items").getKeys(false).iterator();

      while(true) {
         while(var0.hasNext()) {
            String item = (String)var0.next();
            List<Integer> slots = EconomyShopGUI.getInstance().calculateAmount.getSlots(ConfigManager.getConfig().getString("sellgui-screen.items." + item + ".slot", (String)null));
            if (slots == null) {
               SendMessage.warnMessage("Invalid slot for item, skipping...");
               SendMessage.errorItemConfig("sellgui-screen.items." + item);
            } else {
               ItemStack stack = EconomyShopGUI.getInstance().createItem.createFillItem((String)null, ConfigManager.getConfig().getConfigurationSection("sellgui-screen.items." + item));

               for(int i = slots.size() - 1; i > -1; --i) {
                  int slot = (Integer)slots.get(i);
                  if (slot < (EconomyShopGUI.getInstance().navBar.isEnableSellGUINav() ? size - 9 : size) && slot >= 0) {
                     staticItems.put(slot, stack);
                  } else {
                     SendMessage.warnMessage("Item slot " + slot + " is out of bounds for menu size of " + size + ", skipping ...");
                     SendMessage.errorItemConfig("sellgui-screen.items." + item);
                  }
               }
            }
         }

         return;
      }
   }

   public static boolean isFillItem(int slot) {
      return staticItems.containsKey(slot);
   }

   static {
      if (ConfigManager.getConfig().contains("sellgui-screen.items")) {
         loadItems();
      }

   }
}
