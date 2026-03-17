package com.bergerkiller.bukkit.tc.attachments.control.seat;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentItem;
import com.bergerkiller.bukkit.tc.attachments.ui.ItemDropTarget;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.item.MapWidgetItemSelector;
import com.bergerkiller.bukkit.tc.attachments.ui.menus.PositionMenu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SeatDisplayedItemDialog extends MapWidgetMenu {
   MapWidget setItemButton;
   MapWidget positionButton;
   MapWidget showFPVButton;
   MapWidget disableButton;

   public SeatDisplayedItemDialog() {
      this.setBounds(17, 16, 84, 79);
      this.setBackgroundColor(MapColorPalette.getColor(16, 16, 128));
   }

   public void onAttached() {
      this.setItemButton = ((<undefinedtype>)this.addWidget(new MapWidgetButton() {
         public void onActivate() {
            ((<undefinedtype>)this.getParent().addWidget(new SeatDisplayedItemDialog.SelectItemDialog() {
               public void onDetached() {
                  super.onDetached();
                  if ((Boolean)this.attachment.getConfig().get("displayItem.enabled", false)) {
                     SeatDisplayedItemDialog.this.positionButton.setEnabled(true);
                     SeatDisplayedItemDialog.this.showFPVButton.setEnabled(true);
                     SeatDisplayedItemDialog.this.disableButton.setEnabled(true);
                  }

               }
            })).setAttachment(SeatDisplayedItemDialog.this.attachment);
         }
      })).setText("Set Item").setBounds(5, 5, 74, 15);
      this.positionButton = ((<undefinedtype>)this.addWidget(new MapWidgetButton() {
         public void onActivate() {
            ((SeatDisplayedItemDialog.PositionItemDialog)this.getParent().addWidget(new SeatDisplayedItemDialog.PositionItemDialog())).setAttachment(SeatDisplayedItemDialog.this.attachment);
         }
      })).setText("Position").setBounds(5, 23, 74, 15);
      this.showFPVButton = ((<undefinedtype>)this.addWidget(new MapWidgetButton() {
         public void onAttached() {
            this.updateText();
         }

         public void onActivate() {
            SeatDisplayedItemDialog.this.attachment.getConfig().set("displayItem.showFirstPerson", !(Boolean)SeatDisplayedItemDialog.this.attachment.getConfig().get("displayItem.showFirstPerson", false));
            this.updateText();
            this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
         }

         private void updateText() {
            this.setText((Boolean)SeatDisplayedItemDialog.this.attachment.getConfig().get("displayItem.showFirstPerson", false) ? "FPV: Visible" : "FPV: Hidden");
         }
      })).setBounds(5, 41, 74, 15);
      this.disableButton = ((<undefinedtype>)this.addWidget(new MapWidgetButton() {
         public void onActivate() {
            SeatDisplayedItemDialog.this.attachment.getConfig().set("displayItem.enabled", false);
            this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
            SeatDisplayedItemDialog.this.positionButton.setEnabled(false);
            SeatDisplayedItemDialog.this.showFPVButton.setEnabled(false);
            SeatDisplayedItemDialog.this.disableButton.setEnabled(false);
            SeatDisplayedItemDialog.this.setItemButton.focus();
            this.display.playSound(SoundEffect.EXTINGUISH);
         }
      })).setText("Disable").setBounds(5, 59, 74, 15);
      boolean isEnabled = (Boolean)this.attachment.getConfig().get("displayItem.enabled", false);
      this.positionButton.setEnabled(isEnabled);
      this.showFPVButton.setEnabled(isEnabled);
      this.disableButton.setEnabled(isEnabled);
      super.onAttached();
   }

   private static class SelectItemDialog extends MapWidgetMenu implements ItemDropTarget {
      private MapWidgetItemSelector selector;

      public SelectItemDialog() {
         this.setBounds(-13, -12, 111, 97);
         this.setBackgroundColor(MapColorPalette.getColor(0, 128, 200));
         this.setDepthOffset(1);
      }

      public void onAttached() {
         this.selector = (MapWidgetItemSelector)this.addWidget(new MapWidgetItemSelector() {
            public void onAttached() {
               super.onAttached();
               this.setSelectedItem((ItemStack)SelectItemDialog.this.attachment.getConfig().get("displayItem.item", new ItemStack(Material.PUMPKIN)));
            }

            public void onSelectedItemChanged() {
               boolean wasEnabled = (Boolean)SelectItemDialog.this.attachment.getConfig().get("displayItem.enabled", false);
               SelectItemDialog.this.attachment.getConfig().set("displayItem.item", this.getSelectedItem());
               SelectItemDialog.this.attachment.getConfig().set("displayItem.enabled", true);
               if (wasEnabled) {
                  this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed", SelectItemDialog.this.attachment);
               } else {
                  this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
               }

            }
         });
         this.selector.setPosition(5, 5);
         this.display.playSound(SoundEffect.PISTON_EXTEND);
         super.onAttached();
      }

      public void onDetached() {
         super.onDetached();
         this.display.playSound(SoundEffect.PISTON_CONTRACT);
      }

      public boolean acceptItem(ItemStack item) {
         this.selector.setSelectedItem(item);
         this.display.playSound(SoundEffect.CLICK_WOOD);
         return true;
      }
   }

   private static class PositionItemDialog extends PositionMenu {
      private PositionItemDialog() {
      }

      public ConfigurationNode getConfig() {
         return super.getConfig().getNode("displayItem");
      }

      protected AttachmentType getMenuAttachmentType() {
         return CartAttachmentItem.TYPE;
      }

      // $FF: synthetic method
      PositionItemDialog(Object x0) {
         this();
      }
   }
}
