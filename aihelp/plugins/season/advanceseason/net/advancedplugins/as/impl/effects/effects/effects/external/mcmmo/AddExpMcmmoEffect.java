/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.external.mcmmo;

import java.util.Locale;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import net.advancedplugins.as.impl.utils.hooks.plugins.McMMOHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AddExpMcmmoEffect
extends AdvancedEffect {
    public AddExpMcmmoEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "ADD_EXP_MCMMO", "Add EXP to player's mcmmo", "%e:<SKILL>:<AMOUNT>");
        this.addArgument(0, String.class);
        this.addArgument(1, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (!(livingEntity instanceof Player)) {
            return false;
        }
        String string = stringArray[0].toUpperCase(Locale.ROOT);
        int n = ASManager.parseInt(stringArray[1]);
        McMMOHook mcMMOHook = (McMMOHook)HooksHandler.getHook(HookPlugin.MCMMO);
        mcMMOHook.addSkillExperience((Player)livingEntity, string, n);
        return true;
    }
}

