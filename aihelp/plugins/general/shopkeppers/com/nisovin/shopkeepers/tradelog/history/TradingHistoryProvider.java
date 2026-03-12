package com.nisovin.shopkeepers.tradelog.history;

import java.util.concurrent.CompletableFuture;

public interface TradingHistoryProvider {
   CompletableFuture<TradingHistoryResult> getTradingHistory(TradingHistoryRequest var1);
}
