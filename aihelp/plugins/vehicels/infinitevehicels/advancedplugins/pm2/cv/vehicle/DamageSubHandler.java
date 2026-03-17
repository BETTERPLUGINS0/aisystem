package advancedplugins.pm2.cv.vehicle;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.packet.incoming.InteractPacketWrapper;
import advancedplugins.pm2.cv.service.PacketService;
import java.util.Iterator;
import java.util.Objects;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.jetbrains.annotations.NotNull;

public class DamageSubHandler {
   final VehicleHandlerImpl vehicleHandler;

   public DamageSubHandler(VehicleHandlerImpl vehicleHandler) {
      this.vehicleHandler = var1;
   }

   Object redirectDamage(@NotNull VehicleImpl vehicle, @NotNull Object packet, @NotNull InteractPacketWrapper wrapper) {
      return var1.damageHitbox != null && var3.action == InteractPacketWrapper.Action.ATTACK ? ((PacketService)Objects.requireNonNull((PacketService)InfiniteVehicles.getService(PacketService.class))).createInstance(new InteractPacketWrapper(var1.damageHitbox.getHandleId(), InteractPacketWrapper.Action.ATTACK, var3.targetX, var3.targetY, var3.targetZ, var3.hand, var3.sneaking)) : var2;
   }

   void processSlimeSplit(SlimeSplitEvent event) {
      if (this.matchVehicle(var1.getEntity().getEntityId()) != null) {
         var1.setCancelled(true);
      }

   }

   void processDeath(EntityDeathEvent event) {
      VehicleImpl var2 = this.matchVehicle(var1.getEntity().getEntityId());
      if (var2 != null) {
         var2.resetDamageHitbox();
         var1.setDroppedExp(0);
         var1.getDrops().clear();
      }

   }

   private VehicleImpl matchVehicle(int hitboxHandleId) {
      Iterator var2 = this.vehicleHandler.vehicles.iterator();

      while(var2.hasNext()) {
         Vehicle var3 = (Vehicle)var2.next();
         if (var3 instanceof VehicleImpl) {
            VehicleImpl var4 = (VehicleImpl)var3;
            if (var4.damageHitbox != null && var1 == var4.damageHitbox.getHandleId()) {
               return (VehicleImpl)var3;
            }
         }
      }

      return null;
   }
}
