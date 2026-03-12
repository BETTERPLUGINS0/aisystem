package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public class WrapperPlayServerActionBar extends PacketWrapper<WrapperPlayServerActionBar> {
   private Component actionBarText;

   public WrapperPlayServerActionBar(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerActionBar(Component actionBarText) {
      super((PacketTypeCommon)PacketType.Play.Server.ACTION_BAR);
      this.actionBarText = actionBarText;
   }

   public void read() {
      this.actionBarText = this.readComponent();
   }

   public void write() {
      this.writeComponent(this.actionBarText);
   }

   public void copy(WrapperPlayServerActionBar wrapper) {
      this.actionBarText = wrapper.actionBarText;
   }

   public Component getActionBarText() {
      return this.actionBarText;
   }

   public void setActionBarText(Component actionBarText) {
      this.actionBarText = actionBarText;
   }
}
