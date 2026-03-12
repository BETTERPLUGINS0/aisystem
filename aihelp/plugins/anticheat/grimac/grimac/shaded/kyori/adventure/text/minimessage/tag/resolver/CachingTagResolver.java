package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final class CachingTagResolver implements TagResolver.WithoutArguments, MappableResolver, SerializableResolver {
   private static final Tag NULL_REPLACEMENT = () -> {
      throw new UnsupportedOperationException("no-op null tag");
   };
   private final Map<String, Tag> cache = new HashMap();
   private final TagResolver.WithoutArguments resolver;

   CachingTagResolver(final TagResolver.WithoutArguments resolver) {
      this.resolver = resolver;
   }

   private Tag query(@NotNull final String key) {
      return (Tag)this.cache.computeIfAbsent(key, (k) -> {
         Tag result = this.resolver.resolve(k);
         return result == null ? NULL_REPLACEMENT : result;
      });
   }

   @Nullable
   public Tag resolve(@NotNull final String name) {
      Tag potentialValue = this.query(name);
      return potentialValue == NULL_REPLACEMENT ? null : potentialValue;
   }

   public boolean has(@NotNull final String name) {
      return this.query(name) != NULL_REPLACEMENT;
   }

   public boolean contributeToMap(@NotNull final Map<String, Tag> map) {
      return this.resolver instanceof MappableResolver ? ((MappableResolver)this.resolver).contributeToMap(map) : false;
   }

   public void handle(@NotNull final Component serializable, @NotNull final ClaimConsumer consumer) {
      if (this.resolver instanceof SerializableResolver) {
         ((SerializableResolver)this.resolver).handle(serializable, consumer);
      }

   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof CachingTagResolver)) {
         return false;
      } else {
         CachingTagResolver that = (CachingTagResolver)other;
         return Objects.equals(this.resolver, that.resolver);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.resolver});
   }
}
