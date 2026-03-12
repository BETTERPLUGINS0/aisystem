package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class ListTagBuilder<T extends BinaryTag> implements ListBinaryTag.Builder<T> {
   private static final int DEFAULT_CAPACITY = -1;
   @Nullable
   private List<BinaryTag> tags;
   private final boolean permitsHeterogeneity;
   private BinaryTagType<? extends BinaryTag> elementType;
   private final int initialCapacity;

   ListTagBuilder(final boolean permitsHeterogeneity) {
      this(permitsHeterogeneity, BinaryTagTypes.END);
   }

   ListTagBuilder(final boolean permitsHeterogeneity, final int initialCapacity) {
      this(permitsHeterogeneity, BinaryTagTypes.END, initialCapacity);
   }

   ListTagBuilder(final boolean permitsHeterogeneity, final BinaryTagType<? extends BinaryTag> type) {
      this(permitsHeterogeneity, type, -1);
   }

   ListTagBuilder(final boolean permitsHeterogeneity, final BinaryTagType<? extends BinaryTag> type, final int initialCapacity) {
      this.permitsHeterogeneity = permitsHeterogeneity;
      this.elementType = type;
      this.initialCapacity = initialCapacity;
   }

   @NotNull
   public ListBinaryTag.Builder<T> add(final BinaryTag tag) {
      this.elementType = ListBinaryTagImpl.validateTagType(tag, this.elementType, this.permitsHeterogeneity);
      if (this.tags == null) {
         if (this.initialCapacity != -1) {
            if (this.initialCapacity < 0) {
               throw new IllegalArgumentException("initialCapacity cannot be less than 0, was " + this.initialCapacity);
            }

            this.tags = new ArrayList(this.initialCapacity);
         } else {
            this.tags = new ArrayList();
         }
      }

      this.tags.add(tag);
      return this;
   }

   @NotNull
   public ListBinaryTag.Builder<T> add(final Iterable<? extends T> tagsToAdd) {
      Iterator var2 = tagsToAdd.iterator();

      while(var2.hasNext()) {
         T tag = (BinaryTag)var2.next();
         this.add(tag);
      }

      return this;
   }

   @NotNull
   public ListBinaryTag build() {
      return (ListBinaryTag)(this.tags == null ? ListBinaryTag.empty() : new ListBinaryTagImpl(this.elementType, this.permitsHeterogeneity, new ArrayList(this.tags)));
   }
}
