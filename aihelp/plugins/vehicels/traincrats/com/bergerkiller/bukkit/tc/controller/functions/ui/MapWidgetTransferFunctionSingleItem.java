package com.bergerkiller.bukkit.tc.controller.functions.ui;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunction;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionHost;
import java.util.function.BooleanSupplier;

public abstract class MapWidgetTransferFunctionSingleItem extends MapWidgetTransferFunctionItem {
   private boolean functionWasDefault = false;
   private boolean ignoreChanges = false;

   public MapWidgetTransferFunctionSingleItem(TransferFunctionHost host, ConfigurationNode functionConfig, BooleanSupplier isBooleanInput) {
      super(host, TransferFunction.Holder.of(functionConfig != null ? host.loadFunction(functionConfig) : TransferFunction.identity()), isBooleanInput);
      if (functionConfig == null) {
         this.ignoreChanges = true;

         try {
            this.function.setFunction(this.createDefault(), true);
         } finally {
            this.ignoreChanges = false;
         }
      }

      this.updateButtons();
   }

   public MapWidgetTransferFunctionSingleItem(TransferFunctionHost host, TransferFunction.Holder<TransferFunction> function, BooleanSupplier isBooleanInput) {
      super(host, function, isBooleanInput);
      this.updateButtons();
   }

   public abstract void onChanged(TransferFunction.Holder<TransferFunction> var1);

   public abstract TransferFunction createDefault();

   protected void onChangedInternal(TransferFunction.Holder<TransferFunction> function) {
      if (!this.ignoreChanges) {
         this.updateButtons();
         this.onChanged(function);
      }
   }

   protected void updateButtons() {
      if (this.buttons.isEmpty() || this.functionWasDefault != this.function.isDefault()) {
         this.functionWasDefault = this.function.isDefault();
         this.updateButtons((item) -> {
            item.addConfigureButton();
            if (this.function.isDefault()) {
               item.addButton(MapWidgetTransferFunctionItem.ButtonIcon.ADD, () -> {
                  this.getParent().addWidget(new MapWidgetTransferFunctionTypeSelectorDialog(this.host) {
                     public void onSelected(TransferFunction function) {
                        MapWidgetTransferFunctionSingleItem.this.function.setFunction(function);
                        MapWidgetTransferFunctionSingleItem.this.focus();
                     }
                  });
               });
            } else {
               item.addButton(MapWidgetTransferFunctionItem.ButtonIcon.REMOVE, () -> {
                  this.function.setFunction(this.createDefault(), true);
               });
            }

         });
      }
   }
}
