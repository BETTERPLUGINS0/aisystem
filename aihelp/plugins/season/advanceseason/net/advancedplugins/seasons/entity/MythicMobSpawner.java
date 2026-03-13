/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.lumine.mythic.api.mobs.MythicMob
 *  io.lumine.mythic.bukkit.BukkitAdapter
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 */
package net.advancedplugins.seasons.entity;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import net.advancedplugins.seasons.entity.EntityTypeDescriptor;
import net.advancedplugins.seasons.entity.EntityTypeSpawner;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class MythicMobSpawner
implements EntityTypeSpawner {
    private final MythicMob mythicMob;
    private final boolean hostile;
    private final int level;

    public MythicMobSpawner(MythicMob mythicMob, boolean bl, int n) {
        this.mythicMob = mythicMob;
        this.hostile = bl;
        this.level = n;
    }

    @Override
    public Entity spawn(Location location) {
        return this.mythicMob.spawn(BukkitAdapter.adapt((Location)location), (double)this.level).getEntity().getBukkitEntity();
    }

    @Override
    public EntityTypeDescriptor descriptor() {
        return new EntityTypeDescriptor(this.hostile);
    }
}

