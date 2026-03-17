package com.bergerkiller.bukkit.tc.utils.modularconfiguration;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class ModularConfigurationEntryMap<T> implements ModularConfigurationEntry.Container<T> {
   private final HashMap<String, ModularConfigurationEntry<T>> entries = new HashMap();
   private List<ModularConfigurationEntry<T>> entriesList = Collections.emptyList();
   private List<String> entryNamesList = Collections.emptyList();
   private List<T> entryValuesList = Collections.emptyList();

   public void clear() {
      this.entries.clear();
      this.entriesList = Collections.emptyList();
      this.entryNamesList = Collections.emptyList();
      this.entryValuesList = Collections.emptyList();
   }

   public void set(String name, ModularConfigurationEntry<T> entry) {
      this.entries.put(name, entry);
      this.regenSortedLists();
   }

   public ModularConfigurationEntry<T> remove(String name) {
      ModularConfigurationEntry<T> entry = (ModularConfigurationEntry)this.entries.remove(name);
      if (entry != null) {
         this.regenSortedLists();
      }

      return entry;
   }

   public String getName() {
      throw new UnsupportedOperationException();
   }

   public ModularConfigurationEntry<T> getIfExists(String name) {
      return (ModularConfigurationEntry)this.entries.get(name);
   }

   public ModularConfigurationEntry<T> add(String name, ConfigurationNode initialConfig) throws ReadOnlyModuleException {
      throw new UnsupportedOperationException();
   }

   public boolean rename(String name, String newName) {
      throw new UnsupportedOperationException();
   }

   public boolean isEmpty() {
      return this.entries.isEmpty();
   }

   public List<String> getNames() {
      List<String> result = this.entryNamesList;
      if (result == null) {
         List<ModularConfigurationEntry<T>> entries = this.getAll();
         List<String> result = new ArrayList(entries.size());
         Iterator var3 = entries.iterator();

         while(var3.hasNext()) {
            ModularConfigurationEntry<T> entry = (ModularConfigurationEntry)var3.next();
            result.add(entry.getName());
         }

         this.entryNamesList = result = Collections.unmodifiableList(result);
      }

      return result;
   }

   public List<ModularConfigurationEntry<T>> getAll() {
      List<ModularConfigurationEntry<T>> result = this.entriesList;
      if (result == null) {
         List<ModularConfigurationEntry<T>> result = new ArrayList(this.entries.values());
         Collections.sort(result);
         this.entriesList = result = Collections.unmodifiableList(result);
      }

      return result;
   }

   public List<T> getAllValues() {
      List<T> result = this.entryValuesList;
      if (result == null) {
         List<ModularConfigurationEntry<T>> allEntries = this.getAll();
         List<T> result = new ArrayList(allEntries.size());
         Iterator var3 = allEntries.iterator();

         while(var3.hasNext()) {
            ModularConfigurationEntry<T> entry = (ModularConfigurationEntry)var3.next();
            result.add(entry.get());
         }

         this.entryValuesList = result = Collections.unmodifiableList(result);
      }

      return result;
   }

   private void regenSortedLists() {
      this.entriesList = null;
      this.entryNamesList = null;
      this.entryValuesList = null;
   }
}
