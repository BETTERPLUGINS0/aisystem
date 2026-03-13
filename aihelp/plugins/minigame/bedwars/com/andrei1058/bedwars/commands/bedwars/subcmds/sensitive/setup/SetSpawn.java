/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.Misc;
import com.andrei1058.bedwars.configuration.Permissions;
import com.andrei1058.bedwars.support.paper.TeleportManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SetSpawn
extends SubCommand {
    public SetSpawn(ParentCommand parent, String name) {
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
            p.sendMessage(ss.getPrefix() + String.valueOf(ChatColor.RED) + "Usage: /" + BedWars.mainCmd + " setSpawn <team>");
            if (ss.getConfig().getYml().get("Team") != null) {
                for (String team : Objects.requireNonNull(ss.getConfig().getYml().getConfigurationSection("Team")).getKeys(false)) {
                    if (ss.getConfig().getYml().get("Team." + team + ".Spawn") != null) continue;
                    p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "Set spawn for: " + String.valueOf(ss.getTeamColor(team)) + team + " " + ChatColor.getLastColors((String)ss.getPrefix()) + "(click to set)", String.valueOf(ChatColor.WHITE) + "Set spawn for " + String.valueOf(ss.getTeamColor(team)) + team, "/" + BedWars.mainCmd + " setSpawn " + team, ClickEvent.Action.RUN_COMMAND));
                }
            }
        } else if (ss.getConfig().getYml().get("Team." + args[0]) == null) {
            p.sendMessage(ss.getPrefix() + String.valueOf(ChatColor.RED) + "Could not find target team: " + String.valueOf(ChatColor.RED) + args[0]);
            if (ss.getConfig().getYml().get("Team") != null) {
                p.sendMessage(ss.getPrefix() + "Teams list: ");
                for (String team : Objects.requireNonNull(ss.getConfig().getYml().getConfigurationSection("Team")).getKeys(false)) {
                    p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(String.valueOf(ChatColor.GOLD) + " \u25aa " + String.valueOf(ss.getTeamColor(team)) + team + " " + ChatColor.getLastColors((String)ss.getPrefix()) + "(click to set)", String.valueOf(ChatColor.WHITE) + "Set spawn for " + String.valueOf(ss.getTeamColor(team)) + team, "/" + BedWars.mainCmd + " setSpawn " + team, ClickEvent.Action.RUN_COMMAND));
                }
            }
        } else {
            if (ss.getConfig().getYml().get("Team." + args[0] + ".Spawn") != null) {
                Misc.removeArmorStand("spawn", ss.getConfig().getArenaLoc("Team." + args[0] + ".Spawn"), ss.getConfig().getString("Team." + args[0] + ".Spawn"));
            }
            ss.getConfig().saveArenaLoc("Team." + args[0] + ".Spawn", p.getLocation());
            String teamm = String.valueOf(ss.getTeamColor(args[0])) + args[0];
            p.sendMessage(String.valueOf(ChatColor.GOLD) + " \u25aa Spawn set for: " + teamm);
            Misc.createArmorStand(teamm + " " + String.valueOf(ChatColor.GOLD) + "SPAWN SET", p.getLocation(), ss.getConfig().stringLocationArenaFormat(p.getLocation()));
            int radius = ss.getConfig().getInt("island-radius");
            Location l = p.getLocation();
            for (int x = -radius; x < radius; ++x) {
                for (int y = -radius; y < radius; ++y) {
                    for (int z = -radius; z < radius; ++z) {
                        Block b = l.clone().add((double)x, (double)y, (double)z).getBlock();
                        if (!BedWars.nms.isBed(b.getType())) continue;
                        TeleportManager.teleport((Entity)p, b.getLocation());
                        Bukkit.dispatchCommand((CommandSender)p, (String)(this.getParent().getName() + " setBed " + args[0]));
                        return true;
                    }
                }
            }
            if (ss.getConfig().getYml().get("Team") != null) {
                StringBuilder remainging = new StringBuilder();
                for (String team : Objects.requireNonNull(ss.getConfig().getYml().getConfigurationSection("Team")).getKeys(false)) {
                    if (ss.getConfig().getYml().get("Team." + team + ".Spawn") != null) continue;
                    remainging.append(ss.getTeamColor(team)).append(team).append(" ");
                }
                if (remainging.toString().length() > 0) {
                    p.sendMessage(ss.getPrefix() + "Remaining: " + remainging.toString());
                }
            }
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

