package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Obsolete
public class TwitchUserInfoClickEvent implements ClickEvent {
   private final String value;

   public TwitchUserInfoClickEvent(String value) {
      this.value = value;
   }

   public static TwitchUserInfoClickEvent decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      String value = compound.getStringTagValueOrThrow("value");
      return new TwitchUserInfoClickEvent(value);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, TwitchUserInfoClickEvent clickEvent) {
      compound.setTag("value", new NBTString(clickEvent.value));
   }

   public ClickEventAction<?> getAction() {
      return ClickEventActions.TWITCH_USER_INFO;
   }

   public ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent asAdventure() {
      throw new UnsupportedOperationException();
   }

   public String getValue() {
      return this.value;
   }
}
