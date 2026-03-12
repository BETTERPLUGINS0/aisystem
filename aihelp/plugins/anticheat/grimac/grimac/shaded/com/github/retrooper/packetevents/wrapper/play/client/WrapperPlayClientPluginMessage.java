package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientPluginMessage extends PacketWrapper<WrapperPlayClientPluginMessage> {
   private String channelName;
   private byte[] data;

   public WrapperPlayClientPluginMessage(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientPluginMessage(String channelName, byte[] data) {
      super((PacketTypeCommon)PacketType.Play.Client.PLUGIN_MESSAGE);
      this.channelName = channelName;
      this.data = data;
   }

   public WrapperPlayClientPluginMessage(ResourceLocation channelName, byte[] data) {
      super((PacketTypeCommon)PacketType.Play.Client.PLUGIN_MESSAGE);
      this.channelName = channelName.toString();
      this.data = data;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
         this.channelName = this.readString();
      } else {
         this.channelName = this.readString(20);
      }

      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         short var1 = this.readShort();
      }

      if (ByteBufHelper.readableBytes(this.buffer) > 32767) {
         throw new RuntimeException("Payload may not be larger than 32767 bytes");
      } else {
         this.data = this.readRemainingBytes();
      }
   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
         this.writeString(this.channelName);
      } else {
         this.writeString(this.channelName, 20);
      }

      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.writeShort(this.data.length);
      }

      this.writeBytes(this.data);
   }

   public void copy(WrapperPlayClientPluginMessage wrapper) {
      this.channelName = wrapper.channelName;
      this.data = wrapper.data;
   }

   public String getChannelName() {
      return this.channelName;
   }

   public void setChannelName(String channelName) {
      this.channelName = channelName;
   }

   public byte[] getData() {
      return this.data;
   }

   public void setData(byte[] data) {
      this.data = data;
   }
}
