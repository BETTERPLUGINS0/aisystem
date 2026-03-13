/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.Trident
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.persistence.PersistentDataHolder
 *  org.bukkit.projectiles.ProjectileSource
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.GetAllRollItems;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
import net.advancedplugins.as.impl.effects.effects.actions.utils.StackItem;
import net.advancedplugins.as.impl.effects.effects.effects.utils.TridentShootHandler;
import net.advancedplugins.as.impl.effects.effects.settings.SettingValues;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.as.impl.utils.pdc.PDCHandler;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Trident;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.projectiles.ProjectileSource;

public class DefenseProjectileTrigger
extends AdvancedTrigger {
    public DefenseProjectileTrigger() {
        super("DEFENSE_PROJECTILE");
        this.setDescription("Activates when Player/Mob gets hit by projectile from player");
        this.setComboEnabled(true);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onProjectileHitEvent(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Entity entity = entityDamageByEntityEvent.getDamager();
        if (!(entity instanceof Projectile)) {
            return;
        }
        Projectile projectile = (Projectile)entity;
        ProjectileSource projectileSource = projectile.getShooter();
        if (!(projectileSource instanceof Player)) {
            return;
        }
        entity = (Player)projectileSource;
        Entity entity2 = entityDamageByEntityEvent.getEntity();
        if (!(entity2 instanceof LivingEntity)) {
            return;
        }
        projectileSource = (LivingEntity)entity2;
        if (entityDamageByEntityEvent.getEntity().hasMetadata("ae_ignore")) {
            return;
        }
        if (entityDamageByEntityEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.THORNS)) {
            return;
        }
        if (SettingValues.isBowFullPower() && !PDCHandler.getBoolean((PersistentDataHolder)projectile, "projectile-max-force") && !(projectile instanceof Trident)) {
            return;
        }
        if (projectile instanceof Arrow) {
            if (!projectile.hasMetadata(EffectsHandler.getKey() + "handRIT")) {
                return;
            }
            RollItemType rollItemType = (RollItemType)((Object)((MetadataValue)projectile.getMetadata(EffectsHandler.getKey() + "handRIT").get(0)).value());
            entity2 = entity.getEquipment().getItem(rollItemType.getSlot());
        } else if (MinecraftVersion.isNew() && projectile instanceof Trident) {
            entity2 = TridentShootHandler.getItem(entity.getUniqueId());
            if (entity2 == null) {
                return;
            }
        } else {
            return;
        }
        if (entity2.getType() != Material.BOW) {
            if (!MinecraftVersion.isNew()) {
                return;
            }
            if (entity2.getType() != Material.TRIDENT && !entity2.getType().name().contains("CROSSBOW")) {
                return;
            }
        }
        if (projectile.hasMetadata("enchantsActivation") && !((MetadataValue)projectile.getMetadata("enchantsActivation").get(0)).asBoolean()) {
            return;
        }
        if (entityDamageByEntityEvent.getCause().name().equalsIgnoreCase("CUSTOM") && projectileSource.hasMetadata("mcmmo_rupture")) {
            return;
        }
        if (projectileSource instanceof Player && ((Player)projectileSource).isBlocking() && entityDamageByEntityEvent.getFinalDamage() <= 0.0) {
            return;
        }
        boolean bl = projectile.getLocation().getY() - projectileSource.getLocation().getY() > 1.45;
        this.addCombo(projectileSource.getUniqueId());
        for (StackItem stackItem : GetAllRollItems.getMainItems((LivingEntity)projectileSource)) {
            this.executionBuilder().setAttacker((LivingEntity)entity).setVictim((LivingEntity)projectileSource).setStackItem(stackItem).processVariables("%is headshot%;" + bl, "%projectile type%;" + (projectile instanceof Arrow ? "arrow" : "trident")).setAttackerMain(false).setEvent((Event)entityDamageByEntityEvent).setItemType(stackItem.getRollItemType()).setItem(stackItem.getItem()).buildAndExecute();
        }
    }
}

