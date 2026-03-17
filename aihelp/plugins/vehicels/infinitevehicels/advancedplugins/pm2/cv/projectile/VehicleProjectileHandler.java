package advancedplugins.pm2.cv.projectile;

import advancedplugins.pm2.cv.api.handler.PluginHandler;
import advancedplugins.pm2.cv.api.vehicle.projectile.projectiles.impl.BlockProjectile;
import advancedplugins.pm2.cv.handler.PluginHandlerOptions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

@PluginHandlerOptions(
   eventListener = true
)
public class VehicleProjectileHandler implements PluginHandler, Listener {
   private JavaPlugin plugin;

   public VehicleProjectileHandler(JavaPlugin plugin) {
      this.plugin = var1;
   }

   @EventHandler
   public void onEntityBlockForm(EntityBlockFormEvent e) {
      if (Boolean.TRUE.equals(var1.getEntity().getPersistentDataContainer().get(BlockProjectile.FALLING_BLOCK_KEY, PersistentDataType.BOOLEAN))) {
         var1.setCancelled(true);
      }

   }

   @EventHandler
   public void onEntityChangeBlock(EntityChangeBlockEvent e) {
      if (Boolean.TRUE.equals(var1.getEntity().getPersistentDataContainer().get(BlockProjectile.FALLING_BLOCK_KEY, PersistentDataType.BOOLEAN))) {
         var1.setCancelled(true);
      }

   }
}
