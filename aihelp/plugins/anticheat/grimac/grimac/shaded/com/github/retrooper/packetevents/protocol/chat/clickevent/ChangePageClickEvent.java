package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ChangePageClickEvent implements ClickEvent {
   private final int page;

   @ApiStatus.Obsolete
   public ChangePageClickEvent(String page) {
      this(Integer.parseInt(page));
   }

   public ChangePageClickEvent(int page) {
      this.page = page;
   }

   public static ChangePageClickEvent decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      int page = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) ? compound.getNumberTagValueOrThrow("page").intValue() : Integer.parseInt(compound.getStringTagValueOrThrow("value"));
      return new ChangePageClickEvent(page);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, ChangePageClickEvent clickEvent) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         compound.setTag("page", new NBTInt(clickEvent.page));
      } else {
         compound.setTag("value", new NBTString(Integer.toString(clickEvent.page)));
      }

   }

   public ClickEventAction<?> getAction() {
      return ClickEventActions.CHANGE_PAGE;
   }

   public ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent asAdventure() {
      return ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent.changePage(this.page);
   }

   public int getPage() {
      return this.page;
   }
}
