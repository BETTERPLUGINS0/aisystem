package com.dfsek.terra.bukkit.world.inventory.meta;

import com.dfsek.terra.api.inventory.item.Damageable;
import com.dfsek.terra.bukkit.world.inventory.BukkitItemMeta;

public class BukkitDamageable extends BukkitItemMeta implements Damageable {
   public BukkitDamageable(org.bukkit.inventory.meta.Damageable delegate) {
      super(delegate);
   }

   public int getDamage() {
      return ((org.bukkit.inventory.meta.Damageable)this.getHandle()).getDamage();
   }

   public void setDamage(int damage) {
      ((org.bukkit.inventory.meta.Damageable)this.getHandle()).setDamage(damage);
   }

   public boolean hasDamage() {
      return ((org.bukkit.inventory.meta.Damageable)this.getHandle()).hasDamage();
   }
}
