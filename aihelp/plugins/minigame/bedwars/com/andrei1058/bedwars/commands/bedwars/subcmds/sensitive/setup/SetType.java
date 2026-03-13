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
import com.andrei1058.bedwars.api.server.SetupType;
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

public class SetType
extends SubCommand {
    private static final List<String> available = Arrays.asList("Solo", "Doubles", "3v3v3v3", "4v4v4v4");

    public SetType(ParentCommand parent, String name) {
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
            this.sendUsage(p);
        } else {
            String input;
            if (!available.contains(args[0])) {
                this.sendUsage(p);
                return true;
            }
            List groups = BedWars.config.getYml().getStringList("arenaGroups");
            if (!groups.contains(input = args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase())) {
                groups.add(input);
                BedWars.config.set("arenaGroups", groups);
            }
            if (input.equals("Solo")) {
                ss.getConfig().set("maxInTeam", 1);
            } else if (input.equalsIgnoreCase("Doubles")) {
                ss.getConfig().set("maxInTeam", 2);
            } else if (input.equalsIgnoreCase("3v3v3v3")) {
                ss.getConfig().set("maxInTeam", 3);
            } else if (input.equalsIgnoreCase("4v4v4v4")) {
                ss.getConfig().set("maxInTeam", 4);
            }
            ss.getConfig().set("group", input);
            p.sendMessage("\u00a76 \u25aa \u00a77Arena group changed to: \u00a7d" + input);
            if (ss.getSetupType() == SetupType.ASSISTED) {
                Bukkit.dispatchCommand((CommandSender)p, (String)this.getParent().getName());
            }
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        List groups = BedWars.config.getYml().getStringList("arenaGroups");
        SetType.available.forEach(available -> {
            if (!groups.contains(available)) {
                groups.add(available);
            }
        });
        return BedWars.config.getYml().getStringList("arenaGroups");
    }

    private void sendUsage(Player p) {
        p.sendMessage("\u00a79 \u25aa \u00a77Usage: " + this.getParent().getName() + " " + this.getSubCommandName() + " <type>");
        p.sendMessage("\u00a79Available types: ");
        for (String st : available) {
            p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a71 \u25aa \u00a7e" + st + " \u00a77(click to set)", "\u00a7dClick to make the arena " + st, "/" + this.getParent().getName() + " " + this.getSubCommandName() + " " + st, ClickEvent.Action.RUN_COMMAND));
        }
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

