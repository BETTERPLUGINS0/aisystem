package com.bergerkiller.bukkit.tc.portals.plugins;

import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.mw.Portal;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.portals.PortalDestination;
import com.bergerkiller.bukkit.tc.portals.PortalProvider;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class MyWorldsPortalsProvider extends PortalProvider {
   public MyWorldsPortalsProvider(TrainCarts traincarts, Plugin plugin) {
      traincarts.log(Level.INFO, "MyWorlds detected, support for portal sign train teleportation added!");
   }

   public PortalDestination getPortalDestination(World world, String portalName) {
      Location destLoc = Portal.getPortalLocation(portalName, world.getName());
      if (destLoc == null) {
         return null;
      } else {
         Block sign = destLoc.getBlock();
         sign.getChunk();
         if (!(Boolean)MaterialUtil.ISSIGN.get(sign)) {
            return null;
         } else {
            SignActionEvent dest_info = new SignActionEvent(RailLookup.TrackedSign.forRealSign((Block)sign, true, (RailPiece)null));
            return !dest_info.hasRails() ? null : new PortalDestination(dest_info.getRails(), dest_info.getSpawnDirections());
         }
      }
   }

   public static String getPortalDestination(Location portalLocation) {
      Portal portal = Portal.get(portalLocation);
      return portal == null ? null : portal.getDestinationName();
   }
}
