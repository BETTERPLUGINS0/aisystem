package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import java.util.Map;
import java.util.Objects;

final class MapTagResolver implements TagResolver.WithoutArguments, MappableResolver {
   private final Map<String, ? extends Tag> tagMap;

   MapTagResolver(@NotNull final Map<String, ? extends Tag> placeholderMap) {
      this.tagMap = placeholderMap;
   }

   @Nullable
   public Tag resolve(@NotNull final String name) {
      return (Tag)this.tagMap.get(name);
   }

   public boolean has(@NotNull final String name) {
      return this.tagMap.containsKey(name);
   }

   public boolean contributeToMap(@NotNull final Map<String, Tag> map) {
      map.putAll(this.tagMap);
      return true;
   }

   public boolean equals(@Nullable final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof MapTagResolver)) {
         return false;
      } else {
         MapTagResolver that = (MapTagResolver)other;
         return Objects.equals(this.tagMap, that.tagMap);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.tagMap});
   }
}
