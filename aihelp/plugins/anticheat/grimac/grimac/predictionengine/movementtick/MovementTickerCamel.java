package ac.grim.grimac.predictionengine.movementtick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.packetentity.PacketEntityCamel;

public class MovementTickerCamel extends MovementTickerHorse {
   public MovementTickerCamel(GrimPlayer player) {
      super(player);
   }

   public float getExtraSpeed() {
      PacketEntityCamel camel = (PacketEntityCamel)this.player.compensatedEntities.self.getRiding();
      boolean wantsToJump = camel.getJumpPower() > 0.0F && !camel.isJumping() && this.player.lastOnGround;
      if (wantsToJump) {
         return 0.0F;
      } else {
         return this.player.isSprinting && camel.getDashCooldown() <= 0 && !camel.isDashing() ? 0.1F : 0.0F;
      }
   }
}
