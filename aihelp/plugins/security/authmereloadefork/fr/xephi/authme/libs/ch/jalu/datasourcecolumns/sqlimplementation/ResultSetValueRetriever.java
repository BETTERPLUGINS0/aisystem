package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation;

import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.Column;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.ColumnType;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.StandardTypes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultSetValueRetriever<C> {
   private final C context;
   private final Map<ColumnType, ResultSetValueRetriever.ResultSetGetter> resultSetGetters = new ConcurrentHashMap();

   public ResultSetValueRetriever(C context) {
      this.context = context;
   }

   public <T> T get(ResultSet rs, Column<T, C> column) throws SQLException {
      return ((ResultSetValueRetriever.ResultSetGetter)this.resultSetGetters.computeIfAbsent(column.getType(), this::createResultSetGetter)).getValue(rs, column.resolveName(this.context));
   }

   protected <T> ResultSetValueRetriever.ResultSetGetter<T> createResultSetGetter(ColumnType<T> type) {
      ResultSetValueRetriever.ResultSetGetter resultSetGetter;
      if (type == StandardTypes.STRING) {
         resultSetGetter = ResultSet::getString;
      } else if (type == StandardTypes.LONG) {
         resultSetGetter = getTypeNullable(ResultSet::getLong, 0L);
      } else if (type == StandardTypes.INTEGER) {
         resultSetGetter = getTypeNullable(ResultSet::getInt, 0);
      } else if (type == StandardTypes.BOOLEAN) {
         resultSetGetter = getTypeNullable(ResultSet::getBoolean, false);
      } else if (type == StandardTypes.DOUBLE) {
         resultSetGetter = getTypeNullable(ResultSet::getDouble, 0.0D);
      } else {
         if (type != StandardTypes.FLOAT) {
            throw new IllegalArgumentException("Unhandled type '" + type + "'");
         }

         resultSetGetter = getTypeNullable(ResultSet::getFloat, 0.0F);
      }

      return resultSetGetter;
   }

   private static <T> ResultSetValueRetriever.ResultSetGetter<T> getTypeNullable(ResultSetValueRetriever.ResultSetGetter<T> getter, T standInValue) {
      return (rs, column) -> {
         T value = getter.getValue(rs, column);
         return standInValue.equals(value) && rs.wasNull() ? null : value;
      };
   }

   @FunctionalInterface
   public interface ResultSetGetter<T> {
      T getValue(ResultSet var1, String var2) throws SQLException;
   }
}
