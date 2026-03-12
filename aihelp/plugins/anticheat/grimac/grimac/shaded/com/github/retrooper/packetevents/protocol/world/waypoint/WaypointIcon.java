package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.waypoint;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class WaypointIcon {
   public static final ResourceLocation ICON_STYLE_DEFAULT = new ResourceLocation("default");
   public static final ResourceLocation ICON_STYLE_BOWTIE = new ResourceLocation("bowtie");
   private final ResourceLocation style;
   @Nullable
   private final Color color;

   public WaypointIcon(ResourceLocation style, @Nullable Color color) {
      this.style = style;
      this.color = color;
   }

   public static WaypointIcon read(PacketWrapper<?> wrapper) {
      ResourceLocation style = wrapper.readIdentifier();
      Color color = (Color)wrapper.readOptional(Color::readShort);
      return new WaypointIcon(style, color);
   }

   public static void write(PacketWrapper<?> wrapper, WaypointIcon icon) {
      wrapper.writeIdentifier(icon.style);
      wrapper.writeOptional(icon.color, Color::writeShort);
   }

   public ResourceLocation getStyle() {
      return this.style;
   }

   @Nullable
   public Color getColor() {
      return this.color;
   }
}
