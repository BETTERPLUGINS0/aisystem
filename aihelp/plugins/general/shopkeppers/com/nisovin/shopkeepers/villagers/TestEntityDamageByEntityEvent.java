package com.nisovin.shopkeepers.villagers;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class TestEntityDamageByEntityEvent extends EntityDamageByEntityEvent {
   public TestEntityDamageByEntityEvent(Entity damager, Entity damagee) {
      super(damager, damagee, DamageCause.CUSTOM, DamageSource.builder((DamageType)Unsafe.assertNonNull(DamageType.GENERIC)).withCausingEntity(damager).withDirectEntity(damager).build(), 1.0D);
   }
}
