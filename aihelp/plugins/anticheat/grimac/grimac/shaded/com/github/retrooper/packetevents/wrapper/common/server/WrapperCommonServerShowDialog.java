package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.Dialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperCommonServerShowDialog<T extends WrapperCommonServerShowDialog<T>> extends PacketWrapper<T> {
   protected Dialog dialog;

   public WrapperCommonServerShowDialog(PacketSendEvent event) {
      super(event);
   }

   public WrapperCommonServerShowDialog(PacketTypeCommon packetType, Dialog dialog) {
      super(packetType);
      this.dialog = dialog;
   }

   public void read() {
      this.dialog = Dialog.read(this);
   }

   public void write() {
      Dialog.write(this, this.dialog);
   }

   public void copy(T wrapper) {
      this.dialog = wrapper.getDialog();
   }

   public Dialog getDialog() {
      return this.dialog;
   }

   public void setDialog(Dialog dialog) {
      this.dialog = dialog;
   }
}
