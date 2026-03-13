package me.casperge.realisticseasons.season;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.WeakHashMap;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.calendar.Date;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.scheduler.BukkitRunnable;

public class SeasonCycle extends BukkitRunnable {
   private RealisticSeasons main;
   private WeakHashMap<World, Long> lastRecordedTime = new WeakHashMap();
   public ZonedDateTime lastRecordedRealLifeTime;

   public SeasonCycle(RealisticSeasons var1) {
      this.main = var1;
   }

   public void run() {
      if (!this.main.getSettings().calendarEnabled) {
         this.cancel();
      }

      ArrayList var1 = new ArrayList();
      Iterator var2 = this.main.getSettings().syncedWorlds.iterator();

      World var4;
      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (Bukkit.getWorld(var3) != null) {
            var4 = Bukkit.getWorld(var3);
            if (this.lastRecordedTime.containsKey(var4)) {
               var1.add(var4);
            }
         }
      }

      if (var1.size() > 1) {
         Long var6 = -10L;
         Iterator var7 = var1.iterator();

         while(var7.hasNext()) {
            var4 = (World)var7.next();
            if (var6 == -10L) {
               var6 = var4.getFullTime();
            } else if (var6 < var4.getFullTime()) {
               var6 = var4.getFullTime();
            }
         }

         var7 = var1.iterator();

         while(var7.hasNext()) {
            var4 = (World)var7.next();
            if (var4.getFullTime() != var6) {
               var4.setFullTime(var6);
            }
         }
      }

      this.handleTimeChanges();
      if (var1.size() > 1) {
         int var8 = -1;
         World var9 = null;
         Iterator var10 = var1.iterator();

         World var5;
         while(var10.hasNext()) {
            var5 = (World)var10.next();
            if (var8 == -1) {
               var8 = this.main.getTimeManager().getCalendar().getTotalDays(this.main.getTimeManager().getDate(var5));
               var9 = var5;
            } else if (this.main.getTimeManager().getCalendar().getTotalDays(this.main.getTimeManager().getDate(var5)) > var8) {
               var8 = this.main.getTimeManager().getCalendar().getTotalDays(this.main.getTimeManager().getDate(var5));
               var9 = var5;
            }
         }

         var10 = var1.iterator();

         while(var10.hasNext()) {
            var5 = (World)var10.next();
            if (this.main.getTimeManager().getCalendar().getTotalDays(this.main.getTimeManager().getDate(var5)) != var8) {
               this.main.getTimeManager().setDate(var5, this.main.getTimeManager().getDate(var9));
            }
         }
      }

   }

   public void handleTimeChanges() {
      Iterator var1 = Bukkit.getWorlds().iterator();

      while(true) {
         while(true) {
            World var2;
            do {
               do {
                  do {
                     do {
                        if (!var1.hasNext()) {
                           return;
                        }

                        var2 = (World)var1.next();
                     } while(var2.getEnvironment() == Environment.NETHER);
                  } while(var2.getEnvironment() == Environment.THE_END);
               } while(this.main.getSeasonManager().getSeason(var2) == Season.DISABLED);
            } while(this.main.getSeasonManager().getSeason(var2) == Season.RESTORE);

            if (!this.lastRecordedTime.containsKey(var2)) {
               this.lastRecordedTime.put(var2, var2.getFullTime());
            } else {
               if (this.main.getSettings().doSeasonCycle && this.main.getSettings().calendarEnabled && this.main.getTimeManager().getCalendar().getSeason(this.main.getTimeManager().getDate(var2)) != this.main.getSeasonManager().getSeason(var2)) {
                  this.main.getSeasonManager().setSeason(var2, this.main.getTimeManager().getCalendar().getSeason(this.main.getTimeManager().getDate(var2)), false);
               }

               if (!this.main.getSettings().syncWorldTimeWithRealWorld) {
                  if (!this.main.getSettings().isCalendarInRealLifeDays) {
                     if ((Long)this.lastRecordedTime.get(var2) % 24000L < 18000L && (var2.getFullTime() % 24000L > 18000L || (Long)this.lastRecordedTime.get(var2) % 24000L > var2.getFullTime() % 24000L)) {
                        this.main.getTimeManager().nextDay(var2);
                     }
                  } else {
                     ZonedDateTime var3 = this.main.getTimeManager().getCurrentZonedDateTime().with(LocalTime.MIDNIGHT);
                     if (this.lastRecordedRealLifeTime == null) {
                        this.lastRecordedRealLifeTime = this.main.getLoadedTime();
                        if (this.lastRecordedRealLifeTime == null) {
                           this.lastRecordedRealLifeTime = this.main.getTimeManager().getCurrentZonedDateTime().with(LocalTime.MIDNIGHT);
                        }
                     }

                     int var4 = (int)ChronoUnit.DAYS.between(this.lastRecordedRealLifeTime.with(LocalTime.MIDNIGHT), var3);
                     if (var4 == 0) {
                        continue;
                     }

                     if (var4 == 1) {
                        this.main.getTimeManager().nextDay(var2);
                     }

                     if (var4 > 1) {
                        Date var5 = this.main.getTimeManager().getDate(var2);
                        Date var6 = this.main.getTimeManager().getCalendar().getNextDate(var5);

                        for(int var7 = 1; var7 < var4; ++var7) {
                           var6 = this.main.getTimeManager().getCalendar().getNextDate(var6);
                        }

                        this.main.getTimeManager().setDate(var2, var6);
                     }

                     this.lastRecordedRealLifeTime = this.main.getTimeManager().getCurrentZonedDateTime().with(LocalTime.MIDNIGHT);
                  }
               }

               this.lastRecordedTime.replace(var2, var2.getFullTime());
            }
         }
      }
   }
}
