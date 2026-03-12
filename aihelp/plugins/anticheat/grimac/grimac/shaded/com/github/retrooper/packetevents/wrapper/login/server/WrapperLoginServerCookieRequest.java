package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginServerCookieRequest extends PacketWrapper<WrapperLoginServerCookieRequest> {
   private ResourceLocation key;

   public WrapperLoginServerCookieRequest(PacketSendEvent event) {
      super(event);
   }

   public WrapperLoginServerCookieRequest(ResourceLocation key) {
      super((PacketTypeCommon)PacketType.Login.Server.COOKIE_REQUEST);
      this.key = key;
   }

   public void read() {
      this.key = this.readIdentifier();
   }

   public void write() {
      this.writeIdentifier(this.key);
   }

   public void copy(WrapperLoginServerCookieRequest wrapper) {
      this.key = wrapper.key;
   }

   public ResourceLocation getKey() {
      return this.key;
   }

   public void setKey(ResourceLocation key) {
      this.key = key;
   }
}
