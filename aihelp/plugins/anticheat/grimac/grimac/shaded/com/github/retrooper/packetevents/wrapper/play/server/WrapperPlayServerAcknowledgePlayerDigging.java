package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.DiggingAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerAcknowledgePlayerDigging extends PacketWrapper<WrapperPlayServerAcknowledgePlayerDigging> {
   private DiggingAction action;
   private boolean successful;
   private Vector3i blockPosition;
   private int blockID;

   public WrapperPlayServerAcknowledgePlayerDigging(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerAcknowledgePlayerDigging(DiggingAction action, boolean successful, Vector3i blockPosition, int blockID) {
      super((PacketTypeCommon)PacketType.Play.Server.ACKNOWLEDGE_PLAYER_DIGGING);
      this.action = action;
      this.successful = successful;
      this.blockPosition = blockPosition;
      this.blockID = blockID;
   }

   public void read() {
      this.blockPosition = this.readBlockPosition();
      this.blockID = this.readVarInt();
      this.action = DiggingAction.getById(this.readVarInt());
      this.successful = this.readBoolean();
   }

   public void write() {
      this.writeBlockPosition(this.blockPosition);
      this.writeVarInt(this.blockID);
      this.writeVarInt(this.action.getId());
      this.writeBoolean(this.successful);
   }

   public void copy(WrapperPlayServerAcknowledgePlayerDigging wrapper) {
      this.action = wrapper.action;
      this.successful = wrapper.successful;
      this.blockPosition = wrapper.blockPosition;
      this.blockID = wrapper.blockID;
   }

   public DiggingAction getAction() {
      return this.action;
   }

   public void setAction(DiggingAction action) {
      this.action = action;
   }

   public boolean isSuccessful() {
      return this.successful;
   }

   public void setSuccessful(boolean successful) {
      this.successful = successful;
   }

   public Vector3i getBlockPosition() {
      return this.blockPosition;
   }

   public void setBlockPosition(Vector3i blockPosition) {
      this.blockPosition = blockPosition;
   }

   public int getBlockId() {
      return this.blockID;
   }

   public void setBlockId(int blockID) {
      this.blockID = blockID;
   }
}
