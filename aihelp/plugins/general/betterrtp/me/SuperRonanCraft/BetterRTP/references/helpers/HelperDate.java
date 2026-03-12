package me.SuperRonanCraft.BetterRTP.references.helpers;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.references.settings.Settings;

public class HelperDate {
   public static Date getDate() {
      return Calendar.getInstance().getTime();
   }

   public static String left(Long amount) {
      Date current_date = getDate();
      return fromTo(current_date.getTime(), amount);
   }

   public static String total(Long amount) {
      return fromTo(0L, amount);
   }

   public static String fromTo(Long from, Long to) {
      Settings settings = BetterRTP.getInstance().getSettings();
      long min = Math.min(from, to);
      long max = Math.max(from, to);
      if (max == min) {
         return settings.getPlaceholder_timeZero();
      } else if (max >= 0L && min >= 0L) {
         long diffInMillies = max - min;
         long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
         diffInMillies -= 86400000L * days;
         long hours = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
         diffInMillies -= 3600000L * hours;
         long minutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
         diffInMillies -= 60000L * minutes;
         long seconds = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
         LinkedList<String> lst = new LinkedList();
         if (days > 0L) {
            lst.add(settings.getPlaceholder_timeDays().replace("{0}", String.valueOf(days)));
         }

         if (hours > 0L) {
            lst.add(settings.getPlaceholder_timeHours().replace("{0}", String.valueOf(hours)));
         }

         if (minutes > 0L) {
            lst.add(settings.getPlaceholder_timeMinutes().replace("{0}", String.valueOf(minutes)));
         }

         if (seconds > 0L) {
            lst.add(settings.getPlaceholder_timeSeconds().replace("{0}", String.valueOf(seconds)));
         }

         StringBuilder time_str = new StringBuilder();

         for(int i = 0; i < lst.size(); ++i) {
            String str = (String)lst.get(i);
            if (lst.size() - i - 1 >= 2) {
               str = str + settings.getPlaceholder_timeSeparator_middle();
            } else if (lst.size() - 1 - i == 1) {
               str = str + settings.getPlaceholder_timeSeparator_last();
            }

            time_str.append(str);
         }

         return time_str.toString();
      } else {
         return settings.getPlaceholder_timeInf();
      }
   }
}
