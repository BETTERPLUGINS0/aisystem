package com.nisovin.shopkeepers.tradelog.data;

import com.nisovin.shopkeepers.api.events.ShopkeeperTradeEvent;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.trading.MergedTrades;
import java.time.Instant;
import java.util.Objects;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TradeRecord {
   private final Instant timestamp;
   private final PlayerRecord player;
   private final ShopRecord shop;
   private final UnmodifiableItemStack resultItem;
   private final UnmodifiableItemStack item1;
   @Nullable
   private final UnmodifiableItemStack item2;
   private final int tradeCount;

   public static TradeRecord create(MergedTrades trades) {
      Validate.notNull(trades, (String)"trades is null");
      Instant timestamp = trades.getTimestamp();
      ShopkeeperTradeEvent tradeEvent = trades.getInitialTrade();
      PlayerRecord playerRecord = PlayerRecord.of(tradeEvent.getPlayer());
      ShopRecord shopRecord = ShopRecord.of(tradeEvent.getShopkeeper());
      UnmodifiableItemStack resultItem = trades.getResultItem();
      UnmodifiableItemStack item1 = trades.getOfferedItem1();
      UnmodifiableItemStack item2 = trades.getOfferedItem2();
      int tradeCount = trades.getTradeCount();
      return new TradeRecord(timestamp, playerRecord, shopRecord, resultItem, item1, item2, tradeCount);
   }

   public static TradeRecord create(ShopkeeperTradeEvent tradeEvent) {
      Validate.notNull(tradeEvent, (String)"tradeEvent is null");
      Instant timestamp = Instant.now();
      PlayerRecord playerRecord = PlayerRecord.of(tradeEvent.getPlayer());
      ShopRecord shopRecord = ShopRecord.of(tradeEvent.getShopkeeper());
      UnmodifiableItemStack resultItem = tradeEvent.getTradingRecipe().getResultItem();
      UnmodifiableItemStack item1 = tradeEvent.getOfferedItem1();
      UnmodifiableItemStack item2 = tradeEvent.getOfferedItem2();
      return new TradeRecord(timestamp, playerRecord, shopRecord, resultItem, item1, item2, 1);
   }

   public TradeRecord(Instant timestamp, PlayerRecord player, ShopRecord shop, UnmodifiableItemStack resultItem, UnmodifiableItemStack item1, @Nullable UnmodifiableItemStack item2, int tradeCount) {
      Validate.notNull(timestamp, (String)"timestamp is null");
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(shop, (String)"shop is null");
      Validate.notNull(resultItem, (String)"resultItem is null");
      Validate.notNull(item1, (String)"item1 is null");
      Validate.isTrue(tradeCount > 0, "tradeCount has to be positive");
      this.timestamp = timestamp;
      this.player = player;
      this.shop = shop;
      this.resultItem = resultItem;
      this.item1 = item1;
      this.item2 = item2;
      this.tradeCount = tradeCount;
   }

   public Instant getTimestamp() {
      return this.timestamp;
   }

   public PlayerRecord getPlayer() {
      return this.player;
   }

   public ShopRecord getShop() {
      return this.shop;
   }

   public UnmodifiableItemStack getResultItem() {
      return this.resultItem;
   }

   public UnmodifiableItemStack getItem1() {
      return this.item1;
   }

   @Nullable
   public UnmodifiableItemStack getItem2() {
      return this.item2;
   }

   public int getTradeCount() {
      return this.tradeCount;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("TradeRecord [timestamp=");
      builder.append(this.timestamp);
      builder.append(", player=");
      builder.append(this.player);
      builder.append(", shop=");
      builder.append(this.shop);
      builder.append(", resultItem=");
      builder.append(this.resultItem);
      builder.append(", item1=");
      builder.append(this.item1);
      builder.append(", item2=");
      builder.append(this.item2);
      builder.append(", tradeCount=");
      builder.append(this.tradeCount);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.timestamp.hashCode();
      result = 31 * result + this.player.hashCode();
      result = 31 * result + this.shop.hashCode();
      result = 31 * result + this.resultItem.hashCode();
      result = 31 * result + this.item1.hashCode();
      result = 31 * result + Objects.hashCode(this.item2);
      result = 31 * result + this.tradeCount;
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof TradeRecord)) {
         return false;
      } else {
         TradeRecord other = (TradeRecord)obj;
         if (!this.timestamp.equals(other.timestamp)) {
            return false;
         } else if (this.tradeCount != other.tradeCount) {
            return false;
         } else if (!this.player.equals(other.player)) {
            return false;
         } else if (!this.shop.equals(other.shop)) {
            return false;
         } else if (!this.resultItem.equals((Object)other.resultItem)) {
            return false;
         } else if (!this.item1.equals((Object)other.item1)) {
            return false;
         } else {
            return Objects.equals(this.item2, other.item2);
         }
      }
   }
}
