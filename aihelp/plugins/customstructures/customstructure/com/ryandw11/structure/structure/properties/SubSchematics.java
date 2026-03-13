package com.ryandw11.structure.structure.properties;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.structure.properties.schematics.SubSchematic;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class SubSchematics {
   private boolean enabled;
   private List<SubSchematic> schematics;

   public SubSchematics(@NotNull FileConfiguration configuration, @NotNull CustomStructures plugin) {
      this.schematics = new ArrayList();
      if (!configuration.contains("SubSchematics")) {
         this.enabled = false;
      } else {
         ConfigurationSection section = configuration.getConfigurationSection("SubSchematics");

         assert section != null;

         try {
            Iterator var4 = section.getKeys(false).iterator();

            while(var4.hasNext()) {
               String s = (String)var4.next();
               this.schematics.add(new SubSchematic((ConfigurationSection)Objects.requireNonNull(section.getConfigurationSection(s)), false));
            }
         } catch (RuntimeException var6) {
            this.enabled = false;
            plugin.getLogger().warning("Unable to enable SubStructures on structure " + configuration.getName() + ".");
            plugin.getLogger().warning("The following error occurred:");
            plugin.getLogger().warning(var6.getMessage());
         }

         this.enabled = true;
      }
   }

   public SubSchematics(boolean enabled) {
      this.enabled = enabled;
      this.schematics = new ArrayList();
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public List<SubSchematic> getSchematics() {
      return this.schematics;
   }

   public void setSchematics(List<SubSchematic> schematics) {
      this.schematics = schematics;
   }

   public void addSchematic(SubSchematic subSchematic) {
      this.schematics.add(subSchematic);
   }
}
