/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.server.SetupType;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.configuration.Permissions;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CreateTeam
extends SubCommand {
    public CreateTeam(ParentCommand parent, String name) {
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
        if (args.length < 2) {
            p.sendMessage("\u00a7c\u25aa \u00a77Usage: /" + BedWars.mainCmd + " createTeam \u00a7o<name> \u00a7o<color>");
            StringBuilder colors = new StringBuilder("\u00a77");
            for (TeamColor t : TeamColor.values()) {
                colors.append(t.chat()).append((Object)t).append(ChatColor.GRAY).append(", ");
            }
            colors = new StringBuilder(colors.substring(0, colors.toString().length() - 2) + String.valueOf(ChatColor.GRAY) + ".");
            p.sendMessage("\u00a76 \u25aa \u00a77Available colors: " + String.valueOf(colors));
        } else {
            boolean y = true;
            for (TeamColor t : TeamColor.values()) {
                if (!t.toString().equalsIgnoreCase(args[1])) continue;
                y = false;
            }
            if (y) {
                p.sendMessage("\u00a7c\u25aa \u00a77Invalid color!");
                StringBuilder colors = new StringBuilder("\u00a77");
                for (TeamColor t : TeamColor.values()) {
                    colors.append(t.chat()).append((Object)t).append(ChatColor.GRAY).append(", ");
                }
                colors = new StringBuilder(colors.substring(0, colors.toString().length() - 2) + String.valueOf(ChatColor.GRAY) + ".");
                p.sendMessage("\u00a76 \u25aa \u00a77Available colors: " + String.valueOf(colors));
            } else {
                if (ss.getConfig().getYml().get("Team." + args[0] + ".Color") != null) {
                    p.sendMessage("\u00a7c\u25aa \u00a77" + args[0] + " team already exists!");
                    return true;
                }
                ss.getConfig().set("Team." + args[0] + ".Color", args[1].toUpperCase());
                p.sendMessage("\u00a76 \u25aa \u00a77" + String.valueOf(TeamColor.getChatColor(args[1])) + args[0] + " \u00a77created!");
                if (ss.getSetupType() == SetupType.ASSISTED) {
                    ss.getConfig().reload();
                    int teams = ss.getConfig().getYml().getConfigurationSection("Team").getKeys(false).size();
                    int max = 1;
                    if (teams == 4) {
                        max = 2;
                    }
                    ss.getConfig().set("maxInTeam", max);
                }
            }
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
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

