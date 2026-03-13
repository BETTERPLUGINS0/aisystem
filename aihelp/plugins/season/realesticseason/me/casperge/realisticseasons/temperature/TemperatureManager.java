package me.casperge.realisticseasons.temperature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.Version;
import me.casperge.realisticseasons.api.landplugins.LandPlugin;
import me.casperge.realisticseasons.api.landplugins.Priority;
import me.casperge.realisticseasons.runnables.TemperatureUpdater;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.utils.ChunkUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class TemperatureManager {
   private TempData tempdata;
   private TempUtils temputils;
   private RealisticSeasons main;
   private boolean isEnabled;
   private HashMap<UUID, Float> temperatures = new HashMap();
   private List<UUID> noHealing = new ArrayList();
   private List<UUID> freezing = new ArrayList();
   private List<UUID> fahrenheitEnabled = new ArrayList();
   private List<UUID> temperatureDisabled = new ArrayList();
   private List<UUID> hasCheckedTemperatureBefore = new ArrayList();
   private List<UUID> rsBurn = new ArrayList();
   private boolean is1_17;

   public TemperatureManager(final RealisticSeasons var1) {
      this.main = var1;
      this.tempdata = new TempData(var1);
      this.is1_17 = Version.is_1_17_or_up();
      if (this.is1_17) {
         BukkitScheduler var2 = var1.getServer().getScheduler();
         var2.scheduleSyncRepeatingTask(var1, new Runnable() {
            int counter = 0;

            public void run() {
               ++this.counter;
               ArrayList var1x = new ArrayList();
               Iterator var2 = TemperatureManager.this.freezing.iterator();

               UUID var3;
               Player var4;
               while(var2.hasNext()) {
                  var3 = (UUID)var2.next();
                  var4 = Bukkit.getPlayer(var3);
                  if (var4 != null) {
                     var1.getNMSUtils().setFrozen(var4, false);
                  } else {
                     var1x.add(var3);
                  }
               }

               TemperatureManager.this.freezing.removeAll(var1x);
               var1x.clear();
               if (this.counter > 20) {
                  if (TemperatureManager.this.tempdata.isEnabled() && TemperatureManager.this.tempdata.getTempSettings().isReducedMovementSpeedEnabled()) {
                     var2 = Bukkit.getOnlinePlayers().iterator();

                     while(var2.hasNext()) {
                        Player var5 = (Player)var2.next();
                        if (!TemperatureManager.this.getTempData().getEnabledWorlds().contains(var5.getWorld())) {
                           var5.setWalkSpeed(0.2F);
                        }
                     }
                  }

                  this.counter = 0;
                  var2 = TemperatureManager.this.freezing.iterator();

                  while(var2.hasNext()) {
                     var3 = (UUID)var2.next();
                     var4 = Bukkit.getPlayer(var3);
                     if (var4 != null) {
                        if (!TemperatureManager.this.getTempData().getEnabledWorlds().contains(var4.getWorld())) {
                           var1x.add(var3);
                        }
                     } else {
                        var1x.add(var3);
                     }
                  }

                  TemperatureManager.this.freezing.removeAll(var1x);
                  var1x.clear();
                  var2 = TemperatureManager.this.noHealing.iterator();

                  while(var2.hasNext()) {
                     var3 = (UUID)var2.next();
                     var4 = Bukkit.getPlayer(var3);
                     if (var4 != null) {
                        if (!TemperatureManager.this.getTempData().getEnabledWorlds().contains(var4.getWorld())) {
                           var1x.add(var3);
                        }
                     } else {
                        var1x.add(var3);
                     }
                  }

                  TemperatureManager.this.noHealing.removeAll(var1x);
                  var1x.clear();
                  var2 = TemperatureManager.this.rsBurn.iterator();

                  while(var2.hasNext()) {
                     var3 = (UUID)var2.next();
                     var4 = Bukkit.getPlayer(var3);
                     if (var4 == null) {
                        var1x.add(var3);
                     } else if (!var4.isOnline()) {
                        var1x.add(var3);
                     } else if (!TemperatureManager.this.getTempData().getEnabledWorlds().contains(var4.getWorld())) {
                        var1x.add(var3);
                     }
                  }

                  TemperatureManager.this.rsBurn.removeAll(var1x);
               }

            }
         }, 0L, 1L);
      }

   }

   public void loggedIn(UUID var1) {
      this.hasCheckedTemperatureBefore.add(var1);
   }

   public boolean hasPlayedBefore(UUID var1) {
      return this.hasCheckedTemperatureBefore.contains(var1);
   }

   public boolean hasRSBurn(UUID var1) {
      return this.rsBurn.contains(var1);
   }

   public void resetTemperature(UUID var1) {
      if (this.temperatures.containsKey(var1)) {
         this.temperatures.remove(var1);
      }

      this.tempdata.resetBlockEffects(var1);
   }

   public void setRSBurn(UUID var1, boolean var2) {
      if (var2) {
         if (!this.rsBurn.contains(var1)) {
            this.rsBurn.add(var1);
         }
      } else if (this.rsBurn.contains(var1)) {
         this.rsBurn.remove(var1);
      }

   }

   public int getPlayerAirTemperature(Player var1) {
      Location var2 = var1.getLocation();
      if (!var2.getWorld().isChunkLoaded(var2.getBlockX() >> 4, var2.getBlockZ() >> 4)) {
         return 5;
      } else if (var2.getWorld().getEnvironment() != Environment.NETHER && var2.getWorld().getEnvironment() != Environment.THE_END) {
         int var3 = this.temputils.getCurrentWorldTemperature(var2.getWorld());
         var3 = this.temputils.getBiomeModified(var2, var3);
         int var4 = 0;
         if (this.main.getTemperatureManager().getTempData().getBlockEffects().containsKey(var1.getUniqueId())) {
            var4 = (Integer)this.main.getTemperatureManager().getTempData().getBlockEffects().get(var1.getUniqueId());
         }

         var3 += var4;
         return this.temputils.getHeightModified(var2, var3);
      } else if (var2.getWorld().getEnvironment() == Environment.NETHER) {
         return this.tempdata.getTempSettings().getNetherTemperature();
      } else {
         return var2.getWorld().getEnvironment() == Environment.THE_END ? this.tempdata.getTempSettings().getEndTemperature() : 25;
      }
   }

   public int getAirTemperature(Location var1) {
      if (!var1.getWorld().isChunkLoaded(var1.getBlockX() >> 4, var1.getBlockZ() >> 4)) {
         return 5;
      } else if (var1.getWorld().getEnvironment() != Environment.NETHER && var1.getWorld().getEnvironment() != Environment.THE_END) {
         int var2 = this.temputils.getCurrentWorldTemperature(var1.getWorld());
         var2 = this.temputils.getBiomeModified(var1, var2);
         return this.temputils.getHeightModified(var1, var2);
      } else if (var1.getWorld().getEnvironment() == Environment.NETHER) {
         return this.tempdata.getTempSettings().getNetherTemperature();
      } else {
         return var1.getWorld().getEnvironment() == Environment.THE_END ? this.tempdata.getTempSettings().getEndTemperature() : 25;
      }
   }

   public void load() {
      this.isEnabled = this.tempdata.load();
      this.temputils = new TempUtils(this.main, this.tempdata);
      if (this.isEnabled) {
         this.generateTempForLoadedWorlds();
         TemperatureUpdater var1 = new TemperatureUpdater(this.main, this);
         var1.runTaskTimer(this.main, (long)this.tempdata.getTempSettings().getTemperatureUpdateInterval(), (long)this.tempdata.getTempSettings().getTemperatureUpdateInterval());
         this.temputils.tickRate = this.tempdata.getTempSettings().getTemperatureUpdateInterval();
      }
   }

   public TempData getTempData() {
      return this.tempdata;
   }

   public void disableTemperature(Player var1) {
      if (!this.temperatureDisabled.contains(var1.getUniqueId())) {
         this.temperatureDisabled.add(var1.getUniqueId());
      }

   }

   public void enableTemperature(Player var1) {
      if (this.temperatureDisabled.contains(var1.getUniqueId())) {
         this.temperatureDisabled.remove(var1.getUniqueId());
      }

   }

   public void disableTemperature(UUID var1) {
      if (!this.temperatureDisabled.contains(var1)) {
         this.temperatureDisabled.add(var1);
      }

   }

   public void enableTemperature(UUID var1) {
      if (this.temperatureDisabled.contains(var1)) {
         this.temperatureDisabled.remove(var1);
      }

   }

   public void toggleFahrenheit(Player var1) {
      if (this.fahrenheitEnabled.contains(var1.getUniqueId())) {
         this.fahrenheitEnabled.remove(var1.getUniqueId());
      } else {
         this.fahrenheitEnabled.add(var1.getUniqueId());
      }

   }

   public void toggleFahrenheit(UUID var1) {
      if (this.fahrenheitEnabled.contains(var1)) {
         this.fahrenheitEnabled.remove(var1);
      } else {
         this.fahrenheitEnabled.add(var1);
      }

   }

   public boolean hasFahrenheitEnabled(Player var1) {
      return this.fahrenheitEnabled.contains(var1.getUniqueId());
   }

   public List<UUID> getFahrenheitEnabled() {
      return this.fahrenheitEnabled;
   }

   public List<UUID> getKnownPlayers() {
      return this.hasCheckedTemperatureBefore;
   }

   public List<UUID> getTemperatureDisabled() {
      return this.temperatureDisabled;
   }

   public boolean hasTemperature(Player var1) {
      if (!this.tempdata.isEnabled()) {
         return false;
      } else {
         return !this.temperatureDisabled.contains(var1.getUniqueId());
      }
   }

   public TempUtils getTempUtils() {
      return this.temputils;
   }

   public void generateTempForLoadedWorlds() {
      Iterator var1 = this.main.getTemperatureManager().getTempData().getEnabledWorlds().iterator();

      while(var1.hasNext()) {
         World var2 = (World)var1.next();
         if (var2.getEnvironment() != Environment.NETHER && var2.getEnvironment() != Environment.THE_END) {
            this.main.getTemperatureManager().getTempData().setBaseTemperature(var2, this.temputils.generateNewBaseTemperature(var2));
         }
      }

   }

   public void loadWorld(World var1) {
      this.main.getTemperatureManager().getTempData().setBaseTemperature(var1, this.temputils.generateNewBaseTemperature(var1));
      this.main.getTemperatureManager().getTempData().enableWorld(var1);
   }

   public ChatColor getColorCode(int var1) {
      ChatColor var2 = ChatColor.GREEN;
      if (this.getTempData().getTempSettings().getColdHungerTemp() >= var1) {
         var2 = ChatColor.AQUA;
      }

      if (this.getTempData().getTempSettings().getColdSlownessTemp() >= var1) {
         var2 = ChatColor.BLUE;
      }

      if (this.getTempData().getTempSettings().getColdFreezingTemp() >= var1) {
         var2 = ChatColor.DARK_BLUE;
      }

      if (this.getTempData().getTempSettings().getWarmNoHealingTemp() <= var1) {
         var2 = ChatColor.GOLD;
      }

      if (this.getTempData().getTempSettings().getWarmSlownessTemp() <= var1) {
         var2 = ChatColor.RED;
      }

      if (this.getTempData().getTempSettings().getWarmFireTemp() <= var1) {
         var2 = ChatColor.DARK_RED;
      }

      if (this.getTempData().getTempSettings().getBoostMinTemp() <= var1 && this.getTempData().getTempSettings().getBoostMaxTemp() >= var1 && this.getTempData().getTempSettings().getBoostPotionEffects().size() > 0) {
         var2 = ChatColor.LIGHT_PURPLE;
      }

      return var2;
   }

   public int calculateTemperature(Player var1) {
      if (!ChunkUtils.isChunkLoaded(var1.getLocation())) {
         return 10;
      } else {
         if (this.main.hasLandPlugin()) {
            boolean var2 = false;
            int var3 = 0;
            Priority var4 = null;
            Iterator var5 = this.main.getLandPluginAPIs().iterator();

            while(var5.hasNext()) {
               LandPlugin var6 = (LandPlugin)var5.next();
               if (var6.getPermanentTemperature(var1.getLocation().getChunk().getX(), var1.getLocation().getChunk().getZ(), var1.getWorld()) != null) {
                  int var7 = var6.getPermanentTemperature(var1.getLocation().getChunk().getX(), var1.getLocation().getChunk().getZ(), var1.getWorld());
                  var2 = true;
                  if (var4 == null) {
                     var4 = var6.getPriority();
                     var3 = var7;
                  } else if (var6.getPriority().isHigherThan(var4)) {
                     var4 = var6.getPriority();
                     var3 = var7;
                  }
               }
            }

            if (var2) {
               return var3;
            }
         }

         if (!this.main.hasSeasons(var1.getLocation().getChunk().getX(), var1.getLocation().getChunk().getZ(), var1.getWorld())) {
            return 25;
         } else {
            int var8;
            if (var1.getWorld().getEnvironment() != Environment.NETHER && var1.getWorld().getEnvironment() != Environment.THE_END) {
               var8 = this.temputils.getCurrentWorldTemperature(var1.getWorld());
               var8 = this.temputils.getBiomeModified(var1.getLocation(), var8);
               var8 = this.temputils.getHeightModified(var1.getLocation(), var8);
               var8 = this.temputils.getWaterModified(var1, var8, this.main.getSeasonManager().getSeason(var1.getWorld()));
               var8 = this.temputils.getSprintingModified(var1, var8);
               var8 = this.temputils.getVelocityModified(var1, var8);
               var8 = this.temputils.getArmorModified(var1, var8);
               var8 = this.temputils.getFoodModified(var1, var8);
               var8 = this.tempdata.getCustomEffectsModified(var1, var8);
               var8 = this.tempdata.getPermanentEffectsModified(var1, var8);
               return var8;
            } else if (var1.getWorld().getEnvironment() == Environment.NETHER) {
               var8 = this.tempdata.getTempSettings().getNetherTemperature();
               var8 = this.temputils.getSprintingModified(var1, var8);
               var8 = this.temputils.getVelocityModified(var1, var8);
               var8 = this.temputils.getArmorModified(var1, var8);
               var8 = this.temputils.getFoodModified(var1, var8);
               var8 = this.tempdata.getCustomEffectsModified(var1, var8);
               var8 = this.tempdata.getPermanentEffectsModified(var1, var8);
               return var8;
            } else if (var1.getWorld().getEnvironment() == Environment.THE_END) {
               var8 = this.tempdata.getTempSettings().getEndTemperature();
               var8 = this.temputils.getWaterModified(var1, var8, Season.WINTER);
               var8 = this.temputils.getSprintingModified(var1, var8);
               var8 = this.temputils.getVelocityModified(var1, var8);
               var8 = this.temputils.getArmorModified(var1, var8);
               var8 = this.temputils.getFoodModified(var1, var8);
               var8 = this.tempdata.getCustomEffectsModified(var1, var8);
               var8 = this.tempdata.getPermanentEffectsModified(var1, var8);
               return var8;
            } else {
               return 25;
            }
         }
      }
   }

   public void setHealing(Player var1, boolean var2) {
      if (var2) {
         if (this.noHealing.contains(var1.getUniqueId())) {
            this.noHealing.remove(var1.getUniqueId());
         }
      } else if (!this.noHealing.contains(var1.getUniqueId())) {
         this.noHealing.add(var1.getUniqueId());
      }

   }

   public boolean canHeal(Player var1) {
      return !this.noHealing.contains(var1.getUniqueId());
   }

   public void setFreezing(Player var1, boolean var2, boolean var3) {
      if (!var2) {
         if (this.freezing.contains(var1.getUniqueId())) {
            this.freezing.remove(var1.getUniqueId());
         }
      } else {
         this.main.getNMSUtils().setFrozen(var1, var3);
         if (!this.freezing.contains(var1.getUniqueId())) {
            this.freezing.add(var1.getUniqueId());
         }
      }

   }

   public boolean isFrozen(Player var1) {
      return this.freezing.contains(var1.getUniqueId());
   }

   public int getTemperature(Player var1) {
      if (this.temperatures.containsKey(var1.getUniqueId())) {
         return ((Float)this.temperatures.get(var1.getUniqueId())).intValue();
      } else {
         this.temperatures.put(var1.getUniqueId(), (float)this.calculateTemperature(var1));
         return ((Float)this.temperatures.get(var1.getUniqueId())).intValue();
      }
   }

   public void updateTemperature(Player var1) {
      float var2 = (float)this.calculateTemperature(var1);
      float var3;
      if (this.temperatures.containsKey(var1.getUniqueId())) {
         var3 = (Float)this.temperatures.get(var1.getUniqueId());
      } else {
         var3 = var2;
      }

      float var4;
      if (var2 != var3) {
         float var5;
         if (var2 < var3) {
            var5 = (var2 - var3) * this.main.getTemperatureManager().getTempData().getTempSettings().getTemperatureChangeRate();
            var4 = var3 + var5;
         } else {
            var5 = (var2 - var3) * this.main.getTemperatureManager().getTempData().getTempSettings().getTemperatureChangeRate();
            var4 = var3 + var5;
         }
      } else {
         var4 = var2;
      }

      this.temperatures.put(var1.getUniqueId(), var4);
   }
}
