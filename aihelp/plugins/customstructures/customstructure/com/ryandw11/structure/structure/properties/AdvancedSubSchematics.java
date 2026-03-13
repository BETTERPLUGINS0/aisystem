package com.ryandw11.structure.structure.properties;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.structure.properties.schematics.SubSchematic;
import com.ryandw11.structure.utils.RandomCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class AdvancedSubSchematics {
   private boolean enabled;
   private final Map<String, RandomCollection<SubSchematic>> schematicCategories;

   public AdvancedSubSchematics(@NotNull FileConfiguration configuration, @NotNull CustomStructures plugin) {
      this.schematicCategories = new HashMap();
      if (!configuration.contains("AdvancedSubSchematics")) {
         this.enabled = false;
      } else {
         ConfigurationSection section = configuration.getConfigurationSection("AdvancedSubSchematics");

         assert section != null;

         String category;
         RandomCollection schematics;
         for(Iterator var4 = section.getKeys(false).iterator(); var4.hasNext(); this.schematicCategories.put(category, schematics)) {
            category = (String)var4.next();
            schematics = new RandomCollection();

            try {
               Iterator var7 = ((ConfigurationSection)Objects.requireNonNull(section.getConfigurationSection(category))).getKeys(false).iterator();

               while(var7.hasNext()) {
                  String schemName = (String)var7.next();
                  SubSchematic schem = new SubSchematic((ConfigurationSection)Objects.requireNonNull(section.getConfigurationSection(String.format("%s.%s", category, schemName))), true);
                  schematics.add(schem.getWeight(), schem);
               }
            } catch (RuntimeException var10) {
               this.enabled = false;
               plugin.getLogger().warning("Unable to enable AdvancedSubStructures on structure " + configuration.getName() + ".");
               plugin.getLogger().warning("The following error occurred:");
               plugin.getLogger().warning(var10.getMessage());
               if (plugin.isDebug()) {
                  var10.printStackTrace();
               }
            }
         }

         this.enabled = true;
      }
   }

   public AdvancedSubSchematics(boolean enabled) {
      this.enabled = enabled;
      this.schematicCategories = new HashMap();
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public Map<String, RandomCollection<SubSchematic>> getSchematicCategories() {
      return this.schematicCategories;
   }

   public RandomCollection<SubSchematic> getCategory(String name) {
      return (RandomCollection)this.schematicCategories.get(name);
   }

   public boolean containsCategory(String name) {
      return this.schematicCategories.containsKey(name);
   }

   public List<String> getCategoryNames() {
      return new ArrayList(this.schematicCategories.keySet());
   }

   public void addCategory(String name, RandomCollection<SubSchematic> schematics) {
      this.schematicCategories.put(name, schematics);
   }

   public void addSchematicToCategory(String categoryName, SubSchematic subSchematic) {
      if (!this.schematicCategories.containsKey(categoryName)) {
         throw new IllegalArgumentException("Category does not exist.");
      } else if (subSchematic.getWeight() == 0.0D) {
         throw new IllegalArgumentException("Sub-Schematic weight cannot be zero.");
      } else {
         ((RandomCollection)this.schematicCategories.get(categoryName)).add(subSchematic.getWeight(), subSchematic);
      }
   }
}
