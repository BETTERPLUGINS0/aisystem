package me.gypopo.economyshopgui.util;

import me.gypopo.economyshopgui.files.Lang;

public class Transaction {
   public static enum Result {
      SUCCESS,
      SUCCESS_COMMANDS_EXECUTED,
      NOT_ALL_ITEMS_ADDED,
      NOT_ENOUGH_SPACE,
      INSUFFICIENT_FUNDS,
      NO_INVENTORY_SPACE,
      NEGATIVE_ITEM_PRICE,
      TRANSACTION_CANCELLED,
      NO_ITEMS_FOUND,
      NO_ITEM_STOCK_LEFT,
      HIGHER_LEVEL_REQUIRED,
      REACHED_SELL_LIMIT,
      NOT_ENOUGH_ITEMS;

      // $FF: synthetic method
      private static Transaction.Result[] $values() {
         return new Transaction.Result[]{SUCCESS, SUCCESS_COMMANDS_EXECUTED, NOT_ALL_ITEMS_ADDED, NOT_ENOUGH_SPACE, INSUFFICIENT_FUNDS, NO_INVENTORY_SPACE, NEGATIVE_ITEM_PRICE, TRANSACTION_CANCELLED, NO_ITEMS_FOUND, NO_ITEM_STOCK_LEFT, HIGHER_LEVEL_REQUIRED, REACHED_SELL_LIMIT, NOT_ENOUGH_ITEMS};
      }
   }

   public static enum Type {
      SELL_GUI_SCREEN(Lang.SELLGUI_SCREEN.get().getLegacy()),
      SELL_ALL_COMMAND(Lang.SELLALL_COMMAND.get().getLegacy()),
      SELL_ALL_SCREEN(Lang.SELLALL_SCREEN.get().getLegacy()),
      SELL_SCREEN(Lang.SELL_SCREEN.get().getLegacy()),
      BUY_SCREEN(Lang.BUY_SCREEN.get().getLegacy()),
      BUY_STACKS_SCREEN(Lang.BUYSTACKS_SCREEN.get().getLegacy()),
      QUICK_SELL(Lang.QUICK_SELL.get().getLegacy()),
      QUICK_BUY(Lang.QUICK_BUY.get().getLegacy()),
      SHOPSTAND_BUY_SCREEN(Lang.SHOPSTAND_BUY_SCREEN.get().getLegacy()),
      SHOPSTAND_SELL_SCREEN(Lang.SHOPSTAND_SELL_SCREEN.get().getLegacy()),
      AUTO_SELL_CHEST(Lang.AUTO_SELL_CHEST.get().getLegacy());

      private String name;

      private Type(String param3) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      public Transaction.Mode getTransactionMode() {
         return Transaction.Mode.getFromType(this);
      }

      public String getMode() {
         return Transaction.Mode.getFromType(this).getName();
      }

      // $FF: synthetic method
      private static Transaction.Type[] $values() {
         return new Transaction.Type[]{SELL_GUI_SCREEN, SELL_ALL_COMMAND, SELL_ALL_SCREEN, SELL_SCREEN, BUY_SCREEN, BUY_STACKS_SCREEN, QUICK_SELL, QUICK_BUY, SHOPSTAND_BUY_SCREEN, SHOPSTAND_SELL_SCREEN, AUTO_SELL_CHEST};
      }
   }

   public static enum Mode {
      BUY(Lang.BOUGHT.get().getLegacy()),
      SELL(Lang.SOLD.get().getLegacy());

      private String name;

      private Mode(String param3) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      public static Transaction.Mode getFromType(Transaction.Type type) {
         return type != Transaction.Type.BUY_SCREEN && type != Transaction.Type.BUY_STACKS_SCREEN && type != Transaction.Type.QUICK_BUY && type != Transaction.Type.SHOPSTAND_BUY_SCREEN ? SELL : BUY;
      }

      // $FF: synthetic method
      private static Transaction.Mode[] $values() {
         return new Transaction.Mode[]{BUY, SELL};
      }
   }
}
