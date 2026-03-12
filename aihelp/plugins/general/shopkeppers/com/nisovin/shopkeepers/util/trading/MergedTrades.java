package com.nisovin.shopkeepers.util.trading;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.time.Instant;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

public class MergedTrades {
   private final ShopkeeperTradeEvent initialTrade;
   private final Instant timestamp = Instant.now();
   private int tradeCount = 1;

   public MergedTrades(ShopkeeperTradeEvent initialTrade) {
      Validate.notNull(initialTrade, (String)"initialTrade is null");
      this.initialTrade = initialTrade;
   }

   public ShopkeeperTradeEvent getInitialTrade() {
      return this.initialTrade;
   }

   public Instant getTimestamp() {
      return this.timestamp;
   }

   public UnmodifiableItemStack getResultItem() {
      return this.initialTrade.getTradingRecipe().getResultItem();
   }

   public UnmodifiableItemStack getOfferedItem1() {
      return this.initialTrade.getOfferedItem1();
   }

   @Nullable
   public UnmodifiableItemStack getOfferedItem2() {
      return this.initialTrade.getOfferedItem2();
   }

   public int getTradeCount() {
      return this.tradeCount;
   }

   public void addTrades(int tradesToAdd) {
      Validate.isTrue(tradesToAdd > 0, "tradesToAdd has to be positive");
      this.tradeCount += tradesToAdd;
   }

   public boolean canMerge(ShopkeeperTradeEvent tradeEvent, boolean requireSameClickEvent) {
      Validate.notNull(tradeEvent, (String)"tradeEvent is null");
      if (this.initialTrade.getClickEvent() != tradeEvent.getClickEvent()) {
         if (requireSameClickEvent) {
            return false;
         }

         if (this.initialTrade.getPlayer() != tradeEvent.getPlayer()) {
            return false;
         }

         if (this.initialTrade.getShopkeeper() != tradeEvent.getShopkeeper()) {
            return false;
         }

         if (!Objects.equals(this.getResultItem(), tradeEvent.getTradingRecipe().getResultItem())) {
            return false;
         }

         if (!Objects.equals(this.getOfferedItem1(), tradeEvent.getOfferedItem1())) {
            return false;
         }

         if (!Objects.equals(this.getOfferedItem2(), tradeEvent.getOfferedItem2())) {
            return false;
         }
      } else {
         int resultItemAmount = this.getResultItem().getAmount();
         int otherResultItemAmount = tradeEvent.getTradingRecipe().getResultItem().getAmount();
         if (resultItemAmount != otherResultItemAmount) {
            return false;
         }

         int offeredItem1Amount = this.getOfferedItem1().getAmount();
         int otherOfferedItem1Amount = tradeEvent.getOfferedItem1().getAmount();
         if (offeredItem1Amount != otherOfferedItem1Amount) {
            return false;
         }

         int offeredItem2Amount = ItemUtils.getItemStackAmount(this.getOfferedItem2());
         int otherOfferedItem2Amount = ItemUtils.getItemStackAmount(tradeEvent.getOfferedItem2());
         if (offeredItem2Amount != otherOfferedItem2Amount) {
            return false;
         }
      }

      return true;
   }
}
