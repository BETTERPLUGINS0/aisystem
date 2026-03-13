package me.casperge.realisticseasons.seasonevent;

import java.util.ArrayList;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.calendar.Date;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class CustomDatedEvent implements SeasonCustomEvent {
   private String name;
   private boolean doDisplay;
   private int startDay;
   private int startMonth;
   private int startYear;
   private int endDay;
   private int endMonth;
   private int endYear;
   private Date startDate;
   private Date endDate;
   private List<String> startCommands = new ArrayList();
   private List<String> stopCommands = new ArrayList();
   private List<String> disabledWorlds;

   public CustomDatedEvent(ConfigurationSection var1) {
      if (var1.getBoolean("enabled")) {
         this.startCommands = var1.getStringList("commands.start");
         this.stopCommands = var1.getStringList("commands.stop");
         this.doDisplay = var1.getBoolean("display-event");
      } else {
         this.doDisplay = false;
      }

      if (var1.contains("disabled-worlds")) {
         this.disabledWorlds = var1.getStringList("disabled-worlds");
      } else {
         this.disabledWorlds = null;
      }

      this.name = var1.getString("name");
      String var2 = var1.getString("times.event-start.date");
      if (!var2.contains("/")) {
         this.startDay = Integer.valueOf(var2);
         this.startMonth = -1;
         this.startYear = -1;
      } else {
         boolean var3 = RealisticSeasons.getInstance().getSettings().americandateformat;
         String[] var4 = var2.split("/");
         this.startDay = Integer.valueOf(var4[var3 ? 1 : 0]);
         this.startMonth = Integer.valueOf(var4[var3 ? 0 : 1]);
         if (var4.length == 2) {
            this.startYear = -1;
            this.startDate = new Date(this.startDay, this.startMonth);
            if (RealisticSeasons.getInstance().getTimeManager().getCalendar().getMonth(this.startMonth).getLength() < this.startDay) {
               this.startDate = new Date(RealisticSeasons.getInstance().getTimeManager().getCalendar().getMonth(this.startMonth).getLength(), this.startMonth);
            }
         } else if (var4.length == 3) {
            this.startYear = Integer.valueOf(var4[2]);
            this.startDate = new Date(this.startDay, this.startMonth, this.startYear);
            if (RealisticSeasons.getInstance().getTimeManager().getCalendar().getMonth(this.startMonth).getLength() < this.startDay) {
               this.startDate = new Date(RealisticSeasons.getInstance().getTimeManager().getCalendar().getMonth(this.startMonth).getLength(), this.startMonth, this.startYear);
            }
         } else {
            Bukkit.getLogger().severe("Invalid date input in custom-events.yml: " + var2);
         }
      }

      String var6 = var1.getString("times.event-stop.date");
      if (!var6.contains("/")) {
         this.endDay = Integer.valueOf(var6);
         this.endMonth = -1;
         this.endYear = -1;
      } else {
         boolean var7 = RealisticSeasons.getInstance().getSettings().americandateformat;
         String[] var5 = var6.split("/");
         this.endDay = Integer.valueOf(var5[var7 ? 1 : 0]);
         this.endMonth = Integer.valueOf(var5[var7 ? 0 : 1]);
         if (var5.length == 2) {
            this.endDate = new Date(this.endDay, this.endMonth);
            if (RealisticSeasons.getInstance().getTimeManager().getCalendar().getMonth(this.endMonth).getLength() < this.endDay) {
               this.endDate = new Date(RealisticSeasons.getInstance().getTimeManager().getCalendar().getMonth(this.endMonth).getLength(), this.endMonth);
            }

            this.endYear = -1;
         } else if (var5.length == 3) {
            this.endYear = Integer.valueOf(var5[2]);
            this.endDate = new Date(this.endDay, this.endMonth, this.endYear);
            if (RealisticSeasons.getInstance().getTimeManager().getCalendar().getMonth(this.endMonth).getLength() < this.endDay) {
               this.endDate = new Date(RealisticSeasons.getInstance().getTimeManager().getCalendar().getMonth(this.endMonth).getLength(), this.endMonth, this.endYear);
            }
         } else {
            Bukkit.getLogger().severe("Invalid date input in custom-events.yml: " + var6);
         }
      }

   }

   public List<String> getCommands(boolean var1) {
      return var1 ? this.startCommands : this.stopCommands;
   }

   public boolean hasEvent(World var1) {
      if (this.disabledWorlds == null) {
         return true;
      } else {
         return !this.disabledWorlds.contains(var1.toString());
      }
   }

   public String getName() {
      return this.name;
   }

   public boolean doDisplay() {
      return this.doDisplay;
   }

   public Date getStartDate() {
      return this.startDate;
   }

   public Date getEndDate() {
      return this.endDate;
   }

   public boolean isActive(Date var1) {
      if (this.startMonth == -1 && this.startYear == -1) {
         return var1.getDay() >= this.startDay && var1.getDay() <= this.endDay;
      } else if (this.startYear == -1) {
         return RealisticSeasons.getInstance().getTimeManager().getCalendar().getDaysUntil(this.startDate, this.endDate) >= RealisticSeasons.getInstance().getTimeManager().getCalendar().getDaysUntil(var1, this.endDate);
      } else {
         if (this.startYear == this.endYear && var1.getYear() == this.startYear || var1.getYear() >= this.startYear && var1.getYear() <= this.endYear) {
            if (this.startYear != this.endYear || var1.getYear() != this.startYear) {
               if (var1.getYear() == this.startYear) {
                  if (!var1.isLaterInYear(this.startDate)) {
                     return false;
                  }
               } else if (var1.getYear() == this.endYear && var1.isLaterInYear(this.endDate)) {
                  return false;
               }
            }

            if (RealisticSeasons.getInstance().getTimeManager().getCalendar().getDaysUntil(this.startDate, this.endDate) >= RealisticSeasons.getInstance().getTimeManager().getCalendar().getDaysUntil(var1, this.endDate)) {
               return true;
            }
         }

         return false;
      }
   }
}
