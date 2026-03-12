package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation;

import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.Column;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.ColumnsHandler;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.DependentColumn;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValue;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValueImpl;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValues;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValuesImpl;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.UpdateValues;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.Predicate;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.statementgenerator.PreparedStatementGenerator;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.statementgenerator.PreparedStatementGeneratorFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SqlColumnsHandler<C, I> implements ColumnsHandler<C, I> {
   private final String tableName;
   private final String idColumn;
   private final C context;
   private final PreparedStatementGeneratorFactory statementGeneratorFactory;
   private final ResultSetValueRetriever<C> resultSetValueRetriever;
   private final PredicateSqlGenerator<C> predicateSqlGenerator;

   public SqlColumnsHandler(SqlColumnsHandlerConfig<C> config) {
      this.tableName = config.getTableName();
      this.idColumn = config.getIdColumn();
      this.context = config.getContext();
      this.statementGeneratorFactory = config.getStatementGeneratorFactory();
      this.resultSetValueRetriever = config.getResultSetValueRetriever();
      this.predicateSqlGenerator = config.getPredicateSqlGenerator();
   }

   public <T> DataSourceValue<T> retrieve(I identifier, Column<T, C> column) throws SQLException {
      boolean isColumnUsed = column.isColumnUsed(this.context);
      String columnName = isColumnUsed ? column.resolveName(this.context) : "1";
      String sql = "SELECT " + columnName + " FROM " + this.tableName + " WHERE " + this.idColumn + " = ?;";
      PreparedStatementGenerator generator = this.statementGeneratorFactory.create(sql);
      Throwable var7 = null;

      Object var11;
      try {
         PreparedStatement pst = generator.createStatement();
         pst.setObject(1, identifier);
         ResultSet rs = pst.executeQuery();
         Throwable var10 = null;

         try {
            if (!rs.next()) {
               var11 = DataSourceValueImpl.unknownRow();
               return (DataSourceValue)var11;
            }

            var11 = isColumnUsed ? DataSourceValueImpl.of(this.resultSetValueRetriever.get(rs, column)) : DataSourceValueImpl.of((Object)null);
         } catch (Throwable var37) {
            var11 = var37;
            var10 = var37;
            throw var37;
         } finally {
            if (rs != null) {
               if (var10 != null) {
                  try {
                     rs.close();
                  } catch (Throwable var36) {
                     var10.addSuppressed(var36);
                  }
               } else {
                  rs.close();
               }
            }

         }
      } catch (Throwable var39) {
         var7 = var39;
         throw var39;
      } finally {
         if (generator != null) {
            if (var7 != null) {
               try {
                  generator.close();
               } catch (Throwable var35) {
                  var7.addSuppressed(var35);
               }
            } else {
               generator.close();
            }
         }

      }

      return (DataSourceValue)var11;
   }

   public DataSourceValues retrieve(I identifier, Column<?, C>... columns) throws SQLException {
      Set<Column<?, C>> nonEmptyColumns = this.removeSkippedColumns(columns);
      String sql = "SELECT " + (nonEmptyColumns.isEmpty() ? "1" : this.commaSeparatedList(nonEmptyColumns)) + " FROM " + this.tableName + " WHERE " + this.idColumn + " = ?;";
      PreparedStatementGenerator generator = this.statementGeneratorFactory.create(sql);
      Throwable var6 = null;

      Object var10;
      try {
         PreparedStatement pst = generator.createStatement();
         pst.setObject(1, identifier);
         ResultSet rs = pst.executeQuery();
         Throwable var9 = null;

         try {
            var10 = rs.next() ? this.generateDataSourceValuesObject(rs, nonEmptyColumns, columns) : DataSourceValuesImpl.unknownRow();
         } catch (Throwable var33) {
            var10 = var33;
            var9 = var33;
            throw var33;
         } finally {
            if (rs != null) {
               if (var9 != null) {
                  try {
                     rs.close();
                  } catch (Throwable var32) {
                     var9.addSuppressed(var32);
                  }
               } else {
                  rs.close();
               }
            }

         }
      } catch (Throwable var35) {
         var6 = var35;
         throw var35;
      } finally {
         if (generator != null) {
            if (var6 != null) {
               try {
                  generator.close();
               } catch (Throwable var31) {
                  var6.addSuppressed(var31);
               }
            } else {
               generator.close();
            }
         }

      }

      return (DataSourceValues)var10;
   }

   public <T> List<T> retrieve(Predicate<C> predicate, Column<T, C> column) throws SQLException {
      boolean isColumnUsed = column.isColumnUsed(this.context);
      if (!isColumnUsed) {
         int matchingRows = this.count(predicate);
         return Collections.nCopies(matchingRows, (Object)null);
      } else {
         GeneratedSqlWithBindings sqlPredicate = this.predicateSqlGenerator.generateWhereClause(predicate);
         String sql = "SELECT " + column.resolveName(this.context) + " FROM " + this.tableName + " WHERE " + sqlPredicate.getGeneratedSql();
         List<T> results = new ArrayList();
         PreparedStatementGenerator generator = this.statementGeneratorFactory.create(sql);
         Throwable var8 = null;

         try {
            PreparedStatement pst = generator.createStatement();
            this.bindValues(pst, 1, sqlPredicate.getBindings());
            ResultSet rs = pst.executeQuery();
            Throwable var11 = null;

            try {
               while(rs.next()) {
                  results.add(this.resultSetValueRetriever.get(rs, column));
               }
            } catch (Throwable var34) {
               var11 = var34;
               throw var34;
            } finally {
               if (rs != null) {
                  if (var11 != null) {
                     try {
                        rs.close();
                     } catch (Throwable var33) {
                        var11.addSuppressed(var33);
                     }
                  } else {
                     rs.close();
                  }
               }

            }
         } catch (Throwable var36) {
            var8 = var36;
            throw var36;
         } finally {
            if (generator != null) {
               if (var8 != null) {
                  try {
                     generator.close();
                  } catch (Throwable var32) {
                     var8.addSuppressed(var32);
                  }
               } else {
                  generator.close();
               }
            }

         }

         return results;
      }
   }

   public List<DataSourceValues> retrieve(Predicate<C> predicate, Column<?, C>... columns) throws SQLException {
      Set<Column<?, C>> nonEmptyColumns = this.removeSkippedColumns(columns);
      GeneratedSqlWithBindings sqlPredicate = this.predicateSqlGenerator.generateWhereClause(predicate);
      String sql = "SELECT " + (nonEmptyColumns.isEmpty() ? "1" : this.commaSeparatedList(nonEmptyColumns)) + " FROM " + this.tableName + " WHERE " + sqlPredicate.getGeneratedSql();
      List<DataSourceValues> matchingEntries = new ArrayList();
      PreparedStatementGenerator generator = this.statementGeneratorFactory.create(sql);
      Throwable var8 = null;

      try {
         PreparedStatement pst = generator.createStatement();
         this.bindValues(pst, 1, sqlPredicate.getBindings());
         ResultSet rs = pst.executeQuery();
         Throwable var11 = null;

         try {
            while(rs.next()) {
               DataSourceValues values = this.generateDataSourceValuesObject(rs, nonEmptyColumns, columns);
               matchingEntries.add(values);
            }
         } catch (Throwable var34) {
            var11 = var34;
            throw var34;
         } finally {
            if (rs != null) {
               if (var11 != null) {
                  try {
                     rs.close();
                  } catch (Throwable var33) {
                     var11.addSuppressed(var33);
                  }
               } else {
                  rs.close();
               }
            }

         }
      } catch (Throwable var36) {
         var8 = var36;
         throw var36;
      } finally {
         if (generator != null) {
            if (var8 != null) {
               try {
                  generator.close();
               } catch (Throwable var32) {
                  var8.addSuppressed(var32);
               }
            } else {
               generator.close();
            }
         }

      }

      return matchingEntries;
   }

   public <T> boolean update(I identifier, Column<T, C> column, T value) throws SQLException {
      if (!column.isColumnUsed(this.context)) {
         return true;
      } else {
         String sql;
         PreparedStatementGenerator generator;
         Throwable var6;
         PreparedStatement pst;
         boolean var8;
         if (value == null && column.useDefaultForNullValue(this.context)) {
            sql = "UPDATE " + this.tableName + " SET " + column.resolveName(this.context) + " = DEFAULT WHERE " + this.idColumn + " = ?;";
            generator = this.statementGeneratorFactory.create(sql);
            var6 = null;

            try {
               pst = generator.createStatement();
               pst.setObject(1, identifier);
               var8 = this.performUpdateAction(pst);
            } catch (Throwable var31) {
               var6 = var31;
               throw var31;
            } finally {
               if (generator != null) {
                  if (var6 != null) {
                     try {
                        generator.close();
                     } catch (Throwable var30) {
                        var6.addSuppressed(var30);
                     }
                  } else {
                     generator.close();
                  }
               }

            }

            return var8;
         } else {
            sql = "UPDATE " + this.tableName + " SET " + column.resolveName(this.context) + " = ? WHERE " + this.idColumn + " = ?;";
            generator = this.statementGeneratorFactory.create(sql);
            var6 = null;

            try {
               pst = generator.createStatement();
               pst.setObject(1, value);
               pst.setObject(2, identifier);
               var8 = this.performUpdateAction(pst);
            } catch (Throwable var32) {
               var6 = var32;
               throw var32;
            } finally {
               if (generator != null) {
                  if (var6 != null) {
                     try {
                        generator.close();
                     } catch (Throwable var29) {
                        var6.addSuppressed(var29);
                     }
                  } else {
                     generator.close();
                  }
               }

            }

            return var8;
         }
      }
   }

   public boolean update(I identifier, UpdateValues<C> updateValues) throws SQLException {
      Set var10002 = updateValues.getColumns();
      updateValues.getClass();
      return this.performUpdate(identifier, var10002, updateValues::get);
   }

   public <D> boolean update(I identifier, D dependent, DependentColumn<?, C, D>... columns) throws SQLException {
      return this.performUpdate(identifier, Arrays.asList(columns), (column) -> {
         return column.getValueFromDependent(dependent);
      });
   }

   public <T> int update(Predicate<C> predicate, Column<T, C> column, T value) throws SQLException {
      return this.update(predicate, UpdateValues.with(column, value).build());
   }

   public int update(Predicate<C> predicate, UpdateValues<C> updateValues) throws SQLException {
      return this.performPredicateUpdate(predicate, updateValues);
   }

   public boolean insert(UpdateValues<C> updateValues) throws SQLException {
      Set var10001 = updateValues.getColumns();
      updateValues.getClass();
      return this.performInsert(var10001, updateValues::get);
   }

   public <D> boolean insert(D dependent, DependentColumn<?, C, D>... columns) throws SQLException {
      return this.performInsert(Arrays.asList(columns), (column) -> {
         return column.getValueFromDependent(dependent);
      });
   }

   public int count(Predicate<C> predicate) throws SQLException {
      GeneratedSqlWithBindings whereResult = this.predicateSqlGenerator.generateWhereClause(predicate);
      String sql = "SELECT COUNT(1) FROM " + this.tableName + " WHERE " + whereResult.getGeneratedSql();
      PreparedStatementGenerator generator = this.statementGeneratorFactory.create(sql);
      Throwable var5 = null;

      Throwable var9;
      try {
         PreparedStatement pst = generator.createStatement();
         this.bindValues(pst, 1, whereResult.getBindings());
         ResultSet rs = pst.executeQuery();
         Throwable var8 = null;

         try {
            if (!rs.next()) {
               throw new IllegalStateException("Could not fetch count for SQL '" + sql + "'");
            }

            var9 = rs.getInt(1);
         } catch (Throwable var32) {
            var9 = var32;
            var8 = var32;
            throw var32;
         } finally {
            if (rs != null) {
               if (var8 != null) {
                  try {
                     rs.close();
                  } catch (Throwable var31) {
                     var8.addSuppressed(var31);
                  }
               } else {
                  rs.close();
               }
            }

         }
      } catch (Throwable var34) {
         var5 = var34;
         throw var34;
      } finally {
         if (generator != null) {
            if (var5 != null) {
               try {
                  generator.close();
               } catch (Throwable var30) {
                  var5.addSuppressed(var30);
               }
            } else {
               generator.close();
            }
         }

      }

      return (int)var9;
   }

   private <E extends Column<?, C>> boolean performUpdate(I identifier, Collection<E> columns, Function<E, Object> valueGetter) throws SQLException {
      Set<E> nonEmptyColumns = this.removeSkippedColumns(columns);
      if (nonEmptyColumns.isEmpty()) {
         return false;
      } else {
         GeneratedSqlWithBindings columnSetList = this.createColumnsListForUpdate(nonEmptyColumns, valueGetter);
         String sql = "UPDATE " + this.tableName + " SET " + columnSetList.getGeneratedSql() + " WHERE " + this.idColumn + " = ?;";
         PreparedStatementGenerator generator = this.statementGeneratorFactory.create(sql);
         Throwable var8 = null;

         boolean var11;
         try {
            PreparedStatement pst = generator.createStatement();
            int index = this.bindValues(pst, 1, columnSetList.getBindings());
            pst.setObject(index, identifier);
            var11 = this.performUpdateAction(pst);
         } catch (Throwable var20) {
            var8 = var20;
            throw var20;
         } finally {
            if (generator != null) {
               if (var8 != null) {
                  try {
                     generator.close();
                  } catch (Throwable var19) {
                     var8.addSuppressed(var19);
                  }
               } else {
                  generator.close();
               }
            }

         }

         return var11;
      }
   }

   private int performPredicateUpdate(Predicate<C> predicate, UpdateValues<C> updateValues) throws SQLException {
      Set<Column<?, C>> nonEmptyColumns = this.removeSkippedColumns((Collection)updateValues.getColumns());
      if (nonEmptyColumns.isEmpty()) {
         return 0;
      } else {
         updateValues.getClass();
         GeneratedSqlWithBindings columnSetList = this.createColumnsListForUpdate(nonEmptyColumns, updateValues::get);
         GeneratedSqlWithBindings whereClause = this.predicateSqlGenerator.generateWhereClause(predicate);
         String sql = "UPDATE " + this.tableName + " SET " + columnSetList.getGeneratedSql() + " WHERE " + whereClause.getGeneratedSql();
         PreparedStatementGenerator generator = this.statementGeneratorFactory.create(sql);
         Throwable var8 = null;

         int var11;
         try {
            PreparedStatement pst = generator.createStatement();
            int index = this.bindValues(pst, 1, columnSetList.getBindings());
            this.bindValues(pst, index, whereClause.getBindings());
            var11 = pst.executeUpdate();
         } catch (Throwable var20) {
            var8 = var20;
            throw var20;
         } finally {
            if (generator != null) {
               if (var8 != null) {
                  try {
                     generator.close();
                  } catch (Throwable var19) {
                     var8.addSuppressed(var19);
                  }
               } else {
                  generator.close();
               }
            }

         }

         return var11;
      }
   }

   private <E extends Column<?, C>> GeneratedSqlWithBindings createColumnsListForUpdate(Collection<E> columns, Function<E, Object> valueGetter) {
      List<Object> bindings = new LinkedList();
      String sql = (String)columns.stream().map((column) -> {
         Object value = valueGetter.apply(column);
         if (value == null && column.useDefaultForNullValue(this.context)) {
            return column.resolveName(this.context) + " = DEFAULT";
         } else {
            bindings.add(value);
            return column.resolveName(this.context) + " = ?";
         }
      }).collect(Collectors.joining(", "));
      return new GeneratedSqlWithBindings(sql, bindings);
   }

   private <E extends Column<?, C>> boolean performInsert(Collection<E> columns, Function<E, Object> valueGetter) throws SQLException {
      Set<E> nonEmptyColumns = this.removeSkippedColumns(columns);
      if (nonEmptyColumns.isEmpty()) {
         throw new IllegalStateException("Cannot perform insert when all columns are empty: " + columns);
      } else {
         GeneratedSqlWithBindings placeholders = this.createValuePlaceholdersForInsert(nonEmptyColumns, valueGetter);
         String sql = "INSERT INTO " + this.tableName + " (" + this.commaSeparatedList(nonEmptyColumns) + ") VALUES(" + placeholders.getGeneratedSql() + ");";
         PreparedStatementGenerator generator = this.statementGeneratorFactory.create(sql);
         Throwable var7 = null;

         boolean var9;
         try {
            PreparedStatement pst = generator.createStatement();
            this.bindValues(pst, 1, placeholders.getBindings());
            var9 = this.performUpdateAction(pst);
         } catch (Throwable var18) {
            var7 = var18;
            throw var18;
         } finally {
            if (generator != null) {
               if (var7 != null) {
                  try {
                     generator.close();
                  } catch (Throwable var17) {
                     var7.addSuppressed(var17);
                  }
               } else {
                  generator.close();
               }
            }

         }

         return var9;
      }
   }

   private <E extends Column<?, C>> GeneratedSqlWithBindings createValuePlaceholdersForInsert(Collection<E> columns, Function<E, Object> valueGetter) {
      List<Object> bindings = new LinkedList();
      String sql = (String)columns.stream().map((column) -> {
         Object value = valueGetter.apply(column);
         if (value == null && column.useDefaultForNullValue(this.context)) {
            return "DEFAULT";
         } else {
            bindings.add(value);
            return "?";
         }
      }).collect(Collectors.joining(", "));
      return new GeneratedSqlWithBindings(sql, bindings);
   }

   private DataSourceValues generateDataSourceValuesObject(ResultSet rs, Set<Column<?, C>> nonEmptyColumns, Column<?, C>[] columns) throws SQLException {
      DataSourceValuesImpl values = new DataSourceValuesImpl();
      Column[] var5 = columns;
      int var6 = columns.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Column<?, C> column = var5[var7];
         if (nonEmptyColumns.contains(column)) {
            values.put(column, this.resultSetValueRetriever.get(rs, column));
         } else {
            values.put(column, (Object)null);
         }
      }

      return values;
   }

   protected boolean performUpdateAction(PreparedStatement pst) throws SQLException {
      int count = pst.executeUpdate();
      if (count == 1) {
         return true;
      } else if (count == 0) {
         return false;
      } else {
         throw new IllegalStateException("Found " + count + " rows updated/inserted by statement, expected only 1");
      }
   }

   private String commaSeparatedList(Collection<? extends Column<?, C>> columns) {
      return (String)columns.stream().map((column) -> {
         return column.resolveName(this.context);
      }).collect(Collectors.joining(", "));
   }

   private int bindValues(PreparedStatement pst, int startIndex, Collection<Object> bindings) throws SQLException {
      int index = startIndex;

      for(Iterator var5 = bindings.iterator(); var5.hasNext(); ++index) {
         Object binding = var5.next();
         pst.setObject(index, binding);
      }

      return index;
   }

   private <E extends Column<?, C>> Set<E> removeSkippedColumns(Collection<E> cols) {
      return (Set)cols.stream().filter((column) -> {
         return column.isColumnUsed(this.context);
      }).collect(Collectors.toSet());
   }

   @SafeVarargs
   private final Set<Column<?, C>> removeSkippedColumns(Column<?, C>... cols) {
      return this.removeSkippedColumns((Collection)Arrays.asList(cols));
   }
}
