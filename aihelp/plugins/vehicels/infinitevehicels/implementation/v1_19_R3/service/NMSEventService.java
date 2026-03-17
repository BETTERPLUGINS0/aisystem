package implementation.v1_19_R3.service;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.event.wrapper.WrapperEntityDismountEvent;
import advancedplugins.pm2.cv.api.event.wrapper.WrapperEntityMountEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

public class NMSEventService implements advancedplugins.pm2.cv.api.service.NMSEventService {
   public void registerWrapperEvents() {
      Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
         @EventHandler
         public void onEntityMount(EntityMountEvent var1) {
            WrapperEntityMountEvent var2 = new WrapperEntityMountEvent(var1.getEntity(), var1.getMount());
            Bukkit.getPluginManager().callEvent(var2);
         }

         @EventHandler
         public void onEntityDismount(EntityDismountEvent var1) {
            WrapperEntityDismountEvent var2 = new WrapperEntityDismountEvent(var1.getEntity(), var1.getDismounted());
            Bukkit.getPluginManager().callEvent(var2);
         }
      }, InfiniteVehicles.getPlugin());
   }
}
