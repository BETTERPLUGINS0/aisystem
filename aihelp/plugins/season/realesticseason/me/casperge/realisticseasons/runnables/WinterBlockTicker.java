package me.casperge.realisticseasons.runnables;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.WeakHashMap;
import me.casperge.enums.GameRuleType;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.Version;
import me.casperge.realisticseasons.blockscanner.blocksaver.StoredBlockType;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.season.SeasonChunk;
import me.casperge.realisticseasons.utils.ChunkUtils;
import me.casperge.realisticseasons.utils.JavaUtils;
import me.casperge.realisticseasons.utils.SnowPatterns;
import me.casperge.realisticseasons1_21_R4.NmsCode_21_R4;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WinterBlockTicker extends BukkitRunnable {
   private List<Material> destroyForSnow = new ArrayList();
   private List<SeasonChunk> checkedAtleastOnce = new ArrayList();
   private RealisticSeasons main;
   private WeakReference<World> wr;
   private Random r = new Random();
   int currentIndexSnow = 0;
   int firstSnowfall = 1;
   private int iceID;
   private int snowID;
   private int snowLayers;
   private int runDelay;
   private int travelCheck;
   private int travelCounter;
   private WeakHashMap<Player, Location> lastLocation = new WeakHashMap();
   private WeakHashMap<Player, Boolean> traveling = new WeakHashMap();
   private final BlockFace[] sideFaces;

   public World getWorld() {
      return (World)this.wr.get();
   }

   public WinterBlockTicker(RealisticSeasons var1, World var2) {
      this.sideFaces = new BlockFace[]{BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH};
      this.runDelay = var1.getSettings().delayPerTick * 2;
      this.travelCounter = 1;
      this.travelCheck = 100 / this.runDelay;
      if (this.travelCheck < 1) {
         this.travelCheck = 1;
      }

      this.main = var1;
      this.wr = new WeakReference(var2);
      this.destroyForSnow.add(Material.GRASS);
      this.destroyForSnow.add(Material.TALL_GRASS);
      this.destroyForSnow.add(Material.DANDELION);
      this.destroyForSnow.add(Material.POPPY);
      this.destroyForSnow.add(Material.BLUE_ORCHID);
      this.destroyForSnow.add(Material.ALLIUM);
      this.destroyForSnow.add(Material.AZURE_BLUET);
      this.destroyForSnow.add(Material.ORANGE_TULIP);
      this.destroyForSnow.add(Material.PINK_TULIP);
      this.destroyForSnow.add(Material.RED_TULIP);
      this.destroyForSnow.add(Material.WHITE_TULIP);
      this.destroyForSnow.add(Material.OXEYE_DAISY);
      this.destroyForSnow.add(Material.CORNFLOWER);
      this.destroyForSnow.add(Material.LILY_OF_THE_VALLEY);
      this.destroyForSnow.add(Material.SWEET_BERRY_BUSH);
      this.destroyForSnow.add(Material.RED_MUSHROOM);
      this.destroyForSnow.add(Material.BROWN_MUSHROOM);
      if (Version.is_1_21_5_or_up() && var1.getSettings().generateLeafLitter) {
         this.destroyForSnow.add(NmsCode_21_R4.getLeafLitter());
      }

      SnowPatterns.generate();
      this.snowID = Version.getSnowID();
      this.iceID = Version.getIceID();
      this.snowLayers = var1.getSettings().snowLayers;
      this.firstSnowfall = var1.getSettings().firstSnowfall;
   }

   public void run() {
      if (this.wr.get() == null) {
         this.cancel();
      } else {
         World var1 = (World)this.wr.get();
         if (this.main.getSeasonManager().getSeason(var1) == Season.WINTER && var1.getEnvironment() != Environment.NETHER && var1.getEnvironment() != Environment.THE_END) {
            if (Version.is_1_19_3_or_up()) {
               this.snowLayers = this.main.getGameRuleGetter().GetIntegerGameRule(GameRuleType.SNOW_ACCUMULATION_HEIGHT, var1);
            }

            if (!this.main.getSettings().modifyBlocks && this.main.getSettings().winterAnimals.size() == 0) {
               return;
            }

            if (this.main.getNMSUtils().getTPS() < this.main.getSettings().minTPS && this.main.getSettings().prioritiseTPS) {
               return;
            }

            if (var1.getPlayers().size() > 0) {
               this.handleTravelTick();
               if (var1.getPlayers().size() < 4 && this.r.nextInt(5 - var1.getPlayers().size()) != 0) {
                  return;
               }

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

               if (!var2) {
                  return;
               }

               if (!var3.getWorld().isChunkLoaded(var3.getLocation().getBlockX() >> 4, var3.getLocation().getBlockZ() >> 4)) {
                  return;
               }

               Collection var17 = ChunkUtils.around(var3.getLocation().getChunk(), 9);
               int var18 = this.main.getSeasonManager().getSubSeason(var1).getPhase();
               Iterator var6 = var17.iterator();

               while(true) {
                  while(var6.hasNext()) {
                     Chunk var7 = (Chunk)var6.next();
                     if (!this.main.hasSeasons(var7.getX(), var7.getZ(), var7.getWorld())) {
                        if (!this.checkedAtleastOnce.contains(new SeasonChunk(var7.getWorld().getName(), var7.getX(), var7.getZ(), 0L))) {
                           this.checkedAtleastOnce.add(new SeasonChunk(var7.getWorld().getName(), var7.getX(), var7.getZ(), 0L));
                        }
                     } else {
                        Integer[] var8 = SnowPatterns.getArray(var7.getX(), var7.getZ());
                        int var9 = (int)Math.floor((double)(var8[this.currentIndexSnow] / 16));
                        int var10 = var8[this.currentIndexSnow] % 16;
                        Block var11 = var3.getWorld().getHighestBlockAt(var7.getX() * 16 + var9, var7.getZ() * 16 + var10);
                        Chunk var12 = var11.getChunk();
                        if (this.main.getBlockUtils().affectBlockInWinter(var11) && var11.getLightFromBlocks() < 11) {
                           if (var11.getType() != Material.WATER) {
                              boolean var20;
                              if (this.main.getBlockUtils().isSnowable(var11) && var1.hasStorm() && !this.main.getSeasonManager().refreshers.containsKey(var1)) {
                                 var20 = false;
                                 if (!this.checkedAtleastOnce.contains(new SeasonChunk(var7.getWorld().getName(), var7.getX(), var7.getZ(), 0L))) {
                                    if (this.main.getSettings().spawnEntities && this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_MOB_SPAWNING, var1)) {
                                       this.main.getAnimalUtils().updateAnimalSpawns(Season.WINTER, var7);
                                    }

                                    this.checkedAtleastOnce.add(new SeasonChunk(var7.getWorld().getName(), var7.getX(), var7.getZ(), 0L));
                                    var20 = true;
                                 }

                                 if (this.main.hasBlockChanges(var12.getX(), var12.getZ(), var12.getWorld()) && this.main.getSettings().snowPlacement && var18 >= this.firstSnowfall) {
                                    this.main.getNMSUtils().setBlockInNMSChunk(var11.getWorld(), var11.getX(), var11.getY() + 1, var11.getZ(), this.snowID, (byte)0, false);
                                    if (var20) {
                                       this.main.getAsyncChunkHandler().logChunk(new SeasonChunk(var12.getWorld().getName(), var12.getX(), var12.getZ(), 0L));
                                    }
                                 }
                              } else if (this.destroyForSnow.contains(var11.getRelative(0, 1, 0).getType()) && var1.hasStorm() && !this.main.getSeasonManager().refreshers.containsKey(var1)) {
                                 var20 = false;
                                 if (!this.checkedAtleastOnce.contains(new SeasonChunk(var7.getWorld().getName(), var7.getX(), var7.getZ(), 0L))) {
                                    if (this.main.getSettings().spawnEntities && this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_MOB_SPAWNING, var1)) {
                                       this.main.getAnimalUtils().updateAnimalSpawns(Season.WINTER, var7);
                                    }

                                    this.checkedAtleastOnce.add(new SeasonChunk(var7.getWorld().getName(), var7.getX(), var7.getZ(), 0L));
                                    var20 = true;
                                 }

                                 if (this.main.hasBlockChanges(var12.getX(), var12.getZ(), var12.getWorld()) && var18 >= this.firstSnowfall && this.main.getBlockUtils().affectFlora(var11) && this.main.getSettings().snowPlacement && !this.main.getBlockStorage().isStored(var11.getRelative(0, 1, 0).getLocation(), StoredBlockType.FLOWER)) {
                                    this.main.getNMSUtils().setBlockInNMSChunk(var11.getWorld(), var11.getX(), var11.getY() + 1, var11.getZ(), this.snowID, (byte)0, false);
                                    this.main.getNMSUtils().setBlockInNMSChunk(var11.getWorld(), var11.getX(), var11.getY() + 2, var11.getZ(), 0, (byte)0, false);
                                    if (var20) {
                                       this.main.getAsyncChunkHandler().logChunk(new SeasonChunk(var12.getWorld().getName(), var12.getX(), var12.getZ(), 0L));
                                    }
                                 }
                              } else if (var11.getRelative(0, 1, 0).getType() == Material.SNOW && var1.hasStorm() && !this.main.getSeasonManager().refreshers.containsKey(var1) && this.snowLayers > 1) {
                                 if (!this.checkedAtleastOnce.contains(new SeasonChunk(var7.getWorld().getName(), var7.getX(), var7.getZ(), 0L))) {
                                    if (this.main.getSettings().spawnEntities && this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_MOB_SPAWNING, var1)) {
                                       this.main.getAnimalUtils().updateAnimalSpawns(Season.WINTER, var7);
                                    }

                                    this.checkedAtleastOnce.add(new SeasonChunk(var7.getWorld().getName(), var7.getX(), var7.getZ(), 0L));
                                 }

                                 if (this.main.hasBlockChanges(var12.getX(), var12.getZ(), var12.getWorld()) && var18 >= this.firstSnowfall && this.main.getBlockUtils().affectFlora(var11) && this.main.getSettings().snowPlacement) {
                                    Snow var19 = (Snow)var11.getRelative(0, 1, 0).getBlockData();
                                    if (var19.getLayers() < var19.getMaximumLayers() && var19.getLayers() < this.snowLayers) {
                                       var19.setLayers(var19.getLayers() + 1);
                                    }

                                    var11.getRelative(0, 1, 0).setBlockData(var19);
                                 }
                              }
                           } else if (((Levelled)var11.getBlockData()).getLevel() == 0 && this.main.hasBlockChanges(var12.getX(), var12.getZ(), var12.getWorld()) && this.main.getSettings().spawnIceInWinter) {
                              if (var18 >= 2) {
                                 this.main.getNMSUtils().setBlockInNMSChunk(var11.getWorld(), var11.getX(), var11.getY(), var11.getZ(), this.iceID, (byte)0, false);
                                 this.main.getAsyncChunkHandler().logChunk(new SeasonChunk(var12.getWorld().getName(), var12.getX(), var12.getZ(), 0L));
                              } else {
                                 byte var13 = 2;
                                 if (var18 == 0) {
                                    var13 = 4;
                                 }

                                 if (JavaUtils.getRandom().nextInt(var13) == 0) {
                                    boolean var14 = false;

                                    for(int var15 = 0; var15 < this.sideFaces.length; ++var15) {
                                       Material var16 = var11.getRelative(this.sideFaces[var15]).getType();
                                       if (var16 != Material.WATER && var16 != Material.SEAGRASS && var16 != Material.TALL_SEAGRASS && var16 != Material.KELP_PLANT) {
                                          var14 = true;
                                          break;
                                       }
                                    }

                                    if (var14) {
                                       this.main.getNMSUtils().setBlockInNMSChunk(var11.getWorld(), var11.getX(), var11.getY(), var11.getZ(), this.iceID, (byte)0, false);
                                       this.main.getAsyncChunkHandler().logChunk(new SeasonChunk(var12.getWorld().getName(), var12.getX(), var12.getZ(), 0L));
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }

                  if (this.currentIndexSnow < 255) {
                     ++this.currentIndexSnow;
                  } else {
                     this.currentIndexSnow = 0;
                  }
                  break;
               }
            }
         } else {
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
