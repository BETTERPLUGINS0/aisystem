package me.casperge.realisticseasons.api.maps;

import me.casperge.realisticseasons.RealisticSeasons;
import org.bukkit.World;
import org.dynmap.DynmapAPI;

public class DMap implements MapPlugin {
   private DynmapAPI dynmapAPI;

   public DMap(RealisticSeasons var1) {
      this.dynmapAPI = (DynmapAPI)var1.getServer().getPluginManager().getPlugin("dynmap");
   }

   public void setFullRenderPause(boolean var1, World var2) {
      this.dynmapAPI.setPauseUpdateRenders(var1);
   }

   public boolean isFullRenderPause(World var1) {
      return this.dynmapAPI.getPauseUpdateRenders();
   }
}
