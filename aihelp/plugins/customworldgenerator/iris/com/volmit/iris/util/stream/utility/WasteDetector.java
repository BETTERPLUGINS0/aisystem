package com.volmit.iris.util.stream.utility;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WasteDetector<T> extends BasicStream<T> {
   public static final boolean checking = false;
   private static final KMap<String, Integer> allAccesses = new KMap();
   private static final KMap<String, List<Throwable>> allThrows = new KMap();
   private final AtomicInteger accesses;
   private final String name;

   public WasteDetector(ProceduralStream<T> stream, String name) {
      super(var1);
      this.name = var2;
      this.accesses = new AtomicInteger(0);
   }

   public static void printAll() {
   }

   public T get(double x, double z) {
      return this.getTypedSource().get(var1, var3);
   }

   public T get(double x, double y, double z) {
      return this.getTypedSource().get(var1, var3, var5);
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   // $FF: synthetic method
   private static List lambda$get$1(String var0) {
      return new KList();
   }

   // $FF: synthetic method
   private static Integer lambda$get$0(String var0, Integer var1) {
      return var1 == null ? 1 : var1 + 1;
   }
}
