package fr.xephi.authme.libs.ch.jalu.datasourcecolumns;

import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValue;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValues;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.UpdateValues;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.Predicate;
import java.util.List;

public interface ColumnsHandler<C, I> {
   <T> DataSourceValue<T> retrieve(I var1, Column<T, C> var2) throws Exception;

   DataSourceValues retrieve(I var1, Column<?, C>... var2) throws Exception;

   <T> List<T> retrieve(Predicate<C> var1, Column<T, C> var2) throws Exception;

   List<DataSourceValues> retrieve(Predicate<C> var1, Column<?, C>... var2) throws Exception;

   <T> boolean update(I var1, Column<T, C> var2, T var3) throws Exception;

   boolean update(I var1, UpdateValues<C> var2) throws Exception;

   <D> boolean update(I var1, D var2, DependentColumn<?, C, D>... var3) throws Exception;

   <T> int update(Predicate<C> var1, Column<T, C> var2, T var3) throws Exception;

   int update(Predicate<C> var1, UpdateValues<C> var2) throws Exception;

   boolean insert(UpdateValues<C> var1) throws Exception;

   <D> boolean insert(D var1, DependentColumn<?, C, D>... var2) throws Exception;

   int count(Predicate<C> var1) throws Exception;
}
