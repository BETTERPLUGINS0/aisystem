package com.nisovin.shopkeepers.util.data.container;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.bukkit.ConfigUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ConfigBasedDataContainer extends AbstractDataContainer {
   private final ConfigurationSection config;

   public ConfigBasedDataContainer(ConfigurationSection config) {
      Validate.notNull(config, (String)"config is null");
      this.config = config;
   }

   public ConfigurationSection getConfig() {
      return this.config;
   }

   @Nullable
   public Object getOrDefault(String key, @Nullable Object defaultValue) {
      Validate.notEmpty(key, "key is empty");
      Object value = this.config.get(key, (Object)null);
      return value != null ? value : defaultValue;
   }

   protected void internalSet(String key, Object value) {
      this.config.set(key, value);
   }

   public void remove(String key) {
      this.config.set(key, (Object)null);
   }

   public void clear() {
      ConfigUtils.clearConfigSection(this.config);
   }

   public int size() {
      return this.getKeys().size();
   }

   public Set<? extends String> getKeys() {
      return (Set)Unsafe.cast(this.config.getKeys(false));
   }

   public Map<? extends String, ?> getValues() {
      return this.getValuesCopy();
   }

   public Map<String, Object> getValuesCopy() {
      return ConfigUtils.getValues(this.config);
   }

   @Nullable
   public Object serialize() {
      return this.config;
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("ConfigBasedDataContainer [config=");
      builder.append(this.getValues());
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      return this.getValues().hashCode();
   }

   public boolean equals(@Nullable Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof DataContainer)) {
         return false;
      } else {
         DataContainer otherContainer = (DataContainer)obj;
         return this.getValues().equals(otherContainer.getValues());
      }
   }
}
