package com.volmit.iris.engine.framework;

import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.Spiraler;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.apache.commons.lang3.function.TriFunction;

@FunctionalInterface
public interface ResultLocator<T> {
   static void cancelSearch() {
      if (LocatorCanceller.cancel != null) {
         LocatorCanceller.cancel.run();
         LocatorCanceller.cancel = null;
      }

   }

   static ResultLocator<IrisJigsawStructure> locateStructure(Collection<String> keys) {
      return (e, pos) -> {
         IrisJigsawStructure structure = e.getStructureAt(pos.getX(), pos.getZ());
         return structure != null && keys.contains(structure.getLoadKey()) ? structure : null;
      };
   }

   static ResultLocator<IrisObject> locateObject(Collection<String> keys) {
      return (e, pos) -> {
         Set<String> objects = e.getObjectsAt(pos.getX(), pos.getZ());
         Iterator var4 = objects.iterator();

         String object;
         do {
            if (!var4.hasNext()) {
               return null;
            }

            object = (String)var4.next();
         } while(!keys.contains(object));

         return (IrisObject)e.getData().getObjectLoader().load(object);
      };
   }

   T find(Engine e, Position2 chunkPos);

   default <R> ResultLocator<R> then(TriFunction<Engine, Position2, T, R> filter) {
      return (e, pos) -> {
         T t = this.find(e, pos);
         return t != null ? filter.apply(e, pos, t) : null;
      };
   }

   default Future<ResultLocator.Result<T>> find(Engine engine, Position2 pos, long timeout, Consumer<Integer> checks, boolean cancelable) throws WrongEngineBroException {
      if (engine.isClosed()) {
         throw new WrongEngineBroException();
      } else {
         cancelSearch();
         return MultiBurst.burst.completeValue(() -> {
            int tc = IrisSettings.getThreadCount(IrisSettings.get().getConcurrency().getParallelism()) * 17;
            MultiBurst burst = MultiBurst.burst;
            AtomicBoolean found = new AtomicBoolean(false);
            AtomicReference<ResultLocator.Result<T>> foundObj = new AtomicReference();
            AtomicInteger searched = new AtomicInteger();
            AtomicBoolean stop = new AtomicBoolean(false);
            PrecisionStopwatch px = PrecisionStopwatch.start();
            if (cancelable) {
               LocatorCanceller.cancel = () -> {
                  stop.set(true);
               };
            }

            AtomicReference<Position2> next = new AtomicReference(pos);
            Spiraler s = new Spiraler(100000, 100000, (x, z) -> {
               next.set(new Position2(x, z));
            });
            s.setOffset(pos.getX(), pos.getZ());
            s.next();

            while(!found.get() && !stop.get() && px.getMilliseconds() < (double)timeout) {
               BurstExecutor e = burst.burst(tc);

               for(int i = 0; i < tc; ++i) {
                  Position2 p = (Position2)next.get();
                  s.next();
                  e.queue(() -> {
                     T o = this.find(engine, p);
                     if (o != null) {
                        if (foundObj.get() == null) {
                           foundObj.set(new ResultLocator.Result(o, p));
                        }

                        found.set(true);
                     }

                     searched.incrementAndGet();
                  });
               }

               e.complete();
               checks.accept(searched.get());
            }

            LocatorCanceller.cancel = null;
            return found.get() && foundObj.get() != null ? (ResultLocator.Result)foundObj.get() : null;
         });
      }
   }

   public static record Result<T>(T obj, Position2 pos) {
      public Result(T obj, Position2 pos) {
         this.obj = var1;
         this.pos = var2;
      }

      public int getBlockX() {
         return (this.pos.getX() << 4) + 8;
      }

      public int getBlockZ() {
         return (this.pos.getZ() << 4) + 8;
      }

      public T obj() {
         return this.obj;
      }

      public Position2 pos() {
         return this.pos;
      }
   }
}
