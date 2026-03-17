package com.bergerkiller.bukkit.tc.attachments.ui.block;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.attachments.ui.ItemDropTarget;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class MapWidgetBlockGrid extends MapWidget implements ItemDropTarget, BlockDataSelector {
   private int _columns = 4;
   private int _rows = 3;
   private int _itemSize = 16;
   private BlockDataTextureCache _textures = BlockDataTextureCache.get(16, 16);
   private int _itemSpacing = 1;
   private int _scrollOffset = 0;
   private int _selectedIndex = 0;
   private final List<Material> _blocks = new ArrayList();

   public MapWidgetBlockGrid() {
      this.setFocusable(true);
      this.calcSize();
      this.addAllBlocks();
   }

   public Material getSelectedBlock() {
      return this._selectedIndex >= 0 && this._selectedIndex < this._blocks.size() ? (Material)this._blocks.get(this._selectedIndex) : null;
   }

   public BlockData getSelectedBlockData() {
      Material block = this.getSelectedBlock();
      return block == null ? null : BlockData.fromMaterial(block);
   }

   public MapWidgetBlockGrid setSelectedBlock(Material block) {
      int newIndex = block == null ? -1 : this._blocks.indexOf(block);
      if (this._selectedIndex != newIndex) {
         this._selectedIndex = newIndex;
      }

      this.scrollToSelection();
      this.invalidate();
      return this;
   }

   public MapWidgetBlockGrid setSelectedBlockData(BlockData blockData) {
      return this.setSelectedBlock(blockData == null ? null : blockData.getType());
   }

   public MapWidgetBlockGrid setDimensions(int columns, int rows) {
      this._columns = columns;
      this._rows = rows;
      this.calcSize();
      this.invalidate();
      return this;
   }

   public MapWidgetBlockGrid setItemSize(int itemSize) {
      this._itemSize = itemSize;
      this._textures = BlockDataTextureCache.get(itemSize, itemSize);
      this.calcSize();
      this.invalidate();
      return this;
   }

   public MapWidgetBlockGrid setItemSpacing(int itemSpacing) {
      this._itemSpacing = itemSpacing;
      this.calcSize();
      this.invalidate();
      return this;
   }

   public MapWidgetBlockGrid clearAllBlocks() {
      this._blocks.clear();
      this._selectedIndex = -1;
      this.invalidate();
      return this;
   }

   public MapWidgetBlockGrid addAllBlocks() {
      Material[] var1 = MaterialUtil.getAllBlocks();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Material block = var1[var3];
         if (block != Material.AIR) {
            this.addBlock(block);
         }
      }

      return this;
   }

   public MapWidgetBlockGrid addBlock(Material block) {
      if (block != null) {
         this._blocks.add(block);
         this.invalidate();
      }

      return this;
   }

   public void onDraw() {
      boolean activated = this.isActivated();
      int index = this._scrollOffset * this._columns;

      int x;
      int y;
      for(int row = 0; row < this._rows; ++row) {
         for(int col = 0; col < this._columns; ++col) {
            x = this.calcX(col);
            y = this.calcY(row);
            if (index >= 0 && index < this._blocks.size()) {
               this.view.draw(this._textures.get(BlockData.fromMaterial((Material)this._blocks.get(index))), x, y);
               if (this._selectedIndex == index) {
                  if (activated) {
                     this.view.drawRectangle(x, y, this._itemSize, this._itemSize, (byte)18);
                  } else {
                     this.view.drawRectangle(x, y, this._itemSize, this._itemSize, MapColorPalette.getColor(128, 128, 128));
                  }
               }
            }

            ++index;
         }
      }

      if (activated && this._selectedIndex >= 0 && this._selectedIndex < this._blocks.size()) {
         Material block = (Material)this._blocks.get(this._selectedIndex);
         String label = BlockData.fromMaterial(block).getBlockName();
         x = this._selectedIndex % this._columns;
         y = this._selectedIndex / this._columns - this._scrollOffset;
         int spaceRows = Math.max(y, this._rows - y - 1);
         if (spaceRows > 0) {
            int maxLabelWidth = this.getWidth();
            Dimension labelSize = this.view.calcFontSize(MapFont.MINECRAFT, label);
            if (labelSize.getWidth() > (double)maxLabelWidth) {
            }

            int labelX = this.calcX(x) + (this._itemSize - labelSize.width) / 2;
            labelX = MathUtil.clamp(labelX, 0, this.getWidth() - labelSize.width);
            int labelY = this.calcY(y);
            if ((double)(this.getHeight() - (labelY + this._itemSize)) >= labelSize.getHeight()) {
               labelY += this._itemSize;
            } else {
               labelY -= labelSize.height;
               if (labelY < 0) {
                  labelY = 0;
               }
            }

            this.view.fillRectangle(labelX, labelY, labelSize.width, labelSize.height, (byte)119);
            this.view.draw(MapFont.MINECRAFT, labelX, labelY, (byte)34, label);
         }
      }

      if (this.isFocused()) {
         this.view.drawRectangle(0, 0, this.getWidth(), this.getHeight(), (byte)18);
      }

   }

   public void onKeyPressed(MapKeyEvent event) {
      if (!this.isActivated()) {
         super.onKeyPressed(event);
      } else {
         int selCol = this._selectedIndex % this._columns;
         int selRow = this._selectedIndex / this._columns;
         if (event.getKey() == Key.LEFT) {
            this.setSelectedCell(selCol - 1, selRow);
         } else if (event.getKey() == Key.RIGHT) {
            this.setSelectedCell(selCol + 1, selRow);
         } else if (event.getKey() == Key.UP) {
            this.setSelectedCell(selCol, selRow - 1);
         } else if (event.getKey() == Key.DOWN) {
            this.setSelectedCell(selCol, selRow + 1);
         } else {
            super.onKeyPressed(event);
         }

      }
   }

   public boolean acceptItem(ItemStack item) {
      if (this.parent instanceof ItemDropTarget) {
         return ((ItemDropTarget)this.parent).acceptItem(item);
      } else {
         if (item.getType().isBlock()) {
            int newIndex = this._blocks.indexOf(item.getType());
            if (newIndex != -1) {
               if (this._selectedIndex != newIndex) {
                  this._selectedIndex = newIndex;
                  this.onSelectedBlockDataChanged(this.getSelectedBlockData());
               }

               this.scrollToSelection();
               this.invalidate();
               return true;
            }
         }

         return false;
      }
   }

   private int calcX(int col) {
      return col == 0 ? 0 : col * this._itemSize + (col - 1) * this._itemSpacing;
   }

   private int calcY(int row) {
      return row == 0 ? 0 : row * this._itemSize + (row - 1) * this._itemSpacing;
   }

   private void setSelectedCell(int col, int row) {
      while(col >= this._columns) {
         col -= this._columns;
         ++row;
      }

      while(col < 0) {
         col += this._columns;
         --row;
      }

      int maxRowIndex = this._blocks.size() / this._columns - 1;
      if (row < 0) {
         row = 0;
      } else if (row > maxRowIndex) {
         row = maxRowIndex;
      }

      int newIndex = row * this._columns + col;
      if (newIndex < 0) {
         newIndex = 0;
      } else if (newIndex >= this._blocks.size()) {
         newIndex = this._blocks.size() - 1;
      }

      if (this._selectedIndex != newIndex) {
         this._selectedIndex = newIndex;
         this.onSelectedBlockDataChanged(this.getSelectedBlockData());
      }

      this.scrollToSelection();
      this.invalidate();
   }

   private void scrollToSelection() {
      if (this._selectedIndex != -1) {
         int selRow = this._selectedIndex / this._columns;
         int selRowRelative = selRow - this._scrollOffset;
         if (selRowRelative < 0) {
            this._scrollOffset = selRow;
         } else if (selRowRelative >= this._rows) {
            this._scrollOffset = selRow - this._rows + 1;
         }

      }
   }

   private void calcSize() {
      this.setSize(this._columns * this._itemSize + (this._columns - 1) * this._itemSpacing, this._rows * this._itemSize + (this._rows - 1) * this._itemSpacing);
   }
}
