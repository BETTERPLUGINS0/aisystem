package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation;

import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.statementgenerator.ConnectionSupplier;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.statementgenerator.PreparedStatementGeneratorFactory;
import java.sql.Connection;

public class SqlColumnsHandlerConfig<C> {
   private final String tableName;
   private final String idColumn;
   private final C context;
   private PreparedStatementGeneratorFactory statementGeneratorFactory;
   private ResultSetValueRetriever<C> resultSetValueRetriever;
   private PredicateSqlGenerator<C> predicateSqlGenerator;

   protected SqlColumnsHandlerConfig(String tableName, String idColumn, C context, PreparedStatementGeneratorFactory generatorFactory) {
      this.tableName = tableName;
      this.idColumn = idColumn;
      this.context = context;
      this.statementGeneratorFactory = generatorFactory;
      this.resultSetValueRetriever = new ResultSetValueRetriever(context);
      this.predicateSqlGenerator = new PredicateSqlGenerator(context);
   }

   public static <C> SqlColumnsHandlerConfig<C> forSingleConnection(Connection connection, String tableName, String idColumn, C context) {
      PreparedStatementGeneratorFactory statementGeneratorFactory = PreparedStatementGeneratorFactory.fromConnection(connection);
      return new SqlColumnsHandlerConfig(tableName, idColumn, context, statementGeneratorFactory);
   }

   public static <C> SqlColumnsHandlerConfig<C> forConnectionPool(ConnectionSupplier connectionSupplier, String tableName, String idColumn, C context) {
      PreparedStatementGeneratorFactory statementGeneratorFactory = PreparedStatementGeneratorFactory.fromConnectionPool(connectionSupplier);
      return new SqlColumnsHandlerConfig(tableName, idColumn, context, statementGeneratorFactory);
   }

   public SqlColumnsHandlerConfig<C> setPreparedStatementGeneratorFactory(PreparedStatementGeneratorFactory factory) {
      this.statementGeneratorFactory = factory;
      return this;
   }

   public SqlColumnsHandlerConfig<C> setResultSetValueRetriever(ResultSetValueRetriever<C> resultSetValueRetriever) {
      this.resultSetValueRetriever = resultSetValueRetriever;
      return this;
   }

   public SqlColumnsHandlerConfig<C> setPredicateSqlGenerator(PredicateSqlGenerator<C> predicateSqlGenerator) {
      this.predicateSqlGenerator = predicateSqlGenerator;
      return this;
   }

   public String getTableName() {
      return this.tableName;
   }

   public String getIdColumn() {
      return this.idColumn;
   }

   public C getContext() {
      return this.context;
   }

   public PreparedStatementGeneratorFactory getStatementGeneratorFactory() {
      return this.statementGeneratorFactory;
   }

   public ResultSetValueRetriever<C> getResultSetValueRetriever() {
      return this.resultSetValueRetriever;
   }

   public PredicateSqlGenerator<C> getPredicateSqlGenerator() {
      return this.predicateSqlGenerator;
   }
}
