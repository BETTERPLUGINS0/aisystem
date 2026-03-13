/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class DeleteItemEffect
extends AdvancedEffect {
    public DeleteItemEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "DELETE_ITEM", "Delete current item from inventory", "%e:[AMOUNT]");
        this.addArgument(0, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        ItemStack itemStack = executionTask.getBuilder().getItem();
        int n = stringArray.length > 0 ? ASManager.parseInt(stringArray[0], 1) : 1;
        ASManager.removeItems((Inventory)((Player)livingEntity).getInventory(), itemStack, n);
        return true;
    }
}

