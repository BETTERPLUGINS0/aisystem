package com.volmit.iris.core.service;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.framework.MeteredCache;
import com.volmit.iris.util.context.IrisContext;
import com.volmit.iris.util.data.KCache;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.plugin.IrisService;
import com.volmit.iris.util.scheduling.Looper;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.Unmodifiable;

public class PreservationSVC implements IrisService {
   private final List<Thread> threads = new CopyOnWriteArrayList();
   private final List<ExecutorService> services = new CopyOnWriteArrayList();
   private final List<WeakReference<MeteredCache>> caches = new CopyOnWriteArrayList();
   private Looper dereferencer;

   public void register(Thread t) {
      this.threads.add(var1);
   }

   public void register(ExecutorService service) {
      this.services.add(var1);
   }

   public void printCaches() {
      List var1 = this.getCaches();
      long var2 = 0L;
      long var4 = 0L;
      double var6 = 0.0D;
      double var8 = (double)Math.max(var1.size(), 1);

      MeteredCache var11;
      for(Iterator var10 = var1.iterator(); var10.hasNext(); var6 += var11.getUsage()) {
         var11 = (MeteredCache)var10.next();
         var2 += var11.getSize();
         var4 += var11.getMaxSize();
      }

      String var10000 = Form.f(var2);
      Iris.info("Cached " + var10000 + " / " + Form.f(var4) + " (" + Form.pc(var6 / var8) + ") from " + this.caches.size() + " Caches");
   }

   public void dereference() {
      IrisContext.dereference();
      IrisData.dereference();
      this.threads.removeIf((var0) -> {
         return !var0.isAlive();
      });
      this.services.removeIf(ExecutorService::isShutdown);
      this.updateCaches();
   }

   public void onEnable() {
      this.dereferencer = new Looper() {
         protected long loop() {
            PreservationSVC.this.dereference();
            return 60000L;
         }
      };
   }

   public void onDisable() {
      this.dereferencer.interrupt();
      this.dereference();
      this.postShutdown(() -> {
         Iterator var1 = this.threads.iterator();

         while(var1.hasNext()) {
            Thread var2 = (Thread)var1.next();
            if (var2.isAlive()) {
               try {
                  var2.interrupt();
                  Iris.info("Shutdown Thread " + var2.getName());
               } catch (Throwable var5) {
                  Iris.reportError(var5);
               }
            }
         }

         var1 = this.services.iterator();

         while(var1.hasNext()) {
            ExecutorService var6 = (ExecutorService)var1.next();

            try {
               var6.shutdownNow();
               Iris.info("Shutdown Executor Service " + String.valueOf(var6));
            } catch (Throwable var4) {
               Iris.reportError(var4);
            }
         }

      });
   }

   public void updateCaches() {
      this.caches.removeIf((var0) -> {
         MeteredCache var1 = (MeteredCache)var0.get();
         return var1 == null || var1.isClosed();
      });
   }

   public void registerCache(MeteredCache cache) {
      this.caches.add(new WeakReference(var1));
   }

   public List<KCache<?, ?>> caches() {
      return (List)this.cacheStream().map(MeteredCache::getRawCache).collect(Collectors.toList());
   }

   @Unmodifiable
   public List<MeteredCache> getCaches() {
      return this.cacheStream().toList();
   }

   private Stream<MeteredCache> cacheStream() {
      return this.caches.stream().map(Reference::get).filter(Objects::nonNull).filter((var0) -> {
         return !var0.isClosed();
      });
   }
}
