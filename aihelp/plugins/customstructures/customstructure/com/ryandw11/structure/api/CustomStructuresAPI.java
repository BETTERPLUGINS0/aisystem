package com.ryandw11.structure.api;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.api.structaddon.CustomStructureAddon;
import com.ryandw11.structure.loottables.LootTableHandler;
import com.ryandw11.structure.loottables.customitems.CustomItemManager;
import com.ryandw11.structure.structure.StructureHandler;

public class CustomStructuresAPI {
   private final CustomStructures plugin;

   public CustomStructuresAPI() {
      if (CustomStructures.plugin == null) {
         throw new IllegalStateException("CustomStructures has yet to be initialized.");
      } else {
         this.plugin = CustomStructures.plugin;
      }
   }

   public void registerCustomAddon(CustomStructureAddon customStructureAddon) {
      if (this.plugin.getAddonHandler() == null) {
         throw new IllegalStateException("The addon system has not been initialized yet. Please add CustomStructures as a dependency in your plugin.yml file.");
      } else {
         this.plugin.getAddonHandler().registerAddon(customStructureAddon);
      }
   }

   public int getNumberOfStructures() {
      return this.getStructureHandler().getStructures().size();
   }

   public StructureHandler getStructureHandler() {
      return this.plugin.getStructureHandler();
   }

   public LootTableHandler getLootTableHandler() {
      return this.plugin.getLootTableHandler();
   }

   public CustomItemManager getCustomItemManager() {
      return this.plugin.getCustomItemManager();
   }

   public String getSchematicsFolder() {
      return String.valueOf(this.plugin.getDataFolder()) + "/schematics/";
   }

   /** @deprecated */
   @Deprecated
   public boolean isVoidSpawningEnabled() {
      return true;
   }
}
