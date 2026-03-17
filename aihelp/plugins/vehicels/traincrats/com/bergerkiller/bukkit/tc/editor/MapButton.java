package com.bergerkiller.bukkit.tc.editor;

import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.MapDisplay.Layer;

public class MapButton {
   private final Layer layer;
   private final int x;
   private final int y;

   public MapButton(MapDisplay display, int x, int y, int z) {
      this.layer = display.getLayer(z);
      this.x = x;
      this.y = y;
   }
}
