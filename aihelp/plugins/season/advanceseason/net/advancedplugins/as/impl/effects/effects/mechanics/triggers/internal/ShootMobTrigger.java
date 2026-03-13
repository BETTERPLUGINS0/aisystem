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
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.persistence.PersistentDataHolder
 */
package net.advancedplugins.as.impl.effects.effects.mechanics.triggers.internal;

import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.AdvancedTrigger;
import net.advancedplugins.as.impl.effects.effects.actions.utils.RollItemType;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataHolder;

public class ShootMobTrigger
extends AdvancedTrigger {
    public ShootMobTrigger() {
        super("SHOOT_MOB");
        this.setDescription("Activates when player/mob shoots a mob");
        this.setComboEnabled(true);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onProjectileHitEvent(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        ItemStack itemStack;
        RollItemType rollItemType;
        Entity entity = entityDamageByEntityEvent.getDamager();
        if (!(entity instanceof Projectile)) {
            return;
        }
        Projectile projectile = (Projectile)entity;
        if (entityDamageByEntityEvent.getEntity() instanceof Player) {
            return;
        }
        if (entityDamageByEntityEvent.getEntity().hasMetadata("ae_ignore")) {
            return;
        }
        if (entityDamageByEntityEvent.getCause().equals((Object)EntityDamageEvent.DamageCause.THORNS)) {
            return;
        }
        entity = projectile.getShooter();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entity;
        if (SettingValues.isBowFullPower() && !PDCHandler.getBoolean((PersistentDataHolder)projectile, "projectile-max-force") && !(projectile instanceof Trident)) {
            return;
        }
        if (projectile instanceof Arrow) {
            if (!projectile.hasMetadata(EffectsHandler.getKey() + "handRIT")) {
                return;
            }
            rollItemType = (RollItemType)((Object)((MetadataValue)projectile.getMetadata(EffectsHandler.getKey() + "handRIT").get(0)).value());
            itemStack = livingEntity.getEquipment().getItem(rollItemType.getSlot());
        } else if (MinecraftVersion.isNew() && projectile instanceof Trident) {
            itemStack = TridentShootHandler.getItem(livingEntity.getUniqueId());
            if (itemStack == null) {
                return;
            }
            rollItemType = RollItemType.HAND;
        } else {
            return;
        }
        if (itemStack.getType() != Material.BOW && itemStack.getType() != Material.TRIDENT && itemStack.getType() != Material.CROSSBOW) {
            return;
        }
        if (projectile.hasMetadata("enchantsActivation") && !((MetadataValue)projectile.getMetadata("enchantsActivation").get(0)).asBoolean()) {
            return;
        }
        Entity entity2 = entityDamageByEntityEvent.getEntity();
        if (!(entity2 instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity2 = (LivingEntity)entity2;
        boolean bl = entityDamageByEntityEvent.getDamager().getLocation().getY() - livingEntity2.getLocation().getY() > 1.45;
        this.addCombo(livingEntity.getUniqueId());
        this.executionBuilder().setAttacker(livingEntity).processVariables("%is headshot%;" + bl, "%projectile type%;" + (projectile instanceof Arrow ? "arrow" : "trident")).setBlock(projectile.getLocation().getBlock()).setVictim(livingEntity2).setAttackerMain(true).setEvent((Event)entityDamageByEntityEvent).setItemType(rollItemType).setItem(itemStack).buildAndExecute();
    }
}

