package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

final class CompoundTagBuilder implements CompoundBinaryTag.Builder {
   private static final int DEFAULT_CAPACITY = -1;
   @Nullable
   private Map<String, BinaryTag> tags;
   private final int initialCapacity;

   CompoundTagBuilder() {
      this(-1);
   }

   CompoundTagBuilder(final int initialCapacity) {
      this.initialCapacity = initialCapacity;
   }

   private Map<String, BinaryTag> tags() {
      if (this.tags == null) {
         if (this.initialCapacity != -1) {
            if (this.initialCapacity < 0) {
               throw new IllegalArgumentException("initialCapacity cannot be less than 0, was " + this.initialCapacity);
            }

            this.tags = new HashMap(this.initialCapacity);
         } else {
            this.tags = new HashMap();
         }
      }

      return this.tags;
   }

   @NotNull
   public CompoundBinaryTag.Builder put(@NotNull final String key, @NotNull final BinaryTag tag) {
      this.tags().put(key, tag);
      return this;
   }

   @NotNull
   public CompoundBinaryTag.Builder put(@NotNull final CompoundBinaryTag tag) {
      Map<String, BinaryTag> tags = this.tags();
      Iterator var3 = tag.keySet().iterator();

      while(var3.hasNext()) {
         String key = (String)var3.next();
         tags.put(key, tag.get(key));
      }

      return this;
   }

   @NotNull
   public CompoundBinaryTag.Builder put(@NotNull final Map<String, ? extends BinaryTag> tags) {
      this.tags().putAll(tags);
      return this;
   }

   @NotNull
   public CompoundBinaryTag.Builder remove(@NotNull final String key, @Nullable final Consumer<? super BinaryTag> removed) {
      if (this.tags != null) {
         BinaryTag tag = (BinaryTag)this.tags.remove(key);
         if (removed != null) {
            removed.accept(tag);
         }
      }

      return this;
   }

   @NotNull
   public CompoundBinaryTag build() {
      return (CompoundBinaryTag)(this.tags == null ? CompoundBinaryTag.empty() : new CompoundBinaryTagImpl(new HashMap(this.tags)));
   }
}
