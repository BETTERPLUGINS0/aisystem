package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

final class SingleResolver implements TagResolver.Single, MappableResolver {
   private final String key;
   private final Tag tag;

   SingleResolver(final String key, final Tag tag) {
      this.key = key;
      this.tag = tag;
   }

   @NotNull
   public String key() {
      return this.key;
   }

   @NotNull
   public Tag tag() {
      return this.tag;
   }

   public boolean contributeToMap(@NotNull final Map<String, Tag> map) {
      map.put(this.key, this.tag);
      return true;
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.key, this.tag});
   }

   public boolean equals(final Object other) {
      if (this == other) {
         return true;
      } else if (other == null) {
         return false;
      } else if (this.getClass() != other.getClass()) {
         return false;
      } else {
         SingleResolver that = (SingleResolver)other;
         return Objects.equals(this.key, that.key) && Objects.equals(this.tag, that.tag);
      }
   }
}
