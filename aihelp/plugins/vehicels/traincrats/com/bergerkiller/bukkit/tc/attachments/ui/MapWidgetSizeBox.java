package com.bergerkiller.bukkit.tc.attachments.ui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import java.util.function.Consumer;
import org.bukkit.util.Vector;

public abstract class MapWidgetSizeBox extends MapWidget {
   public final MapWidgetNumberBox x = (MapWidgetNumberBox)this.addWidget(new MapWidgetSizeBox.SizeNumberBox() {
      public String getAcceptedPropertyName() {
         return "Size X-Axis";
      }

      protected void onResetClickSound() {
         if (!MapWidgetSizeBox.this.isUniformFocused()) {
            super.onResetClickSound();
         }

      }
   });
   public final MapWidgetNumberBox y = (MapWidgetNumberBox)this.addWidget(new MapWidgetSizeBox.SizeNumberBox() {
      public String getAcceptedPropertyName() {
         return "Size Y-Axis";
      }

      protected void onResetClickSound() {
         if (!MapWidgetSizeBox.this.isUniformFocused()) {
            super.onResetClickSound();
         }

      }
   });
   public final MapWidgetNumberBox z = (MapWidgetNumberBox)this.addWidget(new MapWidgetSizeBox.SizeNumberBox() {
      public String getAcceptedPropertyName() {
         return "Size Z-Axis";
      }

      public void onKeyPressed(MapKeyEvent event) {
         if (event.getKey() == Key.DOWN) {
            MapWidgetSizeBox.this.setUniformFocused(true);
         } else {
            super.onKeyPressed(event);
         }

      }
   });
   private Vector uniformFocusStart = new Vector();
   private boolean suppressSizeChanges = false;
   private boolean uniformFocusActive = false;
   private double defaultValue = 1.0D;
   private boolean canBeNegative = false;

   public MapWidgetSizeBox() {
      this.setRetainChildWidgets(true);
      this.setRangeAndDefault(false, 1.0D);
   }

   public boolean isUniformFocused() {
      return this.uniformFocusActive;
   }

   public void setUniformFocused(boolean focused) {
      if (this.uniformFocusActive != focused) {
         this.uniformFocusActive = focused;
         if (focused) {
            this.setFocusable(true);
            this.focus();
            this.forAllAxis((a) -> {
               a.setAlwaysFocused(true);
            });
            this.uniformFocusStart = new Vector(this.x.getValue(), this.y.getValue(), this.z.getValue());
         } else {
            this.forAllAxis((a) -> {
               a.setAlwaysFocused(false);
               a.updateArrowFocus(false, false);
            });
            this.setFocusable(false);
         }

      }
   }

   public MapWidgetSizeBox setRangeAndDefault(boolean canBeNegative, double defaultValue) {
      double min = canBeNegative ? -1000.0D : 0.01D;
      double max = 1000.0D;
      this.x.setRange(min, max);
      this.y.setRange(min, max);
      this.z.setRange(min, max);
      this.defaultValue = defaultValue;
      return this;
   }

   public abstract void onSizeChanged();

   public void setSize(double sx, double sy, double sz) {
      if (sx != this.x.getValue() || sy != this.y.getValue() || sz != this.z.getValue()) {
         this.setInitialSize(sx, sy, sz);
         this.onSizeChanged();
      }

   }

   public void setInitialSize(double sx, double sy, double sz) {
      this.x.setInitialValue(sx);
      this.y.setInitialValue(sy);
      this.z.setInitialValue(sz);
   }

   public void onBoundsChanged() {
      int selHeight = (this.getHeight() - 2) / 3;
      this.x.setBounds(0, 0, this.getWidth(), selHeight);
      this.y.setBounds(0, (this.getHeight() - selHeight) / 2, this.getWidth(), selHeight);
      this.z.setBounds(0, this.getHeight() - selHeight, this.getWidth(), selHeight);
   }

   private void increaseUniform(double increase, int repeat) {
      double absX = Math.abs(this.uniformFocusStart.getX());
      double absY = Math.abs(this.uniformFocusStart.getY());
      double absZ = Math.abs(this.uniformFocusStart.getZ());
      this.suppressSizeChanges = true;
      if (Math.max(Math.max(absX, absY), absZ) == 0.0D) {
         double value = MapWidgetNumberBox.scaledIncrease(this.x.getValue(), increase, repeat);
         this.x.setValue(value);
         this.y.setValue(value);
         this.z.setValue(value);
      } else if (absX > absZ && absX > absY) {
         this.scaleAxisByIncreasing(this.x, this.uniformFocusStart.getX(), increase, repeat);
      } else if (absY > absX && absY > absZ) {
         this.scaleAxisByIncreasing(this.y, this.uniformFocusStart.getY(), increase, repeat);
      } else {
         this.scaleAxisByIncreasing(this.z, this.uniformFocusStart.getZ(), increase, repeat);
      }

      this.suppressSizeChanges = false;
      this.onSizeChanged();
   }

   private void scaleAxisByIncreasing(MapWidgetNumberBox axis, double uniformStart, double increase, int repeat) {
      axis.setValue(MapWidgetNumberBox.scaledIncrease(axis.getValue(), increase, repeat));
      double scale = axis.getValue() / uniformStart;
      if (axis != this.x) {
         this.x.setValue(this.roundByIncrease(this.uniformFocusStart.getX() * scale, increase));
      }

      if (axis != this.y) {
         this.y.setValue(this.roundByIncrease(this.uniformFocusStart.getY() * scale, increase));
      }

      if (axis != this.z) {
         this.z.setValue(this.roundByIncrease(this.uniformFocusStart.getZ() * scale, increase));
      }

   }

   private double roundByIncrease(double value, double incr) {
      return incr * (double)Math.round(value / incr);
   }

   public void onKey(MapKeyEvent event) {
      if (this.isUniformFocused() && event.getKey() == Key.ENTER) {
         this.forAllAxis((a) -> {
            a.onKey(event);
         });
         if (this.x.isHoldEnterResetComplete()) {
            this.uniformFocusStart = new Vector(this.x.getValue(), this.y.getValue(), this.z.getValue());
         }

      } else {
         super.onKey(event);
      }
   }

   public void onKeyReleased(MapKeyEvent event) {
      if (!this.isUniformFocused()) {
         super.onKeyReleased(event);
      } else {
         if (event.getKey() == Key.LEFT) {
            this.forAllAxis((a) -> {
               a.stopArrowFocus(false);
            });
         } else if (event.getKey() == Key.RIGHT) {
            this.forAllAxis((a) -> {
               a.stopArrowFocus(true);
            });
         } else if (event.getKey() == Key.ENTER) {
            this.forAllAxis((a) -> {
               a.onKeyReleased(event);
            });
         }

      }
   }

   public void onKeyPressed(MapKeyEvent event) {
      if (!this.isUniformFocused()) {
         super.onKeyPressed(event);
      } else {
         if (event.getKey() == Key.LEFT) {
            this.forAllAxis((a) -> {
               a.updateArrowFocus(true, false);
            });
            this.increaseUniform(-0.01D, event.getRepeat());
         } else if (event.getKey() == Key.RIGHT) {
            this.forAllAxis((a) -> {
               a.updateArrowFocus(false, true);
            });
            this.increaseUniform(0.01D, event.getRepeat());
         } else if (event.getKey() == Key.ENTER) {
            this.forAllAxis((a) -> {
               a.onKeyPressed(event);
            });
            return;
         }

         super.onKeyPressed(event);
         if (!this.isFocused()) {
            this.setUniformFocused(false);
         } else if (event.getKey() == Key.UP) {
            this.setUniformFocused(false);
            this.x.focus();
         }

      }
   }

   private void forAllAxis(Consumer<MapWidgetNumberBox> action) {
      action.accept(this.x);
      action.accept(this.y);
      action.accept(this.z);
   }

   private class SizeNumberBox extends MapWidgetNumberBox {
      private SizeNumberBox() {
      }

      public void onValueChanged() {
         if (!MapWidgetSizeBox.this.suppressSizeChanges) {
            MapWidgetSizeBox.this.onSizeChanged();
         }

      }

      public void onResetValue() {
         this.setValue(MapWidgetSizeBox.this.defaultValue);
      }

      protected void onDraw(boolean focused) {
         super.onDraw(focused || MapWidgetSizeBox.this.isFocused());
      }

      // $FF: synthetic method
      SizeNumberBox(Object x1) {
         this();
      }
   }
}
