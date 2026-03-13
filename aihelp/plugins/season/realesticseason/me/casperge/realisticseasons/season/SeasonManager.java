package me.casperge.realisticseasons.season;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.WeakHashMap;
import me.casperge.enums.GameRuleType;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.api.SeasonChangeEvent;
import me.casperge.realisticseasons.biome.BiomeUtils;
import me.casperge.realisticseasons.calendar.Date;
import me.casperge.realisticseasons.data.LanguageManager;
import me.casperge.realisticseasons.data.MessageType;
import me.casperge.realisticseasons.runnables.AnimalRemover;
import me.casperge.realisticseasons.runnables.ChunkRefresher;
import me.casperge.realisticseasons.runnables.FallBlockTicker;
import me.casperge.realisticseasons.runnables.RestoreWorldTicker;
import me.casperge.realisticseasons.runnables.SpringBlockTicker;
import me.casperge.realisticseasons.runnables.SummerBlockTicker;
import me.casperge.realisticseasons.runnables.TimeHandler;
import me.casperge.realisticseasons.runnables.WinterBlockTicker;
import me.casperge.realisticseasons.utils.ChunkUtils;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

public class SeasonManager {
   public RealisticSeasons main;
   public WeakHashMap<World, Season> worldData = new WeakHashMap();
   public WeakHashMap<World, SubSeason> worldDataSubSeasons = new WeakHashMap();
   public WeakHashMap<World, ChunkRefresher> refreshers = new WeakHashMap();
   private WeakHashMap<World, LinkedHashSet<SeasonChunk>> fallqueue = new WeakHashMap();
   private WeakHashMap<World, LinkedHashSet<SeasonChunk>> fallchecked = new WeakHashMap();
   private WeakHashMap<World, LinkedHashSet<SeasonChunk>> springqueue = new WeakHashMap();
   private WeakHashMap<World, LinkedHashSet<SeasonChunk>> springchecked = new WeakHashMap();
   private TimeHandler thandler;
   private AnimalRemover remover;
   private WeakHashMap<World, LinkedHashSet<SeasonChunk>> summerqueue = new WeakHashMap();
   private WeakHashMap<World, LinkedHashSet<SeasonChunk>> summerchecked = new WeakHashMap();
   private WeakHashMap<World, LinkedHashSet<SeasonChunk>> restorequeue = new WeakHashMap();
   private WeakHashMap<World, LinkedHashSet<SeasonChunk>> restorechecked = new WeakHashMap();

   public SeasonManager(final RealisticSeasons var1) {
      this.main = var1;
      Bukkit.getScheduler().scheduleSyncRepeatingTask(var1, new Runnable() {
         public void run() {
            if (JavaUtils.getRandom().nextInt(10) == 1) {
               Iterator var1x = SeasonManager.this.worldData.keySet().iterator();

               while(var1x.hasNext()) {
                  World var2 = (World)var1x.next();
                  if (SeasonManager.this.getCheckedList(var2, SeasonManager.this.getSeason(var2)) != null) {
                     if (SeasonManager.this.getCheckedList(var2, SeasonManager.this.getSeason(var2)).size() > 70000) {
                        SeasonManager.this.clearChunkCheckedList(var2, SeasonManager.this.getSeason(var2));
                     }

                     if (SeasonManager.this.getQueue(var2, SeasonManager.this.getSeason(var2)).size() > 70000) {
                        SeasonManager.this.clearChunkQueue(var2, SeasonManager.this.getSeason(var2));
                     }
                  }
               }
            }

            if (var1.getSettings().calendarEnabled) {
               SeasonManager.this.runSubSeasonCheck();
            }
         }
      }, 400L, 400L);
   }

   public void runSubSeasonCheck() {
      Iterator var1 = this.worldData.keySet().iterator();

      while(true) {
         World var2;
         SubSeason var3;
         SubSeason var4;
         do {
            do {
               do {
                  do {
                     if (!var1.hasNext()) {
                        return;
                     }

                     var2 = (World)var1.next();
                  } while(this.getSeason(var2) == Season.DISABLED);
               } while(this.getSeason(var2) == Season.RESTORE);
            } while(!this.main.getSettings().subSeasonsEnabled);

            var3 = this.getSubSeason(var2);
            var4 = this.main.getTimeManager().getCorrectSubSeason(var2);
         } while(var3 == var4);

         this.setSubSeason(var2, var4, true);
         this.clearChunkCheckedList(var2, this.getSeason(var2));
         this.clearChunkQueue(var2, this.getSeason(var2));
         Chunk[] var5 = var2.getLoadedChunks();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Chunk var8 = var5[var7];
            if (this.getQueue(var2, this.getSeason(var2)) != null) {
               this.getQueue(var2, this.getSeason(var2)).add(new SeasonChunk(var2.getName(), var8.getX(), var8.getZ(), System.currentTimeMillis()));
            }
         }
      }
   }

   public LinkedHashSet<SeasonChunk> getQueue(World var1, Season var2) {
      switch(var2) {
      case FALL:
         if (!this.fallqueue.containsKey(var1)) {
            this.fallqueue.put(var1, new LinkedHashSet());
         }

         return (LinkedHashSet)this.fallqueue.get(var1);
      case SUMMER:
         if (!this.summerqueue.containsKey(var1)) {
            this.summerqueue.put(var1, new LinkedHashSet());
         }

         return (LinkedHashSet)this.summerqueue.get(var1);
      case SPRING:
         if (!this.springqueue.containsKey(var1)) {
            this.springqueue.put(var1, new LinkedHashSet());
         }

         return (LinkedHashSet)this.springqueue.get(var1);
      case RESTORE:
         if (!this.restorequeue.containsKey(var1)) {
            this.restorequeue.put(var1, new LinkedHashSet());
         }

         return (LinkedHashSet)this.restorequeue.get(var1);
      default:
         return null;
      }
   }

   public HashSet<SeasonChunk> getCheckedList(World var1, Season var2) {
      switch(var2) {
      case FALL:
         if (!this.fallchecked.containsKey(var1)) {
            this.fallchecked.put(var1, new LinkedHashSet());
         }

         return (HashSet)this.fallchecked.get(var1);
      case SUMMER:
         if (!this.summerchecked.containsKey(var1)) {
            this.summerchecked.put(var1, new LinkedHashSet());
         }

         return (HashSet)this.summerchecked.get(var1);
      case SPRING:
         if (!this.springchecked.containsKey(var1)) {
            this.springchecked.put(var1, new LinkedHashSet());
         }

         return (HashSet)this.springchecked.get(var1);
      case RESTORE:
         if (!this.restorechecked.containsKey(var1)) {
            this.restorechecked.put(var1, new LinkedHashSet());
         }

         return (HashSet)this.restorechecked.get(var1);
      default:
         return null;
      }
   }

   public void clearChunkCheckedList(World var1, Season var2) {
      switch(var2) {
      case FALL:
         if (!this.fallchecked.containsKey(var1)) {
            return;
         }

         ((LinkedHashSet)this.fallchecked.get(var1)).clear();
         return;
      case SUMMER:
         if (!this.summerchecked.containsKey(var1)) {
            return;
         }

         ((LinkedHashSet)this.summerchecked.get(var1)).clear();
         return;
      case SPRING:
         if (!this.springchecked.containsKey(var1)) {
            return;
         }

         ((LinkedHashSet)this.springchecked.get(var1)).clear();
         return;
      case RESTORE:
         if (!this.restorechecked.containsKey(var1)) {
            return;
         }

         ((LinkedHashSet)this.restorechecked.get(var1)).clear();
         return;
      default:
      }
   }

   public void clearChunkQueue(World var1, Season var2) {
      switch(var2) {
      case FALL:
         if (!this.fallqueue.containsKey(var1)) {
            return;
         }

         ((LinkedHashSet)this.fallqueue.get(var1)).clear();
         return;
      case SUMMER:
         if (!this.summerqueue.containsKey(var1)) {
            return;
         }

         ((LinkedHashSet)this.summerqueue.get(var1)).clear();
         return;
      case SPRING:
         if (!this.summerqueue.containsKey(var1)) {
            return;
         }

         ((LinkedHashSet)this.summerqueue.get(var1)).clear();
         return;
      case RESTORE:
         if (!this.summerqueue.containsKey(var1)) {
            return;
         }

         ((LinkedHashSet)this.summerqueue.get(var1)).clear();
         return;
      default:
      }
   }

   public Season getNextSeason(World var1) {
      Season var2 = this.getSeason(var1);
      if (var2 == Season.SPRING) {
         return Season.SUMMER;
      } else if (var2 == Season.SUMMER) {
         return Season.FALL;
      } else if (var2 == Season.FALL) {
         return Season.WINTER;
      } else {
         return var2 == Season.WINTER ? Season.SPRING : null;
      }
   }

   public void sendSeasonInfo(Player var1) {
      if (var1.getWorld().getEnvironment() != Environment.NETHER && var1.getWorld().getEnvironment() != Environment.THE_END) {
         Season var2 = this.main.getSeasonManager().getSeason(var1.getWorld());
         if (var2 == Season.DISABLED) {
            var1.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_DISABLED)));
            if (var1.hasPermission("realisticseasons.admin")) {
               var1.sendMessage(ChatColor.RED + "Set one by running /rs set <season>");
            }
         } else if (var2 == Season.RESTORE) {
            var1.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_RESTORE)));
            if (var1.hasPermission("realisticseasons.admin")) {
               var1.sendMessage(ChatColor.RED + "Set one by running /rs set <season>");
            }
         } else {
            if (!((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_CURRENTSEASON)).equals("")) {
               var1.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_CURRENTSEASON)).replaceAll("\\$current_season\\$", var2.toString())));
            }

            if (this.main.getSettings().calendarEnabled) {
               if (!((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_CURRENTDATE)).equals("")) {
                  var1.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_CURRENTDATE)).replaceAll("\\$current_weekday\\$", this.main.getTimeManager().getWeekDay(this.main.getTimeManager().getDate(var1.getWorld()))).replaceAll("\\$current_month\\$", this.main.getTimeManager().getCalendar().getMonth(this.main.getTimeManager().getDate(var1.getWorld()).getMonth()).getName()).replaceAll("\\$current_day\\$", String.valueOf(this.main.getTimeManager().getDate(var1.getWorld()).getDay())).replaceAll("\\$current_year\\$", String.valueOf(this.main.getTimeManager().getDate(var1.getWorld()).getYear()))));
               }

               if (!((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_DAYSUNTILNEXT)).equals("")) {
                  var1.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_DAYSUNTILNEXT)).replaceAll("\\$next_season\\$", this.main.getSeasonManager().getNextSeason(var1.getWorld()).toString()).replaceAll("\\$days_until_next_season\\$", String.valueOf(this.main.getTimeManager().getDaysUntilNextSeason(var1.getWorld())))));
               }

               if (!((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_TIME)).equals("")) {
                  var1.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_TIME)).replaceAll("\\$time\\$", this.main.getTimeManager().getTimeAsString(var1.getWorld()))));
               }

               if (!((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_EVENTS)).equals("")) {
                  var1.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_EVENTS)).replaceAll("\\$events\\$", this.main.getEventManager().getActiveEventsAsString(var1.getWorld()))));
               }

               String var3 = this.main.getEventManager().getNextEventsAsString(var1.getWorld());
               int var4 = this.main.getEventManager().getDaysUntilNextEvent(var1.getWorld());
               if (var4 != -1 && !((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_DAYSUNTILNEXTEVENT)).equals("")) {
                  var1.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_DAYSUNTILNEXTEVENT)).replaceAll("\\$next_event\\$", var3).replaceAll("\\$days_until_next_event\\$", String.valueOf(var4))));
               }
            }

            if (this.main.getTemperatureManager().getTempData().isEnabled()) {
               int var6 = this.main.getTemperatureManager().getPlayerAirTemperature(var1);
               ChatColor var7 = this.main.getTemperatureManager().getColorCode(var6);
               String var5;
               if (this.main.getTemperatureManager().getTempData().getTempSettings().isConvertToFahrenheit() && !this.main.getTemperatureManager().hasFahrenheitEnabled(var1) || !this.main.getTemperatureManager().getTempData().getTempSettings().isConvertToFahrenheit() && this.main.getTemperatureManager().hasFahrenheitEnabled(var1)) {
                  var6 = JavaUtils.convertToFahrenheit(var6);
                  var5 = "&" + var7.getChar() + var6 + this.main.getTemperatureManager().getTempData().getTempSettings().getFahrenheitMessage();
                  if (!((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_AIRTEMPERATURE)).equals("")) {
                     var1.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_AIRTEMPERATURE)).replaceAll("ï¿½C", "").replaceAll("ï¿½F", "").replaceAll("\\$air_temperature\\$", var5)));
                  }
               } else {
                  var5 = "&" + var7.getChar() + var6 + this.main.getTemperatureManager().getTempData().getTempSettings().getCelciusMessage();
                  if (!((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_AIRTEMPERATURE)).equals("")) {
                     var1.sendMessage(JavaUtils.hex(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_AIRTEMPERATURE)).replaceAll("ï¿½C", "").replaceAll("ï¿½F", "").replaceAll("\\$air_temperature\\$", var5)));
                  }
               }
            }
         }
      } else {
         var1.sendMessage(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_DISABLED)));
      }

   }

   public void sendSeasonInfoToConsole(World var1) {
      if (var1.getEnvironment() != Environment.NETHER && var1.getEnvironment() != Environment.THE_END) {
         Season var2 = this.main.getSeasonManager().getSeason(var1);
         if (var2 == Season.DISABLED) {
            Bukkit.getLogger().info((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_DISABLED));
         } else if (var2 == Season.RESTORE) {
            Bukkit.getLogger().info((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_RESTORE));
         } else {
            if (!((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_CURRENTSEASON)).equals("")) {
               Bukkit.getLogger().info(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_CURRENTSEASON)).replaceAll("\\$current_season\\$", var2.toString()));
            }

            if (this.main.getSettings().calendarEnabled) {
               if (!((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_CURRENTDATE)).equals("")) {
                  Bukkit.getLogger().info(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_CURRENTDATE)).replaceAll("\\$current_weekday\\$", this.main.getTimeManager().getWeekDay(this.main.getTimeManager().getDate(var1))).replaceAll("\\$current_month\\$", this.main.getTimeManager().getCalendar().getMonth(this.main.getTimeManager().getDate(var1).getMonth()).getName()).replaceAll("\\$current_day\\$", String.valueOf(this.main.getTimeManager().getDate(var1).getDay())).replaceAll("\\$current_year\\$", String.valueOf(this.main.getTimeManager().getDate(var1).getYear())));
               }

               if (!((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_DAYSUNTILNEXT)).equals("")) {
                  Bukkit.getLogger().info(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_DAYSUNTILNEXT)).replaceAll("\\$next_season\\$", this.main.getSeasonManager().getNextSeason(var1).toString()).replaceAll("\\$days_until_next_season\\$", String.valueOf(this.main.getTimeManager().getDaysUntilNextSeason(var1))));
               }

               if (!((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_TIME)).equals("")) {
                  Bukkit.getLogger().info(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_TIME)).replaceAll("\\$time\\$", this.main.getTimeManager().getTimeAsString(var1)));
               }

               if (!((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_EVENTS)).equals("")) {
                  Bukkit.getLogger().info(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_EVENTS)).replaceAll("\\$events\\$", this.main.getEventManager().getActiveEventsAsString(var1)));
               }

               String var3 = this.main.getEventManager().getNextEventsAsString(var1);
               int var4 = this.main.getEventManager().getDaysUntilNextEvent(var1);
               if (var4 != -1 && !((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_DAYSUNTILNEXTEVENT)).equals("")) {
                  Bukkit.getLogger().info(((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_DAYSUNTILNEXTEVENT)).replaceAll("\\$next_event\\$", var3).replaceAll("\\$days_until_next_event\\$", String.valueOf(var4)));
               }
            }
         }
      } else {
         Bukkit.getLogger().info(JavaUtils.hex((String)LanguageManager.messages.get(MessageType.SEASONSCOMMAND_DISABLED)));
      }

   }

   public void checkChunk(SeasonChunk var1) {
      World var2 = var1.getWorld();
      if (var2 != null) {
         Season var3 = this.getSeason(var2);
         if (var3 == Season.FALL) {
            FallBlockTicker.checkChunk(var1);
         } else if (var3 == Season.SUMMER) {
            SummerBlockTicker.checkChunk(var1);
         } else if (var3 == Season.SPRING) {
            if (this.getSubSeason(var2).getPhase() > 1) {
               SpringBlockTicker.checkChunk(var1);
            }
         } else if (var3 == Season.RESTORE) {
            RestoreWorldTicker.checkChunk(var1);
         }

      }
   }

   public SubSeason getSubSeason(World var1) {
      if (!this.main.getSettings().subSeasonsEnabled) {
         return SubSeason.MIDDLE;
      } else if (this.worldDataSubSeasons.containsKey(var1)) {
         return (SubSeason)this.worldDataSubSeasons.get(var1);
      } else {
         SubSeason var2;
         if (this.main.getSettings().calendarEnabled && this.main.getSettings().subSeasonsEnabled) {
            var2 = this.main.getTimeManager().getCorrectSubSeason(var1);
         } else {
            var2 = SubSeason.MIDDLE;
         }

         this.worldDataSubSeasons.put(var1, var2);
         return var2;
      }
   }

   public void setSubSeason(World var1, SubSeason var2, boolean var3) {
      if (this.getSubSeason(var1) != var2) {
         this.worldDataSubSeasons.put(var1, var2);
         if (var3) {
            new ChunkRefresher(this.main, var1);
         }

         this.main.updatemapplugins((Season)this.worldData.get(var1), var2, var1);
         Season var4 = this.getSeason(var1);
         if (var4 != null && (var4 != Season.DISABLED || var4 != Season.RESTORE)) {
            this.clearChunkCheckedList(var1, var4);
         }
      }

   }

   public Season nextSeason(World var1) {
      if (!this.worldData.containsKey(var1)) {
         return null;
      } else if (this.worldData.get(var1) == null) {
         return null;
      } else {
         Season var2 = this.getSeason(var1);
         if (var2 == Season.SPRING) {
            this.setSeason(var1, Season.SUMMER);
         } else if (var2 == Season.SUMMER) {
            this.setSeason(var1, Season.FALL);
         } else if (var2 == Season.FALL) {
            this.setSeason(var1, Season.WINTER);
         } else if (var2 == Season.WINTER) {
            this.setSeason(var1, Season.SPRING);
         }

         return this.getSeason(var1);
      }
   }

   public boolean setSeason(World var1, Season var2, boolean var3) {
      if (this.getSeason(var1) != var2) {
         SeasonChangeEvent var4 = new SeasonChangeEvent(var1, var2, this.getSeason(var1));
         Bukkit.getPluginManager().callEvent(var4);
         if (var4.isCancelled()) {
            return false;
         } else {
            Season var5 = this.getSeason(var1);
            if (this.getSeason(var1) == null || this.getSeason(var1) == Season.DISABLED) {
               this.worldData.put(var1, var2);
            }

            if (var2 == Season.WINTER) {
               this.setWinter(var1);
               if (this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var1) && this.main.getSettings().affectTime) {
                  this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var1, false);
               }
            } else if (var2 == Season.FALL) {
               this.setFall(var1);
               if (this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var1) && this.main.getSettings().affectTime) {
                  this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var1, false);
               }
            } else if (var2 == Season.SPRING) {
               this.setSpring(var1);
               if (this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var1) && this.main.getSettings().affectTime) {
                  this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var1, false);
               }
            } else if (var2 == Season.SUMMER) {
               this.setSummer(var1);
               if (this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var1) && this.main.getSettings().affectTime) {
                  this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var1, false);
               }
            } else if (var2 == Season.RESTORE) {
               this.setRestoring(var1);
               if (this.main.getSettings().affectTime) {
                  this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var1, true);
               }
            } else if (var2 == Season.DISABLED && this.main.getSettings().affectTime) {
               this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var1, true);
            }

            if (var2 != Season.DISABLED) {
            }

            if (var2 != Season.DISABLED && var2 != Season.RESTORE) {
               this.sendCycleMessage(var1, var2);
            }

            this.refreshers.put(var1, new ChunkRefresher(this.main, var1));
            this.worldData.replace(var1, var2);
            if (var3 && var2 != Season.DISABLED && var2 != Season.RESTORE) {
               Date var6 = this.main.getTimeManager().getDate(var1);
               if (var6 != null) {
                  Date var7 = this.main.getTimeManager().getCalendar().getSeasonStart(var2);
                  Date var8;
                  if (var6.isLaterInYear(var7)) {
                     var8 = new Date(var7.getDay(), var7.getMonth(), var6.getYear());
                  } else {
                     var8 = new Date(var7.getDay(), var7.getMonth(), var6.getYear() + 1);
                  }

                  this.main.getTimeManager().setDate(var1, var8);
               } else {
                  this.main.getTimeManager().setDate(var1, this.main.getTimeManager().getCalendar().getSeasonStart(var2));
               }
            }

            if (this.main.getTemperatureManager().getTempData().isEnabled() && (var2 == Season.FALL || var2 == Season.WINTER || var2 == Season.SPRING || var2 == Season.SUMMER)) {
               if (var5 != Season.DISABLED && var5 != null) {
                  if (this.main.getTemperatureManager().getTempData().isEnabledWorld(var1) && this.main.getTemperatureManager().getTempData().isEnabled()) {
                     this.main.getTemperatureManager().getTempData().setBaseTemperature(var1, this.main.getTemperatureManager().getTempUtils().generateNewBaseTemperature(var1));
                  }
               } else {
                  this.main.getTemperatureManager().loadWorld(var1);
               }
            }

            if (var2 != Season.DISABLED && var2 != Season.RESTORE) {
               SubSeason var9 = this.main.getTimeManager().getCorrectSubSeason(var1);
               this.setSubSeason(var1, var9, false);
               this.main.updatemapplugins(var2, var9, var1);
            }

            this.main.getDataReader().save();
            this.runSubSeasonCheck();
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean setSeason(World var1, Season var2) {
      return this.setSeason(var1, var2, true);
   }

   public Season getSeason(World var1) {
      return this.worldData.get(var1) != null ? (Season)this.worldData.get(var1) : Season.DISABLED;
   }

   public void checkWorlds() {
      if (this.main.getSettings().defaultSeason != Season.DISABLED) {
         Iterator var1 = Bukkit.getWorlds().iterator();

         while(var1.hasNext()) {
            World var2 = (World)var1.next();
            if (var2.getEnvironment() != Environment.THE_END && var2.getEnvironment() != Environment.NETHER && !this.worldData.containsKey(var2)) {
               this.setSeason(var2, this.main.getSettings().defaultSeason);
            }
         }
      }

   }

   public void setSpring(World var1) {
      SpringBlockTicker var2 = new SpringBlockTicker(this.main, var1);
      var2.runTaskTimer(this.main, 10L, (long)(this.main.getSettings().delayPerTick * 2));
   }

   public void setFall(World var1) {
      FallBlockTicker var2 = new FallBlockTicker(this.main, var1);
      var2.runTaskTimer(this.main, 10L, (long)(this.main.getSettings().delayPerTick * 2));
   }

   public void setSummer(World var1) {
      SummerBlockTicker var2 = new SummerBlockTicker(this.main, var1);
      var2.runTaskTimer(this.main, 10L, (long)(this.main.getSettings().delayPerTick * 2));
   }

   public void setWinter(World var1) {
      WinterBlockTicker var2 = new WinterBlockTicker(this.main, var1);
      var2.runTaskTimer(this.main, 10L, (long)(this.main.getSettings().delayPerTick * 2));
      this.main.updatemapplugins(Season.WINTER, SubSeason.MIDDLE, var1);
   }

   public void setRestoring(World var1) {
      RestoreWorldTicker var2 = new RestoreWorldTicker(this.main, var1);
      var2.runTaskTimer(this.main, 10L, (long)this.main.getSettings().delayPerTick);
   }

   public void setup() {
      BiomeUtils.treeFallpackets = ChunkUtils.generateChunkPackets();
      Iterator var1 = Bukkit.getWorlds().iterator();

      while(var1.hasNext()) {
         World var2 = (World)var1.next();
         if (var2.getEnvironment() != Environment.NETHER && var2.getEnvironment() != Environment.THE_END) {
            if (this.getSeason(var2) == Season.WINTER) {
               this.setWinter(var2);
            } else if (this.getSeason(var2) == Season.SPRING) {
               this.setSpring(var2);
            } else if (this.getSeason(var2) == Season.SUMMER) {
               this.setSummer(var2);
            } else if (this.getSeason(var2) == Season.FALL) {
               this.setFall(var2);
            } else if (this.getSeason(var2) == Season.RESTORE) {
               this.setRestoring(var2);
            } else {
               this.setSeason(var2, Season.DISABLED);
            }
         }
      }

      this.checkWorlds();
      if (this.main.getSettings().removeAnimalsOnSeasonChange) {
         this.remover = new AnimalRemover(this.main);
         this.remover.runTaskTimer(this.main, 0L, 20L);
         Bukkit.getScheduler().scheduleSyncRepeatingTask(this.main, new Runnable() {
            public void run() {
               SeasonManager.this.remover.cancel();
               SeasonManager.this.remover = new AnimalRemover(SeasonManager.this.main);
               SeasonManager.this.remover.runTaskTimer(SeasonManager.this.main, 0L, 20L);
            }
         }, 72000L, 72000L);
      }

      this.thandler = new TimeHandler(this.main);
      this.thandler.runTaskTimer(this.main, 0L, 1L);
      Bukkit.getScheduler().scheduleSyncRepeatingTask(this.main, new Runnable() {
         public void run() {
            SeasonManager.this.thandler.cancel();
            SeasonManager.this.thandler = new TimeHandler(SeasonManager.this.main);
            SeasonManager.this.thandler.runTaskTimer(SeasonManager.this.main, 0L, 1L);
         }
      }, 72000L, 72000L);
   }

   public void sendCycleMessage(World var1, Season var2) {
      if (this.main.getSettings().sendMessageOnSeasonCycle) {
         String var3 = "";
         if (var2 == Season.FALL) {
            var3 = (String)LanguageManager.messages.get(MessageType.SEASON_TO_FALL);
         } else if (var2 == Season.WINTER) {
            var3 = (String)LanguageManager.messages.get(MessageType.SEASON_TO_WINTER);
         } else if (var2 == Season.SUMMER) {
            var3 = (String)LanguageManager.messages.get(MessageType.SEASON_TO_SUMMER);
         } else {
            if (var2 != Season.SPRING) {
               return;
            }

            var3 = (String)LanguageManager.messages.get(MessageType.SEASON_TO_SPRING);
         }

         if (var3 != "") {
            Iterator var4 = Bukkit.getOnlinePlayers().iterator();

            while(true) {
               while(true) {
                  Player var5;
                  do {
                     if (!var4.hasNext()) {
                        return;
                     }

                     var5 = (Player)var4.next();
                  } while(var5.getWorld() != var1);

                  if (var3.contains("ngtjrd")) {
                     String[] var6 = var3.split("ngtjrd");
                     String[] var7 = var6;
                     int var8 = var6.length;

                     for(int var9 = 0; var9 < var8; ++var9) {
                        String var10 = var7[var9];
                        var5.sendMessage(JavaUtils.hex(var10));
                     }
                  } else {
                     var5.sendMessage(JavaUtils.hex(var3));
                  }
               }
            }
         }
      }
   }
}
