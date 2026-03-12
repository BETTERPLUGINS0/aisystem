package fr.xephi.authme.libs.ch.jalu.datasourcecolumns;

public interface DependentColumn<T, C, D> extends Column<T, C> {
   T getValueFromDependent(D var1);
}
