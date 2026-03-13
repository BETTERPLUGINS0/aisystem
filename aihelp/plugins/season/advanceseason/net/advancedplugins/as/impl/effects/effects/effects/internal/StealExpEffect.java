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
import net.advancedplugins.as.impl.utils.ExperienceManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StealExpEffect
extends AdvancedEffect {
    public StealExpEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "STEAL_EXP", "Steal EXP from one person for another", "%e:<AMOUNT>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        int n = ASManager.parseInt(stringArray[0]);
        Player player = (Player)this.getOtherEntity(livingEntity, executionTask);
        Player player2 = (Player)livingEntity;
        ExperienceManager experienceManager = new ExperienceManager(player);
        if (experienceManager.getTotalExperience() < n) {
            n = experienceManager.getTotalExperience();
        }
        ExperienceManager experienceManager2 = new ExperienceManager(player2);
        experienceManager2.setTotalExperience(experienceManager2.getTotalExperience() + n);
        experienceManager.setTotalExperience(experienceManager.getTotalExperience() - n);
        return true;
    }
}

