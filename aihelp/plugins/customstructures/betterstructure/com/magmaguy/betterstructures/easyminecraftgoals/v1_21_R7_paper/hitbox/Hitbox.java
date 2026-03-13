/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntitySize
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_paper.hitbox;

import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_paper.CraftBukkitBridge;
import java.lang.reflect.Field;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySize;

public class Hitbox {
    private static Field dimensionsField = null;

    private Hitbox() {
    }

    public static boolean setCustomHitbox(Entity entity, float width, float height, boolean fixed) {
        EntitySize entityDimensions = new EntitySize(width, height, height, null, fixed);
        try {
            if (dimensionsField == null) {
                dimensionsField = Hitbox.findDimensionsField();
            }
            dimensionsField.set(entity, entityDimensions);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        entity.a(entityDimensions.a(entity.dI()));
        return true;
    }

    private static Field findDimensionsField() throws NoSuchFieldException {
        String fieldName = CraftBukkitBridge.isPaper() ? "dimensions" : "bz";
        Field field = Entity.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }
}

