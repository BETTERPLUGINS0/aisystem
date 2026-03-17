package com.bergerkiller.bukkit.tc.controller.functions.inputs;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentSelection;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentSelector;
import com.bergerkiller.bukkit.tc.attachments.control.CartAttachmentSeat;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetAttachmentSelector;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunction;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionHost;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TransferFunctionInputSeatOccupied extends TransferFunctionInput {
   public static final TransferFunction.Serializer<TransferFunctionInputSeatOccupied> SERIALIZER = new TransferFunction.Serializer<TransferFunctionInputSeatOccupied>() {
      public String typeId() {
         return "INPUT-SEAT-OCCUPIED";
      }

      public String title() {
         return "In: Seat Occupied";
      }

      public boolean isInput() {
         return true;
      }

      public boolean isListed(TransferFunctionHost host) {
         return host.isAttachment();
      }

      public TransferFunctionInputSeatOccupied createNew(TransferFunctionHost host) {
         TransferFunctionInputSeatOccupied function = new TransferFunctionInputSeatOccupied();
         function.updateSource(host);
         return function;
      }

      public TransferFunctionInputSeatOccupied load(TransferFunctionHost host, ConfigurationNode config) {
         TransferFunctionInputSeatOccupied function = new TransferFunctionInputSeatOccupied();
         function.setSeatSelector(AttachmentSelector.readFromConfig(config, "seat").withType(CartAttachmentSeat.class));
         function.updateSource(host);
         return function;
      }

      public void save(TransferFunctionHost host, ConfigurationNode config, TransferFunctionInputSeatOccupied function) {
         function.getSeatSelector().writeToConfig(config, "seat");
      }
   };
   private AttachmentSelector<CartAttachmentSeat> seatSelector = AttachmentSelector.all(CartAttachmentSeat.class);

   public void setNameFilter(String name) {
      this.seatSelector = this.seatSelector.withName(name);
   }

   public void setSeatSelector(AttachmentSelector<CartAttachmentSeat> selector) {
      this.seatSelector = selector;
   }

   public AttachmentSelector<CartAttachmentSeat> getSeatSelector() {
      return this.seatSelector;
   }

   public TransferFunction.Serializer<? extends TransferFunction> getSerializer() {
      return SERIALIZER;
   }

   public TransferFunctionInput.ReferencedSource createSource(TransferFunctionHost host) {
      Attachment attachment = host.getAttachment();
      return (TransferFunctionInput.ReferencedSource)(attachment != null ? new TransferFunctionInputSeatOccupied.SeatOccupiedReferencedSource(attachment.getSelection(this.seatSelector)) : TransferFunctionInput.ReferencedSource.NONE);
   }

   protected TransferFunctionInput cloneInput() {
      return new TransferFunctionInputSpeed();
   }

   public boolean isBooleanOutput() {
      return true;
   }

   public void drawPreview(MapWidgetTransferFunctionItem widget, MapCanvas view) {
      view.draw(MapFont.MINECRAFT, 0, 3, (byte)30, "<Seat Occupied>");
   }

   public void openDialog(final TransferFunction.Dialog dialog) {
      super.openDialog(dialog);
      dialog.addLabel(29, 21, (byte)18, "Monitored Seats");
      ((<undefinedtype>)dialog.addWidget(new TransferFunctionInputSeatOccupied.SeatNameWidget() {
         public void onChanged() {
            dialog.markChanged();
         }

         public List<String> getSeatNames(AttachmentSelector<CartAttachmentSeat> allSelector) {
            Attachment attachment = dialog.getHost().getAttachment();
            return attachment != null ? attachment.getSelection(allSelector).names() : Collections.emptyList();
         }
      })).setBounds(11, 27, 92, 13);
   }

   private static class SeatOccupiedReferencedSource extends TransferFunctionInput.ReferencedSource {
      private final AttachmentSelection<CartAttachmentSeat> seatSelection;

      public SeatOccupiedReferencedSource(AttachmentSelection<CartAttachmentSeat> seatSelection) {
         this.seatSelection = seatSelection;
      }

      public void onTick() {
         this.seatSelection.sync();
         double result = 0.0D;
         Iterator var3 = this.seatSelection.iterator();

         while(var3.hasNext()) {
            CartAttachmentSeat seat = (CartAttachmentSeat)var3.next();
            if (seat.getEntity() != null) {
               result = 1.0D;
               break;
            }
         }

         this.value = result;
      }

      public boolean equals(Object o) {
         if (o instanceof TransferFunctionInputSeatOccupied.SeatOccupiedReferencedSource) {
            TransferFunctionInputSeatOccupied.SeatOccupiedReferencedSource other = (TransferFunctionInputSeatOccupied.SeatOccupiedReferencedSource)o;
            return this.seatSelection.selector().equals(other.seatSelection.selector());
         } else {
            return false;
         }
      }
   }

   private abstract class SeatNameWidget extends MapWidget {
      private final byte COLOR_BG_DEFAULT = MapColorPalette.getColor(199, 199, 199);
      private final byte COLOR_BG_FOCUSED = MapColorPalette.getColor(255, 252, 245);

      public SeatNameWidget() {
         this.setFocusable(true);
      }

      public abstract void onChanged();

      public abstract List<String> getSeatNames(AttachmentSelector<CartAttachmentSeat> var1);

      public void onActivate() {
         this.getParent().addWidget((new MapWidgetAttachmentSelector<CartAttachmentSeat>(TransferFunctionInputSeatOccupied.this.getSeatSelector()) {
            public List<String> getAttachmentNames(AttachmentSelector<CartAttachmentSeat> allSelector) {
               return SeatNameWidget.this.getSeatNames(allSelector);
            }

            public void onSelected(AttachmentSelector<CartAttachmentSeat> selection) {
               TransferFunctionInputSeatOccupied.this.setSeatSelector(selection);
               SeatNameWidget.this.onChanged();
            }
         }).setTitle("Set Seat name").includeAny("<Any Seat>"));
      }

      public void onDraw() {
         this.view.drawRectangle(0, 0, this.getWidth(), this.getHeight(), (byte)119);
         this.view.fillRectangle(1, 1, this.getWidth() - 2, this.getHeight() - 2, this.isFocused() ? this.COLOR_BG_FOCUSED : this.COLOR_BG_DEFAULT);
         String text;
         int textColor;
         if (TransferFunctionInputSeatOccupied.this.seatSelector.nameFilter().isPresent()) {
            text = (String)TransferFunctionInputSeatOccupied.this.seatSelector.nameFilter().get();
            textColor = this.isFocused() ? 50 : 119;
         } else {
            text = "<Any Seat>";
            textColor = MapColorPalette.getColor(128, 128, 128);
         }

         int textWidth = (int)this.view.calcFontSize(MapFont.MINECRAFT, text).getWidth();
         this.view.draw(MapFont.MINECRAFT, (this.getWidth() - textWidth + 1) / 2, 3, (byte)textColor, text);
      }
   }
}
