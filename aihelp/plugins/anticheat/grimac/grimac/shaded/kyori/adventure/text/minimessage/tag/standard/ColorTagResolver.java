package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.HashMap;
import java.util.Map;

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

   @Nullable
   static TextColor resolveColorOrNull(final String colorName) {
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

   @NotNull
   static TextColor resolveColor(@NotNull final String colorName, @NotNull final Context ctx) throws ParsingException {
      TextColor color = resolveColorOrNull(colorName);
      if (color == null) {
         throw ctx.newException(String.format("Unable to parse a color from '%s'. Please use named colours or hex (#RRGGBB) colors.", colorName));
      } else {
         return color;
      }
   }

   public boolean has(@NotNull final String name) {
      return isColorOrAbbreviation(name) || NamedTextColor.NAMES.value(name) != null || COLOR_ALIASES.containsKey(name) || TextColor.fromHexString(name) != null;
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
