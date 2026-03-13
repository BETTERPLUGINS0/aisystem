/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  net.citizensnpcs.api.npc.NPC
 *  net.citizensnpcs.npc.skin.SkinnableEntity
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.support.citizens;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.command.SubCommand;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.commands.bedwars.MainCommand;
import com.andrei1058.bedwars.commands.bedwars.subcmds.sensitive.NPC;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.Nullable;

public class JoinNPC {
    private static boolean citizensSupport = false;
    public static HashMap<ArmorStand, List<String>> npcs_holos = new HashMap();
    public static HashMap<Integer, String> npcs = new HashMap();

    public static boolean isCitizensSupport() {
        return citizensSupport;
    }

    public static void setCitizensSupport(boolean citizensSupport) {
        JoinNPC.citizensSupport = citizensSupport;
        MainCommand bw = MainCommand.getInstance();
        if (bw == null) {
            return;
        }
        if (citizensSupport) {
            if (bw.isRegistered()) {
                boolean registered = false;
                for (SubCommand sc2 : bw.getSubCommands()) {
                    if (!sc2.getSubCommandName().equalsIgnoreCase("npc")) continue;
                    registered = true;
                    break;
                }
                if (!registered) {
                    new NPC(bw, "npc");
                }
            }
        } else if (bw.isRegistered()) {
            bw.getSubCommands().removeIf(sc -> sc.getSubCommandName().equalsIgnoreCase("npc"));
        }
    }

    @Nullable
    public static net.citizensnpcs.api.npc.NPC spawnNPC(Location l, String name, String group, String skin, net.citizensnpcs.api.npc.NPC spawnExisting) {
        if (!JoinNPC.isCitizensSupport()) {
            return null;
        }
        net.citizensnpcs.api.npc.NPC npc = spawnExisting == null ? CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "") : spawnExisting;
        if (!npc.isSpawned()) {
            npc.spawn(l);
        }
        if (npc.getEntity() instanceof SkinnableEntity) {
            ((SkinnableEntity)npc.getEntity()).setSkinName(skin);
        }
        npc.setProtected(true);
        npc.setName("");
        String separator = "\\\\n";
        String[] nume = name.split(separator);
        for (Entity e : l.getWorld().getNearbyEntities(l, 1.0, 3.0, 1.0)) {
            if (e.getType() != EntityType.ARMOR_STAND) continue;
            e.remove();
        }
        if (nume.length >= 2) {
            ArmorStand a = NPC.createArmorStand(l.clone().add(0.0, 0.05, 0.0));
            a.setMarker(false);
            a.setCustomNameVisible(true);
            a.setCustomName(ChatColor.translateAlternateColorCodes((char)'&', (String)nume[0]).replace("{players}", String.valueOf(Arena.getPlayers(group))));
            npcs.put(npc.getId(), group);
            ArmorStand a2 = NPC.createArmorStand(l.clone().subtract(0.0, 0.25, 0.0));
            a2.setMarker(false);
            a2.setCustomName(ChatColor.translateAlternateColorCodes((char)'&', (String)nume[1].replace("{players}", String.valueOf(Arena.getPlayers(group)))));
            a2.setCustomNameVisible(true);
            npcs_holos.put(a, Arrays.asList(group, nume[0]));
            npcs_holos.put(a2, Arrays.asList(group, nume[1]));
        } else if (nume.length == 1) {
            npcs.put(npc.getId(), group);
            ArmorStand a2 = NPC.createArmorStand(l.clone().subtract(0.0, 0.25, 0.0));
            a2.setMarker(false);
            a2.setCustomName(ChatColor.translateAlternateColorCodes((char)'&', (String)nume[0]).replace("{players}", String.valueOf(Arena.getPlayers(group))));
            a2.setCustomNameVisible(true);
            npcs_holos.put(a2, Arrays.asList(group, nume[0]));
        }
        npc.teleport(l, PlayerTeleportEvent.TeleportCause.PLUGIN);
        npc.setName("");
        return npc;
    }

    public static void spawnNPCs() {
        if (!JoinNPC.isCitizensSupport()) {
            return;
        }
        if (BedWars.config.getYml().get("join-npc-locations") != null) {
            for (String s : BedWars.config.getYml().getStringList("join-npc-locations")) {
                String[] data = s.split(",");
                if (data.length < 10 || !Misc.isNumber(data[0]) || !Misc.isNumber(data[1]) || !Misc.isNumber(data[2]) || !Misc.isNumber(data[3]) || !Misc.isNumber(data[4]) || Misc.isNumber(data[5]) || Misc.isNumber(data[6]) || Misc.isNumber(data[7]) || Misc.isNumber(data[8]) || !Misc.isNumber(data[9])) continue;
                Location l = new Location(Bukkit.getWorld((String)data[5]), Double.parseDouble(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]));
                String skin = data[6];
                String name = data[7];
                String group = data[8];
                int id = Integer.parseInt(data[9]);
                net.citizensnpcs.api.npc.NPC npc = CitizensAPI.getNPCRegistry().getById(id);
                if (npc == null) {
                    BedWars.plugin.getLogger().severe("Invalid npc id: " + id);
                    continue;
                }
                JoinNPC.spawnNPC(l, name, group, skin, npc);
            }
        }
    }

    public static void updateNPCs(String group) {
        String x = String.valueOf(Arena.getPlayers(group));
        for (Map.Entry<ArmorStand, List<String>> e : npcs_holos.entrySet()) {
            if (!e.getValue().get(0).equalsIgnoreCase(group) || e.getKey() == null || e.getKey().isDead()) continue;
            e.getKey().setCustomName(ChatColor.translateAlternateColorCodes((char)'&', (String)e.getValue().get(1).replace("{players}", x)));
        }
    }
}

