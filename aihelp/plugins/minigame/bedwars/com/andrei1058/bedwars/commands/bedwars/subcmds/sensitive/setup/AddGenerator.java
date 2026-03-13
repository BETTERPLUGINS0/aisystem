/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.setup;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.server.SetupType;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.Misc;
import com.andrei1058.bedwars.configuration.Permissions;
import com.andrei1058.bedwars.configuration.Sounds;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AddGenerator
extends SubCommand {
    public AddGenerator(ParentCommand parent, String name) {
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
        if (args.length == 0 && ss.getSetupType() == SetupType.ASSISTED) {
            String team = ss.getNearestTeam();
            if (team.isEmpty()) {
                if (p.getLocation().add(0.0, -1.0, 0.0).getBlock().getType() == Material.DIAMOND_BLOCK) {
                    Bukkit.dispatchCommand((CommandSender)p, (String)(this.getParent().getName() + " " + this.getSubCommandName() + " diamond"));
                    return true;
                }
                if (p.getLocation().add(0.0, -1.0, 0.0).getBlock().getType() == Material.EMERALD_BLOCK) {
                    Bukkit.dispatchCommand((CommandSender)p, (String)(this.getParent().getName() + " " + this.getSubCommandName() + " emerald"));
                    return true;
                }
                p.sendMessage(ss.getPrefix() + String.valueOf(ChatColor.RED) + "Could not find any nearby team.");
                p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "Make sure you set the team's spawn first!", String.valueOf(ChatColor.WHITE) + "Set a team spawn.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
                p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "Or if you set the spawn and it wasn't found automatically try using: /bw addGenerator <team>", "Add a team generator.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
                p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "Other use: /bw addGenerator <emerald/ diamond>", "Add an emerald/ diamond generator.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
                BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.RED) + "Could not find any nearby team.", 5, 60, 5);
                Sounds.playSound("shop-insufficient-money", p);
                return true;
            }
            AddGenerator.saveTeamGen(p.getLocation(), team, ss, "Iron");
            AddGenerator.saveTeamGen(p.getLocation(), team, ss, "Gold");
            AddGenerator.saveTeamGen(p.getLocation(), team, ss, "Emerald");
            Misc.createArmorStand(String.valueOf(ChatColor.GOLD) + "Generator set for team: " + String.valueOf(ss.getTeamColor(team)) + team, p.getLocation(), ss.getConfig().stringLocationArenaFormat(p.getLocation()));
            p.sendMessage(ss.getPrefix() + "Generator set for team: " + String.valueOf(ss.getTeamColor(team)) + team);
            Bukkit.dispatchCommand((CommandSender)p, (String)this.getParent().getName());
            BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.GREEN) + "Generator set for team: " + String.valueOf(ss.getTeamColor(team)) + team, 5, 60, 5);
            Sounds.playSound("shop-bought", p);
            return true;
        }
        if (args.length == 1 && (args[0].equalsIgnoreCase("diamond") || args[0].equalsIgnoreCase("emerald"))) {
            List<Location> locations = ss.getConfig().getArenaLocations("generator." + args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase());
            for (Location l : locations) {
                if (!ss.getConfig().compareArenaLoc(l, p.getLocation())) continue;
                p.sendMessage(ss.getPrefix() + String.valueOf(ChatColor.RED) + "This generator was already set!");
                BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.RED) + "This generator was already set!", 5, 30, 5);
                Sounds.playSound("shop-insufficient-money", p);
                return true;
            }
            String gen = args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase();
            ArrayList saved = ss.getConfig().getYml().get("generator." + gen) == null ? new ArrayList() : (ArrayList)ss.getConfig().getYml().getStringList("generator." + gen);
            saved.add(ss.getConfig().stringLocationArenaFormat(p.getLocation()));
            ss.getConfig().set("generator." + gen, saved);
            p.sendMessage(ss.getPrefix() + gen + " generator was added!");
            Misc.createArmorStand(String.valueOf(ChatColor.GOLD) + gen + " SET", p.getLocation(), ss.getConfig().stringLocationArenaFormat(p.getLocation()));
            if (ss.getSetupType() == SetupType.ASSISTED) {
                Bukkit.dispatchCommand((CommandSender)p, (String)this.getParent().getName());
            }
            BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.GOLD) + gen + String.valueOf(ChatColor.GREEN) + " generator added!", 5, 60, 5);
            Sounds.playSound("shop-bought", p);
            return true;
        }
        if (args.length >= 1 && (args[0].equalsIgnoreCase("iron") || args[0].equalsIgnoreCase("gold") || args[0].equalsIgnoreCase("upgrade")) && ss.getSetupType() == SetupType.ADVANCED) {
            String team;
            if (args.length == 1) {
                team = ss.getNearestTeam();
            } else {
                team = args[1];
                if (ss.getConfig().getYml().get("Team." + team + ".Color") == null) {
                    p.sendMessage(ss.getPrefix() + String.valueOf(ChatColor.RED) + "Could not find team: " + team);
                    p.sendMessage(ss.getPrefix() + "Use: /bw createTeam if you want to create one.");
                    ss.displayAvailableTeams();
                    BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.RED) + "Could not find any nearby team.", 5, 60, 5);
                    Sounds.playSound("shop-insufficient-money", p);
                    return true;
                }
            }
            if (team.isEmpty()) {
                p.sendMessage(ss.getPrefix() + String.valueOf(ChatColor.RED) + "Could not find any nearby team.");
                p.sendMessage(ss.getPrefix() + "Try using: /bw addGenerator <iron/ gold/ upgrade> <team>");
                return true;
            }
            Object gen = args[0].substring(0, 1).toUpperCase() + args[0].substring(1).toLowerCase();
            if (((String)gen).equalsIgnoreCase("upgrade")) {
                gen = "Emerald";
            }
            Misc.createArmorStand(String.valueOf(ChatColor.GOLD) + (String)gen + " generator added for team: " + String.valueOf(ss.getTeamColor(team)) + team, p.getLocation(), ss.getConfig().stringLocationArenaFormat(p.getLocation()));
            p.sendMessage(ss.getPrefix() + (String)gen + " generator added for team: " + String.valueOf(ss.getTeamColor(team)) + team);
            AddGenerator.saveTeamGen(p.getLocation(), team, ss, (String)gen);
            BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.GOLD) + (String)gen + String.valueOf(ChatColor.GREEN) + " generator for " + String.valueOf(ss.getTeamColor(team)) + team + String.valueOf(ChatColor.GREEN) + " was added!", 5, 60, 5);
            Sounds.playSound("shop-bought", p);
            return true;
        }
        if (args.length == 1 && ss.getSetupType() == SetupType.ASSISTED) {
            String team = args[0];
            if (ss.getConfig().getYml().get("Team." + team + ".Color") == null) {
                p.sendMessage(ss.getPrefix() + "Could not find team: " + String.valueOf(ChatColor.RED) + team);
                p.sendMessage(ss.getPrefix() + "Use: /bw createTeam if you want to create one.");
                ss.displayAvailableTeams();
                BedWars.nms.sendTitle(p, " ", "Could not find team: " + String.valueOf(ChatColor.RED) + team, 5, 40, 5);
                Sounds.playSound("shop-insufficient-money", p);
                return true;
            }
            AddGenerator.saveTeamGen(p.getLocation(), team, ss, "Iron");
            AddGenerator.saveTeamGen(p.getLocation(), team, ss, "Gold");
            AddGenerator.saveTeamGen(p.getLocation(), team, ss, "Emerald");
            Misc.createArmorStand(String.valueOf(ChatColor.GOLD) + "Generator set for team: " + String.valueOf(ss.getTeamColor(team)) + team, p.getLocation(), ss.getConfig().stringLocationArenaFormat(p.getLocation()));
            p.sendMessage(ss.getPrefix() + "Generator set for team: " + String.valueOf(ss.getTeamColor(team)) + team);
            Bukkit.dispatchCommand((CommandSender)p, (String)this.getParent().getName());
            BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.GREEN) + "Generator set for team: " + String.valueOf(ss.getTeamColor(team)) + team, 5, 60, 5);
            Sounds.playSound("shop-bought", p);
            return true;
        }
        if (ss.getSetupType() == SetupType.ASSISTED) {
            p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "/bw addGenerator (detect team automatically)", "Add a team generator.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "/bw addGenerator <team>", "Add a team generator.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
        }
        if (ss.getSetupType() == SetupType.ADVANCED) {
            p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "/bw addGenerator <iron/ gold/ upgrade>", "Add a team generator.\nThe team will be detected automatically.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
            p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "/bw addGenerator <iron/ gold/ upgrade> <team>", "Add a team generator.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
        }
        p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "/bw addGenerator <emerald/ diamond>", "Add an emerald/ diamond generator.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return Arrays.asList("Diamond", "Emerald", "Iron", "Gold", "Upgrade");
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

    private static void saveTeamGen(Location l, String t, @NotNull SetupSession ss, String type) {
        List<String> locs;
        Object o = ss.getConfig().getYml().get("Team." + t + "." + type);
        if (o == null) {
            locs = new ArrayList<String>();
        } else if (o instanceof String) {
            locs = new ArrayList();
            locs.add((String)o);
        } else {
            locs = ss.getConfig().getList("Team." + t + "." + type);
        }
        locs.add(ss.getConfig().stringLocationArenaFormat(l));
        ss.getConfig().set("Team." + t + "." + type, locs);
    }
}

