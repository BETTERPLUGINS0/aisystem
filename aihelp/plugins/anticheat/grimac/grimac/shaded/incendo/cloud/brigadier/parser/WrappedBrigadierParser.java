package ac.grim.grimac.shaded.incendo.cloud.brigadier.parser;

import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class WrappedBrigadierParser<C, T> implements ArgumentParser<C, T>, SuggestionProvider<C> {
   public static final String COMMAND_CONTEXT_BRIGADIER_NATIVE_SENDER = "_cloud_brigadier_native_sender";
   private final Supplier<ArgumentType<T>> nativeType;
   @Nullable
   private final WrappedBrigadierParser.ParseFunction<T> parse;

   public WrappedBrigadierParser(final ArgumentType<T> argumentType) {
      this(() -> {
         return argumentType;
      });
   }

   public WrappedBrigadierParser(final Supplier<ArgumentType<T>> argumentTypeSupplier) {
      this(argumentTypeSupplier, (WrappedBrigadierParser.ParseFunction)null);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   public WrappedBrigadierParser(final Supplier<ArgumentType<T>> argumentTypeSupplier, @Nullable final WrappedBrigadierParser.ParseFunction<T> parse) {
      Objects.requireNonNull(argumentTypeSupplier, "brigadierType");
      this.nativeType = argumentTypeSupplier;
      this.parse = parse;
   }

   public final ArgumentType<T> nativeArgumentType() {
      return (ArgumentType)this.nativeType.get();
   }

   @NonNull
   public final ArgumentParseResult<T> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      CloudStringReader reader = CloudStringReader.of(commandInput);

      try {
         T result = this.parse != null ? this.parse.apply((ArgumentType)this.nativeType.get(), reader) : ((ArgumentType)this.nativeType.get()).parse(reader);
         return ArgumentParseResult.success(result);
      } catch (CommandSyntaxException var5) {
         return ArgumentParseResult.failure(var5);
      }
   }

   @NonNull
   public final CompletableFuture<Iterable<Suggestion>> suggestionsFuture(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      com.mojang.brigadier.context.CommandContext<Object> reverseMappedContext = new com.mojang.brigadier.context.CommandContext(commandContext.getOrDefault("_cloud_brigadier_native_sender", commandContext.sender()), input.input(), Collections.emptyMap(), (Command)null, (CommandNode)null, Collections.emptyList(), StringRange.at(input.cursor()), (com.mojang.brigadier.context.CommandContext)null, (RedirectModifier)null, false);
      return ((ArgumentType)this.nativeType.get()).listSuggestions(reverseMappedContext, new SuggestionsBuilder(input.input(), input.cursor())).thenApply((suggestions) -> {
         List<Suggestion> cloud = new ArrayList();
         Iterator var3 = suggestions.getList().iterator();

         while(true) {
            while(var3.hasNext()) {
               com.mojang.brigadier.suggestion.Suggestion suggestion = (com.mojang.brigadier.suggestion.Suggestion)var3.next();
               String beforeSuggestion = input.input().substring(input.cursor(), suggestion.getRange().getStart());
               String afterSuggestion = input.input().substring(suggestion.getRange().getEnd());
               if (beforeSuggestion.isEmpty() && afterSuggestion.isEmpty()) {
                  cloud.add(TooltipSuggestion.suggestion(suggestion.getText(), suggestion.getTooltip()));
               } else {
                  cloud.add(TooltipSuggestion.suggestion(beforeSuggestion + suggestion.getText() + afterSuggestion, suggestion.getTooltip()));
               }
            }

            return cloud;
         }
      });
   }

   @API(
      status = Status.STABLE,
      since = "1.8.0"
   )
   @FunctionalInterface
   public interface ParseFunction<T> {
      T apply(ArgumentType<T> type, StringReader reader) throws CommandSyntaxException;
   }
}
