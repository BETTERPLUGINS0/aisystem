package me.casperge.realisticseasons.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.blockscanner.blocksaver.StoredBlockType;
import me.casperge.realisticseasons1_21_R4.NmsCode_21_R4;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class LitterGeneration {
   private RealisticSeasons main;
   private List<Material> destroyForSnow = new ArrayList();
   private final List<Material> logs;

   public LitterGeneration(RealisticSeasons var1) {
      this.logs = Arrays.asList(Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.SPRUCE_LOG, Material.DARK_OAK_LOG);
      this.main = var1;
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
   }

   public List<Block> getLeafLitterBlocks(Chunk var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 <= 15; ++var3) {
         for(int var4 = 0; var4 <= 15; ++var4) {
            Block var5 = var1.getWorld().getHighestBlockAt(var1.getX() * 16 + var3, var1.getZ() * 16 + var4).getRelative(0, 1, 0);
            Block var6 = var1.getWorld().getHighestBlockAt(var1.getX() * 16 + var3, var1.getZ() * 16 + var4);
            if (NmsCode_21_R4.isLeafLitter(var5.getType())) {
               if (!this.main.getBlockStorage().isStored(var5.getLocation(), StoredBlockType.FLOWER)) {
                  var2.add(var5);
               }
            } else if (var6.getType().toString().contains("LEAVE")) {
               for(int var7 = -1; var7 + var6.getLocation().getBlockY() > this.main.getNMSUtils().getMinHeight(var6.getLocation().getWorld()); --var7) {
                  if (NmsCode_21_R4.isLeafLitter(var6.getRelative(0, var7, 0).getType())) {
                     if (!this.main.getBlockStorage().isStored(var6.getRelative(0, var7, 0).getLocation(), StoredBlockType.FLOWER)) {
                        var2.add(var6.getRelative(0, var7, 0));
                     }
                     break;
                  }

                  if (!var6.getRelative(0, var7, 0).getType().toString().contains("LEAVE") && !this.logs.contains(var6.getRelative(0, var7, 0).getType()) && var6.getRelative(0, var7, 0).getType() != Material.AIR && var6.getRelative(0, var7, 0).getType() != Material.VINE) {
                     break;
                  }
               }
            }
         }
      }

      return var2;
   }

   public void removeLeafLitter(Chunk var1) {
      List var2 = this.getLeafLitterBlocks(var1);
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Block var4 = (Block)var3.next();
         var4.setType(Material.AIR);
      }

   }

   public void spawnLeafLitter(Location var1) {
      if (var1.getWorld().isChunkLoaded(var1.getBlockX() >> 4, var1.getBlockZ() >> 4)) {
         Block var2 = var1.getWorld().getHighestBlockAt(var1);
         Material var3 = var1.getWorld().getHighestBlockAt(var1).getType();
         Material[] var4 = this.main.getSettings().spawnLeafLitterUnder;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Material var7 = var4[var6];
            if (var7 == var3) {
               int var8 = var1.getBlockX() >> 4;
               int var9 = var1.getBlockZ() >> 4;
               boolean var10 = true;

               int var12;
               for(int var11 = -1; var11 <= 1; ++var11) {
                  for(var12 = -1; var12 <= 1; ++var12) {
                     if (!var1.getWorld().isChunkLoaded(var8 + var11, var9 + var12)) {
                        var10 = false;
                     }
                  }
               }

               if (var10) {
                  boolean var26 = false;

                  for(var12 = -1; var12 + var2.getLocation().getBlockY() > this.main.getNMSUtils().getMinHeight(var2.getLocation().getWorld()) && var12 >= -30; --var12) {
                     if (NmsCode_21_R4.isLeafLitter(var2.getRelative(0, var12, 0).getType())) {
                        var26 = true;
                        break;
                     }

                     if (!ArrayUtils.contains(this.main.getSettings().spawnLeafLitterUnder, var2.getRelative(0, var12, 0).getType()) && !this.logs.contains(var2.getRelative(0, var12, 0).getType()) && var2.getRelative(0, var12, 0).getType() != Material.AIR && var2.getRelative(0, var12, 0).getType() != Material.VINE) {
                        if (this.destroyForSnow.contains(var2.getRelative(0, var12, 0).getType()) && this.main.getSettings().destroyPlantsForLeafLitter || this.main.getBlockUtils().isSnowable(var2.getRelative(0, var12, 0)) && var2.getRelative(0, var12 + 1, 0).getType() == Material.AIR) {
                           var26 = true;
                        }
                        break;
                     }
                  }

                  if (var26) {
                     for(var12 = -2; var12 <= 2; ++var12) {
                        for(int var13 = -2; var13 <= 2; ++var13) {
                           Block var14 = var1.getWorld().getHighestBlockAt(var1.getBlockX() + var12, var1.getBlockZ() + var13);
                           boolean var15 = ArrayUtils.contains(this.main.getSettings().spawnLeafLitterUnder, var14.getType());
                           Block var16 = null;
                           if (!NmsCode_21_R4.isLeafLitter(var14.getRelative(0, 1, 0).getType()) && (!this.destroyForSnow.contains(var14.getRelative(0, 1, 0).getType()) || !this.main.getSettings().destroyPlantsForLeafLitter) && (!this.main.getBlockUtils().isSnowable(var14) || var14.getRelative(0, 1, 0).getType() != Material.AIR || ArrayUtils.contains(this.main.getSettings().spawnLeafLitterUnder, var14.getType()))) {
                              if (var15) {
                                 for(int var17 = -1; var17 + var14.getLocation().getBlockY() > this.main.getNMSUtils().getMinHeight(var14.getLocation().getWorld()) && var17 >= -30; --var17) {
                                    if (NmsCode_21_R4.isLeafLitter(var14.getRelative(0, var17, 0).getType())) {
                                       var16 = var14.getRelative(0, var17, 0);
                                       break;
                                    }

                                    if (!ArrayUtils.contains(this.main.getSettings().spawnLeafLitterUnder, var14.getRelative(0, var17, 0).getType()) && !this.logs.contains(var14.getRelative(0, var17, 0).getType()) && var14.getRelative(0, var17, 0).getType() != Material.AIR && var14.getRelative(0, var17, 0).getType() != Material.VINE) {
                                       if (this.destroyForSnow.contains(var14.getRelative(0, var17, 0).getType()) && this.main.getSettings().destroyPlantsForLeafLitter) {
                                          var16 = var14.getRelative(0, var17, 0);
                                          break;
                                       }

                                       if (this.main.getBlockUtils().isSnowable(var14.getRelative(0, var17, 0)) && var14.getRelative(0, var17 + 1, 0).getType() == Material.AIR) {
                                          var16 = var14.getRelative(0, var17 + 1, 0);
                                       }
                                       break;
                                    }
                                 }
                              }
                           } else {
                              var15 = false;
                              var16 = var14.getRelative(0, 1, 0);
                           }

                           if (var16 != null) {
                              Random var27 = new Random(var16.getWorld().getSeed() % 6422400L + (long)(var16.getX() % 203 * 5234) + (long)(var16.getZ() % 3234 * 22345));
                              Random var18 = new Random(var16.getWorld().getSeed() % 53434223L + (long)(var16.getX() % 533 * 424) + (long)(var16.getZ() % 7422 * 732));
                              double var19 = var18.nextDouble();
                              byte var21;
                              if (var19 < 0.25D) {
                                 var21 = 1;
                              } else if (var19 < 0.5D) {
                                 var21 = 2;
                              } else if (var19 < 0.75D) {
                                 var21 = 3;
                              } else {
                                 var21 = 4;
                              }

                              double var22 = var27.nextDouble();
                              boolean var24 = false;
                              if (var15) {
                                 if (var22 < 0.5D) {
                                    var24 = true;
                                 }
                              } else if (Math.abs(var12) <= 1 && Math.abs(var13) <= 1) {
                                 if (var22 < 0.25D) {
                                    var24 = true;
                                 }
                              } else if (var22 < 0.125D) {
                                 var24 = true;
                              }

                              if (var24) {
                                 Material var25 = var16.getType();
                                 if ((this.destroyForSnow.contains(var25) || NmsCode_21_R4.isLeafLitter(var25)) && this.main.getBlockStorage().isStored(var16.getLocation(), StoredBlockType.FLOWER)) {
                                    break;
                                 }

                                 if (this.main.hasBlockChanges(var8, var9, var16.getWorld()) && this.main.hasSeasons(var8, var9, var16.getWorld())) {
                                    var16.setType(NmsCode_21_R4.getLeafLitter());
                                    NmsCode_21_R4.setLeafLitterCount(var16, var21);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }
}
