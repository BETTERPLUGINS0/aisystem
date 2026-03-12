package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.primitives.Ints;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.ObjIntConsumer;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
public final class EnumMultiset<E extends Enum<E>> extends AbstractMultiset<E> implements Serializable {
   private transient Class<E> type;
   private transient E[] enumConstants;
   private transient int[] counts;
   private transient int distinctElements;
   private transient long size;
   @GwtIncompatible
   private static final long serialVersionUID = 0L;

   public static <E extends Enum<E>> EnumMultiset<E> create(Class<E> type) {
      return new EnumMultiset(type);
   }

   public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> elements) {
      Iterator<E> iterator = elements.iterator();
      Preconditions.checkArgument(iterator.hasNext(), "EnumMultiset constructor passed empty Iterable");
      EnumMultiset<E> multiset = new EnumMultiset(((Enum)iterator.next()).getDeclaringClass());
      Iterables.addAll(multiset, elements);
      return multiset;
   }

   public static <E extends Enum<E>> EnumMultiset<E> create(Iterable<E> elements, Class<E> type) {
      EnumMultiset<E> result = create(type);
      Iterables.addAll(result, elements);
      return result;
   }

   private EnumMultiset(Class<E> type) {
      this.type = type;
      Preconditions.checkArgument(type.isEnum());
      this.enumConstants = (Enum[])type.getEnumConstants();
      this.counts = new int[this.enumConstants.length];
   }

   private boolean isActuallyE(@CheckForNull Object o) {
      if (!(o instanceof Enum)) {
         return false;
      } else {
         Enum<?> e = (Enum)o;
         int index = e.ordinal();
         return index < this.enumConstants.length && this.enumConstants[index] == e;
      }
   }

   private void checkIsE(Object element) {
      Preconditions.checkNotNull(element);
      if (!this.isActuallyE(element)) {
         String var2 = String.valueOf(this.type);
         String var3 = String.valueOf(element);
         throw new ClassCastException((new StringBuilder(21 + String.valueOf(var2).length() + String.valueOf(var3).length())).append("Expected an ").append(var2).append(" but got ").append(var3).toString());
      }
   }

   int distinctElements() {
      return this.distinctElements;
   }

   public int size() {
      return Ints.saturatedCast(this.size);
   }

   public int count(@CheckForNull Object element) {
      if (element != null && this.isActuallyE(element)) {
         Enum<?> e = (Enum)element;
         return this.counts[e.ordinal()];
      } else {
         return 0;
      }
   }

   @CanIgnoreReturnValue
   public int add(E element, int occurrences) {
      this.checkIsE(element);
      CollectPreconditions.checkNonnegative(occurrences, "occurrences");
      if (occurrences == 0) {
         return this.count(element);
      } else {
         int index = element.ordinal();
         int oldCount = this.counts[index];
         long newCount = (long)oldCount + (long)occurrences;
         Preconditions.checkArgument(newCount <= 2147483647L, "too many occurrences: %s", newCount);
         this.counts[index] = (int)newCount;
         if (oldCount == 0) {
            ++this.distinctElements;
         }

         this.size += (long)occurrences;
         return oldCount;
      }
   }

   @CanIgnoreReturnValue
   public int remove(@CheckForNull Object element, int occurrences) {
      if (element != null && this.isActuallyE(element)) {
         Enum<?> e = (Enum)element;
         CollectPreconditions.checkNonnegative(occurrences, "occurrences");
         if (occurrences == 0) {
            return this.count(element);
         } else {
            int index = e.ordinal();
            int oldCount = this.counts[index];
            if (oldCount == 0) {
               return 0;
            } else {
               if (oldCount <= occurrences) {
                  this.counts[index] = 0;
                  --this.distinctElements;
                  this.size -= (long)oldCount;
               } else {
                  this.counts[index] = oldCount - occurrences;
                  this.size -= (long)occurrences;
               }

               return oldCount;
            }
         }
      } else {
         return 0;
      }
   }

   @CanIgnoreReturnValue
   public int setCount(E element, int count) {
      this.checkIsE(element);
      CollectPreconditions.checkNonnegative(count, "count");
      int index = element.ordinal();
      int oldCount = this.counts[index];
      this.counts[index] = count;
      this.size += (long)(count - oldCount);
      if (oldCount == 0 && count > 0) {
         ++this.distinctElements;
      } else if (oldCount > 0 && count == 0) {
         --this.distinctElements;
      }

      return oldCount;
   }

   public void clear() {
      Arrays.fill(this.counts, 0);
      this.size = 0L;
      this.distinctElements = 0;
   }

   Iterator<E> elementIterator() {
      return new EnumMultiset<E>.Itr<E>() {
         E output(int index) {
            return EnumMultiset.this.enumConstants[index];
         }
      };
   }

   Iterator<Multiset.Entry<E>> entryIterator() {
      return new EnumMultiset<E>.Itr<Multiset.Entry<E>>() {
         Multiset.Entry<E> output(final int index) {
            return new Multisets.AbstractEntry<E>() {
               public E getElement() {
                  return EnumMultiset.this.enumConstants[index];
               }

               public int getCount() {
                  return EnumMultiset.this.counts[index];
               }
            };
         }
      };
   }

   public void forEachEntry(ObjIntConsumer<? super E> action) {
      Preconditions.checkNotNull(action);

      for(int i = 0; i < this.enumConstants.length; ++i) {
         if (this.counts[i] > 0) {
            action.accept(this.enumConstants[i], this.counts[i]);
         }
      }

   }

   public Iterator<E> iterator() {
      return Multisets.iteratorImpl(this);
   }

   @GwtIncompatible
   private void writeObject(ObjectOutputStream stream) throws IOException {
      stream.defaultWriteObject();
      stream.writeObject(this.type);
      Serialization.writeMultiset(this, stream);
   }

   @GwtIncompatible
   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
      stream.defaultReadObject();
      Class<E> localType = (Class)stream.readObject();
      this.type = localType;
      this.enumConstants = (Enum[])this.type.getEnumConstants();
      this.counts = new int[this.enumConstants.length];
      Serialization.populateMultiset(this, stream);
   }

   abstract class Itr<T> implements Iterator<T> {
      int index = 0;
      int toRemove = -1;

      abstract T output(int var1);

      public boolean hasNext() {
         while(this.index < EnumMultiset.this.enumConstants.length) {
            if (EnumMultiset.this.counts[this.index] > 0) {
               return true;
            }

            ++this.index;
         }

         return false;
      }

      public T next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            T result = this.output(this.index);
            this.toRemove = this.index++;
            return result;
         }
      }

      public void remove() {
         CollectPreconditions.checkRemove(this.toRemove >= 0);
         if (EnumMultiset.this.counts[this.toRemove] > 0) {
            EnumMultiset.this.distinctElements--;
            EnumMultiset.this.size = (long)EnumMultiset.this.counts[this.toRemove];
            EnumMultiset.this.counts[this.toRemove] = 0;
         }

         this.toRemove = -1;
      }
   }
}
