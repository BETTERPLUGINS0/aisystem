package me.casperge.realisticseasons.calendar;

public class Date {
   private int day;
   private int month;
   private int year;

   public Date(int var1, int var2, int var3) {
      this.day = var1;
      this.month = var2;
      this.year = var3;
   }

   public Date(int var1, int var2) {
      this.day = var1;
      this.month = var2;
      this.year = 0;
   }

   public int getDay() {
      return this.day;
   }

   public int getMonth() {
      return this.month;
   }

   public int getYear() {
      return this.year;
   }

   public boolean isLaterInYear(Date var1) {
      if (var1.getMonth() > this.getMonth()) {
         return true;
      } else if (var1.getMonth() < this.getMonth()) {
         return false;
      } else if (var1.getDay() > this.getDay()) {
         return true;
      } else {
         return var1.getDay() >= this.getDay();
      }
   }

   public String toString(boolean var1) {
      return !var1 ? this.day + "/" + this.month + "/" + this.year : this.month + "/" + this.day + "/" + this.year;
   }

   public String toString() {
      return this.toString(false);
   }

   public static Date fromString(String var0, boolean var1) {
      String[] var5 = var0.split("/");
      int var2;
      int var3;
      int var4;
      if (var1) {
         var2 = Integer.valueOf(var5[1]);
         var3 = Integer.valueOf(var5[0]);
         var4 = Integer.valueOf(var5[2]);
      } else {
         var2 = Integer.valueOf(var5[0]);
         var3 = Integer.valueOf(var5[1]);
         var4 = Integer.valueOf(var5[2]);
      }

      return new Date(var2, var3, var4);
   }
}
