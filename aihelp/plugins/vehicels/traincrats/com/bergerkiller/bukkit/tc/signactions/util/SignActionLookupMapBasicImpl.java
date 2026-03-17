package com.bergerkiller.bukkit.tc.signactions.util;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.signactions.SignActionRegisterEvent;
import com.bergerkiller.bukkit.tc.events.signactions.SignActionUnregisterEvent;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

class SignActionLookupMapBasicImpl implements SignActionLookupMap {
   private final List<SignActionLookupMapBasicImpl.SimpleEntry> entries = new ArrayList();

   public Optional<SignActionLookupMap.Entry> lookup(SignActionEvent event, SignActionLookupMap.LookupMode lookupMode) {
      Iterator var3 = this.entries.iterator();

      SignActionLookupMapBasicImpl.SimpleEntry entry;
      SignAction action;
      do {
         if (!var3.hasNext()) {
            return Optional.empty();
         }

         entry = (SignActionLookupMapBasicImpl.SimpleEntry)var3.next();
         action = entry.action;
      } while(!lookupMode.test(entry) || !action.match(event) || !action.verify(event));

      return Optional.of(entry);
   }

   public <T extends SignAction> T register(T action, boolean priority) {
      if (priority) {
         this.entries.add(0, new SignActionLookupMapBasicImpl.SimpleEntry(action));
      } else {
         this.entries.add(new SignActionLookupMapBasicImpl.SimpleEntry(action));
      }

      if (!Common.IS_TEST_MODE) {
         CommonUtil.callEvent(new SignActionRegisterEvent(action, priority));
      }

      return action;
   }

   public void unregister(SignAction action) {
      Iterator iter = this.entries.iterator();

      SignActionLookupMapBasicImpl.SimpleEntry entry;
      do {
         if (!iter.hasNext()) {
            return;
         }

         entry = (SignActionLookupMapBasicImpl.SimpleEntry)iter.next();
      } while(!entry.action.equals(action));

      iter.remove();
      if (!Common.IS_TEST_MODE) {
         CommonUtil.callEvent(new SignActionUnregisterEvent(action));
      }

   }

   private static class SimpleEntry implements SignActionLookupMap.Entry {
      public final SignAction action;
      public final boolean hasLoadedChangedHandler;

      public SimpleEntry(SignAction action) {
         this.action = action;
         this.hasLoadedChangedHandler = CommonUtil.isMethodOverrided(SignAction.class, action.getClass(), "loadedChanged", new Class[]{SignActionEvent.class, Boolean.TYPE});
      }

      public SignAction action() {
         return this.action;
      }

      public boolean hasLoadedChangedHandler() {
         return this.hasLoadedChangedHandler;
      }
   }
}
