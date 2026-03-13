package github.nighter.smartspawner.libs.mariadb;

import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import github.nighter.smartspawner.libs.mariadb.client.util.Parameter;
import github.nighter.smartspawner.libs.mariadb.client.util.Parameters;
import github.nighter.smartspawner.libs.mariadb.util.ParameterList;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.SQLType;

public class FunctionStatement extends BaseCallableStatement implements CallableStatement {
   public FunctionStatement(Connection con, String databaseName, String procedureName, String arguments, ClosableLock lock, int resultSetType, int resultSetConcurrency) throws SQLException {
      super("SELECT " + procedureName + arguments, con, lock, databaseName, procedureName, resultSetType, resultSetConcurrency, 0);
      this.registerOutParameter(1, (SQLType)null);
   }

   public boolean isFunction() {
      return true;
   }

   protected void handleParameterOutput() throws SQLException {
      this.outputResultFromRes(1);
   }

   public void registerOutParameter(int index, int sqlType) throws SQLException {
      if (index != 1) {
         throw this.con.getExceptionFactory().of(this).create(String.format("wrong parameter index %s", index));
      } else {
         super.registerOutParameter(index, sqlType);
      }
   }

   protected void executeInternal() throws SQLException {
      this.preValidParameters();
      super.executeInternal();
   }

   protected void preValidParameters() throws SQLException {
      Parameters newParameters = new ParameterList(this.parameters.size() - 1);

      for(int i = 0; i < this.parameters.size() - 1; ++i) {
         newParameters.set(i, this.parameters.get(i + 1));
      }

      this.parameters = newParameters;
      super.validParameters();
   }

   public String toString() {
      StringBuilder sb = new StringBuilder("FunctionStatement{sql:'" + this.sql + "'");
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
