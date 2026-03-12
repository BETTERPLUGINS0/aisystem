package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerCustomReportDetails;
import java.util.Map;

public class WrapperConfigServerCustomReportDetails extends WrapperCommonServerCustomReportDetails<WrapperConfigServerCustomReportDetails> {
   public WrapperConfigServerCustomReportDetails(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerCustomReportDetails(Map<String, String> details) {
      super(PacketType.Configuration.Server.CUSTOM_REPORT_DETAILS, details);
   }
}
