package me.casperge.realisticseasons.crop;

import java.util.Random;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;

public class CropGrowEvent implements Listener {
   private RealisticSeasons main;
   private Random r;

   public CropGrowEvent(RealisticSeasons var1) {
      var1.getServer().getPluginManager().registerEvents(this, var1);
      this.main = var1;
      this.r = new Random();
   }

   @EventHandler
   public void onSpread(BlockSpreadEvent var1) {
      if (!var1.isCancelled()) {
         if (var1.getNewState().getType() == Material.BAMBOO && this.main.hasSeasons(var1.getBlock().getChunk().getX(), var1.getBlock().getChunk().getZ(), var1.getBlock().getWorld()) && var1.getBlock().getWorld().getEnvironment() != Environment.NETHER && var1.getBlock().getWorld().getEnvironment() != Environment.THE_END) {
            Season var2 = this.main.getSeasonManager().getSeason(var1.getBlock().getWorld());
            if (this.main.getCropSettings().isEnabled() && var2 != Season.DISABLED && var2 != Season.RESTORE) {
               boolean var3 = var1.getBlock().getWorld().getHighestBlockAt(var1.getBlock().getLocation()).getRelative(BlockFace.UP).getY() == var1.getBlock().getY();
               if (this.main.getCropSettings().requiresBlockLight(Material.BAMBOO, var2) && var1.getBlock().getRelative(BlockFace.UP).getLightFromBlocks() < this.main.getCropSettings().getMinLightLevel(Material.BAMBOO, var2)) {
                  var1.setCancelled(true);
                  return;
               }

               double var4 = 1.0D;
               double var6;
               if (var3) {
                  var6 = this.main.getCropSettings().getOutdoorSpeed(Material.BAMBOO, var2);
                  if (var6 != 1.0D) {
                     var4 = var6;
                  }
               } else {
                  var6 = this.main.getCropSettings().getIndoorSpeed(Material.BAMBOO, var2);
                  if (var6 != 1.0D) {
                     var4 = var6;
                  }
               }

               if (var4 < 1.0D) {
                  if (var4 < 0.0D) {
                     var4 = 0.0D;
                  }

                  if (var4 == 0.0D) {
                     var1.setCancelled(true);
                     return;
                  }

                  if (this.r.nextDouble() > var4) {
                     var1.setCancelled(true);
                     return;
                  }
               }
            }
         }

      }
   }

   @EventHandler
   public void onGrow(BlockGrowEvent var1) {
      if (!var1.isCancelled()) {
         if (this.main.hasSeasons(var1.getBlock().getChunk().getX(), var1.getBlock().getChunk().getZ(), var1.getBlock().getWorld()) && var1.getBlock().getWorld().getEnvironment() != Environment.NETHER && var1.getBlock().getWorld().getEnvironment() != Environment.THE_END) {
            Season var2 = this.main.getSeasonManager().getSeason(var1.getBlock().getWorld());
            if (this.main.getCropSettings().isEnabled() && var2 != Season.DISABLED && var2 != Season.RESTORE) {
               boolean var3 = var1.getBlock().getWorld().getHighestBlockAt(var1.getBlock().getLocation()).getRelative(BlockFace.UP).getY() == var1.getBlock().getY();
               Material var4 = var1.getBlock().getType();
               if (var4 == Material.AIR) {
                  if (var1.getNewState().getType() == Material.SUGAR_CANE) {
                     var4 = Material.SUGAR_CANE;
                  } else if (var1.getNewState().getType() == Material.CACTUS) {
                     var4 = Material.CACTUS;
                  } else if (var1.getNewState().getType() == Material.PUMPKIN) {
                     var4 = Material.PUMPKIN_STEM;
                  } else if (var1.getNewState().getType() == Material.MELON) {
                     var4 = Material.MELON_STEM;
                  }
               }

               if (this.main.getCropSettings().requiresBlockLight(var4, var2) && var1.getBlock().getRelative(BlockFace.UP).getLightFromBlocks() < this.main.getCropSettings().getMinLightLevel(var4, var2)) {
                  var1.setCancelled(true);
                  return;
               }

               double var5 = 1.0D;
               double var7;
               if (var3) {
                  var7 = this.main.getCropSettings().getOutdoorSpeed(var4, var2);
                  if (var7 != 1.0D) {
                     var5 = var7;
                  }
               } else {
                  var7 = this.main.getCropSettings().getIndoorSpeed(var4, var2);
                  if (var7 != 1.0D) {
                     var5 = var7;
                  }
               }

               if (var5 != 1.0D) {
                  if (var5 > 1.0D) {
                     var7 = var5 % 1.0D;
                     int var9 = (int)Math.round(var5 - var7);
                     int var10 = 0;
                     if (this.r.nextDouble() < var7) {
                        ++var10;
                     }

                     if (var9 < 0) {
                        var9 = 0;
                     }

                     var10 += var9;
                     if (var10 > 1) {
                        BlockData var11 = var1.getBlock().getBlockData();
                        if (var11 instanceof Ageable) {
                           Ageable var12 = (Ageable)var11;
                           if (var12.getAge() <= var12.getMaximumAge() - var10) {
                              var1.setCancelled(true);
                              var12.setAge(var12.getAge() + var10);
                              var1.getBlock().setBlockData(var12);
                           } else {
                              var1.setCancelled(true);
                              var12.setAge(var12.getMaximumAge());
                              var1.getBlock().setBlockData(var12);
                           }
                        }
                     }
                  } else {
                     if (var5 < 0.0D) {
                        var5 = 0.0D;
                     }

                     if (var5 == 0.0D) {
                        var1.setCancelled(true);
                        return;
                     }

                     if (this.r.nextDouble() > var5) {
                        var1.setCancelled(true);
                        return;
                     }
                  }
               }
            }
         }

      }
   }
}
