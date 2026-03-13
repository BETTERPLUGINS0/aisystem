/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.HashMap;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CancelUseEffect
extends AdvancedEffect {
    private static final HashMap<UUID, CancelledUses> cancelledUses = new HashMap();

    public CancelUseEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "CANCEL_USE", "Cancel use of a material, e.g. ENDER_PEARL", "%e:<MATERIAL>:<TICKS>");
    }

    public static void addCancelled(UUID uUID, Material material, Long l) {
        CancelledUses cancelledUses = CancelUseEffect.cancelledUses.getOrDefault(uUID, new CancelledUses());
        cancelledUses.map.put(material, l);
        CancelUseEffect.cancelledUses.put(uUID, cancelledUses);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        Material material = ASManager.matchMaterial(stringArray[0], 1, 0).getType();
        if (material == null) {
            executionTask.reportIssue(this.getName(), "Invalid material " + stringArray[0] + " provided");
            return true;
        }
        long l = System.currentTimeMillis() + (long)Integer.parseInt(stringArray[1]) * 50L;
        CancelUseEffect.addCancelled(livingEntity.getUniqueId(), material, l);
        return true;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getItem() == null) {
            return;
        }
        CancelledUses cancelledUses = CancelUseEffect.cancelledUses.get(playerInteractEvent.getPlayer().getUniqueId());
        if (cancelledUses == null) {
            return;
        }
        Material material = playerInteractEvent.getItem().getType();
        if (!cancelledUses.map.containsKey(material)) {
            return;
        }
        Long l = cancelledUses.map.get(material);
        if (l < System.currentTimeMillis()) {
            cancelledUses.map.remove(material);
            return;
        }
        playerInteractEvent.setCancelled(true);
        playerInteractEvent.setUseItemInHand(Event.Result.DENY);
    }

    static class CancelledUses {
        public HashMap<Material, Long> map = new HashMap();

        CancelledUses() {
        }
    }
}

