package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data;

public interface DataSourceValue<T> {
   boolean rowExists();

   T getValue();
}
