/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketListener
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package me.zombie_striker.qav.input;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketListener;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.input.BaseInputHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LegacyInputListener
extends BaseInputHandler {
    @Override
    public void onInputReceived(final Player player, PacketContainer packetContainer) {
        final VehicleEntity vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(player.getVehicle());
        final boolean bl = ((Float)packetContainer.getFloat().read(0)).floatValue() > 0.0f;
        final boolean bl2 = ((Float)packetContainer.getFloat().read(0)).floatValue() < 0.0f;
        final boolean bl3 = ((Float)packetContainer.getFloat().read(1)).floatValue() > 0.0f;
        final boolean bl4 = ((Float)packetContainer.getFloat().read(1)).floatValue() < 0.0f;
        final boolean bl5 = (Boolean)packetContainer.getBooleans().read(0);
        final boolean bl6 = (Boolean)packetContainer.getBooleans().read(1);
        new BukkitRunnable(){

            public void run() {
                LegacyInputListener.this.handleInput(vehicleEntity, player, bl3, bl4, bl, bl2, bl5, bl6);
            }
        }.runTaskLater((Plugin)QualityArmoryVehicles.getPlugin(), 0L);
    }

    public void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)this);
    }
}

