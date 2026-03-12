package fr.xephi.authme.libs.com.mysql.cj.protocol.x;

import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntityFactory;

public class NoticeFactory implements ProtocolEntityFactory<Notice, XMessage> {
   public Notice createFromMessage(XMessage message) {
      return Notice.getInstance(message);
   }
}
