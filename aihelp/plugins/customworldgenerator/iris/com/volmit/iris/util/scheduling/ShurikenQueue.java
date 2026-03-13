package com.volmit.iris.util.scheduling;

import com.volmit.iris.util.collection.KList;

public class ShurikenQueue<T> implements Queue<T> {
   private KList<T> queue;
   private boolean randomPop;
   private boolean reversePop;

   public ShurikenQueue() {
      this.clear();
   }

   public ShurikenQueue<T> responsiveMode() {
      this.reversePop = true;
      return this;
   }

   public ShurikenQueue<T> randomMode() {
      this.randomPop = true;
      return this;
   }

   public ShurikenQueue<T> queue(T t) {
      this.queue.add((Object)var1);
      return this;
   }

   public ShurikenQueue<T> queue(KList<T> t) {
      this.queue.add(var1);
      return this;
   }

   public boolean hasNext(int amt) {
      return this.queue.size() >= var1;
   }

   public boolean hasNext() {
      return !this.queue.isEmpty();
   }

   public T next() {
      return this.reversePop ? this.queue.popLast() : (this.randomPop ? this.queue.popRandom() : this.queue.pop());
   }

   public KList<T> next(int amt) {
      KList var2 = new KList();

      for(int var3 = 0; var3 < var1 && this.hasNext(); ++var3) {
         var2.add((Object)this.next());
      }

      return var2;
   }

   public ShurikenQueue<T> clear() {
      this.queue = new KList();
      return this;
   }

   public int size() {
      return this.queue.size();
   }

   public boolean contains(T p) {
      return this.queue.contains(var1);
   }
}
