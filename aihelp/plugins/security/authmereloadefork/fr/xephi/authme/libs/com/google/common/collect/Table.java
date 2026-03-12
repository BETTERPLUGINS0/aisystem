package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CompatibleWith;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;

@DoNotMock("Use ImmutableTable, HashBasedTable, or another implementation")
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface Table<R, C, V> {
   boolean contains(@CheckForNull @CompatibleWith("R") Object var1, @CheckForNull @CompatibleWith("C") Object var2);

   boolean containsRow(@CheckForNull @CompatibleWith("R") Object var1);

   boolean containsColumn(@CheckForNull @CompatibleWith("C") Object var1);

   boolean containsValue(@CheckForNull @CompatibleWith("V") Object var1);

   @CheckForNull
   V get(@CheckForNull @CompatibleWith("R") Object var1, @CheckForNull @CompatibleWith("C") Object var2);

   boolean isEmpty();

   int size();

   boolean equals(@CheckForNull Object var1);

   int hashCode();

   void clear();

   @CheckForNull
   @CanIgnoreReturnValue
   V put(@ParametricNullness R var1, @ParametricNullness C var2, @ParametricNullness V var3);

   void putAll(Table<? extends R, ? extends C, ? extends V> var1);

   @CheckForNull
   @CanIgnoreReturnValue
   V remove(@CheckForNull @CompatibleWith("R") Object var1, @CheckForNull @CompatibleWith("C") Object var2);

   Map<C, V> row(@ParametricNullness R var1);

   Map<R, V> column(@ParametricNullness C var1);

   Set<Table.Cell<R, C, V>> cellSet();

   Set<R> rowKeySet();

   Set<C> columnKeySet();

   Collection<V> values();

   Map<R, Map<C, V>> rowMap();

   Map<C, Map<R, V>> columnMap();

   public interface Cell<R, C, V> {
      @ParametricNullness
      R getRowKey();

      @ParametricNullness
      C getColumnKey();

      @ParametricNullness
      V getValue();

      boolean equals(@CheckForNull Object var1);

      int hashCode();
   }
}
