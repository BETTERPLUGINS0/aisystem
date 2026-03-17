package com.bergerkiller.bukkit.tc.attachments.control.sequencer;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentSelector;
import com.bergerkiller.bukkit.tc.attachments.control.effect.EffectLoop;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentSelector;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunction;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionConstant;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionSingleConfigItem;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapWidgetSequencerEffectGroup extends MapWidget {
   protected static final byte BACKGROUND_COLOR = MapColorPalette.getColor(54, 81, 114);
   private static final NumberFormat DURATION_FORMAT = Util.createNumberFormat(1, 4);
   private static final ConfigurationNode EMPTY_CONFIG = new ConfigurationNode();
   private static final int TOP_HEADER_HEIGHT = 8;
   private final MapWidgetSequencerConfigurationMenu menu;
   private final SequencerMode mode;
   private final List<MapWidgetSequencerEffect> effects = new ArrayList();
   private MapWidgetSequencerEffectGroup.Header header;
   private MapWidgetSequencerEffectGroup.HeaderTitle headerTitle;
   private MapWidgetSequencerEffectGroup.HeaderButton configureButton;
   private MapWidgetSequencerEffectGroup.HeaderButton addEffectButton;
   private EffectLoop.Time duration;

   public MapWidgetSequencerEffectGroup(MapWidgetSequencerConfigurationMenu menu, SequencerMode mode) {
      this.menu = menu;
      this.mode = mode;
      this.duration = EffectLoop.Time.seconds((Double)this.readConfig().getOrDefault("duration", 0.0D));
      this.setClipParent(true);
      this.updateBounds();
      Iterator var3 = this.readConfig().getNodeList("effects").iterator();

      while(var3.hasNext()) {
         ConfigurationNode effectConfig = (ConfigurationNode)var3.next();
         this.addEffect(new MapWidgetSequencerEffect(effectConfig));
      }

   }

   protected ConfigurationNode readConfig() {
      ConfigurationNode config = this.menu.getConfig().getNodeIfExists(this.mode.configKey());
      return config == null ? EMPTY_CONFIG : config;
   }

   protected ConfigurationNode writeConfig() {
      return this.menu.getConfig().getNode(this.mode.configKey());
   }

   public MapWidgetSequencerEffectGroup addEffect(MapWidgetSequencerEffect effect) {
      this.effects.add(effect);
      if (!effect.getConfig().hasParent()) {
         this.writeConfig().getNodeList("effects", false).add(effect.getConfig());
      }

      if (!this.duration.isZero()) {
         this.addEffectWidget(effect);
      }

      if (this.effects.size() == 1 && this.header != null) {
         this.header.invalidate();
      }

      this.updateBounds();
      return this;
   }

   public MapWidgetSequencerEffectGroup removeEffect(MapWidgetSequencerEffect effect) {
      int effectIndex = this.effects.indexOf(effect);
      if (effectIndex != -1) {
         boolean wasFocused = effect.isFocused();
         this.effects.remove(effectIndex);
         effect.getConfig().remove();
         if (!this.duration.isZero()) {
            this.removeWidget(effect);

            for(int i = 0; i < this.effects.size(); ++i) {
               ((MapWidgetSequencerEffect)this.effects.get(i)).setBounds(0, 8 + 10 * i, this.getWidth(), 11);
            }

            this.updateBounds();
            if (wasFocused) {
               if (effectIndex >= this.effects.size()) {
                  effectIndex = this.effects.size() - 1;
               }

               if (effectIndex != -1) {
                  ((MapWidgetSequencerEffect)this.effects.get(effectIndex)).focus();
               } else {
                  this.activate();
               }
            }
         }
      }

      return this;
   }

   public MapWidgetSequencerEffectGroup setDuration(double duration) {
      return this.setDuration(EffectLoop.Time.seconds(Math.max(0.0D, duration)));
   }

   public MapWidgetSequencerEffectGroup setDuration(EffectLoop.Time duration) {
      if (!this.duration.equals(duration)) {
         boolean effectsVisibleChanged = this.duration.isZero() != duration.isZero();
         this.duration = duration;
         this.writeConfig().set("duration", duration.isZero() ? null : duration.seconds);
         if (effectsVisibleChanged) {
            if (this.addEffectButton != null) {
               this.addEffectButton.setEnabled(!duration.isZero());
            }

            Iterator var3 = this.effects.iterator();

            while(var3.hasNext()) {
               MapWidgetSequencerEffect effect = (MapWidgetSequencerEffect)var3.next();
               this.removeWidget(effect);
            }

            if (!duration.isZero()) {
               this.effects.forEach(this::addEffectWidget);
            }

            if (this.header != null) {
               this.header.invalidate();
            }

            this.updateBounds();
         }

         if (this.headerTitle != null) {
            this.headerTitle.invalidate();
         }
      }

      return this;
   }

   public EffectLoop.Time getDuration() {
      return this.duration;
   }

   private void addEffectWidget(MapWidgetSequencerEffect effect) {
      int index = this.effects.indexOf(effect);
      effect.setBounds(0, 8 + 10 * index, this.getWidth(), 11);
      this.addWidget(effect);
   }

   private void updateBounds() {
      int newHeight = 8 + (!this.duration.isZero() && !this.effects.isEmpty() ? this.effects.size() * 10 + 1 : 0);
      boolean heightChanged = this.getHeight() != newHeight;
      this.setBounds(0, this.getY(), this.menu.getWidth(), newHeight);
      if (heightChanged) {
         this.menu.recalculateContainerSize();
      }

   }

   public void onAttached() {
      this.header = (MapWidgetSequencerEffectGroup.Header)this.addWidget(new MapWidgetSequencerEffectGroup.Header());
      this.header.setBounds(0, 0, this.getWidth(), 8);
      this.headerTitle = (MapWidgetSequencerEffectGroup.HeaderTitle)this.header.addWidget(new MapWidgetSequencerEffectGroup.HeaderTitle());
      this.headerTitle.setBounds(0, 0, this.getWidth() - 43, 7);
      this.configureButton = (MapWidgetSequencerEffectGroup.HeaderButton)this.header.addWidget(new MapWidgetSequencerEffectGroup.HeaderButton(MapWidgetSequencerEffect.HeaderIcon.CONFIGURE) {
         public void onActivate() {
            this.display.playSound(SoundEffect.PISTON_EXTEND);
            MapWidgetSequencerEffectGroup.this.menu.addWidget(MapWidgetSequencerEffectGroup.this.new ConfigureDialog());
         }
      });
      this.configureButton.setPosition(this.getWidth() - 43, 0);
      this.addEffectButton = (MapWidgetSequencerEffectGroup.HeaderButton)this.header.addWidget(new MapWidgetSequencerEffectGroup.HeaderButton(MapWidgetSequencerEffect.HeaderIcon.ADD) {
         public void onActivate() {
            this.display.playSound(SoundEffect.PISTON_EXTEND);
            MapWidgetSequencerEffectGroup.this.menu.addWidget((new MapWidgetAttachmentSelector<Attachment.EffectAttachment>(AttachmentSelector.all(Attachment.EffectAttachment.class).excludingSelf()) {
               public List<String> getAttachmentNames(AttachmentSelector<Attachment.EffectAttachment> allSelector) {
                  return MapWidgetSequencerEffectGroup.this.menu.getEffectNames(allSelector);
               }

               public void onSelected(final AttachmentSelector<Attachment.EffectAttachment> effectSelector) {
                  MapWidgetSequencerEffectGroup.this.menu.addWidget(new MapWidgetSequencerTypeSelector() {
                     public void onSelected(SequencerType type) {
                        MapWidgetSequencerEffectGroup.this.menu.effectSelButtonIndex = 0;
                        MapWidgetSequencerEffectGroup.this.addEffect((new MapWidgetSequencerEffect(type, effectSelector)).focusOnActivate());
                     }
                  });
               }
            }).setTitle("Set Effect to play"));
         }
      });
      this.addEffectButton.setEnabled(!this.duration.isZero());
      this.addEffectButton.setPosition(this.getWidth() - 7, 0);
   }

   private class Header extends MapWidget {
      public Header() {
         this.setClipParent(true);
      }

      public void onDraw() {
         if (!MapWidgetSequencerEffectGroup.this.duration.isZero() && !MapWidgetSequencerEffectGroup.this.effects.isEmpty()) {
            this.view.fillRectangle(2, 0, this.getWidth() - 2, this.getHeight(), MapWidgetSequencerEffectGroup.BACKGROUND_COLOR);
            this.view.drawLine(1, 1, 1, this.getHeight() - 1, MapWidgetSequencerEffectGroup.BACKGROUND_COLOR);
            this.view.drawPixel(0, this.getHeight() - 1, MapWidgetSequencerEffectGroup.BACKGROUND_COLOR);
         } else {
            this.view.fillRectangle(2, 0, this.getWidth() - 2, this.getHeight() - 1, MapWidgetSequencerEffectGroup.BACKGROUND_COLOR);
            this.view.drawLine(1, 1, 1, this.getHeight() - 3, MapWidgetSequencerEffectGroup.BACKGROUND_COLOR);
         }

      }
   }

   private abstract static class HeaderButton extends MapWidget {
      private final MapWidgetSequencerEffect.HeaderIcon icon;

      public HeaderButton(MapWidgetSequencerEffect.HeaderIcon icon) {
         this.setFocusable(true);
         this.setClipParent(true);
         this.setSize(icon.getWidth(), icon.getHeight());
         this.icon = icon;
      }

      public abstract void onActivate();

      public void onDraw() {
         this.view.draw(this.icon.getIcon(this.isEnabled(), this.isFocused()), 0, 0);
      }
   }

   private class HeaderTitle extends MapWidget {
      public HeaderTitle() {
         this.setClipParent(true);
      }

      public void onDraw() {
         byte textColor = MapWidgetSequencerEffectGroup.this.duration.isZero() ? MapColorPalette.getColor(72, 108, 152) : MapColorPalette.getColor(213, 201, 140);
         this.view.draw(MapWidgetSequencerEffectGroup.this.mode.icon(), 2, 1, textColor);
         this.view.draw(MapFont.TINY, 11, 1, textColor, MapWidgetSequencerEffectGroup.this.mode.title());
         this.view.draw(MapFont.TINY, 37, 1, textColor, MapWidgetSequencerEffectGroup.this.duration.isZero() ? "[OFF]" : MapWidgetSequencerEffectGroup.DURATION_FORMAT.format(MapWidgetSequencerEffectGroup.this.duration.seconds) + "s");
      }
   }

   private class ConfigureDialog extends MapWidgetMenu {
      public ConfigureDialog() {
         this.setPositionAbsolute(true);
         this.setBounds(14, 30, 100, 78);
         this.setBackgroundColor(MapColorPalette.getColor(72, 108, 152));
         this.labelColor = 119;
      }

      public void onAttached() {
         this.addLabel(5, 6, "Duration (s):");
         ((<undefinedtype>)this.addWidget(new MapWidgetNumberBox() {
            public void onAttached() {
               this.setRange(0.0D, 100000.0D);
               this.setIncrement(0.01D);
               this.setInitialValue(MapWidgetSequencerEffectGroup.this.duration.seconds);
               this.setTextOverride(MapWidgetSequencerEffectGroup.this.duration.isZero() ? "Off" : null);
               super.onAttached();
            }

            public void onValueChanged() {
               MapWidgetSequencerEffectGroup.this.setDuration(this.getValue());
               this.setTextOverride(this.getValue() > 0.0D ? null : "Off");
            }
         })).setBounds(5, 13, 66, 11);
         this.addLabel(5, 27, "Playback Speed:");
         ((<undefinedtype>)this.addWidget(new MapWidgetTransferFunctionSingleConfigItem(MapWidgetSequencerEffectGroup.this.menu.getTransferFunctionHost(), MapWidgetSequencerEffectGroup.this.writeConfig(), "speed", () -> {
            return false;
         }) {
            public TransferFunction createDefault() {
               return TransferFunctionConstant.of(1.0D);
            }
         })).setBounds(5, 34, this.getWidth() - 10, 15);
         this.addLabel(5, 53, "Interrupt Play:");
         ((<undefinedtype>)this.addWidget(new MapWidgetButton() {
            public void onAttached() {
               this.updateText();
               super.onAttached();
            }

            public void onActivate() {
               MapWidgetSequencerEffectGroup.this.writeConfig().set("interrupt", !(Boolean)MapWidgetSequencerEffectGroup.this.readConfig().getOrDefault("interrupt", false));
               this.updateText();
            }

            private void updateText() {
               this.setText((Boolean)MapWidgetSequencerEffectGroup.this.readConfig().getOrDefault("interrupt", false) ? "Yes" : "No");
            }
         })).setBounds(5, 60, 52, 12);
         super.onAttached();
      }
   }
}
