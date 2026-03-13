/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class GiveItemEffect
extends AdvancedEffect {
    public GiveItemEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "GIVE_ITEM", "Give items to user/location", "%e:<MATERIAL>:[AMOUNT]");
        this.addArgument(0, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        int n = stringArray.length > 1 ? ASManager.parseInt(stringArray[1], 1) : 1;
        ItemStack itemStack = ASManager.matchMaterial(stringArray[0], n, 0);
        ASManager.giveItem((Player)livingEntity, itemStack);
        return true;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        int n = stringArray.length > 1 ? ASManager.parseInt(stringArray[1], 1) : 1;
        ItemStack itemStack = ASManager.matchMaterial(stringArray[0], n, 0);
        location.getWorld().dropItem(location, itemStack);
        return true;
    }
}

