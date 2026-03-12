package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.waypoint.TrackedWaypoint;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerWaypoint extends PacketWrapper<WrapperPlayServerWaypoint> {
   private WrapperPlayServerWaypoint.Operation operation;
   private TrackedWaypoint waypoint;

   public WrapperPlayServerWaypoint(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerWaypoint(WrapperPlayServerWaypoint.Operation operation, TrackedWaypoint waypoint) {
      super((PacketTypeCommon)PacketType.Play.Server.WAYPOINT);
      this.operation = operation;
      this.waypoint = waypoint;
   }

   public void read() {
      this.operation = (WrapperPlayServerWaypoint.Operation)this.readEnum(WrapperPlayServerWaypoint.Operation.class);
      this.waypoint = TrackedWaypoint.read(this);
   }

   public void write() {
      this.writeEnum(this.operation);
      TrackedWaypoint.write(this, this.waypoint);
   }

   public void copy(WrapperPlayServerWaypoint wrapper) {
      this.operation = wrapper.operation;
      this.waypoint = wrapper.waypoint;
   }

   public WrapperPlayServerWaypoint.Operation getOperation() {
      return this.operation;
   }

   public void setOperation(WrapperPlayServerWaypoint.Operation operation) {
      this.operation = operation;
   }

   public TrackedWaypoint getWaypoint() {
      return this.waypoint;
   }

   public void setWaypoint(TrackedWaypoint waypoint) {
      this.waypoint = waypoint;
   }

   public static enum Operation {
      TRACK,
      UNTRACK,
      UPDATE;

      // $FF: synthetic method
      private static WrapperPlayServerWaypoint.Operation[] $values() {
         return new WrapperPlayServerWaypoint.Operation[]{TRACK, UNTRACK, UPDATE};
      }
   }
}
