package me.casperge.realisticseasons.particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.api.SeasonParticle;
import me.casperge.realisticseasons.api.SeasonParticleStartEvent;
import me.casperge.realisticseasons.blockscanner.SimpleLocation;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.utils.ChunkUtils;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleSpawner extends BukkitRunnable {
   private ParticleManager pman;
   private RealisticSeasons main;
   private List<UUID> canSeeFallingStar = new ArrayList();
   private boolean sweatTick = true;
   HashMap<UUID, Integer> coldBreathCounter = new HashMap();
   public HashMap<String, Boolean> hasWhiteSparksNight = new HashMap();
   public HashMap<UUID, Long> lastSpawn = new HashMap();

   public ParticleSpawner(RealisticSeasons var1, ParticleManager var2) {
      this.main = var1;
      this.pman = var2;
   }

   public void run() {
      this.sweatTick = !this.sweatTick;
      this.canSeeFallingStar.clear();
      Iterator var1 = this.main.getSeasonManager().worldData.keySet().iterator();

      while(true) {
         World var2;
         Iterator var17;
         Player var18;
         do {
            do {
               do {
                  if (!var1.hasNext()) {
                     return;
                  }

                  var2 = (World)var1.next();
                  Season var3 = this.main.getSeasonManager().getSeason(var2);
                  ArrayList var4;
                  Iterator var5;
                  Player var6;
                  if (var2.getFullTime() % 24000L > 13670L && var2.getFullTime() % 24000L < 22812L) {
                     int var29;
                     int var33;
                     if (this.main.getSettings().fallingStarsEnabled && this.main.getSettings().fallingStarsSeasons.contains(var3)) {
                        var17 = var2.getPlayers().iterator();

                        label281:
                        while(true) {
                           do {
                              if (!var17.hasNext()) {
                                 break label281;
                              }

                              var18 = (Player)var17.next();
                           } while(this.canSeeFallingStar.contains(var18.getUniqueId()));

                           ArrayList var20 = new ArrayList();
                           Iterator var23 = var2.getPlayers().iterator();

                           while(var23.hasNext()) {
                              Player var28 = (Player)var23.next();
                              if (var28 != var18 && var28.getLocation().distance(var18.getLocation()) < 300.0D && var28.getLocation().getBlockY() - var18.getLocation().getBlockY() < 50) {
                                 var20.add(var28);
                                 this.canSeeFallingStar.add(var28.getUniqueId());
                              }
                           }

                           var20.add(var18);
                           this.canSeeFallingStar.add(var18.getUniqueId());
                           if (JavaUtils.getRandom().nextInt((int)(100.0F / this.main.getSettings().fallingStarsChance)) == 0) {
                              Location var24 = var18.getLocation().clone();
                              var29 = 100 - JavaUtils.getRandom().nextInt(200);
                              var33 = 100 - JavaUtils.getRandom().nextInt(200);
                              var24.add((double)var29, (double)this.main.getSettings().fallingStarsHeight, (double)var33);
                              SeasonParticleStartEvent var37 = new SeasonParticleStartEvent(var24, var18, SeasonParticle.SHOOTING_STAR);
                              Bukkit.getPluginManager().callEvent(var37);
                              if (!var37.isCancelled()) {
                                 this.pman.spawnMeteorite(var20, var24);
                              }
                           }
                        }
                     }

                     if (this.main.getSettings().nightSparksEnabled && this.main.getSettings().nightSparksSeasons.contains(var3) && this.hasWhiteSparksNight(var2) && !var2.hasStorm()) {
                        var17 = var2.getPlayers().iterator();

                        while(var17.hasNext()) {
                           var18 = (Player)var17.next();
                           this.pman.playWhiteSparkles(var18);
                        }
                     }

                     var4 = new ArrayList();
                     var5 = this.lastSpawn.keySet().iterator();

                     UUID var21;
                     while(var5.hasNext()) {
                        var21 = (UUID)var5.next();
                        if (System.currentTimeMillis() - (Long)this.lastSpawn.get(var21) > 300000L) {
                           var4.add(var21);
                        }
                     }

                     var5 = var4.iterator();

                     while(var5.hasNext()) {
                        var21 = (UUID)var5.next();
                        this.lastSpawn.remove(var21);
                     }

                     if (this.main.getSettings().fireFliesEnabled && this.main.getSettings().fireFliesSeasons.contains(var3)) {
                        var5 = var2.getPlayers().iterator();

                        label244:
                        while(true) {
                           label242:
                           while(true) {
                              int var27;
                              do {
                                 do {
                                    do {
                                       if (!var5.hasNext()) {
                                          break label244;
                                       }

                                       var6 = (Player)var5.next();
                                    } while(this.lastSpawn.containsKey(var6.getUniqueId()));
                                 } while(!(Math.random() * 100.0D < (double)this.main.getSettings().fireFliesChance));

                                 var27 = 0;
                                 Iterator var32 = var2.getPlayers().iterator();

                                 while(var32.hasNext()) {
                                    Player var34 = (Player)var32.next();
                                    if (var6 != var34 && ChunkUtils.get2dDistance(var6.getLocation().getBlock(), var34.getLocation().getBlock()) < 70.0D) {
                                       ++var27;
                                    }
                                 }
                              } while(var27 > 0 && JavaUtils.getRandom().nextInt(var27) != 0);

                              var29 = 0;

                              while(true) {
                                 Block var40;
                                 do {
                                    if (var29 >= 10) {
                                       continue label242;
                                    }

                                    ++var29;
                                    var33 = 50 - JavaUtils.getRandom().nextInt(100);
                                    int var39 = 50 - JavaUtils.getRandom().nextInt(100);
                                    var40 = var6.getWorld().getHighestBlockAt(var6.getLocation().getBlockX() + var33, var6.getLocation().getBlockZ() + var39);
                                 } while(!var40.getType().equals(Material.OAK_LEAVES) && !var40.getType().equals(Material.BIRCH_LEAVES));

                                 boolean var12 = false;

                                 for(int var13 = 0; var13 > -10; --var13) {
                                    Material var14 = var40.getRelative(0, var13, 0).getType();
                                    if (var14.equals(Material.AIR)) {
                                       var12 = true;
                                    } else if (var12 && (var14.equals(Material.GRASS_BLOCK) || var14.equals(Material.GRASS) || var14.equals(Material.TALL_GRASS) || ChunkUtils.flowers.contains(var14)) && this.main.hasSeasons(var40.getChunk().getX(), var40.getChunk().getZ(), var2)) {
                                       Location var15 = var40.getLocation().clone().add(0.0D, (double)(3 + var13), 0.0D);
                                       SeasonParticleStartEvent var16 = new SeasonParticleStartEvent(var15, var6, SeasonParticle.FIREFLY);
                                       Bukkit.getPluginManager().callEvent(var16);
                                       if (!var16.isCancelled()) {
                                          this.pman.spawnFireFlies(var15);
                                          this.lastSpawn.put(var6.getUniqueId(), System.currentTimeMillis());
                                       }
                                       continue label242;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  } else {
                     if (this.hasWhiteSparksNight.containsKey(var2.getName())) {
                        this.hasWhiteSparksNight.remove(var2.getName());
                     }

                     if (this.main.getSettings().fallingLeavesEnabled && this.main.getSettings().fallingLeavesSeasons.contains(var3)) {
                        var4 = new ArrayList();
                        var5 = var2.getPlayers().iterator();

                        label312:
                        while(true) {
                           do {
                              do {
                                 do {
                                    do {
                                       if (!var5.hasNext()) {
                                          break label312;
                                       }

                                       var6 = (Player)var5.next();
                                    } while(var4.contains(var6));
                                 } while(!(Math.random() * 100.0D < (double)this.main.getSettings().fallingLeavesChance));
                              } while(!this.pman.leaveLocations.containsKey(var6.getUniqueId()));
                           } while(!(Math.random() * 100.0D < (double)((List)this.pman.leaveLocations.get(var6.getUniqueId())).size()));

                           List var7 = (List)this.pman.leaveLocations.get(var6.getUniqueId());
                           SimpleLocation var8 = (SimpleLocation)var7.get(JavaUtils.getRandom().nextInt(var7.size()));
                           ArrayList var9 = new ArrayList();
                           var9.add(var6);
                           Iterator var10 = var2.getPlayers().iterator();

                           while(var10.hasNext()) {
                              Player var11 = (Player)var10.next();
                              if (var11 != var6 && var11.getLocation().distance(var6.getLocation()) < 10.0D) {
                                 var9.add(var11);
                                 var4.add(var11);
                              }
                           }

                           Location var35 = new Location(Bukkit.getWorld(var8.getWorldName()), (double)var8.getX(), (double)(var8.getY() - 1), (double)var8.getZ());
                           SeasonParticleStartEvent var38 = new SeasonParticleStartEvent(var35, var6, SeasonParticle.FALLING_LEAF);
                           Bukkit.getPluginManager().callEvent(var38);
                           if (!var38.isCancelled()) {
                              this.pman.spawnLeaf(var9, var35);
                           }
                        }
                     }

                     if (this.main.getSettings().smallFallingLeavesEnabled && this.main.getSettings().smallFallingLeavesSeasons.contains(var3)) {
                        var17 = var2.getPlayers().iterator();

                        while(var17.hasNext()) {
                           var18 = (Player)var17.next();
                           if (Math.random() * 100.0D < (double)this.main.getSettings().fallingLeavesChance && this.pman.leaveLocations.containsKey(var18.getUniqueId()) && Math.random() * 100.0D < (double)((List)this.pman.leaveLocations.get(var18.getUniqueId())).size()) {
                              List var19 = (List)this.pman.leaveLocations.get(var18.getUniqueId());
                              SimpleLocation var22 = (SimpleLocation)var19.get(JavaUtils.getRandom().nextInt(var19.size()));
                              Location var26 = new Location(Bukkit.getWorld(var22.getWorldName()), (double)var22.getX(), (double)var22.getY(), (double)var22.getZ());
                              SeasonParticleStartEvent var31 = new SeasonParticleStartEvent(var26, var18, SeasonParticle.SMALL_FALLING_LEAF);
                              Bukkit.getPluginManager().callEvent(var31);
                              if (!var31.isCancelled()) {
                                 ArrayList var36 = new ArrayList();
                                 var36.add(var18);
                                 this.pman.spawnSmallFallingLeaves(var36, var26);
                              }
                           }
                        }
                     }
                  }

                  if (this.sweatTick && this.main.getTemperatureManager().getTempData().isEnabled() && this.main.getTemperatureManager().getTempData().isEnabledWorld(var2) && this.main.getSettings().sweatEffectEnabled) {
                     var17 = var2.getPlayers().iterator();

                     while(var17.hasNext()) {
                        var18 = (Player)var17.next();
                        if (this.main.getTemperatureManager().hasTemperature(var18) && this.main.getTemperatureManager().getTemperature(var18) >= this.main.getSettings().sweatMinTemperature && !this.main.getTemperatureManager().getTempUtils().isInWater(var18) && var18.getGameMode() != GameMode.SPECTATOR) {
                           this.pman.playRandomSweatParticle(var18);
                        }
                     }
                  }
               } while(!this.main.getTemperatureManager().getTempData().isEnabled());
            } while(!this.main.getTemperatureManager().getTempData().isEnabledWorld(var2));
         } while(!this.main.getSettings().coldBreathEnabled);

         var17 = var2.getPlayers().iterator();

         while(var17.hasNext()) {
            var18 = (Player)var17.next();
            if (this.main.getTemperatureManager().hasTemperature(var18) && var18.getGameMode() != GameMode.SPECTATOR && !var18.hasPotionEffect(PotionEffectType.INVISIBILITY) && !var18.isInvisible()) {
               if (!this.coldBreathCounter.containsKey(var18.getUniqueId())) {
                  this.coldBreathCounter.put(var18.getUniqueId(), JavaUtils.getRandom().nextInt(25));
               } else {
                  this.coldBreathCounter.put(var18.getUniqueId(), (Integer)this.coldBreathCounter.get(var18.getUniqueId()) + 1);
                  boolean var25 = false;
                  if ((Integer)this.coldBreathCounter.get(var18.getUniqueId()) > 50) {
                     var25 = true;
                     this.coldBreathCounter.put(var18.getUniqueId(), 0);
                  }

                  if (this.main.getTemperatureManager().getPlayerAirTemperature(var18) <= this.main.getSettings().coldBreathMaxTemperature && var25 && !this.main.getTemperatureManager().getTempUtils().isInWater(var18)) {
                     SeasonParticleStartEvent var30 = new SeasonParticleStartEvent(var18.getLocation(), var18, SeasonParticle.COLD_BREATH);
                     Bukkit.getPluginManager().callEvent(var30);
                     if (!var30.isCancelled()) {
                        this.pman.playColdBreathEffect(var18);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean hasWhiteSparksNight(World var1) {
      if (this.hasWhiteSparksNight.containsKey(var1.getName())) {
         return (Boolean)this.hasWhiteSparksNight.get(var1.getName());
      } else if (JavaUtils.getRandom().nextInt((int)(100.0F / this.main.getSettings().nightSparksChance)) == 0) {
         this.hasWhiteSparksNight.put(var1.getName(), true);
         return true;
      } else {
         this.hasWhiteSparksNight.put(var1.getName(), false);
         return false;
      }
   }
}
