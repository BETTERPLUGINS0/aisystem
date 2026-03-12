package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigServerStoreCookie extends PacketWrapper<WrapperConfigServerStoreCookie> {
   public static final int MAX_PAYLOAD_SIZE = 5120;
   private ResourceLocation key;
   private byte[] payload;

   public WrapperConfigServerStoreCookie(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerStoreCookie(ResourceLocation key, byte[] payload) {
      super((PacketTypeCommon)PacketType.Configuration.Server.STORE_COOKIE);
      this.key = key;
      this.payload = payload;
   }

   public void read() {
      this.key = this.readIdentifier();
      this.payload = this.readByteArray(5120);
   }

   public void write() {
      this.writeIdentifier(this.key);
      this.writeByteArray(this.payload);
   }

   public void copy(WrapperConfigServerStoreCookie wrapper) {
      this.key = wrapper.key;
      this.payload = wrapper.payload;
   }

   public ResourceLocation getKey() {
      return this.key;
   }

   public void setKey(ResourceLocation key) {
      this.key = key;
   }

   public byte[] getPayload() {
      return this.payload;
   }

   public void setPayload(byte[] payload) {
      this.payload = payload;
   }
}
