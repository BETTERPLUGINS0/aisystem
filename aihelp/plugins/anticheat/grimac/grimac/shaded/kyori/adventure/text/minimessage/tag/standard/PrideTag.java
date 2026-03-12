package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class PrideTag extends GradientTag {
   private static final String PRIDE = "pride";
   static final TagResolver RESOLVER = TagResolver.resolver("pride", PrideTag::create);
   private static final Map<String, List<TextColor>> FLAGS;
   private final String flag;

   static Tag create(final ArgumentQueue args, final Context ctx) {
      double phase = 0.0D;
      String flag = "pride";
      if (args.hasNext()) {
         String value = args.pop().value().toLowerCase(Locale.ROOT);
         if (FLAGS.containsKey(value)) {
            flag = value;
         } else if (!value.isEmpty()) {
            try {
               phase = Double.parseDouble(value);
            } catch (NumberFormatException var7) {
               throw ctx.newException("Expected phase, got " + value);
            }

            if (phase < -1.0D || phase > 1.0D) {
               throw ctx.newException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0, 1.0] (inclusive).", phase), args);
            }
         }
      }

      return new PrideTag(phase, (List)FLAGS.get(flag), flag, ctx);
   }

   PrideTag(final double phase, @NotNull final List<TextColor> colors, @NotNull final String flag, final Context ctx) {
      super(phase, colors, ctx);
      this.flag = flag;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("flag", this.flag), ExaminableProperty.of("phase", this.phase));
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.flag, this.phase});
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         PrideTag that = (PrideTag)other;
         return this.phase == that.phase && this.flag.equals(that.flag);
      } else {
         return false;
      }
   }

   @NotNull
   private static List<TextColor> colors(@NotNull final int... colors) {
      return (List)Arrays.stream(colors).mapToObj(TextColor::color).collect(Collectors.toList());
   }

   static {
      Map<String, List<TextColor>> flags = new HashMap();
      flags.put("pride", colors(15007744, 16747776, 16772608, 164129, 19711, 7798920));
      flags.put("progress", colors(16777215, 16756679, 7591918, 6371605, 0, 15007744, 16747776, 16772608, 164129, 19711, 7798920));
      flags.put("trans", colors(6017019, 16100281, 16777215, 16100281, 6017019));
      flags.put("bi", colors(14025328, 10178454, 14504));
      flags.put("pan", colors(16718989, 16766720, 1750015));
      flags.put("nb", colors(16577585, 16579836, 10312146, 2631720));
      flags.put("lesbian", colors(14034944, 16751446, 16777215, 13918886, 10748002));
      flags.put("ace", colors(0, 10790052, 16777215, 8454273));
      flags.put("agender", colors(0, 12237498, 16777215, 12252292, 16777215, 12237498, 0));
      flags.put("demisexual", colors(0, 16777215, 7209073, 13882323));
      flags.put("genderqueer", colors(11894749, 16777215, 4817438));
      flags.put("genderfluid", colors(16676514, 16777215, 12522199, 0, 3161278));
      flags.put("intersex", colors(16766976, 7930538, 16766976));
      flags.put("aro", colors(3909440, 11064442, 16777215, 11250603, 0));
      flags.put("baker", colors(13461247, 16737689, 16646144, 16685312, 16776961, 39168, 39371, 3473561, 10027161));
      flags.put("philly", colors(0, 7884567, 16646144, 16616448, 16770304, 1154827, 410803, 12725980));
      flags.put("queer", colors(0, 10148330, 41960, 11920669, 16777215, 16763149, 16541287, 16690889, 0));
      flags.put("gay", colors(495216, 2543274, 10021057, 16777215, 8105442, 5261771, 4004472));
      flags.put("bigender", colors(12876192, 15509195, 14010344, 16777215, 14010344, 10143720, 7111631));
      flags.put("demigender", colors(8355711, 12829635, 16514932, 16777215, 16514932, 12829635, 8355711));
      FLAGS = Collections.unmodifiableMap(flags);
   }
}
