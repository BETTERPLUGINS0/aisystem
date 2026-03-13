package me.casperge.realisticseasons.seasonevent.buildin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.seasonevent.EventManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffectType;

public class StandardEventFileLoader {
   private YamlConfiguration conf;

   public StandardEventFileLoader(RealisticSeasons var1, EventManager var2) {
      File var3 = new File(var1.getDataFolder(), "events/");
      if (!var3.isDirectory()) {
         var3.mkdir();
      }

      File var4 = new File(var1.getDataFolder(), "events/events.yml");
      if (!var4.exists()) {
         InputStream var5 = var1.getResource("events.yml");

         try {
            FileUtils.copyInputStreamToFile(var5, var4);
         } catch (IOException var16) {
            var16.printStackTrace();
         }
      }

      this.conf = YamlConfiguration.loadConfiguration(var4);
      boolean var17 = false;
      if (!this.conf.contains("format")) {
         if (var1.getSettings().americandateformat) {
            var17 = true;
            this.conf.set("format", 1);
         } else {
            this.conf.set("format", 0);
         }
      } else {
         int var6 = this.conf.getInt("format");
         if (var6 == 0 && var1.getSettings().americandateformat) {
            var17 = true;
            this.conf.set("format", 1);
         } else if (var6 == 1 && !var1.getSettings().americandateformat) {
            var17 = true;
            this.conf.set("format", 0);
         }
      }

      if (var17) {
         Iterator var18 = this.conf.getConfigurationSection("dated-events").getKeys(false).iterator();

         while(var18.hasNext()) {
            String var7 = (String)var18.next();
            String[] var8;
            if (this.conf.getString("dated-events." + var7 + ".times.event-start.date").contains("/")) {
               var8 = this.conf.getString("dated-events." + var7 + ".times.event-start.date").trim().split("/");
               this.conf.set("dated-events." + var7 + ".times.event-start.date", var8[1] + "/" + var8[0]);
            }

            if (this.conf.getString("dated-events." + var7 + ".times.event-stop.date").contains("/")) {
               var8 = this.conf.getString("dated-events." + var7 + ".times.event-stop.date").trim().split("/");
               this.conf.set("dated-events." + var7 + ".times.event-stop.date", var8[1] + "/" + var8[0]);
            }
         }
      }

      if (!this.conf.contains("dated-events.easter.killer-bunny-spawn-rate")) {
         this.conf.set("dated-events.easter.killer-bunny-spawn-rate", 0.1D);
         var17 = true;
      }

      if (var17) {
         try {
            this.conf.save(var4);
         } catch (IOException var15) {
            var15.printStackTrace();
         }
      }

      List var19 = var2.getDatedEvents();
      Iterator var20 = this.conf.getConfigurationSection("dated-events").getKeys(false).iterator();

      while(true) {
         while(var20.hasNext()) {
            String var21 = (String)var20.next();
            if (var21.equals("christmas")) {
               ChristmasEvent var24 = new ChristmasEvent(this.conf.getConfigurationSection("dated-events." + var21));
               var2.christmasevent = var24;
               if (this.conf.getBoolean("dated-events." + var21 + ".enabled")) {
                  var19.add(var24);
               }

               if (this.conf.getBoolean("dated-events." + var21 + ".night-particles")) {
                  var24.nightParticles = true;
               }

               var1.christmasTreesEnabled = this.conf.getBoolean("dated-events." + var21 + ".christmas-trees.enabled");
               var24.presentsEnabled = this.conf.getBoolean("dated-events." + var21 + ".presents.enabled");
               var24.minChunkDistance = this.conf.getInt("dated-events." + var21 + ".presents.chunk-distance-between-spawns");
               var24.presentLoot = this.fromStringList(this.conf.getStringList("dated-events." + var21 + ".presents.loot"));
            } else if (var21.equals("newyear")) {
               NewYearEvent var23 = new NewYearEvent(this.conf.getConfigurationSection("dated-events." + var21));
               var2.newyearevent = var23;
               if (this.conf.getBoolean("dated-events." + var21 + ".enabled")) {
                  var19.add(var23);
               }

               if (this.conf.getBoolean("dated-events." + var21 + ".spawn-firework-boxes")) {
                  var23.spawnFireworkBoxes = true;
                  var23.fireworkDistance = this.conf.getInt("dated-events." + var21 + ".min-distance-between-firework");
               }
            } else if (var21.equals("easter")) {
               EasterEvent var22 = new EasterEvent(this.conf.getConfigurationSection("dated-events." + var21));
               var2.easterevent = var22;
               if (this.conf.getBoolean("dated-events." + var21 + ".enabled")) {
                  var19.add(var22);
               }

               var22.spawnKillerBunnies = this.conf.getBoolean("dated-events." + var21 + ".spawn-killer-bunnies");
               var22.killerBunnySpawnRate = this.conf.getDouble("dated-events." + var21 + ".killer-bunny-spawn-rate");
               if (this.conf.getBoolean("dated-events." + var21 + ".easter-eggs.enabled")) {
                  var22.minChunkDistance = this.conf.getInt("dated-events." + var21 + ".easter-eggs.chunk-distance-between-spawns");
                  var22.easterEggLoot = this.fromStringList(this.conf.getStringList("dated-events." + var21 + ".easter-eggs.loot"));
               }
            } else if (var21.equals("halloween")) {
               HalloweenEvent var9 = new HalloweenEvent(this.conf.getConfigurationSection("dated-events." + var21));
               var2.halloweenevent = var9;
               if (this.conf.getBoolean("dated-events." + var21 + ".enabled")) {
                  var19.add(var9);
               }

               if (this.conf.getBoolean("dated-events." + var21 + ".witch-extra-potions.enabled")) {
                  ArrayList var10 = new ArrayList();
                  Iterator var11 = this.conf.getStringList("dated-events." + var21 + ".witch-extra-potions.effects").iterator();

                  while(var11.hasNext()) {
                     String var12 = (String)var11.next();

                     try {
                        var10.add(PotionEffectType.getByName(var12));
                     } catch (Exception var14) {
                        Bukkit.getLogger().severe("[RealisticSeasons] An error occured loading potion effect type in events.yml: " + var12);
                        var14.printStackTrace();
                     }
                  }

                  var9.witchExtraPotions = var10;
               }

               var9.playParticlesAroundMobs = this.conf.getBoolean("dated-events." + var21 + ".play-particles-around-halloween-mobs");
               if (this.conf.getBoolean("dated-events." + var21 + ".duplicating-mobs.enabled")) {
                  var9.entityDupeChance = this.conf.getDouble("dated-events." + var21 + ".duplicating-mobs.spawn-chance");
               }

               if (this.conf.getBoolean("dated-events." + var21 + ".invisible-mobs.enabled")) {
                  var9.entityInvisibleChance = this.conf.getDouble("dated-events." + var21 + ".invisible-mobs.spawn-chance");
               }

               if (this.conf.getBoolean("dated-events." + var21 + ".fast-mobs.enabled")) {
                  var9.entityFastChance = this.conf.getDouble("dated-events." + var21 + ".fast-mobs.spawn-chance");
               }

               if (this.conf.getBoolean("dated-events." + var21 + ".flame-skeletons.enabled")) {
                  var9.skeletonFlameChance = this.conf.getDouble("dated-events." + var21 + ".flame-skeletons.spawn-chance");
               }

               if (this.conf.getBoolean("dated-events." + var21 + ".armored-mobs.enabled")) {
                  var9.armoredMobChance = this.conf.getDouble("dated-events." + var21 + ".armored-mobs.spawn-chance");
               }

               if (this.conf.getBoolean("dated-events." + var21 + ".armored-mobs.enabled")) {
                  var9.armoredMobChance = this.conf.getDouble("dated-events." + var21 + ".armored-mobs.spawn-chance");
               }

               if (this.conf.getBoolean("dated-events." + var21 + ".zombies-hold-pie.enabled")) {
                  var9.zombiePieChance = this.conf.getDouble("dated-events." + var21 + ".zombies-hold-pie.spawn-chance");
               }

               if (this.conf.getBoolean("dated-events." + var21 + ".vincicators-at-night.enabled")) {
                  var9.vindicatorChance = this.conf.getDouble("dated-events." + var21 + ".vincicators-at-night.spawn-chance");
               }
            }
         }

         return;
      }
   }

   private RandomItemStack[] fromStringList(List<String> var1) {
      RandomItemStack[] var2 = new RandomItemStack[var1.size()];

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         try {
            String var4 = ((String)var1.get(var3)).replaceAll(" ", "");
            String[] var5 = var4.split(":");
            int var6;
            int var7;
            if (var5[0].contains("-")) {
               String[] var8 = var5[0].split("-");
               var6 = Integer.valueOf(var8[0]);
               var7 = Integer.valueOf(var8[1]);
            } else {
               var6 = Integer.valueOf(var5[0]);
               var7 = var6;
            }

            Material var10 = Material.valueOf(var5[1]);
            var2[var3] = new RandomItemStack(var10, var6, var7);
         } catch (Exception var9) {
            Bukkit.getLogger().severe("[RealisticSeasons] Error loading events.yml line: " + (String)var1.get(var3));
            var9.printStackTrace();
         }
      }

      return var2;
   }
}
