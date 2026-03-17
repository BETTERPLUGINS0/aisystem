package com.bergerkiller.bukkit.tc.signactions.util;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.signactions.SignActionRegisterEvent;
import com.bergerkiller.bukkit.tc.events.signactions.SignActionUnregisterEvent;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.TrainCartsSignAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

class SignActionLookupMapImpl implements SignActionLookupMap {
   private final SignActionLookupMapImpl.PriorityEntryList allEntries = new SignActionLookupMapImpl.PriorityEntryList();
   private final NavigableMap<String, List<SignActionLookupMapImpl.TrainCartsEntry>> traincartsEntries = new TreeMap();
   private final SignActionLookupMapImpl.PriorityEntryList nonTrainCartsEntries = new SignActionLookupMapImpl.PriorityEntryList();

   public Optional<SignActionLookupMap.Entry> lookup(SignActionEvent event, SignActionLookupMap.LookupMode lookupMode) {
      if (!event.getHeader().isValid()) {
         return this.lookupNonTrainCarts(event, lookupMode);
      } else {
         String signIdentifier = event.getLowerCaseSecondCleanedLine();
         if (signIdentifier.isEmpty()) {
            return this.lookupNonTrainCarts(event, lookupMode);
         } else {
            List<SignActionLookupMapImpl.TrainCartsEntry> allTypeMatchingEntries = Collections.emptyList();
            boolean allTypeMatchingEntriesModifiable = false;
            Set<java.util.Map.Entry<String, List<SignActionLookupMapImpl.TrainCartsEntry>>> orderedMatchingEntries = this.traincartsEntries.headMap(signIdentifier, true).descendingMap().entrySet();
            Iterator var7 = orderedMatchingEntries.iterator();

            while(var7.hasNext()) {
               java.util.Map.Entry<String, List<SignActionLookupMapImpl.TrainCartsEntry>> e = (java.util.Map.Entry)var7.next();
               if (!signIdentifier.startsWith((String)e.getKey())) {
                  break;
               }

               if (((List)allTypeMatchingEntries).isEmpty()) {
                  allTypeMatchingEntries = (List)e.getValue();
               } else {
                  if (!allTypeMatchingEntriesModifiable) {
                     allTypeMatchingEntriesModifiable = true;
                     allTypeMatchingEntries = new ArrayList((Collection)allTypeMatchingEntries);
                  }

                  ((List)allTypeMatchingEntries).addAll((Collection)e.getValue());
                  Collections.sort((List)allTypeMatchingEntries);
               }
            }

            if (((List)allTypeMatchingEntries).isEmpty()) {
               return this.lookupNonTrainCarts(event, lookupMode);
            } else {
               int prevNonTrainCartsIndex = 0;
               var7 = ((List)allTypeMatchingEntries).iterator();

               SignActionLookupMapImpl.TrainCartsEntry tcEntry;
               SignAction tcAction;
               do {
                  if (!var7.hasNext()) {
                     return this.lookupNonTrainCartsRange(event, lookupMode, prevNonTrainCartsIndex, this.nonTrainCartsEntries.size());
                  }

                  tcEntry = (SignActionLookupMapImpl.TrainCartsEntry)var7.next();
                  int nextNonTrainCartsIndex = tcEntry.getFirstIndexAfterOrder(this.nonTrainCartsEntries);
                  if (nextNonTrainCartsIndex > prevNonTrainCartsIndex) {
                     Optional<SignActionLookupMap.Entry> nonTCEntry = this.lookupNonTrainCartsRange(event, lookupMode, prevNonTrainCartsIndex, nextNonTrainCartsIndex);
                     if (nonTCEntry.isPresent()) {
                        return nonTCEntry;
                     }

                     prevNonTrainCartsIndex = nextNonTrainCartsIndex;
                  }

                  tcAction = tcEntry.action;
               } while(!lookupMode.test(tcEntry) || !tcAction.verify(event));

               return Optional.of(tcEntry);
            }
         }
      }
   }

   private Optional<SignActionLookupMap.Entry> lookupNonTrainCartsRange(SignActionEvent event, SignActionLookupMap.LookupMode lookupMode, int fromIndex, int toIndex) {
      for(int i = fromIndex; i < toIndex; ++i) {
         SignActionLookupMapImpl.EntryImpl nonTCEntry = this.nonTrainCartsEntries.getAt(i);
         SignAction nonTCAction = nonTCEntry.action;
         if (lookupMode.test(nonTCEntry) && nonTCAction.match(event) && nonTCAction.verify(event)) {
            return Optional.of(nonTCEntry);
         }
      }

      return Optional.empty();
   }

   private Optional<SignActionLookupMap.Entry> lookupNonTrainCarts(SignActionEvent event, SignActionLookupMap.LookupMode lookupMode) {
      Iterator var3 = this.nonTrainCartsEntries.iterator();

      SignActionLookupMapImpl.EntryImpl e;
      SignAction action;
      do {
         if (!var3.hasNext()) {
            return Optional.empty();
         }

         e = (SignActionLookupMapImpl.EntryImpl)var3.next();
         action = e.action;
      } while(!lookupMode.test(e) || !action.match(event) || !action.verify(event));

      return Optional.of(e);
   }

   public <T extends SignAction> T register(T action, boolean priority) {
      if (action == null) {
         throw new IllegalArgumentException("SignAction is null");
      } else {
         if (action instanceof TrainCartsSignAction) {
            SignActionLookupMapImpl.TrainCartsEntry entry = new SignActionLookupMapImpl.TrainCartsEntry((TrainCartsSignAction)action, priority);
            this.allEntries.add(entry);
            this.allEntries.refreshEntryOrder();
            Iterator var4 = entry.typeIdentifiers.iterator();

            while(var4.hasNext()) {
               String typeIdentifier = (String)var4.next();
               this.traincartsEntries.compute(typeIdentifier, (key, list) -> {
                  if (list == null) {
                     return Collections.singletonList(entry);
                  } else if (list.size() == 1) {
                     List<SignActionLookupMapImpl.TrainCartsEntry> newEntries = new ArrayList(2);
                     newEntries.addAll(list);
                     newEntries.add(entry);
                     Collections.sort(newEntries);
                     return newEntries;
                  } else {
                     list.add(entry);
                     Collections.sort(list);
                     return list;
                  }
               });
            }
         } else {
            SignActionLookupMapImpl.EntryImpl entry = new SignActionLookupMapImpl.EntryImpl(action, priority);
            this.allEntries.add(entry);
            this.allEntries.refreshEntryOrder();
            this.nonTrainCartsEntries.add(entry);
         }

         if (!Common.IS_TEST_MODE) {
            CommonUtil.callEvent(new SignActionRegisterEvent(action, priority));
         }

         return action;
      }
   }

   public void unregister(SignAction action) {
      SignActionLookupMapImpl.EntryImpl e = this.allEntries.remove(action);
      if (e != null) {
         this.allEntries.refreshEntryOrder();
         if (e instanceof SignActionLookupMapImpl.TrainCartsEntry) {
            SignActionLookupMapImpl.TrainCartsEntry tcEntry = (SignActionLookupMapImpl.TrainCartsEntry)e;
            Iterator var4 = tcEntry.typeIdentifiers.iterator();

            while(var4.hasNext()) {
               String typeIdentifier = (String)var4.next();
               this.traincartsEntries.computeIfPresent(typeIdentifier, (key, list) -> {
                  if (list.size() == 1) {
                     return list.get(0) == tcEntry ? null : list;
                  } else {
                     list.remove(tcEntry);
                     return list;
                  }
               });
            }
         } else {
            this.nonTrainCartsEntries.remove(e);
         }

         if (!Common.IS_TEST_MODE) {
            CommonUtil.callEvent(new SignActionUnregisterEvent(action));
         }

      }
   }

   private static class PriorityEntryList implements Iterable<SignActionLookupMapImpl.EntryImpl> {
      private final List<SignActionLookupMapImpl.EntryImpl> entries;

      private PriorityEntryList() {
         this.entries = new ArrayList();
      }

      public Iterator<SignActionLookupMapImpl.EntryImpl> iterator() {
         return this.entries.iterator();
      }

      public SignActionLookupMapImpl.EntryImpl getAt(int index) {
         return (SignActionLookupMapImpl.EntryImpl)this.entries.get(index);
      }

      public int size() {
         return this.entries.size();
      }

      public int getFirstIndexAfterOrder(int orderIndex) {
         int size = this.entries.size();

         for(int i = 0; i < size; ++i) {
            SignActionLookupMapImpl.EntryImpl e = (SignActionLookupMapImpl.EntryImpl)this.entries.get(i);
            if (e.orderIndex > orderIndex) {
               return i;
            }
         }

         return size;
      }

      public void add(SignActionLookupMapImpl.EntryImpl entry) {
         if (entry.priority) {
            this.entries.add(0, entry);
         } else {
            this.entries.add(entry);
         }

      }

      public SignActionLookupMapImpl.EntryImpl remove(SignAction action) {
         int size = this.entries.size();

         for(int i = 0; i < size; ++i) {
            SignActionLookupMapImpl.EntryImpl e = (SignActionLookupMapImpl.EntryImpl)this.entries.get(i);
            if (e.action.equals(action)) {
               this.entries.remove(i);
               return e;
            }
         }

         return null;
      }

      public void remove(SignActionLookupMapImpl.EntryImpl entry) {
         int index = entry.priority ? this.entries.indexOf(entry) : this.entries.lastIndexOf(entry);
         if (index != -1) {
            this.entries.remove(index);
         }

      }

      public void refreshEntryOrder() {
         int size = this.entries.size();

         for(int i = 0; i < size; ++i) {
            ((SignActionLookupMapImpl.EntryImpl)this.entries.get(i)).onOrderUpdated(i);
         }

      }

      // $FF: synthetic method
      PriorityEntryList(Object x0) {
         this();
      }
   }

   private static class TrainCartsEntry extends SignActionLookupMapImpl.EntryImpl {
      public final List<String> typeIdentifiers;
      private int nonTrainCartsAfterEntryIndex = -1;

      public TrainCartsEntry(TrainCartsSignAction action, boolean priority) {
         super(action, priority);
         this.typeIdentifiers = action.getTypeIdentifiers();
      }

      public int getFirstIndexAfterOrder(SignActionLookupMapImpl.PriorityEntryList nonTrainCartsEntries) {
         int index = this.nonTrainCartsAfterEntryIndex;
         if (index == -1) {
            this.nonTrainCartsAfterEntryIndex = index = nonTrainCartsEntries.getFirstIndexAfterOrder(this.orderIndex);
         }

         return index;
      }

      public void onOrderUpdated(int orderIndex) {
         super.onOrderUpdated(orderIndex);
         this.nonTrainCartsAfterEntryIndex = -1;
      }
   }

   private static class EntryImpl implements SignActionLookupMap.Entry, Comparable<SignActionLookupMapImpl.EntryImpl> {
      public final SignAction action;
      public final boolean priority;
      public final boolean hasLoadedChangedHandler;
      public int orderIndex = -1;

      public EntryImpl(SignAction action, boolean priority) {
         this.action = action;
         this.priority = priority;
         this.hasLoadedChangedHandler = CommonUtil.isMethodOverrided(SignAction.class, action.getClass(), "loadedChanged", new Class[]{SignActionEvent.class, Boolean.TYPE});
      }

      public void onOrderUpdated(int orderIndex) {
         this.orderIndex = orderIndex;
      }

      public SignAction action() {
         return this.action;
      }

      public boolean hasLoadedChangedHandler() {
         return this.hasLoadedChangedHandler;
      }

      public int compareTo(SignActionLookupMapImpl.EntryImpl entry) {
         return Integer.compare(this.orderIndex, entry.orderIndex);
      }
   }
}
