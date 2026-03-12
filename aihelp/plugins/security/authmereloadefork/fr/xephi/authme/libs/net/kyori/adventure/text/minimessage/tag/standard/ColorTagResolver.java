package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.standard;

import fr.xephi.authme.libs.net.kyori.adventure.text.format.NamedTextColor;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.TextColor;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.Context;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.ParsingException;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ColorTagResolver implements TagResolver, SerializableResolver.Single {
   private static final String COLOR_3 = "c";
   private static final String COLOR_2 = "colour";
   private static final String COLOR = "color";
   static final TagResolver INSTANCE = new ColorTagResolver();
   private static final StyleClaim<TextColor> STYLE = StyleClaim.claim("color", Style::color, (color, emitter) -> {
      if (color instanceof NamedTextColor) {
         emitter.tag((String)NamedTextColor.NAMES.key((NamedTextColor)color));
      } else {
         emitter.tag(color.asHexString());
      }

   });
   private static final Map<String, TextColor> COLOR_ALIASES = new HashMap();

   private static boolean isColorOrAbbreviation(final String name) {
      return name.equals("color") || name.equals("colour") || name.equals("c");
   }

   @Nullable
   public Tag resolve(@NotNull final String name, @NotNull final ArgumentQueue args, @NotNull final Context ctx) throws ParsingException {
      if (!this.has(name)) {
         return null;
      } else {
         String colorName;
         if (isColorOrAbbreviation(name)) {
            colorName = args.popOr("Expected to find a color parameter: <name>|#RRGGBB").lowerValue();
         } else {
            colorName = name;
         }

         TextColor color = resolveColor(colorName, ctx);
         return Tag.styling(color);
      }
   }

   @NotNull
   static TextColor resolveColor(@NotNull final String colorName, @NotNull final Context ctx) throws ParsingException {
      TextColor color;
      if (COLOR_ALIASES.containsKey(colorName)) {
         color = (TextColor)COLOR_ALIASES.get(colorName);
      } else if (colorName.charAt(0) == '#') {
         color = TextColor.fromHexString(colorName);
      } else {
         color = (TextColor)NamedTextColor.NAMES.value(colorName);
      }

      return color;
   }

   public boolean has(@NotNull final String name) {
      return isColorOrAbbreviation(name) || TextColor.fromHexString(name) != null || NamedTextColor.NAMES.value(name) != null || COLOR_ALIASES.containsKey(name);
   }

   @Nullable
   public StyleClaim<?> claimStyle() {
      return STYLE;
   }

   static {
      COLOR_ALIASES.put("dark_grey", NamedTextColor.DARK_GRAY);
      COLOR_ALIASES.put("grey", NamedTextColor.GRAY);
   }
}
