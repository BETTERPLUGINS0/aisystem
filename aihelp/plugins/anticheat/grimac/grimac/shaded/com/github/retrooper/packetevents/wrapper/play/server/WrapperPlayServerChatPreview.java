package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Optional;

public class WrapperPlayServerChatPreview extends PacketWrapper<WrapperPlayServerChatPreview> {
   private int queryId;
   @Nullable
   private Component message;

   public WrapperPlayServerChatPreview(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerChatPreview(int queryId, @Nullable Component message) {
      super((PacketTypeCommon)PacketType.Play.Server.CHAT_PREVIEW_PACKET);
      this.queryId = queryId;
      this.message = message;
   }

   public void read() {
      this.queryId = this.readInt();
      this.message = (Component)this.readOptional(PacketWrapper::readComponent);
   }

   public void write() {
      this.writeInt(this.queryId);
      this.writeOptional(this.message, PacketWrapper::writeComponent);
   }

   public void copy(WrapperPlayServerChatPreview wrapper) {
      this.queryId = wrapper.queryId;
      this.message = wrapper.message;
   }

   public int getQueryId() {
      return this.queryId;
   }

   public void setQueryId(int queryId) {
      this.queryId = queryId;
   }

   public Optional<Component> getMessage() {
      return Optional.ofNullable(this.message);
   }

   public void setMessage(@Nullable Component message) {
      this.message = message;
   }
}
