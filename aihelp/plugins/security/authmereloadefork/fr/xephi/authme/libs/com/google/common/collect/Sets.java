package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.base.Predicate;
import fr.xephi.authme.libs.com.google.common.base.Predicates;
import fr.xephi.authme.libs.com.google.common.math.IntMath;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Stream;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
public final class Sets {
   private Sets() {
   }

   @GwtCompatible(
      serializable = true
   )
   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E anElement, E... otherElements) {
      return ImmutableEnumSet.asImmutable(EnumSet.of(anElement, otherElements));
   }

   @GwtCompatible(
      serializable = true
   )
   public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements) {
      if (elements instanceof ImmutableEnumSet) {
         return (ImmutableEnumSet)elements;
      } else if (elements instanceof Collection) {
         Collection<E> collection = (Collection)elements;
         return collection.isEmpty() ? ImmutableSet.of() : ImmutableEnumSet.asImmutable(EnumSet.copyOf(collection));
      } else {
         Iterator<E> itr = elements.iterator();
         if (itr.hasNext()) {
            EnumSet<E> enumSet = EnumSet.of((Enum)itr.next());
            Iterators.addAll(enumSet, itr);
            return ImmutableEnumSet.asImmutable(enumSet);
         } else {
            return ImmutableSet.of();
         }
      }
   }

   public static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
      return CollectCollectors.toImmutableEnumSet();
   }

   public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
      EnumSet<E> set = EnumSet.noneOf(elementType);
      Iterables.addAll(set, iterable);
      return set;
   }

   public static <E> HashSet<E> newHashSet() {
      return new HashSet();
   }

   public static <E> HashSet<E> newHashSet(E... elements) {
      HashSet<E> set = newHashSetWithExpectedSize(elements.length);
      Collections.addAll(set, elements);
      return set;
   }

   public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
      return elements instanceof Collection ? new HashSet((Collection)elements) : newHashSet(elements.iterator());
   }

   public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
      HashSet<E> set = newHashSet();
      Iterators.addAll(set, elements);
      return set;
   }

   public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
      return new HashSet(Maps.capacity(expectedSize));
   }

   public static <E> Set<E> newConcurrentHashSet() {
      return Platform.newConcurrentHashSet();
   }

   public static <E> Set<E> newConcurrentHashSet(Iterable<? extends E> elements) {
      Set<E> set = newConcurrentHashSet();
      Iterables.addAll(set, elements);
      return set;
   }

   public static <E> LinkedHashSet<E> newLinkedHashSet() {
      return new LinkedHashSet();
   }

   public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
      if (elements instanceof Collection) {
         return new LinkedHashSet((Collection)elements);
      } else {
         LinkedHashSet<E> set = newLinkedHashSet();
         Iterables.addAll(set, elements);
         return set;
      }
   }

   public static <E> LinkedHashSet<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
      return new LinkedHashSet(Maps.capacity(expectedSize));
   }

   public static <E extends Comparable> TreeSet<E> newTreeSet() {
      return new TreeSet();
   }

   public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
      TreeSet<E> set = newTreeSet();
      Iterables.addAll(set, elements);
      return set;
   }

   public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
      return new TreeSet((Comparator)Preconditions.checkNotNull(comparator));
   }

   public static <E> Set<E> newIdentityHashSet() {
      return Collections.newSetFromMap(Maps.newIdentityHashMap());
   }

   @GwtIncompatible
   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
      return new CopyOnWriteArraySet();
   }

   @GwtIncompatible
   public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(Iterable<? extends E> elements) {
      Collection<? extends E> elementsCollection = elements instanceof Collection ? (Collection)elements : Lists.newArrayList(elements);
      return new CopyOnWriteArraySet((Collection)elementsCollection);
   }

   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection) {
      if (collection instanceof EnumSet) {
         return EnumSet.complementOf((EnumSet)collection);
      } else {
         Preconditions.checkArgument(!collection.isEmpty(), "collection is empty; use the other version of this method");
         Class<E> type = ((Enum)collection.iterator().next()).getDeclaringClass();
         return makeComplementByHand(collection, type);
      }
   }

   public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection, Class<E> type) {
      Preconditions.checkNotNull(collection);
      return collection instanceof EnumSet ? EnumSet.complementOf((EnumSet)collection) : makeComplementByHand(collection, type);
   }

   private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> collection, Class<E> type) {
      EnumSet<E> result = EnumSet.allOf(type);
      result.removeAll(collection);
      return result;
   }

   /** @deprecated */
   @Deprecated
   public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
      return Collections.newSetFromMap(map);
   }

   public static <E> Sets.SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
      Preconditions.checkNotNull(set1, "set1");
      Preconditions.checkNotNull(set2, "set2");
      return new Sets.SetView<E>() {
         public int size() {
            int size = set1.size();
            Iterator var2 = set2.iterator();

            while(var2.hasNext()) {
               E e = var2.next();
               if (!set1.contains(e)) {
                  ++size;
               }
            }

            return size;
         }

         public boolean isEmpty() {
            return set1.isEmpty() && set2.isEmpty();
         }

         public UnmodifiableIterator<E> iterator() {
            return new AbstractIterator<E>() {
               final Iterator<? extends E> itr1 = set1.iterator();
               final Iterator<? extends E> itr2 = set2.iterator();

               @CheckForNull
               protected E computeNext() {
                  if (this.itr1.hasNext()) {
                     return this.itr1.next();
                  } else {
                     Object e;
                     do {
                        if (!this.itr2.hasNext()) {
                           return this.endOfData();
                        }

                        e = this.itr2.next();
                     } while(set1.contains(e));

                     return e;
                  }
               }
            };
         }

         public Stream<E> stream() {
            return Stream.concat(set1.stream(), set2.stream().filter((e) -> {
               return !set1.contains(e);
            }));
         }

         public Stream<E> parallelStream() {
            return (Stream)this.stream().parallel();
         }

         public boolean contains(@CheckForNull Object object) {
            return set1.contains(object) || set2.contains(object);
         }

         public <S extends Set<E>> S copyInto(S set) {
            set.addAll(set1);
            set.addAll(set2);
            return set;
         }

         public ImmutableSet<E> immutableCopy() {
            return (new ImmutableSet.Builder()).addAll((Iterable)set1).addAll((Iterable)set2).build();
         }
      };
   }

   public static <E> Sets.SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
      Preconditions.checkNotNull(set1, "set1");
      Preconditions.checkNotNull(set2, "set2");
      return new Sets.SetView<E>() {
         public UnmodifiableIterator<E> iterator() {
            return new AbstractIterator<E>() {
               final Iterator<E> itr = set1.iterator();

               @CheckForNull
               protected E computeNext() {
                  while(true) {
                     if (this.itr.hasNext()) {
                        E e = this.itr.next();
                        if (!set2.contains(e)) {
                           continue;
                        }

                        return e;
                     }

                     return this.endOfData();
                  }
               }
            };
         }

         public Stream<E> stream() {
            Stream var10000 = set1.stream();
            Set var10001 = set2;
            Objects.requireNonNull(var10001);
            return var10000.filter(var10001::contains);
         }

         public Stream<E> parallelStream() {
            Stream var10000 = set1.parallelStream();
            Set var10001 = set2;
            Objects.requireNonNull(var10001);
            return var10000.filter(var10001::contains);
         }

         public int size() {
            int size = 0;
            Iterator var2 = set1.iterator();

            while(var2.hasNext()) {
               E e = var2.next();
               if (set2.contains(e)) {
                  ++size;
               }
            }

            return size;
         }

         public boolean isEmpty() {
            return Collections.disjoint(set2, set1);
         }

         public boolean contains(@CheckForNull Object object) {
            return set1.contains(object) && set2.contains(object);
         }

         public boolean containsAll(Collection<?> collection) {
            return set1.containsAll(collection) && set2.containsAll(collection);
         }
      };
   }

   public static <E> Sets.SetView<E> difference(final Set<E> set1, final Set<?> set2) {
      Preconditions.checkNotNull(set1, "set1");
      Preconditions.checkNotNull(set2, "set2");
      return new Sets.SetView<E>() {
         public UnmodifiableIterator<E> iterator() {
            return new AbstractIterator<E>() {
               final Iterator<E> itr = set1.iterator();

               @CheckForNull
               protected E computeNext() {
                  while(true) {
                     if (this.itr.hasNext()) {
                        E e = this.itr.next();
                        if (set2.contains(e)) {
                           continue;
                        }

                        return e;
                     }

                     return this.endOfData();
                  }
               }
            };
         }

         public Stream<E> stream() {
            return set1.stream().filter((e) -> {
               return !set2.contains(e);
            });
         }

         public Stream<E> parallelStream() {
            return set1.parallelStream().filter((e) -> {
               return !set2.contains(e);
            });
         }

         public int size() {
            int size = 0;
            Iterator var2 = set1.iterator();

            while(var2.hasNext()) {
               E e = var2.next();
               if (!set2.contains(e)) {
                  ++size;
               }
            }

            return size;
         }

         public boolean isEmpty() {
            return set2.containsAll(set1);
         }

         public boolean contains(@CheckForNull Object element) {
            return set1.contains(element) && !set2.contains(element);
         }
      };
   }

   public static <E> Sets.SetView<E> symmetricDifference(final Set<? extends E> set1, final Set<? extends E> set2) {
      Preconditions.checkNotNull(set1, "set1");
      Preconditions.checkNotNull(set2, "set2");
      return new Sets.SetView<E>() {
         public UnmodifiableIterator<E> iterator() {
            final Iterator<? extends E> itr1 = set1.iterator();
            final Iterator<? extends E> itr2 = set2.iterator();
            return new AbstractIterator<E>() {
               @CheckForNull
               public E computeNext() {
                  while(true) {
                     Object elem2;
                     if (itr1.hasNext()) {
                        elem2 = itr1.next();
                        if (set2.contains(elem2)) {
                           continue;
                        }

                        return elem2;
                     }

                     do {
                        if (!itr2.hasNext()) {
                           return this.endOfData();
                        }

                        elem2 = itr2.next();
                     } while(set1.contains(elem2));

                     return elem2;
                  }
               }
            };
         }

         public int size() {
            int size = 0;
            Iterator var2 = set1.iterator();

            Object e;
            while(var2.hasNext()) {
               e = var2.next();
               if (!set2.contains(e)) {
                  ++size;
               }
            }

            var2 = set2.iterator();

            while(var2.hasNext()) {
               e = var2.next();
               if (!set1.contains(e)) {
                  ++size;
               }
            }

            return size;
         }

         public boolean isEmpty() {
            return set1.equals(set2);
         }

         public boolean contains(@CheckForNull Object element) {
            return set1.contains(element) ^ set2.contains(element);
         }
      };
   }

   public static <E> Set<E> filter(Set<E> unfiltered, Predicate<? super E> predicate) {
      if (unfiltered instanceof SortedSet) {
         return filter((SortedSet)unfiltered, predicate);
      } else if (unfiltered instanceof Sets.FilteredSet) {
         Sets.FilteredSet<E> filtered = (Sets.FilteredSet)unfiltered;
         Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
         return new Sets.FilteredSet((Set)filtered.unfiltered, combinedPredicate);
      } else {
         return new Sets.FilteredSet((Set)Preconditions.checkNotNull(unfiltered), (Predicate)Preconditions.checkNotNull(predicate));
      }
   }

   public static <E> SortedSet<E> filter(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
      if (unfiltered instanceof Sets.FilteredSet) {
         Sets.FilteredSet<E> filtered = (Sets.FilteredSet)unfiltered;
         Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
         return new Sets.FilteredSortedSet((SortedSet)filtered.unfiltered, combinedPredicate);
      } else {
         return new Sets.FilteredSortedSet((SortedSet)Preconditions.checkNotNull(unfiltered), (Predicate)Preconditions.checkNotNull(predicate));
      }
   }

   @GwtIncompatible
   public static <E> NavigableSet<E> filter(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
      if (unfiltered instanceof Sets.FilteredSet) {
         Sets.FilteredSet<E> filtered = (Sets.FilteredSet)unfiltered;
         Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);
         return new Sets.FilteredNavigableSet((NavigableSet)filtered.unfiltered, combinedPredicate);
      } else {
         return new Sets.FilteredNavigableSet((NavigableSet)Preconditions.checkNotNull(unfiltered), (Predicate)Preconditions.checkNotNull(predicate));
      }
   }

   public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> sets) {
      return Sets.CartesianSet.create(sets);
   }

   @SafeVarargs
   public static <B> Set<List<B>> cartesianProduct(Set<? extends B>... sets) {
      return cartesianProduct(Arrays.asList(sets));
   }

   @GwtCompatible(
      serializable = false
   )
   public static <E> Set<Set<E>> powerSet(Set<E> set) {
      return new Sets.PowerSet(set);
   }

   @Beta
   public static <E> Set<Set<E>> combinations(Set<E> set, final int size) {
      final ImmutableMap<E, Integer> index = Maps.indexMap(set);
      CollectPreconditions.checkNonnegative(size, "size");
      Preconditions.checkArgument(size <= index.size(), "size (%s) must be <= set.size() (%s)", size, index.size());
      if (size == 0) {
         return ImmutableSet.of(ImmutableSet.of());
      } else {
         return (Set)(size == index.size() ? ImmutableSet.of(index.keySet()) : new AbstractSet<Set<E>>() {
            public boolean contains(@CheckForNull Object o) {
               if (!(o instanceof Set)) {
                  return false;
               } else {
                  Set<?> s = (Set)o;
                  return s.size() == size && index.keySet().containsAll(s);
               }
            }

            public Iterator<Set<E>> iterator() {
               return new AbstractIterator<Set<E>>() {
                  final BitSet bits = new BitSet(index.size());

                  @CheckForNull
                  protected Set<E> computeNext() {
                     if (this.bits.isEmpty()) {
                        this.bits.set(0, size);
                     } else {
                        int firstSetBit = this.bits.nextSetBit(0);
                        int bitToFlip = this.bits.nextClearBit(firstSetBit);
                        if (bitToFlip == index.size()) {
                           return (Set)this.endOfData();
                        }

                        this.bits.set(0, bitToFlip - firstSetBit - 1);
                        this.bits.clear(bitToFlip - firstSetBit - 1, bitToFlip);
                        this.bits.set(bitToFlip);
                     }

                     final BitSet copy = (BitSet)this.bits.clone();
                     return new AbstractSet<E>() {
                        public boolean contains(@CheckForNull Object o) {
                           Integer i = (Integer)index.get(o);
                           return i != null && copy.get(i);
                        }

                        public Iterator<E> iterator() {
                           return new AbstractIterator<E>() {
                              int i = -1;

                              @CheckForNull
                              protected E computeNext() {
                                 this.i = copy.nextSetBit(this.i + 1);
                                 return this.i == -1 ? this.endOfData() : index.keySet().asList().get(this.i);
                              }
                           };
                        }

                        public int size() {
                           return size;
                        }
                     };
                  }
               };
            }

            public int size() {
               return IntMath.binomial(index.size(), size);
            }

            public String toString() {
               String var1 = String.valueOf(index.keySet());
               int var2 = size;
               return (new StringBuilder(32 + String.valueOf(var1).length())).append("Sets.combinations(").append(var1).append(", ").append(var2).append(")").toString();
            }
         });
      }
   }

   static int hashCodeImpl(Set<?> s) {
      int hashCode = 0;

      for(Iterator var2 = s.iterator(); var2.hasNext(); hashCode = ~(~hashCode)) {
         Object o = var2.next();
         hashCode += o != null ? o.hashCode() : 0;
      }

      return hashCode;
   }

   static boolean equalsImpl(Set<?> s, @CheckForNull Object object) {
      if (s == object) {
         return true;
      } else if (object instanceof Set) {
         Set o = (Set)object;

         try {
            return s.size() == o.size() && s.containsAll(o);
         } catch (ClassCastException | NullPointerException var4) {
            return false;
         }
      } else {
         return false;
      }
   }

   public static <E> NavigableSet<E> unmodifiableNavigableSet(NavigableSet<E> set) {
      return (NavigableSet)(!(set instanceof ImmutableCollection) && !(set instanceof Sets.UnmodifiableNavigableSet) ? new Sets.UnmodifiableNavigableSet(set) : set);
   }

   @GwtIncompatible
   public static <E> NavigableSet<E> synchronizedNavigableSet(NavigableSet<E> navigableSet) {
      return Synchronized.navigableSet(navigableSet);
   }

   static boolean removeAllImpl(Set<?> set, Iterator<?> iterator) {
      boolean changed;
      for(changed = false; iterator.hasNext(); changed |= set.remove(iterator.next())) {
      }

      return changed;
   }

   static boolean removeAllImpl(Set<?> set, Collection<?> collection) {
      Preconditions.checkNotNull(collection);
      if (collection instanceof Multiset) {
         collection = ((Multiset)collection).elementSet();
      }

      return collection instanceof Set && ((Collection)collection).size() > set.size() ? Iterators.removeAll(set.iterator(), (Collection)collection) : removeAllImpl(set, ((Collection)collection).iterator());
   }

   @Beta
   @GwtIncompatible
   public static <K extends Comparable<? super K>> NavigableSet<K> subSet(NavigableSet<K> set, Range<K> range) {
      if (set.comparator() != null && set.comparator() != Ordering.natural() && range.hasLowerBound() && range.hasUpperBound()) {
         Preconditions.checkArgument(set.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0, "set is using a custom comparator which is inconsistent with the natural ordering.");
      }

      if (range.hasLowerBound() && range.hasUpperBound()) {
         return set.subSet(range.lowerEndpoint(), range.lowerBoundType() == BoundType.CLOSED, range.upperEndpoint(), range.upperBoundType() == BoundType.CLOSED);
      } else if (range.hasLowerBound()) {
         return set.tailSet(range.lowerEndpoint(), range.lowerBoundType() == BoundType.CLOSED);
      } else {
         return range.hasUpperBound() ? set.headSet(range.upperEndpoint(), range.upperBoundType() == BoundType.CLOSED) : (NavigableSet)Preconditions.checkNotNull(set);
      }
   }

   @GwtIncompatible
   static class DescendingSet<E> extends ForwardingNavigableSet<E> {
      private final NavigableSet<E> forward;

      DescendingSet(NavigableSet<E> forward) {
         this.forward = forward;
      }

      protected NavigableSet<E> delegate() {
         return this.forward;
      }

      @CheckForNull
      public E lower(@ParametricNullness E e) {
         return this.forward.higher(e);
      }

      @CheckForNull
      public E floor(@ParametricNullness E e) {
         return this.forward.ceiling(e);
      }

      @CheckForNull
      public E ceiling(@ParametricNullness E e) {
         return this.forward.floor(e);
      }

      @CheckForNull
      public E higher(@ParametricNullness E e) {
         return this.forward.lower(e);
      }

      @CheckForNull
      public E pollFirst() {
         return this.forward.pollLast();
      }

      @CheckForNull
      public E pollLast() {
         return this.forward.pollFirst();
      }

      public NavigableSet<E> descendingSet() {
         return this.forward;
      }

      public Iterator<E> descendingIterator() {
         return this.forward.iterator();
      }

      public NavigableSet<E> subSet(@ParametricNullness E fromElement, boolean fromInclusive, @ParametricNullness E toElement, boolean toInclusive) {
         return this.forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
      }

      public SortedSet<E> subSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
         return this.standardSubSet(fromElement, toElement);
      }

      public NavigableSet<E> headSet(@ParametricNullness E toElement, boolean inclusive) {
         return this.forward.tailSet(toElement, inclusive).descendingSet();
      }

      public SortedSet<E> headSet(@ParametricNullness E toElement) {
         return this.standardHeadSet(toElement);
      }

      public NavigableSet<E> tailSet(@ParametricNullness E fromElement, boolean inclusive) {
         return this.forward.headSet(fromElement, inclusive).descendingSet();
      }

      public SortedSet<E> tailSet(@ParametricNullness E fromElement) {
         return this.standardTailSet(fromElement);
      }

      public Comparator<? super E> comparator() {
         Comparator<? super E> forwardComparator = this.forward.comparator();
         return forwardComparator == null ? Ordering.natural().reverse() : reverse(forwardComparator);
      }

      private static <T> Ordering<T> reverse(Comparator<T> forward) {
         return Ordering.from(forward).reverse();
      }

      @ParametricNullness
      public E first() {
         return this.forward.last();
      }

      @ParametricNullness
      public E last() {
         return this.forward.first();
      }

      public Iterator<E> iterator() {
         return this.forward.descendingIterator();
      }

      public Object[] toArray() {
         return this.standardToArray();
      }

      public <T> T[] toArray(T[] array) {
         return this.standardToArray(array);
      }

      public String toString() {
         return this.standardToString();
      }
   }

   static final class UnmodifiableNavigableSet<E> extends ForwardingSortedSet<E> implements NavigableSet<E>, Serializable {
      private final NavigableSet<E> delegate;
      private final SortedSet<E> unmodifiableDelegate;
      @CheckForNull
      private transient Sets.UnmodifiableNavigableSet<E> descendingSet;
      private static final long serialVersionUID = 0L;

      UnmodifiableNavigableSet(NavigableSet<E> delegate) {
         this.delegate = (NavigableSet)Preconditions.checkNotNull(delegate);
         this.unmodifiableDelegate = Collections.unmodifiableSortedSet(delegate);
      }

      protected SortedSet<E> delegate() {
         return this.unmodifiableDelegate;
      }

      public boolean removeIf(java.util.function.Predicate<? super E> filter) {
         throw new UnsupportedOperationException();
      }

      public Stream<E> stream() {
         return this.delegate.stream();
      }

      public Stream<E> parallelStream() {
         return this.delegate.parallelStream();
      }

      public void forEach(Consumer<? super E> action) {
         this.delegate.forEach(action);
      }

      @CheckForNull
      public E lower(@ParametricNullness E e) {
         return this.delegate.lower(e);
      }

      @CheckForNull
      public E floor(@ParametricNullness E e) {
         return this.delegate.floor(e);
      }

      @CheckForNull
      public E ceiling(@ParametricNullness E e) {
         return this.delegate.ceiling(e);
      }

      @CheckForNull
      public E higher(@ParametricNullness E e) {
         return this.delegate.higher(e);
      }

      @CheckForNull
      public E pollFirst() {
         throw new UnsupportedOperationException();
      }

      @CheckForNull
      public E pollLast() {
         throw new UnsupportedOperationException();
      }

      public NavigableSet<E> descendingSet() {
         Sets.UnmodifiableNavigableSet<E> result = this.descendingSet;
         if (result == null) {
            result = this.descendingSet = new Sets.UnmodifiableNavigableSet(this.delegate.descendingSet());
            result.descendingSet = this;
         }

         return result;
      }

      public Iterator<E> descendingIterator() {
         return Iterators.unmodifiableIterator(this.delegate.descendingIterator());
      }

      public NavigableSet<E> subSet(@ParametricNullness E fromElement, boolean fromInclusive, @ParametricNullness E toElement, boolean toInclusive) {
         return Sets.unmodifiableNavigableSet(this.delegate.subSet(fromElement, fromInclusive, toElement, toInclusive));
      }

      public NavigableSet<E> headSet(@ParametricNullness E toElement, boolean inclusive) {
         return Sets.unmodifiableNavigableSet(this.delegate.headSet(toElement, inclusive));
      }

      public NavigableSet<E> tailSet(@ParametricNullness E fromElement, boolean inclusive) {
         return Sets.unmodifiableNavigableSet(this.delegate.tailSet(fromElement, inclusive));
      }
   }

   private static final class PowerSet<E> extends AbstractSet<Set<E>> {
      final ImmutableMap<E, Integer> inputSet;

      PowerSet(Set<E> input) {
         Preconditions.checkArgument(input.size() <= 30, "Too many elements to create power set: %s > 30", input.size());
         this.inputSet = Maps.indexMap(input);
      }

      public int size() {
         return 1 << this.inputSet.size();
      }

      public boolean isEmpty() {
         return false;
      }

      public Iterator<Set<E>> iterator() {
         return new AbstractIndexedListIterator<Set<E>>(this.size()) {
            protected Set<E> get(int setBits) {
               return new Sets.SubSet(PowerSet.this.inputSet, setBits);
            }
         };
      }

      public boolean contains(@CheckForNull Object obj) {
         if (obj instanceof Set) {
            Set<?> set = (Set)obj;
            return this.inputSet.keySet().containsAll(set);
         } else {
            return false;
         }
      }

      public boolean equals(@CheckForNull Object obj) {
         if (obj instanceof Sets.PowerSet) {
            Sets.PowerSet<?> that = (Sets.PowerSet)obj;
            return this.inputSet.keySet().equals(that.inputSet.keySet());
         } else {
            return super.equals(obj);
         }
      }

      public int hashCode() {
         return this.inputSet.keySet().hashCode() << this.inputSet.size() - 1;
      }

      public String toString() {
         String var1 = String.valueOf(this.inputSet);
         return (new StringBuilder(10 + String.valueOf(var1).length())).append("powerSet(").append(var1).append(")").toString();
      }
   }

   private static final class SubSet<E> extends AbstractSet<E> {
      private final ImmutableMap<E, Integer> inputSet;
      private final int mask;

      SubSet(ImmutableMap<E, Integer> inputSet, int mask) {
         this.inputSet = inputSet;
         this.mask = mask;
      }

      public Iterator<E> iterator() {
         return new UnmodifiableIterator<E>() {
            final ImmutableList<E> elements;
            int remainingSetBits;

            {
               this.elements = SubSet.this.inputSet.keySet().asList();
               this.remainingSetBits = SubSet.this.mask;
            }

            public boolean hasNext() {
               return this.remainingSetBits != 0;
            }

            public E next() {
               int index = Integer.numberOfTrailingZeros(this.remainingSetBits);
               if (index == 32) {
                  throw new NoSuchElementException();
               } else {
                  this.remainingSetBits &= ~(1 << index);
                  return this.elements.get(index);
               }
            }
         };
      }

      public int size() {
         return Integer.bitCount(this.mask);
      }

      public boolean contains(@CheckForNull Object o) {
         Integer index = (Integer)this.inputSet.get(o);
         return index != null && (this.mask & 1 << index) != 0;
      }
   }

   private static final class CartesianSet<E> extends ForwardingCollection<List<E>> implements Set<List<E>> {
      private final transient ImmutableList<ImmutableSet<E>> axes;
      private final transient CartesianList<E> delegate;

      static <E> Set<List<E>> create(List<? extends Set<? extends E>> sets) {
         ImmutableList.Builder<ImmutableSet<E>> axesBuilder = new ImmutableList.Builder(sets.size());
         Iterator var2 = sets.iterator();

         while(var2.hasNext()) {
            Set<? extends E> set = (Set)var2.next();
            ImmutableSet<E> copy = ImmutableSet.copyOf((Collection)set);
            if (copy.isEmpty()) {
               return ImmutableSet.of();
            }

            axesBuilder.add((Object)copy);
         }

         final ImmutableList<ImmutableSet<E>> axes = axesBuilder.build();
         ImmutableList<List<E>> listAxes = new ImmutableList<List<E>>() {
            public int size() {
               return axes.size();
            }

            public List<E> get(int index) {
               return ((ImmutableSet)axes.get(index)).asList();
            }

            boolean isPartialView() {
               return true;
            }
         };
         return new Sets.CartesianSet(axes, new CartesianList(listAxes));
      }

      private CartesianSet(ImmutableList<ImmutableSet<E>> axes, CartesianList<E> delegate) {
         this.axes = axes;
         this.delegate = delegate;
      }

      protected Collection<List<E>> delegate() {
         return this.delegate;
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
                  if (!((ImmutableSet)this.axes.get(i)).contains(o)) {
                     return false;
                  }
               }

               return true;
            }
         }
      }

      public boolean equals(@CheckForNull Object object) {
         if (object instanceof Sets.CartesianSet) {
            Sets.CartesianSet<?> that = (Sets.CartesianSet)object;
            return this.axes.equals(that.axes);
         } else {
            return super.equals(object);
         }
      }

      public int hashCode() {
         int adjust = this.size() - 1;

         int hash;
         for(hash = 0; hash < this.axes.size(); ++hash) {
            adjust *= 31;
            adjust = ~(~adjust);
         }

         hash = 1;

         for(UnmodifiableIterator var3 = this.axes.iterator(); var3.hasNext(); hash = ~(~hash)) {
            Set<E> axis = (Set)var3.next();
            hash = 31 * hash + this.size() / axis.size() * axis.hashCode();
         }

         hash += adjust;
         return ~(~hash);
      }
   }

   @GwtIncompatible
   private static class FilteredNavigableSet<E> extends Sets.FilteredSortedSet<E> implements NavigableSet<E> {
      FilteredNavigableSet(NavigableSet<E> unfiltered, Predicate<? super E> predicate) {
         super(unfiltered, predicate);
      }

      NavigableSet<E> unfiltered() {
         return (NavigableSet)this.unfiltered;
      }

      @CheckForNull
      public E lower(@ParametricNullness E e) {
         return Iterators.find(this.unfiltered().headSet(e, false).descendingIterator(), this.predicate, (Object)null);
      }

      @CheckForNull
      public E floor(@ParametricNullness E e) {
         return Iterators.find(this.unfiltered().headSet(e, true).descendingIterator(), this.predicate, (Object)null);
      }

      @CheckForNull
      public E ceiling(@ParametricNullness E e) {
         return Iterables.find(this.unfiltered().tailSet(e, true), this.predicate, (Object)null);
      }

      @CheckForNull
      public E higher(@ParametricNullness E e) {
         return Iterables.find(this.unfiltered().tailSet(e, false), this.predicate, (Object)null);
      }

      @CheckForNull
      public E pollFirst() {
         return Iterables.removeFirstMatching(this.unfiltered(), this.predicate);
      }

      @CheckForNull
      public E pollLast() {
         return Iterables.removeFirstMatching(this.unfiltered().descendingSet(), this.predicate);
      }

      public NavigableSet<E> descendingSet() {
         return Sets.filter(this.unfiltered().descendingSet(), this.predicate);
      }

      public Iterator<E> descendingIterator() {
         return Iterators.filter(this.unfiltered().descendingIterator(), this.predicate);
      }

      @ParametricNullness
      public E last() {
         return Iterators.find(this.unfiltered().descendingIterator(), this.predicate);
      }

      public NavigableSet<E> subSet(@ParametricNullness E fromElement, boolean fromInclusive, @ParametricNullness E toElement, boolean toInclusive) {
         return Sets.filter(this.unfiltered().subSet(fromElement, fromInclusive, toElement, toInclusive), this.predicate);
      }

      public NavigableSet<E> headSet(@ParametricNullness E toElement, boolean inclusive) {
         return Sets.filter(this.unfiltered().headSet(toElement, inclusive), this.predicate);
      }

      public NavigableSet<E> tailSet(@ParametricNullness E fromElement, boolean inclusive) {
         return Sets.filter(this.unfiltered().tailSet(fromElement, inclusive), this.predicate);
      }
   }

   private static class FilteredSortedSet<E> extends Sets.FilteredSet<E> implements SortedSet<E> {
      FilteredSortedSet(SortedSet<E> unfiltered, Predicate<? super E> predicate) {
         super(unfiltered, predicate);
      }

      @CheckForNull
      public Comparator<? super E> comparator() {
         return ((SortedSet)this.unfiltered).comparator();
      }

      public SortedSet<E> subSet(@ParametricNullness E fromElement, @ParametricNullness E toElement) {
         return new Sets.FilteredSortedSet(((SortedSet)this.unfiltered).subSet(fromElement, toElement), this.predicate);
      }

      public SortedSet<E> headSet(@ParametricNullness E toElement) {
         return new Sets.FilteredSortedSet(((SortedSet)this.unfiltered).headSet(toElement), this.predicate);
      }

      public SortedSet<E> tailSet(@ParametricNullness E fromElement) {
         return new Sets.FilteredSortedSet(((SortedSet)this.unfiltered).tailSet(fromElement), this.predicate);
      }

      @ParametricNullness
      public E first() {
         return Iterators.find(this.unfiltered.iterator(), this.predicate);
      }

      @ParametricNullness
      public E last() {
         SortedSet sortedUnfiltered = (SortedSet)this.unfiltered;

         while(true) {
            E element = sortedUnfiltered.last();
            if (this.predicate.apply(element)) {
               return element;
            }

            sortedUnfiltered = sortedUnfiltered.headSet(element);
         }
      }
   }

   private static class FilteredSet<E> extends Collections2.FilteredCollection<E> implements Set<E> {
      FilteredSet(Set<E> unfiltered, Predicate<? super E> predicate) {
         super(unfiltered, predicate);
      }

      public boolean equals(@CheckForNull Object object) {
         return Sets.equalsImpl(this, object);
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }
   }

   public abstract static class SetView<E> extends AbstractSet<E> {
      private SetView() {
      }

      public ImmutableSet<E> immutableCopy() {
         return ImmutableSet.copyOf((Collection)this);
      }

      @CanIgnoreReturnValue
      public <S extends Set<E>> S copyInto(S set) {
         set.addAll(this);
         return set;
      }

      /** @deprecated */
      @Deprecated
      @CanIgnoreReturnValue
      @DoNotCall("Always throws UnsupportedOperationException")
      public final boolean add(@ParametricNullness E e) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      @CanIgnoreReturnValue
      @DoNotCall("Always throws UnsupportedOperationException")
      public final boolean remove(@CheckForNull Object object) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      @CanIgnoreReturnValue
      @DoNotCall("Always throws UnsupportedOperationException")
      public final boolean addAll(Collection<? extends E> newElements) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      @CanIgnoreReturnValue
      @DoNotCall("Always throws UnsupportedOperationException")
      public final boolean removeAll(Collection<?> oldElements) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      @CanIgnoreReturnValue
      @DoNotCall("Always throws UnsupportedOperationException")
      public final boolean removeIf(java.util.function.Predicate<? super E> filter) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      @CanIgnoreReturnValue
      @DoNotCall("Always throws UnsupportedOperationException")
      public final boolean retainAll(Collection<?> elementsToKeep) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      @DoNotCall("Always throws UnsupportedOperationException")
      public final void clear() {
         throw new UnsupportedOperationException();
      }

      public abstract UnmodifiableIterator<E> iterator();

      // $FF: synthetic method
      SetView(Object x0) {
         this();
      }
   }

   abstract static class ImprovedAbstractSet<E> extends AbstractSet<E> {
      public boolean removeAll(Collection<?> c) {
         return Sets.removeAllImpl(this, (Collection)c);
      }

      public boolean retainAll(Collection<?> c) {
         return super.retainAll((Collection)Preconditions.checkNotNull(c));
      }
   }
}
