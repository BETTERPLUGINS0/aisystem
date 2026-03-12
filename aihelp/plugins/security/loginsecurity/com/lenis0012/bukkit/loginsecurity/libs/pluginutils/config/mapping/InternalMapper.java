package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.mapping;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.AutoSavePolicy;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.CommentConfiguration;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InternalMapper {
   private final Map<Class<?>, InternalMapper.SettingsHolder> holders = Maps.newConcurrentMap();

   public void registerSettingsClass(Class<?> settingsClass, CommentConfiguration config, AutoSavePolicy autoSave) {
      InternalMapper.SettingsHolder holder = new InternalMapper.SettingsHolder(config, autoSave, settingsClass);
      this.holders.put(settingsClass, holder);
   }

   public void loadSettings(Class<?> byClass, boolean writeDefaults) {
      InternalMapper.SettingsHolder holder = (InternalMapper.SettingsHolder)this.holders.get(byClass);
      if (holder == null) {
         throw new IllegalArgumentException("Specified settings not registered");
      } else {
         holder.load(writeDefaults);
      }
   }

   public void saveSettings(Class<?> byClass) {
      InternalMapper.SettingsHolder holder = (InternalMapper.SettingsHolder)this.holders.get(byClass);
      if (holder == null) {
         throw new IllegalArgumentException("Specified settings not registered");
      } else {
         holder.save();
      }
   }

   public void shutdown() {
      Iterator var1 = this.holders.values().iterator();

      while(var1.hasNext()) {
         InternalMapper.SettingsHolder holder = (InternalMapper.SettingsHolder)var1.next();
         if (holder.getAutoSave() == AutoSavePolicy.ON_SHUTDOWN) {
            holder.save();
         }
      }

   }

   protected static class SettingsHolder {
      private List<ConfigOption> options = Lists.newArrayList();
      private final CommentConfiguration config;
      private final AutoSavePolicy autoSave;

      public SettingsHolder(CommentConfiguration config, AutoSavePolicy autoSave, Class<?> settingsClass) {
         this.config = config;
         this.autoSave = autoSave;
         this.registerOptions(settingsClass);
      }

      public AutoSavePolicy getAutoSave() {
         return this.autoSave;
      }

      private void registerOptions(Class<?> settingsClass) {
         try {
            String seperator = Character.toString(this.config.options().pathSeparator());
            Field[] var3 = settingsClass.getFields();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Field field = var3[var5];
               if (ConfigOption.class.isAssignableFrom(field.getType())) {
                  ConfigOption<?> value = (ConfigOption)field.get((Object)null);
                  if (value != null) {
                     value.setHolder(this);
                     this.options.add(value);
                     ConfigHeader header = (ConfigHeader)field.getAnnotation(ConfigHeader.class);
                     if (header != null) {
                        String path = header.path().isEmpty() ? value.getPath(seperator) : header.path();
                        this.config.header(path, header.value());
                     }
                  }
               }
            }

         } catch (Throwable var10) {
            throw var10;
         }
      }

      public void load(boolean writeDefaults) {
         this.config.reload();
         Iterator var2 = this.options.iterator();

         while(var2.hasNext()) {
            ConfigOption<?> option = (ConfigOption)var2.next();
            option.loadFromConfig(this.config);
         }

         if (writeDefaults) {
            var2 = this.config.getKeys(false).iterator();

            while(var2.hasNext()) {
               String key = (String)var2.next();
               this.config.set(key, (Object)null);
            }

            this.save();
         }

      }

      public void save() {
         Iterator var1 = this.options.iterator();

         while(var1.hasNext()) {
            ConfigOption<?> option = (ConfigOption)var1.next();
            option.saveToConfig(this.config);
         }

         this.config.save();
      }
   }
}
