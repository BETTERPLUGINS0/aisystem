package com.nisovin.shopkeepers.shopkeeper.spawning;

import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.taskqueue.TaskQueue;
import java.util.function.Consumer;
import org.bukkit.plugin.Plugin;

public class ShopkeeperSpawnQueue extends TaskQueue<AbstractShopkeeper> {
   private static final int SPAWN_TASK_PERIOD_TICKS = 3;
   private static final int SPAWNS_PER_EXECUTION = 6;
   private final Consumer<? super AbstractShopkeeper> spawner;

   ShopkeeperSpawnQueue(Plugin plugin, Consumer<? super AbstractShopkeeper> spawner) {
      super(plugin, 3, 6);
      Validate.notNull(spawner, (String)"spawner is null");
      this.spawner = spawner;
   }

   private void setQueued(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      ShopkeeperSpawnState spawnState = (ShopkeeperSpawnState)shopkeeper.getComponents().getOrAdd(ShopkeeperSpawnState.class);

      assert !spawnState.isSpawningScheduled();

      spawnState.setState(ShopkeeperSpawnState.State.QUEUED);
   }

   private void resetQueued(AbstractShopkeeper shopkeeper) {
      assert shopkeeper != null;

      ShopkeeperSpawnState spawnState = (ShopkeeperSpawnState)shopkeeper.getComponents().getOrAdd(ShopkeeperSpawnState.class);

      assert spawnState.getState() == ShopkeeperSpawnState.State.QUEUED : "spawnState != QUEUED";

      spawnState.setState(ShopkeeperSpawnState.State.DESPAWNED);
   }

   protected void onAdded(AbstractShopkeeper shopkeeper) {
      super.onAdded(shopkeeper);
      this.setQueued(shopkeeper);
   }

   protected void onRemoval(AbstractShopkeeper shopkeeper) {
      super.onRemoval(shopkeeper);
      this.resetQueued(shopkeeper);
   }

   protected Runnable createTask() {
      return new ShopkeeperSpawnQueue.SpawnerTask(super.createTask());
   }

   protected void process(AbstractShopkeeper shopkeeper) {
      this.resetQueued(shopkeeper);
      this.spawner.accept(shopkeeper);
   }

   private static class SpawnerTask implements Runnable {
      private final Runnable parentTask;

      SpawnerTask(Runnable parentTask) {
         assert parentTask != null;

         this.parentTask = parentTask;
      }

      public void run() {
         this.parentTask.run();
      }
   }
}
