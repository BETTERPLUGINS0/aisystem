package emanondev.itemedit.utility;

import java.awt.Color;
import net.md_5.bungee.api.ChatColor;

public class ColorBuilder implements Cloneable {
   private int red;
   private int green;
   private int blue;
   private int alpha;

   private ColorBuilder(int red, int green, int blue, int alpha) {
      this.red = this.clamp(red);
      this.green = this.clamp(green);
      this.blue = this.clamp(blue);
      this.alpha = this.clamp(alpha);
   }

   public static ColorBuilder fromRGBA(int red, int green, int blue, int alpha) {
      return new ColorBuilder(red, green, blue, alpha);
   }

   public static ColorBuilder fromRGB(int red, int green, int blue) {
      return new ColorBuilder(red, green, blue, 255);
   }

   public static ColorBuilder fromRGB() {
      return new ColorBuilder(0, 0, 0, 255);
   }

   public static ColorBuilder from(Color color) {
      return new ColorBuilder(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
   }

   public static ColorBuilder from(org.bukkit.Color color) {
      return new ColorBuilder(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
   }

   public static ColorBuilder from(ChatColor color) {
      int rgb = color.getColor().getRGB();
      return fromRGB(rgb).setAlpha(color.getColor().getAlpha());
   }

   public static ColorBuilder from(org.bukkit.ChatColor color) {
      int rgb = color.asBungee().getColor().getRGB();
      return fromRGB(rgb);
   }

   public static ColorBuilder fromRGB(String hex) {
      if (hex.startsWith("#")) {
         hex = hex.substring(1);
      }

      if (hex.length() != 6) {
         throw new IllegalArgumentException("Hex color must be 6 characters: " + hex);
      } else {
         int rgb = Integer.parseInt(hex, 16);
         return fromRGB(rgb);
      }
   }

   public static ColorBuilder fromRGB(int rgb) {
      int red = rgb >> 16 & 255;
      int green = rgb >> 8 & 255;
      int blue = rgb & 255;
      return new ColorBuilder(red, green, blue, 255);
   }

   public int getRed() {
      return this.red;
   }

   public int getGreen() {
      return this.green;
   }

   public int getBlue() {
      return this.blue;
   }

   public int getAlpha() {
      return this.alpha;
   }

   public ColorBuilder setRed(int red) {
      this.red = this.clamp(red);
      return this;
   }

   public ColorBuilder setGreen(int green) {
      this.green = this.clamp(green);
      return this;
   }

   public ColorBuilder setBlue(int blue) {
      this.blue = this.clamp(blue);
      return this;
   }

   public ColorBuilder setAlpha(int alpha) {
      this.alpha = this.clamp(alpha);
      return this;
   }

   public ColorBuilder incRed(int amount) {
      this.red = this.clamp(this.red + amount);
      return this;
   }

   public ColorBuilder incGreen(int amount) {
      this.green = this.clamp(this.green + amount);
      return this;
   }

   public ColorBuilder incBlue(int amount) {
      this.blue = this.clamp(this.blue + amount);
      return this;
   }

   public ColorBuilder inc(int red, int green, int blue) {
      this.red = this.clamp(this.red + red);
      this.green = this.clamp(this.green + green);
      this.blue = this.clamp(this.blue + blue);
      return this;
   }

   public ColorBuilder inc(ColorBuilder color) {
      this.red = this.clamp(this.red + color.getRed());
      this.green = this.clamp(this.green + color.getGreen());
      this.blue = this.clamp(this.blue + color.getBlue());
      return this;
   }

   public ColorBuilder dec(ColorBuilder color) {
      this.red = this.clamp(this.red - color.getRed());
      this.green = this.clamp(this.green - color.getGreen());
      this.blue = this.clamp(this.blue - color.getBlue());
      return this;
   }

   public String toHex() {
      return String.format("#%02X%02X%02X", this.red, this.green, this.blue);
   }

   public int toInt() {
      return this.red << 16 | this.green << 8 | this.blue;
   }

   public Color toAwt() {
      return new Color(this.red, this.green, this.blue);
   }

   public org.bukkit.Color toBukkit() {
      return org.bukkit.Color.fromRGB(this.red, this.green, this.blue);
   }

   public ChatColor toBungee() {
      return ChatColor.of(new Color(this.red, this.green, this.blue));
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         ColorBuilder other = (ColorBuilder)obj;
         return this.red == other.red && this.green == other.green && this.blue == other.blue && this.alpha == other.alpha;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = Integer.hashCode(this.red);
      result = 31 * result + Integer.hashCode(this.green);
      result = 31 * result + Integer.hashCode(this.blue);
      result = 31 * result + Integer.hashCode(this.alpha);
      return result;
   }

   public ColorBuilder clone() {
      return new ColorBuilder(this.red, this.green, this.blue, this.alpha);
   }

   private int clamp(int value) {
      return Math.max(0, Math.min(255, value));
   }
}
