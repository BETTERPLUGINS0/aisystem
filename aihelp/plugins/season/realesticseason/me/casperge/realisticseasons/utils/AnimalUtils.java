package me.casperge.realisticseasons.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.Version;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.seasonevent.buildin.DefaultEventType;
import me.casperge.realisticseasons1_21_R2.NmsCode_21_R2;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Fox.Type;

public class AnimalUtils {
   private RealisticSeasons main;
   private static Random r = new Random();

   public AnimalUtils(RealisticSeasons var1) {
      this.main = var1;
   }

   public void checkRandomEntity(Season var1, World var2) {
      if (var2.getEntities().size() > 0) {
         List var3 = var2.getEntities();
         int var4 = 10;
         if (var3.size() < 10) {
            var4 = var3.size();
         }

         for(int var5 = 0; var5 < var4; ++var5) {
            Entity var6 = (Entity)var3.get(r.nextInt(var3.size()));
            this.checkEntity(var6, var1, false);
            var3.remove(var6);
         }
      }

   }

   public void removeAllAnimals(Season var1, World var2) {
      Iterator var3 = var2.getEntities().iterator();

      while(var3.hasNext()) {
         Entity var4 = (Entity)var3.next();
         this.checkEntity(var4, var1, false);
      }

   }

   public boolean checkEntity(Entity var1, Season var2, boolean var3) {
      if (var1.getScoreboardTags().contains("seasonal")) {
         if (!this.main.hasSeasons(var1.getLocation().getChunk().getX(), var1.getLocation().getChunk().getZ(), var1.getLocation().getWorld())) {
            return false;
         }

         if (var2 != Season.DISABLED && var2 != null && var1.getCustomName() == null) {
            if (var1 instanceof Tameable) {
               Tameable var4 = (Tameable)var1;
               if (var4.getOwner() != null) {
                  return false;
               }
            }

            if (var2 != Season.SUMMER && this.main.getSettings().fullSummerAnimals.contains(var1.getType())) {
               var1.remove();
               return false;
            }

            if (var2 != Season.WINTER && this.main.getSettings().fullWinterAnimals.contains(var1.getType())) {
               if (var1.getType() != EntityType.WOLF && var1.getType() != EntityType.FOX) {
                  var1.remove();
                  return false;
               }

               if (var1.getType() != EntityType.FOX) {
                  var1.remove();
                  return false;
               }

               if (((Fox)var1).getFoxType() == Type.SNOW) {
                  var1.remove();
                  return false;
               }
            }

            if (var2 != Season.FALL && this.main.getSettings().fullFallAnimals.contains(var1.getType())) {
               if (var1.getType() != EntityType.FOX) {
                  var1.remove();
                  return false;
               }

               if (((Fox)var1).getFoxType() == Type.RED) {
                  var1.remove();
                  return false;
               }
            }

            if (var2 != Season.SPRING && this.main.getSettings().fullSpringAnimals.contains(var1.getType())) {
               if (!(var1 instanceof Bee)) {
                  if (var1.getType() != EntityType.COW && var1.getType() != EntityType.CHICKEN && var1.getType() != EntityType.SHEEP && var1.getType() != EntityType.PIG) {
                     var1.remove();
                     return false;
                  }

                  var1.remove();
                  return false;
               }

               Bee var5 = (Bee)var1;
               if (var5.getHive() == null) {
                  var1.remove();
                  return false;
               }
            }

            return true;
         }
      } else if (var1.getScoreboardTags().contains("RSEASTER") && var1.getType() == EntityType.RABBIT && this.main.getEventManager().getDefaultEvent(DefaultEventType.EASTER) != null) {
         if (this.main.getEventManager().getDefaultEvent(DefaultEventType.EASTER).isEnabled(var1.getWorld())) {
            return true;
         }

         var1.remove();
         return false;
      }

      return false;
   }

   public static boolean generateInGroups(EntityType var0) {
      switch(var0) {
      case FOX:
         return false;
      case MUSHROOM_COW:
         return false;
      case BEE:
         return true;
      case RABBIT:
         return true;
      case OCELOT:
         return false;
      case PANDA:
         return false;
      case PARROT:
         return true;
      case WOLF:
         return true;
      case POLAR_BEAR:
         return false;
      case SNOWMAN:
         return false;
      default:
         return false;
      }
   }

   public void generateAnimal(Chunk var1, EntityType var2, Season var3) {
      boolean var4 = false;
      Iterator var5 = var1.getWorld().getNearbyEntities(var1.getWorld().getHighestBlockAt(var1.getBlock(7, 0, 7).getLocation()).getLocation(), 40.0D, 40.0D, 40.0D).iterator();

      while(var5.hasNext()) {
         Entity var6 = (Entity)var5.next();
         if (this.checkEntity(var6, var3, false)) {
            var4 = true;
         }
      }

      if (!var4) {
         int var11;
         if (generateInGroups(var2)) {
            var11 = r.nextInt(5) + 2;
         } else {
            var11 = 1;
         }

         if (!hasMultipleSeasonAnimals(var1)) {
            int var12;
            if ((var2 == EntityType.COW || var2 == EntityType.CHICKEN || var2 == EntityType.SHEEP || var2 == EntityType.PIG) && this.main.getSettings().spawnExtraAnimalsInSpring) {
               for(var12 = 0; var12 <= 5; ++var12) {
                  Location var13;
                  if (var12 == 1) {
                     var13 = var1.getWorld().getHighestBlockAt(var1.getBlock(r.nextInt(16), 64, r.nextInt(16)).getLocation()).getRelative(BlockFace.UP).getLocation();
                     if (this.isSpawnable(var13.getBlock().getRelative(BlockFace.DOWN).getType())) {
                        LivingEntity var14 = (LivingEntity)var1.getWorld().spawnEntity(var13, var2);
                        var14.addScoreboardTag("seasonal");
                     }
                  } else {
                     var13 = var1.getWorld().getHighestBlockAt(var1.getBlock(r.nextInt(16), 64, r.nextInt(16)).getLocation()).getRelative(BlockFace.UP).getLocation();
                     if (this.isSpawnable(var13.getBlock().getRelative(BlockFace.DOWN).getType())) {
                        Ageable var15 = (Ageable)var1.getWorld().spawnEntity(var13, var2);
                        var15.setBaby();
                        var15.addScoreboardTag("seasonal");
                     }
                  }
               }
            } else {
               var12 = r.nextInt(9);

               for(int var7 = 1; var7 <= var11; ++var7) {
                  Location var8 = var1.getWorld().getHighestBlockAt(var1.getBlock(r.nextInt(16), 64, r.nextInt(16)).getLocation()).getRelative(BlockFace.UP).getLocation();
                  if (this.isSpawnable(var8.getBlock().getRelative(BlockFace.DOWN).getType())) {
                     Entity var9 = var1.getWorld().spawnEntity(var8, var2);
                     if (var9.getType() == EntityType.SNOWMAN) {
                        if (!this.main.getSettings().snowmanWearPumpkin) {
                           Snowman var10 = (Snowman)var9;
                           var10.setDerp(true);
                        }
                     } else if (var9.getType() == EntityType.FOX) {
                        if (var3 == Season.WINTER) {
                           ((Fox)var9).setFoxType(Type.SNOW);
                        } else {
                           ((Fox)var9).setFoxType(Type.RED);
                        }
                     } else if (var9.getType() == EntityType.WOLF) {
                        Wolf var16 = (Wolf)var9;
                        if (Version.is_1_21_2_or_up()) {
                           NmsCode_21_R2.setWolfVariant(var16, var12);
                        }
                     }

                     var9.addScoreboardTag("seasonal");
                  }
               }
            }

         }
      }
   }

   public void updateAnimalSpawns(Season var1, Chunk var2) {
      if (this.main.hasMobSpawns(var2.getX(), var2.getZ(), var2.getWorld())) {
         if (!this.main.getSettings().fallAnimals.isEmpty()) {
            if (var1 == Season.FALL) {
               if (Math.random() * 100.0D < (double)this.main.getSettings().spawnChanceAnimals && this.main.getSettings().fallAnimals.size() > 0) {
                  this.generateAnimal(var2, (EntityType)this.main.getSettings().fallAnimals.get(r.nextInt(this.main.getSettings().fallAnimals.size())), Season.FALL);
               }
            } else if (var1 == Season.SPRING) {
               if (Math.random() * 100.0D < (double)this.main.getSettings().spawnChanceAnimals && this.main.getSettings().springAnimals.size() > 0) {
                  this.generateAnimal(var2, (EntityType)this.main.getSettings().springAnimals.get(r.nextInt(this.main.getSettings().springAnimals.size())), Season.SPRING);
               }
            } else if (var1 == Season.SUMMER) {
               if (Math.random() * 100.0D < (double)this.main.getSettings().spawnChanceAnimals && this.main.getSettings().summerAnimals.size() > 0) {
                  this.generateAnimal(var2, (EntityType)this.main.getSettings().summerAnimals.get(r.nextInt(this.main.getSettings().summerAnimals.size())), Season.SUMMER);
               }
            } else if (var1 == Season.WINTER && Math.random() * 100.0D < (double)this.main.getSettings().spawnChanceAnimals && this.main.getSettings().winterAnimals.size() > 0) {
               this.generateAnimal(var2, (EntityType)this.main.getSettings().winterAnimals.get(r.nextInt(this.main.getSettings().winterAnimals.size())), Season.WINTER);
            }
         }

      }
   }

   public boolean isSpawnable(Material var1) {
      return var1 == Material.GRASS_BLOCK || var1 == Material.GRASS || var1 == Material.TALL_GRASS || var1 == Material.STONE || var1 == Material.SAND || var1.toString().contains("TERRACOTTA") || var1 == Material.MYCELIUM;
   }

   private static boolean hasMultipleSeasonAnimals(Chunk var0) {
      int var1 = 0;
      Entity[] var2 = var0.getEntities();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Entity var5 = var2[var4];
         if (var5.getScoreboardTags().contains("seasonal")) {
            ++var1;
            if (var1 > 1) {
               return true;
            }
         }
      }

      return false;
   }
}
