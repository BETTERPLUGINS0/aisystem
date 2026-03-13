/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Monster
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.seasons.handlers;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.DataHandler;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.LandsHook;
import net.advancedplugins.as.impl.utils.hooks.plugins.WorldGuardHook;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.biomes.BiomeDescriptor;
import net.advancedplugins.seasons.entity.EntityTypeSpawner;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.handlers.sub.SeasonMobs;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SeasonalMobsHandler
extends DataHandler
implements Listener {
    private ImmutableMap<SeasonType, SeasonMobs> seasonMobs;
    private final int spawnChance = this.getConfig().getInt("chance", 30);

    public SeasonalMobsHandler(JavaPlugin javaPlugin) {
        super("mobs", javaPlugin);
        this.load();
        if (!this.isEnabled()) {
            return;
        }
        this.registerListener(this);
    }

    private void load() {
        ImmutableMap.Builder<SeasonType, SeasonMobs> builder = ImmutableMap.builder();
        for (SeasonType seasonType : SeasonType.values()) {
            builder.put(seasonType, new SeasonMobs(seasonType, this.getConfig()));
        }
        this.seasonMobs = builder.build();
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll((Listener)this);
        super.unload();
    }

    public EntityTypeSpawner getMobSpawner(Location location, SeasonType seasonType, boolean bl) {
        SeasonMobs seasonMobs = this.seasonMobs.get((Object)seasonType);
        if (seasonMobs == null) {
            return null;
        }
        BiomeDescriptor biomeDescriptor = BiomeDescriptor.fromLocation(location);
        String string = biomeDescriptor.upperCaseName();
        List<EntityTypeSpawner> list = Optional.ofNullable(seasonMobs.getTypeSpawnersByBiome().get(string)).orElse(seasonMobs.getTypeSpawnersByBiome().get("ALL"));
        if (list == null) {
            return null;
        }
        if ((list = list.stream().filter(entityTypeSpawner -> entityTypeSpawner.descriptor().hostile() == bl).collect(Collectors.toList())).isEmpty()) {
            return null;
        }
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    @EventHandler(ignoreCancelled=true)
    public void onMobSpawn(CreatureSpawnEvent creatureSpawnEvent) {
        LivingEntity livingEntity = creatureSpawnEvent.getEntity();
        if (!Core.getWorldHandler().isWorldEnabled(livingEntity.getWorld().getName())) {
            return;
        }
        if (!creatureSpawnEvent.getSpawnReason().equals((Object)CreatureSpawnEvent.SpawnReason.NATURAL)) {
            return;
        }
        Location location = livingEntity.getLocation().clone();
        if (location.getBlock().isLiquid()) {
            return;
        }
        if (location.getChunk().getEntities().length > 30) {
            return;
        }
        if (!ASManager.doChancesPass(this.spawnChance)) {
            return;
        }
        if (HooksHandler.isEnabled(HookPlugin.LANDS) && !((LandsHook)HooksHandler.getHook(HookPlugin.LANDS)).canMobsSpawn(location)) {
            return;
        }
        if (HooksHandler.isEnabled(HookPlugin.WORLDGUARD) && !((WorldGuardHook)HooksHandler.getHook(HookPlugin.WORLDGUARD)).canMobsSpawn(location)) {
            return;
        }
        EntityTypeSpawner entityTypeSpawner = this.getMobSpawner(livingEntity.getLocation(), Core.getSeasonHandler().getSeason(livingEntity.getWorld()).getType(), livingEntity instanceof Monster);
        if (entityTypeSpawner == null) {
            return;
        }
        location.add(1.0, 0.0, 0.0);
        Entity entity = entityTypeSpawner.spawn(location);
        entity.setPersistent(false);
    }
}

