package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigServerTransfer extends PacketWrapper<WrapperConfigServerTransfer> {
   private String host;
   private int port;

   public WrapperConfigServerTransfer(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerTransfer(String host, int port) {
      super((PacketTypeCommon)PacketType.Configuration.Server.TRANSFER);
      this.host = host;
      this.port = port;
   }

   public void read() {
      this.host = this.readString();
      this.port = this.readVarInt();
   }

   public void write() {
      this.writeString(this.host);
      this.writeVarInt(this.port);
   }

   public void copy(WrapperConfigServerTransfer wrapper) {
      this.host = wrapper.host;
      this.port = wrapper.port;
   }

   public String getHost() {
      return this.host;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public int getPort() {
      return this.port;
   }

   public void setPort(int port) {
      this.port = port;
   }
}
