package com.bergerkiller.bukkit.tc.controller.functions.ui.list;

import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetScroller;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionList;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionDialog;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import java.util.Iterator;
import java.util.Objects;

public class MapWidgetTransferFunctionList extends MapWidgetScroller {
   private final MapWidgetTransferFunctionDialog dialog;
   private final TransferFunctionList list;
   private int onOpenSelectedIndex = -1;

   public MapWidgetTransferFunctionList(MapWidgetTransferFunctionDialog dialog, TransferFunctionList list) {
      this.dialog = dialog;
      this.list = list;
      this.setBounds(5, 9, dialog.getWidth() - 10, dialog.getHeight() - 20);
      this.setScrollPadding(10);
   }

   public void onSelectedItemChanged() {
   }

   public int getSelectedItemIndex() {
      MapWidget w = this.display.getFocusedWidget();
      return w instanceof MapWidgetTransferFunctionListItem ? this.list.indexOf(((MapWidgetTransferFunctionListItem)w).getItem()) : -1;
   }

   public MapWidgetTransferFunctionList setSelectedItemIndex(int index) {
      this.onOpenSelectedIndex = index;
      return this;
   }

   public void onAttached() {
      int index = 0;
      Iterator var2 = this.list.getItems().iterator();

      while(var2.hasNext()) {
         TransferFunctionList.Item listItem = (TransferFunctionList.Item)var2.next();
         MapWidgetTransferFunctionListItem item = this.createItem(listItem);
         this.calcBounds(item, index++);
         this.addContainerWidget(item);
      }

      this.addInitialItemPlaceholder();
      if (this.onOpenSelectedIndex != -1 && this.onOpenSelectedIndex < this.getContainer().getWidgetCount()) {
         this.getContainer().getWidget(this.onOpenSelectedIndex).focus();
      }

      super.onAttached();
   }

   private MapWidgetTransferFunctionListItem createItem(TransferFunctionList.Item listItem) {
      MapWidgetTransferFunctionListItem item = new MapWidgetTransferFunctionListItem(this.dialog.getHost(), listItem, () -> {
         int index = this.list.indexOf(listItem);
         boolean var3;
         if (index != -1) {
            TransferFunctionList var10000 = this.list;
            int var10001 = index - 1;
            MapWidgetTransferFunctionDialog var10002 = this.dialog;
            Objects.requireNonNull(var10002);
            if (var10000.isBooleanOutput(var10001, var10002::isBooleanInput)) {
               var3 = true;
               return var3;
            }
         }

         var3 = false;
         return var3;
      }) {
         public void onMoveUp() {
            int currIndex = MapWidgetTransferFunctionList.this.list.indexOf(this.getItem());
            if (currIndex != -1 && currIndex > 0) {
               MapWidgetTransferFunctionList.this.list.remove(currIndex);
               MapWidgetTransferFunctionList.this.list.add(currIndex - 1, this.getItem());
               MapWidgetTransferFunctionList.this.recalcBounds();
               MapWidgetTransferFunctionList.this.dialog.markChanged();
            }

         }

         public void onMoveDown() {
            int currIndex = MapWidgetTransferFunctionList.this.list.indexOf(this.getItem());
            if (currIndex != -1 && currIndex < MapWidgetTransferFunctionList.this.list.size() - 1) {
               MapWidgetTransferFunctionList.this.list.remove(currIndex);
               MapWidgetTransferFunctionList.this.list.add(currIndex + 1, this.getItem());
               MapWidgetTransferFunctionList.this.recalcBounds();
               MapWidgetTransferFunctionList.this.dialog.markChanged();
            }

         }

         public void onFunctionModeChanged(TransferFunctionList.Item oldItem, TransferFunctionList.Item newItem) {
            int index = MapWidgetTransferFunctionList.this.list.indexOf(oldItem);
            if (index != -1) {
               MapWidgetTransferFunctionList.this.list.set(index, newItem);
               MapWidgetTransferFunctionList.this.dialog.markChanged();
            }

         }

         public void onFocus() {
            super.onFocus();
            MapWidgetTransferFunctionList.this.onSelectedItemChanged();
         }
      };
      MapWidgetTransferFunctionItem.ButtonIcon var10001 = MapWidgetTransferFunctionItem.ButtonIcon.CONFIGURE;
      Objects.requireNonNull(item);
      MapWidgetTransferFunctionItem var10000 = item.addButton(var10001, item::configure);
      var10001 = MapWidgetTransferFunctionItem.ButtonIcon.MOVE;
      Objects.requireNonNull(item);
      var10000.addButton(var10001, item::startMove).addButton(MapWidgetTransferFunctionItem.ButtonIcon.ADD, () -> {
         this.addNewItem(this.list.indexOf(item.getItem()));
      }).addButton(MapWidgetTransferFunctionItem.ButtonIcon.REMOVE, () -> {
         int itemIndex = this.list.indexOf(item.getItem());
         if (itemIndex != -1) {
            this.list.remove(itemIndex);
            this.getContainer().removeWidget(item);
            this.addInitialItemPlaceholder();
            this.recalcBounds();
            this.dialog.markChanged();
            if (itemIndex >= this.list.size()) {
               itemIndex = this.list.size() - 1;
            }

            boolean found = false;
            if (itemIndex >= 0) {
               TransferFunctionList.Item newSelListItem = this.list.get(itemIndex);
               Iterator var5 = this.getContainer().getWidgets().iterator();

               while(var5.hasNext()) {
                  MapWidget w = (MapWidget)var5.next();
                  if (((MapWidgetTransferFunctionListItem)w).getItem() == newSelListItem) {
                     w.focus();
                     found = true;
                     break;
                  }
               }
            }

            if (!found && this.getContainer().getWidgetCount() > 0) {
               this.getContainer().getWidget(0).focus();
            }
         }

      });
      return item;
   }

   private void addNewItem(int index) {
      this.dialog.createNew((newFunction) -> {
         int newItemIndex = index;
         if (index == -1) {
            newItemIndex = this.list.size();
         } else if (index < this.list.size()) {
            newItemIndex = index + 1;
         }

         if (this.list.isEmpty()) {
            this.getContainer().clearWidgets();
         }

         TransferFunctionList.Item newListItem = new TransferFunctionList.Item(TransferFunctionList.FunctionMode.ASSIGN, newFunction);
         this.list.add(newItemIndex, newListItem);
         MapWidgetTransferFunctionListItem newItem = (MapWidgetTransferFunctionListItem)this.addContainerWidget(this.createItem(newListItem));
         this.recalcBounds();
         newItem.focus();
         this.dialog.markChanged();
      });
   }

   private void addInitialItemPlaceholder() {
      if (this.list.isEmpty()) {
         this.getContainer().clearWidgets();
         this.addContainerWidget((new MapWidgetButton() {
            public void onActivate() {
               MapWidgetTransferFunctionList.this.addNewItem(0);
            }
         }).setText("Set Function").setBounds(0, 0, this.getWidth(), 13)).focus();
      }

   }

   private void recalcBounds() {
      if (!this.list.isEmpty()) {
         Iterator var1 = this.getContainer().getWidgets().iterator();

         while(var1.hasNext()) {
            MapWidget w = (MapWidget)var1.next();
            MapWidgetTransferFunctionListItem item = (MapWidgetTransferFunctionListItem)w;
            this.calcBounds(w, this.list.indexOf(item.getItem()));
         }
      }

      super.recalculateContainerSize();
   }

   private void calcBounds(MapWidget widget, int index) {
      if (index == -1) {
         throw new IllegalArgumentException("Index is -1");
      } else {
         widget.setBounds(0, 14 * index, this.getWidth(), 15);
      }
   }
}
