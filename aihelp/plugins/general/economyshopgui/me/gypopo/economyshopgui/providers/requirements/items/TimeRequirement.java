package me.gypopo.economyshopgui.providers.requirements.items;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.RequirementManager;
import me.gypopo.economyshopgui.providers.requirements.ItemRequirement;
import me.gypopo.economyshopgui.util.exceptions.ItemRequirementLoadException;
import org.bukkit.entity.Player;

public class TimeRequirement implements ItemRequirement {
   private final TimeRequirement.TimeRange range;
   private final RequirementManager.RequirementLore lore;

   public TimeRequirement(String timestamp, List<String> lore) throws ItemRequirementLoadException {
      String[] stamps = timestamp.split("-");

      try {
         this.range = new TimeRequirement.TimeRange(stamps[0], stamps[1]);
      } catch (ArrayIndexOutOfBoundsException var5) {
         throw new ItemRequirementLoadException("Invalid time range for " + timestamp + ", should be formatted as 00:00-00:00");
      }

      this.lore = RequirementManager.getLore(lore, Lang.TIME_REQUIREMENT_LORE.get().toString(), this.range.getMin(), this.range.getMax());
   }

   public boolean isMet(Player p) {
      return this.range.isInRange(p.getWorld().getTime());
   }

   public String[] getLore(Player p, boolean fast, boolean pr) {
      return this.lore.get(p, fast, pr, "%progress%", this.range.format12(p.getWorld().getTime()));
   }

   public void sendNotMetMessage(Player p) {
      SendMessage.chatToPlayer(p, Lang.TIME_REQUIREMENT_NOT_MET.get().replace("%minTime%", this.range.getMin()).replace("%maxTime%", this.range.getMax()).replace("%progress%", this.range.format12(p.getWorld().getTime())));
   }

   private class TimeRange {
      private final int ticksAtMidnight = 18000;
      private final int ticksPerDay = 24000;
      private final int ticksPerHour = 1000;
      private final double ticksPerMinute = 16.666666666666668D;
      private final double ticksPerSecond = 0.2777777777777778D;
      private final SimpleDateFormat SDFTwentyFour;
      private final SimpleDateFormat SDFTwelve;
      private final long min;
      private final long max;

      public TimeRange(String param2, String param3) throws ItemRequirementLoadException {
         this.SDFTwentyFour = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
         this.SDFTwelve = new SimpleDateFormat("h:mm aa", Locale.ENGLISH);
         this.min = this.parse24(min);
         this.max = this.parse24(max);
      }

      public boolean isInRange(long time) {
         if (this.min > this.max) {
            return time >= this.min || time <= this.max;
         } else {
            return time >= this.min && time <= this.max;
         }
      }

      public String getMin() {
         return this.format12(this.min);
      }

      public String getMax() {
         return this.format12(this.max);
      }

      private long parse24(String stamp) throws ItemRequirementLoadException {
         if (stamp.length() == 5 && stamp.contains(":")) {
            stamp = stamp.replace(":", "");
            if (!stamp.matches("^[0-9]{2}[^0-9]?[0-9]{2}$")) {
               throw new ItemRequirementLoadException("Invalud timestamp for " + stamp + ", can only contain numbers");
            } else {
               stamp = stamp.toLowerCase(Locale.ENGLISH).replaceAll("[^0-9]", "");
               int hours = Integer.parseInt(stamp.substring(0, 2));
               if (hours <= 24 && hours >= 0) {
                  int minutes = Integer.parseInt(stamp.substring(2, 4));
                  if (minutes <= 60 && minutes >= 0) {
                     return this.hoursMinutesToTicks(hours, minutes);
                  } else {
                     throw new ItemRequirementLoadException("Invalid amount of minutes for timestamp " + stamp + " should be in range of 0-60");
                  }
               } else {
                  throw new ItemRequirementLoadException("Invalid amount of hours for timestamp " + stamp + " should be in range of 0-24");
               }
            }
         } else {
            throw new ItemRequirementLoadException("Invalid timestamp for " + stamp + ", should be formatted as 00:00");
         }
      }

      private long hoursMinutesToTicks(int hours, int minutes) {
         Objects.requireNonNull(this);
         long ret = 18000L;
         Objects.requireNonNull(this);
         ret += (long)(hours * 1000);
         double var10000 = (double)ret;
         double var10001 = (double)minutes / 60.0D;
         Objects.requireNonNull(this);
         ret = (long)(var10000 + var10001 * 1000.0D);
         Objects.requireNonNull(this);
         ret %= 24000L;
         return ret;
      }

      public String format24(long ticks) {
         synchronized(this.SDFTwentyFour) {
            return this.formatDateFormat(ticks, this.SDFTwentyFour);
         }
      }

      public String format12(long ticks) {
         synchronized(this.SDFTwelve) {
            return this.formatDateFormat(ticks, this.SDFTwelve);
         }
      }

      private String formatDateFormat(long ticks, SimpleDateFormat format) {
         Date date = this.ticksToDate(ticks);
         return format.format(date);
      }

      private Date ticksToDate(long ticks) {
         Objects.requireNonNull(this);
         long var10000 = ticks - 18000L;
         Objects.requireNonNull(this);
         ticks = var10000 + 24000L;
         Objects.requireNonNull(this);
         long days = ticks / 24000L;
         Objects.requireNonNull(this);
         ticks -= days * 24000L;
         Objects.requireNonNull(this);
         long hours = ticks / 1000L;
         Objects.requireNonNull(this);
         ticks -= hours * 1000L;
         double var14 = (double)ticks;
         Objects.requireNonNull(this);
         long minutes = (long)Math.floor(var14 / 16.666666666666668D);
         var14 = (double)ticks;
         double var10001 = (double)minutes;
         Objects.requireNonNull(this);
         double dticks = var14 - var10001 * 16.666666666666668D;
         Objects.requireNonNull(this);
         long seconds = (long)Math.floor(dticks / 0.2777777777777778D);
         Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
         cal.setLenient(true);
         cal.set(0, 0, 1, 0, 0, 0);
         cal.add(6, (int)days);
         cal.add(11, (int)hours);
         cal.add(12, (int)minutes);
         cal.add(13, (int)seconds + 1);
         return cal.getTime();
      }
   }
}
