package com.bergerkiller.bukkit.tc.utils.modularconfiguration;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ModularConfigurationModule<T> implements ModularConfigurationBlock<T>, ModularConfigurationEntry.Container<T>, Comparable<ModularConfigurationModule<T>> {
   protected final ModularConfiguration<T> main;
   private final ModularConfigurationEntryMap<T> entries;
   protected final String name;
   final ConfigurationNode config;
   private final boolean readOnly;
   boolean configChanged;

   ModularConfigurationModule(ModularConfiguration<T> main, String name, ConfigurationNode config, boolean readOnly) {
      this.main = main;
      this.entries = new ModularConfigurationEntryMap();
      this.name = name;
      this.config = config;
      this.readOnly = readOnly;
      if (!readOnly) {
         this.config.addChangeListener((p) -> {
            this.configChanged = true;
         });
      }

      this.loadConfig();
   }

   protected void loadConfig() {
      this.configChanged = false;
      this.main.preProcessModuleConfiguration(this.config);
      this.saveChanges();
      this.entries.clear();
      Iterator var1 = this.config.getNodes().iterator();

      while(var1.hasNext()) {
         ConfigurationNode nodeConfig = (ConfigurationNode)var1.next();
         this.entries.set(nodeConfig.getName(), new ModularConfigurationEntry(this.main, nodeConfig.getName(), nodeConfig, this));
      }

      this.configChanged = false;
   }

   void removeInModule(String name) {
      this.entries.remove(name);
      this.config.remove(name);
   }

   void store(ModularConfigurationEntry<T> entry) {
      entry.module = this;
      this.entries.set(entry.getName(), entry);
      this.config.set(entry.getName(), entry.getConfig());
      this.configChanged = true;
   }

   public String getName() {
      return this.name;
   }

   public boolean isReadOnly() {
      return this.readOnly;
   }

   public ModularConfiguration<T> getMain() {
      return this.main;
   }

   public List<? extends ModularConfigurationModule<T>> getFiles() {
      return Collections.singletonList(this);
   }

   public void reload() {
   }

   public void saveChanges() {
      this.configChanged = false;
   }

   public void save() {
      this.configChanged = false;
   }

   public ModularConfigurationEntry<T> add(String name, ConfigurationNode initialConfig) throws ReadOnlyModuleException {
      if (name != null && !name.isEmpty()) {
         if (initialConfig == null) {
            throw new IllegalArgumentException("Initial configuration is null");
         } else {
            ModularConfigurationEntry<T> entry = this.main.get(name);
            entry.createWithConfigInModule(initialConfig, this);
            return entry;
         }
      } else {
         throw new IllegalArgumentException("Name is null or empty");
      }
   }

   public boolean rename(String name, String newName) {
      if (name.equals(newName)) {
         return true;
      } else if (this.isReadOnly()) {
         return false;
      } else {
         ModularConfigurationEntry<T> entry = this.main.getIfExists(name);
         if (!entry.isRemoved() && entry.getModule() == this) {
            this.add(newName, entry.getConfig());
            return true;
         } else {
            return false;
         }
      }
   }

   public ModularConfigurationEntry<T> getIfExists(String name) {
      return this.entries.getIfExists(name);
   }

   public boolean isEmpty() {
      return this.entries.isEmpty();
   }

   public List<String> getNames() {
      return this.entries.getNames();
   }

   public List<ModularConfigurationEntry<T>> getAll() {
      return this.entries.getAll();
   }

   public List<T> getAllValues() {
      return this.entries.getAllValues();
   }

   public int compareTo(ModularConfigurationModule<T> tModularConfigurationFile) {
      if (this.readOnly != tModularConfigurationFile.readOnly) {
         return this.readOnly ? 1 : -1;
      } else {
         return this.name.compareTo(tModularConfigurationFile.name);
      }
   }
}
