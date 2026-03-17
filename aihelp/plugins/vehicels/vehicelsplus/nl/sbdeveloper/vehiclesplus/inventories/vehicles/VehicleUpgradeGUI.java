/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package nl.sbdeveloper.vehiclesplus.inventories.vehicles;

import java.util.ArrayList;
import java.util.Map;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleUpgradeEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.settings.UpgradableSetting;
import nl.sbdeveloper.vehiclesplus.handlers.EconomyAdapter;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.MainUtil;
import nl.sbdeveloper.vehiclesplus.utils.inventories.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class VehicleUpgradeGUI
extends Inventory {
    private final DrivableVehicle vehicle;

    public VehicleUpgradeGUI(Player player, DrivableVehicle drivableVehicle) {
        super(3, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_UPGRADE_TITLE), true);
        this.vehicle = drivableVehicle;
        this.open(player);
    }

    @Override
    public void addItems(Player player, InventoryContents inventoryContents) {
        VehicleModel vehicleModel = this.vehicle.getVehicleModel();
        inventoryContents.set(1, 1, this.getUpgradeItem(vehicleModel.getMaxSpeed(), String.valueOf(this.vehicle.getStatics().getMaxSpeed()), XMaterial.SUGAR, PluginMessage.INVENTORIES_VEHICLES_UPGRADE_ITEMS_SPEED, this.vehicle, player));
        inventoryContents.set(1, 3, this.getUpgradeItem(vehicleModel.getFuelTank(), String.valueOf(this.vehicle.getStatics().getFuelTank()), XMaterial.GLASS, PluginMessage.INVENTORIES_VEHICLES_UPGRADE_ITEMS_FUELTANK, this.vehicle, player));
        inventoryContents.set(1, 5, this.getUpgradeItem(vehicleModel.getAcceleration(), String.valueOf(this.vehicle.getStatics().getAcceleration()), XMaterial.BLAZE_POWDER, PluginMessage.INVENTORIES_VEHICLES_UPGRADE_ITEMS_ACCELERATION, this.vehicle, player));
        inventoryContents.set(1, 7, this.getUpgradeItem(vehicleModel.getTurningRadius(), String.valueOf(this.vehicle.getStatics().getTurningRadius()), XMaterial.STICK, PluginMessage.INVENTORIES_VEHICLES_UPGRADE_ITEMS_TURNINGRADIUS, this.vehicle, player));
    }

    private ClickableItem getUpgradeItem(UpgradableSetting upgradableSetting, String string, XMaterial xMaterial, PluginMessage pluginMessage, DrivableVehicle drivableVehicle, Player player) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_UPGRADE_CURRENT, (Map<String, String>)Map.of((Object)"%current%", (Object)String.valueOf(string), (Object)"%unit%", (Object)upgradableSetting.getUnit())));
        if (!upgradableSetting.isUpgradable()) {
            arrayList.add(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_UPGRADABLE_NOTUPGRADABLE));
        } else if (Double.parseDouble(string) >= upgradableSetting.getMax().doubleValue()) {
            arrayList.add(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_UPGRADE_MAXEDOUT));
        } else {
            arrayList.add(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_UPGRADABLE_STEP, (Map<String, String>)Map.of((Object)"%step%", (Object)String.valueOf(upgradableSetting.getStep()), (Object)"%unit%", (Object)upgradableSetting.getUnit())));
            arrayList.add(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_UPGRADABLE_STEPCOST, (Map<String, String>)Map.of((Object)"%stepcost%", (Object)MainUtil.___(upgradableSetting.getStepCost()))));
            arrayList.add(Locale.getMessage(PluginMessage.COMMANDS_VEHICLEMODEL_INFO_SETTINGS_UPGRADABLE_MAX, (Map<String, String>)Map.of((Object)"%max%", (Object)String.valueOf(upgradableSetting.getMax()), (Object)"%unit%", (Object)upgradableSetting.getUnit())));
        }
        return ClickableItem.of(new ItemBuilder(xMaterial).displayname(Locale.getMessage(pluginMessage)).lore(arrayList).getItemStack(), inventoryClickEvent -> {
            VehicleUpgradeEvent vehicleUpgradeEvent = new VehicleUpgradeEvent(drivableVehicle, VehicleUpgradeEvent.UpgradeType.SPEED);
            Bukkit.getPluginManager().callEvent((Event)vehicleUpgradeEvent);
            if (vehicleUpgradeEvent.isCancelled()) {
                return;
            }
            if (Double.parseDouble(string) >= upgradableSetting.getMax().doubleValue()) {
                player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_UPGRADE_MAXEDOUT));
                this.close(player);
                return;
            }
            if (!EconomyAdapter.withdraw(player, upgradableSetting.getStepCost())) {
                return;
            }
            drivableVehicle.getStatics().setMaxSpeedModifier(drivableVehicle.getStatics().getMaxSpeedModifier() + upgradableSetting.getStep().intValue());
            player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_UPGRADE_UPGRADED));
            this.close(player);
        });
    }
}

