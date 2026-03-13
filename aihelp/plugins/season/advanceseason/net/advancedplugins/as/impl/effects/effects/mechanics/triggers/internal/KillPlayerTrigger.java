/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class KillPlayerTrigger
extends AdvancedTrigger {
    public KillPlayerTrigger() {
        super("KILL_PLAYER");
        this.setDescription("Activates when any Entity kills another Player");
        this.setComboEnabled(true);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onMobDeath(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Arrow arrow;
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        if (entityDamageByEntityEvent.getEntity().hasMetadata("ae_ignore")) {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof LivingEntity)) {
            if (entityDamageByEntityEvent.getDamager() instanceof Arrow) {
                arrow = (Arrow)entityDamageByEntityEvent.getDamager();
                if (!(arrow.getShooter() instanceof LivingEntity)) {
                    return;
                }
            } else {
                return;
            }
        }
        if (entityDamageByEntityEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.THORNS)) {
            return;
        }
        arrow = entityDamageByEntityEvent.getDamager() instanceof Arrow ? (LivingEntity)((Arrow)entityDamageByEntityEvent.getDamager()).getShooter() : (LivingEntity)entityDamageByEntityEvent.getDamager();
        if (arrow == null) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getEntity();
        if (player.getHealth() - entityDamageByEntityEvent.getFinalDamage() > 0.0) {
            return;
        }
        if (player.isBlocking() && entityDamageByEntityEvent.getFinalDamage() <= 0.0) {
            return;
        }
        ItemStack itemStack = player.getEquipment().getItemInMainHand();
        ItemStack itemStack2 = player.getEquipment().getItemInOffHand();
        if (itemStack.getType().name().equalsIgnoreCase("TOTEM_OF_UNDYING") || itemStack2.getType().name().equalsIgnoreCase("TOTEM_OF_UNDYING")) {
            return;
        }
        this.resetCombo(player.getUniqueId());
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)arrow)) {
            this.executionBuilder().setAttacker((LivingEntity)arrow).setVictim((LivingEntity)player).setAttackerMain(true).setEvent((Event)entityDamageByEntityEvent).setStackItem(stackItem).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

