package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.BlockProperties;
import ac.grim.grimac.utils.nmsutil.ReachUtils;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class PacketEntityNautilus extends PacketEntity implements JumpableEntity, DashableEntity {
   private boolean jumping = false;
   private float jumpPower = 0.0F;
   private boolean dashing = false;
   private int dashCooldown = 0;

   public PacketEntityNautilus(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z) {
      super(player, uuid, type, x, y, z);
      this.trackEntityEquipment = true;
      this.trackAttribute(ValuedAttribute.ranged(Attributes.MOVEMENT_SPEED, 1.0D, 0.0D, 1024.0D));
      this.setAttribute(Attributes.STEP_HEIGHT, 1.0D);
   }

   public boolean isJumping() {
      return this.jumping;
   }

   public void setJumping(boolean jumping) {
      this.jumping = jumping;
   }

   public float getJumpPower() {
      return this.jumpPower;
   }

   public void setJumpPower(float jumpPower) {
      this.jumpPower = jumpPower;
   }

   public boolean canPlayerJump(GrimPlayer player) {
      return this.hasSaddle() && this.dashCooldown <= 0;
   }

   public boolean hasSaddle() {
      return this.hasItemInSlot(EquipmentSlot.SADDLE);
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
      boolean wantsToJump = this.getJumpPower() > 0.0F && !this.isJumping();
      if (wantsToJump) {
         float pitch = player.vehicleData.playerPitch;
         float yaw = player.vehicleData.playerYaw;
         double multiplier = this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (double)BlockProperties.getBlockSpeedFactor(player, player.mainSupportingBlockData, new Vector3d(player.lastX, player.lastY, player.lastZ));
         Vector3dm jumpVelocity = ReachUtils.getLook(player, yaw, pitch).multiply((double)((player.wasTouchingWater ? 1.2F : 0.5F) * this.getJumpPower()) * multiplier);
         Iterator var9 = possibleVectors.iterator();

         while(var9.hasNext()) {
            VectorData vectorData = (VectorData)var9.next();
            vectorData.vector.add(jumpVelocity);
         }

         this.setDashing(true);
         this.setDashCooldown(40);
         this.setJumpPower(0.0F);
      }
   }
}
