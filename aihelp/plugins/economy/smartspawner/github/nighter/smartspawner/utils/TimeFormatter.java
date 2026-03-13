package github.nighter.smartspawner.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.plugin.java.JavaPlugin;

public class TimeFormatter {
   private final JavaPlugin plugin;
   private final Map<String, Long> timeCache = new ConcurrentHashMap();
   private static final long TICKS_PER_SECOND = 20L;
   private static final long TICKS_PER_MINUTE = 1200L;
   private static final long TICKS_PER_HOUR = 72000L;
   private static final long TICKS_PER_DAY = 1728000L;
   private static final long TICKS_PER_WEEK = 12096000L;
   private static final long TICKS_PER_MONTH = 51840000L;
   private static final long TICKS_PER_YEAR = 630720000L;
   private static final Pattern TIME_PATTERN = Pattern.compile("(?:(\\d+)y)?_?(?:(\\d+)mo)?_?(?:(\\d+)w)?_?(?:(\\d+)d)?_?(?:(\\d+)h)?_?(?:(\\d+)m)?_?(?:(\\d+)s)?", 2);
   private static final Pattern SIMPLE_TIME_PATTERN = Pattern.compile("(\\d+)([smhdwmoy])", 2);
   private static final Map<String, Long> TIME_UNIT_MULTIPLIERS = new HashMap();

   public TimeFormatter(JavaPlugin plugin) {
      this.plugin = plugin;
   }

   public long getTimeFromConfig(String path, String defaultValue) {
      String cacheKey = path + ":" + defaultValue;
      if (this.timeCache.containsKey(cacheKey)) {
         return (Long)this.timeCache.get(cacheKey);
      } else {
         String timeString = this.plugin.getConfig().getString(path, defaultValue);
         long result = this.parseTimeToTicks(timeString, -1L);
         if (result == -1L) {
            this.plugin.getLogger().warning("Failed to parse time value for '" + path + "' (value: '" + timeString + "'). Using 1h as fallback.");
            result = 72000L;
         }

         this.timeCache.put(cacheKey, result);
         return result;
      }
   }

   public long getTimeInTicks(String path, long defaultValue) {
      String cacheKey = path + ":" + defaultValue;
      if (this.timeCache.containsKey(cacheKey)) {
         return (Long)this.timeCache.get(cacheKey);
      } else {
         String timeString = this.plugin.getConfig().getString(path);
         if (timeString == null) {
            this.timeCache.put(cacheKey, defaultValue);
            return defaultValue;
         } else {
            long result = this.parseTimeToTicks(timeString, defaultValue);
            this.timeCache.put(cacheKey, result);
            return result;
         }
      }
   }

   public void clearCache() {
      this.timeCache.clear();
   }

   public long parseTimeToTicks(String timeString, long defaultValue) {
      timeString = timeString.trim();

      try {
         return Long.parseLong(timeString);
      } catch (NumberFormatException var10) {
         Matcher complexMatcher = TIME_PATTERN.matcher(timeString);
         if (complexMatcher.matches()) {
            long ticks = 0L;
            if (complexMatcher.group(1) != null) {
               ticks += Long.parseLong(complexMatcher.group(1)) * 630720000L;
            }

            if (complexMatcher.group(2) != null) {
               ticks += Long.parseLong(complexMatcher.group(2)) * 51840000L;
            }

            if (complexMatcher.group(3) != null) {
               ticks += Long.parseLong(complexMatcher.group(3)) * 12096000L;
            }

            if (complexMatcher.group(4) != null) {
               ticks += Long.parseLong(complexMatcher.group(4)) * 1728000L;
            }

            if (complexMatcher.group(5) != null) {
               ticks += Long.parseLong(complexMatcher.group(5)) * 72000L;
            }

            if (complexMatcher.group(6) != null) {
               ticks += Long.parseLong(complexMatcher.group(6)) * 1200L;
            }

            if (complexMatcher.group(7) != null) {
               ticks += Long.parseLong(complexMatcher.group(7)) * 20L;
            }

            return ticks;
         } else {
            Matcher simpleMatcher = SIMPLE_TIME_PATTERN.matcher(timeString);
            if (simpleMatcher.matches()) {
               long value = Long.parseLong(simpleMatcher.group(1));
               String unit = simpleMatcher.group(2).toLowerCase();
               if (unit.equals("o") && timeString.toLowerCase().endsWith("mo")) {
                  unit = "mo";
               }

               Long multiplier = (Long)TIME_UNIT_MULTIPLIERS.get(unit);
               if (multiplier != null) {
                  return value * multiplier;
               }
            }

            this.plugin.getLogger().warning("Invalid time format for '" + timeString + "', using default value");
            return defaultValue;
         }
      }
   }

   public String formatTicks(long ticks) {
      StringBuilder builder = new StringBuilder();
      long years = ticks / 630720000L;
      if (years > 0L) {
         builder.append(years).append("y_");
         ticks %= 630720000L;
      }

      long months = ticks / 51840000L;
      if (months > 0L) {
         builder.append(months).append("mo_");
         ticks %= 51840000L;
      }

      long weeks = ticks / 12096000L;
      if (weeks > 0L) {
         builder.append(weeks).append("w_");
         ticks %= 12096000L;
      }

      long days = ticks / 1728000L;
      if (days > 0L) {
         builder.append(days).append("d_");
         ticks %= 1728000L;
      }

      long hours = ticks / 72000L;
      if (hours > 0L) {
         builder.append(hours).append("h_");
         ticks %= 72000L;
      }

      long minutes = ticks / 1200L;
      if (minutes > 0L) {
         builder.append(minutes).append("m_");
         ticks %= 1200L;
      }

      long seconds = ticks / 20L;
      if (seconds <= 0L && builder.length() != 0) {
         if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
         }
      } else {
         builder.append(seconds).append("s");
      }

      return builder.toString();
   }

   static {
      TIME_UNIT_MULTIPLIERS.put("s", 20L);
      TIME_UNIT_MULTIPLIERS.put("m", 1200L);
      TIME_UNIT_MULTIPLIERS.put("h", 72000L);
      TIME_UNIT_MULTIPLIERS.put("d", 1728000L);
      TIME_UNIT_MULTIPLIERS.put("w", 12096000L);
      TIME_UNIT_MULTIPLIERS.put("mo", 51840000L);
      TIME_UNIT_MULTIPLIERS.put("y", 630720000L);
   }
}
