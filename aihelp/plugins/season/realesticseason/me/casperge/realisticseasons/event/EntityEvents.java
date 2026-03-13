package me.casperge.realisticseasons.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.casperge.enums.GameRuleType;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.api.CustomBiomeFileLoader;
import me.casperge.realisticseasons.api.CustomWorldGenerator;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.utils.JavaUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Illager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class EntityEvents implements Listener {
   private RealisticSeasons main;
   private Random r = new Random();
   private EntityType[] animals;
   private HashMap<EntityType, Material[]> tamingMaterials;
   private List<UUID> alreadyMessagedForCustomGenerator;
   private final String[] countriesWithFahrenheit;
   public static boolean plissue = false;

   public EntityEvents(RealisticSeasons var1) {
      this.animals = new EntityType[]{EntityType.CHICKEN, EntityType.COW, EntityType.PIG, EntityType.SHEEP};
      this.tamingMaterials = new HashMap();
      this.alreadyMessagedForCustomGenerator = new ArrayList();
      this.countriesWithFahrenheit = new String[]{"US", "LR", "BZ", "BS", "FM", "AG", "KY", "BM", "MH", "KN", "TC", "VG", "PW", "MS"};
      var1.getServer().getPluginManager().registerEvents(this, var1);
      this.main = var1;
      this.tamingMaterials.put(EntityType.SHEEP, new Material[]{Material.WHEAT});
      this.tamingMaterials.put(EntityType.COW, new Material[]{Material.WHEAT});
      this.tamingMaterials.put(EntityType.MUSHROOM_COW, new Material[]{Material.WHEAT});
      this.tamingMaterials.put(EntityType.PIG, new Material[]{Material.CARROT, Material.POTATO, Material.BEETROOT});
      this.tamingMaterials.put(EntityType.CHICKEN, new Material[]{Material.WHEAT_SEEDS, Material.PUMPKIN_SEEDS, Material.MELON_SEEDS, Material.BEETROOT_SEEDS});
      this.tamingMaterials.put(EntityType.OCELOT, new Material[]{Material.SALMON, Material.COD});
      this.tamingMaterials.put(EntityType.RABBIT, new Material[]{Material.DANDELION, Material.CARROT, Material.GOLDEN_CARROT});
      this.tamingMaterials.put(EntityType.PANDA, new Material[]{Material.BAMBOO});
      this.tamingMaterials.put(EntityType.FOX, new Material[]{Material.SWEET_BERRIES});
      this.tamingMaterials.put(EntityType.BEE, new Material[]{Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.RED_TULIP, Material.ORANGE_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.OXEYE_DAISY, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.WITHER_ROSE, Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH, Material.PEONY});
   }

   @EventHandler
   public void damageEvent(EntityDamageByEntityEvent var1) {
      if (var1.getDamager() instanceof Firework) {
         Firework var2 = (Firework)var1.getDamager();
         if (var2.hasMetadata("nodamage")) {
            var1.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void onDrink(PlayerItemConsumeEvent var1) {
      if (var1.getItem().getType() == Material.POTION) {
         this.main.getTemperatureManager().getTempData().drinked(var1.getPlayer());
      }

   }

   @EventHandler
   public void onHeal(EntityRegainHealthEvent var1) {
      if (var1.getEntity() instanceof Player && !this.main.getTemperatureManager().canHeal((Player)var1.getEntity())) {
         var1.setCancelled(true);
      }

   }

   @EventHandler
   public void onLogin(final PlayerLoginEvent var1) {
      if (this.main.getTemperatureManager().getTempData().isEnabled()) {
         if (!this.main.getTemperatureManager().hasPlayedBefore(var1.getPlayer().getUniqueId())) {
            this.main.getTemperatureManager().loggedIn(var1.getPlayer().getUniqueId());
            Bukkit.getScheduler().runTask(this.main, new Runnable() {
               public void run() {
                  final boolean var1x = EntityEvents.this.main.getTemperatureManager().getTempData().getTempSettings().isConvertToFahrenheit();
                  if (EntityEvents.this.main.getTemperatureManager().getTempData().isEnabled() && EntityEvents.this.main.getTemperatureManager().getTempData().getTempSettings().isIpBasedTemperature()) {
                     Bukkit.getScheduler().runTaskAsynchronously(EntityEvents.this.main, new Runnable() {
                        public void run() {
                           String var1xx = JavaUtils.getGeoLocationCountryCode(var1.getAddress().getHostAddress());
                           if (var1xx != null) {
                              boolean var2 = false;
                              String[] var3 = EntityEvents.this.countriesWithFahrenheit;
                              int var4 = var3.length;

                              for(int var5 = 0; var5 < var4; ++var5) {
                                 String var6 = var3[var5];
                                 if (var6.equals(var1xx)) {
                                    var2 = true;
                                    break;
                                 }
                              }

                              if (var2 && !var1x || !var2 && var1x) {
                                 Bukkit.getScheduler().runTask(EntityEvents.this.main, new Runnable() {
                                    public void run() {
                                       EntityEvents.this.main.getTemperatureManager().toggleFahrenheit(var1.getPlayer().getUniqueId());
                                    }
                                 });
                              }

                           }
                        }
                     });
                  }

               }
            });
         }

      }
   }

   @EventHandler
   public void onJoin(PlayerJoinEvent var1) {
      if (var1.getPlayer().hasPermission("realisticseasons.admin")) {
         if (plissue) {
            var1.getPlayer().sendMessage(ChatColor.RED + "[RealisticSeasons] An error occurred while loading ProtocolLib. Please make sure you've installed the latest dev build of ProtocolLib found here: https://ci.dmulloy2.net/job/ProtocolLib/");
         }

         if (!this.alreadyMessagedForCustomGenerator.contains(var1.getPlayer().getUniqueId())) {
            this.alreadyMessagedForCustomGenerator.add(var1.getPlayer().getUniqueId());
            List var2 = CustomBiomeFileLoader.getActiveGenerators();
            List var3 = CustomBiomeFileLoader.getAlreadyInstalledGenerators();
            ArrayList var4 = new ArrayList();
            Iterator var5 = var2.iterator();

            CustomWorldGenerator var6;
            while(var5.hasNext()) {
               var6 = (CustomWorldGenerator)var5.next();
               if (!var3.contains(var6)) {
                  var4.add(var6);
               }
            }

            if (!var4.isEmpty()) {
               var1.getPlayer().sendMessage(ChatColor.DARK_GREEN + "[RealisticSeasons]" + ChatColor.GREEN + " Supported world generator detected. Install the required config files by running: ");
               var5 = var4.iterator();

               while(var5.hasNext()) {
                  var6 = (CustomWorldGenerator)var5.next();
                  var1.getPlayer().sendMessage(ChatColor.AQUA + "/rs install " + var6.toString());
               }
            }

            if (this.main.isFreshInstall && this.main.getSeasonManager().worldData.isEmpty()) {
               this.main.isFreshInstall = false;
               var1.getPlayer().sendMessage(ChatColor.DARK_GREEN + "[RealisticSeasons]" + ChatColor.GREEN + " thank you for installing RealisticSeasons. Get started by running" + ChatColor.DARK_GREEN + " /rs set <season>");
            }
         }
      }

   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onMobSpawn(CreatureSpawnEvent var1) {
      if (!this.main.getSettings().spawnEntities || !this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_MOB_SPAWNING, var1.getEntity().getWorld())) {
         if (var1.getSpawnReason() == SpawnReason.NATURAL && var1.getLocation().getWorld().getEnvironment() != Environment.NETHER && var1.getLocation().getWorld().getEnvironment() != Environment.THE_END) {
            Season var2 = this.main.getSeasonManager().getSeason(var1.getLocation().getWorld());
            if (var2 != Season.DISABLED) {
               if (var2 == Season.FALL && var1.getEntity() instanceof Mob && this.main.getSettings().pumpkinsInFall && var1.getEntity() instanceof LivingEntity && Math.random() * 100.0D < (double)this.main.getSettings().pumpkinchance && !(var1.getEntity() instanceof Illager)) {
                  LivingEntity var3 = var1.getEntity();
                  var3.getEquipment().setHelmet(new ItemStack(Material.CARVED_PUMPKIN, 1));
               }

               if (var1.getEntityType() == EntityType.ZOMBIE || var1.getEntityType() == EntityType.SKELETON || var1.getEntityType() == EntityType.CREEPER || var1.getEntityType() == EntityType.SPIDER) {
                  if (var2 == Season.SUMMER && this.main.getSettings().spawnHusksInSummer) {
                     if (var1.getEntityType() == EntityType.ZOMBIE && this.r.nextInt(10) != 0) {
                        var1.setCancelled(true);
                        var1.getLocation().getWorld().spawnEntity(var1.getLocation(), EntityType.HUSK);
                     }
                  } else if (var2 == Season.FALL) {
                     if (this.main.getSettings().spawnCaveSpidersInFall && var1.getEntityType() == EntityType.SPIDER && this.r.nextInt(3) == 1) {
                        var1.setCancelled(true);
                        var1.getLocation().getWorld().spawnEntity(var1.getLocation(), EntityType.CAVE_SPIDER);
                     } else if (this.main.getSettings().spawnExtraSpidersInFall && this.r.nextInt(2) == 0 && var1.getEntityType() == EntityType.SPIDER) {
                        var1.getLocation().getWorld().spawnEntity(var1.getLocation(), EntityType.SPIDER);
                     }

                     if (this.main.getSettings().spawnBatsInFall && this.r.nextInt(5) == 0) {
                        var1.getLocation().getWorld().spawnEntity(var1.getLocation().add(0.0D, 1.0D, 0.0D), EntityType.BAT);
                     }

                     if (var1.getEntityType() == EntityType.SPIDER && this.main.getSettings().spawnExtraSpidersInFall && this.r.nextInt(3) == 0) {
                        var1.getLocation().getWorld().spawnEntity(var1.getLocation(), EntityType.SPIDER);
                     }
                  } else if (var2 == Season.WINTER && this.main.getSettings().spawnStraysInWinter && var1.getEntityType() == EntityType.SKELETON && this.r.nextInt(10) != 0) {
                     var1.setCancelled(true);
                     var1.getLocation().getWorld().spawnEntity(var1.getLocation(), EntityType.STRAY);
                  }
               }
            }
         }

      }
   }

   @EventHandler
   public void EntityDamage(EntityDamageEvent var1) {
      if (var1.getEntity().getType() == EntityType.SNOWMAN && this.main.getSettings().snowmanImmuneToWater && var1.getCause() == DamageCause.DROWNING) {
         var1.setCancelled(true);
      }

   }

   @EventHandler
   public void snow(EntityBlockFormEvent var1) {
      if (var1.getEntity().getType() == EntityType.SNOWMAN) {
         if (var1.getEntity().getScoreboardTags().contains("seasonal")) {
            var1.setCancelled(true);
         } else if (this.main.getSettings().snowmanCantPlaceSnow) {
            var1.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void onSnowball(ProjectileLaunchEvent var1) {
      if (var1.getEntityType() == EntityType.SNOWBALL) {
         if (var1.getEntity().getShooter() == null) {
            return;
         }

         if (!(var1.getEntity().getShooter() instanceof Entity)) {
            return;
         }

         Entity var2 = (Entity)var1.getEntity().getShooter();
         if (var2.getType() == EntityType.SNOWMAN && var2.getScoreboardTags().contains("seasonal") && !this.main.getSettings().snowmanAttackMobs) {
            var1.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void onTarget(EntityTargetEvent var1) {
      if (var1.getEntityType() == EntityType.SNOWMAN) {
         if (var1.getEntity().getScoreboardTags().contains("seasonal") && !this.main.getSettings().snowmanAttackMobs) {
            var1.setCancelled(true);
         }
      } else if ((var1.getEntityType() == EntityType.WOLF || var1.getEntityType() == EntityType.FOX) && var1.getEntity().getScoreboardTags().contains("seasonal")) {
         if (var1.getTarget() == null) {
            return;
         }

         if (var1.getEntityType() == EntityType.WOLF && var1.getTarget().getType() != EntityType.SHEEP) {
            return;
         }

         var1.setCancelled(true);
      }

   }

   @EventHandler
   public void onDamage(EntityDamageByBlockEvent var1) {
      if (var1.getDamager() != null) {
         if (var1.getDamager().getType() == Material.SWEET_BERRY_BUSH && !(var1.getEntity() instanceof Player) && !this.main.getSettings().doBerryDamage) {
            var1.setCancelled(true);
         }

      }
   }

   @EventHandler
   public void onCreeperExplode(EntityExplodeEvent var1) {
      if (var1.getEntityType() == EntityType.CREEPER && var1.getEntity().getScoreboardTags().contains("halloween")) {
         Creeper var2 = (Creeper)var1.getEntity();
         var2.getActivePotionEffects().forEach((var1x) -> {
            var2.removePotionEffect(var1x.getType());
         });
      }

   }

   @EventHandler
   public void entityFeedEvent(PlayerInteractAtEntityEvent var1) {
      boolean var2 = false;
      EntityType[] var3 = this.animals;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EntityType var6 = var3[var5];
         if (var6 == var1.getRightClicked().getType()) {
            var2 = true;
         }
      }

      if (var2 && var1.getRightClicked().getScoreboardTags().contains("seasonal") && var1.getPlayer().getInventory().getItem(var1.getHand()) != null) {
         Material var7 = var1.getPlayer().getInventory().getItem(var1.getHand()).getType();
         if (this.tamingMaterials.containsKey(var1.getRightClicked().getType()) && this.containsMaterial(var7, (Material[])this.tamingMaterials.get(var1.getRightClicked().getType()))) {
            var1.getRightClicked().removeScoreboardTag("seasonal");
         }
      }

   }

   @EventHandler
   public void onDeath(PlayerDeathEvent var1) {
      if (this.main.getTemperatureManager().getTempData().isEnabled()) {
         this.main.getTemperatureManager().getTempData().removeIfDrinked(var1.getEntity());
         this.main.getTemperatureManager().resetTemperature(var1.getEntity().getUniqueId());
      }

   }

   @EventHandler
   public void onRespawn(PlayerRespawnEvent var1) {
      if (this.main.getTemperatureManager().getTempData().isEnabled()) {
         this.main.getTemperatureManager().resetTemperature(var1.getPlayer().getUniqueId());
      }

   }

   public boolean containsMaterial(Material var1, Material[] var2) {
      boolean var3 = false;
      Material[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Material var7 = var4[var6];
         if (var7 == var1) {
            var3 = true;
            break;
         }
      }

      return var3;
   }
}
