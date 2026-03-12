package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.data.attribute.ValuedAttribute;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.math.Vector3dm;
import ac.grim.grimac.utils.nmsutil.JumpPower;
import java.util.Iterator;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

public class PacketEntityHorse extends PacketEntityTrackXRot implements JumpableEntity {
   public boolean isRearing = false;
   public boolean hasSaddle = false;
   public boolean isTame = false;
   private boolean horseJumping = false;
   private float horseJump = 0.0F;
   private static final boolean HAS_SADDLE_SENT_BY_SERVER;

   public PacketEntityHorse(GrimPlayer player, UUID uuid, EntityType type, double x, double y, double z, float xRot) {
      super(player, uuid, type, x, y, z, xRot);
      this.trackEntityEquipment = true;
      this.setAttribute(Attributes.STEP_HEIGHT, 1.0D);
      boolean preAttribute = player.getClientVersion().isOlderThan(ClientVersion.V_1_20_5);
      this.trackAttribute(ValuedAttribute.ranged(Attributes.JUMP_STRENGTH, 0.7D, 0.0D, preAttribute ? 2.0D : 32.0D).withSetRewriter((oldValue, newValue) -> {
         return preAttribute && PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5) ? oldValue : newValue;
      }));
      this.trackAttribute(ValuedAttribute.ranged(Attributes.MOVEMENT_SPEED, 0.22499999403953552D, 0.0D, 1024.0D));
      if (EntityTypes.isTypeInstanceOf(type, EntityTypes.CHESTED_HORSE)) {
         this.setAttribute(Attributes.JUMP_STRENGTH, 0.5D);
         this.setAttribute(Attributes.MOVEMENT_SPEED, 0.17499999701976776D);
      }

      if (type == EntityTypes.ZOMBIE_HORSE || type == EntityTypes.SKELETON_HORSE) {
         this.setAttribute(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
      }

   }

   public boolean hasSaddle() {
      return HAS_SADDLE_SENT_BY_SERVER ? this.hasSaddle : this.hasItemInSlot(EquipmentSlot.SADDLE);
   }

   public boolean isJumping() {
      return this.horseJumping;
   }

   public void setJumping(boolean jumping) {
      this.horseJumping = jumping;
   }

   public float getJumpPower() {
      return this.horseJump;
   }

   public void setJumpPower(float jumpPower) {
      this.horseJump = jumpPower;
   }

   public boolean canPlayerJump(GrimPlayer player) {
      return this.hasSaddle();
   }

   public void executeJump(GrimPlayer player, Set<VectorData> possibleVectors) {
      boolean wantsToJump = this.getJumpPower() > 0.0F && !this.isJumping() && player.lastOnGround;
      if (wantsToJump) {
         float forwardInput = player.vehicleData.vehicleForward;
         if (forwardInput <= 0.0F) {
            forwardInput *= 0.25F;
         }

         double jumpFactor = (double)((float)this.getAttributeValue(Attributes.JUMP_STRENGTH) * this.getJumpPower() * JumpPower.getPlayerJumpFactor(player));
         OptionalInt jumpBoost = player.compensatedEntities.getPotionLevelForPlayer(PotionTypes.JUMP_BOOST);
         double jumpVelocity;
         if (jumpBoost.isPresent()) {
            jumpVelocity = jumpFactor + (double)((float)(jumpBoost.getAsInt() + 1) * 0.1F);
         } else {
            jumpVelocity = jumpFactor;
         }

         this.setJumping(true);
         float yawRadians = GrimMath.radians(player.yaw);
         float f2 = player.trigHandler.sin(yawRadians);
         float f3 = player.trigHandler.cos(yawRadians);
         Iterator var13 = possibleVectors.iterator();

         while(var13.hasNext()) {
            VectorData vectorData = (VectorData)var13.next();
            vectorData.vector.setY(jumpVelocity);
            if (forwardInput > 0.0F) {
               vectorData.vector.add(new Vector3dm((double)(-0.4F * f2 * this.getJumpPower()), 0.0D, (double)(0.4F * f3 * this.getJumpPower())));
            }
         }

         this.setJumpPower(0.0F);
      }
   }

   static {
      HAS_SADDLE_SENT_BY_SERVER = PacketEvents.getAPI().getServerManager().getVersion().isOlderThanOrEquals(ServerVersion.V_1_21_4);
   }
}
