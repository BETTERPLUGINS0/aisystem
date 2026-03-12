package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerTransfer extends PacketWrapper<WrapperPlayServerTransfer> {
   private String host;
   private int port;

   public WrapperPlayServerTransfer(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerTransfer(String host, int port) {
      super((PacketTypeCommon)PacketType.Play.Server.TRANSFER);
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

   public void copy(WrapperPlayServerTransfer wrapper) {
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
