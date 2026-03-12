package ac.grim.grimac.shaded.fastutil.floats;

import ac.grim.grimac.shaded.fastutil.doubles.DoubleIterator;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleIterators;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleSpliterator;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleSpliterators;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class FloatSets {
   static final int ARRAY_SET_CUTOFF = 4;
   public static final FloatSets.EmptySet EMPTY_SET = new FloatSets.EmptySet();
   static final FloatSet UNMODIFIABLE_EMPTY_SET;

   private FloatSets() {
   }

   public static FloatSet emptySet() {
      return EMPTY_SET;
   }

   public static FloatSet singleton(float element) {
      return new FloatSets.Singleton(element);
   }

   public static FloatSet singleton(Float element) {
      return new FloatSets.Singleton(element);
   }

   public static FloatSet synchronize(FloatSet s) {
      return new FloatSets.SynchronizedSet(s);
   }

   public static FloatSet synchronize(FloatSet s, Object sync) {
      return new FloatSets.SynchronizedSet(s, sync);
   }

   public static FloatSet unmodifiable(FloatSet s) {
      return new FloatSets.UnmodifiableSet(s);
   }

   static {
      UNMODIFIABLE_EMPTY_SET = unmodifiable(new FloatArraySet(FloatArrays.EMPTY_ARRAY));
   }

   public static class EmptySet extends FloatCollections.EmptyCollection implements FloatSet, Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptySet() {
      }

      public boolean remove(float ok) {
         throw new UnsupportedOperationException();
      }

      public Object clone() {
         return FloatSets.EMPTY_SET;
      }

      public boolean equals(Object o) {
         return o instanceof Set && ((Set)o).isEmpty();
      }

      /** @deprecated */
      @Deprecated
      public boolean rem(float k) {
         return super.rem(k);
      }

      private Object readResolve() {
         return FloatSets.EMPTY_SET;
      }
   }

   public static class Singleton extends AbstractFloatSet implements Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final float element;

      protected Singleton(float element) {
         this.element = element;
      }

      public boolean contains(float k) {
         return Float.floatToIntBits(k) == Float.floatToIntBits(this.element);
      }

      public boolean remove(float k) {
         throw new UnsupportedOperationException();
      }

      public FloatListIterator iterator() {
         return FloatIterators.singleton(this.element);
      }

      public FloatSpliterator spliterator() {
         return FloatSpliterators.singleton(this.element);
      }

      public int size() {
         return 1;
      }

      public float[] toFloatArray() {
         return new float[]{this.element};
      }

      /** @deprecated */
      @Deprecated
      public void forEach(Consumer<? super Float> action) {
         action.accept(this.element);
      }

      public boolean addAll(Collection<? extends Float> c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection<?> c) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public boolean removeIf(Predicate<? super Float> filter) {
         throw new UnsupportedOperationException();
      }

      public void forEach(FloatConsumer action) {
         action.accept(this.element);
      }

      public boolean addAll(FloatCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(FloatCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(FloatCollection c) {
         throw new UnsupportedOperationException();
      }

      public boolean removeIf(FloatPredicate filter) {
         throw new UnsupportedOperationException();
      }

      public DoubleIterator doubleIterator() {
         return DoubleIterators.singleton((double)this.element);
      }

      public DoubleSpliterator doubleSpliterator() {
         return DoubleSpliterators.singleton((double)this.element);
      }

      /** @deprecated */
      @Deprecated
      public Object[] toArray() {
         return new Object[]{this.element};
      }

      public Object clone() {
         return this;
      }
   }

   public static class SynchronizedSet extends FloatCollections.SynchronizedCollection implements FloatSet, Serializable {
      private static final long serialVersionUID = -7046029254386353129L;

      protected SynchronizedSet(FloatSet s, Object sync) {
         super(s, sync);
      }

      protected SynchronizedSet(FloatSet s) {
         super(s);
      }

      public boolean remove(float k) {
         synchronized(this.sync) {
            return this.collection.rem(k);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean rem(float k) {
         return super.rem(k);
      }
   }

   public static class UnmodifiableSet extends FloatCollections.UnmodifiableCollection implements FloatSet, Serializable {
      private static final long serialVersionUID = -7046029254386353129L;

      protected UnmodifiableSet(FloatSet s) {
         super(s);
      }

      public boolean remove(float k) {
         throw new UnsupportedOperationException();
      }

      public boolean equals(Object o) {
         return o == this ? true : this.collection.equals(o);
      }

      public int hashCode() {
         return this.collection.hashCode();
      }

      /** @deprecated */
      @Deprecated
      public boolean rem(float k) {
         return super.rem(k);
      }
   }
}
