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
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class CharacterParser<C> implements ArgumentParser<C, Character> {
   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, Character> characterParser() {
      return ParserDescriptor.of(new CharacterParser(), (Class)Character.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, Character> characterComponent() {
      return CommandComponent.builder().parser(characterParser());
   }

   @NonNull
   public ArgumentParseResult<Character> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return commandInput.peekString().length() != 1 ? ArgumentParseResult.failure(new CharacterParser.CharParseException(commandInput.peekString(), commandContext)) : ArgumentParseResult.success(commandInput.read());
   }

   @API(
      status = Status.STABLE
   )
   public static final class CharParseException extends ParserException {
      private final String input;

      public CharParseException(@NonNull final String input, @NonNull final CommandContext<?> context) {
         super(CharacterParser.class, context, StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_CHAR, CaptionVariable.of("input", input));
         this.input = input;
      }

      @NonNull
      public String input() {
         return this.input;
      }

      public boolean equals(final Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            CharacterParser.CharParseException that = (CharacterParser.CharParseException)o;
            return this.input.equals(that.input);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.input});
      }
   }
}
