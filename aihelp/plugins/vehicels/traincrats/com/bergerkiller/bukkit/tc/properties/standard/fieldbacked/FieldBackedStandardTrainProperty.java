package com.bergerkiller.bukkit.tc.properties.standard.fieldbacked;

import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.IDoubleProperty;
import com.bergerkiller.bukkit.tc.properties.api.ITrainProperty;

public abstract class FieldBackedStandardTrainProperty<T> extends FieldBackedProperty<T> implements ITrainProperty<T> {
   public abstract T getData(FieldBackedProperty.TrainInternalData var1);

   public abstract void setData(FieldBackedProperty.TrainInternalData var1, T var2);

   public void onConfigurationChanged(TrainProperties properties) {
      this.setData(FieldBackedProperty.TrainInternalData.get(properties), this.readFromConfig(properties.getConfig()).orElseGet(this::getDefault));
   }

   public T get(TrainProperties properties) {
      return this.getData(FieldBackedProperty.TrainInternalData.get(properties));
   }

   public void set(TrainProperties properties, T value) {
      ITrainProperty.super.set(properties, value);
      this.setData(FieldBackedProperty.TrainInternalData.get(properties), value);
   }

   public abstract static class StandardDouble extends FieldBackedStandardTrainProperty<Double> implements IDoubleProperty {
      public abstract double getDoubleDefault();

      public abstract double getDoubleData(FieldBackedProperty.TrainInternalData var1);

      public abstract void setDoubleData(FieldBackedProperty.TrainInternalData var1, double var2);

      public final double getDouble(TrainProperties properties) {
         return this.getDoubleData(FieldBackedProperty.TrainInternalData.get(properties));
      }

      public final double getDouble(CartProperties properties) {
         return this.getDouble(properties.getTrainProperties());
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

      public Double getData(FieldBackedProperty.TrainInternalData holder) {
         return this.getDoubleData(holder);
      }

      public void setData(FieldBackedProperty.TrainInternalData holder, Double value) {
         this.setDoubleData(holder, value);
      }
   }
}
