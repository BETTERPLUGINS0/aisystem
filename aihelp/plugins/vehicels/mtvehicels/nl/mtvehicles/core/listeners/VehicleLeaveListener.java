/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.spigotmc.event.entity.EntityDismountEvent
 */
package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.events.VehicleLeaveEvent;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.utils.BossBarUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.spigotmc.event.entity.EntityDismountEvent;

public class VehicleLeaveListener
extends MTVListener {
    @EventHandler
    public void onVehicleLeave(EntityDismountEvent event) {
        this.event = event;
        Entity entity = event.getDismounted();
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        this.player = (Player)event.getEntity();
        if (!VehicleUtils.isVehicle(entity)) {
            return;
        }
        if (!entity.getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
            return;
        }
        String license = VehicleUtils.getLicensePlate(entity);
        if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + license) == null) {
            return;
        }
        this.setAPI(new VehicleLeaveEvent(license));
        this.callAPI();
        if (this.isCancelled()) {
            return;
        }
        BossBarUtils.removeBossBar(this.player, license);
        VehicleUtils.turnOff(license);
    }
}

