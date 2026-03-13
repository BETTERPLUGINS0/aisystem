package me.casperge.realisticseasons.seasonevent.buildin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.seasonevent.CustomDatedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class HalloweenEvent extends CustomDatedEvent implements DefaultEvent, Listener {
   List<World> enabled;
   RealisticSeasons main;
   Random r = new Random();
   EntityType[] monsters;
   public List<PotionEffectType> witchExtraPotions;
   public double entityDupeChance;
   public double entityInvisibleChance;
   public double entityFastChance;
   public double skeletonFlameChance;
   public double armoredMobChance;
   public double zombiePieChance;
   public double vindicatorChance;
   public boolean playParticlesAroundMobs;

   public HalloweenEvent(ConfigurationSection var1) {
      super(var1);
      this.monsters = new EntityType[]{EntityType.SKELETON, EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.DROWNED, EntityType.ENDERMAN, EntityType.HUSK, EntityType.PIGLIN, EntityType.PILLAGER, EntityType.SKELETON, EntityType.WITCH, EntityType.SPIDER, EntityType.STRAY, EntityType.VINDICATOR, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER};
      this.entityDupeChance = 0.0D;
      this.entityInvisibleChance = 0.0D;
      this.entityFastChance = 0.0D;
      this.skeletonFlameChance = 0.0D;
      this.armoredMobChance = 0.0D;
      this.zombiePieChance = 0.0D;
      this.vindicatorChance = 0.0D;
      this.playParticlesAroundMobs = false;
      this.main = RealisticSeasons.getInstance();
      this.enabled = new ArrayList();
      Bukkit.getPluginManager().registerEvents(this, this.main);
      Bukkit.getScheduler().runTaskTimer(this.main, new Runnable() {
         public void run() {
            if (HalloweenEvent.this.playParticlesAroundMobs) {
               ArrayList var1 = new ArrayList();
               Iterator var2 = HalloweenEvent.this.enabled.iterator();

               while(true) {
                  while(var2.hasNext()) {
                     World var3 = (World)var2.next();
                     if (!Bukkit.getWorlds().contains(var3)) {
                        var1.add(var3);
                     } else {
                        Iterator var4 = var3.getPlayers().iterator();

                        while(var4.hasNext()) {
                           Player var5 = (Player)var4.next();
                           Iterator var6 = var5.getNearbyEntities(14.0D, 14.0D, 14.0D).iterator();

                           while(var6.hasNext()) {
                              Entity var7 = (Entity)var6.next();
                              if (var7.getScoreboardTags().contains("halloween")) {
                                 HalloweenEvent.this.main.getParticleManager().playRandomFireParticle(var7);
                              }
                           }
                        }
                     }
                  }

                  HalloweenEvent.this.enabled.removeAll(var1);
                  return;
               }
            }
         }
      }, 7L, 7L);
   }

   public void enable(World var1) {
      if (!this.enabled.contains(var1)) {
         this.enabled.add(var1);
      }

   }

   public void disable(World var1) {
      if (this.enabled.contains(var1)) {
         this.enabled.remove(var1);
      }

   }

   public boolean isEnabled(World var1) {
      return this.enabled.contains(var1);
   }

   public DefaultEventType getType() {
      return DefaultEventType.HALLOWEEN;
   }

   @EventHandler
   public void hitEvent(EntityDamageByEntityEvent var1) {
      if (this.enabled.size() != 0) {
         if (this.enabled.contains(var1.getEntity().getLocation().getWorld())) {
            if (var1.getDamager() instanceof Player && this.r.nextDouble() < 0.5D && this.isMonster(var1.getEntityType())) {
               if (var1.getEntity().getScoreboardTags().contains("halloweenduping")) {
                  this.duplicateMob((LivingEntity)var1.getEntity());
               } else if (var1.getEntity().getScoreboardTags().contains("halloweeninvisible")) {
                  var1.getEntity().removeScoreboardTag("halloweeninvisible");
                  ((LivingEntity)var1.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 200, 0));
                  var1.getEntity().getWorld().spawnParticle(Particle.CLOUD, var1.getEntity().getLocation(), 25, 0.0D, 0.0D, 0.0D, 0.2D);
               }
            }

         }
      }
   }

   @EventHandler
   public void onMobSpawn(CreatureSpawnEvent var1) {
      if (this.enabled.size() != 0) {
         if (this.enabled.contains(var1.getLocation().getWorld())) {
            if (var1.getSpawnReason() == SpawnReason.NATURAL) {
               if (this.isMonster(var1.getEntityType())) {
                  if (var1.getEntityType() == EntityType.SKELETON) {
                     if (this.r.nextDouble() <= this.skeletonFlameChance) {
                        var1.getEntity().addScoreboardTag("halloweenflame");
                        var1.getEntity().addScoreboardTag("halloween");
                     }

                     if (this.r.nextDouble() <= this.armoredMobChance) {
                        var1.getEntity().getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE, 1));
                        var1.getEntity().addScoreboardTag("halloween");
                     }
                  } else if (var1.getEntityType() == EntityType.ZOMBIE) {
                     if (this.r.nextDouble() <= this.armoredMobChance) {
                        var1.getEntity().getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE, 1));
                        var1.getEntity().getEquipment().setItemInMainHand(new ItemStack(Material.GOLDEN_AXE, 1));
                        var1.getEntity().getEquipment().setItem(EquipmentSlot.HEAD, new ItemStack(Material.JACK_O_LANTERN, 1));
                        var1.getEntity().addScoreboardTag("halloween");
                     } else if (this.r.nextDouble() <= this.zombiePieChance) {
                        var1.getEntity().getEquipment().setItemInMainHand(new ItemStack(Material.PUMPKIN_PIE, 1));
                        var1.getEntity().addScoreboardTag("halloween");
                     }
                  }

                  if (this.r.nextDouble() <= this.entityInvisibleChance) {
                     var1.getEntity().addScoreboardTag("halloweeninvisible");
                     var1.getEntity().addScoreboardTag("halloween");
                     return;
                  }

                  if (this.r.nextDouble() <= this.vindicatorChance && (var1.getEntityType() == EntityType.ZOMBIE || var1.getEntityType() == EntityType.CREEPER)) {
                     var1.setCancelled(true);
                     ((LivingEntity)var1.getLocation().getWorld().spawnEntity(var1.getLocation(), EntityType.VINDICATOR)).setRemoveWhenFarAway(true);
                     var1.getEntity().addScoreboardTag("halloween");
                     return;
                  }

                  if (this.r.nextDouble() <= this.entityDupeChance) {
                     var1.getEntity().addScoreboardTag("halloweenduping");
                     var1.getEntity().addScoreboardTag("halloween");
                  }

                  if (this.r.nextDouble() <= this.entityFastChance) {
                     var1.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1));
                     var1.getEntity().addScoreboardTag("halloween");
                  }
               }

            }
         }
      }
   }

   @EventHandler
   public void onProjectile(ProjectileLaunchEvent var1) {
      if (this.enabled.size() != 0) {
         if (this.enabled.contains(var1.getLocation().getWorld())) {
            if (var1.getEntity() instanceof ThrownPotion) {
               ThrownPotion var2 = (ThrownPotion)var1.getEntity();
               if (var2.getShooter() == null) {
                  return;
               }

               if (var2.getShooter() instanceof Witch) {
                  Witch var3 = (Witch)var2.getShooter();
                  if (this.witchExtraPotions != null) {
                     if (!var3.getScoreboardTags().contains("halloween")) {
                        var1.getEntity().addScoreboardTag("halloween");
                     }

                     if (this.r.nextInt(3) == 0 && this.witchExtraPotions.size() > 0) {
                        ItemStack var4 = new ItemStack(Material.SPLASH_POTION);
                        PotionMeta var5 = (PotionMeta)var4.getItemMeta();
                        PotionEffect var6 = new PotionEffect((PotionEffectType)this.witchExtraPotions.get(this.r.nextInt(this.witchExtraPotions.size())), this.r.nextInt(600) + 100, this.r.nextInt(2));
                        var5.addCustomEffect(var6, true);
                        var4.setItemMeta(var5);
                        var2.setItem(var4);
                     }
                  }
               }
            } else if (var1.getEntity() instanceof Arrow) {
               Arrow var7 = (Arrow)var1.getEntity();
               if (var7.getShooter() != null && var7.getShooter() instanceof Skeleton && ((Skeleton)var7.getShooter()).getScoreboardTags().contains("halloweenflame")) {
                  var7.setFireTicks(200);
               }
            }

         }
      }
   }

   public void duplicateMob(LivingEntity var1) {
      if (var1.getCustomName() == null) {
         if (var1.hasAI()) {
            if (var1.hasGravity()) {
               if (!var1.isInvulnerable()) {
                  if (!var1.getScoreboardTags().contains("halloweenduplicate")) {
                     if (this.isMonster(var1.getType())) {
                        var1.removeScoreboardTag("halloweenduping");

                        for(int var2 = 0; var2 < this.r.nextInt(3) + 3; ++var2) {
                           Entity var3 = var1.getWorld().spawnEntity(var1.getLocation(), var1.getType());
                           ((LivingEntity)var3).getEquipment().setHelmet(var1.getEquipment().getHelmet());
                           ((LivingEntity)var3).getEquipment().setChestplate(var1.getEquipment().getChestplate());
                           ((LivingEntity)var3).getEquipment().setLeggings(var1.getEquipment().getLeggings());
                           ((LivingEntity)var3).getEquipment().setBoots(var1.getEquipment().getBoots());
                           ((LivingEntity)var3).getEquipment().setItemInMainHand(var1.getEquipment().getItemInMainHand());
                           ((LivingEntity)var3).getEquipment().setItemInOffHand(var1.getEquipment().getItemInOffHand());
                           ((LivingEntity)var3).addPotionEffects(var1.getActivePotionEffects());
                           var3.setVelocity(getRandomDirection());
                           var3.addScoreboardTag("halloweenduplicate");
                        }

                        var1.getWorld().spawnParticle(Particle.CLOUD, var1.getLocation(), 25, 0.0D, 0.0D, 0.0D, 0.2D);
                     }

                  }
               }
            }
         }
      }
   }

   private boolean isMonster(EntityType var1) {
      EntityType[] var2 = this.monsters;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EntityType var5 = var2[var4];
         if (var1 == var5) {
            return true;
         }
      }

      return false;
   }

   private static Vector getRandomDirection() {
      Vector var0 = new Vector();
      var0.setX(Math.random() * 2.0D - 1.0D);
      var0.setY(Math.random());
      var0.setZ(Math.random() * 2.0D - 1.0D);
      return var0.normalize().multiply(0.5D);
   }
}
