package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CopyToClipboardClickEvent implements ClickEvent {
   private final String value;

   public CopyToClipboardClickEvent(String value) {
      this.value = value;
   }

   public static CopyToClipboardClickEvent decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      String value = compound.getStringTagValueOrThrow("value");
      return new CopyToClipboardClickEvent(value);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, CopyToClipboardClickEvent clickEvent) {
      compound.setTag("value", new NBTString(clickEvent.value));
   }

   public ClickEventAction<?> getAction() {
      return ClickEventActions.COPY_TO_CLIPBOARD;
   }

   public ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent asAdventure() {
      return ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent.copyToClipboard(this.value);
   }

   public String getValue() {
      return this.value;
   }
}
