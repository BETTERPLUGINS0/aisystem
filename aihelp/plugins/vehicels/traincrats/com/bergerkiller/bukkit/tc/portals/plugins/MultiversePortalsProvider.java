package com.bergerkiller.bukkit.tc.portals.plugins;

import com.bergerkiller.bukkit.common.conversion.type.WrapperConversion;
import com.bergerkiller.bukkit.tc.Direction;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.portals.PortalDestination;
import com.bergerkiller.bukkit.tc.portals.PortalProvider;
import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.MultiversePortals;
import com.onarandombox.MultiversePortals.PortalLocation;
import java.util.logging.Level;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class MultiversePortalsProvider extends PortalProvider {
   private final MultiversePortals plugin;

   public MultiversePortalsProvider(TrainCarts traincarts, Plugin plugin) {
      this.plugin = (MultiversePortals)plugin;
      traincarts.log(Level.INFO, "Multiverse Portals detected, trains can be teleported to MV Portals");
   }

   public PortalDestination getPortalDestination(World world, String portalName) {
      MVPortal portal = this.plugin.getPortalManager().getPortal(portalName);
      Direction direction = Direction.NONE;
      if (portal == null) {
         int dirIdx = portalName.lastIndexOf(58);
         if (dirIdx == -1) {
            return null;
         }

         direction = Direction.parse(portalName.substring(dirIdx + 1));
         portalName = portalName.substring(0, dirIdx);
         portal = this.plugin.getPortalManager().getPortal(portalName);
         if (portal == null) {
            return null;
         }
      }

      PortalLocation portalPos = portal.getLocation();
      World portalWorld = portalPos.getMVWorld().getCBWorld();
      if (portalWorld == null) {
         return null;
      } else {
         Block minBlock = WrapperConversion.toIntVector3FromVector(portalPos.getMinimum()).toBlock(portalWorld);
         Block maxBlock = WrapperConversion.toIntVector3FromVector(portalPos.getMaximum()).toBlock(portalWorld);
         return PortalDestination.findDestination(minBlock, maxBlock, direction);
      }
   }
}
