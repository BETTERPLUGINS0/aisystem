/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Ageable
 *  org.bukkit.entity.Creature
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.EntityTargetEvent
 *  org.bukkit.event.entity.ExplosionPrimeEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.ColorUtils;
import net.advancedplugins.as.impl.utils.EntitySpawnUtils;
import net.advancedplugins.as.impl.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GuardEffect
extends AdvancedEffect {
    private final Map<UUID, UUID> guards = new HashMap<UUID, UUID>();
    private static GuardEffect guardEffect;

    public GuardEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "GUARD", "Spawn a mob guarding player", "%e:<ENTITY>:<SECONDS>:<AMOUNT:><NAME>:<SWITCH_ROLES>");
        this.addArgument(0, EntityType.class);
        this.addArgument(1, Integer.class);
        this.addArgument(2, Integer.class);
        this.addArgument(3, String.class);
        this.addArgument(4, Boolean.class);
        guardEffect = this;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        String string;
        String string2 = stringArray[0].toUpperCase(Locale.ROOT);
        boolean bl = string2.startsWith("BABY_");
        String string3 = string = bl ? string2.substring("BABY_".length()) : string2;
        if (!ASManager.isValidEnum(EntityType.class, string)) {
            EffectsHandler.getInstance().getLogger().warning("Tried to summon GUARD of invalid entity type: \"" + string + "\".");
            return false;
        }
        EntityType entityType = EntityType.valueOf((String)string);
        int n = 160;
        int n2 = 1;
        if (stringArray.length > 1 && MathUtils.isInteger(stringArray[1])) {
            n = ASManager.parseInt(stringArray[1]) * 20;
        }
        if (stringArray.length > 2 && MathUtils.isInteger(stringArray[2].trim())) {
            n2 = ASManager.parseInt(stringArray[2].trim());
        }
        boolean bl2 = false;
        if (stringArray.length > 4) {
            bl2 = ASManager.parseBoolean(stringArray[4], false);
        }
        LivingEntity livingEntity2 = this.getOtherEntity(livingEntity, executionTask);
        LivingEntity livingEntity3 = livingEntity;
        if (bl2) {
            livingEntity3 = livingEntity2;
            livingEntity2 = livingEntity;
        }
        String string4 = "";
        if (stringArray.length > 3) {
            string4 = ColorUtils.format(stringArray[3].replaceAll("%player name%", livingEntity3.getName()));
        }
        boolean bl3 = !livingEntity3.equals((Object)livingEntity2);
        this.summonGuard(entityType, bl, string4, n, n2, livingEntity2, bl3);
        return true;
    }

    public boolean isGuard(Entity entity) {
        return this.guards.containsKey(entity.getUniqueId());
    }

    public void steal(UUID uUID, UUID uUID2) {
        if (!this.guards.containsValue(uUID)) {
            return;
        }
        HashMap<UUID, UUID> hashMap = new HashMap<UUID, UUID>(this.guards);
        for (Map.Entry<UUID, UUID> entry : this.getGuards().entrySet()) {
            if (!Objects.equals(entry.getValue(), uUID)) continue;
            hashMap.remove(entry.getKey(), uUID);
            hashMap.put(entry.getKey(), uUID2);
            Entity entity = Bukkit.getEntity((UUID)entry.getKey());
            if (!(entity instanceof Creature)) continue;
            ((Creature)entity).setTarget((LivingEntity)Bukkit.getPlayer((UUID)uUID2));
        }
        this.guards.putAll(hashMap);
    }

    public void summonGuard(EntityType entityType, boolean bl, String string, int n, int n2, LivingEntity livingEntity, boolean bl2) {
        for (int i = 0; i < n2; ++i) {
            this.summonGuard(entityType, bl, string, n, livingEntity, bl2);
        }
    }

    public void summonGuard(EntityType entityType, boolean bl, String string, int n, LivingEntity livingEntity, boolean bl2) {
        Entity entity = EntitySpawnUtils.spawnEntity((Plugin)this.getPlugin(), livingEntity.getWorld(), livingEntity.getLocation(), entityType);
        if (string != null && !string.isEmpty()) {
            entity.setCustomName(string);
            entity.setCustomNameVisible(true);
        }
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity2 = (LivingEntity)entity;
            livingEntity2.setCanPickupItems(false);
            livingEntity2.setRemoveWhenFarAway(true);
            livingEntity2.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, n, 1, false, true));
        }
        this.guards.put(entity.getUniqueId(), livingEntity.getUniqueId());
        if (entity instanceof Creature) {
            ((Creature)entity).setTarget(livingEntity);
        }
        if (bl && entity instanceof Ageable) {
            ((Ageable)entity).setBaby();
        }
        Bukkit.getScheduler().runTaskLater((Plugin)EffectsHandler.getInstance(), () -> {
            entity.remove();
            if (!entity.isDead() && entity instanceof LivingEntity) {
                ((LivingEntity)entity).damage(2.147483647E9);
            }
        }, (long)n);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onExplode(ExplosionPrimeEvent explosionPrimeEvent) {
        if (!this.isGuard(explosionPrimeEvent.getEntity())) {
            return;
        }
        Entity entity = explosionPrimeEvent.getEntity();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entity;
        livingEntity.getActivePotionEffects().forEach(potionEffect -> livingEntity.removePotionEffect(potionEffect.getType()));
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onGuardDeath(EntityDeathEvent entityDeathEvent) {
        if (!this.isGuard((Entity)entityDeathEvent.getEntity())) {
            return;
        }
        entityDeathEvent.setDroppedExp(0);
        entityDeathEvent.getDrops().clear();
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    private void entityTargetEvent(EntityTargetEvent entityTargetEvent) {
        if (!this.isGuard(entityTargetEvent.getEntity())) {
            return;
        }
        Entity entity = entityTargetEvent.getTarget();
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        LivingEntity livingEntity = (LivingEntity)entity;
        if (livingEntity.getUniqueId().equals(this.guards.get(entityTargetEvent.getEntity().getUniqueId()))) {
            return;
        }
        entityTargetEvent.setCancelled(true);
        entity = null;
        double d = 32.0;
        for (Entity entity2 : entityTargetEvent.getTarget().getNearbyEntities(d, d, d)) {
            double d2;
            if (!(entity2 instanceof LivingEntity) || entity2.getUniqueId().equals(entity2.getUniqueId()) || !((d2 = entity2.getLocation().distance(livingEntity.getLocation())) < d)) continue;
            entity = (LivingEntity)entity2;
            d = d2;
        }
        if (entity == null) {
            return;
        }
        entityTargetEvent.setTarget(entity);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onSpawn(CreatureSpawnEvent creatureSpawnEvent) {
        if (!this.isGuard((Entity)creatureSpawnEvent.getEntity())) {
            return;
        }
        creatureSpawnEvent.setCancelled(false);
    }

    public Map<UUID, UUID> getGuards() {
        return this.guards;
    }

    public static GuardEffect getGuardEffect() {
        return guardEffect;
    }
}

