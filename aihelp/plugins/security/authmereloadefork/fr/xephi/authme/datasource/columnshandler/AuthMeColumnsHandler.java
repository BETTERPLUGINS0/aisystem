package fr.xephi.authme.datasource.columnshandler;

import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.SqlDataSourceUtils;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.Column;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.DependentColumn;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValue;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.DataSourceValues;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.data.UpdateValues;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.Predicate;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.PredicateSqlGenerator;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.SqlColumnsHandler;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.SqlColumnsHandlerConfig;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.statementgenerator.ConnectionSupplier;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.DatabaseSettings;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public final class AuthMeColumnsHandler {
   private final SqlColumnsHandler<ColumnContext, String> internalHandler;

   private AuthMeColumnsHandler(SqlColumnsHandler<ColumnContext, String> internalHandler) {
      this.internalHandler = internalHandler;
   }

   public static AuthMeColumnsHandler createForSqlite(Connection connection, Settings settings) {
      ColumnContext columnContext = new ColumnContext(settings, false);
      String tableName = (String)settings.getProperty(DatabaseSettings.MYSQL_TABLE);
      String nameColumn = (String)settings.getProperty(DatabaseSettings.MYSQL_COL_NAME);
      SqlColumnsHandler<ColumnContext, String> sqlColHandler = new SqlColumnsHandler(SqlColumnsHandlerConfig.forSingleConnection(connection, tableName, nameColumn, columnContext).setPredicateSqlGenerator(new PredicateSqlGenerator(columnContext, true)));
      return new AuthMeColumnsHandler(sqlColHandler);
   }

   public static AuthMeColumnsHandler createForH2(Connection connection, Settings settings) {
      ColumnContext columnContext = new ColumnContext(settings, false);
      String tableName = (String)settings.getProperty(DatabaseSettings.MYSQL_TABLE);
      String nameColumn = (String)settings.getProperty(DatabaseSettings.MYSQL_COL_NAME);
      SqlColumnsHandler<ColumnContext, String> sqlColHandler = new SqlColumnsHandler(SqlColumnsHandlerConfig.forSingleConnection(connection, tableName, nameColumn, columnContext).setPredicateSqlGenerator(new PredicateSqlGenerator(columnContext, false)));
      return new AuthMeColumnsHandler(sqlColHandler);
   }

   public static AuthMeColumnsHandler createForMySql(ConnectionSupplier connectionSupplier, Settings settings) {
      ColumnContext columnContext = new ColumnContext(settings, true);
      String tableName = (String)settings.getProperty(DatabaseSettings.MYSQL_TABLE);
      String nameColumn = (String)settings.getProperty(DatabaseSettings.MYSQL_COL_NAME);
      SqlColumnsHandler<ColumnContext, String> sqlColHandler = new SqlColumnsHandler(SqlColumnsHandlerConfig.forConnectionPool(connectionSupplier, tableName, nameColumn, columnContext));
      return new AuthMeColumnsHandler(sqlColHandler);
   }

   public <T> boolean update(String name, DataSourceColumn<T> column, T value) {
      try {
         return this.internalHandler.update((Object)name.toLowerCase(Locale.ROOT), (Column)column, (Object)value);
      } catch (SQLException var5) {
         SqlDataSourceUtils.logSqlException(var5);
         return false;
      }
   }

   public boolean update(PlayerAuth auth, PlayerAuthColumn<?>... columns) {
      try {
         return this.internalHandler.update((Object)auth.getNickname(), (Object)auth, (DependentColumn[])columns);
      } catch (SQLException var4) {
         SqlDataSourceUtils.logSqlException(var4);
         return false;
      }
   }

   public boolean update(String name, UpdateValues<ColumnContext> updateValues) {
      try {
         return this.internalHandler.update((Object)name.toLowerCase(Locale.ROOT), updateValues);
      } catch (SQLException var4) {
         SqlDataSourceUtils.logSqlException(var4);
         return false;
      }
   }

   public <T> int update(Predicate<ColumnContext> predicate, DataSourceColumn<T> column, T value) {
      try {
         return this.internalHandler.update((Predicate)predicate, (Column)column, (Object)value);
      } catch (SQLException var5) {
         SqlDataSourceUtils.logSqlException(var5);
         return 0;
      }
   }

   public <T> DataSourceValue<T> retrieve(String name, DataSourceColumn<T> column) throws SQLException {
      return this.internalHandler.retrieve((Object)name.toLowerCase(Locale.ROOT), (Column)column);
   }

   public DataSourceValues retrieve(String name, DataSourceColumn<?>... columns) throws SQLException {
      return this.internalHandler.retrieve((Object)name.toLowerCase(Locale.ROOT), (Column[])columns);
   }

   public <T> List<T> retrieve(Predicate<ColumnContext> predicate, DataSourceColumn<T> column) throws SQLException {
      return this.internalHandler.retrieve((Predicate)predicate, (Column)column);
   }

   public boolean insert(PlayerAuth auth, PlayerAuthColumn<?>... columns) {
      try {
         return this.internalHandler.insert(auth, columns);
      } catch (SQLException var4) {
         SqlDataSourceUtils.logSqlException(var4);
         return false;
      }
   }

   public int count(Predicate<ColumnContext> predicate) {
      try {
         return this.internalHandler.count(predicate);
      } catch (SQLException var3) {
         SqlDataSourceUtils.logSqlException(var3);
         return 0;
      }
   }
}
