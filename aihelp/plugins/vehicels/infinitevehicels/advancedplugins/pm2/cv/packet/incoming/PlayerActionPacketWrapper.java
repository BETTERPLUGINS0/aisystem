package advancedplugins.pm2.cv.packet.incoming;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.packet.PacketWrapper;

@VersionSensible
public class PlayerActionPacketWrapper extends PacketWrapper {
   public final int positionX;
   public final int positionY;
   public final int positionZ;
   public final PlayerActionPacketWrapper.Direction direction;
   public final PlayerActionPacketWrapper.Action action;
   public final int sequence;

   public PlayerActionPacketWrapper(final int positionX, final int positionY, final int positionZ, final PlayerActionPacketWrapper.Direction direction, final PlayerActionPacketWrapper.Action action, final int sequence) {
      this.positionX = var1;
      this.positionY = var2;
      this.positionZ = var3;
      this.direction = var4;
      this.action = var5;
      this.sequence = var6;
   }

   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof PlayerActionPacketWrapper)) {
         return false;
      } else {
         PlayerActionPacketWrapper var2 = (PlayerActionPacketWrapper)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.positionX != var2.positionX) {
            return false;
         } else if (this.positionY != var2.positionY) {
            return false;
         } else if (this.positionZ != var2.positionZ) {
            return false;
         } else if (this.sequence != var2.sequence) {
            return false;
         } else {
            PlayerActionPacketWrapper.Direction var3 = this.direction;
            PlayerActionPacketWrapper.Direction var4 = var2.direction;
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            PlayerActionPacketWrapper.Action var5 = this.action;
            PlayerActionPacketWrapper.Action var6 = var2.action;
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(final Object other) {
      return var1 instanceof PlayerActionPacketWrapper;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + this.positionX;
      var5 = var5 * 59 + this.positionY;
      var5 = var5 * 59 + this.positionZ;
      var5 = var5 * 59 + this.sequence;
      PlayerActionPacketWrapper.Direction var3 = this.direction;
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      PlayerActionPacketWrapper.Action var4 = this.action;
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   public static enum Direction {
      DOWN,
      UP,
      NORTH,
      SOUTH,
      WEST,
      EAST;

      // $FF: synthetic method
      private static PlayerActionPacketWrapper.Direction[] $values() {
         return new PlayerActionPacketWrapper.Direction[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
      }
   }

   public static enum Action {
      START_DESTROY_BLOCK,
      ABORT_DESTROY_BLOCK,
      STOP_DESTROY_BLOCK,
      DROP_ALL_ITEMS,
      DROP_ITEM,
      RELEASE_USE_ITEM,
      SWAP_ITEM_WITH_OFFHAND;

      // $FF: synthetic method
      private static PlayerActionPacketWrapper.Action[] $values() {
         return new PlayerActionPacketWrapper.Action[]{START_DESTROY_BLOCK, ABORT_DESTROY_BLOCK, STOP_DESTROY_BLOCK, DROP_ALL_ITEMS, DROP_ITEM, RELEASE_USE_ITEM, SWAP_ITEM_WITH_OFFHAND};
      }
   }
}
