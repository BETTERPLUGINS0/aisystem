package com.bergerkiller.bukkit.tc.attachments.control.sequencer;

import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.tc.attachments.control.effect.EffectLoop;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunction;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionBoolean;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionSingleConfigItem;

public class MapWidgetSequencerTopHeader extends MapWidget {
   private static final byte PLAY_STATUS_BG_COLOR_PLAYING = MapColorPalette.getColor(180, 177, 172);
   private static final byte PLAY_STATUS_BG_COLOR_STOPPED = MapColorPalette.getColor(86, 88, 97);
   private static final byte PLAY_STATUS_TEXT_COLOR_STOPPED = MapColorPalette.getColor(220, 220, 220);
   private static final byte PLAY_STATUS_TEXT_COLOR_PLAYING = MapColorPalette.getColor(247, 233, 163);
   private static final MapTexture ICON_PLAYING;
   private static final MapTexture ICON_STOPPED;
   private static final MapTexture ICON_AUTOMATIC;

   public MapWidgetSequencerTopHeader() {
      this.setClipParent(true);
   }

   private MapWidgetSequencerConfigurationMenu getMenu() {
      for(MapWidget w = this.getParent(); w != null; w = w.getParent()) {
         if (w instanceof MapWidgetSequencerConfigurationMenu) {
            return (MapWidgetSequencerConfigurationMenu)w;
         }
      }

      throw new IllegalStateException("Effect not added to a effect group list widget");
   }

   public void onAttached() {
      final MapWidgetSequencerConfigurationMenu menu = this.getMenu();
      ((<undefinedtype>)this.addWidget(new MapWidgetSequencerTopHeader.Button() {
         private boolean wasPlaying = false;

         public void onAttached() {
            SequencerPlayStatus playStatus = menu.getPlayStatus();
            this.updateIcon(playStatus);
            this.wasPlaying = playStatus.isPlaying();
         }

         public void onActivate() {
            if (menu.getPlayStatus().isPlaying()) {
               menu.stopPlaying();
            } else {
               menu.startPlaying();
            }

         }

         public void onTick() {
            SequencerPlayStatus playStatus = menu.getPlayStatus();
            if (playStatus.isPlaying() != this.wasPlaying) {
               this.wasPlaying = playStatus.isPlaying();
               this.updateIcon(playStatus);
            }

         }

         public void updateIcon(SequencerPlayStatus playStatus) {
            this.setIcon(playStatus.isPlaying() ? MapWidgetSequencerEffect.HeaderIcon.STOP : MapWidgetSequencerEffect.HeaderIcon.PLAY);
         }
      })).setPosition(83, 0);
      ((<undefinedtype>)this.addWidget(new MapWidgetSequencerTopHeader.Button() {
         public void onAttached() {
            this.updateIcon(this.getCurrentMode());
         }

         private EffectLoop.RunMode getCurrentMode() {
            return (EffectLoop.RunMode)menu.getConfig().getOrDefault("runMode", EffectLoop.RunMode.ASYNCHRONOUS);
         }

         public void onActivate() {
            EffectLoop.RunMode mode = this.getCurrentMode();
            mode = EffectLoop.RunMode.values()[(mode.ordinal() + 1) % EffectLoop.RunMode.values().length];
            menu.getConfig().set("runMode", mode);
            this.updateIcon(mode);
            this.display.playSound(SoundEffect.CLICK);
         }

         public void updateIcon(EffectLoop.RunMode mode) {
            if (mode == EffectLoop.RunMode.SYNCHRONOUS) {
               this.setIcon(MapWidgetSequencerEffect.HeaderIcon.SYNC);
            } else {
               this.setIcon(MapWidgetSequencerEffect.HeaderIcon.ASYNC);
            }

         }
      })).setPosition(4, 0);
      ((<undefinedtype>)this.addWidget(new MapWidgetSequencerTopHeader.Button() {
         public void onActivate() {
            this.display.playSound(SoundEffect.PISTON_EXTEND);
            menu.addWidget(MapWidgetSequencerTopHeader.this.new ConfigureAutoPlayDialog());
         }
      })).setIcon(MapWidgetSequencerEffect.HeaderIcon.AUTOPLAY).setPosition(39, 0);
   }

   public void onDraw() {
      this.view.fillRectangle(1, 0, this.getWidth() - 2, this.getHeight(), MapWidgetSequencerEffectGroup.BACKGROUND_COLOR);
      this.view.drawLine(0, 1, 0, this.getHeight() - 2, MapWidgetSequencerEffectGroup.BACKGROUND_COLOR);
      this.view.drawLine(this.getWidth() - 1, 1, this.getWidth() - 1, this.getHeight() - 2, MapWidgetSequencerEffectGroup.BACKGROUND_COLOR);
   }

   static {
      ICON_PLAYING = MapWidgetSequencerEffect.TEXTURE_ATLAS.getView(19, 35, 7, 7).clone();
      ICON_STOPPED = MapWidgetSequencerEffect.TEXTURE_ATLAS.getView(26, 35, 7, 7).clone();
      ICON_AUTOMATIC = MapWidgetSequencerEffect.TEXTURE_ATLAS.getView(33, 35, 7, 7).clone();
   }

   private abstract static class Button extends MapWidget {
      private MapWidgetSequencerEffect.HeaderIcon icon;

      public Button() {
         this.setFocusable(true);
         this.setClipParent(true);
         this.icon = this.icon;
      }

      public MapWidgetSequencerTopHeader.Button setIcon(MapWidgetSequencerEffect.HeaderIcon icon) {
         this.icon = icon;
         this.setSize(icon.getWidth(), icon.getHeight());
         this.invalidate();
         return this;
      }

      public abstract void onActivate();

      public void onDraw() {
         this.view.draw(this.icon.getIcon(this.isEnabled(), this.isFocused()), 0, 0);
      }
   }

   private class PlayStatusWidget extends MapWidget {
      private SequencerPlayStatus lastPlayStatus;

      private PlayStatusWidget() {
         this.lastPlayStatus = SequencerPlayStatus.STOPPED_AUTOMATIC;
      }

      public void onAttached() {
         this.updatePlayStatus();
      }

      public void onTick() {
         this.updatePlayStatus();
      }

      public void onDraw() {
         this.view.drawRectangle(0, 0, this.getWidth(), this.getHeight(), (byte)119);
         this.view.fillRectangle(1, 1, this.getWidth() - 2, this.getHeight() - 2, this.lastPlayStatus.isPlaying() ? MapWidgetSequencerTopHeader.PLAY_STATUS_BG_COLOR_PLAYING : MapWidgetSequencerTopHeader.PLAY_STATUS_BG_COLOR_STOPPED);
         int textX = 11;
         this.view.draw(this.lastPlayStatus.isPlaying() ? MapWidgetSequencerTopHeader.ICON_PLAYING : MapWidgetSequencerTopHeader.ICON_STOPPED, 2, 2);
         if (this.lastPlayStatus.isAutomatic()) {
            this.view.draw(MapWidgetSequencerTopHeader.ICON_AUTOMATIC, textX - 1, 2);
            textX += 8;
         }

         String text = this.lastPlayStatus.isAutomatic() ? "automatically" : "manually";
         byte textColor = this.lastPlayStatus.isPlaying() ? MapWidgetSequencerTopHeader.PLAY_STATUS_TEXT_COLOR_PLAYING : MapWidgetSequencerTopHeader.PLAY_STATUS_TEXT_COLOR_STOPPED;
         this.view.getView(textX, 2, this.getWidth() - textX - 1, this.getHeight() - 3).draw(MapFont.MINECRAFT, 0, 0, textColor, text);
      }

      private void updatePlayStatus() {
         SequencerPlayStatus playStatus = MapWidgetSequencerTopHeader.this.getMenu().getPlayStatus();
         if (playStatus != this.lastPlayStatus) {
            this.lastPlayStatus = playStatus;
            this.invalidate();
         }

      }

      // $FF: synthetic method
      PlayStatusWidget(Object x1) {
         this();
      }
   }

   private class ConfigureAutoPlayDialog extends MapWidgetMenu {
      public ConfigureAutoPlayDialog() {
         this.setPositionAbsolute(true);
         this.setBounds(14, 40, 100, 54);
         this.setBackgroundColor(MapColorPalette.getColor(72, 108, 152));
         this.labelColor = 119;
      }

      public void onAttached() {
         MapWidgetSequencerConfigurationMenu menu = MapWidgetSequencerTopHeader.this.getMenu();
         this.addLabel(5, 5, "Play Automatically:");
         ((<undefinedtype>)this.addWidget(new MapWidgetTransferFunctionSingleConfigItem(menu.getTransferFunctionHost(), menu.getConfig(), "autoplay", () -> {
            return false;
         }) {
            public TransferFunction createDefault() {
               return TransferFunctionBoolean.FALSE;
            }
         })).setBounds(5, 12, this.getWidth() - 10, 15);
         this.addLabel(5, 31, "Current Status:");
         ((MapWidgetSequencerTopHeader.PlayStatusWidget)this.addWidget(MapWidgetSequencerTopHeader.this.new PlayStatusWidget())).setBounds(5, 38, this.getWidth() - 10, 11);
         super.onAttached();
      }
   }
}
