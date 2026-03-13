package me.casperge.realisticseasons.api;

import java.util.ArrayList;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.calendar.Date;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

public class SeasonsAPI {
   private static SeasonsAPI api;
   private RealisticSeasons main;

   public SeasonsAPI(RealisticSeasons var1) {
      api = this;
      this.main = var1;
   }

   public Season getSeason(World var1) {
      return this.main.getSeasonManager() == null ? Season.DISABLED : this.main.getSeasonManager().getSeason(var1);
   }

   public void setSeason(World var1, Season var2) {
      if (this.main.getSeasonManager() == null) {
         this.main.getSeasonManager().setSeason(var1, var2);
      }

   }

   public static SeasonsAPI getInstance() {
      return api;
   }

   public void setDate(World var1, Date var2) {
      if (this.main.getTimeManager() == null) {
         this.main.getTimeManager().setDate(var1, var2);
      }

   }

   public int getAirTemperature(Location var1) {
      return this.main.getTemperatureManager() == null ? 0 : this.main.getTemperatureManager().getAirTemperature(var1);
   }

   public Date getDate(World var1) {
      return this.main.getTimeManager() == null ? null : this.main.getTimeManager().getDate(var1);
   }

   public String getDayOfWeek(World var1) {
      if (this.main.getSeasonManager() == null) {
         return "DISABLED";
      } else if (!this.main.getSeasonManager().worldData.containsKey(var1)) {
         return "DISABLED";
      } else {
         Date var2 = this.main.getTimeManager().getDate(var1);
         return var2 == null ? "DISABLED" : this.main.getTimeManager().getWeekDay(this.main.getTimeManager().getDate(var1));
      }
   }

   public String getCurrentMonthName(World var1) {
      if (this.main.getSeasonManager() == null) {
         return "DISABLED";
      } else if (!this.main.getSeasonManager().worldData.containsKey(var1)) {
         return "DISABLED";
      } else {
         Date var2 = this.main.getTimeManager().getDate(var1);
         return var2 == null ? "DISABLED" : this.main.getTimeManager().getCalendar().getMonth(this.main.getTimeManager().getDate(var1).getMonth()).getName();
      }
   }

   public int getTemperature(Player var1) {
      return this.main.getTemperatureManager() == null ? 0 : this.main.getTemperatureManager().getTemperature(var1);
   }

   public int getMinutes(World var1) {
      return this.main.getTimeManager() == null ? 0 : this.main.getTimeManager().getMinutes(var1);
   }

   public int getSeconds(World var1) {
      return this.main.getTimeManager() == null ? 0 : this.main.getTimeManager().getSeconds(var1);
   }

   public int getHours(World var1) {
      return this.main.getTimeManager() == null ? 0 : this.main.getTimeManager().getHours(var1);
   }

   public void applyTimedTemperatureEffect(Player var1, int var2, int var3) {
      this.main.getTemperatureManager().getTempData().applyCustomEffect(var1, var2, var3);
   }

   public TemperatureEffect applyPermanentTemperatureEffect(Player var1, int var2) {
      return this.main.getTemperatureManager().getTempData().applyPermanentEffect(var1, var2);
   }

   public List<String> getActiveEvents(World var1) {
      return this.main.getEventManager() == null ? null : new ArrayList(this.main.getEventManager().getActiveEvents(var1));
   }

   public SeasonBiome getReplacementSeasonBiome(Biome var1, Season var2) {
      String var3 = this.main.getNMSUtils().getBiomeName(var1);
      return this.main.getSeasonBiomesForAPI().containsKey(var3) ? (SeasonBiome)this.main.getSeasonBiomesForAPI().get(var3) : null;
   }

   public SeasonBiome getReplacementSeasonBiome(String var1, Season var2) {
      return this.main.getSeasonBiomesForAPI().containsKey(var1) ? (SeasonBiome)this.main.getSeasonBiomesForAPI().get(var1) : null;
   }

   public SeasonBiome getReplacementSeasonBiome(Location var1, Season var2) {
      String var3 = this.main.getNMSUtils().getBiomeName(var1);
      return this.main.getSeasonBiomesForAPI().containsKey(var3) ? (SeasonBiome)this.main.getSeasonBiomesForAPI().get(var3) : null;
   }
}
