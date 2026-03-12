package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

@ElementTypesAreNonnullByDefault
@GwtCompatible
@Beta
final class SortedLists {
   private SortedLists() {
   }

   public static <E extends Comparable> int binarySearch(List<? extends E> list, E e, SortedLists.KeyPresentBehavior presentBehavior, SortedLists.KeyAbsentBehavior absentBehavior) {
      Preconditions.checkNotNull(e);
      return binarySearch(list, (Object)e, (Comparator)Ordering.natural(), presentBehavior, absentBehavior);
   }

   public static <E, K extends Comparable> int binarySearch(List<E> list, Function<? super E, K> keyFunction, K key, SortedLists.KeyPresentBehavior presentBehavior, SortedLists.KeyAbsentBehavior absentBehavior) {
      Preconditions.checkNotNull(key);
      return binarySearch(list, keyFunction, key, Ordering.natural(), presentBehavior, absentBehavior);
   }

   public static <E, K> int binarySearch(List<E> list, Function<? super E, K> keyFunction, @ParametricNullness K key, Comparator<? super K> keyComparator, SortedLists.KeyPresentBehavior presentBehavior, SortedLists.KeyAbsentBehavior absentBehavior) {
      return binarySearch(Lists.transform(list, keyFunction), key, keyComparator, presentBehavior, absentBehavior);
   }

   public static <E> int binarySearch(List<? extends E> list, @ParametricNullness E key, Comparator<? super E> comparator, SortedLists.KeyPresentBehavior presentBehavior, SortedLists.KeyAbsentBehavior absentBehavior) {
      Preconditions.checkNotNull(comparator);
      Preconditions.checkNotNull(list);
      Preconditions.checkNotNull(presentBehavior);
      Preconditions.checkNotNull(absentBehavior);
      if (!(list instanceof RandomAccess)) {
         list = Lists.newArrayList((Iterable)list);
      }

      int lower = 0;
      int upper = ((List)list).size() - 1;

      while(lower <= upper) {
         int middle = lower + upper >>> 1;
         int c = comparator.compare(key, ((List)list).get(middle));
         if (c < 0) {
            upper = middle - 1;
         } else {
            if (c <= 0) {
               return lower + presentBehavior.resultIndex(comparator, key, ((List)list).subList(lower, upper + 1), middle - lower);
            }

            lower = middle + 1;
         }
      }

      return absentBehavior.resultIndex(lower);
   }

   static enum KeyAbsentBehavior {
      NEXT_LOWER {
         int resultIndex(int higherIndex) {
            return higherIndex - 1;
         }
      },
      NEXT_HIGHER {
         public int resultIndex(int higherIndex) {
            return higherIndex;
         }
      },
      INVERTED_INSERTION_INDEX {
         public int resultIndex(int higherIndex) {
            return ~higherIndex;
         }
      };

      private KeyAbsentBehavior() {
      }

      abstract int resultIndex(int var1);

      // $FF: synthetic method
      private static SortedLists.KeyAbsentBehavior[] $values() {
         return new SortedLists.KeyAbsentBehavior[]{NEXT_LOWER, NEXT_HIGHER, INVERTED_INSERTION_INDEX};
      }

      // $FF: synthetic method
      KeyAbsentBehavior(Object x2) {
         this();
      }
   }

   static enum KeyPresentBehavior {
      ANY_PRESENT {
         <E> int resultIndex(Comparator<? super E> comparator, @ParametricNullness E key, List<? extends E> list, int foundIndex) {
            return foundIndex;
         }
      },
      LAST_PRESENT {
         <E> int resultIndex(Comparator<? super E> comparator, @ParametricNullness E key, List<? extends E> list, int foundIndex) {
            int lower = foundIndex;
            int upper = list.size() - 1;

            while(lower < upper) {
               int middle = lower + upper + 1 >>> 1;
               int c = comparator.compare(list.get(middle), key);
               if (c > 0) {
                  upper = middle - 1;
               } else {
                  lower = middle;
               }
            }

            return lower;
         }
      },
      FIRST_PRESENT {
         <E> int resultIndex(Comparator<? super E> comparator, @ParametricNullness E key, List<? extends E> list, int foundIndex) {
            int lower = 0;
            int upper = foundIndex;

            while(lower < upper) {
               int middle = lower + upper >>> 1;
               int c = comparator.compare(list.get(middle), key);
               if (c < 0) {
                  lower = middle + 1;
               } else {
                  upper = middle;
               }
            }

            return lower;
         }
      },
      FIRST_AFTER {
         public <E> int resultIndex(Comparator<? super E> comparator, @ParametricNullness E key, List<? extends E> list, int foundIndex) {
            return LAST_PRESENT.resultIndex(comparator, key, list, foundIndex) + 1;
         }
      },
      LAST_BEFORE {
         public <E> int resultIndex(Comparator<? super E> comparator, @ParametricNullness E key, List<? extends E> list, int foundIndex) {
            return FIRST_PRESENT.resultIndex(comparator, key, list, foundIndex) - 1;
         }
      };

      private KeyPresentBehavior() {
      }

      abstract <E> int resultIndex(Comparator<? super E> var1, @ParametricNullness E var2, List<? extends E> var3, int var4);

      // $FF: synthetic method
      private static SortedLists.KeyPresentBehavior[] $values() {
         return new SortedLists.KeyPresentBehavior[]{ANY_PRESENT, LAST_PRESENT, FIRST_PRESENT, FIRST_AFTER, LAST_BEFORE};
      }

      // $FF: synthetic method
      KeyPresentBehavior(Object x2) {
         this();
      }
   }
}
