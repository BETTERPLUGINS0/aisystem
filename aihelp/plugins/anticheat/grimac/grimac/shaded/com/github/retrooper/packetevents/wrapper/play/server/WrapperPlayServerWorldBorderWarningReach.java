package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerWorldBorderWarningReach extends PacketWrapper<WrapperPlayServerWorldBorderWarningReach> {
   private int warningBlocks;

   public WrapperPlayServerWorldBorderWarningReach(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerWorldBorderWarningReach(int warningBlocks) {
      super((PacketTypeCommon)PacketType.Play.Server.WORLD_BORDER_WARNING_REACH);
      this.warningBlocks = warningBlocks;
   }

   public void read() {
      this.warningBlocks = this.readVarInt();
   }

   public void write() {
      this.writeVarInt(this.warningBlocks);
   }

   public void copy(WrapperPlayServerWorldBorderWarningReach packet) {
      this.warningBlocks = packet.warningBlocks;
   }

   public int getWarningBlocks() {
      return this.warningBlocks;
   }

   public void setWarningBlocks(int warningBlocks) {
      this.warningBlocks = warningBlocks;
   }
}
