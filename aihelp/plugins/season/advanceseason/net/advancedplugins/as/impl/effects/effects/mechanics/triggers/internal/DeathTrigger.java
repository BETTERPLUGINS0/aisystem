/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import net.advancedplugins.as.impl.effects.effects.effects.internal.KeepOnDeathEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DeathTrigger
extends AdvancedTrigger {
    private final Cache<UUID, Double> lastDeathHP = CacheBuilder.newBuilder().expireAfterWrite(500L, TimeUnit.MILLISECONDS).build();

    public DeathTrigger() {
        super("DEATH");
        this.setComboEnabled(true);
        this.setDescription("Activates when Player/Mob dies");
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onDeath(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getCause().name().contains("CUSTOM")) {
            return;
        }
        Entity entity = entityDamageByEntityEvent.getEntity();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entity;
        Entity entity2 = entityDamageByEntityEvent.getDamager();
        if (!(entity2 instanceof LivingEntity)) {
            return;
        }
        entity = (LivingEntity)entity2;
        if (entityDamageByEntityEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.THORNS)) {
            return;
        }
        double d = livingEntity.getHealth() - entityDamageByEntityEvent.getFinalDamage();
        if (d > 0.0) {
            return;
        }
        Double d2 = this.lastDeathHP.getIfPresent(livingEntity.getUniqueId());
        this.lastDeathHP.put(livingEntity.getUniqueId(), d);
        if (d2 != null && d2 <= 0.0) {
            return;
        }
        if (entityDamageByEntityEvent.getEntity().hasMetadata("ae_ignore") && !entityDamageByEntityEvent.getEntity().hasMetadata("ae_damage_event_not_going_to_run") && d2 != null) {
            return;
        }
        if (livingEntity instanceof Player && ASManager.hasTotem((Player)livingEntity)) {
            return;
        }
        for (StackItem stackItem : GetAllRollItems.get(livingEntity)) {
            boolean bl = !stackItem.rit.equals((Object)RollItemType.OTHER);
            this.executionBuilder().setAttacker((LivingEntity)entity).setVictim(livingEntity).setStackItem(stackItem).setAttackerMain(false).setEvent((Event)entityDamageByEntityEvent).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).setSoulboundOnly(!bl).buildAndExecute();
        }
        KeepOnDeathEffect.DEAD_PLAYERS.put(livingEntity, true);
    }
}

