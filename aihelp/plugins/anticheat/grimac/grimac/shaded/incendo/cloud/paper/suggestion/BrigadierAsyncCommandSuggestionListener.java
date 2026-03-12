package ac.grim.grimac.shaded.incendo.cloud.paper.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import ac.grim.grimac.shaded.incendo.cloud.paper.LegacyPaperCommandManager;
import ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.tooltips.CompletionMapper;
import ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.tooltips.CompletionMapperFactory;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionFactory;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestions;
import ac.grim.grimac.shaded.incendo.cloud.util.StringUtils;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.event.EventHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

class BrigadierAsyncCommandSuggestionListener<C> extends AsyncCommandSuggestionListener<C> {
   private final CompletionMapperFactory completionMapperFactory = CompletionMapperFactory.detectingRelocation();
   private final SuggestionFactory<C, ? extends TooltipSuggestion> suggestionFactory;

   BrigadierAsyncCommandSuggestionListener(@NonNull final LegacyPaperCommandManager<C> paperCommandManager) {
      super(paperCommandManager);
      this.suggestionFactory = paperCommandManager.suggestionFactory().mapped(TooltipSuggestion::tooltipSuggestion);
   }

   @EventHandler
   void onTabCompletion(@NonNull final AsyncTabCompleteEvent event) {
      super.onTabCompletion(event);
   }

   protected Suggestions<C, ? extends TooltipSuggestion> querySuggestions(@NonNull final C commandSender, @NonNull final String input) {
      return this.suggestionFactory.suggestImmediately(commandSender, input);
   }

   protected void setSuggestions(@NonNull final AsyncTabCompleteEvent event, @NonNull final C commandSender, @NonNull final String input) {
      CompletionMapper completionMapper = this.completionMapperFactory.createMapper();
      Suggestions<C, ? extends TooltipSuggestion> suggestions = this.querySuggestions(commandSender, input);
      Stream var10001 = suggestions.list().stream().map((suggestion) -> {
         String trim = StringUtils.trimBeforeLastSpace(suggestion.suggestion(), suggestions.commandInput());
         return trim == null ? null : suggestion.withSuggestion(trim);
      }).filter(Objects::nonNull);
      Objects.requireNonNull(completionMapper);
      event.completions((List)var10001.map(completionMapper::map).collect(Collectors.toList()));
   }
}
