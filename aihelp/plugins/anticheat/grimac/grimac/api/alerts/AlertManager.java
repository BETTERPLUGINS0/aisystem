package ac.grim.grimac.api.alerts;

import ac.grim.grimac.api.GrimUser;
import lombok.NonNull;
import org.bukkit.entity.Player;

public interface AlertManager {
   boolean hasAlertsEnabled(@NonNull GrimUser var1);

   default boolean toggleAlerts(@NonNull GrimUser player) {
      if (player == null) {
         throw new NullPointerException("player is marked non-null but is null");
      } else {
         return this.toggleAlerts(player, false);
      }
   }

   default boolean toggleAlerts(@NonNull GrimUser player, boolean silent) {
      if (player == null) {
         throw new NullPointerException("player is marked non-null but is null");
      } else {
         boolean newState = !this.hasAlertsEnabled(player);
         this.setAlertsEnabled(player, newState, silent);
         return newState;
      }
   }

   default void setAlertsEnabled(@NonNull GrimUser player, boolean enabled) {
      if (player == null) {
         throw new NullPointerException("player is marked non-null but is null");
      } else {
         this.setAlertsEnabled(player, enabled, false);
      }
   }

   void setAlertsEnabled(@NonNull GrimUser var1, boolean var2, boolean var3);

   boolean hasVerboseEnabled(@NonNull GrimUser var1);

   default boolean toggleVerbose(@NonNull GrimUser player) {
      if (player == null) {
         throw new NullPointerException("player is marked non-null but is null");
      } else {
         return this.toggleVerbose(player, false);
      }
   }

   default boolean toggleVerbose(@NonNull GrimUser player, boolean silent) {
      if (player == null) {
         throw new NullPointerException("player is marked non-null but is null");
      } else {
         boolean newState = !this.hasVerboseEnabled(player);
         this.setVerboseEnabled(player, newState, silent);
         return newState;
      }
   }

   default void setVerboseEnabled(@NonNull GrimUser player, boolean enabled) {
      if (player == null) {
         throw new NullPointerException("player is marked non-null but is null");
      } else {
         this.setVerboseEnabled(player, enabled, false);
      }
   }

   void setVerboseEnabled(@NonNull GrimUser var1, boolean var2, boolean var3);

   boolean hasBrandsEnabled(@NonNull GrimUser var1);

   default boolean toggleBrands(@NonNull GrimUser player) {
      if (player == null) {
         throw new NullPointerException("player is marked non-null but is null");
      } else {
         return this.toggleBrands(player, false);
      }
   }

   default boolean toggleBrands(@NonNull GrimUser player, boolean silent) {
      if (player == null) {
         throw new NullPointerException("player is marked non-null but is null");
      } else {
         boolean newState = !this.hasBrandsEnabled(player);
         this.setBrandsEnabled(player, newState, silent);
         return newState;
      }
   }

   default void setBrandsEnabled(@NonNull GrimUser player, boolean enabled) {
      if (player == null) {
         throw new NullPointerException("player is marked non-null but is null");
      } else {
         this.setBrandsEnabled(player, enabled, false);
      }
   }

   void setBrandsEnabled(@NonNull GrimUser var1, boolean var2, boolean var3);

   /** @deprecated */
   @Deprecated
   boolean hasAlertsEnabled(Player var1);

   /** @deprecated */
   @Deprecated
   void toggleAlerts(Player var1);

   /** @deprecated */
   @Deprecated
   boolean hasVerboseEnabled(Player var1);

   /** @deprecated */
   @Deprecated
   void toggleVerbose(Player var1);
}
