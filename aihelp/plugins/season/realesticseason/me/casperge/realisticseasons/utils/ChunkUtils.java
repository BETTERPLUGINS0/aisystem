package me.casperge.realisticseasons.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.Version;
import me.casperge.realisticseasons.blockscanner.blocksaver.StoredBlockType;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class ChunkUtils {
   RealisticSeasons main;
   public static List<Material> naturalplants;
   public static List<Material> flowers;
   private final List<Material> logPlaceAndBreak;
   private final List<Material> logs;
   private int waterID;
   public static List<String> affectflora;
   public static List<String> affectinwinter;

   public ChunkUtils(RealisticSeasons var1) {
      this.logPlaceAndBreak = new ArrayList(Arrays.asList(Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.ORANGE_TULIP, Material.PINK_TULIP, Material.RED_TULIP, Material.WHITE_TULIP, Material.OXEYE_DAISY, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.SWEET_BERRY_BUSH));
      this.logs = Arrays.asList(Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.SPRUCE_LOG, Material.DARK_OAK_LOG);
      this.main = var1;
      this.waterID = Version.getWaterID();
   }

   public static boolean isChunkLoaded(Location var0) {
      int var1 = var0.getBlockX() >> 4;
      int var2 = var0.getBlockZ() >> 4;
      return var0.getWorld().isChunkLoaded(var1, var2);
   }

   public static Collection<Chunk> around(Chunk var0, int var1) {
      World var2 = var0.getWorld();
      HashSet var3 = new HashSet();
      int var4 = var0.getX();
      int var5 = var0.getZ();

      for(int var6 = -var1; var6 <= var1; ++var6) {
         for(int var7 = -var1; var7 <= var1; ++var7) {
            if (!(get2dDistance(var4, var5, var4 + var6, var5 + var7) > (double)var1) && var4 + var6 <= 1875000 && var5 + var7 <= 1875000 && var4 + var6 >= -1875000 && var5 + var7 >= -1875000 && var2.isChunkLoaded(var4 + var6, var5 + var7)) {
               var3.add(var2.getChunkAt(var4 + var6, var5 + var7));
            }
         }
      }

      return var3;
   }

   public static Collection<Chunk> around(Chunk var0, int var1, int var2) {
      World var3 = var0.getWorld();
      ArrayList var4 = new ArrayList();
      int var5 = var0.getX();
      int var6 = var0.getZ();

      for(int var7 = -var1; var7 <= var1; ++var7) {
         for(int var8 = -var1; var8 <= var1; ++var8) {
            if (!(get2dDistance(var5, var6, var5 + var7, var6 + var8) > (double)var1) && var5 + var7 <= 1875000 && var6 + var8 <= 1875000 && var5 + var7 >= -1875000 && var6 + var8 >= -1875000 && var3.isChunkLoaded(var5 + var7, var6 + var8)) {
               var4.add(var3.getChunkAt(var5 + var7, var6 + var8));
            }
         }
      }

      ArrayList var12 = new ArrayList();
      if (var4.size() <= var2) {
         return var4;
      } else {
         Random var13 = new Random();
         int var9 = var2;
         if (var4.size() < var2) {
            var9 = var4.size();
         }

         for(int var10 = 0; var10 < var9; ++var10) {
            Chunk var11 = (Chunk)var4.get(var13.nextInt(var4.size()));
            var12.add(var11);
         }

         return var12;
      }
   }

   public static Collection<String> aroundString(int var0, int var1, String var2, int var3) {
      int var4 = var3 * 2 + 2;
      HashSet var5 = new HashSet(var4 * var4);

      for(int var6 = -var3; var6 <= var3; ++var6) {
         for(int var7 = -var3; var7 <= var3; ++var7) {
            var5.add(var0 + var6 + "," + (var1 + var7) + "," + var2);
         }
      }

      return var5;
   }

   public static Chunk chunkFromString(String var0) {
      String[] var1 = var0.split(",");
      int var2 = Integer.valueOf(var1[0]);
      int var3 = Integer.valueOf(var1[1]);
      World var4 = Bukkit.getWorld(var1[2]);
      return var2 <= 1875000 && var3 <= 1875000 && var2 >= -1875000 && var3 >= -1875000 ? var4.getChunkAt(var2, var3) : null;
   }

   public static boolean isChunkLoadedString(String var0) {
      String[] var1 = var0.split(",");
      int var2 = Integer.valueOf(var1[0]);
      int var3 = Integer.valueOf(var1[1]);
      World var4 = Bukkit.getWorld(var1[2]);
      return var4.isChunkLoaded(var2, var3);
   }

   public HashMap<Material, Integer> getChunkPopulation(Chunk var1) {
      HashMap var2 = new HashMap();

      for(int var3 = 0; var3 <= 15; ++var3) {
         for(int var4 = 0; var4 <= 15; ++var4) {
            Block var5 = var1.getWorld().getHighestBlockAt(var1.getX() * 16 + var3, var1.getZ() * 16 + var4);
            Block var6 = var5.getRelative(0, 1, 0);
            boolean var7 = false;
            if (naturalplants.contains(var6.getType())) {
               var7 = true;
               if (this.logPlaceAndBreak.contains(var6.getType()) && this.main.getBlockStorage().isStored(var6.getLocation(), StoredBlockType.FLOWER)) {
                  continue;
               }

               if (var2.containsKey(var6.getType())) {
                  var2.put(var6.getType(), (Integer)var2.get(var6.getType()) + 1);
               } else {
                  var2.put(var6.getType(), 1);
               }
            }

            if (var6.getRelative(0, -1, 0).getType() == Material.GRASS_BLOCK && var6.getType() != Material.SNOW) {
               var7 = true;
               if (var2.containsKey(Material.GRASS_BLOCK)) {
                  var2.put(Material.GRASS_BLOCK, (Integer)var2.get(Material.GRASS_BLOCK) + 1);
               } else {
                  var2.put(Material.GRASS_BLOCK, 1);
               }
            }

            if (!var7 && var5.getType().toString().contains("LEAVE")) {
               for(int var8 = -1; var8 + var5.getLocation().getBlockY() > this.main.getNMSUtils().getMinHeight(var5.getLocation().getWorld()) + 1; --var8) {
                  Block var9 = var5.getRelative(0, var8, 0);
                  Block var10 = var9.getRelative(0, 1, 0);
                  if (naturalplants.contains(var10.getType())) {
                     var7 = true;
                     if (this.logPlaceAndBreak.contains(var10.getType()) && this.main.getBlockStorage().isStored(var10.getLocation(), StoredBlockType.FLOWER)) {
                        continue;
                     }

                     if (var2.containsKey(var10.getType())) {
                        var2.put(var10.getType(), (Integer)var2.get(var10.getType()) + 1);
                     } else {
                        var2.put(var10.getType(), 1);
                     }
                  }

                  if (var10.getRelative(0, -1, 0).getType() == Material.GRASS_BLOCK && var10.getType() != Material.SNOW) {
                     var7 = true;
                     if (var2.containsKey(Material.GRASS_BLOCK)) {
                        var2.put(Material.GRASS_BLOCK, (Integer)var2.get(Material.GRASS_BLOCK) + 1);
                     } else {
                        var2.put(Material.GRASS_BLOCK, 1);
                     }
                  }

                  if (var7 || !var5.getRelative(0, var8, 0).getType().toString().contains("LEAVE") && !this.logs.contains(var5.getRelative(0, var8, 0).getType()) && var5.getRelative(0, var8, 0).getType() != Material.AIR && var5.getRelative(0, var8, 0).getType() != Material.VINE && !naturalplants.contains(var9.getType())) {
                     break;
                  }
               }
            }
         }
      }

      return var2;
   }

   public List<Block> getPlantBlocks(Chunk var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 <= 15; ++var3) {
         for(int var4 = 0; var4 <= 15; ++var4) {
            Block var5 = var1.getWorld().getHighestBlockAt(var1.getX() * 16 + var3, var1.getZ() * 16 + var4);
            Block var6 = var5.getRelative(0, 1, 0);
            if (naturalplants.contains(var6.getType())) {
               if (!this.logPlaceAndBreak.contains(var6.getType()) || !this.main.getBlockStorage().isStored(var6.getLocation(), StoredBlockType.FLOWER)) {
                  var2.add(var6);
               }
            } else if (var5.getType().toString().contains("LEAVE")) {
               for(int var7 = -1; var7 + var5.getLocation().getBlockY() > this.main.getNMSUtils().getMinHeight(var5.getLocation().getWorld()) + 1; --var7) {
                  if (naturalplants.contains(var5.getRelative(0, var7, 0).getType())) {
                     Block var8 = var5.getRelative(0, var7, 0);
                     if (naturalplants.contains(var8.getRelative(0, -1, 0).getType())) {
                        var8 = var8.getRelative(0, -1, 0);
                     }

                     if (!this.main.getBlockStorage().isStored(var8.getLocation(), StoredBlockType.FLOWER)) {
                        var2.add(var8);
                     }
                     break;
                  }

                  if (!var5.getRelative(0, var7, 0).getType().toString().contains("LEAVE") && !this.logs.contains(var5.getRelative(0, var7, 0).getType()) && var5.getRelative(0, var7, 0).getType() != Material.AIR && var5.getRelative(0, var7, 0).getType() != Material.VINE) {
                     break;
                  }
               }
            }
         }
      }

      return var2;
   }

   public List<Block> getGrassBlocks(Chunk var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 <= 15; ++var3) {
         for(int var4 = 0; var4 <= 15; ++var4) {
            Block var5 = var1.getWorld().getHighestBlockAt(var1.getX() * 16 + var3, var1.getZ() * 16 + var4);
            Block var6 = var5.getRelative(0, 1, 0);
            if (var6.getType() != Material.GRASS && var6.getType() != Material.TALL_GRASS) {
               if (var5.getType().toString().contains("LEAVE")) {
                  for(int var7 = -1; var7 + var5.getLocation().getBlockY() > this.main.getNMSUtils().getMinHeight(var5.getLocation().getWorld()) + 1; --var7) {
                     if (var5.getRelative(0, var7, 0).getType() == Material.GRASS || var5.getRelative(0, var7, 0).getType() == Material.TALL_GRASS) {
                        if (var5.getRelative(0, var7, 0).getType() == Material.GRASS) {
                           var2.add(var5.getRelative(0, var7, 0));
                        } else if (var5.getRelative(0, var7 - 1, 0).getType() == Material.TALL_GRASS) {
                           var2.add(var5.getRelative(0, var7 - 1, 0));
                        } else {
                           var2.add(var5.getRelative(0, var7, 0));
                        }
                        break;
                     }

                     if (!var5.getRelative(0, var7, 0).getType().toString().contains("LEAVE") && !this.logs.contains(var5.getRelative(0, var7, 0).getType()) && var5.getRelative(0, var7, 0).getType() != Material.AIR && var5.getRelative(0, var7, 0).getType() != Material.VINE) {
                        break;
                     }
                  }
               }
            } else {
               var2.add(var6);
            }
         }
      }

      return var2;
   }

   public List<Block> getGrowableBlocks(Chunk var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 <= 15; ++var3) {
         for(int var4 = 0; var4 <= 15; ++var4) {
            Block var5 = var1.getWorld().getHighestBlockAt(var1.getBlock(var3, 60, var4).getLocation());
            if (var5.getType() == Material.GRASS_BLOCK && var5.getRelative(BlockFace.UP).getType() == Material.AIR) {
               var2.add(var5.getRelative(0, 1, 0));
            } else if (var5.getType().toString().contains("LEAVE")) {
               for(int var6 = -1; var6 + var5.getLocation().getBlockY() > this.main.getNMSUtils().getMinHeight(var5.getLocation().getWorld()); --var6) {
                  if (var5.getRelative(0, var6, 0).getType() == Material.GRASS_BLOCK && var5.getRelative(0, var6, 0).getRelative(BlockFace.UP).getType() == Material.AIR) {
                     var2.add(var5.getRelative(0, var6 + 1, 0));
                     break;
                  }

                  if (!var5.getRelative(0, var6, 0).getType().toString().contains("LEAVE") && !this.logs.contains(var5.getRelative(0, var6, 0).getType()) && var5.getRelative(0, var6, 0).getType() != Material.AIR && var5.getRelative(0, var6, 0).getType() != Material.VINE) {
                     break;
                  }
               }
            }
         }
      }

      return var2;
   }

   public int[] getTotalPlantPopulation(Chunk var1) {
      HashMap var2 = this.getChunkPopulation(var1);
      int var3 = 0;
      int var4 = 0;
      int var5 = 0;
      int var6 = 0;
      int var7 = 0;
      int var8 = 0;
      Iterator var9 = var2.entrySet().iterator();

      while(true) {
         while(true) {
            while(var9.hasNext()) {
               Entry var10 = (Entry)var9.next();
               if (var10.getKey() != Material.GRASS && var10.getKey() != Material.TALL_GRASS) {
                  if (flowers.contains(var10.getKey())) {
                     var4 += (Integer)var10.getValue();
                  } else if (var10.getKey() == Material.GRASS_BLOCK) {
                     var5 += (Integer)var10.getValue();
                  } else if (var10.getKey() != Material.SNOW && var10.getKey() != Material.ICE) {
                     if (var10.getKey() == Material.SWEET_BERRY_BUSH) {
                        var7 += (Integer)var10.getValue();
                     } else if (var10.getKey() == Material.RED_MUSHROOM || var10.getKey() == Material.BROWN_MUSHROOM) {
                        var8 += (Integer)var10.getValue();
                     }
                  } else {
                     var6 += (Integer)var10.getValue();
                  }
               } else {
                  var3 += (Integer)var10.getValue();
               }
            }

            int[] var11 = new int[]{var3, var4, var5, var6, var7, var8};
            return var11;
         }
      }
   }

   public boolean checkPopulation(Chunk var1, float var2, float var3, int var4, int var5, Season var6, boolean var7) {
      if (Version.is_1_21_5_or_up() && var6 != Season.FALL && var6 != Season.RESTORE && this.main.getSettings().generateLeafLitter) {
         this.main.getLitterGeneration().removeLeafLitter(var1);
      }

      int[] var8 = this.getTotalPlantPopulation(var1);
      if (var8[3] != 0) {
         return false;
      } else {
         int var9 = Math.round((float)var8[2] / var2);
         int var10 = Math.round((float)var8[2] * (var3 / 100.0F));
         int var11 = Math.round((float)(var8[2] / 6));
         if (var8[1] - var5 > var10) {
            if (this.main.getSettings().flowerchanceinspring > 0.08F) {
               this.reducePopulation(var1, var10, var8[1], ChunkUtils.PlantType.FLOWER);
               var7 = true;
            }
         } else if (var8[1] + var5 < var10) {
            this.increasePopulation(var1, var10, var8[1], ChunkUtils.PlantType.FLOWER);
         }

         int var12 = Math.abs(var1.getX()) % 10;
         int var13 = Math.abs(var1.getZ()) % 10;
         if ((var12 != 1 || var13 != 2) && (var12 != 9 || var13 != 3) && (var12 != 5 || var13 != 8) && (var12 != 9 || var13 != 9) && (var12 != 2 || var13 != 6)) {
            if ((var12 == 2 && var13 == 1 || var12 == 3 && var13 == 9 || var12 == 8 && var13 == 5 || var12 == 9 && var13 == 7 || var12 == 6 && var13 == 2) && this.main.getSettings().spawnMushroomsInFall) {
               if (var6 == Season.FALL) {
                  if (var8[5] < 1) {
                     this.increasePopulation(var1, var11, 0, ChunkUtils.PlantType.MUSHROOM);
                  }
               } else if (var8[5] > 0) {
                  this.reducePopulation(var1, 0, var8[5], ChunkUtils.PlantType.MUSHROOM);
                  var7 = true;
               }
            }
         } else if (var6 == Season.SUMMER) {
            if (var8[4] < 1) {
               if (var11 > this.main.getSettings().maxBerryBushes) {
                  var11 = this.main.getSettings().maxBerryBushes;
               }

               this.increasePopulation(var1, var11, 0, ChunkUtils.PlantType.BERRY);
            }
         } else if (var8[4] > 0) {
            this.reducePopulation(var1, 0, var8[4], ChunkUtils.PlantType.BERRY);
            var7 = true;
         }

         if (var7) {
            if (var8[0] - var4 > var9) {
               this.reducePopulation(var1, var9, var8[0], ChunkUtils.PlantType.GRASS);
            } else if (var8[0] + var4 < var9) {
               this.increasePopulation(var1, var9, var8[0], ChunkUtils.PlantType.GRASS);
            }
         }

         return true;
      }
   }

   public void reducePopulation(Chunk var1, int var2, int var3, ChunkUtils.PlantType var4) {
      Random var5 = new Random();
      List var6;
      Block var7;
      if (var4 == ChunkUtils.PlantType.GRASS) {
         var6 = this.getGrassBlocks(var1);

         while(var3 > var2) {
            var7 = (Block)var6.get(var5.nextInt(var6.size()));
            var6.remove(var7);
            if (var7.getType() == Material.TALL_GRASS) {
               var7.setType(Material.AIR, false);
               var7.getRelative(0, 1, 0).setType(Material.AIR, false);
               --var3;
            } else {
               var7.setType(Material.AIR, false);
               --var3;
            }
         }
      } else if (var4 == ChunkUtils.PlantType.FLOWER) {
         var6 = this.getPlantBlocks(var1);

         while(var3 > var2 && var6.size() > 0) {
            var7 = (Block)var6.get(var5.nextInt(var6.size()));
            var6.remove(var7);
            if (flowers.contains(var7.getType())) {
               var7.setType(Material.AIR, false);
               --var3;
            }
         }
      } else if (var4 == ChunkUtils.PlantType.BERRY) {
         var6 = this.getPlantBlocks(var1);

         while(var3 > var2 && var6.size() > 0) {
            var7 = (Block)var6.get(var5.nextInt(var6.size()));
            var6.remove(var7);
            if (var7.getType() == Material.SWEET_BERRY_BUSH) {
               var7.setType(Material.AIR, false);
               --var3;
            }
         }
      } else if (var4 == ChunkUtils.PlantType.MUSHROOM) {
         var6 = this.getPlantBlocks(var1);

         while(true) {
            do {
               if (var3 <= var2 || var6.size() <= 0) {
                  return;
               }

               var7 = (Block)var6.get(var5.nextInt(var6.size()));
               var6.remove(var7);
            } while(var7.getType() != Material.RED_MUSHROOM && var7.getType() != Material.BROWN_MUSHROOM);

            var7.setType(Material.AIR, false);
            --var3;
         }
      }

   }

   public void increasePopulation(Chunk var1, int var2, int var3, ChunkUtils.PlantType var4) {
      List var5 = this.getGrowableBlocks(var1);
      Random var6 = new Random();
      Block var7;
      if (var4 == ChunkUtils.PlantType.GRASS) {
         while(var3 < var2 && var5.size() > 0) {
            var7 = (Block)var5.get(var6.nextInt(var5.size()));
            var5.remove(var7);
            var7.setType(Material.GRASS, false);
            ++var3;
         }
      } else if (var4 == ChunkUtils.PlantType.FLOWER) {
         while(var3 < var2 && var5.size() > 0) {
            var7 = (Block)var5.get(var6.nextInt(var5.size()));
            var5.remove(var7);
            var7.setType((Material)flowers.get(var6.nextInt(flowers.size())), false);
            ++var3;
         }
      } else if (var4 == ChunkUtils.PlantType.BERRY) {
         while(var3 < var2 && var5.size() > 0) {
            var7 = (Block)var5.get(var6.nextInt(var5.size()));
            var5.remove(var7);
            if (get2dDistanceFromCenter(var7, var1) < 8.0D) {
               var7.setType(Material.SWEET_BERRY_BUSH, false);
               ++var3;
            }
         }
      } else if (var4 == ChunkUtils.PlantType.MUSHROOM) {
         while(var3 < var2 && var5.size() > 0) {
            var7 = (Block)var5.get(var6.nextInt(var5.size()));
            var5.remove(var7);
            if (get2dDistanceFromCenter(var7, var1) < 8.0D) {
               if (var6.nextInt(2) == 0) {
                  var7.setType(Material.BROWN_MUSHROOM, false);
               } else {
                  var7.setType(Material.RED_MUSHROOM, false);
               }

               ++var3;
            }
         }
      }

   }

   public static double get2dDistanceFromCenter(Block var0, Chunk var1) {
      Location var2 = (new Location(var1.getWorld(), (double)(var1.getX() << 4), 64.0D, (double)(var1.getZ() << 4))).add(7.0D, 0.0D, 7.0D);
      int var3 = Math.abs(var2.getBlockX());
      int var4 = Math.abs(var2.getBlockZ());
      int var5 = Math.abs(var0.getX());
      int var6 = Math.abs(var0.getZ());
      return Math.sqrt((double)((var5 - var3) * (var5 - var3) + (var6 - var4) * (var6 - var4)));
   }

   public static double get2dDistance(Block var0, Block var1) {
      int var2 = Math.abs(var1.getX());
      int var3 = Math.abs(var1.getZ());
      int var4 = Math.abs(var0.getX());
      int var5 = Math.abs(var0.getZ());
      return Math.sqrt((double)((var4 - var2) * (var4 - var2) + (var5 - var3) * (var5 - var3)));
   }

   public static double get2dDistance(int var0, int var1, int var2, int var3) {
      return Math.sqrt((double)((var2 - var0) * (var2 - var0) + (var3 - var1) * (var3 - var1)));
   }

   public boolean affectFlora(Chunk var1) {
      String var2 = this.main.getNMSUtils().getBiomeName(var1.getX() * 16, var1.getWorld().getHighestBlockYAt(var1.getX() * 16, var1.getZ() * 16), var1.getZ() * 16, var1.getWorld());
      return !checkBiome(ChunkUtils.BiomeType.AFFECTFLORA, var2);
   }

   public boolean dontAffectInWinter(int var1, int var2, World var3) {
      String var4 = this.main.getNMSUtils().getBiomeName(var1 * 16, var3.getHighestBlockYAt(var1 * 16, var2 * 16), var2 * 16, var3);
      return checkBiome(ChunkUtils.BiomeType.AFFECTINWINTER, var4);
   }

   public boolean unfreezeChunk(Chunk var1) {
      boolean var2 = false;
      World var3 = var1.getWorld();
      if (!this.main.hasBlockChanges(var1.getX(), var1.getZ(), var1.getWorld())) {
         return false;
      } else {
         for(int var4 = 0; var4 <= 15; ++var4) {
            for(int var5 = 0; var5 <= 15; ++var5) {
               int var6 = var4 + var1.getX() * 16;
               int var7 = var5 + var1.getZ() * 16;
               Block var8 = var3.getHighestBlockAt(var6, var7);
               int var9;
               if (var8.getRelative(0, 1, 0).getType() == Material.SNOW && this.main.getSettings().snowRemoval) {
                  var9 = var8.getY() + 1;
                  if (this.main.getBlockUtils().affectBlockInWinter(var3, var6, var9, var7)) {
                     this.main.getNMSUtils().setBlockInNMSChunk(var3, var6, var9, var7, 0, (byte)0, false);
                     if (!var2) {
                        var2 = true;
                     }
                  }
               } else if (var8.getType() == Material.ICE && this.main.getBlockUtils().affectBlockInWinter(var3, var6, var8.getY(), var7)) {
                  this.main.getNMSUtils().setBlockInNMSChunk(var3, var6, var8.getY(), var7, this.waterID, (byte)0, false);
                  if (!var2) {
                     var2 = true;
                  }
               }

               if (var8.getType().toString().contains("LEAVE")) {
                  for(var9 = -1; var9 + var8.getLocation().getBlockY() > this.main.getNMSUtils().getMinHeight(var8.getLocation().getWorld()); --var9) {
                     if (var8.getRelative(0, var9, 0).getType() == Material.SNOW) {
                        if (this.main.getBlockUtils().affectBlockInWinter(var3, var6, var8.getLocation().getBlockY() + var9, var7)) {
                           this.main.getNMSUtils().setBlockInNMSChunk(var3, var6, var8.getLocation().getBlockY() + var9, var7, 0, (byte)0, false);
                           if (!var2) {
                              var2 = true;
                           }
                        }
                        break;
                     }

                     if (!var8.getRelative(0, var9, 0).getType().toString().contains("LEAVE") && !this.logs.contains(var8.getRelative(0, var9, 0).getType()) && var8.getRelative(0, var9, 0).getType() != Material.AIR && var8.getRelative(0, var9, 0).getType() != Material.VINE) {
                        break;
                     }
                  }
               }
            }
         }

         return var2;
      }
   }

   public boolean unfreezeChunk(Chunk var1, double var2) {
      boolean var4 = false;
      World var5 = var1.getWorld();
      if (!this.main.hasBlockChanges(var1.getX(), var1.getZ(), var1.getWorld())) {
         return false;
      } else {
         for(int var6 = 0; var6 <= 15; ++var6) {
            for(int var7 = 0; var7 <= 15; ++var7) {
               int var8 = var6 + var1.getX() * 16;
               int var9 = var7 + var1.getZ() * 16;
               Block var10 = var5.getHighestBlockAt(var8, var9);
               if (!(JavaUtils.getRandom().nextDouble() > var2)) {
                  int var11;
                  if (var10.getRelative(0, 1, 0).getType() == Material.SNOW && this.main.getSettings().snowRemoval) {
                     var11 = var10.getY() + 1;
                     if (this.main.getBlockUtils().affectBlockInWinter(var5, var8, var11, var9)) {
                        this.main.getNMSUtils().setBlockInNMSChunk(var5, var8, var11, var9, 0, (byte)0, false);
                        if (!var4) {
                           var4 = true;
                        }
                     }
                  } else if (var10.getType() == Material.ICE && this.main.getBlockUtils().affectBlockInWinter(var5, var8, var10.getY(), var9)) {
                     this.main.getNMSUtils().setBlockInNMSChunk(var5, var8, var10.getY(), var9, this.waterID, (byte)0, false);
                     if (!var4) {
                        var4 = true;
                     }
                  }

                  if (var10.getType().toString().contains("LEAVE")) {
                     for(var11 = -1; var11 + var10.getLocation().getBlockY() > this.main.getNMSUtils().getMinHeight(var10.getLocation().getWorld()); --var11) {
                        if (var10.getRelative(0, var11, 0).getType() == Material.SNOW) {
                           if (this.main.getBlockUtils().affectBlockInWinter(var5, var8, var10.getLocation().getBlockY() + var11, var9)) {
                              this.main.getNMSUtils().setBlockInNMSChunk(var5, var8, var10.getLocation().getBlockY() + var11, var9, 0, (byte)0, false);
                              if (!var4) {
                                 var4 = true;
                              }
                           }
                           break;
                        }

                        if (!var10.getRelative(0, var11, 0).getType().toString().contains("LEAVE") && !this.logs.contains(var10.getRelative(0, var11, 0).getType()) && var10.getRelative(0, var11, 0).getType() != Material.AIR && var10.getRelative(0, var11, 0).getType() != Material.VINE) {
                           break;
                        }
                     }
                  }
               }
            }
         }

         return var4;
      }
   }

   public static Class<?> getCbClass(String var0) {
      return Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + var0);
   }

   public static List<int[]> generateChunkPackets() {
      Random var0 = new Random();
      ArrayList var1 = new ArrayList();
      int[] var2 = new int[1024];
      int[] var3 = new int[1024];
      int[] var4 = new int[1024];
      int[] var5 = new int[1024];
      int[] var6 = new int[1024];
      byte var7 = 0;
      byte var8 = 1;
      byte var9 = 2;
      byte var10 = 3;
      byte var11 = 4;
      Arrays.fill(var2, var7);
      Arrays.fill(var3, var8);
      Arrays.fill(var4, var9);
      Arrays.fill(var5, var10);
      Arrays.fill(var6, var11);
      var1.add(var2);
      var1.add(var3);
      var1.add(var4);
      var1.add(var5);
      var1.add(var6);

      for(int var12 = 65; var12 < 959; ++var12) {
         int var13 = var0.nextInt(20);
         if (var13 == 1) {
            int var14 = var0.nextInt(5);
            byte var15;
            if (var14 == 0) {
               var15 = var7;
            } else if (var14 == 1) {
               var15 = var8;
            } else if (var14 == 2) {
               var15 = var9;
            } else if (var14 == 3) {
               var15 = var10;
            } else {
               var15 = var11;
            }

            int var16 = var0.nextInt(var1.size());
            addToList((int[])var1.get(var16), var12, var15);
         }
      }

      return var1;
   }

   public static int[] addToList(int[] var0, int var1, int var2) {
      var0[var1] = var2;
      var0[var1 - 1] = var2;
      var0[var1 + 1] = var2;
      var0[var1 + 4] = var2;
      var0[var1 - 4] = var2;
      var0[var1 - 16] = var2;
      var0[var1 - 17] = var2;
      var0[var1 - 15] = var2;
      var0[var1 - 12] = var2;
      var0[var1 - 20] = var2;
      var0[var1 + 16] = var2;
      var0[var1 + 15] = var2;
      var0[var1 + 17] = var2;
      var0[var1 + 21] = var2;
      var0[var1 + 12] = var2;
      return var0;
   }

   public static boolean checkBiome(ChunkUtils.BiomeType var0, String var1) {
      Iterator var2 = getBiomeList(var0).iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (String)var2.next();
      } while(!var1.toLowerCase().equals(var3.toLowerCase()));

      return true;
   }

   public static List<String> getBiomeList(ChunkUtils.BiomeType var0) {
      switch(var0.ordinal()) {
      case 0:
         return affectflora;
      case 1:
         return affectinwinter;
      default:
         return null;
      }
   }

   public void getId(Material var1, Player var2) {
      for(int var3 = 0; var3 < 10000; ++var3) {
         this.main.getNMSUtils().setBlockInNMSChunk(var2.getWorld(), var2.getLocation().getBlockX(), var2.getLocation().getBlockY(), var2.getLocation().getBlockZ(), var3, (byte)0, false);
         if (var2.getLocation().getBlock().getType() == var1) {
            var2.sendMessage("Found: " + String.valueOf(var3));
            break;
         }
      }

   }

   static {
      naturalplants = new ArrayList(Arrays.asList(Material.GRASS, Material.TALL_GRASS, Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.ORANGE_TULIP, Material.PINK_TULIP, Material.RED_TULIP, Material.WHITE_TULIP, Material.OXEYE_DAISY, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.SWEET_BERRY_BUSH));
      flowers = new ArrayList(Arrays.asList(Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.ORANGE_TULIP, Material.PINK_TULIP, Material.RED_TULIP, Material.WHITE_TULIP, Material.OXEYE_DAISY, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY));
      affectflora = new ArrayList();
      affectinwinter = new ArrayList();
   }

   public static enum PlantType {
      GRASS,
      FLOWER,
      BERRY,
      MUSHROOM;

      // $FF: synthetic method
      private static ChunkUtils.PlantType[] $values() {
         return new ChunkUtils.PlantType[]{GRASS, FLOWER, BERRY, MUSHROOM};
      }
   }

   public static enum BiomeType {
      AFFECTFLORA,
      AFFECTINWINTER;

      // $FF: synthetic method
      private static ChunkUtils.BiomeType[] $values() {
         return new ChunkUtils.BiomeType[]{AFFECTFLORA, AFFECTINWINTER};
      }
   }

   public static enum FallChunkType {
      GROUNDONLY,
      ALL,
      NONE;

      // $FF: synthetic method
      private static ChunkUtils.FallChunkType[] $values() {
         return new ChunkUtils.FallChunkType[]{GROUNDONLY, ALL, NONE};
      }
   }
}
