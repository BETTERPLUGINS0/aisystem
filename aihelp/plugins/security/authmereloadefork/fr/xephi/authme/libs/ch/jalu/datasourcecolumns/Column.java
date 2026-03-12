package fr.xephi.authme.libs.ch.jalu.datasourcecolumns;

public interface Column<T, C> {
   String resolveName(C var1);

   ColumnType<T> getType();

   boolean isColumnUsed(C var1);

   boolean useDefaultForNullValue(C var1);
}
