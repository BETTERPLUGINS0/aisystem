package advancedplugins.pm2.cv.models.api.utils.ticker;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.utils.scheduling.PlatformScheduler;
import java.util.function.Consumer;
import org.bukkit.plugin.java.JavaPlugin;

public class DualTicker {
   private final PseudoThread sync;
   private final PseudoThread async;
   private final PseudoThread io;

   public DualTicker(JavaPlugin var1, PlatformScheduler var2) {
      this.sync = new PseudoThread("sync", var2, var1, false, 0, 0, false, false);
      this.async = new PseudoThread("async", var2, var1, true, 0, 0, false, false);
      this.io = new PseudoThread("io", var2, var1, true, 0, 0, true, true);
   }

   public static void queueSyncTask(Runnable var0) {
      queueDelayedSyncTask((Consumer)((var1) -> {
         var0.run();
      }), 0);
   }

   public static void queueDelayedSyncTask(Runnable var0, int var1) {
      queueDelayedSyncTask((var1x) -> {
         var0.run();
      }, var1);
   }

   public static void queueRepeatingSyncTask(Runnable var0, int var1, int var2) {
      queueRepeatingSyncTask((var1x) -> {
         var0.run();
      }, var1, var2);
   }

   public static void queueSyncTask(Consumer<Task> var0) {
      queueDelayedSyncTask((Consumer)var0, 0);
   }

   public static void queueDelayedSyncTask(Consumer<Task> var0, int var1) {
      DualTicker var2 = ModelAPI.getAPI().getTicker();
      var2.sync.queueTask(new Task(var0, var1, 0, false));
   }

   public static void queueRepeatingSyncTask(Consumer<Task> var0, int var1, int var2) {
      DualTicker var3 = ModelAPI.getAPI().getTicker();
      var3.sync.queueTask(new Task(var0, var1, var2, true));
   }

   public static void queueAsyncTask(Runnable var0) {
      queueDelayedAsyncTask((Consumer)((var1) -> {
         var0.run();
      }), 0);
   }

   public static void queueDelayedAsyncTask(Runnable var0, int var1) {
      queueDelayedAsyncTask((var1x) -> {
         var0.run();
      }, var1);
   }

   public static void queueRepeatingAsyncTask(Runnable var0, int var1, int var2) {
      queueRepeatingAsyncTask((var1x) -> {
         var0.run();
      }, var1, var2);
   }

   public static void queueAsyncTask(Consumer<Task> var0) {
      queueDelayedAsyncTask((Consumer)var0, 0);
   }

   public static void queueDelayedAsyncTask(Consumer<Task> var0, int var1) {
      DualTicker var2 = ModelAPI.getAPI().getTicker();
      var2.async.queueTask(new Task(var0, var1, 0, false));
   }

   public static void queueRepeatingAsyncTask(Consumer<Task> var0, int var1, int var2) {
      DualTicker var3 = ModelAPI.getAPI().getTicker();
      var3.async.queueTask(new Task(var0, var1, var2, true));
   }

   public static void queueIOTask(Runnable var0) {
      DualTicker var1 = ModelAPI.getAPI().getTicker();
      var1.io.queueTask(new Task((var1x) -> {
         var0.run();
      }, 0, 0, false));
   }

   public void start() {
      this.sync.start();
      this.async.start();
      this.io.start();
      ModelAPI.getAPI().getDataTrackers().start();
      ModelAPI.getAPI().getModelUpdaters().start();
   }

   public void stop() {
      this.sync.end();
      this.async.end();
      this.io.end();
      ModelAPI.getAPI().getDataTrackers().end();
      ModelAPI.getAPI().getModelUpdaters().end();
   }
}
