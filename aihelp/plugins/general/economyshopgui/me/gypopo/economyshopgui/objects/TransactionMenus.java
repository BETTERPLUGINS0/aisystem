package me.gypopo.economyshopgui.objects;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.methodes.CreateItem;
import me.gypopo.economyshopgui.util.Transaction;

public class TransactionMenus {
   private static TransactionScreen buyScreen;
   private static TransactionScreen sellScreen;
   private static TransactionScreen buyStacksScreen;
   private static TransactionScreen shopStandBuy;
   private static TransactionScreen shopStandSell;

   public static void load(EconomyShopGUI plugin) {
      buyScreen = plugin.createItem.getTransactionScreen(Transaction.Type.BUY_SCREEN);
      sellScreen = plugin.createItem.getTransactionScreen(Transaction.Type.SELL_SCREEN);
      buyStacksScreen = plugin.createItem.getTransactionScreen(Transaction.Type.BUY_STACKS_SCREEN);
      if (plugin.shopStands) {
         shopStandBuy = plugin.createItem.getTransactionScreen(Transaction.Type.SHOPSTAND_BUY_SCREEN);
         shopStandSell = plugin.createItem.getTransactionScreen(Transaction.Type.SHOPSTAND_SELL_SCREEN);
      }

   }

   public static List<TransactionItem> getItems(Transaction.Type type) {
      switch(type) {
      case BUY_STACKS_SCREEN:
         return buyStacksScreen.getItems();
      case BUY_SCREEN:
         return buyScreen.getItems();
      case SELL_SCREEN:
         return sellScreen.getItems();
      case SHOPSTAND_BUY_SCREEN:
         return shopStandBuy.getItems();
      case SHOPSTAND_SELL_SCREEN:
         return shopStandSell.getItems();
      default:
         return null;
      }
   }

   public static CreateItem.TransactionItemAction getActionFromSlot(Transaction.Type type, int slot) {
      try {
         TransactionItem item = (TransactionItem)getItems(type).stream().filter((i) -> {
            return i.getSlots().contains(slot) && i.getAction() != CreateItem.TransactionItemAction.NONE;
         }).findFirst().orElse((Object)null);
         if (item != null) {
            return item.getAction();
         }
      } catch (NullPointerException var3) {
      }

      return CreateItem.TransactionItemAction.NONE;
   }

   public static TransactionItem getItemFromSlot(Transaction.Type type, int slot) {
      return (TransactionItem)getItems(type).stream().filter((item) -> {
         return item.getSlots().contains(slot);
      }).findFirst().orElse((Object)null);
   }

   public static TransactionItem getItemByType(Transaction.Type type, CreateItem.TransactionItemType itemType) {
      return (TransactionItem)getItems(type).stream().filter((i) -> {
         return i.getType() == itemType;
      }).findFirst().orElse((Object)null);
   }

   public static List<TransactionItem> getItemsByType(Transaction.Type type, CreateItem.TransactionItemType itemType) {
      return (List)getItems(type).stream().filter((i) -> {
         return i.getType() == itemType;
      }).collect(Collectors.toList());
   }

   public static List<TransactionItem> getItemsByAction(Transaction.Type type, CreateItem.TransactionItemAction... actions) {
      return (List)getItems(type).stream().filter((i) -> {
         return Arrays.stream(actions).anyMatch((a) -> {
            return i.getAction() == a;
         });
      }).collect(Collectors.toList());
   }

   public static int getSize(Transaction.Type type) {
      switch(type) {
      case BUY_STACKS_SCREEN:
         return buyStacksScreen.getSize();
      case BUY_SCREEN:
         return buyScreen.getSize();
      case SELL_SCREEN:
         return sellScreen.getSize();
      case SHOPSTAND_BUY_SCREEN:
         return shopStandBuy.getSize();
      case SHOPSTAND_SELL_SCREEN:
         return shopStandSell.getSize();
      default:
         return 0;
      }
   }
}
