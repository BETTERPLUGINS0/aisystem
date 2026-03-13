/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DefenseTrigger
extends AdvancedTrigger {
    public DefenseTrigger() {
        super("DEFENSE");
        this.setDescription("Activates when Player/Mob gets hit by a player");
        this.setComboEnabled(true);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void playerHitPlayer(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (!(entityDamageByEntityEvent.getEntity() instanceof LivingEntity) || entityDamageByEntityEvent.getEntity() instanceof ArmorStand) {
            return;
        }
        if (entityDamageByEntityEvent.getEntity().hasMetadata("ae_ignore")) {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        if (entityDamageByEntityEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.THORNS)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entityDamageByEntityEvent.getEntity();
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        if (livingEntity.getHealth() - entityDamageByEntityEvent.getFinalDamage() <= 0.0) {
            return;
        }
        this.resetCombo(livingEntity.getUniqueId());
        if (entityDamageByEntityEvent.getCause().name().equalsIgnoreCase("CUSTOM") && livingEntity.hasMetadata("mcmmo_rupture")) {
            return;
        }
        if (livingEntity instanceof Player && ((Player)livingEntity).isBlocking() && entityDamageByEntityEvent.getFinalDamage() <= 0.0) {
            return;
        }
        for (StackItem stackItem : GetAllRollItems.getMainItems(livingEntity)) {
            this.executionBuilder().setAttacker((LivingEntity)player).setVictim(livingEntity).setStackItem(stackItem).setAttackerMain(false).setEvent((Event)entityDamageByEntityEvent).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

