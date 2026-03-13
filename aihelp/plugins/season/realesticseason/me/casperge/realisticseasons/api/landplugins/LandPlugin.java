package me.casperge.realisticseasons.api.landplugins;

import me.casperge.realisticseasons.season.Season;
import org.bukkit.World;

public interface LandPlugin {
   boolean hasBlockChanges(int var1, int var2, World var3);

   boolean hasMobSpawns(int var1, int var2, World var3);

   Priority getPriority();

   boolean hasSeasonEffects(int var1, int var2, World var3);

   Integer getPermanentTemperature(int var1, int var2, World var3);

   Integer getTemperatureModifier(int var1, int var2, World var3);

   Season getPermanentSeason(int var1, int var2, World var3);
}
