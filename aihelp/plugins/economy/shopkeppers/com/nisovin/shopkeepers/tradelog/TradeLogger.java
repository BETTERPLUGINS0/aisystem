package com.nisovin.shopkeepers.tradelog;

import com.nisovin.shopkeepers.tradelog.data.TradeRecord;

public interface TradeLogger {
   void setup();

   void logTrade(TradeRecord var1);

   void flush();
}
