package com.bergerkiller.bukkit.tc.properties.api;

import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;

public interface IDoubleProperty extends IProperty<Double> {
   double getDoubleDefault();

   double getDouble(CartProperties var1);

   double getDouble(TrainProperties var1);

   default Double getDefault() {
      return this.getDoubleDefault();
   }

   default Double get(CartProperties properties) {
      return this.getDouble(properties);
   }

   default Double get(TrainProperties properties) {
      return this.getDouble(properties);
   }
}
