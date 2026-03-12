package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigClientPluginMessage extends PacketWrapper<WrapperConfigClientPluginMessage> {
   private String channelName;
   private byte[] data;

   public WrapperConfigClientPluginMessage(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperConfigClientPluginMessage(ResourceLocation channelName, byte[] data) {
      this(channelName.toString(), data);
   }

   public WrapperConfigClientPluginMessage(String channelName, byte[] data) {
      super((PacketTypeCommon)PacketType.Configuration.Client.PLUGIN_MESSAGE);
      this.channelName = channelName;
      this.data = data;
   }

   public void read() {
      this.channelName = this.readString();
      if (ByteBufHelper.readableBytes(this.buffer) > 32767) {
         throw new RuntimeException("Payload may not be larger than 32767 bytes");
      } else {
         this.data = this.readRemainingBytes();
      }
   }

   public void write() {
      this.writeString(this.channelName);
      this.writeBytes(this.data);
   }

   public void copy(WrapperConfigClientPluginMessage wrapper) {
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
