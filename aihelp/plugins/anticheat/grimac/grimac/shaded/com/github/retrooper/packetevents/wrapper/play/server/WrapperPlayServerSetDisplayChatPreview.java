package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetDisplayChatPreview extends PacketWrapper<WrapperPlayServerSetDisplayChatPreview> {
   private boolean chatPreviewDisplay;

   public WrapperPlayServerSetDisplayChatPreview(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerSetDisplayChatPreview(boolean chatPreviewDisplay) {
      super((PacketTypeCommon)PacketType.Play.Server.DISPLAY_CHAT_PREVIEW);
      this.chatPreviewDisplay = chatPreviewDisplay;
   }

   public void read() {
      this.chatPreviewDisplay = this.readBoolean();
   }

   public void write() {
      this.writeBoolean(this.chatPreviewDisplay);
   }

   public void copy(WrapperPlayServerSetDisplayChatPreview wrapper) {
      this.chatPreviewDisplay = wrapper.chatPreviewDisplay;
   }

   public boolean isChatPreviewDisplay() {
      return this.chatPreviewDisplay;
   }

   public void setChatPreviewDisplay(boolean chatPreviewDisplay) {
      this.chatPreviewDisplay = chatPreviewDisplay;
   }
}
