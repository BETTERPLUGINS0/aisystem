package ac.grim.grimac.shaded.incendo.cloud.paper.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitPluginRegistrationHandler;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.PluginHolder;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.BukkitHelper;
import ac.grim.grimac.shaded.incendo.cloud.paper.LegacyPaperCommandManager;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestions;
import ac.grim.grimac.shaded.incendo.cloud.util.StringUtils;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.event.EventHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

class AsyncCommandSuggestionListener<C> implements SuggestionListener<C> {
   private final LegacyPaperCommandManager<C> paperCommandManager;

   AsyncCommandSuggestionListener(@NonNull final LegacyPaperCommandManager<C> paperCommandManager) {
      this.paperCommandManager = paperCommandManager;
   }

   @EventHandler
   void onTabCompletion(@NonNull final AsyncTabCompleteEvent event) {
      String strippedBuffer = event.getBuffer().startsWith("/") ? event.getBuffer().substring(1) : event.getBuffer();
      if (!strippedBuffer.trim().isEmpty()) {
         BukkitPluginRegistrationHandler<C> bukkitPluginRegistrationHandler = (BukkitPluginRegistrationHandler)this.paperCommandManager.commandRegistrationHandler();
         String commandLabel = strippedBuffer.split(" ")[0];
         if (bukkitPluginRegistrationHandler.isRecognized(commandLabel)) {
            String input = event.getBuffer();
            if (input.charAt(0) == '/') {
               input = input.substring(1);
            }

            this.setSuggestions(event, this.paperCommandManager.senderMapper().map(event.getSender()), BukkitHelper.stripNamespace((PluginHolder)this.paperCommandManager, input));
            event.setHandled(true);
         }
      }
   }

   protected Suggestions<C, ?> querySuggestions(@NonNull final C commandSender, @NonNull final String input) {
      return this.paperCommandManager.suggestionFactory().suggestImmediately(commandSender, input);
   }

   protected void setSuggestions(@NonNull final AsyncTabCompleteEvent event, @NonNull final C commandSender, @NonNull final String input) {
      Suggestions<C, ?> suggestions = this.querySuggestions(commandSender, input);
      event.setCompletions((List)suggestions.list().stream().map(Suggestion::suggestion).map((suggestion) -> {
         return StringUtils.trimBeforeLastSpace(suggestion, suggestions.commandInput());
      }).filter(Objects::nonNull).collect(Collectors.toList()));
   }
}
