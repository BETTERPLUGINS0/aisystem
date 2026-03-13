/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleCommandEffect
extends AdvancedEffect {
    public ConsoleCommandEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "CONSOLE_COMMAND", "Run command through console", "%e:<COMMAND>");
        this.addArgument(0, String.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        return this.executeEffect(executionTask, executionTask.getBuilder().getMain(), stringArray);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        String string = String.join((CharSequence)":", stringArray);
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask((Plugin)ASManager.getInstance(), () -> Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)string));
            return true;
        }
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)string);
        return true;
    }
}

