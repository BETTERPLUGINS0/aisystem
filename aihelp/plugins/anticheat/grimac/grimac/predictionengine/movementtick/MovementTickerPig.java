package ac.grim.grimac.predictionengine.movementtick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.attribute.Attributes;
import ac.grim.grimac.utils.data.packetentity.PacketEntityRideable;
import ac.grim.grimac.utils.math.Vector3dm;

public class MovementTickerPig extends MovementTickerRideable {
   public MovementTickerPig(GrimPlayer player) {
      super(player);
      this.movementInput = new Vector3dm(0, 0, 1);
   }

   public float getSteeringSpeed() {
      PacketEntityRideable pig = (PacketEntityRideable)this.player.compensatedEntities.self.getRiding();
      return (float)pig.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.225F;
   }
}
