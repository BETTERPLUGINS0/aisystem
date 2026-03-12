package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.statementgenerator;

import java.sql.Connection;

@FunctionalInterface
public interface PreparedStatementGeneratorFactory {
   PreparedStatementGenerator create(String var1);

   static PreparedStatementGeneratorFactory fromConnection(Connection con) {
      return (sql) -> {
         return new SingleConnectionPreparedStatementGenerator(con, sql);
      };
   }

   static PreparedStatementGeneratorFactory fromConnectionPool(ConnectionSupplier connectionSupplier) {
      return (sql) -> {
         return new ConnectionPoolPreparedStatementGenerator(connectionSupplier, sql);
      };
   }
}
