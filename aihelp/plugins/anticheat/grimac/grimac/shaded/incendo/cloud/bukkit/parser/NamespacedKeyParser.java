package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCaptionKeys;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitParserParameters;
import ac.grim.grimac.shaded.incendo.cloud.caption.Caption;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionVariable;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.ParserException;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.BlockingSuggestionProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class NamespacedKeyParser<C> implements ArgumentParser<C, NamespacedKey>, BlockingSuggestionProvider.Strings<C> {
   private final boolean requireExplicitNamespace;
   private final String defaultNamespace;

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, NamespacedKey> namespacedKeyParser() {
      return namespacedKeyParser(false);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, NamespacedKey> namespacedKeyParser(final boolean requireExplicitNamespace) {
      return namespacedKeyParser(requireExplicitNamespace, "minecraft");
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, NamespacedKey> namespacedKeyParser(final boolean requireExplicitNamespace, @NonNull final String defaultNamespace) {
      return ParserDescriptor.of(new NamespacedKeyParser(requireExplicitNamespace, defaultNamespace), (Class)NamespacedKey.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, NamespacedKey> namespacedKeyComponent() {
      return CommandComponent.builder().parser(namespacedKeyParser());
   }

   public NamespacedKeyParser(final boolean requireExplicitNamespace, final String defaultNamespace) {
      this.requireExplicitNamespace = requireExplicitNamespace;
      this.defaultNamespace = defaultNamespace;
   }

   @NonNull
   public ArgumentParseResult<NamespacedKey> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      String input = commandInput.peekString();
      String[] split = input.split(":");
      int maxSemi = split.length > 1 ? 1 : 0;
      if (input.length() - input.replace(":", "").length() > maxSemi) {
         return ArgumentParseResult.failure(new NamespacedKeyParser.NamespacedKeyParseException(BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_NAMESPACED_KEY_KEY, input, commandContext));
      } else {
         try {
            NamespacedKey ret;
            if (split.length == 1) {
               if (this.requireExplicitNamespace) {
                  return ArgumentParseResult.failure(new NamespacedKeyParser.NamespacedKeyParseException(BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_NAMESPACED_KEY_NEED_NAMESPACE, input, commandContext));
               }

               ret = new NamespacedKey(this.defaultNamespace, commandInput.readString());
            } else {
               if (split.length != 2) {
                  return ArgumentParseResult.failure(new NamespacedKeyParser.NamespacedKeyParseException(BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_NAMESPACED_KEY_KEY, input, commandContext));
               }

               ret = new NamespacedKey(commandInput.readUntilAndSkip(':'), commandInput.readString());
            }

            return ArgumentParseResult.success(ret);
         } catch (IllegalArgumentException var8) {
            Caption caption = var8.getMessage().contains("namespace") ? BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_NAMESPACED_KEY_NAMESPACE : BukkitCaptionKeys.ARGUMENT_PARSE_FAILURE_NAMESPACED_KEY_KEY;
            return ArgumentParseResult.failure(new NamespacedKeyParser.NamespacedKeyParseException(caption, input, commandContext));
         }
      }
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      List<String> ret = new ArrayList();
      ret.add(this.defaultNamespace + ":");
      String token = input.peekString();
      if (!token.contains(":") && !token.isEmpty()) {
         ret.add(token + ":");
      }

      return ret;
   }

   private static <C> void registerParserSupplier(@NonNull final CommandManager<C> commandManager) {
      commandManager.parserRegistry().registerParserSupplier(TypeToken.get(NamespacedKey.class), (params) -> {
         return new NamespacedKeyParser(params.has(BukkitParserParameters.REQUIRE_EXPLICIT_NAMESPACE), (String)params.get(BukkitParserParameters.DEFAULT_NAMESPACE, "minecraft"));
      });
   }

   public static final class NamespacedKeyParseException extends ParserException {
      private final String input;

      public NamespacedKeyParseException(@NonNull final Caption caption, @NonNull final String input, @NonNull final CommandContext<?> context) {
         super(NamespacedKeyParser.class, context, caption, CaptionVariable.of("input", input));
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
            NamespacedKeyParser.NamespacedKeyParseException that = (NamespacedKeyParser.NamespacedKeyParseException)o;
            return this.input.equals(that.input) && this.errorCaption().equals(that.errorCaption());
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.input, this.errorCaption()});
      }
   }
}
