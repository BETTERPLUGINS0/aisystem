package me.casperge.realisticseasons.calendar;

import org.bukkit.World;

public class Time {
   public static int getSeconds(World var0, double var1, double var3) {
      return getFullSeconds(var0, var1, var3) - getHours(var0, var1, var3) * 3600 - getMinutes(var0, var1, var3) * 60;
   }

   public static int getMinutes(World var0, double var1, double var3) {
      return (getFullSeconds(var0, var1, var3) - getHours(var0, var1, var3) * 3600) / 60;
   }

   public static int getHours(World var0, double var1, double var3) {
      return getFullSeconds(var0, var1, var3) / 3600;
   }

   public static int getFullSeconds(World var0, double var1, double var3) {
      return (int)((double)(getTimeModified(var0, var1, var3) % 24000L) * 3.6D);
   }

   public static Long getTimeModified(World var0, double var1, double var3) {
      int var5 = (int)(var0.getFullTime() + 6000L) % 24000;
      if (var1 == 1.0D && var3 == 1.0D) {
         return var0.getFullTime() + 6000L;
      } else if (var5 < 6000) {
         return (long)((double)(var0.getFullTime() - var0.getFullTime() % 24000L) + (double)var5 * var3);
      } else {
         return var5 >= 6000 && var5 < 18000 ? (long)((double)(var0.getFullTime() - var0.getFullTime() % 24000L) + 5999.0D * var3 + ((double)var5 - 5999.0D) * var1) : (long)((double)(var0.getFullTime() - var0.getFullTime() % 24000L) + (5999.0D + ((double)var5 - 17999.0D)) * var3 + 12000.0D * var1);
      }
   }
}
