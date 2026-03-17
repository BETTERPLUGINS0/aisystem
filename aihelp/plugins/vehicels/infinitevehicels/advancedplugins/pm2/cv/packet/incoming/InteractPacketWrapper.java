package advancedplugins.pm2.cv.packet.incoming;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.packet.PacketWrapper;
import org.jetbrains.annotations.Nullable;

@VersionSensible
public class InteractPacketWrapper extends PacketWrapper {
   public final int entityId;
   public final InteractPacketWrapper.Action action;
   @Nullable
   public final Double targetX;
   @Nullable
   public final Double targetY;
   @Nullable
   public final Double targetZ;
   @Nullable
   public final InteractPacketWrapper.Hand hand;
   public boolean sneaking;

   public InteractPacketWrapper(final int entityId, final InteractPacketWrapper.Action action, @Nullable final Double targetX, @Nullable final Double targetY, @Nullable final Double targetZ, @Nullable final InteractPacketWrapper.Hand hand, final boolean sneaking) {
      this.entityId = var1;
      this.action = var2;
      this.targetX = var3;
      this.targetY = var4;
      this.targetZ = var5;
      this.hand = var6;
      this.sneaking = var7;
   }

   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof InteractPacketWrapper)) {
         return false;
      } else {
         InteractPacketWrapper var2 = (InteractPacketWrapper)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.entityId != var2.entityId) {
            return false;
         } else if (this.sneaking != var2.sneaking) {
            return false;
         } else {
            label76: {
               Double var3 = this.targetX;
               Double var4 = var2.targetX;
               if (var3 == null) {
                  if (var4 == null) {
                     break label76;
                  }
               } else if (var3.equals(var4)) {
                  break label76;
               }

               return false;
            }

            Double var5 = this.targetY;
            Double var6 = var2.targetY;
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label62: {
               Double var7 = this.targetZ;
               Double var8 = var2.targetZ;
               if (var7 == null) {
                  if (var8 == null) {
                     break label62;
                  }
               } else if (var7.equals(var8)) {
                  break label62;
               }

               return false;
            }

            label55: {
               InteractPacketWrapper.Action var9 = this.action;
               InteractPacketWrapper.Action var10 = var2.action;
               if (var9 == null) {
                  if (var10 == null) {
                     break label55;
                  }
               } else if (var9.equals(var10)) {
                  break label55;
               }

               return false;
            }

            InteractPacketWrapper.Hand var11 = this.hand;
            InteractPacketWrapper.Hand var12 = var2.hand;
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(final Object other) {
      return var1 instanceof InteractPacketWrapper;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var8 = var2 * 59 + this.entityId;
      var8 = var8 * 59 + (this.sneaking ? 79 : 97);
      Double var3 = this.targetX;
      var8 = var8 * 59 + (var3 == null ? 43 : var3.hashCode());
      Double var4 = this.targetY;
      var8 = var8 * 59 + (var4 == null ? 43 : var4.hashCode());
      Double var5 = this.targetZ;
      var8 = var8 * 59 + (var5 == null ? 43 : var5.hashCode());
      InteractPacketWrapper.Action var6 = this.action;
      var8 = var8 * 59 + (var6 == null ? 43 : var6.hashCode());
      InteractPacketWrapper.Hand var7 = this.hand;
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   public static enum Action {
      INTERACT,
      ATTACK,
      INTERACT_AT;

      // $FF: synthetic method
      private static InteractPacketWrapper.Action[] $values() {
         return new InteractPacketWrapper.Action[]{INTERACT, ATTACK, INTERACT_AT};
      }
   }

   public static enum Hand {
      MAIN_HAND,
      OFF_HAND;

      // $FF: synthetic method
      private static InteractPacketWrapper.Hand[] $values() {
         return new InteractPacketWrapper.Hand[]{MAIN_HAND, OFF_HAND};
      }
   }
}
