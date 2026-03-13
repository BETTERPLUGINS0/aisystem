/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.actions.handlers.DamageHandler;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.TotemUndying;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public class RemoveHealthDamageTotemEffect
extends AdvancedEffect {
    public RemoveHealthDamageTotemEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "REMOVE_HEALTH_DAMAGE_TOTEM", "Remove health from an entity with damage effect while allowing the use of totem", "%e:<HEALTH>");
        this.addArgument(0, Integer.class);
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        DamageHandler damageHandler = executionTask.getDamageHandler();
        damageHandler.damage((Damageable)livingEntity, (Entity)this.getOtherEntity(livingEntity, executionTask), 0.01);
        int n = ASManager.parseInt(stringArray[0]);
        if (!(livingEntity instanceof Player)) {
            damageHandler.damageIgnoringArmor((Damageable)livingEntity, livingEntity, this.getOtherEntity(livingEntity, executionTask), n);
            return true;
        }
        if (livingEntity.getHealth() - (double)n > 0.0) {
            damageHandler.damageIgnoringArmor((Damageable)livingEntity, livingEntity, this.getOtherEntity(livingEntity, executionTask), n);
            return true;
        }
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        if (entityEquipment == null) {
            return true;
        }
        if (entityEquipment.getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
            if (!new TotemUndying().playEffect(livingEntity, EquipmentSlot.OFF_HAND, 900, 800, 100, true)) {
                damageHandler.damageIgnoringArmor((Damageable)livingEntity, livingEntity, this.getOtherEntity(livingEntity, executionTask), n);
                return true;
            }
            entityEquipment.getItemInOffHand().setAmount(entityEquipment.getItemInOffHand().getAmount() - 1);
            return true;
        }
        if (entityEquipment.getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING) {
            if (!new TotemUndying().playEffect(livingEntity, EquipmentSlot.HAND, 900, 800, 100, true)) {
                damageHandler.damageIgnoringArmor((Damageable)livingEntity, livingEntity, this.getOtherEntity(livingEntity, executionTask), n);
                return true;
            }
            entityEquipment.getItemInMainHand().setAmount(entityEquipment.getItemInMainHand().getAmount() - 1);
            return true;
        }
        damageHandler.damageIgnoringArmor((Damageable)livingEntity, livingEntity, this.getOtherEntity(livingEntity, executionTask), n);
        return true;
    }
}

