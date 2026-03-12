package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.login.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.client.WrapperCommonCookieResponse;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class WrapperLoginClientCookieResponse extends WrapperCommonCookieResponse<WrapperLoginClientCookieResponse> {
   /** @deprecated */
   @Deprecated
   public WrapperLoginClientCookieResponse(PacketSendEvent event) {
      super(event);
   }

   public WrapperLoginClientCookieResponse(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperLoginClientCookieResponse(ResourceLocation key, @Nullable byte[] payload) {
      super(PacketType.Login.Client.COOKIE_RESPONSE, key, payload);
   }
}
