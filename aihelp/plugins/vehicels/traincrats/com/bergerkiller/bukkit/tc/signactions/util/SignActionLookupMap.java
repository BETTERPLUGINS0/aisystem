package com.bergerkiller.bukkit.tc.signactions.util;

import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import java.util.Optional;
import java.util.function.Predicate;

public interface SignActionLookupMap {
   SignActionLookupMap DISABLED = new SignActionLookupMap() {
      public Optional<SignActionLookupMap.Entry> lookup(SignActionEvent event, SignActionLookupMap.LookupMode lookupMode) {
         return Optional.empty();
      }

      public <T extends SignAction> T register(T action, boolean priority) {
         return action;
      }

      public void unregister(SignAction action) {
      }
   };

   static SignActionLookupMap create() {
      return new SignActionLookupMapImpl();
   }

   static SignActionLookupMap createBasicUnoptimized() {
      return new SignActionLookupMapBasicImpl();
   }

   default Optional<SignActionLookupMap.Entry> lookup(SignActionEvent event) {
      return this.lookup(event, SignActionLookupMap.LookupMode.ALL);
   }

   Optional<SignActionLookupMap.Entry> lookup(SignActionEvent var1, SignActionLookupMap.LookupMode var2);

   default <T extends SignAction> T register(T action) {
      return this.register(action, false);
   }

   <T extends SignAction> T register(T var1, boolean var2);

   void unregister(SignAction var1);

   public static enum LookupMode implements Predicate<SignActionLookupMap.Entry> {
      ALL {
         public boolean test(SignActionLookupMap.Entry e) {
            return true;
         }
      },
      WITH_LOADED_CHANGED_HANDLER {
         public boolean test(SignActionLookupMap.Entry e) {
            return e.hasLoadedChangedHandler();
         }
      };

      private LookupMode() {
      }

      // $FF: synthetic method
      private static SignActionLookupMap.LookupMode[] $values() {
         return new SignActionLookupMap.LookupMode[]{ALL, WITH_LOADED_CHANGED_HANDLER};
      }

      // $FF: synthetic method
      LookupMode(Object x2) {
         this();
      }
   }

   public interface Entry {
      SignAction action();

      boolean hasLoadedChangedHandler();
   }
}
