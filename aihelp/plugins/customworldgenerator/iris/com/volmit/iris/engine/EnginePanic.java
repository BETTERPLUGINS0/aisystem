package com.volmit.iris.engine;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KMap;
import java.util.Iterator;

public class EnginePanic {
   private static final KMap<String, String> stuff = new KMap();
   private static KMap<String, String> last = new KMap();

   public static void add(String key, String value) {
      stuff.put(var0, var1);
   }

   public static void saveLast() {
      last = stuff.copy();
   }

   public static void lastPanic() {
      Iterator var0 = last.keySet().iterator();

      while(var0.hasNext()) {
         String var1 = (String)var0.next();
         Iris.error("Last Panic " + var1 + ": " + (String)stuff.get(var1));
      }

   }

   public static void panic() {
      lastPanic();
      Iterator var0 = stuff.keySet().iterator();

      while(var0.hasNext()) {
         String var1 = (String)var0.next();
         Iris.error("Engine Panic " + var1 + ": " + (String)stuff.get(var1));
      }

   }
}
