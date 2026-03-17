package com.bergerkiller.bukkit.tc.attachments.ui.item;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetTooltip;
import com.bergerkiller.bukkit.tc.attachments.ui.SetValueTarget;

public abstract class CustomModelDataSelector extends MapWidget implements SetValueTarget {
   private int numDigits = 0;
   private int selectedDigit = 0;
   private int value = 0;
   private boolean isKeyUp = false;
   private boolean isKeyDown = false;
   public final MapWidgetTooltip tooltip = new MapWidgetTooltip();

   public CustomModelDataSelector() {
      this.setNumDigits(8);
      this.setFocusable(true);
      this.tooltip.setText("Custom Model Data");
   }

   public CustomModelDataSelector setNumDigits(int num) {
      if (this.numDigits != num) {
         this.numDigits = num;
         this.setSize(num * 4 + 3, 13);
      }

      return this;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      value = MathUtil.clamp(value, 0, (int)Math.pow(10.0D, (double)this.numDigits) - 1);
      if (this.value != value) {
         this.value = value;
         this.invalidate();
      }

   }

   public void onDraw() {
      int text_x = this.getWidth() - 1;
      int text_y = (this.getHeight() - 5) / 2;
      int tmp = this.value;

      int digit;
      for(digit = 0; digit < this.numDigits; ++digit) {
         text_x -= 4;
         char ch = '-';
         if (this.value > 0) {
            ch = Character.forDigit(tmp % 10, 10);
            tmp /= 10;
         }

         byte color = 34;
         if (this.isActivated() && digit == this.selectedDigit) {
            color = 122;
         }

         this.view.draw(MapFont.TINY.getSprite(ch), text_x, text_y, color);
      }

      if (this.isActivated()) {
         digit = this.getWidth() - 4 * (this.selectedDigit + 1) - 1;
         int upY = 0;
         int downY = this.getHeight() - 2;
         byte upColor = this.isKeyUp ? 18 : 122;
         byte downColor = this.isKeyDown ? 18 : 122;
         this.view.drawPixel(digit, upY + 1, (byte)upColor);
         this.view.drawPixel(digit + 1, upY, (byte)upColor);
         this.view.drawPixel(digit + 2, upY + 1, (byte)upColor);
         this.view.drawPixel(digit, downY, (byte)downColor);
         this.view.drawPixel(digit + 1, downY + 1, (byte)downColor);
         this.view.drawPixel(digit + 2, downY, (byte)downColor);
      } else if (this.isFocused()) {
         this.view.drawRectangle(0, (this.getHeight() - 5) / 2 - 2, this.getWidth(), 9, (byte)18);
      }

   }

   private static double getExp(int repeat) {
      int a = repeat / 3;
      int b = repeat % 3;
      double f = b == 0 ? 1.0D : (b == 1 ? 2.0D : 5.0D);
      return f * Math.pow(10.0D, (double)a);
   }

   public void onKeyReleased(MapKeyEvent event) {
      if (this.isKeyUp && event.getKey() == Key.UP) {
         this.isKeyUp = false;
         this.invalidate();
      } else if (this.isKeyDown && event.getKey() == Key.DOWN) {
         this.isKeyDown = false;
         this.invalidate();
      }

      super.onKeyReleased(event);
   }

   public void onKeyPressed(MapKeyEvent event) {
      if (event.getKey() == Key.ENTER) {
         if (this.isActivated()) {
            this.deactivate();
         } else {
            this.selectedDigit = 0;
            this.activate();
         }

      } else if (!this.isActivated()) {
         super.onKeyPressed(event);
      } else {
         if (event.getKey() == Key.BACK) {
            this.deactivate();
         } else if (event.getKey() == Key.LEFT) {
            ++this.selectedDigit;
            if (this.selectedDigit >= this.numDigits) {
               this.selectedDigit = this.numDigits - 1;
            }

            this.invalidate();
         } else if (event.getKey() == Key.RIGHT) {
            --this.selectedDigit;
            if (this.selectedDigit < 0) {
               this.selectedDigit = 0;
            }

            this.invalidate();
         } else {
            int incr = 0;
            if (event.getKey() == Key.UP) {
               incr = 1;
               this.isKeyUp = true;
               this.isKeyDown = false;
            } else if (event.getKey() == Key.DOWN) {
               incr = -1;
               this.isKeyUp = false;
               this.isKeyDown = true;
            }

            int incr = (int)((double)incr * getExp(event.getRepeat() / 50));
            incr *= (int)Math.pow(10.0D, (double)this.selectedDigit);
            this.value += incr;
            int max_value = (int)Math.pow(10.0D, (double)this.numDigits) - 1;
            if (this.value < 0) {
               this.value = 0;
            } else if (this.value > max_value) {
               this.value = max_value;
            }

            this.onValueChanged();
            this.invalidate();
         }

      }
   }

   public void onFocus() {
      super.onFocus();
      this.addWidget(this.tooltip);
      this.display.playSound(SoundEffect.CLICK_WOOD);
   }

   public void onBlur() {
      super.onBlur();
      this.removeWidget(this.tooltip);
   }

   public String getAcceptedPropertyName() {
      return "Custom Model Data";
   }

   public boolean acceptTextValue(String value) {
      return this.acceptTextValue(SetValueTarget.Operation.SET, value);
   }

   public boolean acceptTextValue(SetValueTarget.Operation operation, String value) {
      if (operation.perform(this::getValue, this::setValue, value)) {
         this.onValueChanged();
         return true;
      } else {
         return false;
      }
   }

   public abstract void onValueChanged();
}
