package com.bergerkiller.bukkit.tc.attachments.ui.animation;

import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationNode;
import org.bukkit.util.Vector;

public class MapWidgetAnimationNode extends MapWidget {
   private AnimationNode _node = null;
   private double _maxPosition = 1.0D;
   private boolean _selected = false;
   private boolean _multiSelectRoot = false;

   public MapWidgetAnimationNode() {
      this.setSize(100, 5);
   }

   public MapWidgetAnimationNode setMaximumPosition(double maximum) {
      if (this._maxPosition != maximum) {
         this._maxPosition = maximum;
         this.invalidate();
      }

      return this;
   }

   public MapWidgetAnimationNode setValue(AnimationNode node) {
      this._node = node;
      this.invalidate();
      return this;
   }

   public AnimationNode getValue() {
      return this._node;
   }

   public void setSelected(boolean selected) {
      if (this._selected != selected) {
         this._selected = selected;
         this.invalidate();
      }

   }

   public boolean isSelected() {
      return this._selected;
   }

   public void setIsMultiSelectRoot(boolean root) {
      if (this._multiSelectRoot != root) {
         this._multiSelectRoot = root;
         this.invalidate();
      }

   }

   public void onDraw() {
      MapWidgetAnimationNode.Column[] columns = calculateColumns(this.getWidth());
      byte top_color;
      byte mid_color;
      byte btm_color;
      if (this._multiSelectRoot && this.isSelected()) {
         top_color = MapColorPalette.getColor(219, 145, 92);
         mid_color = MapColorPalette.getColor(188, 124, 79);
         btm_color = MapColorPalette.getColor(154, 101, 64);
      } else if (this._node != null && this.isSelected() && !this._node.hasSceneMarker()) {
         top_color = MapColorPalette.getColor(213, 219, 92);
         mid_color = MapColorPalette.getColor(183, 188, 79);
         btm_color = MapColorPalette.getColor(150, 154, 64);
      } else if (this._node != null && this.isSelected() && this._node.hasSceneMarker()) {
         top_color = MapColorPalette.getColor(216, 76, 178);
         mid_color = MapColorPalette.getColor(186, 65, 153);
         btm_color = MapColorPalette.getColor(178, 63, 127);
      } else if (this._node != null && this._node.hasSceneMarker()) {
         top_color = MapColorPalette.getColor(97, 63, 148);
         mid_color = MapColorPalette.getColor(83, 54, 127);
         btm_color = MapColorPalette.getColor(68, 44, 104);
      } else {
         top_color = MapColorPalette.getColor(51, 127, 216);
         mid_color = MapColorPalette.getColor(44, 109, 186);
         btm_color = MapColorPalette.getColor(36, 82, 159);
      }

      this.view.drawLine(0, 0, this.getWidth() - 1, 0, top_color);
      this.view.fillRectangle(0, 1, this.getWidth(), this.getHeight() - 2, mid_color);
      this.view.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, btm_color);

      for(int y = 1; y < this.getHeight(); y += 2) {
         this.view.drawPixel(columns[1].x - 1, y, (byte)119);
         this.view.drawPixel(columns[2].x - 1, y, (byte)119);
      }

      if (this._node != null) {
         double time = this._node.getDuration();
         Vector pos = this._node.getPosition();
         Vector rot = this._node.getRotationVector();
         String timeStr = Util.stringifyAnimationNodeTime(time);
         byte light_green_color = MapColorPalette.getColor(56, 178, 127);
         byte dt_color = this._node.hasSceneMarker() ? MapColorPalette.getColor(133, 180, 20) : light_green_color;
         int drawTimeOffset = 1;
         int numDigits = 0;

         for(int ch_idx = 0; ch_idx < timeStr.length(); ++ch_idx) {
            char c = timeStr.charAt(ch_idx);
            if (c != '.' && c != ',') {
               ++numDigits;
               MapTexture sprite = MapFont.TINY.getSprite(c);
               this.view.draw(sprite, drawTimeOffset, 0, dt_color);
               drawTimeOffset += sprite.getWidth();
               if (numDigits == 4) {
                  break;
               }
            } else if (numDigits <= 3) {
               this.view.drawPixel(drawTimeOffset, 4, dt_color);
               drawTimeOffset += 2;
            }
         }

         byte color_x;
         byte color_y;
         byte color_z;
         if (this._node.isActive()) {
            color_x = 18;
            color_y = light_green_color;
            color_z = 50;
         } else {
            color_x = MapColorPalette.getColor(199, 199, 199);
            color_y = MapColorPalette.getColor(180, 180, 180);
            color_z = MapColorPalette.getColor(158, 144, 141);
         }

         this.view.drawLine(columns[1].mid, 1, columns[1].getPos(pos.getX(), this._maxPosition), 1, color_x);
         this.view.drawLine(columns[1].mid, 2, columns[1].getPos(pos.getY(), this._maxPosition), 2, color_y);
         this.view.drawLine(columns[1].mid, 3, columns[1].getPos(pos.getZ(), this._maxPosition), 3, color_z);
         this.view.drawLine(columns[2].mid, 1, columns[2].getRot(rot.getX()), 1, color_x);
         this.view.drawLine(columns[2].mid, 2, columns[2].getRot(rot.getY()), 2, color_y);
         this.view.drawLine(columns[2].mid, 3, columns[2].getRot(rot.getZ()), 3, color_z);
      }

   }

   protected static MapWidgetAnimationNode.Column[] calculateColumns(int width) {
      width -= 2;
      int time_width = 20;
      width -= time_width;

      while(width > 0) {
         if ((width & 1) == 1) {
            --width;
            ++time_width;
         } else {
            if ((width >> 1 & 1) == 1) {
               break;
            }

            --width;
            ++time_width;
         }
      }

      return new MapWidgetAnimationNode.Column[]{new MapWidgetAnimationNode.Column(0, time_width), new MapWidgetAnimationNode.Column(time_width + 1, width >> 1), new MapWidgetAnimationNode.Column(time_width + (width >> 1) + 2, width >> 1)};
   }

   protected static final class Column {
      public final int x;
      public final int width;
      public final int mid;

      public Column(int x, int width) {
         this.x = x;
         this.width = width;
         this.mid = x + (width - 1 >> 1);
      }

      public int getPos(double value, double maximum) {
         int pixels = this.mid;
         if (value != 0.0D && maximum > 0.0D) {
            pixels += (int)(value * (double)(this.width >> 1) / maximum);
         }

         return pixels;
      }

      public int getRot(double angle) {
         while(angle > 180.0D) {
            angle -= 360.0D;
         }

         while(angle < -180.0D) {
            angle += 360.0D;
         }

         return this.getPos(angle, 180.0D);
      }
   }
}
