package com.bergerkiller.bukkit.tc.controller.functions.inputs;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.map.MapCanvas;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.attachments.ui.MapWidgetSelectionBox;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunction;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionHost;
import com.bergerkiller.bukkit.tc.controller.functions.ui.MapWidgetTransferFunctionItem;
import com.bergerkiller.bukkit.tc.controller.functions.ui.inputs.MapWidgetInputFilterExpression;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.properties.api.IDoubleProperty;
import com.bergerkiller.bukkit.tc.properties.api.IProperty;
import com.bergerkiller.bukkit.tc.properties.api.IStringSetProperty;
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class TransferFunctionInputProperty extends TransferFunctionInput {
   public static final TransferFunction.Serializer<TransferFunctionInputProperty> SERIALIZER = new TransferFunction.Serializer<TransferFunctionInputProperty>() {
      public String typeId() {
         return "INPUT-PROPERTY";
      }

      public String title() {
         return "In: Property";
      }

      public boolean isInput() {
         return true;
      }

      public TransferFunctionInputProperty createNew(TransferFunctionHost host) {
         TransferFunctionInputProperty propertyInput = new TransferFunctionInputProperty(StandardProperties.SPEEDLIMIT);
         propertyInput.updateSource(host);
         return propertyInput;
      }

      public TransferFunctionInputProperty load(TransferFunctionHost host, ConfigurationNode config) {
         IProperty<?> property = (IProperty)host.getTrainCarts().getPropertyRegistry().byListedName().get(config.getOrDefault("property", ""));
         TransferFunctionInputProperty propertyInput = new TransferFunctionInputProperty(property);
         propertyInput.property.load(config);
         propertyInput.updateSource(host);
         return propertyInput;
      }

      public void save(TransferFunctionHost host, ConfigurationNode config, TransferFunctionInputProperty function) {
         config.set("property", function.property.name);
         if (function.property.exists()) {
            function.property.save(config);
         }

      }
   };
   private TransferFunctionInputProperty.ListedProperty<?> property;

   public TransferFunctionInputProperty(IProperty<?> property) {
      this(TransferFunctionInputProperty.ListedProperty.of(property));
   }

   private TransferFunctionInputProperty(TransferFunctionInputProperty.ListedProperty<?> property) {
      if (property == null) {
         throw new IllegalArgumentException("Listed Property cannot be null");
      } else {
         this.property = property;
      }
   }

   public IProperty<?> getProperty() {
      return this.property.property;
   }

   public void setProperty(IProperty<?> property) {
      this.property = TransferFunctionInputProperty.ListedProperty.of(property);
   }

   public TransferFunction.Serializer<? extends TransferFunction> getSerializer() {
      return SERIALIZER;
   }

   public TransferFunctionInput.ReferencedSource createSource(TransferFunctionHost host) {
      if (this.property.canCreateSource()) {
         CartProperties properties = host.getCartProperties();
         if (properties != null) {
            return this.property.createSource(properties);
         }
      }

      return TransferFunctionInput.ReferencedSource.NONE;
   }

   public boolean isBooleanOutput() {
      return this.property.isBooleanOutput();
   }

   protected TransferFunctionInput cloneInput() {
      return new TransferFunctionInputProperty(this.property);
   }

   public void drawPreview(MapWidgetTransferFunctionItem widget, MapCanvas view) {
      view.draw(MapFont.MINECRAFT, 0, 3, (byte)30, "Property [input]");
   }

   public void openDialog(final TransferFunction.Dialog dialog) {
      super.openDialog(dialog);
      ((<undefinedtype>)dialog.addWidget(new MapWidgetSelectionBox() {
         private List<TransferFunctionInputProperty.ListedProperty<?>> properties = Collections.emptyList();
         private boolean loading = false;

         public void onAttached() {
            this.properties = (List)TrainCarts.plugin.getPropertyRegistry().byListedName().entrySet().stream().map((e) -> {
               return TransferFunctionInputProperty.ListedProperty.of((String)e.getKey(), (IProperty)e.getValue());
            }).filter(TransferFunctionInputProperty.ListedProperty::canCreateSource).sorted().collect(Collectors.toList());
            this.loading = true;
            Iterator var1 = this.properties.iterator();

            while(var1.hasNext()) {
               TransferFunctionInputProperty.ListedProperty<?> listedProperty = (TransferFunctionInputProperty.ListedProperty)var1.next();
               this.addItem(listedProperty.name);
               if (listedProperty.property == TransferFunctionInputProperty.this.property.property) {
                  this.setSelectedIndex(this.getItemCount() - 1);
               }
            }

            super.onAttached();
            this.loading = false;
         }

         public void onSelectedItemChanged() {
            if (!this.loading && this.getSelectedIndex() >= 0 && this.getSelectedIndex() < this.properties.size()) {
               TransferFunctionInputProperty.this.setProperty(((TransferFunctionInputProperty.ListedProperty)this.properties.get(this.getSelectedIndex())).property);
               Iterator var1 = dialog.getWidget().getWidgets().iterator();

               while(var1.hasNext()) {
                  MapWidget w = (MapWidget)var1.next();
                  if (w instanceof TransferFunctionInputProperty.PropertyOptionsWidget) {
                     ((TransferFunctionInputProperty.PropertyOptionsWidget)w).update();
                     break;
                  }
               }

               TransferFunctionInputProperty.this.updateSource(dialog.getHost());
               dialog.markChanged();
            }

         }
      })).setBounds(4, 18, dialog.getWidth() - 8, 11);
      dialog.addWidget((new TransferFunctionInputProperty.PropertyOptionsWidget(dialog)).setBounds(0, 31, dialog.getWidth(), dialog.getHeight() - 31));
   }

   private static class ListedProperty<P extends IProperty<?>> implements Comparable<TransferFunctionInputProperty.ListedProperty<?>>, Cloneable {
      public final String name;
      public final P property;
      private final BiFunction<CartProperties, TransferFunctionInputProperty.ListedProperty<P>, TransferFunctionInput.ReferencedSource> sourceCreator;

      public <LP extends TransferFunctionInputProperty.ListedProperty<P>> ListedProperty(String listedName, P property, BiFunction<CartProperties, LP, TransferFunctionInput.ReferencedSource> sourceCreator) {
         this.name = listedName;
         this.property = property;
         this.sourceCreator = sourceCreator;
      }

      public boolean exists() {
         return this.property != null;
      }

      public boolean canCreateSource() {
         return this.sourceCreator != null;
      }

      public TransferFunctionInput.ReferencedSource createSource(CartProperties properties) {
         return (TransferFunctionInput.ReferencedSource)this.sourceCreator.apply(properties, this);
      }

      public boolean isBooleanOutput() {
         return this.exists() && this.property.getDefault() instanceof Boolean;
      }

      public void load(ConfigurationNode config) {
      }

      public void save(ConfigurationNode config) {
      }

      public void addWidgets(TransferFunction.Dialog dialog, TransferFunctionInputProperty function) {
      }

      public int compareTo(TransferFunctionInputProperty.ListedProperty<?> listedProperty) {
         return this.name.compareTo(listedProperty.name);
      }

      public TransferFunctionInputProperty.ListedProperty<P> clone() {
         return this;
      }

      public static TransferFunctionInputProperty.ListedProperty<?> of(IProperty<?> property) {
         return of(property == null ? "" : property.getListedName(), property);
      }

      public static TransferFunctionInputProperty.ListedProperty<?> of(String name, IProperty<?> property) {
         if (property == null) {
            return new TransferFunctionInputProperty.ListedProperty(name, (IProperty)null, (BiFunction)null);
         } else if (!property.isListed()) {
            return new TransferFunctionInputProperty.ListedProperty(name, property, (BiFunction)null);
         } else if (property instanceof IDoubleProperty) {
            return new TransferFunctionInputProperty.ListedProperty(name, (IDoubleProperty)property, TransferFunctionInputProperty.PropertySourceDouble::new);
         } else if (property.getDefault() instanceof Double) {
            return new TransferFunctionInputProperty.ListedProperty(name, property, TransferFunctionInputProperty.PropertySourceDoubleBoxed::new);
         } else if (property.getDefault() instanceof Boolean) {
            return new TransferFunctionInputProperty.ListedProperty(name, property, TransferFunctionInputProperty.PropertySourceBool::new);
         } else {
            return (TransferFunctionInputProperty.ListedProperty)(property instanceof IStringSetProperty ? new TransferFunctionInputProperty.ListedPropertyStringSet(name, (IStringSetProperty)property) : new TransferFunctionInputProperty.ListedProperty(name, property, (BiFunction)null));
         }
      }
   }

   private class PropertyOptionsWidget extends MapWidget {
      public final TransferFunction.Dialog dialog;

      public PropertyOptionsWidget(TransferFunction.Dialog dialog) {
         this.dialog = dialog.wrapWidget(this);
      }

      public void onAttached() {
         this.update();
      }

      public void update() {
         if (this.display != null) {
            this.clearWidgets();
            TransferFunctionInputProperty.this.property.addWidgets(this.dialog, TransferFunctionInputProperty.this);
         }

      }
   }

   private static class PropertySourceDouble extends TransferFunctionInput.ReferencedSource {
      public final CartProperties properties;
      public final IDoubleProperty property;

      public PropertySourceDouble(CartProperties properties, TransferFunctionInputProperty.ListedProperty<IDoubleProperty> property) {
         this.properties = properties;
         this.property = (IDoubleProperty)property.property;
      }

      public void onTick() {
         this.value = this.property.getDouble(this.properties);
      }

      public boolean equals(Object o) {
         return o instanceof TransferFunctionInputProperty.PropertySourceDouble && ((TransferFunctionInputProperty.PropertySourceDouble)o).property == this.property;
      }
   }

   private static class PropertySourceDoubleBoxed extends TransferFunctionInput.ReferencedSource {
      public final CartProperties properties;
      public final IProperty<Double> property;

      public PropertySourceDoubleBoxed(CartProperties properties, TransferFunctionInputProperty.ListedProperty<IProperty<Double>> property) {
         this.properties = properties;
         this.property = property.property;
      }

      public void onTick() {
         this.value = (Double)this.property.get(this.properties);
      }

      public boolean equals(Object o) {
         return o instanceof TransferFunctionInputProperty.PropertySourceDoubleBoxed && ((TransferFunctionInputProperty.PropertySourceDoubleBoxed)o).property == this.property;
      }
   }

   private static class PropertySourceBool extends TransferFunctionInput.ReferencedSource {
      public final CartProperties properties;
      public final IProperty<Boolean> property;

      public PropertySourceBool(CartProperties properties, TransferFunctionInputProperty.ListedProperty<IProperty<Boolean>> property) {
         this.properties = properties;
         this.property = property.property;
      }

      public void onTick() {
         this.value = (Boolean)this.property.get(this.properties) ? 1.0D : 0.0D;
      }

      public boolean equals(Object o) {
         return o instanceof TransferFunctionInputProperty.PropertySourceBool && ((TransferFunctionInputProperty.PropertySourceBool)o).property == this.property;
      }
   }

   private static class PropertyStringSet extends TransferFunctionInput.ReferencedSource {
      public final CartProperties properties;
      public final IStringSetProperty property;
      public final boolean train;
      public final String expression;
      private Set<String> previousResult = null;

      public PropertyStringSet(CartProperties properties, TransferFunctionInputProperty.ListedPropertyStringSet property) {
         this.properties = properties;
         this.property = (IStringSetProperty)property.property;
         this.train = property.train;
         this.expression = property.expression;
      }

      public void onTick() {
         IProperties props = this.properties;
         if (this.train) {
            props = this.properties.getTrainProperties();
            if (props == null) {
               this.value = 0.0D;
               return;
            }
         }

         Set<String> allValues = (Set)((IProperties)props).get(this.property);
         if (this.previousResult != allValues) {
            this.previousResult = allValues;
            this.value = Util.matchText((Collection)allValues, this.expression) ? 1.0D : 0.0D;
         }

      }

      public boolean equals(Object o) {
         if (!(o instanceof TransferFunctionInputProperty.PropertyStringSet)) {
            return false;
         } else {
            TransferFunctionInputProperty.PropertyStringSet other = (TransferFunctionInputProperty.PropertyStringSet)o;
            return this.property == other.property && this.expression.equals(other.expression) && this.train == other.train;
         }
      }
   }

   private static class ListedPropertyStringSet extends TransferFunctionInputProperty.ListedProperty<IStringSetProperty> {
      public boolean train = false;
      public String expression = "";

      public ListedPropertyStringSet(String listedName, IStringSetProperty property) {
         super(listedName, property, TransferFunctionInputProperty.PropertyStringSet::new);
      }

      public boolean isBooleanOutput() {
         return true;
      }

      public void load(ConfigurationNode config) {
         this.train = (Boolean)config.getOrDefault("ofTrain", false);
         this.expression = (String)config.getOrDefault("expression", "");
      }

      public void save(ConfigurationNode config) {
         config.set("ofTrain", this.train);
         config.set("expression", this.expression);
      }

      public void addWidgets(final TransferFunction.Dialog dialog, final TransferFunctionInputProperty function) {
         dialog.addLabel(11, 3, (byte)18, "Check " + ((IStringSetProperty)this.property).getListedName() + " of:");
         ((<undefinedtype>)dialog.addWidget(new MapWidgetButton() {
            public void onAttached() {
               this.updateText();
               super.onAttached();
            }

            public void onActivate() {
               ListedPropertyStringSet.this.train = !ListedPropertyStringSet.this.train;
               function.updateSource(dialog.getHost());
               dialog.markChanged();
               this.updateText();
            }

            private void updateText() {
               this.setText(ListedPropertyStringSet.this.train ? "TRAIN" : "CART");
            }
         })).setBounds(11, 10, dialog.getWidth() - 22, 12);
         dialog.addLabel(11, 26, (byte)18, "Filter Expression:");
         ((<undefinedtype>)dialog.addWidget(new MapWidgetInputFilterExpression() {
            public void onChanged(String expression) {
               ListedPropertyStringSet.this.expression = expression;
               function.updateSource(dialog.getHost());
               dialog.markChanged();
            }
         })).setExpression(this.expression).setBounds(11, 33, dialog.getWidth() - 22, 12);
      }

      public TransferFunctionInputProperty.ListedPropertyStringSet clone() {
         TransferFunctionInputProperty.ListedPropertyStringSet clone = new TransferFunctionInputProperty.ListedPropertyStringSet(this.name, (IStringSetProperty)this.property);
         clone.train = this.train;
         clone.expression = this.expression;
         return clone;
      }
   }
}
