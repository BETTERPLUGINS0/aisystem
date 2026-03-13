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
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.economy.AdvancedEconomy;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StealMoneyEffect
extends AdvancedEffect {
    public StealMoneyEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "STEAL_MONEY", "Steal Money from one person for another", "%e:<AMOUNT>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        int n = ASManager.parseInt(stringArray[0]);
        Player player = (Player)this.getOtherEntity(livingEntity, executionTask);
        Player player2 = (Player)livingEntity;
        AdvancedEconomy advancedEconomy = EffectsHandler.getEconomyHandler().getEcon("MONEY");
        int n2 = (int)advancedEconomy.getBalance(player);
        if (n2 == 0) {
            return false;
        }
        if (n2 < n) {
            advancedEconomy.chargeUser(player, n2);
            advancedEconomy.giveUser(player2, n2);
            return true;
        }
        advancedEconomy.chargeUser(player, n);
        advancedEconomy.giveUser(player2, n);
        return true;
    }
}

