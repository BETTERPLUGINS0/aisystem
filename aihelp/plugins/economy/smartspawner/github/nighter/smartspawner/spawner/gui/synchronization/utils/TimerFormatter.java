package github.nighter.smartspawner.spawner.gui.synchronization.utils;

import java.util.concurrent.ConcurrentHashMap;

public final class TimerFormatter {
   private static final ConcurrentHashMap<Long, String> TIMER_CACHE = new ConcurrentHashMap(128);
   private static final ThreadLocal<char[]> CHAR_BUFFER = ThreadLocal.withInitial(() -> {
      return new char[5];
   });

   private TimerFormatter() {
      throw new UnsupportedOperationException("Utility class");
   }

   public static String formatTime(long milliseconds) {
      if (milliseconds <= 0L) {
         return "00:00";
      } else {
         String cached = (String)TIMER_CACHE.get(milliseconds);
         if (cached != null) {
            return cached;
         } else {
            long seconds = milliseconds / 1000L;
            long minutes = seconds / 60L;
            seconds %= 60L;
            char[] buffer = (char[])CHAR_BUFFER.get();
            buffer[0] = (char)((int)(48L + minutes / 10L));
            buffer[1] = (char)((int)(48L + minutes % 10L));
            buffer[2] = ':';
            buffer[3] = (char)((int)(48L + seconds / 10L));
            buffer[4] = (char)((int)(48L + seconds % 10L));
            String result = new String(buffer);
            if (minutes < 100L && TIMER_CACHE.size() < 150) {
               TIMER_CACHE.put(milliseconds, result);
            }

            return result;
         }
      }
   }

   public static void clearCache() {
      TIMER_CACHE.clear();
   }
}
