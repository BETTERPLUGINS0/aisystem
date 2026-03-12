package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data;

import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.Column;

public interface DataSourceValues {
   boolean rowExists();

   <T> T get(Column<T, ?> var1);
}
