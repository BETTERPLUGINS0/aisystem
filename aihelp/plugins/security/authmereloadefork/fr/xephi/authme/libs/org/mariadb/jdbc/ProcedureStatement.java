package fr.xephi.authme.libs.org.mariadb.jdbc;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Completion;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.result.Result;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.util.Parameter;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

public class ProcedureStatement extends BaseCallableStatement implements CallableStatement {
   public ProcedureStatement(Connection con, String sql, String databaseName, String procedureName, ReentrantLock lock, boolean canUseServerTimeout, boolean canUseServerMaxRows, boolean canCachePrepStmts, int resultSetType, int resultSetConcurrency) throws SQLException {
      super(sql, con, lock, databaseName, procedureName, canUseServerTimeout, canUseServerMaxRows, canCachePrepStmts, resultSetType, resultSetConcurrency, 0);
   }

   public boolean isFunction() {
      return false;
   }

   protected void handleParameterOutput() throws SQLException {
      for(int i = 1; i <= Math.min(this.results.size(), 2); ++i) {
         Completion compl = (Completion)this.results.get(this.results.size() - i);
         if (compl instanceof Result && ((Result)compl).isOutputParameter()) {
            this.outputResultFromRes(i);
         }
      }

   }

   public String toString() {
      StringBuilder sb = new StringBuilder("ProcedureStatement{sql:'" + this.sql + "'");
      sb.append(", parameters:[");

      for(int i = 0; i < this.parameters.size(); ++i) {
         Parameter param = this.parameters.get(i);
         if (this.outputParameters.contains(i + 1)) {
            sb.append("<OUT>");
         }

         if (param == null) {
            sb.append("null");
         } else {
            sb.append(param.bestEffortStringValue(this.con.getContext()));
         }

         if (i != this.parameters.size() - 1) {
            sb.append(",");
         }
      }

      sb.append("]}");
      return sb.toString();
   }
}
