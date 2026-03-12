package fr.xephi.authme.libs.com.google.common.primitives;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CheckReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Spliterator.OfInt;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import javax.annotation.CheckForNull;

@Immutable
@ElementTypesAreNonnullByDefault
@Beta
@GwtCompatible
public final class ImmutableIntArray implements Serializable {
   private static final ImmutableIntArray EMPTY = new ImmutableIntArray(new int[0]);
   private final int[] array;
   private final transient int start;
   private final int end;

   public static ImmutableIntArray of() {
      return EMPTY;
   }

   public static ImmutableIntArray of(int e0) {
      return new ImmutableIntArray(new int[]{e0});
   }

   public static ImmutableIntArray of(int e0, int e1) {
      return new ImmutableIntArray(new int[]{e0, e1});
   }

   public static ImmutableIntArray of(int e0, int e1, int e2) {
      return new ImmutableIntArray(new int[]{e0, e1, e2});
   }

   public static ImmutableIntArray of(int e0, int e1, int e2, int e3) {
      return new ImmutableIntArray(new int[]{e0, e1, e2, e3});
   }

   public static ImmutableIntArray of(int e0, int e1, int e2, int e3, int e4) {
      return new ImmutableIntArray(new int[]{e0, e1, e2, e3, e4});
   }

   public static ImmutableIntArray of(int e0, int e1, int e2, int e3, int e4, int e5) {
      return new ImmutableIntArray(new int[]{e0, e1, e2, e3, e4, e5});
   }

   public static ImmutableIntArray of(int first, int... rest) {
      Preconditions.checkArgument(rest.length <= 2147483646, "the total number of elements must fit in an int");
      int[] array = new int[rest.length + 1];
      array[0] = first;
      System.arraycopy(rest, 0, array, 1, rest.length);
      return new ImmutableIntArray(array);
   }

   public static ImmutableIntArray copyOf(int[] values) {
      return values.length == 0 ? EMPTY : new ImmutableIntArray(Arrays.copyOf(values, values.length));
   }

   public static ImmutableIntArray copyOf(Collection<Integer> values) {
      return values.isEmpty() ? EMPTY : new ImmutableIntArray(Ints.toArray(values));
   }

   public static ImmutableIntArray copyOf(Iterable<Integer> values) {
      return values instanceof Collection ? copyOf((Collection)values) : builder().addAll(values).build();
   }

   public static ImmutableIntArray copyOf(IntStream stream) {
      int[] array = stream.toArray();
      return array.length == 0 ? EMPTY : new ImmutableIntArray(array);
   }

   public static ImmutableIntArray.Builder builder(int initialCapacity) {
      Preconditions.checkArgument(initialCapacity >= 0, "Invalid initialCapacity: %s", initialCapacity);
      return new ImmutableIntArray.Builder(initialCapacity);
   }

   public static ImmutableIntArray.Builder builder() {
      return new ImmutableIntArray.Builder(10);
   }

   private ImmutableIntArray(int[] array) {
      this(array, 0, array.length);
   }

   private ImmutableIntArray(int[] array, int start, int end) {
      this.array = array;
      this.start = start;
      this.end = end;
   }

   public int length() {
      return this.end - this.start;
   }

   public boolean isEmpty() {
      return this.end == this.start;
   }

   public int get(int index) {
      Preconditions.checkElementIndex(index, this.length());
      return this.array[this.start + index];
   }

   public int indexOf(int target) {
      for(int i = this.start; i < this.end; ++i) {
         if (this.array[i] == target) {
            return i - this.start;
         }
      }

      return -1;
   }

   public int lastIndexOf(int target) {
      for(int i = this.end - 1; i >= this.start; --i) {
         if (this.array[i] == target) {
            return i - this.start;
         }
      }

      return -1;
   }

   public boolean contains(int target) {
      return this.indexOf(target) >= 0;
   }

   public void forEach(IntConsumer consumer) {
      Preconditions.checkNotNull(consumer);

      for(int i = this.start; i < this.end; ++i) {
         consumer.accept(this.array[i]);
      }

   }

   public IntStream stream() {
      return Arrays.stream(this.array, this.start, this.end);
   }

   public int[] toArray() {
      return Arrays.copyOfRange(this.array, this.start, this.end);
   }

   public ImmutableIntArray subArray(int startIndex, int endIndex) {
      Preconditions.checkPositionIndexes(startIndex, endIndex, this.length());
      return startIndex == endIndex ? EMPTY : new ImmutableIntArray(this.array, this.start + startIndex, this.start + endIndex);
   }

   private OfInt spliterator() {
      return Spliterators.spliterator(this.array, this.start, this.end, 1040);
   }

   public List<Integer> asList() {
      return new ImmutableIntArray.AsList(this);
   }

   public boolean equals(@CheckForNull Object object) {
      if (object == this) {
         return true;
      } else if (!(object instanceof ImmutableIntArray)) {
         return false;
      } else {
         ImmutableIntArray that = (ImmutableIntArray)object;
         if (this.length() != that.length()) {
            return false;
         } else {
            for(int i = 0; i < this.length(); ++i) {
               if (this.get(i) != that.get(i)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public int hashCode() {
      int hash = 1;

      for(int i = this.start; i < this.end; ++i) {
         hash *= 31;
         hash += Ints.hashCode(this.array[i]);
      }

      return hash;
   }

   public String toString() {
      if (this.isEmpty()) {
         return "[]";
      } else {
         StringBuilder builder = new StringBuilder(this.length() * 5);
         builder.append('[').append(this.array[this.start]);

         for(int i = this.start + 1; i < this.end; ++i) {
            builder.append(", ").append(this.array[i]);
         }

         builder.append(']');
         return builder.toString();
      }
   }

   public ImmutableIntArray trimmed() {
      return this.isPartialView() ? new ImmutableIntArray(this.toArray()) : this;
   }

   private boolean isPartialView() {
      return this.start > 0 || this.end < this.array.length;
   }

   Object writeReplace() {
      return this.trimmed();
   }

   Object readResolve() {
      return this.isEmpty() ? EMPTY : this;
   }

   // $FF: synthetic method
   ImmutableIntArray(int[] x0, int x1, int x2, Object x3) {
      this(x0, x1, x2);
   }

   static class AsList extends AbstractList<Integer> implements RandomAccess, Serializable {
      private final ImmutableIntArray parent;

      private AsList(ImmutableIntArray parent) {
         this.parent = parent;
      }

      public int size() {
         return this.parent.length();
      }

      public Integer get(int index) {
         return this.parent.get(index);
      }

      public boolean contains(@CheckForNull Object target) {
         return this.indexOf(target) >= 0;
      }

      public int indexOf(@CheckForNull Object target) {
         return target instanceof Integer ? this.parent.indexOf((Integer)target) : -1;
      }

      public int lastIndexOf(@CheckForNull Object target) {
         return target instanceof Integer ? this.parent.lastIndexOf((Integer)target) : -1;
      }

      public List<Integer> subList(int fromIndex, int toIndex) {
         return this.parent.subArray(fromIndex, toIndex).asList();
      }

      public Spliterator<Integer> spliterator() {
         return this.parent.spliterator();
      }

      public boolean equals(@CheckForNull Object object) {
         if (object instanceof ImmutableIntArray.AsList) {
            ImmutableIntArray.AsList that = (ImmutableIntArray.AsList)object;
            return this.parent.equals(that.parent);
         } else if (!(object instanceof List)) {
            return false;
         } else {
            List<?> that = (List)object;
            if (this.size() != that.size()) {
               return false;
            } else {
               int i = this.parent.start;
               Iterator var4 = that.iterator();

               Object element;
               do {
                  if (!var4.hasNext()) {
                     return true;
                  }

                  element = var4.next();
               } while(element instanceof Integer && this.parent.array[i++] == (Integer)element);

               return false;
            }
         }
      }

      public int hashCode() {
         return this.parent.hashCode();
      }

      public String toString() {
         return this.parent.toString();
      }

      // $FF: synthetic method
      AsList(ImmutableIntArray x0, Object x1) {
         this(x0);
      }
   }

   @CanIgnoreReturnValue
   public static final class Builder {
      private int[] array;
      private int count = 0;

      Builder(int initialCapacity) {
         this.array = new int[initialCapacity];
      }

      public ImmutableIntArray.Builder add(int value) {
         this.ensureRoomFor(1);
         this.array[this.count] = value;
         ++this.count;
         return this;
      }

      public ImmutableIntArray.Builder addAll(int[] values) {
         this.ensureRoomFor(values.length);
         System.arraycopy(values, 0, this.array, this.count, values.length);
         this.count += values.length;
         return this;
      }

      public ImmutableIntArray.Builder addAll(Iterable<Integer> values) {
         if (values instanceof Collection) {
            return this.addAll((Collection)values);
         } else {
            Iterator var2 = values.iterator();

            while(var2.hasNext()) {
               Integer value = (Integer)var2.next();
               this.add(value);
            }

            return this;
         }
      }

      public ImmutableIntArray.Builder addAll(Collection<Integer> values) {
         this.ensureRoomFor(values.size());

         Integer value;
         for(Iterator var2 = values.iterator(); var2.hasNext(); this.array[this.count++] = value) {
            value = (Integer)var2.next();
         }

         return this;
      }

      public ImmutableIntArray.Builder addAll(IntStream stream) {
         OfInt spliterator = stream.spliterator();
         long size = spliterator.getExactSizeIfKnown();
         if (size > 0L) {
            this.ensureRoomFor(Ints.saturatedCast(size));
         }

         spliterator.forEachRemaining(this::add);
         return this;
      }

      public ImmutableIntArray.Builder addAll(ImmutableIntArray values) {
         this.ensureRoomFor(values.length());
         System.arraycopy(values.array, values.start, this.array, this.count, values.length());
         this.count += values.length();
         return this;
      }

      private void ensureRoomFor(int numberToAdd) {
         int newCount = this.count + numberToAdd;
         if (newCount > this.array.length) {
            this.array = Arrays.copyOf(this.array, expandedCapacity(this.array.length, newCount));
         }

      }

      private static int expandedCapacity(int oldCapacity, int minCapacity) {
         if (minCapacity < 0) {
            throw new AssertionError("cannot store more than MAX_VALUE elements");
         } else {
            int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
            if (newCapacity < minCapacity) {
               newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
            }

            if (newCapacity < 0) {
               newCapacity = Integer.MAX_VALUE;
            }

            return newCapacity;
         }
      }

      @CheckReturnValue
      public ImmutableIntArray build() {
         return this.count == 0 ? ImmutableIntArray.EMPTY : new ImmutableIntArray(this.array, 0, this.count);
      }
   }
}
