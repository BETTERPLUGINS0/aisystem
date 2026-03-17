package com.bergerkiller.bukkit.tc.attachments.control.sound;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;

public abstract class MapWidgetSoundButton extends MapWidgetSoundElement {
   protected boolean pressed = false;
   protected int pressedTicks = 0;

   public abstract void onClick();

   public void onClickHold(int pressedTicks) {
   }

   public void onKey(MapKeyEvent event) {
      if (event.getKey() == Key.ENTER) {
         if (!this.pressed) {
            this.pressed = true;
            this.pressedTicks = 0;
            this.invalidate();
            this.onClick();
         } else {
            ++this.pressedTicks;
            this.onClickHold(this.pressedTicks);
         }
      } else {
         super.onKey(event);
      }

   }

   public void onKeyPressed(MapKeyEvent event) {
      if (event.getKey() != Key.ENTER) {
         super.onKeyPressed(event);
      }

   }

   public void onKeyReleased(MapKeyEvent event) {
      if (event.getKey() == Key.ENTER) {
         this.pressedTicks = 0;
         if (this.pressed) {
            this.pressed = false;
            this.invalidate();
         }
      } else {
         super.onKeyReleased(event);
      }

   }

   public void onBlur() {
      this.pressedTicks = 0;
      this.pressed = false;
   }

   public void onDraw() {
      if (this.pressed) {
         this.drawBackground((byte)119, MapColorPalette.getColor(36, 89, 152), MapColorPalette.getColor(44, 109, 186));
      } else {
         super.onDraw();
      }

   }
}
