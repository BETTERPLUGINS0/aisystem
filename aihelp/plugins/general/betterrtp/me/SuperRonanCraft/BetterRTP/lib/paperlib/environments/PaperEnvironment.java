package me.SuperRonanCraft.BetterRTP.lib.paperlib.environments;

import me.SuperRonanCraft.BetterRTP.lib.paperlib.features.asyncchunks.AsyncChunksPaper_13;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.features.asyncchunks.AsyncChunksPaper_15;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.features.asyncchunks.AsyncChunksPaper_9_12;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.features.asyncteleport.AsyncTeleportPaper;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.features.asyncteleport.AsyncTeleportPaper_13;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.features.bedspawnlocation.BedSpawnLocationPaper;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.features.blockstatesnapshot.BlockStateSnapshotOptionalSnapshots;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.features.chunkisgenerated.ChunkIsGeneratedApiExists;
import me.SuperRonanCraft.BetterRTP.lib.paperlib.features.inventoryholdersnapshot.InventoryHolderSnapshotOptionalSnapshots;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;

public class PaperEnvironment extends SpigotEnvironment {
   public PaperEnvironment() {
      if (this.isVersion(13, 1)) {
         this.asyncChunksHandler = new AsyncChunksPaper_13();
         this.asyncTeleportHandler = new AsyncTeleportPaper_13();
      } else if (this.isVersion(9) && !this.isVersion(13)) {
         this.asyncChunksHandler = new AsyncChunksPaper_9_12();
         this.asyncTeleportHandler = new AsyncTeleportPaper();
      }

      if (this.isVersion(12)) {
         this.isGeneratedHandler = new ChunkIsGeneratedApiExists();
         this.blockStateSnapshotHandler = new BlockStateSnapshotOptionalSnapshots();
      }

      if (this.isVersion(15, 2)) {
         try {
            World.class.getDeclaredMethod("getChunkAtAsyncUrgently", Location.class);
            this.asyncChunksHandler = new AsyncChunksPaper_15();
            HumanEntity.class.getDeclaredMethod("getPotentialBedLocation");
            this.bedSpawnLocationHandler = new BedSpawnLocationPaper();
         } catch (NoSuchMethodException var2) {
         }
      }

      if (this.isVersion(16)) {
         this.inventoryHolderSnapshotHandler = new InventoryHolderSnapshotOptionalSnapshots();
      }

   }

   public String getName() {
      return "Paper";
   }

   public boolean isPaper() {
      return true;
   }
}
