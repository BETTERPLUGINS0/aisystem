package advancedplugins.pm2.cv.util.task;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleSeat;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class VehicleRightClickWorkaroundTask extends BukkitRunnable {
   private final Map<UUID, ArmorStand> armorStands = new ConcurrentHashMap();

   public void run() {
      Bukkit.getOnlinePlayers().forEach((var1) -> {
         ArmorStand var2 = (ArmorStand)this.armorStands.get(var1.getUniqueId());
         Vehicle var3 = InfiniteVehicles.getVehicleHandler().getVehicleByOperator(var1);
         if (var3 == null && var2 != null) {
            var2.remove();
            this.armorStands.remove(var1.getUniqueId());
         } else if (var3 != null) {
            VehicleSeat var4 = (VehicleSeat)var3.getSeats().stream().filter((var1x) -> {
               return var1x.getPassenger() != null && var1x.getPassenger().getUniqueId().equals(var1.getUniqueId());
            }).findFirst().orElse((Object)null);
            if (var4 != null) {
               Location var5 = new Location(var1.getWorld(), var4.getX(), var4.getY(), var4.getZ(), var4.getRotation(), 0.0F);
               Location var6 = var5.add(var1.getLocation().getDirection().multiply(1)).add(0.0D, 0.2D, 0.0D);
               if (var6.getWorld() != null) {
                  if (var2 == null) {
                     var2 = (ArmorStand)var6.getWorld().spawn(var6, ArmorStand.class);
                     var2.setSmall(false);
                     var2.setVisible(false);
                     var2.setGravity(false);
                     var2.setArms(false);
                     var2.setBasePlate(false);
                     var2.setRemoveWhenFarAway(false);
                     var2.setCanPickupItems(false);
                     var2.setMarker(true);
                     var2.setSilent(true);
                     this.armorStands.put(var1.getUniqueId(), var2);
                  }

                  var2.teleport(var6);
               }
            }
         }
      });
   }

   public synchronized void cancel() {
      super.cancel();
      this.armorStands.values().forEach(Entity::remove);
   }

   public Map<UUID, ArmorStand> getArmorStands() {
      return this.armorStands;
   }
}
