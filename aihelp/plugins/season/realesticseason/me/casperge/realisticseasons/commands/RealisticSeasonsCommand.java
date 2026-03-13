package me.casperge.realisticseasons.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import me.casperge.enums.GameRuleType;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.api.CustomBiomeFileLoader;
import me.casperge.realisticseasons.api.CustomWorldGenerator;
import me.casperge.realisticseasons.api.SeasonsAPI;
import me.casperge.realisticseasons.api.TemperatureEffect;
import me.casperge.realisticseasons.biome.BiomeRegister;
import me.casperge.realisticseasons.calendar.Date;
import me.casperge.realisticseasons.data.LanguageManager;
import me.casperge.realisticseasons.data.MessageType;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class RealisticSeasonsCommand implements CommandExecutor {
   RealisticSeasons main;
   private HashMap<UUID, TemperatureEffect> tempEffects = new HashMap();

   public RealisticSeasonsCommand(RealisticSeasons var1) {
      this.main = var1;
   }

   public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
      boolean var6;
      String[] var7;
      int var8;
      final int var9;
      String var10;
      final int var11;
      int var13;
      int var14;
      Date var23;
      Season var25;
      boolean var28;
      boolean var36;
      if (var1 instanceof Player) {
         Player var29 = (Player)var1;
         if (var29.hasPermission("realisticseasons.admin")) {
            if (var4.length < 1) {
               this.displayHelp(var29);
            } else {
               List var27;
               Iterator var35;
               String var39;
               if (var4.length == 1) {
                  if (var4[0].equalsIgnoreCase("disable")) {
                     if (this.hasSeasons(var29)) {
                        this.disable(var29.getWorld());
                        var29.sendMessage(ChatColor.GREEN + "Disabled seasons in this world");
                     }
                  } else if (var4[0].equalsIgnoreCase("getinfo")) {
                     this.main.getSeasonManager().sendSeasonInfo(var29);
                  } else if (var4[0].equalsIgnoreCase("nextseason")) {
                     if (this.hasSeasons(var29) && !this.nextSeason(var29.getWorld())) {
                        var29.sendMessage(ChatColor.RED + "Seasons are currently disabled in this world, so there isn't a next one");
                        var29.sendMessage(ChatColor.RED + "Type '/rs set <spring/summer/fall/winter>' to enable seasons in this world");
                     }
                  } else if (var4[0].equalsIgnoreCase("set")) {
                     var29.sendMessage(ChatColor.RED + "Please choose between spring, summer, fall and winter");
                  } else if (var4[0].equalsIgnoreCase("restoreworld")) {
                     if (!this.setSeason(Season.RESTORE, var29.getWorld())) {
                        var29.sendMessage(ChatColor.RED + "You can't have seasons in this type of world!");
                     } else {
                        var29.sendMessage(ChatColor.GREEN + "Succesfully started restoring this world. It is recommended to still leave the plugin on the server for a while, since unloaded chunks will only be restored once a player loads them.");
                     }
                  } else if (var4[0].equalsIgnoreCase("install")) {
                     var27 = CustomWorldGenerator.getAllGenerators();
                     var28 = true;
                     var39 = "";
                     var35 = var27.iterator();

                     while(var35.hasNext()) {
                        var10 = (String)var35.next();
                        if (var28) {
                           var39 = var10;
                           var28 = false;
                        } else {
                           var39 = "/" + var39;
                        }
                     }

                     var29.sendMessage(ChatColor.RED + "Usage: /rs install <" + var39 + ">");
                  } else if (var4[0].equalsIgnoreCase("pausetime")) {
                     if (var29.getWorld().getEnvironment() == Environment.NETHER || var29.getWorld().getEnvironment() == Environment.THE_END) {
                        var29.sendMessage(ChatColor.RED + "You can only pause time in an overworld.");
                        return true;
                     }

                     var25 = this.main.getSeasonManager().getSeason(var29.getWorld());
                     var28 = true;
                     if (var25 == Season.DISABLED || var25 == Season.RESTORE) {
                        var28 = false;
                     }

                     if (var28 && this.main.getSettings().syncWorldTimeWithRealWorld) {
                        var29.sendMessage(ChatColor.RED + "You can't pause time while sync time with real world is enabled.");
                        return true;
                     }

                     if (var28 && this.main.getSettings().affectTime) {
                        if (!this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var29.getWorld())) {
                           this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var29.getWorld(), false);
                        }

                        var36 = this.main.getTimeManager().hasTime(var29.getWorld());
                        if (var36) {
                           this.main.getTimeManager().pauseTime(var29.getWorld());
                           var29.sendMessage(ChatColor.GREEN + "Time is now paused in this world");
                        } else {
                           this.main.getTimeManager().resumeTime(var29.getWorld());
                           var29.sendMessage(ChatColor.GREEN + "Time is now resumed in this world");
                        }
                     } else {
                        var36 = this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var29.getWorld());
                        if (var36) {
                           this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var29.getWorld(), false);
                           var29.sendMessage(ChatColor.GREEN + "Time is now paused in this world");
                        } else {
                           this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var29.getWorld(), true);
                           var29.sendMessage(ChatColor.GREEN + "Time is now resumed in this world");
                        }
                     }
                  } else if (var4[0].equalsIgnoreCase("reload")) {
                     var29.sendMessage(ChatColor.GREEN + "Succesfully reloaded the RealisticSeasons files");
                     var29.sendMessage(ChatColor.GREEN + "Changes made to biome files will require a full restart");
                     this.main.reload();
                  } else if (var4[0].equalsIgnoreCase("debug")) {
                     this.main.debugMode = !this.main.debugMode;
                     var29.sendMessage(ChatColor.GREEN + "Debug mode is now: " + this.main.debugMode);
                     this.main.reload();
                  } else {
                     this.displayHelp(var29);
                  }
               } else if (var4.length == 2) {
                  if (var4[0].equalsIgnoreCase("temperature") && var4[1].equalsIgnoreCase("toggle")) {
                     if (this.main.getTemperatureManager().getTempData().isEnabledWorld(var29.getWorld())) {
                        var29.sendMessage(ChatColor.GREEN + "Temperature in this world is now: " + ChatColor.DARK_GREEN + "DISABLED");
                     } else {
                        var29.sendMessage(ChatColor.GREEN + "Temperature in this world is now: " + ChatColor.DARK_GREEN + "ENABLED");
                     }

                     this.main.getTemperatureManager().getTempData().toggleTemperature(var29.getWorld());
                  } else if (var4[0].equalsIgnoreCase("set")) {
                     this.setByPlayer(var29, var29.getWorld(), var4);
                  } else if (var4[0].equalsIgnoreCase("setdate")) {
                     if (var4[1].split("/").length != 3) {
                        if (this.main.getSettings().americandateformat) {
                           var29.sendMessage(ChatColor.RED + "Please use the format: mm/dd/yyyy");
                        } else {
                           var29.sendMessage(ChatColor.RED + "Please use the format: dd/mm/yyyy");
                        }
                     } else {
                        var6 = true;
                        var7 = var4[1].split("/");
                        var8 = var7.length;

                        for(var9 = 0; var9 < var8; ++var9) {
                           var10 = var7[var9];
                           if (!JavaUtils.isNumeric(var10)) {
                              var6 = false;
                           }
                        }

                        if (!var6) {
                           if (this.main.getSettings().americandateformat) {
                              var29.sendMessage(ChatColor.RED + "Please use the format: mm/dd/yyyy. Example: 01/05/0001");
                           } else {
                              var29.sendMessage(ChatColor.RED + "Please use the format: dd/mm/yyyy. Example: 01/05/0001");
                           }
                        } else if (Date.fromString(var4[1], this.main.getSettings().americandateformat).getMonth() > this.main.getTimeManager().getCalendar().getTotalMonths()) {
                           var29.sendMessage(ChatColor.RED + "Month can't be more than " + this.main.getTimeManager().getCalendar().getTotalMonths());
                        } else {
                           var23 = Date.fromString(var4[1], this.main.getSettings().americandateformat);
                           if (var23.getDay() != 0 && var23.getMonth() != 0) {
                              this.main.getTimeManager().setDate(var29.getWorld(), var23);
                              var29.sendMessage(ChatColor.GREEN + "Changing date to: " + ChatColor.DARK_GREEN + Date.fromString(var4[1], this.main.getSettings().americandateformat).toString(this.main.getSettings().americandateformat));
                           }
                        }
                     }
                  } else if (var4[0].equalsIgnoreCase("install")) {
                     if (CustomWorldGenerator.isWorldGenerator(var4[1])) {
                        CustomWorldGenerator var32 = CustomWorldGenerator.fromFile(var4[1]);
                        if (CustomBiomeFileLoader.getActiveGenerators().contains(var32)) {
                           if (!CustomBiomeFileLoader.getAlreadyInstalledGenerators().contains(var32)) {
                              CustomBiomeFileLoader.writeFiles(var32);
                              var29.sendMessage(ChatColor.GREEN + "Successfully installed " + var32.toString() + " configuration files. Please restart your server to activate them.");
                           } else {
                              var29.sendMessage(ChatColor.RED + "Files for " + var32.toString() + " are already detected in the biomes folder. Please remove them first");
                           }
                        } else {
                           var29.sendMessage(ChatColor.RED + var32.toString() + " is currently not installed on your server. Please install it first.");
                        }
                     } else {
                        var27 = CustomWorldGenerator.getAllGenerators();
                        var28 = true;
                        var39 = "";
                        var35 = var27.iterator();

                        while(var35.hasNext()) {
                           var10 = (String)var35.next();
                           if (var28) {
                              var39 = var10;
                              var28 = false;
                           } else {
                              var39 = ", " + var39;
                           }
                        }

                        var29.sendMessage(ChatColor.RED + "Please choose between " + var39 + "");
                     }
                  } else {
                     this.displayHelp(var29);
                  }
               } else {
                  String var37;
                  final Player var43;
                  if (var4.length == 3) {
                     if (var4[0].equalsIgnoreCase("set")) {
                        World var38 = Bukkit.getWorld(var4[2]);
                        if (var38 == null) {
                           var29.sendMessage(ChatColor.RED + "Could not find that world!");
                           return true;
                        }

                        this.setByPlayer(var29, var38, var4);
                        return true;
                     }

                     if (var4[0].equalsIgnoreCase("temperature") && var4[1].equalsIgnoreCase("clear")) {
                        var37 = var4[2];
                        if (Bukkit.getPlayer(var37) != null) {
                           var43 = Bukkit.getPlayer(var37);
                           if (!var43.isOnline()) {
                              var29.sendMessage(ChatColor.RED + "Could not find player: " + var37);
                              return true;
                           }

                           this.main.getTemperatureManager().getTempData().clearCustomTemperatureEffects(var43);
                           var29.sendMessage(ChatColor.GREEN + "Removed all custom temperature effects of " + var43.getDisplayName());
                        } else {
                           var29.sendMessage(ChatColor.RED + "Could not find player: " + var37);
                        }

                        return true;
                     }

                     this.displayHelp(var29);
                     return true;
                  }

                  if (var4.length == 5) {
                     if (var4[0].equalsIgnoreCase("temperature") && var4[1].equalsIgnoreCase("modify")) {
                        var37 = var4[2];
                        if (Bukkit.getPlayer(var37) != null) {
                           var43 = Bukkit.getPlayer(var37);
                           if (!var43.isOnline()) {
                              var29.sendMessage(ChatColor.RED + "Could not find player: " + var37);
                              return true;
                           }

                           if (!JavaUtils.isNumeric(var4[3])) {
                              var29.sendMessage(ChatColor.YELLOW + "Usage: /rs temperature modify <player> <temperature-change> <duration(s)>");
                              var29.sendMessage(ChatColor.RED + var4[3] + " is not a number.");
                              return true;
                           }

                           if (!var4[4].contains("FIXED(")) {
                              if (!JavaUtils.isNumeric(var4[4])) {
                                 var29.sendMessage(ChatColor.YELLOW + "Usage: /rs temperature modify <player> <temperature-change> <duration(s)>");
                                 var29.sendMessage(ChatColor.RED + var4[4] + " is not a number.");
                                 return true;
                              }

                              var9 = Integer.parseInt(var4[3]);
                              var8 = Integer.parseInt(var4[4]);
                              this.main.getTemperatureManager().getTempData().applyCustomEffect(var43, var9, var8);
                              var29.sendMessage(ChatColor.GREEN + "Successfully applied a temperature change of " + var4[3] + " to " + var37 + " for " + var4[4] + "s");
                           } else {
                              var9 = Integer.parseInt(var4[3]);
                              String[] var40 = var4[4].replaceAll("FIXED\\(", "").replaceAll("\\)", "").split(",");
                              var11 = Integer.valueOf(var40[0]);
                              final int var41 = Integer.valueOf(var40[1]);
                              int var44;
                              if (var40.length == 2) {
                                 TemperatureEffect var42;
                                 if (this.tempEffects.containsKey(var43.getUniqueId())) {
                                    var42 = (TemperatureEffect)this.tempEffects.get(var43.getUniqueId());
                                    var14 = var42.getModifier();
                                    var44 = var14 + var9;
                                    if (var44 > var41) {
                                       var44 = var41;
                                    }

                                    if (var44 < var11) {
                                       var44 = var11;
                                    }

                                    if (var44 != var14) {
                                       var42.cancel();
                                       TemperatureEffect var16 = SeasonsAPI.getInstance().applyPermanentTemperatureEffect(var43, var44);
                                       this.tempEffects.put(var43.getUniqueId(), var16);
                                    }
                                 } else {
                                    if (var9 > var41) {
                                       var9 = var41;
                                    }

                                    if (var9 < var11) {
                                       var9 = var11;
                                    }

                                    var42 = SeasonsAPI.getInstance().applyPermanentTemperatureEffect(var43, var9);
                                    this.tempEffects.put(var43.getUniqueId(), var42);
                                 }
                              } else {
                                 var13 = Integer.valueOf(var40[2]);
                                 TemperatureEffect var45;
                                 if (this.tempEffects.containsKey(var43.getUniqueId())) {
                                    var45 = (TemperatureEffect)this.tempEffects.get(var43.getUniqueId());
                                    var44 = var45.getModifier();
                                    int var17 = var44 + var9;
                                    if (var17 > var41) {
                                       var17 = var41;
                                    }

                                    if (var17 < var11) {
                                       var17 = var11;
                                    }

                                    if (var17 != var44) {
                                       var45.cancel();
                                       TemperatureEffect var18 = SeasonsAPI.getInstance().applyPermanentTemperatureEffect(var43, var17);
                                       this.tempEffects.put(var43.getUniqueId(), var18);
                                    }

                                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.main, new Runnable() {
                                       public void run() {
                                          TemperatureEffect var1 = (TemperatureEffect)RealisticSeasonsCommand.this.tempEffects.get(var43.getUniqueId());
                                          int var2 = var1.getModifier();
                                          int var3 = var2 - var9;
                                          if (var3 > var41) {
                                             var3 = var41;
                                          }

                                          if (var3 < var11) {
                                             var3 = var11;
                                          }

                                          if (var9 > 0 && var3 < 0) {
                                             var3 = 0;
                                          }

                                          if (var9 < 0 && var3 > 0) {
                                             var3 = 0;
                                          }

                                          if (var3 != var2) {
                                             var1.cancel();
                                             TemperatureEffect var4 = SeasonsAPI.getInstance().applyPermanentTemperatureEffect(var43, var3);
                                             RealisticSeasonsCommand.this.tempEffects.put(var43.getUniqueId(), var4);
                                          }

                                       }
                                    }, (long)var13);
                                 } else {
                                    if (var9 > var41) {
                                       var9 = var41;
                                    }

                                    if (var9 < var11) {
                                       var9 = var11;
                                    }

                                    var45 = SeasonsAPI.getInstance().applyPermanentTemperatureEffect(var43, var9);
                                    this.tempEffects.put(var43.getUniqueId(), var45);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.main, new Runnable() {
                                       public void run() {
                                          TemperatureEffect var1 = (TemperatureEffect)RealisticSeasonsCommand.this.tempEffects.get(var43.getUniqueId());
                                          int var2 = var1.getModifier();
                                          int var3 = var2 - var9;
                                          if (var3 > var41) {
                                             var3 = var41;
                                          }

                                          if (var3 < var11) {
                                             var3 = var11;
                                          }

                                          if (var9 > 0 && var3 < 0) {
                                             var3 = 0;
                                          }

                                          if (var9 < 0 && var3 > 0) {
                                             var3 = 0;
                                          }

                                          if (var3 != var2) {
                                             var1.cancel();
                                             TemperatureEffect var4 = SeasonsAPI.getInstance().applyPermanentTemperatureEffect(var43, var3);
                                             RealisticSeasonsCommand.this.tempEffects.put(var43.getUniqueId(), var4);
                                          }

                                       }
                                    }, (long)var13);
                                 }
                              }
                           }
                        } else {
                           var29.sendMessage(ChatColor.RED + "Could not find player: " + var37);
                        }
                     } else {
                        this.displayHelp(var29);
                     }
                  } else {
                     this.displayHelp(var29);
                  }
               }
            }
         } else {
            var29.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.NO_PERMISSION)));
         }
      } else if (var1 instanceof ConsoleCommandSender) {
         if (var4.length < 1) {
            this.displayConsoleHelp();
         } else {
            List var22;
            String var26;
            String var31;
            Iterator var33;
            if (var4.length == 1) {
               if (var4[0].equalsIgnoreCase("install")) {
                  var22 = CustomWorldGenerator.getAllGenerators();
                  var6 = true;
                  var26 = "";
                  var33 = var22.iterator();

                  while(var33.hasNext()) {
                     var31 = (String)var33.next();
                     if (var6) {
                        var26 = var31;
                        var6 = false;
                     } else {
                        var26 = "/" + var26;
                     }
                  }

                  Bukkit.getLogger().info("Usage: /rs install <" + var26 + ">");
               } else if (var4[0].equalsIgnoreCase("reload")) {
                  Bukkit.getLogger().info("Succesfully reloaded the RealisticSeasons files");
                  Bukkit.getLogger().info("Changes made to biome files will require a full restart");
                  this.main.reload();
               } else if (var4[0].equalsIgnoreCase("biomelist")) {
                  this.logBiomeRegister();
               } else if (var4[0].equalsIgnoreCase("debug")) {
                  this.main.debugMode = !this.main.debugMode;
                  Bukkit.getLogger().info("Debug mode is now: " + String.valueOf(this.main.debugMode));
                  this.main.reload();
               } else {
                  this.displayConsoleHelp();
               }
            } else {
               World var5;
               if (var4.length == 2) {
                  if (var4[0].equalsIgnoreCase("disable")) {
                     var5 = Bukkit.getWorld(var4[1]);
                     if (var5 == null) {
                        Bukkit.getLogger().info("ERROR: Invalid world");
                        return true;
                     }

                     this.disable(var5);
                     Bukkit.getLogger().info("Disabled seasons in this world");
                  } else if (var4[0].equalsIgnoreCase("getinfo")) {
                     var5 = Bukkit.getWorld(var4[1]);
                     if (var5 == null) {
                        Bukkit.getLogger().info("ERROR: Invalid world");
                        return true;
                     }

                     this.main.getSeasonManager().sendSeasonInfoToConsole(var5);
                  } else if (var4[0].equalsIgnoreCase("nextseason")) {
                     var5 = Bukkit.getWorld(var4[1]);
                     if (var5 == null) {
                        Bukkit.getLogger().info("ERROR: Invalid world");
                        return true;
                     }

                     if (!this.nextSeason(var5)) {
                        Bukkit.getLogger().info("Seasons are currently disabled in this world, so there isn't a next one");
                        Bukkit.getLogger().info("Type '/rs set <spring/summer/fall/winter>' to enable seasons in this world");
                     }
                  } else if (var4[0].equalsIgnoreCase("restoreworld")) {
                     var5 = Bukkit.getWorld(var4[1]);
                     if (var5 == null) {
                        Bukkit.getLogger().info("ERROR: Invalid world");
                        return true;
                     }

                     if (!this.setSeason(Season.RESTORE, var5)) {
                        Bukkit.getLogger().info("You can't have seasons in this type of world!");
                     } else {
                        Bukkit.getLogger().info("Succesfully started restoring this world. It is recommended to still leave the plugin on the server for a while, since unloaded chunks will only be restored once a player loads them.");
                     }
                  } else if (var4[0].equalsIgnoreCase("pausetime")) {
                     var5 = Bukkit.getWorld(var4[1]);
                     if (var5 == null) {
                        Bukkit.getLogger().info("ERROR: Invalid world");
                        return true;
                     }

                     if (var5.getEnvironment() == Environment.NETHER || var5.getEnvironment() == Environment.THE_END) {
                        Bukkit.getLogger().info("You can only pause time in an overworld.");
                        return true;
                     }

                     var25 = this.main.getSeasonManager().getSeason(var5);
                     var28 = true;
                     if (var25 == Season.DISABLED || var25 == Season.RESTORE) {
                        var28 = false;
                     }

                     if (var28 && this.main.getSettings().syncWorldTimeWithRealWorld) {
                        Bukkit.getLogger().info("You can't pause time while sync time with real world is enabled.");
                        return true;
                     }

                     if (var28 && this.main.getSettings().affectTime) {
                        if (!this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var5)) {
                           this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var5, false);
                        }

                        var36 = this.main.getTimeManager().hasTime(var5);
                        if (var36) {
                           this.main.getTimeManager().pauseTime(var5);
                           Bukkit.getLogger().info("Time is now paused in this world");
                        } else {
                           this.main.getTimeManager().resumeTime(var5);
                           Bukkit.getLogger().info("Time is now resumed in this world");
                        }
                     } else {
                        var36 = this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var5);
                        if (var36) {
                           this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var5, false);
                           Bukkit.getLogger().info("Time is now paused in this world");
                        } else {
                           this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var5, true);
                           Bukkit.getLogger().info("Time is now resumed in this world");
                        }
                     }
                  } else if (var4[0].equalsIgnoreCase("install")) {
                     if (CustomWorldGenerator.isWorldGenerator(var4[1])) {
                        CustomWorldGenerator var21 = CustomWorldGenerator.fromFile(var4[1]);
                        if (CustomBiomeFileLoader.getActiveGenerators().contains(var21)) {
                           if (!CustomBiomeFileLoader.getAlreadyInstalledGenerators().contains(var21)) {
                              CustomBiomeFileLoader.writeFiles(var21);
                              Bukkit.getLogger().info("Successfully installed " + var21.toString() + " configuration files. Please restart your server to activate them.");
                           } else {
                              Bukkit.getLogger().info("Files for " + var21.toString() + " are already detected in the biomes folder. Please remove them first");
                           }
                        } else {
                           Bukkit.getLogger().info(var21.toString() + " is currently not installed on your server. Please install it first.");
                        }
                     } else {
                        var22 = CustomWorldGenerator.getAllGenerators();
                        var6 = true;
                        var26 = "";
                        var33 = var22.iterator();

                        while(var33.hasNext()) {
                           var31 = (String)var33.next();
                           if (var6) {
                              var26 = var31;
                              var6 = false;
                           } else {
                              var26 = ", " + var26;
                           }
                        }

                        Bukkit.getLogger().info("Please choose between " + var26 + "");
                     }
                  } else {
                     this.displayConsoleHelp();
                  }
               } else {
                  String var19;
                  Player var20;
                  if (var4.length == 3) {
                     if (var4[0].equalsIgnoreCase("temperature") && var4[1].equalsIgnoreCase("toggle")) {
                        var5 = Bukkit.getWorld(var4[2]);
                        if (var5 == null) {
                           Bukkit.getLogger().info("ERROR: Invalid world");
                           return true;
                        }

                        if (this.main.getTemperatureManager().getTempData().isEnabledWorld(var5)) {
                           Bukkit.getLogger().info("Temperature in this world is now: DISABLED");
                        } else {
                           Bukkit.getLogger().info("Temperature in this world is now: ENABLED");
                        }

                        this.main.getTemperatureManager().getTempData().toggleTemperature(var5);
                     } else if (var4[0].equalsIgnoreCase("set")) {
                        var5 = Bukkit.getWorld(var4[2]);
                        if (var5 == null) {
                           Bukkit.getLogger().info("ERROR: Invalid world");
                           return true;
                        }

                        if (var4[1].equalsIgnoreCase("summer")) {
                           if (!this.setSeason(Season.SUMMER, var5)) {
                              Bukkit.getLogger().log(Level.INFO, "You can't have seasons in this type of world!");
                           } else {
                              Bukkit.getLogger().log(Level.INFO, "Manually changed season in this world to: Summer");
                           }
                        } else if (var4[1].equalsIgnoreCase("fall")) {
                           if (!this.setSeason(Season.FALL, var5)) {
                              Bukkit.getLogger().log(Level.INFO, "You can't have seasons in this type of world!");
                           } else {
                              Bukkit.getLogger().log(Level.INFO, "Manually changed season in this world to: Fall");
                           }
                        } else if (var4[1].equalsIgnoreCase("winter")) {
                           if (!this.setSeason(Season.WINTER, var5)) {
                              Bukkit.getLogger().log(Level.INFO, "You can't have seasons in this type of world!");
                           } else {
                              Bukkit.getLogger().log(Level.INFO, "Manually changed season in this world to: Winter");
                           }
                        } else if (var4[1].equalsIgnoreCase("spring")) {
                           if (!this.setSeason(Season.SPRING, var5)) {
                              Bukkit.getLogger().log(Level.INFO, "You can't have seasons in this type of world!");
                           } else {
                              Bukkit.getLogger().log(Level.INFO, "Manually changed season in this world to: Spring");
                           }
                        } else {
                           Bukkit.getLogger().log(Level.INFO, "Please choose between spring, summer, fall and winter");
                        }
                     } else if (var4[0].equalsIgnoreCase("setdate")) {
                        var5 = Bukkit.getWorld(var4[2]);
                        if (var5 == null) {
                           Bukkit.getLogger().info("ERROR: Invalid world");
                           return true;
                        }

                        if (var4[1].split("/").length != 3) {
                           if (this.main.getSettings().americandateformat) {
                              Bukkit.getLogger().info("Please use the format: mm/dd/yyyy");
                           } else {
                              Bukkit.getLogger().info("Please use the format: dd/mm/yyyy");
                           }
                        } else {
                           var6 = true;
                           var7 = var4[1].split("/");
                           var8 = var7.length;

                           for(var9 = 0; var9 < var8; ++var9) {
                              var10 = var7[var9];
                              if (!JavaUtils.isNumeric(var10)) {
                                 var6 = false;
                              }
                           }

                           if (!var6) {
                              if (this.main.getSettings().americandateformat) {
                                 Bukkit.getLogger().info("Please use the format: mm/dd/yyyy. Example: 01/05/0001");
                              } else {
                                 Bukkit.getLogger().info("Please use the format: dd/mm/yyyy. Example: 01/05/0001");
                              }
                           } else {
                              var23 = Date.fromString(var4[1], this.main.getSettings().americandateformat);
                              if (var23.getMonth() > this.main.getTimeManager().getCalendar().getTotalMonths()) {
                                 Bukkit.getLogger().info("Month can't be more than " + String.valueOf(this.main.getTimeManager().getCalendar().getTotalMonths()));
                              } else if (var23.getDay() != 0 && var23.getMonth() != 0) {
                                 this.main.getTimeManager().setDate(var5, var23);
                                 Bukkit.getLogger().info("Changing date to: " + Date.fromString(var4[1], this.main.getSettings().americandateformat).toString(this.main.getSettings().americandateformat));
                              }
                           }
                        }
                     } else {
                        if (var4[0].equalsIgnoreCase("temperature") && var4[1].equalsIgnoreCase("clear")) {
                           var19 = var4[2];
                           if (Bukkit.getPlayer(var19) != null) {
                              var20 = Bukkit.getPlayer(var19);
                              if (!var20.isOnline()) {
                                 Bukkit.getLogger().info("Could not find player: " + var19);
                                 return true;
                              }

                              this.main.getTemperatureManager().getTempData().clearCustomTemperatureEffects(var20);
                              Bukkit.getLogger().info("Removed all custom temperature effects of " + var20.getDisplayName());
                           } else {
                              Bukkit.getLogger().info("Could not find player: " + var19);
                           }

                           return true;
                        }

                        this.displayConsoleHelp();
                     }
                  } else {
                     if (var4.length == 4) {
                        this.displayConsoleHelp();
                        return true;
                     }

                     if (var4.length == 5) {
                        if (var4[0].equalsIgnoreCase("temperature") && var4[1].equalsIgnoreCase("modify")) {
                           var19 = var4[2];
                           if (Bukkit.getPlayer(var19) != null) {
                              var20 = Bukkit.getPlayer(var19);
                              if (!var20.isOnline()) {
                                 Bukkit.getLogger().info("Could not find player: " + var19);
                                 return true;
                              }

                              if (!JavaUtils.isNumeric(var4[3])) {
                                 Bukkit.getLogger().info("Usage: /rs temperature modify <player> <temperature-change> <duration(s)>");
                                 Bukkit.getLogger().info(var4[3] + " is not a number.");
                                 return true;
                              }

                              if (!var4[4].contains("FIXED(")) {
                                 if (!JavaUtils.isNumeric(var4[4])) {
                                    Bukkit.getLogger().info("Usage: /rs temperature modify <player> <temperature-change> <duration(s)>");
                                    Bukkit.getLogger().info(" is not a number.");
                                    return true;
                                 }

                                 var8 = Integer.parseInt(var4[3]);
                                 int var24 = Integer.parseInt(var4[4]);
                                 this.main.getTemperatureManager().getTempData().applyCustomEffect(var20, var8, var24);
                              } else {
                                 var8 = Integer.parseInt(var4[3]);
                                 String[] var30 = var4[4].replaceAll("FIXED\\(", "").replaceAll("\\)", "").split(",");
                                 int var34 = Integer.valueOf(var30[0]);
                                 var11 = Integer.valueOf(var30[1]);
                                 TemperatureEffect var12;
                                 if (this.tempEffects.containsKey(var20.getUniqueId())) {
                                    var12 = (TemperatureEffect)this.tempEffects.get(var20.getUniqueId());
                                    var13 = var12.getModifier();
                                    var14 = var13 + var8;
                                    if (var14 > var11) {
                                       var14 = var11;
                                    }

                                    if (var14 < var34) {
                                       var14 = var34;
                                    }

                                    if (var14 != var13) {
                                       var12.cancel();
                                       TemperatureEffect var15 = SeasonsAPI.getInstance().applyPermanentTemperatureEffect(var20, var14);
                                       this.tempEffects.put(var20.getUniqueId(), var15);
                                    }
                                 } else {
                                    if (var8 > var11) {
                                       var8 = var11;
                                    }

                                    if (var8 < var34) {
                                       var8 = var34;
                                    }

                                    var12 = SeasonsAPI.getInstance().applyPermanentTemperatureEffect(var20, var8);
                                    this.tempEffects.put(var20.getUniqueId(), var12);
                                 }
                              }
                           } else {
                              Bukkit.getLogger().info("Could not find player: " + var19);
                           }
                        } else {
                           this.displayConsoleHelp();
                        }
                     } else {
                        this.displayConsoleHelp();
                     }
                  }
               }
            }
         }
      }

      return true;
   }

   private void logBiomeRegister() {
      Bukkit.getLogger().info("----------------------------------------------");
      Bukkit.getLogger().info(" ");
      Bukkit.getLogger().info(" Full list of RealisticSeasons biomes: ");
      Bukkit.getLogger().info(" ");
      Iterator var1 = this.main.getNMSUtils().getBiomes(this.main.getSettings().biomeDisplayName).iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         Bukkit.getLogger().info(var2);
      }

      Bukkit.getLogger().info(" ");
      Bukkit.getLogger().info("---------------------------------------");
      Bukkit.getLogger().info(" Biome replacements:");
      ArrayList var17 = new ArrayList();
      boolean var18 = this.main.getSettings().subSeasonsEnabled;
      Season[] var3 = new Season[]{Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER};
      List var4 = this.main.getNMSUtils().getBiomes("minecraft");
      var4.addAll(this.main.getNMSUtils().getCustomBiomes(this.main.getSettings().biomeDisplayName));
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         Bukkit.getLogger().info("----------------------------------------------");
         Bukkit.getLogger().info(" ");
         Bukkit.getLogger().info("Biome: " + var6);
         Season[] var7 = var3;
         int var8 = var3.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            Season var10 = var7[var9];
            Bukkit.getLogger().info(" ");
            Bukkit.getLogger().info(var10.toString());
            if (var10 == Season.SUMMER) {
               if (var18) {
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixSummerReplacement(this.main.getNMSUtils().getBiomeID(var6), 0)));
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixSummerReplacement(this.main.getNMSUtils().getBiomeID(var6), 1)));
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixSummerReplacement(this.main.getNMSUtils().getBiomeID(var6), 2)));
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixSummerReplacement(this.main.getNMSUtils().getBiomeID(var6), 3)));
                  Bukkit.getLogger().info("Start season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixSummerReplacement(this.main.getNMSUtils().getBiomeID(var6), 0)));
                  Bukkit.getLogger().info("Early season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixSummerReplacement(this.main.getNMSUtils().getBiomeID(var6), 1)));
                  Bukkit.getLogger().info("Late season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixSummerReplacement(this.main.getNMSUtils().getBiomeID(var6), 2)));
                  Bukkit.getLogger().info("End season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixSummerReplacement(this.main.getNMSUtils().getBiomeID(var6), 3)));
               }

               var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getSummerReplacement(this.main.getNMSUtils().getBiomeID(var6))));
               Bukkit.getLogger().info("Mid/Full season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getSummerReplacement(this.main.getNMSUtils().getBiomeID(var6))));
            } else if (var10 == Season.SPRING) {
               if (var18) {
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixSpringReplacement(this.main.getNMSUtils().getBiomeID(var6), 0)));
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixSpringReplacement(this.main.getNMSUtils().getBiomeID(var6), 1)));
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixSpringReplacement(this.main.getNMSUtils().getBiomeID(var6), 2)));
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixSpringReplacement(this.main.getNMSUtils().getBiomeID(var6), 3)));
                  Bukkit.getLogger().info("Start season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixSpringReplacement(this.main.getNMSUtils().getBiomeID(var6), 0)));
                  Bukkit.getLogger().info("Early season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixSpringReplacement(this.main.getNMSUtils().getBiomeID(var6), 1)));
                  Bukkit.getLogger().info("Late season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixSpringReplacement(this.main.getNMSUtils().getBiomeID(var6), 2)));
                  Bukkit.getLogger().info("End season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixSpringReplacement(this.main.getNMSUtils().getBiomeID(var6), 3)));
               }

               var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getSpringReplacement(this.main.getNMSUtils().getBiomeID(var6))));
               Bukkit.getLogger().info("Mid/Full season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getSpringReplacement(this.main.getNMSUtils().getBiomeID(var6))));
            } else if (var10 == Season.WINTER) {
               if (var18) {
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixWinterReplacement(this.main.getNMSUtils().getBiomeID(var6), 0)));
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixWinterReplacement(this.main.getNMSUtils().getBiomeID(var6), 1)));
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixWinterReplacement(this.main.getNMSUtils().getBiomeID(var6), 2)));
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixWinterReplacement(this.main.getNMSUtils().getBiomeID(var6), 3)));
                  Bukkit.getLogger().info("Start season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixWinterReplacement(this.main.getNMSUtils().getBiomeID(var6), 0)));
                  Bukkit.getLogger().info("Early season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixWinterReplacement(this.main.getNMSUtils().getBiomeID(var6), 1)));
                  Bukkit.getLogger().info("Late season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixWinterReplacement(this.main.getNMSUtils().getBiomeID(var6), 2)));
                  Bukkit.getLogger().info("End season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixWinterReplacement(this.main.getNMSUtils().getBiomeID(var6), 3)));
               }

               var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getWinterReplacement(this.main.getNMSUtils().getBiomeID(var6))));
               Bukkit.getLogger().info("Mid/Full season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getWinterReplacement(this.main.getNMSUtils().getBiomeID(var6))));
            } else if (var10 == Season.FALL) {
               if (var18) {
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixFallReplacement(this.main.getNMSUtils().getBiomeID(var6), 0)));
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixFallReplacement(this.main.getNMSUtils().getBiomeID(var6), 1)));
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixFallReplacement(this.main.getNMSUtils().getBiomeID(var6), 2)));
                  var17.add(this.main.getNMSUtils().getBiome(BiomeRegister.getMixFallReplacement(this.main.getNMSUtils().getBiomeID(var6), 3)));
                  Bukkit.getLogger().info("Start season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixFallReplacement(this.main.getNMSUtils().getBiomeID(var6), 0)));
                  Bukkit.getLogger().info("Early season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixFallReplacement(this.main.getNMSUtils().getBiomeID(var6), 1)));
                  Bukkit.getLogger().info("Late season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixFallReplacement(this.main.getNMSUtils().getBiomeID(var6), 2)));
                  Bukkit.getLogger().info("End season: " + this.main.getNMSUtils().getBiome(BiomeRegister.getMixFallReplacement(this.main.getNMSUtils().getBiomeID(var6), 3)));
               }

               List var11 = BiomeRegister.getFallReplacements(this.main.getNMSUtils().getBiomeID(var6));
               String var12 = null;
               boolean var13 = true;
               if (var11.size() == 1) {
                  var12 = this.main.getNMSUtils().getBiome((Integer)var11.get(0));
                  var17.add(var12);
               } else {
                  Iterator var14 = var11.iterator();

                  while(var14.hasNext()) {
                     Integer var15 = (Integer)var14.next();
                     String var16 = this.main.getNMSUtils().getBiome(var15);
                     var17.add(var16);
                     if (var13) {
                        var12 = var16;
                        var13 = false;
                     } else {
                        var12 = var12 + ", " + var16;
                     }
                  }
               }

               Bukkit.getLogger().info("Mid/Full season: " + var12);
            }
         }

         Bukkit.getLogger().info(" ");
         String var19 = null;
         boolean var20 = true;
         if (var17.size() == 1) {
            var19 = (String)var17.get(0);
         } else {
            Iterator var21 = var17.iterator();

            while(var21.hasNext()) {
               String var22 = (String)var21.next();
               if (!var22.equals("NONE")) {
                  if (var20) {
                     var19 = var22;
                     var20 = false;
                  } else {
                     var19 = var19 + ", " + var22;
                  }
               }
            }
         }

         if (var19 == null || var19 == "") {
            var19 = "NONE";
         }

         var17.clear();
         Bukkit.getLogger().info("All possible replacements for this biome: " + var19);
      }

      Bukkit.getLogger().info("----------------------------------------------");
      Bukkit.getLogger().info(" ");
      Bukkit.getLogger().info(" For a better viewing experience, use your server's logs to navigate the list.");
      Bukkit.getLogger().info(" ");
      Bukkit.getLogger().info("----------------------------------------------");
   }

   public void displayHelp(Player var1) {
      var1.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Realistic Seasons v" + this.main.getPlugin().getDescription().getVersion());
      var1.sendMessage(ChatColor.YELLOW + "Wiki: " + ChatColor.GOLD + "wiki.realisticseasons.com");
      var1.sendMessage(ChatColor.YELLOW + "Discord: " + ChatColor.GOLD + "https://discord.com/invite/BxPzCgup56");
      var1.sendMessage(ChatColor.AQUA + "" + ChatColor.UNDERLINE + "Normal commands:");
      var1.sendMessage(ChatColor.DARK_GREEN + "/season : " + ChatColor.GREEN + ChatColor.ITALIC + "display current season, year, day and days left until next season");
      var1.sendMessage(ChatColor.DARK_GREEN + "/toggleseasoncolors : " + ChatColor.GREEN + ChatColor.ITALIC + "toggle custom biome colors (player running the command only)");
      var1.sendMessage(ChatColor.DARK_GREEN + "/toggletemperature (<player>): " + ChatColor.GREEN + ChatColor.ITALIC + "toggle temperature system (player running the command only)");
      var1.sendMessage(ChatColor.DARK_GREEN + "/toggleseasonparticles : " + ChatColor.GREEN + ChatColor.ITALIC + "toggle season particles (player running the command only)");
      var1.sendMessage(ChatColor.DARK_GREEN + "/togglefahrenheit : " + ChatColor.GREEN + ChatColor.ITALIC + "toggle temperature between Celcius and Fahrenheit (player running the command only)");
      var1.sendMessage(ChatColor.DARK_GREEN + "/currentbiome : " + ChatColor.GREEN + ChatColor.ITALIC + "get current biome");
      var1.sendMessage(ChatColor.AQUA + "" + ChatColor.UNDERLINE + "Admin commands:");
      var1.sendMessage(ChatColor.DARK_GREEN + "/rs set <spring/summer/fall/winter> (<world>): " + ChatColor.GREEN + ChatColor.ITALIC + "change the current season in (this) world");
      if (this.main.getSettings().americandateformat) {
         var1.sendMessage(ChatColor.DARK_GREEN + "/rs setdate mm/dd/yyyy : " + ChatColor.GREEN + ChatColor.ITALIC + "set the date in the current world");
      } else {
         var1.sendMessage(ChatColor.DARK_GREEN + "/rs setdate dd/mm/yyyy : " + ChatColor.GREEN + ChatColor.ITALIC + "set the date in the current world");
      }

      var1.sendMessage(ChatColor.DARK_GREEN + "/rs nextseason : " + ChatColor.GREEN + ChatColor.ITALIC + "change the current season in this world to the next one");
      var1.sendMessage(ChatColor.DARK_GREEN + "/rs disable : " + ChatColor.GREEN + ChatColor.ITALIC + "disable seasons in this world");
      var1.sendMessage(ChatColor.DARK_GREEN + "/rs restoreworld : " + ChatColor.GREEN + ChatColor.ITALIC + "restores all changes done by this plugin in this world");
      var1.sendMessage(ChatColor.DARK_GREEN + "/rs getinfo : " + ChatColor.GREEN + ChatColor.ITALIC + "display current season, year, day and days left until next season");
      var1.sendMessage(ChatColor.DARK_GREEN + "/rs temperature toggle : " + ChatColor.GREEN + ChatColor.ITALIC + "toggle temperature in the current world. Also works in the nether and the end");
      var1.sendMessage(ChatColor.DARK_GREEN + "/rs temperature modify <player> <temperature-change> <duration(s)> : " + ChatColor.GREEN + ChatColor.ITALIC + "apply a temporary temperature change to a specific player. Useful for custom items etc.");
      var1.sendMessage(ChatColor.DARK_GREEN + "/rs temperature clear <player> : " + ChatColor.GREEN + ChatColor.ITALIC + "clear all temporary custom temperature effects that were added to a player. Useful for custom items etc.");
      var1.sendMessage(ChatColor.DARK_GREEN + "/rs install <generator> : " + ChatColor.GREEN + ChatColor.ITALIC + "install the correct biome files for the specified world generator");
      var1.sendMessage(ChatColor.DARK_GREEN + "/rs pausetime : " + ChatColor.GREEN + ChatColor.ITALIC + "temporarily pause/unpause time in the current world");
      var1.sendMessage(ChatColor.DARK_GREEN + "/rs reload : " + ChatColor.GREEN + ChatColor.ITALIC + "reload all RealisticSeasons files, except the biome files");
      var1.sendMessage(ChatColor.DARK_GREEN + "/rs help : " + ChatColor.GREEN + ChatColor.ITALIC + "display this message");
   }

   public void displayConsoleHelp() {
      Bukkit.getLogger().info("Realistic Seasons v" + this.main.getPlugin().getDescription().getVersion());
      Bukkit.getLogger().info("Wiki: wiki.realisticseasons.com");
      Bukkit.getLogger().info("Discord: https://discord.com/invite/BxPzCgup56");
      Bukkit.getLogger().info("Normal commands:");
      Bukkit.getLogger().info("/season <world>: display current season, year, day and days left until next season");
      Bukkit.getLogger().info("Admin commands:");
      Bukkit.getLogger().info("/rs set <spring/summer/fall/winter> <world>: change the current season");
      if (this.main.getSettings().americandateformat) {
         Bukkit.getLogger().info("/rs setdate mm/dd/yyyy <world>: set the date");
      } else {
         Bukkit.getLogger().info("/rs setdate dd/mm/yyyy <world>: set the date");
      }

      Bukkit.getLogger().info("/rs nextseason <world>: change the current season to the next one");
      Bukkit.getLogger().info("/rs disable <world>: disable seasons in the specified world");
      Bukkit.getLogger().info("/rs restoreworld <world>: restores all changes done by this plugin");
      Bukkit.getLogger().info("/rs getinfo <world>: display current season, year, day and days left until next season");
      Bukkit.getLogger().info("/rs temperature toggle <world>: toggle temperature. Also works in the nether and the end");
      Bukkit.getLogger().info("/rs temperature modify <player> <temperature-change> <duration(s)> : apply a temporary temperature change to a specific player. Useful for custom items etc.");
      Bukkit.getLogger().info("/rs temperature clear <player> : clear all temporary custom temperature effects that were added to a player. Useful for custom items etc.");
      Bukkit.getLogger().info("/rs install <generator> : install the correct biome files for the specified world generator");
      Bukkit.getLogger().info("/rs pausetime <world>: temporarily pause/unpause time in the specified world");
      Bukkit.getLogger().info("/rs biomelist : Generates a big list of custom biomes that RealisticSeasons uses. Useful for resource packs, creators, etc");
      Bukkit.getLogger().info("/rs reload : reload all RealisticSeasons files, except the biome files");
      Bukkit.getLogger().info("/rs help : display this message");
   }

   public boolean nextSeason(World var1) {
      if (this.main.getSeasonManager().getSeason(var1) != Season.DISABLED) {
         this.main.getSeasonManager().nextSeason(var1);
         return true;
      } else {
         return false;
      }
   }

   public boolean setSeason(Season var1, World var2) {
      if (var2.getEnvironment() != Environment.NETHER && var2.getEnvironment() != Environment.THE_END) {
         if (!this.main.getSettings().subSeasonsEnabled || !this.main.getSettings().calendarEnabled || var1 != Season.SUMMER && var1 != Season.WINTER && var1 != Season.SPRING && var1 != Season.FALL) {
            this.main.getSeasonManager().setSeason(var2, var1);
         } else {
            Date var3;
            Date var4;
            if (this.main.getSeasonManager().getSeason(var2) != Season.DISABLED && this.main.getSeasonManager().getSeason(var2) != Season.RESTORE) {
               var3 = this.main.getTimeManager().getHalfwaySeason(var1);
               var4 = this.main.getTimeManager().getDate(var2);
               Date var5 = new Date(var3.getDay(), var3.getMonth(), var4.getYear());
               if (var5.isLaterInYear(var4)) {
                  var5 = new Date(var3.getDay(), var3.getMonth(), var4.getYear() + 1);
               }

               this.main.getSeasonManager().setSeason(var2, var1);
               this.main.getTimeManager().setDate(var2, var5);
            } else {
               var3 = this.main.getTimeManager().getHalfwaySeason(var1);
               var4 = new Date(var3.getDay(), var3.getMonth(), 0);
               this.main.getSeasonManager().setSeason(var2, var1);
               this.main.getTimeManager().setDate(var2, var4);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean disable(World var1) {
      if (var1.getEnvironment() != Environment.NETHER && var1.getEnvironment() != Environment.THE_END) {
         this.main.getSeasonManager().setSeason(var1, Season.DISABLED);
         return true;
      } else {
         return false;
      }
   }

   public boolean hasSeasons(Player var1) {
      if (var1.getWorld().getEnvironment() != Environment.NETHER && var1.getWorld().getEnvironment() != Environment.THE_END) {
         return true;
      } else {
         var1.sendMessage(ChatColor.RED + "You can't have seasons in this type of world!");
         return false;
      }
   }

   public void reload() {
      this.main.getSettings().reload();
      this.main.getLangManager().reload();
      this.main.getTimeManager().load();
   }

   public void getinfo(Player var1) {
      this.main.getSeasonManager().sendSeasonInfo(var1);
   }

   public void setByPlayer(Player var1, World var2, String[] var3) {
      if (var3[1].equalsIgnoreCase("summer")) {
         if (!this.setSeason(Season.SUMMER, var2)) {
            var1.sendMessage(ChatColor.RED + "You can't have seasons in this type of world!");
         } else {
            var1.sendMessage(ChatColor.GREEN + "Manually changed season in this world to: " + ChatColor.DARK_GREEN + "Summer");
         }
      } else if (var3[1].equalsIgnoreCase("fall")) {
         if (!this.setSeason(Season.FALL, var2)) {
            var1.sendMessage(ChatColor.RED + "You can't have seasons in this type of world!");
         } else {
            var1.sendMessage(ChatColor.GREEN + "Manually changed season in this world to: " + ChatColor.DARK_GREEN + "Fall");
         }
      } else if (var3[1].equalsIgnoreCase("winter")) {
         if (!this.setSeason(Season.WINTER, var2)) {
            var1.sendMessage(ChatColor.RED + "You can't have seasons in this type of world!");
         } else {
            var1.sendMessage(ChatColor.GREEN + "Manually changed season in this world to: " + ChatColor.DARK_GREEN + "Winter");
         }
      } else if (var3[1].equalsIgnoreCase("spring")) {
         if (!this.setSeason(Season.SPRING, var2)) {
            var1.sendMessage(ChatColor.RED + "You can't have seasons in this type of world!");
         } else {
            var1.sendMessage(ChatColor.GREEN + "Manually changed season in this world to: " + ChatColor.DARK_GREEN + "Spring");
         }
      } else {
         var1.sendMessage(ChatColor.RED + "Please choose between spring, summer, fall and winter");
      }

   }
}
