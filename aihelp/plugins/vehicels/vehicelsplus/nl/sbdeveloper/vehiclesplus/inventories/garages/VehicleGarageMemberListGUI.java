/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.conversations.Prompt
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.inventories.garages;

import java.util.Map;
import java.util.UUID;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GarageMember;
import nl.sbdeveloper.vehiclesplus.conversations.ChatConversation;
import nl.sbdeveloper.vehiclesplus.conversations.prompts.garage.MemberAddRolePrompt;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.builder.XSkull;
import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.objects.Profileable;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.PaginationInventory;
import org.bukkit.Bukkit;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class VehicleGarageMemberListGUI
extends PaginationInventory {
    public VehicleGarageMemberListGUI(Player player, Garage garage) {
        super(5, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_TITLE, (Map<String, String>)Map.of((Object)"%garage%", (Object)garage.getName())));
        garage.getMembers().forEach(garageMember -> {
            if (garageMember.getGarageRole().equals("owner")) {
                this.addItem(ClickableItem.empty(new ItemBuilder(XSkull.createItem().profile(Profileable.of(garageMember.getMember())).apply()).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_MANAGEMEMBERS_NAME, (Map<String, String>)Map.of((Object)"%player%", (Object)Bukkit.getOfflinePlayer((UUID)garageMember.getMember()).getPlayer().getDisplayName()))).lore(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_MANAGEMEMBERS_BUTTON_LOREOWNER, (Map<String, String>)Map.of((Object)"%role%", (Object)garageMember.getGarageRole()))).getItemStack()));
            } else {
                this.addItem(ClickableItem.of(new ItemBuilder(XSkull.createItem().profile(Profileable.of(garageMember.getMember())).apply()).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_MANAGEMEMBERS_NAME, (Map<String, String>)Map.of((Object)"%player%", (Object)Bukkit.getOfflinePlayer((UUID)garageMember.getMember()).getPlayer().getDisplayName()))).lore(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_GARAGE_SETTINGS_SETTING_MANAGEMEMBERS_BUTTON_LORE, (Map<String, String>)Map.of((Object)"%role%", (Object)garageMember.getGarageRole()))).getItemStack(), inventoryClickEvent -> {
                    new ChatConversation((Prompt)new MemberAddRolePrompt(garage, (GarageMember)garageMember)).begin(player);
                    this.close(player);
                }));
            }
        });
        this.open(player);
    }
}

