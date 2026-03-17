/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.command.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import me.zombie_striker.qav.ItemFact;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.MessagesConfig;
import me.zombie_striker.qav.UnlockedVehicle;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.command.QAVCommand;
import me.zombie_striker.qav.command.SubCommand;
import me.zombie_striker.qav.vehicles.AbstractVehicle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GiveCommand
extends SubCommand {
    public GiveCommand(QAVCommand qAVCommand) {
        super(qAVCommand);
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public void perform(CommandSender commandSender, String[] stringArray) {
        if (!commandSender.hasPermission("qualityarmoryvehicles.give")) {
            commandSender.sendMessage(Main.prefix + MessagesConfig.COMMANDMESSAGES_NO_PERM);
            return;
        }
        if (stringArray.length < 1) {
            this.help(commandSender);
            return;
        }
        String string = stringArray[0];
        AbstractVehicle abstractVehicle = QualityArmoryVehicles.getVehicle(string);
        if (abstractVehicle == null && !string.equalsIgnoreCase("repair")) {
            commandSender.sendMessage("Vehicle does not exist.");
            return;
        }
        Player player = null;
        if (commandSender instanceof Player) {
            player = (Player)commandSender;
        }
        if (stringArray.length > 1) {
            player = Bukkit.getPlayer((String)stringArray[1]);
        }
        if (player == null) {
            commandSender.sendMessage("Player \"" + stringArray[1] + "\"is not on the server.");
            return;
        }
        if (string.equalsIgnoreCase("repair")) {
            player.getInventory().addItem(new ItemStack[]{Main.repairItem.asItem()});
            return;
        }
        if (Main.enableGarage) {
            QualityArmoryVehicles.addUnlockedVehicle((OfflinePlayer)player, new UnlockedVehicle(abstractVehicle, abstractVehicle.getMaxHealth(), true));
        } else {
            player.getInventory().addItem(new ItemStack[]{ItemFact.getItem(abstractVehicle)});
        }
        commandSender.sendMessage(Main.prefix + " Gave " + ChatColor.GOLD + player.getName() + " " + abstractVehicle.getName() + ".");
    }

    @Override
    public List<String> complete(CommandSender commandSender, String[] stringArray) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (stringArray.length == 0 || stringArray.length == 1) {
            for (AbstractVehicle abstractVehicle : Main.vehicleTypes) {
                if (!abstractVehicle.getName().toLowerCase().startsWith(stringArray[0].toLowerCase())) continue;
                arrayList.add(abstractVehicle.getName());
            }
        } else {
            arrayList.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(string -> string.toLowerCase().startsWith(stringArray[1].toLowerCase())).collect(Collectors.toList()));
        }
        return arrayList;
    }

    @Override
    public String getDescription(@NotNull CommandSender commandSender) {
        return MessagesConfig.subcommand_GiveVehicle;
    }
}

