/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerInteractAtEntityEvent
 */
package nl.mtvehicles.core.listeners;

import java.util.HashMap;
import java.util.Map;
import nl.mtvehicles.core.events.VehicleEnterEvent;
import nl.mtvehicles.core.events.VehiclePickUpEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class VehicleClickListener
extends MTVListener {
    private final Map<String, Long> lastUsage = new HashMap<String, Long>();
    private Entity entity;
    private String license;

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        long lastUsed;
        this.event = event;
        this.player = event.getPlayer();
        this.entity = event.getRightClicked();
        if (!VehicleUtils.isVehicle(this.entity)) {
            return;
        }
        event.setCancelled(true);
        if (this.entity.getCustomName().startsWith("VEHICLE")) {
            return;
        }
        String playerName = this.player.getName();
        long currentTime = System.currentTimeMillis();
        if (currentTime - (lastUsed = this.lastUsage.getOrDefault(playerName, 0L).longValue()) < 500L) {
            return;
        }
        this.lastUsage.put(playerName, currentTime);
        this.license = VehicleUtils.getLicensePlate(this.entity);
        if (this.player.isSneaking()) {
            this.pickup();
        } else {
            this.enter();
        }
    }

    private void pickup() {
        this.setAPI(new VehiclePickUpEvent());
        VehiclePickUpEvent api = (VehiclePickUpEvent)this.getAPI();
        api.setLicensePlate(this.license);
        this.callAPI();
        if (this.isCancelled()) {
            return;
        }
        this.license = api.getLicensePlate();
        Vehicle vehicle = VehicleUtils.getVehicle(this.license);
        if (vehicle == null) {
            return;
        }
        boolean disablePickupFromWater = (Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.DISABLE_PICKUP_FROM_WATER);
        if (!this.player.hasPermission("mtvehicles.anwb") && disablePickupFromWater && this.entity.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().isLiquid()) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.VEHICLE_IN_WATER);
            return;
        }
        if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.PICKUP, vehicle.getVehicleType(), this.entity.getLocation(), this.player)) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.CANNOT_DO_THAT_HERE);
            return;
        }
        VehicleUtils.pickupVehicle(this.license, this.player);
    }

    private void enter() {
        Vehicle vehicle = VehicleUtils.getVehicle(this.license);
        if (vehicle == null) {
            return;
        }
        this.setAPI(new VehicleEnterEvent(this.license, this.entity.getLocation()));
        this.callAPI();
        if (this.isCancelled()) {
            return;
        }
        String customName = this.entity.getCustomName();
        if (customName.contains("MTVEHICLES_SEAT")) {
            if (!(vehicle.isPublic() || vehicle.isOwner((OfflinePlayer)this.player) || vehicle.canSit(this.player) || this.player.hasPermission("mtvehicles.ride"))) {
                this.player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NO_RIDER_ENTER).replace("%p%", vehicle.getOwnerName())));
                return;
            }
            if (this.entity.isEmpty()) {
                this.entity.addPassenger((Entity)this.player);
                this.player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_ENTER_MEMBER).replace("%p%", vehicle.getOwnerName())));
            }
            return;
        }
        VehicleUtils.enterVehicle(this.license, this.player);
    }
}

