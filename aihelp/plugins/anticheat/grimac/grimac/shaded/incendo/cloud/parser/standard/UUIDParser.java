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
import java.util.UUID;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class UUIDParser<C> implements ArgumentParser<C, UUID> {
   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, UUID> uuidParser() {
      return ParserDescriptor.of(new UUIDParser(), (Class)UUID.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, UUID> uuidComponent() {
      return CommandComponent.builder().parser(uuidParser());
   }

   @NonNull
   public ArgumentParseResult<UUID> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      String input = commandInput.readString();

      try {
         UUID uuid = UUID.fromString(input);
         return ArgumentParseResult.success(uuid);
      } catch (IllegalArgumentException var5) {
         return ArgumentParseResult.failure(new UUIDParser.UUIDParseException(input, commandContext));
      }
   }

   @API(
      status = Status.STABLE
   )
   public static final class UUIDParseException extends ParserException {
      private final String input;

      public UUIDParseException(@NonNull final String input, @NonNull final CommandContext<?> context) {
         super(UUIDParser.class, context, StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_UUID, CaptionVariable.of("input", input));
         this.input = input;
      }

      public String input() {
         return this.input;
      }

      public boolean equals(final Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            UUIDParser.UUIDParseException that = (UUIDParser.UUIDParseException)o;
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
