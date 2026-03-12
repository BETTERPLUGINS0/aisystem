package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.CheckForNull;

@DoNotMock("Use ImmutableRangeSet or TreeRangeSet")
@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public interface RangeSet<C extends Comparable> {
   boolean contains(C var1);

   @CheckForNull
   Range<C> rangeContaining(C var1);

   boolean intersects(Range<C> var1);

   boolean encloses(Range<C> var1);

   boolean enclosesAll(RangeSet<C> var1);

   default boolean enclosesAll(Iterable<Range<C>> other) {
      Iterator var2 = other.iterator();

      Range range;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         range = (Range)var2.next();
      } while(this.encloses(range));

      return false;
   }

   boolean isEmpty();

   Range<C> span();

   Set<Range<C>> asRanges();

   Set<Range<C>> asDescendingSetOfRanges();

   RangeSet<C> complement();

   RangeSet<C> subRangeSet(Range<C> var1);

   void add(Range<C> var1);

   void remove(Range<C> var1);

   void clear();

   void addAll(RangeSet<C> var1);

   default void addAll(Iterable<Range<C>> ranges) {
      Iterator var2 = ranges.iterator();

      while(var2.hasNext()) {
         Range<C> range = (Range)var2.next();
         this.add(range);
      }

   }

   void removeAll(RangeSet<C> var1);

   default void removeAll(Iterable<Range<C>> ranges) {
      Iterator var2 = ranges.iterator();

      while(var2.hasNext()) {
         Range<C> range = (Range)var2.next();
         this.remove(range);
      }

   }

   boolean equals(@CheckForNull Object var1);

   int hashCode();

   String toString();
}
