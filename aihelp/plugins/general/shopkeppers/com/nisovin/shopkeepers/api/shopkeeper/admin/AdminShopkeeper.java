package com.nisovin.shopkeepers.api.shopkeeper.admin;

import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface AdminShopkeeper extends Shopkeeper {
   @Nullable
   String getTradePermission();

   void setTradePermission(@Nullable String var1);
}
