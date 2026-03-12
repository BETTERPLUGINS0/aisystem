package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data;

import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.Column;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class UpdateValues<C> {
   private final Map<Column<?, C>, Object> values;

   private UpdateValues(Map<Column<?, C>, Object> map) {
      this.values = map;
   }

   public Set<Column<?, C>> getColumns() {
      return this.values.keySet();
   }

   public <T> T get(Column<T, C> column) {
      T value = this.values.get(column);
      if (value == null && !this.values.containsKey(column)) {
         throw new IllegalArgumentException("No value available for column '" + column + "'");
      } else {
         return value;
      }
   }

   public static <C, T> UpdateValues.Builder<C> with(Column<T, C> column, T value) {
      return (new UpdateValues.Builder()).and(column, value);
   }

   // $FF: synthetic method
   UpdateValues(Map x0, Object x1) {
      this(x0);
   }

   public static final class Builder<C> {
      private Map<Column<?, C>, Object> map = new HashMap();

      public <T> UpdateValues.Builder<C> and(Column<T, C> column, T value) {
         this.map.put(column, value);
         return this;
      }

      public UpdateValues<C> build() {
         return new UpdateValues(this.map);
      }
   }
}
