package me.casperge.realisticseasons.seasonevent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import me.casperge.realisticseasons.RealisticSeasons;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

public class EventFileLoader {
   private YamlConfiguration conf;

   public EventFileLoader(RealisticSeasons var1, EventManager var2) {
      File var3 = new File(var1.getDataFolder(), "events/");
      if (!var3.isDirectory()) {
         var3.mkdir();
      }

      File var4 = new File(var1.getDataFolder(), "events/custom-events.yml");
      if (!var4.exists()) {
         InputStream var5 = var1.getResource("custom-events.yml");

         try {
            FileUtils.copyInputStreamToFile(var5, var4);
         } catch (IOException var10) {
            var10.printStackTrace();
         }
      }

      this.conf = YamlConfiguration.loadConfiguration(var4);
      ArrayList var11 = new ArrayList();
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();
      Iterator var8;
      String var9;
      if (this.conf.contains("custom-events.dated-events")) {
         var8 = this.conf.getConfigurationSection("custom-events.dated-events").getKeys(false).iterator();

         while(var8.hasNext()) {
            var9 = (String)var8.next();
            if (this.conf.getBoolean("custom-events.dated-events." + var9 + ".enabled")) {
               var11.add(new CustomDatedEvent(this.conf.getConfigurationSection("custom-events.dated-events." + var9)));
            }
         }
      }

      if (this.conf.contains("custom-events.weekly-events")) {
         var8 = this.conf.getConfigurationSection("custom-events.weekly-events").getKeys(false).iterator();

         while(var8.hasNext()) {
            var9 = (String)var8.next();
            if (this.conf.getBoolean("custom-events.weekly-events." + var9 + ".enabled")) {
               var6.add(new CustomWeeklyEvent(this.conf.getConfigurationSection("custom-events.weekly-events." + var9)));
            }
         }
      }

      if (this.conf.contains("custom-events.weekly-events")) {
         var8 = this.conf.getConfigurationSection("custom-events.weekly-events").getKeys(false).iterator();

         while(var8.hasNext()) {
            var9 = (String)var8.next();
            if (this.conf.getBoolean("custom-events.daily-events." + var9 + ".enabled")) {
               var7.add(new CustomDailyEvent(this.conf.getConfigurationSection("custom-events.daily-events." + var9)));
            }
         }
      }

      var2.setDailyEvents(var7);
      var2.setDatedEvents(var11);
      var2.setWeeklyEvents(var6);
   }
}
