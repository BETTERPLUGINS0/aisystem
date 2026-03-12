package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.math.IntMath;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import fr.xephi.authme.libs.com.google.j2objc.annotations.RetainedWith;
import java.io.Serializable;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E> {
   static final int SPLITERATOR_CHARACTERISTICS = 1297;
   static final int MAX_TABLE_SIZE = 1073741824;
   private static final double DESIRED_LOAD_FACTOR = 0.7D;
   private static final int CUTOFF = 751619276;

   public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
      return CollectCollectors.toImmutableSet();
   }

   public static <E> ImmutableSet<E> of() {
      return RegularImmutableSet.EMPTY;
   }

   public static <E> ImmutableSet<E> of(E element) {
      return new SingletonImmutableSet(element);
   }

   public static <E> ImmutableSet<E> of(E e1, E e2) {
      return construct(2, 2, e1, e2);
   }

   public static <E> ImmutableSet<E> of(E e1, E e2, E e3) {
      return construct(3, 3, e1, e2, e3);
   }

   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4) {
      return construct(4, 4, e1, e2, e3, e4);
   }

   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5) {
      return construct(5, 5, e1, e2, e3, e4, e5);
   }

   @SafeVarargs
   public static <E> ImmutableSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
      Preconditions.checkArgument(others.length <= 2147483641, "the total number of elements must fit in an int");
      int paramCount = true;
      Object[] elements = new Object[6 + others.length];
      elements[0] = e1;
      elements[1] = e2;
      elements[2] = e3;
      elements[3] = e4;
      elements[4] = e5;
      elements[5] = e6;
      System.arraycopy(others, 0, elements, 6, others.length);
      return construct(elements.length, elements.length, elements);
   }

   private static <E> ImmutableSet<E> constructUnknownDuplication(int n, Object... elements) {
      return construct(n, Math.max(4, IntMath.sqrt(n, RoundingMode.CEILING)), elements);
   }

   private static <E> ImmutableSet<E> construct(int n, int expectedSize, Object... elements) {
      switch(n) {
      case 0:
         return of();
      case 1:
         E elem = elements[0];
         return of(elem);
      default:
         ImmutableSet.SetBuilderImpl<E> builder = new ImmutableSet.RegularSetBuilderImpl(expectedSize);

         for(int i = 0; i < n; ++i) {
            E e = Preconditions.checkNotNull(elements[i]);
            builder = ((ImmutableSet.SetBuilderImpl)builder).add(e);
         }

         return ((ImmutableSet.SetBuilderImpl)builder).review().build();
      }
   }

   public static <E> ImmutableSet<E> copyOf(Collection<? extends E> elements) {
      if (elements instanceof ImmutableSet && !(elements instanceof SortedSet)) {
         ImmutableSet<E> set = (ImmutableSet)elements;
         if (!set.isPartialView()) {
            return set;
         }
      } else if (elements instanceof EnumSet) {
         return copyOfEnumSet((EnumSet)elements);
      }

      Object[] array = elements.toArray();
      return elements instanceof Set ? construct(array.length, array.length, array) : constructUnknownDuplication(array.length, array);
   }

   public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) {
      return elements instanceof Collection ? copyOf((Collection)elements) : copyOf(elements.iterator());
   }

   public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> elements) {
      if (!elements.hasNext()) {
         return of();
      } else {
         E first = elements.next();
         return !elements.hasNext() ? of(first) : (new ImmutableSet.Builder()).add(first).addAll(elements).build();
      }
   }

   public static <E> ImmutableSet<E> copyOf(E[] elements) {
      switch(elements.length) {
      case 0:
         return of();
      case 1:
         return of(elements[0]);
      default:
         return constructUnknownDuplication(elements.length, (Object[])elements.clone());
      }
   }

   private static ImmutableSet copyOfEnumSet(EnumSet enumSet) {
      return ImmutableEnumSet.asImmutable(EnumSet.copyOf(enumSet));
   }

   ImmutableSet() {
   }

   boolean isHashCodeFast() {
      return false;
   }

   public boolean equals(@CheckForNull Object object) {
      if (object == this) {
         return true;
      } else {
         return object instanceof ImmutableSet && this.isHashCodeFast() && ((ImmutableSet)object).isHashCodeFast() && this.hashCode() != object.hashCode() ? false : Sets.equalsImpl(this, object);
      }
   }

   public int hashCode() {
      return Sets.hashCodeImpl(this);
   }

   public abstract UnmodifiableIterator<E> iterator();

   Object writeReplace() {
      return new ImmutableSet.SerializedForm(this.toArray());
   }

   public static <E> ImmutableSet.Builder<E> builder() {
      return new ImmutableSet.Builder();
   }

   @Beta
   public static <E> ImmutableSet.Builder<E> builderWithExpectedSize(int expectedSize) {
      CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
      return new ImmutableSet.Builder(expectedSize);
   }

   static int chooseTableSize(int setSize) {
      setSize = Math.max(setSize, 2);
      if (setSize >= 751619276) {
         Preconditions.checkArgument(setSize < 1073741824, "collection too large");
         return 1073741824;
      } else {
         int tableSize;
         for(tableSize = Integer.highestOneBit(setSize - 1) << 1; (double)tableSize * 0.7D < (double)setSize; tableSize <<= 1) {
         }

         return tableSize;
      }
   }

   private static final class JdkBackedSetBuilderImpl<E> extends ImmutableSet.SetBuilderImpl<E> {
      private final Set<Object> delegate;

      JdkBackedSetBuilderImpl(ImmutableSet.SetBuilderImpl<E> toCopy) {
         super(toCopy);
         this.delegate = Sets.newHashSetWithExpectedSize(this.distinct);

         for(int i = 0; i < this.distinct; ++i) {
            this.delegate.add(Objects.requireNonNull(this.dedupedElements[i]));
         }

      }

      ImmutableSet.SetBuilderImpl<E> add(E e) {
         Preconditions.checkNotNull(e);
         if (this.delegate.add(e)) {
            this.addDedupedElement(e);
         }

         return this;
      }

      ImmutableSet.SetBuilderImpl<E> copy() {
         return new ImmutableSet.JdkBackedSetBuilderImpl(this);
      }

      ImmutableSet<E> build() {
         switch(this.distinct) {
         case 0:
            return ImmutableSet.of();
         case 1:
            return ImmutableSet.of(Objects.requireNonNull(this.dedupedElements[0]));
         default:
            return new JdkBackedImmutableSet(this.delegate, ImmutableList.asImmutableList(this.dedupedElements, this.distinct));
         }
      }
   }

   private static final class RegularSetBuilderImpl<E> extends ImmutableSet.SetBuilderImpl<E> {
      @Nullable
      private Object[] hashTable;
      private int maxRunBeforeFallback;
      private int expandTableThreshold;
      private int hashCode;
      static final int MAX_RUN_MULTIPLIER = 13;

      RegularSetBuilderImpl(int expectedCapacity) {
         super(expectedCapacity);
         this.hashTable = null;
         this.maxRunBeforeFallback = 0;
         this.expandTableThreshold = 0;
      }

      RegularSetBuilderImpl(ImmutableSet.RegularSetBuilderImpl<E> toCopy) {
         super(toCopy);
         this.hashTable = toCopy.hashTable == null ? null : (Object[])toCopy.hashTable.clone();
         this.maxRunBeforeFallback = toCopy.maxRunBeforeFallback;
         this.expandTableThreshold = toCopy.expandTableThreshold;
         this.hashCode = toCopy.hashCode;
      }

      ImmutableSet.SetBuilderImpl<E> add(E e) {
         Preconditions.checkNotNull(e);
         if (this.hashTable == null) {
            if (this.distinct == 0) {
               this.addDedupedElement(e);
               return this;
            } else {
               this.ensureTableCapacity(this.dedupedElements.length);
               E elem = this.dedupedElements[0];
               --this.distinct;
               return this.insertInHashTable(elem).add(e);
            }
         } else {
            return this.insertInHashTable(e);
         }
      }

      private ImmutableSet.SetBuilderImpl<E> insertInHashTable(E e) {
         Objects.requireNonNull(this.hashTable);
         int eHash = e.hashCode();
         int i0 = Hashing.smear(eHash);
         int mask = this.hashTable.length - 1;

         for(int i = i0; i - i0 < this.maxRunBeforeFallback; ++i) {
            int index = i & mask;
            Object tableEntry = this.hashTable[index];
            if (tableEntry == null) {
               this.addDedupedElement(e);
               this.hashTable[index] = e;
               this.hashCode += eHash;
               this.ensureTableCapacity(this.distinct);
               return this;
            }

            if (tableEntry.equals(e)) {
               return this;
            }
         }

         return (new ImmutableSet.JdkBackedSetBuilderImpl(this)).add(e);
      }

      ImmutableSet.SetBuilderImpl<E> copy() {
         return new ImmutableSet.RegularSetBuilderImpl(this);
      }

      ImmutableSet.SetBuilderImpl<E> review() {
         if (this.hashTable == null) {
            return this;
         } else {
            int targetTableSize = ImmutableSet.chooseTableSize(this.distinct);
            if (targetTableSize * 2 < this.hashTable.length) {
               this.hashTable = rebuildHashTable(targetTableSize, this.dedupedElements, this.distinct);
               this.maxRunBeforeFallback = maxRunBeforeFallback(targetTableSize);
               this.expandTableThreshold = (int)(0.7D * (double)targetTableSize);
            }

            return (ImmutableSet.SetBuilderImpl)(hashFloodingDetected(this.hashTable) ? new ImmutableSet.JdkBackedSetBuilderImpl(this) : this);
         }
      }

      ImmutableSet<E> build() {
         switch(this.distinct) {
         case 0:
            return ImmutableSet.of();
         case 1:
            return ImmutableSet.of(Objects.requireNonNull(this.dedupedElements[0]));
         default:
            Object[] elements = this.distinct == this.dedupedElements.length ? this.dedupedElements : Arrays.copyOf(this.dedupedElements, this.distinct);
            return new RegularImmutableSet(elements, this.hashCode, (Object[])Objects.requireNonNull(this.hashTable), this.hashTable.length - 1);
         }
      }

      static Object[] rebuildHashTable(int newTableSize, Object[] elements, int n) {
         Object[] hashTable = new Object[newTableSize];
         int mask = hashTable.length - 1;

         for(int i = 0; i < n; ++i) {
            Object e = Objects.requireNonNull(elements[i]);
            int j0 = Hashing.smear(e.hashCode());
            int j = j0;

            while(true) {
               int index = j & mask;
               if (hashTable[index] == null) {
                  hashTable[index] = e;
                  break;
               }

               ++j;
            }
         }

         return hashTable;
      }

      void ensureTableCapacity(int minCapacity) {
         int newTableSize;
         if (this.hashTable == null) {
            newTableSize = ImmutableSet.chooseTableSize(minCapacity);
            this.hashTable = new Object[newTableSize];
         } else {
            if (minCapacity <= this.expandTableThreshold || this.hashTable.length >= 1073741824) {
               return;
            }

            newTableSize = this.hashTable.length * 2;
            this.hashTable = rebuildHashTable(newTableSize, this.dedupedElements, this.distinct);
         }

         this.maxRunBeforeFallback = maxRunBeforeFallback(newTableSize);
         this.expandTableThreshold = (int)(0.7D * (double)newTableSize);
      }

      static boolean hashFloodingDetected(Object[] hashTable) {
         int maxRunBeforeFallback = maxRunBeforeFallback(hashTable.length);
         int mask = hashTable.length - 1;
         int knownRunStart = 0;
         int knownRunEnd = 0;

         while(true) {
            label33:
            while(knownRunStart < hashTable.length) {
               if (knownRunStart == knownRunEnd && hashTable[knownRunStart] == null) {
                  if (hashTable[knownRunStart + maxRunBeforeFallback - 1 & mask] == null) {
                     knownRunStart += maxRunBeforeFallback;
                  } else {
                     ++knownRunStart;
                  }

                  knownRunEnd = knownRunStart;
               } else {
                  for(int j = knownRunStart + maxRunBeforeFallback - 1; j >= knownRunEnd; --j) {
                     if (hashTable[j & mask] == null) {
                        knownRunEnd = knownRunStart + maxRunBeforeFallback;
                        knownRunStart = j + 1;
                        continue label33;
                     }
                  }

                  return true;
               }
            }

            return false;
         }
      }

      static int maxRunBeforeFallback(int tableSize) {
         return 13 * IntMath.log2(tableSize, RoundingMode.UNNECESSARY);
      }
   }

   private static final class EmptySetBuilderImpl<E> extends ImmutableSet.SetBuilderImpl<E> {
      private static final ImmutableSet.EmptySetBuilderImpl<Object> INSTANCE = new ImmutableSet.EmptySetBuilderImpl();

      static <E> ImmutableSet.SetBuilderImpl<E> instance() {
         return INSTANCE;
      }

      private EmptySetBuilderImpl() {
         super(0);
      }

      ImmutableSet.SetBuilderImpl<E> add(E e) {
         return (new ImmutableSet.RegularSetBuilderImpl(4)).add(e);
      }

      ImmutableSet.SetBuilderImpl<E> copy() {
         return this;
      }

      ImmutableSet<E> build() {
         return ImmutableSet.of();
      }
   }

   private abstract static class SetBuilderImpl<E> {
      E[] dedupedElements;
      int distinct;

      SetBuilderImpl(int expectedCapacity) {
         this.dedupedElements = new Object[expectedCapacity];
         this.distinct = 0;
      }

      SetBuilderImpl(ImmutableSet.SetBuilderImpl<E> toCopy) {
         this.dedupedElements = Arrays.copyOf(toCopy.dedupedElements, toCopy.dedupedElements.length);
         this.distinct = toCopy.distinct;
      }

      private void ensureCapacity(int minCapacity) {
         if (minCapacity > this.dedupedElements.length) {
            int newCapacity = ImmutableCollection.Builder.expandedCapacity(this.dedupedElements.length, minCapacity);
            this.dedupedElements = Arrays.copyOf(this.dedupedElements, newCapacity);
         }

      }

      final void addDedupedElement(E e) {
         this.ensureCapacity(this.distinct + 1);
         this.dedupedElements[this.distinct++] = e;
      }

      abstract ImmutableSet.SetBuilderImpl<E> add(E var1);

      final ImmutableSet.SetBuilderImpl<E> combine(ImmutableSet.SetBuilderImpl<E> other) {
         ImmutableSet.SetBuilderImpl<E> result = this;

         for(int i = 0; i < other.distinct; ++i) {
            result = result.add(Objects.requireNonNull(other.dedupedElements[i]));
         }

         return result;
      }

      abstract ImmutableSet.SetBuilderImpl<E> copy();

      ImmutableSet.SetBuilderImpl<E> review() {
         return this;
      }

      abstract ImmutableSet<E> build();
   }

   public static class Builder<E> extends ImmutableCollection.Builder<E> {
      @CheckForNull
      private ImmutableSet.SetBuilderImpl<E> impl;
      boolean forceCopy;

      public Builder() {
         this(0);
      }

      Builder(int capacity) {
         if (capacity > 0) {
            this.impl = new ImmutableSet.RegularSetBuilderImpl(capacity);
         } else {
            this.impl = ImmutableSet.EmptySetBuilderImpl.instance();
         }

      }

      Builder(boolean subclass) {
         this.impl = null;
      }

      @VisibleForTesting
      void forceJdk() {
         Objects.requireNonNull(this.impl);
         this.impl = new ImmutableSet.JdkBackedSetBuilderImpl(this.impl);
      }

      final void copyIfNecessary() {
         if (this.forceCopy) {
            this.copy();
            this.forceCopy = false;
         }

      }

      void copy() {
         Objects.requireNonNull(this.impl);
         this.impl = this.impl.copy();
      }

      @CanIgnoreReturnValue
      public ImmutableSet.Builder<E> add(E element) {
         Objects.requireNonNull(this.impl);
         Preconditions.checkNotNull(element);
         this.copyIfNecessary();
         this.impl = this.impl.add(element);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableSet.Builder<E> add(E... elements) {
         super.add(elements);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableSet.Builder<E> addAll(Iterable<? extends E> elements) {
         super.addAll(elements);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableSet.Builder<E> addAll(Iterator<? extends E> elements) {
         super.addAll(elements);
         return this;
      }

      ImmutableSet.Builder<E> combine(ImmutableSet.Builder<E> other) {
         Objects.requireNonNull(this.impl);
         Objects.requireNonNull(other.impl);
         this.copyIfNecessary();
         this.impl = this.impl.combine(other.impl);
         return this;
      }

      public ImmutableSet<E> build() {
         Objects.requireNonNull(this.impl);
         this.forceCopy = true;
         this.impl = this.impl.review();
         return this.impl.build();
      }
   }

   private static class SerializedForm implements Serializable {
      final Object[] elements;
      private static final long serialVersionUID = 0L;

      SerializedForm(Object[] elements) {
         this.elements = elements;
      }

      Object readResolve() {
         return ImmutableSet.copyOf(this.elements);
      }
   }

   abstract static class Indexed<E> extends ImmutableSet.CachingAsList<E> {
      abstract E get(int var1);

      public UnmodifiableIterator<E> iterator() {
         return this.asList().iterator();
      }

      public Spliterator<E> spliterator() {
         return CollectSpliterators.indexed(this.size(), 1297, this::get);
      }

      public void forEach(Consumer<? super E> consumer) {
         Preconditions.checkNotNull(consumer);
         int n = this.size();

         for(int i = 0; i < n; ++i) {
            consumer.accept(this.get(i));
         }

      }

      int copyIntoArray(Object[] dst, int offset) {
         return this.asList().copyIntoArray(dst, offset);
      }

      ImmutableList<E> createAsList() {
         return new ImmutableAsList<E>() {
            public E get(int index) {
               return Indexed.this.get(index);
            }

            ImmutableSet.Indexed<E> delegateCollection() {
               return Indexed.this;
            }
         };
      }
   }

   @GwtCompatible
   abstract static class CachingAsList<E> extends ImmutableSet<E> {
      @LazyInit
      @CheckForNull
      @RetainedWith
      private transient ImmutableList<E> asList;

      public ImmutableList<E> asList() {
         ImmutableList<E> result = this.asList;
         return result == null ? (this.asList = this.createAsList()) : result;
      }

      ImmutableList<E> createAsList() {
         return new RegularImmutableAsList(this, this.toArray());
      }
   }
}
