package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.math.IntMath;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class CartesianList<E> extends AbstractList<List<E>> implements RandomAccess {
   private final transient ImmutableList<List<E>> axes;
   private final transient int[] axesSizeProduct;

   static <E> List<List<E>> create(List<? extends List<? extends E>> lists) {
      ImmutableList.Builder<List<E>> axesBuilder = new ImmutableList.Builder(lists.size());
      Iterator var2 = lists.iterator();

      while(var2.hasNext()) {
         List<? extends E> list = (List)var2.next();
         List<E> copy = ImmutableList.copyOf((Collection)list);
         if (copy.isEmpty()) {
            return ImmutableList.of();
         }

         axesBuilder.add((Object)copy);
      }

      return new CartesianList(axesBuilder.build());
   }

   CartesianList(ImmutableList<List<E>> axes) {
      this.axes = axes;
      int[] axesSizeProduct = new int[axes.size() + 1];
      axesSizeProduct[axes.size()] = 1;

      try {
         for(int i = axes.size() - 1; i >= 0; --i) {
            axesSizeProduct[i] = IntMath.checkedMultiply(axesSizeProduct[i + 1], ((List)axes.get(i)).size());
         }
      } catch (ArithmeticException var4) {
         throw new IllegalArgumentException("Cartesian product too large; must have size at most Integer.MAX_VALUE");
      }

      this.axesSizeProduct = axesSizeProduct;
   }

   private int getAxisIndexForProductIndex(int index, int axis) {
      return index / this.axesSizeProduct[axis + 1] % ((List)this.axes.get(axis)).size();
   }

   public int indexOf(@CheckForNull Object o) {
      if (!(o instanceof List)) {
         return -1;
      } else {
         List<?> list = (List)o;
         if (list.size() != this.axes.size()) {
            return -1;
         } else {
            ListIterator<?> itr = list.listIterator();

            int computedIndex;
            int axisIndex;
            int elemIndex;
            for(computedIndex = 0; itr.hasNext(); computedIndex += elemIndex * this.axesSizeProduct[axisIndex + 1]) {
               axisIndex = itr.nextIndex();
               elemIndex = ((List)this.axes.get(axisIndex)).indexOf(itr.next());
               if (elemIndex == -1) {
                  return -1;
               }
            }

            return computedIndex;
         }
      }
   }

   public int lastIndexOf(@CheckForNull Object o) {
      if (!(o instanceof List)) {
         return -1;
      } else {
         List<?> list = (List)o;
         if (list.size() != this.axes.size()) {
            return -1;
         } else {
            ListIterator<?> itr = list.listIterator();

            int computedIndex;
            int axisIndex;
            int elemIndex;
            for(computedIndex = 0; itr.hasNext(); computedIndex += elemIndex * this.axesSizeProduct[axisIndex + 1]) {
               axisIndex = itr.nextIndex();
               elemIndex = ((List)this.axes.get(axisIndex)).lastIndexOf(itr.next());
               if (elemIndex == -1) {
                  return -1;
               }
            }

            return computedIndex;
         }
      }
   }

   public ImmutableList<E> get(final int index) {
      Preconditions.checkElementIndex(index, this.size());
      return new ImmutableList<E>() {
         public int size() {
            return CartesianList.this.axes.size();
         }

         public E get(int axis) {
            Preconditions.checkElementIndex(axis, this.size());
            int axisIndex = CartesianList.this.getAxisIndexForProductIndex(index, axis);
            return ((List)CartesianList.this.axes.get(axis)).get(axisIndex);
         }

         boolean isPartialView() {
            return true;
         }
      };
   }

   public int size() {
      return this.axesSizeProduct[0];
   }

   public boolean contains(@CheckForNull Object object) {
      if (!(object instanceof List)) {
         return false;
      } else {
         List<?> list = (List)object;
         if (list.size() != this.axes.size()) {
            return false;
         } else {
            int i = 0;

            for(Iterator var4 = list.iterator(); var4.hasNext(); ++i) {
               Object o = var4.next();
               if (!((List)this.axes.get(i)).contains(o)) {
                  return false;
               }
            }

            return true;
         }
      }
   }
}
