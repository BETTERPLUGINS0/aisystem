package libs.com.ryderbelserion.vital.common.api.commands;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import libs.com.ryderbelserion.vital.common.api.commands.context.CommandInfo;
import org.jetbrains.annotations.NotNull;

public abstract class Command<S, I extends CommandInfo<S>> {
   public abstract void execute(I var1);

   @NotNull
   public abstract String getPermission();

   @NotNull
   public abstract LiteralCommandNode<S> literal();

   @NotNull
   public abstract Command<S, I> registerPermission();

   @NotNull
   public final CompletableFuture<Suggestions> suggestNames(SuggestionsBuilder builder, int min, int max) {
      for(int count = min; count <= max; ++count) {
         builder.suggest(UUID.randomUUID().toString().replace("-", "").substring(0, 8));
      }

      return builder.buildFuture();
   }

   @NotNull
   public final CompletableFuture<Suggestions> suggestNames(SuggestionsBuilder builder) {
      return this.suggestNames(builder, 1, 8);
   }

   @NotNull
   public final CompletableFuture<Suggestions> suggestIntegers(SuggestionsBuilder builder, int min, int max) {
      for(int count = min; count <= max; ++count) {
         builder.suggest(count);
      }

      return builder.buildFuture();
   }

   @NotNull
   public final CompletableFuture<Suggestions> suggestIntegers(SuggestionsBuilder builder) {
      return this.suggestIntegers(builder, 1, 60);
   }

   @NotNull
   public final CompletableFuture<Suggestions> suggestDoubles(SuggestionsBuilder builder, int min, int max) {
      for(int count = min; count <= max; ++count) {
         double x = (double)count / 10.0D;
         builder.suggest(String.valueOf(x));
      }

      return builder.buildFuture();
   }

   @NotNull
   public final CompletableFuture<Suggestions> suggestDoubles(SuggestionsBuilder builder) {
      return this.suggestDoubles(builder, 0, 1000);
   }
}
