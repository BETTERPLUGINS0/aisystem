package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerCustomReportDetails;
import java.util.Map;

public class WrapperPlayServerCustomReportDetails extends WrapperCommonServerCustomReportDetails<WrapperPlayServerCustomReportDetails> {
   public WrapperPlayServerCustomReportDetails(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerCustomReportDetails(Map<String, String> details) {
      super(PacketType.Play.Server.CUSTOM_REPORT_DETAILS, details);
   }
}
