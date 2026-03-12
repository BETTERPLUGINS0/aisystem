package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation.statementgenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SingleConnectionPreparedStatementGenerator implements PreparedStatementGenerator {
   private final Connection connection;
   private final String sql;
   private PreparedStatement preparedStatement;

   public SingleConnectionPreparedStatementGenerator(Connection connection, String sql) {
      this.connection = connection;
      this.sql = sql;
   }

   public PreparedStatement createStatement() throws SQLException {
      if (this.preparedStatement == null) {
         this.preparedStatement = this.connection.prepareStatement(this.sql);
      }

      return this.preparedStatement;
   }

   public void close() throws SQLException {
      if (this.preparedStatement != null) {
         this.preparedStatement.close();
      }

   }
}
