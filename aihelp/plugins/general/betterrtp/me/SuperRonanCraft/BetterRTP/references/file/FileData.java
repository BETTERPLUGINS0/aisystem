package me.SuperRonanCraft.BetterRTP.references.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public interface FileData {
   YamlConfiguration getConfig();

   File getFile();

   String fileName();

   Plugin plugin();

   default String getString(String path) {
      return this.getConfig().isString(path) ? this.getConfig().getString(path) : "SOMETHING WENT WRONG";
   }

   default boolean exists(String path) {
      return this.getConfig().contains(path);
   }

   default boolean getBoolean(String path) {
      return this.getConfig().getBoolean(path);
   }

   default int getInt(String path) {
      return this.getConfig().getInt(path);
   }

   default long getLong(String path) {
      return this.getConfig().getLong(path);
   }

   default List<String> getStringList(String path) {
      return (List)(this.getConfig().isList(path) ? this.getConfig().getStringList(path) : new ArrayList());
   }

   default List<String> getList(String path) {
      List<String> list = new ArrayList();
      if (this.getConfig().isList(path)) {
         list.addAll(this.getStringList(path));
      } else {
         if (!this.getConfig().isString(path)) {
            return new ArrayList(Collections.singleton("&7The path &e" + path + " &7was not configured correctly!"));
         }

         list.add(this.getString(path));
      }

      return list;
   }

   default ConfigurationSection getConfigurationSection(String path) {
      return this.getConfig().getConfigurationSection(path);
   }

   default boolean isString(String path) {
      return this.getConfig().isString(path);
   }

   default boolean isList(String path) {
      return this.getConfig().isList(path);
   }

   default List<Map<?, ?>> getMapList(String path) {
      return this.getConfig().getMapList(path);
   }

   default void setValue(String path, Object value) {
      this.getConfig().set(path, value);
   }

   default void load() {
      YamlConfiguration config = this.getConfig();
      File file = this.getFile();
      if (!this.getFile().exists()) {
         this.plugin().saveResource(this.fileName(), false);

         try {
            config.load(file);
         } catch (Exception var4) {
            this.plugin().getLogger().info("File " + this.fileName() + " was unable to load!");
            var4.printStackTrace();
         }
      } else {
         try {
            config.load(file);
            InputStream in = this.plugin().getResource(this.fileName().replace(File.separator, "/"));
            if (in != null && in.available() > 0) {
               config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(in)));
               config.options().copyDefaults(true);
               in.close();
            } else {
               System.out.println("Input file was nulled " + this.fileName());
            }

            config.save(file);
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

   }

   default void save() {
      try {
         this.getConfig().save(this.getFile());
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }
}
