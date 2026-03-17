package com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.implementation;

import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.Feature;
import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.NetworkInterface;
import java.util.EnumSet;
import org.bukkit.entity.Player;

public interface Implementation {
   default boolean isSupported(Feature feature) {
      return this.getFeatures().contains(feature);
   }

   EnumSet<Feature> getFeatures();

   byte getVersion();

   default void sendRotation(NetworkInterface network, Player player, float x, float y, float z, float w, byte ticks) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default void sendEntityRotation(NetworkInterface network, Player player, int entity, float x, float y, float z, float w, byte ticks) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default void sendEntityProperties(NetworkInterface network, Player player, int entity, byte ticks) {
      throw new UnsupportedOperationException();
   }

   default void sendRotationLimit(NetworkInterface network, Player player, float minYaw, float maxYaw, float minPitch, float maxPitch) {
      throw new UnsupportedOperationException();
   }
}
