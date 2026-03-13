/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class DropItemEffect
extends AdvancedEffect {
    public DropItemEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "DROP_ITEM", "Drop an item in a location", "%e:<MATERIAL>:[AMOUNT]");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        if (stringArray.length == 0) {
            this.warn("No material provided for DROP_ITEM effect.");
            return false;
        }
        Material material = Material.matchMaterial((String)stringArray[0]);
        if (material == null || material == Material.AIR) {
            this.warn("Invalid material provided for DROP_ITEM effect.");
            return false;
        }
        int n = stringArray.length > 1 ? ASManager.parseInt(stringArray[1], 1) : 1;
        location.getWorld().dropItem(location, new ItemStack(material, n));
        return true;
    }
}

