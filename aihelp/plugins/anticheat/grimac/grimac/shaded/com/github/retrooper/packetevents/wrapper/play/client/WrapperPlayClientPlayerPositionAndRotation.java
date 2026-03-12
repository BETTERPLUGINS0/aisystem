package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Location;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;

public class WrapperPlayClientPlayerPositionAndRotation extends WrapperPlayClientPlayerFlying {
   public WrapperPlayClientPlayerPositionAndRotation(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientPlayerPositionAndRotation(Vector3d position, float yaw, float pitch, boolean onGround) {
      super(true, true, onGround, new Location(position, yaw, pitch));
   }

   public WrapperPlayClientPlayerPositionAndRotation(Location location, boolean onGround) {
      super(true, true, onGround, location);
   }

   public Vector3d getPosition() {
      return this.getLocation().getPosition();
   }

   public void setPosition(Vector3d position) {
      this.getLocation().setPosition(position);
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
