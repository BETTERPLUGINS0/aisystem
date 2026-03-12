package fr.xephi.authme.libs.com.mysql.cj.xdevapi;

import fr.xephi.authme.libs.com.mysql.cj.protocol.x.StatementExecuteOk;
import java.util.List;

public class AddResultImpl extends UpdateResult implements AddResult {
   public AddResultImpl(StatementExecuteOk ok) {
      super(ok);
   }

   public List<String> getGeneratedIds() {
      return this.ok.getGeneratedIds();
   }
}
