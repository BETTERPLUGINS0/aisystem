package com.bergerkiller.bukkit.tc.properties.api;

import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import java.util.Optional;

public interface ITrainProperty<T> extends IProperty<T> {
   default T get(TrainProperties properties) {
      return this.readFromConfig(properties.getConfig()).orElseGet(this::getDefault);
   }

   default T get(CartProperties properties) {
      return this.get(properties.getTrainProperties());
   }

   default void set(CartProperties properties, T value) {
      this.set(properties.getTrainProperties(), value);
   }

   default void set(TrainProperties properties, T value) {
      if (value != null && !value.equals(this.getDefault())) {
         this.writeToConfig(properties.getConfig(), Optional.of(value));
      } else {
         this.writeToConfig(properties.getConfig(), Optional.empty());
      }

      properties.tryUpdate();
   }
}
