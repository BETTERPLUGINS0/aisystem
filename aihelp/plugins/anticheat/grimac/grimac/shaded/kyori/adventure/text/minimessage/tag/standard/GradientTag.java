package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.Range;
import ac.grim.grimac.shaded.kyori.adventure.text.format.NamedTextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.format.TextColor;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.function.Consumer;
import java.util.stream.Stream;

class GradientTag extends AbstractColorChangingTag {
   private static final String GRADIENT = "gradient";
   private static final TextColor DEFAULT_WHITE = TextColor.color(16777215);
   private static final TextColor DEFAULT_BLACK = TextColor.color(0);
   static final TagResolver RESOLVER = SerializableResolver.claimingComponent("gradient", GradientTag::create, AbstractColorChangingTag::claimComponent);
   private int index = 0;
   private double multiplier = 1.0D;
   private final TextColor[] colors;
   @Range(
      from = -1L,
      to = 1L
   )
   double phase;
   private final boolean negativePhase;

   static Tag create(final ArgumentQueue args, final Context ctx) {
      double phase = 0.0D;
      Object textColors;
      if (!args.hasNext()) {
         textColors = Collections.emptyList();
      } else {
         textColors = new ArrayList();

         while(args.hasNext()) {
            Tag.Argument arg = args.pop();
            String argValue = arg.value();
            TextColor color = ColorTagResolver.resolveColorOrNull(argValue);
            if (color == null) {
               if (!args.hasNext()) {
                  OptionalDouble possiblePhase = arg.asDouble();
                  if (possiblePhase.isPresent()) {
                     phase = possiblePhase.getAsDouble();
                     if (!(phase < -1.0D) && !(phase > 1.0D)) {
                        break;
                     }

                     throw ctx.newException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0, 1.0] (inclusive).", phase), args);
                  }
               }

               throw ctx.newException(String.format("Unable to parse a color from '%s'. Please use named colors or hex (#RRGGBB) colors.", argValue), args);
            }

            ((List)textColors).add(color);
         }

         if (((List)textColors).size() == 1) {
            throw ctx.newException("Invalid gradient, not enough colors. Gradients must have at least two colors.", args);
         }
      }

      return new GradientTag(phase, (List)textColors, ctx);
   }

   GradientTag(final double phase, final List<TextColor> colors, final Context ctx) {
      super(ctx);
      if (colors.isEmpty()) {
         this.colors = new TextColor[]{DEFAULT_WHITE, DEFAULT_BLACK};
      } else {
         this.colors = (TextColor[])colors.toArray(new TextColor[0]);
      }

      if (phase < 0.0D) {
         this.negativePhase = true;
         this.phase = 1.0D + phase;
         Collections.reverse(Arrays.asList(this.colors));
      } else {
         this.negativePhase = false;
         this.phase = phase;
      }

   }

   protected void init() {
      this.multiplier = this.size() == 1 ? 0.0D : (double)(this.colors.length - 1) / (double)(this.size() - 1);
      this.phase *= (double)(this.colors.length - 1);
      this.index = 0;
   }

   protected void advanceColor() {
      ++this.index;
   }

   protected TextColor color() {
      double position = (double)this.index * this.multiplier + this.phase;
      int lowUnclamped = (int)Math.floor(position);
      int high = (int)Math.ceil(position) % this.colors.length;
      int low = lowUnclamped % this.colors.length;
      return TextColor.lerp((float)position - (float)lowUnclamped, this.colors[low], this.colors[high]);
   }

   @NotNull
   protected Consumer<TokenEmitter> preserveData() {
      TextColor[] colors;
      double phase;
      if (this.negativePhase) {
         colors = (TextColor[])Arrays.copyOf(this.colors, this.colors.length);
         Collections.reverse(Arrays.asList(colors));
         phase = this.phase - 1.0D;
      } else {
         colors = this.colors;
         phase = this.phase;
      }

      return (emit) -> {
         emit.tag("gradient");
         if (colors.length != 2 || !colors[0].equals(DEFAULT_WHITE) || !colors[1].equals(DEFAULT_BLACK)) {
            TextColor[] var4 = colors;
            int var5 = colors.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               TextColor color = var4[var6];
               if (color instanceof NamedTextColor) {
                  emit.argument((String)NamedTextColor.NAMES.keyOrThrow((NamedTextColor)color));
               } else {
                  emit.argument(color.asHexString());
               }
            }
         }

         if (phase != 0.0D) {
            emit.argument(Double.toString(phase));
         }

      };
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("phase", this.phase), ExaminableProperty.of("colors", (Object)this.colors));
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         GradientTag that = (GradientTag)other;
         return this.index == that.index && this.phase == that.phase && Arrays.equals(this.colors, that.colors);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = Objects.hash(new Object[]{this.index, this.phase});
      result = 31 * result + Arrays.hashCode(this.colors);
      return result;
   }
}
