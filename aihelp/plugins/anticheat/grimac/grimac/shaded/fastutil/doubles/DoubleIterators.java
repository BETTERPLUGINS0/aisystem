package ac.grim.grimac.shaded.fastutil.doubles;

import ac.grim.grimac.shaded.fastutil.BigArrays;
import ac.grim.grimac.shaded.fastutil.bytes.ByteIterator;
import ac.grim.grimac.shaded.fastutil.chars.CharIterator;
import ac.grim.grimac.shaded.fastutil.floats.FloatIterator;
import ac.grim.grimac.shaded.fastutil.ints.IntIterator;
import ac.grim.grimac.shaded.fastutil.shorts.ShortIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator.OfDouble;
import java.util.function.Consumer;

public final class DoubleIterators {
   public static final DoubleIterators.EmptyIterator EMPTY_ITERATOR = new DoubleIterators.EmptyIterator();

   private DoubleIterators() {
   }

   public static DoubleListIterator singleton(double element) {
      return new DoubleIterators.SingletonIterator(element);
   }

   public static DoubleListIterator wrap(double[] array, int offset, int length) {
      DoubleArrays.ensureOffsetLength(array, offset, length);
      return new DoubleIterators.ArrayIterator(array, offset, length);
   }

   public static DoubleListIterator wrap(double[] array) {
      return new DoubleIterators.ArrayIterator(array, 0, array.length);
   }

   public static int unwrap(DoubleIterator i, double[] array, int offset, int max) {
      if (max < 0) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else if (offset >= 0 && offset + max <= array.length) {
         int j;
         for(j = max; j-- != 0 && i.hasNext(); array[offset++] = i.nextDouble()) {
         }

         return max - j - 1;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static int unwrap(DoubleIterator i, double[] array) {
      return unwrap(i, array, 0, array.length);
   }

   public static double[] unwrap(DoubleIterator i, int max) {
      if (max < 0) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else {
         double[] array = new double[16];

         int j;
         for(j = 0; max-- != 0 && i.hasNext(); array[j++] = i.nextDouble()) {
            if (j == array.length) {
               array = DoubleArrays.grow(array, j + 1);
            }
         }

         return DoubleArrays.trim(array, j);
      }
   }

   public static double[] unwrap(DoubleIterator i) {
      return unwrap(i, Integer.MAX_VALUE);
   }

   public static long unwrap(DoubleIterator i, double[][] array, long offset, long max) {
      if (max < 0L) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else if (offset >= 0L && offset + max <= BigArrays.length(array)) {
         long j = max;

         while(j-- != 0L && i.hasNext()) {
            BigArrays.set(array, offset++, i.nextDouble());
         }

         return max - j - 1L;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static long unwrap(DoubleIterator i, double[][] array) {
      return unwrap(i, array, 0L, BigArrays.length(array));
   }

   public static int unwrap(DoubleIterator i, DoubleCollection c, int max) {
      if (max < 0) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else {
         int j = max;

         while(j-- != 0 && i.hasNext()) {
            c.add(i.nextDouble());
         }

         return max - j - 1;
      }
   }

   public static double[][] unwrapBig(DoubleIterator i, long max) {
      if (max < 0L) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else {
         double[][] array = DoubleBigArrays.newBigArray(16L);

         long j;
         for(j = 0L; max-- != 0L && i.hasNext(); BigArrays.set(array, j++, i.nextDouble())) {
            if (j == BigArrays.length(array)) {
               array = BigArrays.grow(array, j + 1L);
            }
         }

         return BigArrays.trim(array, j);
      }
   }

   public static double[][] unwrapBig(DoubleIterator i) {
      return unwrapBig(i, Long.MAX_VALUE);
   }

   public static long unwrap(DoubleIterator i, DoubleCollection c) {
      long n;
      for(n = 0L; i.hasNext(); ++n) {
         c.add(i.nextDouble());
      }

      return n;
   }

   public static int pour(DoubleIterator i, DoubleCollection s, int max) {
      if (max < 0) {
         throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
      } else {
         int j = max;

         while(j-- != 0 && i.hasNext()) {
            s.add(i.nextDouble());
         }

         return max - j - 1;
      }
   }

   public static int pour(DoubleIterator i, DoubleCollection s) {
      return pour(i, s, Integer.MAX_VALUE);
   }

   public static DoubleList pour(DoubleIterator i, int max) {
      DoubleArrayList l = new DoubleArrayList();
      pour(i, l, max);
      l.trim();
      return l;
   }

   public static DoubleList pour(DoubleIterator i) {
      return pour(i, Integer.MAX_VALUE);
   }

   public static DoubleIterator asDoubleIterator(Iterator i) {
      if (i instanceof DoubleIterator) {
         return (DoubleIterator)i;
      } else {
         return (DoubleIterator)(i instanceof OfDouble ? new DoubleIterators.PrimitiveIteratorWrapper((OfDouble)i) : new DoubleIterators.IteratorWrapper(i));
      }
   }

   public static DoubleListIterator asDoubleIterator(ListIterator i) {
      return (DoubleListIterator)(i instanceof DoubleListIterator ? (DoubleListIterator)i : new DoubleIterators.ListIteratorWrapper(i));
   }

   public static boolean any(DoubleIterator iterator, java.util.function.DoublePredicate predicate) {
      return indexOf(iterator, predicate) != -1;
   }

   public static boolean all(DoubleIterator iterator, java.util.function.DoublePredicate predicate) {
      Objects.requireNonNull(predicate);

      while(iterator.hasNext()) {
         if (!predicate.test(iterator.nextDouble())) {
            return false;
         }
      }

      return true;
   }

   public static int indexOf(DoubleIterator iterator, java.util.function.DoublePredicate predicate) {
      Objects.requireNonNull(predicate);

      for(int i = 0; iterator.hasNext(); ++i) {
         if (predicate.test(iterator.nextDouble())) {
            return i;
         }
      }

      return -1;
   }

   public static DoubleIterator concat(DoubleIterator... a) {
      return concat(a, 0, a.length);
   }

   public static DoubleIterator concat(DoubleIterator[] a, int offset, int length) {
      return new DoubleIterators.IteratorConcatenator(a, offset, length);
   }

   public static DoubleIterator unmodifiable(DoubleIterator i) {
      return new DoubleIterators.UnmodifiableIterator(i);
   }

   public static DoubleBidirectionalIterator unmodifiable(DoubleBidirectionalIterator i) {
      return new DoubleIterators.UnmodifiableBidirectionalIterator(i);
   }

   public static DoubleListIterator unmodifiable(DoubleListIterator i) {
      return new DoubleIterators.UnmodifiableListIterator(i);
   }

   public static DoubleIterator wrap(ByteIterator iterator) {
      return new DoubleIterators.ByteIteratorWrapper(iterator);
   }

   public static DoubleIterator wrap(ShortIterator iterator) {
      return new DoubleIterators.ShortIteratorWrapper(iterator);
   }

   public static DoubleIterator wrap(CharIterator iterator) {
      return new DoubleIterators.CharIteratorWrapper(iterator);
   }

   public static DoubleIterator wrap(IntIterator iterator) {
      return new DoubleIterators.IntIteratorWrapper(iterator);
   }

   public static DoubleIterator wrap(FloatIterator iterator) {
      return new DoubleIterators.FloatIteratorWrapper(iterator);
   }

   private static class SingletonIterator implements DoubleListIterator {
      private final double element;
      private byte curr;

      public SingletonIterator(double element) {
         this.element = element;
      }

      public boolean hasNext() {
         return this.curr == 0;
      }

      public boolean hasPrevious() {
         return this.curr == 1;
      }

      public double nextDouble() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            this.curr = 1;
            return this.element;
         }
      }

      public double previousDouble() {
         if (!this.hasPrevious()) {
            throw new NoSuchElementException();
         } else {
            this.curr = 0;
            return this.element;
         }
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
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

   private static class ArrayIterator implements DoubleListIterator {
      private final double[] array;
      private final int offset;
      private final int length;
      private int curr;

      public ArrayIterator(double[] array, int offset, int length) {
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

      public double nextDouble() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return this.array[this.offset + this.curr++];
         }
      }

      public double previousDouble() {
         if (!this.hasPrevious()) {
            throw new NoSuchElementException();
         } else {
            return this.array[this.offset + --this.curr];
         }
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
         Objects.requireNonNull(action);

         for(double[] array = this.array; this.curr < this.length; ++this.curr) {
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

   private static class PrimitiveIteratorWrapper implements DoubleIterator {
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

      public double nextDouble() {
         return this.i.nextDouble();
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
         this.i.forEachRemaining(action);
      }
   }

   private static class IteratorWrapper implements DoubleIterator {
      final Iterator<Double> i;

      public IteratorWrapper(Iterator<Double> i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public void remove() {
         this.i.remove();
      }

      public double nextDouble() {
         return (Double)this.i.next();
      }

      public void forEachRemaining(DoubleConsumer action) {
         this.i.forEachRemaining(action);
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
         Objects.requireNonNull(action);
         Iterator var10000 = this.i;
         Consumer var10001;
         if (action instanceof Consumer) {
            var10001 = (Consumer)action;
         } else {
            Objects.requireNonNull(action);
            var10001 = action::accept;
         }

         var10000.forEachRemaining(var10001);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Double> action) {
         this.i.forEachRemaining(action);
      }
   }

   private static class ListIteratorWrapper implements DoubleListIterator {
      final ListIterator<Double> i;

      public ListIteratorWrapper(ListIterator<Double> i) {
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

      public void set(double k) {
         this.i.set(k);
      }

      public void add(double k) {
         this.i.add(k);
      }

      public void remove() {
         this.i.remove();
      }

      public double nextDouble() {
         return (Double)this.i.next();
      }

      public double previousDouble() {
         return (Double)this.i.previous();
      }

      public void forEachRemaining(DoubleConsumer action) {
         this.i.forEachRemaining(action);
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
         Objects.requireNonNull(action);
         ListIterator var10000 = this.i;
         Consumer var10001;
         if (action instanceof Consumer) {
            var10001 = (Consumer)action;
         } else {
            Objects.requireNonNull(action);
            var10001 = action::accept;
         }

         var10000.forEachRemaining(var10001);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Double> action) {
         this.i.forEachRemaining(action);
      }
   }

   private static class IteratorConcatenator implements DoubleIterator {
      final DoubleIterator[] a;
      int offset;
      int length;
      int lastOffset = -1;

      public IteratorConcatenator(DoubleIterator[] a, int offset, int length) {
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

      public double nextDouble() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            double next = this.a[this.lastOffset = this.offset].nextDouble();
            this.advance();
            return next;
         }
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
         while(this.length > 0) {
            this.a[this.lastOffset = this.offset].forEachRemaining((java.util.function.DoubleConsumer)action);
            this.advance();
         }

      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Double> action) {
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

   public static class UnmodifiableIterator implements DoubleIterator {
      protected final DoubleIterator i;

      public UnmodifiableIterator(DoubleIterator i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public double nextDouble() {
         return this.i.nextDouble();
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
         this.i.forEachRemaining((java.util.function.DoubleConsumer)action);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Double> action) {
         this.i.forEachRemaining(action);
      }
   }

   public static class UnmodifiableBidirectionalIterator implements DoubleBidirectionalIterator {
      protected final DoubleBidirectionalIterator i;

      public UnmodifiableBidirectionalIterator(DoubleBidirectionalIterator i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public boolean hasPrevious() {
         return this.i.hasPrevious();
      }

      public double nextDouble() {
         return this.i.nextDouble();
      }

      public double previousDouble() {
         return this.i.previousDouble();
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
         this.i.forEachRemaining(action);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Double> action) {
         this.i.forEachRemaining(action);
      }
   }

   public static class UnmodifiableListIterator implements DoubleListIterator {
      protected final DoubleListIterator i;

      public UnmodifiableListIterator(DoubleListIterator i) {
         this.i = i;
      }

      public boolean hasNext() {
         return this.i.hasNext();
      }

      public boolean hasPrevious() {
         return this.i.hasPrevious();
      }

      public double nextDouble() {
         return this.i.nextDouble();
      }

      public double previousDouble() {
         return this.i.previousDouble();
      }

      public int nextIndex() {
         return this.i.nextIndex();
      }

      public int previousIndex() {
         return this.i.previousIndex();
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
         this.i.forEachRemaining(action);
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Double> action) {
         this.i.forEachRemaining(action);
      }
   }

   private static final class ByteIteratorWrapper implements DoubleIterator {
      final ByteIterator iterator;

      public ByteIteratorWrapper(ByteIterator iterator) {
         this.iterator = iterator;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      /** @deprecated */
      @Deprecated
      public Double next() {
         return (double)this.iterator.nextByte();
      }

      public double nextDouble() {
         return (double)this.iterator.nextByte();
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
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

   private static final class ShortIteratorWrapper implements DoubleIterator {
      final ShortIterator iterator;

      public ShortIteratorWrapper(ShortIterator iterator) {
         this.iterator = iterator;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      /** @deprecated */
      @Deprecated
      public Double next() {
         return (double)this.iterator.nextShort();
      }

      public double nextDouble() {
         return (double)this.iterator.nextShort();
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
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

   private static final class CharIteratorWrapper implements DoubleIterator {
      final CharIterator iterator;

      public CharIteratorWrapper(CharIterator iterator) {
         this.iterator = iterator;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      /** @deprecated */
      @Deprecated
      public Double next() {
         return (double)this.iterator.nextChar();
      }

      public double nextDouble() {
         return (double)this.iterator.nextChar();
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
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

   private static final class IntIteratorWrapper implements DoubleIterator {
      final IntIterator iterator;

      public IntIteratorWrapper(IntIterator iterator) {
         this.iterator = iterator;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      /** @deprecated */
      @Deprecated
      public Double next() {
         return (double)this.iterator.nextInt();
      }

      public double nextDouble() {
         return (double)this.iterator.nextInt();
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
         Objects.requireNonNull(action);
         IntIterator var10000 = this.iterator;
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

   private static final class FloatIteratorWrapper implements DoubleIterator {
      final FloatIterator iterator;

      public FloatIteratorWrapper(FloatIterator iterator) {
         this.iterator = iterator;
      }

      public boolean hasNext() {
         return this.iterator.hasNext();
      }

      /** @deprecated */
      @Deprecated
      public Double next() {
         return (double)this.iterator.nextFloat();
      }

      public double nextDouble() {
         return (double)this.iterator.nextFloat();
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
         Objects.requireNonNull(action);
         FloatIterator var10000 = this.iterator;
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

   public static class EmptyIterator implements DoubleListIterator, Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyIterator() {
      }

      public boolean hasNext() {
         return false;
      }

      public boolean hasPrevious() {
         return false;
      }

      public double nextDouble() {
         throw new NoSuchElementException();
      }

      public double previousDouble() {
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

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
      }

      /** @deprecated */
      @Deprecated
      public void forEachRemaining(Consumer<? super Double> action) {
      }

      public Object clone() {
         return DoubleIterators.EMPTY_ITERATOR;
      }

      private Object readResolve() {
         return DoubleIterators.EMPTY_ITERATOR;
      }
   }

   public abstract static class AbstractIndexBasedListIterator extends DoubleIterators.AbstractIndexBasedIterator implements DoubleListIterator {
      protected AbstractIndexBasedListIterator(int minPos, int initialPos) {
         super(minPos, initialPos);
      }

      protected abstract void add(int var1, double var2);

      protected abstract void set(int var1, double var2);

      public boolean hasPrevious() {
         return this.pos > this.minPos;
      }

      public double previousDouble() {
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

      public void add(double k) {
         this.add(this.pos++, k);
         this.lastReturned = -1;
      }

      public void set(double k) {
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

   public abstract static class AbstractIndexBasedIterator extends AbstractDoubleIterator {
      protected final int minPos;
      protected int pos;
      protected int lastReturned;

      protected AbstractIndexBasedIterator(int minPos, int initialPos) {
         this.minPos = minPos;
         this.pos = initialPos;
      }

      protected abstract double get(int var1);

      protected abstract void remove(int var1);

      protected abstract int getMaxPos();

      public boolean hasNext() {
         return this.pos < this.getMaxPos();
      }

      public double nextDouble() {
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

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
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
