/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.InternalStructure
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketListener
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package me.zombie_striker.qav.input;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketListener;
import java.util.HashMap;
import java.util.Map;
import me.zombie_striker.qav.VehicleEntity;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.input.BaseInputHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class ModernInputListener
extends BaseInputHandler
implements Listener,
Runnable {
    private static BukkitTask task;
    private final Map<Player, InternalStructure> lastInput = new HashMap<Player, InternalStructure>();

    @Override
    public void onInputReceived(Player player, PacketContainer packetContainer) {
        InternalStructure internalStructure = (InternalStructure)packetContainer.getStructures().read(0);
        this.lastInput.put(player, internalStructure);
    }

    public void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)this);
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)QualityArmoryVehicles.getPlugin());
        task = Bukkit.getScheduler().runTaskTimer((Plugin)QualityArmoryVehicles.getPlugin(), (Runnable)this, 0L, 1L);
    }

    @Override
    public void run() {
        new HashMap<Player, InternalStructure>(this.lastInput).forEach((player, internalStructure) -> {
            if (player == null || !player.isOnline() || player.getVehicle() == null) {
                this.lastInput.remove(player);
                return;
            }
            VehicleEntity vehicleEntity = QualityArmoryVehicles.getVehicleEntityByEntity(player.getVehicle());
            if (vehicleEntity == null) {
                this.lastInput.remove(player);
                return;
            }
            boolean bl = (Boolean)internalStructure.getBooleans().read(2);
            boolean bl2 = (Boolean)internalStructure.getBooleans().read(3);
            boolean bl3 = (Boolean)internalStructure.getBooleans().read(0);
            boolean bl4 = (Boolean)internalStructure.getBooleans().read(1);
            boolean bl5 = (Boolean)internalStructure.getBooleans().read(4);
            boolean bl6 = (Boolean)internalStructure.getBooleans().read(5);
            if (!(bl || bl2 || bl3 || bl4 || bl5 || bl6)) {
                this.lastInput.remove(player);
                return;
            }
            this.handleInput(vehicleEntity, (Player)player, bl3, bl4, bl, bl2, bl5, bl6);
        });
    }

    public static void unregister() {
        if (task == null || task.isCancelled()) {
            return;
        }
        task.cancel();
    }
}

