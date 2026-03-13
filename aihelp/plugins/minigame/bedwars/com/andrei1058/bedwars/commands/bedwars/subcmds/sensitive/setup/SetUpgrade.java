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
import com.andrei1058.bedwars.api.arena.team.TeamColor;
import com.andrei1058.bedwars.api.command.ParentCommand;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.api.server.SetupType;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.commands.Misc;
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

public class SetUpgrade
extends SubCommand {
    public SetUpgrade(ParentCommand parent, String name) {
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
            String foundTeam = ss.getNearestTeam();
            if (foundTeam.isEmpty()) {
                p.sendMessage("");
                p.sendMessage(ss.getPrefix() + String.valueOf(ChatColor.RED) + "Could not find any nearby team.");
                p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "Make sure you set the team's spawn first!", String.valueOf(ChatColor.WHITE) + "Set a team spawn.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
                p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(ss.getPrefix() + "Or if you set the spawn and it wasn't found automatically try using: /bw " + this.getSubCommandName() + " <team>", "Set team upgrades NPC for a team.", "/" + this.getParent().getName() + " " + this.getSubCommandName() + " ", ClickEvent.Action.SUGGEST_COMMAND));
                BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.RED) + "Could not find any nearby team.", 0, 60, 10);
                Sounds.playSound("shop-insufficient-money", p);
            } else {
                Bukkit.dispatchCommand((CommandSender)s, (String)(this.getParent().getName() + " " + this.getSubCommandName() + " " + foundTeam));
            }
        } else if (ss.getConfig().getYml().get("Team." + args[0]) == null) {
            p.sendMessage(ss.getPrefix() + String.valueOf(ChatColor.RED) + "This team doesn't exist!");
            if (ss.getConfig().getYml().get("Team") != null) {
                p.sendMessage(ss.getPrefix() + "Available teams: ");
                for (String team : Objects.requireNonNull(ss.getConfig().getYml().getConfigurationSection("Team")).getKeys(false)) {
                    p.spigot().sendMessage((BaseComponent)com.andrei1058.bedwars.arena.Misc.msgHoverClick(String.valueOf(ChatColor.GOLD) + " \u25aa " + String.valueOf(ss.getTeamColor(team)) + team + " " + ChatColor.getLastColors((String)ss.getPrefix()) + "(click to set)", String.valueOf(ChatColor.WHITE) + "Upgrade npc set for " + String.valueOf(TeamColor.getChatColor(Objects.requireNonNull(ss.getConfig().getYml().getString("Team." + team + ".Color")))) + team, "/" + BedWars.mainCmd + " setUpgrade " + team, ClickEvent.Action.RUN_COMMAND));
                }
            }
        } else {
            String teamm = String.valueOf(ss.getTeamColor(args[0])) + args[0];
            if (ss.getConfig().getYml().get("Team." + args[0] + ".Upgrade") != null) {
                Misc.removeArmorStand("upgrade", ss.getConfig().getArenaLoc("Team." + args[0] + ".Upgrade"), null);
            }
            Misc.createArmorStand(teamm + " " + String.valueOf(ChatColor.GOLD) + "UPGRADE SET", p.getLocation(), null);
            ss.getConfig().saveArenaLoc("Team." + args[0] + ".Upgrade", p.getLocation());
            p.sendMessage(ss.getPrefix() + "Upgrade npc set for: " + teamm);
            if (ss.getSetupType() == SetupType.ASSISTED) {
                Bukkit.dispatchCommand((CommandSender)p, (String)this.getParent().getName());
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

