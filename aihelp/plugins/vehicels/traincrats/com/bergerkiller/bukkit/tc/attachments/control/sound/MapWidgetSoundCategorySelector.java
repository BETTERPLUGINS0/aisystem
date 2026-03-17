package com.bergerkiller.bukkit.tc.attachments.control.sound;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapBlendMode;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetArrow;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.block.BlockFace;

abstract class MapWidgetSoundCategorySelector extends MapWidgetSoundElement {
   private MapWidgetSoundCategorySelector.SoundCategory category;
   private final MapWidgetArrow upArrow;
   private final MapWidgetArrow downArrow;
   private final MapWidgetSoundCategorySelector.ToolTipWidget tooltip;

   public MapWidgetSoundCategorySelector() {
      this.category = MapWidgetSoundCategorySelector.SoundCategory.MASTER;
      this.upArrow = new MapWidgetArrow(BlockFace.SOUTH);
      this.downArrow = new MapWidgetArrow(BlockFace.NORTH);
      this.tooltip = new MapWidgetSoundCategorySelector.ToolTipWidget();
      this.setSize(11, 11);
      this.tooltip.setText(this.category.getId());
   }

   public abstract void onCategoryChanged(String var1);

   public MapWidgetSoundCategorySelector setCategory(String categoryName) {
      return this.setCategory(MapWidgetSoundCategorySelector.SoundCategory.byId(categoryName));
   }

   private MapWidgetSoundCategorySelector setCategory(MapWidgetSoundCategorySelector.SoundCategory newCategory) {
      if (this.category != newCategory) {
         this.category = newCategory;
         this.tooltip.setText(newCategory.getId());
         this.updateArrowsEnabled();
         this.invalidate();
      }

      return this;
   }

   public String getCategory() {
      return this.category.getId();
   }

   public void onDraw() {
      super.onDraw();
      this.view.draw(this.category.getIcon(this.isFocused()), 0, 0);
   }

   public void onActivate() {
      super.onActivate();
      this.removeWidget(this.upArrow);
      this.removeWidget(this.downArrow);
      this.updateArrowsEnabled();
      this.addWidget(this.upArrow.setPosition(0, -this.upArrow.getHeight() - 1));
      this.addWidget(this.downArrow.setPosition(0, this.getHeight() + 1));
   }

   public void onDeactivate() {
      super.onDeactivate();
      this.removeWidget(this.upArrow);
      this.removeWidget(this.downArrow);
      if (!this.isFocused()) {
         this.removeWidget(this.tooltip);
      }

   }

   public void onFocus() {
      this.removeWidget(this.tooltip);
      this.tooltip.setPosition(-this.tooltip.getWidth() - 1, 1);
      this.addWidget(this.tooltip);
   }

   public void onBlur() {
      if (!this.isActivated()) {
         this.removeWidget(this.tooltip);
      }

   }

   public void onKeyPressed(MapKeyEvent event) {
      if (this.isActivated()) {
         if (event.getKey() == Key.UP) {
            this.upArrow.sendFocus();
            if (this.category.hasPrev()) {
               this.setCategory(this.category.getPrev());
               this.onCategoryChanged(this.getCategory());
            }

            return;
         }

         if (event.getKey() == Key.DOWN) {
            this.downArrow.sendFocus();
            if (this.category.hasNext()) {
               this.setCategory(this.category.getNext());
               this.onCategoryChanged(this.getCategory());
            }

            return;
         }

         if (event.getKey() == Key.ENTER) {
            this.deactivate();
            return;
         }

         if (event.getKey() == Key.LEFT || event.getKey() == Key.RIGHT) {
            this.focus();
         }
      }

      super.onKeyPressed(event);
   }

   public void onKeyReleased(MapKeyEvent event) {
      if (this.isActivated()) {
         if (event.getKey() == Key.UP) {
            this.upArrow.stopFocus();
         } else if (event.getKey() == Key.DOWN) {
            this.downArrow.stopFocus();
         } else {
            super.onKeyReleased(event);
         }
      } else {
         super.onKeyReleased(event);
      }

   }

   private void updateArrowsEnabled() {
      this.upArrow.setEnabled(this.category.hasPrev());
      this.downArrow.setEnabled(this.category.hasNext());
   }

   private static enum SoundCategory {
      MASTER("master"),
      MUSIC("music"),
      RECORD("record"),
      WEATHER("weather"),
      BLOCK("block"),
      HOSTILE("hostile"),
      NEUTRAL("neutral"),
      PLAYER("player"),
      AMBIENT("ambient"),
      VOICE("voice");

      private final String id;
      private final MapTexture icon;
      private final MapTexture icon_focused;
      private MapWidgetSoundCategorySelector.SoundCategory prev;
      private MapWidgetSoundCategorySelector.SoundCategory next;
      private static final Map<String, MapWidgetSoundCategorySelector.SoundCategory> byId = new HashMap();

      public static MapWidgetSoundCategorySelector.SoundCategory byId(String id) {
         return (MapWidgetSoundCategorySelector.SoundCategory)byId.getOrDefault(id.toLowerCase(Locale.ENGLISH), MASTER);
      }

      private SoundCategory(String id) {
         this.id = id;
         this.icon = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/sound_categories.png").getView(this.ordinal() * 11, 0, 11, 11).clone();
         this.icon_focused = this.icon.clone();
         this.icon_focused.setBlendMode(MapBlendMode.ADD);
         this.icon_focused.fill(MapColorPalette.getColor(80, 80, 0));
         this.icon_focused.setBlendMode(MapBlendMode.NONE);
      }

      public String getId() {
         return this.id;
      }

      public MapTexture getIcon(boolean focused) {
         return focused ? this.icon_focused : this.icon;
      }

      public boolean hasPrev() {
         return this.prev != null;
      }

      public MapWidgetSoundCategorySelector.SoundCategory getPrev() {
         return this.prev;
      }

      public boolean hasNext() {
         return this.next != null;
      }

      public MapWidgetSoundCategorySelector.SoundCategory getNext() {
         return this.next;
      }

      // $FF: synthetic method
      private static MapWidgetSoundCategorySelector.SoundCategory[] $values() {
         return new MapWidgetSoundCategorySelector.SoundCategory[]{MASTER, MUSIC, RECORD, WEATHER, BLOCK, HOSTILE, NEUTRAL, PLAYER, AMBIENT, VOICE};
      }

      static {
         MapWidgetSoundCategorySelector.SoundCategory prev = null;
         MapWidgetSoundCategorySelector.SoundCategory[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            MapWidgetSoundCategorySelector.SoundCategory cat = var1[var3];
            byId.put(cat.getId(), cat);
            cat.prev = prev;
            if (prev != null) {
               prev.next = cat;
            }

            prev = cat;
         }

      }
   }

   private static class ToolTipWidget extends MapWidget {
      private String text = "";

      public ToolTipWidget() {
         this.setDepthOffset(2);
      }

      public MapWidgetSoundCategorySelector.ToolTipWidget setText(String text) {
         if (!this.text.equals(text)) {
            this.text = text;
            if (this.display != null) {
               this.calcSize();
            }

            this.invalidate();
         }

         return this;
      }

      public void onAttached() {
         this.calcSize();
      }

      public void onDraw() {
         this.view.fill((byte)119);
         this.view.draw(MapFont.MINECRAFT, 2, 1, (byte)34, this.text);
      }

      private void calcSize() {
         Dimension dim = this.view.calcFontSize(MapFont.MINECRAFT, this.text);
         int tw = (int)dim.getWidth() + 2;
         int th = (int)dim.getHeight() + 1;
         this.setBounds(this.getX() + this.getWidth() - tw, this.getY(), tw, th);
      }
   }
}
