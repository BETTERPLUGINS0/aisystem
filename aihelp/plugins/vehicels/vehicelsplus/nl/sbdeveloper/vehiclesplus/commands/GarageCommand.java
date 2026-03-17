/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package nl.sbdeveloper.vehiclesplus.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.MessageKeys;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import java.util.Map;
import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.impl.GarageOpenEvent;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.garages.permissions.GarageRole;
import nl.sbdeveloper.vehiclesplus.inventories.garages.VehicleGarageGUI;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@CommandAlias(value="garage|vpg|vg|vgarage")
@Description(value="{@@vehiclesplus.commands.garage.descriptions.main}")
public class GarageCommand
extends BaseCommand {
    private static final String[] commonGarageNames = new String[]{"383518", "70523", "1886503327", "%%__LICENSE_1__%%", "%%__LICENSE_2__%%", "%%__PHRASE_1__%%", "%%__PHRASE_2__%%"};

    @HelpCommand
    @CatchUnknown
    public static void onHelp(CommandSender commandSender, CommandHelp commandHelp) {
        commandHelp.showHelp();
    }

    @Default
    @Description(value="{@@vehiclesplus.commands.garage.descriptions.open}")
    public void openGarage(Player player) {
        this.openGarage(player, player.getName());
    }

    @Subcommand(value="open")
    @Description(value="{@@vehiclesplus.commands.garage.descriptions.open}")
    @CommandPermission(value="vp.garage.open.others")
    @CommandCompletion(value="@garages")
    public void openGarage(Player player, @co.aikar.commands.annotation.Optional String string) {
        Garage garage;
        String string2 = player.getName();
        if (string != null) {
            string2 = string;
        }
        if ((garage = this.getGarage(player, string2)) == null) {
            return;
        }
        GarageOpenEvent garageOpenEvent = new GarageOpenEvent(player, garage);
        Bukkit.getPluginManager().callEvent((Event)garageOpenEvent);
        if (garageOpenEvent.isCancelled()) {
            return;
        }
        new VehicleGarageGUI(player, garage);
    }

    @Subcommand(value="create")
    @Description(value="{@@vehiclesplus.commands.garage.descriptions.create}")
    @CommandPermission(value="vp.garage.create")
    @CommandCompletion(value="@nothing")
    public void createGarage(Player player, String string, @co.aikar.commands.annotation.Optional String string2) {
        if (VehiclesPlusAPI.getGarage(string).isPresent()) {
            player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_GARAGE_ALREADY));
            return;
        }
        Garage garage = string2 != null ? new Garage(string, player.getUniqueId(), string2) : new Garage(string, player.getUniqueId());
        VehiclesPlusAPI.addGarage(garage, false);
        garage.save();
        player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_GARAGE_CREATED, (Map<String, String>)Map.of((Object)"%garage%", (Object)ColorUtil.__(garage.getDisplayName()))));
    }

    @Subcommand(value="delete")
    @Description(value="{@@vehiclesplus.commands.garage.descriptions.delete}")
    @CommandPermission(value="vp.garage.delete.own")
    @CommandCompletion(value="@garages")
    public void deleteGarage(Player player, String string) {
        Garage garage = this.getGarage(player, string);
        if (garage == null) {
            return;
        }
        GarageRole garageRole = garage.getRole(player);
        if (!garageRole.getPermissions().isDeleteGarage() && !player.hasPermission("vp.garage.delete.others")) {
            player.sendMessage(Locale.getMessage(MessageKeys.PERMISSION_DENIED.getMessageKey()));
            return;
        }
        VehiclesPlusAPI.removeGarage(garage.getName());
        player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_GARAGE_DELETED, (Map<String, String>)Map.of((Object)"%garage%", (Object)ColorUtil.__(garage.getDisplayName()))));
    }

    @Subcommand(value="addmember")
    @Description(value="{@@vehiclesplus.commands.garage.descriptions.addmember}")
    @CommandPermission(value="vp.garage.member.add")
    @CommandCompletion(value="@garages @players")
    public void addMember(Player player, String string, OnlinePlayer onlinePlayer) {
        Garage garage = this.getGarage(player, string);
        if (garage == null) {
            return;
        }
        garage.addMember(onlinePlayer.getPlayer().getUniqueId());
        garage.save();
        player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_GARAGE_ADDEDMEMBER, (Map<String, String>)Map.of((Object)"%garage%", (Object)ColorUtil.__(garage.getDisplayName()), (Object)"%player%", (Object)onlinePlayer.getPlayer().getName())));
    }

    @Subcommand(value="removemember")
    @Description(value="{@@vehiclesplus.commands.garage.descriptions.removemember}")
    @CommandPermission(value="vp.garage.member.remove")
    @CommandCompletion(value="@garages @players")
    public void removeMember(Player player, String string, OnlinePlayer onlinePlayer) {
        Garage garage = this.getGarage(player, string);
        if (garage == null) {
            return;
        }
        garage.removeMember(onlinePlayer.getPlayer().getUniqueId());
        garage.save();
        player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_GARAGE_REMOVEDMEMBER, (Map<String, String>)Map.of((Object)"%garage%", (Object)ColorUtil.__(garage.getDisplayName()), (Object)"%player%", (Object)onlinePlayer.getPlayer().getName())));
    }

    @Subcommand(value="setowner")
    @Description(value="{@@vehiclesplus.commands.garage.descriptions.setowner}")
    @CommandPermission(value="vp.garage.setowner")
    @CommandCompletion(value="@garages @players")
    public void setOwner(Player player, String string, OfflinePlayer offlinePlayer) {
        Garage garage = this.getGarage(player, string);
        if (garage == null) {
            return;
        }
        garage.setOwner(offlinePlayer.getPlayer());
        garage.save();
        player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_GARAGE_SETOWNER, (Map<String, String>)Map.of((Object)"%garage%", (Object)ColorUtil.__(garage.getDisplayName()), (Object)"%player%", (Object)offlinePlayer.getPlayer().getName())));
    }

    private Garage getGarage(Player player, String string) {
        Optional<Garage> optional = VehiclesPlusAPI.getGarage(string);
        if (optional.isEmpty()) {
            player.sendMessage(Locale.getMessage(PluginMessage.COMMANDS_GARAGE_INVALID));
            return null;
        }
        return optional.get();
    }
}

