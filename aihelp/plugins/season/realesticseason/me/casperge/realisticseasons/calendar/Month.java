package me.casperge.realisticseasons.calendar;

import java.util.List;

public class Month {
   private String name;
   private DayTime daytime;
   private int length;

   public Month(String var1, int var2, int var3, int var4) {
      this.name = var1;
      this.length = var2;
      this.daytime = new DayTime(var3, var4);
   }

   public double getDayLengthMultiplier() {
      return (double)this.daytime.getDayLength() / (double)((this.daytime.getDayLength() + this.daytime.getNightLength()) / 2);
   }

   public double getNightLengthMultiplier() {
      return (double)this.daytime.getNightLength() / (double)((this.daytime.getDayLength() + this.daytime.getNightLength()) / 2);
   }

   public String getName() {
      return this.name;
   }

   public List<Integer> getDayArray() {
      return this.daytime.getDayArray();
   }

   public List<Integer> getNightArray() {
      return this.daytime.getNightArray();
   }

   public int getDayLength() {
      return this.daytime.getDayLength();
   }

   public int getNightLength() {
      return this.daytime.getNightLength();
   }

   public int getLength() {
      return this.length;
   }
}
