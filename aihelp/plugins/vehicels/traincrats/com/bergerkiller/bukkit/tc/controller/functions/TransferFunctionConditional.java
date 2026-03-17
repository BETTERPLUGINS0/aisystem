package com.bergerkiller.bukkit.tc.controller.functions;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionSingleItem;
import com.bergerkiller.bukkit.tc.controller.functions.ui.conditional.MapWidgetTransferFunctionConditionalHysteresis;
import com.bergerkiller.bukkit.tc.controller.functions.ui.conditional.MapWidgetTransferFunctionConditionalOperator;
import com.bergerkiller.bukkit.tc.utils.CachedBooleanSupplier;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class TransferFunctionConditional implements TransferFunction {
   public static final TransferFunction.Serializer<TransferFunctionConditional> SERIALIZER = new TransferFunction.Serializer<TransferFunctionConditional>() {
      public String typeId() {
         return "CONDITIONAL";
      }

      public String title() {
         return "Conditional";
      }

      public TransferFunctionConditional createNew(TransferFunctionHost host) {
         TransferFunctionConditional conditional = new TransferFunctionConditional();
         conditional.setOperator(TransferFunctionConditional.Operator.GREATER_THAN);
         conditional.setRightInput(TransferFunctionConstant.zero());
         conditional.setTrueOutput(TransferFunctionBoolean.TRUE);
         conditional.setFalseOutput(TransferFunctionBoolean.FALSE);
         return conditional;
      }

      public TransferFunctionConditional load(TransferFunctionHost host, ConfigurationNode config) {
         TransferFunctionConditional conditional = new TransferFunctionConditional();
         if (config.isNode("left")) {
            conditional.setLeftInput(host.loadFunction(config.getNode("left")));
         }

         if (config.isNode("right")) {
            conditional.setRightInput(host.loadFunction(config.getNode("right")));
         }

         conditional.setOperator((TransferFunctionConditional.Operator)config.getOrDefault("operator", conditional.operator));
         conditional.setHysteresis((Double)config.getOrDefault("hysteresis", 0.0D));
         if (config.isNode("falseOutput")) {
            conditional.setFalseOutput(host.loadFunction(config.getNode("falseOutput")));
         }

         if (config.isNode("trueOutput")) {
            conditional.setTrueOutput(host.loadFunction(config.getNode("trueOutput")));
         }

         return conditional;
      }

      public void save(TransferFunctionHost host, ConfigurationNode config, TransferFunctionConditional conditional) {
         if (!conditional.leftInput.isDefault()) {
            config.set("left", host.saveFunction(conditional.leftInput.getFunction()));
         }

         if (!conditional.rightInput.isDefault()) {
            config.set("right", host.saveFunction(conditional.rightInput.getFunction()));
         }

         config.set("operator", conditional.operator);
         config.set("hysteresis", conditional.hysteresis != 0.0D ? conditional.hysteresis : null);
         if (!conditional.falseOutput.isDefault()) {
            config.set("falseOutput", host.saveFunction(conditional.falseOutput.getFunction()));
         }

         if (!conditional.trueOutput.isDefault()) {
            config.set("trueOutput", host.saveFunction(conditional.trueOutput.getFunction()));
         }

      }
   };
   private final TransferFunction.Holder<TransferFunction> leftInput = TransferFunction.Holder.of(TransferFunction.identity(), true);
   private final TransferFunction.Holder<TransferFunction> rightInput = TransferFunction.Holder.of(TransferFunction.identity(), true);
   private TransferFunctionConditional.Operator operator;
   private double hysteresis;
   private Boolean hysteresisLastState;
   private final TransferFunction.Holder<TransferFunction> falseOutput;
   private final TransferFunction.Holder<TransferFunction> trueOutput;
   private Boolean leftWasBooleanOnOpen;

   public TransferFunctionConditional() {
      this.operator = TransferFunctionConditional.Operator.GREATER_THAN;
      this.hysteresis = 0.0D;
      this.hysteresisLastState = null;
      this.falseOutput = TransferFunction.Holder.of(TransferFunction.identity(), true);
      this.trueOutput = TransferFunction.Holder.of(TransferFunction.identity(), true);
      this.leftWasBooleanOnOpen = null;
   }

   public TransferFunction.Serializer<? extends TransferFunction> getSerializer() {
      return SERIALIZER;
   }

   public double map(double input) {
      boolean result;
      if (this.operator == TransferFunctionConditional.Operator.BOOL) {
         result = this.leftInput.getFunction().map(input) != 0.0D;
      } else if (this.hysteresis == 0.0D) {
         result = this.operator.compare(this.leftInput.getFunction().map(input), this.rightInput.getFunction().map(input));
      } else {
         if (this.hysteresisLastState == null) {
            this.hysteresisLastState = this.hysteresis < 0.0D;
         }

         result = this.operator.compareWithHysteresis(this.hysteresisLastState, this.leftInput.getFunction().map(input), this.rightInput.getFunction().map(input), Math.abs(this.hysteresis));
      }

      this.hysteresisLastState = result;
      return (result ? this.trueOutput : this.falseOutput).getFunction().map(input);
   }

   public boolean isBooleanOutput(BooleanSupplier isBooleanInput) {
      BooleanSupplier isBooleanInput = CachedBooleanSupplier.of(isBooleanInput);
      return this.trueOutput.getFunction().isBooleanOutput(isBooleanInput) && this.falseOutput.getFunction().isBooleanOutput(isBooleanInput);
   }

   public boolean isPure() {
      return this.leftInput.getFunction().isPure() && (this.operator == TransferFunctionConditional.Operator.BOOL || this.rightInput.getFunction().isPure()) && this.falseOutput.getFunction().isPure() && this.trueOutput.getFunction().isPure();
   }

   public void setLeftInput(TransferFunction input) {
      this.leftInput.setFunction(input);
   }

   public void setRightInput(TransferFunction input) {
      this.rightInput.setFunction(input);
   }

   public void setOperator(TransferFunctionConditional.Operator operator) {
      this.operator = operator;
   }

   public void setHysteresis(double hysteresis) {
      this.hysteresis = hysteresis;
   }

   public void setFalseOutput(TransferFunction output) {
      this.falseOutput.setFunction(output);
   }

   public void setTrueOutput(TransferFunction output) {
      this.trueOutput.setFunction(output);
   }

   public TransferFunctionConditional clone() {
      TransferFunctionConditional copy = new TransferFunctionConditional();
      copy.leftInput.setFunction(this.leftInput.getFunction().clone());
      copy.rightInput.setFunction(this.rightInput.getFunction().clone());
      copy.operator = this.operator;
      copy.falseOutput.setFunction(this.falseOutput.getFunction().clone());
      copy.trueOutput.setFunction(this.trueOutput.getFunction().clone());
      return copy;
   }

   public void drawPreview(MapWidgetTransferFunctionItem widget, MapCanvas view) {
      view.draw(MapFont.MINECRAFT, 2, 3, (byte)30, "Conditional [Y:N]");
   }

   public void openDialog(final TransferFunction.Dialog dialog) {
      Objects.requireNonNull(dialog);
      BooleanSupplier isBooleanInput = CachedBooleanSupplier.of(dialog::isBooleanInput);
      final Consumer<Boolean> autoToggleOperator = (isBool) -> {
         TransferFunctionConditional.Operator newOperator = isBool ? TransferFunctionConditional.Operator.BOOL : TransferFunctionConditional.Operator.GREATER_EQUAL_THAN;
         boolean found = false;
         Iterator var5 = dialog.getWidget().getWidgets().iterator();

         while(var5.hasNext()) {
            MapWidget w = (MapWidget)var5.next();
            if (w instanceof MapWidgetTransferFunctionConditionalOperator) {
               ((MapWidgetTransferFunctionConditionalOperator)w).setOperator(newOperator);
               found = true;
            }
         }

         if (!found) {
            this.setOperator(newOperator);
            dialog.markChanged();
         }

      };
      boolean leftSideIsBoolean = this.leftInput.getFunction().isBooleanOutput(isBooleanInput);
      if (this.leftWasBooleanOnOpen != null && leftSideIsBoolean != this.leftWasBooleanOnOpen && dialog.isPreviousFunction(this.leftInput)) {
         autoToggleOperator.accept(leftSideIsBoolean);
      }

      this.leftWasBooleanOnOpen = leftSideIsBoolean;
      final Runnable focusOperatorWidget = () -> {
         Iterator var1 = dialog.getWidget().getWidgets().iterator();

         while(var1.hasNext()) {
            MapWidget w = (MapWidget)var1.next();
            if (w instanceof MapWidgetTransferFunctionConditionalOperator) {
               w.focus();
               break;
            }
         }

      };
      dialog.addLabel(39, 3, (byte)18, "CONDITION");
      ((<undefinedtype>)dialog.addWidget(new MapWidgetTransferFunctionSingleItem(dialog.getHost(), this.leftInput, isBooleanInput) {
         public void onChanged(TransferFunction.Holder<TransferFunction> function) {
            boolean leftSideIsBoolean = function.getFunction().isBooleanOutput(this.isBooleanInput);
            if (leftSideIsBoolean != TransferFunctionConditional.this.leftWasBooleanOnOpen) {
               autoToggleOperator.accept(leftSideIsBoolean);
               TransferFunctionConditional.this.leftWasBooleanOnOpen = leftSideIsBoolean;
            }

            dialog.markChanged();
         }

         public void onAttached() {
            super.onAttached();
            if (dialog.isPreviousFunction(TransferFunctionConditional.this.leftInput)) {
               this.focus();
            }

         }

         public TransferFunction createDefault() {
            return TransferFunction.identity();
         }

         public void onKeyPressed(MapKeyEvent event) {
            if (event.getKey() == Key.DOWN && this.isFocused()) {
               focusOperatorWidget.run();
            } else {
               super.onKeyPressed(event);
            }

         }
      })).setBounds(5, 9, dialog.getWidth() - 10, 15);
      MapWidgetTransferFunctionConditionalHysteresis hysteresisWidget = (MapWidgetTransferFunctionConditionalHysteresis)dialog.addWidget(new MapWidgetTransferFunctionConditionalHysteresis(this.hysteresis) {
         public void onHysteresisChanged(double hysteresis) {
            TransferFunctionConditional.this.setHysteresis(hysteresis);
            dialog.markChanged();
         }
      });
      hysteresisWidget.setBounds(dialog.getWidth() - 55, 26, 50, 13);
      MapWidgetTransferFunctionSingleItem rightInputWidget = (MapWidgetTransferFunctionSingleItem)dialog.addWidget(new MapWidgetTransferFunctionSingleItem(dialog.getHost(), this.rightInput, isBooleanInput) {
         public void onChanged(TransferFunction.Holder<TransferFunction> function) {
            dialog.markChanged();
         }

         public TransferFunction createDefault() {
            return TransferFunction.identity();
         }

         public void onAttached() {
            super.onAttached();
            if (dialog.isPreviousFunction(TransferFunctionConditional.this.rightInput)) {
               this.focus();
            }

         }

         public void onKeyPressed(MapKeyEvent event) {
            if (event.getKey() == Key.UP && this.isFocused()) {
               focusOperatorWidget.run();
            } else {
               super.onKeyPressed(event);
            }

         }
      });
      rightInputWidget.setBounds(5, 41, dialog.getWidth() - 10, 15);
      final Runnable operatorChangeHandler = () -> {
         hysteresisWidget.setVisible(this.operator != TransferFunctionConditional.Operator.BOOL);
         rightInputWidget.setVisible(this.operator != TransferFunctionConditional.Operator.BOOL);
      };
      operatorChangeHandler.run();
      ((<undefinedtype>)dialog.addWidget(new MapWidgetTransferFunctionConditionalOperator(this.operator) {
         public void onOperatorChanged(TransferFunctionConditional.Operator operator) {
            TransferFunctionConditional.this.operator = operator;
            operatorChangeHandler.run();
            dialog.markChanged();
         }
      })).setBounds(5, 26, 21, 13);
      dialog.addLabel(44, dialog.getHeight() - 44, (byte)18, "RESULT");
      dialog.addLabel(3, dialog.getHeight() - 33, (byte)18, "Y");
      ((<undefinedtype>)dialog.addWidget(new MapWidgetTransferFunctionSingleItem(dialog.getHost(), this.trueOutput, isBooleanInput) {
         public void onChanged(TransferFunction.Holder<TransferFunction> function) {
            dialog.markChanged();
         }

         public void onAttached() {
            super.onAttached();
            if (dialog.isPreviousFunction(TransferFunctionConditional.this.trueOutput)) {
               this.focus();
            }

         }

         public TransferFunction createDefault() {
            return TransferFunction.identity();
         }
      })).setBounds(7, dialog.getHeight() - 38, dialog.getWidth() - 12, 15);
      dialog.addLabel(3, dialog.getHeight() - 16, (byte)18, "N");
      ((<undefinedtype>)dialog.addWidget(new MapWidgetTransferFunctionSingleItem(dialog.getHost(), this.falseOutput, isBooleanInput) {
         public void onChanged(TransferFunction.Holder<TransferFunction> function) {
            dialog.markChanged();
         }

         public void onAttached() {
            super.onAttached();
            if (dialog.isPreviousFunction(TransferFunctionConditional.this.falseOutput)) {
               this.focus();
            }

         }

         public TransferFunction createDefault() {
            return TransferFunction.identity();
         }
      })).setBounds(7, dialog.getHeight() - 21, dialog.getWidth() - 12, 15);
   }

   public static enum Operator {
      EQUAL("==", (l, r) -> {
         return l == r;
      }, (l, r, h) -> {
         return l == r;
      }, (l, r, h) -> {
         return Math.abs(l - r) > h;
      }),
      NOT_EQUAL("!=", (l, r) -> {
         return l != r;
      }, (l, r, h) -> {
         return Math.abs(l - r) > h;
      }, (l, r, h) -> {
         return l == r;
      }),
      GREATER_THAN(">", (l, r) -> {
         return l > r;
      }, (l, r, h) -> {
         return l - r > h;
      }, (l, r, h) -> {
         return r - l >= h;
      }),
      GREATER_EQUAL_THAN(">=", (l, r) -> {
         return l >= r;
      }, (l, r, h) -> {
         return l - r >= h;
      }, (l, r, h) -> {
         return r - l > h;
      }),
      LESSER_THAN("<", (l, r) -> {
         return l < r;
      }, (l, r, h) -> {
         return r - l > h;
      }, (l, r, h) -> {
         return l - r >= h;
      }),
      LESSER_EQUAL_THAN("<=", (l, r) -> {
         return l <= r;
      }, (l, r, h) -> {
         return r - l >= h;
      }, (l, r, h) -> {
         return l - r > h;
      }),
      BOOL("!=0", (l, r) -> {
         return l != 0.0D;
      }, (l, r, h) -> {
         return l != 0.0D;
      }, (l, r, h) -> {
         return l == 0.0D;
      });

      private final String title;
      private final TransferFunctionConditional.DoubleComparator comparator;
      private final TransferFunctionConditional.DoubleHysteresisComparator trueHysteresisComparator;
      private final TransferFunctionConditional.DoubleHysteresisComparator falseHysteresisComparator;

      private Operator(String title, TransferFunctionConditional.DoubleComparator comparator, TransferFunctionConditional.DoubleHysteresisComparator trueHysteresisComparator, TransferFunctionConditional.DoubleHysteresisComparator falseHysteresisComparator) {
         this.title = title;
         this.comparator = comparator;
         this.trueHysteresisComparator = trueHysteresisComparator;
         this.falseHysteresisComparator = falseHysteresisComparator;
      }

      public String title() {
         return this.title;
      }

      public boolean compare(double left, double right) {
         return this.comparator.compare(left, right);
      }

      public boolean compareWithHysteresis(boolean wasTrue, double left, double right, double hysteresis) {
         if (wasTrue) {
            return !this.falseHysteresisComparator.compare(left, right, hysteresis);
         } else {
            return this.trueHysteresisComparator.compare(left, right, hysteresis);
         }
      }

      public boolean hasRightHandSide() {
         return this != BOOL;
      }

      // $FF: synthetic method
      private static TransferFunctionConditional.Operator[] $values() {
         return new TransferFunctionConditional.Operator[]{EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_EQUAL_THAN, LESSER_THAN, LESSER_EQUAL_THAN, BOOL};
      }
   }

   @FunctionalInterface
   private interface DoubleHysteresisComparator {
      boolean compare(double var1, double var3, double var5);
   }

   @FunctionalInterface
   private interface DoubleComparator {
      boolean compare(double var1, double var3);
   }
}
