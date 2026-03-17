package advancedplugins.pm2.cv.models.api.model.rpc.joint.type;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface PlayerLimb {
   void setTexture(@Nullable Player var1);

   void setTexture(@Nullable PlayerProfile var1);

   PlayerLimb.Limb getLimbType();

   public static enum Limb {
      HEAD(0, 1, 0, 1),
      RIGHT_ARM(1, 2, 3, 7),
      LEFT_ARM(2, 3, 4, 8),
      BODY(5, 4, 5, 4),
      RIGHT_LEG(6, 5, 6, 5),
      LEFT_LEG(7, 6, 7, 6);

      public final float defaultYOffset;
      public final int defaultId;
      public final float slimYOffset;
      public final int slimId;

      private Limb(int param3, int param4, int param5, int param6) {
         this.defaultYOffset = (float)(var3 * -1024);
         this.defaultId = var4;
         this.slimYOffset = (float)(var5 * -1024);
         this.slimId = var6;
      }

      private static PlayerLimb.Limb[] $values() {
         return new PlayerLimb.Limb[]{HEAD, RIGHT_ARM, LEFT_ARM, BODY, RIGHT_LEG, LEFT_LEG};
      }

      // $FF: synthetic method
      private static PlayerLimb.Limb[] $values$() {
         return new PlayerLimb.Limb[]{HEAD, RIGHT_ARM, LEFT_ARM, BODY, RIGHT_LEG, LEFT_LEG};
      }
   }
}
