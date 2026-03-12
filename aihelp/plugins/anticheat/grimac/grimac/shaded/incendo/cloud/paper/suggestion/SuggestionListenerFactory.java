package ac.grim.grimac.shaded.incendo.cloud.paper.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.incendo.cloud.paper.LegacyPaperCommandManager;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   since = "2.0.0"
)
public interface SuggestionListenerFactory<C> {
   @NonNull
   static <C> SuggestionListenerFactory<C> create(@NonNull final LegacyPaperCommandManager<C> commandManager) {
      return new SuggestionListenerFactory.SuggestionListenerFactoryImpl(commandManager);
   }

   @NonNull
   SuggestionListener<C> createListener();

   public static final class SuggestionListenerFactoryImpl<C> implements SuggestionListenerFactory<C> {
      private final LegacyPaperCommandManager<C> commandManager;

      private SuggestionListenerFactoryImpl(@NonNull final LegacyPaperCommandManager<C> commandManager) {
         this.commandManager = commandManager;
      }

      public SuggestionListener<C> createListener() {
         Class<?> completionCls = CraftBukkitReflection.findClass("com.destroystokyo.paper.event.server.AsyncTabCompleteEvent$Completion");
         return (SuggestionListener)(completionCls != null ? new BrigadierAsyncCommandSuggestionListener(this.commandManager) : new AsyncCommandSuggestionListener(this.commandManager));
      }

      // $FF: synthetic method
      SuggestionListenerFactoryImpl(LegacyPaperCommandManager x0, Object x1) {
         this(x0);
      }
   }
}
