package me.casperge.realisticseasons.temperature;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimalTemperature extends BukkitRunnable {
   private HashMap<String, List<Entity>> toTick;
   private HashMap<String, Integer> entityCount;
   private TemperatureSettings tsettings;
   private TempUtils temputils;
   private int counter;
   private RealisticSeasons main;
   private int refreshInterval = 40;

   public AnimalTemperature(RealisticSeasons var1) {
      this.main = var1;
      if (var1.getTemperatureManager().getTempData().isEnabled()) {
         this.tsettings = var1.getTemperatureManager().getTempData().getTempSettings();
         this.temputils = var1.getTemperatureManager().getTempUtils();
         this.counter = 0;
      }

   }

   public void run() {
      if (this.main.getTemperatureManager().getTempData().isEnabled()) {
         if (this.tsettings.isEntityTemperatureEnabled()) {
            Iterator var1;
            if (this.counter <= this.refreshInterval && this.toTick != null) {
               var1 = this.toTick.keySet().iterator();

               while(true) {
                  List var4;
                  String var14;
                  do {
                     World var3;
                     do {
                        if (!var1.hasNext()) {
                           ++this.counter;
                           return;
                        }

                        var14 = (String)var1.next();
                        var3 = Bukkit.getWorld(var14);
                     } while(var3 == null);

                     var4 = (List)this.toTick.get(var14);
                  } while(var4.isEmpty());

                  int var5 = (Integer)this.entityCount.get(var14) / this.refreshInterval;
                  if (var5 > var4.size()) {
                     var5 = var4.size();
                  }

                  if (this.counter >= this.refreshInterval) {
                     var5 = var4.size();
                  }

                  List var6 = var4.subList(var4.size() - var5, var4.size());
                  Iterator var7 = var6.iterator();

                  while(var7.hasNext()) {
                     Entity var8 = (Entity)var7.next();
                     if (var8 instanceof LivingEntity && !(var8 instanceof Player) && !this.tsettings.getEntitiesWithoutTemperature().contains(var8.getType())) {
                        boolean var9 = this.tsettings.getEntitiesImmuneToCold().contains(var8.getType());
                        boolean var10 = this.tsettings.getEntitiesImmuneToHeat().contains(var8.getType());
                        boolean var11 = this.tsettings.getEntitiesResilient().contains(var8.getType());
                        int var12 = this.temputils.getCurrentWorldTemperature(var8.getWorld());
                        var12 = this.temputils.getBiomeModified(var8.getLocation(), var12);
                        var12 = this.temputils.getHeightModified(var8.getLocation(), var12);
                        var12 = this.temputils.getWaterModified((LivingEntity)var8, var12, this.main.getSeasonManager().getSeason(var8.getWorld()));
                        var12 = this.temputils.getBlockLightModified(var8, var12);
                        LivingEntity var13 = (LivingEntity)var8;
                        if (var11 && var12 < 25) {
                           var12 += 15;
                           if (var12 > 25) {
                              var12 = 25;
                           }
                        }

                        if (!var9) {
                           if (var12 <= this.tsettings.getEntityColdSlownessStart() && this.tsettings.getEntityColdSlownessEffect() != null) {
                              var13.addPotionEffect(new PotionEffect(this.tsettings.getEntityColdSlownessEffect(), this.refreshInterval * 2, this.tsettings.getEntityColdSlownessLevel()));
                           }

                           if (var12 <= this.tsettings.getEntityColdFreezing()) {
                              var13.setFreezeTicks(this.refreshInterval + 40);
                              var13.damage(1.0D);
                           }
                        }

                        if (!var10) {
                           if (var12 >= this.tsettings.getEntityWarmSlownessStart() && this.tsettings.getEntityWarmSlownessEffect() != null) {
                              var13.addPotionEffect(new PotionEffect(this.tsettings.getEntityWarmSlownessEffect(), this.refreshInterval * 2, this.tsettings.getEntityWarmSlownessLevel()));
                           }

                           if (var12 >= this.tsettings.getEntityWarmBurn()) {
                              var13.setFireTicks(this.refreshInterval + 40);
                           }
                        }
                     }
                  }

                  var6.clear();
               }
            } else {
               this.counter = 0;
               this.toTick = new HashMap();
               this.entityCount = new HashMap();
               var1 = this.main.getTemperatureManager().getTempData().getEnabledWorlds().iterator();

               while(var1.hasNext()) {
                  World var2 = (World)var1.next();
                  this.toTick.put(var2.getName(), var2.getEntities());
                  this.entityCount.put(var2.getName(), ((List)this.toTick.get(var2.getName())).size());
               }

            }
         }
      }
   }
}
