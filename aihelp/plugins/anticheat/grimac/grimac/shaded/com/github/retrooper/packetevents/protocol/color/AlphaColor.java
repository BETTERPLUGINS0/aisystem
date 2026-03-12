package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import ac.grim.grimac.shaded.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AlphaColor extends Color {
   public static final AlphaColor WHITE = new AlphaColor(-1);
   public static final AlphaColor BLACK = new AlphaColor(-16777216);
   public static final AlphaColor TRANSPARENT = new AlphaColor(0);
   private final int alpha;

   public AlphaColor(@Range(from = 0L,to = 255L) int red, @Range(from = 0L,to = 255L) int green, @Range(from = 0L,to = 255L) int blue) {
      this(255, red, green, blue);
   }

   public AlphaColor(@Range(from = 0L,to = 255L) int alpha, @Range(from = 0L,to = 255L) int red, @Range(from = 0L,to = 255L) int green, @Range(from = 0L,to = 255L) int blue) {
      super(red, green, blue);
      this.alpha = MathUtil.clamp(alpha, 0, 255);
   }

   public AlphaColor(@Range(from = 0L,to = 1L) float red, @Range(from = 0L,to = 1L) float green, @Range(from = 0L,to = 1L) float blue) {
      this(1.0F, red, green, blue);
   }

   public AlphaColor(@Range(from = 0L,to = 1L) float alpha, @Range(from = 0L,to = 1L) float red, @Range(from = 0L,to = 1L) float green, @Range(from = 0L,to = 1L) float blue) {
      super(red, green, blue);
      this.alpha = MathUtil.floor(alpha * 255.0F);
   }

   public AlphaColor(int rgb) {
      this(rgb >> 24 & 255, rgb >> 16 & 255, rgb >> 8 & 255, rgb & 255);
   }

   public static AlphaColor decode(NBT nbt, ClientVersion version) {
      if (nbt instanceof NBTNumber) {
         return new AlphaColor(((NBTNumber)nbt).getAsInt());
      } else {
         NBTList<?> list = (NBTList)nbt;
         float red = ((NBTNumber)list.getTag(0)).getAsFloat();
         float green = ((NBTNumber)list.getTag(1)).getAsFloat();
         float blue = ((NBTNumber)list.getTag(2)).getAsFloat();
         float alpha = ((NBTNumber)list.getTag(3)).getAsFloat();
         return new AlphaColor(alpha, red, green, blue);
      }
   }

   public static NBT encode(AlphaColor color, ClientVersion version) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_21_2)) {
         return new NBTInt(color.asRGB());
      } else {
         NBTList<NBTFloat> list = new NBTList(NBTType.FLOAT, 4);
         list.addTag(new NBTFloat((float)color.red));
         list.addTag(new NBTFloat((float)color.green));
         list.addTag(new NBTFloat((float)color.blue));
         list.addTag(new NBTFloat((float)color.alpha));
         return list;
      }
   }

   public AlphaColor withAlpha() {
      return this;
   }

   public AlphaColor withRed(@Range(from = 0L,to = 255L) int red) {
      return new AlphaColor(this.alpha, red, this.green, this.blue);
   }

   public AlphaColor withGreen(@Range(from = 0L,to = 255L) int green) {
      return new AlphaColor(this.alpha, this.red, green, this.blue);
   }

   public AlphaColor withBlue(@Range(from = 0L,to = 255L) int blue) {
      return new AlphaColor(this.alpha, this.red, this.green, blue);
   }

   public int asRGB() {
      return this.alpha << 24 | this.red << 16 | this.green << 8 | this.blue;
   }

   public AlphaColor plus(Color other) {
      return new AlphaColor(this.alpha, this.red + other.red, this.green + other.green, this.blue + other.blue);
   }

   public AlphaColor minus(Color other) {
      return new AlphaColor(this.alpha, this.red - other.red, this.green - other.green, this.blue - other.blue);
   }

   public AlphaColor times(Color other) {
      return other.alpha() == 255 && other.red == 255 && other.green == 255 && other.blue == 255 ? this : new AlphaColor(this.alpha * other.alpha() / 255, this.red * other.red / 255, this.green * other.green / 255, this.blue * other.blue / 255);
   }

   public AlphaColor blendWith(AlphaColor source) {
      int srcAlpha = source.alpha;
      if (srcAlpha == 255) {
         return source;
      } else if (srcAlpha == 0) {
         return this;
      } else {
         int alpha = srcAlpha + this.alpha * (255 - srcAlpha) / 255;
         return new AlphaColor(alpha, alphaBlendChannel(alpha, srcAlpha, this.red, source.red), alphaBlendChannel(alpha, srcAlpha, this.green, source.green), alphaBlendChannel(alpha, srcAlpha, this.blue, source.blue));
      }
   }

   protected static int alphaBlendChannel(int alpha, int srcAlpha, int dest, int src) {
      return (src * srcAlpha + dest * (alpha - srcAlpha)) / alpha;
   }

   public AlphaColor asGrayscale() {
      int grayscale = (int)((float)this.red * 0.3F + (float)this.green * 0.59F + (float)this.blue * 0.11F);
      return new AlphaColor(this.alpha, grayscale, grayscale, grayscale);
   }

   public AlphaColor scale(float scale) {
      return this.scale(scale, scale, scale);
   }

   public AlphaColor scale(float redScale, float greenScale, float blueScale) {
      return new AlphaColor(this.alpha, (int)((float)this.red * redScale), (int)((float)this.green * greenScale), (int)((float)this.blue * blueScale));
   }

   public AlphaColor lerpSrgb(Color dest, float t) {
      return new AlphaColor(MathUtil.lerp(t, this.alpha, dest.alpha()), MathUtil.lerp(t, this.red, dest.red), MathUtil.lerp(t, this.green, dest.green), MathUtil.lerp(t, this.blue, dest.blue));
   }

   @Range(
      from = 0L,
      to = 255L
   )
   public int alpha() {
      return this.alpha;
   }
}
