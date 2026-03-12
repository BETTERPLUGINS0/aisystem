package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Objects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true,
   serializable = true
)
class RegularImmutableMultiset<E> extends ImmutableMultiset<E> {
   private static final Multisets.ImmutableEntry<?>[] EMPTY_ARRAY = new Multisets.ImmutableEntry[0];
   static final ImmutableMultiset<Object> EMPTY = create(ImmutableList.of());
   @VisibleForTesting
   static final double MAX_LOAD_FACTOR = 1.0D;
   @VisibleForTesting
   static final double HASH_FLOODING_FPP = 0.001D;
   @VisibleForTesting
   static final int MAX_HASH_BUCKET_LENGTH = 9;
   private final transient Multisets.ImmutableEntry<E>[] entries;
   private final transient Multisets.ImmutableEntry<?>[] hashTable;
   private final transient int size;
   private final transient int hashCode;
   @LazyInit
   @CheckForNull
   private transient ImmutableSet<E> elementSet;

   static <E> ImmutableMultiset<E> create(Collection<? extends Multiset.Entry<? extends E>> entries) {
      int distinct = entries.size();
      Multisets.ImmutableEntry<E>[] entryArray = new Multisets.ImmutableEntry[distinct];
      if (distinct == 0) {
         return new RegularImmutableMultiset(entryArray, EMPTY_ARRAY, 0, 0, ImmutableSet.of());
      } else {
         int tableSize = Hashing.closedTableSize(distinct, 1.0D);
         int mask = tableSize - 1;
         Multisets.ImmutableEntry<E>[] hashTable = new Multisets.ImmutableEntry[tableSize];
         int index = 0;
         int hashCode = 0;
         long size = 0L;

         int count;
         for(Iterator var10 = entries.iterator(); var10.hasNext(); size += (long)count) {
            Multiset.Entry<? extends E> entryWithWildcard = (Multiset.Entry)var10.next();
            E element = Preconditions.checkNotNull(entryWithWildcard.getElement());
            count = entryWithWildcard.getCount();
            int hash = element.hashCode();
            int bucket = Hashing.smear(hash) & mask;
            Multisets.ImmutableEntry<E> bucketHead = hashTable[bucket];
            Object newEntry;
            if (bucketHead != null) {
               newEntry = new RegularImmutableMultiset.NonTerminalEntry(element, count, bucketHead);
            } else {
               boolean canReuseEntry = entryWithWildcard instanceof Multisets.ImmutableEntry && !(entryWithWildcard instanceof RegularImmutableMultiset.NonTerminalEntry);
               newEntry = canReuseEntry ? (Multisets.ImmutableEntry)entryWithWildcard : new Multisets.ImmutableEntry(element, count);
            }

            hashCode += hash ^ count;
            entryArray[index++] = (Multisets.ImmutableEntry)newEntry;
            hashTable[bucket] = (Multisets.ImmutableEntry)newEntry;
         }

         return (ImmutableMultiset)(hashFloodingDetected(hashTable) ? JdkBackedImmutableMultiset.create(ImmutableList.asImmutableList(entryArray)) : new RegularImmutableMultiset(entryArray, hashTable, Ints.saturatedCast(size), hashCode, (ImmutableSet)null));
      }
   }

   private static boolean hashFloodingDetected(Multisets.ImmutableEntry<?>[] hashTable) {
      for(int i = 0; i < hashTable.length; ++i) {
         int bucketLength = 0;

         for(Multisets.ImmutableEntry entry = hashTable[i]; entry != null; entry = entry.nextInBucket()) {
            ++bucketLength;
            if (bucketLength > 9) {
               return true;
            }
         }
      }

      return false;
   }

   private RegularImmutableMultiset(Multisets.ImmutableEntry<E>[] entries, Multisets.ImmutableEntry<?>[] hashTable, int size, int hashCode, @CheckForNull ImmutableSet<E> elementSet) {
      this.entries = entries;
      this.hashTable = hashTable;
      this.size = size;
      this.hashCode = hashCode;
      this.elementSet = elementSet;
   }

   boolean isPartialView() {
      return false;
   }

   public int count(@CheckForNull Object element) {
      Multisets.ImmutableEntry<?>[] hashTable = this.hashTable;
      if (element != null && hashTable.length != 0) {
         int hash = Hashing.smearedHash(element);
         int mask = hashTable.length - 1;

         for(Multisets.ImmutableEntry entry = hashTable[hash & mask]; entry != null; entry = entry.nextInBucket()) {
            if (Objects.equal(element, entry.getElement())) {
               return entry.getCount();
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public int size() {
      return this.size;
   }

   public ImmutableSet<E> elementSet() {
      ImmutableSet<E> result = this.elementSet;
      return result == null ? (this.elementSet = new ImmutableMultiset.ElementSet(Arrays.asList(this.entries), this)) : result;
   }

   Multiset.Entry<E> getEntry(int index) {
      return this.entries[index];
   }

   public int hashCode() {
      return this.hashCode;
   }

   private static final class NonTerminalEntry<E> extends Multisets.ImmutableEntry<E> {
      private final Multisets.ImmutableEntry<E> nextInBucket;

      NonTerminalEntry(E element, int count, Multisets.ImmutableEntry<E> nextInBucket) {
         super(element, count);
         this.nextInBucket = nextInBucket;
      }

      public Multisets.ImmutableEntry<E> nextInBucket() {
         return this.nextInBucket;
      }
   }
}
