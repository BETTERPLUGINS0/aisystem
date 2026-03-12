package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.BlockProperties;
import ac.grim.grimac.utils.nmsutil.JumpPower;
import ac.grim.grimac.utils.nmsutil.ReachUtils;
import java.util.Iterator;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

public class PacketEntityCamel extends PacketEntityHorse implements DashableEntity {
   private boolean dashing = false;
   private int dashCooldown = 0;

   public PacketEntityCamel(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z, float xRot) {
      super(player, uuid, type, x, y, z, xRot);
      this.setAttribute(Attributes.JUMP_STRENGTH, 0.41999998688697815D);
      this.setAttribute(Attributes.MOVEMENT_SPEED, 0.09000000357627869D);
      this.setAttribute(Attributes.STEP_HEIGHT, 1.5D);
   }

   public boolean canPlayerJump(GrimPlayer player) {
      return this.hasSaddle() && this.dashCooldown <= 0 && player.onGround;
   }

   public boolean isDashing() {
      return this.dashing;
   }

   public void setDashing(boolean dashing) {
      this.dashing = dashing;
   }

   public int getDashCooldown() {
      return this.dashCooldown;
   }

   public void setDashCooldown(int dashCooldown) {
      this.dashCooldown = dashCooldown;
   }

   public void executeJump(GrimPlayer player, Set<VectorData> possibleVectors) {
      boolean wantsToJump = this.getJumpPower() > 0.0F && !this.isJumping() && player.lastOnGround;
      if (wantsToJump) {
         double jumpFactor = this.getAttributeValue(Attributes.JUMP_STRENGTH) * (double)JumpPower.getPlayerJumpFactor(player);
         OptionalInt jumpBoost = player.compensatedEntities.getPotionLevelForPlayer(PotionTypes.JUMP_BOOST);
         double jumpYVelocity;
         if (jumpBoost.isPresent()) {
            jumpYVelocity = jumpFactor + (double)((float)(jumpBoost.getAsInt() + 1) * 0.1F);
         } else {
            jumpYVelocity = jumpFactor;
         }

         double multiplier = (double)(22.2222F * this.getJumpPower()) * this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (double)BlockProperties.getBlockSpeedFactor(player, player.mainSupportingBlockData, new Vector3d(player.lastX, player.lastY, player.lastZ));
         Vector3dm jumpVelocity = ReachUtils.getLook(player, player.yaw, player.pitch).multiply(1.0D, 0.0D, 1.0D).normalize().multiply(multiplier).add(0.0D, (double)(1.4285F * this.getJumpPower()) * jumpYVelocity, 0.0D);
         Iterator var12 = possibleVectors.iterator();

         while(var12.hasNext()) {
            VectorData vectorData = (VectorData)var12.next();
            vectorData.vector.add(jumpVelocity);
         }

         this.setDashing(true);
         this.setDashCooldown(55);
         this.setJumpPower(0.0F);
      }
   }
}
