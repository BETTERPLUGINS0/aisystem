package me.casperge.realisticseasons.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import me.casperge.enums.GameRuleType;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.blockscanner.blocksaver.StoredBlockType;
import me.casperge.realisticseasons.calendar.Date;
import me.casperge.realisticseasons.commands.ToggleSeasonsCommand;
import me.casperge.realisticseasons.particle.ParticleManager;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.temperature.TempData;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataHandler {
   private RealisticSeasons main;
   private File dataFile;
   private YamlConfiguration data;

   public DataHandler(RealisticSeasons var1) {
      this.dataFile = new File(var1.getDataFolder(), "data.yml");
      if (!this.dataFile.exists()) {
         File var2 = new File(var1.getDataFolder(), "data/");
         if (var2.isDirectory()) {
            this.dataFile = new File(var1.getDataFolder(), "data/data.yml");
         } else {
            try {
               InputStream var3 = var1.getResource("data.yml");
               FileUtils.copyInputStreamToFile(var3, this.dataFile);
            } catch (IOException var4) {
               var4.printStackTrace();
            }
         }
      }

      this.data = YamlConfiguration.loadConfiguration(this.dataFile);
      this.main = var1;
      this.load();
   }

   public void save() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.main.getSeasonManager().worldData.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.put(((World)var3.getKey()).getName(), ((Season)var3.getValue()).getConfigName());
      }

      var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var13 = (String)var2.next();
         this.data.set("worlds." + var13, var1.get(var13));
      }

      if (this.main.getSeasonCycle().lastRecordedRealLifeTime != null) {
         this.data.set("last-recorded-date", this.main.getSeasonCycle().lastRecordedRealLifeTime.toString());
      }

      WeakHashMap var12 = this.main.getTimeManager().getDates();
      Iterator var14 = var12.keySet().iterator();

      while(var14.hasNext()) {
         World var4 = (World)var14.next();
         if (var12.get(var4) != null) {
            this.data.set("dates." + var4.getName() + ".day", ((Date)var12.get(var4)).getDay());
            this.data.set("dates." + var4.getName() + ".month", ((Date)var12.get(var4)).getMonth());
            this.data.set("dates." + var4.getName() + ".year", ((Date)var12.get(var4)).getYear());
         }
      }

      ArrayList var15 = new ArrayList();
      Iterator var16 = ToggleSeasonsCommand.disabled.iterator();

      while(var16.hasNext()) {
         UUID var5 = (UUID)var16.next();
         var15.add(var5.toString());
      }

      this.data.set("players.colorsdisabled", var15);
      ArrayList var17 = new ArrayList();
      Iterator var18 = this.main.getTemperatureManager().getTemperatureDisabled().iterator();

      while(var18.hasNext()) {
         UUID var6 = (UUID)var18.next();
         var17.add(var6.toString());
      }

      this.data.set("players.tempdisabled", var17);
      ArrayList var19 = new ArrayList();
      this.main.getParticleManager();
      Iterator var20 = ParticleManager.disabledParticles.iterator();

      UUID var7;
      while(var20.hasNext()) {
         var7 = (UUID)var20.next();
         var19.add(var7.toString());
      }

      this.data.set("players.particlesdisabled", var19);
      this.data.set("players.tempeffects", (Object)null);
      var20 = this.main.getTemperatureManager().getTempData().getActiveCustomEffects().keySet().iterator();

      while(true) {
         do {
            if (!var20.hasNext()) {
               this.data.set("temperature", (Object)null);
               var20 = this.main.getTemperatureManager().getTempData().getEnabledWorlds().iterator();

               while(var20.hasNext()) {
                  World var22 = (World)var20.next();
                  this.data.set("temperature." + var22.getName(), true);
               }

               this.data.set("time-paused", (Object)null);
               ArrayList var21 = new ArrayList();
               Iterator var23 = this.main.getTimeManager().getPausedWorlds().iterator();

               while(var23.hasNext()) {
                  World var25 = (World)var23.next();
                  var21.add(var25.getName());
               }

               this.data.set("time-paused", var21);
               ArrayList var24 = new ArrayList();
               Iterator var26 = this.main.getTemperatureManager().getKnownPlayers().iterator();

               while(var26.hasNext()) {
                  UUID var28 = (UUID)var26.next();
                  var24.add(var28.toString());
               }

               this.data.set("players.knownplayers", var24);
               this.data.set("players.fahrenheit-mode", (Object)null);
               ArrayList var27 = new ArrayList();
               Iterator var29 = this.main.getTemperatureManager().getFahrenheitEnabled().iterator();

               while(var29.hasNext()) {
                  UUID var10 = (UUID)var29.next();
                  var27.add(var10.toString());
               }

               this.data.set("players.fahrenheit-mode", var27);
               this.main.getBlockStorage().save(this.data);

               try {
                  this.data.save(this.dataFile);
               } catch (IOException var11) {
                  var11.printStackTrace();
               }

               return;
            }

            var7 = (UUID)var20.next();
         } while(((List)this.main.getTemperatureManager().getTempData().getActiveCustomEffects().get(var7)).size() == 0);

         for(int var8 = 0; var8 < ((List)this.main.getTemperatureManager().getTempData().getActiveCustomEffects().get(var7)).size(); ++var8) {
            TempData.CustomTemperatureEffect var9 = (TempData.CustomTemperatureEffect)((List)this.main.getTemperatureManager().getTempData().getActiveCustomEffects().get(var7)).get(var8);
            this.data.set("players.tempeffects." + var7.toString() + "." + var8 + ".modifier", var9.getTemperatureModifier());
            this.data.set("players.tempeffects." + var7.toString() + "." + var8 + ".start", var9.getStartTime());
            this.data.set("players.tempeffects." + var7.toString() + "." + var8 + ".duration", var9.getDuration());
         }
      }
   }

   public void load() {
      ConfigurationSection var1 = this.data.getConfigurationSection("worlds");
      Iterator var3;
      String var4;
      String var6;
      if (var1 != null) {
         Set var2 = var1.getKeys(false);
         if (!var2.isEmpty()) {
            var3 = var2.iterator();

            while(var3.hasNext()) {
               var4 = (String)var3.next();
               World var5 = Bukkit.getWorld(var4);
               if (var5 != null) {
                  var6 = var1.getString(var4);
                  if (var6 != null) {
                     this.main.getSeasonManager().worldData.put(var5, Season.getSeason(var6));
                     if (this.main.getSettings().affectTime && Season.getSeason(var6) != Season.DISABLED && Season.getSeason(var6) != Season.RESTORE && this.main.getGameRuleGetter().GetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var5)) {
                        this.main.getGameRuleGetter().SetBooleanGameRule(GameRuleType.DO_DAYLIGHT_CYCLE, var5, false);
                     }

                     if (this.main.getTimeManager().getDate(var5) == null) {
                        this.main.getTimeManager().setDate(var5, this.main.getTimeManager().getCalendar().getSeasonStart(Season.getSeason(var6)));
                     }
                  }
               }
            }
         }
      }

      List var12 = this.data.getStringList("players.colorsdisabled");
      var3 = var12.iterator();

      while(var3.hasNext()) {
         var4 = (String)var3.next();
         ToggleSeasonsCommand.disabled.add(UUID.fromString(var4));
      }

      List var13;
      Iterator var14;
      String var18;
      if (this.data.contains("players.tempdisabled")) {
         var13 = this.data.getStringList("players.tempdisabled");
         var14 = var13.iterator();

         while(var14.hasNext()) {
            var18 = (String)var14.next();
            this.main.getTemperatureManager().disableTemperature(UUID.fromString(var18));
         }
      }

      Iterator var20;
      if (this.data.contains("players.particlesdisabled")) {
         var13 = this.data.getStringList("players.particlesdisabled");
         ArrayList var16 = new ArrayList();
         var20 = var13.iterator();

         while(var20.hasNext()) {
            var6 = (String)var20.next();
            var16.add(UUID.fromString(var6));
         }

         ParticleManager.disabledParticles = var16;
      }

      if (this.data.contains("players.knownplayers")) {
         var13 = this.data.getStringList("players.knownplayers");
         var14 = var13.iterator();

         while(var14.hasNext()) {
            var18 = (String)var14.next();
            this.main.getTemperatureManager().loggedIn(UUID.fromString(var18));
         }
      }

      if (this.data.contains("last-recorded-date")) {
         ZonedDateTime var15 = ZonedDateTime.parse(this.data.getString("last-recorded-date"));
         this.main.setLoadedTime(var15);
      }

      String var7;
      World var8;
      int var11;
      ConfigurationSection var21;
      Set var22;
      Iterator var23;
      if (this.data.isConfigurationSection("dates")) {
         HashMap var17 = new HashMap();
         var21 = this.data.getConfigurationSection("dates");
         if (var21 != null) {
            var22 = this.data.getConfigurationSection("dates").getKeys(false);
            if (!var22.isEmpty()) {
               var23 = var22.iterator();

               while(var23.hasNext()) {
                  var7 = (String)var23.next();
                  var8 = Bukkit.getWorld(var7);
                  if (var8 != null) {
                     int var9 = this.data.getInt("dates." + var7 + ".day");
                     int var10 = this.data.getInt("dates." + var7 + ".month");
                     var11 = this.data.getInt("dates." + var7 + ".year");
                     var17.put(var8, new Date(var9, var10, var11));
                  }
               }
            }
         }

         this.main.getTimeManager().addToDatesRegister(var17);
      }

      World var24;
      if (this.data.isConfigurationSection("temperature")) {
         ArrayList var19 = new ArrayList();
         var21 = this.data.getConfigurationSection("temperature");
         if (var21 != null) {
            var22 = this.data.getConfigurationSection("temperature").getKeys(false);
            if (!var22.isEmpty()) {
               var23 = var22.iterator();

               while(var23.hasNext()) {
                  var7 = (String)var23.next();
                  var8 = Bukkit.getWorld(var7);
                  if (var8 != null && this.data.getBoolean("temperature." + var7)) {
                     var19.add(var8);
                  }
               }
            }
         }

         var20 = var19.iterator();

         while(var20.hasNext()) {
            var24 = (World)var20.next();
            this.main.getTemperatureManager().getTempData().enableWorld(var24);
         }
      }

      if (this.data.isList("time-paused")) {
         var13 = this.data.getStringList("time-paused");
         var14 = var13.iterator();

         while(var14.hasNext()) {
            var18 = (String)var14.next();
            var24 = Bukkit.getWorld(var18);
            if (var24 != null) {
               this.main.getTimeManager().pauseTime(var24);
            }
         }
      }

      if (this.data.contains("players.tempeffects")) {
         var3 = this.data.getConfigurationSection("players.tempeffects").getKeys(false).iterator();

         while(var3.hasNext()) {
            var4 = (String)var3.next();
            UUID var25 = UUID.fromString(var4);
            var23 = this.data.getConfigurationSection("players.tempeffects." + var4).getKeys(false).iterator();

            while(var23.hasNext()) {
               var7 = (String)var23.next();
               int var26 = this.data.getInt("players.tempeffects." + var4 + "." + var7 + ".duration");
               long var27 = this.data.getLong("players.tempeffects." + var4 + "." + var7 + ".start");
               var11 = this.data.getInt("players.tempeffects." + var4 + "." + var7 + ".modifier");
               this.main.getTemperatureManager().getTempData().applyCustomEffect(var25, var11, var26, var27);
            }
         }
      }

      if (this.data.contains("players.fahrenheit-mode")) {
         var13 = this.data.getStringList("players.fahrenheit-mode");
         var14 = var13.iterator();

         while(var14.hasNext()) {
            var18 = (String)var14.next();
            this.main.getTemperatureManager().toggleFahrenheit(UUID.fromString(var18));
         }
      }

      if (this.data.isConfigurationSection("placedblocks")) {
         this.main.getBlockStorage().load(this.data.getConfigurationSection("placedblocks"), StoredBlockType.FLOWER);
      }

      if (this.data.isConfigurationSection("placedpresents")) {
         this.main.getBlockStorage().load(this.data.getConfigurationSection("placedpresents"), StoredBlockType.PRESENT);
         this.data.set("placedpresents", (Object)null);
      }

      if (this.data.isConfigurationSection("placedeggs")) {
         this.main.getBlockStorage().load(this.data.getConfigurationSection("placedeggs"), StoredBlockType.EGG);
         this.data.set("placedeggs", (Object)null);
      }

   }
}
