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
import net.advancedplugins.as.impl.utils.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class SubtitleEffect
extends AdvancedEffect {
    public SubtitleEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "SUBTITLE", "Send a subtitle", "%e:<TEXT>");
        this.addArgument(0, String.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        String string = Text.modify(String.join((CharSequence)":", stringArray));
        Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), "minecraft:title " + livingEntity.getName() + " subtitle {\"text\":\"" + string + "\"}");
        return true;
    }
}

