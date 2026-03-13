package github.nighter.smartspawner.spawner.utils;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import org.bukkit.Location;

public class SpawnerLocationLockManager {
   private final SmartSpawner plugin;
   private final SpawnerManager spawnerManager;
   private final ConcurrentHashMap<Location, ReentrantLock> locationLocks = new ConcurrentHashMap(128, 0.75F, 4);

   public SpawnerLocationLockManager(SmartSpawner plugin) {
      this.plugin = plugin;
      this.spawnerManager = plugin.getSpawnerManager();
      this.startCleanupTask();
   }

   public ReentrantLock getLock(Location location) {
      return (ReentrantLock)this.locationLocks.computeIfAbsent(location, (k) -> {
         return new ReentrantLock();
      });
   }

   public boolean tryLock(Location location) {
      ReentrantLock lock = this.getLock(location);
      return lock.tryLock();
   }

   public void unlock(Location location) {
      ReentrantLock lock = (ReentrantLock)this.locationLocks.get(location);
      if (lock != null && lock.isHeldByCurrentThread()) {
         lock.unlock();
      }

   }

   public void removeLock(Location location) {
      this.locationLocks.remove(location);
   }

   public boolean isLocked(Location location) {
      ReentrantLock lock = (ReentrantLock)this.locationLocks.get(location);
      return lock != null && lock.isLocked();
   }

   public int getActiveLockCount() {
      return this.locationLocks.size();
   }

   private void startCleanupTask() {
      Scheduler.runTaskTimerAsync(() -> {
         Iterator<Entry<Location, ReentrantLock>> iterator = this.locationLocks.entrySet().iterator();
         int removed = 0;

         while(iterator.hasNext()) {
            Entry<Location, ReentrantLock> entry = (Entry)iterator.next();
            ReentrantLock lock = (ReentrantLock)entry.getValue();
            Location location = (Location)entry.getKey();
            if (!lock.isLocked() && lock.tryLock()) {
               try {
                  if (this.spawnerManager.getSpawnerByLocation(location) == null) {
                     iterator.remove();
                     ++removed;
                  }
               } finally {
                  lock.unlock();
               }
            }
         }

         if (removed > 0) {
            this.plugin.debug("SpawnerLocationLockManager: Cleaned up " + removed + " unused locks. Active locks: " + this.locationLocks.size());
         }

      }, 6000L, 6000L);
   }

   public void shutdown() {
      this.locationLocks.clear();
   }
}
