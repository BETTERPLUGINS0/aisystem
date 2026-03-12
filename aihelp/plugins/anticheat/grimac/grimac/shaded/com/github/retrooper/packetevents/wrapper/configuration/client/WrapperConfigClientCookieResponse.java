package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.configuration.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.client.WrapperCommonCookieResponse;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class WrapperConfigClientCookieResponse extends WrapperCommonCookieResponse<WrapperConfigClientCookieResponse> {
   /** @deprecated */
   @Deprecated
   public WrapperConfigClientCookieResponse(PacketSendEvent event) {
      super(event);
   }

   public WrapperConfigClientCookieResponse(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperConfigClientCookieResponse(ResourceLocation key, @Nullable byte[] payload) {
      super(PacketType.Configuration.Client.COOKIE_RESPONSE, key, payload);
   }
}
