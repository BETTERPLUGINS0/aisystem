package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerTickingStep extends PacketWrapper<WrapperPlayServerTickingStep> {
   private int tickSteps;

   public WrapperPlayServerTickingStep(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerTickingStep(int tickSteps) {
      super((PacketTypeCommon)PacketType.Play.Server.TICKING_STEP);
      this.tickSteps = tickSteps;
   }

   public void read() {
      this.tickSteps = this.readVarInt();
   }

   public void write() {
      this.writeVarInt(this.tickSteps);
   }

   public void copy(WrapperPlayServerTickingStep wrapper) {
      this.tickSteps = wrapper.tickSteps;
   }

   public int getTickSteps() {
      return this.tickSteps;
   }

   public void setTickSteps(int tickSteps) {
      this.tickSteps = tickSteps;
   }
}
