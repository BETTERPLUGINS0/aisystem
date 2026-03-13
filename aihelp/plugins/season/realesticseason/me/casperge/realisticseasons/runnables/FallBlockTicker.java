package me.casperge.realisticseasons.runnables;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.WeakHashMap;
import me.casperge.enums.GameRuleType;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.Version;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.season.SeasonChunk;
import me.casperge.realisticseasons.utils.ChunkUtils;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FallBlockTicker extends BukkitRunnable {
   private RealisticSeasons main;
   private WeakReference<World> wr;
   private Random r = new Random();
   private boolean hasChangedBlocks;
   private int travelCheck;
   private int travelCounter;
   private WeakHashMap<Player, Location> lastLocation = new WeakHashMap();
   private WeakHashMap<Player, Boolean> traveling = new WeakHashMap();

   public World getWorld() {
      return (World)this.wr.get();
   }

   public FallBlockTicker(RealisticSeasons var1, World var2) {
      this.main = var1;
      this.wr = new WeakReference(var2);
      Chunk[] var3 = var2.getLoadedChunks();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Chunk var6 = var3[var5];
         var1.getSeasonManager().getQueue(var2, Season.FALL).add(new SeasonChunk(var6.getWorld().getName(), var6.getX(), var6.getZ(), System.currentTimeMillis()));
      }

   }

   public static void checkChunk(SeasonChunk var0) {
      RealisticSeasons var1 = RealisticSeasons.getInstance();
      if (var1.getSettings().modifyBlocks || var1.getSettings().fallAnimals.size() != 0) {
         var1.getAsyncChunkHandler().remove(var0);
         World var2 = var0.getWorld();
         if (var2 != null) {
            boolean var3 = false;
            Chunk var4 = var0.getChunk();
            if (var4 != null) {
               var4.load();
               if (!var1.getChunkUtils().affectFlora(var4)) {
                  if (var1.hasBlockChanges(var4.getX(), var4.getZ(), var4.getWorld()) && var1.hasSeasons(var4.getX(), var4.getZ(), var4.getWorld())) {
                     var1.getChunkUtils().unfreezeChunk(var4);
                  }
               } else {
                  if (!var1.hasSeasons(var4.getX(), var4.getZ(), var4.getWorld())) {
                     return;
                  }

                  if (var1.hasBlockChanges(var4.getX(), var4.getZ(), var4.getWorld())) {
                     var3 = var1.getChunkUtils().unfreezeChunk(var4);
                     if (var1.getSettings().regrowGrass) {
                        var3 = true;
                     }

                     var1.getChunkUtils().checkPopulation(var4, 4.0F, 0.1F, 1, 1, Season.FALL, var3);
                  }

                  if (var1.getSettings().spawnEntities && var1.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_MOB_SPAWNING, var2)) {
                     var1.getAnimalUtils().updateAnimalSpawns(Season.FALL, var4);
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
         if (this.main.getSeasonManager().getSeason(var1) == Season.FALL && var1.getEnvironment() != Environment.NETHER && var1.getEnvironment() != Environment.THE_END) {
            if (!this.main.getSettings().modifyBlocks && this.main.getSettings().fallAnimals.size() == 0) {
               return;
            }

            if (Version.is_1_21_5_or_up() && this.main.getSettings().generateLeafLitter && var1.getPlayers().size() > 0) {
               this.handleTravelTick();
               if (var1.getPlayers().size() >= 15 || this.r.nextInt(16 - var1.getPlayers().size()) == 0) {
                  boolean var2 = false;
                  Player var3 = (Player)var1.getPlayers().get(this.r.nextInt(var1.getPlayers().size()));

                  for(int var4 = 0; var4 < var1.getPlayers().size(); ++var4) {
                     Player var5 = (Player)var1.getPlayers().get(this.r.nextInt(var1.getPlayers().size()));
                     if (!this.isTraveling(var5) && var3.getGameMode() != GameMode.SPECTATOR) {
                        var3 = var5;
                        var2 = true;
                        break;
                     }
                  }

                  if (var2 && var3.getWorld().isChunkLoaded(var3.getLocation().getBlockX() >> 4, var3.getLocation().getBlockZ() >> 4)) {
                     List var10 = (List)ChunkUtils.around(var3.getLocation().getChunk(), 6, 6);
                     Iterator var12 = var10.iterator();

                     while(var12.hasNext()) {
                        Chunk var6 = (Chunk)var12.next();
                        if (this.main.hasSeasons(var6.getX(), var6.getZ(), var6.getWorld()) && this.main.hasBlockChanges(var6.getX(), var6.getZ(), var6.getWorld())) {
                           Block var7 = var3.getWorld().getHighestBlockAt(var6.getX() * 16 + this.r.nextInt(16), var6.getZ() * 16 + this.r.nextInt(16));
                           this.main.getLitterGeneration().spawnLeafLitter(var7.getLocation());
                        }
                     }
                  }
               }
            }

            if (!this.main.getSeasonManager().getQueue(var1, Season.FALL).isEmpty()) {
               if (this.main.getNMSUtils().getTPS() < this.main.getSettings().minTPS && this.main.getSettings().prioritiseTPS) {
                  return;
               }

               Iterator var8 = this.main.getSeasonManager().getQueue(var1, Season.FALL).iterator();
               LinkedHashSet var9 = new LinkedHashSet();

               label132:
               while(true) {
                  while(true) {
                     if (!var8.hasNext()) {
                        break label132;
                     }

                     SeasonChunk var11 = (SeasonChunk)var8.next();
                     if (var11.getWorld() != var1) {
                        var9.add(var11);
                     } else if (System.currentTimeMillis() - var11.getLoadTime() < 10000L) {
                        var9.add(var11);
                     } else if (!this.main.getSeasonManager().getCheckedList(var1, Season.FALL).contains(var11) && var1.isChunkLoaded(var11.getX(), var11.getZ())) {
                        if (!var1.isChunkLoaded(var11.getX(), var11.getZ())) {
                           var8.remove();
                        } else if (var1.isChunkLoaded(var11.getX() + 1, var11.getZ()) && var1.isChunkLoaded(var11.getX() - 1, var11.getZ()) && var1.isChunkLoaded(var11.getX(), var11.getZ() + 1) && var1.isChunkLoaded(var11.getX(), var11.getZ() - 1)) {
                           if (var11.getX() <= 1875000 && var11.getX() <= 1875000 && var11.getX() >= -1875000 && var11.getX() >= -1875000) {
                              Chunk var14 = var11.getChunk();
                              if (!this.main.getChunkUtils().affectFlora(var14)) {
                                 if (this.main.hasBlockChanges(var14.getX(), var14.getZ(), var14.getWorld()) && this.main.hasSeasons(var14.getX(), var14.getZ(), var14.getWorld())) {
                                    this.main.getChunkUtils().unfreezeChunk(var14);
                                    this.main.getAsyncChunkHandler().remove(var11);
                                 }

                                 var8.remove();
                                 this.main.getSeasonManager().getCheckedList(var1, Season.FALL).add(var11);
                                 break label132;
                              }

                              if (this.main.hasSeasons(var14.getX(), var14.getZ(), var14.getWorld())) {
                                 if (this.main.hasBlockChanges(var14.getX(), var14.getZ(), var14.getWorld())) {
                                    this.hasChangedBlocks = this.main.getChunkUtils().unfreezeChunk(var14);
                                    this.main.getAsyncChunkHandler().remove(var11);
                                    if (this.main.getSettings().regrowGrass) {
                                       this.hasChangedBlocks = true;
                                    }

                                    this.main.getChunkUtils().checkPopulation(var14, 4.0F, 0.1F, 1, 1, Season.FALL, this.hasChangedBlocks);
                                 }

                                 if (this.main.getSettings().spawnEntities && this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_MOB_SPAWNING, var1)) {
                                    this.main.getAnimalUtils().updateAnimalSpawns(Season.FALL, var14);
                                 }

                                 var8.remove();
                                 this.main.getSeasonManager().getCheckedList(var1, Season.FALL).add(var11);
                                 break label132;
                              }

                              var8.remove();
                              this.main.getSeasonManager().getCheckedList(var1, Season.FALL).add(var11);
                           } else {
                              var8.remove();
                           }
                        } else {
                           var9.add(var11);
                        }
                     } else {
                        var8.remove();
                     }
                  }
               }

               Iterator var13 = var9.iterator();

               while(var13.hasNext()) {
                  SeasonChunk var15 = (SeasonChunk)var13.next();
                  JavaUtils.moveToBack(this.main.getSeasonManager().getQueue(var1, Season.FALL), var15);
               }
            }
         } else {
            this.main.getSeasonManager().clearChunkCheckedList(var1, Season.FALL);
            this.main.getSeasonManager().clearChunkQueue(var1, Season.FALL);
            this.cancel();
         }

      }
   }

   private void handleTravelTick() {
      if (this.wr.get() != null) {
         World var1 = (World)this.wr.get();
         if (this.travelCheck != 1) {
            if (this.travelCounter != this.travelCheck) {
               ++this.travelCounter;
               return;
            }

            this.travelCounter = 1;
         }

         Iterator var2 = var1.getPlayers().iterator();

         Player var3;
         while(var2.hasNext()) {
            var3 = (Player)var2.next();
            boolean var4 = false;
            if (this.lastLocation.containsKey(var3)) {
               if (ChunkUtils.get2dDistance(var3.getLocation().getBlock(), ((Location)this.lastLocation.get(var3)).getBlock()) > 30.0D) {
                  var4 = true;
               }

               this.lastLocation.replace(var3, var3.getLocation());
            } else {
               this.lastLocation.put(var3, var3.getLocation());
            }

            if (var4) {
               if (this.traveling.containsKey(var3)) {
                  if (!(Boolean)this.traveling.get(var3)) {
                     this.traveling.replace(var3, true);
                  }
               } else {
                  this.traveling.put(var3, true);
               }
            } else if (this.traveling.containsKey(var3)) {
               if ((Boolean)this.traveling.get(var3)) {
                  this.traveling.replace(var3, false);
               }
            } else {
               this.traveling.put(var3, false);
            }
         }

         var2 = (new ArrayList(this.lastLocation.keySet())).iterator();

         while(var2.hasNext()) {
            var3 = (Player)var2.next();
            if (var3.getWorld() != var1) {
               this.lastLocation.remove(var3);
            }
         }

         var2 = (new ArrayList(this.traveling.keySet())).iterator();

         while(var2.hasNext()) {
            var3 = (Player)var2.next();
            if (var3.getWorld() != var1) {
               this.traveling.remove(var3);
            }
         }

      }
   }

   public boolean isTraveling(Player var1) {
      return !this.traveling.containsKey(var1) ? false : (Boolean)this.traveling.get(var1);
   }
}
