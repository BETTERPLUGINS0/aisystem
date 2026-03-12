package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.statementgenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionPoolPreparedStatementGenerator implements PreparedStatementGenerator {
   private final ConnectionSupplier connectionSupplier;
   private final String sql;
   private Connection connection;
   private PreparedStatement preparedStatement;

   public ConnectionPoolPreparedStatementGenerator(ConnectionSupplier connectionSupplier, String sql) {
      this.connectionSupplier = connectionSupplier;
      this.sql = sql;
   }

   public PreparedStatement createStatement() throws SQLException {
      if (this.connection == null) {
         this.connection = this.connectionSupplier.get();
      }

      if (this.preparedStatement == null) {
         this.preparedStatement = this.connection.prepareStatement(this.sql);
      }

      return this.preparedStatement;
   }

   public void close() throws SQLException {
      try {
         if (this.connection != null) {
            this.connection.close();
         }
      } finally {
         if (this.preparedStatement != null) {
            this.preparedStatement.close();
         }

      }

   }
}
