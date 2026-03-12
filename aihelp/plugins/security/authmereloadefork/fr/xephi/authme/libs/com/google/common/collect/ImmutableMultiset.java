package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableMultiset<E> extends ImmutableMultisetGwtSerializationDependencies<E> implements Multiset<E> {
   @LazyInit
   @CheckForNull
   private transient ImmutableList<E> asList;
   @LazyInit
   @CheckForNull
   private transient ImmutableSet<Multiset.Entry<E>> entrySet;

   public static <E> Collector<E, ?, ImmutableMultiset<E>> toImmutableMultiset() {
      return CollectCollectors.toImmutableMultiset(Function.identity(), (e) -> {
         return 1;
      });
   }

   public static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
      return CollectCollectors.toImmutableMultiset(elementFunction, countFunction);
   }

   public static <E> ImmutableMultiset<E> of() {
      return RegularImmutableMultiset.EMPTY;
   }

   public static <E> ImmutableMultiset<E> of(E element) {
      return copyFromElements(element);
   }

   public static <E> ImmutableMultiset<E> of(E e1, E e2) {
      return copyFromElements(e1, e2);
   }

   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3) {
      return copyFromElements(e1, e2, e3);
   }

   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4) {
      return copyFromElements(e1, e2, e3, e4);
   }

   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
      return copyFromElements(e1, e2, e3, e4, e5);
   }

   public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
      return (new ImmutableMultiset.Builder()).add(e1).add(e2).add(e3).add(e4).add(e5).add(e6).add(others).build();
   }

   public static <E> ImmutableMultiset<E> copyOf(E[] elements) {
      return copyFromElements(elements);
   }

   public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> elements) {
      if (elements instanceof ImmutableMultiset) {
         ImmutableMultiset<E> result = (ImmutableMultiset)elements;
         if (!result.isPartialView()) {
            return result;
         }
      }

      Multiset<? extends E> multiset = elements instanceof Multiset ? Multisets.cast(elements) : LinkedHashMultiset.create(elements);
      return copyFromEntries(((Multiset)multiset).entrySet());
   }

   public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> elements) {
      Multiset<E> multiset = LinkedHashMultiset.create();
      Iterators.addAll(multiset, elements);
      return copyFromEntries(multiset.entrySet());
   }

   private static <E> ImmutableMultiset<E> copyFromElements(E... elements) {
      Multiset<E> multiset = LinkedHashMultiset.create();
      Collections.addAll(multiset, elements);
      return copyFromEntries(multiset.entrySet());
   }

   static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Multiset.Entry<? extends E>> entries) {
      return entries.isEmpty() ? of() : RegularImmutableMultiset.create(entries);
   }

   ImmutableMultiset() {
   }

   public UnmodifiableIterator<E> iterator() {
      final Iterator<Multiset.Entry<E>> entryIterator = this.entrySet().iterator();
      return new UnmodifiableIterator<E>(this) {
         int remaining;
         @CheckForNull
         E element;

         public boolean hasNext() {
            return this.remaining > 0 || entryIterator.hasNext();
         }

         public E next() {
            if (this.remaining <= 0) {
               Multiset.Entry<E> entry = (Multiset.Entry)entryIterator.next();
               this.element = entry.getElement();
               this.remaining = entry.getCount();
            }

            --this.remaining;
            return Objects.requireNonNull(this.element);
         }
      };
   }

   public ImmutableList<E> asList() {
      ImmutableList<E> result = this.asList;
      return result == null ? (this.asList = super.asList()) : result;
   }

   public boolean contains(@CheckForNull Object object) {
      return this.count(object) > 0;
   }

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final int add(E element, int occurrences) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final int remove(@CheckForNull Object element, int occurrences) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final int setCount(E element, int count) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final boolean setCount(E element, int oldCount, int newCount) {
      throw new UnsupportedOperationException();
   }

   @GwtIncompatible
   int copyIntoArray(Object[] dst, int offset) {
      Multiset.Entry entry;
      for(UnmodifiableIterator var3 = this.entrySet().iterator(); var3.hasNext(); offset += entry.getCount()) {
         entry = (Multiset.Entry)var3.next();
         Arrays.fill(dst, offset, offset + entry.getCount(), entry.getElement());
      }

      return offset;
   }

   public boolean equals(@CheckForNull Object object) {
      return Multisets.equalsImpl(this, object);
   }

   public int hashCode() {
      return Sets.hashCodeImpl(this.entrySet());
   }

   public String toString() {
      return this.entrySet().toString();
   }

   public abstract ImmutableSet<E> elementSet();

   public ImmutableSet<Multiset.Entry<E>> entrySet() {
      ImmutableSet<Multiset.Entry<E>> es = this.entrySet;
      return es == null ? (this.entrySet = this.createEntrySet()) : es;
   }

   private ImmutableSet<Multiset.Entry<E>> createEntrySet() {
      return (ImmutableSet)(this.isEmpty() ? ImmutableSet.of() : new ImmutableMultiset.EntrySet());
   }

   abstract Multiset.Entry<E> getEntry(int var1);

   @GwtIncompatible
   Object writeReplace() {
      return new ImmutableMultiset.SerializedForm(this);
   }

   public static <E> ImmutableMultiset.Builder<E> builder() {
      return new ImmutableMultiset.Builder();
   }

   static final class SerializedForm implements Serializable {
      final Object[] elements;
      final int[] counts;
      private static final long serialVersionUID = 0L;

      SerializedForm(Multiset<? extends Object> multiset) {
         int distinct = multiset.entrySet().size();
         this.elements = new Object[distinct];
         this.counts = new int[distinct];
         int i = 0;

         for(Iterator var4 = multiset.entrySet().iterator(); var4.hasNext(); ++i) {
            Multiset.Entry<? extends Object> entry = (Multiset.Entry)var4.next();
            this.elements[i] = entry.getElement();
            this.counts[i] = entry.getCount();
         }

      }

      Object readResolve() {
         LinkedHashMultiset<Object> multiset = LinkedHashMultiset.create(this.elements.length);

         for(int i = 0; i < this.elements.length; ++i) {
            multiset.add(this.elements[i], this.counts[i]);
         }

         return ImmutableMultiset.copyOf((Iterable)multiset);
      }
   }

   static final class ElementSet<E> extends ImmutableSet.Indexed<E> {
      private final List<Multiset.Entry<E>> entries;
      private final Multiset<E> delegate;

      ElementSet(List<Multiset.Entry<E>> entries, Multiset<E> delegate) {
         this.entries = entries;
         this.delegate = delegate;
      }

      E get(int index) {
         return ((Multiset.Entry)this.entries.get(index)).getElement();
      }

      public boolean contains(@CheckForNull Object object) {
         return this.delegate.contains(object);
      }

      boolean isPartialView() {
         return true;
      }

      public int size() {
         return this.entries.size();
      }
   }

   public static class Builder<E> extends ImmutableCollection.Builder<E> {
      final Multiset<E> contents;

      public Builder() {
         this(LinkedHashMultiset.create());
      }

      Builder(Multiset<E> contents) {
         this.contents = contents;
      }

      @CanIgnoreReturnValue
      public ImmutableMultiset.Builder<E> add(E element) {
         this.contents.add(Preconditions.checkNotNull(element));
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableMultiset.Builder<E> add(E... elements) {
         super.add(elements);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableMultiset.Builder<E> addCopies(E element, int occurrences) {
         this.contents.add(Preconditions.checkNotNull(element), occurrences);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableMultiset.Builder<E> setCount(E element, int count) {
         this.contents.setCount(Preconditions.checkNotNull(element), count);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableMultiset.Builder<E> addAll(Iterable<? extends E> elements) {
         if (elements instanceof Multiset) {
            Multiset<? extends E> multiset = Multisets.cast(elements);
            multiset.forEachEntry((e, n) -> {
               this.contents.add(Preconditions.checkNotNull(e), n);
            });
         } else {
            super.addAll(elements);
         }

         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableMultiset.Builder<E> addAll(Iterator<? extends E> elements) {
         super.addAll(elements);
         return this;
      }

      public ImmutableMultiset<E> build() {
         return ImmutableMultiset.copyOf((Iterable)this.contents);
      }

      @VisibleForTesting
      ImmutableMultiset<E> buildJdkBacked() {
         return this.contents.isEmpty() ? ImmutableMultiset.of() : JdkBackedImmutableMultiset.create(this.contents.entrySet());
      }
   }

   @GwtIncompatible
   static class EntrySetSerializedForm<E> implements Serializable {
      final ImmutableMultiset<E> multiset;

      EntrySetSerializedForm(ImmutableMultiset<E> multiset) {
         this.multiset = multiset;
      }

      Object readResolve() {
         return this.multiset.entrySet();
      }
   }

   private final class EntrySet extends IndexedImmutableSet<Multiset.Entry<E>> {
      private static final long serialVersionUID = 0L;

      private EntrySet() {
      }

      boolean isPartialView() {
         return ImmutableMultiset.this.isPartialView();
      }

      Multiset.Entry<E> get(int index) {
         return ImmutableMultiset.this.getEntry(index);
      }

      public int size() {
         return ImmutableMultiset.this.elementSet().size();
      }

      public boolean contains(@CheckForNull Object o) {
         if (o instanceof Multiset.Entry) {
            Multiset.Entry<?> entry = (Multiset.Entry)o;
            if (entry.getCount() <= 0) {
               return false;
            } else {
               int count = ImmutableMultiset.this.count(entry.getElement());
               return count == entry.getCount();
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return ImmutableMultiset.this.hashCode();
      }

      @GwtIncompatible
      Object writeReplace() {
         return new ImmutableMultiset.EntrySetSerializedForm(ImmutableMultiset.this);
      }

      // $FF: synthetic method
      EntrySet(Object x1) {
         this();
      }
   }
}
