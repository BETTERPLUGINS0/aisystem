package com.bergerkiller.bukkit.tc.attachments.ui.block;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.common.wrappers.BlockState;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetArrow;
import com.bergerkiller.bukkit.tc.attachments.ui.SetValueTarget;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public abstract class MapWidgetBlockDataVariantList extends MapWidget implements SetValueTarget, BlockDataSelector {
   private final MapWidgetArrow nav_left;
   private final MapWidgetArrow nav_right;
   private final MapTexture background;
   private List<BlockData> variants;
   private final BlockDataTextureCache iconCache;
   private int variantIndex;

   public MapWidgetBlockDataVariantList() {
      this.nav_left = new MapWidgetArrow(BlockFace.WEST);
      this.nav_right = new MapWidgetArrow(BlockFace.EAST);
      this.iconCache = BlockDataTextureCache.get(16, 16);
      this.variantIndex = 0;
      this.background = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/item_selector_bg.png");
      this.setSize(100, 18);
      this.setFocusable(true);
      this.variants = new ArrayList(0);
      this.nav_left.setPosition(0, 4);
      this.nav_right.setPosition(this.getWidth() - this.nav_right.getWidth(), 4);
      this.nav_left.setVisible(false);
      this.nav_right.setVisible(false);
      this.addWidget(this.nav_left);
      this.addWidget(this.nav_right);
      this.setRetainChildWidgets(true);
   }

   public BlockData getSelectedBlockData() {
      return this.variantIndex >= 0 && this.variantIndex < this.variants.size() ? (BlockData)this.variants.get(this.variantIndex) : null;
   }

   public MapWidgetBlockDataVariantList setSelectedBlockData(BlockData blockData) {
      this.setSelectedBlockData(blockData, false);
      return this;
   }

   private void setSelectedBlockData(BlockData blockData, boolean fireEvent) {
      if (this.getSelectedBlockData() != blockData) {
         if (blockData == null) {
            this.variants = new ArrayList(0);
            this.variantIndex = 0;
            this.invalidate();
            if (fireEvent) {
               this.onSelectedBlockDataChanged((BlockData)null);
            }

         } else {
            this.variants.clear();
            this.variants.add(blockData);
            Iterator var3 = blockData.getStates().keySet().iterator();

            while(var3.hasNext()) {
               BlockState<?> state = (BlockState)var3.next();
               List<BlockData> tmp = new ArrayList(this.variants);
               this.variants.clear();
               Iterator var6 = state.values().iterator();

               while(var6.hasNext()) {
                  Comparable<?> value = (Comparable)var6.next();
                  Iterator var8 = tmp.iterator();

                  while(var8.hasNext()) {
                     BlockData original = (BlockData)var8.next();

                     try {
                        this.variants.add(original.setState(state, value));
                     } catch (Throwable var11) {
                     }
                  }
               }
            }

            this.variantIndex = 0;

            for(int i = 0; i < this.variants.size(); ++i) {
               BlockData variant = (BlockData)this.variants.get(i);
               if (variant.equals(blockData)) {
                  this.variantIndex = i;
                  break;
               }
            }

            this.invalidate();
            if (fireEvent) {
               this.onSelectedBlockDataChanged(blockData);
            }

         }
      }
   }

   public String getAcceptedPropertyName() {
      return "Block Information";
   }

   public boolean acceptTextValue(String value) {
      value = value.trim();

      int nameEnd;
      for(nameEnd = 0; nameEnd < value.length() && value.charAt(nameEnd) != '{' && value.charAt(nameEnd) != ' '; ++nameEnd) {
      }

      String itemName = value.substring(0, nameEnd);
      if (nameEnd >= value.length()) {
         value = "";
      } else {
         value = value.substring(nameEnd).trim();
      }

      if (!ParseUtil.isNumeric(itemName)) {
         Material newItemMaterial = ParseUtil.parseMaterial(itemName, (Material)null);
         if (newItemMaterial == null) {
            return false;
         }

         BlockData newBlock = BlockData.fromMaterial(newItemMaterial);
         this.setSelectedBlockData(newBlock, true);
      } else {
         try {
            this.setVariantIndex(Integer.parseInt(itemName));
         } catch (NumberFormatException var6) {
            return false;
         }
      }

      return true;
   }

   public void onFocus() {
      this.nav_left.setVisible(true);
      this.nav_right.setVisible(true);
   }

   public void onBlur() {
      this.nav_left.setVisible(false);
      this.nav_right.setVisible(false);
   }

   public void onDraw() {
      int selector_edge = this.nav_left.getWidth() + 1;
      MapCanvas itemView = this.view.getView(selector_edge, 0, this.getWidth() - 2 * selector_edge, this.getHeight());
      itemView.draw(this.background, 0, 0);
      int x = 1;
      int y = 1;

      for(int index = this.variantIndex - 2; index <= this.variantIndex + 2; ++index) {
         if (index >= 0 && index < this.variants.size()) {
            itemView.draw(this.iconCache.get((BlockData)this.variants.get(index)), x, y);
         }

         x += 17;
      }

      if (this.isFocused()) {
         int fx = 35;
         int fy = 1;
         itemView.drawRectangle(fx, fy, 16, 16, (byte)18);
      }

   }

   private void changeVariantIndex(int offset) {
      this.setVariantIndex(this.variantIndex + offset);
   }

   private void setVariantIndex(int newVariantIndex) {
      if (newVariantIndex < 0) {
         newVariantIndex = 0;
      } else if (newVariantIndex >= this.variants.size()) {
         newVariantIndex = this.variants.size() - 1;
      }

      if (this.variantIndex != newVariantIndex) {
         this.variantIndex = newVariantIndex;
         this.invalidate();
         this.onSelectedBlockDataChanged(this.getSelectedBlockData());
         this.display.playSound(SoundEffect.CLICK);
      }
   }

   public void onKeyReleased(MapKeyEvent event) {
      super.onKeyReleased(event);
      if (event.getKey() == Key.LEFT) {
         this.nav_left.stopFocus();
      } else if (event.getKey() == Key.RIGHT) {
         this.nav_right.stopFocus();
      }

   }

   public void onKeyPressed(MapKeyEvent event) {
      if (event.getKey() == Key.LEFT) {
         this.changeVariantIndex(-1 - event.getRepeat() / 40);
         this.nav_left.sendFocus();
      } else if (event.getKey() == Key.RIGHT) {
         this.changeVariantIndex(1 + event.getRepeat() / 40);
         this.nav_right.sendFocus();
      } else {
         super.onKeyPressed(event);
      }

   }
}
