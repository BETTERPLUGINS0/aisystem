package com.volmit.iris.util.format;

import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RollingSequence;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Form {
   private static final String[] NAMES = new String[]{"Thousand", "Million", "Billion", "Trillion", "Quadrillion", "Quintillion", "Sextillion", "Septillion", "Octillion", "Nonillion", "Decillion", "Undecillion", "Duodecillion", "Tredecillion", "Quattuordecillion", "Quindecillion", "Sexdecillion", "Septendecillion", "Octodecillion", "Novemdecillion", "Vigintillion"};
   private static final BigInteger THOUSAND = BigInteger.valueOf(1000L);
   private static final NavigableMap<BigInteger, String> MAP = new TreeMap();
   private static NumberFormat NF;
   private static DecimalFormat DF;

   public static String getNumberSuffixThStRd(int day) {
      if (var0 >= 11 && var0 <= 13) {
         return f(var0) + "th";
      } else {
         String var10000;
         switch(var0 % 10) {
         case 1:
            var10000 = f(var0) + "st";
            break;
         case 2:
            var10000 = f(var0) + "nd";
            break;
         case 3:
            var10000 = f(var0) + "rd";
            break;
         default:
            var10000 = f(var0) + "th";
         }

         return var10000;
      }
   }

   private static void instantiate() {
      if (NF == null) {
         NF = NumberFormat.getInstance(Locale.US);
      }

   }

   public static String scroll(String smx, int viewport, long time) {
      String var4 = repeat(" ", var1) + var0 + repeat(" ", var1);
      int var5 = var4.length();
      int var6 = (int)(var2 % (long)(var5 - var1));
      String var7 = var4.substring(var6, (Integer)M.min(var6 + var1, var5 - 1));
      var7 = var7.length() < var1 ? var7 + repeat(" ", var1 - var7.length() - 3) : var7;
      return var7;
   }

   public static String capitalize(String s) {
      StringBuilder var1 = new StringBuilder();
      boolean var2 = true;
      char[] var3 = var0.trim().toCharArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Character var6 = var3[var5];
         if (var2) {
            var1.append(Character.toUpperCase(var6));
            var2 = false;
         } else {
            var1.append(var6);
         }
      }

      return var1.toString();
   }

   public static String capitalizeWords(String s) {
      StringBuilder var1 = new StringBuilder();
      String[] var2 = var0.trim().split(" ");
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         var1.append(" ").append(capitalize(var5.trim()));
      }

      return var1.substring(1);
   }

   public static String wrap(String s, int len) {
      return wrap(var0, var1, (String)null, false);
   }

   public static String wrapWords(String s, int len) {
      return wrap(var0, var1, (String)null, true);
   }

   public static String wrap(String s, int len, String newLineSep, boolean soft) {
      return wrap(var0, var1, var2, var3, " ");
   }

   public static String hardWrap(String s, int len) {
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var0.length(); var3 += var1) {
         if (var3 + var1 > var0.length()) {
            var2.append(var0, var3, var0.length());
            break;
         }

         var2.append(var0, var3, var3 + var1).append("\n");
      }

      return var2.toString();
   }

   public static List<String> hardWrapList(String s, int len) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var0.length(); var3 += var1) {
         if (var3 + var1 > var0.length()) {
            var2.add(var0.substring(var3));
            break;
         }

         var2.add(var0.substring(var3, var3 + var1));
      }

      return var2;
   }

   public static String wrap(String s, int len, String newLineSep, boolean soft, String regex) {
      if (var0 == null) {
         return null;
      } else {
         if (var2 == null) {
            var2 = "\n";
         }

         if (var1 < 1) {
            var1 = 1;
         }

         if (var4.trim().equals("")) {
            var4 = " ";
         }

         Pattern var5 = Pattern.compile(var4);
         int var6 = var0.length();
         int var7 = 0;
         StringBuilder var8 = new StringBuilder(var6 + 32);

         while(var7 < var6) {
            int var9 = -1;
            Matcher var10 = var5.matcher(var0.substring(var7, Math.min(var7 + var1 + 1, var6)));
            if (var10.find()) {
               if (var10.start() == 0) {
                  var7 += var10.end();
                  continue;
               }

               var9 = var10.start();
            }

            if (var6 - var7 <= var1) {
               break;
            }

            while(var10.find()) {
               var9 = var10.start() + var7;
            }

            if (var9 >= var7) {
               var8.append(var0, var7, var9);
               var8.append(var2);
               var7 = var9 + 1;
            } else if (var3) {
               var8.append(var0, var7, var1 + var7);
               var8.append(var2);
               var7 += var1;
            } else {
               var10 = var5.matcher(var0.substring(var7 + var1));
               if (var10.find()) {
                  var9 = var10.start() + var7 + var1;
               }

               if (var9 >= 0) {
                  var8.append(var0, var7, var9);
                  var8.append(var2);
                  var7 = var9 + 1;
               } else {
                  var8.append(var0.substring(var7));
                  var7 = var6;
               }
            }
         }

         var8.append(var0.substring(var7));
         return var8.toString();
      }
   }

   public static String duration(RollingSequence rollingSequence, long duration) {
      String var3 = "Millisecond";
      double var4 = (double)var1;
      short var6 = 1000;
      if (var4 > (double)var6) {
         var4 /= (double)var6;
         var3 = "Second";
         byte var7 = 60;
         String var10000;
         if (var4 > (double)var7) {
            var4 /= (double)var7;
            var3 = "Minute";
            if (var4 > (double)var7) {
               var4 /= (double)var7;
               var3 = "Hour";
               var7 = 24;
               if (var4 > 24.0D) {
                  var4 /= (double)var7;
                  var3 = "Day";
                  var7 = 7;
                  if (var4 > (double)var7) {
                     var4 /= (double)var7;
                     var3 = "Week";
                     var7 = 4;
                     if (var4 > (double)var7) {
                        var4 /= (double)var7;
                        var3 = "Month";
                        var7 = 12;
                        if (var4 > (double)var7) {
                           var4 /= (double)var7;
                           var3 = "Year";
                           var10000 = fd(var4, 0);
                           return var10000 + " " + var3 + ((int)var4 == 1 ? "" : "s");
                        } else {
                           var10000 = fd(var4, 0);
                           return var10000 + " " + var3 + ((int)var4 == 1 ? "" : "s");
                        }
                     } else {
                        var10000 = fd(var4, 0);
                        return var10000 + " " + var3 + ((int)var4 == 1 ? "" : "s");
                     }
                  } else {
                     var10000 = fd(var4, 0);
                     return var10000 + " " + var3 + ((int)var4 == 1 ? "" : "s");
                  }
               } else {
                  var10000 = fd(var4, 0);
                  return var10000 + " " + var3 + ((int)var4 == 1 ? "" : "s");
               }
            } else {
               var10000 = fd(var4, 0);
               return var10000 + " " + var3 + ((int)var4 == 1 ? "" : "s");
            }
         } else {
            var10000 = fd(var4, 0);
            return var10000 + " " + var3 + ((int)var4 == 1 ? "" : "s");
         }
      } else {
         return "Under a Second";
      }
   }

   public static String fmin(Calendar c) {
      String var1 = var0.get(12).makeConcatWithConstants<invokedynamic>(var0.get(12));
      return var1.length() == 1 ? "0" + var1 : var1;
   }

   public static String ago(long time) {
      long var2 = M.ms();
      if (var0 > var2 - TimeUnit.SECONDS.toMillis(30L) && var0 < var2) {
         return "Just Now";
      } else if (var0 > var2 - TimeUnit.SECONDS.toMillis(60L) && var0 < var2) {
         return "Seconds Ago";
      } else if (var0 > var2 - TimeUnit.MINUTES.toMillis(10L) && var0 < var2) {
         return "Minutes Ago";
      } else {
         Calendar var4 = Calendar.getInstance();
         Calendar var5 = Calendar.getInstance();
         var5.setTimeInMillis(var0);
         boolean var6 = var4.get(1) == var5.get(1);
         boolean var7 = var4.get(6) == var5.get(6);
         int var8;
         if (var7) {
            var8 = var5.get(10);
            var8 = var8 == 0 ? 12 : var8;
            return "Today at " + var8 + ":" + fmin(var5) + " " + (var5.get(9) == 1 ? "PM" : "AM");
         } else {
            String var10;
            int var12;
            String var10000;
            if (var6) {
               boolean var15 = var4.get(6) - 1 == var5.get(6);
               int var16;
               if (var15) {
                  var16 = var5.get(10);
                  var16 = var16 == 0 ? 12 : var16;
                  return "Yesterday at " + var16 + ":" + fmin(var5) + " " + (var5.get(9) == 1 ? "PM" : "AM");
               } else {
                  var16 = var5.get(10);
                  var16 = var16 == 0 ? 12 : var16;
                  switch(var5.get(7)) {
                  case 1:
                     var10000 = "Sunday";
                     break;
                  case 2:
                     var10000 = "Monday";
                     break;
                  case 3:
                     var10000 = "Tuesday";
                     break;
                  case 4:
                     var10000 = "Wednesday";
                     break;
                  case 5:
                     var10000 = "Thursday";
                     break;
                  case 6:
                     var10000 = "Friday";
                     break;
                  case 7:
                     var10000 = "Saturday";
                     break;
                  default:
                     var10000 = "Error Day";
                  }

                  var10 = var10000;
                  String var17 = "Error Month";
                  var12 = var5.get(2);
                  switch(var12) {
                  case 0:
                     var17 = "Jan";
                     break;
                  case 1:
                     var17 = "Feb";
                     break;
                  case 2:
                     var17 = "Mar";
                     break;
                  case 3:
                     var17 = "Apr";
                     break;
                  case 4:
                     var17 = "May";
                     break;
                  case 5:
                     var17 = "Jun";
                     break;
                  case 6:
                     var17 = "Jul";
                     break;
                  case 7:
                     var17 = "Aug";
                     break;
                  case 8:
                     var17 = "Sep";
                     break;
                  case 9:
                     var17 = "Oct";
                     break;
                  case 10:
                     var17 = "Nov";
                     break;
                  case 11:
                     var17 = "Dec";
                  }

                  int var18 = var5.get(5);
                  String var19 = numberSuffix(var18);
                  return var10 + ", " + var17 + " " + var19 + " at " + var16 + ":" + fmin(var5) + " " + (var5.get(9) == 1 ? "PM" : "AM");
               }
            } else {
               var8 = var5.get(10);
               var8 = var8 == 0 ? 12 : var8;
               switch(var5.get(7)) {
               case 1:
                  var10000 = "Sunday";
                  break;
               case 2:
                  var10000 = "Monday";
                  break;
               case 3:
                  var10000 = "Tuesday";
                  break;
               case 4:
                  var10000 = "Wednesday";
                  break;
               case 5:
                  var10000 = "Thursday";
                  break;
               case 6:
                  var10000 = "Friday";
                  break;
               case 7:
                  var10000 = "Saturday";
                  break;
               default:
                  var10000 = "Error Day";
               }

               String var9 = var10000;
               var10 = "Error Month";
               int var11 = var5.get(2);
               switch(var11) {
               case 0:
                  var10 = "Jan";
                  break;
               case 1:
                  var10 = "Feb";
                  break;
               case 2:
                  var10 = "Mar";
                  break;
               case 3:
                  var10 = "Apr";
                  break;
               case 4:
                  var10 = "May";
                  break;
               case 5:
                  var10 = "Jun";
                  break;
               case 6:
                  var10 = "Jul";
                  break;
               case 7:
                  var10 = "Aug";
                  break;
               case 8:
                  var10 = "Sep";
                  break;
               case 9:
                  var10 = "Oct";
                  break;
               case 10:
                  var10 = "Nov";
                  break;
               case 11:
                  var10 = "Dec";
               }

               var12 = var5.get(5);
               String var13 = numberSuffix(var12);
               int var14 = var5.get(1);
               return var14 + ", " + var9 + ", " + var10 + " " + var13 + " at " + var8 + ":" + fmin(var5) + " " + (var5.get(9) == 1 ? "PM" : "AM");
            }
         }
      }
   }

   public static String numberSuffix(int i) {
      String[] var1 = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
      String var10000;
      switch(var0 % 100) {
      case 11:
      case 12:
      case 13:
         var10000 = var0 + "th";
         break;
      default:
         var10000 = var0 + var1[var0 % 10];
      }

      return var10000;
   }

   public static String duration(double ms, int prec) {
      String var10000;
      if (var0 < 1000.0D) {
         var10000 = f(var0, var2);
         return var10000 + "ms";
      } else if (var0 / 1000.0D < 60.0D) {
         return f(var0 / 1000.0D, var2) + "s";
      } else if (var0 / 1000.0D / 60.0D < 60.0D) {
         return f(var0 / 1000.0D / 60.0D, var2) + "m";
      } else if (var0 / 1000.0D / 60.0D / 60.0D < 24.0D) {
         return f(var0 / 1000.0D / 60.0D / 60.0D, var2) + " hours";
      } else if (var0 / 1000.0D / 60.0D / 60.0D / 24.0D < 7.0D) {
         return f(var0 / 1000.0D / 60.0D / 24.0D, var2) + " days";
      } else {
         var10000 = f(var0, var2);
         return var10000 + "ms";
      }
   }

   public static String duration(long ms) {
      return duration(var0, 0);
   }

   public static String duration(long ms, int prec) {
      if ((double)var0 < 1000.0D) {
         return f((float)var0, var2) + "ms";
      } else if ((double)var0 / 1000.0D < 60.0D) {
         return f((double)var0 / 1000.0D, var2) + " seconds";
      } else if ((double)var0 / 1000.0D / 60.0D < 60.0D) {
         return f((double)var0 / 1000.0D / 60.0D, var2) + " minutes";
      } else if ((double)var0 / 1000.0D / 60.0D / 60.0D < 24.0D) {
         return f((double)var0 / 1000.0D / 60.0D / 60.0D, var2) + " hours";
      } else {
         return (double)var0 / 1000.0D / 60.0D / 60.0D / 24.0D < 7.0D ? f((double)var0 / 1000.0D / 60.0D / 60.0D / 24.0D, var2) + " days" : f((float)var0, var2) + "ms";
      }
   }

   public static String b(int i) {
      return b(new BigInteger(String.valueOf(var0)));
   }

   public static String b(long i) {
      return b(new BigInteger(String.valueOf(var0)));
   }

   public static String b(double i) {
      return b(new BigInteger(String.valueOf((long)var0)));
   }

   public static String b(BigInteger number) {
      Entry var1 = MAP.floorEntry(var0);
      if (var1 == null) {
         return "Nearly nothing";
      } else {
         BigInteger var2 = (BigInteger)var1.getKey();
         BigInteger var3 = var2.divide(THOUSAND);
         BigInteger var4 = var0.divide(var3);
         float var5 = var4.floatValue() / 1000.0F;
         float var6 = (float)((int)((double)var5 * 100.0D)) / 100.0F;
         return var6 % 1.0F == 0.0F ? (int)var6 + " " + (String)var1.getValue() : var6 + " " + (String)var1.getValue();
      }
   }

   public static String fileSize(long s) {
      return ofSize(var0, 1000);
   }

   public static String split(String splitter, String... strings) {
      StringBuilder var2 = new StringBuilder();
      String[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         var2.append(var0);
         var2.append(var6);
      }

      return var2.substring(var0.length());
   }

   public static String memSize(long s) {
      return ofSize(var0, 1024);
   }

   public static String memSize(long s, int dec) {
      return ofSize(var0, 1024, var2);
   }

   public static String stamp(long t) {
      Date var2 = new Date(var0);
      int var10000 = var2.getMonth();
      return var10000 + "-" + var2.getDate() + "-" + (var2.getYear() + 1900) + " " + var2.getHours() + "h " + var2.getMinutes() + "m " + var2.getSeconds() + "s ";
   }

   public static String stampTime(long t) {
      Date var2 = new Date(var0);
      int var10000 = Calendar.getInstance().get(11);
      return var10000 + ":" + forceDoubleDigit(var2.getMinutes()) + ":" + forceDoubleDigit(var2.getSeconds());
   }

   public static String forceDoubleDigit(int dig) {
      return var0 < 10 ? "0" + var0 : var0.makeConcatWithConstants<invokedynamic>(var0);
   }

   public static String stampDay(long t) {
      Date var2 = new Date(var0);
      int var10000 = var2.getMonth();
      return var10000 + "-" + var2.getDate() + "-" + (var2.getYear() + 1900);
   }

   public static String ofSize(long s, int div) {
      double var3 = (double)var0;
      String var5 = " Bytes";
      if (var3 > (double)(var2 - 1)) {
         var3 /= (double)var2;
         var5 = " KB";
         if (var3 > (double)(var2 - 1)) {
            var3 /= (double)var2;
            var5 = " MB";
            if (var3 > (double)(var2 - 1)) {
               var3 /= (double)var2;
               var5 = " GB";
               if (var3 > (double)(var2 - 1)) {
                  var3 /= (double)var2;
                  var5 = " TB";
               }
            }
         }
      }

      String var10000;
      if (!var5.equals("GB") && !var5.equals("TB")) {
         var10000 = f(var3, 0);
         return var10000 + var5;
      } else {
         var10000 = f(var3, 1);
         return var10000 + var5;
      }
   }

   public static String ofSize(long s, int div, int dec) {
      double var4 = (double)var0;
      String var6 = "Bytes";
      if (var4 > (double)(var2 - 1)) {
         var4 /= (double)var2;
         var6 = "KB";
         if (var4 > (double)(var2 - 1)) {
            var4 /= (double)var2;
            var6 = "MB";
            if (var4 > (double)(var2 - 1)) {
               var4 /= (double)var2;
               var6 = "GB";
               if (var4 > (double)(var2 - 1)) {
                  var4 /= (double)var2;
                  var6 = "TB";
               }
            }
         }
      }

      String var10000 = f(var4, var3);
      return var10000 + " " + var6;
   }

   public static String ofSizeMetricWeight(long s, int div, int dec) {
      boolean var4 = var0 < 0L;
      if (var4) {
         var0 = -var0;
      }

      double var5 = (double)var0;
      String var7 = "Grams";
      if (var5 > (double)(var2 - 1)) {
         var5 /= (double)var2;
         var7 = "KG";
         if (var5 > (double)(var2 - 1)) {
            var5 /= (double)var2;
            var7 = "MG";
            if (var5 > (double)(var2 - 1)) {
               var5 /= (double)var2;
               var7 = "GG";
               if (var5 > (double)(var2 - 1)) {
                  var5 /= (double)var2;
                  var7 = "TG";
               }
            }
         }
      }

      return (var4 ? "-" : "") + f(var5, var3) + " " + var7;
   }

   public static String trim(String s, int l) {
      if (var0.length() <= var1) {
         return var0;
      } else {
         String var10000 = var0.substring(0, var1);
         return var10000 + "...";
      }
   }

   public static String cname(String clazz) {
      StringBuilder var1 = new StringBuilder();
      char[] var2 = var0.toCharArray();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Character var5 = var2[var4];
         if (Character.isUpperCase(var5)) {
            var1.append("-").append(Character.toLowerCase(var5));
         } else {
            var1.append(var5);
         }
      }

      if (var1.toString().startsWith("-")) {
         var1 = new StringBuilder(var1.substring(1));
      }

      return var1.toString();
   }

   public static String mem(long mb) {
      return var0 < 1024L ? f(var0) + " MB" : f((double)var0 / 1024.0D, 1) + " GB";
   }

   public static String memx(long kb) {
      if (var0 < 1024L) {
         return fd((double)var0, 2) + " KB";
      } else {
         double var2 = (double)var0 / 1024.0D;
         if (var2 < 1024.0D) {
            return fd(var2, 2) + " MB";
         } else {
            double var4 = var2 / 1024.0D;
            return fd(var4, 2) + " GB";
         }
      }
   }

   public static String f(long i) {
      instantiate();
      return NF.format(var0);
   }

   public static String f(int i) {
      instantiate();
      return NF.format((long)var0);
   }

   public static String f(double i, int p) {
      String var3 = "#";
      if (var2 > 0) {
         var3 = var3 + "." + repeat("#", var2);
      }

      DF = new DecimalFormat(var3);
      return DF.format(var0).replaceAll("\\Q,\\E", ".");
   }

   public static String fd(double i, int p) {
      String var3 = "0";
      if (var2 > 0) {
         var3 = var3 + "." + repeat("0", var2);
      }

      DF = new DecimalFormat(var3);
      return DF.format(var0);
   }

   public static String f(float i, int p) {
      String var2 = "#";
      if (var1 > 0) {
         var2 = var2 + "." + repeat("#", var1);
      }

      DF = new DecimalFormat(var2);
      return DF.format((double)var0);
   }

   public static String f(double i) {
      return f(var0, 1);
   }

   public static String f(float i) {
      return f(var0, 1);
   }

   public static String pc(double i, int p) {
      return f(var0 * 100.0D, var2) + "%";
   }

   public static String pc(float i, int p) {
      return f(var0 * 100.0F, var1) + "%";
   }

   public static String pc(double i) {
      return f(var0 * 100.0D, 0) + "%";
   }

   public static String pc(float i) {
      return f(var0 * 100.0F, 0) + "%";
   }

   public static String pc(int i, int of, int p) {
      return f(100.0D * ((double)var0 / (double)var1), var2) + "%";
   }

   public static String pc(int i, int of) {
      return pc(var0, var1, 0);
   }

   public static String pc(long i, long of, int p) {
      return f(100.0D * ((double)var0 / (double)var2), var4) + "%";
   }

   public static String pc(long i, long of) {
      return pc(var0, var2, 0);
   }

   public static String msSeconds(long ms) {
      return f((double)var0 / 1000.0D);
   }

   public static String msSeconds(long ms, int p) {
      return f((double)var0 / 1000.0D, var2);
   }

   public static String nsMs(long ns) {
      return f((double)var0 / 1000000.0D);
   }

   public static String nsMs(long ns, int p) {
      return f((double)var0 / 1000000.0D, var2);
   }

   public static String nsMsd(long ns, int p) {
      return fd((double)var0 / 1000000.0D, var2);
   }

   public static String toRoman(int num) {
      LinkedHashMap var1 = new LinkedHashMap();
      var1.put("M", 1000);
      var1.put("CM", 900);
      var1.put("D", 500);
      var1.put("CD", 400);
      var1.put("C", 100);
      var1.put("XC", 90);
      var1.put("L", 50);
      var1.put("XL", 40);
      var1.put("X", 10);
      var1.put("IX", 9);
      var1.put("V", 5);
      var1.put("IV", 4);
      var1.put("I", 1);
      StringBuilder var2 = new StringBuilder();

      Entry var4;
      for(Iterator var3 = var1.entrySet().iterator(); var3.hasNext(); var0 %= (Integer)var4.getValue()) {
         var4 = (Entry)var3.next();
         int var5 = var0 / (Integer)var4.getValue();
         var2.append(repeat((String)var4.getKey(), var5));
      }

      return var2.toString();
   }

   public static int fromRoman(String number) {
      if (var0.isEmpty()) {
         return 0;
      } else {
         var0 = var0.toUpperCase();
         if (var0.startsWith("M")) {
            return 1000 + fromRoman(var0.substring(1));
         } else if (var0.startsWith("CM")) {
            return 900 + fromRoman(var0.substring(2));
         } else if (var0.startsWith("D")) {
            return 500 + fromRoman(var0.substring(1));
         } else if (var0.startsWith("CD")) {
            return 400 + fromRoman(var0.substring(2));
         } else if (var0.startsWith("C")) {
            return 100 + fromRoman(var0.substring(1));
         } else if (var0.startsWith("XC")) {
            return 90 + fromRoman(var0.substring(2));
         } else if (var0.startsWith("L")) {
            return 50 + fromRoman(var0.substring(1));
         } else if (var0.startsWith("XL")) {
            return 40 + fromRoman(var0.substring(2));
         } else if (var0.startsWith("X")) {
            return 10 + fromRoman(var0.substring(1));
         } else if (var0.startsWith("IX")) {
            return 9 + fromRoman(var0.substring(2));
         } else if (var0.startsWith("V")) {
            return 5 + fromRoman(var0.substring(1));
         } else if (var0.startsWith("IV")) {
            return 4 + fromRoman(var0.substring(2));
         } else {
            return var0.startsWith("I") ? 1 + fromRoman(var0.substring(1)) : 0;
         }
      }
   }

   public static String repeat(String s, int n) {
      if (var0 == null) {
         return null;
      } else {
         StringBuilder var2 = new StringBuilder();

         for(int var3 = 0; var3 < var1; ++var3) {
            var2.append(var0);
         }

         return var2.toString();
      }
   }

   static {
      for(int var0 = 0; var0 < NAMES.length; ++var0) {
         MAP.put(THOUSAND.pow(var0 + 1), NAMES[var0]);
      }

   }
}
