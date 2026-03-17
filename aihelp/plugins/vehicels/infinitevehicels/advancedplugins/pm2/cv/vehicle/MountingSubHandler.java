package advancedplugins.pm2.cv.vehicle;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.api.configuration.LangConfiguration;
import advancedplugins.pm2.cv.api.event.VehicleClickedEvent;
import advancedplugins.pm2.cv.api.util.Run;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleSeat;
import java.util.Iterator;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MountingSubHandler {
   void processClickedVehicle(@NotNull VehicleClickedEvent event) {
      if (!var1.isCancelled() && var1.getClickType() == VehicleClickedEvent.ClickType.RIGHT_CLICK) {
         if (InfiniteVehicles.getVehicleHandler().getVehicleByOperator(var1.getPlayer()) == null) {
            Player var2 = var1.getPlayer();
            Vehicle var3 = var1.getVehicle();
            VehicleSeat var4 = this.findClosestFreeSeat(var3, var2);
            if (var3.isKeyed() && !var3.isTheOwner(var2)) {
               var2.sendMessage(LangConfiguration.VEHICLE_KEY_NOT_OWNER.value());
            } else {
               if (var4 != null) {
                  Run.sync(() -> {
                     var4.setPassenger(var2);
                  });
               }

            }
         }
      }
   }

   @Nullable
   private VehicleSeat findClosestFreeSeat(Vehicle vehicle, Player player) {
      boolean var3 = Objects.equals(var2.getUniqueId(), var1.getOwnerUniqueId());
      boolean var4 = Configuration.OWNERSHIP_ONLY_OWNER.booleanValue();
      Vector var5 = var2.getLocation().add(0.0D, var2.getEyeHeight() / 2.0D, 0.0D).toVector();
      VehicleSeat var6 = null;
      double var7 = Double.MAX_VALUE;
      Iterator var9 = var1.getSeats().iterator();

      while(true) {
         VehicleSeat var10;
         double var11;
         do {
            do {
               do {
                  if (!var9.hasNext()) {
                     return var6;
                  }

                  var10 = (VehicleSeat)var9.next();
               } while(var10.getPassenger() != null);
            } while(var10.isMain() && var4 && !var3);

            var11 = var5.distance(new Vector(var10.getX(), var10.getY(), var10.getZ()));
         } while(var6 != null && !(var11 < var7));

         var6 = var10;
         var7 = var11;
      }
   }
}
