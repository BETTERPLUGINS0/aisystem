/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.configuration.Permissions;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetupArena
extends SubCommand {
    public SetupArena(ParentCommand parent, String name) {
        super(parent, name);
        this.setPriority(2);
        this.setDisplayInfo(Misc.msgHoverClick("\u00a76 \u25aa \u00a77/" + MainCommand.getInstance().getName() + " setupArena \u00a76<worldName>", "\u00a7fCreate or edit an arena.\n'_' and '-' will not be displayed in the arena's name.", "/" + MainCommand.getInstance().getName() + " setupArena ", ClickEvent.Action.SUGGEST_COMMAND));
        this.showInList(true);
        this.setPermission(Permissions.PERMISSION_SETUP_ARENA);
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (!MainCommand.isLobbySet(p)) {
            return true;
        }
        if (args.length != 1) {
            p.sendMessage("\u00a7c\u25aa \u00a77Usage: \u00a7o/" + this.getParent().getName() + " " + this.getSubCommandName() + " <mapName>");
            return true;
        }
        if (!args[0].equals(args[0].toLowerCase())) {
            p.sendMessage("\u00a7c\u25aa \u00a7c" + args[0] + String.valueOf(ChatColor.GRAY) + " mustn't contain capital letters! Rename your folder to: " + String.valueOf(ChatColor.GREEN) + args[0].toLowerCase());
            return true;
        }
        if (args[0].contains("+")) {
            p.sendMessage("\u00a7c\u25aa \u00a77" + args[0] + " mustn't contain this symbol: " + String.valueOf(ChatColor.RED) + "+");
            return true;
        }
        if (Arena.getArenaByName(args[0]) != null && !BedWars.autoscale) {
            p.sendMessage("\u00a7c\u25aa \u00a77Please disable it first!");
            return true;
        }
        if (SetupSession.isInSetupSession(p.getUniqueId())) {
            p.sendMessage("\u00a7c \u25aa \u00a77You're already in a setup session!");
            return true;
        }
        new SetupSession(p, args[0]);
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return BedWars.getAPI().getRestoreAdapter().getWorldsList();
    }

    @Override
    public boolean canSee(CommandSender s, com.andrei1058.bedwars.api.BedWars api) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (Arena.isInArena(p)) {
            return false;
        }
        if (SetupSession.isInSetupSession(p.getUniqueId())) {
            return false;
        }
        return this.hasPermission(s);
    }
}

