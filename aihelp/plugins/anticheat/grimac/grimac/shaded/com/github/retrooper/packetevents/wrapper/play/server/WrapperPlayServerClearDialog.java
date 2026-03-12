package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerClearDialog;

public class WrapperPlayServerClearDialog extends WrapperCommonServerClearDialog<WrapperPlayServerClearDialog> {
   public WrapperPlayServerClearDialog(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerClearDialog() {
      super((PacketTypeCommon)PacketType.Play.Server.CLEAR_DIALOG);
   }
}
