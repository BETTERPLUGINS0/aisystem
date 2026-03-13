/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.economy.AdvancedEconomy;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoveMoneyEffect
extends AdvancedEffect {
    public RemoveMoneyEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "REMOVE_MONEY", "Removes vault money to the player", "%e:<AMOUNT>");
        this.addArgument(0, Double.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (!(livingEntity instanceof Player)) {
            return false;
        }
        if (stringArray.length < 1) {
            return false;
        }
        AdvancedEconomy advancedEconomy = EffectsHandler.getEconomyHandler().getEcon("MONEY");
        if (advancedEconomy == null) {
            return false;
        }
        Player player = (Player)livingEntity;
        double d = Double.parseDouble(stringArray[0]);
        advancedEconomy.chargeUser(player, d);
        return true;
    }
}

