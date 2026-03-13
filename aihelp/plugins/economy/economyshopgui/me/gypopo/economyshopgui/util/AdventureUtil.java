package me.gypopo.economyshopgui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.Builder;

public class AdventureUtil {
   private static final Pattern tagPattern;
   private static final Pattern rgbSerializer = Pattern.compile("(?<![:/<&{])(#([0-9a-fA-F]{6}))");
   private static final GsonComponentSerializer gsonSerializer = GsonComponentSerializer.gson();
   private static final LegacyComponentSerializer legacyDeserializer = LegacyComponentSerializer.legacySection();
   private static final LegacyComponentSerializer legacySerializer;
   private static final MiniMessage miniMessage;

   public String formatMiniToLegacy(String msg) {
      return legacyDeserializer.serialize(this.formatMini(msg));
   }

   public String formatLegacyToMini(String msg) {
      return ((String)miniMessage.serialize(legacySerializer.deserialize(rgbSerializer.matcher(msg.replace('§', '&')).replaceAll("<$1>")))).replace("\\<", "<");
   }

   public Component formatMini(String msg) {
      return miniMessage.deserialize(msg.replace('§', '&'));
   }

   public Component formatLegacyAndMini(String msg) {
      return this.formatMini(this.formatLegacyToMini(msg));
   }

   public String getGsonComponent(String s) {
      return (String)gsonSerializer.serialize(this.formatMini(this.formatLegacyToMini(s)));
   }

   public static Component fromGson(String gson) {
      return gsonSerializer.deserialize(gson);
   }

   public static String toLegacy(Component comp) {
      return legacyDeserializer.serialize(comp);
   }

   public static String serialize(Component component) {
      return (String)miniMessage.serialize(component);
   }

   public static String asGson(Component comp) {
      return (String)gsonSerializer.serialize(comp);
   }

   public String stripColor(String s) {
      return tagPattern.matcher(s).replaceAll("");
   }

   public static String getLastColor(String miniFormat) {
      int pos = miniFormat.length();
      Map<String, Boolean> formattingState = new LinkedHashMap();
      String color = null;
      boolean colorClosed = false;

      String formatName;
      while(pos > 0) {
         int openBracket = miniFormat.lastIndexOf(60, pos - 1);
         if (openBracket == -1) {
            break;
         }

         int closeBracket = miniFormat.indexOf(62, openBracket);
         if (closeBracket == -1) {
            pos = openBracket;
         } else {
            String tag = miniFormat.substring(openBracket + 1, closeBracket);
            if (tag.startsWith("/")) {
               formatName = tag.substring(1);
               if (!formatName.isEmpty() && !AdventureUtil.Color.isValid(formatName) && !tag.substring(1).matches("#[0-9a-fA-F]{6}")) {
                  if (AdventureUtil.Format.isValid(formatName)) {
                     formattingState.put(formatName.toLowerCase(), false);
                  }
               } else {
                  colorClosed = true;
               }
            } else if (color == null && AdventureUtil.Format.isValid(tag)) {
               formatName = tag.toLowerCase();
               if (!formattingState.containsKey(formatName)) {
                  formattingState.put(formatName, true);
               }
            } else if (tag.startsWith("#") && tag.matches("#[0-9a-fA-F]{6}") || AdventureUtil.Color.isValid(tag)) {
               color = tag;
               break;
            }

            pos = openBracket;
         }
      }

      if (color == null) {
         return "";
      } else if (colorClosed) {
         return "";
      } else {
         StringBuilder result = (new StringBuilder("<")).append(color).append(">");
         List<String> formats = new ArrayList();
         Iterator var11 = formattingState.entrySet().iterator();

         while(var11.hasNext()) {
            Entry<String, Boolean> entry = (Entry)var11.next();
            if ((Boolean)entry.getValue()) {
               formats.add((String)entry.getKey());
            }
         }

         Collections.reverse(formats);
         var11 = formats.iterator();

         while(var11.hasNext()) {
            formatName = (String)var11.next();
            result.append("<").append(formatName).append(">");
         }

         return result.toString();
      }
   }

   static {
      Builder legacyBuilder = ((Builder)LegacyComponentSerializer.legacyAmpersand().toBuilder()).flattener(ComponentFlattener.basic());
      if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_16_R1)) {
         legacyBuilder.hexColors();
         legacyBuilder.useUnusualXRepeatedCharacterHexFormat();
      }

      legacySerializer = legacyBuilder.build();
      net.kyori.adventure.text.minimessage.MiniMessage.Builder miniBuilder = MiniMessage.builder();
      miniBuilder.tags(TagResolver.standard());
      miniBuilder.strict(false);
      miniMessage = miniBuilder.build();
      Set<String> tags = new HashSet();
      tags.addAll(AdventureUtil.Color.names());
      tags.addAll(AdventureUtil.Format.names());
      tags.addAll(AdventureUtil.Special.names());
      tagPattern = Pattern.compile("</?!?(?:(" + String.join("|", tags) + ")(?::[^>]+)?|#[0-9a-fA-F]{6})>");
   }

   private static enum Color {
      BLACK("black"),
      DARK_BLUE("dark_blue"),
      DARK_GREEN("dark_green"),
      DARK_AQUA("dark_aqua"),
      DARK_RED("dark_red"),
      DARK_PURPLE("dark_purple"),
      GOLD("gold"),
      GRAY("gray"),
      DARK_GRAY("dark_gray"),
      BLUE("blue"),
      GREEN("green"),
      AQUA("aqua"),
      RED("red"),
      LIGHT_PURPLE("light_purple"),
      YELLOW("yellow"),
      WHITE("white");

      private final String name;

      private Color(String param3) {
         this.name = name;
      }

      public static List<String> names() {
         return (List)Arrays.stream(values()).map(AdventureUtil.Color::getName).collect(Collectors.toList());
      }

      public String getName() {
         return this.name;
      }

      private static boolean isValid(String color) {
         AdventureUtil.Color[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            AdventureUtil.Color c = var1[var3];
            if (c.name.equals(color.toLowerCase(Locale.ENGLISH))) {
               return true;
            }
         }

         return false;
      }

      // $FF: synthetic method
      private static AdventureUtil.Color[] $values() {
         return new AdventureUtil.Color[]{BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE};
      }
   }

   private static enum Format {
      BOLD(new String[]{"bold", "b"}),
      ITALIC(new String[]{"italic", "i", "em"}),
      UNDERLINED(new String[]{"underlined", "u"}),
      STRIKETHROUGH(new String[]{"strikethrough", "st"}),
      OBFUSCATED(new String[]{"obfuscated", "obf"});

      private final String[] names;

      private Format(String... param3) {
         this.names = names;
      }

      public static List<String> names() {
         return (List)Arrays.stream(values()).flatMap((s) -> {
            return Arrays.stream(s.getNames());
         }).collect(Collectors.toList());
      }

      public String[] getNames() {
         return this.names;
      }

      private static boolean isValid(String s) {
         s = s.toLowerCase(Locale.ROOT);
         AdventureUtil.Format[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            AdventureUtil.Format f = var1[var3];
            String[] var5 = f.names;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String name = var5[var7];
               if (name.equals(s)) {
                  return true;
               }
            }
         }

         return false;
      }

      // $FF: synthetic method
      private static AdventureUtil.Format[] $values() {
         return new AdventureUtil.Format[]{BOLD, ITALIC, UNDERLINED, STRIKETHROUGH, OBFUSCATED};
      }
   }

   private static enum Special {
      COLOR(new String[]{"color", "colour", "c"}),
      HOVER(new String[]{"hover"}),
      CLICK(new String[]{"click"}),
      KEY(new String[]{"key"}),
      TRANSLATABLE(new String[]{"lang", "translatable", "tr"}),
      TRANSLATABLE_FALLBACK(new String[]{"lang_or", "translate_or", "tr_or"}),
      INSERTION(new String[]{"insertion"}),
      FONT(new String[]{"font"}),
      GRADIENT(new String[]{"gradient"}),
      RAINBOW(new String[]{"rainbow"}),
      TRANSITION(new String[]{"transition"}),
      RESET(new String[]{"reset"}),
      NEWLINE(new String[]{"newLine", "br"}),
      SELECTOR(new String[]{"selector", "sel"}),
      SCORE(new String[]{"score"}),
      NBT(new String[]{"nbt", "data"});

      private final String[] names;

      private Special(String... param3) {
         this.names = names;
      }

      public static List<String> names() {
         return (List)Arrays.stream(values()).flatMap((s) -> {
            return Arrays.stream(s.getNames());
         }).collect(Collectors.toList());
      }

      public String[] getNames() {
         return this.names;
      }

      private static boolean isValid(String s) {
         s = s.toLowerCase(Locale.ROOT);
         AdventureUtil.Special[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            AdventureUtil.Special f = var1[var3];
            String[] var5 = f.names;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String name = var5[var7];
               if (name.equals(s)) {
                  return true;
               }
            }
         }

         return false;
      }

      // $FF: synthetic method
      private static AdventureUtil.Special[] $values() {
         return new AdventureUtil.Special[]{COLOR, HOVER, CLICK, KEY, TRANSLATABLE, TRANSLATABLE_FALLBACK, INSERTION, FONT, GRADIENT, RAINBOW, TRANSITION, RESET, NEWLINE, SELECTOR, SCORE, NBT};
      }
   }
}
