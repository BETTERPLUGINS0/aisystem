package com.bergerkiller.bukkit.tc.attachments.control.sound;

import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import java.util.ArrayList;
import java.util.List;

public abstract class MapWidgetSoundPlayStop extends MapWidget {
   private final MapWidgetSoundPlayStop.PlayButton play = new MapWidgetSoundPlayStop.PlayButton();
   private final MapWidgetSoundPlayStop.StopButton stop = new MapWidgetSoundPlayStop.StopButton();
   private boolean sentPlay = false;

   public abstract void onPlay();

   public abstract void onStop();

   public void onAttached() {
      this.addWidget(this.play.setBounds(0, 0, 11, 11));
      this.addWidget(this.stop.setBounds(this.play.getWidth() + 1, 0, 11, 11));
   }

   public void onDetached() {
      if (this.sentPlay) {
         this.sentPlay = false;
         this.onStop();
      }

      super.onDetached();
   }

   private void sendPlay() {
      this.sentPlay = true;
      this.onPlay();
   }

   private void sendStop() {
      this.sentPlay = false;
      this.onStop();
   }

   private class PlayButton extends MapWidgetSoundButton {
      private static final int AUTOPLAY_START_DELAY = 5;
      private static final int AUTOPLAY_ACTIVATION_TIME = 20;
      private int autoPlayCtr;
      private int autoPlayInterval;
      private boolean autoPlayActive;
      private int ticksSinceLastPress;
      private List<MapWidgetSoundPlayStop.PlayButton.HighlightPixel> highlightPixels;
      private MapWidgetSoundPlayStop.PlayButton.HighlightPixel lastDrawn;

      private PlayButton() {
         this.autoPlayCtr = 0;
         this.autoPlayInterval = 20;
         this.autoPlayActive = false;
         this.ticksSinceLastPress = 0;
         this.highlightPixels = new ArrayList();
         this.lastDrawn = null;
      }

      public void disableAutoPlay() {
         if (this.autoPlayActive) {
            this.autoPlayActive = false;
            this.resetPlayCounters();
            this.invalidate();
         }

      }

      private void resetPlayCounters() {
         this.autoPlayCtr = 0;
         this.ticksSinceLastPress = 0;
         this.lastDrawn = null;
      }

      public void onAttached() {
         this.lastDrawn = null;
         this.highlightPixels.clear();

         int x;
         for(x = this.getWidth() / 2; x <= this.getWidth() - 3; ++x) {
            this.addHighlightPixel(x, 1);
         }

         for(x = 2; x <= this.getHeight() - 3; ++x) {
            this.addHighlightPixel(this.getWidth() - 2, x);
         }

         for(x = this.getWidth() - 3; x >= 2; --x) {
            this.addHighlightPixel(x, this.getHeight() - 2);
         }

         for(x = this.getHeight() - 3; x >= 2; --x) {
            this.addHighlightPixel(1, x);
         }

         for(x = 2; x < this.getWidth() / 2; ++x) {
            this.addHighlightPixel(x, 1);
         }

         ((MapWidgetSoundPlayStop.PlayButton.HighlightPixel)this.highlightPixels.get(this.highlightPixels.size() - 1)).next = (MapWidgetSoundPlayStop.PlayButton.HighlightPixel)this.highlightPixels.get(0);
      }

      public void onClick() {
         if (this.autoPlayActive) {
            this.autoPlayInterval = this.ticksSinceLastPress;
            this.resetPlayCounters();
         }

         MapWidgetSoundPlayStop.this.sendPlay();
      }

      public void onClickHold(int ticksHeld) {
         if (!this.autoPlayActive && ticksHeld >= 5) {
            if (ticksHeld >= 25) {
               this.autoPlayActive = true;
               this.resetPlayCounters();
               MapWidgetSoundPlayStop.this.sendPlay();
            }

            this.invalidate();
         } else if (ticksHeld == 1) {
            this.invalidate();
         }

      }

      public void onFocus() {
         this.ticksSinceLastPress = this.autoPlayCtr;
      }

      public void onTick() {
         if (this.autoPlayActive) {
            ++this.ticksSinceLastPress;
            if (++this.autoPlayCtr >= this.autoPlayInterval) {
               this.autoPlayCtr = 0;
               MapWidgetSoundPlayStop.this.sendPlay();
            }

            this.invalidate();
         }

      }

      public void onDraw() {
         byte highlightColor = this.isFocused() ? MapColorPalette.getColor(200, 200, 150) : MapColorPalette.getColor(140, 140, 0);
         boolean isPlayPressed = false;
         if ((!this.autoPlayActive || this.autoPlayCtr != 0) && (!this.pressed || this.pressedTicks != 0)) {
            isPlayPressed = this.pressed;
            super.onDraw();
         } else {
            this.drawBackground(highlightColor, MapColorPalette.getColor(36, 89, 152), MapColorPalette.getColor(44, 109, 186));
            isPlayPressed = true;
         }

         MapWidgetSoundPlayStop.PlayButton.HighlightPixel end;
         MapWidgetSoundPlayStop.PlayButton.HighlightPixel p;
         if (this.autoPlayActive) {
            end = this.getHighlightProgress(this.autoPlayCtr, this.autoPlayInterval);
            if (this.lastDrawn != null && this.lastDrawn != end) {
               for(p = this.lastDrawn.next; p != end; p = p.next) {
                  this.view.writePixel(p.x, p.y, highlightColor);
               }
            }

            this.lastDrawn = end;
            this.view.writePixel(end.x, end.y, highlightColor);
         } else if (this.pressedTicks >= 5) {
            end = this.getHighlightProgress(this.pressedTicks - 5, 20);
            p = (MapWidgetSoundPlayStop.PlayButton.HighlightPixel)this.highlightPixels.get(0);

            while(true) {
               this.view.writePixel(p.x, p.y, highlightColor);
               if (p == end) {
                  break;
               }

               p = p.next;
            }
         }

         byte color = isPlayPressed ? 30 : MapColorPalette.getColor(0, 180, 0);
         int play_height = this.getHeight() - 4;
         int play_width = (play_height + 1) / 2;
         int play_x = (this.getWidth() - play_width + 1) / 2;
         int play_y = 2;

         for(int dx = 0; dx < play_width && play_height > 0; ++dx) {
            for(int dy = 0; dy < play_height; ++dy) {
               this.view.writePixel(play_x + dx, play_y + dy, color);
            }

            play_height -= 2;
            ++play_y;
         }

      }

      private MapWidgetSoundPlayStop.PlayButton.HighlightPixel getHighlightProgress(int mul, int div) {
         if (div <= 0) {
            return (MapWidgetSoundPlayStop.PlayButton.HighlightPixel)this.highlightPixels.get(0);
         } else {
            int index = this.highlightPixels.size() * Math.floorMod(mul, div) / div;
            index = Math.min(index, this.highlightPixels.size() - 1);
            return (MapWidgetSoundPlayStop.PlayButton.HighlightPixel)this.highlightPixels.get(index);
         }
      }

      private void addHighlightPixel(int x, int y) {
         MapWidgetSoundPlayStop.PlayButton.HighlightPixel curr = new MapWidgetSoundPlayStop.PlayButton.HighlightPixel(this.highlightPixels.size(), x, y);
         this.highlightPixels.add(curr);
         if (this.highlightPixels.size() > 1) {
            ((MapWidgetSoundPlayStop.PlayButton.HighlightPixel)this.highlightPixels.get(this.highlightPixels.size() - 2)).next = curr;
         }

      }

      // $FF: synthetic method
      PlayButton(Object x1) {
         this();
      }

      private class HighlightPixel {
         public final int x;
         public final int y;
         public final int index;
         public MapWidgetSoundPlayStop.PlayButton.HighlightPixel next;

         public HighlightPixel(int index, int x, int y) {
            this.index = index;
            this.x = x;
            this.y = y;
         }
      }
   }

   private class StopButton extends MapWidgetSoundButton {
      private StopButton() {
      }

      public void onClick() {
         MapWidgetSoundPlayStop.this.play.disableAutoPlay();
         MapWidgetSoundPlayStop.this.sendStop();
      }

      public void onDraw() {
         super.onDraw();
         this.view.fillRectangle(3, 3, this.getWidth() - 6, this.getHeight() - 6, this.pressed ? MapColorPalette.getColor(180, 0, 0) : 18);
      }

      // $FF: synthetic method
      StopButton(Object x1) {
         this();
      }
   }
}
