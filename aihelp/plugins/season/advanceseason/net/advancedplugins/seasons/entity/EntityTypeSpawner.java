/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason
 */
package net.advancedplugins.seasons.entity;

import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.seasons.entity.EntityTypeDescriptor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

public interface EntityTypeSpawner {
    public Entity spawn(Location var1);

    public EntityTypeDescriptor descriptor();

    public static EntityTypeSpawner bukkit(final EntityType type) {
        return new EntityTypeSpawner(){

            @Override
            public Entity spawn(Location location) {
                if (MinecraftVersion.isPaper()) {
                    return location.getWorld().spawnEntity(location, type, CreatureSpawnEvent.SpawnReason.COMMAND);
                }
                return location.getWorld().spawnEntity(location, type);
            }

            @Override
            public EntityTypeDescriptor descriptor() {
                return EntityTypeDescriptor.of(type);
            }
        };
    }
}

