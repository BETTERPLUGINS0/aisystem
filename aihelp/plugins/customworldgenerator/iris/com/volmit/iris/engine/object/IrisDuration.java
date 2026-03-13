package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.format.Form;
import java.util.concurrent.TimeUnit;
import lombok.Generated;

@Snippet("duration")
@Desc("Represents a combined duration. Fill each property to add time into a single duration")
public class IrisDuration {
   @Desc("Milliseconds (1000ms = 1 second)")
   private int milliseconds = 0;
   @Desc("Minecraft Ticks (20 minecraft ticks = 1 second")
   private int minecraftTicks = 0;
   @Desc("Seconds (60 seconds = 1 minute)")
   private int seconds = 0;
   @Desc("Minutes (60 minutes = 1 hour)")
   private int minutes = 0;
   @Desc("Minecraft Hours (about 50 real seconds)")
   private int minecraftHours = 0;
   @Desc("Hours (24 hours = 1 day)")
   private int hours = 0;
   @Desc("Minecraft Days (1 minecraft day = 20 real minutes)")
   private int minecraftDays = 0;
   @Desc("Minecraft Weeks (1 minecraft week = 2 real hours and 18 real minutes)")
   private int minecraftWeeks = 0;
   @Desc("Minecraft Lunar Cycles (1 minecraft lunar cycle = 2 real hours and 36 real minutes)")
   private int minecraftLunarCycles = 0;
   @Desc("REAL (not minecraft) Days")
   private int days = 0;

   public String toString() {
      return Form.duration((double)this.toMilliseconds(), 2);
   }

   public long toMilliseconds() {
      return (long)this.getMilliseconds() + TimeUnit.SECONDS.toMillis((long)this.getSeconds()) + TimeUnit.MINUTES.toMillis((long)this.getMinutes()) + TimeUnit.HOURS.toMillis((long)this.getHours()) + TimeUnit.DAYS.toMillis((long)this.getDays()) + (long)this.getMinecraftTicks() * 50L + (long)this.getMinecraftHours() * 50000L + (long)this.getMinecraftWeeks() * 50000L + (long)this.getMinecraftDays() * 24000L + (long)this.getMinecraftWeeks() * 168000L + (long)this.getMinecraftLunarCycles() * 192000L;
   }

   @Generated
   public int getMilliseconds() {
      return this.milliseconds;
   }

   @Generated
   public int getMinecraftTicks() {
      return this.minecraftTicks;
   }

   @Generated
   public int getSeconds() {
      return this.seconds;
   }

   @Generated
   public int getMinutes() {
      return this.minutes;
   }

   @Generated
   public int getMinecraftHours() {
      return this.minecraftHours;
   }

   @Generated
   public int getHours() {
      return this.hours;
   }

   @Generated
   public int getMinecraftDays() {
      return this.minecraftDays;
   }

   @Generated
   public int getMinecraftWeeks() {
      return this.minecraftWeeks;
   }

   @Generated
   public int getMinecraftLunarCycles() {
      return this.minecraftLunarCycles;
   }

   @Generated
   public int getDays() {
      return this.days;
   }

   @Generated
   public void setMilliseconds(final int milliseconds) {
      this.milliseconds = var1;
   }

   @Generated
   public void setMinecraftTicks(final int minecraftTicks) {
      this.minecraftTicks = var1;
   }

   @Generated
   public void setSeconds(final int seconds) {
      this.seconds = var1;
   }

   @Generated
   public void setMinutes(final int minutes) {
      this.minutes = var1;
   }

   @Generated
   public void setMinecraftHours(final int minecraftHours) {
      this.minecraftHours = var1;
   }

   @Generated
   public void setHours(final int hours) {
      this.hours = var1;
   }

   @Generated
   public void setMinecraftDays(final int minecraftDays) {
      this.minecraftDays = var1;
   }

   @Generated
   public void setMinecraftWeeks(final int minecraftWeeks) {
      this.minecraftWeeks = var1;
   }

   @Generated
   public void setMinecraftLunarCycles(final int minecraftLunarCycles) {
      this.minecraftLunarCycles = var1;
   }

   @Generated
   public void setDays(final int days) {
      this.days = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisDuration)) {
         return false;
      } else {
         IrisDuration var2 = (IrisDuration)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMilliseconds() != var2.getMilliseconds()) {
            return false;
         } else if (this.getMinecraftTicks() != var2.getMinecraftTicks()) {
            return false;
         } else if (this.getSeconds() != var2.getSeconds()) {
            return false;
         } else if (this.getMinutes() != var2.getMinutes()) {
            return false;
         } else if (this.getMinecraftHours() != var2.getMinecraftHours()) {
            return false;
         } else if (this.getHours() != var2.getHours()) {
            return false;
         } else if (this.getMinecraftDays() != var2.getMinecraftDays()) {
            return false;
         } else if (this.getMinecraftWeeks() != var2.getMinecraftWeeks()) {
            return false;
         } else if (this.getMinecraftLunarCycles() != var2.getMinecraftLunarCycles()) {
            return false;
         } else {
            return this.getDays() == var2.getDays();
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisDuration;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.getMilliseconds();
      var3 = var3 * 59 + this.getMinecraftTicks();
      var3 = var3 * 59 + this.getSeconds();
      var3 = var3 * 59 + this.getMinutes();
      var3 = var3 * 59 + this.getMinecraftHours();
      var3 = var3 * 59 + this.getHours();
      var3 = var3 * 59 + this.getMinecraftDays();
      var3 = var3 * 59 + this.getMinecraftWeeks();
      var3 = var3 * 59 + this.getMinecraftLunarCycles();
      var3 = var3 * 59 + this.getDays();
      return var3;
   }
}
