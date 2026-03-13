package me.gypopo.economyshopgui.providers.requirements.items.region;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.requirements.items.region.hooks.WorldGuardIntegration;
import me.gypopo.economyshopgui.util.exceptions.ItemRequirementLoadException;
import org.bukkit.plugin.PluginManager;

public class RegionManager {
   private final RegionProvider provider;
   private final Map<RegionRequirement, String> requirements = new HashMap();

   public RegionManager(EconomyShopGUI plugin) {
      PluginManager pm = plugin.getServer().getPluginManager();
      this.provider = this.getProvider(plugin, pm);
      if (this.provider != null) {
         this.provider.init(plugin, this);
      }

   }

   private RegionProvider getProvider(EconomyShopGUI plugin, PluginManager pm) {
      return pm.getPlugin("WorldGuard") != null ? new WorldGuardIntegration(plugin) : null;
   }

   public RegionRequirement addRequirement(RegionRequirement requirement, String path) {
      this.requirements.put(requirement, path);
      return requirement;
   }

   public RegionProvider getProvider() throws ItemRequirementLoadException {
      if (this.provider != null) {
         return this.provider;
      } else {
         throw new ItemRequirementLoadException("Failed to enable integration with supported region plugin");
      }
   }

   public void onLoad() {
      if (!this.provider.isReady()) {
         SendMessage.errorMessage("Failed to enable quest integration with '" + this.provider.getName() + "'");
      }

      Iterator var1 = this.requirements.keySet().iterator();

      while(var1.hasNext()) {
         RegionRequirement requirement = (RegionRequirement)var1.next();
         String path = (String)this.requirements.get(requirement);
         if (!this.provider.isReady()) {
            SendMessage.warnMessage("Failed to load region item requirement: " + this.provider.getName() + " integration failed to load");
            SendMessage.errorShops(path.split("\\.")[0], path.split("\\.", 2)[1]);
         } else {
            if (!this.provider.isLoaded(requirement.getRegionID())) {
               SendMessage.warnMessage("Issue while loading region item requirement: Region using ID '" + requirement.getRegionID() + "' was not found in " + this.provider.getName() + ", is this supposed to be a region which hasn't been created yet? Else the name of the region is incorrect, continuing anyway...");
               SendMessage.errorShops(path.split("\\.")[0], path.split("\\.", 2)[1]);
            }

            requirement.onLoad();
         }
      }

   }
}
