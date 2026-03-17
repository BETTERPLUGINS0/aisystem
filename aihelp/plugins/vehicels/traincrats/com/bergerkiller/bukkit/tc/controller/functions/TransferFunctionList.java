package com.bergerkiller.bukkit.tc.controller.functions;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionDialog;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import com.bergerkiller.bukkit.tc.controller.functions.ui.list.MapWidgetTransferFunctionList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BooleanSupplier;

public class TransferFunctionList implements TransferFunction, Cloneable {
   public static final TransferFunction.Serializer<TransferFunctionList> SERIALIZER = new TransferFunction.Serializer<TransferFunctionList>() {
      public String typeId() {
         return "LIST";
      }

      public String title() {
         return "List Sequence";
      }

      public TransferFunctionList createNew(TransferFunctionHost host) {
         return new TransferFunctionList();
      }

      public TransferFunctionList load(TransferFunctionHost host, ConfigurationNode config) {
         TransferFunctionList list = new TransferFunctionList();
         TransferFunctionRegistry registry = host.getRegistry();
         Iterator var5 = config.getNodeList("functions").iterator();

         while(var5.hasNext()) {
            ConfigurationNode functionConfig = (ConfigurationNode)var5.next();
            TransferFunction function = registry.load(host, functionConfig);
            TransferFunctionList.FunctionMode mode = (TransferFunctionList.FunctionMode)functionConfig.getOrDefault("functionMode", TransferFunctionList.FunctionMode.ASSIGN);
            list.add(new TransferFunctionList.Item(mode, function));
         }

         return list;
      }

      public void save(TransferFunctionHost host, ConfigurationNode config, TransferFunctionList list) {
         if (!list.isEmpty()) {
            List<Object> effectConfigs = config.getList("functions");
            TransferFunctionRegistry registry = host.getRegistry();

            ConfigurationNode functionConfig;
            for(Iterator var6 = list.getItems().iterator(); var6.hasNext(); effectConfigs.add(functionConfig)) {
               TransferFunctionList.Item item = (TransferFunctionList.Item)var6.next();
               functionConfig = registry.save(host, item.getFunction());
               if (item.mode() != TransferFunctionList.FunctionMode.ASSIGN) {
                  functionConfig.set("functionMode", item.mode());
               }
            }
         }

      }
   };
   private final List<TransferFunctionList.Item> items = new ArrayList();
   private int lastSelectedFunctionIndex = -1;
   private int lastScrollPosition = 0;

   public TransferFunction.Serializer<? extends TransferFunction> getSerializer() {
      return SERIALIZER;
   }

   public List<TransferFunctionList.Item> getItems() {
      return Collections.unmodifiableList(this.items);
   }

   public int size() {
      return this.items.size();
   }

   public boolean isEmpty() {
      return this.items.isEmpty();
   }

   public TransferFunctionList.Item get(int index) {
      return (TransferFunctionList.Item)this.items.get(index);
   }

   public void set(int index, TransferFunctionList.Item item) {
      this.items.set(index, item);
   }

   public void add(TransferFunction function) {
      this.add(new TransferFunctionList.Item(TransferFunctionList.FunctionMode.ASSIGN, function));
   }

   public void add(TransferFunctionList.Item item) {
      this.items.add(item);
   }

   public void add(int index, TransferFunctionList.Item item) {
      this.items.add(index, item);
   }

   public void remove(int index) {
      this.items.remove(index);
   }

   public int indexOf(TransferFunctionList.Item item) {
      return this.items.indexOf(item);
   }

   public double map(double input) {
      TransferFunctionList.Item item;
      for(Iterator var3 = this.items.iterator(); var3.hasNext(); input = item.map(input)) {
         item = (TransferFunctionList.Item)var3.next();
      }

      return input;
   }

   public boolean isPure() {
      for(int i = this.items.size() - 1; i >= 0; --i) {
         TransferFunctionList.Item item = (TransferFunctionList.Item)this.items.get(i);
         if (!item.getFunction().isPure()) {
            return false;
         }

         if (item.mode() == TransferFunctionList.FunctionMode.ASSIGN) {
            break;
         }
      }

      return true;
   }

   public boolean isBooleanOutput(int index, BooleanSupplier isBooleanInput) {
      BooleanSupplier chain = isBooleanInput;
      int itemCount = this.items.size();

      for(int i = 0; i < itemCount; ++i) {
         TransferFunctionList.Item item = (TransferFunctionList.Item)this.items.get(i);
         if (item.mode.booleanMode() == TransferFunctionList.FunctionBooleanMode.INPUT) {
            chain = () -> {
               return item.function.isBooleanOutput(chain);
            };
         } else {
            boolean result = item.mode.booleanMode().asBool();
            chain = () -> {
               return result;
            };
         }

         if (i == index) {
            break;
         }
      }

      return chain.getAsBoolean();
   }

   public boolean isBooleanOutput(BooleanSupplier isBooleanInput) {
      return this.isBooleanOutput(this.items.size() - 1, isBooleanInput);
   }

   public TransferFunctionList clone() {
      TransferFunctionList copy = new TransferFunctionList();
      Iterator var2 = this.items.iterator();

      while(var2.hasNext()) {
         TransferFunctionList.Item item = (TransferFunctionList.Item)var2.next();
         copy.items.add(item.clone());
      }

      return copy;
   }

   public void drawPreview(MapWidgetTransferFunctionItem widget, MapCanvas view) {
      byte color = widget.defaultColor((byte)30);
      view.drawLine(0, 3, 6, 3, color);
      view.drawLine(0, 5, 6, 5, color);
      view.drawLine(0, 7, 6, 7, color);
      view.drawLine(0, 9, 6, 9, color);
      view.draw(MapFont.MINECRAFT, 8, 3, color, "[" + this.items.size() + (this.items.size() == 1 ? " step]" : " steps]"));
   }

   public void openDialog(TransferFunction.Dialog dialog) {
      dialog.addWidget((new MapWidgetTransferFunctionList((MapWidgetTransferFunctionDialog)dialog, this) {
         public void onSelectedItemChanged() {
            TransferFunctionList.this.lastSelectedFunctionIndex = this.getSelectedItemIndex();
         }

         public void onTick() {
            super.onTick();
            TransferFunctionList.this.lastScrollPosition = this.getVScroll();
         }
      }).setSelectedItemIndex(this.lastSelectedFunctionIndex).setVScroll(this.lastScrollPosition));
   }

   public static class Item extends TransferFunction.Holder<TransferFunction> implements Cloneable {
      private final TransferFunctionList.FunctionMode mode;

      public Item(TransferFunctionList.FunctionMode mode, TransferFunction function) {
         super(function, false);
         this.mode = mode;
      }

      public TransferFunctionList.FunctionMode mode() {
         return this.mode;
      }

      public double map(double input) {
         return this.mode.apply(input, this.function.map(input));
      }

      public TransferFunctionList.Item clone() {
         return new TransferFunctionList.Item(this.mode, this.function.clone());
      }
   }

   public static enum FunctionMode {
      ASSIGN((i, fo) -> {
         return fo;
      }, TransferFunctionList.FunctionBooleanMode.INPUT),
      MULTIPLY((i, fo) -> {
         return i * fo;
      }, TransferFunctionList.FunctionBooleanMode.NEVER),
      DIVIDE((i, fo) -> {
         return i / fo;
      }, TransferFunctionList.FunctionBooleanMode.NEVER),
      SUBTRACT((i, fo) -> {
         return i - fo;
      }, TransferFunctionList.FunctionBooleanMode.NEVER),
      ADD((i, fo) -> {
         return i + fo;
      }, TransferFunctionList.FunctionBooleanMode.NEVER),
      OR((i, fo) -> {
         return i == 0.0D && fo == 0.0D ? 0.0D : 1.0D;
      }, TransferFunctionList.FunctionBooleanMode.ALWAYS),
      AND((i, fo) -> {
         return i != 0.0D && fo != 0.0D ? 1.0D : 0.0D;
      }, TransferFunctionList.FunctionBooleanMode.ALWAYS);

      private final TransferFunctionList.FunctionModeOperator operator;
      private final TransferFunctionList.FunctionBooleanMode booleanMode;

      private FunctionMode(TransferFunctionList.FunctionModeOperator operator, TransferFunctionList.FunctionBooleanMode booleanMode) {
         this.operator = operator;
         this.booleanMode = booleanMode;
      }

      public TransferFunctionList.FunctionBooleanMode booleanMode() {
         return this.booleanMode;
      }

      public double apply(double input, double functionOutput) {
         return this.operator.apply(input, functionOutput);
      }

      // $FF: synthetic method
      private static TransferFunctionList.FunctionMode[] $values() {
         return new TransferFunctionList.FunctionMode[]{ASSIGN, MULTIPLY, DIVIDE, SUBTRACT, ADD, OR, AND};
      }
   }

   public static enum FunctionBooleanMode {
      INPUT(false),
      ALWAYS(true),
      NEVER(false);

      private final boolean asBool;

      private FunctionBooleanMode(boolean asBool) {
         this.asBool = asBool;
      }

      public boolean asBool() {
         return this.asBool;
      }

      // $FF: synthetic method
      private static TransferFunctionList.FunctionBooleanMode[] $values() {
         return new TransferFunctionList.FunctionBooleanMode[]{INPUT, ALWAYS, NEVER};
      }
   }

   @FunctionalInterface
   private interface FunctionModeOperator {
      double apply(double var1, double var3);
   }
}
