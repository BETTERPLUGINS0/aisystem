package com.bergerkiller.bukkit.tc.attachments.ui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapFont.Alignment;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.block.BlockFace;

public class MapWidgetSelectionBox extends MapWidget {
   private List<String> items = new ArrayList();
   private int selectedIndex = -1;
   private final MapWidgetArrow nav_left;
   private final MapWidgetArrow nav_right;
   private MapFont<Character> font;

   public MapWidgetSelectionBox() {
      this.nav_left = new MapWidgetArrow(BlockFace.WEST);
      this.nav_right = new MapWidgetArrow(BlockFace.EAST);
      this.font = MapFont.MINECRAFT;
      this.setFocusable(true);
   }

   public List<String> getItems() {
      return this.items;
   }

   public void clearItems() {
      this.items.clear();
      if (this.selectedIndex != -1) {
         this.selectedIndex = -1;
         this.invalidate();
      }

   }

   public MapWidgetSelectionBox setFont(MapFont<Character> font) {
      this.font = font;
      this.invalidate();
      return this;
   }

   public MapWidgetSelectionBox addItem(String item) {
      this.items.add(item);
      return this;
   }

   public MapWidgetSelectionBox removeItem(String item) {
      int index = this.items.indexOf(item);
      if (index != -1) {
         this.items.remove(index);
         if (index == this.selectedIndex) {
            if (this.selectedIndex >= this.items.size()) {
               --this.selectedIndex;
            }

            this.invalidate();
            this.onSelectedItemChanged();
         } else if (index < this.selectedIndex) {
            --this.selectedIndex;
         }
      }

      return this;
   }

   public int getSelectedIndex() {
      return this.selectedIndex;
   }

   public String getSelectedItem() {
      return this.selectedIndex >= 0 && this.selectedIndex < this.items.size() ? (String)this.items.get(this.selectedIndex) : null;
   }

   public void setSelectedItem(String item) {
      this.setSelectedIndex(this.items.indexOf(item));
   }

   public void setSelectedIndex(int new_index) {
      if (new_index != this.selectedIndex) {
         this.selectedIndex = new_index;
         this.invalidate();
         this.onSelectedItemChanged();
      }

   }

   public int getItemCount() {
      return this.items.size();
   }

   public void onAttached() {
      super.onAttached();
      this.nav_left.setVisible(false);
      this.nav_right.setVisible(false);
      this.nav_left.setClipParent(this.isClipParent());
      this.nav_right.setClipParent(this.isClipParent());
      this.addWidget(this.nav_left);
      this.addWidget(this.nav_right);
   }

   public void onDraw() {
      int offset = this.nav_left.getWidth() + 1;
      MapWidgetButton.fillBackground(this.view.getView(offset + 1, 1, this.getWidth() - 2 * offset - 2, this.getHeight() - 2), true, this.isFocused());
      this.view.drawRectangle(offset, 0, this.getWidth() - 2 * offset, this.getHeight(), (byte)(this.isFocused() ? 18 : 119));
      String selectedItem = this.getSelectedItem();
      if (selectedItem != null) {
         this.view.setAlignment(Alignment.MIDDLE);
         this.view.draw(this.font, this.getWidth() / 2, 2, (byte)34, selectedItem);
      }

   }

   public void onActivate() {
   }

   public void onBoundsChanged() {
      this.nav_left.setPosition(0, 0);
      this.nav_right.setPosition(this.getWidth() - this.nav_right.getWidth(), 0);
   }

   public void onKeyPressed(MapKeyEvent event) {
      if (event.getKey() == Key.LEFT) {
         this.nav_left.sendFocus();
         if (this.selectedIndex > 0) {
            --this.selectedIndex;
            this.invalidate();
            this.onSelectedItemChanged();
            if (this.display != null) {
               this.display.playSound(SoundEffect.CLICK);
            }
         }
      } else if (event.getKey() == Key.RIGHT) {
         this.nav_right.sendFocus();
         if (this.selectedIndex < this.items.size() - 1) {
            ++this.selectedIndex;
            this.invalidate();
            this.onSelectedItemChanged();
            if (this.display != null) {
               this.display.playSound(SoundEffect.CLICK);
            }
         }
      } else {
         super.onKeyPressed(event);
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

   public void onFocus() {
      this.nav_left.setVisible(true);
      this.nav_right.setVisible(true);
   }

   public void onBlur() {
      this.nav_left.setVisible(false);
      this.nav_right.setVisible(false);
   }

   public void onSelectedItemChanged() {
   }
}
