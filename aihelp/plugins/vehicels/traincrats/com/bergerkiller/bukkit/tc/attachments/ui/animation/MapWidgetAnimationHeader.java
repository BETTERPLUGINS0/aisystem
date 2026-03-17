package com.bergerkiller.bukkit.tc.attachments.ui.animation;

import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.tc.TrainCarts;

public class MapWidgetAnimationHeader extends MapWidget {
   public MapWidgetAnimationHeader() {
      this.setSize(100, 5);
   }

   public void onDraw() {
      MapWidgetAnimationNode.Column[] columns = MapWidgetAnimationNode.calculateColumns(this.getWidth());
      byte top_color = MapColorPalette.getColor(163, 233, 247);
      byte mid_color = MapColorPalette.getColor(140, 201, 213);
      byte btm_color = MapColorPalette.getColor(115, 164, 174);
      this.view.drawLine(0, 0, this.getWidth() - 1, 0, top_color);
      this.view.fillRectangle(0, 1, this.getWidth(), this.getHeight() - 2, mid_color);
      this.view.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, btm_color);
      this.view.drawLine(columns[1].x - 1, 0, columns[1].x - 1, this.getHeight() - 1, (byte)119);
      this.view.drawLine(columns[2].x - 1, 0, columns[2].x - 1, this.getHeight() - 1, (byte)119);
      MapTexture labelTex = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/anim_header.png");
      int labelDX = labelTex.getWidth() >> 1;
      int labelHeight = labelTex.getHeight() / 3;
      int top_y = this.getHeight() - labelHeight >> 1;

      for(int i = 0; i < columns.length; ++i) {
         this.view.draw(labelTex.getView(0, i * labelHeight, labelTex.getWidth(), labelHeight), columns[i].mid - labelDX, top_y);
      }

   }
}
