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
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.persistence.PersistentDataHolder
 *  org.bukkit.plugin.Plugin
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
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.plugin.Plugin;

public class ShootTrigger
extends AdvancedTrigger {
    public ShootTrigger() {
        super("SHOOT");
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
        Entity entity2 = entityDamageByEntityEvent.getEntity();
        if (!(entity2 instanceof Player)) {
            return;
        }
        entity = (Player)entity2;
        if (entityDamageByEntityEvent.getEntity().hasMetadata("ae_ignore")) {
            return;
        }
        entity2 = projectile.getShooter();
        if (!(entity2 instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entity2;
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
        boolean bl = projectile.getLocation().getY() - entity.getLocation().getY() > 1.45;
        this.executionBuilder().setAttacker(livingEntity).processVariables("%is headshot%;" + bl, "%projectile type%;" + (projectile instanceof Arrow ? "arrow" : "trident")).setBlock(projectile.getLocation().getBlock()).setVictim((LivingEntity)entity).setAttackerMain(true).setEvent((Event)entityDamageByEntityEvent).setItemType(rollItemType).setItem(itemStack).buildAndExecute();
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onShoot(EntityShootBowEvent entityShootBowEvent) {
        boolean bl = entityShootBowEvent.getForce() >= 0.5f;
        entityShootBowEvent.getProjectile().setMetadata(EffectsHandler.getKey() + "enchantsActivation", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)bl));
        EquipmentSlot equipmentSlot = EquipmentSlot.HAND;
        if (MinecraftVersion.getVersionNumber() < 1160) {
            if (entityShootBowEvent.getEntity() instanceof Player) {
                equipmentSlot = RollItemType.getHand((Player)entityShootBowEvent.getEntity(), entityShootBowEvent.getBow()).getSlot();
            } else if (entityShootBowEvent.getEntity().getEquipment() != null) {
                equipmentSlot = entityShootBowEvent.getEntity().getEquipment().getItemInMainHand().equals((Object)entityShootBowEvent.getBow()) ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;
            }
        } else {
            equipmentSlot = entityShootBowEvent.getHand();
        }
        entityShootBowEvent.getProjectile().setMetadata(EffectsHandler.getKey() + "handRIT", (MetadataValue)new FixedMetadataValue((Plugin)EffectsHandler.getInstance(), (Object)RollItemType.getFromEquipment(equipmentSlot)));
    }
}

