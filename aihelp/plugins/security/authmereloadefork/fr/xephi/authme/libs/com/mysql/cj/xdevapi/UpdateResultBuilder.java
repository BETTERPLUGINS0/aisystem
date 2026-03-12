package fr.xephi.authme.libs.com.mysql.cj.xdevapi;

import fr.xephi.authme.libs.com.mysql.cj.exceptions.ExceptionFactory;
import fr.xephi.authme.libs.com.mysql.cj.exceptions.WrongArgumentException;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntity;
import fr.xephi.authme.libs.com.mysql.cj.protocol.ResultBuilder;
import fr.xephi.authme.libs.com.mysql.cj.protocol.x.Notice;
import fr.xephi.authme.libs.com.mysql.cj.protocol.x.Ok;
import fr.xephi.authme.libs.com.mysql.cj.protocol.x.StatementExecuteOk;
import fr.xephi.authme.libs.com.mysql.cj.protocol.x.StatementExecuteOkBuilder;

public class UpdateResultBuilder<T extends Result> implements ResultBuilder<T> {
   protected StatementExecuteOkBuilder statementExecuteOkBuilder = new StatementExecuteOkBuilder();

   public boolean addProtocolEntity(ProtocolEntity entity) {
      if (entity instanceof Notice) {
         this.statementExecuteOkBuilder.addProtocolEntity(entity);
         return false;
      } else if (entity instanceof StatementExecuteOk) {
         return true;
      } else if (entity instanceof Ok) {
         return true;
      } else {
         throw (WrongArgumentException)ExceptionFactory.createException(WrongArgumentException.class, "Unexpected protocol entity " + entity);
      }
   }

   public T build() {
      return new UpdateResult(this.statementExecuteOkBuilder.build());
   }
}
