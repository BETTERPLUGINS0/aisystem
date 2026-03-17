package com.bergerkiller.bukkit.tc.controller.functions.inputs;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSelectionBox;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunction;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionHost;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BooleanSupplier;

public abstract class TransferFunctionInput implements TransferFunction {
   private TransferFunctionInput.ReferencedSource source;

   public TransferFunctionInput() {
      this.source = TransferFunctionInput.ReferencedSource.NONE;
   }

   public abstract TransferFunctionInput.ReferencedSource createSource(TransferFunctionHost var1);

   public final void updateSource(TransferFunctionHost host) {
      TransferFunctionInput.ReferencedSource newSource = this.createSource(host);
      newSource = host.registerInputSource(newSource);
      if (!this.source.equals(newSource)) {
         this.source.removeRecipient(this);
         this.source = newSource;
         newSource.addRecipient(this);
      }

   }

   public double map(double unusedInput) {
      return this.source.value();
   }

   public final boolean isBooleanOutput(BooleanSupplier isBooleanInput) {
      return this.isBooleanOutput();
   }

   public abstract boolean isBooleanOutput();

   public final TransferFunctionInput clone() {
      TransferFunctionInput copy = this.cloneInput();
      if (!copy.source.equals(this.source)) {
         copy.source = this.source;
         copy.source.addRecipient(copy);
      }

      return copy;
   }

   protected abstract TransferFunctionInput cloneInput();

   public void openDialog(final TransferFunction.Dialog dialog) {
      ((<undefinedtype>)dialog.addWidget(new MapWidgetSelectionBox() {
         private final List<TransferFunction.Serializer<?>> serializers = new ArrayList();
         private boolean loading = false;

         public void onAttached() {
            this.serializers.clear();
            this.loading = true;
            Iterator var1 = dialog.getHost().getRegistry().all().iterator();

            while(var1.hasNext()) {
               TransferFunction.Serializer<?> serializer = (TransferFunction.Serializer)var1.next();
               if (serializer.isListed(dialog.getHost()) && serializer.isInput()) {
                  this.serializers.add(serializer);
                  this.addItem(serializer.title());
                  if (serializer == TransferFunctionInput.this.getSerializer()) {
                     this.setSelectedIndex(this.getItemCount() - 1);
                  }
               }
            }

            super.onAttached();
            this.loading = false;
            this.focus();
         }

         public void onSelectedItemChanged() {
            if (!this.loading && this.getSelectedIndex() >= 0 && this.getSelectedIndex() < this.serializers.size()) {
               TransferFunction.Serializer<?> newSerializer = (TransferFunction.Serializer)this.serializers.get(this.getSelectedIndex());
               dialog.setFunction(newSerializer.createNew(dialog.getHost()));
            }

         }
      })).setBounds(4, 5, dialog.getWidth() - 8, 11);
   }

   public abstract static class ReferencedSource {
      public static final TransferFunctionInput.ReferencedSource NONE = new TransferFunctionInput.ReferencedSource() {
         public boolean equals(Object o) {
            return this == o;
         }

         public void addRecipient(TransferFunctionInput recipient) {
         }
      };
      protected double value = 0.0D;
      private final List<WeakReference<TransferFunctionInput>> recipients = new ArrayList();

      public double value() {
         return this.value;
      }

      public void addRecipient(TransferFunctionInput recipient) {
         this.recipients.add(new WeakReference(recipient));
      }

      public void removeRecipient(TransferFunctionInput recipient) {
         this.recipients.removeIf((ref) -> {
            TransferFunctionInput input = (TransferFunctionInput)ref.get();
            return input == null || input == recipient;
         });
      }

      public boolean hasRecipients() {
         this.recipients.removeIf((ref) -> {
            return ref.get() == null;
         });
         return !this.recipients.isEmpty();
      }

      public void onTick() {
      }

      public boolean isTickedDuringPlay() {
         return false;
      }

      public void onTransform(Matrix4x4 transform) {
      }

      public abstract boolean equals(Object var1);
   }
}
