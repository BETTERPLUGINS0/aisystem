package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.Dialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerShowDialog;

public class WrapperConfigServerShowDialog extends WrapperCommonServerShowDialog<WrapperConfigServerShowDialog> {
   public WrapperConfigServerShowDialog(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerShowDialog(Dialog dialog) {
      super(PacketType.Configuration.Server.SHOW_DIALOG, dialog);
   }

   public void read() {
      this.dialog = Dialog.readDirect(this);
   }

   public void write() {
      Dialog.writeDirect(this, this.dialog);
   }
}
