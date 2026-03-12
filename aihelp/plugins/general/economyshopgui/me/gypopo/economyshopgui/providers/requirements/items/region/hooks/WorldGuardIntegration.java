package me.gypopo.economyshopgui.providers.requirements.items.region.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import java.util.Iterator;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.requirements.items.region.RegionManager;
import me.gypopo.economyshopgui.providers.requirements.items.region.RegionProvider;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldGuardIntegration implements RegionProvider {
   private final EconomyShopGUI plugin;

   public WorldGuardIntegration(EconomyShopGUI plugin) {
      this.plugin = plugin;
   }

   public void init(EconomyShopGUI plugin, RegionManager manager) {
      if (!this.isReady()) {
         SendMessage.infoMessage(this.getName() + " found, waiting...");
         plugin.runTaskLater(() -> {
            if (plugin.getServer().getPluginManager().isPluginEnabled(this.getName())) {
               SendMessage.infoMessage(Lang.ENABLED_PLUGIN_HOOK.get().replace("%plugin%", this.getName()));
            } else {
               SendMessage.warnMessage(Lang.FAILED_PLUGIN_INTEGRATION.get().replace("%plugin%", this.getName()));
            }

            manager.onLoad();
         }, 5L);
      } else {
         SendMessage.infoMessage(Lang.ENABLED_PLUGIN_HOOK.get().replace("%plugin%", this.getName()));
      }

   }

   public boolean isReady() {
      RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
      Iterator var2 = this.plugin.getServer().getWorlds().iterator();

      com.sk89q.worldguard.protection.managers.RegionManager regions;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         World world = (World)var2.next();
         regions = container.get(BukkitAdapter.adapt(world));
      } while(regions == null);

      return true;
   }

   public String getName() {
      return "WorldGuard";
   }

   public boolean isLoaded(String regionID) {
      RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
      Iterator var3 = this.plugin.getServer().getWorlds().iterator();

      com.sk89q.worldguard.protection.managers.RegionManager regions;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         World world = (World)var3.next();
         regions = container.get(BukkitAdapter.adapt(world));
      } while(regions == null || !regions.hasRegion(regionID));

      return true;
   }

   public String getRegionName(String regionID) {
      return regionID;
   }

   public boolean isInsideRegion(Player p, String regionID) {
      Location loc = BukkitAdapter.adapt(p.getLocation());
      RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
      com.sk89q.worldguard.protection.managers.RegionManager regions = container.get(BukkitAdapter.adapt(p.getWorld()));
      if (regions == null) {
         return false;
      } else {
         ApplicableRegionSet set = regions.getApplicableRegions(loc.toVector().toBlockPoint());
         Iterator var7 = set.getRegions().iterator();

         ProtectedRegion region;
         do {
            if (!var7.hasNext()) {
               return false;
            }

            region = (ProtectedRegion)var7.next();
         } while(!region.getId().equalsIgnoreCase(regionID));

         return true;
      }
   }
}
