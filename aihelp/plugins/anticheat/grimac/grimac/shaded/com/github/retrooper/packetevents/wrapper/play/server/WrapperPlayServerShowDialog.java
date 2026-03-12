package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.Dialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerShowDialog;

public class WrapperPlayServerShowDialog extends WrapperCommonServerShowDialog<WrapperPlayServerShowDialog> {
   public WrapperPlayServerShowDialog(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerShowDialog(Dialog dialog) {
      super(PacketType.Play.Server.SHOW_DIALOG, dialog);
   }
}
