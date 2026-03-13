package github.nighter.smartspawner.extras;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.utils.BlockPos;
import github.nighter.smartspawner.utils.ChunkUtil;
import java.util.Iterator;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class HopperService {
   private final SmartSpawner plugin;
   private final HopperRegistry registry;
   private final HopperTransfer transfer;
   private final HopperTracker tracker;
   private final Scheduler.Task task;

   public HopperService(SmartSpawner plugin) {
      this.plugin = plugin;
      this.registry = new HopperRegistry();
      this.transfer = new HopperTransfer(plugin);
      this.tracker = new HopperTracker(plugin, this.registry);
      this.tracker.scanLoadedWorlds();
      long delay = plugin.getTimeFromConfig("hopper.check_delay", "3s");
      this.task = Scheduler.runTaskTimer(this::tick, 40L, delay);
   }

   private void tick() {
      if (this.plugin.getHopperConfig().isHopperEnabled()) {
         this.registry.forEachChunk((worldId, chunkKey) -> {
            World world = Bukkit.getWorld(worldId);
            if (world != null) {
               int chunkX = ChunkUtil.getChunkX(chunkKey);
               int chunkZ = ChunkUtil.getChunkZ(chunkKey);
               Scheduler.runChunkTask(world, chunkX, chunkZ, () -> {
                  Iterator var3 = this.registry.getChunkHoppers(worldId, chunkKey).iterator();

                  while(var3.hasNext()) {
                     BlockPos pos = (BlockPos)var3.next();
                     this.transfer.process(pos);
                  }

               });
            }
         });
      }
   }

   public void cleanup() {
      if (this.task != null) {
         try {
            this.task.cancel();
         } catch (Exception var2) {
         }
      }

   }

   @Generated
   public HopperRegistry getRegistry() {
      return this.registry;
   }

   @Generated
   public HopperTracker getTracker() {
      return this.tracker;
   }
}
