/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemFlag
 */
package nl.sbdeveloper.vehiclesplus.inventories.garages;

import co.aikar.commands.MessageKeys;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GaragePermissions;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GarageRole;
import nl.sbdeveloper.vehiclesplus.api.vehicles.Vehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
import nl.sbdeveloper.vehiclesplus.handlers.EconomyAdapter;
import nl.sbdeveloper.vehiclesplus.inventories.garages.VehicleGarageSettingsGUI;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.storage.db.exceptions.DataStorageException;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.ConfirmationInventory;
import nl.sbdeveloper.vehiclesplus.utils.inventories.PaginationInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class VehicleGarageGUI
extends PaginationInventory {
    private final Garage garage;

    public VehicleGarageGUI(Player player, Garage garage) {
        super(5, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_TITLE, (Map<String, String>)Map.of((Object)"%garage%", (Object)garage.getName())));
        this.garage = garage;
        GaragePermissions garagePermissions = this.getGaragePermissions(player);
        for (UUID uUID : new ArrayList<UUID>(garage.getVehicles())) {
            Vehicle vehicle = VehiclesPlusAPI.getVehicle(uUID);
            if (vehicle == null) {
                Bukkit.getLogger().warning("Vehicle with UUID " + String.valueOf(uUID) + " is not found in the database! Removing from garage (" + garage.getName() + ").");
                garage.removeVehicle(uUID);
                continue;
            }
            StorageVehicle storageVehicle = vehicle.getStorageVehicle();
            Skin skin = storageVehicle.getPart(Skin.class);
            if (skin == null) continue;
            ItemFlag[] itemFlagArray = XMaterial.supports(16) ? (ItemFlag[])List.of((Object)ItemFlag.HIDE_DYE, (Object)ItemFlag.HIDE_ATTRIBUTES, (Object)ItemFlag.HIDE_UNBREAKABLE, (Object)ItemFlag.HIDE_PLACED_ON).toArray(ItemFlag[]::new) : (ItemFlag[])List.of((Object)ItemFlag.HIDE_ATTRIBUTES, (Object)ItemFlag.HIDE_UNBREAKABLE, (Object)ItemFlag.HIDE_PLACED_ON).toArray(ItemFlag[]::new);
            this.addItem(ClickableItem.of(new ItemBuilder(skin.getItem()).displayname(storageVehicle.getDisplayNameColored()).lore(storageVehicle.getInfoList()).flag(itemFlagArray).getItemStack(), inventoryClickEvent -> {
                if (inventoryClickEvent.isLeftClick()) {
                    if (storageVehicle.getStatics().isBroken()) {
                        if (!garagePermissions.isRepair()) {
                            player.sendMessage(Locale.getMessage(MessageKeys.PERMISSION_DENIED));
                            return;
                        }
                        double d = VehiclesPlus.getStorage().getConfig().getRepairCostDivision();
                        double d2 = storageVehicle.getVehicleModel().getPrice() / d;
                        if (EconomyAdapter.isLoaded() && !EconomyAdapter.withdraw(player, d2)) {
                            return;
                        }
                        storageVehicle.getStatics().setBroken(false);
                        storageVehicle.getStatics().setCurrentHealth(storageVehicle.getVehicleModel().getHealth());
                        player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_REPAIR_REPAIRED, (Map<String, String>)Map.of((Object)"%vehicle%", (Object)vehicle.getVehicleModel().getDisplayNameColored())));
                    } else if (vehicle.isSpawned()) {
                        if (garagePermissions.isRemove()) {
                            vehicle.getSpawnedVehicle().despawn(player);
                        } else {
                            player.sendMessage(Locale.getMessage(MessageKeys.PERMISSION_DENIED));
                        }
                    } else {
                        int n = VehiclesPlus.getStorage().getConfig().getLimits().getSpawn();
                        if (n != -1 && VehiclesPlusAPI.getGarage(storageVehicle).stream().flatMap(garage -> garage.getVehicles().stream().filter(uUID -> VehiclesPlusAPI.getVehicle(uUID).isSpawned())).count() >= (long)n) {
                            player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SPAWN_LIMITED, (Map<String, String>)Map.of((Object)"%limit%", (Object)String.valueOf(n))));
                        } else if (!garagePermissions.isSpawn()) {
                            player.sendMessage(Locale.getMessage(MessageKeys.PERMISSION_DENIED));
                        } else if (storageVehicle.spawn(player, false) == null) {
                            player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SPAWN_FAILED));
                        }
                    }
                    this.close(player);
                } else if (inventoryClickEvent.isRightClick()) {
                    if (!garagePermissions.isDelete()) {
                        player.sendMessage(Locale.getMessage(MessageKeys.PERMISSION_DENIED));
                        return;
                    }
                    if (vehicle.isSpawned() && vehicle.getSpawnedVehicle().getStatics().getCurrentSpeed() != 0.0f) {
                        player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_MOVING));
                        return;
                    }
                    new ConfirmationInventory(player, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_REMOVE_CONFIRMATION_TITLE), void_ -> {
                        garage.removeVehicle(storageVehicle.getUuid());
                        try {
                            vehicle.remove(player);
                        } catch (DataStorageException dataStorageException) {
                            throw new RuntimeException(dataStorageException);
                        }
                        player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_REMOVE_CONFIRMATION_REMOVED, (Map<String, String>)Map.of((Object)"%vehicle%", (Object)storageVehicle.getDisplayNameColored())));
                        new VehicleGarageGUI(player, garage);
                    }, void_ -> new VehicleGarageGUI(player, garage), false);
                }
            }));
        }
        this.open(player);
    }

    @Override
    public void addStaticItems(Player player, InventoryContents inventoryContents) {
        GaragePermissions garagePermissions = this.getGaragePermissions(player);
        if (!(garagePermissions.isManageMembers() || garagePermissions.isManageRoles() || garagePermissions.isRename())) {
            return;
        }
        inventoryContents.set(5, 4, ClickableItem.of(new ItemBuilder(XMaterial.CRAFTING_TABLE).displayname(ColorUtil.__("&cSettings")).lore(ColorUtil.__("&7Click to open the garage settings.")).getItemStack(), inventoryClickEvent -> new VehicleGarageSettingsGUI(player, this.garage)));
    }

    private GaragePermissions getGaragePermissions(Player player) {
        GarageRole garageRole = this.garage.getRole(player);
        return garageRole.getPermissions();
    }
}

