/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class TakeAwayEffect
extends AdvancedEffect {
    public TakeAwayEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "TAKE_AWAY", "Take item away from player", "%e:<MATERIAL>:<AMOUNT>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        int n;
        Material material = ASManager.getItemFromBlock(ASManager.matchMaterial(stringArray[0], 1, 0).getType());
        int n2 = n = stringArray.length >= 2 ? ASManager.parseInt(stringArray[1]) : 1;
        if (n <= 0) {
            return true;
        }
        Player player = (Player)livingEntity;
        ASManager.removeItems((Inventory)player.getInventory(), material, n);
        player.updateInventory();
        return true;
    }
}

