/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.scheduler.BukkitTask
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import java.util.HashMap;
import java.util.List;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitTask;

class UserRepeaters {
    public final HashMap<EquipmentSlot, List<BukkitTask>> itemRunnables = new HashMap();

    UserRepeaters() {
    }
}

