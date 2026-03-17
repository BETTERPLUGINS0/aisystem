/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.inventories.garages;

import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GaragePermissions;
import nl.sbdeveloper.vehiclesplus.conversations.ChatConversation;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.garage.RenamePrompt;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.garage.TransferOwnerPrompt;
import nl.sbdeveloper.vehiclesplus.inventories.garages.VehicleGarageMemberListGUI;
import nl.sbdeveloper.vehiclesplus.inventories.garages.VehicleGarageRoleListGUI;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.Inventory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class VehicleGarageSettingsGUI
extends Inventory {
    private final Garage garage;

    public VehicleGarageSettingsGUI(Player player, Garage garage) {
        super(3, "Garage Settings", true);
        this.garage = garage;
        this.open(player);
    }

    @Override
    public void addItems(Player player, InventoryContents inventoryContents) {
        GaragePermissions garagePermissions = this.garage.getRole(player).getPermissions();
        inventoryContents.set(1, this.garage.isPersonal() ? 3 : 1, this.generateItem(garagePermissions.isManageMembers(), XMaterial.SKELETON_SKULL, "&cManage members", () -> new VehicleGarageMemberListGUI(player, this.garage)));
        if (!this.garage.isPersonal()) {
            inventoryContents.set(1, 3, this.generateItem(garagePermissions.isRename(), XMaterial.ANVIL, "&cRename garage", () -> {
                new ChatConversation((Prompt)new RenamePrompt(this.garage)).begin(player);
                this.close(player);
            }));
            inventoryContents.set(1, 5, this.generateItem(this.garage.getOwner().getUniqueId().equals(player.getUniqueId()), XMaterial.OAK_SIGN, "&cTransfer ownership", () -> {
                new ChatConversation((Prompt)new TransferOwnerPrompt(this.garage)).begin(player);
                this.close(player);
            }));
        }
        inventoryContents.set(1, this.garage.isPersonal() ? 5 : 7, this.generateItem(garagePermissions.isManageRoles(), XMaterial.SKELETON_SKULL, "&cManage roles", () -> new VehicleGarageRoleListGUI(player, this.garage)));
    }

    private ClickableItem generateItem(boolean bl, XMaterial xMaterial, String string, Runnable runnable) {
        ItemBuilder itemBuilder = new ItemBuilder(bl ? xMaterial : XMaterial.BARRIER).displayname(ColorUtil.__(string));
        if (!bl) {
            itemBuilder.lore("&cYou do not have permission");
        }
        return bl ? ClickableItem.of(itemBuilder.getItemStack(), inventoryClickEvent -> runnable.run()) : ClickableItem.empty(itemBuilder.getItemStack());
    }
}

