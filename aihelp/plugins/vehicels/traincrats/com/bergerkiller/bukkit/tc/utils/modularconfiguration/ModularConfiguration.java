package com.bergerkiller.bukkit.tc.utils.modularconfiguration;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.google.common.collect.MapMaker;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public abstract class ModularConfiguration<T> extends ModularConfigurationBlockList<T> implements ModularConfigurationEntry.Container<T> {
   protected final Logger logger;
   final ModularConfigurationEntryMap<T> entries = new ModularConfigurationEntryMap();
   final ConcurrentMap<String, ModularConfigurationEntry<T>> removedEntries = (new MapMaker()).weakValues().makeMap();

   public ModularConfiguration(Logger logger) {
      this.logger = logger;
   }

   public ModularConfiguration<T> getMain() {
      return this;
   }

   protected void preProcessModuleConfiguration(ConfigurationNode moduleConfig) {
   }

   protected void postProcessEntryConfiguration(ModularConfigurationEntry<T> entry) {
   }

   protected abstract T decodeConfig(ModularConfigurationEntry<T> var1);

   public abstract ModularConfigurationModule<T> getDefaultModule();

   void onModuleAdded(ModularConfigurationModule<T> module) {
      Iterator var2 = module.getAll().iterator();

      while(true) {
         while(var2.hasNext()) {
            ModularConfigurationEntry<T> newEntry = (ModularConfigurationEntry)var2.next();
            ModularConfigurationEntry<T> existing = this.entries.getIfExists(newEntry.getName());
            if (existing == null) {
               existing = (ModularConfigurationEntry)this.removedEntries.remove(newEntry.getName());
            }

            if (existing == null) {
               this.entries.set(newEntry.getName(), newEntry);
            } else if (!existing.isRemoved() && !this.isModuleOverriding(module, existing.getModule())) {
               int index;
               for(index = 0; index < existing.shadowModules.size() && this.isModuleOverriding(module, (ModularConfigurationModule)existing.shadowModules.get(index)); ++index) {
               }

               existing.shadowModules.add(index, module);
            } else {
               existing.loadFromModule(module);
            }
         }

         return;
      }
   }

   void onModuleRemoved(ModularConfigurationModule<T> module) {
      Iterator var2 = module.getNames().iterator();

      while(var2.hasNext()) {
         String name = (String)var2.next();
         ModularConfigurationEntry<T> e = this.getIfExists(name);
         if (e != null) {
            e.onModuleRemoved(module);
         }
      }

   }

   private boolean isModuleOverriding(ModularConfigurationModule<T> module, ModularConfigurationModule<T> otherModule) {
      Iterator var3 = this.blocks.iterator();

      while(var3.hasNext()) {
         ModularConfigurationBlock<T> block = (ModularConfigurationBlock)var3.next();
         Iterator var5 = block.getFiles().iterator();

         while(var5.hasNext()) {
            ModularConfigurationModule<T> existingModule = (ModularConfigurationModule)var5.next();
            if (existingModule == module) {
               return true;
            }

            if (existingModule == otherModule) {
               return false;
            }
         }
      }

      return false;
   }

   public void clear() {
      List<ModularConfigurationEntry<T>> newRemovedEntries = this.entries.getAll();
      newRemovedEntries.forEach(ModularConfigurationEntry::removeSilent);
      this.entries.clear();
      this.blocks.clear();
      newRemovedEntries.forEach((e) -> {
         this.removedEntries.put(e.getName(), e);
      });
   }

   public ModularConfigurationEntry<T> get(String name) {
      ModularConfigurationEntry<T> e = this.entries.getIfExists(name);
      return e != null ? e : (ModularConfigurationEntry)this.removedEntries.computeIfAbsent(name, (n) -> {
         return new ModularConfigurationEntry(this, n);
      });
   }

   public ModularConfigurationEntry<T> getIfExists(String name) {
      return this.entries.getIfExists(name);
   }

   public ModularConfigurationEntry<T> add(String name, ConfigurationNode config) {
      ModularConfigurationEntry entry = this.get(name);

      try {
         if (entry.isReadOnly()) {
            return this.getDefaultModule().add(name, config);
         } else {
            entry.setConfig(config);
            return entry;
         }
      } catch (ReadOnlyModuleException var5) {
         throw new IllegalStateException("Unexpected read-only module exception", var5);
      }
   }

   public boolean rename(String name, String newName) {
      if (name.equals(newName)) {
         return true;
      } else {
         ModularConfigurationEntry<T> entry = this.get(name);
         if (entry.isRemoved()) {
            return false;
         } else {
            ModularConfigurationEntry<T> target = this.get(newName);
            if (!entry.isReadOnly()) {
               target.createWithConfigInModule(entry.getConfig(), entry.getModule());
               entry.remove();
               return true;
            } else {
               if (target.isReadOnly()) {
                  target.createWithConfigInModule(entry.getConfig(), this.getDefaultModule());
               } else {
                  target.setConfig(entry.getConfig());
               }

               return true;
            }
         }
      }
   }

   public boolean isEmpty() {
      return this.entries.isEmpty();
   }

   public String getName() {
      return this.getDefaultModule().getName();
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
}
