package com.volmit.iris.util.scheduling;

import com.volmit.iris.util.collection.KList;

public class O<T> implements Observable<T> {
   private T t = null;
   private KList<Observer<T>> observers;

   public T get() {
      return this.t;
   }

   public O<T> set(T t) {
      this.t = var1;
      if (this.observers != null && this.observers.hasElements()) {
         this.observers.forEach((var1x) -> {
            var1x.onChanged(var1, var1);
         });
      }

      return this;
   }

   public boolean has() {
      return this.t != null;
   }

   public O<T> clearObservers() {
      this.observers.clear();
      return this;
   }

   public O<T> observe(Observer<T> t) {
      if (this.observers == null) {
         this.observers = new KList();
      }

      this.observers.add((Object)var1);
      return this;
   }
}
