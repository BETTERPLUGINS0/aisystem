/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class BroadcastPermissionEffect
extends AdvancedEffect {
    public BroadcastPermissionEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "BROADCAST_PERMISSION", "Broadcast a message to users who have permission", "%e:<PERMISSION>:<TEXT>");
        this.addArgument(0, String.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        String string = stringArray[0];
        CharSequence[] charSequenceArray = new String[stringArray.length - 1];
        System.arraycopy(stringArray, 1, charSequenceArray, 0, charSequenceArray.length);
        String string2 = String.join((CharSequence)" ", charSequenceArray);
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(string)).forEach(player -> player.sendMessage(Text.modify(string2)));
        return true;
    }
}

