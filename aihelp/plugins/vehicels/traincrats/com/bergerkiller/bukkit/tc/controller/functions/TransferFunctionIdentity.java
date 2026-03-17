package com.bergerkiller.bukkit.tc.controller.functions;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import java.util.function.BooleanSupplier;

class TransferFunctionIdentity implements TransferFunction {
   public static final TransferFunctionIdentity INSTANCE = new TransferFunctionIdentity();
   public static final TransferFunction.Serializer<TransferFunctionIdentity> SERIALIZER = new TransferFunction.Serializer<TransferFunctionIdentity>() {
      public String typeId() {
         return "IDENTITY";
      }

      public String title() {
         return "Identity";
      }

      public boolean isListed(TransferFunctionHost host) {
         return false;
      }

      public TransferFunctionIdentity createNew(TransferFunctionHost host) {
         return TransferFunctionIdentity.INSTANCE;
      }

      public TransferFunctionIdentity load(TransferFunctionHost host, ConfigurationNode config) {
         return TransferFunctionIdentity.INSTANCE;
      }

      public void save(TransferFunctionHost host, ConfigurationNode config, TransferFunctionIdentity function) {
      }
   };

   private TransferFunctionIdentity() {
   }

   public TransferFunction.Serializer<? extends TransferFunction> getSerializer() {
      return SERIALIZER;
   }

   public double map(double input) {
      return input;
   }

   public boolean isPure() {
      return true;
   }

   public boolean isBooleanOutput(BooleanSupplier isBooleanInput) {
      return isBooleanInput.getAsBoolean();
   }

   public TransferFunction clone() {
      return INSTANCE;
   }

   public void drawPreview(MapWidgetTransferFunctionItem widget, MapCanvas view) {
      view.draw(MapFont.MINECRAFT, 2, 3, (byte)50, "<Input>");
   }

   public void openDialog(TransferFunction.Dialog dialog) {
   }

   public TransferFunction.DialogMode openDialogMode() {
      return TransferFunction.DialogMode.NONE;
   }
}
