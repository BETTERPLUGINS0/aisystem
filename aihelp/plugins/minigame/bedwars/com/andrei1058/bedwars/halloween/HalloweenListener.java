/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.world.WorldLoadEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.halloween;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.events.gameplay.GameStateChangeEvent;
import com.andrei1058.bedwars.api.events.player.PlayerJoinArenaEvent;
import com.andrei1058.bedwars.api.events.player.PlayerKillEvent;
import com.andrei1058.bedwars.api.events.player.PlayerXpGainEvent;
import com.andrei1058.bedwars.api.events.server.ArenaDisableEvent;
import com.andrei1058.bedwars.api.events.server.ArenaEnableEvent;
import com.andrei1058.bedwars.api.events.server.ArenaRestartEvent;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.halloween.CobWebRemover;
import com.andrei1058.bedwars.halloween.HalloweenSpecial;
import com.andrei1058.bedwars.levels.internal.PlayerLevel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class HalloweenListener
implements Listener {
    private final Sound ambienceSound = Sound.valueOf((String)BedWars.getForCurrentVersion("AMBIENCE_CAVE", "AMBIENT_CAVE", "AMBIENT_CAVE"));
    private final Sound ghastSound = Sound.valueOf((String)BedWars.getForCurrentVersion("GHAST_SCREAM2", "ENTITY_GHAST_SCREAM", "ENTITY_GHAST_SCREAM"));

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.isCancelled()) {
            return;
        }
        LivingEntity entity = e.getEntity();
        if (entity.getType() == EntityType.ARMOR_STAND) {
            return;
        }
        entity.getEquipment().setHelmet(new ItemStack(Material.PUMPKIN));
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent e) {
        if (HalloweenSpecial.getINSTANCE() != null && !HalloweenSpecial.checkAvailabilityDate()) {
            CreatureSpawnEvent.getHandlerList().unregister((Listener)this);
        }
    }

    @EventHandler
    public void onPlayerDie(PlayerKillEvent e) {
        Location location;
        if (e.getKiller() != null && (location = e.getVictim().getLocation().add(0.0, 1.0, 0.0)).getBlock().getType() == Material.AIR) {
            location.getWorld().playSound(location, this.ghastSound, 2.0f, 1.0f);
            if (!Misc.isBuildProtected(location, e.getArena())) {
                location.getBlock().setType(Material.valueOf((String)BedWars.getForCurrentVersion("WEB", "WEB", "COBWEB")));
                e.getArena().addPlacedBlock(location.getBlock());
                location.getBlock().setMetadata("give-bw-exp", (MetadataValue)new FixedMetadataValue((Plugin)BedWars.plugin, (Object)"ok"));
                CobWebRemover remover = CobWebRemover.getByArena(e.getArena());
                if (remover != null) {
                    remover.addCobWeb(location.getBlock());
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        PlayerLevel level;
        if (e.isCancelled()) {
            return;
        }
        if (e.getBlock().hasMetadata("give-bw-exp") && (level = PlayerLevel.getLevelByPlayer(e.getPlayer().getUniqueId())) != null) {
            e.getBlock().getDrops().clear();
            level.addXp(5, PlayerXpGainEvent.XpSource.OTHER);
            e.getPlayer().sendMessage(String.valueOf(ChatColor.GOLD) + "+5 xp!");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinArenaEvent e) {
        if (!e.isSpectator()) {
            Bukkit.getScheduler().runTaskLater((Plugin)BedWars.plugin, () -> e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), this.ambienceSound, 3.0f, 1.0f), 20L);
        }
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent e) {
        CobWebRemover remover;
        if (e.getNewState() == GameState.restarting && (remover = CobWebRemover.getByArena(e.getArena())) != null) {
            remover.destroy();
        }
    }

    @EventHandler
    public void onRestart(ArenaRestartEvent e) {
        CobWebRemover remover = CobWebRemover.getByArenaWorld(e.getWorldName());
        if (remover != null) {
            remover.destroy();
        }
    }

    @EventHandler
    public void onDisable(ArenaDisableEvent e) {
        CobWebRemover remover = CobWebRemover.getByArenaWorld(e.getWorldName());
        if (remover != null) {
            remover.destroy();
        }
    }

    @EventHandler
    public void onEnable(ArenaEnableEvent e) {
        new CobWebRemover(e.getArena());
    }
}

