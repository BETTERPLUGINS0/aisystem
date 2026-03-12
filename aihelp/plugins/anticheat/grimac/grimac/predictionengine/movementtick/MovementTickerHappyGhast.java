package ac.grim.grimac.predictionengine.movementtick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.rideable.PredictionEngineHappyGhast;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.utils.data.packetentity.PacketEntityHappyGhast;
import ac.grim.grimac.utils.math.Vector3dm;

public class MovementTickerHappyGhast extends MovementTickerLivingVehicle {
   public MovementTickerHappyGhast(GrimPlayer player) {
      super(player);
      PacketEntityHappyGhast happyGhastPacket = (PacketEntityHappyGhast)player.compensatedEntities.self.getRiding();
      if (happyGhastPacket.isControllingPassenger()) {
         player.speed = (double)((float)happyGhastPacket.getAttributeValue(Attributes.FLYING_SPEED) * 5.0F / 3.0F);
         float sideways = player.vehicleData.vehicleHorizontal;
         float forward = 0.0F;
         float upAndDown = 0.0F;
         if (player.vehicleData.vehicleForward != 0.0F) {
            float xRot = player.pitch * 2.0F;
            float calcForward = player.trigHandler.cos(xRot * 0.017453292F);
            float calcUpAndDown = -player.trigHandler.sin(xRot * 0.017453292F);
            if (player.vehicleData.vehicleForward < 0.0F) {
               calcForward *= -0.5F;
               calcUpAndDown *= -0.5F;
            }

            upAndDown = calcUpAndDown;
            forward = calcForward;
         }

         if (player.lastJumping) {
            upAndDown += 0.5F;
         }

         this.movementInput = (new Vector3dm(sideways, upAndDown, forward)).multiply(3.9000000953674316D * happyGhastPacket.getAttributeValue(Attributes.FLYING_SPEED));
         if (this.movementInput.lengthSquared() > 1.0D) {
            this.movementInput.normalize();
         }

      }
   }

   public void doNormalMove(float blockFriction) {
      PacketEntityHappyGhast happyGhastPacket = (PacketEntityHappyGhast)this.player.compensatedEntities.self.getRiding();
      float flyingSpeed = (float)happyGhastPacket.getAttributeValue(Attributes.FLYING_SPEED) * 5.0F / 3.0F;
      (new PredictionEngineHappyGhast(this.movementInput, 0.9100000262260437D)).guessBestMovement(flyingSpeed, this.player);
   }

   public void doLavaMove() {
      PacketEntityHappyGhast happyGhastPacket = (PacketEntityHappyGhast)this.player.compensatedEntities.self.getRiding();
      float flyingSpeed = (float)happyGhastPacket.getAttributeValue(Attributes.FLYING_SPEED) * 5.0F / 3.0F;
      (new PredictionEngineHappyGhast(this.movementInput, 0.5D)).guessBestMovement(flyingSpeed, this.player);
   }

   public void doWaterMove(float swimSpeed, boolean isFalling, float swimFriction) {
      PacketEntityHappyGhast happyGhastPacket = (PacketEntityHappyGhast)this.player.compensatedEntities.self.getRiding();
      float flyingSpeed = (float)happyGhastPacket.getAttributeValue(Attributes.FLYING_SPEED) * 5.0F / 3.0F;
      (new PredictionEngineHappyGhast(this.movementInput, 0.800000011920929D)).guessBestMovement(flyingSpeed, this.player);
   }
}
