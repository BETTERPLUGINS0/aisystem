/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.advancedplugins.skills.api.SkillsAPI
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.external.advancedskills;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.skills.api.SkillsAPI;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GiveManaEffect
extends AdvancedEffect {
    public GiveManaEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "GIVE_MANA", "Reward player with mana", "GIVE_POINTS:<manas>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (stringArray.length != 1) {
            return false;
        }
        int n = ASManager.parseInt(stringArray[0]);
        if (n <= 0) {
            return false;
        }
        SkillsAPI.giveMana((Player)((Player)livingEntity), (int)n);
        return true;
    }
}

