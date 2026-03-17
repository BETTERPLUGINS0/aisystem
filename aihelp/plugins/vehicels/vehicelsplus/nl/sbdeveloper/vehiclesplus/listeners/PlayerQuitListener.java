/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerQuitEvent
 */
package nl.sbdeveloper.vehiclesplus.listeners;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener
implements Listener {
    private static final String quitPlaceholder = "Player quit: %%__NONCE__%%";

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        if (!playerQuitEvent.getPlayer().isInsideVehicle()) {
            return;
        }
        if (!(playerQuitEvent.getPlayer().getVehicle() instanceof ArmorStand)) {
            return;
        }
        ArmorStand armorStand = (ArmorStand)playerQuitEvent.getPlayer().getVehicle();
        Optional<SpawnedVehicle> optional2 = VehiclesPlusAPI.getVehicleFromPart(armorStand);
        if (optional2.isEmpty()) {
            return;
        }
        DrivableVehicle drivableVehicle = optional2.get().getAsDrivableVehicle();
        if (drivableVehicle == null) {
            return;
        }
        Seat seat = (Seat)drivableVehicle.getPart(armorStand);
        drivableVehicle.getStatics().setCurrentSpeed(0.0f);
        seat.getHolder().removePassenger((Entity)playerQuitEvent.getPlayer());
        List<Optional> list = VehiclesPlusAPI.getGarages((OfflinePlayer)playerQuitEvent.getPlayer()).stream().flatMap(garage -> garage.getVehicles().stream().map(uUID -> VehiclesPlusAPI.getVehicle(uUID).getSpawnedVehicle()).filter(Objects::nonNull)).map(spawnedVehicle -> spawnedVehicle.getPart(Seat.class, seat -> seat.getPassenger().isPresent())).filter(Optional::isPresent).map(optional -> ((Seat)optional.get()).getOwningVehicle()).collect(Collectors.toList());
        list.forEach(optional -> {
            if (optional.isEmpty()) {
                return;
            }
            SpawnedVehicle spawnedVehicle = (SpawnedVehicle)optional.get();
            if (spawnedVehicle.isDrivable()) {
                spawnedVehicle.despawn(true, playerQuitEvent.getPlayer());
            }
            spawnedVehicle.getStorageVehicle().save();
        });
    }
}

