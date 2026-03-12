package me.gypopo.economyshopgui.objects;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.methodes.CreateItem;
import me.gypopo.economyshopgui.util.ItemBuilder;
import me.gypopo.economyshopgui.util.Transaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TransactionItem {
   private final CreateItem.TransactionItemAction action;
   private final CreateItem.TransactionItemType type;
   private final Set<Integer> placeholders;
   private final boolean pricingPlaceholders;
   private final ItemStack item;
   private final List<Integer> slots;
   private final boolean dynamicItem;

   public TransactionItem(CreateItem.TransactionItemAction action, CreateItem.TransactionItemType type, ItemStack item, List<Integer> slots, boolean dynamicItem) {
      this.action = action;
      this.dynamicItem = dynamicItem;
      this.placeholders = this.getPlaceholders(item);
      this.pricingPlaceholders = !this.placeholders.isEmpty();
      this.type = type;
      this.item = item;
      this.slots = slots;
   }

   public CreateItem.TransactionItemAction getAction() {
      return this.action;
   }

   public CreateItem.TransactionItemType getType() {
      return this.type;
   }

   public ItemStack getItem() {
      return new ItemStack(this.item);
   }

   public ItemStack getItem(ShopItem shopItem, Player p, Transaction.Type type, int amount) {
      ItemStack item = this.dynamicItem ? (new ItemBuilder(shopItem.getShopItem())).withSimpleMeta(this.getItem()).build() : this.getItem();
      return this.pricingPlaceholders && p != null ? this.parsePlaceholders(shopItem, p, item, type, amount) : item;
   }

   public ItemStack getItem(ItemStack selectedItem, ShopItem shopItem, Player p, Transaction.Type type, int amount) {
      selectedItem.setAmount(amount);
      ItemMeta dMeta = selectedItem.getItemMeta();
      ItemMeta sMeta = this.item.getItemMeta();
      if (sMeta.hasDisplayName()) {
         dMeta.setDisplayName(sMeta.getDisplayName());
      }

      if (sMeta.hasLore()) {
         dMeta.setLore(sMeta.getLore());
      }

      this.parsePricePlaceholders(shopItem, p, dMeta, type, type == Transaction.Type.BUY_STACKS_SCREEN ? amount * selectedItem.getMaxStackSize() : amount);
      selectedItem.setItemMeta(dMeta);
      return selectedItem;
   }

   public List<Integer> getSlots() {
      return this.slots;
   }

   private ItemStack parsePlaceholders(ShopItem shopItem, Player p, ItemStack item, Transaction.Type type, int amount) {
      ItemMeta meta = item.getItemMeta();
      if (this.action.type == CreateItem.TransactionItemActionType.INSTA_BUY || this.action.type == CreateItem.TransactionItemActionType.INSTA_SELL) {
         amount = item.getAmount();
      }

      if (this.placeholders.contains(-1)) {
         meta.setDisplayName(meta.getDisplayName().replace("%amount%", amount + "").replace("%price%", this.getCalculatedPriceFormatted(shopItem, p, item, type, amount)));
      }

      if (meta.hasLore()) {
         List<String> lore = meta.getLore();
         Iterator var8 = this.placeholders.iterator();

         while(var8.hasNext()) {
            Integer i = (Integer)var8.next();
            if (i != -1) {
               lore.set(i, ((String)lore.get(i)).replace("%amount%", amount + "").replace("%price%", this.getCalculatedPriceFormatted(shopItem, p, item, type, amount)));
            }
         }

         meta.setLore(lore);
      }

      item.setItemMeta(meta);
      return item;
   }

   private ItemMeta parsePricePlaceholders(ShopItem shopItem, Player p, ItemMeta meta, Transaction.Type type, int amount) {
      if (this.placeholders.contains(-1)) {
         EconomyShopGUI.getInstance().getMetaUtils().setRawDisplayName(meta, EconomyShopGUI.getInstance().getMetaUtils().getRawDisplayName(meta).replace("%amount%", amount + "").replace("%price%", this.getCalculatedPriceFormatted(shopItem, p, this.item, type, amount)));
      }

      if (meta.hasLore()) {
         List<String> lore = EconomyShopGUI.getInstance().getMetaUtils().getRawLore(meta);
         Iterator var7 = this.placeholders.iterator();

         while(var7.hasNext()) {
            Integer i = (Integer)var7.next();
            if (i != -1) {
               lore.set(i, ((String)lore.get(i)).replace("%amount%", amount + "").replace("%price%", this.getCalculatedPriceFormatted(shopItem, p, this.item, type, amount)));
            }
         }

         EconomyShopGUI.getInstance().getMetaUtils().setRawLore(meta, lore);
      }

      return meta;
   }

   private double getCalculatedPrice(ShopItem shopItem, Player p, ItemStack item, Transaction.Mode mode, int amount) {
      if (!this.pricingPlaceholders) {
         return 0.0D;
      } else {
         if (this.action.type == CreateItem.TransactionItemActionType.INSTA_BUY || this.action.type == CreateItem.TransactionItemActionType.INSTA_SELL) {
            amount = item.getAmount();
         }

         if (mode == Transaction.Mode.BUY) {
            return shopItem.getBuyPrice(p) * (double)amount;
         } else {
            return mode == Transaction.Mode.SELL ? shopItem.getSellPrice(p, item, amount) : 0.0D;
         }
      }
   }

   private String getCalculatedPriceFormatted(ShopItem shopItem, Player p, ItemStack item, Transaction.Type type, int amount) {
      if (!this.pricingPlaceholders) {
         return "0.0";
      } else {
         if (this.action.type == CreateItem.TransactionItemActionType.INSTA_BUY || this.action.type == CreateItem.TransactionItemActionType.INSTA_SELL) {
            amount = type == Transaction.Type.BUY_STACKS_SCREEN ? item.getAmount() * item.getMaxStackSize() : item.getAmount();
         }

         Transaction.Mode mode = type.getTransactionMode();
         if (mode == Transaction.Mode.BUY) {
            return EconomyShopGUI.getInstance().formatPrice(shopItem.getEcoType(), shopItem.getBuyPrice(p) * (double)amount);
         } else {
            return mode == Transaction.Mode.SELL ? EconomyShopGUI.getInstance().formatPrice(shopItem.getEcoType(), shopItem.getSellPrice(p, item, amount)) : "0.0";
         }
      }
   }

   private Set<Integer> getPlaceholders(ItemStack item) {
      Set<Integer> placeholders = new HashSet();
      if (!item.hasItemMeta()) {
         return placeholders;
      } else {
         String name = item.getItemMeta().getDisplayName();
         if (name != null && (name.contains("%price%") || name.contains("%amount%"))) {
            placeholders.add(-1);
         }

         if (!item.getItemMeta().hasLore()) {
            return placeholders;
         } else {
            int i = 0;

            for(Iterator var5 = item.getItemMeta().getLore().iterator(); var5.hasNext(); ++i) {
               String s = (String)var5.next();
               if (s.contains("%price%") || s.contains("%amount%")) {
                  placeholders.add(i);
               }
            }

            return placeholders;
         }
      }
   }
}
