package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientChatPreview extends PacketWrapper<WrapperPlayClientChatPreview> {
   private int queryId;
   private String message;

   public WrapperPlayClientChatPreview(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientChatPreview(int queryId, String message) {
      super((PacketTypeCommon)PacketType.Play.Client.CHAT_PREVIEW);
      this.queryId = queryId;
      this.message = message;
   }

   public void read() {
      this.queryId = this.readInt();
      this.message = this.readString(256);
   }

   public void write() {
      this.writeInt(this.queryId);
      this.writeString(this.message, 256);
   }

   public void copy(WrapperPlayClientChatPreview wrapper) {
      this.queryId = wrapper.queryId;
      this.message = wrapper.message;
   }

   public int getQueryId() {
      return this.queryId;
   }

   public void setQueryId(int queryId) {
      this.queryId = queryId;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
