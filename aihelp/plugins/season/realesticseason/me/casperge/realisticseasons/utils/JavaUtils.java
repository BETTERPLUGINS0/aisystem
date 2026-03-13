package me.casperge.realisticseasons.utils;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.casperge.realisticseasons.RealisticSeasons;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class JavaUtils {
   private static Random r;

   public static Random getRandom() {
      if (r == null) {
         r = new Random();
      }

      return r;
   }

   public static int gcd(int var0, int var1) {
      return var1 == 0 ? var0 : gcd(var1, var0 % var1);
   }

   public static String getMonthOfStringDate(String var0) {
      return var0.substring(0, var0.lastIndexOf(" "));
   }

   public static final double[] toCartesianAsVector(double var0) {
      if (!(var0 < 0.0D) && !(var0 > 360.0D)) {
         double[] var2 = new double[]{Math.cos(Math.toRadians(var0)), Math.sin(Math.toRadians(var0))};
         return var2;
      } else {
         throw new IndexOutOfBoundsException("angle ( " + var0 + " ) is out of bounds, should be between 0 and 360 degree");
      }
   }

   public static <T> void moveToBack(LinkedHashSet<T> var0, T var1) {
      if (var0.remove(var1)) {
         var0.add(var1);
      }

   }

   public static String getDayOfStringDate(String var0) {
      return var0.substring(var0.lastIndexOf(" ") + 1);
   }

   public static double randomDouble(double var0, double var2) {
      return Math.random() * (var2 - var0 + 1.0D) + var0;
   }

   public static boolean isNumeric(String var0) {
      if (var0 == null) {
         return false;
      } else {
         try {
            double var1 = Double.parseDouble(var0);
            return true;
         } catch (NumberFormatException var3) {
            return false;
         }
      }
   }

   public static String mixColor(String var0, String var1, double var2) {
      if (!var0.contains("#")) {
         var0 = "#" + var0;
      }

      if (!var1.contains("#")) {
         var1 = "#" + var1;
      }

      Color var4 = Color.decode(var0);
      Color var5 = Color.decode(var1);
      Color var6 = mixColor(var4, var5, var2);
      return String.format("%02x%02x%02x", var6.getRed(), var6.getGreen(), var6.getBlue());
   }

   public static Color mixColor(Color var0, Color var1, double var2) {
      float[] var4 = var0.getComponents((float[])null);
      float[] var5 = var1.getComponents((float[])null);
      float[] var6 = new float[4];

      for(int var7 = 0; var7 < 4; ++var7) {
         var6[var7] = (float)(var2 * (double)var4[var7] + (1.0D - var2) * (double)var5[var7]);
      }

      return new Color(var6[0], var6[1], var6[2], var6[3]);
   }

   public static String hex(String var0) {
      Pattern var1 = Pattern.compile("(#[a-fA-F0-9]{6})");

      for(Matcher var2 = var1.matcher(var0); var2.find(); var2 = var1.matcher(var0)) {
         String var3 = var0.substring(var2.start(), var2.end());
         String var4 = var3.replace('#', 'x');
         char[] var5 = var4.toCharArray();
         StringBuilder var6 = new StringBuilder("");
         char[] var7 = var5;
         int var8 = var5.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            char var10 = var7[var9];
            var6.append("&" + var10);
         }

         var0 = var0.replace(var3, var6.toString());
      }

      return ChatColor.translateAlternateColorCodes('&', var0);
   }

   public static void saveDefaultConfigValues(String var0, String var1) {
      File var2 = new File(RealisticSeasons.getInstance().getDataFolder() + var0);
      YamlConfiguration var3 = YamlConfiguration.loadConfiguration(var2);
      InputStreamReader var4 = null;

      try {
         var4 = new InputStreamReader(RealisticSeasons.getInstance().getResource(var1), "UTF-8");
      } catch (UnsupportedEncodingException var8) {
         var8.printStackTrace();
      }

      if (var4 != null) {
         YamlConfiguration var5 = YamlConfiguration.loadConfiguration(var4);
         var3.setDefaults(var5);
         var3.options().copyDefaults(true);

         try {
            var3.save(var2);
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

   }

   public static int convertToFahrenheit(int var0) {
      return var0 * 9 / 5 + 32;
   }

   public static String getGeoLocationCountryCode(String var0) {
      String var1 = getHttp("http://ip-api.com/line/" + var0 + "?fields=49471");
      if (var1 != null && var1.startsWith("success")) {
         String[] var2 = var1.split("\n");
         String var3 = var2[2];
         return var3;
      } else {
         return "NL";
      }
   }

   private static String getHttp(String var0) {
      try {
         BufferedReader var1 = new BufferedReader(new InputStreamReader((new URL(var0)).openStream()));
         StringBuilder var3 = new StringBuilder();

         String var2;
         while((var2 = var1.readLine()) != null) {
            var3.append(var2).append(System.lineSeparator());
         }

         var1.close();
         return var3.toString();
      } catch (Exception var4) {
         return null;
      }
   }

   public static int hexToDecimal(String var0) {
      return Integer.parseInt(var0, 16);
   }

   public static String decimalToHex(int var0) {
      return String.format("%06X", 16777215 & var0);
   }
}
