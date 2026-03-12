package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Location;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;

public class WrapperPlayClientPlayerRotation extends WrapperPlayClientPlayerFlying {
   public WrapperPlayClientPlayerRotation(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientPlayerRotation(float yaw, float pitch, boolean onGround) {
      super(false, true, onGround, new Location(new Vector3d(), yaw, pitch));
   }

   public float getYaw() {
      return this.getLocation().getYaw();
   }

   public void setYaw(float yaw) {
      this.getLocation().setYaw(yaw);
   }

   public float getPitch() {
      return this.getLocation().getPitch();
   }

   public void setPitch(float pitch) {
      this.getLocation().setPitch(pitch);
   }
}
