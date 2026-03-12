package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class OpenUrlClickEvent implements ClickEvent {
   private final String url;

   public OpenUrlClickEvent(String url) {
      this.url = url;
   }

   public static OpenUrlClickEvent decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      boolean v1215 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5);
      String url = compound.getStringTagValueOrThrow(v1215 ? "url" : "value");
      return new OpenUrlClickEvent(url);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, OpenUrlClickEvent clickEvent) {
      boolean v1215 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5);
      compound.setTag(v1215 ? "url" : "value", new NBTString(clickEvent.url));
   }

   public ClickEventAction<?> getAction() {
      return ClickEventActions.OPEN_URL;
   }

   public ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent asAdventure() {
      return ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent.openUrl(this.url);
   }

   public String getUrl() {
      return this.url;
   }
}
