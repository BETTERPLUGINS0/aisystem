package com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config;

import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.mapping.ConfigHeader;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.mapping.ConfigKey;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.mapping.ConfigMapper;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.mapping.ConfigSection;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class AbstractConfig {
   private final Map<Class<?>, List<Field>> dataFields = new HashMap();
   private final ConfigMapper mapper;
   private final CommentConfiguration config;
   private final Logger logger;
   private boolean clearOnSave = false;

   /** @deprecated */
   @Deprecated
   protected AbstractConfig(ConfigurationModule module) {
      this.mapper = (ConfigMapper)this.getClass().getAnnotation(ConfigMapper.class);
      this.config = module.getConfiguration(this.mapper.fileName());
      this.logger = module.logger();
      this.loadSectionKeys(this.getClass(), "");
   }

   protected AbstractConfig(@NonNull Plugin plugin) {
      if (plugin == null) {
         throw new NullPointerException("plugin is marked non-null but is null");
      } else {
         this.mapper = (ConfigMapper)this.getClass().getAnnotation(ConfigMapper.class);
         this.config = new CommentConfiguration(new File(plugin.getDataFolder(), this.mapper.fileName()));
         this.logger = plugin.getLogger();
         this.loadSectionKeys(this.getClass(), "");
      }
   }

   private void loadSectionKeys(Class<?> source, String basePath) {
      List<Field> dataFields = new ArrayList();
      Field[] var4 = source.getDeclaredFields();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Field field = var4[var6];
         ConfigKey key = (ConfigKey)field.getAnnotation(ConfigKey.class);
         if (key != null) {
            String keyPath = key.path().isEmpty() ? this.toConfigString(field.getName()) : key.path();
            ConfigHeader header = (ConfigHeader)field.getAnnotation(ConfigHeader.class);
            if (header != null) {
               String path = header.path().isEmpty() ? keyPath : header.path();
               this.config.header(basePath + path, header.value());
            }

            if (field.getType().isAnnotationPresent(ConfigSection.class)) {
               this.loadSectionKeys(field.getType(), basePath + keyPath + ".");
            }

            field.setAccessible(true);
            dataFields.add(field);
         }
      }

      this.dataFields.put(source, dataFields);
   }

   protected boolean isClearOnSave() {
      return this.clearOnSave;
   }

   protected void setClearOnSave(boolean flag) {
      this.clearOnSave = flag;
   }

   public void reload() {
      this.config.reload();
      if (this.mapper.header().length > 0) {
         this.config.mainHeader(this.mapper.header());
      }

      this.reloadSection(this.config, this);
   }

   private void reloadSection(ConfigurationSection source, Object target) {
      try {
         Iterator var3 = ((List)this.dataFields.get(target.getClass())).iterator();

         while(true) {
            while(true) {
               Field field;
               String path;
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  field = (Field)var3.next();
                  ConfigKey key = (ConfigKey)field.getAnnotation(ConfigKey.class);
                  path = key.path().isEmpty() ? this.toConfigString(field.getName()) : key.path();
               } while(!this.config.contains(path));

               Object value = source.get(path);
               if (value instanceof ConfigurationSection && field.getType().isAnnotationPresent(ConfigSection.class)) {
                  try {
                     Object result = field.getType().getDeclaredConstructor().newInstance();
                     this.reloadSection((ConfigurationSection)value, result);
                     field.set(target, result);
                  } catch (Exception var9) {
                     Bukkit.getLogger().log(Level.WARNING, "Failed to load property \"" + path + "\" from " + this.mapper.fileName());
                  }
               } else {
                  field.set(target, value);
               }
            }
         }
      } catch (Throwable var10) {
         throw var10;
      }
   }

   public void save() {
      this.saveSection(this.config, this);
      this.config.save();
   }

   private void saveSection(ConfigurationSection target, Object source) {
      try {
         Iterator var3 = ((List)this.dataFields.get(source.getClass())).iterator();

         while(var3.hasNext()) {
            Field field = (Field)var3.next();
            ConfigKey key = (ConfigKey)field.getAnnotation(ConfigKey.class);
            String path = key.path().isEmpty() ? this.toConfigString(field.getName()) : key.path();
            if (field.getType().isAnnotationPresent(ConfigSection.class)) {
               ConfigurationSection section = target.getConfigurationSection(path);
               if (section == null) {
                  section = target.createSection(path);
               }

               Object sourceValue = field.get(source);
               this.saveSection(section, sourceValue);
            } else {
               target.set(path, field.get(source));
            }
         }

      } catch (Throwable var9) {
         throw var9;
      }
   }

   private String toConfigString(String value) {
      StringBuilder builder = new StringBuilder();

      for(int i = 0; i < value.length(); ++i) {
         char c = value.charAt(i);
         if (Character.isUpperCase(c)) {
            builder.append('-').append(Character.toLowerCase(c));
         } else {
            builder.append(c);
         }
      }

      return builder.toString();
   }
}
