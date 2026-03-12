package ac.grim.grimac.platform.api.command;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractPlayerSelectorParser<C> {
   public abstract ParserDescriptor<C, PlayerSelector> descriptor();

   protected abstract ParserDescriptor<C, ?> getPlatformSpecificDescriptor();

   protected abstract CompletableFuture<PlayerSelector> adaptToCommonSelector(CommandContext<C> var1, Object var2);

   protected ParserDescriptor<C, PlayerSelector> createDescriptor() {
      return ParserDescriptor.of(this.getPlatformSpecificDescriptor().parser().mapSuccess(this::adaptToCommonSelector), (Class)PlayerSelector.class);
   }
}
