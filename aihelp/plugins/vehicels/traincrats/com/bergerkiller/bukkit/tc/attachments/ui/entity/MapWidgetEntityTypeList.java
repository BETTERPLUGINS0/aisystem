package com.bergerkiller.bukkit.tc.attachments.ui.entity;

import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentEntity;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSelectionBox;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.bukkit.entity.EntityType;

public class MapWidgetEntityTypeList extends MapWidget {
   private final MapWidgetSelectionBox selector = new MapWidgetSelectionBox() {
      public void onSelectedItemChanged() {
         MapWidgetEntityTypeList.this.onEntityTypeChanged();
      }
   };

   public void onAttached() {
      this.selector.clearItems();
      ArrayList<String> items = new ArrayList();
      EntityType[] var2 = EntityType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EntityType type = var2[var4];
         if (CartAttachmentEntity.isEntityTypeSupported(type)) {
            items.add(type.toString());
         }
      }

      Collections.sort(items);
      Iterator var6 = items.iterator();

      while(var6.hasNext()) {
         String item = (String)var6.next();
         this.selector.addItem(item);
      }

      this.addWidget(this.selector);
   }

   public void onBoundsChanged() {
      this.selector.setBounds(0, 0, this.getWidth(), this.getHeight());
   }

   public EntityType getEntityType() {
      return (EntityType)ParseUtil.parseEnum(this.selector.getSelectedItem(), EntityType.MINECART);
   }

   public void setEntityType(EntityType entityType) {
      this.selector.setSelectedItem(entityType.toString());
   }

   public void onEntityTypeChanged() {
   }
}
