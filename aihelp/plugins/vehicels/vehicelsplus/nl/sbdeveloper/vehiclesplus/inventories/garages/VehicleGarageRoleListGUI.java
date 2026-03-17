/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.inventories.garages;

import java.util.Map;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GarageRole;
import nl.sbdeveloper.vehiclesplus.conversations.ChatConversation;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.garage.RoleCreatePrompt;
import nl.sbdeveloper.vehiclesplus.inventories.garages.VehicleGarageRoleSettingsGUI;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.ConfirmationInventory;
import nl.sbdeveloper.vehiclesplus.utils.inventories.PaginationInventory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class VehicleGarageRoleListGUI
extends PaginationInventory {
    private final Garage garage;

    public VehicleGarageRoleListGUI(Player player, Garage garage) {
        super(3, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_TITLE, (Map<String, String>)Map.of((Object)"%garage%", (Object)garage.getName())));
        this.garage = garage;
        garage.getRoles().forEach(garageRole -> this.addItem(ClickableItem.of(new ItemBuilder(XMaterial.CHEST).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_ROLELIST_BUTTON_NAME, (Map<String, String>)Map.of((Object)"%role%", (Object)garageRole.getRoleName()))).lore(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_ROLELIST_BUTTON_LORE)).getItemStack(), inventoryClickEvent -> {
            if (!inventoryClickEvent.isRightClick()) {
                if (garageRole.getRoleName().equalsIgnoreCase("owner")) {
                    player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_ROLELIST_NOOWNER));
                    return;
                }
                new VehicleGarageRoleSettingsGUI(player, garage, (GarageRole)garageRole);
            } else {
                new ConfirmationInventory(player, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_ROLELIST_DELETE_TITLE), void_ -> {
                    if (garageRole.getRoleName().equalsIgnoreCase("default") || garageRole.getRoleName().equalsIgnoreCase("member") || garageRole.getRoleName().equalsIgnoreCase("owner")) {
                        player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_ROLELIST_DELETE_DEFAULT));
                        return;
                    }
                    if (!garage.removeRole(garageRole.getRoleName())) {
                        player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_ROLELIST_DELETE_FAILED));
                    }
                    new VehicleGarageRoleListGUI(player, garage);
                }, void_ -> new VehicleGarageRoleListGUI(player, garage), false);
            }
        })));
        this.open(player);
    }

    @Override
    public void addStaticItems(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(3, 4, ClickableItem.of(new ItemBuilder(XMaterial.WRITABLE_BOOK).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_ROLELIST_CREATE_NAME)).lore(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_ROLELIST_CREATE_LORE)).getItemStack(), inventoryClickEvent -> {
            new ChatConversation((Prompt)new RoleCreatePrompt(this.garage)).begin(player);
            this.close(player);
        }));
    }
}

