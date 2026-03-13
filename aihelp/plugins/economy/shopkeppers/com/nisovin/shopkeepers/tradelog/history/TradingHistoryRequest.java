package com.nisovin.shopkeepers.tradelog.history;

import com.nisovin.shopkeepers.util.java.Range;
import com.nisovin.shopkeepers.util.java.Validate;

public class TradingHistoryRequest {
   public final PlayerSelector playerSelector;
   public final ShopSelector shopSelector;
   public final Range range;

   public TradingHistoryRequest(PlayerSelector playerSelector, ShopSelector shopSelector, Range range) {
      Validate.notNull(playerSelector, (String)"playerSelector is null");
      Validate.notNull(shopSelector, (String)"shopSelector is null");
      Validate.notNull(range, (String)"range is null");
      this.playerSelector = playerSelector;
      this.shopSelector = shopSelector;
      this.range = range;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("TradingHistoryRequest [playerSelector=");
      builder.append(this.playerSelector);
      builder.append(", shopSelector=");
      builder.append(this.shopSelector);
      builder.append(", range=");
      builder.append(this.range);
      builder.append("]");
      return builder.toString();
   }
}
