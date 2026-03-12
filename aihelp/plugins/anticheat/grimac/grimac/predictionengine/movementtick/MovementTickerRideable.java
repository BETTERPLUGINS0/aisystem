package ac.grim.grimac.predictionengine.movementtick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.utils.data.packetentity.PacketEntityRideable;
import ac.grim.grimac.utils.nmsutil.Collisions;

public class MovementTickerRideable extends MovementTickerLivingVehicle {
   public MovementTickerRideable(GrimPlayer player) {
      super(player);
      float f = this.getSteeringSpeed();
      PacketEntityRideable boost = (PacketEntityRideable)player.compensatedEntities.self.getRiding();
      if (boost.currentBoostTime++ < boost.boostTimeMax) {
         f += f * 1.15F * player.trigHandler.sin((float)boost.currentBoostTime / (float)boost.boostTimeMax * 3.1415927F);
      }

      player.speed = (double)f;
   }

   public float getSteeringSpeed() {
      throw new IllegalStateException("Not implemented");
   }

   public void livingEntityTravel() {
      super.livingEntityTravel();
      if (this.player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17)) {
         Collisions.handleInsideBlocks(this.player);
      }

   }
}
