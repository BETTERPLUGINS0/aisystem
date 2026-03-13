package me.casperge.realisticseasons.data.chunksaver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.season.SeasonChunk;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class AsyncDataSaver {
   private List<SeasonChunk> saveQueue = new ArrayList();
   private List<SeasonChunk> deleteQueue = new ArrayList();
   File dataFile;
   RealisticSeasons main;
   YamlConfiguration data;

   public AsyncDataSaver(RealisticSeasons var1) {
      this.dataFile = new File(var1.getDataFolder(), "chunkdata.yml");
      this.main = var1;
      if (!this.dataFile.exists()) {
         try {
            InputStream var2 = var1.getResource("chunkdata.yml");
            FileUtils.copyInputStreamToFile(var2, this.dataFile);
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

      this.data = YamlConfiguration.loadConfiguration(this.dataFile);
   }

   public void tick() {
      Iterator var2;
      SeasonChunk var3;
      synchronized(this.saveQueue) {
         if (!this.saveQueue.isEmpty()) {
            var2 = this.saveQueue.iterator();

            while(var2.hasNext()) {
               var3 = (SeasonChunk)var2.next();
               this.save(var3);
            }

            this.saveQueue.clear();
         }
      }

      synchronized(this.deleteQueue) {
         if (!this.deleteQueue.isEmpty()) {
            var2 = this.deleteQueue.iterator();

            while(var2.hasNext()) {
               var3 = (SeasonChunk)var2.next();
               this.delete(var3);
            }

            this.deleteQueue.clear();
         }
      }

      try {
         this.data.save(this.dataFile);
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   private void save(SeasonChunk var1) {
      if (var1 != null) {
         this.data.set("chunkdata." + var1.getWorldName() + "." + var1.getX() + "." + var1.getZ(), "1");
      }

   }

   private void delete(SeasonChunk var1) {
      if (var1 != null) {
         this.data.set("chunkdata." + var1.getWorldName() + "." + var1.getX() + "." + var1.getZ(), (Object)null);
      }

   }

   public SeasonChunk getRandomChunk(String var1) {
      SeasonChunk var2 = null;
      ConfigurationSection var3 = this.data.getConfigurationSection("chunkdata." + var1);
      if (var3 != null && var3.getKeys(false).size() != 0) {
         ConfigurationSection var4 = this.data.getConfigurationSection("chunkdata." + var1 + "." + var3.getKeys(false).toArray()[0]);
         if (var4 != null) {
            if (var4.getKeys(false).size() != 0) {
               String var5 = var4.getCurrentPath() + "." + var4.getKeys(false).toArray()[0];
               if (this.data.isString(var5)) {
                  String[] var6 = var5.split("\\.");
                  if (var6.length == 4) {
                     String var7 = var6[2];
                     String var8 = var6[3];
                     Integer var9 = Integer.valueOf(var7);
                     Integer var10 = Integer.valueOf(var8);
                     if (var9 != null && var10 != null) {
                        var2 = new SeasonChunk(var1, var9, var10, 0L);
                     }
                  }
               }
            } else {
               this.data.set(var4.getCurrentPath(), (Object)null);
            }
         }
      }

      return var2;
   }

   public void saveChunk(SeasonChunk var1) {
      synchronized(this.saveQueue) {
         this.saveQueue.add(var1);
      }
   }

   public void deleteChunk(SeasonChunk var1) {
      synchronized(this.deleteQueue) {
         this.deleteQueue.add(var1);
      }
   }

   public Set<String> getWorlds() {
      Set var1 = null;
      ConfigurationSection var2 = this.data.getConfigurationSection("chunkdata");
      if (var2 != null) {
         var1 = this.data.getConfigurationSection("chunkdata").getKeys(false);
      }

      return var1;
   }
}
