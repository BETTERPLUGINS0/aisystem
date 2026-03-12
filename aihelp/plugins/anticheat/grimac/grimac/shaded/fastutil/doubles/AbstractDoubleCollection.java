package ac.grim.grimac.shaded.fastutil.doubles;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractDoubleCollection extends AbstractCollection<Double> implements DoubleCollection {
   protected AbstractDoubleCollection() {
   }

   public abstract DoubleIterator iterator();

   public boolean add(double k) {
      throw new UnsupportedOperationException();
   }

   public boolean contains(double k) {
      DoubleIterator iterator = this.iterator();

      do {
         if (!iterator.hasNext()) {
            return false;
         }
      } while(k != iterator.nextDouble());

      return true;
   }

   public boolean rem(double k) {
      DoubleIterator iterator = this.iterator();

      do {
         if (!iterator.hasNext()) {
            return false;
         }
      } while(k != iterator.nextDouble());

      iterator.remove();
      return true;
   }

   /** @deprecated */
   @Deprecated
   public boolean add(Double key) {
      return DoubleCollection.super.add(key);
   }

   /** @deprecated */
   @Deprecated
   public boolean contains(Object key) {
      return DoubleCollection.super.contains(key);
   }

   /** @deprecated */
   @Deprecated
   public boolean remove(Object key) {
      return DoubleCollection.super.remove(key);
   }

   public double[] toArray(double[] a) {
      int size = this.size();
      if (a == null) {
         a = new double[size];
      } else if (a.length < size) {
         a = Arrays.copyOf(a, size);
      }

      DoubleIterators.unwrap(this.iterator(), a);
      return a;
   }

   public double[] toDoubleArray() {
      int size = this.size();
      if (size == 0) {
         return DoubleArrays.EMPTY_ARRAY;
      } else {
         double[] a = new double[size];
         DoubleIterators.unwrap(this.iterator(), a);
         return a;
      }
   }

   /** @deprecated */
   @Deprecated
   public double[] toDoubleArray(double[] a) {
      return this.toArray(a);
   }

   public final void forEach(DoubleConsumer action) {
      DoubleCollection.super.forEach(action);
   }

   public final boolean removeIf(DoublePredicate filter) {
      return DoubleCollection.super.removeIf(filter);
   }

   public boolean addAll(DoubleCollection c) {
      boolean retVal = false;
      DoubleIterator i = c.iterator();

      while(i.hasNext()) {
         if (this.add(i.nextDouble())) {
            retVal = true;
         }
      }

      return retVal;
   }

   public boolean addAll(Collection<? extends Double> c) {
      return c instanceof DoubleCollection ? this.addAll((DoubleCollection)c) : super.addAll(c);
   }

   public boolean containsAll(DoubleCollection c) {
      DoubleIterator i = c.iterator();

      do {
         if (!i.hasNext()) {
            return true;
         }
      } while(this.contains(i.nextDouble()));

      return false;
   }

   public boolean containsAll(Collection<?> c) {
      return c instanceof DoubleCollection ? this.containsAll((DoubleCollection)c) : super.containsAll(c);
   }

   public boolean removeAll(DoubleCollection c) {
      boolean retVal = false;
      DoubleIterator i = c.iterator();

      while(i.hasNext()) {
         if (this.rem(i.nextDouble())) {
            retVal = true;
         }
      }

      return retVal;
   }

   public boolean removeAll(Collection<?> c) {
      return c instanceof DoubleCollection ? this.removeAll((DoubleCollection)c) : super.removeAll(c);
   }

   public boolean retainAll(DoubleCollection c) {
      boolean retVal = false;
      DoubleIterator i = this.iterator();

      while(i.hasNext()) {
         if (!c.contains(i.nextDouble())) {
            i.remove();
            retVal = true;
         }
      }

      return retVal;
   }

   public boolean retainAll(Collection<?> c) {
      return c instanceof DoubleCollection ? this.retainAll((DoubleCollection)c) : super.retainAll(c);
   }

   public String toString() {
      StringBuilder s = new StringBuilder();
      DoubleIterator i = this.iterator();
      int n = this.size();
      boolean first = true;
      s.append("{");

      while(n-- != 0) {
         if (first) {
            first = false;
         } else {
            s.append(", ");
         }

         double k = i.nextDouble();
         s.append(String.valueOf(k));
      }

      s.append("}");
      return s.toString();
   }
}
