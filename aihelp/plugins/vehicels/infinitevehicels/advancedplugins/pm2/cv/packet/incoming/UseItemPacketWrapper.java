package advancedplugins.pm2.cv.packet.incoming;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.packet.PacketWrapper;
import org.jetbrains.annotations.NotNull;

@VersionSensible
public class UseItemPacketWrapper extends PacketWrapper {
   @NotNull
   public final UseItemPacketWrapper.Hand hand;
   public final int sequence;

   public UseItemPacketWrapper(@NotNull final UseItemPacketWrapper.Hand hand, final int sequence) {
      this.hand = var1;
      this.sequence = var2;
   }

   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof UseItemPacketWrapper)) {
         return false;
      } else {
         UseItemPacketWrapper var2 = (UseItemPacketWrapper)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.sequence != var2.sequence) {
            return false;
         } else {
            UseItemPacketWrapper.Hand var3 = this.hand;
            UseItemPacketWrapper.Hand var4 = var2.hand;
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
      return var1 instanceof UseItemPacketWrapper;
   }

   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.sequence;
      UseItemPacketWrapper.Hand var3 = this.hand;
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   public static enum Hand {
      MAIN_HAND,
      OFF_HAND;

      // $FF: synthetic method
      private static UseItemPacketWrapper.Hand[] $values() {
         return new UseItemPacketWrapper.Hand[]{MAIN_HAND, OFF_HAND};
      }
   }
}
