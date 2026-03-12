package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data;

import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.Column;
import java.util.HashMap;
import java.util.Map;

public class DataSourceValuesImpl implements DataSourceValues {
   private static final DataSourceValuesImpl UNKNOWN_ROW = new DataSourceValuesImpl();
   private final Map<Column, Object> values = new HashMap();

   public static DataSourceValuesImpl unknownRow() {
      return UNKNOWN_ROW;
   }

   public <T> void put(Column<T, ?> column, Object value) {
      Class<T> typeClass = column.getType().getClazz();
      if (value != null && !typeClass.isInstance(value)) {
         throw new IllegalArgumentException("Value '" + value + "' does not have the correct type for column '" + column + "'");
      } else {
         this.values.put(column, value);
      }
   }

   public <T> T get(Column<T, ?> column) {
      T value = this.values.get(column);
      if (value == null && !this.values.containsKey(column)) {
         throw new IllegalArgumentException("No value available for column '" + column + "'");
      } else {
         return value;
      }
   }

   public boolean rowExists() {
      return this != UNKNOWN_ROW;
   }
}
