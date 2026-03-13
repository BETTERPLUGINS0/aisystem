package me.casperge.realisticseasons.paperlib.environments;

import java.util.concurrent.CompletableFuture;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.casperge.realisticseasons.paperlib.features.asyncchunks.AsyncChunks;
import me.casperge.realisticseasons.paperlib.features.asyncchunks.AsyncChunksSync;
import me.casperge.realisticseasons.paperlib.features.asyncteleport.AsyncTeleport;
import me.casperge.realisticseasons.paperlib.features.asyncteleport.AsyncTeleportSync;
import me.casperge.realisticseasons.paperlib.features.bedspawnlocation.BedSpawnLocation;
import me.casperge.realisticseasons.paperlib.features.bedspawnlocation.BedSpawnLocationSync;
import me.casperge.realisticseasons.paperlib.features.blockstatesnapshot.BlockStateSnapshot;
import me.casperge.realisticseasons.paperlib.features.blockstatesnapshot.BlockStateSnapshotBeforeSnapshots;
import me.casperge.realisticseasons.paperlib.features.blockstatesnapshot.BlockStateSnapshotNoOption;
import me.casperge.realisticseasons.paperlib.features.blockstatesnapshot.BlockStateSnapshotResult;
import me.casperge.realisticseasons.paperlib.features.chunkisgenerated.ChunkIsGenerated;
import me.casperge.realisticseasons.paperlib.features.chunkisgenerated.ChunkIsGeneratedApiExists;
import me.casperge.realisticseasons.paperlib.features.chunkisgenerated.ChunkIsGeneratedUnknown;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public abstract class Environment {
   private final int minecraftVersion;
   private final int minecraftPatchVersion;
   private final int minecraftPreReleaseVersion;
   protected AsyncChunks asyncChunksHandler = new AsyncChunksSync();
   protected AsyncTeleport asyncTeleportHandler = new AsyncTeleportSync();
   protected ChunkIsGenerated isGeneratedHandler = new ChunkIsGeneratedUnknown();
   protected BlockStateSnapshot blockStateSnapshotHandler;
   protected BedSpawnLocation bedSpawnLocationHandler = new BedSpawnLocationSync();

   public Environment() {
      Pattern var1 = Pattern.compile("(?i)\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?(?: Pre-Release )?(\\d)?\\)");
      Matcher var2 = var1.matcher(Bukkit.getVersion());
      int var3 = 0;
      int var4 = 0;
      int var5 = 0;
      if (var2.find()) {
         MatchResult var6 = var2.toMatchResult();

         try {
            var3 = Integer.parseInt(var6.group(2), 10);
         } catch (Exception var10) {
         }

         if (var6.groupCount() >= 3) {
            try {
               var4 = Integer.parseInt(var6.group(3), 10);
            } catch (Exception var9) {
            }
         }

         if (var6.groupCount() >= 4) {
            try {
               var5 = Integer.parseInt(var2.group(4));
            } catch (Exception var8) {
            }
         }
      }

      this.minecraftVersion = var3;
      this.minecraftPatchVersion = var4;
      this.minecraftPreReleaseVersion = var5;
      if (this.isVersion(13, 1)) {
         this.isGeneratedHandler = new ChunkIsGeneratedApiExists();
      }

      if (!this.isVersion(12)) {
         this.blockStateSnapshotHandler = new BlockStateSnapshotBeforeSnapshots();
      } else {
         this.blockStateSnapshotHandler = new BlockStateSnapshotNoOption();
      }

   }

   public abstract String getName();

   public CompletableFuture<Chunk> getChunkAtAsync(World var1, int var2, int var3, boolean var4) {
      return this.asyncChunksHandler.getChunkAtAsync(var1, var2, var3, var4, false);
   }

   public CompletableFuture<Chunk> getChunkAtAsync(World var1, int var2, int var3, boolean var4, boolean var5) {
      return this.asyncChunksHandler.getChunkAtAsync(var1, var2, var3, var4, var5);
   }

   public CompletableFuture<Chunk> getChunkAtAsyncUrgently(World var1, int var2, int var3, boolean var4) {
      return this.asyncChunksHandler.getChunkAtAsync(var1, var2, var3, var4, true);
   }

   public CompletableFuture<Boolean> teleport(Entity var1, Location var2, TeleportCause var3) {
      return this.asyncTeleportHandler.teleportAsync(var1, var2, var3);
   }

   public boolean isChunkGenerated(World var1, int var2, int var3) {
      return this.isGeneratedHandler.isChunkGenerated(var1, var2, var3);
   }

   public BlockStateSnapshotResult getBlockState(Block var1, boolean var2) {
      return this.blockStateSnapshotHandler.getBlockState(var1, var2);
   }

   public CompletableFuture<Location> getBedSpawnLocationAsync(Player var1, boolean var2) {
      return this.bedSpawnLocationHandler.getBedSpawnLocationAsync(var1, var2);
   }

   public boolean isVersion(int var1) {
      return this.isVersion(var1, 0);
   }

   public boolean isVersion(int var1, int var2) {
      return this.minecraftVersion > var1 || this.minecraftVersion >= var1 && this.minecraftPatchVersion >= var2;
   }

   public int getMinecraftVersion() {
      return this.minecraftVersion;
   }

   public int getMinecraftPatchVersion() {
      return this.minecraftPatchVersion;
   }

   public int getMinecraftPreReleaseVersion() {
      return this.minecraftPreReleaseVersion;
   }

   public boolean isSpigot() {
      return false;
   }

   public boolean isPaper() {
      return false;
   }
}
