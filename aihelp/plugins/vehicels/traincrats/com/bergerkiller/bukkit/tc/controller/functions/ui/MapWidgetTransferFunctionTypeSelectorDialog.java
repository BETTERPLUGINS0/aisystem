package com.bergerkiller.bukkit.tc.controller.functions.ui;

import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetMenu;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetScroller;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunction;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionHost;
import java.util.Iterator;

public abstract class MapWidgetTransferFunctionTypeSelectorDialog extends MapWidgetMenu {
   private static final byte ITEM_BG_DEFAULT = MapColorPalette.getColor(199, 199, 199);
   private static final byte ITEM_BG_FOCUS = MapColorPalette.getColor(255, 252, 245);
   private static final int ROW_HEIGHT = 11;
   private final TransferFunctionHost host;

   public MapWidgetTransferFunctionTypeSelectorDialog(TransferFunctionHost host) {
      this.host = host;
      this.setBounds(20, 25, 88, 88);
      this.setPositionAbsolute(true);
      this.setBackgroundColor(MapColorPalette.getColor(164, 168, 184));
   }

   public abstract void onSelected(TransferFunction var1);

   public void onAttached() {
      ((<undefinedtype>)this.addWidget(new MapWidgetScroller() {
         public void onAttached() {
            int y = 0;
            boolean addedInput = false;
            Iterator var3 = MapWidgetTransferFunctionTypeSelectorDialog.this.host.getRegistry().all().iterator();

            while(true) {
               TransferFunction.Serializer serializer;
               while(true) {
                  do {
                     if (!var3.hasNext()) {
                        super.onAttached();
                        return;
                     }

                     serializer = (TransferFunction.Serializer)var3.next();
                  } while(!serializer.isListed(MapWidgetTransferFunctionTypeSelectorDialog.this.host));

                  if (!serializer.isInput()) {
                     break;
                  }

                  if (!addedInput) {
                     addedInput = true;
                     break;
                  }
               }

               this.addContainerWidget((MapWidgetTransferFunctionTypeSelectorDialog.this.new Item(serializer)).setBounds(0, y, this.getWidth(), 12));
               y += 11;
            }
         }
      })).setScrollPadding(5).setBounds(4, 4, this.getWidth() - 8, this.getHeight() - 8);
      super.onAttached();
   }

   private class Item extends MapWidget {
      private final TransferFunction.Serializer<?> serializer;

      public Item(TransferFunction.Serializer<?> serializer) {
         this.serializer = serializer;
         this.setFocusable(true);
      }

      public void onActivate() {
         MapWidgetTransferFunctionTypeSelectorDialog.this.close();
         MapWidgetTransferFunctionTypeSelectorDialog.this.onSelected(this.serializer.createNew(MapWidgetTransferFunctionTypeSelectorDialog.this.host));
      }

      public void onDraw() {
         this.view.drawRectangle(0, 0, this.getWidth(), this.getHeight(), (byte)119);
         this.view.fillRectangle(1, 1, this.getWidth() - 2, this.getHeight() - 2, this.isFocused() ? MapWidgetTransferFunctionTypeSelectorDialog.ITEM_BG_FOCUS : MapWidgetTransferFunctionTypeSelectorDialog.ITEM_BG_DEFAULT);
         this.view.draw(MapFont.MINECRAFT, 2, 2, (byte)(this.isFocused() ? 50 : 119), this.serializer.isInput() ? "Input" : this.serializer.title());
      }
   }
}
