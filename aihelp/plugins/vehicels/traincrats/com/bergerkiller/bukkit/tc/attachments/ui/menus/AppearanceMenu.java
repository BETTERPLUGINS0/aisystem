package com.bergerkiller.bukkit.tc.attachments.ui.menus;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapEventPropagation;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetTabView.Tab;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentType;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentTypeRegistry;
import com.bergerkiller.bukkit.tc.attachments.ui.ItemDropTarget;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentNode;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSelectionBox;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AppearanceMenu extends MapWidgetMenu implements ItemDropTarget {
   private final MapWidgetTabView tabView = new MapWidgetTabView();
   private AttachmentTypeRegistry typeRegistry;
   private List<AppearanceMenu.TypePage> pages;

   public AppearanceMenu() {
      this.setBounds(5, 15, 118, 104);
      this.setBackgroundColor((byte)50);
   }

   public void onAttached() {
      super.onAttached();
      this.typeRegistry = AttachmentTypeRegistry.instance();
      List<AttachmentType> types = this.typeRegistry.all();
      this.pages = new ArrayList(types.size());
      Iterator var2 = types.iterator();

      while(var2.hasNext()) {
         AttachmentType type = (AttachmentType)var2.next();
         boolean listed = true;
         Iterator var5 = this.display.getOwners().iterator();

         while(var5.hasNext()) {
            Player player = (Player)var5.next();
            if (!type.isListed(player)) {
               listed = false;
               break;
            }
         }

         if (listed) {
            this.pages.add(new AppearanceMenu.TypePage(type, this.tabView.addTab()));
         }
      }

      this.tabView.setPosition(9, 16);
      this.addWidget(this.tabView);
      MapWidgetSelectionBox typeSelectionBox = (MapWidgetSelectionBox)this.addWidget(new MapWidgetSelectionBox() {
         public void onSelectedItemChanged() {
            int index = this.getSelectedIndex();
            if (index >= 0 && index < AppearanceMenu.this.pages.size()) {
               AppearanceMenu.this.setPage((AppearanceMenu.TypePage)AppearanceMenu.this.pages.get(index));
            }

         }
      });
      AttachmentType selected = this.typeRegistry.fromConfig(this.getAttachment().getConfig());
      Iterator var9 = this.pages.iterator();

      while(var9.hasNext()) {
         AppearanceMenu.TypePage page = (AppearanceMenu.TypePage)var9.next();
         typeSelectionBox.addItem(page.type.getName());
         if (selected != null && selected.getID().equalsIgnoreCase(page.type.getID())) {
            typeSelectionBox.setSelectedIndex(typeSelectionBox.getItemCount() - 1);
         }
      }

      typeSelectionBox.setBounds(9, 3, 100, 11);
      this.setType(selected);
      typeSelectionBox.focus();
   }

   public void setType(AttachmentType type) {
      Iterator var2 = this.pages.iterator();

      AppearanceMenu.TypePage page;
      do {
         if (!var2.hasNext()) {
            this.setPage((AppearanceMenu.TypePage)this.pages.get(0));
            return;
         }

         page = (AppearanceMenu.TypePage)var2.next();
      } while(page.type != type);

      this.setPage(page);
   }

   private void setPage(AppearanceMenu.TypePage page) {
      if (this.typeRegistry.fromConfig(this.getAttachment().getConfig()) != page.type) {
         this.typeRegistry.toConfig(this.getAttachment().getConfig(), page.type);
         this.sendStatusChange(MapEventPropagation.DOWNSTREAM, "changed");
         this.getAttachment().resetIcon();
      }

      if (!page.appearanceCreated) {
         page.appearanceCreated = true;

         try {
            page.type.migrateConfiguration(this.attachment.getConfig());
         } catch (Throwable var4) {
            TrainCarts.plugin.getLogger().log(Level.SEVERE, "Failed to migrate attachment configuration of " + page.type.getName(), var4);
         }

         try {
            page.type.createAppearanceTab(page.tab, this.attachment);
         } catch (Throwable var3) {
            TrainCarts.plugin.getLogger().log(Level.SEVERE, "Failed to display appearance tab for " + page.type.getName(), var3);
            page.tab.clear();
            ((MapWidgetText)page.tab.addWidget(new MapWidgetText())).setText("An error occurred!").setColor((byte)18).setPosition(5, 5);
         }
      }

      page.tab.select();
   }

   public boolean acceptItem(ItemStack item) {
      Iterator var2 = this.tabView.getSelectedTab().getWidgets().iterator();

      MapWidget widget;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         widget = (MapWidget)var2.next();
      } while(!(widget instanceof ItemDropTarget) || !((ItemDropTarget)widget).acceptItem(item));

      return true;
   }

   public ConfigurationNode getConfig() {
      return this.attachment.getConfig();
   }

   public MapWidgetAttachmentNode getAttachment() {
      return this.attachment;
   }

   private static class TypePage {
      public final AttachmentType type;
      public final Tab tab;
      public boolean appearanceCreated;

      public TypePage(AttachmentType type, Tab tab) {
         this.type = type;
         this.tab = tab;
         this.appearanceCreated = false;
      }
   }
}
