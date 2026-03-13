package github.nighter.smartspawner.extras;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.utils.BlockPos;
import java.util.Iterator;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class HopperTracker implements Listener {
   private final SmartSpawner plugin;
   private final HopperRegistry registry;

   public HopperTracker(SmartSpawner plugin, HopperRegistry registry) {
      this.plugin = plugin;
      this.registry = registry;
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
   }

   public void tryAdd(Block hopper) {
      if (hopper.getType() == Material.HOPPER) {
         if (hopper.getRelative(BlockFace.UP).getType() == Material.SPAWNER) {
            this.registry.add(new BlockPos(hopper.getLocation()));
         }
      }
   }

   public void scanLoadedWorlds() {
      Iterator var1 = this.plugin.getServer().getWorlds().iterator();

      while(var1.hasNext()) {
         World world = (World)var1.next();
         this.scanLoadedChunksInWorld(world);
      }

   }

   public void scanLoadedChunksInWorld(World world) {
      Chunk[] var2 = world.getLoadedChunks();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Chunk loadedChunk = var2[var4];
         int x = loadedChunk.getX();
         int z = loadedChunk.getZ();
         Scheduler.runChunkTask(world, x, z, () -> {
            this.scanChunkInternal(world, x, z);
         });
      }

   }

   private void scanChunkInternal(World world, int chunkX, int chunkZ) {
      if (world.isChunkLoaded(chunkX, chunkZ)) {
         Chunk chunk = world.getChunkAt(chunkX, chunkZ);
         this.scanChunk(chunk);
      }
   }

   private void scanChunk(Chunk chunk) {
      Iterator var2 = chunk.getTileEntities((b) -> {
         return b.getType() == Material.HOPPER;
      }, false).iterator();

      while(var2.hasNext()) {
         BlockState state = (BlockState)var2.next();
         this.tryAdd(state.getBlock());
      }

   }

   @EventHandler
   public void onWorldLoad(WorldLoadEvent e) {
      if (this.plugin.getHopperConfig().isHopperEnabled()) {
         this.scanLoadedChunksInWorld(e.getWorld());
      }
   }

   @EventHandler
   public void onWorldUnload(WorldUnloadEvent e) {
      if (this.plugin.getHopperConfig().isHopperEnabled()) {
         this.registry.removeWorld(e.getWorld());
      }
   }

   @EventHandler
   public void onChunkLoad(ChunkLoadEvent e) {
      if (this.plugin.getHopperConfig().isHopperEnabled()) {
         this.scanChunk(e.getChunk());
      }
   }

   @EventHandler
   public void onChunkUnload(ChunkUnloadEvent e) {
      if (this.plugin.getHopperConfig().isHopperEnabled()) {
         Chunk chunk = e.getChunk();
         this.registry.removeChunk(chunk.getWorld().getUID(), chunk.getX(), chunk.getZ());
      }
   }

   @EventHandler
   public void onPlace(BlockPlaceEvent e) {
      if (this.plugin.getHopperConfig().isHopperEnabled()) {
         if (e.getBlockPlaced().getType() == Material.HOPPER) {
            this.tryAdd(e.getBlockPlaced());
         }

      }
   }

   @EventHandler
   public void onBreak(BlockBreakEvent e) {
      if (this.plugin.getHopperConfig().isHopperEnabled()) {
         if (e.getBlock().getType() == Material.HOPPER) {
            this.registry.remove(new BlockPos(e.getBlock().getLocation()));
         }

      }
   }
}
