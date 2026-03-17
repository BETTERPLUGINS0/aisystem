package com.bergerkiller.bukkit.tc.properties.api.context;

import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;

public class PropertyContext implements TrainCarts.Provider {
   private final TrainCarts traincarts;
   private final IProperties properties;

   public PropertyContext(TrainCarts traincarts, IProperties properties) {
      this.traincarts = traincarts;
      this.properties = properties;
   }

   public TrainCarts getTrainCarts() {
      return this.traincarts;
   }

   public CartProperties cartProperties() {
      return this.isCartProperties() ? (CartProperties)this.properties : null;
   }

   public TrainProperties trainProperties() {
      if (this.isTrainProperties()) {
         return (TrainProperties)this.properties;
      } else {
         return this.isCartProperties() ? ((CartProperties)this.properties).getTrainProperties() : null;
      }
   }

   public boolean isCartProperties() {
      return this.properties instanceof CartProperties;
   }

   public boolean isTrainProperties() {
      return this.properties instanceof TrainProperties;
   }
}
