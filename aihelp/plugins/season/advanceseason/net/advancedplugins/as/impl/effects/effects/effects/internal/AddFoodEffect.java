/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.MathUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AddFoodEffect
extends AdvancedEffect {
    public AddFoodEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "ADD_FOOD", "Modify player's food level", "%e:<AMOUNT>");
        this.addArgument(0, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (!(livingEntity instanceof Player)) {
            return false;
        }
        Player player = (Player)livingEntity;
        int n = MathUtils.clamp(player.getFoodLevel() + ASManager.parseInt(stringArray[0]), 0, 20);
        player.setFoodLevel(n);
        return true;
    }
}

