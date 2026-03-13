/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.configuration.Permissions;
import java.util.Arrays;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class WaitingPos
extends SubCommand {
    public WaitingPos(ParentCommand parent, String name) {
        super(parent, name);
        this.setArenaSetupCommand(true);
        this.setPermission(Permissions.PERMISSION_SETUP_ARENA);
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        SetupSession ss = SetupSession.getSession(p.getUniqueId());
        if (ss == null) {
            s.sendMessage("\u00a7c \u25aa \u00a77You're not in a setup session!");
            return true;
        }
        if (args.length == 0) {
            p.sendMessage("\u00a7c\u25aa \u00a77Usage: /" + BedWars.mainCmd + " " + this.getSubCommandName() + " 1 or 2");
        } else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("2")) {
            p.sendMessage("\u00a76 \u25aa \u00a77Pos " + args[0] + " set!");
            ss.getConfig().saveArenaLoc("waiting.Pos" + args[0], p.getLocation());
            ss.getConfig().reload();
            if (ss.getConfig().getYml().get("waiting.Pos1") == null) {
                p.sendMessage("\u00a7c \u25aa \u00a77Set the remaining position:");
                p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a7c \u25aa \u00a77/" + BedWars.mainCmd + " waitingPos 1", "\u00a7dSet pos 1", "/" + this.getParent().getName() + " waitingPos 1", ClickEvent.Action.RUN_COMMAND));
            } else if (ss.getConfig().getYml().get("waiting.Pos2") == null) {
                p.sendMessage("\u00a7c \u25aa \u00a77Set the remaining position:");
                p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a7c \u25aa \u00a77/" + BedWars.mainCmd + " waitingPos 2", "\u00a7dSet pos 2", "/" + this.getParent().getName() + " waitingPos 2", ClickEvent.Action.RUN_COMMAND));
            }
        } else {
            p.sendMessage("\u00a7c\u25aa \u00a77Usage: /" + BedWars.mainCmd + " " + this.getSubCommandName() + " 1 or 2");
        }
        if (ss.getConfig().getYml().get("waiting.Pos1") != null && ss.getConfig().getYml().get("waiting.Pos2") != null) {
            Bukkit.dispatchCommand((CommandSender)p, (String)(BedWars.mainCmd + " cmds"));
            s.sendMessage("\u00a76 \u25aa \u00a77Set teams spawn if you didn't!");
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return Arrays.asList("1", "2");
    }

    @Override
    public boolean canSee(CommandSender s, com.andrei1058.bedwars.api.BedWars api) {
        if (s instanceof ConsoleCommandSender) {
            return false;
        }
        Player p = (Player)s;
        if (!SetupSession.isInSetupSession(p.getUniqueId())) {
            return false;
        }
        return this.hasPermission(s);
    }
}

