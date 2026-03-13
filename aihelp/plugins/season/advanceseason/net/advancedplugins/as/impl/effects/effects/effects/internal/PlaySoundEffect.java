/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Sound
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.PlayASound;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaySoundEffect
extends AdvancedEffect {
    public PlaySoundEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "PLAY_SOUND", "Play sound for a specific player", "%e:<SOUND>:[PITCH]:[VOLUME]");
        this.addArgument(0, Sound.class);
        this.addArgument(1, Float.class);
        this.addArgument(2, Float.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        if (!(livingEntity instanceof Player)) {
            return false;
        }
        PlayASound.playSound(stringArray[0], (Player)livingEntity, stringArray.length >= 2 ? Float.parseFloat(stringArray[1]) : 1.0f, stringArray.length >= 3 ? Float.parseFloat(stringArray[2]) : 10.0f);
        return true;
    }
}

