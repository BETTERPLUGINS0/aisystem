package com.badbones69.crazyauctions.api.enums;

public enum ShopType {
   SELL("Sell"),
   BID("Bid");

   private final String name;

   private ShopType(String param3) {
      this.name = name;
   }

   public static ShopType getFromName(String name) {
      ShopType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ShopType type = var1[var3];
         if (type.getName().equalsIgnoreCase(name)) {
            return type;
         }
      }

      return null;
   }

   public String getName() {
      return this.name;
   }

   // $FF: synthetic method
   private static ShopType[] $values() {
      return new ShopType[]{SELL, BID};
   }
}
