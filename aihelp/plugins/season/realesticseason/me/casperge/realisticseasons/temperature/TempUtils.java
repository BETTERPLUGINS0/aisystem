package me.casperge.realisticseasons.temperature;

import java.util.Iterator;
import java.util.Random;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.Version;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TempUtils {
   private final Material[] leather;
   private final Material[] iron;
   private final Material[] gold;
   private final Material[] diamond;
   private final Material[] netherite;
   private RealisticSeasons main;
   private TempData tempdata;
   private final Random r;
   public int tickRate;

   public TempUtils(RealisticSeasons var1, TempData var2) {
      this.leather = new Material[]{Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_HELMET};
      this.iron = new Material[]{Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS};
      this.gold = new Material[]{Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS};
      this.diamond = new Material[]{Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS};
      this.netherite = new Material[]{Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS};
      this.r = new Random();
      this.tickRate = 40;
      this.main = var1;
      this.tempdata = var2;
   }

   public int getCurrentWorldTemperature(World var1) {
      int var2 = this.getCurrentBaseTemperature(var1) + this.getWeatherModifier(var1) + this.getCurrentTimeModifier(var1);
      if (var2 > this.tempdata.getTempSettings().getMaxTemperature(this.main.getSeasonManager().getSeason(var1))) {
         return this.tempdata.getTempSettings().getMaxTemperature(this.main.getSeasonManager().getSeason(var1));
      } else {
         return var2 < this.tempdata.getTempSettings().getMinTemperature(this.main.getSeasonManager().getSeason(var1)) ? this.tempdata.getTempSettings().getMinTemperature(this.main.getSeasonManager().getSeason(var1)) : var2;
      }
   }

   private int getCurrentBaseTemperature(World var1) {
      return this.main.getTemperatureManager().getTempData().getBaseTemperature(var1);
   }

   private int getWeatherModifier(World var1) {
      if (var1.isThundering()) {
         return this.tempdata.getTempSettings().getWeatherModifier(TemperatureSettings.Weather.STORM);
      } else {
         return var1.hasStorm() ? this.tempdata.getTempSettings().getWeatherModifier(TemperatureSettings.Weather.RAIN) : 0;
      }
   }

   private int getCurrentTimeModifier(World var1) {
      if (var1.getTime() < 22500L && var1.getTime() >= 6000L) {
         if (var1.getTime() >= 14800L && var1.getTime() < 22500L) {
            return -5;
         }

         if (var1.getTime() >= 6000L && var1.getTime() < 12000L) {
            return 3;
         }

         if (var1.getTime() >= 12000L && var1.getTime() < 12450L) {
            return 2;
         }

         if (var1.getTime() >= 12450L && var1.getTime() < 12900L) {
            return 1;
         }

         if (var1.getTime() >= 12900L && var1.getTime() < 13350L) {
            return 0;
         }

         if (var1.getTime() >= 13350L && var1.getTime() < 13800L) {
            return -1;
         }

         if (var1.getTime() >= 13800L && var1.getTime() < 14200L) {
            return -2;
         }

         if (var1.getTime() >= 14200L && var1.getTime() < 14400L) {
            return -3;
         }

         if (var1.getTime() >= 14400L && var1.getTime() < 14600L) {
            return -4;
         }

         if (var1.getTime() >= 14600L && var1.getTime() < 14800L) {
            return -5;
         }
      } else {
         if (var1.getTime() >= 22500L && var1.getTime() < 23500L) {
            return -5;
         }

         if (var1.getTime() >= 23500L || var1.getTime() < 500L) {
            return -4;
         }

         if (var1.getTime() >= 500L && var1.getTime() < 1500L) {
            return -3;
         }

         if (var1.getTime() >= 1500L && var1.getTime() < 2500L) {
            return -2;
         }

         if (var1.getTime() >= 2500L && var1.getTime() < 3500L) {
            return -1;
         }

         if (var1.getTime() >= 3500L && var1.getTime() < 4500L) {
            return 0;
         }

         if (var1.getTime() >= 4500L && var1.getTime() < 5500L) {
            return 1;
         }

         if (var1.getTime() >= 5500L) {
            return 2;
         }
      }

      return 0;
   }

   public int getArmorModified(Player var1, int var2) {
      if (var1.getInventory().getArmorContents().length == 0) {
         return var2;
      } else {
         float var3 = 0.0F;
         ItemStack[] var4 = var1.getInventory().getArmorContents();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            ItemStack var7 = var4[var6];
            if (var7 != null) {
               boolean var8 = false;
               CustomTemperatureItem var9 = null;
               Iterator var10 = this.tempdata.getTempSettings().getCustomItems().iterator();

               while(var10.hasNext()) {
                  CustomTemperatureItem var11 = (CustomTemperatureItem)var10.next();
                  if (var11.onWear() && var11.isItem(var7)) {
                     var8 = true;
                     var9 = var11;
                  }
               }

               if (var8) {
                  var3 += (float)var9.getModifier();
               } else if (this.containsMaterial(var7.getType(), this.leather)) {
                  if (var3 + (float)var2 < (float)this.tempdata.getTempSettings().getLeatherCap()) {
                     var3 += (float)this.tempdata.getTempSettings().getArmorModifier(TemperatureSettings.Armor.LEATHER) / 4.0F;
                  }
               } else if (this.containsMaterial(var7.getType(), this.iron)) {
                  var3 += (float)this.tempdata.getTempSettings().getArmorModifier(TemperatureSettings.Armor.IRON) / 4.0F;
               } else if (this.containsMaterial(var7.getType(), this.gold)) {
                  var3 += (float)this.tempdata.getTempSettings().getArmorModifier(TemperatureSettings.Armor.GOLD) / 4.0F;
               } else if (this.containsMaterial(var7.getType(), this.diamond)) {
                  var3 += (float)this.tempdata.getTempSettings().getArmorModifier(TemperatureSettings.Armor.DIAMOND) / 4.0F;
               } else if (this.containsMaterial(var7.getType(), this.netherite)) {
                  var3 += (float)this.tempdata.getTempSettings().getArmorModifier(TemperatureSettings.Armor.NETHERITE) / 4.0F;
               }
            }
         }

         return (int)((float)var2 + var3);
      }
   }

   public int getBlockLightModified(Entity var1, int var2) {
      if (var2 < 25) {
         int var3 = (int)((float)var1.getLocation().getBlock().getRelative(0, 1, 0).getLightFromBlocks() * 2.0F);
         return var2 + var3 > 25 ? 25 : var2 + var3;
      } else {
         return var2;
      }
   }

   public int getWaterModified(LivingEntity var1, int var2, Season var3) {
      boolean var4 = false;
      if (var1.isInsideVehicle()) {
         if (var1.getVehicle().getType() == EntityType.BOAT) {
            var4 = true;
         } else if (Version.is_1_19_or_up() && var1.getVehicle().getType() == EntityType.CHEST_BOAT) {
            var4 = true;
         }
      }

      boolean var5 = false;
      int var6;
      if (var1.getWorld().hasStorm()) {
         var6 = var1.getLocation().getWorld().getHighestBlockYAt(var1.getLocation());
         if ((double)var6 <= var1.getLocation().getY()) {
            var5 = true;
         }
      }

      if ((!this.isInWater(var1) && !this.main.getNMSUtils().isInPowderedSnow(var1) || var4) && !var5) {
         return var2;
      } else {
         var6 = this.main.getTemperatureManager().getTempData().getWaterTemperatureModifier(var1.getLocation(), var3);
         if (var6 == 5555) {
            var6 = this.tempdata.getTempSettings().getSwimmingModifier(this.main.getSeasonManager().getSeason(var1.getWorld()));
         }

         return var2 + var6;
      }
   }

   public int getWaterModified(Player var1, int var2, Season var3) {
      boolean var4 = false;
      if (var1.isInsideVehicle()) {
         if (var1.getVehicle().getType() == EntityType.BOAT) {
            var4 = true;
         } else if (Version.is_1_19_or_up() && var1.getVehicle().getType() == EntityType.CHEST_BOAT) {
            var4 = true;
         }
      }

      boolean var5 = false;
      int var6;
      if (var1.getWorld().hasStorm()) {
         var6 = var1.getLocation().getWorld().getHighestBlockYAt(var1.getLocation());
         if ((double)var6 <= var1.getLocation().getY()) {
            var5 = true;
         }
      }

      if ((!this.isInWater(var1) && !this.main.getNMSUtils().isInPowderedSnow(var1) || var4) && !var5) {
         var6 = this.main.getTemperatureManager().getTempData().getWaterModifier(var1);
         if (var6 >= 0) {
            if (var6 <= 1) {
               this.main.getTemperatureManager().getTempData().removeFromWaterList(var1);
               return var2;
            } else {
               this.main.getTemperatureManager().getTempData().setWaterModifier(var1, var6 - 1);
               return var2 + (var6 - 1);
            }
         } else if (var6 == -1) {
            this.main.getTemperatureManager().getTempData().removeFromWaterList(var1);
            return var2;
         } else {
            this.main.getTemperatureManager().getTempData().setWaterModifier(var1, var6 + 1);
            return var2 + var6 + 1;
         }
      } else {
         var6 = this.main.getTemperatureManager().getTempData().getWaterTemperatureModifier(var1.getLocation(), var3);
         if (var6 == 5555) {
            var6 = this.tempdata.getTempSettings().getSwimmingModifier(this.main.getSeasonManager().getSeason(var1.getWorld()));
         }

         this.main.getTemperatureManager().getTempData().setWaterModifier(var1, var6);
         return var2 + var6;
      }
   }

   public int getVelocityModified(Player var1, int var2) {
      double var3 = this.tempdata.getTempSettings().getVelocityModifier();
      if (var1.isGliding()) {
         var3 *= 2.0D;
      }

      double var5 = var1.getVelocity().length();
      int var7 = (int)(var3 * var5);
      return var2 + var7;
   }

   public int getSprintingModified(Player var1, int var2) {
      int var3 = this.main.getTemperatureManager().getTempData().getSprintingModifier(var1);
      if (var1.isSprinting() && !var1.isFlying() && !var1.isGliding() && !var1.isSwimming()) {
         if (var3 >= this.tempdata.getTempSettings().getMaxSprintingModifier()) {
            return var2 + var3;
         } else if (var3 + 1 == this.tempdata.getTempSettings().getMaxSprintingModifier()) {
            this.main.getTemperatureManager().getTempData().setSprintingModifier(var1, var3 + 1);
            return var2 + var3 + 1;
         } else {
            this.main.getTemperatureManager().getTempData().setSprintingModifier(var1, var3 + 2);
            return var2 + var3 + 2;
         }
      } else if (var3 <= 0) {
         return var2;
      } else {
         this.main.getTemperatureManager().getTempData().setSprintingModifier(var1, var3 - 1);
         return var2 + var3 - 1;
      }
   }

   public int getFoodModified(Player var1, int var2) {
      if (var2 > 25) {
         if (this.hasDrinked(var1)) {
            if (var2 + this.tempdata.getTempSettings().getWaterBottleModifier() < 25) {
               return 25;
            }

            return var2 + this.tempdata.getTempSettings().getWaterBottleModifier();
         }
      } else if (var1.getFoodLevel() >= 18) {
         if (var2 + this.tempdata.getTempSettings().getFullHungerModifier() > 25) {
            return 25;
         }

         return var2 + this.tempdata.getTempSettings().getFullHungerModifier();
      }

      return var2;
   }

   public int getHeightModified(Location var1, int var2) {
      if (!this.tempdata.getTempSettings().isHeightModifying()) {
         return var2;
      } else {
         if (this.main.getSeasonManager().getSeason(var1.getWorld()) == Season.WINTER) {
            if (var1.getBlockY() < 64) {
               return var2 + (64 - var1.getBlockY()) / 10;
            }
         } else if (var1.getBlockY() > 64) {
            double var3 = this.tempdata.getTempSettings().getHeightBlockIncrease();
            if (this.tempdata.getTempSettings().getHeightBlockIncreaseOverwrites().containsKey(var1.getWorld().getName())) {
               var3 = (Double)this.tempdata.getTempSettings().getHeightBlockIncreaseOverwrites().get(var1.getWorld().getName());
            }

            return var2 - (int)((double)(var1.getBlockY() - 64) * var3);
         }

         return var2;
      }
   }

   public int getBiomeModified(Location var1, int var2) {
      return var2 + this.main.getTemperatureManager().getTempData().getBiomeTemperatureModifier(var1);
   }

   public void applyEffect(Player var1, TempEffect var2) {
      switch(var2) {
      case COLD_FREEZING:
         this.main.getTemperatureManager().setFreezing(var1, true, true);
         break;
      case COLD_HUNGER:
         if (this.main.getTemperatureManager().getTempData().getTempSettings().getColdHungerType() != null) {
            var1.addPotionEffect(new PotionEffect(this.main.getTemperatureManager().getTempData().getTempSettings().getColdHungerType(), this.tickRate + 60, this.main.getTemperatureManager().getTempData().getTempSettings().getColdHungerLevel() - 1, false, false));
         }
         break;
      case COLD_SLOWNESS:
         if (this.main.getTemperatureManager().getTempData().getTempSettings().getColdSlownessType() != null) {
            var1.addPotionEffect(new PotionEffect(this.main.getTemperatureManager().getTempData().getTempSettings().getColdSlownessType(), this.tickRate + 60, this.main.getTemperatureManager().getTempData().getTempSettings().getColdSlownessLevel() - 1, false, false));
         }

         this.main.getTemperatureManager().setFreezing(var1, true, false);
         break;
      case HEAT_FIRE:
         if (this.tickRate < 40) {
            var1.setFireTicks(this.tickRate + 40);
         } else {
            var1.setFireTicks(this.tickRate + 20);
         }
         break;
      case HEAT_NO_HEALING:
         this.main.getTemperatureManager().setHealing(var1, false);
         break;
      case HEAT_SLOWNESS:
         if (this.main.getTemperatureManager().getTempData().getTempSettings().getWarmSlownessType() != null) {
            var1.addPotionEffect(new PotionEffect(this.main.getTemperatureManager().getTempData().getTempSettings().getWarmSlownessType(), this.tickRate + 60, this.main.getTemperatureManager().getTempData().getTempSettings().getWarmSlownessLevel() - 1, false, false));
         }
         break;
      case BOOSTS:
         Iterator var3 = this.tempdata.getTempSettings().getBoostPotionEffects().iterator();

         while(var3.hasNext()) {
            PotionEffectType var4 = (PotionEffectType)var3.next();
            if (var4.equals(PotionEffectType.HEALTH_BOOST)) {
               if (!var1.hasPotionEffect(var4)) {
                  var1.addPotionEffect(new PotionEffect(var4, Integer.MAX_VALUE, 0, false, false));
               }

               return;
            }

            if (!var1.hasPotionEffect(var4)) {
               var1.addPotionEffect(new PotionEffect(var4, this.tickRate + 240, 0, false, false));
            } else if (var1.getPotionEffect(var4).getDuration() < 120) {
               var1.addPotionEffect(new PotionEffect(var4, this.tickRate + 240, 0, false, false));
            }
         }

         return;
      case HYDRATED:
         if (this.tempdata.getTempSettings().getHydrationEffect() != null && !var1.hasPotionEffect(this.tempdata.getTempSettings().getHydrationEffect())) {
            var1.addPotionEffect(new PotionEffect(this.tempdata.getTempSettings().getHydrationEffect(), this.tickRate + 240, this.tempdata.getTempSettings().getHydrationLevel(), false, false));
         }
      }

   }

   public int generateNewBaseTemperature(World var1) {
      Season var2 = this.main.getSeasonManager().getSeason(var1);
      int var3 = this.tempdata.getTempSettings().getMinTemperature(var2);
      int var4 = this.tempdata.getTempSettings().getMaxTemperature(var2);
      if (var3 > var4) {
         int var5 = var3;
         var3 = var4;
         var4 = var5;
      }

      return var3 == var4 ? var4 : this.r.nextInt(var4 - var3 + 1) + var3;
   }

   public int generateNextDayBaseTemp(World var1, int var2) {
      Season var3 = this.main.getSeasonManager().getSeason(var1);
      int var4 = this.tempdata.getTempSettings().getMinTemperature(var3);
      int var5 = this.tempdata.getTempSettings().getMaxTemperature(var3);
      int var6 = this.r.nextInt(var5 - var4 + 1) + var4;
      if (var6 < var2) {
         return var2 - 2;
      } else {
         return var6 > var2 ? var2 + 2 : var2;
      }
   }

   public boolean containsMaterial(Material var1, Material[] var2) {
      Material[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Material var6 = var3[var5];
         if (var6 == var1) {
            return true;
         }
      }

      return false;
   }

   public boolean isInWater(Entity var1) {
      return var1.getLocation().getBlock().getType() == Material.WATER || var1.getLocation().getBlock().getRelative(0, 1, 0).getType() == Material.WATER;
   }

   public boolean hasDrinked(Player var1) {
      return System.currentTimeMillis() - this.main.getTemperatureManager().getTempData().getLastDrink(var1) < (long)(this.main.getTemperatureManager().getTempData().getTempSettings().getWaterBottleEffectDuration() * 1000);
   }

   public boolean flashingWaterBottle(Player var1) {
      return System.currentTimeMillis() - this.main.getTemperatureManager().getTempData().getLastDrink(var1) > (long)(this.main.getTemperatureManager().getTempData().getTempSettings().getWaterBottleEffectDuration() * 900);
   }
}
