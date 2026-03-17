package com.bergerkiller.bukkit.tc.controller.functions;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetNumberBox;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import java.text.NumberFormat;

public final class TransferFunctionConstant implements TransferFunction {
   public static final TransferFunction.Serializer<TransferFunctionConstant> SERIALIZER = new TransferFunction.Serializer<TransferFunctionConstant>() {
      public String typeId() {
         return "CONSTANT";
      }

      public String title() {
         return "Constant";
      }

      public TransferFunctionConstant createNew(TransferFunctionHost host) {
         return TransferFunctionConstant.zero();
      }

      public TransferFunctionConstant load(TransferFunctionHost host, ConfigurationNode config) {
         double output = (Double)config.getOrDefault("output", 0.0D);
         return new TransferFunctionConstant(output);
      }

      public void save(TransferFunctionHost host, ConfigurationNode config, TransferFunctionConstant function) {
         config.set("output", function.output);
      }
   };
   private static final NumberFormat PREVIEW_NUM_FORMAT = Util.createNumberFormat(1, 5);
   private double output;

   public static TransferFunctionConstant zero() {
      return new TransferFunctionConstant(0.0D);
   }

   public static TransferFunctionConstant of(double output) {
      return new TransferFunctionConstant(output);
   }

   private TransferFunctionConstant(double output) {
      this.output = output;
   }

   public TransferFunction.Serializer<? extends TransferFunction> getSerializer() {
      return SERIALIZER;
   }

   public void setOutput(double output) {
      this.output = output;
   }

   public double getOutput() {
      return this.output;
   }

   public double map(double input) {
      return this.output;
   }

   public boolean isPure() {
      return true;
   }

   public TransferFunction clone() {
      return new TransferFunctionConstant(this.output);
   }

   public void drawPreview(MapWidgetTransferFunctionItem widget, MapCanvas view) {
      view.draw(MapFont.MINECRAFT, 0, 3, widget.defaultColor((byte)30), PREVIEW_NUM_FORMAT.format(this.output));
   }

   public void openDialog(final TransferFunction.Dialog dialog) {
      ((<undefinedtype>)dialog.addWidget(new MapWidgetNumberBox() {
         public void onAttached() {
            this.setInitialValue(TransferFunctionConstant.this.output);
            this.setIncrement(0.001D);
            super.onAttached();
         }

         public void onValueChanged() {
            TransferFunctionConstant.this.output = this.getValue();
            dialog.markChanged();
         }
      })).setBounds(8, 1, dialog.getWidth() - 16, dialog.getHeight() - 2);
   }

   public TransferFunction.DialogMode openDialogMode() {
      return TransferFunction.DialogMode.INLINE;
   }

   // $FF: synthetic method
   TransferFunctionConstant(double x0, Object x1) {
      this(x0);
   }
}
