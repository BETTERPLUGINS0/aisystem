package me.casperge.realisticseasons.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageManager {
   public static HashMap<MessageType, String> messages = new HashMap();
   private FileConfiguration translations;
   private File languageFile;
   private RealisticSeasons main;

   public LanguageManager(RealisticSeasons var1) {
      this.main = var1;
      this.languageFile = new File(var1.getDataFolder(), "lang.yml");
      if (!this.languageFile.exists()) {
         try {
            InputStream var2 = var1.getResource("lang.yml");
            FileUtils.copyInputStreamToFile(var2, this.languageFile);
         } catch (IOException var12) {
            var12.printStackTrace();
         }
      }

      this.copyDefaults();
      this.translations = YamlConfiguration.loadConfiguration(this.languageFile);
      Iterator var13 = this.translations.getKeys(false).iterator();

      while(var13.hasNext()) {
         String var3 = (String)var13.next();
         if (this.translations.isString(var3)) {
            messages.put(MessageType.getMessageType(var3), this.translations.getString(var3));
         }
      }

      String[] var14 = new String[]{"SUMMER", "FALL", "WINTER", "SPRING"};
      String[] var15 = var14;
      int var4 = var14.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var15[var5];
         if (this.translations.isList("SEASON-TO-" + var6)) {
            List var7 = this.translations.getStringList("SEASON-TO-" + var6);
            String var8 = "";
            boolean var9 = true;
            Iterator var10 = var7.iterator();

            while(var10.hasNext()) {
               String var11 = (String)var10.next();
               if (var9) {
                  var8 = var11;
                  var9 = false;
               } else {
                  var8 = var8 + "ngtjrd" + var11;
               }
            }

            messages.put(MessageType.getMessageType("SEASON-TO-" + var6), var8);
         }
      }

   }

   public void reload() {
      this.languageFile = new File(this.main.getDataFolder(), "lang.yml");
      if (!this.languageFile.exists()) {
         try {
            InputStream var1 = this.main.getResource("lang.yml");
            FileUtils.copyInputStreamToFile(var1, this.languageFile);
         } catch (IOException var11) {
            var11.printStackTrace();
         }
      }

      messages.clear();
      this.copyDefaults();
      this.translations = YamlConfiguration.loadConfiguration(this.languageFile);
      Iterator var12 = this.translations.getKeys(false).iterator();

      while(var12.hasNext()) {
         String var2 = (String)var12.next();
         if (this.translations.isString(var2)) {
            messages.put(MessageType.getMessageType(var2), this.translations.getString(var2));
         }
      }

      String[] var13 = new String[]{"SUMMER", "FALL", "WINTER", "SPRING"};
      String[] var14 = var13;
      int var3 = var13.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var14[var4];
         if (this.translations.isList("SEASON-TO-" + var5)) {
            List var6 = this.translations.getStringList("SEASON-TO-" + var5);
            String var7 = "";
            boolean var8 = true;
            Iterator var9 = var6.iterator();

            while(var9.hasNext()) {
               String var10 = (String)var9.next();
               if (var8) {
                  var7 = var10;
                  var8 = false;
               } else {
                  var7 = var7 + "ngtjrd" + var10;
               }
            }

            messages.put(MessageType.getMessageType("SEASON-TO-" + var5), var7);
         }
      }

   }

   public void copyDefaults() {
      JavaUtils.saveDefaultConfigValues("/lang.yml", "lang.yml");
   }
}
