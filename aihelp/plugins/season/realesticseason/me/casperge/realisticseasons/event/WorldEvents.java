package me.casperge.realisticseasons.event;

import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.calendar.Date;
import me.casperge.realisticseasons.data.Settings;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldInitEvent;

public class WorldEvents implements Listener {
   RealisticSeasons main;

   public WorldEvents(RealisticSeasons var1) {
      var1.getServer().getPluginManager().registerEvents(this, var1);
      this.main = var1;
   }

   @EventHandler
   public void weatherChange(WeatherChangeEvent var1) {
      if (this.main.getSettings().affectWeather && this.main.getSeasonManager().getSeason(var1.getWorld()) != Season.DISABLED && this.main.getSeasonManager().getSeason(var1.getWorld()) != Season.RESTORE) {
         Settings.WeatherSettings var2 = (Settings.WeatherSettings)this.main.getSettings().weathersettings.get(this.main.getSeasonManager().getSeason(var1.getWorld()));
         if (var1.toWeatherState() && !var2.hasRain()) {
            var1.setCancelled(true);
         }

         if (this.main.getSeasonManager().getSeason(var1.getWorld()) == Season.WINTER) {
            var1.getWorld().setThunderDuration(0);
         }
      }

   }

   @EventHandler
   public void onWorldCreate(WorldInitEvent var1) {
      World var2 = var1.getWorld();
      if (var2.getEnvironment() != Environment.THE_END && var2.getEnvironment() != Environment.NETHER) {
         if (this.main.getSeasonManager().getSeason(var2) == Season.DISABLED && this.main.getSettings().defaultSeason != Season.DISABLED) {
            this.main.getSeasonManager().setSeason(var2, this.main.getSettings().defaultSeason);
         }

         if (this.main.getSettings().syncNewWorlds) {
            this.main.getSettings().addSyncedWorld(var1.getWorld().getName());
            Date var3 = this.main.getTimeManager().currentDateFromCalendar();
            Season var4 = this.main.getTimeManager().getCalendar().getSeason(var3);
            this.main.getSeasonManager().setSeason(var2, var4, false);
         }
      }

   }
}
