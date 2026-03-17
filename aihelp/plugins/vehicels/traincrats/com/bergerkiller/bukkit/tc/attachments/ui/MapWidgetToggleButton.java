package com.bergerkiller.bukkit.tc.attachments.ui;

import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

public abstract class MapWidgetToggleButton<T> extends MapWidgetButton {
   private final Map<T, String> _values = new LinkedHashMap();
   private T _value = null;

   public abstract void onSelectionChanged();

   public MapWidgetToggleButton<T> addOption(T value, String text) {
      if (this._values.isEmpty()) {
         this._value = value;
         this.setText(text);
         if (this.getDisplay() != null) {
            this.onSelectionChanged();
         }
      }

      this._values.put(value, text);
      return this;
   }

   @SafeVarargs
   public final MapWidgetToggleButton<T> addOptions(Function<T, String> textFunction, T... values) {
      Object[] var3 = values;
      int var4 = values.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         T value = var3[var5];
         this.addOption(value, (String)textFunction.apply(value));
      }

      return this;
   }

   public final MapWidgetToggleButton<T> addOptions(Function<T, String> textFunction, Class<T> enumType) {
      return this.addOptions(textFunction, enumType.getEnumConstants());
   }

   public MapWidgetToggleButton<T> setSelectedOption(T value) {
      String text = (String)this._values.get(value);
      if (text == null) {
         throw new IllegalArgumentException("Value " + value + " is not a valid option");
      } else {
         if (!LogicUtil.bothNullOrEqual(this._value, value)) {
            this._value = value;
            this.setText(text);
            if (this.getDisplay() != null) {
               this.onSelectionChanged();
            }
         }

         return this;
      }
   }

   public T getSelectedOption() {
      return this._value;
   }

   public void nextOption() {
      if (this._values.size() > 1) {
         Iterator iter = this._values.entrySet().iterator();

         while(true) {
            Entry e;
            if (!iter.hasNext()) {
               e = (Entry)this._values.entrySet().iterator().next();
               this._value = e.getKey();
               this.setText((String)e.getValue());
               break;
            }

            if (((Entry)iter.next()).getKey().equals(this._value) && iter.hasNext()) {
               e = (Entry)iter.next();
               this._value = e.getKey();
               this.setText((String)e.getValue());
               break;
            }
         }

         if (this.getDisplay() != null) {
            this.onSelectionChanged();
         }
      }

   }

   public void onActivate() {
      this.nextOption();
   }
}
