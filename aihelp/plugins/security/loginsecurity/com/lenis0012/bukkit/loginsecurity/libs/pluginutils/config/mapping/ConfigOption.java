package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.mapping;

import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.AutoSavePolicy;
import org.bukkit.configuration.ConfigurationSection;

public class ConfigOption<T> {
   private final String path;
   private T value;
   private InternalMapper.SettingsHolder holder;

   public ConfigOption(String path) {
      this(path, (Object)null);
   }

   public ConfigOption(String path, T defaultValue) {
      this.path = path;
      this.value = defaultValue;
   }

   protected String getPath(String seperator) {
      return this.path;
   }

   protected void setHolder(InternalMapper.SettingsHolder holder) {
      this.holder = holder;
   }

   protected void loadFromConfig(ConfigurationSection section) {
      if (section.contains(this.path)) {
         this.value = section.get(this.path);
      }
   }

   protected void saveToConfig(ConfigurationSection section) {
      section.set(this.path, this.value);
   }

   public void set(T value) {
      this.value = value;
      if (this.holder != null && this.holder.getAutoSave() == AutoSavePolicy.ON_CHANGE) {
         this.holder.save();
      }

   }

   public T value() {
      return this.value;
   }
}
