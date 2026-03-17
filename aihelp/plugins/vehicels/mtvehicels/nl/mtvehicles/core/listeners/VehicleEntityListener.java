/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.inventory.ItemStack
 */
package nl.mtvehicles.core.listeners;

import java.util.HashMap;
import javax.annotation.Nullable;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleFuel;
import nl.mtvehicles.core.events.VehicleDamageEvent;
import nl.mtvehicles.core.events.VehicleFuelEvent;
import nl.mtvehicles.core.events.VehicleOpenTrunkEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.BossBarUtils;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class VehicleEntityListener
extends MTVListener {
    public static HashMap<String, Double> speed = new HashMap();

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(EntityDamageByEntityEvent event) {
        this.event = event;
        Entity victim = event.getEntity();
        if (!VehicleUtils.isVehicle(victim)) {
            return;
        }
        String license = VehicleUtils.getLicensePlate(victim);
        if (license == null) {
            return;
        }
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) {
            this.handleVehicleDamage(damager, license);
            return;
        }
        this.player = (Player)damager;
        if (this.player.isSneaking() && !this.player.isInsideVehicle()) {
            this.handleOpenTrunk(license);
            event.setCancelled(true);
            return;
        }
        ItemStack heldItem = this.player.getInventory().getItemInMainHand();
        if (!heldItem.hasItemMeta()) {
            this.handleVehicleDamage((Entity)this.player, license);
            return;
        }
        NBTItem nbt = new NBTItem(heldItem);
        if (!nbt.hasKey("mtvehicles.benzineval").booleanValue()) {
            this.handleVehicleDamage((Entity)this.player, license);
            return;
        }
        this.handleFueling(license, this.player, nbt);
    }

    private void handleVehicleDamage(Entity damager, String license) {
        this.setupDamageAPI(damager, license);
        this.callAPI(null);
        if (this.isCancelled()) {
            return;
        }
        String newLicense = ((VehicleDamageEvent)this.getAPI()).getLicensePlate();
        double damage = ((VehicleDamageEvent)this.getAPI()).getDamage();
        VehicleEntityListener.damage(newLicense, damage);
    }

    private void handleOpenTrunk(String license) {
        this.setAPI(new VehicleOpenTrunkEvent());
        VehicleOpenTrunkEvent api = (VehicleOpenTrunkEvent)this.getAPI();
        api.setLicensePlate(license);
        this.callAPI();
        if (this.isCancelled()) {
            return;
        }
        if (!VehicleUtils.isTrunkInventoryOpen(this.player, license)) {
            VehicleUtils.openTrunk(this.player, api.getLicensePlate());
        }
    }

    private void handleFueling(String license, Player player, NBTItem nbt) {
        double vehicleFuel = Math.max(VehicleData.fuel.getOrDefault(license, 0.0), (Double)ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.FUEL));
        String jerryCanFuelStr = nbt.getString("mtvehicles.benzineval");
        String jerryCanSizeStr = nbt.getString("mtvehicles.benzinesize");
        if (jerryCanFuelStr == null || jerryCanSizeStr == null) {
            return;
        }
        int jerryCanFuel = Integer.parseInt(jerryCanFuelStr);
        int jerryCanSize = Integer.parseInt(jerryCanSizeStr);
        this.setAPI(new VehicleFuelEvent(vehicleFuel, jerryCanFuel, jerryCanSize));
        VehicleFuelEvent api = (VehicleFuelEvent)this.getAPI();
        api.setLicensePlate(license);
        this.callAPI();
        if (this.isCancelled()) {
            return;
        }
        license = api.getLicensePlate();
        if (!((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.FUEL_ENABLED)).booleanValue() || !((Boolean)ConfigModule.vehicleDataConfig.get(license, VehicleDataConfig.Option.FUEL_ENABLED)).booleanValue()) {
            return;
        }
        if (!ConfigModule.defaultConfig.canUseJerryCan(player)) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)player, Message.NOT_IN_A_GAS_STATION);
            return;
        }
        if (jerryCanFuel < 1) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)player, Message.NO_FUEL);
            return;
        }
        if (vehicleFuel >= 100.0) {
            ConfigModule.messagesConfig.sendMessage((CommandSender)player, Message.VEHICLE_FULL);
            return;
        }
        if (VehicleData.fallDamage.get(license) != null && vehicleFuel > 2.0) {
            VehicleData.fallDamage.remove(license);
        }
        int fuelToAdd = Math.min(5, jerryCanFuel);
        vehicleFuel = Math.min(vehicleFuel + (double)fuelToAdd, 100.0);
        if (player.isInsideVehicle()) {
            VehicleData.fuel.put(license, vehicleFuel);
        } else {
            ConfigModule.vehicleDataConfig.set(license, VehicleDataConfig.Option.FUEL, vehicleFuel);
            ConfigModule.vehicleDataConfig.save();
        }
        BossBarUtils.setBossBarValue(vehicleFuel / 100.0, license);
        player.getInventory().setItemInMainHand(VehicleFuel.jerrycanItem(jerryCanSize, jerryCanFuel - fuelToAdd));
    }

    public static void damage(String license, double damage) {
        if (!((Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.DAMAGE_ENABLED)).booleanValue()) {
            return;
        }
        if (VehicleUtils.getVehicle(license) == null) {
            return;
        }
        double damageMultiplier = (Double)ConfigModule.defaultConfig.get(DefaultConfig.Option.DAMAGE_MULTIPLIER);
        damageMultiplier = Math.max(0.1, Math.min(damageMultiplier, 5.0));
        ConfigModule.vehicleDataConfig.damageVehicle(license, damage * damageMultiplier);
    }

    private void setupDamageAPI(@Nullable Entity damager, String license) {
        this.setAPI(new VehicleDamageEvent());
        VehicleDamageEvent api = (VehicleDamageEvent)this.getAPI();
        api.setDamager(damager);
        api.setDamageCause(EntityDamageEvent.DamageCause.ENTITY_ATTACK);
        api.setLicensePlate(license);
        api.setDamage(((EntityDamageByEntityEvent)this.event).getDamage());
    }
}

