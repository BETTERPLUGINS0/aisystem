/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
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
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.arena.SetupSession;
import com.andrei1058.bedwars.configuration.Permissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class AutoCreateTeams
extends SubCommand {
    private static HashMap<Player, Long> timeOut = new HashMap();
    private static HashMap<Player, List<Byte>> teamsFoundOld = new HashMap();
    private static HashMap<Player, List<String>> teamsFound13 = new HashMap();

    public AutoCreateTeams(ParentCommand parent, String name) {
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
        if (ss.getSetupType() == SetupType.ASSISTED) {
            if (AutoCreateTeams.is13Higher()) {
                if (timeOut.containsKey(p) && timeOut.get(p) >= System.currentTimeMillis() && teamsFound13.containsKey(p)) {
                    for (String tf : teamsFound13.get(p)) {
                        Bukkit.dispatchCommand((CommandSender)s, (String)(BedWars.mainCmd + " createTeam " + TeamColor.enName(tf) + " " + TeamColor.enName(tf)));
                    }
                    if (ss.getConfig().getYml().get("waiting.Pos1") == null) {
                        s.sendMessage("");
                        s.sendMessage("\u00a76\u00a7lWAITING LOBBY REMOVAL:");
                        s.sendMessage("\u00a7fIf you'd like the lobby to disappear when the game starts,");
                        s.sendMessage("\u00a7fplease use the following commands like a world edit selection.");
                        p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a7c \u25aa \u00a77/" + BedWars.mainCmd + " waitingPos 1", "\u00a7dSet pos 1", "/" + this.getParent().getName() + " waitingPos 1", ClickEvent.Action.RUN_COMMAND));
                        p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a7c \u25aa \u00a77/" + BedWars.mainCmd + " waitingPos 2", "\u00a7dSet pos 2", "/" + this.getParent().getName() + " waitingPos 2", ClickEvent.Action.RUN_COMMAND));
                        s.sendMessage("");
                        s.sendMessage("\u00a77This step is OPTIONAL. If you wan to skip it do \u00a76/" + BedWars.mainCmd);
                    }
                    return true;
                }
                ArrayList<String> found = new ArrayList<String>();
                World w = p.getWorld();
                if (ss.getConfig().getYml().get("Team") == null) {
                    p.sendMessage("\u00a76 \u25aa \u00a77Searching for teams. This may cause lag.");
                    for (int x = -200; x < 200; ++x) {
                        for (int y = 50; y < 130; ++y) {
                            for (int z = -200; z < 200; ++z) {
                                Block b = new Location(w, (double)x, (double)y, (double)z).getBlock();
                                if (!b.getType().toString().contains("_WOOL") || found.contains(b.getType().toString())) continue;
                                int count = 0;
                                for (int x1 = -2; x1 < 2; ++x1) {
                                    for (int y1 = -2; y1 < 2; ++y1) {
                                        for (int z1 = -2; z1 < 2; ++z1) {
                                            Block b2 = new Location(w, (double)x, (double)y, (double)z).getBlock();
                                            if (b2.getType() != b.getType()) continue;
                                            ++count;
                                        }
                                    }
                                }
                                if (count < 5 || TeamColor.enName(b.getType().toString()).isEmpty() || ss.getConfig().getYml().get("Team." + TeamColor.enName(b.getType().toString())) != null) continue;
                                found.add(b.getType().toString());
                            }
                        }
                    }
                }
                if (found.isEmpty()) {
                    p.sendMessage("\u00a76 \u25aa \u00a77No new teams were found.\n\u00a76 \u25aa \u00a77Manually create teams with: \u00a76/" + BedWars.mainCmd + " createTeam");
                } else {
                    if (timeOut.containsKey(p)) {
                        p.sendMessage("\u00a7c \u25aa \u00a77Time out. Type again to search for teams.");
                        timeOut.remove(p);
                        return true;
                    }
                    timeOut.put(p, System.currentTimeMillis() + 16000L);
                    if (teamsFound13.containsKey(p)) {
                        teamsFound13.replace(p, found);
                    } else {
                        teamsFound13.put(p, found);
                    }
                    p.sendMessage("\u00a76\u00a7lNEW TEAMS FOUND:");
                    for (String tf : found) {
                        String name = TeamColor.enName(tf);
                        p.sendMessage("\u00a7f \u25aa " + String.valueOf(TeamColor.getChatColor(name)) + name.replace("_", " "));
                    }
                    p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a76 \u25aa \u00a77\u00a7lClick here to create found teams.", "\u00a7fClick to create found teams!", "/" + this.getParent().getName() + " " + this.getSubCommandName(), ClickEvent.Action.RUN_COMMAND));
                }
            } else {
                if (timeOut.containsKey(p) && timeOut.get(p) >= System.currentTimeMillis() && teamsFoundOld.containsKey(p)) {
                    for (Byte tf : teamsFoundOld.get(p)) {
                        Bukkit.dispatchCommand((CommandSender)s, (String)(BedWars.mainCmd + " createTeam " + TeamColor.enName(tf) + " " + TeamColor.enName(tf)));
                    }
                    if (ss.getConfig().getYml().get("waiting.Pos1") == null) {
                        s.sendMessage("");
                        s.sendMessage("\u00a76\u00a7lWAITING LOBBY REMOVAL:");
                        s.sendMessage("\u00a7fIf you'd like the lobby to disappear when the game starts,");
                        s.sendMessage("\u00a7fplease use the following commands like a world edit selection.");
                        p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a7c \u25aa \u00a77/" + BedWars.mainCmd + " waitingPos 1", "\u00a7dSet pos 1", "/" + this.getParent().getName() + " waitingPos 1", ClickEvent.Action.RUN_COMMAND));
                        p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a7c \u25aa \u00a77/" + BedWars.mainCmd + " waitingPos 2", "\u00a7dSet pos 2", "/" + this.getParent().getName() + " waitingPos 2", ClickEvent.Action.RUN_COMMAND));
                        s.sendMessage("");
                        s.sendMessage("\u00a77This step is OPTIONAL. If you wan to skip it do \u00a76/" + BedWars.mainCmd);
                    }
                    return true;
                }
                ArrayList<Byte> found = new ArrayList<Byte>();
                World w = p.getWorld();
                if (ss.getConfig().getYml().get("Team") == null) {
                    p.sendMessage("\u00a76 \u25aa \u00a77Searching for teams. This may cause lag.");
                    for (int x = -200; x < 200; ++x) {
                        for (int y = 50; y < 130; ++y) {
                            for (int z = -200; z < 200; ++z) {
                                Block b = new Location(w, (double)x, (double)y, (double)z).getBlock();
                                if (b.getType() != Material.valueOf((String)"WOOL") || found.contains(b.getData())) continue;
                                int count = 0;
                                for (int x1 = -2; x1 < 2; ++x1) {
                                    for (int y1 = -2; y1 < 2; ++y1) {
                                        for (int z1 = -2; z1 < 2; ++z1) {
                                            Block b2 = new Location(w, (double)x, (double)y, (double)z).getBlock();
                                            if (b2.getType() != b.getType() || b.getData() != b2.getData()) continue;
                                            ++count;
                                        }
                                    }
                                }
                                if (count < 5 || TeamColor.enName(b.getData()).isEmpty() || ss.getConfig().getYml().get("Team." + TeamColor.enName(b.getData())) != null) continue;
                                found.add(b.getData());
                            }
                        }
                    }
                }
                if (found.isEmpty()) {
                    p.sendMessage("\u00a76 \u25aa \u00a77No new teams were found.\n\u00a76 \u25aa \u00a77Manually create teams with: \u00a76/" + BedWars.mainCmd + " createTeam");
                } else {
                    if (timeOut.containsKey(p)) {
                        p.sendMessage("\u00a7c \u25aa \u00a77Time out. Type again to search for teams.");
                        timeOut.remove(p);
                        return true;
                    }
                    timeOut.put(p, System.currentTimeMillis() + 16000L);
                    if (teamsFoundOld.containsKey(p)) {
                        teamsFoundOld.replace(p, found);
                    } else {
                        teamsFoundOld.put(p, found);
                    }
                    p.sendMessage("\u00a76\u00a7lNEW TEAMS FOUND:");
                    for (Byte tf : found) {
                        String name = TeamColor.enName(tf);
                        p.sendMessage("\u00a7f \u25aa " + String.valueOf(TeamColor.getChatColor(name)) + name.replace('_', ' '));
                    }
                    p.spigot().sendMessage((BaseComponent)Misc.msgHoverClick("\u00a76 \u25aa \u00a77\u00a7lClick here to create found teams.", "\u00a7fClick to create found teams!", "/" + this.getParent().getName() + " " + this.getSubCommandName(), ClickEvent.Action.RUN_COMMAND));
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return null;
    }

    public static boolean is13Higher() {
        return !BedWars.getServerVersion().equals("v1_8_R3") && !BedWars.getServerVersion().equals("v1_12_R1");
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

