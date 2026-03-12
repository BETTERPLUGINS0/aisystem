package ac.grim.grimac.shaded.incendo.cloud.internal;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionMapper;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProcessor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class SuggestionContext<C, S extends Suggestion> {
   private final List<S> suggestions = new ArrayList();
   private final CommandPreprocessingContext<C> preprocessingContext;
   private final SuggestionMapper<S> mapper;
   private final SuggestionProcessor<C> processor;
   private final CommandContext<C> commandContext;

   public SuggestionContext(@NonNull final SuggestionProcessor<C> processor, @NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput, @NonNull final SuggestionMapper<S> mapper) {
      this.processor = processor;
      this.commandContext = commandContext;
      this.preprocessingContext = CommandPreprocessingContext.of(this.commandContext, commandInput);
      this.mapper = mapper;
   }

   @NonNull
   public Suggestions<C, S> makeSuggestions() {
      Stream<S> stream = this.suggestions.stream();
      Stream<Suggestion> processedStream = this.processor.process(this.preprocessingContext, stream);
      List list;
      if (stream == processedStream) {
         list = Collections.unmodifiableList(this.suggestions);
      } else {
         Stream var10000 = processedStream.peek((obj) -> {
            Objects.requireNonNull(obj, "suggestion");
         });
         SuggestionMapper var10001 = this.mapper;
         Objects.requireNonNull(var10001);
         list = Collections.unmodifiableList((List)var10000.map(var10001::map).collect(Collectors.toList()));
      }

      return Suggestions.create(this.commandContext, list, this.preprocessingContext.commandInput());
   }

   @NonNull
   public CommandContext<C> commandContext() {
      return this.commandContext;
   }

   public void addSuggestions(@NonNull final Iterable<? extends Suggestion> suggestions) {
      suggestions.forEach(this::addSuggestion);
   }

   public void addSuggestion(@NonNull final Suggestion suggestion) {
      Objects.requireNonNull(suggestion, "suggestion");
      this.suggestions.add(this.mapper.map(suggestion));
   }
}
