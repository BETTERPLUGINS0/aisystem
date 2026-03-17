/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDismountEvent
 */
package nl.sbdeveloper.vehiclesplus.listeners;

import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleLeaveEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;

public class EntityDismountListener
implements Listener {
    @EventHandler
    public void onDismount(EntityDismountEvent entityDismountEvent) {
        Seat seat;
        if (!(entityDismountEvent.getDismounted() instanceof ArmorStand)) {
            return;
        }
        ArmorStand armorStand = (ArmorStand)entityDismountEvent.getDismounted();
        if (!(entityDismountEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDismountEvent.getEntity();
        Optional<SpawnedVehicle> optional = VehiclesPlusAPI.getVehicleFromPart(armorStand);
        if (optional.isEmpty()) {
            return;
        }
        DrivableVehicle drivableVehicle = optional.get().getAsDrivableVehicle();
        if (drivableVehicle == null) {
            return;
        }
        if (drivableVehicle.getStatics().getCurrentHealth() <= 0) {
            return;
        }
        if (drivableVehicle.isLocked()) {
            entityDismountEvent.setCancelled(true);
            return;
        }
        boolean bl = drivableVehicle.getHolder().getLocation().clone().add(0.0, -1.0, 0.0).getBlock().getType().name().contains("AIR");
        if ((drivableVehicle.getStatics().isMoving() || bl) && !drivableVehicle.getVehicleModel().isExitWhileMoving()) {
            entityDismountEvent.setCancelled(true);
            return;
        }
        Part part = drivableVehicle.getPart(armorStand);
        if (!(part instanceof Seat)) {
            return;
        }
        VehicleLeaveEvent vehicleLeaveEvent = new VehicleLeaveEvent(drivableVehicle, player, (Seat)part);
        Bukkit.getPluginManager().callEvent((Event)vehicleLeaveEvent);
        if (vehicleLeaveEvent.isCancelled()) {
            entityDismountEvent.setCancelled(true);
            return;
        }
        if (VehiclesPlus.getSmoothCoasters().isEnabled(player)) {
            VehiclesPlus.getSmoothCoasters().resetRotation(null, player);
        }
        if ((seat = (Seat)part).getHolder() != null) {
            seat.setOccupied(false);
            if (seat.isSteer()) {
                drivableVehicle.getStatics().setCurrentSpeed(0.0f);
            }
        }
    }
}

