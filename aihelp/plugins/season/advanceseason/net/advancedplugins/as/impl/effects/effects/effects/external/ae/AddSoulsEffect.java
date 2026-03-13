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

public class AddSoulsEffect
extends AdvancedEffect {
    public AddSoulsEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "ADD_SOULS", "Add souls to item", "%e:<AMOUNT>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        ItemStack itemStack = executionTask.getBuilder().getItem();
        int n = ASManager.parseInt(stringArray[0]);
        if (!ASManager.isValid(itemStack)) {
            return true;
        }
        SoulUseEvent soulUseEvent = new SoulUseEvent(itemStack, livingEntity, n, true);
        Bukkit.getPluginManager().callEvent((Event)soulUseEvent);
        if (soulUseEvent.isCancelled() || soulUseEvent.isCustom()) {
            return true;
        }
        itemStack = SoulsAPI.addSouls((ItemStack)itemStack, (int)n);
        AddSoulsEffect.updateItem(executionTask.getBuilder(), livingEntity, itemStack, executionTask.getBuilder().getItemType());
        return true;
    }
}

