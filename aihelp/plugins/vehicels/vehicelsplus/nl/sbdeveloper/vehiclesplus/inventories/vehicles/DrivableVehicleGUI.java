/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.inventories.vehicles;

import java.util.Map;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GaragePermissions;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.handlers.EconomyAdapter;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.VehicleEnterGUI;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.VehicleSettingsGUI;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.VehicleUpgradeGUI;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.tuning.VehicleTuningGUI;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.Inventory;
import org.bukkit.entity.Player;

public class DrivableVehicleGUI
extends Inventory {
    private final DrivableVehicle vehicle;
    private final GaragePermissions permissions;

    public DrivableVehicleGUI(Player player, DrivableVehicle drivableVehicle, GaragePermissions garagePermissions) {
        super((garagePermissions.isUpgrade() || garagePermissions.isTune()) && EconomyAdapter.isLoaded() ? 5 : 3, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_INTERACTION_TITLE, (Map<String, String>)Map.of((Object)"%vehicle%", (Object)drivableVehicle.getStorageVehicle().getDisplayNameColored())), true);
        this.vehicle = drivableVehicle;
        this.permissions = garagePermissions;
        this.open(player);
    }

    @Override
    public void addItems(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(1, 1, ClickableItem.of(new ItemBuilder(XMaterial.OAK_DOOR.parseItem()).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_INTERACTION_ENTER)).getItemStack(), inventoryClickEvent -> new VehicleEnterGUI(player, this.vehicle)));
        if (!this.vehicle.isConfigurator()) {
            if (this.permissions.isRemove()) {
                inventoryContents.set(1, 3, ClickableItem.of(new ItemBuilder(XMaterial.BARRIER.parseItem()).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_INTERACTION_REMOVE)).getItemStack(), inventoryClickEvent -> {
                    this.vehicle.despawn(player);
                    this.close(player);
                }));
            }
            if (this.permissions.isLock()) {
                inventoryContents.set(1, 5, ClickableItem.of(new ItemBuilder(XMaterial.TRIPWIRE_HOOK.parseItem()).displayname(Locale.getMessage(this.vehicle.isLocked() ? PluginMessage.INVENTORIES_VEHICLES_INTERACTION_UNLOCK : PluginMessage.INVENTORIES_VEHICLES_INTERACTION_LOCK)).getItemStack(), inventoryClickEvent -> {
                    if (this.vehicle.setLocked(!this.vehicle.isLocked())) {
                        player.sendMessage(Locale.getMessage(this.vehicle.isLocked() ? PluginMessage.INVENTORIES_VEHICLES_INTERACTION_LOCKED : PluginMessage.INVENTORIES_VEHICLES_INTERACTION_UNLOCKED));
                    }
                    this.close(player);
                }));
            }
            if (this.permissions.isOpenTrunk() && this.vehicle.getTrunk() != null) {
                inventoryContents.set(1, 7, ClickableItem.of(new ItemBuilder(XMaterial.CHEST.parseItem()).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_INTERACTION_TRUNK)).getItemStack(), inventoryClickEvent -> player.openInventory(this.vehicle.getTrunk())));
            }
            if (this.permissions.isVehicleRename() || this.permissions.isTransferVehicle()) {
                inventoryContents.set(3, 3, ClickableItem.of(new ItemBuilder(XMaterial.ARROW.parseItem()).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_INTERACTION_SETTINGS)).getItemStack(), inventoryClickEvent -> new VehicleSettingsGUI(player, this.vehicle, this.permissions)));
            }
            if (this.permissions.isTune()) {
                inventoryContents.set(3, 5, ClickableItem.of(new ItemBuilder(XMaterial.IRON_INGOT.parseItem()).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_INTERACTION_TUNING)).getItemStack(), inventoryClickEvent -> new VehicleTuningGUI(player, this.vehicle)));
            }
        }
        if (this.permissions.isUpgrade() && EconomyAdapter.isLoaded() && !this.vehicle.isConfigurator()) {
            inventoryContents.set(3, 7, ClickableItem.of(new ItemBuilder(XMaterial.SUGAR.parseItem()).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_INTERACTION_UPGRADES)).getItemStack(), inventoryClickEvent -> new VehicleUpgradeGUI(player, this.vehicle)));
        }
    }
}

