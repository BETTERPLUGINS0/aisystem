package com.nisovin.shopkeepers.tradelog;

public enum TradeLogStorageType {
   DISABLED,
   SQLITE,
   CSV;

   // $FF: synthetic method
   private static TradeLogStorageType[] $values() {
      return new TradeLogStorageType[]{DISABLED, SQLITE, CSV};
   }
}
