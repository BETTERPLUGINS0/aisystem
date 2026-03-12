package fr.xephi.authme.libs.com.mysql.cj.protocol.x;

import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntityFactory;
import fr.xephi.authme.libs.com.mysql.cj.x.protobuf.MysqlxResultset;

public class XProtocolRowFactory implements ProtocolEntityFactory<XProtocolRow, XMessage> {
   public XProtocolRow createFromMessage(XMessage message) {
      return new XProtocolRow((MysqlxResultset.Row)MysqlxResultset.Row.class.cast(message.getMessage()));
   }
}
