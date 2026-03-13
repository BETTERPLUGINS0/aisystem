/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class RemoveDeathItems
implements Listener {
    private static final HashMap<UUID, List<ItemStack>> itemsCache = new HashMap();

    public static void add(UUID uUID, ItemStack itemStack) {
        if (!ASManager.isValid(itemStack)) {
            return;
        }
        List<Object> list = itemsCache.containsKey(uUID) ? itemsCache.get(uUID) : new ArrayList();
        list.add(itemStack);
        itemsCache.put(uUID, list);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent entityDeathEvent) {
        if (entityDeathEvent.getEntityType() != EntityType.PLAYER) {
            return;
        }
        if (!itemsCache.containsKey(entityDeathEvent.getEntity().getUniqueId())) {
            return;
        }
        List<ItemStack> list = itemsCache.remove(entityDeathEvent.getEntity().getUniqueId());
        for (ItemStack itemStack : new ArrayList(entityDeathEvent.getDrops())) {
            ItemStack itemStack2 = itemStack.clone();
            for (ItemStack itemStack3 : list) {
                ItemStack itemStack4 = itemStack3.clone();
                boolean bl = itemStack4.getType() == itemStack2.getType();
                if (!bl) continue;
                entityDeathEvent.getDrops().remove(itemStack);
            }
        }
    }
}

