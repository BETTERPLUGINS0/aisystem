package ac.grim.grimac.shaded.fastutil.floats;

import ac.grim.grimac.shaded.fastutil.BigArrays;
import ac.grim.grimac.shaded.fastutil.SafeMath;
import ac.grim.grimac.shaded.fastutil.bytes.ByteIterator;
import ac.grim.grimac.shaded.fastutil.chars.CharIterator;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleIterator;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleIterators;
import ac.grim.grimac.shaded.fastutil.shorts.ShortIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator.OfDouble;
import java.util.function.Consumer;
import java.util.function.DoublePredicate;

public final class FloatIterators {
   public static final FloatIterators.EmptyIterator EMPTY_ITERATOR = new FloatIterators.EmptyIterator();

   private FloatIterators() {
   }

   public static FloatListIterator singleton(float element) {
      return new FloatIterators.SingletonIterator(element);
   }

   public static FloatListIterator wrap(float[] array, int offset, int length) {
      FloatArrays.ensureOffsetLength(array, offset, length);
      return new FloatIterators.ArrayIterator(array, offset, length);
   }

   public static FloatListIterator wrap(float[] array) {
      return new FloatIterators.ArrayIterator(array, 0, array.length);
   }

   public static int unwrap(FloatIterator i, float[] array, int offset, int max) {
      if (max < 0) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else if (offset >= 0 && offset + max <= array.length) {
         int j;
         for(j = max; j-- != 0 && i.hasNext(); array[offset++] = i.nextFloat()) {
         }

         return max - j - 1;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static int unwrap(FloatIterator i, float[] array) {
      return unwrap(i, array, 0, array.length);
   }

   public static float[] unwrap(FloatIterator i, int max) {
      if (max < 0) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else {
         float[] array = new float[16];

         int j;
         for(j = 0; max-- != 0 && i.hasNext(); array[j++] = i.nextFloat()) {
            if (j == array.length) {
               array = FloatArrays.grow(array, j + 1);
            }
         }

         return FloatArrays.trim(array, j);
      }
   }

   public static float[] unwrap(FloatIterator i) {
      return unwrap(i, Integer.MAX_VALUE);
   }

   public static long unwrap(FloatIterator i, float[][] array, long offset, long max) {
      if (max < 0L) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else if (offset >= 0L && offset + max <= BigArrays.length(array)) {
         long j = max;

         while(j-- != 0L && i.hasNext()) {
            BigArrays.set(array, offset++, i.nextFloat());
         }

         return max - j - 1L;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static long unwrap(FloatIterator i, float[][] array) {
      return unwrap(i, array, 0L, BigArrays.length(array));
   }

   public static int unwrap(FloatIterator i, FloatCollection c, int max) {
      if (max < 0) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else {
         int j = max;

         while(j-- != 0 && i.hasNext()) {
            c.add(i.nextFloat());
         }

         return max - j - 1;
      }
   }

   public static float[][] unwrapBig(FloatIterator i, long max) {
      if (max < 0L) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else {
         float[][] array = FloatBigArrays.newBigArray(16L);

         long j;
         for(j = 0L; max-- != 0L && i.hasNext(); BigArrays.set(array, j++, i.nextFloat())) {
            if (j == BigArrays.length(array)) {
               array = BigArrays.grow(array, j + 1L);
            }
         }

         return BigArrays.trim(array, j);
      }
   }

   public static float[][] unwrapBig(FloatIterator i) {
      return unwrapBig(i, Long.MAX_VALUE);
   }

   public static long unwrap(FloatIterator i, FloatCollection c) {
      long n;
      for(n = 0L; i.hasNext(); ++n) {
         c.add(i.nextFloat());
      }

      return n;
   }

   public static int pour(FloatIterator i, FloatCollection s, int max) {
      if (max < 0) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else {
         int j = max;

         while(j-- != 0 && i.hasNext()) {
            s.add(i.nextFloat());
         }

         return max - j - 1;
      }
   }

   public static int pour(FloatIterator i, FloatCollection s) {
      return pour(i, s, Integer.MAX_VALUE);
   }

   public static FloatList pour(FloatIterator i, int max) {
      FloatArrayList l = new FloatArrayList();
      pour(i, l, max);
      l.trim();
      return l;
   }

   public static FloatList pour(FloatIterator i) {
      return pour(i, Integer.MAX_VALUE);
   }

   public static FloatIterator asFloatIterator(Iterator i) {
      return (FloatIterator)(i instanceof FloatIterator ? (FloatIterator)i : new FloatIterators.IteratorWrapper(i));
   }

   public static FloatIterator narrow(OfDouble i) {
      return new FloatIterators.CheckedPrimitiveIteratorWrapper(i);
   }

   public static FloatIterator uncheckedNarrow(OfDouble i) {
      return new FloatIterators.PrimitiveIteratorWrapper(i);
   }

   public static DoubleIterator widen(FloatIterator i) {
      return DoubleIterators.wrap(i);
   }

   public static FloatListIterator asFloatIterator(ListIterator i) {
      return (FloatListIterator)(i instanceof FloatListIterator ? (FloatListIterator)i : new FloatIterators.ListIteratorWrapper(i));
   }

   public static boolean any(FloatIterator iterator, FloatPredicate predicate) {
      return indexOf(iterator, predicate) != -1;
   }

   public static boolean any(FloatIterator iterator, DoublePredicate predicate) {
      FloatPredicate var10001;
      if (predicate instanceof FloatPredicate) {
         var10001 = (FloatPredicate)predicate;
      } else {
         Objects.requireNonNull(predicate);
         var10001 = predicate::test;
      }

      return any(iterator, var10001);
   }

   public static boolean all(FloatIterator iterator, FloatPredicate predicate) {
      Objects.requireNonNull(predicate);

      while(iterator.hasNext()) {
         if (!predicate.test(iterator.nextFloat())) {
            return false;
         }
      }

      return true;
   }

   public static boolean all(FloatIterator iterator, DoublePredicate predicate) {
      FloatPredicate var10001;
      if (predicate instanceof FloatPredicate) {
         var10001 = (FloatPredicate)predicate;
      } else {
         Objects.requireNonNull(predicate);
         var10001 = predicate::test;
      }

      return all(iterator, var10001);
   }

   public static int indexOf(FloatIterator iterator, FloatPredicate predicate) {
      Objects.requireNonNull(predicate);

      for(int i = 0; iterator.hasNext(); ++i) {
         if (predicate.test(iterator.nextFloat())) {
            return i;
         }
      }

      return -1;
   }

   public static int indexOf(FloatIterator iterator, DoublePredicate predicate) {
      FloatPredicate var10001;
      if (predicate instanceof FloatPredicate) {
         var10001 = (FloatPredicate)predicate;
      } else {
         Objects.requireNonNull(predicate);
         var10001 = predicate::test;
      }

      return indexOf(iterator, var10001);
   }

   public static FloatIterator concat(FloatIterator... a) {
      return concat(a, 0, a.length);
   }

   public static FloatIterator concat(FloatIterator[] a, int offset, int length) {
      return new FloatIterators.IteratorConcatenator(a, offset, length);
   }

   public static FloatIterator unmodifiable(FloatIterator i) {
      return new FloatIterators.UnmodifiableIterator(i);
   }

   public static FloatBidirectionalIterator unmodifiable(FloatBidirectionalIterator i) {
      return new FloatIterators.UnmodifiableBidirectionalIterator(i);
   }

   public static FloatListIterator unmodifiable(FloatListIterator i) {
      return new FloatIterators.UnmodifiableListIterator(i);
   }

   public static FloatIterator wrap(ByteIterator iterator) {
      return new FloatIterators.ByteIteratorWrapper(iterator);
   }

   public static FloatIterator wrap(ShortIterator iterator) {
      return new FloatIterators.ShortIteratorWrapper(iterator);
   }

   public static FloatIterator wrap(CharIterator iterator) {
      return new FloatIterators.CharIteratorWrapper(iterator);
   }

   private static class SingletonIterator implements FloatListIterator {
      private final float element;
      private byte curr;

      public SingletonIterator(float element) {
         this.element = element;
      }

      public boolean hasNext() {
         return this.curr == 0;
      }

      public boolean hasPrevious() {
         return this.curr == 1;
      }

      public float nextFloat() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            this.curr = 1;
            return this.element;
         }
      }

      public float previousFloat() {
         if (!this.hasPrevious()) {
            throw new NoSuchElementException();
         } else {
            this.curr = 0;
            return this.element;
         }
      }

      public void forEachRemaining(FloatConsumer action) {
         Objects.requireNonNull(action);
         if (this.curr == 0) {
            action.accept(this.element);
            this.curr = 1;
         }

      }

      public int nextIndex() {
         return this.curr;
      }

      public int previousIndex() {
         return this.curr - 1;
      }

      public int back(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else if (n != 0 && this.curr >= 1) {
            this.curr = 1;
            return 1;
         } else {
            return 0;
         }
      }

      public int skip(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else if (n != 0 && this.curr <= 0) {
            this.curr = 0;
            return 1;
         } else {
            return 0;
         }
      }
   }

   private static class ArrayIterator implements FloatListIterator {
      private final float[] array;
      private final int offset;
      private final int length;
      private int curr;

      public ArrayIterator(float[] array, int offset, int length) {
         this.array = array;
         this.offset = offset;
         this.length = length;
      }

      public boolean hasNext() {
         return this.curr < this.length;
      }

      public boolean hasPrevious() {
         return this.curr > 0;
      }

      public float nextFloat() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return this.array[this.offset + this.curr++];
         }
      }

      public float previousFloat() {
         if (!this.hasPrevious()) {
            throw new NoSuchElementException();
         } else {
            return this.array[this.offset + --this.curr];
         }
      }

      public void forEachRemaining(FloatConsumer action) {
         Objects.requireNonNull(action);

         for(float[] array = this.array; this.curr < this.length; ++this.curr) {
            action.accept(array[this.offset + this.curr]);
         }

      }

      public int skip(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else if (n <= this.length - this.curr) {
            this.curr += n;
            return n;
         } else {
            n = this.length - this.curr;
            this.curr = this.length;
            return n;
         }
      }

      public int back(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else if (n <= this.curr) {
            this.curr -= n;
            return n;
         } else {
            n = this.curr;
            this.curr = 0;
            return n;
         }
      }

      public int nextIndex() {
         return this.curr;
      }

      public int previousIndex() {
         return this.curr - 1;
      }
   }

   private static class IteratorWrapper implements FloatIterator {
      final Iterator<Float> i;

      public IteratorWrapper(Iterator<Float> i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public void remove() {
         this.i.remove();
      }

      public float nextFloat() {
         return (Float)this.i.next();
      }

      public void forEachRemaining(FloatConsumer action) {
         this.i.forEachRemaining(action);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Float> action) {
         this.i.forEachRemaining(action);
      }
   }

   private static class CheckedPrimitiveIteratorWrapper extends FloatIterators.PrimitiveIteratorWrapper {
      public CheckedPrimitiveIteratorWrapper(OfDouble i) {
         super(i);
      }

      public float nextFloat() {
         return SafeMath.safeDoubleToFloat(this.i.nextDouble());
      }

      public void forEachRemaining(FloatConsumer action) {
         this.i.forEachRemaining((value) -> {
            action.accept(SafeMath.safeDoubleToFloat(value));
         });
      }
   }

   private static class PrimitiveIteratorWrapper implements FloatIterator {
      final OfDouble i;

      public PrimitiveIteratorWrapper(OfDouble i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public void remove() {
         this.i.remove();
      }

      public float nextFloat() {
         return (float)this.i.nextDouble();
      }

      public void forEachRemaining(FloatConsumer action) {
         this.i.forEachRemaining(action);
      }
   }

   private static class ListIteratorWrapper implements FloatListIterator {
      final ListIterator<Float> i;

      public ListIteratorWrapper(ListIterator<Float> i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public boolean hasPrevious() {
         return this.i.hasPrevious();
      }

      public int nextIndex() {
         return this.i.nextIndex();
      }

      public int previousIndex() {
         return this.i.previousIndex();
      }

      public void set(float k) {
         this.i.set(k);
      }

      public void add(float k) {
         this.i.add(k);
      }

      public void remove() {
         this.i.remove();
      }

      public float nextFloat() {
         return (Float)this.i.next();
      }

      public float previousFloat() {
         return (Float)this.i.previous();
      }

      public void forEachRemaining(FloatConsumer action) {
         this.i.forEachRemaining(action);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Float> action) {
         this.i.forEachRemaining(action);
      }
   }

   private static class IteratorConcatenator implements FloatIterator {
      final FloatIterator[] a;
      int offset;
      int length;
      int lastOffset = -1;

      public IteratorConcatenator(FloatIterator[] a, int offset, int length) {
         this.a = a;
         this.offset = offset;
         this.length = length;
         this.advance();
      }

      private void advance() {
         while(this.length != 0 && !this.a[this.offset].hasNext()) {
            --this.length;
            ++this.offset;
         }

      }

      public boolean hasNext() {
         return this.length > 0;
      }

      public float nextFloat() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            float next = this.a[this.lastOffset = this.offset].nextFloat();
            this.advance();
            return next;
         }
      }

      public void forEachRemaining(FloatConsumer action) {
         while(this.length > 0) {
            this.a[this.lastOffset = this.offset].forEachRemaining(action);
            this.advance();
         }

      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Float> action) {
         while(this.length > 0) {
            this.a[this.lastOffset = this.offset].forEachRemaining(action);
            this.advance();
         }

      }

      public void remove() {
         if (this.lastOffset == -1) {
            throw new IllegalStateException();
         } else {
            this.a[this.lastOffset].remove();
         }
      }

      public int skip(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else {
            this.lastOffset = -1;

            int skipped;
            for(skipped = 0; skipped < n && this.length != 0; ++this.offset) {
               skipped += this.a[this.offset].skip(n - skipped);
               if (this.a[this.offset].hasNext()) {
                  break;
               }

               --this.length;
            }

            return skipped;
         }
      }
   }

   public static class UnmodifiableIterator implements FloatIterator {
      protected final FloatIterator i;

      public UnmodifiableIterator(FloatIterator i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public float nextFloat() {
         return this.i.nextFloat();
      }

      public void forEachRemaining(FloatConsumer action) {
         this.i.forEachRemaining(action);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Float> action) {
         this.i.forEachRemaining(action);
      }
   }

   public static class UnmodifiableBidirectionalIterator implements FloatBidirectionalIterator {
      protected final FloatBidirectionalIterator i;

      public UnmodifiableBidirectionalIterator(FloatBidirectionalIterator i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public boolean hasPrevious() {
         return this.i.hasPrevious();
      }

      public float nextFloat() {
         return this.i.nextFloat();
      }

      public float previousFloat() {
         return this.i.previousFloat();
      }

      public void forEachRemaining(FloatConsumer action) {
         this.i.forEachRemaining(action);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Float> action) {
         this.i.forEachRemaining(action);
      }
   }

   public static class UnmodifiableListIterator implements FloatListIterator {
      protected final FloatListIterator i;

      public UnmodifiableListIterator(FloatListIterator i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public boolean hasPrevious() {
         return this.i.hasPrevious();
      }

      public float nextFloat() {
         return this.i.nextFloat();
      }

      public float previousFloat() {
         return this.i.previousFloat();
      }

      public int nextIndex() {
         return this.i.nextIndex();
      }

      public int previousIndex() {
         return this.i.previousIndex();
      }

      public void forEachRemaining(FloatConsumer action) {
         this.i.forEachRemaining(action);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Float> action) {
         this.i.forEachRemaining(action);
      }
   }

   private static final class ByteIteratorWrapper implements FloatIterator {
      final ByteIterator iterator;

      public ByteIteratorWrapper(ByteIterator iterator) {
         this.iterator = iterator;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      /** @deprecated */
      @Deprecated
      public Float next() {
         return (float)this.iterator.nextByte();
      }

      public float nextFloat() {
         return (float)this.iterator.nextByte();
      }

      public void forEachRemaining(FloatConsumer action) {
         Objects.requireNonNull(action);
         ByteIterator var10000 = this.iterator;
         Objects.requireNonNull(action);
         var10000.forEachRemaining(action::accept);
      }

      public void remove() {
         this.iterator.remove();
      }

      public int skip(int n) {
         return this.iterator.skip(n);
      }
   }

   private static final class ShortIteratorWrapper implements FloatIterator {
      final ShortIterator iterator;

      public ShortIteratorWrapper(ShortIterator iterator) {
         this.iterator = iterator;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      /** @deprecated */
      @Deprecated
      public Float next() {
         return (float)this.iterator.nextShort();
      }

      public float nextFloat() {
         return (float)this.iterator.nextShort();
      }

      public void forEachRemaining(FloatConsumer action) {
         Objects.requireNonNull(action);
         ShortIterator var10000 = this.iterator;
         Objects.requireNonNull(action);
         var10000.forEachRemaining(action::accept);
      }

      public void remove() {
         this.iterator.remove();
      }

      public int skip(int n) {
         return this.iterator.skip(n);
      }
   }

   private static final class CharIteratorWrapper implements FloatIterator {
      final CharIterator iterator;

      public CharIteratorWrapper(CharIterator iterator) {
         this.iterator = iterator;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      /** @deprecated */
      @Deprecated
      public Float next() {
         return (float)this.iterator.nextChar();
      }

      public float nextFloat() {
         return (float)this.iterator.nextChar();
      }

      public void forEachRemaining(FloatConsumer action) {
         Objects.requireNonNull(action);
         CharIterator var10000 = this.iterator;
         Objects.requireNonNull(action);
         var10000.forEachRemaining(action::accept);
      }

      public void remove() {
         this.iterator.remove();
      }

      public int skip(int n) {
         return this.iterator.skip(n);
      }
   }

   public static class EmptyIterator implements FloatListIterator, Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyIterator() {
      }

      public boolean hasNext() {
         return false;
      }

      public boolean hasPrevious() {
         return false;
      }

      public float nextFloat() {
         throw new NoSuchElementException();
      }

      public float previousFloat() {
         throw new NoSuchElementException();
      }

      public int nextIndex() {
         return 0;
      }

      public int previousIndex() {
         return -1;
      }

      public int skip(int n) {
         return 0;
      }

      public int back(int n) {
         return 0;
      }

      public void forEachRemaining(FloatConsumer action) {
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Float> action) {
      }

      public Object clone() {
         return FloatIterators.EMPTY_ITERATOR;
      }

      private Object readResolve() {
         return FloatIterators.EMPTY_ITERATOR;
      }
   }

   public abstract static class AbstractIndexBasedListIterator extends FloatIterators.AbstractIndexBasedIterator implements FloatListIterator {
      protected AbstractIndexBasedListIterator(int minPos, int initialPos) {
         super(minPos, initialPos);
      }

      protected abstract void add(int var1, float var2);

      protected abstract void set(int var1, float var2);

      public boolean hasPrevious() {
         return this.pos > this.minPos;
      }

      public float previousFloat() {
         if (!this.hasPrevious()) {
            throw new NoSuchElementException();
         } else {
            return this.get(this.lastReturned = --this.pos);
         }
      }

      public int nextIndex() {
         return this.pos;
      }

      public int previousIndex() {
         return this.pos - 1;
      }

      public void add(float k) {
         this.add(this.pos++, k);
         this.lastReturned = -1;
      }

      public void set(float k) {
         if (this.lastReturned == -1) {
            throw new IllegalStateException();
         } else {
            this.set(this.lastReturned, k);
         }
      }

      public int back(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else {
            int remaining = this.pos - this.minPos;
            if (n < remaining) {
               this.pos -= n;
            } else {
               n = remaining;
               this.pos = this.minPos;
            }

            this.lastReturned = this.pos;
            return n;
         }
      }
   }

   public abstract static class AbstractIndexBasedIterator extends AbstractFloatIterator {
      protected final int minPos;
      protected int pos;
      protected int lastReturned;

      protected AbstractIndexBasedIterator(int minPos, int initialPos) {
         this.minPos = minPos;
         this.pos = initialPos;
      }

      protected abstract float get(int var1);

      protected abstract void remove(int var1);

      protected abstract int getMaxPos();

      public boolean hasNext() {
         return this.pos < this.getMaxPos();
      }

      public float nextFloat() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return this.get(this.lastReturned = this.pos++);
         }
      }

      public void remove() {
         if (this.lastReturned == -1) {
            throw new IllegalStateException();
         } else {
            this.remove(this.lastReturned);
            if (this.lastReturned < this.pos) {
               --this.pos;
            }

            this.lastReturned = -1;
         }
      }

      public void forEachRemaining(FloatConsumer action) {
         while(this.pos < this.getMaxPos()) {
            action.accept(this.get(this.lastReturned = this.pos++));
         }

      }

      public int skip(int n) {
         if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
         } else {
            int max = this.getMaxPos();
            int remaining = max - this.pos;
            if (n < remaining) {
               this.pos += n;
            } else {
               n = remaining;
               this.pos = max;
            }

            this.lastReturned = this.pos - 1;
            return n;
         }
      }
   }
}
