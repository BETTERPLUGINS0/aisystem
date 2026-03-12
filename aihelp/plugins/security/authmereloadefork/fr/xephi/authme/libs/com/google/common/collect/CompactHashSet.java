package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Objects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
class CompactHashSet<E> extends AbstractSet<E> implements Serializable {
   @VisibleForTesting
   static final double HASH_FLOODING_FPP = 0.001D;
   private static final int MAX_HASH_BUCKET_LENGTH = 9;
   @CheckForNull
   private transient Object table;
   @CheckForNull
   private transient int[] entries;
   @CheckForNull
   @VisibleForTesting
   transient Object[] elements;
   private transient int metadata;
   private transient int size;

   public static <E> CompactHashSet<E> create() {
      return new CompactHashSet();
   }

   public static <E> CompactHashSet<E> create(Collection<? extends E> collection) {
      CompactHashSet<E> set = createWithExpectedSize(collection.size());
      set.addAll(collection);
      return set;
   }

   @SafeVarargs
   public static <E> CompactHashSet<E> create(E... elements) {
      CompactHashSet<E> set = createWithExpectedSize(elements.length);
      Collections.addAll(set, elements);
      return set;
   }

   public static <E> CompactHashSet<E> createWithExpectedSize(int expectedSize) {
      return new CompactHashSet(expectedSize);
   }

   CompactHashSet() {
      this.init(3);
   }

   CompactHashSet(int expectedSize) {
      this.init(expectedSize);
   }

   void init(int expectedSize) {
      Preconditions.checkArgument(expectedSize >= 0, "Expected size must be >= 0");
      this.metadata = Ints.constrainToRange(expectedSize, 1, 1073741823);
   }

   @VisibleForTesting
   boolean needsAllocArrays() {
      return this.table == null;
   }

   @CanIgnoreReturnValue
   int allocArrays() {
      Preconditions.checkState(this.needsAllocArrays(), "Arrays already allocated");
      int expectedSize = this.metadata;
      int buckets = CompactHashing.tableSize(expectedSize);
      this.table = CompactHashing.createTable(buckets);
      this.setHashTableMask(buckets - 1);
      this.entries = new int[expectedSize];
      this.elements = new Object[expectedSize];
      return expectedSize;
   }

   @CheckForNull
   @VisibleForTesting
   Set<E> delegateOrNull() {
      return this.table instanceof Set ? (Set)this.table : null;
   }

   private Set<E> createHashFloodingResistantDelegate(int tableSize) {
      return new LinkedHashSet(tableSize, 1.0F);
   }

   @VisibleForTesting
   @CanIgnoreReturnValue
   Set<E> convertToHashFloodingResistantImplementation() {
      Set<E> newDelegate = this.createHashFloodingResistantDelegate(this.hashTableMask() + 1);

      for(int i = this.firstEntryIndex(); i >= 0; i = this.getSuccessor(i)) {
         newDelegate.add(this.element(i));
      }

      this.table = newDelegate;
      this.entries = null;
      this.elements = null;
      this.incrementModCount();
      return newDelegate;
   }

   @VisibleForTesting
   boolean isUsingHashFloodingResistance() {
      return this.delegateOrNull() != null;
   }

   private void setHashTableMask(int mask) {
      int hashTableBits = 32 - Integer.numberOfLeadingZeros(mask);
      this.metadata = CompactHashing.maskCombine(this.metadata, hashTableBits, 31);
   }

   private int hashTableMask() {
      return (1 << (this.metadata & 31)) - 1;
   }

   void incrementModCount() {
      this.metadata += 32;
   }

   @CanIgnoreReturnValue
   public boolean add(@ParametricNullness E object) {
      if (this.needsAllocArrays()) {
         this.allocArrays();
      }

      Set<E> delegate = this.delegateOrNull();
      if (delegate != null) {
         return delegate.add(object);
      } else {
         int[] entries = this.requireEntries();
         Object[] elements = this.requireElements();
         int newEntryIndex = this.size;
         int newSize = newEntryIndex + 1;
         int hash = Hashing.smearedHash(object);
         int mask = this.hashTableMask();
         int tableIndex = hash & mask;
         int next = CompactHashing.tableGet(this.requireTable(), tableIndex);
         if (next == 0) {
            if (newSize > mask) {
               mask = this.resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
            } else {
               CompactHashing.tableSet(this.requireTable(), tableIndex, newEntryIndex + 1);
            }
         } else {
            int hashPrefix = CompactHashing.getHashPrefix(hash, mask);
            int bucketLength = 0;

            int entryIndex;
            int entry;
            do {
               entryIndex = next - 1;
               entry = entries[entryIndex];
               if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && Objects.equal(object, elements[entryIndex])) {
                  return false;
               }

               next = CompactHashing.getNext(entry, mask);
               ++bucketLength;
            } while(next != 0);

            if (bucketLength >= 9) {
               return this.convertToHashFloodingResistantImplementation().add(object);
            }

            if (newSize > mask) {
               mask = this.resizeTable(mask, CompactHashing.newCapacity(mask), hash, newEntryIndex);
            } else {
               entries[entryIndex] = CompactHashing.maskCombine(entry, newEntryIndex + 1, mask);
            }
         }

         this.resizeMeMaybe(newSize);
         this.insertEntry(newEntryIndex, object, hash, mask);
         this.size = newSize;
         this.incrementModCount();
         return true;
      }
   }

   void insertEntry(int entryIndex, @ParametricNullness E object, int hash, int mask) {
      this.setEntry(entryIndex, CompactHashing.maskCombine(hash, 0, mask));
      this.setElement(entryIndex, object);
   }

   private void resizeMeMaybe(int newSize) {
      int entriesSize = this.requireEntries().length;
      if (newSize > entriesSize) {
         int newCapacity = Math.min(1073741823, entriesSize + Math.max(1, entriesSize >>> 1) | 1);
         if (newCapacity != entriesSize) {
            this.resizeEntries(newCapacity);
         }
      }

   }

   void resizeEntries(int newCapacity) {
      this.entries = Arrays.copyOf(this.requireEntries(), newCapacity);
      this.elements = Arrays.copyOf(this.requireElements(), newCapacity);
   }

   @CanIgnoreReturnValue
   private int resizeTable(int oldMask, int newCapacity, int targetHash, int targetEntryIndex) {
      Object newTable = CompactHashing.createTable(newCapacity);
      int newMask = newCapacity - 1;
      if (targetEntryIndex != 0) {
         CompactHashing.tableSet(newTable, targetHash & newMask, targetEntryIndex + 1);
      }

      Object oldTable = this.requireTable();
      int[] entries = this.requireEntries();

      int oldEntry;
      for(int oldTableIndex = 0; oldTableIndex <= oldMask; ++oldTableIndex) {
         for(int oldNext = CompactHashing.tableGet(oldTable, oldTableIndex); oldNext != 0; oldNext = CompactHashing.getNext(oldEntry, oldMask)) {
            int entryIndex = oldNext - 1;
            oldEntry = entries[entryIndex];
            int hash = CompactHashing.getHashPrefix(oldEntry, oldMask) | oldTableIndex;
            int newTableIndex = hash & newMask;
            int newNext = CompactHashing.tableGet(newTable, newTableIndex);
            CompactHashing.tableSet(newTable, newTableIndex, oldNext);
            entries[entryIndex] = CompactHashing.maskCombine(hash, newNext, newMask);
         }
      }

      this.table = newTable;
      this.setHashTableMask(newMask);
      return newMask;
   }

   public boolean contains(@CheckForNull Object object) {
      if (this.needsAllocArrays()) {
         return false;
      } else {
         Set<E> delegate = this.delegateOrNull();
         if (delegate != null) {
            return delegate.contains(object);
         } else {
            int hash = Hashing.smearedHash(object);
            int mask = this.hashTableMask();
            int next = CompactHashing.tableGet(this.requireTable(), hash & mask);
            if (next == 0) {
               return false;
            } else {
               int hashPrefix = CompactHashing.getHashPrefix(hash, mask);

               do {
                  int entryIndex = next - 1;
                  int entry = this.entry(entryIndex);
                  if (CompactHashing.getHashPrefix(entry, mask) == hashPrefix && Objects.equal(object, this.element(entryIndex))) {
                     return true;
                  }

                  next = CompactHashing.getNext(entry, mask);
               } while(next != 0);

               return false;
            }
         }
      }
   }

   @CanIgnoreReturnValue
   public boolean remove(@CheckForNull Object object) {
      if (this.needsAllocArrays()) {
         return false;
      } else {
         Set<E> delegate = this.delegateOrNull();
         if (delegate != null) {
            return delegate.remove(object);
         } else {
            int mask = this.hashTableMask();
            int index = CompactHashing.remove(object, (Object)null, mask, this.requireTable(), this.requireEntries(), this.requireElements(), (Object[])null);
            if (index == -1) {
               return false;
            } else {
               this.moveLastEntry(index, mask);
               --this.size;
               this.incrementModCount();
               return true;
            }
         }
      }
   }

   void moveLastEntry(int dstIndex, int mask) {
      Object table = this.requireTable();
      int[] entries = this.requireEntries();
      Object[] elements = this.requireElements();
      int srcIndex = this.size() - 1;
      if (dstIndex < srcIndex) {
         Object object = elements[srcIndex];
         elements[dstIndex] = object;
         elements[srcIndex] = null;
         entries[dstIndex] = entries[srcIndex];
         entries[srcIndex] = 0;
         int tableIndex = Hashing.smearedHash(object) & mask;
         int next = CompactHashing.tableGet(table, tableIndex);
         int srcNext = srcIndex + 1;
         if (next == srcNext) {
            CompactHashing.tableSet(table, tableIndex, dstIndex + 1);
         } else {
            int entryIndex;
            int entry;
            do {
               entryIndex = next - 1;
               entry = entries[entryIndex];
               next = CompactHashing.getNext(entry, mask);
            } while(next != srcNext);

            entries[entryIndex] = CompactHashing.maskCombine(entry, dstIndex + 1, mask);
         }
      } else {
         elements[dstIndex] = null;
         entries[dstIndex] = 0;
      }

   }

   int firstEntryIndex() {
      return this.isEmpty() ? -1 : 0;
   }

   int getSuccessor(int entryIndex) {
      return entryIndex + 1 < this.size ? entryIndex + 1 : -1;
   }

   int adjustAfterRemove(int indexBeforeRemove, int indexRemoved) {
      return indexBeforeRemove - 1;
   }

   public Iterator<E> iterator() {
      Set<E> delegate = this.delegateOrNull();
      return delegate != null ? delegate.iterator() : new Iterator<E>() {
         int expectedMetadata;
         int currentIndex;
         int indexToRemove;

         {
            this.expectedMetadata = CompactHashSet.this.metadata;
            this.currentIndex = CompactHashSet.this.firstEntryIndex();
            this.indexToRemove = -1;
         }

         public boolean hasNext() {
            return this.currentIndex >= 0;
         }

         @ParametricNullness
         public E next() {
            this.checkForConcurrentModification();
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               this.indexToRemove = this.currentIndex;
               E result = CompactHashSet.this.element(this.currentIndex);
               this.currentIndex = CompactHashSet.this.getSuccessor(this.currentIndex);
               return result;
            }
         }

         public void remove() {
            this.checkForConcurrentModification();
            CollectPreconditions.checkRemove(this.indexToRemove >= 0);
            this.incrementExpectedModCount();
            CompactHashSet.this.remove(CompactHashSet.this.element(this.indexToRemove));
            this.currentIndex = CompactHashSet.this.adjustAfterRemove(this.currentIndex, this.indexToRemove);
            this.indexToRemove = -1;
         }

         void incrementExpectedModCount() {
            this.expectedMetadata += 32;
         }

         private void checkForConcurrentModification() {
            if (CompactHashSet.this.metadata != this.expectedMetadata) {
               throw new ConcurrentModificationException();
            }
         }
      };
   }

   public Spliterator<E> spliterator() {
      if (this.needsAllocArrays()) {
         return Spliterators.spliterator(new Object[0], 17);
      } else {
         Set<E> delegate = this.delegateOrNull();
         return delegate != null ? delegate.spliterator() : Spliterators.spliterator(this.requireElements(), 0, this.size, 17);
      }
   }

   public void forEach(Consumer<? super E> action) {
      Preconditions.checkNotNull(action);
      Set<E> delegate = this.delegateOrNull();
      if (delegate != null) {
         delegate.forEach(action);
      } else {
         for(int i = this.firstEntryIndex(); i >= 0; i = this.getSuccessor(i)) {
            action.accept(this.element(i));
         }
      }

   }

   public int size() {
      Set<E> delegate = this.delegateOrNull();
      return delegate != null ? delegate.size() : this.size;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public Object[] toArray() {
      if (this.needsAllocArrays()) {
         return new Object[0];
      } else {
         Set<E> delegate = this.delegateOrNull();
         return delegate != null ? delegate.toArray() : Arrays.copyOf(this.requireElements(), this.size);
      }
   }

   @CanIgnoreReturnValue
   public <T> T[] toArray(T[] a) {
      if (this.needsAllocArrays()) {
         if (a.length > 0) {
            a[0] = null;
         }

         return a;
      } else {
         Set<E> delegate = this.delegateOrNull();
         return delegate != null ? delegate.toArray(a) : ObjectArrays.toArrayImpl(this.requireElements(), 0, this.size, a);
      }
   }

   public void trimToSize() {
      if (!this.needsAllocArrays()) {
         Set<E> delegate = this.delegateOrNull();
         if (delegate != null) {
            Set<E> newDelegate = this.createHashFloodingResistantDelegate(this.size());
            newDelegate.addAll(delegate);
            this.table = newDelegate;
         } else {
            int size = this.size;
            if (size < this.requireEntries().length) {
               this.resizeEntries(size);
            }

            int minimumTableSize = CompactHashing.tableSize(size);
            int mask = this.hashTableMask();
            if (minimumTableSize < mask) {
               this.resizeTable(mask, minimumTableSize, 0, 0);
            }

         }
      }
   }

   public void clear() {
      if (!this.needsAllocArrays()) {
         this.incrementModCount();
         Set<E> delegate = this.delegateOrNull();
         if (delegate != null) {
            this.metadata = Ints.constrainToRange(this.size(), 3, 1073741823);
            delegate.clear();
            this.table = null;
            this.size = 0;
         } else {
            Arrays.fill(this.requireElements(), 0, this.size, (Object)null);
            CompactHashing.tableClear(this.requireTable());
            Arrays.fill(this.requireEntries(), 0, this.size, 0);
            this.size = 0;
         }

      }
   }

   private void writeObject(ObjectOutputStream stream) throws IOException {
      stream.defaultWriteObject();
      stream.writeInt(this.size());
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         E e = var2.next();
         stream.writeObject(e);
      }

   }

   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
      stream.defaultReadObject();
      int elementCount = stream.readInt();
      if (elementCount < 0) {
         throw new InvalidObjectException((new StringBuilder(25)).append("Invalid size: ").append(elementCount).toString());
      } else {
         this.init(elementCount);

         for(int i = 0; i < elementCount; ++i) {
            E element = stream.readObject();
            this.add(element);
         }

      }
   }

   private Object requireTable() {
      return java.util.Objects.requireNonNull(this.table);
   }

   private int[] requireEntries() {
      return (int[])java.util.Objects.requireNonNull(this.entries);
   }

   private Object[] requireElements() {
      return (Object[])java.util.Objects.requireNonNull(this.elements);
   }

   private E element(int i) {
      return this.requireElements()[i];
   }

   private int entry(int i) {
      return this.requireEntries()[i];
   }

   private void setElement(int i, E value) {
      this.requireElements()[i] = value;
   }

   private void setEntry(int i, int value) {
      this.requireEntries()[i] = value;
   }
}
