/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerTeleportEvent
 */
package com.magmaguy.magmacore.instance;

import com.magmaguy.magmacore.events.MatchInstantiateEvent;
import com.magmaguy.magmacore.instance.InstanceProtector;
import com.magmaguy.magmacore.instance.MatchInstance;
import com.magmaguy.magmacore.instance.MatchInstanceConfiguration;
import com.magmaguy.magmacore.instance.MatchInstanceInterface;
import com.magmaguy.magmacore.instance.MatchPlayer;
import com.magmaguy.magmacore.util.Logger;
import com.magmaguy.magmacore.util.TemporaryWorldManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MatchInstanceWorld
extends MatchInstance
implements MatchInstanceInterface {
    private List<World> worlds;
    private File worldDirectory;

    public MatchInstanceWorld(MatchInstanceConfiguration matchInstanceConfiguration, World worlds, File worldDirectory) {
        super(matchInstanceConfiguration);
        this.worlds = new ArrayList<World>(List.of((Object)worlds));
        this.worldDirectory = worldDirectory;
        if (matchInstanceConfiguration.isProtected()) {
            InstanceProtector.addProtectedWorld(worlds);
        }
        if (matchInstanceConfiguration.isPvpPrevented()) {
            InstanceProtector.addPvpPreventedWorld(worlds);
        }
        if (matchInstanceConfiguration.isRedstonePrevented()) {
            InstanceProtector.addRedstonePreventedWorld(worlds);
        }
    }

    @Override
    public MatchInstantiateEvent start() {
        MatchInstantiateEvent matchInstantiateEvent = super.start();
        if (matchInstantiateEvent.isCancelled()) {
            return matchInstantiateEvent;
        }
        return matchInstantiateEvent;
    }

    @Override
    public void destroyMatch() {
        super.destroyMatch();
        for (World world : this.worlds) {
            TemporaryWorldManager.permanentlyDeleteWorld(world);
        }
    }

    @Override
    public boolean isInRegion(Location location) {
        for (World world : this.worlds) {
            if (!location.getWorld().equals((Object)world)) continue;
            return true;
        }
        return false;
    }

    @Generated
    public List<World> getWorlds() {
        return this.worlds;
    }

    public static class MatchInstanceWorldEvents
    implements Listener {
        @EventHandler(ignoreCancelled=true, priority=EventPriority.LOW)
        public void onPlayerTeleport(PlayerTeleportEvent event) {
            Logger.debug("Player " + event.getPlayer().getName() + " teleported to " + event.getTo().getWorld().getName());
            if (MatchInstance.MatchInstanceEvents.teleportBypass) {
                MatchInstance.MatchInstanceEvents.teleportBypass = false;
                return;
            }
            for (MatchInstance instance : MatchInstance.instances) {
                if (((MatchInstanceWorld)instance).worlds == null || !((MatchInstanceWorld)instance).worlds.equals(event.getFrom()) && !((MatchInstanceWorld)instance).worlds.equals(event.getTo())) continue;
                event.setCancelled(true);
                return;
            }
            MatchPlayer matchPlayer = MatchPlayer.getMatchPlayer(event.getPlayer());
            if (matchPlayer == null) {
                return;
            }
            if (matchPlayer.getMatchInstance().state == MatchInstance.InstanceState.WAITING) {
                return;
            }
            event.setCancelled(true);
        }
    }
}

