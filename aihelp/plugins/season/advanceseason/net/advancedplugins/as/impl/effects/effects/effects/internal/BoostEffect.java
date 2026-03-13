/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.util.Vector
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class BoostEffect
extends AdvancedEffect {
    private final List<UUID> ignoreFall = new ArrayList<UUID>();

    public BoostEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "BOOST", "Boost entity up in air", "%e:[DIRECTION]:[AMOUNT]");
        this.addArgument(0, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        Bukkit.getScheduler().runTaskLater((Plugin)EffectsHandler.getInstance(), () -> {
            try {
                Vector vector;
                String string = "UP";
                double d = 1.0;
                if (stringArray.length == 1) {
                    if (MathUtils.isDouble(stringArray[0])) {
                        d = Double.parseDouble(stringArray[0]);
                    } else {
                        string = stringArray[0];
                    }
                } else if (stringArray.length != 0) {
                    string = stringArray[0];
                    if (!MathUtils.isDouble(stringArray[1])) {
                        EffectsHandler.getInstance().getLogger().severe("'BOOST' effect was used with an invalid amount! Expected a number, got " + stringArray[1]);
                        return;
                    }
                    d = MathUtils.clamp(Double.parseDouble(stringArray[1]), -20.0, 20.0);
                }
                Vector vector2 = livingEntity.getLocation().getDirection().normalize();
                double d2 = d / 10.0;
                double d3 = vector2.getX() * d2;
                double d4 = vector2.getZ() * d2;
                switch (string.toUpperCase(Locale.ROOT)) {
                    case "UP": {
                        vector = new Vector(0.0, d2, 0.0);
                        break;
                    }
                    case "DOWN": {
                        vector = new Vector(0.0, -d2, 0.0);
                        break;
                    }
                    case "FORWARD": {
                        vector = new Vector(d3, 0.0, d4);
                        break;
                    }
                    case "BACKWARD": {
                        vector = new Vector(-d3, 0.0, -d4);
                        break;
                    }
                    case "LOOK": {
                        Vector vector3 = livingEntity.getEyeLocation().getDirection().normalize();
                        double d5 = vector3.getX() * d2;
                        double d6 = vector3.getZ() * d2;
                        double d7 = vector3.getY() * d2;
                        vector = new Vector(d5, d7, d6);
                        break;
                    }
                    default: {
                        EffectsHandler.getInstance().getLogger().severe("'BOOST' effect used with incorrect direction! You can choose from 'UP', 'DOWN', 'FORWARD', and 'BACKWARD'.");
                        return;
                    }
                }
                if (ASManager.isExcessVelocity(vector)) {
                    EffectsHandler.getInstance().getLogger().severe("'BOOST' effect used with too much velocity! Please lower the effect value.");
                    return;
                }
                livingEntity.setVelocity(vector);
                if (!this.ignoreFall.contains(livingEntity.getUniqueId())) {
                    this.ignoreFall.add(livingEntity.getUniqueId());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }, 1L);
        return true;
    }

    @EventHandler
    public void onFall(EntityDamageEvent entityDamageEvent) {
        if (!entityDamageEvent.getEntity().getType().equals((Object)EntityType.PLAYER)) {
            return;
        }
        if (!entityDamageEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.FALL)) {
            return;
        }
        UUID uUID = entityDamageEvent.getEntity().getUniqueId();
        if (!this.ignoreFall.contains(uUID)) {
            return;
        }
        entityDamageEvent.setCancelled(true);
        this.ignoreFall.remove(uUID);
    }
}

