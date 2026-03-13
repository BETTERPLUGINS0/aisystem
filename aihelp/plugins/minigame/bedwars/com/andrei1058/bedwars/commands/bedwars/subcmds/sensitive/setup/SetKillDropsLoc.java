/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.server.SetupType;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.Misc;
import com.andrei1058.bedwars.configuration.ArenaConfig;
import com.andrei1058.bedwars.configuration.Permissions;
import com.andrei1058.bedwars.configuration.Sounds;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetKillDropsLoc
extends SubCommand {
    public SetKillDropsLoc(ParentCommand parent, String name) {
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
        ArenaConfig arena = ss.getConfig();
        if (args.length < 1) {
            String foundTeam = "";
            double distance = 100.0;
            if (ss.getConfig().getYml().getConfigurationSection("Team") == null) {
                p.sendMessage(ss.getPrefix() + "Please create teams first!");
                BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.RED) + "Please create teams first!", 5, 40, 5);
                Sounds.playSound("shop-insufficient-money", p);
                return true;
            }
            for (String team : ss.getConfig().getYml().getConfigurationSection("Team").getKeys(false)) {
                double dis;
                if (ss.getConfig().getYml().get("Team." + team + ".Spawn") == null || !((dis = ss.getConfig().getArenaLoc("Team." + team + ".Spawn").distance(p.getLocation())) <= (double)ss.getConfig().getInt("island-radius")) || !(dis < distance)) continue;
                distance = dis;
                foundTeam = team;
            }
            if (!foundTeam.isEmpty()) {
                if (ss.getConfig().getYml().get("Team." + foundTeam + ".kill-drops-loc") != null) {
                    Misc.removeArmorStand("Kill drops", ss.getConfig().getArenaLoc("Team." + foundTeam + ".kill-drops-loc"), null);
                }
                arena.set("Team." + foundTeam + ".kill-drops-loc", arena.stringLocationArenaFormat(p.getLocation()));
                String team = String.valueOf(ss.getTeamColor(foundTeam)) + foundTeam;
                p.sendMessage(ss.getPrefix() + "Kill drops set for team: " + team);
                Misc.createArmorStand(String.valueOf(ChatColor.GOLD) + "Kill drops " + team, p.getLocation(), null);
                BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.GREEN) + "Kill drops set for team: " + team, 5, 40, 5);
                Sounds.playSound("shop-bought", p);
                if (ss.getSetupType() == SetupType.ASSISTED) {
                    Bukkit.dispatchCommand((CommandSender)p, (String)this.getParent().getName());
                }
                return true;
            }
            p.sendMessage(ss.getPrefix() + String.valueOf(ChatColor.RED) + "Usage: /" + BedWars.mainCmd + " setKillDrops <teamName>");
            return true;
        }
        String foundTeam = ss.getNearestTeam();
        if (foundTeam.isEmpty()) {
            p.sendMessage("");
            p.sendMessage(ss.getPrefix() + String.valueOf(ChatColor.RED) + "Could not find any nearby team.");
            p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "Make sure you set the team's spawn first!", String.valueOf(ChatColor.WHITE) + "Set a team spawn.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "Or if you set the spawn and it wasn't found automatically try using: /bw " + this.getSubCommandName() + " <team>", "Set kill drops location for a team.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
            BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.RED) + "Could not find any nearby team.", 5, 60, 5);
            Sounds.playSound("shop-insufficient-money", p);
            return true;
        }
        if (args.length == 1) {
            if (arena.getYml().get("Team." + args[0]) != null) {
                foundTeam = args[0];
            } else {
                p.sendMessage(ss.getPrefix() + String.valueOf(ChatColor.RED) + "This team doesn't exist!");
                if (arena.getYml().get("Team") != null) {
                    p.sendMessage(ss.getPrefix() + "Available teams: ");
                    for (String team : Objects.requireNonNull(arena.getYml().getConfigurationSection("Team")).getKeys(false)) {
                        p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(String.valueOf(ChatColor.GOLD) + " \u25aa Kill drops " + String.valueOf(ss.getTeamColor(team)) + team + " " + ChatColor.getLastColors((String)ss.getPrefix()) + "(click to set)", String.valueOf(ChatColor.WHITE) + "Set Kill drops for " + String.valueOf(ss.getTeamColor(team)) + team, "/" + BedWars.mainCmd + " setKillDrops " + team, ClickEvent.Action.RUN_COMMAND));
                    }
                }
                return true;
            }
        }
        arena.set("Team." + foundTeam + ".kill-drops-loc", arena.stringLocationArenaFormat(p.getLocation()));
        p.sendMessage(ss.getPrefix() + "Kill drops set for: " + String.valueOf(ss.getTeamColor(foundTeam)) + foundTeam);
        if (ss.getSetupType() == SetupType.ASSISTED) {
            Bukkit.dispatchCommand((CommandSender)p, (String)this.getParent().getName());
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return new ArrayList<String>();
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

