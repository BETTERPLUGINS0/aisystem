package com.nisovin.shopkeepers.tradelog.history;

import com.nisovin.shopkeepers.tradelog.data.TradeRecord;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.List;

public class TradingHistoryResult {
   private final List<TradeRecord> trades;
   private final int totalTradesCount;

   public TradingHistoryResult(List<TradeRecord> trades, int totalTradesCount) {
      Validate.notNull(trades, (String)"trades is null!");
      Validate.noNullElements(trades, (String)"trades cannot contain null!");
      Validate.isTrue(totalTradesCount >= 0, "Total trades count cannot be negative!");
      this.trades = trades;
      this.totalTradesCount = totalTradesCount;
   }

   public List<TradeRecord> getTrades() {
      return this.trades;
   }

   public int getTotalTradesCount() {
      return this.totalTradesCount;
   }
}
