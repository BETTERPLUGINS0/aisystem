package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data;

public final class DataSourceValueImpl<T> implements DataSourceValue<T> {
   private static final DataSourceValueImpl UNKNOWN_ROW = new DataSourceValueImpl((Object)null);
   private final T value;

   private DataSourceValueImpl(T value) {
      this.value = value;
   }

   public static <T> DataSourceValueImpl<T> of(T value) {
      return new DataSourceValueImpl(value);
   }

   public static <T> DataSourceValueImpl<T> unknownRow() {
      return UNKNOWN_ROW;
   }

   public boolean rowExists() {
      return this != UNKNOWN_ROW;
   }

   public T getValue() {
      return this.value;
   }
}
