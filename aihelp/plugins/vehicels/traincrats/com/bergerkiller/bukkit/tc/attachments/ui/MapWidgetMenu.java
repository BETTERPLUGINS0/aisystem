package com.bergerkiller.bukkit.tc.attachments.ui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetWindow;
import com.bergerkiller.bukkit.common.resources.SoundEffect;

public class MapWidgetMenu extends MapWidgetWindow {
   protected MapWidgetAttachmentNode attachment;
   protected byte labelColor = 30;
   protected boolean playSoundWhenBackClosed = false;
   protected boolean exitOnBack = true;

   public MapWidgetMenu() {
      this.setDepthOffset(4);
      this.setFocusable(true);
   }

   public void setAttachment(MapWidgetAttachmentNode attachment) {
      this.attachment = attachment;
   }

   public void setExitOnBack(boolean exitOnBack) {
      this.exitOnBack = exitOnBack;
   }

   public void onAttached() {
      super.onAttached();
      this.activate();
   }

   public void onKeyPressed(MapKeyEvent event) {
      if (this.exitOnBack && event.getKey() == Key.BACK && this.isActivated()) {
         if (this.playSoundWhenBackClosed) {
            this.display.playSound(SoundEffect.CLICK, 1.0F, 0.6F);
         }

         this.close();
      } else {
         super.onKeyPressed(event);
      }
   }

   public void onTick() {
      super.onTick();
      if (this.attachment != null && this.attachment.getAttachmentConfig().isRemoved()) {
         this.close();
      }

   }

   public void close() {
      this.removeWidget();
   }

   public void addLabel(int x, int y, String text) {
      MapWidgetText label = new MapWidgetText();
      label.setFont(MapFont.TINY);
      label.setText(text);
      label.setPosition(x, y);
      label.setColor(MapColorPalette.getSpecular(this.labelColor, 0.5F));
      this.addWidget(label);
   }
}
