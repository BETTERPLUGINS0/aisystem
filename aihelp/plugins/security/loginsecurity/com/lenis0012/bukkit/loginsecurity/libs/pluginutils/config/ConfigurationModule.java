package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config;

import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.mapping.InternalMapper;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules.ModularPlugin;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules.Module;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import org.bukkit.plugin.Plugin;

public class ConfigurationModule extends Module {
   private InternalMapper mapper;

   public ConfigurationModule(ModularPlugin plugin) {
      super(plugin);
   }

   public void enable() {
      this.mapper = new InternalMapper();
   }

   public void disable() {
      this.mapper.shutdown();
   }

   public CommentConfiguration getConfiguration(String fileName) {
      File file = new File(this.plugin.getDataFolder(), fileName);
      file.getParentFile().mkdirs();
      if (!file.exists()) {
         try {
            file.createNewFile();
         } catch (IOException var4) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to create config file", var4);
         }
      }

      return new CommentConfiguration(file);
   }

   public <T extends AbstractConfig> T createCustomConfig(Class<T> configClass) {
      Constructor constructor;
      try {
         constructor = configClass.getConstructor(Plugin.class);
         return (AbstractConfig)constructor.newInstance(this.plugin);
      } catch (Exception var4) {
         try {
            constructor = configClass.getConstructor(ConfigurationModule.class);
            return (AbstractConfig)constructor.newInstance(this);
         } catch (Exception var3) {
            throw new IllegalArgumentException("Invalid config class", var3);
         }
      }
   }

   public void registerSettings(Class<?> settingsClass) {
      this.registerSettings(settingsClass, "config.yml");
   }

   public void registerSettings(Class<?> settingsClass, String fileName) {
      this.registerSettings(settingsClass, fileName, AutoSavePolicy.DISABLED);
   }

   public void registerSettings(Class<?> settingsClass, String fileName, AutoSavePolicy autoSave) {
      this.mapper.registerSettingsClass(settingsClass, this.getConfiguration(fileName), autoSave);
   }

   public void reloadSettings(Class<?> settingsClass) {
      this.reloadSettings(settingsClass, true);
   }

   public void reloadSettings(Class<?> settingsClass, boolean writeDefaults) {
      this.mapper.loadSettings(settingsClass, writeDefaults);
   }

   public void saveSettings(Class<?> settingsClass) {
      this.mapper.saveSettings(settingsClass);
   }
}
