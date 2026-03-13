package com.ryandw11.structure.loottables.customitems;

import com.ryandw11.structure.CustomStructures;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class CustomItemManager {
   private FileConfiguration config;
   private File file;
   private CustomStructures structures;

   public CustomItemManager(CustomStructures structures, File file, File dir) {
      if (!dir.exists()) {
         dir.mkdir();
      }

      if (!file.exists()) {
         try {
            file.createNewFile();
         } catch (IOException var5) {
            structures.getLogger().severe("Cannot create Custom Items file. Enable debug mode for more information.");
            if (structures.isDebug()) {
               var5.printStackTrace();
            }

            return;
         }
      }

      this.file = file;
      this.config = YamlConfiguration.loadConfiguration(file);
      this.structures = structures;
   }

   public boolean addItem(String key, ItemStack itemStack) {
      if (this.config.contains(key)) {
         return false;
      } else {
         this.config.set(key, itemStack.clone());

         try {
            this.config.save(this.file);
            return true;
         } catch (IOException var4) {
            this.structures.getLogger().severe("Failed to save Custom Items file after adding an item.");
            if (this.structures.isDebug()) {
               var4.printStackTrace();
            }

            return false;
         }
      }
   }

   public boolean removeItem(String key) {
      if (!this.config.contains(key)) {
         return false;
      } else {
         this.config.set(key, (Object)null);

         try {
            this.config.save(this.file);
            return true;
         } catch (IOException var3) {
            this.structures.getLogger().severe("Failed to save Custom Items file after removing an item.");
            if (this.structures.isDebug()) {
               var3.printStackTrace();
            }

            return false;
         }
      }
   }

   public ItemStack getItem(String key) {
      return !this.config.contains(key) ? null : this.config.getItemStack(key);
   }

   public FileConfiguration getConfig() {
      return this.config;
   }
}
