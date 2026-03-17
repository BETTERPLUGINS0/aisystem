package advancedplugins.pm2.cv.models.core.listener;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.manager.MountManager;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.MountController;
import it.unimi.dsi.fastutil.Pair;
import java.util.Optional;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EntityListener implements Listener {
   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onEntityDamage(EntityDamageEvent var1) {
      if (!var1.isCancelled()) {
         IModelContainer var2 = ModelAPI.getModeledEntity(var1.getEntity());
         if (var2 != null) {
            var2.markHurt();
         }
      }
   }

   @EventHandler
   public void onEntityAttacked(EntityDamageByEntityEvent var1) {
      Pair var2 = ModelAPI.getMountPairManager().get(var1.getDamager().getUniqueId());
      if (var2 != null) {
         Object var3 = ((IVisualModel)var2.left()).getModeledEntity().getBase().getOriginal();
         MountController var4 = (MountController)var2.right();
         if (var3.equals(var1.getEntity()) && !var4.canDamageMount()) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler
   public void onEntityInteracted(PlayerInteractEntityEvent var1) {
      Pair var2 = ModelAPI.getMountPairManager().get(var1.getPlayer().getUniqueId());
      if (var2 != null) {
         Object var3 = ((IVisualModel)var2.left()).getModeledEntity().getBase().getOriginal();
         MountController var4 = (MountController)var2.right();
         if (var3.equals(var1.getRightClicked()) && !var4.canInteractMount()) {
            var1.setCancelled(true);
         }
      }
   }

   @EventHandler
   public void onProjectileHit(ProjectileHitEvent var1) {
      ProjectileSource var2 = var1.getEntity().getShooter();
      if (var2 instanceof Entity) {
         Entity var3 = (Entity)var2;
         if (var1.getHitEntity() != null) {
            Pair var4 = ModelAPI.getMountPairManager().get(var3.getUniqueId());
            if (var4 != null) {
               Object var5 = ((IVisualModel)var4.left()).getModeledEntity().getBase().getOriginal();
               MountController var6 = (MountController)var4.right();
               if (var5.equals(var1.getHitEntity()) && !var6.canDamageMount()) {
                  var1.setCancelled(true);
               }
            }
         }
      }
   }

   @EventHandler
   public void onPortal(EntityPortalEvent var1) {
      Entity var2 = var1.getEntity();
      if (ModelAPI.getMountPairManager().get(var2.getUniqueId()) != null) {
         var1.setCancelled(true);
      } else {
         IModelContainer var3 = ModelAPI.getModeledEntity(var2);
         if (var3 != null && this.hasPassengers(var3)) {
            var1.setCancelled(true);
         }
      }
   }

   private boolean hasPassengers(IModelContainer var1) {
      return var1.getModels().values().stream().map(IVisualModel::getMountManager).filter(Optional::isPresent).map(Optional::get).anyMatch((var0) -> {
         return ((MountManager)var0).hasPassengers();
      });
   }

   @EventHandler
   public void onTeleport(EntityTeleportEvent var1) {
      if (!(var1 instanceof EntityPortalEvent)) {
         ModelAPI.getMountPairManager().tryDismount(var1.getEntity());
      }
   }

   @EventHandler
   public void onDeath(EntityDeathEvent var1) {
      ModelAPI.getMountPairManager().tryDismount(var1.getEntity());
   }
}
