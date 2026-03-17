package com.bergerkiller.bukkit.tc.controller.functions.ui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapBlendMode;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.MapPlayerInput.Key;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunction;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionHost;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class MapWidgetTransferFunctionItem extends MapWidget {
   public static final int HEIGHT = 15;
   protected static final byte COLOR_BG_DEFAULT = MapColorPalette.getColor(199, 199, 199);
   protected static final byte COLOR_BG_FOCUSED = MapColorPalette.getColor(255, 252, 245);
   protected static final byte COLOR_BG_MOVING = MapColorPalette.getColor(247, 233, 163);
   protected final List<MapWidgetTransferFunctionItem.Button> buttons = new ArrayList();
   protected final TransferFunctionHost host;
   protected final TransferFunction.Holder<TransferFunction> function;
   protected final BooleanSupplier isBooleanInput;
   protected boolean moving;
   protected int selButtonIdx = 0;

   public MapWidgetTransferFunctionItem(TransferFunctionHost host, TransferFunction.Holder<TransferFunction> function, BooleanSupplier isBooleanInput) {
      this.host = host;
      this.function = function.withChangeListener(this::onChangedInternal);
      this.isBooleanInput = isBooleanInput;
      this.setFocusable(true);
      this.setSize(104, 15);
   }

   protected void onChangedInternal(TransferFunction.Holder<TransferFunction> function) {
   }

   public void onMoveUp() {
   }

   public void onMoveDown() {
   }

   public TransferFunction getFunction() {
      return this.function.getFunction();
   }

   public boolean isDefault() {
      return this.function.isDefault();
   }

   public byte defaultColor(byte color) {
      return this.isDefault() ? TransferFunction.DEFAULT_FUNCTION_COLOR : color;
   }

   public MapWidgetTransferFunctionItem addConfigureButton() {
      return this.addButton(MapWidgetTransferFunctionItem.ButtonIcon.CONFIGURE, this::configure);
   }

   public void updateButtons(Consumer<MapWidgetTransferFunctionItem> addActions) {
      MapWidgetTransferFunctionItem.ButtonIcon prevSelectedIcon = null;
      if (this.selButtonIdx >= 0 && this.selButtonIdx < this.buttons.size()) {
         prevSelectedIcon = ((MapWidgetTransferFunctionItem.Button)this.buttons.get(this.selButtonIdx)).icon;
      }

      this.buttons.clear();
      addActions.accept(this);
      this.invalidate();
      if (prevSelectedIcon != null) {
         for(int i = 0; i < this.buttons.size(); ++i) {
            if (((MapWidgetTransferFunctionItem.Button)this.buttons.get(i)).icon == prevSelectedIcon) {
               this.selButtonIdx = i;
               return;
            }
         }
      }

      if (this.selButtonIdx >= this.buttons.size()) {
         this.selButtonIdx = this.buttons.size() - 1;
      }

   }

   public MapWidgetTransferFunctionItem addButton(MapWidgetTransferFunctionItem.ButtonIcon icon, Runnable action) {
      this.buttons.add(new MapWidgetTransferFunctionItem.Button(icon, action));
      return this;
   }

   public void configure() {
      if (this.getFunction().openDialogMode() == TransferFunction.DialogMode.NONE) {
         this.display.playSound(SoundEffect.EXTINGUISH);
      } else if (this.getFunction().openDialogMode() == TransferFunction.DialogMode.INLINE) {
         MapWidgetTransferFunctionItem.InlineDialog inlineDialog = new MapWidgetTransferFunctionItem.InlineDialog();
         this.updateInlineDialogBounds(inlineDialog);
         this.addWidget(inlineDialog);
         this.getFunction().openDialog(inlineDialog);
         if (inlineDialog.getWidgetCount() == 0) {
            inlineDialog.finish();
            this.display.playSound(SoundEffect.EXTINGUISH);
         } else {
            this.activate();
         }

      } else {
         MapWidgetTransferFunctionDialog dialog = this.getCurrentDialog();
         if (dialog != null) {
            dialog.navigate(this.function, this.isBooleanInput);
         } else {
            dialog = new MapWidgetTransferFunctionDialog(this.host, this.function.getFunction(), this.isBooleanInput) {
               public void onChanged(TransferFunction function) {
                  MapWidgetTransferFunctionItem.this.function.setFunction(function);
                  MapWidgetTransferFunctionItem.this.invalidate();
               }
            };
            this.getParent().addWidget(dialog);
         }

      }
   }

   public boolean isMoving() {
      return this.moving;
   }

   protected void setSelectedButton(int index) {
      if (index < 0) {
         index = 0;
      } else if (index >= this.buttons.size()) {
         index = this.buttons.size() - 1;
      }

      if (this.selButtonIdx != index) {
         this.selButtonIdx = index;
         this.invalidate();
      }

   }

   protected MapWidgetTransferFunctionDialog getCurrentDialog() {
      for(MapWidget w = this.getParent(); w != null; w = w.getParent()) {
         if (w instanceof MapWidgetTransferFunctionDialog) {
            return (MapWidgetTransferFunctionDialog)w;
         }
      }

      return null;
   }

   public void onFocus() {
      this.selButtonIdx = 0;
   }

   public void onDraw() {
      this.view.drawRectangle(0, 0, this.getWidth(), this.getHeight(), (byte)119);
      this.view.fillRectangle(1, 1, this.getWidth() - 2, this.getHeight() - 2, this.moving ? COLOR_BG_MOVING : (this.isFocused() ? COLOR_BG_FOCUSED : COLOR_BG_DEFAULT));
      if (!this.isActivated()) {
         MapCanvas previewView = this.view.getView(2, 1, this.getWidth() - 2, this.getHeight() - 2);
         this.getFunction().drawPreview(this, previewView);
         this.drawUI();
      }

   }

   protected void updateInlineDialogBounds(MapWidgetTransferFunctionItem.InlineDialog dialog) {
      dialog.setBounds(1, 1, this.getWidth() - 2, this.getHeight() - 2);
   }

   protected void drawUI() {
      if (!this.moving && this.isFocused() && !this.buttons.isEmpty()) {
         int x_icon_step = ((MapWidgetTransferFunctionItem.Button)this.buttons.get(0)).icon.width() + 1;
         int x = this.getWidth() - this.buttons.size() * x_icon_step - 1;
         int i = 0;

         for(Iterator var4 = this.buttons.iterator(); var4.hasNext(); x += x_icon_step) {
            MapWidgetTransferFunctionItem.Button b = (MapWidgetTransferFunctionItem.Button)var4.next();
            this.view.draw(b.icon.icon(this.selButtonIdx == i), x, 2);
            ++i;
         }
      }

   }

   public void onKeyPressed(MapKeyEvent event) {
      if (this.moving) {
         if (event.getKey() == Key.UP) {
            this.onMoveUp();
         } else if (event.getKey() == Key.DOWN) {
            this.onMoveDown();
         } else if (event.getKey() == Key.BACK || event.getKey() == Key.ENTER) {
            this.moving = false;
            MapWidgetTransferFunctionDialog dialog = this.getCurrentDialog();
            if (dialog != null) {
               dialog.setExitOnBack(true);
            }

            this.invalidate();
         }
      } else if (event.getKey() == Key.LEFT && this.isFocused()) {
         this.setSelectedButton(this.selButtonIdx - 1);
      } else if (event.getKey() == Key.RIGHT && this.isFocused()) {
         this.setSelectedButton(this.selButtonIdx + 1);
      } else if (event.getKey() == Key.ENTER && this.selButtonIdx >= 0 && !this.buttons.isEmpty() && this.isFocused()) {
         ((MapWidgetTransferFunctionItem.Button)this.buttons.get(this.selButtonIdx)).action.run();
      } else {
         super.onKeyPressed(event);
      }

   }

   public static enum ButtonIcon {
      CONFIGURE("Configure"),
      MOVE("Change order"),
      REMOVE("Remove operation"),
      ADD("Add new operation"),
      INITIALIZE("Set operation");

      private final MapTexture icon_selected;
      private final MapTexture icon_default;
      private final String tooltip;

      private ButtonIcon(String tooltip) {
         MapTexture atlas = MapTexture.loadPluginResource(TrainCarts.plugin, "com/bergerkiller/bukkit/tc/textures/attachments/transfer_function_item_buttons.png");
         this.icon_selected = atlas.getView(this.ordinal() * atlas.getHeight(), 0, atlas.getHeight(), atlas.getHeight()).clone();
         this.icon_default = this.icon_selected.clone();
         this.icon_default.setBlendMode(MapBlendMode.SUBTRACT);
         this.icon_default.fill(MapColorPalette.getColor(20, 20, 64));
         this.tooltip = tooltip;
      }

      public int width() {
         return this.icon_selected.getWidth();
      }

      public MapTexture icon(boolean selected) {
         return selected ? this.icon_selected : this.icon_default;
      }

      public String tooltip() {
         return this.tooltip;
      }

      // $FF: synthetic method
      private static MapWidgetTransferFunctionItem.ButtonIcon[] $values() {
         return new MapWidgetTransferFunctionItem.ButtonIcon[]{CONFIGURE, MOVE, REMOVE, ADD, INITIALIZE};
      }
   }

   private static class Button {
      public final MapWidgetTransferFunctionItem.ButtonIcon icon;
      public Runnable action;

      public Button(MapWidgetTransferFunctionItem.ButtonIcon icon, Runnable action) {
         this.icon = icon;
         this.action = action;
      }
   }

   protected class InlineDialog extends MapWidget implements TransferFunction.Dialog {
      public MapWidget getWidget() {
         return this;
      }

      public TransferFunctionHost getHost() {
         return MapWidgetTransferFunctionItem.this.host;
      }

      public void setFunction(TransferFunction function) {
         MapWidgetTransferFunctionItem.this.function.setFunction(function);
      }

      public boolean isBooleanInput() {
         return MapWidgetTransferFunctionItem.this.isBooleanInput.getAsBoolean();
      }

      public boolean isPreviousFunction(TransferFunction.Holder<?> functionHolder) {
         return MapWidgetTransferFunctionItem.this.function.isSame(functionHolder);
      }

      public void markChanged() {
         this.setFunction(MapWidgetTransferFunctionItem.this.function.getFunction());
         MapWidgetTransferFunctionDialog dialog = MapWidgetTransferFunctionItem.this.getCurrentDialog();
         if (dialog != null) {
            dialog.markChanged();
         }

      }

      public void finish() {
         this.removeWidget();
         MapWidgetTransferFunctionItem.this.focus();
      }

      public void onKeyPressed(MapKeyEvent event) {
         if (event.getKey() == Key.BACK) {
            this.finish();
         } else {
            super.onKeyPressed(event);
         }

      }
   }
}
