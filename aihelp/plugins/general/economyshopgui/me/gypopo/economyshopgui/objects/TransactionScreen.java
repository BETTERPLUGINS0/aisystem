package me.gypopo.economyshopgui.objects;

import java.util.List;

public class TransactionScreen {
   private final List<TransactionItem> items;
   private final int size;

   public TransactionScreen(List<TransactionItem> items, int size) {
      this.items = items;
      this.size = size;
   }

   public List<TransactionItem> getItems() {
      return this.items;
   }

   public int getSize() {
      return this.size;
   }
}
