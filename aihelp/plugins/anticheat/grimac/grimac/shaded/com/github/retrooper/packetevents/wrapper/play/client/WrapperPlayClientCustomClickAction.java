package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.common.client.WrapperCommonClientCustomClickAction;
import org.jspecify.annotations.Nullable;

public class WrapperPlayClientCustomClickAction extends WrapperCommonClientCustomClickAction<WrapperPlayClientCustomClickAction> {
   public WrapperPlayClientCustomClickAction(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientCustomClickAction(ResourceLocation id, @Nullable NBT payload) {
      super(PacketType.Play.Client.CUSTOM_CLICK_ACTION, id, payload);
   }
}
