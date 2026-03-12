package me.gypopo.economyshopgui.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.methodes.CalculateAmount;
import me.gypopo.economyshopgui.methodes.CreateItem;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.UserManager;
import me.gypopo.economyshopgui.util.ItemBuilder;
import me.gypopo.economyshopgui.util.Transaction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TransactionMenu extends ShopInventory {
   private static final TransactionMenu.VisualLimitAction visualLimitAction = TransactionMenu.VisualLimitAction.get();
   private static final boolean checkInitialAmount = ConfigManager.getConfig().getBoolean("visual-transaction-limits.initial-amount");
   private final List<ItemStack> CACHE = new ArrayList();
   private final ShopItem shopItem;
   private final boolean disabledBackButton;
   private final String rootSection;
   private final ItemStack selectedItem;
   private final Transaction.Mode transactionMode;
   private final Transaction.Type transactionType;
   private final Inventory inv;
   private final Player player;
   private int amount;

   public TransactionMenu(Player p, ShopItem shopItem, String rootSection, boolean disabledBackButton, Transaction.Mode transactionMode, Transaction.Type transactionType, int amount) {
      this.shopItem = shopItem;
      this.player = p;
      this.selectedItem = new ItemStack(this.shopItem.getShopItem());
      this.rootSection = rootSection;
      this.disabledBackButton = disabledBackButton;
      this.transactionMode = transactionMode;
      this.transactionType = transactionType;
      this.amount = this.getInitialAmount(this.shopItem.getStackSize());
      this.inv = EconomyShopGUI.getInstance().getMetaUtils().createInventory(this, CalculateAmount.getInvSlotsDef(UserManager.getUser(p), TransactionMenus.getSize(transactionType)), this.getInvTitle());
      this.init();
   }

   private int getInitialAmount(int stackSize) {
      if (this.transactionMode == Transaction.Mode.BUY) {
         return this.shopItem.isMinBuy(stackSize) ? Math.max(this.shopItem.getMinBuy(), stackSize) : stackSize;
      } else if (this.transactionMode == Transaction.Mode.SELL) {
         return this.shopItem.isMinSell(stackSize) ? Math.max(this.shopItem.getMinSell(), stackSize) : stackSize;
      } else {
         return 1;
      }
   }

   public boolean isDisabledBackButton() {
      return this.disabledBackButton;
   }

   public String getRootSection() {
      return this.rootSection;
   }

   public Transaction.Mode getTransactionMode() {
      return this.transactionMode;
   }

   public Transaction.Type getTransactionType() {
      return this.transactionType;
   }

   public String getItemPath() {
      return this.shopItem.getItemPath();
   }

   public ItemStack getSelectedItem() {
      return this.selectedItem;
   }

   public ShopItem getShopItem() {
      return this.shopItem;
   }

   public int getAmount() {
      return this.amount;
   }

   public void emptyCache() {
      Iterator var1 = this.CACHE.iterator();

      while(var1.hasNext()) {
         ItemStack i = (ItemStack)var1.next();
         this.player.getInventory().removeItem(new ItemStack[]{i});
      }

      this.CACHE.clear();
   }

   public int getTotalAmount() {
      return this.transactionType == Transaction.Type.BUY_STACKS_SCREEN ? this.amount * this.selectedItem.getMaxStackSize() : this.amount;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }

   public void addItemStacks() {
      int maxStackSize = this.shopItem.getItemToGive().getMaxStackSize();
      double div = (double)this.getTotalAmount() / (double)maxStackSize;
      int fullStacks = (int)Math.floor(div);
      int stacks = div == Math.floor(div) ? fullStacks : (int)Math.ceil(div);
      int amountLeft = this.getTotalAmount();
      ItemStack[] items = new ItemStack[stacks];
      if (fullStacks >= 1) {
         for(int i = 0; i < fullStacks; ++i) {
            ItemStack item = new ItemStack(this.shopItem.getItemToGive());
            item.setAmount(maxStackSize);
            items[i] = item;
            amountLeft -= maxStackSize;
         }
      }

      if (amountLeft >= 1) {
         ItemStack item = new ItemStack(this.shopItem.getItemToGive());
         item.setAmount(amountLeft);
         items[stacks - 1] = item;
      }

      this.addItems(items);
   }

   private void addItems(ItemStack[] items) {
      HashMap<Integer, ItemStack> leftItems = this.player.getInventory().addItem(items);
      if (!leftItems.isEmpty()) {
         if (EconomyShopGUI.getInstance().dropItemsOnGround) {
            Location loc = this.player.getLocation();
            World world = this.player.getWorld();
            Iterator var5 = leftItems.values().iterator();

            while(var5.hasNext()) {
               ItemStack item = (ItemStack)var5.next();
               world.dropItem(loc, item);
            }

            SendMessage.chatToPlayer(this.player, Lang.NOT_ENOUGH_SPACE_INSIDE_INVENTORY.get());
         } else {
            int itemsLeft = 0;

            ItemStack item;
            for(Iterator var8 = leftItems.values().iterator(); var8.hasNext(); itemsLeft += item.getAmount()) {
               item = (ItemStack)var8.next();
            }

            SendMessage.chatToPlayer(this.player, Lang.COULD_NOT_STORE_ALL_ITEMS.get().replace("%amount%", Integer.toString(itemsLeft)));
         }
      }

      this.player.updateInventory();
   }

   public Double sellAllItems(int limit) {
      double sellPrice = 0.0D;
      int amount = 0;
      if (this.shopItem.isMaxSell(limit)) {
         limit = this.shopItem.getMaxSell();
      }

      for(int i = 0; i < 36; ++i) {
         ItemStack itemInInv = this.player.getInventory().getItem(i);
         if (itemInInv != null && !itemInInv.getType().equals(Material.AIR) && this.shopItem.match(itemInInv)) {
            if (limit < itemInInv.getAmount()) {
               ItemStack item = new ItemStack(itemInInv);
               item.setAmount(limit);
               this.CACHE.add(item);
               amount += limit;
               sellPrice += this.shopItem.getSellPrice(this.player, itemInInv, limit);
               break;
            }

            this.CACHE.add(itemInInv);
            amount += itemInInv.getAmount();
            sellPrice += this.shopItem.getSellPrice(this.player, itemInInv, itemInInv.getAmount());
            limit -= itemInInv.getAmount();
         }
      }

      if (this.shopItem.isMinSell(amount)) {
         return null;
      } else {
         this.amount = amount;
         return amount == 0 ? null : sellPrice;
      }
   }

   public int getFreeSpace() {
      int freespace = 0;
      int maxStackSize = this.shopItem.getItemToGive().getMaxStackSize();
      List<ItemStack> items = new ArrayList(Arrays.asList(this.player.getInventory().getContents()));

      for(int i = 0; i < 36; ++i) {
         ItemStack item = (ItemStack)items.get(i);
         if (item != null && !item.getType().equals(Material.AIR)) {
            if (item.isSimilar(this.shopItem.getItemToGive()) && item.getAmount() < maxStackSize) {
               freespace += maxStackSize - item.getAmount();
            }
         } else {
            freespace += maxStackSize;
         }
      }

      return freespace;
   }

   public int getAmountToSell(int freeSpace) {
      int totalAmount = this.getTotalAmount();
      if (totalAmount > freeSpace && !EconomyShopGUI.getInstance().dropItemsOnGround) {
         return freeSpace;
      } else {
         if (this.shopItem.isMaxBuy(totalAmount)) {
            totalAmount = this.shopItem.getMaxBuy();
         }

         if (this.shopItem.isMinBuy(totalAmount)) {
            totalAmount = this.shopItem.getMinBuy();
         }

         return totalAmount;
      }
   }

   public void sendTransactionCompleteMessage(double price) {
      SendMessage.sendTransactionMessage(this.player, this.getTotalAmount(), price, this.shopItem, this.transactionMode, this.transactionType);
   }

   private String getTransactionScreenType() {
      return this.transactionType.getName();
   }

   private Translatable getInvTitle() {
      Translatable title = null;
      switch(this.transactionType) {
      case SELL_SCREEN:
         title = Lang.INVENTORY_HOWMUCHSELL_TITLE.get();
         break;
      case BUY_SCREEN:
         title = Lang.INVENTORY_HOWMUCHBUY_TITLE.get();
         break;
      case BUY_STACKS_SCREEN:
         title = Lang.INVENTORY_BUYSTACKS_TITLE.get();
         break;
      case SHOPSTAND_BUY_SCREEN:
         title = Lang.SHOPSTAND_BUY_SCREEN_TITLE.get();
         break;
      case SHOPSTAND_SELL_SCREEN:
         title = Lang.SHOPSTAND_SELL_SCREEN_TITLE.get();
      }

      return title.replace("%item%", this.shopItem.getDisplayname()).replace("%material%", EconomyShopGUI.getInstance().getMaterialName(this.selectedItem.getType().name()));
   }

   public double getBasePrice() {
      return this.transactionMode == Transaction.Mode.BUY ? this.shopItem.getBuyPrice() : this.shopItem.getSellPrice();
   }

   public double getPrice(int itemsToSell) {
      return this.shopItem.getBuyPrice(this.player) * (double)itemsToSell;
   }

   private void updateButtons(int amount) {
      TransactionItem fillItem = TransactionMenus.getItemByType(this.transactionType, CreateItem.TransactionItemType.FILLITEM);
      int maxStack = EconomyShopGUI.getInstance().allowIllegalStacks ? 64 : this.shopItem.getItemToGive().getMaxStackSize();
      Iterator var4;
      TransactionItem item;
      if (amount > this.amount || !this.isMaxAmount(amount)) {
         if (visualLimitAction != TransactionMenu.VisualLimitAction.NONE) {
            var4 = TransactionMenus.getItemsByType(this.transactionType, CreateItem.TransactionItemType.INCREASE_AMOUNT).iterator();

            label101:
            while(true) {
               while(true) {
                  if (!var4.hasNext()) {
                     break label101;
                  }

                  item = (TransactionItem)var4.next();
                  if (amount + item.getAction().amount <= maxStack && !this.isMaxAmount(amount + item.getAction().amount)) {
                     this.setItem(item.getSlots(), item.getItem(this.shopItem, this.player, this.transactionType, this.getTotalAmount()));
                  } else if (visualLimitAction == TransactionMenu.VisualLimitAction.RENAME) {
                     this.setItem(item.getSlots(), (new ItemBuilder(item.getItem(this.shopItem, this.player, this.transactionType, this.getTotalAmount()))).withDisplayName(Lang.MAX_AMOUNT_REACHED.get()).build());
                  } else {
                     this.setItem(item.getSlots(), fillItem == null ? new ItemStack(Material.AIR) : fillItem.getItem());
                  }
               }
            }
         }

         amount = amount <= maxStack && !this.isMaxAmount(amount) ? amount : (this.getMaxAmount() != 0 ? Math.min(maxStack, this.getMaxAmount()) : maxStack);
      }

      if (amount < this.amount || !this.isMinAmount(amount)) {
         if (visualLimitAction != TransactionMenu.VisualLimitAction.NONE) {
            var4 = TransactionMenus.getItemsByType(this.transactionType, CreateItem.TransactionItemType.DECREASE_AMOUNT).iterator();

            label77:
            while(true) {
               while(true) {
                  if (!var4.hasNext()) {
                     break label77;
                  }

                  item = (TransactionItem)var4.next();
                  if (amount - item.getAction().amount >= 1 && !this.isMinAmount(amount - item.getAction().amount)) {
                     this.setItem(item.getSlots(), item.getItem(this.shopItem, this.player, this.transactionType, this.getTotalAmount()));
                  } else if (visualLimitAction == TransactionMenu.VisualLimitAction.RENAME) {
                     this.setItem(item.getSlots(), (new ItemBuilder(item.getItem(this.shopItem, this.player, this.transactionType, this.getTotalAmount()))).withDisplayName(Lang.MIN_AMOUNT_REACHED.get()).build());
                  } else {
                     this.setItem(item.getSlots(), fillItem == null ? new ItemStack(Material.AIR) : fillItem.getItem());
                  }
               }
            }
         }

         amount = amount >= 1 && !this.isMinAmount(amount) ? amount : (this.getMinAmount() != 0 ? Math.max(1, this.getMinAmount()) : 1);
      }

      this.amount = amount;
   }

   public void setConfirmTransactionItem() {
      TransactionItem item = TransactionMenus.getItemByType(this.transactionType, CreateItem.TransactionItemType.CONFIRM);
      if (item != null) {
         String formatted = EconomyShopGUI.getInstance().formatPrice(this.shopItem.getEcoType(), (this.transactionMode == Transaction.Mode.BUY ? this.shopItem.getBuyPrice(this.player) : this.shopItem.getSellPrice(this.player)) * (double)this.getTotalAmount());
         ItemStack stack = new ItemStack(item.getItem(this.shopItem, this.player, this.transactionType, this.getTotalAmount()));
         if (stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();
            ItemBuilder builder = new ItemBuilder(stack);
            builder.setRawDisplayName(builder.getRawDisplayName().replace("%price%", formatted));
            if (meta.hasLore()) {
               builder.setRawLore((List)builder.getRawLore().stream().map((s) -> {
                  return s.replace("%price%", formatted);
               }).collect(Collectors.toList()));
            }

            stack = builder.build();
         }

         this.setItem(item.getSlots(), stack);
      }
   }

   public void setSelectedItem() {
      TransactionItem item = TransactionMenus.getItemByType(this.transactionType, CreateItem.TransactionItemType.SELECTED_ITEM);
      if (item != null) {
         this.setItem(item.getSlots(), item.getItem(this.selectedItem, this.shopItem, this.player, this.transactionType, this.amount));
      }

   }

   private void init() {
      TransactionItem fillItem = TransactionMenus.getItemByType(this.transactionType, CreateItem.TransactionItemType.FILLITEM);
      int maxStack = EconomyShopGUI.getInstance().allowIllegalStacks ? 64 : this.shopItem.getItemToGive().getMaxStackSize();
      TransactionMenus.getItems(this.transactionType).forEach((item) -> {
         switch(item.getType()) {
         case DECREASE_AMOUNT:
            if (visualLimitAction == TransactionMenu.VisualLimitAction.NONE || !checkInitialAmount || this.amount - item.getAction().amount >= 1 && !this.isMinAmount(this.amount - item.getAction().amount)) {
               this.setItem(item.getSlots(), item.getItem(this.shopItem, this.player, this.transactionType, this.getTotalAmount()));
            } else if (visualLimitAction == TransactionMenu.VisualLimitAction.RENAME) {
               this.setItem(item.getSlots(), (new ItemBuilder(item.getItem(this.shopItem, this.player, this.transactionType, this.getTotalAmount()))).withDisplayName(Lang.MIN_AMOUNT_REACHED.get()).build());
            } else {
               this.setItem(item.getSlots(), fillItem == null ? new ItemStack(Material.AIR) : fillItem.getItem());
            }
            break;
         case INCREASE_AMOUNT:
            if (visualLimitAction != TransactionMenu.VisualLimitAction.NONE && checkInitialAmount && (this.amount + item.getAction().amount > maxStack || this.isMaxAmount(this.amount + item.getAction().amount))) {
               if (visualLimitAction == TransactionMenu.VisualLimitAction.RENAME) {
                  this.setItem(item.getSlots(), (new ItemBuilder(item.getItem(this.shopItem, this.player, this.transactionType, this.getTotalAmount()))).withDisplayName(Lang.MAX_AMOUNT_REACHED.get()).build());
               } else {
                  this.setItem(item.getSlots(), fillItem == null ? new ItemStack(Material.AIR) : fillItem.getItem());
               }
            } else {
               this.setItem(item.getSlots(), item.getItem(this.shopItem, this.player, this.transactionType, this.getTotalAmount()));
            }
            break;
         case NORMAL:
            this.setItem(item.getSlots(), item.getItem(this.shopItem, this.player, this.transactionType, this.getTotalAmount()));
         }

      });
      if (fillItem != null) {
         for(int i = 0; i < this.inv.getSize() - (EconomyShopGUI.getInstance().navBar.isEnableTransactionNav() ? 9 : 0); ++i) {
            if (this.inv.getItem(i) == null) {
               this.inv.setItem(i, fillItem.getItem());
            }
         }
      }

   }

   private boolean isMaxAmount(int amount) {
      return this.transactionMode == Transaction.Mode.BUY ? (this.transactionType == Transaction.Type.BUY_STACKS_SCREEN ? this.shopItem.isMaxBuy(amount * this.selectedItem.getMaxStackSize()) : this.shopItem.isMaxBuy(amount)) : this.shopItem.isMaxSell(amount);
   }

   private int getMaxAmount() {
      return this.transactionMode == Transaction.Mode.BUY ? (this.transactionType == Transaction.Type.BUY_STACKS_SCREEN ? this.shopItem.getMaxBuy() / this.selectedItem.getMaxStackSize() : this.shopItem.getMaxBuy()) : this.shopItem.getMaxSell();
   }

   private boolean isMinAmount(int amount) {
      return this.transactionMode == Transaction.Mode.BUY ? (this.transactionType == Transaction.Type.BUY_STACKS_SCREEN ? this.shopItem.isMinBuy(amount * this.selectedItem.getMaxStackSize()) : this.shopItem.isMinBuy(amount)) : this.shopItem.isMinSell(amount);
   }

   private int getMinAmount() {
      return this.transactionMode == Transaction.Mode.BUY ? (this.transactionType == Transaction.Type.BUY_STACKS_SCREEN ? this.shopItem.getMinBuy() / this.selectedItem.getMaxStackSize() : this.shopItem.getMinBuy()) : this.shopItem.getMinSell();
   }

   public void updateAmount(CreateItem.TransactionItemAction action) {
      int current = this.amount;
      switch(action.type) {
      case ADD:
         current += action.amount;
         break;
      case REMOVE:
         current -= action.amount;
      }

      this.updateButtons(current);
      this.setSelectedItem();
      this.setConfirmTransactionItem();
   }

   private void setItem(List<Integer> slots, ItemStack stack) {
      slots.forEach((i) -> {
         this.inv.setItem(i, stack);
      });
   }

   public void open() {
      this.setSelectedItem();
      this.setConfirmTransactionItem();
      if (this.transactionType != Transaction.Type.SHOPSTAND_BUY_SCREEN && this.transactionType != Transaction.Type.SHOPSTAND_SELL_SCREEN) {
         EconomyShopGUI.getInstance().navBar.addTransactionNavBar(this.inv, this.player, this.shopItem.getEcoType());
      } else {
         EconomyShopGUI.getInstance().navBar.addShopStandsNavBar(this.inv, this.player, this.shopItem.getEcoType());
      }

      UserManager.getUser(this.player).setOpenNewGUI(true);
      this.player.openInventory(this.inv);
   }

   public Inventory getInventory() {
      return null;
   }

   private static enum VisualLimitAction {
      NONE,
      RENAME,
      REMOVE;

      public static TransactionMenu.VisualLimitAction get() {
         try {
            return valueOf(ConfigManager.getConfig().getString("visual-transaction-limits.action").toUpperCase(Locale.ROOT));
         } catch (IllegalArgumentException var1) {
            SendMessage.logDebugMessage("Failed to load transaction visual limit for " + ConfigManager.getConfig().getString("visual-transaction-limits.action"));
            return REMOVE;
         }
      }

      // $FF: synthetic method
      private static TransactionMenu.VisualLimitAction[] $values() {
         return new TransactionMenu.VisualLimitAction[]{NONE, RENAME, REMOVE};
      }
   }
}
