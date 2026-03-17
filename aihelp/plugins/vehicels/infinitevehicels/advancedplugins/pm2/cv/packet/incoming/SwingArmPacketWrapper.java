package advancedplugins.pm2.cv.packet.incoming;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.packet.PacketWrapper;
import org.jetbrains.annotations.NotNull;

@VersionSensible
public class SwingArmPacketWrapper extends PacketWrapper {
   @NotNull
   public final SwingArmPacketWrapper.Hand hand;

   public SwingArmPacketWrapper(@NotNull final SwingArmPacketWrapper.Hand hand) {
      this.hand = var1;
   }

   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof SwingArmPacketWrapper)) {
         return false;
      } else {
         SwingArmPacketWrapper var2 = (SwingArmPacketWrapper)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            SwingArmPacketWrapper.Hand var3 = this.hand;
            SwingArmPacketWrapper.Hand var4 = var2.hand;
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   protected boolean canEqual(final Object other) {
      return var1 instanceof SwingArmPacketWrapper;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      SwingArmPacketWrapper.Hand var3 = this.hand;
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   public static enum Hand {
      MAIN_HAND,
      OFF_HAND;

      // $FF: synthetic method
      private static SwingArmPacketWrapper.Hand[] $values() {
         return new SwingArmPacketWrapper.Hand[]{MAIN_HAND, OFF_HAND};
      }
   }
}
