package com.nisovin.shopkeepers.api.storage;

public interface ShopkeeperStorage {
   boolean isDirty();

   default void saveIfDirty() {
      if (this.isDirty()) {
         this.saveNow();
      }

   }

   void save();

   void saveDelayed();

   void saveNow();

   void saveImmediate();

   void saveIfDirtyAndAwaitCompletion();
}
