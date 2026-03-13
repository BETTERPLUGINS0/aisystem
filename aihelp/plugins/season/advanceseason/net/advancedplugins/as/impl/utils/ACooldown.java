/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 */
package net.advancedplugins.as.impl.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.LivingEntity;

public class ACooldown {
    private static final Map<UUID, Map<String, Double>> cooldown = new HashMap<UUID, Map<String, Double>>();

    public static void reload() {
        cooldown.clear();
    }

    public static boolean isInCooldown(LivingEntity livingEntity, String string) {
        if (livingEntity == null || string == null) {
            return false;
        }
        UUID uUID = livingEntity.getUniqueId();
        if (!cooldown.containsKey(uUID)) {
            return false;
        }
        Map<String, Double> map = cooldown.get(uUID);
        if (!map.containsKey(string)) {
            return false;
        }
        double d = map.get(string);
        if (d >= (double)System.currentTimeMillis()) {
            return true;
        }
        map.remove(string);
        cooldown.put(uUID, map);
        return false;
    }

    public static void putToCooldown(LivingEntity livingEntity, String string, double d) {
        if (livingEntity == null || string == null) {
            return;
        }
        UUID uUID = livingEntity.getUniqueId();
        Map map = cooldown.getOrDefault(uUID, new HashMap());
        map.put(string, (double)System.currentTimeMillis() + d * 1000.0);
        cooldown.put(uUID, map);
    }
}

