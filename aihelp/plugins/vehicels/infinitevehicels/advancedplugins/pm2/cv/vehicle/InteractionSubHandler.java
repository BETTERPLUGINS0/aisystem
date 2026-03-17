package advancedplugins.pm2.cv.vehicle;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.api.event.VehicleClickedEvent;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.input.PlayerInput;
import advancedplugins.pm2.cv.packet.incoming.InteractPacketWrapper;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InteractionSubHandler {
   final VehicleHandlerImpl vehicleHandler;
   final Map<UUID, Long> lastLeftClickMap = Maps.newConcurrentMap();

   public InteractionSubHandler(VehicleHandlerImpl vehicleHandler) {
      this.vehicleHandler = var1;
   }

   void processPlayerConnecting(@NotNull Player player) {
      this.perform(true, var1, (var1x) -> {
         var1x.add(var1);
      });
   }

   void processPlayerDisconnecting(@NotNull Player player) {
      this.perform(true, var1, (var1x) -> {
         var1x.remove(var1);
      });
   }

   void processPlayerChangingLocation(@NotNull Player player) {
      this.processPlayerChangingLocation(var1, (Location)null);
   }

   void processPlayerChangingLocation(@NotNull Player player, @Nullable Location playerLocationOverride) {
      this.perform(true, var1, (var2x) -> {
         var2x.processPlayerLocationChanged(var1, var2);
      });
   }

   void processPlayerChangingWorld(@NotNull Player player, @NotNull World to) {
      this.perform(false, var1, (var2x) -> {
         var2x.processPlayerWorldChanged(var1, var2);
      });
   }

   void perform(boolean worldCheck, @NotNull Player player, @NotNull Consumer<VehicleInteraction> interaction) {
      Iterator var4 = this.vehicleHandler.vehicles.iterator();

      while(true) {
         Vehicle var5;
         do {
            do {
               if (!var4.hasNext()) {
                  return;
               }

               var5 = (Vehicle)var4.next();
            } while(!(var5 instanceof VehicleImpl));
         } while(var1 && !Objects.equals(var2.getWorld(), var5.getWorld()));

         var3.accept(((VehicleImpl)var5).vehicleInteraction);
      }
   }

   Object processInteractionPacket(@NotNull Player player, @NotNull Object packet, @NotNull InteractPacketWrapper wrapper) {
      if (var3.action == InteractPacketWrapper.Action.ATTACK) {
         return this.processLeftClick(var1, var2, var3);
      } else {
         if (var3.action == InteractPacketWrapper.Action.INTERACT_AT) {
            this.processRightClick(var1, var3);
         }

         return var2;
      }
   }

   Object processLeftClick(@NotNull Player player, @NotNull Object packet, @NotNull InteractPacketWrapper wrapper) {
      int var4 = var3.entityId;
      Long var5 = (Long)this.lastLeftClickMap.get(var1.getUniqueId());
      Long var6 = System.currentTimeMillis();
      this.lastLeftClickMap.put(var1.getUniqueId(), var6);
      VehicleImpl var7 = null;
      Iterator var8 = this.getVehiclesByDistance(var1).iterator();

      while(var8.hasNext()) {
         VehicleImpl var9 = (VehicleImpl)var8.next();
         if (var9.vehicleInteraction.isClicked(var1, var4)) {
            var7 = var9;
            this.fireClickEvent(var1, var9, true, var1.isSneaking());
            break;
         }
      }

      Vehicle var10 = this.vehicleHandler.getVehicleByOperator(var1);
      if (var10 != null) {
         var10.input(new PlayerInput(true, false, false, false, false));
         ((VehicleImpl)var10).vehicleInteraction.addPrimaryBinding(var1, PlayerInput.InputType.LEFT_CLICK);
         ((VehicleImpl)var10).vehicleInteraction.addSecondaryBinding(var1, PlayerInput.InputType.LEFT_CLICK);
      }

      if (var5 != null && var6 - var5 <= 200L) {
         this.lastLeftClickMap.remove(var1.getUniqueId());
         if (var7 != null && this.vehicleHandler.getVehicleByPassenger(var1) == null) {
            this.pickUp(var7, var1);
            return var2;
         }
      }

      return var7 != null ? this.vehicleHandler.damageSubHandler.redirectDamage(var7, var2, var3) : var2;
   }

   void processRightClick(@NotNull Player player, @NotNull InteractPacketWrapper wrapper) {
      int var3 = var2.entityId;
      VehicleImpl var4 = null;
      Iterator var5 = this.getVehiclesByDistance(var1).iterator();

      while(var5.hasNext()) {
         VehicleImpl var6 = (VehicleImpl)var5.next();
         if (var6.vehicleInteraction.isClicked(var1, var3)) {
            var4 = var6;
            this.fireClickEvent(var1, var6, false, var1.isSneaking());
            break;
         }
      }

      Vehicle var7 = this.vehicleHandler.getVehicleByOperator(var1);
      if (var7 != null) {
         var7.input(new PlayerInput(false, true, false, false, false));
         ((VehicleImpl)var7).vehicleInteraction.addPrimaryBinding(var1, PlayerInput.InputType.RIGHT_CLICK);
         ((VehicleImpl)var7).vehicleInteraction.addSecondaryBinding(var1, PlayerInput.InputType.RIGHT_CLICK);
      }

      if (var4 != null) {
         ;
      }
   }

   List<VehicleImpl> getVehiclesByDistance(Player player) {
      ArrayList var2 = new ArrayList();
      Location var3 = var1.getLocation().add(0.0D, var1.getEyeHeight() / 2.0D, 0.0D);
      Iterator var4 = this.vehicleHandler.vehicles.iterator();

      while(var4.hasNext()) {
         Vehicle var5 = (Vehicle)var4.next();
         if (var5 instanceof VehicleImpl && Objects.equals(var1.getWorld(), var5.getWorld())) {
            var2.add((VehicleImpl)var5);
         }
      }

      var2.sort((var1x, var2x) -> {
         double var3x = var1x.getLocation().distanceSquared(var3);
         double var5 = var2x.getLocation().distanceSquared(var3);
         if (var3x != var5) {
            return var3x < var5 ? -1 : 1;
         } else {
            return 0;
         }
      });
      return var2;
   }

   void pickUp(VehicleImpl vehicle, Player player) {
      if (Configuration.PICKUP_ENABLE.booleanValue() && (var1.isTheOwner(var2) || !Configuration.PICKUP_ONLY_OWNER.booleanValue() || var2.hasPermission("infinitevehicles.admin.pickup"))) {
         InfiniteVehicles.getVehicleHandler().pickupVehicle(var1, var2, false, true);
      }

   }

   void fireClickEvent(@NotNull Player player, @NotNull Vehicle vehicle, boolean leftClick, boolean crouching) {
      VehicleClickedEvent.ClickType var5;
      if (var3) {
         var5 = var4 ? VehicleClickedEvent.ClickType.LEFT_CLICK_CROUCHING : VehicleClickedEvent.ClickType.LEFT_CLICK;
      } else {
         var5 = var4 ? VehicleClickedEvent.ClickType.RIGHT_CLICK_CROUCHING : VehicleClickedEvent.ClickType.RIGHT_CLICK;
      }

      (new VehicleClickedEvent(var2, var1, var5)).callEventSynchronously((Consumer)null);
   }
}
