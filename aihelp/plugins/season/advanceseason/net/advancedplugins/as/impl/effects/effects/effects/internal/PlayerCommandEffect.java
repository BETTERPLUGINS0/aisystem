/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerCommandEffect
extends AdvancedEffect {
    public PlayerCommandEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "PLAYER_COMMAND", "Run command through player", "%e:<COMMAND>");
        this.addArgument(0, String.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        String string = String.join((CharSequence)":", stringArray);
        Bukkit.dispatchCommand((CommandSender)livingEntity, (String)string);
        return true;
    }
}

