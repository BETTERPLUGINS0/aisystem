package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class WrapperPlayServerSpawnPosition extends PacketWrapper<WrapperPlayServerSpawnPosition> {
   private ResourceLocation dimension;
   private Vector3i position;
   private float yaw;
   private float pitch;

   public WrapperPlayServerSpawnPosition(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSpawnPosition(Vector3i position) {
      this(position, 0.0F);
   }

   public WrapperPlayServerSpawnPosition(Vector3i position, float yaw) {
      this(WorldBlockPosition.OVERWORLD_DIMENSION, position, yaw, 0.0F);
   }

   public WrapperPlayServerSpawnPosition(ResourceLocation dimension, Vector3i position, float yaw, float pitch) {
      super((PacketTypeCommon)PacketType.Play.Server.SPAWN_POSITION);
      this.dimension = dimension;
      this.position = position;
      this.yaw = yaw;
      this.pitch = pitch;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         this.dimension = ResourceLocation.read(this);
      }

      this.position = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8) ? this.readBlockPosition() : new Vector3i(this.readInt(), this.readInt(), this.readInt());
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
         this.yaw = this.readFloat();
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         this.pitch = this.readFloat();
      }

   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         ResourceLocation.write(this, this.dimension);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
         this.writeBlockPosition(this.position);
      } else {
         this.writeInt(this.position.x);
         this.writeInt(this.position.y);
         this.writeInt(this.position.z);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
         this.writeFloat(this.yaw);
      }

      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         this.writeFloat(this.pitch);
      }

   }

   public void copy(WrapperPlayServerSpawnPosition wrapper) {
      this.dimension = wrapper.dimension;
      this.position = wrapper.position;
      this.yaw = wrapper.yaw;
      this.pitch = wrapper.pitch;
   }

   public ResourceLocation getDimension() {
      return this.dimension;
   }

   public void setDimension(ResourceLocation dimension) {
      this.dimension = dimension;
   }

   public Vector3i getPosition() {
      return this.position;
   }

   public void setPosition(Vector3i position) {
      this.position = position;
   }

   /** @deprecated */
   @Deprecated
   public Optional<Float> getAngle() {
      return Optional.ofNullable(this.yaw);
   }

   /** @deprecated */
   @Deprecated
   public void setAngle(float yaw) {
      this.yaw = yaw;
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
