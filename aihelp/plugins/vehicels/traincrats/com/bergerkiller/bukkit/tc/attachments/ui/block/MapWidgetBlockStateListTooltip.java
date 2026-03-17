package com.bergerkiller.bukkit.tc.attachments.ui.block;

import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.common.wrappers.BlockState;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.Map.Entry;

public class MapWidgetBlockStateListTooltip extends MapWidget implements BlockDataSelector {
   private static final int ROW_HEIGHT = 8;
   private static final int NAME_STATE_GAP = 2;
   private BlockData block = null;

   public MapWidgetBlockStateListTooltip() {
      this.setFocusable(false);
      this.setDepthOffset(2);
   }

   public void onSelectedBlockDataChanged(BlockData blockData) {
   }

   public BlockData getSelectedBlockData() {
      return this.block;
   }

   public MapWidgetBlockStateListTooltip setSelectedBlockData(BlockData blockData) {
      if (this.block == blockData) {
         return this;
      } else {
         this.block = blockData;
         this.updateBounds();
         this.invalidate();
         return this;
      }
   }

   public void onAttached() {
      this.updateBounds();
   }

   public void onDraw() {
      if (this.block != null) {
         int y = 0;
         this.drawText(y, this.block.getBlockName());
         int y = y + 10;

         for(Iterator var2 = this.block.getStates().entrySet().iterator(); var2.hasNext(); y += 8) {
            Entry<? extends BlockState<?>, Comparable<?>> entry = (Entry)var2.next();
            String text = ((BlockState)entry.getKey()).name() + " = " + ((BlockState)entry.getKey()).valueName((Comparable)entry.getValue());
            this.drawText(y, text);
         }

      }
   }

   private void drawText(int y, String text) {
      Dimension size = this.view.calcFontSize(MapFont.MINECRAFT, text);
      int x = (this.getWidth() - size.width) / 2;
      this.view.fillRectangle(x, y, size.width + 1, size.height, (byte)119);
      this.view.draw(MapFont.MINECRAFT, x + 1, y, (byte)34, text);
      y += 8;
   }

   private void updateBounds() {
      if (this.getParent() != null) {
         int x = (this.getParent().getWidth() - this.getWidth()) / 2;
         int y = this.getY();
         if (this.block != null) {
            this.setBounds(x, y, this.getWidth(), 2 + (1 + this.block.getStates().size()) * 8);
         } else {
            this.setBounds(x, y, this.getWidth(), 0);
         }

      }
   }
}
