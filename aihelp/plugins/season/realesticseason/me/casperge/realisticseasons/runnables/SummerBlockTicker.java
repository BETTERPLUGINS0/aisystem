package me.casperge.realisticseasons.runnables;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import me.casperge.enums.GameRuleType;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.season.SeasonChunk;
import me.casperge.realisticseasons.season.SubSeason;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.scheduler.BukkitRunnable;

public class SummerBlockTicker extends BukkitRunnable {
   private RealisticSeasons main;
   private WeakReference<World> wr;
   private Random r = new Random();
   private boolean hasChangedBlocks;

   public World getWorld() {
      return (World)this.wr.get();
   }

   public SummerBlockTicker(RealisticSeasons var1, World var2) {
      this.main = var1;
      this.wr = new WeakReference(var2);
      Chunk[] var3 = var2.getLoadedChunks();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Chunk var6 = var3[var5];
         var1.getSeasonManager().getQueue(var2, Season.SUMMER).add(new SeasonChunk(var6.getWorld().getName(), var6.getX(), var6.getZ(), System.currentTimeMillis()));
      }

   }

   public static void checkChunk(SeasonChunk var0) {
      RealisticSeasons var1 = RealisticSeasons.getInstance();
      if (var1.getSettings().modifyBlocks || var1.getSettings().summerAnimals.size() != 0) {
         var1.getAsyncChunkHandler().remove(var0);
         boolean var2 = false;
         Chunk var3 = var0.getChunk();
         if (var3 != null) {
            var3.load();
            World var4 = var0.getWorld();
            if (var4 != null) {
               if (!var1.getChunkUtils().affectFlora(var3)) {
                  if (var1.hasBlockChanges(var3.getX(), var3.getZ(), var3.getWorld()) && var1.hasSeasons(var3.getX(), var3.getZ(), var3.getWorld())) {
                     var1.getChunkUtils().unfreezeChunk(var3);
                  }
               } else {
                  if (!var1.hasSeasons(var3.getX(), var3.getZ(), var3.getWorld())) {
                     return;
                  }

                  if (var1.hasBlockChanges(var3.getX(), var3.getZ(), var3.getWorld())) {
                     var2 = var1.getChunkUtils().unfreezeChunk(var3);
                     if (var1.getSettings().regrowGrass) {
                        var2 = true;
                     }

                     if (var1.getSettings().flowerchanceinspring > 0.1F) {
                        if (var1.getSeasonManager().getSubSeason(var4) == SubSeason.START) {
                           var1.getChunkUtils().checkPopulation(var3, 4.0F, (0.1F + var1.getSettings().flowerchanceinspring) / 2.0F, 1, 1, Season.SUMMER, var2);
                        } else if (var1.getSeasonManager().getSubSeason(var4) == SubSeason.EARLY) {
                           float var5 = (0.1F + var1.getSettings().flowerchanceinspring) / 4.0F;
                           if (var5 < 0.1F) {
                              var5 = 0.1F;
                           }

                           var1.getChunkUtils().checkPopulation(var3, 4.0F, var5, 1, 1, Season.SUMMER, var2);
                        } else {
                           var1.getChunkUtils().checkPopulation(var3, 4.0F, 0.1F, 1, 1, Season.SUMMER, var2);
                        }
                     } else {
                        var1.getChunkUtils().checkPopulation(var3, 4.0F, 0.1F, 1, 1, Season.SUMMER, var2);
                     }
                  }

                  if (var1.getSettings().spawnEntities && var1.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_MOB_SPAWNING, var4)) {
                     var1.getAnimalUtils().updateAnimalSpawns(Season.SUMMER, var3);
                  }
               }

            }
         }
      }
   }

   public void run() {
      if (this.wr.get() == null) {
         this.cancel();
      } else {
         World var1 = (World)this.wr.get();
         if (this.main.getSeasonManager().getSeason(var1) == Season.SUMMER && var1.getEnvironment() != Environment.NETHER && var1.getEnvironment() != Environment.THE_END) {
            if (!this.main.getSettings().modifyBlocks && this.main.getSettings().summerAnimals.size() == 0) {
               return;
            }

            if (!this.main.getSeasonManager().getQueue(var1, Season.SUMMER).isEmpty()) {
               if (this.main.getNMSUtils().getTPS() < this.main.getSettings().minTPS && this.main.getSettings().prioritiseTPS) {
                  return;
               }

               Iterator var2 = this.main.getSeasonManager().getQueue(var1, Season.SUMMER).iterator();
               LinkedHashSet var3 = new LinkedHashSet();

               label121:
               while(true) {
                  while(true) {
                     if (!var2.hasNext()) {
                        break label121;
                     }

                     SeasonChunk var4 = (SeasonChunk)var2.next();
                     if (var4.getWorld() != var1) {
                        var3.add(var4);
                     } else if (System.currentTimeMillis() - var4.getLoadTime() < 10000L) {
                        var3.add(var4);
                     } else if (!this.main.getSeasonManager().getCheckedList(var1, Season.SUMMER).contains(var4) && var1.isChunkLoaded(var4.getX(), var4.getZ())) {
                        if (var4.getX() <= 1875000 && var4.getX() <= 1875000 && var4.getX() >= -1875000 && var4.getX() >= -1875000) {
                           if (!var1.isChunkLoaded(var4.getX(), var4.getZ())) {
                              var2.remove();
                           } else if (var1.isChunkLoaded(var4.getX() + 1, var4.getZ()) && var1.isChunkLoaded(var4.getX() - 1, var4.getZ()) && var1.isChunkLoaded(var4.getX(), var4.getZ() + 1) && var1.isChunkLoaded(var4.getX(), var4.getZ() - 1)) {
                              Chunk var5 = var4.getChunk();
                              if (!this.main.getChunkUtils().affectFlora(var5)) {
                                 if (this.main.hasBlockChanges(var5.getX(), var5.getZ(), var5.getWorld()) && this.main.hasSeasons(var5.getX(), var5.getZ(), var5.getWorld())) {
                                    this.main.getChunkUtils().unfreezeChunk(var5);
                                    this.main.getAsyncChunkHandler().remove(var4);
                                 }

                                 var2.remove();
                                 this.main.getSeasonManager().getCheckedList(var1, Season.SUMMER).add(var4);
                                 break label121;
                              }

                              if (this.main.hasSeasons(var5.getX(), var5.getZ(), var5.getWorld())) {
                                 if (this.main.hasBlockChanges(var5.getX(), var5.getZ(), var5.getWorld())) {
                                    this.hasChangedBlocks = this.main.getChunkUtils().unfreezeChunk(var5);
                                    this.main.getAsyncChunkHandler().remove(var4);
                                    if (this.main.getSettings().regrowGrass) {
                                       this.hasChangedBlocks = true;
                                    }

                                    if (this.main.getSettings().flowerchanceinspring > 0.1F) {
                                       if (this.main.getSeasonManager().getSubSeason(var1) == SubSeason.START) {
                                          this.main.getChunkUtils().checkPopulation(var5, 4.0F, (0.1F + this.main.getSettings().flowerchanceinspring) / 2.0F, 1, 1, Season.SUMMER, this.hasChangedBlocks);
                                       } else if (this.main.getSeasonManager().getSubSeason(var1) == SubSeason.EARLY) {
                                          float var6 = (0.1F + this.main.getSettings().flowerchanceinspring) / 4.0F;
                                          if (var6 < 0.1F) {
                                             var6 = 0.1F;
                                          }

                                          this.main.getChunkUtils().checkPopulation(var5, 4.0F, var6, 1, 1, Season.SUMMER, this.hasChangedBlocks);
                                       } else {
                                          this.main.getChunkUtils().checkPopulation(var5, 4.0F, 0.1F, 1, 1, Season.SUMMER, this.hasChangedBlocks);
                                       }
                                    } else {
                                       this.main.getChunkUtils().checkPopulation(var5, 4.0F, 0.1F, 1, 1, Season.SUMMER, this.hasChangedBlocks);
                                    }
                                 }

                                 if (this.main.getSettings().spawnEntities && this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_MOB_SPAWNING, var1)) {
                                    this.main.getAnimalUtils().updateAnimalSpawns(Season.SUMMER, var5);
                                 }

                                 var2.remove();
                                 this.main.getSeasonManager().getCheckedList(var1, Season.SUMMER).add(var4);
                                 break label121;
                              }

                              var2.remove();
                              this.main.getSeasonManager().getCheckedList(var1, Season.SUMMER).add(var4);
                           } else {
                              var3.add(var4);
                           }
                        } else {
                           var2.remove();
                        }
                     } else {
                        var2.remove();
                     }
                  }
               }

               Iterator var7 = var3.iterator();

               while(var7.hasNext()) {
                  SeasonChunk var8 = (SeasonChunk)var7.next();
                  JavaUtils.moveToBack(this.main.getSeasonManager().getQueue(var1, Season.SUMMER), var8);
               }
            }
         } else {
            this.main.getSeasonManager().clearChunkCheckedList(var1, Season.SUMMER);
            this.main.getSeasonManager().clearChunkQueue(var1, Season.SUMMER);
            this.cancel();
         }

      }
   }
}
