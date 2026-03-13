package com.volmit.iris.engine.framework;

import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.nms.container.BlockPos;
import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.Spiraler;
import com.volmit.iris.util.matter.MatterCavern;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import com.volmit.iris.util.scheduling.jobs.SingleJob;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface Locator<T> {
   static void cancelSearch() {
      if (LocatorCanceller.cancel != null) {
         LocatorCanceller.cancel.run();
         LocatorCanceller.cancel = null;
      }

   }

   static Locator<IrisRegion> region(String loadKey) {
      return (e, c) -> {
         return e.getRegion((c.getX() << 4) + 8, (c.getZ() << 4) + 8).getLoadKey().equals(loadKey);
      };
   }

   static Locator<IrisJigsawStructure> jigsawStructure(String loadKey) {
      return (e, c) -> {
         IrisJigsawStructure s = e.getStructureAt(c.getX(), c.getZ());
         return s != null && s.getLoadKey().equals(loadKey);
      };
   }

   static Locator<IrisObject> object(String loadKey) {
      return (e, c) -> {
         return e.getObjectsAt(c.getX(), c.getZ()).contains(loadKey);
      };
   }

   static Locator<IrisBiome> surfaceBiome(String loadKey) {
      return (e, c) -> {
         return e.getSurfaceBiome((c.getX() << 4) + 8, (c.getZ() << 4) + 8).getLoadKey().equals(loadKey);
      };
   }

   static Locator<BlockPos> poi(String type) {
      return (e, c) -> {
         Set<Pair<String, BlockPos>> pos = e.getPOIsAt((c.getX() << 4) + 8, (c.getZ() << 4) + 8);
         return pos.stream().anyMatch((p) -> {
            return ((String)p.getA()).equals(type);
         });
      };
   }

   static Locator<IrisBiome> caveBiome(String loadKey) {
      return (e, c) -> {
         return e.getCaveBiome((c.getX() << 4) + 8, (c.getZ() << 4) + 8).getLoadKey().equals(loadKey);
      };
   }

   static Locator<IrisBiome> caveOrMantleBiome(String loadKey) {
      return (e, c) -> {
         AtomicBoolean found = new AtomicBoolean(false);
         e.generateMatter(c.getX(), c.getZ(), true, new ChunkContext(c.getX() << 4, c.getZ() << 4, e.getComplex(), false));
         e.getMantle().getMantle().iterateChunk(c.getX(), c.getZ(), MatterCavern.class, (x, y, z, t) -> {
            if (!found.get()) {
               if (t != null && t.getCustomBiome().equals(loadKey)) {
                  found.set(true);
               }

            }
         });
         return found.get();
      };
   }

   boolean matches(Engine engine, Position2 chunk);

   default void find(Player player, boolean teleport, String message) {
      this.find(player, (location) -> {
         if (teleport) {
            J.s(() -> {
               player.teleport(location);
            });
         } else {
            String var10001 = String.valueOf(C.GREEN);
            player.sendMessage(var10001 + message + " at: " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ());
         }

      });
   }

   default void find(Player player, Consumer<Location> consumer) {
      this.find(player, 30000L, consumer);
   }

   default void find(Player player, long timeout, Consumer<Location> consumer) {
      final AtomicLong checks = new AtomicLong();
      final long ms = M.ms();
      (new SingleJob(this, "Searching", () -> {
         try {
            World world = player.getWorld();
            Engine engine = IrisToolbelt.access(world).getEngine();
            Position2 var10002 = new Position2(player.getLocation().getBlockX() >> 4, player.getLocation().getBlockZ() >> 4);
            Objects.requireNonNull(checks);
            Position2 at = (Position2)this.find(engine, var10002, timeout, checks::set).get();
            if (at != null) {
               consumer.accept(new Location(world, (double)((at.getX() << 4) + 8), (double)engine.getHeight((at.getX() << 4) + 8, (at.getZ() << 4) + 8, false), (double)((at.getZ() << 4) + 8)));
            }
         } catch (InterruptedException | ExecutionException | WrongEngineBroException var9) {
            var9.printStackTrace();
         }

      }) {
         public String getName() {
            return "Searched " + Form.f(checks.get()) + " Chunks";
         }

         public int getTotalWork() {
            return (int)timeout;
         }

         public int getWorkCompleted() {
            return (int)Math.min(M.ms() - ms, timeout - 1L);
         }
      }).execute(new VolmitSender(player));
   }

   default Future<Position2> find(Engine engine, Position2 pos, long timeout, Consumer<Integer> checks) throws WrongEngineBroException {
      if (engine.isClosed()) {
         throw new WrongEngineBroException();
      } else {
         cancelSearch();
         return MultiBurst.burst.completeValue(() -> {
            int tc = IrisSettings.getThreadCount(IrisSettings.get().getConcurrency().getParallelism()) * 17;
            MultiBurst burst = MultiBurst.burst;
            AtomicBoolean found = new AtomicBoolean(false);
            AtomicInteger searched = new AtomicInteger();
            AtomicBoolean stop = new AtomicBoolean(false);
            AtomicReference<Position2> foundPos = new AtomicReference();
            PrecisionStopwatch px = PrecisionStopwatch.start();
            LocatorCanceller.cancel = () -> {
               stop.set(true);
            };
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
                     if (this.matches(engine, p)) {
                        if (foundPos.get() == null) {
                           foundPos.set(p);
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
            return found.get() && foundPos.get() != null ? (Position2)foundPos.get() : null;
         });
      }
   }
}
