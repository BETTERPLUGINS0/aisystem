package com.ryandw11.structure.api.structaddon;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.loottables.ConfigLootItem;
import com.ryandw11.structure.loottables.LootTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.bukkit.plugin.Plugin;

public final class CustomStructureAddon {
   private final String name;
   private final List<String> authors;
   private final Set<StructureSectionProvider> providerSet = new HashSet();
   private final List<Class<? extends StructureSection>> structureSections;
   private final List<LootTable> lootTables;
   private final Map<String, Class<? extends ConfigLootItem>> lootItems;

   public CustomStructureAddon(Plugin plugin) {
      if (plugin == CustomStructures.getInstance()) {
         throw new IllegalArgumentException("Cannot add CustomStructures as an addon.");
      } else {
         this.name = plugin.getName();
         if (!this.name.equalsIgnoreCase("CustomStructures") && !this.name.equalsIgnoreCase("CustomStructure")) {
            this.structureSections = new ArrayList();
            this.lootTables = new ArrayList();
            this.lootItems = new HashMap();
            this.authors = plugin.getDescription().getAuthors();
         } else {
            throw new IllegalArgumentException("Addon name cannot be the same as the plugin.");
         }
      }
   }

   public String getName() {
      return this.name;
   }

   public List<String> getAuthors() {
      return this.authors;
   }

   public void addStructureSection(Class<? extends StructureSection> structureSection) {
      this.structureSections.add(structureSection);
   }

   public void registerStructureSectionProvider(StructureSectionProvider provider) {
      this.providerSet.add(provider);
   }

   public void unregisterStructureSectionProvider(StructureSectionProvider provider) {
      this.providerSet.remove(provider);
   }

   public List<Class<? extends StructureSection>> getStructureSections() {
      return this.structureSections;
   }

   public Set<StructureSectionProvider> getProviderSet() {
      return Collections.unmodifiableSet(this.providerSet);
   }

   public boolean registerStructureSign(String name, Class<? extends StructureSign> structureSign) {
      return CustomStructures.getInstance().getStructureSignHandler().registerStructureSign(name, structureSign);
   }

   public void registerLootTable(LootTable lootTable) {
      CustomStructures.getInstance().getLootTableHandler().addLootTable(lootTable);
      this.lootTables.add(lootTable);
   }

   public void registerLootItem(String typeName, Class<? extends ConfigLootItem> lootItemClass) {
      CustomStructures.getInstance().getLootTableHandler().addLootItem(typeName, lootItemClass);
      this.lootItems.put(typeName, lootItemClass);
   }

   public void handlePluginReload() {
      Iterator var1 = this.lootTables.iterator();

      while(var1.hasNext()) {
         LootTable lootTable = (LootTable)var1.next();
         CustomStructures.getInstance().getLootTableHandler().addLootTable(lootTable);
      }

      var1 = this.lootItems.entrySet().iterator();

      while(var1.hasNext()) {
         Entry<String, Class<? extends ConfigLootItem>> itemEntry = (Entry)var1.next();
         CustomStructures.getInstance().getLootTableHandler().addLootItem((String)itemEntry.getKey(), (Class)itemEntry.getValue());
      }

   }
}
