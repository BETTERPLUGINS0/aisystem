package com.bergerkiller.bukkit.tc.attachments.ui.item;

import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.wrappers.Brightness;
import com.bergerkiller.bukkit.tc.attachments.VirtualDisplayEntity;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;

public abstract class MapWidgetBrightnessDialog extends MapWidgetMenu {
   private boolean disabled = true;
   private final MapWidgetNumberBox blockLight = new MapWidgetNumberBox() {
      public void onValueChanged() {
         MapWidgetBrightnessDialog.this.updateDisabled(false);
         MapWidgetBrightnessDialog.this.onBrightnessChanged();
      }
   };
   private final MapWidgetNumberBox skyLight = new MapWidgetNumberBox() {
      public void onValueChanged() {
         MapWidgetBrightnessDialog.this.updateDisabled(false);
         MapWidgetBrightnessDialog.this.onBrightnessChanged();
      }
   };
   private final MapWidgetButton disabledButton = new MapWidgetButton() {
      public void onActivate() {
         MapWidgetBrightnessDialog.this.updateDisabled(!MapWidgetBrightnessDialog.this.disabled);
         MapWidgetBrightnessDialog.this.onBrightnessChanged();
         MapWidgetBrightnessDialog.this.blockLight.focus();
      }
   };

   public MapWidgetBrightnessDialog() {
      this.setRetainChildWidgets(true);
      this.setSize(74, 73);
      this.blockLight.setRange(0.0D, 15.0D);
      this.blockLight.setIncrement(1.0D);
      this.blockLight.setTextOverride("Default");
      this.addLabel(16, 5, "Block Light");
      this.addWidget(this.blockLight.setBounds(7, 12, 60, 13));
      this.skyLight.setRange(0.0D, 15.0D);
      this.skyLight.setIncrement(1.0D);
      this.skyLight.setTextOverride("Default");
      this.addLabel(19, 28, "Sky Light");
      this.addWidget(this.skyLight.setBounds(7, 35, 60, 13));
      this.disabledButton.setText("Default");
      this.disabledButton.setEnabled(false);
      this.disabledButton.setBounds(14, 53, 46, 12);
      this.addWidget(this.disabledButton);
   }

   public void setBrightness(Brightness brightness) {
      if (brightness == Brightness.UNSET) {
         if (!this.disabled) {
            this.updateDisabled(true);
            this.blockLight.setInitialValue(0.0D);
            this.skyLight.setInitialValue(0.0D);
         }
      } else {
         this.updateDisabled(false);
         this.blockLight.setInitialValue((double)brightness.blockLight());
         this.skyLight.setInitialValue((double)brightness.skyLight());
      }

   }

   private void updateDisabled(boolean disabled) {
      if (this.disabled != disabled) {
         this.disabled = disabled;
         this.disabledButton.setEnabled(!disabled);
         if (disabled) {
            this.blockLight.setTextOverride("Default");
            this.skyLight.setTextOverride("Default");
         } else {
            this.blockLight.setTextOverride((String)null);
            this.skyLight.setTextOverride((String)null);
         }
      }

   }

   public int getBlockLight() {
      return this.disabled ? -1 : (int)this.blockLight.getValue();
   }

   public int getSkyLight() {
      return this.disabled ? -1 : (int)this.skyLight.getValue();
   }

   public Brightness getBrightness() {
      return this.disabled ? Brightness.UNSET : Brightness.blockAndSkyLight((int)this.blockLight.getValue(), (int)this.skyLight.getValue());
   }

   public abstract void onBrightnessChanged();

   public static class AttachmentBrightnessDialog extends MapWidgetBrightnessDialog {
      public AttachmentBrightnessDialog(MapWidgetAttachmentNode attachment) {
         this.setAttachment(attachment);
      }

      public void onAttached() {
         this.setBrightness(VirtualDisplayEntity.loadBrightnessFromConfig(this.attachment.getConfig()));
      }

      public void onBrightnessChanged() {
         VirtualDisplayEntity.saveBrightnessToConfig(this.attachment.getConfig(), this.getBrightness());
      }
   }
}
