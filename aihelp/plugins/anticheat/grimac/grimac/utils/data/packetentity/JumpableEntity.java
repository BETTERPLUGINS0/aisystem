package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.VectorData;
import java.util.Set;

public interface JumpableEntity {
   boolean isJumping();

   void setJumping(boolean var1);

   float getJumpPower();

   void setJumpPower(float var1);

   boolean canPlayerJump(GrimPlayer var1);

   boolean hasSaddle();

   void executeJump(GrimPlayer var1, Set<VectorData> var2);
}
