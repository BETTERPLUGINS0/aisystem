package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.InteractionHand;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerOpenBook extends PacketWrapper<WrapperPlayServerOpenBook> {
   private InteractionHand hand;

   public WrapperPlayServerOpenBook(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerOpenBook(InteractionHand hand) {
      super((PacketTypeCommon)PacketType.Play.Server.OPEN_BOOK);
      this.hand = hand;
   }

   public void read() {
      this.hand = InteractionHand.values()[this.readVarInt()];
   }

   public void write() {
      this.writeVarInt(this.hand.ordinal());
   }

   public void copy(WrapperPlayServerOpenBook wrapper) {
      this.hand = wrapper.hand;
   }

   public InteractionHand getHand() {
      return this.hand;
   }

   public void setHand(InteractionHand hand) {
      this.hand = hand;
   }
}
