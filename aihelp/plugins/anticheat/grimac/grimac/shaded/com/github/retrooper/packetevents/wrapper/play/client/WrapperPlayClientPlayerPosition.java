package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Location;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;

public class WrapperPlayClientPlayerPosition extends WrapperPlayClientPlayerFlying {
   public WrapperPlayClientPlayerPosition(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientPlayerPosition(Vector3d position, boolean onGround) {
      super(true, false, onGround, new Location(position, 0.0F, 0.0F));
   }

   public Vector3d getPosition() {
      return this.getLocation().getPosition();
   }

   public void setPosition(Vector3d position) {
      this.getLocation().setPosition(position);
   }
}
