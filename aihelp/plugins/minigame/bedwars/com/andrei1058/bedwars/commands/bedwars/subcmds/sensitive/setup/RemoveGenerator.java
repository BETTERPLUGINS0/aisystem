/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
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
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveGenerator
extends SubCommand {
    public RemoveGenerator(ParentCommand parent, String name) {
        super(parent, name);
        this.setArenaSetupCommand(true);
        this.setPermission(Permissions.PERMISSION_SETUP_ARENA);
    }

    @Override
    public boolean execute(String[] args, CommandSender s) {
        if (!(s instanceof Player)) {
            return false;
        }
        Player p = (Player)s;
        SetupSession ss = SetupSession.getSession(p.getUniqueId());
        if (ss == null) {
            return false;
        }
        if (args.length == 0) {
            List<String> list;
            String[] toRemove = new String[]{"", "", ""};
            Location nearest = null;
            if (ss.getConfig().getYml().get("Team") != null) {
                for (String team : ss.getConfig().getYml().getConfigurationSection("Team").getKeys(false)) {
                    for (String type : new String[]{"Iron", "Gold", "Emerald"}) {
                        if (ss.getConfig().getYml().get("Team." + team + "." + type) == null) continue;
                        for (String loc : ss.getConfig().getList("Team." + team + "." + type)) {
                            Location loc2 = ss.getConfig().convertStringToArenaLocation(loc);
                            if (loc2 == null || !(p.getLocation().distance(loc2) <= 2.0)) continue;
                            if (nearest != null) {
                                if (!(p.getLocation().distance(nearest) > p.getLocation().distance(loc2))) continue;
                                nearest = loc2;
                                toRemove[0] = type;
                                toRemove[1] = loc;
                                toRemove[2] = team;
                                continue;
                            }
                            nearest = loc2;
                            toRemove[0] = type;
                            toRemove[1] = loc;
                            toRemove[2] = team;
                        }
                    }
                }
            }
            if (ss.getConfig().getYml().get("generator") != null) {
                for (String type : new String[]{"Emerald", "Diamond"}) {
                    if (ss.getConfig().getYml().get("generator." + type) == null) continue;
                    for (String loc : ss.getConfig().getList("generator." + type)) {
                        Location loc2 = ss.getConfig().convertStringToArenaLocation(loc);
                        if (loc2 == null || !(p.getLocation().distance(loc2) <= 2.0)) continue;
                        if (nearest != null) {
                            if (!(p.getLocation().distance(nearest) > p.getLocation().distance(loc2))) continue;
                            nearest = loc2;
                            toRemove[0] = type;
                            toRemove[1] = loc;
                            toRemove[2] = "";
                            continue;
                        }
                        nearest = loc2;
                        toRemove[0] = type;
                        toRemove[1] = loc;
                        toRemove[2] = "";
                    }
                }
            }
            if (nearest == null) {
                p.sendMessage(ss.getPrefix() + "Could not find any nearby generator (Range 2x2).");
                p.sendMessage(ss.getPrefix() + "You mast stand close to the generator hologram's you want to remove.");
                BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.RED) + "Could not find any nearby generator.", 5, 40, 5);
                Sounds.playSound("shop-insufficient-money", p);
                return true;
            }
            if (toRemove[2].isEmpty()) {
                list = ss.getConfig().getList("generator." + toRemove[0]);
                list.remove(toRemove[1]);
                ss.getConfig().set("generator." + toRemove[0], list);
                p.sendMessage(ss.getPrefix() + "Removed " + toRemove[0] + " generator at location: X:" + nearest.getBlockX() + " Y:" + nearest.getBlockY() + " Z:" + nearest.getZ());
                BedWars.nms.sendTitle(p, " ", String.valueOf(ChatColor.GREEN) + toRemove[0] + " generator removed.", 5, 40, 5);
                Sounds.playSound("shop-bought", p);
                Misc.removeArmorStand(toRemove[0], nearest, toRemove[1]);
                return true;
            }
            if (ss.getSetupType() == SetupType.ASSISTED) {
                ss.getConfig().set("Team." + toRemove[2] + ".Emerald", new ArrayList());
                ss.getConfig().set("Team." + toRemove[2] + ".Iron", new ArrayList());
                ss.getConfig().set("Team." + toRemove[2] + ".Gold", new ArrayList());
                BedWars.nms.sendTitle(p, " ", String.valueOf(ss.getTeamColor(toRemove[2])) + toRemove[2] + " generator was removed.", 5, 40, 5);
                Sounds.playSound("shop-bought", p);
                Misc.removeArmorStand(null, nearest, toRemove[1]);
                p.sendMessage(ss.getPrefix() + String.valueOf(ss.getTeamColor(toRemove[2])) + toRemove[2] + ChatColor.getLastColors((String)ss.getPrefix()) + " generators were removed!");
                return true;
            }
            list = ss.getConfig().getList("Team." + toRemove[2] + "." + toRemove[0]);
            list.remove(toRemove[1]);
            ss.getConfig().set("Team." + toRemove[2] + "." + toRemove[0], list);
            p.sendMessage(ss.getPrefix() + "Removed " + String.valueOf(ss.getTeamColor(toRemove[2])) + toRemove[2] + " " + ChatColor.getLastColors((String)ss.getPrefix()) + toRemove[0] + " generator at location: X:" + nearest.getBlockX() + " Y:" + nearest.getBlockY() + " Z:" + nearest.getZ());
            BedWars.nms.sendTitle(p, " ", String.valueOf(ss.getTeamColor(toRemove[2])) + toRemove[2] + " " + String.valueOf(ChatColor.GREEN) + toRemove[0] + " generator removed.", 5, 40, 5);
            Sounds.playSound("shop-bought", p);
            Misc.removeArmorStand(toRemove[0], nearest, toRemove[1]);
            return true;
        }
        return true;
    }

    @Override
    public List<String> getTabComplete() {
        return new ArrayList<String>();
    }
}

