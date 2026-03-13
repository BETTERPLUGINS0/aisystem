package me.casperge.realisticseasons.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import me.casperge.realisticseasons.season.Season;

public class Calendar {
   private Week week;
   private Year year;
   private Date winterstart;
   private Date springstart;
   private Date summerstart;
   private Date fallstart;
   HashMap<Date, Season> seasonStart = new HashMap();

   public Calendar(Week var1, Year var2, Date var3, Date var4, Date var5, Date var6) {
      this.week = var1;
      this.year = var2;
      this.winterstart = var3;
      this.springstart = var4;
      this.summerstart = var5;
      this.fallstart = var6;
      this.seasonStart.put(var3, Season.WINTER);
      this.seasonStart.put(var6, Season.FALL);
      this.seasonStart.put(var5, Season.SUMMER);
      this.seasonStart.put(var4, Season.SPRING);
   }

   public String getWeekDay(int var1) {
      return this.week.getWeekDay(var1);
   }

   public Month getMonth(int var1) {
      return this.year.getMonth(var1);
   }

   public Date getSeasonStart(Season var1) {
      switch(var1) {
      case WINTER:
         return this.winterstart;
      case SUMMER:
         return this.summerstart;
      case FALL:
         return this.fallstart;
      case SPRING:
         return this.springstart;
      default:
         return null;
      }
   }

   public Season getSeason(Date var1) {
      HashMap var2 = new HashMap();
      ArrayList var3 = new ArrayList();
      var3.add(this.summerstart);
      var3.add(this.winterstart);
      var3.add(this.fallstart);
      var3.add(this.springstart);
      Iterator var4 = var3.iterator();

      Date var5;
      while(var4.hasNext()) {
         var5 = (Date)var4.next();
         int var6 = 0;
         Iterator var7 = var3.iterator();

         while(var7.hasNext()) {
            Date var8 = (Date)var7.next();
            if (var5 != var8 && !var5.isLaterInYear(var8)) {
               ++var6;
            }
         }

         var2.put(var5, var6);
      }

      var4 = var2.keySet().iterator();

      while(var4.hasNext()) {
         var5 = (Date)var4.next();
         var3.set((Integer)var2.get(var5), var5);
      }

      if (((Date)var3.get(0)).isLaterInYear(var1) && !((Date)var3.get(1)).isLaterInYear(var1)) {
         return (Season)this.seasonStart.get(var3.get(0));
      } else if (((Date)var3.get(1)).isLaterInYear(var1) && !((Date)var3.get(2)).isLaterInYear(var1)) {
         return (Season)this.seasonStart.get(var3.get(1));
      } else if (((Date)var3.get(2)).isLaterInYear(var1) && !((Date)var3.get(3)).isLaterInYear(var1)) {
         return (Season)this.seasonStart.get(var3.get(2));
      } else {
         return (Season)this.seasonStart.get(var3.get(3));
      }
   }

   public Date getNextDate(Date var1) {
      int var2 = var1.getDay();
      int var3 = var1.getMonth();
      int var4 = var1.getYear();
      Month var5 = this.getMonth(var3);
      if (var2 >= var5.getLength()) {
         return var3 >= this.year.getMonths().size() ? new Date(1, 1, var4 + 1) : new Date(1, var3 + 1, var4);
      } else {
         return new Date(var2 + 1, var3, var4);
      }
   }

   public int getTotalDays(Date var1) {
      int var2 = var1.getYear();
      int var3 = 0;
      boolean var4 = false;

      Iterator var5;
      Month var6;
      for(var5 = this.year.getMonths().iterator(); var5.hasNext(); var3 += var6.getLength()) {
         var6 = (Month)var5.next();
      }

      int var7 = var3 * var2;

      for(var5 = this.year.getMonths().iterator(); var5.hasNext(); var7 += var6.getLength()) {
         var6 = (Month)var5.next();
         if (var6.getName().equals(this.getMonth(var1.getMonth()).getName())) {
            break;
         }
      }

      return var7 + var1.getDay();
   }

   public int getTotalDaysPassedThisYear(Date var1) {
      int var2 = 0;

      Month var4;
      for(Iterator var3 = this.year.getMonths().iterator(); var3.hasNext(); var2 += var4.getLength()) {
         var4 = (Month)var3.next();
         if (var4.getName().equals(this.getMonth(var1.getMonth()).getName())) {
            break;
         }
      }

      return var2 + var1.getDay();
   }

   public String getWeekDay(Date var1) {
      int var2 = this.getTotalDays(var1);
      int var3 = var2 % this.week.getWeekDays().size();
      return this.getWeekDay(var3);
   }

   public int getWeekDayAsInt(Date var1) {
      int var2 = this.getTotalDays(var1);
      int var3 = var2 % this.week.getWeekDays().size();
      return var3;
   }

   public int getDaysUntil(Date var1, Date var2) {
      int var3 = this.getTotalDaysPassedThisYear(var2);
      int var4 = this.getTotalDaysPassedThisYear(var1);
      if (var4 <= var3) {
         return var3 - var4;
      } else {
         int var5 = 0;

         Month var7;
         for(Iterator var6 = this.year.getMonths().iterator(); var6.hasNext(); var5 += var7.getLength()) {
            var7 = (Month)var6.next();
         }

         return var3 + (var5 - var4);
      }
   }

   public int getTotalMonths() {
      return this.year.getMonths().size();
   }

   public int weekDayFromString(String var1) {
      return this.week.weekDayFromString(var1);
   }
}
