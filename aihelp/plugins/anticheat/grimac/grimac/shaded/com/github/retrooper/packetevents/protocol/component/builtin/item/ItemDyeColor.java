package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;

public class ItemDyeColor {
   private int rgb;
   @ApiStatus.Obsolete
   private boolean showInTooltip;

   public ItemDyeColor(int rgb) {
      this(rgb, true);
   }

   @ApiStatus.Obsolete
   public ItemDyeColor(int rgb, boolean showInTooltip) {
      this.rgb = rgb;
      this.showInTooltip = showInTooltip;
   }

   public static ItemDyeColor read(PacketWrapper<?> wrapper) {
      int rgb = wrapper.readInt();
      boolean showInTooltip = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) || wrapper.readBoolean();
      return new ItemDyeColor(rgb, showInTooltip);
   }

   public static void write(PacketWrapper<?> wrapper, ItemDyeColor color) {
      wrapper.writeInt(color.rgb);
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
         wrapper.writeBoolean(color.showInTooltip);
      }

   }

   public int getRgb() {
      return this.rgb;
   }

   public void setRgb(int rgb) {
      this.rgb = rgb;
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
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemDyeColor)) {
         return false;
      } else {
         ItemDyeColor that = (ItemDyeColor)obj;
         if (this.rgb != that.rgb) {
            return false;
         } else {
            return this.showInTooltip == that.showInTooltip;
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.rgb, this.showInTooltip});
   }
}
