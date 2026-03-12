package com.nisovin.shopkeepers.ui.trading;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.util.java.KeyValueStore;
import com.nisovin.shopkeepers.util.java.MapBasedKeyValueStore;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class TradingContext {
   private final Shopkeeper shopkeeper;
   private final InventoryClickEvent inventoryClickEvent;
   private final MerchantInventory merchantInventory;
   private final Player tradingPlayer;
   private final PlayerInventory playerInventory;
   private final KeyValueStore metadata = new MapBasedKeyValueStore();
   private int tradeCount = 0;
   @Nullable
   private Trade currentTrade = null;

   TradingContext(Shopkeeper shopkeeper, InventoryClickEvent inventoryClickEvent) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      Validate.notNull(inventoryClickEvent, (String)"inventoryClickEvent is null");

      assert inventoryClickEvent.getView().getTopInventory() instanceof MerchantInventory;

      assert inventoryClickEvent.getWhoClicked() instanceof Player;

      this.shopkeeper = shopkeeper;
      this.inventoryClickEvent = inventoryClickEvent;
      this.merchantInventory = (MerchantInventory)inventoryClickEvent.getView().getTopInventory();
      this.tradingPlayer = (Player)inventoryClickEvent.getWhoClicked();
      this.playerInventory = this.tradingPlayer.getInventory();
   }

   public Shopkeeper getShopkeeper() {
      return this.shopkeeper;
   }

   public InventoryClickEvent getInventoryClickEvent() {
      return this.inventoryClickEvent;
   }

   public MerchantInventory getMerchantInventory() {
      return this.merchantInventory;
   }

   public Player getTradingPlayer() {
      return this.tradingPlayer;
   }

   public PlayerInventory getPlayerInventory() {
      return this.playerInventory;
   }

   public KeyValueStore getMetadata() {
      return this.metadata;
   }

   public int getTradeCount() {
      return this.tradeCount;
   }

   @Nullable
   public Trade getCurrentTrade() {
      return this.currentTrade;
   }

   void startNewTrade() {
      ++this.tradeCount;
      this.currentTrade = null;
   }

   void setCurrentTrade(Trade trade) {
      Validate.notNull(trade, (String)"trade is null");
      this.currentTrade = trade;
   }
}
