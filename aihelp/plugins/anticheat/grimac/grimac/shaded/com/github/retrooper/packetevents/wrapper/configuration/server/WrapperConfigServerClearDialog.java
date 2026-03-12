package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerClearDialog;

public class WrapperConfigServerClearDialog extends WrapperCommonServerClearDialog<WrapperConfigServerClearDialog> {
   public WrapperConfigServerClearDialog(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerClearDialog() {
      super((PacketTypeCommon)PacketType.Configuration.Server.CLEAR_DIALOG);
   }
}
