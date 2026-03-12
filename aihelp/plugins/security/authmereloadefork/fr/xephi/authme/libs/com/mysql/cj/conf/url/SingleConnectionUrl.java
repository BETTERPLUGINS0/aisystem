package fr.xephi.authme.libs.com.mysql.cj.conf.url;

import fr.xephi.authme.libs.com.mysql.cj.conf.ConnectionUrl;
import fr.xephi.authme.libs.com.mysql.cj.conf.ConnectionUrlParser;
import java.util.Properties;

public class SingleConnectionUrl extends ConnectionUrl {
   public SingleConnectionUrl(ConnectionUrlParser connStrParser, Properties info) {
      super(connStrParser, info);
      this.type = ConnectionUrl.Type.SINGLE_CONNECTION;
   }
}
