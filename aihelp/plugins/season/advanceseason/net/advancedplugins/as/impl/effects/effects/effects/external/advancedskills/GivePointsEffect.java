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

public class GivePointsEffect
extends AdvancedEffect {
    public GivePointsEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "GIVE_POINTS", "Reward player with skill points", "GIVE_POINTS:<skill>:<points>");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (stringArray.length != 2) {
            return false;
        }
        String string = stringArray[0];
        int n = ASManager.parseInt(stringArray[1]);
        if (n <= 0) {
            return false;
        }
        SkillsAPI.givePoints((Player)((Player)livingEntity), (String)string, (int)n);
        return true;
    }
}

