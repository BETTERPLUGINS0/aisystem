/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.Misc;
import com.andrei1058.bedwars.configuration.Permissions;
import com.andrei1058.bedwars.configuration.Sounds;
import java.util.List;
import java.util.Objects;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class RemoveTeam
extends SubCommand {
    public RemoveTeam(ParentCommand parent, String name) {
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
            return false;
        }
        if (args.length < 1) {
            p.sendMessage(ss.getPrefix() + String.valueOf(ChatColor.RED) + "Usage: /" + BedWars.mainCmd + " removeTeam <teamName>");
            if (ss.getConfig().getYml().get("Team") != null) {
                p.sendMessage(ss.getPrefix() + "Available teams: ");
                for (String team : Objects.requireNonNull(ss.getConfig().getYml().getConfigurationSection("Team")).getKeys(false)) {
                    p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(String.valueOf(ChatColor.GOLD) + " \u25aa " + String.valueOf(TeamColor.getChatColor(team)) + team, String.valueOf(ChatColor.GRAY) + "Remove " + String.valueOf(TeamColor.getChatColor(team)) + team + " " + String.valueOf(ChatColor.GRAY) + "(click to remove)", "/" + BedWars.mainCmd + " removeTeam " + team, ClickEvent.Action.RUN_COMMAND));
                }
            }
        } else if (ss.getConfig().getYml().get("Team." + args[0] + ".Color") == null) {
            p.sendMessage(ss.getPrefix() + "This team doesn't exist: " + args[0]);
            BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.RED) + "Team not found: " + args[0], 5, 40, 5);
            Sounds.playSound("shop-insufficient-money", p);
        } else {
            if (ss.getConfig().getYml().get("Team." + args[0] + ".Iron") != null) {
                for (Location loc : ss.getConfig().getArenaLocations("Team." + args[0] + ".Iron")) {
                    Misc.removeArmorStand(null, loc, null);
                }
            }
            if (ss.getConfig().getYml().get("Team." + args[0] + ".Gold") != null) {
                for (Location loc : ss.getConfig().getArenaLocations("Team." + args[0] + ".Gold")) {
                    Misc.removeArmorStand(null, loc, null);
                }
            }
            if (ss.getConfig().getYml().get("Team." + args[0] + ".Emerald") != null) {
                for (Location loc : ss.getConfig().getArenaLocations("Team." + args[0] + ".Emerald")) {
                    Misc.removeArmorStand(null, loc, null);
                }
            }
            if (ss.getConfig().getYml().get("Team." + args[0] + ".Shop") != null) {
                Misc.removeArmorStand(null, ss.getConfig().getArenaLoc("Team." + args[0] + ".Shop"), null);
            }
            if (ss.getConfig().getYml().get("Team." + args[0] + ".Upgrade") != null) {
                Misc.removeArmorStand(null, ss.getConfig().getArenaLoc("Team." + args[0] + ".Upgrade"), null);
            }
            if (ss.getConfig().getYml().get("Team." + args[0] + ".kill-drops-loc") != null) {
                Misc.removeArmorStand(null, ss.getConfig().getArenaLoc("Team." + args[0] + ".kill-drops-loc"), null);
            }
            p.sendMessage(ss.getPrefix() + "Team removed: " + String.valueOf(ss.getTeamColor(args[0])) + args[0]);
            BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.GREEN) + "Team removed: " + String.valueOf(ss.getTeamColor(args[0])) + args[0], 5, 40, 5);
            Sounds.playSound("shop-bought", p);
            ss.getConfig().set("Team." + args[0], null);
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

