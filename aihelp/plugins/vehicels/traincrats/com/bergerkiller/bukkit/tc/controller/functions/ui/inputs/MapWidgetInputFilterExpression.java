package com.bergerkiller.bukkit.tc.controller.functions.ui.inputs;

import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetSubmitText;

public abstract class MapWidgetInputFilterExpression extends MapWidget {
   private static final byte COLOR_BG_DEFAULT = MapColorPalette.getColor(199, 199, 199);
   private static final byte COLOR_BG_FOCUSED = MapColorPalette.getColor(255, 252, 245);
   private static final byte COLOR_INVERTED = MapColorPalette.getColor(94, 40, 114);
   private MapWidgetSubmitText submitWidget;
   private String expression = "";

   public MapWidgetInputFilterExpression() {
      this.setFocusable(true);
   }

   public abstract void onChanged(String var1);

   public MapWidgetInputFilterExpression setExpression(String expression) {
      if (!this.expression.equals(expression)) {
         this.expression = expression;
         this.invalidate();
      }

      return this;
   }

   public String getExpression() {
      return this.expression;
   }

   public void onAttached() {
      this.submitWidget = (MapWidgetSubmitText)this.getParent().addWidget((new MapWidgetSubmitText() {
         public void onAccept(String text) {
            text = text.trim();
            MapWidgetInputFilterExpression.this.setExpression(text);
            MapWidgetInputFilterExpression.this.onChanged(text);
         }
      }).setDescription("Set Expression"));
   }

   public void onActivate() {
      this.submitWidget.activate();
   }

   public void onDraw() {
      this.view.drawRectangle(0, 0, this.getWidth(), this.getHeight(), (byte)119);
      this.view.fillRectangle(1, 1, this.getWidth() - 2, this.getHeight() - 2, this.isFocused() ? COLOR_BG_FOCUSED : COLOR_BG_DEFAULT);
      MapCanvas textView = this.view.getView(1, 1, this.getWidth() - 1, this.getHeight() - 1);
      if (this.expression.isEmpty()) {
         textView.draw(MapFont.MINECRAFT, (this.getWidth() - 50) / 2, 2, (byte)18, "<Not Set>");
      } else if (this.expression.startsWith("!")) {
         textView.draw(MapFont.MINECRAFT, 1, 2, COLOR_INVERTED, "!");
         textView.draw(MapFont.MINECRAFT, 3, 2, (byte)(this.isFocused() ? 50 : 119), this.expression.substring(1));
      } else {
         textView.draw(MapFont.MINECRAFT, 1, 2, (byte)(this.isFocused() ? 50 : 119), this.expression);
      }

   }
}
