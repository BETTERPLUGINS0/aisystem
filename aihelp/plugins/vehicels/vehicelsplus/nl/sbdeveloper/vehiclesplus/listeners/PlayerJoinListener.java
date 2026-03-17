/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package nl.sbdeveloper.vehiclesplus.listeners;

import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener
implements Listener {
    private String joinPlaceholder = "Player joined: 383518";

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        if (VehiclesPlusAPI.getGarage(player.getName()).isEmpty()) {
            Garage garage = new Garage(player.getName(), player.getUniqueId(), "&a" + player.getName() + " his garage", true);
            VehiclesPlusAPI.addGarage(garage, false);
            garage.save();
        }
    }
}

