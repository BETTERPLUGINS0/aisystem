package fr.xephi.authme.data.limbo;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import org.bukkit.entity.Player;

public enum WalkFlySpeedRestoreType {
   RESTORE {
      public void restoreFlySpeed(Player player, LimboPlayer limbo) {
         WalkFlySpeedRestoreType.logger.debug(() -> {
            return "Restoring fly speed for LimboPlayer " + player.getName() + " to " + limbo.getFlySpeed() + " (RESTORE mode)";
         });
         player.setFlySpeed(limbo.getFlySpeed());
      }

      public void restoreWalkSpeed(Player player, LimboPlayer limbo) {
         WalkFlySpeedRestoreType.logger.debug(() -> {
            return "Restoring walk speed for LimboPlayer " + player.getName() + " to " + limbo.getWalkSpeed() + " (RESTORE mode)";
         });
         player.setWalkSpeed(limbo.getWalkSpeed());
      }
   },
   RESTORE_NO_ZERO {
      public void restoreFlySpeed(Player player, LimboPlayer limbo) {
         float limboFlySpeed = limbo.getFlySpeed();
         if (limboFlySpeed > 0.01F) {
            WalkFlySpeedRestoreType.logger.debug(() -> {
               return "Restoring fly speed for LimboPlayer " + player.getName() + " to " + limboFlySpeed + " (RESTORE_NO_ZERO mode)";
            });
            player.setFlySpeed(limboFlySpeed);
         } else {
            WalkFlySpeedRestoreType.logger.debug(() -> {
               return "Restoring fly speed for LimboPlayer " + player.getName() + " to DEFAULT, it was 0! (RESTORE_NO_ZERO mode)";
            });
            player.setFlySpeed(0.1F);
         }

      }

      public void restoreWalkSpeed(Player player, LimboPlayer limbo) {
         float limboWalkSpeed = limbo.getWalkSpeed();
         if (limboWalkSpeed > 0.01F) {
            WalkFlySpeedRestoreType.logger.debug(() -> {
               return "Restoring walk speed for LimboPlayer " + player.getName() + " to " + limboWalkSpeed + " (RESTORE_NO_ZERO mode)";
            });
            player.setWalkSpeed(limboWalkSpeed);
         } else {
            WalkFlySpeedRestoreType.logger.debug(() -> {
               return "Restoring walk speed for LimboPlayer " + player.getName() + " to DEFAULT, it was 0! (RESTORE_NO_ZERO mode)";
            });
            player.setWalkSpeed(0.2F);
         }

      }
   },
   MAX_RESTORE {
      public void restoreFlySpeed(Player player, LimboPlayer limbo) {
         float newSpeed = Math.max(player.getFlySpeed(), limbo.getFlySpeed());
         WalkFlySpeedRestoreType.logger.debug(() -> {
            return "Restoring fly speed for LimboPlayer " + player.getName() + " to " + newSpeed + " (Current: " + player.getFlySpeed() + ", Limbo: " + limbo.getFlySpeed() + ") (MAX_RESTORE mode)";
         });
         player.setFlySpeed(newSpeed);
      }

      public void restoreWalkSpeed(Player player, LimboPlayer limbo) {
         float newSpeed = Math.max(player.getWalkSpeed(), limbo.getWalkSpeed());
         WalkFlySpeedRestoreType.logger.debug(() -> {
            return "Restoring walk speed for LimboPlayer " + player.getName() + " to " + newSpeed + " (Current: " + player.getWalkSpeed() + ", Limbo: " + limbo.getWalkSpeed() + ") (MAX_RESTORE mode)";
         });
         player.setWalkSpeed(newSpeed);
      }
   },
   DEFAULT {
      public void restoreFlySpeed(Player player, LimboPlayer limbo) {
         WalkFlySpeedRestoreType.logger.debug(() -> {
            return "Restoring fly speed for LimboPlayer " + player.getName() + " to DEFAULT (DEFAULT mode)";
         });
         player.setFlySpeed(0.1F);
      }

      public void restoreWalkSpeed(Player player, LimboPlayer limbo) {
         WalkFlySpeedRestoreType.logger.debug(() -> {
            return "Restoring walk speed for LimboPlayer " + player.getName() + " to DEFAULT (DEFAULT mode)";
         });
         player.setWalkSpeed(0.2F);
      }
   };

   private static final ConsoleLogger logger = ConsoleLoggerFactory.get(WalkFlySpeedRestoreType.class);

   private WalkFlySpeedRestoreType() {
   }

   public abstract void restoreFlySpeed(Player var1, LimboPlayer var2);

   public abstract void restoreWalkSpeed(Player var1, LimboPlayer var2);

   // $FF: synthetic method
   private static WalkFlySpeedRestoreType[] $values() {
      return new WalkFlySpeedRestoreType[]{RESTORE, RESTORE_NO_ZERO, MAX_RESTORE, DEFAULT};
   }

   // $FF: synthetic method
   WalkFlySpeedRestoreType(Object x2) {
      this();
   }
}
