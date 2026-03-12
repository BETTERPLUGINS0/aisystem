package fr.xephi.authme.libs.ch.jalu.datasourcecolumns;

public final class StandardTypes<T> implements ColumnType<T> {
   public static final ColumnType<String> STRING = new StandardTypes(String.class);
   public static final ColumnType<Long> LONG = new StandardTypes(Long.class);
   public static final ColumnType<Integer> INTEGER = new StandardTypes(Integer.class);
   public static final ColumnType<Boolean> BOOLEAN = new StandardTypes(Boolean.class);
   public static final ColumnType<Double> DOUBLE = new StandardTypes(Double.class);
   public static final ColumnType<Float> FLOAT = new StandardTypes(Float.class);
   private final Class<T> clazz;

   private StandardTypes(Class<T> clazz) {
      this.clazz = clazz;
   }

   public Class<T> getClazz() {
      return this.clazz;
   }
}
