package advancedplugins.pm2.cv.models.core.listener;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.lod.AnimationLODHandler;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BukkitPlayer;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.data.IEntityData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class PlayerListener implements Listener {
   @EventHandler
   public void onJoin(PlayerJoinEvent var1) {
      ModelAPI.getNetworkHandler().injectChannel(var1.getPlayer());
      AnimationLODHandler.registerPlayer(var1.getPlayer().getUniqueId());
   }

   @EventHandler
   public void onQuit(PlayerQuitEvent var1) {
      ModelAPI.onPlayerQuit(var1);
   }

   @EventHandler
   public void onPortal(PlayerPortalEvent var1) {
      Player var2 = var1.getPlayer();
      if (ModelAPI.getMountPairManager().get(var2.getUniqueId()) != null) {
         var1.setCancelled(true);
      }

   }

   @EventHandler
   public void onTeleport(PlayerTeleportEvent var1) {
      if (!(var1 instanceof PlayerPortalEvent)) {
         ModelAPI.getMountPairManager().tryDismount(var1.getPlayer());
      }

   }

   @EventHandler
   public void onDeath(PlayerDeathEvent var1) {
      ModelAPI.getMountPairManager().tryDismount(var1.getPlayer());
   }

   @EventHandler
   public void onMove(PlayerMoveEvent var1) {
      Vector var2 = var1.getTo().toVector().subtract(var1.getFrom().toVector());
      if (!var2.isZero()) {
         Player var3 = var1.getPlayer();
         IModelContainer var4 = ModelAPI.getModeledEntity(var3.getUniqueId());
         if (var4 != null) {
            IEntityData var5 = var4.getBase().getData();
            if (var5 instanceof BukkitPlayer.BukkitPlayerData) {
               BukkitPlayer.BukkitPlayerData var6 = (BukkitPlayer.BukkitPlayerData)var5;
               if (var2.getX() != 0.0D || var2.getZ() != 0.0D) {
                  var6.setWalkTick(3);
               }

               if (var2.getY() > 0.0D) {
                  var6.setJumpTick(3);
               }

            }
         }
      }
   }
}
