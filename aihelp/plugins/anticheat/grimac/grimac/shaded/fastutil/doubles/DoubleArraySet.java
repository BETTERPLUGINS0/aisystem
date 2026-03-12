package ac.grim.grimac.shaded.fastutil.doubles;

import ac.grim.grimac.shaded.fastutil.SafeMath;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class DoubleArraySet extends AbstractDoubleSet implements Serializable, Cloneable {
   private static final long serialVersionUID = 1L;
   protected transient double[] a;
   protected int size;

   public DoubleArraySet(double[] a) {
      this.a = a;
      this.size = a.length;
   }

   public DoubleArraySet() {
      this.a = DoubleArrays.EMPTY_ARRAY;
   }

   public DoubleArraySet(int capacity) {
      this.a = new double[capacity];
   }

   public DoubleArraySet(DoubleCollection c) {
      this(c.size());
      this.addAll(c);
   }

   public DoubleArraySet(Collection<? extends Double> c) {
      this(c.size());
      this.addAll(c);
   }

   public DoubleArraySet(DoubleSet c) {
      this(c.size());
      int i = 0;

      for(DoubleIterator var3 = c.iterator(); var3.hasNext(); ++i) {
         double x = (Double)var3.next();
         this.a[i] = x;
      }

      this.size = i;
   }

   public DoubleArraySet(Set<? extends Double> c) {
      this(c.size());
      int i = 0;

      for(Iterator var3 = c.iterator(); var3.hasNext(); ++i) {
         Double x = (Double)var3.next();
         this.a[i] = x;
      }

      this.size = i;
   }

   public DoubleArraySet(double[] a, int size) {
      this.a = a;
      this.size = size;
      if (size > a.length) {
         throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the array size (" + a.length + ")");
      }
   }

   public static DoubleArraySet of() {
      return ofUnchecked();
   }

   public static DoubleArraySet of(double e) {
      return ofUnchecked(e);
   }

   public static DoubleArraySet of(double... a) {
      if (a.length == 2) {
         if (Double.doubleToLongBits(a[0]) == Double.doubleToLongBits(a[1])) {
            throw new IllegalArgumentException("Duplicate element: " + a[1]);
         }
      } else if (a.length > 2) {
         DoubleOpenHashSet.of(a);
      }

      return ofUnchecked(a);
   }

   public static DoubleArraySet ofUnchecked() {
      return new DoubleArraySet();
   }

   public static DoubleArraySet ofUnchecked(double... a) {
      return new DoubleArraySet(a);
   }

   private int findKey(double o) {
      double[] a = this.a;
      int i = this.size;

      do {
         if (i-- == 0) {
            return -1;
         }
      } while(Double.doubleToLongBits(a[i]) != Double.doubleToLongBits(o));

      return i;
   }

   public DoubleIterator iterator() {
      return new DoubleIterator() {
         int curr = -1;
         int next = 0;

         public boolean hasNext() {
            return this.next < DoubleArraySet.this.size;
         }

         public double nextDouble() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return DoubleArraySet.this.a[this.curr = this.next++];
            }
         }

         public void remove() {
            if (this.curr == -1) {
               throw new IllegalStateException();
            } else {
               this.curr = -1;
               int tail = DoubleArraySet.this.size-- - this.next--;
               System.arraycopy(DoubleArraySet.this.a, this.next + 1, DoubleArraySet.this.a, this.next, tail);
            }
         }

         public int skip(int n) {
            if (n < 0) {
               throw new IllegalArgumentException("Argument must be nonnegative: " + n);
            } else {
               n = Math.min(n, DoubleArraySet.this.size - this.next);
               this.next += n;
               if (n != 0) {
                  this.curr = this.next - 1;
               }

               return n;
            }
         }

         public void forEachRemaining(java.util.function.DoubleConsumer action) {
            double[] a = DoubleArraySet.this.a;

            while(this.next < DoubleArraySet.this.size) {
               action.accept(a[this.next++]);
            }

         }
      };
   }

   public DoubleSpliterator spliterator() {
      return new DoubleArraySet.Spliterator();
   }

   public boolean contains(double k) {
      return this.findKey(k) != -1;
   }

   public int size() {
      return this.size;
   }

   public boolean remove(double k) {
      int pos = this.findKey(k);
      if (pos == -1) {
         return false;
      } else {
         int tail = this.size - pos - 1;

         for(int i = 0; i < tail; ++i) {
            this.a[pos + i] = this.a[pos + i + 1];
         }

         --this.size;
         return true;
      }
   }

   public boolean add(double k) {
      int pos = this.findKey(k);
      if (pos != -1) {
         return false;
      } else {
         if (this.size == this.a.length) {
            double[] b = new double[this.size == 0 ? 2 : this.size * 2];

            for(int i = this.size; i-- != 0; b[i] = this.a[i]) {
            }

            this.a = b;
         }

         this.a[this.size++] = k;
         return true;
      }
   }

   public void clear() {
      this.size = 0;
   }

   public boolean isEmpty() {
      return this.size == 0;
   }

   public double[] toDoubleArray() {
      return this.size == 0 ? DoubleArrays.EMPTY_ARRAY : Arrays.copyOf(this.a, this.size);
   }

   public double[] toArray(double[] a) {
      if (a == null || a.length < this.size) {
         a = new double[this.size];
      }

      System.arraycopy(this.a, 0, a, 0, this.size);
      return a;
   }

   public DoubleArraySet clone() {
      DoubleArraySet c;
      try {
         c = (DoubleArraySet)super.clone();
      } catch (CloneNotSupportedException var3) {
         throw new InternalError();
      }

      c.a = (double[])this.a.clone();
      return c;
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();
      double[] a = this.a;

      for(int i = 0; i < this.size; ++i) {
         s.writeDouble(a[i]);
      }

   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      double[] a = this.a = new double[this.size];

      for(int i = 0; i < this.size; ++i) {
         a[i] = s.readDouble();
      }

   }

   private final class Spliterator implements DoubleSpliterator {
      boolean hasSplit;
      int pos;
      int max;

      public Spliterator() {
         this(0, DoubleArraySet.this.size, false);
      }

      private Spliterator(int param2, int param3, boolean param4) {
         this.hasSplit = false;

         assert pos <= max : "pos " + pos + " must be <= max " + max;

         this.pos = pos;
         this.max = max;
         this.hasSplit = hasSplit;
      }

      private int getWorkingMax() {
         return this.hasSplit ? this.max : DoubleArraySet.this.size;
      }

      public int characteristics() {
         return 16721;
      }

      public long estimateSize() {
         return (long)(this.getWorkingMax() - this.pos);
      }

      public boolean tryAdvance(java.util.function.DoubleConsumer action) {
         if (this.pos >= this.getWorkingMax()) {
            return false;
         } else {
            action.accept(DoubleArraySet.this.a[this.pos++]);
            return true;
         }
      }

      public void forEachRemaining(java.util.function.DoubleConsumer action) {
         double[] a = DoubleArraySet.this.a;

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

      public DoubleSpliterator trySplit() {
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
            return DoubleArraySet.this.new Spliterator(oldPos, myNewPos, true);
         }
      }
   }
}
