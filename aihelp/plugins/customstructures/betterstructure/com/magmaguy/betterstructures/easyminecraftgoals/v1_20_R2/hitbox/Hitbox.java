/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntitySize
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R2.hitbox;

import java.lang.reflect.Field;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySize;

public class Hitbox {
    private Hitbox() {
    }

    public static boolean setCustomHitbox(Entity entity, float width, float height, boolean fixed) {
        EntitySize entityDimensions = new EntitySize(width, height, fixed);
        Class<Entity> entityClass = Entity.class;
        try {
            Field field = entityClass.getDeclaredField("bh");
            field.setAccessible(true);
            field.set(entity, entityDimensions);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        entity.a(entityDimensions.a(entity.dj()));
        return true;
    }
}

