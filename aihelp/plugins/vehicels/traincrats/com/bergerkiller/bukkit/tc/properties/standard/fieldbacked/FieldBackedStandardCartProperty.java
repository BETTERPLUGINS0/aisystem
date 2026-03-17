package com.bergerkiller.bukkit.tc.properties.standard.fieldbacked;

import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.ICartProperty;
import com.bergerkiller.bukkit.tc.properties.api.IDoubleProperty;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class FieldBackedStandardCartProperty<T> extends FieldBackedProperty<T> implements ICartProperty<T> {
   public abstract T getData(FieldBackedProperty.CartInternalData var1);

   public abstract void setData(FieldBackedProperty.CartInternalData var1, T var2);

   public void onConfigurationChanged(CartProperties properties) {
      this.setData(FieldBackedProperty.CartInternalData.get(properties), this.readFromConfig(properties.getConfig()).orElseGet(this::getDefault));
   }

   public T get(CartProperties properties) {
      return this.getData(FieldBackedProperty.CartInternalData.get(properties));
   }

   public void set(CartProperties properties, T value) {
      ICartProperty.super.set(properties, value);
      this.setData(FieldBackedProperty.CartInternalData.get(properties), value);
   }

   public static Set<String> combineCartValues(TrainProperties properties, FieldBackedStandardCartProperty<Set<String>> property) {
      if (properties.size() == 1) {
         return (Set)property.get(properties.get(0));
      } else {
         Set<String> result = new HashSet();
         Iterator var3 = properties.iterator();

         while(var3.hasNext()) {
            CartProperties cprop = (CartProperties)var3.next();
            result.addAll((Collection)property.get(cprop));
         }

         return Collections.unmodifiableSet(result);
      }
   }

   public abstract static class StandardDouble extends FieldBackedStandardCartProperty<Double> implements IDoubleProperty {
      public abstract double getDoubleDefault();

      public abstract double getDataDouble(FieldBackedProperty.CartInternalData var1);

      public abstract void setDataDouble(FieldBackedProperty.CartInternalData var1, double var2);

      public final double getDouble(CartProperties properties) {
         return this.getDataDouble(FieldBackedProperty.CartInternalData.get(properties));
      }

      public final double getDouble(TrainProperties properties) {
         return properties.isEmpty() ? this.getDoubleDefault() : this.getDouble(properties.get(0));
      }

      public Double getDefault() {
         return this.getDoubleDefault();
      }

      public Double get(CartProperties properties) {
         return this.getDouble(properties);
      }

      public Double get(TrainProperties properties) {
         return this.getDouble(properties);
      }

      public Double getData(FieldBackedProperty.CartInternalData holder) {
         return this.getDataDouble(holder);
      }

      public void setData(FieldBackedProperty.CartInternalData holder, Double value) {
         this.setDataDouble(holder, value);
      }
   }
}
