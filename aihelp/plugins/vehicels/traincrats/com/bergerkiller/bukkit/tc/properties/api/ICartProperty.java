package com.bergerkiller.bukkit.tc.properties.api;

import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import java.util.Iterator;
import java.util.Optional;

public interface ICartProperty<T> extends IProperty<T> {
   default T get(CartProperties properties) {
      return this.readFromConfig(properties.getConfig()).orElseGet(this::getDefault);
   }

   default T get(TrainProperties properties) {
      return properties.isEmpty() ? this.getDefault() : this.get(properties.get(0));
   }

   default void set(TrainProperties properties, T value) {
      Iterator var3 = properties.iterator();

      while(var3.hasNext()) {
         CartProperties cProp = (CartProperties)var3.next();
         this.set(cProp, value);
      }

   }

   default void set(CartProperties properties, T value) {
      if (value != null && !value.equals(this.getDefault())) {
         this.writeToConfig(properties.getConfig(), Optional.of(value));
      } else {
         this.writeToConfig(properties.getConfig(), Optional.empty());
      }

      properties.tryUpdate();
   }
}
