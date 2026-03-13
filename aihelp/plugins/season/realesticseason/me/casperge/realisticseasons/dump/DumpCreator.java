package me.casperge.realisticseasons.dump;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.Season;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DumpCreator {
   private RealisticSeasons main;
   private Long lastDump;

   public DumpCreator(RealisticSeasons var1) {
      this.main = var1;
   }

   public void createDump(final UUID var1, String var2) {
      final DumpInformation var3 = this.createDumpInformation();
      if (var3.isRateLimit()) {
         Player var4 = Bukkit.getPlayer(var1);
         if (var4 != null) {
            var4.sendMessage(ChatColor.RED + "[RealisticSeasons] Could not create dump, there needs to be atleast 30 seconds between attempts.");
         }

      } else {
         var3.setPassword(Bukkit.getOfflinePlayer(var1).getName());
         var3.setKey(var2);
         Bukkit.getScheduler().runTaskAsynchronously(this.main, new Runnable() {
            public void run() {
               var3.addEntry("log", LogGrabber.grabLatestLog());
               final DumpResponse var1x = DumpProcessor.processDump(var3);
               Bukkit.getScheduler().runTask(DumpCreator.this.main, new Runnable() {
                  public void run() {
                     if (var1x.isSucces()) {
                        Bukkit.getPlayer(var1).sendMessage(ChatColor.DARK_GREEN + "[RealisticSeasons]" + ChatColor.GREEN + " Successfully uploaded server dump, your information is now available to the support team.");
                     } else {
                        Bukkit.getPlayer(var1).sendMessage(ChatColor.RED + "[RealisticSeasons] A problem occured while uploading your server dump: " + var1x.getMessage());
                     }

                  }
               });
            }
         });
      }
   }

   public void createConsoleDump(String var1) {
      final DumpInformation var2 = this.createDumpInformation();
      if (var2.isRateLimit()) {
         Bukkit.getLogger().info("[RealisticSeasons] Could not create dump, there needs to be atleast 30 seconds between attempts.");
      } else {
         var2.setKey(var1);
         final String var3 = generateString(new SecureRandom(), "abcdefghijklmnopqrstuvwxyz1234567890", 12);
         var2.setPassword(var3);
         var2.addEntry("log", LogGrabber.grabLatestLog());
         Bukkit.getScheduler().runTaskAsynchronously(this.main, new Runnable() {
            public void run() {
               var2.addEntry("log", LogGrabber.grabLatestLog());
               DumpResponse var1 = DumpProcessor.processDump(var2);
               if (var1.isSucces()) {
                  Bukkit.getLogger().info("[RealisticSeasons] Successfully uploaded server dump, your information is now available to the support team. In case you're using discord and want to view the page yourself, The link will be up in the discord channel soon. The password is " + var3 + ". You do not need to share this password, the support team can access your dump without it.");
               } else {
                  Bukkit.getLogger().info("[RealisticSeasons] A problem occured while uploading your server dump: " + var1.getMessage());
               }

            }
         });
      }
   }

   private DumpInformation createDumpInformation() {
      DumpInformation var1 = new DumpInformation();
      if (this.lastDump != null && System.currentTimeMillis() - this.lastDump < 30000L) {
         var1.addEntry("RATELIMIT", "RATELIMIT");
         var1.setRateLimit(true);
         return var1;
      } else {
         var1.setRateLimit(false);
         this.lastDump = System.currentTimeMillis();
         var1.addEntry("rs-version", this.main.getDescription().getVersion());
         var1.addEntry("java-version", System.getProperty("java.version"));
         var1.addEntry("os", System.getProperty("os.name"));
         String var2 = "";

         String var8;
         try {
            boolean var3 = true;
            Iterator var4 = Bukkit.getWorlds().iterator();

            label74:
            while(true) {
               while(true) {
                  if (!var4.hasNext()) {
                     break label74;
                  }

                  World var5 = (World)var4.next();
                  if (var3) {
                     var3 = false;
                  } else {
                     var2 = var2 + ", ";
                  }

                  String var6 = var5.getName();
                  Season var7 = this.main.getSeasonManager().getSeason(var5);
                  if (var7 != Season.DISABLED && var7 != Season.RESTORE) {
                     if (this.main.getSettings().calendarEnabled) {
                        var8 = this.main.getTimeManager().getDate(var5).toString();
                        var2 = var2 + var6 + "{season=" + var7.toString() + ", date=" + var8 + "}";
                     } else {
                        var2 = var2 + var6 + "{season=" + var7.toString() + "}";
                     }
                  } else {
                     var2 = var2 + var6 + "{season=" + var7.toString() + "}";
                  }
               }
            }
         } catch (Exception var11) {
            var2 = var11.getMessage();
         }

         var1.addEntry("worlds", var2);
         Object var12 = new ArrayList();

         try {
            var12 = this.main.getNMSUtils().getCustomBiomes(this.main.getSettings().biomeDisplayName);
         } catch (Exception var9) {
            ((List)var12).add(var9.getMessage());
         }

         var1.addEntry("custom-biomes", ((List)var12).toString());
         Plugin[] var13 = Bukkit.getPluginManager().getPlugins();
         String var14 = "";

         for(int var15 = 0; var15 < var13.length; ++var15) {
            if (var15 != 0) {
               var14 = var14 + ", ";
            }

            Plugin var17 = var13[var15];
            var14 = var14 + var17.getName() + "{version=" + var17.getDescription().getVersion() + "}";
         }

         var1.addEntry("plugins", var14);
         var1.addEntry("server-version", Bukkit.getName() + " " + Bukkit.getVersion());

         try {
            HashMap var16 = this.getFiles();
            Iterator var18 = var16.keySet().iterator();

            while(var18.hasNext()) {
               var8 = (String)var18.next();
               var1.addEntry("file-" + var8, (String)var16.get(var8));
            }
         } catch (Exception var10) {
            var1.addEntry("file-config", var10.getMessage());
         }

         return var1;
      }
   }

   private HashMap<String, String> getFiles() {
      HashMap var1 = new HashMap();
      File var2 = new File("plugins/RealisticSeasons/");
      File[] var3 = var2.listFiles();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         File var6 = var3[var5];
         if (var6.isDirectory()) {
            File[] var7 = var6.listFiles();
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               File var10 = var7[var9];
               if (var10.getName().contains(".yml")) {
                  try {
                     var1.put(var6.getName() + "/" + var10.getName().replaceAll(".yml", ""), this.readFile(var6.getName() + "/" + var10.getName(), StandardCharsets.UTF_8));
                  } catch (Exception var12) {
                     var1.put("config", var12.getMessage());
                  }
               }
            }
         } else if (var6.getName().contains(".yml") && !var6.getName().equalsIgnoreCase("chunkdata.yml")) {
            try {
               var1.put(var6.getName().replaceAll(".yml", ""), this.readFile(var6.getName(), StandardCharsets.UTF_8));
            } catch (Exception var13) {
               var1.put("config", var13.getMessage());
            }
         }
      }

      return var1;
   }

   private String readFile(String var1, Charset var2) {
      String var3 = this.main.getPlugin().getDataFolder().getAbsolutePath() + "/" + var1;
      byte[] var4 = Files.readAllBytes(Paths.get(var3));
      return new String(var4, var2);
   }

   public static String generateString(Random var0, String var1, int var2) {
      char[] var3 = new char[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         var3[var4] = var1.charAt(var0.nextInt(var1.length()));
      }

      return new String(var3);
   }
}
