package com.nisovin.shopkeepers.ui.trading;

import com.nisovin.shopkeepers.api.ui.UISession;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.TradeSelectEvent;

public abstract class TradingListener {
   public void onInventoryClick(UISession uiSession, InventoryClickEvent event) {
   }

   public void onTradeSelect(UISession uiSession, TradeSelectEvent event) {
   }

   public void onTradeCompleted(Trade trade, boolean silent) {
   }

   public void onTradeAborted(TradingContext tradingContext, boolean silent) {
   }
}
