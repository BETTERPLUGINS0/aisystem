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
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class BooleanParser<C> implements ArgumentParser<C, Boolean>, BlockingSuggestionProvider.Strings<C> {
   private static final List<String> STRICT_LOWER;
   private static final List<String> LIBERAL_LOWER;
   private final boolean liberal;

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Boolean> booleanParser() {
      return booleanParser(false);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Boolean> booleanParser(final boolean liberal) {
      return ParserDescriptor.of(new BooleanParser(liberal), (Class)Boolean.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, Boolean> booleanComponent() {
      return CommandComponent.builder().parser(booleanParser());
   }

   public BooleanParser(final boolean liberal) {
      this.liberal = liberal;
   }

   @NonNull
   public ArgumentParseResult<Boolean> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return !commandInput.isValidBoolean(this.liberal) ? ArgumentParseResult.failure(new BooleanParser.BooleanParseException(commandInput.peekString(), this.liberal, commandContext)) : ArgumentParseResult.success(commandInput.readBoolean());
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      return !this.liberal ? STRICT_LOWER : LIBERAL_LOWER;
   }

   static {
      STRICT_LOWER = (List)CommandInput.BOOLEAN_STRICT.stream().map((s) -> {
         return s.toLowerCase(Locale.ROOT);
      }).collect(Collectors.toList());
      LIBERAL_LOWER = (List)CommandInput.BOOLEAN_LIBERAL.stream().map((s) -> {
         return s.toLowerCase(Locale.ROOT);
      }).collect(Collectors.toList());
   }

   @API(
      status = Status.STABLE
   )
   public static final class BooleanParseException extends ParserException {
      private final String input;
      private final boolean liberal;

      public BooleanParseException(@NonNull final String input, final boolean liberal, @NonNull final CommandContext<?> context) {
         super(BooleanParser.class, context, StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_BOOLEAN, CaptionVariable.of("input", input));
         this.input = input;
         this.liberal = liberal;
      }

      @NonNull
      public String input() {
         return this.input;
      }

      public boolean liberal() {
         return this.liberal;
      }

      public boolean equals(final Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            BooleanParser.BooleanParseException that = (BooleanParser.BooleanParseException)o;
            return this.liberal == that.liberal && this.input.equals(that.input);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.input, this.liberal});
      }
   }
}
