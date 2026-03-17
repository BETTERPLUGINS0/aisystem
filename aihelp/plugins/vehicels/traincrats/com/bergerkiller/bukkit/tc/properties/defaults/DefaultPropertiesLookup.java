package com.bergerkiller.bukkit.tc.properties.defaults;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import com.bergerkiller.bukkit.tc.properties.api.IProperty;
import com.bergerkiller.bukkit.tc.properties.api.IPropertyRegistry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import org.bukkit.entity.Player;

public class DefaultPropertiesLookup {
   private static final String defaultPropertiesFile = "DefaultTrainProperties.yml";
   private final FileConfiguration config;
   private final Map<String, DefaultPropertiesLookup.CachedDefaultProperties> defaultPropertiesByName;
   private final List<DefaultPropertiesLookup.CachedDefaultProperties> namedDefaults;
   private final DefaultPropertiesLookup.CachedDefaultProperties defaultProperties;
   private Collection<IProperty<Object>> allPropertiesAtTimeOfCaching;

   private DefaultPropertiesLookup(FileConfiguration config) {
      this.config = config;
      this.defaultPropertiesByName = new HashMap(config.getNodes().size());
      Iterator var2 = config.getNodes().iterator();

      while(var2.hasNext()) {
         ConfigurationNode node = (ConfigurationNode)var2.next();
         this.defaultPropertiesByName.put(node.getName(), new DefaultPropertiesLookup.CachedDefaultProperties(node));
      }

      this.defaultProperties = (DefaultPropertiesLookup.CachedDefaultProperties)this.defaultPropertiesByName.get("default");
      if (this.defaultProperties == null) {
         throw new IllegalStateException("No default configuration is included");
      } else {
         this.namedDefaults = new ArrayList(this.defaultPropertiesByName.size());
         var2 = this.defaultPropertiesByName.values().iterator();

         while(var2.hasNext()) {
            DefaultPropertiesLookup.CachedDefaultProperties props = (DefaultPropertiesLookup.CachedDefaultProperties)var2.next();
            if (!LogicUtil.contains(props.name(), new String[]{"default", "spawner"})) {
               this.namedDefaults.add(props);
            }
         }

         this.namedDefaults.sort(Comparator.comparing(DefaultPropertiesLookup.CachedDefaultProperties::name));
         this.allPropertiesAtTimeOfCaching = IPropertyRegistry.instance().all();
      }
   }

   private void invalidateIfPropertiesChanged() {
      Collection<IProperty<Object>> all = IPropertyRegistry.instance().all();
      if (this.allPropertiesAtTimeOfCaching != all) {
         this.allPropertiesAtTimeOfCaching = all;
         this.defaultPropertiesByName.values().forEach(DefaultPropertiesLookup.CachedDefaultProperties::invalidate);
      }

   }

   public DefaultProperties getByName(String name) {
      this.invalidateIfPropertiesChanged();
      DefaultPropertiesLookup.CachedDefaultProperties props = (DefaultPropertiesLookup.CachedDefaultProperties)this.defaultPropertiesByName.get(name);
      return props == null ? null : props.get();
   }

   public DefaultProperties getForPlayer(Player player) {
      this.invalidateIfPropertiesChanged();
      Iterator var2 = this.namedDefaults.iterator();

      DefaultPropertiesLookup.CachedDefaultProperties props;
      do {
         if (!var2.hasNext()) {
            return this.defaultProperties.get();
         }

         props = (DefaultPropertiesLookup.CachedDefaultProperties)var2.next();
      } while(!props.hasPermission(player));

      return props.get();
   }

   public static DefaultPropertiesLookup load(TrainCarts traincarts) {
      FileConfiguration defconfig = new FileConfiguration(traincarts, "DefaultTrainProperties.yml");
      defconfig.load();
      boolean changed = false;
      ConfigurationNode node;
      Iterator var4;
      if (!defconfig.contains("default")) {
         node = defconfig.getNode("default");
         var4 = IPropertyRegistry.instance().all().iterator();

         while(var4.hasNext()) {
            IProperty<Object> property = (IProperty)var4.next();
            if (property.isAppliedAsDefault()) {
               Object value = property.getDefault();
               if (value != null) {
                  property.writeToConfig(node, Optional.of(value));
               }
            }
         }

         node.set("blockTypes", "");
         node.set("blockOffset", "unset");
         changed = true;
      }

      Entry entry;
      if (!defconfig.contains("admin")) {
         node = defconfig.getNode("admin");
         var4 = defconfig.getNode("default").getValues().entrySet().iterator();

         while(var4.hasNext()) {
            entry = (Entry)var4.next();
            node.set((String)entry.getKey(), entry.getValue());
         }

         changed = true;
      }

      if (!defconfig.contains("spawner")) {
         node = defconfig.getNode("spawner");
         var4 = defconfig.getNode("default").getValues().entrySet().iterator();

         while(var4.hasNext()) {
            entry = (Entry)var4.next();
            node.set((String)entry.getKey(), entry.getValue());
         }

         changed = true;
      }

      if (TrainPropertiesStore.fixDeprecation(defconfig)) {
         changed = true;
      }

      if (changed) {
         defconfig.save();
      }

      return new DefaultPropertiesLookup(defconfig);
   }

   private static class CachedDefaultProperties {
      private final ConfigurationNode config;
      private final String permNode;
      private DefaultProperties cachedProperties;

      public CachedDefaultProperties(ConfigurationNode config) {
         this.config = config;
         this.permNode = "train.properties." + config.getName();
         this.cachedProperties = null;
      }

      public String name() {
         return this.config.getName();
      }

      public boolean hasPermission(Player player) {
         return CommonUtil.hasPermission(player, this.permNode);
      }

      public void invalidate() {
         this.cachedProperties = null;
      }

      public DefaultProperties get() {
         DefaultProperties cached = this.cachedProperties;
         if (cached == null) {
            this.cachedProperties = cached = DefaultProperties.of(this.config);
         }

         return cached;
      }
   }
}
