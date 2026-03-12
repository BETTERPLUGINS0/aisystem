package fr.xephi.authme.libs.com.mysql.cj.xdevapi;

import fr.xephi.authme.libs.com.mysql.cj.protocol.x.StatementExecuteOk;

public class InsertResultImpl extends UpdateResult implements InsertResult {
   public InsertResultImpl(StatementExecuteOk ok) {
      super(ok);
   }

   public Long getAutoIncrementValue() {
      return this.ok.getLastInsertId();
   }
}
