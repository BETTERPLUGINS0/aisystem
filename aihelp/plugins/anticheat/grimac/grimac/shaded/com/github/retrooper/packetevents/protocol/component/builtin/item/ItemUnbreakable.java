package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;

public class ItemUnbreakable {
   @ApiStatus.Obsolete
   private boolean showInTooltip;

   public ItemUnbreakable() {
      this(true);
   }

   @ApiStatus.Obsolete
   public ItemUnbreakable(boolean showInTooltip) {
      this.showInTooltip = showInTooltip;
   }

   public static ItemUnbreakable read(PacketWrapper<?> wrapper) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         return new ItemUnbreakable();
      } else {
         boolean showInTooltip = wrapper.readBoolean();
         return new ItemUnbreakable(showInTooltip);
      }
   }

   public static void write(PacketWrapper<?> wrapper, ItemUnbreakable value) {
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
         wrapper.writeBoolean(value.showInTooltip);
      }

   }

   @ApiStatus.Obsolete
   public boolean isShowInTooltip() {
      return this.showInTooltip;
   }

   @ApiStatus.Obsolete
   public void setShowInTooltip(boolean showInTooltip) {
      this.showInTooltip = showInTooltip;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ItemUnbreakable)) {
         return false;
      } else {
         ItemUnbreakable that = (ItemUnbreakable)obj;
         return this.showInTooltip == that.showInTooltip;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.showInTooltip);
   }
}
