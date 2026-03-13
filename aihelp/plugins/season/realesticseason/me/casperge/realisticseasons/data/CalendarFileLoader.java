package me.casperge.realisticseasons.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.calendar.Calendar;
import me.casperge.realisticseasons.calendar.Date;
import me.casperge.realisticseasons.calendar.Month;
import me.casperge.realisticseasons.calendar.Week;
import me.casperge.realisticseasons.calendar.Year;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CalendarFileLoader {
   private RealisticSeasons main;
   private FileConfiguration calendar;
   private File calendarFile;
   private Year year;
   private int[] monthLengths = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

   public CalendarFileLoader(RealisticSeasons var1) {
      this.main = var1;
      this.load();
   }

   public Calendar load() {
      this.calendarFile = new File(this.main.getDataFolder(), "calendar.yml");
      if (!this.calendarFile.exists()) {
         try {
            InputStream var1 = this.main.getResource("calendar.yml");
            FileUtils.copyInputStreamToFile(var1, this.calendarFile);
         } catch (IOException var17) {
            var17.printStackTrace();
         }
      }

      this.calendar = YamlConfiguration.loadConfiguration(this.calendarFile);
      if (!this.calendar.contains("time-sync-offset")) {
         this.calendar.set("time-sync-offset", 0);

         try {
            this.calendar.save(this.calendarFile);
         } catch (IOException var16) {
            var16.printStackTrace();
         }
      }

      boolean var18 = false;
      if (this.calendar.contains("sync-time-with-real-world")) {
         this.main.getSettings().syncWorldTimeWithRealWorld = this.calendar.getBoolean("sync-time-with-real-world");
      } else {
         this.calendar.set("sync-time-with-real-world", false);
         this.main.getSettings().syncWorldTimeWithRealWorld = false;
         var18 = true;
      }

      if (this.calendar.contains("calendar-is-real-life-days")) {
         this.main.getSettings().isCalendarInRealLifeDays = this.calendar.getBoolean("calendar-is-real-life-days");
      } else {
         this.calendar.set("calendar-is-real-life-days", false);
         this.main.getSettings().isCalendarInRealLifeDays = false;
         var18 = true;
      }

      if (this.calendar.contains("time-sync-timezone")) {
         this.main.getSettings().timezone = this.calendar.getString("time-sync-timezone");
      } else {
         this.calendar.set("time-sync-timezone", "system");
         var18 = true;
      }

      List var2 = this.calendar.getStringList("week-days");
      Week var3 = new Week(var2);
      boolean var4 = this.main.getSettings().syncWorldTimeWithRealWorld;
      ArrayList var5 = new ArrayList();
      int var6 = 0;

      for(Iterator var7 = this.calendar.getConfigurationSection("months").getKeys(false).iterator(); var7.hasNext(); ++var6) {
         String var8 = (String)var7.next();
         int var10;
         if (var4) {
            var10 = this.monthLengths[var6];
         } else {
            var10 = this.calendar.getInt("months." + var8 + ".days");
         }

         int var11 = this.calendar.getInt("months." + var8 + ".day-length-in-min");
         int var12 = this.calendar.getInt("months." + var8 + ".night-length-in-min");
         var5.add(new Month(var8, var10, var11, var12));
      }

      this.year = new Year(var5);
      this.main.getSettings().timeSyncOffset = this.calendar.getInt("time-sync-offset");
      Date var19 = this.dateFromString(this.calendar.getString("winter-start"));
      Date var20 = this.dateFromString(this.calendar.getString("spring-start"));
      Date var9 = this.dateFromString(this.calendar.getString("summer-start"));
      Date var21 = this.dateFromString(this.calendar.getString("fall-start"));
      if (var19 == null) {
         Bukkit.getLogger().severe("[RealisticSeasons] Could not parse date for winter start: " + this.calendar.getString("winter-start") + "(" + JavaUtils.getDayOfStringDate(this.calendar.getString("winter-start")) + ", " + this.getMonthFromString(this.calendar.getString("winter-start")) + ")");
      }

      if (var20 == null) {
         Bukkit.getLogger().severe("[RealisticSeasons] Could not parse date for spring start: " + this.calendar.getString("spring-start") + "(" + JavaUtils.getDayOfStringDate(this.calendar.getString("spring-start")) + ", " + this.getMonthFromString(this.calendar.getString("spring-start")) + ")");
      }

      if (var9 == null) {
         Bukkit.getLogger().severe("[RealisticSeasons] Could not parse date for summer start: " + this.calendar.getString("summer-start") + "(" + JavaUtils.getDayOfStringDate(this.calendar.getString("summer-start")) + ", " + this.getMonthFromString(this.calendar.getString("summer-start")) + ")");
      }

      if (var21 == null) {
         Bukkit.getLogger().severe("[RealisticSeasons] Could not parse date for fall start: " + this.calendar.getString("fall-start") + "(" + JavaUtils.getDayOfStringDate(this.calendar.getString("fall-start")) + ", " + this.getMonthFromString(this.calendar.getString("fall-start")) + ")");
      }

      this.main.getSettings().calendarEnabled = this.calendar.getBoolean("enabled");
      this.main.getSettings().americandateformat = this.calendar.getBoolean("american-date-format");
      ArrayList var22;
      if (!this.calendar.contains("worlds-without-events")) {
         var18 = true;
         var22 = new ArrayList();
         var22.add("none");
         this.calendar.set("worlds-without-events", var22);
      }

      if (!this.calendar.contains("synced-worlds")) {
         var18 = true;
         var22 = new ArrayList();
         var22.add("none");
         this.calendar.set("synced-worlds", var22);
      }

      var22 = new ArrayList();
      Iterator var23 = this.calendar.getStringList("synced-worlds").iterator();

      while(var23.hasNext()) {
         String var13 = (String)var23.next();
         if (!var13.equalsIgnoreCase("none")) {
            var22.add(var13);
         }
      }

      ArrayList var24 = new ArrayList();
      Iterator var25 = this.calendar.getStringList("worlds-without-events").iterator();

      while(var25.hasNext()) {
         String var14 = (String)var25.next();
         if (!var14.equalsIgnoreCase("none")) {
            var24.add(var14);
         }
      }

      this.main.getSettings().worldsWithoutEvents = var24;
      this.main.getSettings().syncedWorlds = var22;
      if (this.calendar.contains("time")) {
         this.main.getSettings().timeFormat = this.calendar.getString("time.format");
         this.main.getSettings().AmPm = this.calendar.getBoolean("time.12-hour-clock.add-am-pm");
         this.main.getSettings().is12hourClock = this.calendar.getBoolean("time.12-hour-clock.enabled");
      } else {
         this.calendar.set("time.format", "$hours$:$minutes$");
         this.calendar.set("time.12-hour-clock.enabled", false);
         this.calendar.set("time.12-hour-clock.add-am-pm", false);
         this.main.getSettings().timeFormat = "$hours$:$minutes$";
         this.main.getSettings().AmPm = false;
         this.main.getSettings().is12hourClock = false;
         var18 = true;
      }

      if (var18) {
         try {
            this.calendar.save(this.calendarFile);
         } catch (IOException var15) {
            var15.printStackTrace();
         }
      }

      return new Calendar(var3, this.year, var19, var20, var9, var21);
   }

   public int getMonthIndex(List<Month> var1, Month var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         if (((Month)var1.get(var3)).getName().equals(var2.getName())) {
            return var3 + 1;
         }
      }

      return 1;
   }

   public int getMonthFromString(String var1) {
      return this.getMonthIndex(this.year.getMonths(), this.year.getMonth(JavaUtils.getMonthOfStringDate(var1)));
   }

   public Date dateFromString(String var1) {
      try {
         return new Date(Integer.valueOf(JavaUtils.getDayOfStringDate(var1)), this.getMonthFromString(var1));
      } catch (NullPointerException var3) {
         Bukkit.getLogger().severe("Could not find month in: " + var1 + " as season start");
         return null;
      }
   }
}
