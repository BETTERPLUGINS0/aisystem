package com.volmit.iris.util.format;

import com.volmit.iris.Iris;
import com.volmit.iris.util.kyori.adventure.text.minimessage.MiniMessage;
import com.volmit.iris.util.plugin.VolmitSender;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.DyeColor;

public enum C {
   BLACK('0', 0) {
      public ChatColor asBungee() {
         return ChatColor.BLACK;
      }
   },
   DARK_BLUE('1', 1) {
      public ChatColor asBungee() {
         return ChatColor.DARK_BLUE;
      }
   },
   DARK_GREEN('2', 2) {
      public ChatColor asBungee() {
         return ChatColor.DARK_GREEN;
      }
   },
   DARK_AQUA('3', 3) {
      public ChatColor asBungee() {
         return ChatColor.DARK_AQUA;
      }
   },
   DARK_RED('4', 4) {
      public ChatColor asBungee() {
         return ChatColor.DARK_RED;
      }
   },
   DARK_PURPLE('5', 5) {
      public ChatColor asBungee() {
         return ChatColor.DARK_PURPLE;
      }
   },
   GOLD('6', 6) {
      public ChatColor asBungee() {
         return ChatColor.GOLD;
      }
   },
   GRAY('7', 7) {
      public ChatColor asBungee() {
         return ChatColor.GRAY;
      }
   },
   DARK_GRAY('8', 8) {
      public ChatColor asBungee() {
         return ChatColor.DARK_GRAY;
      }
   },
   BLUE('9', 9) {
      public ChatColor asBungee() {
         return ChatColor.BLUE;
      }
   },
   GREEN('a', 10) {
      public ChatColor asBungee() {
         return ChatColor.GREEN;
      }
   },
   IRIS("<#1bb19e>", 'a', 10) {
      public ChatColor asBungee() {
         return ChatColor.GREEN;
      }
   },
   AQUA('b', 11) {
      public ChatColor asBungee() {
         return ChatColor.AQUA;
      }
   },
   RED('c', 12) {
      public ChatColor asBungee() {
         return ChatColor.RED;
      }
   },
   LIGHT_PURPLE('d', 13) {
      public ChatColor asBungee() {
         return ChatColor.LIGHT_PURPLE;
      }
   },
   YELLOW('e', 14) {
      public ChatColor asBungee() {
         return ChatColor.YELLOW;
      }
   },
   WHITE('f', 15) {
      public ChatColor asBungee() {
         return ChatColor.WHITE;
      }
   },
   MAGIC("<obf>", 'k', 16, true) {
      public ChatColor asBungee() {
         return ChatColor.MAGIC;
      }
   },
   BOLD('l', 17, true) {
      public ChatColor asBungee() {
         return ChatColor.BOLD;
      }
   },
   STRIKETHROUGH('m', 18, true) {
      public ChatColor asBungee() {
         return ChatColor.STRIKETHROUGH;
      }
   },
   UNDERLINE("<underlined>", 'n', 19, true) {
      public ChatColor asBungee() {
         return ChatColor.UNDERLINE;
      }
   },
   ITALIC('o', 20, true) {
      public ChatColor asBungee() {
         return ChatColor.ITALIC;
      }
   },
   RESET('r', 21) {
      public ChatColor asBungee() {
         return ChatColor.RESET;
      }
   };

   public static final char COLOR_CHAR = '§';
   public static final C[] COLORCYCLE = new C[]{GOLD, YELLOW, GREEN, AQUA, LIGHT_PURPLE, AQUA, GREEN, YELLOW, GOLD, RED};
   private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
   private static final C[] COLORS = new C[]{BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE};
   private static final Map<Integer, C> BY_ID = new HashMap();
   private static final Map<Character, C> BY_CHAR = new HashMap();
   private static final Map<DyeColor, C> dyeChatMap = new HashMap();
   private static final Map<C, String> chatHexMap = new HashMap();
   private static final Map<DyeColor, String> dyeHexMap = new HashMap();
   private final int intCode;
   private final char code;
   private final String token;
   private final boolean isFormat;
   private final String toString;

   private C(char code, int intCode) {
      this("^", var3, var4, false);
   }

   private C(String token, char code, int intCode) {
      this(var3, var4, var5, false);
   }

   private C(char code, int intCode, boolean isFormat) {
      this("^", var3, var4, false);
   }

   private C(String token, char code, int intCode, boolean isFormat) {
      this.code = var4;
      this.token = var3.equalsIgnoreCase("^") ? "<" + this.name().toLowerCase(Locale.ROOT) + ">" : var3;
      this.intCode = var5;
      this.isFormat = var6;
      this.toString = new String(new char[]{'§', var4});
   }

   public static float[] spin(float[] c, int shift) {
      return new float[]{spin(var0[0], var1), spinc(var0[1], var1), spinc(var0[2], var1)};
   }

   public static float[] spin(float[] c, int a, int b, int d) {
      return new float[]{spin(var0[0], var1), spinc(var0[1], var2), spinc(var0[2], var3)};
   }

   public static float spin(float c, int shift) {
      float var2 = (float)(((int)Math.floor((double)(var0 * 360.0F)) + var1) % 360) / 360.0F;
      return var2 < 0.0F ? 1.0F - var2 : var2;
   }

   public static float spinc(float c, int shift) {
      float var2 = (float)((int)Math.floor((double)(var0 * 255.0F)) + var1) / 255.0F;
      return Math.max(0.0F, Math.min(var2, 1.0F));
   }

   public static Color spin(Color c, int h, int s, int b) {
      float[] var4 = Color.RGBtoHSB(var0.getRed(), var0.getGreen(), var0.getBlue(), (float[])null);
      var4 = spin(var4, var1, var2, var3);
      return Color.getHSBColor(var4[0], var4[1], var4[2]);
   }

   public static String spinToHex(C color, int h, int s, int b) {
      String var10000 = Integer.toHexString(spin(var0.awtColor(), var1, var2, var3).getRGB());
      return "#" + var10000.substring(2);
   }

   public static String mini(String s) {
      String var1 = compress(var0);
      StringBuilder var2 = new StringBuilder();
      boolean var3 = false;
      char[] var4 = var1.toCharArray();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         char var7 = var4[var6];
         if (!var3) {
            if (var7 == 167) {
               var3 = true;
            } else {
               var2.append(var7);
            }
         } else {
            var3 = false;
            C var8 = getByChar(var7);
            var2.append(var8.token);
         }
      }

      return var2.toString();
   }

   public static String aura(String s, int hrad, int srad, int vrad) {
      return aura(var0, var1, var2, var3, 0.3D);
   }

   public static String aura(String s, int hrad, int srad, int vrad, double pulse) {
      String var6 = compress(var0);
      StringBuilder var7 = new StringBuilder();
      boolean var8 = false;
      char[] var9 = var6.toCharArray();
      int var10 = var9.length;

      for(int var11 = 0; var11 < var10; ++var11) {
         char var12 = var9[var11];
         if (var8) {
            var8 = false;
            C var13 = getByChar(var12);
            if (var1 == 0 && var2 == 0 && var3 == 0) {
               var7.append(getByChar(var12).token);
            } else if (var4 > 0.0D) {
               var7.append(VolmitSender.pulse(spinToHex(var13, var1, var2, var3), spinToHex(var13, -var1, -var2, -var3), var4));
            } else {
               var7.append("<gradient:").append(spinToHex(var13, var1, var2, var3)).append(":").append(spinToHex(var13, -var1, -var2, -var3)).append(">");
            }
         } else if (var12 == 167) {
            var8 = true;
         } else {
            var7.append(var12);
         }
      }

      return var7.toString();
   }

   public static String compress(String c) {
      return BaseComponent.toLegacyText(TextComponent.fromLegacyText(var0));
   }

   public static C getByChar(char code) {
      try {
         C var1 = (C)BY_CHAR.get(var0);
         return var1 == null ? WHITE : var1;
      } catch (Exception var2) {
         Iris.reportError(var2);
         return WHITE;
      }
   }

   public static C getByChar(String code) {
      try {
         Validate.notNull(var0, "Code cannot be null");
         Validate.isTrue(var0.length() > 0, "Code must have at least one char");
         return (C)BY_CHAR.get(var0.charAt(0));
      } catch (Exception var2) {
         Iris.reportError(var2);
         return WHITE;
      }
   }

   public static String stripColor(final String input) {
      return var0 == null ? null : STRIP_COLOR_PATTERN.matcher(var0).replaceAll("");
   }

   public static String strip(final String input) {
      return var0 == null ? null : MiniMessage.miniMessage().stripTags(stripColor(var0));
   }

   public static C dyeToChat(DyeColor dclr) {
      return dyeChatMap.containsKey(var0) ? (C)dyeChatMap.get(var0) : MAGIC;
   }

   public static DyeColor chatToDye(org.bukkit.ChatColor color) {
      Iterator var1 = dyeChatMap.entrySet().iterator();

      Entry var2;
      do {
         if (!var1.hasNext()) {
            return DyeColor.BLACK;
         }

         var2 = (Entry)var1.next();
      } while(!((C)var2.getValue()).toString().equals(var0.toString()));

      return (DyeColor)var2.getKey();
   }

   public static String chatToHex(C clr) {
      return chatHexMap.containsKey(var0) ? (String)chatHexMap.get(var0) : "#000000";
   }

   public static String dyeToHex(DyeColor clr) {
      return dyeHexMap.containsKey(var0) ? (String)dyeHexMap.get(var0) : "#000000";
   }

   public static org.bukkit.Color hexToColor(String hex) {
      if (var0.startsWith("#")) {
         var0 = var0.substring(1);
      }

      if (var0.contains("x")) {
         var0 = var0.substring(var0.indexOf("x"));
      }

      if (var0.length() != 6 && var0.length() != 3) {
         return null;
      } else {
         int var1 = var0.length() / 3;
         int var2 = 1 << (2 - var1) * 4;
         int var3 = 0;
         int var4 = 0;

         for(int var5 = 0; var5 < var0.length(); var5 += var1) {
            var3 |= var2 * Integer.parseInt(var0.substring(var5, var5 + var1), 16) << var4 * 8;
            ++var4;
         }

         return org.bukkit.Color.fromBGR(var3 & 16777215);
      }
   }

   public static org.bukkit.Color rgbToColor(String rgb) {
      String[] var1 = var0.split("[^0-9]+");
      if (var1.length < 3) {
         return null;
      } else {
         int var2 = 0;

         for(int var3 = 0; var3 < 3; ++var3) {
            var2 |= Integer.parseInt(var1[var3]) << var3 * 8;
         }

         return org.bukkit.Color.fromBGR(var2 & 16777215);
      }
   }

   public static String generateColorTable() {
      StringBuilder var0 = new StringBuilder();
      var0.append("<table><tr><td>Chat Color</td><td>Color</td></tr>");
      Iterator var1 = chatHexMap.entrySet().iterator();

      Entry var2;
      while(var1.hasNext()) {
         var2 = (Entry)var1.next();
         var0.append(String.format("<tr><td style='color: %2$s;'>%1$s</td><td style='color: %2$s;'>Test String</td></tr>", ((C)var2.getKey()).name(), var2.getValue()));
      }

      var0.append("</table>");
      var0.append("<table><tr><td>Dye Color</td><td>Color</td></tr>");
      var1 = dyeHexMap.entrySet().iterator();

      while(var1.hasNext()) {
         var2 = (Entry)var1.next();
         var0.append(String.format("<tr><td style='color: %2$s;'>%1$s</td><td style='color: %2$s;'>Test String</td></tr>", ((DyeColor)var2.getKey()).name(), var2.getValue()));
      }

      var0.append("</table>");
      return var0.toString();
   }

   public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
      if (var1 == null) {
         return null;
      } else {
         char[] var2 = var1.toCharArray();

         for(int var3 = 0; var3 < var2.length - 1; ++var3) {
            if (var2[var3] == var0 && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(var2[var3 + 1]) > -1) {
               var2[var3] = 167;
               var2[var3 + 1] = Character.toLowerCase(var2[var3 + 1]);
            }
         }

         return new String(var2);
      }
   }

   public static C fromItemMeta(byte c) {
      C[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         C var4 = var1[var3];
         if (var4.getItemMeta() == var0) {
            return var4;
         }
      }

      return null;
   }

   public static C randomColor() {
      return COLORS[(int)(Math.random() * (double)(COLORS.length - 1))];
   }

   public static String getLastColors(String input) {
      StringBuilder var1 = new StringBuilder();
      int var2 = var0.length();

      for(int var3 = var2 - 1; var3 > -1; --var3) {
         char var4 = var0.charAt(var3);
         if (var4 == 167 && var3 < var2 - 1) {
            char var5 = var0.charAt(var3 + 1);
            C var6 = getByChar(var5);
            if (var6 != null) {
               var1.insert(0, var6);
               if (var6.isColor() || var6.equals(RESET)) {
                  break;
               }
            }
         }
      }

      return var1.toString();
   }

   public ChatColor asBungee() {
      return ChatColor.RESET;
   }

   public char getChar() {
      return this.code;
   }

   public String toString() {
      return this.intCode == -1 ? this.token : this.toString;
   }

   public DyeColor dye() {
      return chatToDye(this.chatColor());
   }

   public String hex() {
      return chatToHex(this);
   }

   public Color awtColor() {
      return Color.decode(this.hex());
   }

   public boolean isFormat() {
      return this.isFormat;
   }

   public boolean isColor() {
      return !this.isFormat && this != RESET;
   }

   public org.bukkit.ChatColor chatColor() {
      return org.bukkit.ChatColor.getByChar(this.code);
   }

   public byte getMeta() {
      byte var10000;
      switch(this.ordinal()) {
      case 0:
         var10000 = 0;
         break;
      case 1:
         var10000 = 1;
         break;
      case 2:
         var10000 = 2;
         break;
      case 3:
      case 9:
         var10000 = 9;
         break;
      case 4:
         var10000 = 4;
         break;
      case 5:
         var10000 = 5;
         break;
      case 6:
         var10000 = 6;
         break;
      case 7:
         var10000 = 7;
         break;
      case 8:
         var10000 = 8;
         break;
      case 10:
         var10000 = 10;
         break;
      case 11:
      case 16:
      default:
         var10000 = 15;
         break;
      case 12:
         var10000 = 11;
         break;
      case 13:
         var10000 = 12;
         break;
      case 14:
         var10000 = 13;
         break;
      case 15:
         var10000 = 14;
         break;
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
         var10000 = -1;
      }

      return var10000;
   }

   public byte getItemMeta() {
      byte var10000;
      switch(this.ordinal()) {
      case 1:
         var10000 = 11;
         break;
      case 2:
         var10000 = 13;
         break;
      case 3:
      case 12:
         var10000 = 9;
         break;
      case 4:
      case 13:
         var10000 = 14;
         break;
      case 5:
         var10000 = 10;
         break;
      case 6:
      case 15:
         var10000 = 4;
         break;
      case 7:
         var10000 = 8;
         break;
      case 8:
         var10000 = 7;
         break;
      case 9:
         var10000 = 3;
         break;
      case 10:
         var10000 = 5;
         break;
      case 11:
      default:
         var10000 = 15;
         break;
      case 14:
         var10000 = 2;
         break;
      case 16:
         var10000 = 0;
         break;
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
         var10000 = -1;
      }

      return var10000;
   }

   // $FF: synthetic method
   private static C[] $values() {
      return new C[]{BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, IRIS, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE, MAGIC, BOLD, STRIKETHROUGH, UNDERLINE, ITALIC, RESET};
   }

   static {
      chatHexMap.put(BLACK, "#000000");
      chatHexMap.put(DARK_BLUE, "#0000AA");
      chatHexMap.put(IRIS, "#1bb19e");
      chatHexMap.put(DARK_GREEN, "#00AA00");
      chatHexMap.put(DARK_AQUA, "#00AAAA");
      chatHexMap.put(DARK_RED, "#AA0000");
      chatHexMap.put(DARK_PURPLE, "#AA00AA");
      chatHexMap.put(GOLD, "#FFAA00");
      chatHexMap.put(GRAY, "#AAAAAA");
      chatHexMap.put(DARK_GRAY, "#555555");
      chatHexMap.put(BLUE, "#5555FF");
      chatHexMap.put(GREEN, "#55FF55");
      chatHexMap.put(AQUA, "#55FFFF");
      chatHexMap.put(RED, "#FF5555");
      chatHexMap.put(LIGHT_PURPLE, "#FF55FF");
      chatHexMap.put(YELLOW, "#FFFF55");
      chatHexMap.put(WHITE, "#FFFFFF");
      dyeChatMap.put(DyeColor.BLACK, DARK_GRAY);
      dyeChatMap.put(DyeColor.BLUE, DARK_BLUE);
      dyeChatMap.put(DyeColor.BROWN, GOLD);
      dyeChatMap.put(DyeColor.CYAN, AQUA);
      dyeChatMap.put(DyeColor.GRAY, GRAY);
      dyeChatMap.put(DyeColor.GREEN, DARK_GREEN);
      dyeChatMap.put(DyeColor.LIGHT_BLUE, BLUE);
      dyeChatMap.put(DyeColor.LIME, GREEN);
      dyeChatMap.put(DyeColor.MAGENTA, LIGHT_PURPLE);
      dyeChatMap.put(DyeColor.ORANGE, GOLD);
      dyeChatMap.put(DyeColor.PINK, LIGHT_PURPLE);
      dyeChatMap.put(DyeColor.PURPLE, DARK_PURPLE);
      dyeChatMap.put(DyeColor.RED, RED);
      dyeChatMap.put(DyeColor.LIGHT_GRAY, GRAY);
      dyeChatMap.put(DyeColor.WHITE, WHITE);
      dyeChatMap.put(DyeColor.YELLOW, YELLOW);
      dyeHexMap.put(DyeColor.BLACK, "#181414");
      dyeHexMap.put(DyeColor.BLUE, "#253193");
      dyeHexMap.put(DyeColor.BROWN, "#56331c");
      dyeHexMap.put(DyeColor.CYAN, "#267191");
      dyeHexMap.put(DyeColor.GRAY, "#414141");
      dyeHexMap.put(DyeColor.GREEN, "#364b18");
      dyeHexMap.put(DyeColor.LIGHT_BLUE, "#6387d2");
      dyeHexMap.put(DyeColor.LIME, "#39ba2e");
      dyeHexMap.put(DyeColor.MAGENTA, "#be49c9");
      dyeHexMap.put(DyeColor.ORANGE, "#ea7e35");
      dyeHexMap.put(DyeColor.PINK, "#d98199");
      dyeHexMap.put(DyeColor.PURPLE, "#7e34bf");
      dyeHexMap.put(DyeColor.RED, "#9e2b27");
      dyeHexMap.put(DyeColor.LIGHT_GRAY, "#a0a7a7");
      dyeHexMap.put(DyeColor.WHITE, "#a4a4a4");
      dyeHexMap.put(DyeColor.YELLOW, "#c2b51c");
      C[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         C var3 = var0[var2];
         BY_ID.put(var3.intCode, var3);
         BY_CHAR.put(var3.code, var3);
      }

   }
}
