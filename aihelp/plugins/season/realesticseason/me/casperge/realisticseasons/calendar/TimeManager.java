package me.casperge.realisticseasons.calendar;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.data.CalendarFileLoader;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.season.SubSeason;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class TimeManager {
   private Calendar calendar;
   private RealisticSeasons main;
   private WeakHashMap<World, Date> dates = new WeakHashMap();
   private List<World> disabledWorlds = new ArrayList();
   private boolean useSystemTime = true;

   public TimeManager(RealisticSeasons var1) {
      this.main = var1;
      this.load();
      new DayChangeHandler(var1);
   }

   public ZonedDateTime getCurrentZonedDateTime() {
      if (this.useSystemTime) {
         LocalDateTime var3 = LocalDateTime.now();
         ZonedDateTime var2 = var3.atZone(ZoneId.systemDefault());
         return var2;
      } else {
         ZonedDateTime var1 = ZonedDateTime.now().withZoneSameInstant(ZoneId.of(this.main.getSettings().timezone));
         return var1;
      }
   }

   public void load() {
      this.calendar = (new CalendarFileLoader(this.main)).load();
      if (!this.main.getSettings().timezone.equalsIgnoreCase("system")) {
         this.useSystemTime = false;
      }

   }

   public void addToDatesRegister(HashMap<World, Date> var1) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         World var3 = (World)var2.next();
         this.dates.put(var3, (Date)var1.get(var3));
      }

   }

   public void nextDay(World var1) {
      Date var2 = (Date)this.dates.get(var1);
      Date var3 = this.calendar.getNextDate(var2);
      DayChangeEvent var4 = new DayChangeEvent(var1, var2, var3);
      Bukkit.getPluginManager().callEvent(var4);
      this.dates.replace(var1, var3);
   }

   public void setDate(World var1, Date var2) {
      if (!this.dates.containsKey(var1)) {
         this.dates.put(var1, var2);
      } else {
         Date var3 = (Date)this.dates.get(var1);
         DayChangeEvent var4 = new DayChangeEvent(var1, var3, var2);
         Bukkit.getPluginManager().callEvent(var4);
         this.dates.replace(var1, var2);
      }
   }

   public Calendar getCalendar() {
      return this.calendar;
   }

   public WeakHashMap<World, Date> getDates() {
      return this.dates;
   }

   public boolean hasTime(World var1) {
      return !this.disabledWorlds.contains(var1);
   }

   public void resumeTime(World var1) {
      if (!this.hasTime(var1)) {
         this.disabledWorlds.remove(var1);
      }

      ArrayList var2 = new ArrayList();
      Iterator var3 = this.disabledWorlds.iterator();

      while(var3.hasNext()) {
         World var4 = (World)var3.next();
         if (!Bukkit.getWorlds().contains(var4)) {
            var2.add(var4);
         }
      }

      this.disabledWorlds.removeAll(var2);
   }

   public void pauseTime(World var1) {
      if (this.hasTime(var1)) {
         this.disabledWorlds.add(var1);
      }

      ArrayList var2 = new ArrayList();
      Iterator var3 = this.disabledWorlds.iterator();

      while(var3.hasNext()) {
         World var4 = (World)var3.next();
         if (!Bukkit.getWorlds().contains(var4)) {
            var2.add(var4);
         }
      }

      this.disabledWorlds.removeAll(var2);
   }

   public List<World> getPausedWorlds() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.disabledWorlds.iterator();

      while(var2.hasNext()) {
         World var3 = (World)var2.next();
         if (!Bukkit.getWorlds().contains(var3)) {
            var1.add(var3);
         }
      }

      this.disabledWorlds.removeAll(var1);
      return this.disabledWorlds;
   }

   public String getWeekDay(Date var1) {
      if (this.main.getSettings().syncWorldTimeWithRealWorld) {
         int var2 = this.getCurrentZonedDateTime().getDayOfWeek().getValue() - 1;
         if (var2 == -1) {
            var2 = 6;
         }

         return this.calendar.getWeekDay(var2);
      } else {
         return this.calendar.getWeekDay(var1);
      }
   }

   public int getWeekDayAsInt(Date var1, boolean var2) {
      if (this.main.getSettings().syncWorldTimeWithRealWorld) {
         int var3 = this.getCurrentZonedDateTime().getDayOfWeek().getValue() - 1;
         if (var3 == -1) {
            var3 = 6;
         }

         if (var2) {
            --var3;
            if (var3 == -1) {
               var3 = 6;
            }
         }

         return var3;
      } else {
         return this.calendar.getWeekDayAsInt(var1);
      }
   }

   public Date getDate(World var1) {
      return this.main.getSettings().syncWorldTimeWithRealWorld ? this.currentDateFromCalendar() : (Date)this.dates.get(var1);
   }

   public Date currentDateFromCalendar() {
      ZonedDateTime var1 = this.getCurrentZonedDateTime();
      return new Date(var1.getDayOfMonth(), var1.getMonthValue(), var1.getYear());
   }

   public int getDaysUntilNextSeason(World var1) {
      return this.main.getTimeManager().getCalendar().getDaysUntil(this.main.getTimeManager().getDate(var1), this.main.getTimeManager().getCalendar().getSeasonStart(this.main.getSeasonManager().getSeason(var1).getNextSeason()));
   }

   public int getTotalDays(Season var1) {
      return this.main.getTimeManager().getCalendar().getDaysUntil(this.main.getTimeManager().getCalendar().getSeasonStart(var1), this.main.getTimeManager().getCalendar().getSeasonStart(var1.getNextSeason()));
   }

   public int getSeconds(World var1) {
      if (this.main.getSettings().syncWorldTimeWithRealWorld) {
         return this.getCurrentZonedDateTime().getSecond();
      } else if (this.main.getSettings().affectTime && this.main.getSeasonManager().getSeason(var1) != Season.DISABLED && this.main.getSeasonManager().getSeason(var1) != Season.RESTORE) {
         Month var2 = this.calendar.getMonth(this.getDate(var1).getMonth());
         return Time.getSeconds(var1, var2.getDayLengthMultiplier(), var2.getNightLengthMultiplier());
      } else {
         return Time.getSeconds(var1, 1.0D, 1.0D);
      }
   }

   public int getHours(World var1) {
      if (this.main.getSettings().syncWorldTimeWithRealWorld) {
         return this.getCurrentZonedDateTime().getHour();
      } else if (this.main.getSettings().affectTime && this.main.getSeasonManager().getSeason(var1) != Season.DISABLED && this.main.getSeasonManager().getSeason(var1) != Season.RESTORE) {
         Month var2 = this.calendar.getMonth(this.getDate(var1).getMonth());
         return Time.getHours(var1, var2.getDayLengthMultiplier(), var2.getNightLengthMultiplier());
      } else {
         return Time.getHours(var1, 1.0D, 1.0D);
      }
   }

   public int getMinutes(World var1) {
      if (this.main.getSettings().syncWorldTimeWithRealWorld) {
         return this.getCurrentZonedDateTime().getMinute();
      } else if (this.main.getSettings().affectTime && this.main.getSeasonManager().getSeason(var1) != Season.DISABLED && this.main.getSeasonManager().getSeason(var1) != Season.RESTORE) {
         Month var2 = this.calendar.getMonth(this.getDate(var1).getMonth());
         return Time.getMinutes(var1, var2.getDayLengthMultiplier(), var2.getNightLengthMultiplier());
      } else {
         return Time.getMinutes(var1, 1.0D, 1.0D);
      }
   }

   public String getTimeAsString(World var1) {
      String var2 = this.main.getSettings().timeFormat;
      String var3 = this.main.getTimeManager().getHours(var1) <= 11 ? "AM" : "PM";
      String var4 = String.valueOf(this.main.getTimeManager().getSeconds(var1));
      String var5 = String.valueOf(this.main.getTimeManager().getMinutes(var1));
      if (var4.length() == 1) {
         var4 = "0" + var4;
      }

      if (var5.length() == 1) {
         var5 = "0" + var5;
      }

      var2 = var2.replaceAll("\\$seconds\\$", var4);
      var2 = var2.replaceAll("\\$minutes\\$", var5);
      String var6;
      if (this.main.getSettings().is12hourClock) {
         var6 = String.valueOf((this.main.getTimeManager().getHours(var1) + 11) % 12 + 1);
         if (var6.length() == 1) {
            var6 = "0" + var6;
         }

         var2 = var2.replaceAll("\\$hours\\$", var6);
         if (this.main.getSettings().AmPm) {
            var2 = var2 + " " + var3;
         }
      } else {
         var6 = String.valueOf(this.main.getTimeManager().getHours(var1));
         if (var6.length() == 1) {
            var6 = "0" + var6;
         }

         var2 = var2.replaceAll("\\$hours\\$", var6);
      }

      return var2;
   }

   public SubSeason getCorrectSubSeason(World var1) {
      Date var2 = this.getDate(var1);
      if (var2 == null) {
         return SubSeason.MIDDLE;
      } else {
         Long var3 = var1.getFullTime() % 24000L;
         double var4 = this.getSeasonProgressPercentage(var2, var3);
         if (var4 < 9.0D) {
            return SubSeason.START;
         } else if (var4 >= 9.0D && var4 < 18.0D) {
            return SubSeason.EARLY;
         } else if (var4 >= 18.0D && var4 < 84.0D) {
            return SubSeason.MIDDLE;
         } else if (var4 >= 84.0D && var4 < 92.0D) {
            return SubSeason.LATE;
         } else {
            return var4 >= 92.0D ? SubSeason.END : SubSeason.MIDDLE;
         }
      }
   }

   public double getSeasonProgressPercentage(Date var1, Long var2) {
      Season var3 = this.calendar.getSeason(var1);
      int var4 = this.getTotalDays(var3);
      int var5 = this.calendar.getDaysUntil(var1, this.main.getTimeManager().getCalendar().getSeasonStart(var3.getNextSeason()));
      int var6 = var4 - var5;
      Long var7 = (long)var4 * 24000L;
      Long var8 = (long)var6 * 24000L + var2;
      return (double)var8 / (double)var7 * 100.0D;
   }

   public Date getHalfwaySeason(Season var1) {
      Date var2 = this.getCalendar().getSeasonStart(var1);
      int var3 = this.getTotalDays(var1);
      int var4 = var3 / 2;
      Date var5 = null;

      for(int var6 = 0; var6 < var4; ++var6) {
         if (var6 == 0) {
            var5 = this.getCalendar().getNextDate(var2);
         } else {
            var5 = this.getCalendar().getNextDate(var5);
         }
      }

      return var5;
   }
}
