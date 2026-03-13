/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Monster
 */
package net.advancedplugins.seasons.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;

public class EntityTypeDescriptor {
    private final boolean hostile;

    public EntityTypeDescriptor(boolean bl) {
        this.hostile = bl;
    }

    public boolean hostile() {
        return this.hostile;
    }

    public static EntityTypeDescriptor of(EntityType entityType) {
        Class clazz = entityType.getEntityClass();
        boolean bl = clazz != null && Monster.class.isAssignableFrom(clazz);
        return new EntityTypeDescriptor(bl);
    }
}

