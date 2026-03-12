package ac.grim.grimac.shaded.incendo.cloud.paper.suggestion.tooltips;

import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent.Completion;
import com.mojang.brigadier.Message;
import io.papermc.paper.brigadier.PaperBrigadier;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

final class NativeCompletionMapper implements CompletionMapper {
   @NonNull
   public Completion map(@NonNull final TooltipSuggestion suggestion) {
      return !CraftBukkitReflection.classExists("io.papermc.paper.command.brigadier.MessageComponentSerializer") ? mapLegacy(suggestion) : Completion.completion(suggestion.suggestion(), MessageComponentSerializer.message().deserializeOrNull(suggestion.tooltip()));
   }

   @NonNull
   private static Completion mapLegacy(@NotNull final TooltipSuggestion suggestion) {
      Message tooltip = suggestion.tooltip();
      return tooltip == null ? Completion.completion(suggestion.suggestion()) : Completion.completion(suggestion.suggestion(), PaperBrigadier.componentFromMessage(tooltip));
   }
}
