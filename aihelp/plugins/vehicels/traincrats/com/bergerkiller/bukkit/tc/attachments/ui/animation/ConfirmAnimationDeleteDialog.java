package com.bergerkiller.bukkit.tc.attachments.ui.animation;

import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;

public class ConfirmAnimationDeleteDialog extends MapWidgetMenu {
   public ConfirmAnimationDeleteDialog() {
      this.setBounds(10, 40, 95, 58);
      this.setBackgroundColor(MapColorPalette.getColor(135, 33, 33));
   }

   public void onAttached() {
      super.onAttached();
      this.addWidget((new MapWidgetText()).setText("Are you sure you\nwant to delete\nthis animation?").setBounds(5, 5, 80, 30));
      this.addWidget((new MapWidgetButton() {
         public void onActivate() {
            ConfirmAnimationDeleteDialog.this.close();
         }
      }).setText("No").setBounds(10, 40, 35, 13));
      this.addWidget((new MapWidgetButton() {
         public void onActivate() {
            ConfirmAnimationDeleteDialog.this.close();
            ConfirmAnimationDeleteDialog.this.onConfirmDelete();
         }
      }).setText("Yes").setBounds(50, 40, 35, 13));
   }

   public void onConfirmDelete() {
   }
}
