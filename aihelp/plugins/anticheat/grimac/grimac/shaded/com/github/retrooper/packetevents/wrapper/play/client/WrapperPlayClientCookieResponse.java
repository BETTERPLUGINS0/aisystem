package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.client.WrapperCommonCookieResponse;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class WrapperPlayClientCookieResponse extends WrapperCommonCookieResponse<WrapperPlayClientCookieResponse> {
   /** @deprecated */
   @Deprecated
   public WrapperPlayClientCookieResponse(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayClientCookieResponse(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientCookieResponse(ResourceLocation key, @Nullable byte[] payload) {
      super(PacketType.Play.Client.COOKIE_RESPONSE, key, payload);
   }
}
