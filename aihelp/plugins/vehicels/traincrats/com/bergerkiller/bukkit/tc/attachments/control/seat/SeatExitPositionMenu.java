package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentAnchor;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.attachments.ui.AttachmentEditor;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSelectionBox;
import java.util.Iterator;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SeatExitPositionMenu extends MapWidgetMenu {
   private SeatExitPositionMenu.SeatMapWidgetNumberBox _positionX;
   private SeatExitPositionMenu.SeatMapWidgetNumberBox _positionY;
   private SeatExitPositionMenu.SeatMapWidgetNumberBox _positionZ;
   private SeatExitPositionMenu.SeatMapWidgetNumberBox _rotationX;
   private SeatExitPositionMenu.SeatMapWidgetNumberBox _rotationY;
   private SeatExitPositionMenu.SeatMapWidgetNumberBox _rotationZ;

   public SeatExitPositionMenu() {
      this.setBounds(5, 3, 108, 98);
      this.setBackgroundColor((byte)30);
      this.getTitle().setText("Seat Exit Position");
      this.getTitle().setColor(MapColorPalette.getSpecular((byte)30, 1.7F));
   }

   public void onAttached() {
      super.onAttached();
      int slider_width = 74;
      int y_offset = 12;
      int y_step = 12;
      ((<undefinedtype>)this.addWidget(new MapWidgetSelectionBox() {
         public void onAttached() {
            super.onAttached();
            Iterator var1 = AttachmentAnchor.values().iterator();

            while(var1.hasNext()) {
               AttachmentAnchor type = (AttachmentAnchor)var1.next();
               this.addItem(type.toString());
            }

            this.setSelectedItem((String)SeatExitPositionMenu.this.getConfig().get("anchor", AttachmentAnchor.DEFAULT.getName()));
         }

         public void onSelectedItemChanged() {
            SeatExitPositionMenu.this.getConfig().set("anchor", this.getSelectedItem());
            this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", SeatExitPositionMenu.this.attachment);
            SeatExitPositionMenu.this.previewEjectPosition();
         }
      })).setBounds(30, y_offset, slider_width, 11);
      this.addLabel(5, y_offset + 3, "Anchor");
      int y_offset = y_offset + y_step;
      this._positionX = (SeatExitPositionMenu.SeatMapWidgetNumberBox)this.addWidget(new SeatExitPositionMenu.SeatMapWidgetNumberBox(this, "posX"));
      this._positionX.setBounds(30, y_offset, slider_width, 11);
      this.addLabel(5, y_offset + 3, "Pos.X");
      y_offset += y_step;
      this._positionY = (SeatExitPositionMenu.SeatMapWidgetNumberBox)this.addWidget(new SeatExitPositionMenu.SeatMapWidgetNumberBox(this, "posY"));
      this._positionY.setBounds(30, y_offset, slider_width, 11);
      this.addLabel(5, y_offset + 3, "Pos.Y");
      y_offset += y_step;
      this._positionZ = (SeatExitPositionMenu.SeatMapWidgetNumberBox)this.addWidget(new SeatExitPositionMenu.SeatMapWidgetNumberBox(this, "posZ"));
      this._positionZ.setBounds(30, y_offset, slider_width, 11);
      this.addLabel(5, y_offset + 3, "Pos.Z");
      y_offset += y_step;
      this._rotationX = (SeatExitPositionMenu.SeatMapWidgetNumberBox)this.addWidget(new SeatExitPositionMenu.SeatMapWidgetNumberBox(this, "rotX") {
         public void onActivate() {
            SeatExitPositionMenu.this.setRotationLocked(!SeatExitPositionMenu.this.isRotationLocked());
            this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", SeatExitPositionMenu.this.attachment);
         }

         public void onValueChangeStart() {
            SeatExitPositionMenu.this.setRotationLocked(true);
         }
      });
      this._rotationX.setIncrement(0.1D);
      this._rotationX.setBounds(30, y_offset, slider_width, 11);
      this.addLabel(5, y_offset + 3, "Pitch");
      y_offset += y_step;
      this._rotationY = (SeatExitPositionMenu.SeatMapWidgetNumberBox)this.addWidget(new SeatExitPositionMenu.SeatMapWidgetNumberBox(this, "rotY") {
         public void onActivate() {
            SeatExitPositionMenu.this.setRotationLocked(!SeatExitPositionMenu.this.isRotationLocked());
            this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", SeatExitPositionMenu.this.attachment);
         }

         public void onValueChangeStart() {
            SeatExitPositionMenu.this.setRotationLocked(true);
         }
      });
      this._rotationY.setIncrement(0.1D);
      this._rotationY.setBounds(30, y_offset, slider_width, 11);
      this.addLabel(5, y_offset + 3, "Yaw");
      y_offset += y_step;
      this._rotationZ = (SeatExitPositionMenu.SeatMapWidgetNumberBox)this.addWidget(new SeatExitPositionMenu.SeatMapWidgetNumberBox(this, "rotZ") {
         public void onActivate() {
            SeatExitPositionMenu.this.setRotationLocked(!SeatExitPositionMenu.this.isRotationLocked());
            this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", SeatExitPositionMenu.this.attachment);
         }

         public void onValueChangeStart() {
            SeatExitPositionMenu.this.setRotationLocked(true);
         }
      });
      this._rotationZ.setIncrement(0.1D);
      this._rotationZ.setBounds(30, y_offset, slider_width, 11);
      this.addLabel(5, y_offset + 3, "Roll");
      int var10000 = y_offset + y_step;
      this.refreshRotationLocked();
   }

   public boolean isRotationLocked() {
      return (Boolean)this.getConfig().get("lockRotation", false);
   }

   public void setRotationLocked(boolean locked) {
      this.getConfig().set("lockRotation", locked);
      this.refreshRotationLocked();
   }

   public void refreshRotationLocked() {
      String overrideStr = this.isRotationLocked() ? null : "FREE";
      this._rotationX.setTextOverride(overrideStr);
      this._rotationY.setTextOverride(overrideStr);
      this._rotationZ.setTextOverride(overrideStr);
   }

   public ConfigurationNode getConfig() {
      return this.attachment.getConfig().getNode("ejectPosition");
   }

   private void previewEjectPosition() {
      Iterator var1 = this.attachment.getAttachments().iterator();

      while(true) {
         Attachment attachment;
         do {
            if (!var1.hasNext()) {
               return;
            }

            attachment = (Attachment)var1.next();
         } while(!(attachment instanceof CartAttachmentSeat));

         CartAttachmentSeat seat = (CartAttachmentSeat)attachment;
         Iterator var4 = ((AttachmentEditor)this.display).getViewers().iterator();

         while(var4.hasNext()) {
            Player viewer = (Player)var4.next();
            Location ejectPos = ((CartAttachmentSeat)attachment).getEjectPosition(viewer);
            PlayerUtil.spawnDustParticles(viewer, ejectPos.toVector(), Color.BLUE);
         }
      }
   }

   private class SeatMapWidgetNumberBox extends MapWidgetNumberBox {
      private final String field;
      private boolean ignoreValueChange = true;

      public SeatMapWidgetNumberBox(SeatExitPositionMenu menu, String field) {
         this.field = field;
      }

      public void onAttached() {
         super.onAttached();
         this.setValue((Double)SeatExitPositionMenu.this.getConfig().get(this.field, 0.0D));
         this.ignoreValueChange = false;
      }

      public void onValueChanged() {
         if (!this.ignoreValueChange) {
            SeatExitPositionMenu.this.getConfig().set(this.field, this.getValue());
            this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", SeatExitPositionMenu.this.attachment);
            if (this.getChangeRepeat() <= 1) {
               this.onValueChangeStart();
            }

            SeatExitPositionMenu.this.previewEjectPosition();
         }
      }

      public void onValueChangeStart() {
      }

      public void onValueChangeEnd() {
      }
   }
}
