/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Egg
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 */
package com.andrei1058.bedwars.listeners;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.events.gameplay.EggBridgeThrowEvent;
import com.andrei1058.bedwars.api.server.ServerType;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.tasks.EggBridgeTask;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class EggBridge
implements Listener {
    private static HashMap<Egg, EggBridgeTask> bridges = new HashMap();

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        Player shooter;
        IArena arena;
        Egg projectile;
        if (BedWars.getServerType() == ServerType.MULTIARENA && event.getEntity().getLocation().getWorld().getName().equalsIgnoreCase(BedWars.getLobbyWorld())) {
            event.setCancelled(true);
            return;
        }
        if (event.getEntity() instanceof Egg && (projectile = (Egg)event.getEntity()).getShooter() instanceof Player && (arena = Arena.getArenaByPlayer(shooter = (Player)projectile.getShooter())) != null && arena.isPlayer(shooter)) {
            EggBridgeThrowEvent throwEvent = new EggBridgeThrowEvent(shooter, arena);
            Bukkit.getPluginManager().callEvent((Event)throwEvent);
            if (event.isCancelled()) {
                event.setCancelled(true);
                return;
            }
            bridges.put(projectile, new EggBridgeTask(shooter, projectile, arena.getTeam(shooter).getColor()));
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Egg) {
            EggBridge.removeEgg((Egg)e.getEntity());
        }
    }

    public static void removeEgg(Egg e) {
        if (bridges.containsKey(e)) {
            if (bridges.get(e) != null) {
                bridges.get(e).cancel();
            }
            bridges.remove(e);
        }
    }

    public static Map<Egg, EggBridgeTask> getBridges() {
        return Collections.unmodifiableMap(bridges);
    }
}

