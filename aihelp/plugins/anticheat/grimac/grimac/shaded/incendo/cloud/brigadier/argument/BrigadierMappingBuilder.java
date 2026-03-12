package ac.grim.grimac.shaded.incendo.cloud.brigadier.argument;

import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Objects;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.returnsreceiver.qual.This;

public interface BrigadierMappingBuilder<K extends ArgumentParser<?, ?>, S> {
   @This
   @NonNull
   BrigadierMappingBuilder<K, S> toConstant(ArgumentType<?> constant);

   @This
   @NonNull
   BrigadierMappingBuilder<K, S> to(Function<K, ? extends ArgumentType<?>> mapper);

   @This
   @NonNull
   BrigadierMappingBuilder<K, S> nativeSuggestions();

   @This
   @NonNull
   BrigadierMappingBuilder<K, S> cloudSuggestions();

   @This
   @NonNull
   default BrigadierMappingBuilder<K, S> suggestedByConstant(final SuggestionProvider<S> provider) {
      Objects.requireNonNull(provider, "provider");
      return this.suggestedBy((argument, useCloud) -> {
         return provider;
      });
   }

   @This
   @NonNull
   BrigadierMappingBuilder<K, S> suggestedBy(BrigadierMappingBuilder.SuggestionProviderSupplier<K, S> provider);

   @NonNull
   BrigadierMapping<?, K, S> build();

   @FunctionalInterface
   public interface SuggestionProviderSupplier<K extends ArgumentParser<?, ?>, S> {
      @Nullable
      SuggestionProvider<? super S> provide(@NonNull K argument, SuggestionProvider<S> useCloud);
   }
}
