/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ApplyPotionEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.PotionEffectMatcher;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionOverrideEffect
extends AdvancedEffect {
    public PotionOverrideEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "POTION_OVERRIDE", "Force-add potion effect", "%e:<POTION>:<LEVEL>:[TICKS]");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        PotionEffectType potionEffectType = PotionEffectMatcher.matchPotion(stringArray[0]);
        int n = ASManager.parseInt(stringArray[1]);
        if (executionTask.getBuilder().isPermanent()) {
            if (executionTask.getBuilder().isRemoved()) {
                livingEntity.removePotionEffect(potionEffectType);
            } else {
                PotionEffect potionEffect = new PotionEffect(potionEffectType, ApplyPotionEffect.getPermanentLength(), n);
                livingEntity.addPotionEffect(potionEffect);
            }
            return true;
        }
        if (stringArray.length > 2) {
            int n2 = ASManager.parseInt(stringArray[2]);
            livingEntity.addPotionEffect(new PotionEffect(potionEffectType, n2, n));
            return true;
        }
        livingEntity.addPotionEffect(new PotionEffect(potionEffectType, ApplyPotionEffect.getPermanentLength(), n));
        return true;
    }
}

