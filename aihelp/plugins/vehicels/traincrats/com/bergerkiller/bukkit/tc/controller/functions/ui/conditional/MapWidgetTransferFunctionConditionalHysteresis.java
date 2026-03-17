package com.bergerkiller.bukkit.tc.controller.functions.ui.conditional;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetArrow;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;
import com.bergerkiller.bukkit.tc.attachments.ui.SetValueTarget;
import java.text.NumberFormat;
import org.bukkit.block.BlockFace;

public abstract class MapWidgetTransferFunctionConditionalHysteresis extends MapWidget implements SetValueTarget {
   private static final byte COLOR_BG_DEFAULT = MapColorPalette.getColor(199, 199, 199);
   private static final byte COLOR_BG_FOCUSED = MapColorPalette.getColor(255, 252, 245);
   private static final byte COLOR_BG_ACTIVATED = MapColorPalette.getColor(247, 233, 163);
   private static final NumberFormat NUMBER_FORMAT = Util.createNumberFormat(1, 4);
   private static final MapTexture HYSTERESIS_ICON;
   private final MapWidgetArrow leftArrow;
   private final MapWidgetArrow rightArrow;
   private double hysteresis;

   public MapWidgetTransferFunctionConditionalHysteresis(double hysteresis) {
      this.leftArrow = new MapWidgetArrow(BlockFace.WEST);
      this.rightArrow = new MapWidgetArrow(BlockFace.EAST);
      this.hysteresis = hysteresis;
      this.setFocusable(true);
   }

   public abstract void onHysteresisChanged(double var1);

   public void setHysteresis(double hysteresis) {
      if (this.hysteresis != hysteresis) {
         this.hysteresis = hysteresis;
         this.invalidate();
         this.onHysteresisChanged(hysteresis);
      }

   }

   private void increment(double incr, int repeat) {
      this.setHysteresis(MapWidgetNumberBox.scaledIncrease(this.hysteresis, incr, repeat));
   }

   public String getAcceptedPropertyName() {
      return "Hysteresis";
   }

   public boolean acceptTextValue(String value) {
      return this.acceptTextValue(SetValueTarget.Operation.SET, value);
   }

   public boolean acceptTextValue(SetValueTarget.Operation operation, String value) {
      return operation.perform(() -> {
         return this.hysteresis;
      }, this::setHysteresis, value);
   }

   public void onActivate() {
      this.addWidget(this.leftArrow.setPosition(-this.leftArrow.getWidth() - 1, (this.getHeight() - this.leftArrow.getHeight()) / 2));
      this.addWidget(this.rightArrow.setPosition(this.getWidth() + 1, (this.getHeight() - this.rightArrow.getHeight()) / 2));
      super.onActivate();
   }

   public void onDeactivate() {
      this.removeWidget(this.leftArrow);
      this.removeWidget(this.rightArrow);
      super.onDeactivate();
   }

   public void onKeyPressed(MapKeyEvent event) {
      if (!this.isActivated()) {
         super.onKeyPressed(event);
      } else {
         if (event.getKey() == Key.LEFT) {
            this.increment(-0.001D, event.getRepeat());
            this.leftArrow.sendFocus();
            this.rightArrow.stopFocus();
         } else if (event.getKey() == Key.RIGHT) {
            this.increment(0.001D, event.getRepeat());
            this.rightArrow.sendFocus();
            this.leftArrow.stopFocus();
         } else if (event.getKey() == Key.ENTER) {
            this.setHysteresis(0.0D);
            this.display.playSound(SoundEffect.EXTINGUISH);
         } else {
            this.focus();
            if (event.getKey() == Key.UP || event.getKey() == Key.DOWN) {
               super.onKeyPressed(event);
            }
         }

      }
   }

   public void onKeyReleased(MapKeyEvent event) {
      if (this.isActivated()) {
         if (event.getKey() == Key.LEFT) {
            this.leftArrow.stopFocus();
         } else if (event.getKey() == Key.RIGHT) {
            this.rightArrow.stopFocus();
         }
      }

      super.onKeyReleased(event);
   }

   public void onDraw() {
      this.view.drawRectangle(0, 0, this.getWidth(), this.getHeight(), (byte)119);
      this.view.fillRectangle(1, 1, this.getWidth() - 2, this.getHeight() - 2, this.isActivated() ? COLOR_BG_ACTIVATED : (this.isFocused() ? COLOR_BG_FOCUSED : COLOR_BG_DEFAULT));
      byte color = this.isFocused() ? 50 : 119;
      this.view.draw(HYSTERESIS_ICON, 2, 2, (byte)color);
      this.view.draw(MapFont.MINECRAFT, 14, 3, (byte)color, NUMBER_FORMAT.format(this.hysteresis));
   }

   static {
      HYSTERESIS_ICON = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/hysteresis.png");
   }
}
