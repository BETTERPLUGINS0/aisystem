/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class EntitySpawnUtils
implements Listener {
    public static String metaDataPrefix = "advanced";
    private final Plugin plugin;

    public EntitySpawnUtils(Plugin plugin) {
        this.plugin = plugin;
    }

    public static Entity spawnEntity(@NotNull Plugin plugin, @NotNull World world, @NotNull Location location, @NotNull EntityType entityType) {
        Entity entity = world.spawnEntity(location, entityType);
        entity.setMetadata(metaDataPrefix + "-entity", (MetadataValue)new FixedMetadataValue(plugin, (Object)true));
        return entity;
    }

    @EventHandler
    private void onEntityDeath(EntityDeathEvent entityDeathEvent) {
        entityDeathEvent.getEntity().removeMetadata(metaDataPrefix + "-entity", this.plugin);
    }
}

