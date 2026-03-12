package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Map;

public class WrapperPlayServerStatistics extends PacketWrapper<WrapperPlayServerStatistics> {
   private Map<String, Integer> statistics;

   public WrapperPlayServerStatistics(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerStatistics(Map<String, Integer> statistics) {
      super((PacketTypeCommon)PacketType.Play.Server.STATISTICS);
      this.statistics = statistics;
   }

   public void read() {
      this.statistics = this.readMap(PacketWrapper::readString, PacketWrapper::readVarInt);
   }

   public void write() {
      this.writeMap(this.statistics, PacketWrapper::writeString, PacketWrapper::writeVarInt);
   }

   public void copy(WrapperPlayServerStatistics wrapper) {
      this.statistics = wrapper.statistics;
   }

   public Map<String, Integer> getStatistics() {
      return this.statistics;
   }

   public void setStatistics(Map<String, Integer> statistics) {
      this.statistics = statistics;
   }
}
