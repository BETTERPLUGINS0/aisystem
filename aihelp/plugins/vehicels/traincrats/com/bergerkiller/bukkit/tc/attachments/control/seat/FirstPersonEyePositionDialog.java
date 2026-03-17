package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetBlinkyButton;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetTooltip;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FirstPersonEyePositionDialog extends MapWidgetMenu {
   private final MapWidgetAttachmentNode attachment;
   private boolean isLoadingWidgets;

   public FirstPersonEyePositionDialog(MapWidgetAttachmentNode attachment) {
      this.attachment = attachment;
      this.setBounds(0, -10, 103, 95);
      this.setBackgroundColor((byte)30);
   }

   public void onAttached() {
      super.onAttached();
      this.isLoadingWidgets = true;
      int slider_width = 75;
      int y_offset = 5;
      int y_step = 12;
      ((FirstPersonEyePositionDialog.SeatEyeNumberBox)this.addWidget(new FirstPersonEyePositionDialog.SeatEyeNumberBox("posX", "Position X-Coordinate", 0.01D))).setBounds(26, y_offset, slider_width, 11);
      this.addLabel(4, y_offset + 3, "Pos.X");
      int y_offset = y_offset + y_step;
      ((FirstPersonEyePositionDialog.SeatEyeNumberBox)this.addWidget(new FirstPersonEyePositionDialog.SeatEyeNumberBox("posY", "Position Y-Coordinate", 0.01D))).setBounds(26, y_offset, slider_width, 11);
      this.addLabel(4, y_offset + 3, "Pos.Y");
      y_offset += y_step;
      ((FirstPersonEyePositionDialog.SeatEyeNumberBox)this.addWidget(new FirstPersonEyePositionDialog.SeatEyeNumberBox("posZ", "Position Z-Coordinate", 0.01D))).setBounds(26, y_offset, slider_width, 11);
      this.addLabel(4, y_offset + 3, "Pos.Z");
      y_offset += y_step;
      ((FirstPersonEyePositionDialog.SeatEyeNumberBox)this.addWidget(new FirstPersonEyePositionDialog.SeatEyeNumberBox("rotX", "Rotation Pitch", 0.1D))).setBounds(26, y_offset, slider_width, 11);
      this.addLabel(4, y_offset + 3, "Pitch");
      y_offset += y_step;
      ((FirstPersonEyePositionDialog.SeatEyeNumberBox)this.addWidget(new FirstPersonEyePositionDialog.SeatEyeNumberBox("rotY", "Rotation Yaw", 0.1D))).setBounds(26, y_offset, slider_width, 11);
      this.addLabel(4, y_offset + 3, "Yaw");
      y_offset += y_step;
      ((FirstPersonEyePositionDialog.SeatEyeNumberBox)this.addWidget(new FirstPersonEyePositionDialog.SeatEyeNumberBox("rotZ", "Rotation Roll", 0.1D))).setBounds(26, y_offset, slider_width, 11);
      this.addLabel(4, y_offset + 3, "Roll");
      y_offset += y_step;
      ((<undefinedtype>)this.addWidget(new MapWidgetButton() {
         private final MapWidgetTooltip tooltip = new MapWidgetTooltip();

         public void onAttached() {
            super.onAttached();
            this.tooltip.setText("Sets eye position based\non seat display mode");
         }

         public void onFocus() {
            this.addWidget(this.tooltip);
         }

         public void onBlur() {
            this.removeWidget(this.tooltip);
         }

         public void onActivate() {
            this.display.playSound(SoundEffect.CLICK);
            ConfigurationNode config = FirstPersonEyePositionDialog.this.attachment.getConfig();
            if (config.isNode("firstPersonViewPosition")) {
               config.remove("firstPersonViewPosition");
               this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
            }

            FirstPersonEyePositionDialog.this.setAutomaticDisplayed(true);
            FirstPersonEyePositionDialog.this.showArrowPreview(true);
         }
      })).setText("Automatic").setBounds(33, y_offset, 61, 13);
      ((<undefinedtype>)this.addWidget(new MapWidgetBlinkyButton() {
         public void onClick() {
            FirstPersonEyePositionDialog.this.previewEye(20);
         }

         public void onRepeatClick() {
            FirstPersonEyePositionDialog.this.previewEye(2);
         }

         public void onClickHoldRelease() {
            FirstPersonEyePositionDialog.this.previewEye(0);
         }
      })).setRepeatClickEnabled(true).setTooltip("Preview").setIcon("attachments/view_camera_preview.png").setPosition(17, y_offset);
      this.isLoadingWidgets = false;
   }

   public void onDetached() {
      super.onDetached();
      this.previewEye(0);
      this.showArrowPreview(false);
   }

   private void previewEye(int numTicks) {
      List<Attachment> attachments = this.attachment.getAttachments();
      if (!attachments.isEmpty()) {
         Iterator var3 = this.display.getOwners().iterator();

         while(true) {
            Player player;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               player = (Player)var3.next();
            } while(!this.display.isControlling(player));

            Vector pos = player.getEyeLocation().toVector();
            CartAttachmentSeat closestSeat = null;
            Iterator var7 = attachments.iterator();

            while(var7.hasNext()) {
               Attachment liveAttachment = (Attachment)var7.next();
               if (liveAttachment instanceof CartAttachmentSeat) {
                  if (closestSeat == null) {
                     closestSeat = (CartAttachmentSeat)liveAttachment;
                  } else {
                     double d1 = closestSeat.getTransform().toVector().distanceSquared(pos);
                     double d2 = liveAttachment.getTransform().toVector().distanceSquared(pos);
                     if (d2 < d1) {
                        closestSeat = (CartAttachmentSeat)liveAttachment;
                     }
                  }
               }
            }

            if (closestSeat != null) {
               closestSeat.debug.previewEye(player, numTicks);
            }
         }
      }
   }

   public boolean isAutomatic() {
      return !this.attachment.getConfig().isNode("firstPersonViewPosition");
   }

   private void setAutomaticDisplayed(boolean automatic) {
      this.isLoadingWidgets = true;
      Iterator var2 = this.getWidgets().iterator();

      while(var2.hasNext()) {
         MapWidget widget = (MapWidget)var2.next();
         if (widget instanceof FirstPersonEyePositionDialog.SeatEyeNumberBox) {
            ((FirstPersonEyePositionDialog.SeatEyeNumberBox)widget).setAutomatic(automatic);
         }
      }

      this.isLoadingWidgets = false;
   }

   public <T> T getConfigValue(String key, T def) {
      ConfigurationNode config = this.attachment.getConfig();
      if (config.isNode("firstPersonViewPosition")) {
         config = config.getNode("firstPersonViewPosition");
         if (config.contains(key)) {
            return config.get(key, def);
         }
      }

      return def;
   }

   public void updateConfigValue(String key, Object value) {
      if (!this.isLoadingWidgets) {
         ConfigurationNode config = this.attachment.getConfig();
         if (config.isNode("firstPersonViewPosition")) {
            config = config.getNode("firstPersonViewPosition");
            config.set(key, value);
            this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", this.attachment);
         } else {
            config = config.getNode("firstPersonViewPosition");
            config.set("posX", 0.0D);
            config.set("posY", 0.0D);
            config.set("posZ", 0.0D);
            config.set("rotX", 0.0D);
            config.set("rotY", 0.0D);
            config.set("rotZ", 0.0D);
            config.set(key, value);
            this.setAutomaticDisplayed(false);
            this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
         }

         this.showArrowPreview(true);
      }
   }

   private void showArrowPreview(boolean show) {
      int numTicks = show ? 100 : 0;
      Iterator var3 = this.attachment.getAttachments().iterator();

      while(true) {
         Attachment liveAttachment;
         do {
            if (!var3.hasNext()) {
               return;
            }

            liveAttachment = (Attachment)var3.next();
         } while(!(liveAttachment instanceof CartAttachmentSeat));

         Iterator var5 = this.display.getOwners().iterator();

         while(var5.hasNext()) {
            Player player = (Player)var5.next();
            if (this.display.isControlling(player)) {
               ((CartAttachmentSeat)liveAttachment).debug.showEyeArrow(player, numTicks);
            }
         }
      }
   }

   private class SeatEyeNumberBox extends MapWidgetNumberBox {
      private final String configField;
      private final String acceptedPropertyName;

      public SeatEyeNumberBox(String configField, String acceptedPropertyName, double increment) {
         this.configField = configField;
         this.acceptedPropertyName = acceptedPropertyName;
         this.setIncrement(increment);
      }

      public String getAcceptedPropertyName() {
         return this.acceptedPropertyName;
      }

      public void onAttached() {
         super.onAttached();
         this.setAutomatic(FirstPersonEyePositionDialog.this.isAutomatic());
      }

      public void onValueChanged() {
         FirstPersonEyePositionDialog.this.updateConfigValue(this.configField, this.getValue());
      }

      public void setAutomatic(boolean automatic) {
         this.setTextOverride(automatic ? "Auto" : null);
         if (automatic) {
            this.setValue(0.0D);
         } else {
            this.setValue((Double)FirstPersonEyePositionDialog.this.getConfigValue(this.configField, 0.0D));
         }

      }
   }
}
