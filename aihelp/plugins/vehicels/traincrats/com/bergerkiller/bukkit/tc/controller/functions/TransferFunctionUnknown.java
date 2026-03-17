package com.bergerkiller.bukkit.tc.controller.functions;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import java.util.Collections;

class TransferFunctionUnknown implements TransferFunction {
   private final String typeId;
   private final ConfigurationNode config;
   private final boolean error;

   public TransferFunctionUnknown(String typeId, ConfigurationNode config, boolean error) {
      this.typeId = typeId;
      this.config = config.clone();
      this.error = error;
   }

   public TransferFunction.Serializer<? extends TransferFunction> getSerializer() {
      return new TransferFunction.Serializer<TransferFunctionUnknown>() {
         public String typeId() {
            return TransferFunctionUnknown.this.typeId;
         }

         public String title() {
            return (TransferFunctionUnknown.this.error ? "LOAD ERROR [" : "UNKNOWN [") + TransferFunctionUnknown.this.typeId + "]";
         }

         public TransferFunctionUnknown createNew(TransferFunctionHost host) {
            return TransferFunctionUnknown.this.clone();
         }

         public TransferFunctionUnknown load(TransferFunctionHost host, ConfigurationNode config) {
            return new TransferFunctionUnknown(this.typeId(), config, TransferFunctionUnknown.this.error);
         }

         public void save(TransferFunctionHost host, ConfigurationNode config, TransferFunctionUnknown function) {
            config.setToExcept(function.config.clone(), Collections.singleton("type"));
         }
      };
   }

   public double map(double input) {
      return input;
   }

   public boolean isPure() {
      return true;
   }

   public void drawPreview(MapWidgetTransferFunctionItem widget, MapCanvas view) {
      view.draw(MapFont.MINECRAFT, 2, 2, (byte)18, "Unknown [" + this.typeId + "]");
   }

   public void openDialog(TransferFunction.Dialog dialog) {
   }

   public TransferFunction.DialogMode openDialogMode() {
      return TransferFunction.DialogMode.NONE;
   }

   public TransferFunctionUnknown clone() {
      return new TransferFunctionUnknown(this.typeId, this.config, this.error);
   }
}
