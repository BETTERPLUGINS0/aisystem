package com.bergerkiller.bukkit.tc.attachments.ui.menus.general;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNameSet;
import java.util.List;

public class NameAttachmentDialog extends MapWidgetMenu {
   public NameAttachmentDialog(MapWidgetAttachmentNode attachment) {
      this.attachment = attachment;
      this.setBounds(5, 8, 108, 89);
      this.setBackgroundColor(MapColorPalette.getColor(53, 33, 167));
   }

   public void onAttached() {
      List<String> setNames = this.attachment.getConfig().getList("names", String.class);
      ((<undefinedtype>)this.addWidget(new MapWidgetNameSet() {
         public void onItemAdded(String item) {
            NameAttachmentDialog.this.attachment.getConfig().getList("names", String.class).add(item);
         }

         public void onItemRemoved(String item) {
            List<String> names = NameAttachmentDialog.this.attachment.getConfig().getList("names", String.class);
            if (names.remove(item) && names.isEmpty()) {
               NameAttachmentDialog.this.attachment.getConfig().remove("names");
            }

         }

         public void onKeyPressed(MapKeyEvent event) {
            if (event.getKey() == Key.BACK && this.isActivated()) {
               NameAttachmentDialog.this.close();
            } else {
               super.onKeyPressed(event);
            }

         }
      })).setNewItemText("+++ New Name +++").setNewItemDescription("Add a new name").setItems(this.attachment.getConfig().getList("names", String.class)).setBounds(5, 5, this.getWidth() - 10, this.getHeight() - 10).activate();
   }
}
