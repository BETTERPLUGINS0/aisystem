/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.inventories.vehicles;

import java.util.Map;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GaragePermissions;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.conversations.ChatConversation;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.vehicles.NameVehiclePrompt;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.vehicles.TransferOwnerPrompt;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.MainUtil;
import nl.sbdeveloper.vehiclesplus.utils.inventories.Inventory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class VehicleSettingsGUI
extends Inventory {
    private final DrivableVehicle vehicle;
    private final GaragePermissions permissions;

    public VehicleSettingsGUI(Player player, DrivableVehicle drivableVehicle, GaragePermissions garagePermissions) {
        super(3, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SETTINGS_TITLE), true);
        this.vehicle = drivableVehicle;
        this.permissions = garagePermissions;
        this.open(player);
    }

    @Override
    public void addItems(Player player, InventoryContents inventoryContents) {
        if (this.permissions.isVehicleRename()) {
            inventoryContents.set(1, 3, ClickableItem.of(new ItemBuilder(XMaterial.NAME_TAG.parseItem()).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SETTINGS_RENAME_NAME)).lore(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SETTINGS_RENAME_LORE, (Map<String, String>)Map.of((Object)"%cost%", (Object)MainUtil.___(VehiclesPlus.getStorage().getConfig().getRenameCost())))).getItemStack(), inventoryClickEvent -> {
                new ChatConversation((Prompt)new NameVehiclePrompt(this.vehicle)).begin(player);
                this.close(player);
            }));
        }
        if (this.permissions.isTransferVehicle()) {
            inventoryContents.set(1, 5, ClickableItem.of(new ItemBuilder(XMaterial.PLAYER_HEAD.parseItem()).durability(3).lore(ColorUtil.__("&aTransfer to another garage")).getItemStack(), inventoryClickEvent -> {
                new ChatConversation((Prompt)new TransferOwnerPrompt(this.vehicle)).begin(player);
                this.close(player);
            }));
        }
    }
}

