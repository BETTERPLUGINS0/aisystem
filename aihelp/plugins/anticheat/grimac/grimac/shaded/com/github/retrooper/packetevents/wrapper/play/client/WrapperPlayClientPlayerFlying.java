package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.Location;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPlayerFlying extends PacketWrapper<WrapperPlayClientPlayerFlying> {
   private boolean positionChanged;
   private boolean rotationChanged;
   private Location location;
   private boolean onGround;
   private boolean horizontalCollision;

   public WrapperPlayClientPlayerFlying(PacketReceiveEvent event) {
      super(event, false);
      this.positionChanged = event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION || event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
      this.rotationChanged = event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION || event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
      this.readEvent(event);
   }

   public WrapperPlayClientPlayerFlying(boolean positionChanged, boolean rotationChanged, boolean onGround, Location location) {
      this(positionChanged, rotationChanged, onGround, false, location);
   }

   public WrapperPlayClientPlayerFlying(boolean positionChanged, boolean rotationChanged, boolean onGround, boolean horizontalCollision, Location location) {
      super((PacketTypeCommon)(positionChanged && rotationChanged ? PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION : (positionChanged ? PacketType.Play.Client.PLAYER_POSITION : (rotationChanged ? PacketType.Play.Client.PLAYER_ROTATION : PacketType.Play.Client.PLAYER_FLYING))));
      this.positionChanged = positionChanged;
      this.rotationChanged = rotationChanged;
      this.onGround = onGround;
      this.horizontalCollision = horizontalCollision;
      this.location = location;
   }

   public static boolean isFlying(PacketTypeCommon type) {
      return type == PacketType.Play.Client.PLAYER_FLYING || type == PacketType.Play.Client.PLAYER_POSITION || type == PacketType.Play.Client.PLAYER_ROTATION || type == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
   }

   public void read() {
      Vector3d position = new Vector3d();
      float yaw = 0.0F;
      float pitch = 0.0F;
      if (this.positionChanged) {
         double x = this.readDouble();
         double y = this.readDouble();
         double z;
         if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            z = this.readDouble();
         }

         z = this.readDouble();
         position = new Vector3d(x, y, z);
      }

      if (this.rotationChanged) {
         yaw = this.readFloat();
         pitch = this.readFloat();
      }

      this.location = new Location(position, yaw, pitch);
      byte flags = this.readByte();
      this.onGround = (flags & 1) == 1;
      this.horizontalCollision = (flags & 2) == 2;
   }

   public void write() {
      if (this.positionChanged) {
         this.writeDouble(this.location.getPosition().getX());
         if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            this.writeDouble(this.location.getPosition().getY() + 1.62D);
         }

         this.writeDouble(this.location.getPosition().getY());
         this.writeDouble(this.location.getPosition().getZ());
      }

      if (this.rotationChanged) {
         this.writeFloat(this.location.getYaw());
         this.writeFloat(this.location.getPitch());
      }

      this.writeByte((this.onGround ? 1 : 0) | (this.horizontalCollision ? 2 : 0));
   }

   public void copy(WrapperPlayClientPlayerFlying wrapper) {
      this.positionChanged = wrapper.positionChanged;
      this.rotationChanged = wrapper.rotationChanged;
      this.location = wrapper.location;
      this.onGround = wrapper.onGround;
      this.horizontalCollision = wrapper.horizontalCollision;
   }

   public Location getLocation() {
      return this.location;
   }

   public void setLocation(Location location) {
      this.location = location;
   }

   public boolean hasPositionChanged() {
      return this.positionChanged;
   }

   public void setPositionChanged(boolean positionChanged) {
      this.positionChanged = positionChanged;
   }

   public boolean hasRotationChanged() {
      return this.rotationChanged;
   }

   public void setRotationChanged(boolean rotationChanged) {
      this.rotationChanged = rotationChanged;
   }

   public boolean isOnGround() {
      return this.onGround;
   }

   public void setOnGround(boolean onGround) {
      this.onGround = onGround;
   }

   public boolean isHorizontalCollision() {
      return this.horizontalCollision;
   }

   public void setHorizontalCollision(boolean horizontalCollision) {
      this.horizontalCollision = horizontalCollision;
   }
}
