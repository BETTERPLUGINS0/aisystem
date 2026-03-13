package github.nighter.smartspawner.libs.mariadb;

import github.nighter.smartspawner.libs.mariadb.client.Completion;
import github.nighter.smartspawner.libs.mariadb.client.result.Result;
import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import github.nighter.smartspawner.libs.mariadb.client.util.Parameter;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class ProcedureStatement extends BaseCallableStatement implements CallableStatement {
   public ProcedureStatement(Connection con, String sql, String databaseName, String procedureName, ClosableLock lock, int resultSetType, int resultSetConcurrency) throws SQLException {
      super(sql, con, lock, databaseName, procedureName, resultSetType, resultSetConcurrency, 0);
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

      this.currResult = (Completion)this.results.remove(0);
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
