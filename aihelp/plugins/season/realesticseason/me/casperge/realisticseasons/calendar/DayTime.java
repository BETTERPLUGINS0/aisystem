package me.casperge.realisticseasons.calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.bukkit.Bukkit;

public class DayTime {
   private List<Integer> dayArray = new ArrayList();
   private List<Integer> nightArray = new ArrayList();
   private int dayLength;
   private int nightLength;

   public DayTime(int var1, int var2) {
      this.dayLength = var1;
      this.nightLength = var2;
      this.dayArray = generateIntList(var1);
      this.nightArray = generateIntList(var2);
   }

   public int getDayLength() {
      return this.dayLength;
   }

   public int getNightLength() {
      return this.nightLength;
   }

   public List<Integer> getDayArray() {
      return this.dayArray;
   }

   public List<Integer> getNightArray() {
      return this.nightArray;
   }

   public static List<Integer> generateIntList(int var0) {
      double var1 = (double)var0;
      if (10.0D / var1 <= 1.0D) {
         int var8 = JavaUtils.gcd(10, var0);
         int var4 = var0 / var8;
         int var5 = 10 / var8;
         ArrayList var6 = new ArrayList();

         for(int var7 = 0; var7 < var4; ++var7) {
            if (var7 < var5) {
               var6.add(1);
            } else {
               var6.add(0);
            }
         }

         Collections.shuffle(var6);
         return var6;
      } else {
         List var3 = getCorrectListBelow10(var0);
         Collections.shuffle(var3);
         return var3;
      }
   }

   public static List<Integer> getCorrectListBelow10(int var0) {
      switch(var0) {
      case 0:
         Bukkit.getLogger().severe("Day/night length can't be 0!");
         return null;
      case 1:
         return Arrays.asList(10);
      case 2:
         return Arrays.asList(5);
      case 3:
         return Arrays.asList(3, 3, 4);
      case 4:
         return Arrays.asList(3, 3, 3, 3, 2, 1);
      case 5:
         return Arrays.asList(2);
      case 6:
         return Arrays.asList(2, 2, 1);
      case 7:
         return Arrays.asList(2, 2, 2, 1, 1, 1, 1);
      case 8:
         return Arrays.asList(2, 1, 1, 1);
      case 9:
         return Arrays.asList(2, 1, 1, 1, 1, 1, 1, 1, 1);
      case 10:
         return Arrays.asList(1);
      default:
         return Arrays.asList(1);
      }
   }
}
