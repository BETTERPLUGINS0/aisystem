package com.ryandw11.structure;

import com.ryandw11.structure.api.structaddon.CustomStructureAddon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class AddonHandler {
   private final List<CustomStructureAddon> addons = new ArrayList();

   protected AddonHandler() {
   }

   public List<CustomStructureAddon> getCustomStructureAddons() {
      return Collections.unmodifiableList(this.addons);
   }

   public void registerAddon(CustomStructureAddon addon) {
      if (this.addons.contains(addon)) {
         throw new IllegalArgumentException("Addon is already registered!");
      } else {
         this.addons.add(addon);
      }
   }

   public void handlePluginReload() {
      Iterator var1 = this.addons.iterator();

      while(var1.hasNext()) {
         CustomStructureAddon addon = (CustomStructureAddon)var1.next();
         addon.handlePluginReload();
      }

   }
}
