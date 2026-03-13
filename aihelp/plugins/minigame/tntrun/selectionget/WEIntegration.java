package tntrun.selectionget;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.RegionSelector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import tntrun.messages.Messages;

public class WEIntegration {
   private WorldEditPlugin weplugin = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
   private WorldEdit we = WorldEdit.getInstance();

   protected Location[] getLocations(Player player) {
      try {
         return this.getPlayerSelection(player);
      } catch (Exception var3) {
         return null;
      }
   }

   private Location[] getPlayerSelection(Player player) {
      Location[] locs = new Location[2];
      BukkitPlayer bplayer = new BukkitPlayer(this.weplugin, player);
      RegionSelector selector = this.we.getSessionManager().get(bplayer).getRegionSelector(bplayer.getWorld());

      try {
         BlockVector3 v1 = selector.getRegion().getMinimumPoint();
         BlockVector3 v2 = selector.getRegion().getMaximumPoint();
         locs[0] = new Location(player.getWorld(), (double)v1.x(), (double)v1.y(), (double)v1.z());
         locs[1] = new Location(player.getWorld(), (double)v2.x(), (double)v2.y(), (double)v2.z());
         return locs;
      } catch (IncompleteRegionException var7) {
         Messages.sendMessage(player, "&c Invalid WorldEdit selection");
         return null;
      }
   }
}
