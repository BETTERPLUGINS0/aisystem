package com.bergerkiller.bukkit.tc.controller.functions;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import java.util.function.BooleanSupplier;

public class TransferFunctionBoolean implements TransferFunction {
   public static final TransferFunctionBoolean TRUE = new TransferFunctionBoolean(true);
   public static final TransferFunctionBoolean FALSE = new TransferFunctionBoolean(false);
   public static final TransferFunction.Serializer<TransferFunctionBoolean> SERIALIZER = new TransferFunction.Serializer<TransferFunctionBoolean>() {
      public String typeId() {
         return "BOOLEAN";
      }

      public String title() {
         return "Yes/No";
      }

      public TransferFunctionBoolean createNew(TransferFunctionHost host) {
         return TransferFunctionBoolean.TRUE;
      }

      public TransferFunctionBoolean load(TransferFunctionHost host, ConfigurationNode config) {
         return (Boolean)config.getOrDefault("output", false) ? TransferFunctionBoolean.TRUE : TransferFunctionBoolean.FALSE;
      }

      public void save(TransferFunctionHost host, ConfigurationNode config, TransferFunctionBoolean function) {
         config.set("output", function.output);
      }
   };
   private final boolean boolOutput;
   private final double output;

   private TransferFunctionBoolean(boolean output) {
      this.boolOutput = output;
      this.output = this.boolOutput ? 1.0D : 0.0D;
   }

   public TransferFunction.Serializer<? extends TransferFunction> getSerializer() {
      return SERIALIZER;
   }

   public boolean getOutput() {
      return this.boolOutput;
   }

   public TransferFunctionBoolean opposite() {
      return this.boolOutput ? FALSE : TRUE;
   }

   public double map(double input) {
      return this.output;
   }

   public boolean isBooleanOutput(BooleanSupplier isBooleanInput) {
      return true;
   }

   public boolean isPure() {
      return true;
   }

   public TransferFunction clone() {
      return this;
   }

   public void drawPreview(MapWidgetTransferFunctionItem widget, MapCanvas view) {
      view.draw(MapFont.MINECRAFT, 0, 3, widget.defaultColor((byte)(this.boolOutput ? 30 : 18)), this.boolOutput ? "Yes [ 1 ]" : "No [ 0 ]");
   }

   public void openDialog(final TransferFunction.Dialog dialog) {
      ((<undefinedtype>)dialog.addWidget(new TransferFunctionBoolean.TrueFalseToggleWidget(this.boolOutput) {
         public void onChanged(boolean state) {
            dialog.setFunction(state ? TransferFunctionBoolean.TRUE : TransferFunctionBoolean.FALSE);
         }

         public void onClosed() {
            dialog.finish();
         }
      })).setBounds(8, 1, dialog.getWidth() - 16, dialog.getHeight() - 2);
   }

   public TransferFunction.DialogMode openDialogMode() {
      return TransferFunction.DialogMode.INLINE;
   }

   private abstract static class TrueFalseToggleWidget extends MapWidget {
      private boolean state;

      public TrueFalseToggleWidget(boolean initial) {
         this.state = initial;
         this.setFocusable(true);
      }

      public abstract void onChanged(boolean var1);

      public abstract void onClosed();

      public void onDraw() {
         this.view.draw(MapFont.MINECRAFT, 11, 2, this.state ? MapColorPalette.getColor(0, 217, 58) : MapColorPalette.getColor(0, 65, 0), "Yes");
         this.view.draw(MapFont.MINECRAFT, 33, 2, (byte)34, "/");
         this.view.draw(MapFont.MINECRAFT, 43, 2, this.state ? MapColorPalette.getColor(100, 25, 25) : 18, "No");
      }

      public void onKeyPressed(MapKeyEvent event) {
         if (event.getKey() == Key.LEFT) {
            if (!this.state) {
               this.state = true;
               this.invalidate();
               this.onChanged(true);
            }
         } else if (event.getKey() == Key.RIGHT) {
            if (this.state) {
               this.state = false;
               this.invalidate();
               this.onChanged(false);
            }
         } else if (event.getKey() == Key.ENTER || event.getKey() == Key.BACK) {
            this.onClosed();
         }

      }
   }
}
