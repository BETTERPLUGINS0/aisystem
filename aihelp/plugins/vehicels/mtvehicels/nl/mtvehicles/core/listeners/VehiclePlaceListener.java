/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 */
package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.events.VehiclePlaceEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.RegionAction;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class VehiclePlaceListener
extends MTVListener {
    public VehiclePlaceListener() {
        super(new VehiclePlaceEvent());
    }

    @EventHandler
    public void onVehiclePlace(PlayerInteractEvent event) {
        this.event = event;
        this.player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        Block clickedBlock = event.getClickedBlock();
        if (action != Action.RIGHT_CLICK_BLOCK || item == null || item.getType() == Material.AIR || item.getAmount() == 0 || !item.hasItemMeta() || clickedBlock == null) {
            return;
        }
        NBTItem nbtItem = new NBTItem(item);
        if (!nbtItem.hasTag("mtvehicles.kenteken")) {
            return;
        }
        String license = VehicleUtils.getLicensePlate(item);
        if (license == null || !VehicleUtils.existsByLicensePlate(license)) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.VEHICLE_NOT_FOUND);
            event.setCancelled(true);
            return;
        }
        VehiclePlaceEvent api = (VehiclePlaceEvent)this.getAPI();
        Location spawnLoc = clickedBlock.getLocation();
        if (ConfigModule.vehicleDataConfig.getType(license).isBoat()) {
            while (spawnLoc.getBlock().getType().toString().contains("WATER")) {
                spawnLoc.add(0.0, 1.0, 0.0);
                if (!(spawnLoc.getY() >= clickedBlock.getLocation().getY() + 512.0)) continue;
            }
        }
        api.setLocation(spawnLoc);
        api.setLicensePlate(license);
        this.callAPI();
        if (this.isCancelled()) {
            return;
        }
        Location loc = api.getLocation();
        Vehicle vehicle = VehicleUtils.getVehicle(license);
        if (vehicle == null) {
            return;
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            event.setCancelled(true);
            this.player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.WRONG_HAND)));
            return;
        }
        if (ConfigModule.defaultConfig.isBlockWhitelistEnabled() && !ConfigModule.defaultConfig.blockWhiteList().contains(clickedBlock.getType())) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.BLOCK_NOT_IN_WHITELIST);
            return;
        }
        if (!ConfigModule.defaultConfig.canProceedWithAction(RegionAction.PLACE, vehicle.getVehicleType(), loc, this.player)) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)this.player, Message.CANNOT_DO_THAT_HERE);
            return;
        }
        Location location = loc.clone().add(0.0, 1.0, 0.0);
        VehicleUtils.spawnVehicle(license, location);
        this.player.getInventory().remove(this.player.getEquipment().getItemInHand());
        this.player.sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_PLACE).replace("%p%", vehicle.getOwnerName())));
        event.setCancelled(true);
    }
}

