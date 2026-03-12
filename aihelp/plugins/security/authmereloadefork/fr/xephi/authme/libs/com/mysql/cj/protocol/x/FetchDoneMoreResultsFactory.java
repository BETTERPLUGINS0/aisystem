package fr.xephi.authme.libs.com.mysql.cj.protocol.x;

import fr.xephi.authme.libs.com.mysql.cj.protocol.ProtocolEntityFactory;

public class FetchDoneMoreResultsFactory implements ProtocolEntityFactory<FetchDoneMoreResults, XMessage> {
   public FetchDoneMoreResults createFromMessage(XMessage message) {
      return new FetchDoneMoreResults();
   }
}
