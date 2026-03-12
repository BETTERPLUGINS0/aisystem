package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerVehicleMove extends PacketWrapper<WrapperPlayServerVehicleMove> {
   private Vector3d position;
   private float yaw;
   private float pitch;

   public WrapperPlayServerVehicleMove(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerVehicleMove(Vector3d position, float yaw, float pitch) {
      super((PacketTypeCommon)PacketType.Play.Server.VEHICLE_MOVE);
      this.position = position;
      this.yaw = yaw;
      this.pitch = pitch;
   }

   public void read() {
      this.position = new Vector3d(this.readDouble(), this.readDouble(), this.readDouble());
      this.yaw = this.readFloat();
      this.pitch = this.readFloat();
   }

   public void write() {
      this.writeDouble(this.position.x);
      this.writeDouble(this.position.y);
      this.writeDouble(this.position.z);
      this.writeFloat(this.yaw);
      this.writeFloat(this.pitch);
   }

   public void copy(WrapperPlayServerVehicleMove wrapper) {
      this.position = wrapper.position;
      this.yaw = wrapper.yaw;
      this.pitch = wrapper.pitch;
   }

   public Vector3d getPosition() {
      return this.position;
   }

   public void setPosition(Vector3d position) {
      this.position = position;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }
}
