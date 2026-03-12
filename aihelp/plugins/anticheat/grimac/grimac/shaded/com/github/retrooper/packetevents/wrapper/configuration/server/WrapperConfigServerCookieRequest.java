package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperConfigServerCookieRequest extends PacketWrapper<WrapperConfigServerCookieRequest> {
   private ResourceLocation key;

   public WrapperConfigServerCookieRequest(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigServerCookieRequest(ResourceLocation key) {
      super((PacketTypeCommon)PacketType.Configuration.Server.COOKIE_REQUEST);
      this.key = key;
   }

   public void read() {
      this.key = this.readIdentifier();
   }

   public void write() {
      this.writeIdentifier(this.key);
   }

   public void copy(WrapperConfigServerCookieRequest wrapper) {
      this.key = wrapper.key;
   }

   public ResourceLocation getKey() {
      return this.key;
   }

   public void setKey(ResourceLocation key) {
      this.key = key;
   }
}
