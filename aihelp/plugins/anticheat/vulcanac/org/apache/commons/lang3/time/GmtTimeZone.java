package org.apache.commons.lang3.time;

import java.util.Date;
import java.util.TimeZone;

class GmtTimeZone extends TimeZone {
   private static final int MILLISECONDS_PER_MINUTE = 60000;
   private static final int MINUTES_PER_HOUR = 60;
   private static final int HOURS_PER_DAY = 24;
   static final long serialVersionUID = 1L;
   private final int offset;
   private final String zoneId;

   GmtTimeZone(boolean var1, int var2, int var3) {
      if (var2 >= 24) {
         throw new IllegalArgumentException(var2 + " hours out of range");
      } else if (var3 >= 60) {
         throw new IllegalArgumentException(var3 + " minutes out of range");
      } else {
         int var4 = (var3 + var2 * 60) * '\uea60';
         this.offset = var1 ? -var4 : var4;
         this.zoneId = twoDigits(twoDigits((new StringBuilder(9)).append("GMT").append((char)(var1 ? '-' : '+')), var2).append(':'), var3).toString();
      }
   }

   private static StringBuilder twoDigits(StringBuilder var0, int var1) {
      return var0.append((char)(48 + var1 / 10)).append((char)(48 + var1 % 10));
   }

   public int getOffset(int var1, int var2, int var3, int var4, int var5, int var6) {
      return this.offset;
   }

   public void setRawOffset(int var1) {
      throw new UnsupportedOperationException();
   }

   public int getRawOffset() {
      return this.offset;
   }

   public String getID() {
      return this.zoneId;
   }

   public boolean useDaylightTime() {
      return false;
   }

   public boolean inDaylightTime(Date var1) {
      return false;
   }

   public String toString() {
      return "[GmtTimeZone id=\"" + this.zoneId + "\",offset=" + this.offset + ']';
   }

   public int hashCode() {
      return this.offset;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof GmtTimeZone)) {
         return false;
      } else {
         return this.zoneId == ((GmtTimeZone)var1).zoneId;
      }
   }
}
