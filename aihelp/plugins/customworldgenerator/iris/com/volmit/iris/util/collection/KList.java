package com.volmit.iris.util.collection;

import com.google.common.util.concurrent.AtomicDoubleArray;
import com.volmit.iris.util.function.NastyFunction;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RNG;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class KList<T> extends ArrayList<T> implements List<T> {
   private static final long serialVersionUID = -2892550695744823337L;

   @SafeVarargs
   public KList(T... ts) {
      this.add(var1);
   }

   public KList() {
   }

   public KList(int cap) {
      super(var1);
   }

   public KList(Collection<T> values) {
      this.add(var1);
   }

   public KList(Enumeration<T> e) {
      this.add(var1);
   }

   public static KList<String> fromJSONAny(JSONArray oo) {
      KList var1 = new KList();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         var1.add((Object)var0.get(var2).toString());
      }

      return var1;
   }

   public static <T> Collector<T, ?, KList<T>> collector() {
      return Collectors.toCollection(KList::new);
   }

   public static KList<String> asStringList(List<?> oo) {
      KList var1 = new KList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         var1.add((Object)var3.toString());
      }

      return var1;
   }

   public int indexOfAddIfNeeded(T v) {
      this.addIfMissing(var1);
      return this.indexOf(var1);
   }

   public void addMultiple(T t, int c) {
      for(int var3 = 0; var3 < var2; ++var3) {
         this.add((Object)var1);
      }

   }

   private KList<T> add(Enumeration<T> e) {
      while(var1.hasMoreElements()) {
         this.add((Object)var1.nextElement());
      }

      return this;
   }

   public KList<T> add(Collection<T> values) {
      this.addAll(var1);
      return this;
   }

   public <K> KMap<K, T> asValues(Function<T, K> f) {
      KMap var2 = new KMap();
      this.forEach((var2x) -> {
         var2.putNonNull(var1.apply(var2x), var2x);
      });
      return var2;
   }

   public <V> KMap<T, V> asKeys(Function<T, V> f) {
      KMap var2 = new KMap();
      this.forEach((var2x) -> {
         var2.putNonNull(var2x, var1.apply(var2x));
      });
      return var2;
   }

   public KList<KList<T>> divide(int targetCount) {
      return this.split(this.size() / var1);
   }

   public KList<KList<T>> split(int targetSize) {
      var1 = var1 < 1 ? 1 : var1;
      KList var2 = new KList();
      KList var3 = new KList();

      Object var5;
      for(Iterator var4 = this.iterator(); var4.hasNext(); var3.add((Object)var5)) {
         var5 = var4.next();
         if (var3.size() >= var1) {
            var2.add((Object)var3.copy());
            var3.clear();
         }
      }

      if (!var3.isEmpty()) {
         var2.add((Object)var3);
      }

      return var2;
   }

   public KList<T> rewrite(Function<T, T> t) {
      KList var2 = this.copy();
      this.clear();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         this.addNonNull(var1.apply(var4));
      }

      return this;
   }

   public T[] array() {
      return this.toArray();
   }

   public KList<T> copy() {
      return (new KList()).add(this);
   }

   public KList<T> shuffle() {
      Collections.shuffle(this);
      return this;
   }

   public KList<T> shuffle(Random rng) {
      Collections.shuffle(this, var1);
      return this;
   }

   public KList<T> sort() {
      Collections.sort(this, (var0, var1) -> {
         return var0.toString().compareTo(var1.toString());
      });
      return this;
   }

   public KList<T> reverse() {
      Collections.reverse(this);
      return this;
   }

   public String toString() {
      return "[" + this.toString(", ") + "]";
   }

   public String toString(String split) {
      if (this.isEmpty()) {
         return "";
      } else if (this.size() == 1) {
         return String.valueOf(this.get(0)).makeConcatWithConstants<invokedynamic>(String.valueOf(this.get(0)));
      } else {
         StringBuilder var2 = new StringBuilder();
         Iterator var3 = this.toStringList().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.append(var1).append(var4 == null ? "null" : var4);
         }

         return var2.substring(var1.length());
      }
   }

   public KList<String> toStringList() {
      return this.convert((var0) -> {
         return String.valueOf(var0).makeConcatWithConstants<invokedynamic>(String.valueOf(var0));
      });
   }

   public <V> KList<T> addFrom(List<V> v, Function<V, T> converter) {
      var1.forEach((var2x) -> {
         this.add((Object)var2.apply(var2x));
      });
      return this;
   }

   public <V> KList<V> convert(Function<T, V> converter) {
      KList var2 = new KList();
      this.forEach((var2x) -> {
         var2.addNonNull(var1.apply(var2x));
      });
      return var2;
   }

   public <V> KList<V> convertNasty(NastyFunction<T, V> converter) {
      KList var2 = new KList(this.size());
      Iterator var3 = this.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         var2.addNonNull(var1.run(var4));
      }

      return var2;
   }

   public KList<T> removeWhere(Predicate<T> t) {
      Iterator var2 = this.copy().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if (var1.test(var3)) {
            this.remove(var3);
         }
      }

      return this;
   }

   public KList<T> addNonNull(T t) {
      if (var1 != null) {
         super.add(var1);
      }

      return this;
   }

   public KList<T> swapIndexes(int a, int b) {
      Object var3 = this.remove(var1);
      Object var4 = this.get(var2);
      this.add(var1, var4);
      this.remove(var2);
      this.add(var2, var3);
      return this;
   }

   public KList<T> remove(T... t) {
      Object[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         super.remove(var5);
      }

      return this;
   }

   public KList<T> add(KList<T> t) {
      super.addAll(var1);
      return this;
   }

   public KList<T> add(T... t) {
      Object[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         super.add(var5);
      }

      return this;
   }

   public boolean hasIndex(int index) {
      return this.size() > var1 && var1 >= 0;
   }

   public int last() {
      return this.size() - 1;
   }

   public KList<T> dedupe() {
      LinkedHashSet var1 = new LinkedHashSet(this);
      return this.qclear().add((Collection)var1);
   }

   public KList<T> qclear() {
      super.clear();
      return this;
   }

   public boolean hasElements() {
      return !this.isEmpty();
   }

   public boolean isNotEmpty() {
      return !this.isEmpty();
   }

   public T pop() {
      return this.isEmpty() ? null : this.remove(0);
   }

   public T popLast() {
      return this.isEmpty() ? null : this.remove(this.last());
   }

   public T popRandom() {
      if (this.isEmpty()) {
         return null;
      } else {
         return this.size() == 1 ? this.pop() : this.remove(M.irand(0, this.last()));
      }
   }

   public T popRandom(RNG rng) {
      if (this.isEmpty()) {
         return null;
      } else {
         return this.size() == 1 ? this.pop() : this.remove(var1.i(0, this.last()));
      }
   }

   public KList<T> sub(int f, int t) {
      KList var3 = new KList();

      for(int var4 = var1; var4 < (Integer)M.min(this.size(), var2); ++var4) {
         var3.add((Object)this.get(var4));
      }

      return var3;
   }

   public JSONArray toJSONStringArray() {
      JSONArray var1 = new JSONArray();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         var1.put((Object)var3.toString());
      }

      return var1;
   }

   public KList<T> forceAdd(Object[] values) {
      Object[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         this.add((Object)var5);
      }

      return this;
   }

   public KList<T> forceAdd(int[] values) {
      int[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Integer var5 = var2[var4];
         this.add((Object)var5);
      }

      return this;
   }

   public KList<T> forceAdd(double[] values) {
      double[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Double var5 = var2[var4];
         this.add((Object)var5);
      }

      return this;
   }

   public KList<T> forceAdd(AtomicDoubleArray values) {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         this.add((Object)var1.get(var2));
      }

      return this;
   }

   public KList<T> forceAdd(float[] values) {
      float[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Float var5 = var2[var4];
         this.add((Object)var5);
      }

      return this;
   }

   public KList<T> forceAdd(byte[] values) {
      byte[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Byte var5 = var2[var4];
         this.add((Object)var5);
      }

      return this;
   }

   public KList<T> forceAdd(short[] values) {
      short[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Short var5 = var2[var4];
         this.add((Object)var5);
      }

      return this;
   }

   public KList<T> forceAdd(long[] values) {
      long[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Long var5 = var2[var4];
         this.add((Object)var5);
      }

      return this;
   }

   public KList<T> forceAdd(boolean[] values) {
      boolean[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Boolean var5 = var2[var4];
         this.add((Object)var5);
      }

      return this;
   }

   public T middleValue() {
      return this.get(this.middleIndex());
   }

   private int middleIndex() {
      return this.size() % 2 == 0 ? this.size() / 2 : this.size() / 2 + 1;
   }

   public T getRandom() {
      if (this.isEmpty()) {
         return null;
      } else {
         return this.size() == 1 ? this.get(0) : this.get(M.irand(0, this.last()));
      }
   }

   public KList<T> popRandom(RNG rng, int c) {
      KList var3 = new KList();

      for(int var4 = 0; var4 < var2 && !this.isEmpty(); ++var4) {
         var3.add((Object)this.popRandom());
      }

      return var3;
   }

   public T getRandom(RNG rng) {
      if (this.isEmpty()) {
         return null;
      } else {
         return this.size() == 1 ? this.get(0) : this.get(var1.i(0, this.last()));
      }
   }

   public KList<T> qdel(T t) {
      this.remove(var1);
      return this;
   }

   public KList<T> qadd(T t) {
      this.add((Object)var1);
      return this;
   }

   public KList<T> qaddIfMissing(T t) {
      this.addIfMissing(var1);
      return this;
   }

   public KList<T> removeDuplicates() {
      KSet var1 = new KSet(new Object[0]);
      var1.addAll(this);
      KList var2 = new KList();
      var2.addAll(var1);
      return var2;
   }

   public boolean addIfMissing(T t) {
      if (!this.contains(var1)) {
         this.add((Object)var1);
         return true;
      } else {
         return false;
      }
   }

   public void addAllIfMissing(KList<T> t) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if (!this.contains(var3)) {
            this.add((Object)var3);
         }
      }

   }

   public KList<T> shuffleCopy(Random rng) {
      KList var2 = this.copy();
      var2.shuffle(var1);
      return var2;
   }

   public KList<T> qdrop() {
      this.pop();
      return this;
   }
}
