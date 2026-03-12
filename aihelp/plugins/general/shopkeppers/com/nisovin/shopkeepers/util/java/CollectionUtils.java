package com.nisovin.shopkeepers.util.java;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class CollectionUtils {
   public static <E> boolean replace(List<E> list, E element, E replacement) {
      if (list instanceof RandomAccess) {
         int index = list.indexOf(Unsafe.nullableAsNonNull(element));
         if (index != -1) {
            list.set(index, replacement);
            return true;
         } else {
            return false;
         }
      } else {
         Validate.notNull(list, (String)"list is null");
         ListIterator iterator = list.listIterator();

         Object next;
         do {
            if (!iterator.hasNext()) {
               return false;
            }

            next = iterator.next();
         } while(!Objects.equals(element, next));

         iterator.set(replacement);
         return true;
      }
   }

   @SafeVarargs
   public static <E> List<E> asList(@Nullable E... array) {
      return array == null ? Collections.emptyList() : Arrays.asList(array);
   }

   public static <E, L extends List<? extends E>> L sort(L list, Comparator<? super E> comparator) {
      Validate.notNull(list, (String)"list is null");
      Validate.notNull(comparator, (String)"comparator is null");
      list.sort((Comparator)Unsafe.castNonNull(comparator));
      return list;
   }

   public static <E, C extends Collection<? super E>> C addAll(C collection, Collection<? extends E> toAdd) {
      Validate.notNull(collection, (String)"collection is null");
      Validate.notNull(toAdd, (String)"toAdd is null");
      collection.addAll((Collection)Unsafe.castNonNull(toAdd));
      return collection;
   }

   @Nullable
   public static <E> E findFirst(Iterable<? extends E> iterable, Predicate<? super E> predicate) {
      Validate.notNull(predicate, (String)"predicate is null");
      Iterator var2 = iterable.iterator();

      Object element;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         element = var2.next();
      } while(!predicate.test(element));

      return element;
   }

   public static boolean containsNull(Collection<?> collection) {
      try {
         return collection.contains(Unsafe.uncheckedNull());
      } catch (NullPointerException | ClassCastException var2) {
         return false;
      }
   }

   public static <E> boolean contains(Iterable<? extends E> iterable, Predicate<? super E> predicate) {
      Validate.notNull(predicate, (String)"predicate is null");
      Iterator var2 = iterable.iterator();

      Object element;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         element = var2.next();
      } while(!predicate.test(element));

      return true;
   }

   public static <E> List<E> copyAndAdd(Collection<? extends E> collection, E toAdd) {
      Validate.notNull(collection, (String)"collection is null");
      List<E> newList = new ArrayList(collection.size() + 1);
      newList.addAll(collection);
      newList.add(toAdd);
      return newList;
   }

   public static <E> List<E> unmodifiableCopyAndAdd(Collection<? extends E> collection, E toAdd) {
      List<E> newList = copyAndAdd(collection, toAdd);
      return Collections.unmodifiableList(newList);
   }

   public static <E> List<E> copyAndAddAll(Collection<? extends E> collection, Collection<? extends E> toAdd) {
      Validate.notNull(collection, (String)"collection is null");
      Validate.notNull(toAdd, (String)"toAdd is null");
      List<E> newList = new ArrayList(collection.size() + toAdd.size());
      newList.addAll(collection);
      newList.addAll(toAdd);
      return newList;
   }

   public static <E> List<E> unmodifiableCopyAndAddAll(Collection<? extends E> collection, Collection<? extends E> toAdd) {
      List<E> newList = copyAndAddAll(collection, toAdd);
      return Collections.unmodifiableList(newList);
   }

   @SafeVarargs
   public static <T> T[] concat(T[] array1, T... array2) {
      if (array1 == null) {
         return array2;
      } else if (array2 == null) {
         return array1;
      } else {
         int length1 = array1.length;
         int length2 = array2.length;
         T[] result = (Object[])Unsafe.assertNonNull(Arrays.copyOf(array1, length1 + length2));
         System.arraycopy(array2, 0, result, length1, length2);
         return result;
      }
   }

   @SafeVarargs
   public static <T> T[] concat(T[]... arrays) {
      if (arrays != null && arrays.length != 0) {
         int length = 0;
         Object[][] var2 = arrays;
         int resultLength = arrays.length;

         for(int var4 = 0; var4 < resultLength; ++var4) {
            T[] array = var2[var4];
            Validate.notNull(array, (String)"array element is null");
            length += ((Object[])Unsafe.assertNonNull(array)).length;
         }

         T[] result = (Object[])Unsafe.assertNonNull(Arrays.copyOf(arrays[0], length));
         resultLength = 0;
         boolean first = true;
         Object[][] var12 = arrays;
         int var6 = arrays.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            T[] array = var12[var7];
            T[] nonNullArray = (Object[])Unsafe.assertNonNull(array);
            resultLength += nonNullArray.length;
            if (first) {
               first = false;
            } else {
               System.arraycopy(nonNullArray, 0, result, resultLength, nonNullArray.length);
            }
         }

         return result;
      } else {
         return (Object[])Unsafe.castNonNull(new Object[0]);
      }
   }

   public static <T> Stream<T> stream(Iterable<T> iterable) {
      if (iterable instanceof Collection) {
         Collection<T> collection = (Collection)iterable;
         return collection.stream();
      } else {
         return StreamSupport.stream(iterable.spliterator(), false);
      }
   }

   public static <T> Iterable<T> toIterable(Stream<T> stream) {
      Objects.requireNonNull(stream);
      return stream::iterator;
   }

   public static <T> T getFirstOrNull(Stream<? extends T> stream) {
      return stream.findFirst().orElse((Object)null);
   }

   @NonNull
   public static <T> T cycleValue(List<T> values, @NonNull T current, boolean backwards) {
      return cycleValue(values, current, backwards, PredicateUtils.alwaysTrue());
   }

   @NonNull
   public static <T> T cycleValue(List<T> values, @NonNull T current, boolean backwards, Predicate<? super T> predicate) {
      return cycleValue(values, false, current, backwards, predicate);
   }

   public static <T> T cycleValueNullable(List<T> values, T current, boolean backwards) {
      return cycleValueNullable(values, current, backwards, PredicateUtils.alwaysTrue());
   }

   @Nullable
   public static <T> T cycleValueNullable(List<T> values, @Nullable T current, boolean backwards, Predicate<? super T> predicate) {
      return cycleValue(values, true, current, backwards, predicate);
   }

   public static <T> T cycleValue(List<T> values, boolean nullable, T current, boolean backwards, Predicate<? super T> predicate) {
      Validate.notNull(values, (String)"values is null");
      Validate.isTrue(current != null || nullable, "Not nullable, but current is null");
      Validate.notNull(predicate, (String)"predicate is null");

      assert values != null;

      int currentId = current == null ? -1 : values.indexOf(current);
      int nextId = currentId;

      Object next;
      do {
         if (backwards) {
            --nextId;
            if (nextId < (nullable ? -1 : 0)) {
               nextId = values.size() - 1;
            }
         } else {
            ++nextId;
            if (nextId >= values.size()) {
               nextId = nullable ? -1 : 0;
            }
         }

         if (nextId == currentId) {
            return current;
         }

         next = nextId == -1 ? Unsafe.cast((Object)null) : values.get(nextId);
      } while(!predicate.test(next));

      return next;
   }

   private CollectionUtils() {
   }
}
