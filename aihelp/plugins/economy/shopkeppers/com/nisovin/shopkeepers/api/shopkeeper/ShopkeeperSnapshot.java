package com.nisovin.shopkeepers.api.shopkeeper;

import com.nisovin.shopkeepers.api.internal.ApiInternals;
import java.time.Instant;

public interface ShopkeeperSnapshot {
   static int getMaxNameLength() {
      return ApiInternals.getInstance().getShopkeeperSnapshotMaxNameLength();
   }

   static boolean isNameValid(String name) {
      return ApiInternals.getInstance().isShopkeeperSnapshotNameValid(name);
   }

   String getName();

   Instant getTimestamp();
}
