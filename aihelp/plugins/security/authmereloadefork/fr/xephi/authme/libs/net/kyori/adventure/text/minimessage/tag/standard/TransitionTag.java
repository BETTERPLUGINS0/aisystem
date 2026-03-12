package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.standard;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.NamedTextColor;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.TextColor;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.Context;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Inserting;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import fr.xephi.authme.libs.net.kyori.examination.Examinable;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public final class TransitionTag implements Inserting, Examinable {
   public static final String TRANSITION = "transition";
   private final TextColor[] colors;
   private final float phase;
   private final boolean negativePhase;
   static final TagResolver RESOLVER = TagResolver.resolver("transition", TransitionTag::create);

   static Tag create(final ArgumentQueue args, final Context ctx) {
      float phase = 0.0F;
      Object textColors;
      if (!args.hasNext()) {
         textColors = Collections.emptyList();
      } else {
         textColors = new ArrayList();

         while(args.hasNext()) {
            Tag.Argument arg = args.pop();
            if (!args.hasNext()) {
               OptionalDouble possiblePhase = arg.asDouble();
               if (possiblePhase.isPresent()) {
                  phase = (float)possiblePhase.getAsDouble();
                  if (!(phase < -1.0F) && !(phase > 1.0F)) {
                     break;
                  }

                  throw ctx.newException(String.format("Gradient phase is out of range (%s). Must be in the range [-1.0f, 1.0f] (inclusive).", phase), args);
               }
            }

            String argValue = arg.value();
            TextColor parsedColor;
            if (argValue.charAt(0) == '#') {
               parsedColor = TextColor.fromHexString(argValue);
            } else {
               parsedColor = (TextColor)NamedTextColor.NAMES.value(arg.lowerValue());
            }

            if (parsedColor == null) {
               throw ctx.newException(String.format("Unable to parse a color from '%s'. Please use named colors or hex (#RRGGBB) colors.", argValue), args);
            }

            ((List)textColors).add(parsedColor);
         }

         if (((List)textColors).size() < 2) {
            throw ctx.newException("Invalid transition, not enough colors. Transitions must have at least two colors.", args);
         }
      }

      return new TransitionTag(phase, (List)textColors);
   }

   private TransitionTag(final float phase, final List<TextColor> colors) {
      if (phase < 0.0F) {
         this.negativePhase = true;
         this.phase = 1.0F + phase;
         Collections.reverse(colors);
      } else {
         this.negativePhase = false;
         this.phase = phase;
      }

      if (colors.isEmpty()) {
         this.colors = new TextColor[]{TextColor.color(16777215), TextColor.color(0)};
      } else {
         this.colors = (TextColor[])colors.toArray(new TextColor[0]);
      }

   }

   @NotNull
   public Component value() {
      return Component.text("", this.color());
   }

   private TextColor color() {
      float steps = 1.0F / (float)(this.colors.length - 1);

      for(int colorIndex = 1; colorIndex < this.colors.length; ++colorIndex) {
         float val = (float)colorIndex * steps;
         if (val >= this.phase) {
            float factor = 1.0F + (this.phase - val) * (float)(this.colors.length - 1);
            if (this.negativePhase) {
               return TextColor.lerp(1.0F - factor, this.colors[colorIndex], this.colors[colorIndex - 1]);
            }

            return TextColor.lerp(factor, this.colors[colorIndex - 1], this.colors[colorIndex]);
         }
      }

      return this.colors[0];
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("phase", this.phase), ExaminableProperty.of("colors", (Object)this.colors));
   }

   public boolean equals(final Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         TransitionTag that = (TransitionTag)other;
         return this.phase == that.phase && Arrays.equals(this.colors, that.colors);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = Objects.hash(new Object[]{this.phase});
      result = 31 * result + Arrays.hashCode(this.colors);
      return result;
   }
}
