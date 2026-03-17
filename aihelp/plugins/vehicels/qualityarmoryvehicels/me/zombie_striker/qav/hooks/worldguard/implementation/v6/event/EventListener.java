/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldguard.bukkit.event.block.UseBlockEvent
 *  com.sk89q.worldguard.bukkit.event.entity.DamageEntityEvent
 *  com.sk89q.worldguard.bukkit.event.entity.UseEntityEvent
 *  com.sk89q.worldguard.protection.events.DisallowedPVPEvent
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 */
package me.zombie_striker.qav.hooks.worldguard.implementation.v6.event;

import com.sk89q.worldguard.bukkit.event.block.UseBlockEvent;
import com.sk89q.worldguard.bukkit.event.entity.DamageEntityEvent;
import com.sk89q.worldguard.bukkit.event.entity.UseEntityEvent;
import com.sk89q.worldguard.protection.events.DisallowedPVPEvent;
import me.zombie_striker.qav.hooks.worldguard.event.WrappedDamageEntityEvent;
import me.zombie_striker.qav.hooks.worldguard.event.WrappedDisallowedPVPEvent;
import me.zombie_striker.qav.hooks.worldguard.event.WrappedUseBlockEvent;
import me.zombie_striker.qav.hooks.worldguard.event.WrappedUseEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EventListener
implements Listener {
    @EventHandler(priority=EventPriority.LOW)
    public void onUseBlock(UseBlockEvent useBlockEvent) {
        Player player = useBlockEvent.getCause().getFirstPlayer();
        if (player == null) {
            return;
        }
        WrappedUseBlockEvent wrappedUseBlockEvent = new WrappedUseBlockEvent(useBlockEvent.getOriginalEvent(), player, useBlockEvent.getWorld(), useBlockEvent.getBlocks(), useBlockEvent.getEffectiveMaterial());
        Bukkit.getServer().getPluginManager().callEvent((Event)wrappedUseBlockEvent);
        if (wrappedUseBlockEvent.getResult() != Event.Result.DEFAULT) {
            useBlockEvent.setResult(wrappedUseBlockEvent.getResult());
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onUseEntity(UseEntityEvent useEntityEvent) {
        Player player = useEntityEvent.getCause().getFirstPlayer();
        if (player == null) {
            return;
        }
        WrappedUseEntityEvent wrappedUseEntityEvent = new WrappedUseEntityEvent(useEntityEvent.getOriginalEvent(), player, useEntityEvent.getTarget(), useEntityEvent.getEntity());
        Bukkit.getServer().getPluginManager().callEvent((Event)wrappedUseEntityEvent);
        if (wrappedUseEntityEvent.getResult() != Event.Result.DEFAULT) {
            useEntityEvent.setResult(wrappedUseEntityEvent.getResult());
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onDamageEntity(DamageEntityEvent damageEntityEvent) {
        Player player = damageEntityEvent.getCause().getFirstPlayer();
        if (player == null) {
            return;
        }
        WrappedDamageEntityEvent wrappedDamageEntityEvent = new WrappedDamageEntityEvent(damageEntityEvent.getOriginalEvent(), player, damageEntityEvent.getTarget(), damageEntityEvent.getEntity());
        Bukkit.getServer().getPluginManager().callEvent((Event)wrappedDamageEntityEvent);
        if (wrappedDamageEntityEvent.getResult() != Event.Result.DEFAULT) {
            damageEntityEvent.setResult(wrappedDamageEntityEvent.getResult());
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onDisallowedPVP(DisallowedPVPEvent disallowedPVPEvent) {
        WrappedDisallowedPVPEvent wrappedDisallowedPVPEvent = new WrappedDisallowedPVPEvent(disallowedPVPEvent.getAttacker(), disallowedPVPEvent.getDefender(), disallowedPVPEvent.getCause());
        Bukkit.getServer().getPluginManager().callEvent((Event)wrappedDisallowedPVPEvent);
        if (wrappedDisallowedPVPEvent.getResult() != Event.Result.DEFAULT) {
            disallowedPVPEvent.setCancelled(wrappedDisallowedPVPEvent.getResult() == Event.Result.DENY);
        }
    }
}

