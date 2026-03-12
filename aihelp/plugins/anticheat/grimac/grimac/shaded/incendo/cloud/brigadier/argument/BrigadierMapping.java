package ac.grim.grimac.shaded.incendo.cloud.brigadier.argument;

import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Objects;
import java.util.function.Function;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.common.returnsreceiver.qual.This;

@API(
   status = Status.INTERNAL,
   since = "2.0.0"
)
public final class BrigadierMapping<C, K extends ArgumentParser<C, ?>, S> {
   private static final SuggestionProvider<?> DELEGATE_TO_CLOUD = (c, b) -> {
      return b.buildFuture();
   };
   private final boolean cloudSuggestions;
   @Nullable
   private final BrigadierMappingBuilder.SuggestionProviderSupplier<K, S> suggestionsOverride;
   @Nullable
   private final Function<K, ? extends ArgumentType<?>> mapper;

   public static <T> SuggestionProvider<T> delegateSuggestions() {
      return DELEGATE_TO_CLOUD;
   }

   @NonNull
   public static <C, K extends ArgumentParser<C, ?>, S> BrigadierMappingBuilder<K, S> builder() {
      return new BrigadierMapping.BuilderImpl();
   }

   BrigadierMapping(final boolean cloudSuggestions, @Nullable final BrigadierMappingBuilder.SuggestionProviderSupplier<K, S> suggestionsOverride, @Nullable final Function<K, ? extends ArgumentType<?>> mapper) {
      this.cloudSuggestions = cloudSuggestions;
      this.suggestionsOverride = suggestionsOverride;
      this.mapper = mapper;
   }

   @Nullable
   public Function<K, ? extends ArgumentType<?>> mapper() {
      return this.mapper;
   }

   @NonNull
   public BrigadierMapping<C, K, S> withNativeSuggestions(final boolean nativeSuggestions) {
      if (nativeSuggestions && this.cloudSuggestions) {
         return new BrigadierMapping(false, this.suggestionsOverride, this.mapper);
      } else {
         return !nativeSuggestions && !this.cloudSuggestions ? new BrigadierMapping(true, this.suggestionsOverride, this.mapper) : this;
      }
   }

   @Nullable
   public SuggestionProvider<S> makeSuggestionProvider(final K commandArgument) {
      if (this.cloudSuggestions) {
         return delegateSuggestions();
      } else {
         return this.suggestionsOverride == null ? null : this.suggestionsOverride.provide(commandArgument, delegateSuggestions());
      }
   }

   private static final class BuilderImpl<C, K extends ArgumentParser<C, ?>, S> implements BrigadierMappingBuilder<K, S> {
      private Function<K, ? extends ArgumentType<?>> mapper;
      private boolean cloudSuggestions;
      private BrigadierMappingBuilder.SuggestionProviderSupplier<K, S> suggestionsOverride;

      private BuilderImpl() {
         this.cloudSuggestions = false;
      }

      @This
      @NonNull
      public BrigadierMappingBuilder<K, S> toConstant(final ArgumentType<?> constant) {
         return this.to((parser) -> {
            return constant;
         });
      }

      @This
      @NonNull
      public BrigadierMappingBuilder<K, S> to(final Function<K, ? extends ArgumentType<?>> mapper) {
         this.mapper = mapper;
         return this;
      }

      @This
      @NonNull
      public BrigadierMappingBuilder<K, S> nativeSuggestions() {
         this.cloudSuggestions = false;
         this.suggestionsOverride = null;
         return this;
      }

      @This
      @NonNull
      public BrigadierMappingBuilder<K, S> cloudSuggestions() {
         this.cloudSuggestions = true;
         this.suggestionsOverride = null;
         return this;
      }

      @This
      @NonNull
      public BrigadierMappingBuilder<K, S> suggestedByConstant(final SuggestionProvider<S> provider) {
         BrigadierMappingBuilder.super.suggestedByConstant(provider);
         this.cloudSuggestions = false;
         return this;
      }

      @This
      @NonNull
      public BrigadierMappingBuilder<K, S> suggestedBy(final BrigadierMappingBuilder.SuggestionProviderSupplier<K, S> provider) {
         this.suggestionsOverride = (BrigadierMappingBuilder.SuggestionProviderSupplier)Objects.requireNonNull(provider, "provider");
         this.cloudSuggestions = false;
         return this;
      }

      @NonNull
      public BrigadierMapping<C, K, S> build() {
         return new BrigadierMapping(this.cloudSuggestions, this.suggestionsOverride, this.mapper);
      }

      // $FF: synthetic method
      BuilderImpl(Object x0) {
         this();
      }
   }
}
