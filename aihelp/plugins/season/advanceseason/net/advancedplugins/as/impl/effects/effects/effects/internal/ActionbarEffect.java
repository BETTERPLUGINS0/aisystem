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
import net.advancedplugins.as.impl.utils.text.Text;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ActionbarEffect
extends AdvancedEffect {
    public ActionbarEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "ACTION_BAR", "Send an actionbar message", "%e:<TEXT>");
        this.addArgument(0, String.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (!(livingEntity instanceof Player)) {
            return true;
        }
        String string = Text.modify(String.join((CharSequence)":", stringArray));
        ASManager.sendActionBar(string, (Player)livingEntity);
        return true;
    }
}

