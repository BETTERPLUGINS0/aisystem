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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.PotionEffectMatcher;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ApplyPotionEffect
extends AdvancedEffect {
    private static int permanentLength = -1;
    private static final Map<UUID, List<PotionEffect>> activatedPermanentPotions = new HashMap<UUID, List<PotionEffect>>();
    private static final Queue<Triple<Long, LivingEntity, Double>> healthQueue = new ArrayDeque<Triple<Long, LivingEntity, Double>>();

    public ApplyPotionEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "POTION", "Add potion effect", "%e:<POTION>:<LEVEL>:[TICKS]");
        if (!MinecraftVersion.isNewerThan(MinecraftVersion.MC1_19_R3)) {
            permanentLength = Integer.MAX_VALUE;
        }
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        PotionEffectType potionEffectType = PotionEffectMatcher.matchPotion(stringArray[0]);
        int n = ASManager.parseInt(stringArray[1]);
        if (livingEntity == null) {
            return true;
        }
        if (executionTask.getBuilder().isPermanent()) {
            if (executionTask.getBuilder().isRemoved()) {
                List list = activatedPermanentPotions.getOrDefault(livingEntity.getUniqueId(), new ArrayList());
                ApplyPotionEffect.handlePermanentRemoval(livingEntity, list, potionEffectType, n);
                if (list.isEmpty()) {
                    activatedPermanentPotions.remove(livingEntity.getUniqueId());
                } else {
                    activatedPermanentPotions.put(livingEntity.getUniqueId(), list);
                }
            } else {
                PotionEffect potionEffect = new PotionEffect(potionEffectType, permanentLength, n, true, false);
                if (!livingEntity.hasPotionEffect(potionEffectType) || !ASManager.hasPotionEffect(livingEntity, potionEffectType, n)) {
                    livingEntity.addPotionEffect(potionEffect);
                }
                List list = activatedPermanentPotions.getOrDefault(livingEntity.getUniqueId(), new ArrayList());
                list.add(potionEffect);
                activatedPermanentPotions.put(livingEntity.getUniqueId(), list);
            }
            return true;
        }
        if (stringArray.length > 2) {
            PotionEffect potionEffect;
            int n2 = ASManager.parseInt(stringArray[2]);
            if ((livingEntity.hasPotionEffect(potionEffectType) || ASManager.hasPotionEffect(livingEntity, potionEffectType, n)) && ((potionEffect = livingEntity.getPotionEffect(potionEffectType)).getAmplifier() > n || potionEffect.getAmplifier() == n && potionEffect.getDuration() > n2)) {
                return true;
            }
            if (stringArray.length > 3 && !ASManager.parseBoolean(stringArray[3], true)) {
                return true;
            }
            livingEntity.addPotionEffect(new PotionEffect(potionEffectType, n2, n));
            return true;
        }
        return true;
    }

    public static void handlePermanentRemoval(LivingEntity livingEntity, List<PotionEffect> list, PotionEffectType potionEffectType, int n) {
        if (list.stream().map(PotionEffect::getType).anyMatch(potionEffectType::equals)) {
            Iterator<PotionEffect> iterator = list.iterator();
            double d = livingEntity.getHealth();
            while (iterator.hasNext()) {
                PotionEffect potionEffect = iterator.next();
                if (!potionEffect.getType().equals(potionEffectType) || potionEffect.getAmplifier() != n) continue;
                iterator.remove();
                break;
            }
            healthQueue.add(Triple.of(System.currentTimeMillis(), livingEntity, d));
            livingEntity.removePotionEffect(potionEffectType);
        }
    }

    public static int getPermanentLength() {
        return permanentLength;
    }

    public static Map<UUID, List<PotionEffect>> getActivatedPermanentPotions() {
        return activatedPermanentPotions;
    }

    static {
        SchedulerUtils.runTaskTimer(() -> {
            long l = System.currentTimeMillis();
            while (!healthQueue.isEmpty() && healthQueue.peek().getLeft() + 500L < l) {
                Triple<Long, LivingEntity, Double> triple = healthQueue.poll();
                if (triple == null) continue;
                LivingEntity livingEntity = triple.getMiddle();
                double d = triple.getRight();
                if (d <= livingEntity.getHealth()) continue;
                livingEntity.setHealth(Math.min(d, livingEntity.getMaxHealth()));
            }
        }, 1L, 1L);
    }
}

