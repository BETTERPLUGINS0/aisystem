package ac.grim.grimac.shaded.fastutil.floats;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractFloatCollection extends AbstractCollection<Float> implements FloatCollection {
   protected AbstractFloatCollection() {
   }

   public abstract FloatIterator iterator();

   public boolean add(float k) {
      throw new UnsupportedOperationException();
   }

   public boolean contains(float k) {
      FloatIterator iterator = this.iterator();

      do {
         if (!iterator.hasNext()) {
            return false;
         }
      } while(k != iterator.nextFloat());

      return true;
   }

   public boolean rem(float k) {
      FloatIterator iterator = this.iterator();

      do {
         if (!iterator.hasNext()) {
            return false;
         }
      } while(k != iterator.nextFloat());

      iterator.remove();
      return true;
   }

   /** @deprecated */
   @Deprecated
   public boolean add(Float key) {
      return FloatCollection.super.add(key);
   }

   /** @deprecated */
   @Deprecated
   public boolean contains(Object key) {
      return FloatCollection.super.contains(key);
   }

   /** @deprecated */
   @Deprecated
   public boolean remove(Object key) {
      return FloatCollection.super.remove(key);
   }

   public float[] toArray(float[] a) {
      int size = this.size();
      if (a == null) {
         a = new float[size];
      } else if (a.length < size) {
         a = Arrays.copyOf(a, size);
      }

      FloatIterators.unwrap(this.iterator(), a);
      return a;
   }

   public float[] toFloatArray() {
      int size = this.size();
      if (size == 0) {
         return FloatArrays.EMPTY_ARRAY;
      } else {
         float[] a = new float[size];
         FloatIterators.unwrap(this.iterator(), a);
         return a;
      }
   }

   /** @deprecated */
   @Deprecated
   public float[] toFloatArray(float[] a) {
      return this.toArray(a);
   }

   public boolean addAll(FloatCollection c) {
      boolean retVal = false;
      FloatIterator i = c.iterator();

      while(i.hasNext()) {
         if (this.add(i.nextFloat())) {
            retVal = true;
         }
      }

      return retVal;
   }

   public boolean addAll(Collection<? extends Float> c) {
      return c instanceof FloatCollection ? this.addAll((FloatCollection)c) : super.addAll(c);
   }

   public boolean containsAll(FloatCollection c) {
      FloatIterator i = c.iterator();

      do {
         if (!i.hasNext()) {
            return true;
         }
      } while(this.contains(i.nextFloat()));

      return false;
   }

   public boolean containsAll(Collection<?> c) {
      return c instanceof FloatCollection ? this.containsAll((FloatCollection)c) : super.containsAll(c);
   }

   public boolean removeAll(FloatCollection c) {
      boolean retVal = false;
      FloatIterator i = c.iterator();

      while(i.hasNext()) {
         if (this.rem(i.nextFloat())) {
            retVal = true;
         }
      }

      return retVal;
   }

   public boolean removeAll(Collection<?> c) {
      return c instanceof FloatCollection ? this.removeAll((FloatCollection)c) : super.removeAll(c);
   }

   public boolean retainAll(FloatCollection c) {
      boolean retVal = false;
      FloatIterator i = this.iterator();

      while(i.hasNext()) {
         if (!c.contains(i.nextFloat())) {
            i.remove();
            retVal = true;
         }
      }

      return retVal;
   }

   public boolean retainAll(Collection<?> c) {
      return c instanceof FloatCollection ? this.retainAll((FloatCollection)c) : super.retainAll(c);
   }

   public String toString() {
      StringBuilder s = new StringBuilder();
      FloatIterator i = this.iterator();
      int n = this.size();
      boolean first = true;
      s.append("{");

      while(n-- != 0) {
         if (first) {
            first = false;
         } else {
            s.append(", ");
         }

         float k = i.nextFloat();
         s.append(String.valueOf(k));
      }

      s.append("}");
      return s.toString();
   }
}
