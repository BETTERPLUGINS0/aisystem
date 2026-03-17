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
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.legacy.event;

import com.sk89q.worldguard.bukkit.event.block.UseBlockEvent;
import com.sk89q.worldguard.bukkit.event.entity.DamageEntityEvent;
import com.sk89q.worldguard.bukkit.event.entity.UseEntityEvent;
import com.sk89q.worldguard.protection.events.DisallowedPVPEvent;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.event.WrappedDamageEntityEvent;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.event.WrappedDisallowedPVPEvent;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.event.WrappedUseBlockEvent;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.event.WrappedUseEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class EventListener
implements Listener {
    @EventHandler(priority=EventPriority.LOW)
    public void onUseBlock(UseBlockEvent worldGuardEvent) {
        Player player = worldGuardEvent.getCause().getFirstPlayer();
        if (player == null) {
            return;
        }
        WrappedUseBlockEvent event = new WrappedUseBlockEvent(worldGuardEvent.getOriginalEvent(), player, worldGuardEvent.getWorld(), worldGuardEvent.getBlocks(), worldGuardEvent.getEffectiveMaterial());
        Bukkit.getServer().getPluginManager().callEvent((Event)event);
        if (event.getResult() != Event.Result.DEFAULT) {
            worldGuardEvent.setResult(event.getResult());
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onUseEntity(UseEntityEvent worldGuardEvent) {
        Player player = worldGuardEvent.getCause().getFirstPlayer();
        if (player == null) {
            return;
        }
        WrappedUseEntityEvent event = new WrappedUseEntityEvent(worldGuardEvent.getOriginalEvent(), player, worldGuardEvent.getTarget(), worldGuardEvent.getEntity());
        Bukkit.getServer().getPluginManager().callEvent((Event)event);
        if (event.getResult() != Event.Result.DEFAULT) {
            worldGuardEvent.setResult(event.getResult());
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onDamageEntity(DamageEntityEvent worldGuardEvent) {
        Player player = worldGuardEvent.getCause().getFirstPlayer();
        if (player == null) {
            return;
        }
        WrappedDamageEntityEvent event = new WrappedDamageEntityEvent(worldGuardEvent.getOriginalEvent(), player, worldGuardEvent.getTarget(), worldGuardEvent.getEntity());
        Bukkit.getServer().getPluginManager().callEvent((Event)event);
        if (event.getResult() != Event.Result.DEFAULT) {
            worldGuardEvent.setResult(event.getResult());
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onDisallowedPVP(DisallowedPVPEvent worldGuardEvent) {
        WrappedDisallowedPVPEvent event = new WrappedDisallowedPVPEvent(worldGuardEvent.getAttacker(), worldGuardEvent.getDefender(), worldGuardEvent.getCause());
        Bukkit.getServer().getPluginManager().callEvent((Event)event);
        if (event.getResult() != Event.Result.DEFAULT) {
            worldGuardEvent.setCancelled(event.getResult() == Event.Result.DENY);
        }
    }
}

