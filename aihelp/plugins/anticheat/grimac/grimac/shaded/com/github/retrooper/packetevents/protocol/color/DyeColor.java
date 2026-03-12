package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.util.RGBLike;

public enum DyeColor implements RGBLike {
   WHITE(new Color(16383998), new Color(16777215), new Color(15790320), 8),
   ORANGE(new Color(16351261), new Color(16738335), new Color(15435844), 15),
   MAGENTA(new Color(13061821), new Color(16711935), new Color(12801229), 16),
   LIGHT_BLUE(new Color(3847130), new Color(10141901), new Color(6719955), 17),
   YELLOW(new Color(16701501), new Color(16776960), new Color(14602026), 18),
   LIME(new Color(8439583), new Color(12582656), new Color(4312372), 19),
   PINK(new Color(15961002), new Color(16738740), new Color(14188952), 20),
   GRAY(new Color(4673362), new Color(8421504), new Color(4408131), 21),
   LIGHT_GRAY(new Color(10329495), new Color(13882323), new Color(11250603), 22),
   CYAN(new Color(1481884), new Color(65535), new Color(2651799), 23),
   PURPLE(new Color(8991416), new Color(10494192), new Color(8073150), 24),
   BLUE(new Color(3949738), new Color(255), new Color(2437522), 25),
   BROWN(new Color(8606770), new Color(9127187), new Color(5320730), 26),
   GREEN(new Color(6192150), new Color(65280), new Color(3887386), 27),
   RED(new Color(11546150), new Color(16711680), new Color(11743532), 28),
   BLACK(new Color(1908001), new Color(0), new Color(1973019), 29);

   private static final DyeColor[] COLORS = values();
   private final Color textureDiffuseColor;
   private final Color textColor;
   private final Color fireworkColor;
   private final int mapColorId;

   private DyeColor(@NotNull Color textureDiffuseColor, @NotNull Color textColor, @NotNull Color fireworkColor, int mapColorId) {
      this.textureDiffuseColor = textureDiffuseColor;
      this.textColor = textColor;
      this.fireworkColor = fireworkColor;
      this.mapColorId = mapColorId;
   }

   public static DyeColor read(PacketWrapper<?> wrapper) {
      return (DyeColor)wrapper.readEnum((Enum[])COLORS);
   }

   public static void write(PacketWrapper<?> wrapper, DyeColor color) {
      wrapper.writeEnum(color);
   }

   @NotNull
   public Color color() {
      return this.textureDiffuseColor;
   }

   @NotNull
   public Color textColor() {
      return this.textColor;
   }

   @NotNull
   public Color fireworkColor() {
      return this.fireworkColor;
   }

   public int red() {
      return this.textureDiffuseColor.red();
   }

   public int green() {
      return this.textureDiffuseColor.green();
   }

   public int blue() {
      return this.textureDiffuseColor.blue();
   }

   public int mapColorId() {
      return this.mapColorId;
   }

   // $FF: synthetic method
   private static DyeColor[] $values() {
      return new DyeColor[]{WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK};
   }
}
