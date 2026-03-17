package advancedplugins.pm2.cv.models.api.utils.ticker;

import java.util.function.Consumer;

public class Task {
   private final Consumer<Task> task;
   private final int startDelay;
   private final int interval;
   private final boolean isRepeating;
   private int delay;
   private int tick;
   private int runCount;
   private boolean canceled;

   public Task(Consumer<Task> var1, int var2, int var3, boolean var4) {
      this.task = var1;
      this.startDelay = var2;
      this.interval = var3;
      this.isRepeating = var4;
   }

   public boolean tick() {
      if (this.delay++ >= this.startDelay && this.tick-- <= 0) {
         this.tick = this.interval;
         this.task.accept(this);
         ++this.runCount;
         return this.canceled || !this.isRepeating;
      } else {
         return this.canceled;
      }
   }

   public void cancel() {
      this.canceled = true;
   }

   public int getStartDelay() {
      return this.startDelay;
   }

   public int getInterval() {
      return this.interval;
   }

   public boolean isRepeating() {
      return this.isRepeating;
   }

   public int getDelay() {
      return this.delay;
   }

   public int getTick() {
      return this.tick;
   }

   public int getRunCount() {
      return this.runCount;
   }

   public boolean isCanceled() {
      return this.canceled;
   }
}
