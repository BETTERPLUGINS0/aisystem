package ac.grim.grimac.shaded.incendo.cloud.parser.standard;

import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionVariable;
import ac.grim.grimac.shaded.incendo.cloud.caption.StandardCaptionKeys;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.ParserException;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.BlockingSuggestionProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class EnumParser<C, E extends Enum<E>> implements ArgumentParser<C, E>, BlockingSuggestionProvider.Strings<C> {
   private final Class<E> enumClass;
   private final EnumSet<E> acceptedValues;

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C, E extends Enum<E>> ParserDescriptor<C, E> enumParser(@NonNull final Class<E> enumClass) {
      return ParserDescriptor.of(new EnumParser(enumClass), (Class)enumClass);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C, E extends Enum<E>> CommandComponent.Builder<C, E> enumComponent(@NonNull final Class<E> enumClass) {
      return CommandComponent.builder().parser(enumParser(enumClass));
   }

   public EnumParser(@NonNull final Class<E> enumClass) {
      this.enumClass = enumClass;
      this.acceptedValues = EnumSet.allOf(enumClass);
   }

   @NonNull
   public Class<E> enumClass() {
      return this.enumClass;
   }

   @NonNull
   public Collection<E> acceptedValues() {
      return Collections.unmodifiableSet(this.acceptedValues);
   }

   @NonNull
   public ArgumentParseResult<E> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      String input = commandInput.readString();
      Iterator var4 = this.acceptedValues.iterator();

      Enum value;
      do {
         if (!var4.hasNext()) {
            return ArgumentParseResult.failure(new EnumParser.EnumParseException(input, this.enumClass, commandContext));
         }

         value = (Enum)var4.next();
      } while(!value.name().equalsIgnoreCase(input));

      return ArgumentParseResult.success(value);
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      return (Iterable)EnumSet.allOf(this.enumClass).stream().map((e) -> {
         return e.name().toLowerCase(Locale.ROOT);
      }).collect(Collectors.toList());
   }

   @API(
      status = Status.STABLE
   )
   public static final class EnumParseException extends ParserException {
      private final String input;
      private final Class<? extends Enum<?>> enumClass;

      public EnumParseException(@NonNull final String input, @NonNull final Class<? extends Enum<?>> enumClass, @NonNull final CommandContext<?> context) {
         super(EnumParser.class, context, StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_ENUM, CaptionVariable.of("input", input), CaptionVariable.of("acceptableValues", join(enumClass)));
         this.input = input;
         this.enumClass = enumClass;
      }

      @NonNull
      private static String join(@NonNull final Class<? extends Enum> clazz) {
         EnumSet<?> enumSet = EnumSet.allOf(clazz);
         return (String)enumSet.stream().map((e) -> {
            return e.toString().toLowerCase(Locale.ROOT);
         }).collect(Collectors.joining(", "));
      }

      @NonNull
      public String input() {
         return this.input;
      }

      @NonNull
      public Class<? extends Enum<?>> enumClass() {
         return this.enumClass;
      }

      public boolean equals(final Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            EnumParser.EnumParseException that = (EnumParser.EnumParseException)o;
            return this.input.equals(that.input) && this.enumClass.equals(that.enumClass);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.input, this.enumClass});
      }
   }
}
