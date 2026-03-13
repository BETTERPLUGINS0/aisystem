package me.casperge.realisticseasons.runnables;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.WeakHashMap;
import me.casperge.enums.GameRuleType;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.Version;
import me.casperge.realisticseasons.calendar.Date;
import me.casperge.realisticseasons.calendar.DayChangeEvent;
import me.casperge.realisticseasons.calendar.Month;
import me.casperge.realisticseasons.data.Settings;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeHandler extends BukkitRunnable {
   private Random r = new Random();
   private RealisticSeasons main;
   int counter = 0;
   int testCounter = 0;
   private int lastSec;
   private Date lastDate;
   public WeakHashMap<World, Integer> currenttick = new WeakHashMap();
   private List<Player> currentsleeping = new ArrayList();

   public TimeHandler(RealisticSeasons var1) {
      this.main = var1;
      ZonedDateTime var2 = var1.getTimeManager().getCurrentZonedDateTime();
      this.lastSec = var2.getSecond() + var2.getMinute() * 60 + var2.getHour() * 3600;
      this.lastDate = var1.getTimeManager().currentDateFromCalendar();
   }

   public void run() {
      Iterator var1;
      World var2;
      if (!this.main.getSettings().affectTime && this.counter > 40 && this.main.hasTimePauser) {
         this.counter = 0;
         if (Bukkit.getOnlinePlayers().size() == 0) {
            var1 = Bukkit.getWorlds().iterator();

            while(var1.hasNext()) {
               var2 = (World)var1.next();
               if (this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var2)) {
                  this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var2, false);
               }
            }
         } else {
            var1 = Bukkit.getWorlds().iterator();

            while(var1.hasNext()) {
               var2 = (World)var1.next();
               if (!this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var2)) {
                  this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var2, true);
               }
            }
         }
      }

      ++this.counter;
      if (this.counter > 100000) {
         this.counter = 0;
      }

      if (this.main.getSettings().affectTime || this.main.getSettings().affectWeather) {
         var1 = this.main.getSeasonManager().worldData.keySet().iterator();

         while(true) {
            while(true) {
               if (!var1.hasNext()) {
                  return;
               }

               var2 = (World)var1.next();
               if (!this.main.getSettings().affectTime || this.main.getSettings().syncWorldTimeWithRealWorld) {
                  break;
               }

               if (this.main.getSeasonManager().getSeason(var2) != Season.DISABLED && this.main.getSeasonManager().getSeason(var2) != Season.RESTORE && (!this.main.hasTimePauser || Bukkit.getOnlinePlayers().size() != 0)) {
                  if (this.main.getTimeManager().getDate(var2) != null) {
                     if (!this.main.getTimeManager().hasTime(var2)) {
                        continue;
                     }

                     if (!this.currenttick.containsKey(var2)) {
                        this.currenttick.put(var2, 0);
                     }

                     Month var3 = this.main.getTimeManager().getCalendar().getMonth(this.main.getTimeManager().getDate(var2).getMonth());
                     if (var2.getTime() >= 12000L) {
                        if ((Integer)this.currenttick.get(var2) >= var3.getNightArray().size()) {
                           this.currenttick.replace(var2, 0);
                        }

                        var2.setFullTime(var2.getFullTime() + (long)(Integer)var3.getNightArray().get((Integer)this.currenttick.get(var2)));
                        if ((Integer)this.currenttick.get(var2) == var3.getNightArray().size() - 1) {
                           this.currenttick.replace(var2, 0);
                        } else {
                           this.currenttick.replace(var2, (Integer)this.currenttick.get(var2) + 1);
                        }
                     } else {
                        if ((Integer)this.currenttick.get(var2) >= var3.getDayArray().size()) {
                           this.currenttick.replace(var2, 0);
                        }

                        var2.setFullTime(var2.getFullTime() + (long)(Integer)var3.getDayArray().get((Integer)this.currenttick.get(var2)));
                        if ((Integer)this.currenttick.get(var2) == var3.getDayArray().size() - 1) {
                           this.currenttick.replace(var2, 0);
                        } else {
                           this.currenttick.replace(var2, (Integer)this.currenttick.get(var2) + 1);
                        }
                     }
                  }

                  if (var2.getTime() <= 12000L && !var2.hasStorm()) {
                     break;
                  }

                  this.currentsleeping.clear();
                  if (var2.getPlayers().size() <= 0) {
                     break;
                  }

                  Iterator var8 = var2.getPlayers().iterator();

                  while(var8.hasNext()) {
                     Player var4 = (Player)var8.next();
                     if (var4.getSleepTicks() >= 90) {
                        this.currentsleeping.add(var4);
                     }
                  }

                  int var9;
                  if (Version.is_1_17_or_up()) {
                     var9 = (int)(Double.valueOf((double)this.main.getGameRuleGetter().GetIntegerGameRule(GameRuleType.PLAYER_SLEEPING_PERCENTAGE, var2)) / 100.0D * (double)var2.getPlayers().size());
                     if (Double.valueOf((double)this.main.getGameRuleGetter().GetIntegerGameRule(GameRuleType.PLAYER_SLEEPING_PERCENTAGE, var2)) > 100.0D) {
                        var9 = Integer.MAX_VALUE;
                     }
                  } else {
                     var9 = var2.getPlayers().size();
                  }

                  if (var9 <= 0) {
                     var9 = 1;
                  }

                  if (this.currentsleeping.size() >= var9) {
                     var2.setTime(0L);
                     var2.setClearWeatherDuration((int)(Math.random() * 168000.0D + 12000.0D));
                  }
                  break;
               }
            }

            int var10;
            if (this.main.getSettings().syncWorldTimeWithRealWorld) {
               ZonedDateTime var11 = this.main.getTimeManager().getCurrentZonedDateTime();
               var10 = var11.getSecond() + var11.getMinute() * 60 + var11.getHour() * 3600;
               long var5 = (long)((double)var10 / 3.6D + 18000.0D + (double)this.main.getSettings().timeSyncOffset) + (long)(var11.getDayOfYear() * 24000);
               if (var2.getFullTime() != var5 && this.main.getSettings().affectTime) {
                  var2.setFullTime((long)((double)var10 / 3.6D + 18000.0D + (double)this.main.getSettings().timeSyncOffset) + (long)(var11.getDayOfYear() * 24000));
               }

               if (this.lastSec > var10) {
                  DayChangeEvent var7 = new DayChangeEvent(var2, this.lastDate, this.main.getTimeManager().currentDateFromCalendar());
                  Bukkit.getPluginManager().callEvent(var7);
                  this.lastDate = this.main.getTimeManager().currentDateFromCalendar();
               }

               this.lastSec = var10;
            }

            if (this.main.getSettings().affectWeather && this.main.getSettings().weathersettings.containsKey(this.main.getSeasonManager().getSeason(var2))) {
               Settings.WeatherSettings var12 = (Settings.WeatherSettings)this.main.getSettings().weathersettings.get(this.main.getSeasonManager().getSeason(var2));
               if (var12.getDownfallChance() > 0.0D) {
                  var10 = (int)Math.round(1.0D / (var12.getDownfallChance() / 100.0D) * 24000.0D);
                  if (this.r.nextInt(var10) == 0 && var12.hasRain()) {
                     var2.setStorm(true);
                  }
               }
            }
         }
      }
   }
}
