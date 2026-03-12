package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginClientPluginResponse extends PacketWrapper<WrapperLoginClientPluginResponse> {
   private int messageID;
   private boolean successful;
   private byte[] data;

   public WrapperLoginClientPluginResponse(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperLoginClientPluginResponse(ClientVersion clientVersion, int messageID, boolean successful, byte[] data) {
      super(PacketType.Login.Client.LOGIN_PLUGIN_RESPONSE.getId(), clientVersion);
      this.messageID = messageID;
      this.successful = successful;
      this.data = data;
   }

   public void read() {
      this.messageID = this.readVarInt();
      this.successful = this.readBoolean();
      if (this.successful) {
         this.data = this.readBytes(ByteBufHelper.readableBytes(this.buffer));
      } else {
         this.data = new byte[0];
      }

   }

   public void write() {
      this.writeVarInt(this.messageID);
      this.writeBoolean(this.successful);
      if (this.successful) {
         this.writeBytes(this.data);
      }

   }

   public void copy(WrapperLoginClientPluginResponse wrapper) {
      this.messageID = wrapper.messageID;
      this.successful = wrapper.successful;
      this.data = wrapper.data;
   }

   public int getMessageId() {
      return this.messageID;
   }

   public void setMessageId(int messageID) {
      this.messageID = messageID;
   }

   public boolean isSuccessful() {
      return this.successful;
   }

   public void setSuccessful(boolean successful) {
      this.successful = successful;
   }

   public byte[] getData() {
      return this.data;
   }

   public void setData(byte[] data) {
      this.data = data;
   }
}
