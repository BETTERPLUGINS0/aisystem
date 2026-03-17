package com.bergerkiller.bukkit.tc.controller.functions;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetText;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleUnaryOperator;

public interface TransferFunction extends DoubleUnaryOperator, Cloneable {
   byte DEFAULT_FUNCTION_COLOR = MapColorPalette.getColor(100, 100, 100);

   TransferFunction.Serializer<? extends TransferFunction> getSerializer();

   static TransferFunctionRegistry getRegistry() {
      return TransferFunctionRegistry.INSTANCE;
   }

   static TransferFunction identity() {
      return TransferFunctionIdentity.INSTANCE;
   }

   double map(double var1);

   default boolean isBooleanOutput(BooleanSupplier isBooleanInput) {
      return false;
   }

   default boolean isPure() {
      return false;
   }

   default double applyAsDouble(double v) {
      return this.map(v);
   }

   TransferFunction clone();

   void drawPreview(MapWidgetTransferFunctionItem var1, MapCanvas var2);

   void openDialog(TransferFunction.Dialog var1);

   default TransferFunction.DialogMode openDialogMode() {
      return TransferFunction.DialogMode.WINDOW;
   }

   public static enum DialogMode {
      NONE,
      INLINE,
      WINDOW;

      // $FF: synthetic method
      private static TransferFunction.DialogMode[] $values() {
         return new TransferFunction.DialogMode[]{NONE, INLINE, WINDOW};
      }
   }

   public interface Serializer<T extends TransferFunction> {
      String TYPE_FIELD = "type";

      String typeId();

      String title();

      default boolean isListed(TransferFunctionHost host) {
         return true;
      }

      default boolean isInput() {
         return false;
      }

      T createNew(TransferFunctionHost var1);

      T load(TransferFunctionHost var1, ConfigurationNode var2);

      void save(TransferFunctionHost var1, ConfigurationNode var2, T var3);
   }

   public static class Holder<T extends TransferFunction> {
      protected T function;
      protected boolean isDefault = false;

      protected Holder(T function, boolean isDefault) {
         this.function = function;
         this.isDefault = isDefault;
      }

      public T getFunction() {
         return this.function;
      }

      public final void setFunction(T function) {
         this.setFunction(function, false);
      }

      public void setFunction(T function, boolean isDefault) {
         this.function = function;
         this.isDefault = isDefault;
      }

      public boolean isIdentity() {
         return this.getFunction() == TransferFunction.identity();
      }

      public boolean isDefault() {
         return this.isDefault;
      }

      public TransferFunction.Holder<T> withChangeListener(final Consumer<TransferFunction.Holder<T>> onChanged) {
         return new TransferFunction.Holder<T>(this.function, this.isDefault) {
            public void setFunction(T function, boolean isDefault) {
               super.setFunction(function, isDefault);
               Holder.this.setFunction(function, isDefault);
               onChanged.accept(this);
            }

            protected TransferFunction.Holder<?> rootHolder() {
               return Holder.this.rootHolder();
            }
         };
      }

      public final boolean isSame(TransferFunction.Holder<?> other) {
         return this.rootHolder() == other.rootHolder();
      }

      protected TransferFunction.Holder<?> rootHolder() {
         return this;
      }

      public static <T extends TransferFunction> TransferFunction.Holder<T> of(T function) {
         return new TransferFunction.Holder(function, false);
      }

      public static <T extends TransferFunction> TransferFunction.Holder<T> of(T function, boolean isDefault) {
         return new TransferFunction.Holder(function, isDefault);
      }
   }

   public interface Dialog {
      MapWidget getWidget();

      TransferFunctionHost getHost();

      void setFunction(TransferFunction var1);

      boolean isBooleanInput();

      boolean isPreviousFunction(TransferFunction.Holder<?> var1);

      void markChanged();

      void finish();

      default <T extends MapWidget> T addWidget(T widget) {
         return this.getWidget().addWidget(widget);
      }

      default int getWidth() {
         return this.getWidget().getWidth();
      }

      default int getHeight() {
         return this.getWidget().getHeight();
      }

      default void addLabel(int x, int y, byte color, String text) {
         MapWidgetText label = new MapWidgetText();
         label.setFont(MapFont.TINY);
         label.setText(text);
         label.setPosition(x, y);
         label.setColor(color);
         this.addWidget(label);
      }

      default TransferFunction.Dialog wrapWidget(final MapWidget widget) {
         return new TransferFunction.Dialog() {
            public MapWidget getWidget() {
               return widget;
            }

            public TransferFunctionHost getHost() {
               return Dialog.this.getHost();
            }

            public void setFunction(TransferFunction function) {
               Dialog.this.setFunction(function);
            }

            public boolean isBooleanInput() {
               return Dialog.this.isBooleanInput();
            }

            public boolean isPreviousFunction(TransferFunction.Holder<?> functionHolder) {
               return Dialog.this.isPreviousFunction(functionHolder);
            }

            public void markChanged() {
               Dialog.this.markChanged();
            }

            public void finish() {
               Dialog.this.finish();
            }
         };
      }
   }
}
