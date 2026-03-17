/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabExecutor
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.command.SubCommand;
import me.zombie_striker.qav.command.subcommands.CallbackAllCommand;
import me.zombie_striker.qav.command.subcommands.CallbackCommand;
import me.zombie_striker.qav.command.subcommands.DebugCommand;
import me.zombie_striker.qav.command.subcommands.DumpVehicleCommand;
import me.zombie_striker.qav.command.subcommands.GarageCommand;
import me.zombie_striker.qav.command.subcommands.GiveCommand;
import me.zombie_striker.qav.command.subcommands.LicenseCommand;
import me.zombie_striker.qav.command.subcommands.ListCommand;
import me.zombie_striker.qav.command.subcommands.OverrideWhitelistCommand;
import me.zombie_striker.qav.command.subcommands.ReloadCommand;
import me.zombie_striker.qav.command.subcommands.RemoveBuggedCommand;
import me.zombie_striker.qav.command.subcommands.RemoveCommand;
import me.zombie_striker.qav.command.subcommands.RemoveNearbyCommand;
import me.zombie_striker.qav.command.subcommands.ShopCommand;
import me.zombie_striker.qav.command.subcommands.SpawnCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QAVCommand
implements TabExecutor {
    private final Map<String, SubCommand> subcommands = new HashMap<String, SubCommand>();

    public QAVCommand() {
        ArrayList<SubCommand> arrayList = new ArrayList<SubCommand>();
        arrayList.add(new CallbackAllCommand(this));
        arrayList.add(new CallbackCommand(this));
        arrayList.add(new DebugCommand(this));
        arrayList.add(new GarageCommand(this));
        arrayList.add(new GiveCommand(this));
        arrayList.add(new LicenseCommand(this));
        arrayList.add(new ListCommand(this));
        arrayList.add(new ReloadCommand(this));
        arrayList.add(new RemoveBuggedCommand(this));
        arrayList.add(new RemoveCommand(this));
        arrayList.add(new RemoveNearbyCommand(this));
        arrayList.add(new OverrideWhitelistCommand(this));
        arrayList.add(new ShopCommand(this));
        arrayList.add(new SpawnCommand(this));
        arrayList.add(new DumpVehicleCommand(this));
        for (SubCommand subCommand : arrayList) {
            this.subcommands.put(subCommand.getName().toLowerCase(), subCommand);
        }
    }

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull @NotNull String @NotNull [] stringArray) {
        if (stringArray.length > 0 && this.subcommands.containsKey(stringArray[0].toLowerCase())) {
            this.subcommands.get(stringArray[0].toLowerCase()).perform(commandSender, Arrays.copyOfRange(stringArray, 1, stringArray.length));
            return true;
        }
        this.sendHelp(commandSender);
        return true;
    }

    public void sendHelp(@NotNull CommandSender commandSender) {
        commandSender.sendMessage(Main.prefix + " Commands");
        for (SubCommand subCommand : this.subcommands.values()) {
            String string = subCommand.getDescription(commandSender);
            if (string == null) continue;
            commandSender.sendMessage("/QAV " + subCommand.getName() + ChatColor.GRAY + string);
        }
    }

    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, @NotNull String[] stringArray) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (stringArray.length == 0) {
            return new ArrayList<String>(this.subcommands.keySet());
        }
        if (stringArray.length == 1) {
            for (SubCommand subCommand : this.subcommands.values()) {
                if (!subCommand.getName().toLowerCase(Locale.ROOT).startsWith(stringArray[0].toLowerCase(Locale.ROOT))) continue;
                arrayList.add(subCommand.getName());
            }
        }
        if (stringArray.length > 1 && this.subcommands.containsKey(stringArray[0].toLowerCase())) {
            arrayList.addAll(this.subcommands.get(stringArray[0].toLowerCase()).complete(commandSender, Arrays.copyOfRange(stringArray, 1, stringArray.length)));
        }
        return arrayList;
    }
}

