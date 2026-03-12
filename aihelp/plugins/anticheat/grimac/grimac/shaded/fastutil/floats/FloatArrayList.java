package ac.grim.grimac.shaded.fastutil.floats;

import ac.grim.grimac.shaded.fastutil.SafeMath;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class FloatArrayList extends AbstractFloatList implements RandomAccess, Cloneable, Serializable {
   private static final long serialVersionUID = -7046029254386353130L;
   public static final int DEFAULT_INITIAL_CAPACITY = 10;
   protected transient float[] a;
   protected int size;

   private static final float[] copyArraySafe(float[] a, int length) {
      return length == 0 ? FloatArrays.EMPTY_ARRAY : Arrays.copyOf(a, length);
   }

   private static final float[] copyArrayFromSafe(FloatArrayList l) {
      return copyArraySafe(l.a, l.size);
   }

   protected FloatArrayList(float[] a, boolean wrapped) {
      this.a = a;
   }

   private void initArrayFromCapacity(int capacity) {
      if (capacity < 0) {
         throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
      } else {
         if (capacity == 0) {
            this.a = FloatArrays.EMPTY_ARRAY;
         } else {
            this.a = new float[capacity];
         }

      }
   }

   public FloatArrayList(int capacity) {
      this.initArrayFromCapacity(capacity);
   }

   public FloatArrayList() {
      this.a = FloatArrays.DEFAULT_EMPTY_ARRAY;
   }

   public FloatArrayList(Collection<? extends Float> c) {
      if (c instanceof FloatArrayList) {
         this.a = copyArrayFromSafe((FloatArrayList)c);
         this.size = this.a.length;
      } else {
         this.initArrayFromCapacity(c.size());
         if (c instanceof FloatList) {
            ((FloatList)c).getElements(0, this.a, 0, this.size = c.size());
         } else {
            this.size = FloatIterators.unwrap(FloatIterators.asFloatIterator(c.iterator()), this.a);
         }
      }

   }

   public FloatArrayList(FloatCollection c) {
      if (c instanceof FloatArrayList) {
         this.a = copyArrayFromSafe((FloatArrayList)c);
         this.size = this.a.length;
      } else {
         this.initArrayFromCapacity(c.size());
         if (c instanceof FloatList) {
            ((FloatList)c).getElements(0, this.a, 0, this.size = c.size());
         } else {
            this.size = FloatIterators.unwrap(c.iterator(), this.a);
         }
      }

   }

   public FloatArrayList(FloatList l) {
      if (l instanceof FloatArrayList) {
         this.a = copyArrayFromSafe((FloatArrayList)l);
         this.size = this.a.length;
      } else {
         this.initArrayFromCapacity(l.size());
         l.getElements(0, this.a, 0, this.size = l.size());
      }

   }

   public FloatArrayList(float[] a) {
      this(a, 0, a.length);
   }

   public FloatArrayList(float[] a, int offset, int length) {
      this(length);
      System.arraycopy(a, offset, this.a, 0, length);
      this.size = length;
   }

   public FloatArrayList(Iterator<? extends Float> i) {
      this();

      while(i.hasNext()) {
         this.add((Float)i.next());
      }

   }

   public FloatArrayList(FloatIterator i) {
      this();

      while(i.hasNext()) {
         this.add(i.nextFloat());
      }

   }

   public float[] elements() {
      return this.a;
   }

   public static FloatArrayList wrap(float[] a, int length) {
      if (length > a.length) {
         throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a.length + ")");
      } else {
         FloatArrayList l = new FloatArrayList(a, true);
         l.size = length;
         return l;
      }
   }

   public static FloatArrayList wrap(float[] a) {
      return wrap(a, a.length);
   }

   public static FloatArrayList of() {
      return new FloatArrayList();
   }

   public static FloatArrayList of(float... init) {
      return wrap(init);
   }

   public void ensureCapacity(int capacity) {
      if (capacity > this.a.length && (this.a != FloatArrays.DEFAULT_EMPTY_ARRAY || capacity > 10)) {
         this.a = FloatArrays.ensureCapacity(this.a, capacity, this.size);

         assert this.size <= this.a.length;

      }
   }

   private void grow(int capacity) {
      if (capacity > this.a.length) {
         if (this.a != FloatArrays.DEFAULT_EMPTY_ARRAY) {
            capacity = (int)Math.max(Math.min((long)this.a.length + (long)(this.a.length >> 1), 2147483639L), (long)capacity);
         } else if (capacity < 10) {
            capacity = 10;
         }

         this.a = FloatArrays.forceCapacity(this.a, capacity, this.size);

         assert this.size <= this.a.length;

      }
   }

   public void add(int index, float k) {
      this.ensureIndex(index);
      this.grow(this.size + 1);
      if (index != this.size) {
         System.arraycopy(this.a, index, this.a, index + 1, this.size - index);
      }

      this.a[index] = k;
      ++this.size;

      assert this.size <= this.a.length;

   }

   public boolean add(float k) {
      this.grow(this.size + 1);
      this.a[this.size++] = k;

      assert this.size <= this.a.length;

      return true;
   }

   public float getFloat(int index) {
      if (index >= this.size) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
      } else {
         return this.a[index];
      }
   }

   public int indexOf(float k) {
      float[] a = this.a;

      for(int i = 0; i < this.size; ++i) {
         if (Float.floatToIntBits(k) == Float.floatToIntBits(a[i])) {
            return i;
         }
      }

      return -1;
   }

   public int lastIndexOf(float k) {
      float[] a = this.a;
      int i = this.size;

      do {
         if (i-- == 0) {
            return -1;
         }
      } while(Float.floatToIntBits(k) != Float.floatToIntBits(a[i]));

      return i;
   }

   public float removeFloat(int index) {
      if (index >= this.size) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
      } else {
         float[] a = this.a;
         float old = a[index];
         --this.size;
         if (index != this.size) {
            System.arraycopy(a, index + 1, a, index, this.size - index);
         }

         assert this.size <= a.length;

         return old;
      }
   }

   public boolean rem(float k) {
      int index = this.indexOf(k);
      if (index == -1) {
         return false;
      } else {
         this.removeFloat(index);

         assert this.size <= this.a.length;

         return true;
      }
   }

   public float set(int index, float k) {
      if (index >= this.size) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
      } else {
         float old = this.a[index];
         this.a[index] = k;
         return old;
      }
   }

   public void clear() {
      this.size = 0;

      assert this.size <= this.a.length;

   }

   public int size() {
      return this.size;
   }

   public void size(int size) {
      if (size > this.a.length) {
         this.a = FloatArrays.forceCapacity(this.a, size, this.size);
      }

      if (size > this.size) {
         Arrays.fill(this.a, this.size, size, 0.0F);
      }

      this.size = size;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public void trim() {
      this.trim(0);
   }

   public void trim(int n) {
      if (n < this.a.length && this.size != this.a.length) {
         float[] t = new float[Math.max(n, this.size)];
         System.arraycopy(this.a, 0, t, 0, this.size);
         this.a = t;

         assert this.size <= this.a.length;

      }
   }

   public FloatList subList(int from, int to) {
      if (from == 0 && to == this.size()) {
         return this;
      } else {
         this.ensureIndex(from);
         this.ensureIndex(to);
         if (from > to) {
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
         } else {
            return new FloatArrayList.SubList(from, to);
         }
      }
   }

   public void getElements(int from, float[] a, int offset, int length) {
      FloatArrays.ensureOffsetLength(a, offset, length);
      System.arraycopy(this.a, from, a, offset, length);
   }

   public void removeElements(int from, int to) {
      ac.grim.grimac.shaded.fastutil.Arrays.ensureFromTo(this.size, from, to);
      System.arraycopy(this.a, to, this.a, from, this.size - to);
      this.size -= to - from;
   }

   public void addElements(int index, float[] a, int offset, int length) {
      this.ensureIndex(index);
      FloatArrays.ensureOffsetLength(a, offset, length);
      this.grow(this.size + length);
      System.arraycopy(this.a, index, this.a, index + length, this.size - index);
      System.arraycopy(a, offset, this.a, index, length);
      this.size += length;
   }

   public void setElements(int index, float[] a, int offset, int length) {
      this.ensureIndex(index);
      FloatArrays.ensureOffsetLength(a, offset, length);
      if (index + length > this.size) {
         throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size + ")");
      } else {
         System.arraycopy(a, offset, this.a, index, length);
      }
   }

   public void forEach(FloatConsumer action) {
      float[] a = this.a;

      for(int i = 0; i < this.size; ++i) {
         action.accept(a[i]);
      }

   }

   public boolean addAll(int index, FloatCollection c) {
      if (c instanceof FloatList) {
         return this.addAll(index, (FloatList)c);
      } else {
         this.ensureIndex(index);
         int n = c.size();
         if (n == 0) {
            return false;
         } else {
            this.grow(this.size + n);
            System.arraycopy(this.a, index, this.a, index + n, this.size - index);
            FloatIterator i = c.iterator();

            for(this.size += n; n-- != 0; this.a[index++] = i.nextFloat()) {
            }

            assert this.size <= this.a.length;

            return true;
         }
      }
   }

   public boolean addAll(int index, FloatList l) {
      this.ensureIndex(index);
      int n = l.size();
      if (n == 0) {
         return false;
      } else {
         this.grow(this.size + n);
         System.arraycopy(this.a, index, this.a, index + n, this.size - index);
         l.getElements(0, this.a, index, n);
         this.size += n;

         assert this.size <= this.a.length;

         return true;
      }
   }

   public boolean removeAll(FloatCollection c) {
      float[] a = this.a;
      int j = 0;

      for(int i = 0; i < this.size; ++i) {
         if (!c.contains(a[i])) {
            a[j++] = a[i];
         }
      }

      boolean modified = this.size != j;
      this.size = j;
      return modified;
   }

   public boolean removeIf(FloatPredicate filter) {
      float[] a = this.a;
      int j = 0;

      for(int i = 0; i < this.size; ++i) {
         if (!filter.test(a[i])) {
            a[j++] = a[i];
         }
      }

      boolean modified = this.size != j;
      this.size = j;
      return modified;
   }

   public float[] toArray(float[] a) {
      if (a == null || a.length < this.size) {
         a = Arrays.copyOf(a, this.size);
      }

      System.arraycopy(this.a, 0, a, 0, this.size);
      return a;
   }

   public FloatListIterator listIterator(final int index) {
      this.ensureIndex(index);
      return new FloatListIterator() {
         int pos = index;
         int last = -1;

         public boolean hasNext() {
            return this.pos < FloatArrayList.this.size;
         }

         public boolean hasPrevious() {
            return this.pos > 0;
         }

         public float nextFloat() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return FloatArrayList.this.a[this.last = this.pos++];
            }
         }

         public float previousFloat() {
            if (!this.hasPrevious()) {
               throw new NoSuchElementException();
            } else {
               return FloatArrayList.this.a[this.last = --this.pos];
            }
         }

         public int nextIndex() {
            return this.pos;
         }

         public int previousIndex() {
            return this.pos - 1;
         }

         public void add(float k) {
            FloatArrayList.this.add(this.pos++, k);
            this.last = -1;
         }

         public void set(float k) {
            if (this.last == -1) {
               throw new IllegalStateException();
            } else {
               FloatArrayList.this.set(this.last, k);
            }
         }

         public void remove() {
            if (this.last == -1) {
               throw new IllegalStateException();
            } else {
               FloatArrayList.this.removeFloat(this.last);
               if (this.last < this.pos) {
                  --this.pos;
               }

               this.last = -1;
            }
         }

         public void forEachRemaining(FloatConsumer action) {
            float[] a = FloatArrayList.this.a;

            while(this.pos < FloatArrayList.this.size) {
               action.accept(a[this.last = this.pos++]);
            }

         }

         public int back(int n) {
            if (n < 0) {
               throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            } else {
               int remaining = this.pos;
               if (n < remaining) {
                  this.pos -= n;
               } else {
                  n = remaining;
                  this.pos = 0;
               }

               this.last = this.pos;
               return n;
            }
         }

         public int skip(int n) {
            if (n < 0) {
               throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            } else {
               int remaining = FloatArrayList.this.size - this.pos;
               if (n < remaining) {
                  this.pos += n;
               } else {
                  n = remaining;
                  this.pos = FloatArrayList.this.size;
               }

               this.last = this.pos - 1;
               return n;
            }
         }
      };
   }

   public FloatSpliterator spliterator() {
      return new FloatArrayList.Spliterator();
   }

   public void sort(FloatComparator comp) {
      if (comp == null) {
         FloatArrays.stableSort(this.a, 0, this.size);
      } else {
         FloatArrays.stableSort(this.a, 0, this.size, comp);
      }

   }

   public void unstableSort(FloatComparator comp) {
      if (comp == null) {
         FloatArrays.unstableSort(this.a, 0, this.size);
      } else {
         FloatArrays.unstableSort(this.a, 0, this.size, comp);
      }

   }

   public FloatArrayList clone() {
      FloatArrayList cloned = null;
      if (this.getClass() == FloatArrayList.class) {
         cloned = new FloatArrayList(copyArraySafe(this.a, this.size), false);
         cloned.size = this.size;
      } else {
         try {
            cloned = (FloatArrayList)super.clone();
         } catch (CloneNotSupportedException var3) {
            throw new InternalError(var3);
         }

         cloned.a = copyArraySafe(this.a, this.size);
      }

      return cloned;
   }

   public boolean equals(FloatArrayList l) {
      if (l == this) {
         return true;
      } else {
         int s = this.size();
         if (s != l.size()) {
            return false;
         } else {
            float[] a1 = this.a;
            float[] a2 = l.a;
            if (a1 == a2 && s == l.size()) {
               return true;
            } else {
               do {
                  if (s-- == 0) {
                     return true;
                  }
               } while(a1[s] == a2[s]);

               return false;
            }
         }
      }
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (o == null) {
         return false;
      } else if (!(o instanceof List)) {
         return false;
      } else if (o instanceof FloatArrayList) {
         return this.equals((FloatArrayList)o);
      } else {
         return o instanceof FloatArrayList.SubList ? ((FloatArrayList.SubList)o).equals(this) : super.equals(o);
      }
   }

   public int compareTo(FloatArrayList l) {
      int s1 = this.size();
      int s2 = l.size();
      float[] a1 = this.a;
      float[] a2 = l.a;
      if (a1 == a2 && s1 == s2) {
         return 0;
      } else {
         int i;
         for(i = 0; i < s1 && i < s2; ++i) {
            float e1 = a1[i];
            float e2 = a2[i];
            int r;
            if ((r = Float.compare(e1, e2)) != 0) {
               return r;
            }
         }

         return i < s2 ? -1 : (i < s1 ? 1 : 0);
      }
   }

   public int compareTo(List<? extends Float> l) {
      if (l instanceof FloatArrayList) {
         return this.compareTo((FloatArrayList)l);
      } else {
         return l instanceof FloatArrayList.SubList ? -((FloatArrayList.SubList)l).compareTo((List)this) : super.compareTo(l);
      }
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();
      float[] a = this.a;

      for(int i = 0; i < this.size; ++i) {
         s.writeFloat(a[i]);
      }

   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      float[] a = this.a = new float[this.size];

      for(int i = 0; i < this.size; ++i) {
         a[i] = s.readFloat();
      }

   }

   private class SubList extends AbstractFloatList.FloatRandomAccessSubList {
      private static final long serialVersionUID = -3185226345314976296L;

      protected SubList(int param2, int param3) {
         super(FloatArrayList.this, from, to);
      }

      private float[] getParentArray() {
         return FloatArrayList.this.a;
      }

      public float getFloat(int i) {
         this.ensureRestrictedIndex(i);
         return FloatArrayList.this.a[i + this.from];
      }

      public FloatListIterator listIterator(int index) {
         return new FloatArrayList.SubList.SubListIterator(index);
      }

      public FloatSpliterator spliterator() {
         return new FloatArrayList.SubList.SubListSpliterator();
      }

      boolean contentsEquals(float[] otherA, int otherAFrom, int otherATo) {
         if (FloatArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
            return true;
         } else if (otherATo - otherAFrom != this.size()) {
            return false;
         } else {
            int pos = this.from;
            int var5 = otherAFrom;

            do {
               if (pos >= this.to) {
                  return true;
               }
            } while(FloatArrayList.this.a[pos++] == otherA[var5++]);

            return false;
         }
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (o == null) {
            return false;
         } else if (!(o instanceof List)) {
            return false;
         } else if (o instanceof FloatArrayList) {
            FloatArrayList other = (FloatArrayList)o;
            return this.contentsEquals(other.a, 0, other.size());
         } else if (o instanceof FloatArrayList.SubList) {
            FloatArrayList.SubList otherx = (FloatArrayList.SubList)o;
            return this.contentsEquals(otherx.getParentArray(), otherx.from, otherx.to);
         } else {
            return super.equals(o);
         }
      }

      int contentsCompareTo(float[] otherA, int otherAFrom, int otherATo) {
         if (FloatArrayList.this.a == otherA && this.from == otherAFrom && this.to == otherATo) {
            return 0;
         } else {
            int i = this.from;

            for(int j = otherAFrom; i < this.to && i < otherATo; ++j) {
               float e1 = FloatArrayList.this.a[i];
               float e2 = otherA[j];
               int r;
               if ((r = Float.compare(e1, e2)) != 0) {
                  return r;
               }

               ++i;
            }

            return i < otherATo ? -1 : (i < this.to ? 1 : 0);
         }
      }

      public int compareTo(List<? extends Float> l) {
         if (l instanceof FloatArrayList) {
            FloatArrayList other = (FloatArrayList)l;
            return this.contentsCompareTo(other.a, 0, other.size());
         } else if (l instanceof FloatArrayList.SubList) {
            FloatArrayList.SubList otherx = (FloatArrayList.SubList)l;
            return this.contentsCompareTo(otherx.getParentArray(), otherx.from, otherx.to);
         } else {
            return super.compareTo(l);
         }
      }

      private final class SubListIterator extends FloatIterators.AbstractIndexBasedListIterator {
         SubListIterator(int param2) {
            super(0, index);
         }

         protected final float get(int i) {
            return FloatArrayList.this.a[SubList.this.from + i];
         }

         protected final void add(int i, float k) {
            SubList.this.add(i, k);
         }

         protected final void set(int i, float k) {
            SubList.this.set(i, k);
         }

         protected final void remove(int i) {
            SubList.this.removeFloat(i);
         }

         protected final int getMaxPos() {
            return SubList.this.to - SubList.this.from;
         }

         public float nextFloat() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return FloatArrayList.this.a[SubList.this.from + (this.lastReturned = this.pos++)];
            }
         }

         public float previousFloat() {
            if (!this.hasPrevious()) {
               throw new NoSuchElementException();
            } else {
               return FloatArrayList.this.a[SubList.this.from + (this.lastReturned = --this.pos)];
            }
         }

         public void forEachRemaining(FloatConsumer action) {
            float[] a = FloatArrayList.this.a;
            int max = SubList.this.to - SubList.this.from;

            while(this.pos < max) {
               action.accept(a[SubList.this.from + (this.lastReturned = this.pos++)]);
            }

         }
      }

      private final class SubListSpliterator extends FloatSpliterators.LateBindingSizeIndexBasedSpliterator {
         SubListSpliterator() {
            super(SubList.this.from);
         }

         private SubListSpliterator(int param2, int param3) {
            super(pos, maxPos);
         }

         protected final int getMaxPosFromBackingStore() {
            return SubList.this.to;
         }

         protected final float get(int i) {
            return FloatArrayList.this.a[i];
         }

         protected final FloatArrayList.SubList.SubListSpliterator makeForSplit(int pos, int maxPos) {
            return SubList.this.new SubListSpliterator(pos, maxPos);
         }

         public boolean tryAdvance(FloatConsumer action) {
            if (this.pos >= this.getMaxPos()) {
               return false;
            } else {
               action.accept(FloatArrayList.this.a[this.pos++]);
               return true;
            }
         }

         public void forEachRemaining(FloatConsumer action) {
            float[] a = FloatArrayList.this.a;
            int max = this.getMaxPos();

            while(this.pos < max) {
               action.accept(a[this.pos++]);
            }

         }
      }
   }

   private final class Spliterator implements FloatSpliterator {
      boolean hasSplit;
      int pos;
      int max;

      public Spliterator() {
         this(0, FloatArrayList.this.size, false);
      }

      private Spliterator(int param2, int param3, boolean param4) {
         this.hasSplit = false;

         assert pos <= max : "pos " + pos + " must be <= max " + max;

         this.pos = pos;
         this.max = max;
         this.hasSplit = hasSplit;
      }

      private int getWorkingMax() {
         return this.hasSplit ? this.max : FloatArrayList.this.size;
      }

      public int characteristics() {
         return 16720;
      }

      public long estimateSize() {
         return (long)(this.getWorkingMax() - this.pos);
      }

      public boolean tryAdvance(FloatConsumer action) {
         if (this.pos >= this.getWorkingMax()) {
            return false;
         } else {
            action.accept(FloatArrayList.this.a[this.pos++]);
            return true;
         }
      }

      public void forEachRemaining(FloatConsumer action) {
         float[] a = FloatArrayList.this.a;

         for(int max = this.getWorkingMax(); this.pos < max; ++this.pos) {
            action.accept(a[this.pos]);
         }

      }

      public long skip(long n) {
         if (n < 0L) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else {
            int max = this.getWorkingMax();
            if (this.pos >= max) {
               return 0L;
            } else {
               int remaining = max - this.pos;
               if (n < (long)remaining) {
                  this.pos = SafeMath.safeLongToInt((long)this.pos + n);
                  return n;
               } else {
                  n = (long)remaining;
                  this.pos = max;
                  return n;
               }
            }
         }
      }

      public FloatSpliterator trySplit() {
         int max = this.getWorkingMax();
         int retLen = max - this.pos >> 1;
         if (retLen <= 1) {
            return null;
         } else {
            this.max = max;
            int myNewPos = this.pos + retLen;
            int oldPos = this.pos;
            this.pos = myNewPos;
            this.hasSplit = true;
            return FloatArrayList.this.new Spliterator(oldPos, myNewPos, true);
         }
      }
   }
}
