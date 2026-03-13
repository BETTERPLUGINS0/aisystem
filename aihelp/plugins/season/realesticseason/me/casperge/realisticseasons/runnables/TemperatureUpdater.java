package me.casperge.realisticseasons.runnables;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.temperature.TempEffect;
import me.casperge.realisticseasons.temperature.TemperatureManager;
import me.casperge.realisticseasons.utils.ChunkUtils;
import me.casperge.realisticseasons.utils.JavaUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class TemperatureUpdater extends BukkitRunnable {
   private RealisticSeasons main;
   private TemperatureManager tman;
   private String bottleEmoij = new String(Character.toChars(129514));
   private HashMap<UUID, Long> lastWarning = new HashMap();
   private boolean waterBottleFlash;

   public TemperatureUpdater(RealisticSeasons var1, TemperatureManager var2) {
      this.main = var1;
      this.tman = var2;
      this.waterBottleFlash = true;
   }

   public void run() {
      this.waterBottleFlash = !this.waterBottleFlash;
      Iterator var1 = Bukkit.getWorlds().iterator();

      while(true) {
         label213:
         while(var1.hasNext()) {
            World var2 = (World)var1.next();
            Iterator var3;
            Player var4;
            if (this.tman.getTempData().getEnabledWorlds().contains(var2)) {
               var3 = var2.getPlayers().iterator();

               label275:
               while(true) {
                  int var5;
                  boolean var6;
                  boolean var7;
                  boolean var8;
                  boolean var9;
                  boolean var10;
                  ChatColor var11;
                  do {
                     while(true) {
                        do {
                           if (!var3.hasNext()) {
                              continue label213;
                           }

                           var4 = (Player)var3.next();
                        } while(!ChunkUtils.isChunkLoaded(var4.getLocation()));

                        if (var4.getGameMode() != GameMode.SPECTATOR && var4.getGameMode() != GameMode.CREATIVE) {
                           var5 = this.tman.getTemperature(var4);
                           this.tman.updateTemperature(var4);
                           var6 = false;
                           var7 = false;
                           var8 = false;
                           var9 = false;
                           var10 = this.main.getTemperatureManager().getTempUtils().hasDrinked(var4);
                           if (this.main.getTemperatureManager().getTempData().getTempSettings().getWaterBottleModifier() == 0) {
                              var10 = false;
                           }

                           var11 = ChatColor.GREEN;
                           break;
                        }

                        if (this.tman.isFrozen(var4)) {
                           this.tman.setFreezing(var4, false, false);
                        }

                        if (!this.tman.canHeal(var4)) {
                           this.tman.setHealing(var4, true);
                        }
                     }
                  } while(!this.tman.hasTemperature(var4));

                  if (this.tman.getTempData().getTempSettings().getColdHungerTemp() >= this.tman.getTemperature(var4)) {
                     this.tman.getTempUtils().applyEffect(var4, TempEffect.COLD_HUNGER);
                     var7 = true;
                     var11 = ChatColor.AQUA;
                     if (this.tman.getTempData().getTempSettings().getColdHungerTemp() < var5) {
                        var9 = true;
                     }
                  }

                  boolean var12 = false;
                  if (this.tman.getTempData().getTempSettings().getColdSlownessTemp() >= this.tman.getTemperature(var4)) {
                     this.tman.getTempUtils().applyEffect(var4, TempEffect.COLD_SLOWNESS);
                     var7 = true;
                     var11 = ChatColor.BLUE;
                     if (this.tman.getTempData().getTempSettings().getColdSlownessTemp() < var5) {
                        var9 = true;
                     }

                     var12 = true;
                  }

                  if (this.tman.getTempData().getTempSettings().getColdFreezingTemp() >= this.tman.getTemperature(var4)) {
                     this.tman.getTempUtils().applyEffect(var4, TempEffect.COLD_FREEZING);
                     var11 = ChatColor.DARK_BLUE;
                     var7 = true;
                     if (this.tman.getTempData().getTempSettings().getColdFreezingTemp() < var5) {
                        var9 = true;
                     }

                     var12 = true;
                  }

                  if (!var12) {
                     this.tman.setFreezing(var4, false, false);
                  }

                  if (this.tman.getTempData().getTempSettings().getWarmNoHealingTemp() >= this.tman.getTemperature(var4)) {
                     this.tman.setHealing(var4, true);
                  } else {
                     this.tman.getTempUtils().applyEffect(var4, TempEffect.HEAT_NO_HEALING);
                     var6 = true;
                     if (this.tman.getTempData().getTempSettings().getWarmNoHealingTemp() > var5) {
                        var8 = true;
                     }

                     var11 = ChatColor.GOLD;
                  }

                  if (this.tman.getTempData().getTempSettings().getWarmSlownessTemp() <= this.tman.getTemperature(var4)) {
                     this.tman.getTempUtils().applyEffect(var4, TempEffect.HEAT_SLOWNESS);
                     var6 = true;
                     var11 = ChatColor.RED;
                     if (this.tman.getTempData().getTempSettings().getWarmSlownessTemp() > var5) {
                        var8 = true;
                     }
                  }

                  boolean var13 = false;
                  if (this.tman.getTempData().getTempSettings().getWarmFireTemp() <= this.tman.getTemperature(var4)) {
                     this.tman.getTempUtils().applyEffect(var4, TempEffect.HEAT_FIRE);
                     var13 = true;
                     var6 = true;
                     var11 = ChatColor.DARK_RED;
                     if (this.tman.getTempData().getTempSettings().getWarmFireTemp() > var5) {
                        var8 = true;
                     }
                  }

                  if (this.tman.getTempData().getTempSettings().isReducedMovementSpeedEnabled()) {
                     float var14;
                     if (this.tman.getTemperature(var4) > this.tman.getTempData().getTempSettings().getWarmMovementStart()) {
                        if (this.tman.getTemperature(var4) < this.tman.getTempData().getTempSettings().getWarmMovementEnd()) {
                           var14 = (float)((this.tman.getTemperature(var4) - this.tman.getTempData().getTempSettings().getWarmMovementStart()) / (this.tman.getTempData().getTempSettings().getWarmMovementEnd() - this.tman.getTempData().getTempSettings().getWarmMovementStart()));
                           var14 = 1.0F - var14;
                           if (var14 < 0.3F) {
                              var14 = 0.3F;
                           }

                           var4.setWalkSpeed(var14 * 0.2F);
                        } else {
                           var4.setWalkSpeed(0.07F);
                        }
                     } else if (this.tman.getTemperature(var4) < this.tman.getTempData().getTempSettings().getColdMovementStart()) {
                        if (this.tman.getTemperature(var4) > this.tman.getTempData().getTempSettings().getColdMovementEnd()) {
                           var14 = (float)Math.abs((this.tman.getTemperature(var4) - this.tman.getTempData().getTempSettings().getColdMovementStart()) / (this.tman.getTempData().getTempSettings().getColdMovementEnd() - this.tman.getTempData().getTempSettings().getColdMovementStart()));
                           var14 = 1.0F - var14;
                           if (var14 < 0.3F) {
                              var14 = 0.3F;
                           }

                           var4.setWalkSpeed(0.2F * var14);
                        } else {
                           var4.setWalkSpeed(0.07F);
                        }
                     } else {
                        var4.setWalkSpeed(0.2F);
                     }
                  }

                  this.main.getTemperatureManager().setRSBurn(var4.getUniqueId(), var13);
                  boolean var20 = false;
                  if (this.tman.getTempData().getTempSettings().getBoostMinTemp() <= this.tman.getTemperature(var4) && this.tman.getTempData().getTempSettings().getBoostMaxTemp() >= this.tman.getTemperature(var4) && this.tman.getTempData().getTempSettings().getBoostPotionEffects().size() > 0 && var4.getFoodLevel() >= this.tman.getTempData().getTempSettings().getBoostsMinHunger()) {
                     this.tman.getTempUtils().applyEffect(var4, TempEffect.BOOSTS);
                     var11 = ChatColor.LIGHT_PURPLE;
                     var20 = true;
                  }

                  int var15 = this.tman.getTemperature(var4);
                  if (!var13 && var4.getFireTicks() > 0) {
                     if (var4.getLocation().getBlock().getType() == Material.LAVA) {
                        var15 += this.tman.getTempData().getTempSettings().getLavaTemp();
                        var11 = ChatColor.DARK_RED;
                     } else {
                        var15 += 100;
                        var11 = ChatColor.DARK_RED;
                     }
                  }

                  String var16;
                  if ((!this.main.getTemperatureManager().getTempData().getTempSettings().isConvertToFahrenheit() || this.main.getTemperatureManager().hasFahrenheitEnabled(var4)) && (this.main.getTemperatureManager().getTempData().getTempSettings().isConvertToFahrenheit() || !this.main.getTemperatureManager().hasFahrenheitEnabled(var4))) {
                     var16 = var15 + this.main.getTemperatureManager().getTempData().getTempSettings().getCelciusMessage();
                  } else {
                     var16 = JavaUtils.convertToFahrenheit(var15) + this.main.getTemperatureManager().getTempData().getTempSettings().getFahrenheitMessage();
                  }

                  String var17 = var11 + this.tman.getTempData().getTempSettings().getActionbarDisplay().replaceAll("\\%temperature\\%", String.valueOf(var16));
                  if (var10) {
                     this.tman.getTempUtils().applyEffect(var4, TempEffect.HYDRATED);
                     if (this.tman.getTempUtils().flashingWaterBottle(var4)) {
                        if (this.waterBottleFlash) {
                           var17 = "  " + var17 + " " + ChatColor.AQUA + this.bottleEmoij;
                        } else {
                           var17 = " " + var17 + "  ";
                        }
                     } else {
                        var17 = "  " + var17 + " " + ChatColor.AQUA + this.bottleEmoij;
                     }
                  }

                  Iterator var18 = this.main.getTemperatureManager().getTempData().getTempSettings().getBoostPotionEffects().iterator();

                  while(true) {
                     while(true) {
                        PotionEffectType var19;
                        do {
                           do {
                              if (!var18.hasNext()) {
                                 String var21;
                                 if (this.tman.getTempData().getTempSettings().isDisplayTempOnActionBar()) {
                                    if (this.tman.getTempData().getTempSettings().isActionbarWarningEnabled()) {
                                       if (var6) {
                                          var21 = this.tman.getTempData().getTempSettings().getActionbarOverheatingWarning();
                                          var17 = this.placeInString(var21, var17);
                                       } else if (var7) {
                                          var21 = this.tman.getTempData().getTempSettings().getActionbarFreezingWarning();
                                          var17 = this.placeInString(var21, var17);
                                       }
                                    }

                                    if (this.main.getActionbarListener().canReceiveTempBar(var4)) {
                                       this.main.getActionbarListener().setWaitingForTempBar(var4, true);
                                       var4.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(var11 + this.main.setPlaceHolders(JavaUtils.hex(var17), var4)));
                                    }
                                 } else if (this.tman.getTempData().getTempSettings().isActionbarWarningEnabled()) {
                                    if (var6) {
                                       var21 = this.tman.getTempData().getTempSettings().getActionbarOverheatingWarning();
                                       if (this.main.getActionbarListener().canReceiveTempBar(var4)) {
                                          this.main.getActionbarListener().setWaitingForTempBar(var4, true);
                                          var4.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(var11 + var21));
                                       }
                                    } else if (var7) {
                                       var21 = this.tman.getTempData().getTempSettings().getActionbarFreezingWarning();
                                       if (this.main.getActionbarListener().canReceiveTempBar(var4)) {
                                          this.main.getActionbarListener().setWaitingForTempBar(var4, true);
                                          var4.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(var11 + var21));
                                       }
                                    }
                                 }

                                 if (this.tman.getTempData().getTempSettings().isMessageWarningsEnabled() && var4.getGameMode() == GameMode.SURVIVAL) {
                                    if (var9) {
                                       if (this.lastWarning.containsKey(var4.getUniqueId())) {
                                          if (System.currentTimeMillis() - (Long)this.lastWarning.get(var4.getUniqueId()) > 20000L) {
                                             var4.sendMessage(JavaUtils.hex(this.tman.getTempData().getTempSettings().getMessageFreezingWarning()));
                                             this.lastWarning.put(var4.getUniqueId(), System.currentTimeMillis());
                                          }
                                       } else {
                                          var4.sendMessage(JavaUtils.hex(this.tman.getTempData().getTempSettings().getMessageFreezingWarning()));
                                          this.lastWarning.put(var4.getUniqueId(), System.currentTimeMillis());
                                       }
                                    } else if (var8) {
                                       if (this.lastWarning.containsKey(var4.getUniqueId())) {
                                          if (System.currentTimeMillis() - (Long)this.lastWarning.get(var4.getUniqueId()) > 20000L) {
                                             var4.sendMessage(JavaUtils.hex(this.tman.getTempData().getTempSettings().getMessageOverheatingWarning()));
                                             this.lastWarning.put(var4.getUniqueId(), System.currentTimeMillis());
                                          }
                                       } else {
                                          var4.sendMessage(JavaUtils.hex(this.tman.getTempData().getTempSettings().getMessageOverheatingWarning()));
                                          this.lastWarning.put(var4.getUniqueId(), System.currentTimeMillis());
                                       }
                                    }
                                 }
                                 continue label275;
                              }

                              var19 = (PotionEffectType)var18.next();
                           } while(!var19.equals(PotionEffectType.HEALTH_BOOST));
                        } while(!var4.hasPotionEffect(var19));

                        if (var4.getPotionEffect(var19).getDuration() > 9999999 && !var19.equals(PotionEffectType.HEALTH_BOOST)) {
                           var4.removePotionEffect(var19);
                        } else if (var19.equals(PotionEffectType.HEALTH_BOOST) && !var20) {
                           var4.removePotionEffect(var19);
                        }
                     }
                  }
               }
            } else {
               var3 = var2.getPlayers().iterator();

               while(var3.hasNext()) {
                  var4 = (Player)var3.next();
                  if (this.tman.isFrozen(var4)) {
                     this.tman.setFreezing(var4, false, false);
                  }

                  if (!this.tman.canHeal(var4)) {
                     this.tman.setHealing(var4, true);
                  }
               }
            }
         }

         return;
      }
   }

   public String placeInString(String var1, String var2) {
      int var3 = 0;
      char[] var4 = var2.toCharArray();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Character var7 = var4[var6];
         if (!var7.equals(' ')) {
            break;
         }

         ++var3;
      }

      if (var3 < var1.length() + 1) {
         var2 = var1 + this.removeSpaces(var2, var1.length());
         return var2;
      } else {
         return var1 + " " + var2.trim();
      }
   }

   public String removeSpaces(String var1, int var2) {
      for(int var3 = 0; var3 <= var2 + 1; ++var3) {
         var1 = var1.replaceFirst(" ", "");
      }

      return var1;
   }
}
