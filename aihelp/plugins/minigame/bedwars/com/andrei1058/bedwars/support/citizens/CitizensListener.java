/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.citizensnpcs.api.CitizensAPI
 *  net.citizensnpcs.api.event.NPCRemoveEvent
 *  net.citizensnpcs.api.npc.NPC
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 */
package com.andrei1058.bedwars.support.citizens;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.support.citizens.JoinNPC;
import java.util.ArrayList;
import java.util.List;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRemoveEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class CitizensListener
implements Listener {
    @EventHandler
    public void removeNPC(NPCRemoveEvent e) {
        if (e == null) {
            return;
        }
        if (e.getNPC() == null) {
            return;
        }
        if (e.getNPC().getEntity() == null) {
            return;
        }
        List locations = BedWars.config.getYml().getStringList("join-npc-locations");
        boolean removed = false;
        if (JoinNPC.npcs.containsKey(e.getNPC().getId())) {
            JoinNPC.npcs.remove(e.getNPC().getId());
            removed = true;
        }
        for (String s : new ArrayList(locations)) {
            String[] data = s.split(",");
            if (data.length < 10 || !Misc.isNumber(data[9]) || Integer.parseInt(data[9]) != e.getNPC().getId()) continue;
            locations.remove(s);
            removed = true;
        }
        for (Entity e2 : e.getNPC().getEntity().getNearbyEntities(0.0, 3.0, 0.0)) {
            if (e2.getType() != EntityType.ARMOR_STAND) continue;
            e2.remove();
        }
        if (removed) {
            BedWars.config.set("join-npc-locations", locations);
        }
    }

    @EventHandler
    public void onNPCInteract(PlayerInteractEntityEvent e) {
        if (!JoinNPC.isCitizensSupport()) {
            return;
        }
        if (e.getPlayer().isSneaking()) {
            return;
        }
        if (!e.getRightClicked().hasMetadata("NPC")) {
            return;
        }
        NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getRightClicked());
        if (npc == null) {
            return;
        }
        if (JoinNPC.npcs.containsKey(npc.getId())) {
            if (!Arena.joinRandomFromGroup(e.getPlayer(), JoinNPC.npcs.get(npc.getId()))) {
                e.getPlayer().sendMessage(Language.getMsg(e.getPlayer(), Messages.COMMAND_JOIN_NO_EMPTY_FOUND));
                Sounds.playSound("join-denied", e.getPlayer());
            } else {
                Sounds.playSound("join-allowed", e.getPlayer());
            }
        }
    }
}

