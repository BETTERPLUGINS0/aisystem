/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Client
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 */
package me.zombie_striker.qav.input;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.api.events.PlayerExitQAVehicleEvent;
import me.zombie_striker.qav.util.VehicleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

public abstract class BaseInputHandler
extends PacketAdapter {
    public BaseInputHandler() {
        super((Plugin)QualityArmoryVehicles.getPlugin(), ListenerPriority.NORMAL, new PacketType[]{PacketType.Play.Client.STEER_VEHICLE});
    }

    public void onPacketReceiving(PacketEvent packetEvent) {
        Player player = packetEvent.getPlayer();
        try {
            packetEvent.getPlayer().getVehicle();
        } catch (UnsupportedOperationException unsupportedOperationException) {
            Main.DEBUG("The method getVehicle is not supported for temporary players.");
            return;
        }
        if (player.getVehicle() == null) {
            return;
        }
        VehicleEntity vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(player.getVehicle());
        if (vehicleEntity == null) {
            return;
        }
        if (!vehicleEntity.getDriverSeat().equals((Object)player.getVehicle())) {
            return;
        }
        PacketContainer packetContainer = packetEvent.getPacket();
        this.onInputReceived(player, packetContainer);
    }

    public abstract void onInputReceived(Player var1, PacketContainer var2);

    public void handleInput(VehicleEntity vehicleEntity, Player player, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6) {
        if (bl4) {
            vehicleEntity.getType().handleTurnLeft(vehicleEntity, player);
        }
        if (bl3) {
            vehicleEntity.getType().handleTurnRight(vehicleEntity, player);
        }
        if (bl2) {
            vehicleEntity.getType().handleSpeedDecrease(vehicleEntity, player);
        }
        if (bl) {
            vehicleEntity.getType().handleSpeedIncrease(vehicleEntity, player);
        }
        if (bl5) {
            vehicleEntity.getType().handleSpace(vehicleEntity, player);
        }
        if (bl6) {
            PlayerExitQAVehicleEvent playerExitQAVehicleEvent = new PlayerExitQAVehicleEvent(vehicleEntity, player);
            Bukkit.getPluginManager().callEvent((Event)playerExitQAVehicleEvent);
            if (Main.antiCheatHook) {
                Location location = player.getVehicle().getLocation();
                player.teleport(location);
            }
            if (Main.removeVehicleOnDismount) {
                VehicleUtils.callback(vehicleEntity, player, "Dismount");
            }
        }
    }
}

