package me.casperge.realisticseasons.api.maps;

import org.bukkit.World;

public interface MapPlugin {
   void setFullRenderPause(boolean var1, World var2);

   boolean isFullRenderPause(World var1);
}
