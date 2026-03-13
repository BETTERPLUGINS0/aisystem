package com.volmit.iris.util.context;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.IrisComplex;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.mantle.EngineMantle;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.scheduling.ChronoLatch;
import java.util.Iterator;
import java.util.Map.Entry;
import lombok.Generated;

public class IrisContext {
   private static final KMap<Thread, IrisContext> context = new KMap();
   private static final ChronoLatch cl = new ChronoLatch(60000L);
   private final Engine engine;
   private ChunkContext chunkContext;

   public IrisContext(Engine engine) {
      this.engine = var1;
   }

   public static IrisContext getOr(Engine engine) {
      IrisContext var1 = get();
      if (var1 == null) {
         var1 = new IrisContext(var0);
         touch(var1);
      }

      return var1;
   }

   public static IrisContext get() {
      return (IrisContext)context.get(Thread.currentThread());
   }

   public static void touch(IrisContext c) {
      context.put(Thread.currentThread(), var0);
      if (cl.couldFlip()) {
         synchronized(cl) {
            if (cl.flip()) {
               dereference();
            }

         }
      }
   }

   public static synchronized void dereference() {
      Iterator var0 = context.entrySet().iterator();

      while(true) {
         Thread var2;
         IrisContext var3;
         label26:
         do {
            while(var0.hasNext()) {
               Entry var1 = (Entry)var0.next();
               var2 = (Thread)var1.getKey();
               var3 = (IrisContext)var1.getValue();
               if (var2 != null && var3 != null) {
                  continue label26;
               }

               var0.remove();
            }

            return;
         } while(var2.isAlive() && !var3.engine.isClosed());

         String var10000 = var2.getName();
         Iris.debug("Dereferenced Context<Engine> " + var10000 + " " + var2.threadId());
         var0.remove();
      }
   }

   public void touch() {
      touch(this);
   }

   public IrisData getData() {
      return this.engine.getData();
   }

   public IrisComplex getComplex() {
      return this.engine.getComplex();
   }

   public KMap<String, Object> asContext() {
      Long var1 = (Long)this.engine.getHash32().getNow((Object)null);
      IrisDimension var2 = this.engine.getDimension();
      EngineMantle var3 = this.engine.getMantle();
      return (new KMap()).qput("studio", this.engine.isStudio()).qput("closed", this.engine.isClosed()).qput("pack", (new KMap()).qput("key", var2 == null ? "" : var2.getLoadKey()).qput("version", var2 == null ? "" : var2.getVersion()).qput("hash", var1 == null ? "" : Long.toHexString(var1))).qput("mantle", (new KMap()).qput("idle", var3.getAdjustedIdleDuration()).qput("loaded", var3.getLoadedRegionCount()).qput("queued", var3.getUnloadRegionCount()));
   }

   @Generated
   public Engine getEngine() {
      return this.engine;
   }

   @Generated
   public ChunkContext getChunkContext() {
      return this.chunkContext;
   }

   @Generated
   public void setChunkContext(final ChunkContext chunkContext) {
      this.chunkContext = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisContext)) {
         return false;
      } else {
         IrisContext var2 = (IrisContext)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            Engine var3 = this.getEngine();
            Engine var4 = var2.getEngine();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            ChunkContext var5 = this.getChunkContext();
            ChunkContext var6 = var2.getChunkContext();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisContext;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      Engine var3 = this.getEngine();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      ChunkContext var4 = this.getChunkContext();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getEngine());
      return "IrisContext(engine=" + var10000 + ", chunkContext=" + String.valueOf(this.getChunkContext()) + ")";
   }
}
