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
import net.advancedplugins.as.impl.utils.PotionEffectMatcher;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CureEffect
extends AdvancedEffect {
    public CureEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "CURE", "Remove non-permanent potion effect", "%e:<POTION>");
        this.addArgument(0, PotionEffectType.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        PotionEffectType potionEffectType = PotionEffectMatcher.matchPotion(stringArray[0]);
        if (potionEffectType == null) {
            return false;
        }
        PotionEffect potionEffect2 = livingEntity.getActivePotionEffects().stream().filter(potionEffect -> potionEffect.getType().equals(potionEffectType)).findFirst().orElse(null);
        if (potionEffect2 == null) {
            return true;
        }
        if (potionEffect2.getDuration() != ApplyPotionEffect.getPermanentLength() && potionEffect2.getDuration() <= ApplyPotionEffect.getPermanentLength()) {
            return true;
        }
        livingEntity.removePotionEffect(potionEffectType);
        return true;
    }
}

