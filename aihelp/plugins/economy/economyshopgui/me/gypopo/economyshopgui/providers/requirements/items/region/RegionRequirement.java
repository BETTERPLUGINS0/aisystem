package me.gypopo.economyshopgui.providers.requirements.items.region;

import java.util.List;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.RequirementManager;
import me.gypopo.economyshopgui.providers.requirements.ItemRequirement;
import org.bukkit.entity.Player;

public class RegionRequirement implements ItemRequirement {
   private final String regionID;
   private final RegionProvider regionProvider;
   private final RequirementManager.RequirementLore lore;

   public RegionRequirement(RegionProvider regionProvider, String regionID, List<String> lore) {
      this.regionID = regionID;
      this.regionProvider = regionProvider;
      this.lore = RequirementManager.getLore(lore, Lang.REGION_REQUIREMENT_LORE.get().toString());
      if (this.regionProvider.isReady()) {
         this.onLoad();
      }

   }

   public String getRegionID() {
      return this.regionID;
   }

   public void onLoad() {
      if (!this.regionProvider.isLoaded(this.regionID)) {
         SendMessage.logDebugMessage("Region using ID '" + this.regionID + "' wasn't found yet after provider enabled, is this supposed to be a region which isn't created yet? Else the name of the region is incorrect for the item requirement");
      } else {
         String regionName = this.regionProvider.getRegionName(this.regionID);
         this.lore.replace("%regionName%", regionName);
      }
   }

   public boolean isMet(Player p) {
      return this.regionProvider.isInsideRegion(p, this.regionID);
   }

   public String[] getLore(Player p, boolean fast, boolean pr) {
      return this.lore.isReplaced() ? this.lore.get(p, fast, pr) : this.lore.get(p, fast, pr, "%regionName%", this.regionProvider.getRegionName(this.regionID));
   }

   public void sendNotMetMessage(Player p) {
      SendMessage.chatToPlayer(p, Lang.REGION_REQUIREMENT_NOT_MET.get().replace("%regionName%", this.regionProvider.getRegionName(this.regionID)));
   }
}
