package me.gypopo.economyshopgui.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.methodes.SendMessage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SkullCache {
   private final File file;
   private JSONObject skullCache;

   public SkullCache(EconomyShopGUI plugin) {
      this.file = new File(plugin.getDataFolder() + File.separator + "cache" + File.separator + "skullcache.json");

      try {
         if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdirs();
         }

         if (!this.file.exists()) {
            this.file.createNewFile();
         }

         if (this.file.length() == 0L) {
            OutputStream outputStream = new FileOutputStream(this.file);
            outputStream.write("{}".getBytes());
            outputStream.close();
         }
      } catch (IOException var8) {
         SendMessage.warnMessage("Failed to create 'skullcache.json' cache file");
         var8.printStackTrace();
      }

      try {
         FileReader reader = new FileReader(this.file);

         try {
            this.skullCache = (JSONObject)(new JSONParser()).parse(reader);
         } catch (Throwable var6) {
            try {
               reader.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         reader.close();
      } catch (Exception var7) {
         SendMessage.warnMessage("Failed to load skullcache from 'skullcache.json'");
         var7.printStackTrace();
      }

   }

   public String getSkull(String name) {
      return (String)this.skullCache.get(name);
   }

   public SkullCache.PlayerTexture getTexture(String name) {
      Object object = this.skullCache.get(name);
      return object == null ? null : new SkullCache.PlayerTexture((String)object);
   }

   public String cacheSkull(String name, String texture) {
      this.skullCache.put(name, texture);
      return texture;
   }

   public void write() {
      try {
         FileWriter file = new FileWriter(this.file);

         try {
            file.write(this.skullCache.toJSONString());
            file.flush();
         } catch (Throwable var5) {
            try {
               file.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }

            throw var5;
         }

         file.close();
      } catch (IOException var6) {
         SendMessage.warnMessage("Failed to save skull cache to 'skullcache.json'");
         var6.printStackTrace();
      }

   }

   public static class PlayerTexture {
      private final String texture;

      public PlayerTexture(String texture) {
         this.texture = texture;
      }

      public String getTexture() {
         return this.texture;
      }
   }
}
