package me.casperge.realisticseasons.api.maps;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.BlueMapWorld;
import java.util.Iterator;
import java.util.Optional;
import me.casperge.realisticseasons.RealisticSeasons;
import org.bukkit.World;

public class BMap implements MapPlugin {
   RealisticSeasons main;
   boolean enabled = false;
   BlueMapAPI api;

   public BMap(RealisticSeasons var1) {
      this.main = var1;
      BlueMapAPI.onEnable((var1x) -> {
         this.enabled = true;
         this.api = var1x;
      });
      BlueMapAPI.onDisable((var1x) -> {
         this.enabled = false;
         this.api = null;
      });
   }

   public void setFullRenderPause(boolean var1, World var2) {
      if (this.enabled) {
         Optional var3 = this.api.getWorld(var2);
         if (var3.isPresent()) {
            BlueMapWorld var4 = (BlueMapWorld)var3.get();
            Iterator var5 = var4.getMaps().iterator();

            while(var5.hasNext()) {
               BlueMapMap var6 = (BlueMapMap)var5.next();
               var6.setFrozen(var1);
            }
         }
      }

   }

   public boolean isFullRenderPause(World var1) {
      if (!this.enabled) {
         return false;
      } else {
         boolean var2 = false;
         Optional var3 = this.api.getWorld(var1);
         if (var3.isPresent()) {
            BlueMapWorld var4 = (BlueMapWorld)var3.get();
            Iterator var5 = var4.getMaps().iterator();

            while(var5.hasNext()) {
               BlueMapMap var6 = (BlueMapMap)var5.next();
               if (var6.isFrozen()) {
                  var2 = true;
               }
            }
         }

         return var2;
      }
   }
}
