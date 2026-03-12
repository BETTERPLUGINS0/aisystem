package fr.xephi.authme.data.limbo;

import org.bukkit.entity.Player;

public enum AllowFlightRestoreType {
   RESTORE {
      public void restoreAllowFlight(Player player, LimboPlayer limbo) {
         player.setAllowFlight(limbo.isCanFly());
      }
   },
   ENABLE {
      public void restoreAllowFlight(Player player, LimboPlayer limbo) {
         player.setAllowFlight(true);
      }
   },
   DISABLE {
      public void restoreAllowFlight(Player player, LimboPlayer limbo) {
         player.setAllowFlight(false);
      }
   },
   NOTHING {
      public void restoreAllowFlight(Player player, LimboPlayer limbo) {
      }

      public void processPlayer(Player player) {
      }
   };

   private AllowFlightRestoreType() {
   }

   public abstract void restoreAllowFlight(Player var1, LimboPlayer var2);

   public void processPlayer(Player player) {
      player.setAllowFlight(false);
   }

   // $FF: synthetic method
   private static AllowFlightRestoreType[] $values() {
      return new AllowFlightRestoreType[]{RESTORE, ENABLE, DISABLE, NOTHING};
   }

   // $FF: synthetic method
   AllowFlightRestoreType(Object x2) {
      this();
   }
}
