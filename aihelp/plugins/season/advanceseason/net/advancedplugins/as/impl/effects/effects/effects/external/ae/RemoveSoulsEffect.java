/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.ae.api.SoulUseEvent
 *  net.advancedplugins.ae.features.souls.SoulsAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.external.ae;

import net.advancedplugins.ae.api.SoulUseEvent;
import net.advancedplugins.ae.features.souls.SoulsAPI;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoveSoulsEffect
extends AdvancedEffect {
    public RemoveSoulsEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "REMOVE_SOULS", "Remove souls from item", "%e:<AMOUNT>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        ItemStack itemStack = executionTask.getBuilder().getItem();
        int n = ASManager.parseInt(stringArray[0]);
        if (!ASManager.isValid(itemStack)) {
            return true;
        }
        int n2 = SoulsAPI.getSoulsOnItem((ItemStack)itemStack);
        SoulUseEvent soulUseEvent = new SoulUseEvent(itemStack, livingEntity, n, n2 >= n);
        Bukkit.getPluginManager().callEvent((Event)soulUseEvent);
        if (soulUseEvent.isCancelled() || soulUseEvent.isCustom()) {
            return true;
        }
        if (n2 < n) {
            return true;
        }
        SoulsAPI.useSouls((ItemStack)itemStack, (int)n);
        executionTask.getBuilder().setItem(itemStack);
        return true;
    }
}

