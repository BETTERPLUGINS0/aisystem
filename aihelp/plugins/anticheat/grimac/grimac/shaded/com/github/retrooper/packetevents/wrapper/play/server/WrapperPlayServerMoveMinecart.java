package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class WrapperPlayServerMoveMinecart extends PacketWrapper<WrapperPlayServerMoveMinecart> {
   private int entityId;
   private List<WrapperPlayServerMoveMinecart.MinecartStep> lerpSteps;

   public WrapperPlayServerMoveMinecart(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerMoveMinecart(int entityId, List<WrapperPlayServerMoveMinecart.MinecartStep> lerpSteps) {
      super((PacketTypeCommon)PacketType.Play.Server.MOVE_MINECART);
      this.entityId = entityId;
      this.lerpSteps = lerpSteps;
   }

   public void read() {
      this.entityId = this.readVarInt();
      this.lerpSteps = this.readList(WrapperPlayServerMoveMinecart.MinecartStep::read);
   }

   public void write() {
      this.writeVarInt(this.entityId);
      this.writeList(this.lerpSteps, WrapperPlayServerMoveMinecart.MinecartStep::write);
   }

   public void copy(WrapperPlayServerMoveMinecart wrapper) {
      this.entityId = wrapper.entityId;
      this.lerpSteps = wrapper.lerpSteps;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void setEntityId(int entityId) {
      this.entityId = entityId;
   }

   public List<WrapperPlayServerMoveMinecart.MinecartStep> getLerpSteps() {
      return this.lerpSteps;
   }

   public void setLerpSteps(List<WrapperPlayServerMoveMinecart.MinecartStep> lerpSteps) {
      this.lerpSteps = lerpSteps;
   }

   public static final class MinecartStep {
      private Vector3d position;
      private Vector3d movement;
      private float yaw;
      private float pitch;
      private float weight;

      public MinecartStep(Vector3d position, Vector3d movement, float yaw, float pitch, float weight) {
         this.position = position;
         this.movement = movement;
         this.yaw = yaw;
         this.pitch = pitch;
         this.weight = weight;
      }

      public static WrapperPlayServerMoveMinecart.MinecartStep read(PacketWrapper<?> wrapper) {
         Vector3d position = Vector3d.read(wrapper);
         Vector3d movement = Vector3d.read(wrapper);
         float yaw = wrapper.readRotation();
         float pitch = wrapper.readRotation();
         float weight = wrapper.readFloat();
         return new WrapperPlayServerMoveMinecart.MinecartStep(position, movement, yaw, pitch, weight);
      }

      public static void write(PacketWrapper<?> wrapper, WrapperPlayServerMoveMinecart.MinecartStep step) {
         Vector3d.write(wrapper, step.position);
         Vector3d.write(wrapper, step.movement);
         wrapper.writeRotation(step.yaw);
         wrapper.writeRotation(step.pitch);
         wrapper.writeFloat(step.weight);
      }

      public Vector3d getPosition() {
         return this.position;
      }

      public void setPosition(Vector3d position) {
         this.position = position;
      }

      public Vector3d getMovement() {
         return this.movement;
      }

      public void setMovement(Vector3d movement) {
         this.movement = movement;
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

      public float getWeight() {
         return this.weight;
      }

      public void setWeight(float weight) {
         this.weight = weight;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof WrapperPlayServerMoveMinecart.MinecartStep)) {
            return false;
         } else {
            WrapperPlayServerMoveMinecart.MinecartStep that = (WrapperPlayServerMoveMinecart.MinecartStep)obj;
            if (Float.compare(that.yaw, this.yaw) != 0) {
               return false;
            } else if (Float.compare(that.pitch, this.pitch) != 0) {
               return false;
            } else if (Float.compare(that.weight, this.weight) != 0) {
               return false;
            } else {
               return !this.position.equals(that.position) ? false : this.movement.equals(that.movement);
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.position, this.movement, this.yaw, this.pitch, this.weight});
      }
   }
}
