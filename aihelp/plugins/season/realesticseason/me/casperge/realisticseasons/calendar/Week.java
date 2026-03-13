package me.casperge.realisticseasons.calendar;

import java.util.ArrayList;
import java.util.List;

public class Week {
   private List<String> weekdays = new ArrayList();

   public Week(List<String> var1) {
      this.weekdays = var1;
   }

   public List<String> getWeekDays() {
      return this.weekdays;
   }

   public String getNextWeekDay(String var1) {
      for(int var2 = 0; var2 < this.weekdays.size(); ++var2) {
         if (((String)this.weekdays.get(var2)).equals(var1)) {
            if (var2 + 1 == this.weekdays.size()) {
               return (String)this.weekdays.get(0);
            }

            return (String)this.weekdays.get(var2 + 1);
         }
      }

      return "ERROR";
   }

   public String getWeekDay(int var1) {
      return (String)this.weekdays.get(var1);
   }

   public int weekDayFromString(String var1) {
      for(int var2 = 0; var2 < this.weekdays.size(); ++var2) {
         if (((String)this.weekdays.get(var2)).equalsIgnoreCase(var1)) {
            return var2;
         }
      }

      return 0;
   }
}
