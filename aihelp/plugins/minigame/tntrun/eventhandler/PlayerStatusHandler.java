package tntrun.eventhandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.arena.structure.StructureManager;

public class PlayerStatusHandler implements Listener {
   private TNTRun plugin;
   private final Map<UUID, Integer> snowballLevels = new HashMap();
   // $FF: synthetic field
   private static volatile int[] $SWITCH_TABLE$tntrun$arena$structure$StructureManager$DamageEnabled;

   public PlayerStatusHandler(TNTRun plugin) {
      this.plugin = plugin;
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onPlayerDamage(EntityDamageEvent e) {
      if (e.getEntity() instanceof Player) {
         Player player = (Player)e.getEntity();
         Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
         if (arena == null) {
            return;
         }

         if (!arena.getStatusManager().isArenaRunning()) {
            e.setCancelled(true);
            return;
         }

         if (e.getCause() == DamageCause.FALL) {
            e.setCancelled(true);
            return;
         }

         StructureManager.DamageEnabled status = arena.getStructureManager().getDamageEnabled();
         switch($SWITCH_TABLE$tntrun$arena$structure$StructureManager$DamageEnabled()[status.ordinal()]) {
         case 1:
            return;
         case 2:
            e.setDamage(0.0D);
            return;
         case 3:
            e.setCancelled(true);
            return;
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onDamageByPlayer(EntityDamageByEntityEvent e) {
      if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
         Player player = (Player)e.getEntity();
         Player damager = (Player)e.getDamager();
         Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
         if (arena == null) {
            return;
         }

         if (!arena.getStructureManager().getDamageEnabled().toString().equals("NO") && damager.getInventory().getItemInMainHand().getType() == Material.AIR && !arena.getStructureManager().isPunchDamage()) {
            e.setCancelled(true);
            return;
         }

         if (arena.getPlayersManager().isSpectator(player.getName()) || arena.getPlayersManager().isSpectator(damager.getName())) {
            e.setCancelled(true);
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onPlayerDamage(FoodLevelChangeEvent e) {
      if (e.getEntity() instanceof Player) {
         Player player = (Player)e.getEntity();
         if (this.plugin.amanager.getPlayerArena(player.getName()) != null) {
            e.setCancelled(true);
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onSnowballHit(ProjectileHitEvent e) {
      Projectile projectile = e.getEntity();
      if (projectile instanceof Snowball) {
         if (e.getHitEntity() != null && e.getHitEntity().getType() == EntityType.PLAYER) {
            Player player = (Player)e.getHitEntity();
            Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
            if (arena != null) {
               if (arena.getStatusManager().isArenaRunning()) {
                  player.damage(0.5D, projectile);
                  double knockback = this.getKnockbackLevel(projectile);
                  if (!(knockback <= 0.0D)) {
                     player.setVelocity(projectile.getVelocity().multiply(knockback));
                  }
               }
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onSnowballThrow(ProjectileLaunchEvent e) {
      Projectile projectile = e.getEntity();
      if (projectile instanceof Snowball) {
         if (projectile.getShooter() instanceof Player) {
            Player player = (Player)projectile.getShooter();
            Arena arena = this.plugin.amanager.getPlayerArena(player.getName());
            if (arena != null) {
               if (arena.getStatusManager().isArenaRunning()) {
                  ItemStack is = player.getInventory().getItemInMainHand();
                  if (is.hasItemMeta()) {
                     ItemMeta im = is.getItemMeta();
                     if (im.hasEnchant(Enchantment.KNOCKBACK)) {
                        this.snowballLevels.put(projectile.getUniqueId(), im.getEnchantLevel(Enchantment.KNOCKBACK));
                     }

                  }
               }
            }
         }
      }
   }

   private double getKnockbackLevel(Projectile projectile) {
      Snowball snowball = (Snowball)projectile;
      int level = (Integer)this.snowballLevels.getOrDefault(snowball.getUniqueId(), 0);
      this.snowballLevels.remove(snowball.getUniqueId());
      double kb = 0.3D + 0.2D * (double)level;
      return kb;
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$tntrun$arena$structure$StructureManager$DamageEnabled() {
      int[] var10000 = $SWITCH_TABLE$tntrun$arena$structure$StructureManager$DamageEnabled;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[StructureManager.DamageEnabled.values().length];

         try {
            var0[StructureManager.DamageEnabled.NO.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[StructureManager.DamageEnabled.YES.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[StructureManager.DamageEnabled.ZERO.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$tntrun$arena$structure$StructureManager$DamageEnabled = var0;
         return var0;
      }
   }
}
