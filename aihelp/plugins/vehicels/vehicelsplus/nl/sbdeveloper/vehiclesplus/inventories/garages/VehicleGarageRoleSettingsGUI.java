/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.inventories.garages;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GaragePermissions;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GarageRole;
import nl.sbdeveloper.vehiclesplus.inventories.garages.Permissions;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.PaginationInventory;
import org.bukkit.entity.Player;

public class VehicleGarageRoleSettingsGUI
extends PaginationInventory {
    protected VehicleGarageRoleSettingsGUI(Player player, Garage garage, GarageRole garageRole) {
        super(2, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_TITLE, (Map<String, String>)Map.of((Object)"%garage%", (Object)garage.getName())), true);
        GaragePermissions garagePermissions = garageRole.getPermissions();
        if (garageRole.getRoleName().equalsIgnoreCase("default")) {
            for (Permissions permissions2 : Arrays.stream(Permissions.values()).filter(permissions -> !permissions.isMembersOnly()).collect(Collectors.toList())) {
                this.createSetting(permissions2.getMaterial(), Locale.getMessage(permissions2.getMessage()), garagePermissions, permissions2.getGetter().apply(garagePermissions), permissions2.getSetter());
            }
        } else {
            for (Permissions permissions3 : Permissions.values()) {
                this.createSetting(permissions3.getMaterial(), Locale.getMessage(permissions3.getMessage()), garagePermissions, permissions3.getGetter().apply(garagePermissions), permissions3.getSetter());
            }
        }
        this.open(player);
    }

    public void createSetting(XMaterial xMaterial, String string, GaragePermissions garagePermissions, boolean bl, BiConsumer<GaragePermissions, Boolean> biConsumer) {
        this.createSettingItem(xMaterial, string);
        this.createClickableWithPermission(garagePermissions, bl, biConsumer);
    }

    private void createSettingItem(XMaterial xMaterial, String string) {
        this.addItem(0, ClickableItem.empty(new ItemBuilder(xMaterial).displayname(string).getItemStack()));
    }

    private void createClickableWithPermission(GaragePermissions garagePermissions, boolean bl, BiConsumer<GaragePermissions, Boolean> biConsumer) {
        this.addItem(1, ClickableItem.of(new ItemBuilder(bl ? XMaterial.GREEN_DYE : XMaterial.RED_DYE).displayname(bl ? Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_TOGGLE_ENABLED) : Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_TOGGLE_DISABLED)).getItemStack(), inventoryClickEvent -> {
            biConsumer.accept(garagePermissions, !bl);
            this.createClickableWithPermission(garagePermissions, !bl, biConsumer);
        }));
    }
}

