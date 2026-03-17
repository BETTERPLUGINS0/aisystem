package advancedplugins.pm2.cv.models.api.utils.ticker;

import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import advancedplugins.pm2.cv.models.api.utils.scheduling.PlatformScheduler;
import advancedplugins.pm2.cv.models.api.utils.scheduling.PlatformTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import org.bukkit.plugin.java.JavaPlugin;

public class PseudoThread {
   private final String name;
   private final PlatformScheduler scheduler;
   private final JavaPlugin plugin;
   private final boolean isAsync;
   private final int delay;
   private final int period;
   private final boolean canWait;
   private final boolean canMultiTick;
   private final Queue<Task> taskQueue = new ConcurrentLinkedQueue();
   private final List<Task> tasks = new ArrayList();
   private final List<Consumer<Integer>> overloadCallback = new ArrayList();
   private PlatformTask tickTask;
   private boolean locked;
   private int skipped;
   private long lastTick;

   public PseudoThread(String var1, PlatformScheduler var2, JavaPlugin var3, boolean var4, int var5, int var6, boolean var7, boolean var8) {
      this.name = var1;
      this.scheduler = var2;
      this.plugin = var3;
      this.isAsync = var4;
      this.delay = var5;
      this.period = var6;
      this.canWait = var7;
      this.canMultiTick = var8;
   }

   public void start() {
      if (this.isAsync) {
         this.tickTask = this.scheduler.scheduleRepeatingAsync(this.plugin, this::tick, (long)this.delay, (long)this.period);
      } else {
         this.tickTask = this.scheduler.scheduleRepeating(this.plugin, this::tick, (long)this.delay, (long)this.period);
      }

   }

   public void end() {
      this.taskQueue.clear();
      this.tasks.clear();
      if (this.tickTask != null) {
         this.tickTask.cancel();
      }

   }

   public void queueTask(Task var1) {
      this.taskQueue.add(var1);
   }

   public void registerOverloadCallback(Consumer<Integer> var1) {
      this.overloadCallback.add(var1);
   }

   private void tick() {
      long var1 = System.currentTimeMillis();
      if (this.canMultiTick || var1 - this.lastTick >= 45L) {
         this.lastTick = var1;
         if (this.locked) {
            ++this.skipped;
         } else {
            this.locked = true;

            while(!this.taskQueue.isEmpty()) {
               this.tasks.add((Task)this.taskQueue.poll());
            }

            this.tasks.removeIf(Task::tick);
            long var3 = System.currentTimeMillis() - var1;
            this.locked = false;
            if (this.skipped > 0) {
               this.overloadCallback.forEach((var1x) -> {
                  var1x.accept(this.skipped);
               });
               if (!this.canWait) {
                  LogUtil.debug("The pseudo thread [" + this.name + "] has skipped " + this.skipped + (this.skipped == 1 ? " tick" : " ticks") + " (" + var3 + "ms). Is it overloaded?");
               }

               this.skipped = 0;
            }
         }
      }

   }

   public String getName() {
      return this.name;
   }
}
