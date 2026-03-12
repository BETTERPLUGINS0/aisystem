package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.Debug;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.Range;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Debug.Renderer(
   text = "\"ListBinaryTag[type=\" + this.type.toString() + \"]\"",
   childrenArray = "this.tags.toArray()",
   hasChildren = "!this.tags.isEmpty()"
)
final class ListBinaryTagImpl extends AbstractBinaryTag implements ListBinaryTag {
   static final ListBinaryTag EMPTY;
   private final List<BinaryTag> tags;
   private final boolean permitsHeterogeneity;
   private final BinaryTagType<? extends BinaryTag> elementType;
   private final int hashCode;

   ListBinaryTagImpl(final BinaryTagType<? extends BinaryTag> elementType, final boolean permitsHeterogeneity, final List<BinaryTag> tags) {
      this.tags = Collections.unmodifiableList(tags);
      this.permitsHeterogeneity = permitsHeterogeneity;
      this.elementType = elementType;
      this.hashCode = tags.hashCode();
   }

   @NotNull
   public BinaryTagType<? extends BinaryTag> elementType() {
      return this.elementType;
   }

   public int size() {
      return this.tags.size();
   }

   public boolean isEmpty() {
      return this.tags.isEmpty();
   }

   @NotNull
   public BinaryTag get(@Range(from = 0L,to = 2147483647L) final int index) {
      return (BinaryTag)this.tags.get(index);
   }

   @NotNull
   public ListBinaryTag set(final int index, @NotNull final BinaryTag newTag, @Nullable final Consumer<? super BinaryTag> removed) {
      BinaryTagType<?> targetType = validateTagType(newTag, this.elementType, this.permitsHeterogeneity);
      return this.edit((tags) -> {
         BinaryTag oldTag = (BinaryTag)tags.set(index, newTag);
         if (removed != null) {
            removed.accept(oldTag);
         }

      }, targetType);
   }

   @NotNull
   public ListBinaryTag remove(final int index, @Nullable final Consumer<? super BinaryTag> removed) {
      return this.edit((tags) -> {
         BinaryTag oldTag = (BinaryTag)tags.remove(index);
         if (removed != null) {
            removed.accept(oldTag);
         }

      }, (BinaryTagType)null);
   }

   @NotNull
   public ListBinaryTag add(final BinaryTag tag) {
      BinaryTagType<?> targetType = validateTagType(tag, this.elementType, this.permitsHeterogeneity);
      return this.edit((tags) -> {
         tags.add(tag);
      }, targetType);
   }

   @NotNull
   public ListBinaryTag add(final Iterable<? extends BinaryTag> tagsToAdd) {
      if (tagsToAdd instanceof Collection && ((Collection)tagsToAdd).isEmpty()) {
         return this;
      } else {
         BinaryTagType<?> type = validateTagType(tagsToAdd, this.permitsHeterogeneity);
         return this.edit((tags) -> {
            Iterator var2 = tagsToAdd.iterator();

            while(var2.hasNext()) {
               BinaryTag tag = (BinaryTag)var2.next();
               tags.add(tag);
            }

         }, type);
      }
   }

   static void noAddEnd(final BinaryTag tag) {
      if (tag.type() == BinaryTagTypes.END) {
         throw new IllegalArgumentException(String.format("Cannot add a %s to a %s", BinaryTagTypes.END, BinaryTagTypes.LIST));
      }
   }

   static BinaryTagType<?> validateTagType(final Iterable<? extends BinaryTag> tags, final boolean permitHeterogeneity) {
      BinaryTagType<?> type = null;
      Iterator var3 = tags.iterator();

      while(var3.hasNext()) {
         BinaryTag tag = (BinaryTag)var3.next();
         if (type == null) {
            noAddEnd(tag);
            type = tag.type();
         } else {
            validateTagType(tag, type, permitHeterogeneity);
            if (type != tag.type()) {
               type = BinaryTagTypes.LIST_WILDCARD;
            }
         }
      }

      return type;
   }

   static BinaryTagType<?> validateTagType(final BinaryTag tag, final BinaryTagType<? extends BinaryTag> type, final boolean permitHeterogenity) {
      noAddEnd(tag);
      if (type == BinaryTagTypes.END) {
         return tag.type();
      } else if (tag.type() != type && !permitHeterogenity) {
         throw new IllegalArgumentException(String.format("Trying to add tag of type %s to list of %s", tag.type(), type));
      } else {
         return tag.type() != type ? BinaryTagTypes.LIST_WILDCARD : type;
      }
   }

   private ListBinaryTag edit(final Consumer<List<BinaryTag>> consumer, @Nullable final BinaryTagType<? extends BinaryTag> maybeElementType) {
      List<BinaryTag> tags = new ArrayList(this.tags);
      consumer.accept(tags);
      BinaryTagType<? extends BinaryTag> elementType = this.elementType;
      if (maybeElementType != null) {
         elementType = maybeElementType;
      }

      return new ListBinaryTagImpl(elementType, this.permitsHeterogeneity, new ArrayList(tags));
   }

   @NotNull
   public Stream<BinaryTag> stream() {
      return this.tags.stream();
   }

   @NotNull
   public ListBinaryTag unwrapHeterogeneity() {
      if (this.permitsHeterogeneity) {
         return this;
      } else if (this.elementType != BinaryTagTypes.COMPOUND) {
         return new ListBinaryTagImpl(this.elementType, true, this.tags);
      } else {
         List<BinaryTag> newTags = null;
         ListIterator it = this.tags.listIterator();

         while(it.hasNext()) {
            BinaryTag current = (BinaryTag)it.next();
            BinaryTag unboxed = ListBinaryTag0.unbox((CompoundBinaryTag)current);
            if (unboxed != current && newTags == null) {
               newTags = new ArrayList(this.tags.size());
               int idx = it.nextIndex() - 1;

               for(int ptr = 0; ptr < idx; ++ptr) {
                  newTags.add((BinaryTag)this.tags.get(ptr));
               }
            }

            if (newTags != null) {
               newTags.add(unboxed);
            }
         }

         return new ListBinaryTagImpl(newTags == null ? BinaryTagTypes.COMPOUND : BinaryTagTypes.LIST_WILDCARD, true, (List)(newTags == null ? this.tags : newTags));
      }
   }

   @NotNull
   public ListBinaryTag wrapHeterogeneity() {
      if (this.elementType != BinaryTagTypes.LIST_WILDCARD) {
         return this;
      } else {
         List<BinaryTag> newTags = new ArrayList(this.tags.size());
         Iterator var2 = this.tags.iterator();

         while(var2.hasNext()) {
            BinaryTag tag = (BinaryTag)var2.next();
            newTags.add(ListBinaryTag0.box(tag));
         }

         return new ListBinaryTagImpl(BinaryTagTypes.COMPOUND, false, newTags);
      }
   }

   @NotNull
   public Iterator<BinaryTag> iterator() {
      final Iterator<BinaryTag> iterator = this.tags.iterator();
      return new Iterator<BinaryTag>() {
         public boolean hasNext() {
            return iterator.hasNext();
         }

         public BinaryTag next() {
            return (BinaryTag)iterator.next();
         }

         public void forEachRemaining(final Consumer<? super BinaryTag> action) {
            iterator.forEachRemaining(action);
         }
      };
   }

   public void forEach(final Consumer<? super BinaryTag> action) {
      this.tags.forEach(action);
   }

   public Spliterator<BinaryTag> spliterator() {
      return Spliterators.spliterator(this.tags, 1040);
   }

   public boolean equals(final Object that) {
      return this == that || that instanceof ListBinaryTagImpl && this.tags.equals(((ListBinaryTagImpl)that).tags);
   }

   public int hashCode() {
      return this.hashCode;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("tags", (Object)this.tags), ExaminableProperty.of("type", (Object)this.elementType));
   }

   static {
      EMPTY = new ListBinaryTagImpl(BinaryTagTypes.END, false, Collections.emptyList());
   }
}
