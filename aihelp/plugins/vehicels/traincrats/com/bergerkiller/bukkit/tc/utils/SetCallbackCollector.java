package com.bergerkiller.bukkit.tc.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class SetCallbackCollector<T> implements Consumer<T> {
   private Set<T> buffer = Collections.emptySet();
   private Set<T> result;

   public SetCallbackCollector() {
      this.result = this.buffer;
   }

   public Set<T> result() {
      return this.result;
   }

   public void accept(T t) {
      this.acceptCheckAdded(t);
   }

   public boolean acceptCheckAdded(T t) {
      Set<T> buffer = this.buffer;
      int size = ((Set)buffer).size();
      if (size == 0) {
         this.result = this.buffer = Collections.singleton(t);
         return true;
      } else if (size == 1) {
         if (!(buffer instanceof HashSet)) {
            HashSet<T> newSet = new HashSet(16);
            newSet.addAll((Collection)buffer);
            buffer = newSet;
            this.buffer = newSet;
         }

         if (((Set)buffer).add(t)) {
            this.result = Collections.unmodifiableSet((Set)buffer);
            return true;
         } else {
            return false;
         }
      } else {
         return ((Set)buffer).add(t);
      }
   }
}
