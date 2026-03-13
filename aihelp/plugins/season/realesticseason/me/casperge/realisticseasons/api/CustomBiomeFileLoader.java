package me.casperge.realisticseasons.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import me.casperge.realisticseasons.RealisticSeasons;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

public class CustomBiomeFileLoader {
   public static void writeFiles(CustomWorldGenerator var0) {
      String var1 = var0.toString();
      String var2 = var0.getResourceFolderName();
      String[] var3 = getResources(RealisticSeasons.class, var2);
      if (var3 != null && var3.length != 0) {
         String[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            File var8 = new File(RealisticSeasons.getInstance().getDataFolder() + "/biomes", var7);
            if (!var8.exists()) {
               try {
                  InputStream var9 = RealisticSeasons.getInstance().getResource(var2 + "/" + var7);
                  FileUtils.copyInputStreamToFile(var9, var8);
               } catch (Exception var10) {
                  Bukkit.getLogger().severe("[RealisticSeasons] Could not install biome file");
                  Bukkit.getLogger().severe("[RealisticSeasons] File: " + var2 + File.separator + var7);
                  var10.printStackTrace();
               }
            }
         }

      } else {
         Bukkit.getLogger().severe("Could not load " + var1 + " files of RealisticSeasons");
      }
   }

   private static String[] getResources(Class var0, String var1) {
      RealisticSeasons var2 = RealisticSeasons.getInstance();
      URL var3 = var0.getClassLoader().getResource(var1);
      ArrayList var4 = new ArrayList();
      String var5 = var3.getPath().substring(5, var3.getPath().indexOf("!"));
      JarFile var6 = null;

      try {
         var6 = new JarFile(URLDecoder.decode(var5, "UTF-8"));
      } catch (UnsupportedEncodingException var11) {
         var2.getServer().getConsoleSender().sendMessage("ERROR - getResources() - couldn't decode the Jar file to index resources.");
      } catch (IOException var12) {
         var2.getServer().getConsoleSender().sendMessage("ERROR - getResources() - couldn't perform IO operations on jar file");
      }

      Enumeration var7 = var6.entries();

      while(var7.hasMoreElements()) {
         String var8 = ((JarEntry)var7.nextElement()).getName();
         if (var8.startsWith(var1)) {
            String var9 = var8.substring(var1.length() + 1);
            String var10 = var8.substring(var8.length() - 1);
            if (var10 != File.separator && var9.matches(".*[a-zA-Z0-9].*")) {
               var4.add(var9);
            }
         }
      }

      return (String[])var4.toArray(new String[var4.size()]);
   }

   public static List<CustomWorldGenerator> getAlreadyInstalledGenerators() {
      ArrayList var0 = new ArrayList();
      File var1 = new File("plugins/RealisticSeasons/biomes/");
      File[] var2 = var1.listFiles();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         File var5 = var2[var4];
         if (var5.getName().contains("_")) {
            String var6 = var5.getName().split("_")[0];
            if (CustomWorldGenerator.isWorldGenerator(var6)) {
               CustomWorldGenerator var7 = CustomWorldGenerator.fromFile(var6);
               if (!var0.contains(var7)) {
                  var0.add(var7);
               }
            }
         }
      }

      return var0;
   }

   public static List<CustomWorldGenerator> getActiveGenerators() {
      ArrayList var0 = new ArrayList();
      Iterator var1 = RealisticSeasons.getInstance().getNMSUtils().getCustomBiomes(RealisticSeasons.getInstance().getSettings().biomeDisplayName).iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         if (CustomWorldGenerator.isKnownBiome(var2)) {
            CustomWorldGenerator var3 = CustomWorldGenerator.fromBiome(var2);
            if (!var0.contains(var3)) {
               var0.add(var3);
            }
         }
      }

      return var0;
   }
}
