package me.casperge.realisticseasons.runnables;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import me.casperge.enums.GameRuleType;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.season.SeasonChunk;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.scheduler.BukkitRunnable;

public class SpringBlockTicker extends BukkitRunnable {
   private RealisticSeasons main;
   private WeakReference<World> wr;
   private Random r = new Random();
   private boolean hasChangedBlocks;

   public World getWorld() {
      return (World)this.wr.get();
   }

   public SpringBlockTicker(RealisticSeasons var1, World var2) {
      this.main = var1;
      this.wr = new WeakReference(var2);
      Chunk[] var3 = var2.getLoadedChunks();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Chunk var6 = var3[var5];
         var1.getSeasonManager().getQueue(var2, Season.SPRING).add(new SeasonChunk(var6.getWorld().getName(), var6.getX(), var6.getZ(), System.currentTimeMillis()));
      }

   }

   public static void checkChunk(SeasonChunk var0) {
      RealisticSeasons var1 = RealisticSeasons.getInstance();
      if (var1.getSettings().modifyBlocks || var1.getSettings().springAnimals.size() != 0) {
         var1.getAsyncChunkHandler().remove(var0);
         boolean var2 = false;
         if (var0.getWorld().isChunkGenerated(var0.getX(), var0.getZ())) {
            Chunk var3 = var0.getChunk();
            if (var3 != null) {
               World var4 = var0.getWorld();
               if (var4 != null) {
                  if (!var1.getChunkUtils().affectFlora(var3)) {
                     if (var1.hasBlockChanges(var3.getX(), var3.getZ(), var3.getWorld()) && var1.hasSeasons(var3.getX(), var3.getZ(), var3.getWorld())) {
                        if (var1.getSeasonManager().getSubSeason(var4).getPhase() > 1) {
                           var1.getChunkUtils().unfreezeChunk(var3);
                        } else {
                           var1.getChunkUtils().unfreezeChunk(var3, 0.14D);
                        }
                     }
                  } else {
                     if (!var1.hasSeasons(var3.getX(), var3.getZ(), var3.getWorld())) {
                        return;
                     }

                     if (var1.hasBlockChanges(var3.getX(), var3.getZ(), var3.getWorld())) {
                        if (var1.getSeasonManager().getSubSeason(var4).getPhase() > 1) {
                           var2 = var1.getChunkUtils().unfreezeChunk(var3);
                           if (var1.getSettings().regrowGrass) {
                              var2 = true;
                           }

                           var1.getChunkUtils().checkPopulation(var3, 4.0F, var1.getSettings().flowerchanceinspring, 3, 3, Season.SPRING, var2);
                        } else if (var1.getSeasonManager().getSubSeason(var4).getPhase() == 0) {
                           var2 = var1.getChunkUtils().unfreezeChunk(var3, 0.14D);
                           if (var1.getSettings().regrowGrass) {
                              var2 = true;
                           }

                           var1.getChunkUtils().checkPopulation(var3, 4.0F, var1.getSettings().flowerchanceinspring / 4.0F, 3, 3, Season.SPRING, var2);
                        } else {
                           var2 = var1.getChunkUtils().unfreezeChunk(var3, 0.14D);
                           if (var1.getSettings().regrowGrass) {
                              var2 = true;
                           }

                           var1.getChunkUtils().checkPopulation(var3, 4.0F, var1.getSettings().flowerchanceinspring / 2.0F, 3, 3, Season.SPRING, var2);
                        }
                     }

                     if (var1.getSettings().spawnEntities && var1.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_MOB_SPAWNING, var4)) {
                        var1.getAnimalUtils().updateAnimalSpawns(Season.SPRING, var3);
                     }
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
         if (this.main.getSeasonManager().getSeason(var1) == Season.SPRING && var1.getEnvironment() != Environment.NETHER && var1.getEnvironment() != Environment.THE_END) {
            if (!this.main.getSettings().modifyBlocks && this.main.getSettings().springAnimals.size() == 0) {
               return;
            }

            if (!this.main.getSeasonManager().getQueue(var1, Season.SPRING).isEmpty()) {
               if (this.main.getNMSUtils().getTPS() < this.main.getSettings().minTPS && this.main.getSettings().prioritiseTPS) {
                  return;
               }

               Iterator var2 = this.main.getSeasonManager().getQueue(var1, Season.SPRING).iterator();
               LinkedHashSet var3 = new LinkedHashSet();

               label136:
               while(true) {
                  while(true) {
                     if (!var2.hasNext()) {
                        break label136;
                     }

                     SeasonChunk var4 = (SeasonChunk)var2.next();
                     if (var4.getWorld() != var1) {
                        var3.add(var4);
                     } else if (System.currentTimeMillis() - var4.getLoadTime() < 10000L) {
                        var3.add(var4);
                     } else if (this.main.getSeasonManager().getCheckedList(var1, Season.SPRING).contains(var4) && this.main.getSeasonManager().getSubSeason(var1).getPhase() > 1) {
                        var2.remove();
                     } else if (!var1.isChunkLoaded(var4.getX(), var4.getZ())) {
                        var2.remove();
                     } else if (var4.getX() <= 1875000 && var4.getX() <= 1875000 && var4.getX() >= -1875000 && var4.getX() >= -1875000) {
                        if (var1.isChunkLoaded(var4.getX() + 1, var4.getZ()) && var1.isChunkLoaded(var4.getX() - 1, var4.getZ()) && var1.isChunkLoaded(var4.getX(), var4.getZ() + 1) && var1.isChunkLoaded(var4.getX(), var4.getZ() - 1)) {
                           Chunk var5 = var4.getChunk();
                           if (!this.main.getChunkUtils().affectFlora(var5)) {
                              if (this.main.hasBlockChanges(var5.getX(), var5.getZ(), var5.getWorld()) && this.main.hasSeasons(var5.getX(), var5.getZ(), var5.getWorld())) {
                                 if (this.main.getSeasonManager().getSubSeason(var1).getPhase() > 1) {
                                    this.main.getChunkUtils().unfreezeChunk(var5);
                                    this.main.getAsyncChunkHandler().remove(var4);
                                 } else {
                                    this.main.getChunkUtils().unfreezeChunk(var5, 0.14D);
                                 }
                              }

                              if (this.main.getSeasonManager().getSubSeason(var1).getPhase() > 1) {
                                 var2.remove();
                                 this.main.getSeasonManager().getCheckedList(var1, Season.SPRING).add(var4);
                              } else {
                                 var3.add(var4);
                              }
                              break label136;
                           }

                           if (this.main.hasSeasons(var5.getX(), var5.getZ(), var5.getWorld())) {
                              if (this.main.hasBlockChanges(var5.getX(), var5.getZ(), var5.getWorld())) {
                                 if (this.main.getSeasonManager().getSubSeason(var1).getPhase() > 1) {
                                    this.hasChangedBlocks = this.main.getChunkUtils().unfreezeChunk(var5);
                                    this.main.getAsyncChunkHandler().remove(var4);
                                    if (this.main.getSettings().regrowGrass) {
                                       this.hasChangedBlocks = true;
                                    }

                                    this.main.getChunkUtils().checkPopulation(var5, 4.0F, this.main.getSettings().flowerchanceinspring, 3, 3, Season.SPRING, this.hasChangedBlocks);
                                 } else if (this.main.getSeasonManager().getSubSeason(var1).getPhase() == 0) {
                                    this.hasChangedBlocks = this.main.getChunkUtils().unfreezeChunk(var5, 0.14D);
                                    if (this.main.getSettings().regrowGrass) {
                                       this.hasChangedBlocks = true;
                                    }

                                    this.main.getChunkUtils().checkPopulation(var5, 4.0F, this.main.getSettings().flowerchanceinspring / 4.0F, 3, 3, Season.SPRING, this.hasChangedBlocks);
                                 } else {
                                    this.hasChangedBlocks = this.main.getChunkUtils().unfreezeChunk(var5, 0.14D);
                                    if (this.main.getSettings().regrowGrass) {
                                       this.hasChangedBlocks = true;
                                    }

                                    this.main.getChunkUtils().checkPopulation(var5, 4.0F, this.main.getSettings().flowerchanceinspring / 2.0F, 3, 3, Season.SPRING, this.hasChangedBlocks);
                                 }
                              }

                              if (!this.main.getSeasonManager().getCheckedList(var1, Season.SPRING).contains(var4) && this.main.getSettings().spawnEntities && this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_MOB_SPAWNING, var1)) {
                                 this.main.getAnimalUtils().updateAnimalSpawns(Season.SPRING, var5);
                              }

                              if (this.main.getSeasonManager().getSubSeason(var1).getPhase() > 1) {
                                 var2.remove();
                                 this.main.getSeasonManager().getCheckedList(var1, Season.SPRING).add(var4);
                              } else {
                                 var3.add(var4);
                              }
                              break label136;
                           }

                           if (this.main.getSeasonManager().getSubSeason(var1).getPhase() > 1) {
                              var2.remove();
                              this.main.getSeasonManager().getCheckedList(var1, Season.SPRING).add(var4);
                           } else {
                              var3.add(var4);
                           }
                        } else {
                           var3.add(var4);
                        }
                     } else {
                        var2.remove();
                     }
                  }
               }

               Iterator var6 = var3.iterator();

               while(var6.hasNext()) {
                  SeasonChunk var7 = (SeasonChunk)var6.next();
                  JavaUtils.moveToBack(this.main.getSeasonManager().getQueue(var1, Season.SPRING), var7);
               }
            }
         } else {
            this.main.getSeasonManager().clearChunkCheckedList(var1, Season.SPRING);
            this.main.getSeasonManager().clearChunkQueue(var1, Season.SPRING);
            this.cancel();
         }

      }
   }
}
