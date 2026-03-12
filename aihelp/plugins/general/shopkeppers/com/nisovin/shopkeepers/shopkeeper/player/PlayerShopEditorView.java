package com.nisovin.shopkeepers.shopkeeper.player;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.currency.Currency;
import com.nisovin.shopkeepers.ui.editor.ShopkeeperEditorView;
import com.nisovin.shopkeepers.ui.lib.UIState;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class PlayerShopEditorView extends ShopkeeperEditorView {
   protected PlayerShopEditorView(PlayerShopEditorViewProvider viewProvider, Player player, UIState uiState) {
      super(viewProvider, player, uiState);
   }

   protected void onInventoryDragEarly(InventoryDragEvent event) {
      event.setCancelled(true);
      super.onInventoryDragEarly(event);
   }

   protected void onInventoryClickEarly(InventoryClickEvent event) {
      event.setCancelled(true);
      super.onInventoryClickEarly(event);
   }

   @Nullable
   protected ItemStack updateItemAmountOnClick(InventoryClickEvent event, int minAmount, @Nullable UnmodifiableItemStack emptySlotItem) {
      Validate.isTrue(minAmount >= 0, "minAmount cannot be negative");

      assert event.isCancelled();

      ItemStack clickedItem = event.getCurrentItem();
      if (!ItemUtils.isEmpty(clickedItem) && !ItemUtils.equals(emptySlotItem, clickedItem)) {
         clickedItem = (ItemStack)Unsafe.assertNonNull(clickedItem);
         int currentItemAmount = clickedItem.getAmount();
         int newItemAmount = this.getNewAmountAfterEditorClick(event, currentItemAmount, minAmount, clickedItem.getMaxStackSize());

         assert newItemAmount >= minAmount;

         assert newItemAmount <= clickedItem.getMaxStackSize();

         if (newItemAmount == 0) {
            event.setCurrentItem(ItemUtils.asItemStackOrNull(emptySlotItem));
            return null;
         } else {
            clickedItem.setAmount(newItemAmount);
            return clickedItem;
         }
      } else {
         return null;
      }
   }

   protected void updateTradeCostItemOnClick(InventoryClickEvent event, @Nullable Currency currency, @Nullable UnmodifiableItemStack emptySlotItem) {
      assert event != null;

      assert event.isCancelled();

      if (currency != null) {
         ItemStack clickedItem = event.getCurrentItem();
         int currentItemAmount = 0;
         boolean isCurrencyItem = currency.getItemData().matches(clickedItem);
         if (isCurrencyItem) {
            assert clickedItem != null;

            currentItemAmount = clickedItem.getAmount();
         }

         int newItemAmount = this.getNewAmountAfterEditorClick(event, currentItemAmount, 0, currency.getMaxStackSize());

         assert newItemAmount >= 0;

         assert newItemAmount <= currency.getMaxStackSize();

         if (newItemAmount == 0) {
            event.setCurrentItem(ItemUtils.asItemStackOrNull(emptySlotItem));
         } else if (isCurrencyItem) {
            assert clickedItem != null;

            clickedItem.setAmount(newItemAmount);
         } else {
            ItemStack currencyItem = currency.getItemData().createItemStack(newItemAmount);
            event.setCurrentItem(currencyItem);
         }

      }
   }
}
