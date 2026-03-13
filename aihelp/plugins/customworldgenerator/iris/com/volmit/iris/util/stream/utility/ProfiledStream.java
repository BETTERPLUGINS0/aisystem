package com.volmit.iris.util.stream.utility;

import com.volmit.iris.Iris;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.math.RollingSequence;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import com.volmit.iris.util.stream.BasicStream;
import com.volmit.iris.util.stream.ProceduralStream;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.Generated;

public class ProfiledStream<T> extends BasicStream<T> {
   public static final AtomicInteger ids = new AtomicInteger();
   public static final KList<ProfiledStream<?>> profiles = new KList();
   private final int id;
   private final RollingSequence metrics;

   public ProfiledStream(ProceduralStream<T> stream, int memory) {
      super(var1);
      this.metrics = new RollingSequence(var2);
      this.id = ids.getAndAdd(1);
      profiles.add((Object)this);
   }

   public static void print(Consumer<String> printer, ProceduralStream<?> stream) {
      KList var2 = getTails(var1);
      int var3 = var2.size();

      for(Iterator var4 = var2.iterator(); var4.hasNext(); --var3) {
         ProfiledStream.ProfiledTail var5 = (ProfiledStream.ProfiledTail)var4.next();
         String var10001 = Form.repeat("  ", var3);
         var0.accept(var10001 + String.valueOf(var5));
      }

   }

   private static KList<ProceduralStream<?>> getAllChildren(ProceduralStream<?> s) {
      KList var1 = new KList();
      ProceduralStream var2 = var0;

      for(int var3 = 0; var3 < 32; ++var3) {
         var1.add((Object)var2);
         var2 = nextChuld(var2);
         if (var2 == null) {
            break;
         }
      }

      return var1;
   }

   private static ProceduralStream<?> nextChuld(ProceduralStream<?> s) {
      ProceduralStream var1 = var0.getTypedSource();
      return var1 == null ? var0.getSource() : var1;
   }

   private static ProfiledStream.ProfiledTail getTail(ProceduralStream<?> t) {
      if (var0 instanceof ProfiledStream) {
         ProfiledStream var1 = (ProfiledStream)var0;
         return new ProfiledStream.ProfiledTail(var1.getId(), var1.getMetrics(), var1.getClass().getSimpleName().replaceAll("\\QStream\\E", ""));
      } else {
         return null;
      }
   }

   private static KList<ProfiledStream.ProfiledTail> getTails(ProceduralStream<?> t) {
      KList var1 = new KList();
      Iterator var2 = getAllChildren(var0).iterator();

      ProfiledStream.ProfiledTail var4;
      while(var2.hasNext()) {
         ProceduralStream var3 = (ProceduralStream)var2.next();
         var4 = getTail(var3);
         if (var4 != null) {
            var1.add((Object)var4);
         }
      }

      if (var1.isEmpty()) {
         return null;
      } else {
         ProfiledStream.ProfiledTail var5 = (ProfiledStream.ProfiledTail)var1.popLast();
         KList var6 = new KList();
         var6.add((Object)var5);

         while(var1.isNotEmpty()) {
            var6.add((Object)var5);
            var4 = (ProfiledStream.ProfiledTail)var1.popLast();
            var4.setChild(var5);
            var5 = var4;
            var6.add((Object)var4);
         }

         return var6;
      }
   }

   public int getId() {
      return this.id;
   }

   public double toDouble(T t) {
      return this.getTypedSource().toDouble(var1);
   }

   public T fromDouble(double d) {
      return this.getTypedSource().fromDouble(var1);
   }

   public T get(double x, double z) {
      PrecisionStopwatch var5 = PrecisionStopwatch.start();
      Object var6 = this.getTypedSource().get(var1, var3);

      try {
         this.metrics.put(var5.getMilliseconds());
      } catch (Throwable var8) {
         Iris.reportError(var8);
      }

      return var6;
   }

   public T get(double x, double y, double z) {
      PrecisionStopwatch var7 = PrecisionStopwatch.start();
      Object var8 = this.getTypedSource().get(var1, var3, var5);

      try {
         this.metrics.put(var7.getMilliseconds());
      } catch (Throwable var10) {
         Iris.reportError(var10);
      }

      return var8;
   }

   public RollingSequence getMetrics() {
      return this.metrics;
   }

   private static class ProfiledTail {
      private final int id;
      private final RollingSequence metrics;
      private final String name;
      private ProfiledStream.ProfiledTail child;

      public ProfiledTail(int id, RollingSequence metrics, String name) {
         this.id = var1;
         this.metrics = var2;
         this.name = var3;
      }

      public String toString() {
         int var10000 = this.id;
         return var10000 + "-" + this.name + ": " + Form.duration(this.metrics.getAverage(), 2);
      }

      @Generated
      public int getId() {
         return this.id;
      }

      @Generated
      public RollingSequence getMetrics() {
         return this.metrics;
      }

      @Generated
      public String getName() {
         return this.name;
      }

      @Generated
      public ProfiledStream.ProfiledTail getChild() {
         return this.child;
      }

      @Generated
      public void setChild(final ProfiledStream.ProfiledTail child) {
         this.child = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof ProfiledStream.ProfiledTail)) {
            return false;
         } else {
            ProfiledStream.ProfiledTail var2 = (ProfiledStream.ProfiledTail)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else if (this.getId() != var2.getId()) {
               return false;
            } else {
               label49: {
                  RollingSequence var3 = this.getMetrics();
                  RollingSequence var4 = var2.getMetrics();
                  if (var3 == null) {
                     if (var4 == null) {
                        break label49;
                     }
                  } else if (var3.equals(var4)) {
                     break label49;
                  }

                  return false;
               }

               String var5 = this.getName();
               String var6 = var2.getName();
               if (var5 == null) {
                  if (var6 != null) {
                     return false;
                  }
               } else if (!var5.equals(var6)) {
                  return false;
               }

               ProfiledStream.ProfiledTail var7 = this.getChild();
               ProfiledStream.ProfiledTail var8 = var2.getChild();
               if (var7 == null) {
                  if (var8 != null) {
                     return false;
                  }
               } else if (!var7.equals(var8)) {
                  return false;
               }

               return true;
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof ProfiledStream.ProfiledTail;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var6 = var2 * 59 + this.getId();
         RollingSequence var3 = this.getMetrics();
         var6 = var6 * 59 + (var3 == null ? 43 : var3.hashCode());
         String var4 = this.getName();
         var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
         ProfiledStream.ProfiledTail var5 = this.getChild();
         var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
         return var6;
      }
   }
}
