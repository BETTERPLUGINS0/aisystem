package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.VersionComparison;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayWorldBorderWarningDelay extends PacketWrapper<WrapperPlayWorldBorderWarningDelay> {
   private long delay;

   public WrapperPlayWorldBorderWarningDelay(long delay) {
      super((PacketTypeCommon)PacketType.Play.Server.WORLD_BORDER_WARNING_DELAY);
      this.delay = delay;
   }

   public WrapperPlayWorldBorderWarningDelay(PacketSendEvent event) {
      super(event);
   }

   public void read() {
      this.delay = (Long)this.readMultiVersional(VersionComparison.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_19, PacketWrapper::readVarInt, PacketWrapper::readVarLong);
   }

   public void write() {
      this.writeMultiVersional(VersionComparison.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_19, this.delay, (packetWrapper, aLong) -> {
         packetWrapper.writeVarInt(Math.toIntExact(aLong));
      }, PacketWrapper::writeVarLong);
   }

   public void copy(WrapperPlayWorldBorderWarningDelay packet) {
      this.delay = packet.delay;
   }
}
