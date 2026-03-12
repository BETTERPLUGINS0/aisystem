package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.MathUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Range;
import ac.grim.grimac.shaded.kyori.adventure.util.RGBLike;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Color implements RGBLike {
   public static final Color WHITE = new Color(-1);
   public static final Color BLACK = new Color(-16777216);
   protected static final int BIT_MASK = 255;
   protected final int red;
   protected final int green;
   protected final int blue;

   public Color(@Range(from = 0L,to = 255L) int red, @Range(from = 0L,to = 255L) int green, @Range(from = 0L,to = 255L) int blue) {
      this.red = MathUtil.clamp(red, 0, 255);
      this.green = MathUtil.clamp(green, 0, 255);
      this.blue = MathUtil.clamp(blue, 0, 255);
   }

   public Color(@Range(from = 0L,to = 1L) float red, @Range(from = 0L,to = 1L) float green, @Range(from = 0L,to = 1L) float blue) {
      this(MathUtil.floor(red * 255.0F), MathUtil.floor(green * 255.0F), MathUtil.floor(blue * 255.0F));
   }

   public Color(int rgb) {
      this(rgb >> 16 & 255, rgb >> 8 & 255, rgb & 255);
   }

   public static Color read(PacketWrapper<?> wrapper) {
      return new Color(wrapper.readInt());
   }

   public static void write(PacketWrapper<?> wrapper, Color color) {
      wrapper.writeInt(color.asRGB());
   }

   public static Color readShort(PacketWrapper<?> wrapper) {
      return new Color(wrapper.readUnsignedByte(), wrapper.readUnsignedByte(), wrapper.readUnsignedByte());
   }

   public static void writeShort(PacketWrapper<?> wrapper, Color color) {
      wrapper.writeByte(color.red);
      wrapper.writeByte(color.green);
      wrapper.writeByte(color.blue);
   }

   public static Color decode(NBT nbt, PacketWrapper<?> wrapper) {
      return decode(nbt, wrapper.getServerVersion().toClientVersion());
   }

   /** @deprecated */
   @Deprecated
   public static Color decode(NBT nbt, ClientVersion version) {
      if (nbt instanceof NBTNumber) {
         return new Color(((NBTNumber)nbt).getAsInt());
      } else {
         NBTList<?> list = (NBTList)nbt;
         float red = ((NBTNumber)list.getTag(0)).getAsFloat();
         float green = ((NBTNumber)list.getTag(1)).getAsFloat();
         float blue = ((NBTNumber)list.getTag(2)).getAsFloat();
         return new Color(red, green, blue);
      }
   }

   public static NBT encode(PacketWrapper<?> wrapper, Color color) {
      return encode(color, wrapper.getServerVersion().toClientVersion());
   }

   /** @deprecated */
   @Deprecated
   public static NBT encode(Color color, ClientVersion version) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_21_2)) {
         return new NBTInt(color.asRGB());
      } else {
         NBTList<NBTFloat> list = new NBTList(NBTType.FLOAT, 3);
         list.addTag(new NBTFloat((float)color.red));
         list.addTag(new NBTFloat((float)color.green));
         list.addTag(new NBTFloat((float)color.blue));
         return list;
      }
   }

   public AlphaColor withAlpha() {
      return this.withAlpha(255);
   }

   public AlphaColor withAlpha(@Range(from = 0L,to = 255L) int alpha) {
      return new AlphaColor(alpha, this.red, this.green, this.blue);
   }

   public Color withRed(@Range(from = 0L,to = 255L) int red) {
      return new Color(red, this.green, this.blue);
   }

   public Color withGreen(@Range(from = 0L,to = 255L) int green) {
      return new Color(this.red, green, this.blue);
   }

   public Color withBlue(@Range(from = 0L,to = 255L) int blue) {
      return new Color(this.red, this.green, blue);
   }

   public int asRGB() {
      return this.red << 16 | this.green << 8 | this.blue;
   }

   public Color plus(Color other) {
      return new Color(this.red + other.red, this.green + other.green, this.blue + other.blue);
   }

   public Color minus(Color other) {
      return new Color(this.red - other.red, this.green - other.green, this.blue - other.blue);
   }

   public Color times(Color other) {
      if (other instanceof AlphaColor) {
         return other.times(this);
      } else if (this.red == 255 && this.green == 255 && this.blue == 255) {
         return other;
      } else {
         return other.red == 255 && other.green == 255 && other.blue == 255 ? this : new Color(this.red * other.red / 255, this.green * other.green / 255, this.blue * other.blue / 255);
      }
   }

   public AlphaColor blendWith(AlphaColor source) {
      int srcAlpha = source.alpha();
      if (srcAlpha == 255) {
         return source;
      } else if (srcAlpha == 0) {
         return this.withAlpha();
      } else {
         int alpha = srcAlpha + (255 - srcAlpha);
         return new AlphaColor(alpha, alphaBlendChannel(alpha, srcAlpha, this.red, source.red), alphaBlendChannel(alpha, srcAlpha, this.green, source.green), alphaBlendChannel(alpha, srcAlpha, this.blue, source.blue));
      }
   }

   protected static int alphaBlendChannel(int alpha, int srcAlpha, int dest, int src) {
      return (src * srcAlpha + dest * (alpha - srcAlpha)) / alpha;
   }

   public Color asGrayscale() {
      int grayscale = (int)((float)this.red * 0.3F + (float)this.green * 0.59F + (float)this.blue * 0.11F);
      return new Color(grayscale, grayscale, grayscale);
   }

   public Color scale(float scale) {
      return this.scale(scale, scale, scale);
   }

   public Color scale(float redScale, float greenScale, float blueScale) {
      return new Color((int)((float)this.red * redScale), (int)((float)this.green * greenScale), (int)((float)this.blue * blueScale));
   }

   public Color lerpSrgb(Color dest, float t) {
      return new Color(MathUtil.lerp(t, this.red, dest.red), MathUtil.lerp(t, this.green, dest.green), MathUtil.lerp(t, this.blue, dest.blue));
   }

   @Range(
      from = 0L,
      to = 255L
   )
   public int alpha() {
      return 255;
   }

   @Range(
      from = 0L,
      to = 255L
   )
   public int red() {
      return this.red;
   }

   @Range(
      from = 0L,
      to = 255L
   )
   public int green() {
      return this.green;
   }

   @Range(
      from = 0L,
      to = 255L
   )
   public int blue() {
      return this.blue;
   }

   public boolean equals(Object obj) {
      if (obj instanceof Color) {
         return this.asRGB() == ((Color)obj).asRGB();
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.asRGB()});
   }
}
