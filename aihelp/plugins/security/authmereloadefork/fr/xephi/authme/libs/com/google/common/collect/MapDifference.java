package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.util.Map;
import javax.annotation.CheckForNull;

@DoNotMock("Use Maps.difference")
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface MapDifference<K, V> {
   boolean areEqual();

   Map<K, V> entriesOnlyOnLeft();

   Map<K, V> entriesOnlyOnRight();

   Map<K, V> entriesInCommon();

   Map<K, MapDifference.ValueDifference<V>> entriesDiffering();

   boolean equals(@CheckForNull Object var1);

   int hashCode();

   @DoNotMock("Use Maps.difference")
   public interface ValueDifference<V> {
      @ParametricNullness
      V leftValue();

      @ParametricNullness
      V rightValue();

      boolean equals(@CheckForNull Object var1);

      int hashCode();
   }
}
