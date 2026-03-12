package me.SuperRonanCraft.BetterRTP.versions;

import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.lib.folialib.impl.ServerImplementation;
import me.SuperRonanCraft.BetterRTP.lib.folialib.wrapper.task.WrappedTask;
import org.bukkit.entity.Entity;

public class AsyncHandler {
   public static void async(Runnable runnable) {
      getFolia().runAsync((task) -> {
         runnable.run();
      });
   }

   public static void sync(Runnable runnable) {
      getFolia().runNextTick((task) -> {
         runnable.run();
      });
   }

   public static void syncAtEntity(Entity entity, Runnable runnable) {
      getFolia().runAtEntity(entity, (task) -> {
         runnable.run();
      });
   }

   public static WrappedTask asyncLater(Runnable runnable, long ticks) {
      return getFolia().runLaterAsync(runnable, ticks);
   }

   public static WrappedTask syncLater(Runnable runnable, long ticks) {
      return getFolia().runLater(runnable, ticks);
   }

   private static ServerImplementation getFolia() {
      return BetterRTP.getInstance().getFoliaHandler().get();
   }
}
