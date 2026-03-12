package fr.xephi.authme.libs.com.mysql.cj.protocol.x;

import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntityFactory;

public class StatementExecuteOkFactory implements ProtocolEntityFactory<StatementExecuteOk, XMessage> {
   public StatementExecuteOk createFromMessage(XMessage message) {
      return new StatementExecuteOk();
   }
}
