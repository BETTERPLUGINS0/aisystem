package me.casperge.realisticseasons.event;

import java.util.Iterator;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.blockscanner.SimpleLocation;
import me.casperge.realisticseasons.blockscanner.blocksaver.StoredBlockType;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.season.SeasonChunk;
import me.casperge.realisticseasons.seasonevent.buildin.DefaultEventType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkLoadManager implements Listener {
   private RealisticSeasons main;

   public ChunkLoadManager(RealisticSeasons var1) {
      this.main = var1;
      if (var1.getSettings().modifyBlocks || var1.getSettings().spawnEntities) {
         var1.getServer().getPluginManager().registerEvents(this, var1);
      }
   }

   @EventHandler
   public void onLoad(ChunkLoadEvent var1) {
      if (this.main.debugMode) {
         UnexpectedChunkLoadException var2 = new UnexpectedChunkLoadException();
         boolean var3 = false;
         boolean var4 = false;
         StackTraceElement[] var5 = var2.getStackTrace();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            StackTraceElement var8 = var5[var7];
            if (var8.toString().contains("casperge") && !var8.toString().contains("ChunkLoadManager")) {
               var3 = true;
            }

            if (var8.toString().contains("casperge") && var8.toString().contains("checkChunk")) {
               var4 = true;
            }
         }

         if (var3 && !var4) {
            Bukkit.getLogger().warning("RealisticSeasons tried to load chunk: " + String.valueOf(var1.getChunk().getX()) + ", " + var1.getChunk().getZ() + " in world " + var1.getChunk().getWorld().getName());
            var2.printStackTrace();
         }
      }

      int var9 = var1.getChunk().getX();
      int var10 = var1.getChunk().getZ();
      if (var9 <= 1875000 && var10 <= 1875000 && var9 >= -1875000 && var10 >= -1875000) {
         if (var1.getWorld().getEnvironment() != Environment.NETHER && var1.getWorld().getEnvironment() != Environment.THE_END) {
            List var11;
            Iterator var12;
            SimpleLocation var14;
            Block var15;
            if (this.main.getEventManager().getDefaultEvent(DefaultEventType.CHRISTMAS) == null) {
               var11 = this.main.getBlockStorage().getPresents(var1.getChunk());

               for(var12 = var11.iterator(); var12.hasNext(); this.main.getBlockStorage().logBreak(var15.getLocation(), StoredBlockType.PRESENT)) {
                  var14 = (SimpleLocation)var12.next();
                  var15 = var1.getWorld().getBlockAt(var14.getX(), var14.getY(), var14.getZ());
                  if (var15.getType() == Material.PLAYER_HEAD) {
                     var15.setType(Material.AIR);
                  }
               }
            } else if (!this.main.getEventManager().getDefaultEvent(DefaultEventType.CHRISTMAS).isEnabled(var1.getWorld())) {
               var11 = this.main.getBlockStorage().getPresents(var1.getChunk());

               for(var12 = var11.iterator(); var12.hasNext(); this.main.getBlockStorage().logBreak(var15.getLocation(), StoredBlockType.PRESENT)) {
                  var14 = (SimpleLocation)var12.next();
                  var15 = var1.getWorld().getBlockAt(var14.getX(), var14.getY(), var14.getZ());
                  if (var15.getType() == Material.PLAYER_HEAD) {
                     var15.setType(Material.AIR);
                  }
               }
            }

            if (this.main.getEventManager().getDefaultEvent(DefaultEventType.EASTER) == null) {
               var11 = this.main.getBlockStorage().getEggs(var1.getChunk());

               for(var12 = var11.iterator(); var12.hasNext(); this.main.getBlockStorage().logBreak(var15.getLocation(), StoredBlockType.EGG)) {
                  var14 = (SimpleLocation)var12.next();
                  var15 = var1.getWorld().getBlockAt(var14.getX(), var14.getY(), var14.getZ());
                  if (var15.getType() == Material.PLAYER_HEAD) {
                     var15.setType(Material.AIR);
                  }
               }
            } else if (!this.main.getEventManager().getDefaultEvent(DefaultEventType.EASTER).isEnabled(var1.getWorld())) {
               var11 = this.main.getBlockStorage().getEggs(var1.getChunk());

               for(var12 = var11.iterator(); var12.hasNext(); this.main.getBlockStorage().logBreak(var15.getLocation(), StoredBlockType.EGG)) {
                  var14 = (SimpleLocation)var12.next();
                  var15 = var1.getWorld().getBlockAt(var14.getX(), var14.getY(), var14.getZ());
                  if (var15.getType() == Material.PLAYER_HEAD) {
                     var15.setType(Material.AIR);
                  }
               }
            }

            SeasonChunk var13 = new SeasonChunk(var1.getChunk().getWorld().getName(), var1.getChunk().getX(), var1.getChunk().getZ(), System.currentTimeMillis());
            if (this.main.getSeasonManager().getSeason(var1.getWorld()) == Season.SPRING) {
               if (!this.main.getSeasonManager().getCheckedList(var1.getChunk().getWorld(), Season.SPRING).contains(var13)) {
                  this.main.getSeasonManager().getQueue(var1.getChunk().getWorld(), Season.SPRING).add(var13);
               }
            } else if (this.main.getSeasonManager().getSeason(var1.getWorld()) == Season.SUMMER) {
               if (!this.main.getSeasonManager().getCheckedList(var1.getChunk().getWorld(), Season.SUMMER).contains(var13)) {
                  this.main.getSeasonManager().getQueue(var1.getChunk().getWorld(), Season.SUMMER).add(var13);
               }
            } else if (this.main.getSeasonManager().getSeason(var1.getWorld()) == Season.FALL) {
               if (!this.main.getSeasonManager().getCheckedList(var1.getChunk().getWorld(), Season.FALL).contains(var13)) {
                  this.main.getSeasonManager().getQueue(var1.getChunk().getWorld(), Season.FALL).add(var13);
               }
            } else if (this.main.getSeasonManager().getSeason(var1.getWorld()) == Season.RESTORE && !this.main.getSeasonManager().getCheckedList(var1.getChunk().getWorld(), Season.RESTORE).contains(var13)) {
               this.main.getSeasonManager().getQueue(var1.getChunk().getWorld(), Season.RESTORE).add(var13);
            }
         }

      }
   }

   @EventHandler
   public void onUnload(ChunkUnloadEvent var1) {
      if (this.main.isEnabled()) {
         int var2 = var1.getChunk().getX();
         int var3 = var1.getChunk().getZ();
         if (var2 <= 1875000 && var3 <= 1875000 && var2 >= -1875000 && var3 >= -1875000) {
            if (var1.getWorld().getEnvironment() != Environment.NETHER && var1.getWorld().getEnvironment() != Environment.THE_END) {
               SeasonChunk var4;
               if (this.main.getSeasonManager().getSeason(var1.getWorld()) == Season.SPRING) {
                  var4 = new SeasonChunk(var1.getChunk().getWorld().getName(), var1.getChunk().getX(), var1.getChunk().getZ(), 0L);
                  this.main.getSeasonManager().getQueue(var1.getChunk().getWorld(), Season.SPRING).remove(var4);
               } else if (this.main.getSeasonManager().getSeason(var1.getWorld()) == Season.SUMMER) {
                  var4 = new SeasonChunk(var1.getChunk().getWorld().getName(), var1.getChunk().getX(), var1.getChunk().getZ(), 0L);
                  this.main.getSeasonManager().getQueue(var1.getChunk().getWorld(), Season.SUMMER).remove(var4);
               } else if (this.main.getSeasonManager().getSeason(var1.getWorld()) == Season.FALL) {
                  var4 = new SeasonChunk(var1.getChunk().getWorld().getName(), var1.getChunk().getX(), var1.getChunk().getZ(), 0L);
                  this.main.getSeasonManager().getQueue(var1.getChunk().getWorld(), Season.FALL).remove(var4);
               } else if (this.main.getSeasonManager().getSeason(var1.getWorld()) == Season.RESTORE) {
                  var4 = new SeasonChunk(var1.getChunk().getWorld().getName(), var1.getChunk().getX(), var1.getChunk().getZ(), 0L);
                  this.main.getSeasonManager().getQueue(var1.getChunk().getWorld(), Season.RESTORE).remove(var4);
               }
            }

         }
      }
   }
}
