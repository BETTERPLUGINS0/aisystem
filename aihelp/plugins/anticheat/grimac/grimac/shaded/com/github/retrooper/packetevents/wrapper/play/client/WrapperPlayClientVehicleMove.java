package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientVehicleMove extends PacketWrapper<WrapperPlayClientVehicleMove> {
   private Vector3d position;
   private float yaw;
   private float pitch;
   private boolean onGround;

   public WrapperPlayClientVehicleMove(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientVehicleMove(Vector3d position, float yaw, float pitch) {
      this(position, yaw, pitch, false);
   }

   public WrapperPlayClientVehicleMove(Vector3d position, float yaw, float pitch, boolean onGround) {
      super((PacketTypeCommon)PacketType.Play.Client.VEHICLE_MOVE);
      this.position = position;
      this.yaw = yaw;
      this.pitch = pitch;
      this.onGround = onGround;
   }

   public void read() {
      this.position = Vector3d.read(this);
      this.yaw = this.readFloat();
      this.pitch = this.readFloat();
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
         this.onGround = this.readBoolean();
      }

   }

   public void write() {
      Vector3d.write(this, this.position);
      this.writeFloat(this.yaw);
      this.writeFloat(this.pitch);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
         this.writeBoolean(this.onGround);
      }

   }

   public void copy(WrapperPlayClientVehicleMove wrapper) {
      this.position = wrapper.position;
      this.yaw = wrapper.yaw;
      this.pitch = wrapper.pitch;
      this.onGround = wrapper.onGround;
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

   public boolean isOnGround() {
      return this.onGround;
   }

   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }
}
