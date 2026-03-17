package com.bergerkiller.bukkit.tc.utils.modularconfiguration;

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import java.io.File;
import java.util.Locale;

public class ModularConfigurationFile<T> extends ModularConfigurationModule<T> {
   ModularConfigurationFile(ModularConfiguration<T> main, File file) {
      this(main, decodeModuleNameFromFile(file), file, !file.canWrite());
   }

   ModularConfigurationFile(ModularConfiguration<T> main, String name, File file, boolean readOnly) {
      super(main, name, new FileConfiguration(file), readOnly);
   }

   protected void loadConfig() {
      ((FileConfiguration)this.config).load();
      super.loadConfig();
   }

   public void reload() {
      if (this.configChanged) {
         this.saveChanges();
      } else {
         this.main.onModuleRemoved(this);
         this.loadConfig();
         this.main.onModuleAdded(this);
      }
   }

   public void saveChanges() {
      if (this.configChanged) {
         if (!this.isReadOnly()) {
            ((FileConfiguration)this.config).save();
         }

         this.configChanged = false;
      }

   }

   public void save() {
      if (!this.isReadOnly()) {
         ((FileConfiguration)this.config).save();
      }

      this.configChanged = false;
   }

   static String decodeModuleNameFromFile(File file) {
      String name = file.getName();
      if (name.indexOf(".") > 0) {
         name = name.substring(0, name.lastIndexOf("."));
      }

      name = name.toLowerCase(Locale.ENGLISH);
      return name;
   }
}
